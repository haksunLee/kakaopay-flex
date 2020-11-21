package com.kakaopay.flex.web;

import com.kakaopay.flex.constants.Header;
import com.kakaopay.flex.service.FlexRegistService;
import com.kakaopay.flex.web.dto.FlexRegistRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class FlexRegistController {

    private final FlexRegistService flexRegistService;

    @PostMapping("/api/v1/flex")
    public String registFlex (
            @RequestHeader(Header.USER_ID) Long createUserId,
            @RequestHeader(Header.ROOM_ID) String roomId,
            @RequestBody FlexRegistRequestDto requestDto) throws Exception {
        requestDto.setCreateUserId(createUserId);
        requestDto.setRoomId(roomId);
        return flexRegistService.registFlex(requestDto);
    }
}
