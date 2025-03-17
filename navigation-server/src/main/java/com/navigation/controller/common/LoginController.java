package com.navigation.controller.common;

import com.navigation.dto.LoginDto;
import com.navigation.entity.User;
import com.navigation.mapper.UserMapper;
import com.navigation.result.Result;
import com.navigation.service.UserService;
import com.navigation.utils.JwtUtil;
import com.navigation.vo.UserLoginVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/1")
    @ApiOperation("用户登录验证")

    public Result userLogin(@RequestBody LoginDto loginDto) {

        log.info("用户登录：{}", loginDto);
        User user = userService.login(loginDto);
        if(userMapper.exist(loginDto.getUsername()) == null){
            return Result.error("账号错误");
        }
        if(user == null) {
            return Result.error("用户名或密码错误");
        }
        String token = JwtUtil.createToken(String.valueOf(user.getId()));

        UserLoginVo userLoginVo = new UserLoginVo();
        userLoginVo.setId(user.getId());
        userLoginVo.setUsername(user.getUsername());
        userLoginVo.setToken(token);

        return Result.success(userLoginVo);
    }
}
