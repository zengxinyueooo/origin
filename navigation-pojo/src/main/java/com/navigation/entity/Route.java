package com.navigation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 路线实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Route {
    private Integer id;  // 路线ID
    private Integer userId;  // 关联的用户ID
    private String routeData;  // 存储整个路径信息的 JSON 字段

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createTime; // 创建时间

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updateTime; // 修改时间
}
