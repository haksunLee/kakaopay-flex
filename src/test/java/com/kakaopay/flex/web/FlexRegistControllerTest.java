package com.kakaopay.flex.web;

import com.kakaopay.flex.domain.entity.Flex;
import com.kakaopay.flex.domain.entity.FlexItem;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FlexRegistControllerTest extends FlexTest {

    @Test
    public void 뿌리기를_검증한다() throws Exception {
        super.뿌리기를_생성한다();

        //then
        List<Flex> result1 = flexRepository.findAll();
        assertThat(result1.get(0).getRoomId()).isEqualTo(regRoomId);
        assertThat(result1.get(0).getAmount()).isEqualTo(regAmount);
        assertThat(result1.get(0).getCount()).isEqualTo(regCount);
        assertThat(result1.get(0).getCreateUserId()).isEqualTo(regUserId);

        List<FlexItem> result2 = flexItemRepository.findAll();
        assertThat(result2.size()).isEqualTo(regCount);
        assertThat(result2.stream().mapToLong(FlexItem::getMoney).sum()).isEqualTo(regAmount);
    }
}
