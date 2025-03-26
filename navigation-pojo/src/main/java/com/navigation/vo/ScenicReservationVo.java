package com.navigation.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 景点订单预约 VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ScenicReservationVo extends UserLoginVo{

    private Integer scenicId; // 景点ID

    private String nickName; // 用户昵称

    private String scenicName; // 景点名称

    private String detail; // 门票介绍

    private String number; // 门票数量

    private String price; // 单价

    private String discount; // 折扣


}