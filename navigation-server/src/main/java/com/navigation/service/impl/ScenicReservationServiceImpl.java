package com.navigation.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.navigation.context.BaseContext;
import com.navigation.entity.Scenic;
import com.navigation.entity.ScenicReservation;

import com.navigation.mapper.ScenicMapper;
import com.navigation.mapper.ScenicReservationMapper;
import com.navigation.result.PageResult;
import com.navigation.result.Result;
import com.navigation.service.ScenicReservationService;
import com.navigation.service.ScenicService;
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
public class ScenicReservationServiceImpl extends ServiceImpl<ScenicReservationMapper, ScenicReservation> implements ScenicReservationService {


    @Resource
    private ScenicReservationMapper scenicReservationMapper;

    @Resource
    private ScenicMapper scenicMapper;

    @Autowired
    private Validator validator;

    @Override
    public Result<Void> saveScenicReservation(ScenicReservation scenicReservation) {
        // 参数校验
        if (scenicReservation == null) {
            log.error("传入的 ScenicReservation 对象为空，无法保存景点预约信息");
            return Result.error("传入的景点预约信息为空");
        }

        // 从UserHolder获取userId并设置到scenicReservation对象
        Integer userId = BaseContext.getUserId();
        scenicReservation.setUserId(userId);

        if (scenicReservation.getScenicId() == null) {
            log.error("景点ID为空，无法保存景点预约信息");
            return Result.error("景点ID为空");
        }

        // 获取景点最大承载量
        Integer scenicId = scenicReservation.getScenicId();
        int maxCapacity = scenicMapper.getMaxCapacityByScenicId(scenicId);
        if (maxCapacity <= 0) {
            log.error("景点ID: {} 对应的最大承载量数据异常", scenicId);
            return Result.error("景点最大承载量数据异常，请检查");
        }

        // 获取该景点所有预约记录的总人数
        int totalPeopleCount = scenicReservationMapper.getTotalPeopleCountByScenicId(scenicId);
        // 判断是否拥堵
        int total = scenicReservation.getPeopleCount() + totalPeopleCount;
        if (total >= maxCapacity * 0.8) {
            scenicReservation.setIsCongested(1);
        } else {
            scenicReservation.setIsCongested(0);
        }

        // 使用Validator校验除userId和scenicId外的其他字段
        Set<ConstraintViolation<ScenicReservation>> violations = validator.validate(scenicReservation);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("以下字段校验不通过: ");
            for (ConstraintViolation<ScenicReservation> violation : violations) {
                errorMessage.append(violation.getPropertyPath()).append(" - ").append(violation.getMessage()).append("; ");
            }
            log.error(errorMessage.toString());
            return Result.error(errorMessage.toString());
        }

        try {
            scenicReservation.setCreateTime(LocalDateTime.now());
            scenicReservation.setUpdateTime(LocalDateTime.now());
            scenicReservationMapper.saveScenicReservation(scenicReservation);
            return Result.success();
        } catch (Exception e) {
            log.error("保存景点预约信息时出现异常", e);
            return Result.error("保存景点预约信息失败，请稍后重试");
        }
    }

    @Override
    public Result<Void> update(ScenicReservation scenicReservation) {
        if (scenicReservation == null) {
            log.error("传入的 ScenicReservation 对象为空，无法更新景点预约信息");
            return Result.error("传入的景点预约信息为空");
        }

        try {
            // 从UserHolder获取当前用户ID
            Integer userId = BaseContext.getUserId();
            if (userId == null) {
                log.error("无法获取当前用户ID，更新操作失败");
                return Result.error("无法获取当前用户ID，请检查");
            }

            // 获取传入的景点ID
            Integer scenicId = scenicReservation.getScenicId();
            if (scenicId == null) {
                log.error("传入的景点ID为空，无法定位预约订单");
                return Result.error("传入的景点ID为空，请检查");
            }

            // 检查该用户对该景点是否已存在预约记录
            boolean isExist = scenicReservationMapper.existsByUserIdAndScenicId(userId, scenicId);
            if (isExist) {
                log.error("用户ID: {} 已存在对景点ID: {} 的预约记录，不允许重复预约", userId, scenicId);
                return Result.error("您已预约过该景点，不允许重复预约");
            }

            // 更新预约信息（将传入的信息覆盖到已存在的订单上）
            scenicReservation.setUpdateTime(LocalDateTime.now());
            scenicReservationMapper.update(scenicReservation);
            return Result.success();
        } catch (Exception e) {
            log.error("更新景点预约信息时出现异常", e);
            return Result.error("更新景点预约信息失败，请稍后重试");
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
            List<Integer> allExistingIds = scenicReservationMapper.getAllExistingIds();
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
                        .collect(Collectors.joining(","));
                return Result.error("ID为 " + idsString + " 的记录不存在，删除失败");
            }

            // 执行批量删除操作
            int rows = scenicReservationMapper.batchDelete(ids);
            if (rows > 0) {
                return Result.success();
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除操作出现异常", e);
            return Result.error("批量删除操作失败，请稍后重试");
        }
    }

    @Override
    public PageResult queryScenicReservation(Integer pageNum, Integer pageSize) {
        // 设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        List<ScenicReservation> scenicList = scenicReservationMapper.queryScenicReservation(pageNum, pageSize);
        //获取分页元数据（总记录数、总页数等）
        Page list = (Page<ScenicReservation>) scenicList;
        PageResult pageResult = new PageResult(list.getTotal(), list.getResult());
        return pageResult;
    }

    @Override
    public Result<ScenicReservation> queryScenicReservationById(Integer id) {
        if (id == null) {
            log.error("传入的景点预约ID为空");
            return Result.error("id为空！");
        }

        try {
            ScenicReservation scenicReservation = scenicReservationMapper.queryScenicReservationById(id);
            if (scenicReservation == null) {
                log.error("景点预约id不存在");
                return Result.error("id不存在！");
            }
            return Result.success(scenicReservation);
        } catch (Exception e) {
            log.error("根据id查询景点预约信息时发生异常", e);
            return Result.error("根据id查询景点预约信息失败，请稍后重试");
        }
    }


}
