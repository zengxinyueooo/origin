package com.navigation.service;

import com.navigation.dto.LoginDto;
import com.navigation.dto.RegisterDto;
import com.navigation.dto.UserUpdateDto;
import com.navigation.entity.User;
import com.navigation.result.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface UserService {

    /**
     * 用户注册
     * @param registerDto 注册信息数据传输对象
     * @return 操作结果（成功或失败）和附加信息
     */
    Map<String, Object> RegisterUser(RegisterDto registerDto);

    /**
     * 用户登录
     * @param loginDto
     * @return
     */
    Map<String, Object> LoginUser(LoginDto loginDto);

}
