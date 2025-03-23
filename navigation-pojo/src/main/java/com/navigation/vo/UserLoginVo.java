package com.navigation.vo;


import lombok.Data;

/**
 * 用户登录 VO（返回给前端的数据）
 */
@Data
public class UserLoginVo {

    private Integer userId; // 用户ID

    private String userName; // 用户名

    private String nickName; // 昵称

    private String email; // 邮箱

    private String role; // 用户角色（user/admin）

    private String token; // 认证 Token（JWT）
}
