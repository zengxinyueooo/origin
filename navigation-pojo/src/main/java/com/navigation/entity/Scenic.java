  package com.navigation.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("scenic")
public class Scenic {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    @NotBlank(message = "景区名称不能为空")
    private String scenicName;

    @NotBlank(message = "景区封面不能为空")
    private String scenicCover;

    private String scenicFavoriteIds;

    private int scenicStatus;

    @NotBlank(message = "景区位置不能为空")
    private String location;

    @NotBlank(message = "景区描述不能为空")
    private String scenicDescription;

    @NotNull(message = "景区最大容量不能为空")
    private int maxCapacity;

    @NotNull(message = "景区开放时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")  // 注意格式中的空格和大小写
    private LocalDateTime openStartTime;

    @NotNull(message = "景区关闭时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime openEndTime;

    @JsonFormat(pattern = "YYYY-MM-dd")
    LocalDateTime createTime;

    @JsonFormat(pattern = "YYYY-MM-dd")
    LocalDateTime updateTime;

    private Integer regionId; // 地区ID

}
