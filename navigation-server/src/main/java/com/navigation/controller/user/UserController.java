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
import java.util.Map;

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
    @GetMapping("/login")
    @ApiOperation("用户登录")
    public String userLogin(@RequestBody LoginDto loginDto){
        UserLoginVo userLoginVo = new UserLoginVo();
        return "hello";
    }


//    @PostMapping("/register")
//    @ApiOperation("用户注册")
//    public Map<String,Object> RegisterUser(@RequestBody RegisterDto registerDto) {
//        // 调用 UserService 进行注册逻辑
//        boolean isRegistered = userService.RegisterUser(registerDto).isEmpty();
//        if (isRegistered) {
//            return Map.of("status", "success", "message", "注册成功");
//        } else {
//            return Map.of("status", "failure", "message", "注册失败");
//        }
//
//        }
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Map<String, Object> RegisterUser(@RequestBody RegisterDto registerDto) {
        // 调用 UserService 进行注册逻辑
        Map<String, Object> result = userService.RegisterUser(registerDto);

        // 如果 result 为 null 或为空，表示注册失败
        if (result == null || result.isEmpty()) {
            return Map.of("status", "failure", "message", "注册失败");
        } else {
            return Map.of("status", "success", "message", "注册成功");
        }
    }




}
