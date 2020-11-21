package com.kakaopay.flex.domain.entity;

import com.kakaopay.flex.constants.ErrorCode;
import com.kakaopay.flex.domain.BaseTimeEntity;
import com.kakaopay.flex.exception.FlexException;
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

    public void checkEqualRoomId(String roomId) {
        if (!roomId.equals(this.roomId)) {
           throw new FlexException(ErrorCode.NOT_INCLUDE_SAME_ROOM);
        }
    }

    public void checkEqualWithCreateUser(Long userId) {
        if (!userId.equals(this.createUserId)) {
            throw new FlexException(ErrorCode.USER_IS_NOT_CREATE_USER);
        }
    }

    public void checkNotEqualWithCreateUser(Long userId) {
        if (userId.equals(this.createUserId)) {
            throw new FlexException(ErrorCode.USER_IS_CREATE_USER);
        }
    }

    public void checkNotReceivedMoneyBefore(Long userId) {
        if (this.flexItems.stream()
                .filter(FlexItem::isReceived)
                .anyMatch(item -> item.getReceiverId().equals(userId))) {
            throw new FlexException(ErrorCode.USER_GET_BEFORE_ALREADY);
        }
    }

    public void checkNotCompleted() {
        if (this.flexItems.stream()
                .allMatch(FlexItem::isReceived)) {
            throw new FlexException(ErrorCode.FLEX_COMPLETE_ALREADY);
        }
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
