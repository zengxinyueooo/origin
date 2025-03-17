package com.navigation.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.navigation.dto.LoginDto;
import com.navigation.dto.UserUpdateDto;
import com.navigation.entity.User;
import com.navigation.exception.AccountExist;
import com.navigation.mapper.UserMapper;
import com.navigation.result.Result;
import com.navigation.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.navigation.constant.MessageConstant.USER_NICK_NAME_PREFIX;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    private String password;
    @Override
    public User login(LoginDto loginDto) {
        password= loginDto.getPassword();
        String password = DigestUtils.md5DigestAsHex(loginDto.getPassword().getBytes());
        return userMapper.login(loginDto.getUsername(),password);
    }

    @Override
    public void register(LoginDto loginDto) {
        password= loginDto.getPassword();
        String password = DigestUtils.md5DigestAsHex(loginDto.getPassword().getBytes());
        User userExist = userMapper.exist(loginDto.getUsername());
        if(userExist == null){
        User user = new User();
        user.setNickname(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        user.setUsername(loginDto.getUsername());
        user.setPassword(password);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.register(user);
        }else {
            throw new AccountExist("账号已被占用,请重新填写");
        }
    }

}
