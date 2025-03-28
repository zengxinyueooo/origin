package com.navigation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.navigation.dto.ScenicQueryDto;
import com.navigation.entity.Scenic;
import com.navigation.result.PageResult;
import com.navigation.result.Result;

import java.util.List;

public interface ScenicService extends IService<Scenic> {

    Result<Void> saveScenic(Scenic scenic);

    Result<Void> update(Scenic scenic);

    Result<Void> batchDelete(List<Integer> ids);

    PageResult queryScenic(Integer pageNum, Integer pageSize);


    Result<Scenic> queryScenicById(Integer id);
}
