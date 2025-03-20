package com.navigation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 管理员实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Manager {
    private Integer managerId; // 管理员ID

    private String userName; // 管理员账号名

    private String role; // 角色字段：'user' 代表普通用户，'admin' 代表管理员

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; // 创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime; // 修改时间
}
