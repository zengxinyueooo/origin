package com.navigation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 景点预约实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScenicReservation {

    @TableId(value = "reservation_id", type = IdType.AUTO)
    private Integer reservationId; // 预约ID

    @NotBlank(message = "预约日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime reservationDate; // 预约日期

    @NotBlank(message = "预约人数不能为空")
    private Integer peopleCount; // 预约人数

    @NotBlank(message = "是否拥堵不能为空")
    private Integer isCongested; // 是否拥堵（0表示不拥堵，1表示拥堵）

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; // 创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime; // 更新时间

    @NotBlank(message = "用户Id不能为空")
    private Integer userId; // 用户ID

    @NotBlank(message = "景点Id不能为空")
    private Integer scenicId; // 景点ID
}