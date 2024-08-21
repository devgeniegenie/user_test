package com.example.user_test.service;

import com.example.user_test.dto.TokenDto;
import com.example.user_test.dto.UserDto;
import com.example.user_test.entity.UserEntity;
import com.example.user_test.exception.InvalidRequestException;
import com.example.user_test.repository.UserRepository;
import com.example.user_test.util.jwt.JwtTokenUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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
    public UserDto createUser(UserDto userDto) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        if (userRepository.findById(userDto.getId()).isPresent()) {
            throw new InvalidRequestException("중복된 ID입니다");
        }

        //비밀번호 암호화
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userRepository.save(userEntity);

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);

        return returnUserDto;
    }

    @Override
    public UserDto login(UserDto userDto) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // 사용자 확인
        UserEntity userEntity = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new InvalidRequestException("잘못된 ID입니다"));

        // 비밀번호 확인
        if (!bCryptPasswordEncoder.matches(userDto.getPassword(), userEntity.getPassword())) {
            throw new InvalidRequestException("잘못된 비밀번호입니다");
        }

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);

        //access토큰, refresh토큰 생성
        String accessToken = jwtTokenUtil.createJwtToken(returnUserDto.getId(), env.getProperty("access_token.expiration_time"), env.getProperty("access_token.secret"));
        String refreshToken = jwtTokenUtil.createJwtToken(returnUserDto.getId(), env.getProperty("refresh_token.expiration_time"), env.getProperty("refresh_token.secret"));

        TokenDto tokenDto = TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        returnUserDto.setTokenDto(tokenDto);

        // 응답 객체 생성 및 반환
        return returnUserDto;
    }
}
