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
  @TableName("hotel")
  public class Hotel {
      @TableId(value = "id", type = IdType.AUTO)
      private int id;

      @NotBlank(message = "酒店封面不能为空")
      private String cover;

      @NotBlank(message = "酒店地点不能为空")
      private String address;

      @NotBlank(message = "酒店描述不能为空")
      private String hotelDescription;

      @NotBlank(message = "酒店电话不能为空")
      private String phoneNumber;

      @NotBlank(message = "酒店名称不能为空")
      private String hotelName;


      @JsonFormat(pattern = "YYYY-MM-dd")
      LocalDateTime createTime;

      @JsonFormat(pattern = "YYYY-MM-dd")
      LocalDateTime updateTime;

  }
