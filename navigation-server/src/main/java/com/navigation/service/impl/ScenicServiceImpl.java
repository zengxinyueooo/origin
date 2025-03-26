package com.navigation.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.navigation.entity.Scenic;
import com.navigation.mapper.ScenicMapper;
import com.navigation.result.PageResult;
import com.navigation.result.Result;
import com.navigation.service.ScenicService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScenicServiceImpl extends ServiceImpl<ScenicMapper, Scenic> implements ScenicService {


    @Resource
    private ScenicMapper scenicMapper;


    @Override
    public Result<Void> saveScenic(Scenic scenic) {
        // 参数校验
        if (scenic == null) {
            log.error("传入的 Scenic 对象为空，无法保存景点信息");
            return Result.error("传入的景点信息为空");
        }
        try {
            scenic.setScenicStatus(1);
            scenic.setCreateTime(LocalDateTime.now());
            scenic.setUpdateTime(LocalDateTime.now());
            scenicMapper.saveScenic(scenic);
            return Result.success();
        } catch (Exception e) {
            log.error("保存景点信息时出现异常", e);
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
            scenic.setUpdateTime(LocalDateTime.now());
            scenicMapper.update(scenic);
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
        int rows = scenicMapper.batchDelete(ids);
        if (rows > 0) {
            return Result.success();
        } else {
            return Result.error("删除失败");
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
    public Scenic queryScenicById(Integer id) {
        if(id == null){
            log.error("传入的景点ID为空");
            return null;
        }
        return scenicMapper.queryScenicById(id);
    }


}
