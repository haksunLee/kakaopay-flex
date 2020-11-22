package com.kakaopay.flex.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.flex.constants.ErrorCode;
import com.kakaopay.flex.constants.Header;
import com.kakaopay.flex.constants.ResponseCode;
import com.kakaopay.flex.constants.TestParams;
import com.kakaopay.flex.domain.entity.Flex;
import com.kakaopay.flex.domain.entity.FlexItem;
import com.kakaopay.flex.domain.repository.FlexItemRepository;
import com.kakaopay.flex.domain.repository.FlexRepository;
import com.kakaopay.flex.service.FlexRegistService;
import com.kakaopay.flex.util.JwpTokenGenerator;
import com.kakaopay.flex.web.dto.FlexRegistRequestDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlexRegistControllerTest {

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

    @Test
    @DisplayName("정상적으로 뿌리기를 등록합니다.")
    public void registFlex_normalCase() throws Exception {
        //given
        FlexRegistRequestDto requestDto = FlexRegistRequestDto.builder()
                .amount(TestParams.regAmount)
                .count(TestParams.regCount)
                .build();

        String url = "http://localhost:" + port + "/api/v1/flex";

        //when
        mvc.perform(post(url)
                .header(Header.USER_ID, TestParams.regUserId)
                .header(Header.ROOM_ID, TestParams.regRoomId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(jsonPath("$.result", is(ResponseCode.SUCCESS)))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        List<Flex> result1 = flexRepository.findAll();
        assertThat(result1.get(0).getRoomId()).isEqualTo(TestParams.regRoomId);
        assertThat(result1.get(0).getAmount()).isEqualTo(TestParams.regAmount);
        assertThat(result1.get(0).getCount()).isEqualTo(TestParams.regCount);
        assertThat(result1.get(0).getCreateUserId()).isEqualTo(TestParams.regUserId);

        List<FlexItem> result2 = flexItemRepository.findAll();
        assertThat(result2.size()).isEqualTo(TestParams.regCount);
        assertThat(result2.stream().mapToLong(FlexItem::getMoney).sum()).isEqualTo(TestParams.regAmount);
    }

    @Test
    @DisplayName("뿌리기 등록 시, 필수 Header 값인 Room 정보를 누락시켜 호출합니다.")
    public void registFlex_abnormalCase01() throws Exception {
        //given
        FlexRegistRequestDto requestDto = FlexRegistRequestDto.builder()
                .amount(TestParams.regAmount)
                .count(TestParams.regCount)
                .build();

        String url = "http://localhost:" + port + "/api/v1/flex";

        //when
        mvc.perform(post(url)
                .header(Header.USER_ID, TestParams.regUserId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(jsonPath("$.result", is(ResponseCode.FAIL)))
                .andDo(print());
    }
}
