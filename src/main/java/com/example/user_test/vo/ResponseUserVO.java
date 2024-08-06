package com.example.user_test.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseUserVO {
    private String id;

    //TODO : 토큰들을 관리할 하나의 dto 생성 예정
    private String accessToken;
    private String refreshToken;
    private URI location;
}
