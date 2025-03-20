package com.navigation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 酒店实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {
    private Integer id; // 酒店ID

    private String cover; // 酒店封面图片的URL或路径

    private String address; // 酒店地址

    private String phoneNumber; // 酒店联系电话

    private String hotelName; // 酒店名称

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; // 创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime; // 修改时间
}
