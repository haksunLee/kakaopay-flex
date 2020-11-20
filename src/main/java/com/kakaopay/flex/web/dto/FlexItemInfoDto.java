package com.kakaopay.flex.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FlexItemInfoDto {

    private Long money;
    private Long receiverId;

    @Builder
    public FlexItemInfoDto(Long money, Long receiverId) {
        this.money = money;
        this.receiverId = receiverId;
    }
}