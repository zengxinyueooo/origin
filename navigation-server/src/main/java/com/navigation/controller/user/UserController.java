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
import org.springframework.web.bind.annotation.*;
import com.navigation.result.Result;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/users")

@Api(tags = "用户操作")
public class UserController {
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/1")
//    @ApiOperation("用户注册")
////    public Result register(LoginDto loginDto){
////        userService.register(loginDto);
////        return Result.success();
////    }
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result userLogin(@RequestBody LoginDto loginDto){
        UserLoginVo userLoginVo = new UserLoginVo();
        return Result.success(userLoginVo);
    }
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result register(@RequestBody RegisterDto registerDto) {
        // 调用 UserService 进行注册逻辑
//        boolean isRegistered = userService.register(registerDto);
//
//        if (isRegistered) {
//            return Result.success("注册成功");
//        } else {
//            return Result.failure("注册失败");
        UserRegisterVo userRegisterVo = new UserRegisterVo();
        return Result.success(userRegisterVo);
        }


}
