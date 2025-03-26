package com.navigation.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.navigation.entity.Region;


import com.navigation.mapper.RegionMapper;
import com.navigation.result.PageResult;
import com.navigation.result.Result;
import com.navigation.service.RegionService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
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
            log.error ("传入的 region 对象为空，无法更新地区信息");
            return Result.error("传入的地区信息为空");
        }
        try {
            region.setUpdateTime(LocalDateTime.now());
            regionMapper.update(region);
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
        int rows = regionMapper.batchDelete(ids);
        if (rows > 0) {
            return Result.success();
        } else {
            return Result.error("删除失败");
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
    public Region queryRegionById(Integer id) {
        if(id == null){
            log.error("传入的id为空");
            return null;
        }
        return regionMapper.queryRegionById(id);
    }

}
