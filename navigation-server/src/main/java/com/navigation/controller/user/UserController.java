package com.navigation.controller.user;

import com.navigation.dto.LoginDto;
import com.navigation.service.UserService;
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
@ApiModel("用户注册")
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
}
