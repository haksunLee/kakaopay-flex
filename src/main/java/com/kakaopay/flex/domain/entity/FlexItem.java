package com.kakaopay.flex.domain.entity;

import com.kakaopay.flex.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(indexes = {
    @Index(name = "flex_item_idx01", columnList = "token", unique = false)
})
public class FlexItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Long money;

    @Column
    private Long receiverId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "token", insertable = false, updatable = false)
    private Flex flex;

    @Builder
    public FlexItem(String token, long money) {
        this.token = token;
        this.money = money;
    }

    public boolean isReceived() {
        return Objects.nonNull(this.receiverId);
    }

    public boolean isNotReceived() {
        return Objects.isNull(this.receiverId);
    }

    public void updateReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

}
