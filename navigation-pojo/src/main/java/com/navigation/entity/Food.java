package com.navigation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 美食实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Food {
    private Integer foodId; // 美食ID

    private Integer regionId; // 地区ID，外键，关联到地区表的region_id

    private String foodName; // 美食名称

    private String foodDescription; // 美食简介

    private String coverImage; // 美食封面图片URL或路径

    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm", timezone = "GMT+8")
    private LocalDateTime createTime; // 创建时间

    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm", timezone = "GMT+8")
    private LocalDateTime updateTime; // 修改时间
}
