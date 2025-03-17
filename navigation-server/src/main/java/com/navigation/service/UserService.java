package com.navigation.service;

import com.navigation.dto.LoginDto;
import com.navigation.dto.UserUpdateDto;
import com.navigation.entity.User;
import com.navigation.result.Result;

import java.util.ArrayList;
import java.util.List;

public interface UserService {
    User login(LoginDto loginDto);
    void register(LoginDto loginDto);

}
