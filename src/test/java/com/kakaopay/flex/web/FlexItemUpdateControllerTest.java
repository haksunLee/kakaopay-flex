package com.kakaopay.flex.web;

import com.kakaopay.flex.constants.Header;
import com.kakaopay.flex.domain.entity.Flex;
import com.kakaopay.flex.domain.entity.FlexItem;
import com.kakaopay.flex.web.dto.FlexRequestDto;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FlexItemUpdateControllerTest extends FlexTest {

    @Test
    public void FlexItem_검증한다() throws Exception {
        super.뿌리기를_생성한다();

        Flex flex = flexRepository.findAllDesc().get(0);
        assertThat(flex.getToken()).isNotEmpty();

        //받는 사람 정보
        long userId = 2;
        String roomId = "test-roomId";

        FlexRequestDto requestDto = FlexRequestDto.builder()
                .token(flex.getToken())
                .userId(userId)
                .roomId(roomId)
                .build();

        this.FlexItem_검증한다(requestDto);
    }

    private void FlexItem_검증한다(FlexRequestDto requestDto) throws Exception {
        String url = "http://localhost:" + port + "/api/v1/flex/item";

        //when
        mvc.perform(put(url)
                .header(Header.USER_ID, requestDto.getUserId())
                .header(Header.ROOM_ID, requestDto.getRoomId())
                .header(Header.TOKEN, requestDto.getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        //then
        List<FlexItem> all = flexItemRepository.findAll();
        assertThat(all.stream().filter(item ->
                (item.isReceived() && item.getReceiverId().equals(requestDto.getUserId()))
        ).findAny().get()).isInstanceOf(FlexItem.class);
    }
}
