package com.kakaopay.flex.service;

import com.kakaopay.flex.domain.entity.Flex;
import com.kakaopay.flex.domain.entity.FlexItem;
import com.kakaopay.flex.domain.repository.FlexItemRepository;
import com.kakaopay.flex.domain.repository.FlexRepository;
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
    JwpTokenGenerator jwpTokenGenerator;

    @Transactional
    public Long takeFlexItem(FlexRequestDto requestDto) throws Exception {

        if (!jwpTokenGenerator.isValidateToken(requestDto.getToken())) {
            throw new Exception("뿌리기 건은 10분간만 유효합니다.");
        }

        Flex flex = flexRepository.findByToken(requestDto.getToken())
                .orElseThrow(() -> new Exception("해당 뿌리기 건을 찾을 수 없습니다."));

        if (!flex.isEqualRoomId(requestDto.getRoomId())) {
            throw new Exception("뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다.");
        }

        if (flex.isCompleted()) {
            throw new Exception("완료된 뿌리기 건 입니다. 더이상 받을 수 있는 돈이 없습니다.");
        }

        if (flex.isEqualCreateUserId(requestDto.getUserId())) {
            throw new Exception("자신이 뿌리기한 건은 자신이 받을 수 없습니다.");
        }

        if (flex.isReceivedMoneyBefore(requestDto.getUserId())) {
            throw new Exception("뿌리기 당 한 사용자는 한번만 받을 수 있습니다.");
        }

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
