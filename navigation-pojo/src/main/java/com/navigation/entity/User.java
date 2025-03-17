package com.navigation.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
public class User {
    private int id;
    private String password;
    private String username;
    private String Nickname;
    private int age;
    private String gender;
    private String telephone;
    @JsonFormat(pattern = "YYYY-MM-dd")
    LocalDateTime createTime;
    @JsonFormat(pattern = "YYYY-MM-dd")
    LocalDateTime updateTime;


}
