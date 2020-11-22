package com.kakaopay.flex.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FlexRequestDto {
    private String token;
    private long userId;
    private String roomId;

    @Builder
    public FlexRequestDto(String token, long userId, String roomId) {
        this.token = token;
        this.userId = userId;
        this.roomId = roomId;
    }
}