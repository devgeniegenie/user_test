package com.example.user_test.service;

import com.example.user_test.entity.UserEntity;
import com.example.user_test.exception.InvalidRequestException;
import com.example.user_test.repository.UserRepository;
import com.example.user_test.util.jwt.JwtTokenUtil;
import com.example.user_test.vo.RequestUserVO;
import com.example.user_test.vo.ResponseUserVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment env;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public ResponseUserVO createUser(RequestUserVO requestUserVO) {
        //TODO : ModelMapper 사용

        if (userRepository.findById(requestUserVO.getId()).isPresent()) {
            throw new InvalidRequestException("중복된 ID입니다");
        }

        UserEntity userEntity = UserEntity.builder()
                .id(requestUserVO.getId())
                .password(bCryptPasswordEncoder.encode(requestUserVO.getPassword()))
                .build();
        userRepository.save(userEntity);

        return ResponseUserVO.builder().id(userEntity.getId()).build();
    }

    @Override
    public ResponseUserVO login(RequestUserVO requestUserVO) {
        //TODO : ModelMapper 사용

        // 사용자 확인
        UserEntity userEntity = userRepository.findById(requestUserVO.getId())
                .orElseThrow(() -> new InvalidRequestException("잘못된 ID입니다"));

        // 비밀번호 확인
        if (!bCryptPasswordEncoder.matches(requestUserVO.getPassword(), userEntity.getPassword())) {
            throw new InvalidRequestException("잘못된 비밀번호입니다");
        }

        //TODO : 토큰들을 담을 하나의 dto 생성 예정
        //access토큰, refresh토큰 생성
        String accessToken = jwtTokenUtil.createJwtToken(userEntity.getId(), env.getProperty("access_token.expiration_time"), env.getProperty("access_token.secret"));
        String refreshToken = jwtTokenUtil.createJwtToken(userEntity.getId(), env.getProperty("refresh_token.expiration_time"), env.getProperty("refresh_token.secret"));

        log.info("로그인 성공: {}", userEntity.getId());

        // 응답 객체 생성 및 반환
        return ResponseUserVO.builder()
                .id(userEntity.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
