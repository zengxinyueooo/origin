package com.navigation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 景点预约实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScenicReservation {
    private Integer reservationId; // 预约ID

    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm", timezone = "GMT+8")
    private LocalDateTime reservationDate; // 预约日期

    private Integer peopleCount; // 预约人数

    private Integer isCongested; // 是否拥堵（0表示不拥堵，1表示拥堵）

    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm", timezone = "GMT+8")
    private LocalDateTime createTime; // 创建时间

    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm", timezone = "GMT+8")
    private LocalDateTime updateTime; // 更新时间

    private Integer userId; // 用户ID

    private Integer scenicId; // 景点ID
}
