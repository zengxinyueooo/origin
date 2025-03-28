package com.navigation.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.navigation.entity.Food;
import com.navigation.entity.Hotel;

import com.navigation.mapper.HotelMapper;

import com.navigation.result.PageResult;
import com.navigation.result.Result;
import com.navigation.service.HotelService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HotelServiceImpl extends ServiceImpl<HotelMapper, Hotel> implements HotelService {

    @Resource
    private HotelMapper hotelMapper;

    @Autowired
    private Validator validator;

    @Override
    public Result<Void> saveHotel(Hotel hotel) {
        // 参数校验
        if (hotel == null) {
            log.error("传入的 hotel 对象为空，无法保存酒店信息");
            return Result.error("传入的酒店信息为空");
        }

        Set<ConstraintViolation<Hotel>> violations = validator.validate(hotel);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("以下必填参数未传入: ");
            for (ConstraintViolation<Hotel> violation : violations) {
                errorMessage.append(violation.getMessage()).append("; ");
            }
            log.error(errorMessage.toString());
            return Result.error(errorMessage.toString());
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
            log.error("传入的 hotel 对象为空，无法更新酒店信息");
            return Result.error("传入的酒店信息为空");
        }

        try {
            // 检查 hotelId 是否存在
            Integer hotelId = hotel.getId();
            if (hotelId != null) {
                int count = hotelMapper.countHotelById(hotelId);
                if (count == 0) {
                    log.error("酒店id不存在");
                    return Result.error("酒店id不存在");
                }
            } else {
                log.error("传入的 Hotel 对象中 hotelId 为空");
                return Result.error("传入的 Hotel 对象中 hotelId 为空");
            }

            hotel.setUpdateTime(LocalDateTime.now());
            hotelMapper.update(hotel);
            return Result.success();
        } catch (Exception e) {
            log.error("更新酒店信息时出现异常", e);
            return Result.error("更新酒店信息时出现异常: " + e.getMessage());
        }
    }

    @Override
    @Transactional  // 添加事务管理（确保批量删除原子性）
    public Result<Void> batchDelete(List<Integer> ids) {
        // 检查传入的ID列表是否为空
        if (ids == null || ids.isEmpty()) {
            return Result.error("删除的ID列表不能为空");
        }

        try {
            // 获取数据库中所有存在的酒店ID集合
            List<Integer> allExistingIds = hotelMapper.getAllExistingHotelIds();
            Set<Integer> existingIdSet = allExistingIds.stream().collect(Collectors.toSet());

            // 用于存储不存在的ID
            List<Integer> nonExistingIds = new ArrayList<>();

            // 检查传入的每个ID是否存在
            for (Integer id : ids) {
                if (!existingIdSet.contains(id)) {
                    nonExistingIds.add(id);
                }
            }

            // 如果有不存在的ID，返回包含所有不存在ID的错误信息
            if (!nonExistingIds.isEmpty()) {
                String idsString = nonExistingIds.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));
                return Result.error("ID为 " + idsString + " 的酒店记录不存在，删除失败");
            }

            // 执行批量删除操作
            int rows = hotelMapper.batchDelete(ids);
            if (rows > 0) {
                return Result.success();
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            // 记录详细的异常日志，实际应用中建议使用日志框架，如Logback或Log4j
            log.error("删除酒店过程中出现异常", e);
            // 返回包含具体异常信息的错误结果
            return Result.error("删除过程中出现异常: " + e.getMessage());
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
    public Result<Hotel> queryHotelById(Integer id) {
        if(id == null){
            log.error("传入的id为空");
            return null;
        }
        int num = hotelMapper.countHotelById(id);
        if(num == 0){
            return Result.error("酒店Id不存在");
        }
        try {
            Hotel hotel = hotelMapper.queryHotelById(id);
            return Result.success(hotel);
        } catch (Exception e) {
            log.error("根据酒店ID查询酒店信息时发生异常", e);
            return Result.error("根据酒店ID查询酒店信息失败，请稍后重试");
        }
    }

}
