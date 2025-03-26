package com.navigation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 路线实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Route {

    @TableId(value = "route_id", type = IdType.AUTO)
    private Integer routeId; // 路线ID

    @NotBlank(message = "用户ID不能为空")
    private Integer userId; // 用户ID

    @NotBlank(message = "起始景点ID不能为空")
    private Integer startSpotId; // 起点景点ID

    @NotBlank
    private Integer endSpotId; // 终点景点ID

    private Float distance; // 总距离（公里）

    private List<Integer> sequence; // 途经点顺序（以JSON格式存储）

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; // 创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime; // 修改时间
}