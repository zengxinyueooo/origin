package com.navigation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserUpdateDto {
    private int id;
    private String username;
    private String password;
    private String nickname;
    private String telephone;
    private int age;
    private int status;
    private String gender;
}
