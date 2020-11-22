package com.kakaopay.flex.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FlexItemInfoDto {

    private long money;
    private long receiverId;

    @Builder
    public FlexItemInfoDto(long money, long receiverId) {
        this.money = money;
        this.receiverId = receiverId;
    }
}