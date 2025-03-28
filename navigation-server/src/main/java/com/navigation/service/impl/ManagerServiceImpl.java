package com.navigation.service.impl;

import com.navigation.entity.Manager;
import com.navigation.mapper.ManagerMapper;
import com.navigation.service.ManagerService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ManagerServiceImpl implements ManagerService {
    @Resource
    private ManagerMapper managerMapper;

    // 验证管理员登录
    public Manager managerlogin(String userName, String password) {
        return managerMapper.findByUserNameAndPassword(userName, password);
    }
}