package com.kakaopay.flex.web;

import com.kakaopay.flex.constants.Header;
import com.kakaopay.flex.constants.ResponseCode;
import com.kakaopay.flex.service.FlexSearchService;
import com.kakaopay.flex.web.dto.FlexInfoResponseDto;
import com.kakaopay.flex.web.dto.FlexRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class FlexSearchController {

    private final FlexSearchService flexSearchService;

    @GetMapping("/api/v1/flex")
    public ResponseEntity<FlexResponse> getFlexInfo(
            @RequestHeader(Header.USER_ID) long userId,
            @RequestHeader(Header.ROOM_ID) String roomId,
            @RequestHeader(Header.TOKEN) String token) {
        return ResponseEntity.ok(new FlexResponse(
                flexSearchService.getFlexInfo(FlexRequestDto.builder()
                    .token(token)
                    .userId(userId)
                    .roomId(roomId)
                    .build())));
    }
}
