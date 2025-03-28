package com.navigation.controller.manager;

import com.navigation.entity.Manager;
import com.navigation.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    // 管理员登录
    @PostMapping("/login")
    public String managerlogin(@RequestParam String userName, @RequestParam String password) {
        Manager manager = managerService.managerlogin(userName, password);
        if (manager != null) {
            return "登录成功";
        } else {
            return "用户名或密码错误";
        }
    }
}