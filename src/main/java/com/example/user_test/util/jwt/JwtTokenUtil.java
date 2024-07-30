package com.example.user_test.util.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@AllArgsConstructor
public class JwtTokenUtil {
    public String createJwtToken(String id, String expiration, String secret) {
        return Jwts.builder().setSubject(id)
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(expiration)))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();
    }
}
