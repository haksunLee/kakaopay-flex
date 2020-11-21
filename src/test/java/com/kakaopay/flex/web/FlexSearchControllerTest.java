package com.kakaopay.flex.web;

import com.kakaopay.flex.constants.ErrorCode;
import com.kakaopay.flex.constants.Header;
import com.kakaopay.flex.constants.TestParams;
import com.kakaopay.flex.domain.repository.FlexItemRepository;
import com.kakaopay.flex.domain.repository.FlexRepository;
import com.kakaopay.flex.service.FlexItemUpdateService;
import com.kakaopay.flex.service.FlexRegistService;
import com.kakaopay.flex.util.JwpTokenGenerator;
import com.kakaopay.flex.web.dto.FlexRegistRequestDto;
import com.kakaopay.flex.web.dto.FlexRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlexSearchControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(FlexSearchControllerTest.class);

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
    private FlexItemUpdateService flexItemUpdateService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @AfterEach
    public void tearDown() throws Exception {
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

    private void updateFlexItem(String token) {
        FlexRequestDto requestDto = FlexRequestDto.builder()
                .token(token)
                .userId(TestParams.receiverUserId)
                .roomId(TestParams.regRoomId)
                .build();

        flexItemUpdateService.takeFlexItem(requestDto);
    }

    @Test
    @DisplayName("정상적으로 뿌리기 조회 처리를 합니다.")
    public void searchFlex_normalCase() throws Exception {
        //given
        String token = registFlex();
        updateFlexItem(token);
        FlexRequestDto requestDto = FlexRequestDto.builder()
                .token(token)
                .userId(TestParams.regUserId)
                .roomId(TestParams.regRoomId)
                .build();

        String url = "http://localhost:" + port + "/api/v1/flex";

        //when
        mvc.perform(get(url)
                .header(Header.USER_ID, requestDto.getUserId())
                .header(Header.ROOM_ID, requestDto.getRoomId())
                .header(Header.TOKEN, requestDto.getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andDo(print());
    }


    @Test
    @DisplayName("조회 처리 시, 잘못된 Token 정보를 던집니다.")
    public void updateFlexItem_abnormalCase01() throws Exception {
        //given
        String token = registFlex();
        updateFlexItem(token);
        FlexRequestDto requestDto = FlexRequestDto.builder()
                .token("another.token.value")
                .userId(TestParams.receiverUserId)
                .roomId(TestParams.receiverRoomId)
                .build();

        String url = "http://localhost:" + port + "/api/v1/flex";

        //when
        mvc.perform(get(url)
                .header(Header.USER_ID, requestDto.getUserId())
                .header(Header.ROOM_ID, requestDto.getRoomId())
                .header(Header.TOKEN, requestDto.getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(ErrorCode.FLEX_NOT_FOUND.getCode())));
    }

    @Test
    @DisplayName("조회 처리 시, 등록자가 아닌 User값을 던집니다.")
    public void updateFlexItem_abnormalCase02() throws Exception {
        //given
        String token = registFlex();
        updateFlexItem(token);
        FlexRequestDto requestDto = FlexRequestDto.builder()
                .token(token)
                .userId(99L)
                .roomId(TestParams.receiverRoomId)
                .build();

        String url = "http://localhost:" + port + "/api/v1/flex";

        //when
        mvc.perform(get(url)
                .header(Header.USER_ID, requestDto.getUserId())
                .header(Header.ROOM_ID, requestDto.getRoomId())
                .header(Header.TOKEN, requestDto.getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code", is(ErrorCode.USER_IS_NOT_CREATE_USER.getCode())));
    }
}
