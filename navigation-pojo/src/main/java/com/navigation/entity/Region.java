  package com.navigation.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

  /**
   * 用户实体类
   */
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @TableName("region")
  public class Region {
      @TableId(value = "region_id", type = IdType.AUTO)
      private int regionId;

      @NotBlank(message = "地区名称不能为空")
      private String regionName;

      @NotBlank(message = "地区描述不能为空")
      private String regionDescription;


      @JsonFormat(pattern = "YYYY-MM-dd")
      LocalDateTime createTime;

      @JsonFormat(pattern = "YYYY-MM-dd")
      LocalDateTime updateTime;

  }
