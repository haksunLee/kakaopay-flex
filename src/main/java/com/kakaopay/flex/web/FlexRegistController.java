package com.kakaopay.flex.web;

import com.kakaopay.flex.constants.Header;
import com.kakaopay.flex.constants.ResponseCode;
import com.kakaopay.flex.service.FlexRegistService;
import com.kakaopay.flex.web.dto.FlexRegistRequestDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class FlexRegistController {

    private final FlexRegistService flexRegistService;

    @PostMapping("/api/v1/flex")
    public ResponseEntity<FlexResponse> registFlex (
            @NonNull @RequestHeader(Header.USER_ID) Long createUserId,
            @NonNull @RequestHeader(Header.ROOM_ID) String roomId,
            @RequestBody FlexRegistRequestDto requestDto) throws Exception {
        requestDto.setCreateUserId(createUserId);
        requestDto.setRoomId(roomId);
        return ResponseEntity.ok(new FlexResponse(flexRegistService.registFlex(requestDto)));
    }
}
