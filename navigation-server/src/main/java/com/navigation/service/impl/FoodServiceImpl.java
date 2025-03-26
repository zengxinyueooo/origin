package com.navigation.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.navigation.entity.Food;
import com.navigation.entity.Food;
import com.navigation.mapper.FoodMapper;
import com.navigation.mapper.FoodMapper;
import com.navigation.result.PageResult;
import com.navigation.result.Result;
import com.navigation.service.FoodService;
import com.navigation.service.FoodService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements FoodService {


    @Resource
    private FoodMapper foodMapper;


    @Override
    public Result<Void> saveFood(Food food) {
        // 参数校验
        if (food == null) {
            log.error("传入的 Food 对象为空，无法保存美食信息");
            return Result.error("传入的美食信息为空");
        }
        try {
            food.setCreateTime(LocalDateTime.now());
            food.setUpdateTime(LocalDateTime.now());
            foodMapper.saveFood(food);
            return Result.success();
        } catch (Exception e) {
            log.error("保存美食信息时出现异常", e);
            return Result.error("保存美食信息失败，请稍后重试");
        }
    }

    @Override
    public Result<Void> update(Food food) {
        if (food == null) {
            log.error("传入的 Food 对象为空，无法更新美食信息");
            return Result.error("传入的美食信息为空");
        }
        try {
            food.setUpdateTime(LocalDateTime.now());
            foodMapper.update(food);
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
        int rows = foodMapper.batchDelete(ids);
        if (rows > 0) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }

    @Override
    public PageResult queryFood(Integer pageNum, Integer pageSize) {
        // 设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        List<Food> FoodList = foodMapper.queryFood(pageNum, pageSize);
        //获取分页元数据（总记录数、总页数等）
        Page list = (Page<Food>) FoodList;
        PageResult pageResult = new PageResult(list.getTotal(), list.getResult());
        return pageResult;
    }

    @Override
    public Food queryFoodById(Integer id) {
        if(id == null){
            log.error("传入的id为空");
            return null;
        }
        return foodMapper.queryFoodById(id);
    }

    @Override
    public List<Food> queryFoodByRegionId(Integer id) {
        if(id == null){
            log.error("传入的美食ID为空");
            return null;
        }
        return foodMapper.queryFoodByRegionId(id);
    }


}
