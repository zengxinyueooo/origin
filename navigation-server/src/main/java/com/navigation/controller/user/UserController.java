package com.navigation.controller.user;

import com.navigation.dto.LoginDto;
import com.navigation.dto.RegisterDto;
import com.navigation.service.UserService;
import com.navigation.result.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/users")

@Api(tags = "用户操作管理") // API 分组
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "用户输入账号密码进行登录")
    @ApiResponses({
            @ApiResponse(code = 200, message = "登录成功", response = Map.class),
            @ApiResponse(code = 400, message = "登录失败，账号或密码错误")
    })
    public Map<String, Object> userLogin(@RequestBody @ApiParam(value = "登录请求数据", required = true) LoginDto loginDto) {
        return userService.LoginUser(loginDto);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册", notes = "用户输入必要信息进行注册，并在注册成功后提示用户前往邮箱激活账号")
    @ApiResponses({
            @ApiResponse(code = 200, message = "注册成功，请前往邮箱激活账号", response = Map.class),
            @ApiResponse(code = 400, message = "注册失败，用户名已存在或其他错误")
    })
    public Map<String, Object> RegisterUser(@RequestBody @ApiParam(value = "注册请求数据", required = true) RegisterDto registerDto) {
        Map<String, Object> result = userService.RegisterUser(registerDto);
        if (result == null || !"success".equals(result.get("status"))) {
            return Map.of("status", "failure", "message", result != null ? result.get("message") : "注册失败");
        } else {
            return Map.of("status", "success", "message", "注册成功，请前往邮箱激活账号");
        }
    }


    /**
     * 账号激活
     */
    @GetMapping("/activation")
    @ApiOperation(value = "账号激活", notes = "用户点击邮件中的激活链接进行账号激活")
    @ApiImplicitParam(name = "confirmCode", value = "激活码", required = true, paramType = "query", dataType = "String")
    @ApiResponses({
            @ApiResponse(code = 200, message = "激活成功", response = Map.class),
            @ApiResponse(code = 400, message = "激活失败，激活码无效")
    })
    public Map<String, Object> activationAccount(@RequestParam String confirmCode) {
        return userService.activationAccount(confirmCode);
    }
}
