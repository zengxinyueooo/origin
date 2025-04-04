package com.navigation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 景点实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Scenic {
    private Integer id; // 景点ID

    private String scenicName; // 景点名称

    private String scenicCover; // 景点封面图片URL或路径

    private String scenicFavoriterIds; // 景点收藏者ID列表，以逗号分隔

    private String scenicStatus; // 景点开放状态（如开放、关闭）

    //private String location; // 景点位置，存储经纬度或具体地址
    private BigDecimal lat;  // 纬度
    private BigDecimal lng;  // 经度

    private String scenicDescription; // 景点介绍

    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm", timezone = "GMT+8")
    private LocalDateTime createTime; // 创建时间

    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm", timezone = "GMT+8")
    private LocalDateTime updateTime; // 更新时间

    private Integer maxCapacity; // 最大承载人数

    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm", timezone = "GMT+8")
    private LocalDateTime openStartTime; // 开放时间起始时间

    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm", timezone = "GMT+8")
    private LocalDateTime openEndTime; // 开放时间结束时间

    private Integer regionId; // 地区ID
}
