package com.navigation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 票务预定实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketReservation {

    @TableId(value = "reservation_id", type = IdType.AUTO)
    private Integer reservationId; // 预定ID

    @NotBlank(message = "用户Id不能为空")
    private Integer userId; // 用户ID

    @NotBlank(message = "门票Id不能为空")
    private Integer ticketId; // 门票ID

    @NotBlank(message = "预订数量不能为空")
    private Integer quantity; // 预定的数量

    @NotBlank(message = "预定时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime reservationTime; // 预定的时间

    private Integer status; // 预定状态，0表示未支付

    private BigDecimal totalPrice; // 总金额

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; // 创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime; // 修改时间
}