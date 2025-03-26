package com.navigation.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.navigation.entity.Hotel;

import com.navigation.mapper.HotelMapper;

import com.navigation.result.PageResult;
import com.navigation.result.Result;
import com.navigation.service.HotelService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HotelServiceImpl extends ServiceImpl<HotelMapper, Hotel> implements HotelService {

    @Resource
    private HotelMapper hotelMapper;


    @Override
    public Result<Void> saveHotel(Hotel hotel) {
        // 参数校验
        if (hotel == null) {
            log.error("传入的 hotel 对象为空，无法保存酒店信息");
            return Result.error("传入的酒店信息为空");
        }
        try {
            hotel.setCreateTime(LocalDateTime.now());
            hotel.setUpdateTime(LocalDateTime.now());
            hotelMapper.saveHotel(hotel);
            return Result.success();
        } catch (Exception e) {
            log.error("保存酒店信息时出现异常", e);
            return Result.error("保存酒店信息失败，请稍后重试");
        }
    }

    @Override
    public Result<Void> update(Hotel hotel) {
        if (hotel == null) {
            log.error ("传入的 hotel 对象为空，无法更新酒店信息");
            return Result.error("传入的酒店信息为空");
        }
        try {
            hotel.setUpdateTime(LocalDateTime.now());
            hotelMapper.update(hotel);
            return Result.success();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional  // 添加事务管理（确保批量删除原子性）
    public Result<Void> batchDelete(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("删除的ID列表不能为空");
        }
        int rows = hotelMapper.batchDelete(ids);
        if (rows > 0) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }

    @Override
    public PageResult queryHotel(Integer pageNum, Integer pageSize) {
        // 设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        List<Hotel> hotelList = hotelMapper.queryHotel(pageNum, pageSize);
        //获取分页元数据（总记录数、总页数等）
        Page list = (Page<Hotel>) hotelList;
        PageResult pageResult = new PageResult(list.getTotal(), list.getResult());
        return pageResult;
    }

    @Override
    public Hotel queryHotelById(Integer id) {
        if(id == null){
            log.error("传入的id为空");
            return null;
        }
        return hotelMapper.queryHotelById(id);
    }

}
