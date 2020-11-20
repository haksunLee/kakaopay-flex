package com.kakaopay.flex.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class FlexInfoResponseDto {
    private LocalDateTime createdDate;
    private Long amount;
    private Long totalReceivedMoney;
    private List<FlexItemInfoDto> receivedItemInfo;

    @Builder
    public FlexInfoResponseDto(LocalDateTime createdDate, long amount, long totalReceivedMoney, List<FlexItemInfoDto> receivedItemInfo) {
        this.createdDate = createdDate;
        this.amount = amount;
        this.totalReceivedMoney = totalReceivedMoney;
        this.receivedItemInfo = receivedItemInfo;
    }
}