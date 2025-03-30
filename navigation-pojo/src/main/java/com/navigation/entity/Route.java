package com.navigation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 路线实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Route {
    private Long id; // 路线ID，数据库中的主键为BIGINT，实体类对应为Long类型

    private Integer startScenicId; // 起点景点ID

    private Integer endScenicId; // 终点景点ID

    private Double originLat; // 起点纬度

    private Double originLng; // 起点经度

    private Double destinationLat; // 终点纬度

    private Double destinationLng; // 终点经度

    private String steps; // 途经点，存储JSON字符串

    private double distance; // 路线总距离（米）

    private Integer duration; // 预计步行时间（秒）

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; // 创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime; // 修改时间
}
