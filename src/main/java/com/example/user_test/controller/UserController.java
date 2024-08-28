package com.example.user_test.controller;

import com.example.user_test.dto.UserDto;
import com.example.user_test.service.UserService;
import com.example.user_test.vo.RequestUserVO;
import com.example.user_test.vo.ResponseUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/jpa")
@Tag(name = "Controller", description = "회원관리를 윈한 컨트롤러입니다.")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "회원가입 API",
            description = "유저아이디, 비밀번호를 입력하여 회원가입합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = RequestUserVO.class),
                            examples =
                                    {
                                            @ExampleObject(
                                                    name = "회원가입 성공 입력값 예제",
                                                    value = "{\n" +
                                                            " \"id\": \"userId\",\n" +
                                                            " \"password\": \"123456\"\n" +
                                                            "}\n"
                                            )
                                    }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "가입요청 성공",
                            content = @Content(
                                    schema = @Schema(implementation = ResponseUserVO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "가입요청 성공 예시",
                                                    value = "{\"message\":\"가입이 완료되었습니다.\",\"success\":true}"
                                            )
                                    }
                            )
                    )
            }
    )
    @PostMapping("/users")
    public ResponseEntity<?> user(@RequestBody RequestUserVO requestUserVO) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(requestUserVO, UserDto.class);
        userService.createUser(userDto);
        ResponseUserVO responseUserVO = mapper.map(userDto, ResponseUserVO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUserVO);
    }

    @Operation(
            summary = "로그인 API",
            description = "유저아이디, 비밀번호를 입력하여 로그인합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = RequestUserVO.class),
                            examples =
                                    {
                                            @ExampleObject(
                                                    name = "로그인 성공 입력값 예제",
                                                    value = "{\n" +
                                                            " \"id\": \"userId\",\n" +
                                                            " \"password\": \"123456\"\n" +
                                                            "}\n"
                                            )
                                    }
                    )
            )
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestUserVO requestUserVO) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(requestUserVO, UserDto.class);
        ResponseUserVO responseUserVO = mapper.map(userService.login(userDto), ResponseUserVO.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseUserVO);
    }

    // test : JWT 검증이 필요 없는 엔드포인트
    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint, accessible without authentication.";
    }

    // test : JWT 검증이 필요한 엔드포인트
    @GetMapping("/secure")
    public String secureEndpoint() {
        return "This is a secure endpoint, accessible only with a valid JWT token.";
    }

}
