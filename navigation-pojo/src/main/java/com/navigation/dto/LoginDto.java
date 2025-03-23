package com.navigation.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录 DTO
 */
@Data
public class LoginDto {

    @NotBlank(message = "用户名或邮箱不能为空")
    private String usernameOrEmail; // 用户名或邮箱（允许用户使用邮箱或用户名登录）

    @NotBlank(message = "密码不能为空")
    private String password; // 密码
}
