package com.example.user_test.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@AllArgsConstructor
public class JwtTokenUtil {
    private final Environment env;
    public String createJwtToken(String id, String expiration, String secret) {
        //TODO : 토큰생성시 차후 필요할 값들 더 세팅하기
        return Jwts.builder().setSubject(id)
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(expiration)))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(env.getProperty("access_token.secret"))
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        //Subject를 먼저 토큰에서 추출한 다음에 이 값이 정상적인 계정 값인지 판단
        String subject = null;

        try {
            subject = Jwts.parser().setSigningKey(env.getProperty("access_token.secret"))
                    .parseClaimsJws(jwt).getBody()
                    .getSubject();
        } catch (Exception exception) {
            returnValue = false;
        }

        if (subject == null || subject.isEmpty()) {
            returnValue = false;
        }

        return returnValue;
    }
}
