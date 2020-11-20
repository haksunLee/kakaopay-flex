package com.kakaopay.flex.domain.entity;

import com.kakaopay.flex.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Flex extends BaseTimeEntity {

    @Id
    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private String roomId;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private Integer count;

    @Column(nullable = false)
    private Long createUserId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "token")
    private List<FlexItem> flexItems;

    public boolean isEqualRoomId(String roomId) {
        return roomId.equals(this.roomId);
    }

    public boolean isEqualCreateUserId(Long userId) {
        return userId.equals(this.createUserId);
    }

    public boolean isReceivedMoneyBefore(Long userId) {
        return this.flexItems.stream()
                .filter(FlexItem::isReceived)
                .filter(item -> item.getReceiverId().equals(userId))
                .count() > 0;
    }

    public boolean isCompleted() {
        return this.flexItems.stream()
                .filter(FlexItem::isNotReceived)
                .count() == 0;
    }

    @Builder
    public Flex(String token, String roomId, Long amount, int count, Long createUserId, List<FlexItem> flexItems) {
        this.token = token;
        this.roomId = roomId;
        this.amount = amount;
        this.count = count;
        this.createUserId = createUserId;
        this.flexItems = flexItems;
    }
}
