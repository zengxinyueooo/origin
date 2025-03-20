package com.navigation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 门票实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private Integer id; // 门票ID

    private Integer scenicSpotId; // 景点ID（外键，关联到景点表的id）

    private String ticketType; // 票种类型（如成人票、儿童票、学生票、老年票等）

    private BigDecimal price; // 门票价格

    private Integer availability; // 门票剩余数量

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime validFrom; // 门票有效期开始时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime validTo; // 门票有效期结束时间

    private String version; // 乐观锁版本号字段

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; // 创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime; // 修改时间
}
