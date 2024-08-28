package com.example.user_test.service;

import com.example.user_test.dto.UserDto;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto login(UserDto userDto);
}
