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
public class Admin {
    private int id;
    private String username;
    private String password;
    private String nickname;
    private String gender;
    private String telephone;
    @JsonFormat(pattern = "YYYY-MM-dd")
    LocalDateTime createTime;
    @JsonFormat(pattern = "YYYY-MM-dd")
    LocalDateTime updateTime;


}
