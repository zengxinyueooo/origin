package com.navigation.vo;

import lombok.Data;

/**
 * 用户注册 VO
 */
@Data
public class UserRegisterVo {

    private Integer userId; // 用户ID

    private String userName; // 用户名

    private String nickName; // 昵称

    private String email; // 邮箱

    private String role; // 用户角色（默认 user）

    private String message = "注册成功"; // 提示消息
}