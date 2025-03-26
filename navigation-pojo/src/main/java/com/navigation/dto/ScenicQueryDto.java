package com.navigation.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ScenicQueryDto {
    private Integer id;

    private String name;

    private int status;

}
