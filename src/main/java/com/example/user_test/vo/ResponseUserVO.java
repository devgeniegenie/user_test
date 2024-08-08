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

    private String accessToken;
    private String refreshToken;
    private URI location;
}
