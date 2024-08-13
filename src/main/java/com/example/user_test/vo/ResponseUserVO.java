package com.example.user_test.vo;

import com.example.user_test.dto.TokenDto;
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

    private TokenDto tokenDto;
    private URI location;
}
