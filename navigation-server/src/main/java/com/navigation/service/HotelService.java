package com.navigation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.navigation.entity.Hotel;

import com.navigation.result.PageResult;
import com.navigation.result.Result;

import java.util.List;

public interface HotelService extends IService<Hotel> {

    Result<Void> saveHotel(Hotel hotel);

    Result<Void> update(Hotel hotel);

    Result<Void> batchDelete(List<Integer> ids);

    PageResult queryHotel(Integer pageNum, Integer pageSize);


    Result<Hotel> queryHotelById(Integer id);
}