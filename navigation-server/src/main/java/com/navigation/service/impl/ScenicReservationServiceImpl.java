package com.navigation.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.navigation.entity.Scenic;
import com.navigation.entity.ScenicReservation;

import com.navigation.mapper.ScenicReservationMapper;
import com.navigation.result.PageResult;
import com.navigation.result.Result;
import com.navigation.service.ScenicReservationService;
import com.navigation.service.ScenicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScenicReservationServiceImpl extends ServiceImpl<ScenicReservationMapper, ScenicReservation> implements ScenicReservationService {


    @Resource
    private ScenicReservationMapper scenicReservationMapper;


    @Override
    public Result<Void> saveScenicReservation(ScenicReservation scenicReservation) {
        // 参数校验
        if (scenicReservation == null) {
            log.error("传入的 Scenic 对象为空，无法保存景点预约信息");
            return Result.error("传入的景点预约信息为空");
        }
        int userId = scenicReservation.getUserId();
        // 检查用户是否存在
        /*int count = userMapper.countUserById(userId);
        if (count == 0) {
            log.error("用户ID：" + userId + " 不存在，无法保存景点预订信息");
            return Result.error("用户ID对应的用户不存在，请检查");
        }*/
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
            log.error("传入的 Scenic 对象为空，无法更新景点预约信息");
            return Result.error("传入的景点预约信息为空");
        }
        try {
            scenicReservation.setUpdateTime(LocalDateTime.now());
            scenicReservationMapper.update(scenicReservation);
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
        int rows = scenicReservationMapper.batchDelete(ids);
        if (rows > 0) {
            return Result.success();
        } else {
            return Result.error("删除失败");
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
    public ScenicReservation queryScenicReservationById(Integer id) {
        if(id == null){
            log.error("传入的景点预约ID为空");
            return null;
        }
        return scenicReservationMapper.queryScenicReservationById(id);
    }


}
