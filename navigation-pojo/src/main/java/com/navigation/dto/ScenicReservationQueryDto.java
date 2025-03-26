package com.navigation.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.management.Query;

@Data
@EqualsAndHashCode(callSuper = false)
public class ScenicReservationQueryDto {

    private Integer userId; //用户Id

    private Integer ticketId; //门票Id

    private boolean status; //支付状态

}
