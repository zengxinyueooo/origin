package com.navigation.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 封装分页查询结果
 * PageHelper实现
 */
@Data
public class PageResult<T> implements Serializable {

    private long total; //总记录数

    private List<T> records; //当前页数据集合

    public PageResult(long total, List records) {
        this.total = total;
        this.records = records;
    }

}
