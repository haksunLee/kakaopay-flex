package com.kakaopay.flex.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.flex.constants.Header;
import com.kakaopay.flex.domain.repository.FlexItemRepository;
import com.kakaopay.flex.domain.repository.FlexRepository;
import com.kakaopay.flex.web.dto.FlexRegistRequestDto;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class FlexTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected FlexRepository flexRepository;

    @Autowired
    protected FlexItemRepository flexItemRepository;

    @Autowired
    protected MockMvc mvc;

    //뿌리기 등록 정보
    protected long regUserId = 1;
    protected String regRoomId = "test-roomId";
    protected long regAmount = 1000;
    protected int regCount = 5;

    protected void 뿌리기를_생성한다() throws Exception {

        FlexRegistRequestDto requestDto = FlexRegistRequestDto.builder()
                .amount(regAmount)
                .count(regCount)
                .build();

        String url = "http://localhost:" + port + "/api/v1/flex";

        //when
        mvc.perform(post(url)
                .header(Header.USER_ID, regUserId)
                .header(Header.ROOM_ID, regRoomId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }
}
