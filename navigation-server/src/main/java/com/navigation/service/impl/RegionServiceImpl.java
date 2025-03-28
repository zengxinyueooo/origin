package com.navigation.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.navigation.entity.Region;


import com.navigation.mapper.RegionMapper;
import com.navigation.result.PageResult;
import com.navigation.result.Result;
import com.navigation.service.RegionService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {

    @Resource
    private RegionMapper regionMapper;


    @Override
    public Result<Void> saveRegion(Region region) {
        // 参数校验
        if (region == null) {
            log.error("传入的 region 对象为空，无法保存地区信息");
            return Result.error("传入的地区信息为空");
        }

        List<String> missingFields = new ArrayList<>();
        // 假设Region类中有name、description等必填字段，这里以name为例
        if (region.getRegionName() == null || region.getRegionName().trim().isEmpty()) {
            missingFields.add("region_name");
        }
        if (region.getRegionDescription() == null || region.getRegionDescription().trim().isEmpty()) {
            missingFields.add("region_description");
        }

        // 可根据实际情况继续添加对其他必填字段的检查，如description等
        if (!missingFields.isEmpty()) {
            String fieldsString = String.join(", ", missingFields);
            return Result.error("以下必填参数未传入: " + fieldsString);
        }

        try {
            region.setCreateTime(LocalDateTime.now());
            region.setUpdateTime(LocalDateTime.now());
            regionMapper.saveRegion(region);
            return Result.success();
        } catch (Exception e) {
            log.error("保存地区信息时出现异常", e);
            return Result.error("保存地区信息失败，请稍后重试");
        }
    }

    @Override
    public Result<Void> update(Region region) {
        if (region == null) {
            log.error("传入的 region 对象为空，无法更新地区信息");
            return Result.error("传入的地区信息为空");
        }

        try {
            // 检查 regionId 是否存在
            Integer regionId = region.getRegionId();
            if (regionId != null) {
                int count = regionMapper.countRegionById(regionId);
                if (count == 0) {
                    log.error("地区记录不存在");
                    return Result.error("地区记录不存在");
                }
            } else {
                log.error("传入的 Region 对象中 regionId 为空");
                return Result.error("传入的 Region 对象中 regionId 为空");
            }

            region.setUpdateTime(LocalDateTime.now());
            regionMapper.update(region);
            return Result.success();
        } catch (Exception e) {
            log.error("更新地区信息时出现异常", e);
            return Result.error("更新地区信息时出现异常: " + e.getMessage());
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
            // 获取数据库中所有存在的地区ID集合
            List<Integer> allExistingIds = regionMapper.getAllExistingRegionIds();
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
                return Result.error("ID为 " + idsString + " 的地区记录不存在，删除失败");
            }

            // 执行批量删除操作
            int rows = regionMapper.batchDelete(ids);
            if (rows > 0) {
                return Result.success();
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            // 记录详细的异常日志，实际应用中建议使用日志框架，如Logback或Log4j
            log.error("批量删除地区过程中出现异常", e);
            // 返回包含具体异常信息的错误结果
            return Result.error("删除过程中出现异常: " + e.getMessage());
        }
    }

    @Override
    public PageResult queryRegion(Integer pageNum, Integer pageSize) {
        // 设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        List<Region> regionList = regionMapper.queryRegion(pageNum, pageSize);
        //获取分页元数据（总记录数、总页数等）
        Page list = (Page<Region>) regionList;
        PageResult pageResult = new PageResult(list.getTotal(), list.getResult());
        return pageResult;
    }

    @Override
    public Result<Region> queryRegionById(Integer id) {
        if (id == null) {
            log.error("传入的id为空");
            // 返回错误结果，而不是null
            return Result.error("传入的id为空");
        }

        int num = regionMapper.countRegionById(id);
        if(num == 0){
            return Result.error("地区Id不存在");
        }
        try {
            Region region = regionMapper.queryRegionById(id);
            return Result.success(region);
        } catch (Exception e) {
            log.error("查询地区信息时出现异常", e);
            // 返回包含异常信息的错误结果
            return Result.error("查询地区信息时出现异常: " + e.getMessage());
        }
    }

}
