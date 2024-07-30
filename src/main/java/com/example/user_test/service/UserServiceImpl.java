package com.example.user_test.service;

import com.example.user_test.entity.UserEntity;
import com.example.user_test.exception.ConflictResourceException;
import com.example.user_test.repository.UserRepository;
import com.example.user_test.util.jwt.JwtTokenUtil;
import com.example.user_test.vo.RequestUserVO;
import com.example.user_test.vo.ResponseUserVO;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment env;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Optional<UserEntity> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public ResponseUserVO createUser(RequestUserVO requestUserVO) {

        UserEntity userEntity = UserEntity.builder()
                .id(requestUserVO.getId())
                .password(bCryptPasswordEncoder.encode(requestUserVO.getPassword()))
                .build();

        try {
            userRepository.save(userEntity);
        } catch (ConflictResourceException e) {
            throw new ConflictResourceException(requestUserVO.getId() + "는 중복된 ID입니다");
        }

        return ResponseUserVO.builder().id(userEntity.getId()).build();
    }
}
