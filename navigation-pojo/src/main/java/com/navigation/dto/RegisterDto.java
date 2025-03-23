package com.navigation.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户注册 DTO
 */
@Data
public class RegisterDto {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
    private String userName; // 用户名

    @NotBlank(message = "昵称不能为空")
    private String nickName; // 昵称

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email; // 邮箱

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password; // 密码（前端明文传输，后端加密存储）

    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword; // 确认密码（需要在服务层验证）

    private String gender; // 性别（可选）

    private Integer age; // 年龄（可选）
}