package com.kakaopay.flex.service;

import com.kakaopay.flex.constants.ErrorCode;
import com.kakaopay.flex.domain.entity.Flex;
import com.kakaopay.flex.domain.entity.FlexItem;
import com.kakaopay.flex.domain.repository.FlexRepository;
import com.kakaopay.flex.exception.FlexException;
import com.kakaopay.flex.web.dto.FlexInfoResponseDto;
import com.kakaopay.flex.web.dto.FlexItemInfoDto;
import com.kakaopay.flex.web.dto.FlexRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FlexSearchService {
    private final FlexRepository flexRepository;

    @Transactional(readOnly = true)
    public FlexInfoResponseDto getFlexInfo(FlexRequestDto requestDto) {
        LocalDateTime createdDate = LocalDateTime.now().minusDays(7);
        Flex flex = flexRepository.findByTokenAndCreatedDateGreaterThan(requestDto.getToken(), createdDate)
                .orElseThrow(() -> new FlexException(ErrorCode.FLEX_NOT_FOUND));

        flex.checkEqualWithCreateUser(requestDto.getUserId());

        return FlexInfoResponseDto.builder()
                .createdDate(flex.getCreatedDate())
                .amount(flex.getAmount())
                .totalReceivedMoney(flex.getFlexItems().stream()
                        .filter(FlexItem::isReceived)
                        .mapToLong(FlexItem::getMoney)
                        .sum())
                .receivedItemInfo(flex.getFlexItems().stream()
                        .filter(FlexItem::isReceived)
                        .map(item -> FlexItemInfoDto.builder()
                                .money(item.getMoney())
                                .receiverId(item.getReceiverId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
