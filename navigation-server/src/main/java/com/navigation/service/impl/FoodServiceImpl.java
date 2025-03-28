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
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements FoodService {


    @Resource
    private FoodMapper foodMapper;

    @Autowired
    private Validator validator;

    @Override
    public Result<Void> saveFood(Food food) {
        // 参数校验
        if (food == null) {
            log.error("传入的 Food 对象为空，无法保存美食信息");
            return Result.error("传入的美食信息为空");
        }


        Set<ConstraintViolation<Food>> violations = validator.validate(food);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("以下必填参数未传入: ");
            for (ConstraintViolation<Food> violation : violations) {
                errorMessage.append(violation.getMessage()).append("; ");
            }
            log.error(errorMessage.toString());
            return Result.error(errorMessage.toString());
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
            // 检查 food_id 是否存在
            Integer foodId = food.getFoodId();
            if (foodId != null) {
                Food existingFood = foodMapper.queryFoodById(foodId);
                if (existingFood == null) {
                    log.error("food_id为 {} 的美食记录不存在", foodId);
                    return Result.error("food_id为 " + foodId + " 的美食不存在");
                }
            } else {
                log.error("传入的 Food 对象中 food_id 为空");
                return Result.error("传入的 Food 对象中 food_id 为空");
            }

            // 检查地区id是否存在
            Integer regionId = food.getRegionId();
            if (regionId != null) {
                int num = foodMapper.countFoodById(regionId);
                if (num == 0) {
                    log.error("地区id为 {} 的地区记录不存在", regionId);
                    return Result.error("地区id为 " + regionId + " 的地区记录不存在");
                }
            } else {
                log.error("传入的 Food 对象中地区id 为空");
                return Result.error("传入的 Food 对象中地区id 为空");
            }

            food.setUpdateTime(LocalDateTime.now());
            foodMapper.update(food);
            return Result.success();
        } catch (Exception e) {
            log.error("更新美食信息时出现异常", e);
            return Result.error("更新美食信息时出现异常: " + e.getMessage());
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
            // 获取数据库中所有存在的ID集合
            List<Integer> allExistingIds = foodMapper.getAllExistingIds();
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
                return Result.error("ID为 " + idsString + " 的记录不存在，删除失败");
            }

            // 执行批量删除操作
            int rows = foodMapper.batchDelete(ids);
            if (rows > 0) {
                return Result.success();
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            // 记录详细的异常日志，实际应用中建议使用日志框架，如Logback或Log4j
            e.printStackTrace();
            // 返回包含具体异常信息的错误结果
            return Result.error("删除过程中出现异常: " + e.getMessage());
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
    public Result<Food> queryFoodById(Integer id) {
        if (id == null) {
            log.error("传入的id为空");
            return Result.error("id为空！");
        }
        try {
            Food food = foodMapper.queryFoodById(id);
            if (food == null) {
                log.error("美食id不存在");
                return Result.error("id不存在！");
            }
            return Result.success(food);
        } catch (Exception e) {
            log.error("根据id查询美食信息时发生异常", e);
            return null;
        }
    }

    @Override
    public Result<List<Food>> queryFoodByRegionId(Integer id) {
        if (id == null) {
            log.error("传入的区域ID为空");
            return Result.error("id为空！");
        }
        int num = foodMapper.countFoodById(id);
        if(num == 0){
            return Result.error("地区Id为空！");
        }

        try {
            List<Food> foodList = foodMapper.queryFoodByRegionId(id);
            return Result.success(foodList);
        } catch (Exception e) {
            log.error("根据区域ID查询美食信息时发生异常", e);
            return Result.error("根据区域ID查询美食信息失败，请稍后重试");
        }
    }


}
