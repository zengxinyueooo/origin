package com.navigation.controller.user;

import com.navigation.dto.LoginDto;
import com.navigation.dto.RegisterDto;
import com.navigation.service.UserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/users")
@Api(tags = "账号登录注册激活操作管理")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录（支持管理员和普通用户，登录成功返回 JWT Token）
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "用户输入账号密码进行登录，支持管理员直接使用明文密码登录")
    @ApiResponses({
            @ApiResponse(code = 200, message = "登录成功，返回 token", response = Map.class),
            @ApiResponse(code = 400, message = "登录失败，账号或密码错误")
    })
    public Map<String, Object> userLogin(
            @RequestBody @ApiParam(value = "登录请求数据", required = true) LoginDto loginDto) {
        return userService.LoginUser(loginDto);
    }

    /**
     * 用户注册（仅适用于普通用户）
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册", notes = "用户输入必要信息进行注册，并在注册成功后提示用户前往邮箱激活账号")
    @ApiResponses({
            @ApiResponse(code = 200, message = "注册成功，请前往邮箱激活账号", response = Map.class),
            @ApiResponse(code = 400, message = "注册失败，邮箱已存在或其他错误")
    })
    public Map<String, Object> registerUser(
            @RequestBody @ApiParam(value = "注册请求数据", required = true) RegisterDto registerDto) {
        Map<String, Object> result = userService.RegisterUser(registerDto);
        if (result == null || !"success".equals(result.get("status"))) {
            return Map.of("status", "failure", "message", result != null ? result.get("message") : "注册失败");
        } else {
            return Map.of("status", "success", "message", "注册成功，请前往邮箱激活账号");
        }
    }

    /**
     * 账号激活（仅适用于普通用户）
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

    /**
     * 获取当前登录用户信息（通过 JWT Token）
     */
    @GetMapping("/profile")
    @ApiOperation(value = "获取当前登录用户信息", notes = "通过请求头携带的 JWT Token 获取当前用户信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功", response = Map.class),
            @ApiResponse(code = 401, message = "Token 无效或已过期")
    })
    public Map<String, Object> getUserProfile(@RequestHeader("Authorization") String token) {
        // 此处可通过工具类解析 token 并返回对应用户信息，示例仅返回 token 信息
        return Map.of("status", "success", "message", "Token 有效", "token", token);
    }
}
