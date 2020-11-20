package com.kakaopay.flex.service;

import com.kakaopay.flex.domain.entity.FlexItem;
import com.kakaopay.flex.domain.repository.FlexItemRepository;
import com.kakaopay.flex.domain.repository.FlexRepository;
import com.kakaopay.flex.util.JwpTokenGenerator;
import com.kakaopay.flex.util.RandomMoneyDevider;
import com.kakaopay.flex.web.dto.FlexRegistRequestDto;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class FlexRegistService {
    private final FlexRepository flexRepository;
    private final FlexItemRepository flexItemRepository;

    @Autowired
    JwpTokenGenerator jwpTokenGenerator;

    @Autowired
    RandomMoneyDevider randomMoneyDevider;

    @Transactional
    public String registFlex(FlexRegistRequestDto requestDto) throws Exception {
        String token = jwpTokenGenerator.getToken(requestDto);
        requestDto.setToken(token);
        flexRepository.save(requestDto.toEntity());

        List<Long> dividedMoneys = randomMoneyDevider.getDevidedMoneys(requestDto);
        for(Long dividedMoney : dividedMoneys) {
            FlexItem flexItem = new FlexItem(token, dividedMoney);
            flexItemRepository.save(flexItem);
        }
        return token;
    }
}
