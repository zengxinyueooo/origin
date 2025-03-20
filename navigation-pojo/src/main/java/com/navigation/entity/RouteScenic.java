package com.navigation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 路线景点关联实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteScenic {
    private Integer id; // ID

    private Integer routeId; // 路线ID

    private Integer scenicId; // 景点ID

    private Integer sequence; // 顺序号（从1开始）

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; // 创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime; // 修改时间
}
