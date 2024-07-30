package com.example.user_test.service;

import com.example.user_test.entity.UserEntity;
import com.example.user_test.vo.RequestUserVO;
import com.example.user_test.vo.ResponseUserVO;

import java.util.Optional;

public interface UserService {

    ResponseUserVO createUser(RequestUserVO userEntity);
}
