package com.kakaopay.flex.web;

import com.kakaopay.flex.constants.Header;
import com.kakaopay.flex.service.FlexItemUpdateService;
import com.kakaopay.flex.web.dto.FlexRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class FlexItemUpdateController {

    private final FlexItemUpdateService flexItemUpdateService;

    @PutMapping("/api/v1/flex/item")
    public Long takeFlexItem(
            @RequestHeader(Header.USER_ID) Long userId,
            @RequestHeader(Header.ROOM_ID) String roomId,
            @RequestHeader(Header.TOKEN) String token) throws Exception {
        return flexItemUpdateService.takeFlexItem(FlexRequestDto.builder()
                .token(token)
                .userId(userId)
                .roomId(roomId)
                .build());
    }
}
