package com.navigation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer userId; // 用户ID



    private String nickName; // 昵称

    private String email; // 用户邮箱地址

    private String password; // 用户密码，使用MD5和盐加密存储

    private String salt; // 盐，用于密码加密

    private Integer age; // 用户年龄

    private String gender; // 用户性别

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime activationTime; // 账号激活失效时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; // 账号注册时间

    private Integer isValid; // 账号是否有效（0：不可用，1：可以用）

    private String confirmCode; // 确认码，用于用户注册或验证

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime; // 账号修改时间

    private String head; // 用户头像

    private String role; // 用户角色，'user' 代表普通用户，'admin' 代表管理员
}
