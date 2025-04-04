package com.navigation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 票务预定实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketReservation {
    private Integer reservationId; // 预定ID

    private Integer userId; // 用户ID

    private Integer ticketId; // 门票ID

    private Integer quantity; // 预定的数量

    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm", timezone = "GMT+8")
    private LocalDateTime reservationTime; // 预定的时间

    private Integer status; // 预定状态，0表示未支付

    private BigDecimal totalPrice; // 总金额

    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm", timezone = "GMT+8")
    private LocalDateTime createTime; // 创建时间

    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm", timezone = "GMT+8")
    private LocalDateTime updateTime; // 修改时间
}
