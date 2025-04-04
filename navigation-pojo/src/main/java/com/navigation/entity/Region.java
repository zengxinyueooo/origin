package com.navigation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 地区实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Region {
    private Integer regionId; // 地区ID

    private String regionName; // 地区名称

    private String regionDescription; // 地区描述

    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm", timezone = "GMT+8")
    private LocalDateTime createTime; // 创建时间

    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm", timezone = "GMT+8")
    private LocalDateTime updateTime; // 修改时间
}
