package com.kakaopay.flex.util;

import com.kakaopay.flex.web.dto.FlexRegistRequestDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class RandomMoneyDevider {

    public static List<Long> getDevidedMoneys(FlexRegistRequestDto dto) {
        Random random = new Random();
        List<Long> devidedMoneys = new ArrayList<Long>();

        long min = 1;
        long max = dto.getAmount(); //최소 1원씩은 배분 하기 위해 사람수 만큼 뺀다.

        for (int i = 0; i < dto.getCount() - 1; i++) {
            long devidedMoney = min + (long) (random.nextDouble() * ((max - dto.getCount() + i + 2) - min));
            devidedMoneys.add(devidedMoney);
            max -= devidedMoney;
        }
        devidedMoneys.add(max);

        return devidedMoneys;
    }
}
