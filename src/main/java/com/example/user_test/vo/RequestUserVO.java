package com.example.user_test.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestUserVO {
    private String id;
    private String password;
    private String accessToken;
    private String refreshToken;
}
