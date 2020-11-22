package com.kakaopay.flex.web;

import com.kakaopay.flex.constants.ErrorCode;
import com.kakaopay.flex.constants.Header;
import com.kakaopay.flex.constants.TestParams;
import com.kakaopay.flex.domain.entity.FlexItem;
import com.kakaopay.flex.domain.repository.FlexItemRepository;
import com.kakaopay.flex.domain.repository.FlexRepository;
import com.kakaopay.flex.service.FlexRegistService;
import com.kakaopay.flex.util.JwpTokenGenerator;
import com.kakaopay.flex.web.dto.FlexRegistRequestDto;
import com.kakaopay.flex.web.dto.FlexRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlexItemUpdateControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FlexRepository flexRepository;

    @Autowired
    private FlexItemRepository flexItemRepository;

    @Autowired
    private JwpTokenGenerator tokenGenerator;

    @Autowired
    private FlexRegistService flexRegistService;

    @Autowired
    private MockMvc mvc;

    @AfterEach
    public void tearDown() {
        flexItemRepository.deleteAllData();
        flexRepository.deleteAllData();
        assertThat(flexItemRepository.count()).isEqualTo(0);
        assertThat(flexRepository.count()).isEqualTo(0);
    }

    private String registFlex() {
        FlexRegistRequestDto registRequestDto = FlexRegistRequestDto.builder()
                .roomId(TestParams.regRoomId)
                .amount(TestParams.regAmount)
                .count(TestParams.regCount)
                .createUserId(TestParams.regUserId)
                .build();

        return flexRegistService.registFlex(registRequestDto);
    }

    @Test
    @DisplayName("정상적으로 받기 처리를 합니다.")
    public void updateFlexItem_normalCase() throws Exception {
        //given
        String token = registFlex();
        FlexRequestDto requestDto = FlexRequestDto.builder()
                .token(token)
                .userId(TestParams.receiverUserId)
                .roomId(TestParams.receiverRoomId)
                .build();

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
        assertThat(all.stream().anyMatch(FlexItem::isReceived));
        assertThat(all.stream().anyMatch(item -> item.getReceiverId().equals(TestParams.receiverUserId)));
    }


    @Test
    @DisplayName("받기 처리 시, 잘못된 Token 정보를 던집니다.")
    public void updateFlexItem_abnormalCase01() throws Exception {
        String token = registFlex();

        FlexRequestDto requestDto = FlexRequestDto.builder()
                .token("another.token.value")
                .userId(TestParams.receiverUserId)
                .roomId(TestParams.receiverRoomId)
                .build();

        String url = "http://localhost:" + port + "/api/v1/flex/item";

        //when
        mvc.perform(put(url)
                .header(Header.USER_ID, requestDto.getUserId())
                .header(Header.ROOM_ID, requestDto.getRoomId())
                .header(Header.TOKEN, requestDto.getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCode.FLEX_EXPIRATION.getCode())));
    }


    @Test
    @DisplayName("받기 처리 시, 잘못된 Room 정보를 던집니다.")
    public void updateFlexItem_abnormalCase02() throws Exception {
        String token = registFlex();

        FlexRequestDto requestDto = FlexRequestDto.builder()
                .token(token)
                .userId(TestParams.receiverUserId)
                .roomId("another-room")
                .build();

        String url = "http://localhost:" + port + "/api/v1/flex/item";

        //when
        mvc.perform(put(url)
                .header(Header.USER_ID, requestDto.getUserId())
                .header(Header.ROOM_ID, requestDto.getRoomId())
                .header(Header.TOKEN, requestDto.getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code", is(ErrorCode.NOT_INCLUDE_SAME_ROOM.getCode())));
    }


    @Test
    @DisplayName("받기 처리 시, 뿌리기 등록자와 동일한 User 정보를 던집니다.")
    public void updateFlexItem_abnormalCase03() throws Exception {
        String token = registFlex();

        FlexRequestDto requestDto = FlexRequestDto.builder()
                .token(token)
                .userId(TestParams.regUserId)
                .roomId(TestParams.receiverRoomId)
                .build();

        String url = "http://localhost:" + port + "/api/v1/flex/item";

        //when
        mvc.perform(put(url)
                .header(Header.USER_ID, requestDto.getUserId())
                .header(Header.ROOM_ID, requestDto.getRoomId())
                .header(Header.TOKEN, requestDto.getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code", is(ErrorCode.USER_IS_CREATE_USER.getCode())));
    }

    @Test
    @DisplayName("받기 처리 시, 이전에 이미 받은 사용자 정보를 던집니다.")
    public void updateFlexItem_abnormalCase04() throws Exception {
        String token = registFlex();

        FlexItem flexItem = flexItemRepository.findAll().get(0);
        flexItem.updateReceiverId(TestParams.receiverUserId);
        flexItemRepository.save(flexItem);

        FlexRequestDto requestDto = FlexRequestDto.builder()
                .token(token)
                .userId(TestParams.receiverUserId)
                .roomId(TestParams.receiverRoomId)
                .build();

        String url = "http://localhost:" + port + "/api/v1/flex/item";

        //when
        mvc.perform(put(url)
                .header(Header.USER_ID, requestDto.getUserId())
                .header(Header.ROOM_ID, requestDto.getRoomId())
                .header(Header.TOKEN, requestDto.getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code", is(ErrorCode.USER_GET_BEFORE_ALREADY.getCode())));
    }
}
