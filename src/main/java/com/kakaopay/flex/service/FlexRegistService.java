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
    JwpTokenGenerator tokenGenerator;

    @Autowired
    RandomMoneyDevider randomMoneyDevider;

    @Transactional
    public String registFlex(FlexRegistRequestDto requestDto) {
        String token = tokenGenerator.getToken(requestDto);
        requestDto.setToken(token);
        flexRepository.save(requestDto.toEntity());

        List<Long> dividedMoneys = randomMoneyDevider.getDevidedMoneys(requestDto);

        dividedMoneys.stream()
                .forEach(dividedMoney -> flexItemRepository.save(
                        FlexItem.builder()
                        .token(token)
                        .money(dividedMoney)
                        .build())
                );
        return token;
    }
}
