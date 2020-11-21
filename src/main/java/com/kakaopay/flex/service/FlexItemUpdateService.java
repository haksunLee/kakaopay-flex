package com.kakaopay.flex.service;

import com.kakaopay.flex.constants.ErrorCode;
import com.kakaopay.flex.domain.entity.Flex;
import com.kakaopay.flex.domain.entity.FlexItem;
import com.kakaopay.flex.domain.repository.FlexItemRepository;
import com.kakaopay.flex.domain.repository.FlexRepository;
import com.kakaopay.flex.exception.FlexException;
import com.kakaopay.flex.util.JwpTokenGenerator;
import com.kakaopay.flex.web.dto.FlexRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FlexItemUpdateService {
    private final FlexRepository flexRepository;
    private final FlexItemRepository flexItemRepository;

    @Autowired
    JwpTokenGenerator tokenGenerator;

    @Transactional
    public Long takeFlexItem(FlexRequestDto requestDto) {

        tokenGenerator.checkValidateToken(requestDto.getToken());

        Flex flex = flexRepository.findByToken(requestDto.getToken())
                .orElseThrow(() -> new FlexException(ErrorCode.FLEX_NOT_FOUND));

        flex.checkEqualRoomId(requestDto.getRoomId());
        flex.checkNotCompleted();
        flex.checkNotEqualWithCreateUser(requestDto.getUserId());
        flex.checkNotReceivedMoneyBefore(requestDto.getUserId());

        FlexItem flexItem = flex.getFlexItems()
                .stream()
                .filter(FlexItem::isNotReceived)
                .findAny()
                .get();

        flexItem.updateReceiverId(requestDto.getUserId());
        flexItemRepository.save(flexItem);

        return flexItem.getMoney();
    }
}
