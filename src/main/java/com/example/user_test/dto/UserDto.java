package com.example.user_test.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String id;
    private String password;
    private String accessToken;
    private String refreshToken;
}
