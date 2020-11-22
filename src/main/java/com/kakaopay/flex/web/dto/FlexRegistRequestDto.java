package com.kakaopay.flex.web.dto;

import com.kakaopay.flex.domain.entity.Flex;
import lombok.*;

@Getter
@NoArgsConstructor
public class FlexRegistRequestDto {
    @Setter
    private String token;

    @Setter
    private String roomId;

    private long amount;

    private int count;

    @Setter
    private long createUserId;

    @Builder
    public FlexRegistRequestDto(String token, String roomId, long amount, int count, long createUserId) {
        this.token = token;
        this.roomId = roomId;
        this.amount = amount;
        this.count = count;
        this.createUserId = createUserId;
    }

    public Flex toEntity() {
        return Flex.builder()
                .token(token)
                .roomId(roomId)
                .amount(amount)
                .count(count)
                .createUserId(createUserId)
                .build();
    }

}
