package com.navigation.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.navigation.entity.Scenic;
import com.navigation.mapper.ScenicMapper;
import com.navigation.result.PageResult;
import com.navigation.result.Result;
import com.navigation.service.ScenicService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
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
public class ScenicServiceImpl extends ServiceImpl<ScenicMapper, Scenic> implements ScenicService {


    @Resource
    private ScenicMapper scenicMapper;

    @Autowired
    private Validator validator;

    @Override
    public Result<Void> saveScenic(Scenic scenic) {
        // 参数校验
        if (scenic == null) {
            log.error("传入的 Scenic 对象为空，无法保存景点信息");
            return Result.error("传入的景点信息为空");
        }

        // 使用Validator校验Scenic对象的其他必填字段
        Set<ConstraintViolation<Scenic>> violations = validator.validate(scenic);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("以下必填参数未传入: ");
            for (ConstraintViolation<Scenic> violation : violations) {
                errorMessage.append(violation.getMessage()).append("; ");
            }
            log.error(errorMessage.toString());
            return Result.error(errorMessage.toString());
        }

        try {
            scenic.setScenicStatus(1);
            scenic.setCreateTime(LocalDateTime.now());
            scenic.setUpdateTime(LocalDateTime.now());
            scenicMapper.saveScenic(scenic);
            return Result.success();
        } catch (Exception e) {
            log.error("保存景点信息时出现异常", e);
            // 这里如果开启了事务，异常会触发事务回滚
            return Result.error("保存景点信息失败，请稍后重试");
        }
    }

    @Override
    public Result<Void> update(Scenic scenic) {
        if (scenic == null) {
            log.error("传入的 Scenic 对象为空，无法更新景点信息");
            return Result.error("传入的景点信息为空");
        }

        try {
            // 检查scenicId是否存在
            Integer scenicId = scenic.getId();
            int count = scenicMapper.countScenicById(scenicId);
            if (count == 0) {
                log.error("景区id为 {} 的景区记录不存在", scenicId);
                return Result.error("景区id为 " + scenicId + " 的景区记录不存在");
            }

            scenic.setUpdateTime(LocalDateTime.now());
            scenicMapper.update(scenic);
            return Result.success();
        } catch (Exception e) {
            log.error("更新景点信息时出现异常", e);
            throw new RuntimeException(e);
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
            List<Integer> allExistingIds = scenicMapper.getAllExistingIds();
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
            int rows = scenicMapper.batchDelete(ids);
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
    public PageResult queryScenic(Integer pageNum, Integer pageSize) {
        // 设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        List<Scenic> scenicList = scenicMapper.queryScenic(pageNum, pageSize);
        //获取分页元数据（总记录数、总页数等）
        Page list = (Page<Scenic>) scenicList;
        PageResult pageResult = new PageResult(list.getTotal(), list.getResult());
        return pageResult;
    }

    @Override
    public Result<Scenic> queryScenicById(Integer id) {
        if (id == null) {
            log.error("传入的景点ID为空");
            return Result.error("id为空！");
        }

        try {
            Scenic scenic = scenicMapper.queryScenicById(id);
            if (scenic == null) {
                log.error("景点ID为 {} 的景点记录不存在", id);
                return Result.error("id不存在！");
            }
            return Result.success(scenic);
        } catch (Exception e) {
            log.error("根据景点ID查询景点信息时发生异常", e);
            return Result.error("根据景点ID查询景点信息失败，请稍后重试");
        }
    }


}
