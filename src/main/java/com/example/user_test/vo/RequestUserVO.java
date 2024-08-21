package com.example.user_test.vo;

import com.example.user_test.dto.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUserVO {
    private String id;
    private String password;
    private TokenDto tokenDto;
}
