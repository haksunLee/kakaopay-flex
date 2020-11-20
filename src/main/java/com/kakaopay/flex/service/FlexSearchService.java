package com.kakaopay.flex.service;

import com.kakaopay.flex.domain.entity.Flex;
import com.kakaopay.flex.domain.entity.FlexItem;
import com.kakaopay.flex.domain.repository.FlexRepository;
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
    public FlexInfoResponseDto getFlexInfo(FlexRequestDto requestDto) throws Exception {
        LocalDateTime createdDate = LocalDateTime.now().minusDays(7);
        Flex flex = flexRepository.findByTokenAndCreatedDateGreaterThan(requestDto.getToken(), createdDate)
                 .orElseThrow(() -> new Exception("해당하는 뿌리기 메시지를 찾을 수 없습니다."));

        if (!flex.isEqualCreateUserId(requestDto.getUserId())) {
            throw new Exception("뿌린 사람 자신만 조회를 할 수 있습니다.");
        }

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
