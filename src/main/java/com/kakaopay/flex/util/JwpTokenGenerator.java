package com.kakaopay.flex.util;

import com.kakaopay.flex.web.dto.FlexRegistRequestDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Component
public class JwpTokenGenerator {

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;

    private final long tokenValidTime = 60 * 10 * 1000; // 1시간만 토큰 유효

    @PostConstruct
    private void init() {
        this.key = Keys.hmacShaKeyFor(this.secretKey.getBytes());
    }

    public String getToken(FlexRegistRequestDto dto) {
        return generateToken(dto);
    }

    private String generateToken(FlexRegistRequestDto dto) {
        Date now = new Date();
        String token = Jwts.builder()
                .claim("userId", dto.getCreateUserId())
                .claim("roomId", dto.getRoomId())
                .claim("amount", dto.getAmount())
                .claim("count", dto.getCount())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + this.tokenValidTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    public boolean isValidateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
