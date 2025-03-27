package com.navigation.controller.user;

import com.navigation.dto.LoginDto;
import com.navigation.dto.RegisterDto;
import com.navigation.service.UserService;
import com.navigation.vo.UserLoginVo;
import com.navigation.vo.UserRegisterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.navigation.result.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/users")

@Api(tags = "用户操作")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping ("/login")
    @ApiOperation("用户登录")
    public Map<String, Object> userLogin(@RequestBody LoginDto loginDto) {
        return userService.LoginUser(loginDto);
    }


    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Map<String, Object> RegisterUser(@RequestBody RegisterDto registerDto) {

        Map<String, Object> result = userService.RegisterUser(registerDto);


        if (result == null || !"success".equals(result.get("status"))) {
            return Map.of("status", "failure", "message", result != null ? result.get("message") : "注册失败");
        } else {
            return result;
        }
    }

    /**
     * 激活账号
     * @param confirmCode
     * @return
     */
    @GetMapping("/activation")
    @ApiOperation("账号激活")
    public Map<String, Object> activationAccount(String confirmCode){

        return userService.activationAccount(confirmCode);
    }




}
