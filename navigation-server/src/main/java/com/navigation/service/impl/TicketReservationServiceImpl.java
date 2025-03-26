package com.navigation.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.navigation.entity.TicketReservation;
import com.navigation.mapper.TicketReservationMapper;
import com.navigation.result.PageResult;
import com.navigation.result.Result;
import com.navigation.service.TicketReservationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketReservationServiceImpl extends ServiceImpl<TicketReservationMapper, TicketReservation> implements TicketReservationService {


    @Resource
    private TicketReservationMapper ticketReservationMapper;


    @Override
    public Result<Void> saveTicketReservation(TicketReservation ticketReservation) {
        // 参数校验
        if (ticketReservation == null) {
            log.error("传入的 Ticket 对象为空，无法保存景点预约信息");
            return Result.error("传入的景点预约信息为空");
        }
        int userId = ticketReservation.getUserId();
        // 检查用户是否存在
        /*int count = userMapper.countUserById(userId);
        if (count == 0) {
            log.error("用户ID：" + userId + " 不存在，无法保存景点预订信息");
            return Result.error("用户ID对应的用户不存在，请检查");
        }*/
        try {
            ticketReservation.setCreateTime(LocalDateTime.now());
            ticketReservation.setUpdateTime(LocalDateTime.now());
            ticketReservationMapper.saveTicketReservation(ticketReservation);
            return Result.success();
        } catch (Exception e) {
            log.error("保存景点预约信息时出现异常", e);
            return Result.error("保存景点预约信息失败，请稍后重试");
        }
    }

    @Override
    public Result<Void> update(TicketReservation ticketReservation) {
        if (ticketReservation == null) {
            log.error("传入的 Ticket 对象为空，无法更新景点预约信息");
            return Result.error("传入的景点预约信息为空");
        }
        try {
            ticketReservation.setUpdateTime(LocalDateTime.now());
            ticketReservationMapper.update(ticketReservation);
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
        int rows = ticketReservationMapper.batchDelete(ids);
        if (rows > 0) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }

    @Override
    public PageResult queryTicketReservation(Integer pageNum, Integer pageSize) {
        // 设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        List<TicketReservation> TicketList = ticketReservationMapper.queryTicketReservation(pageNum, pageSize);
        //获取分页元数据（总记录数、总页数等）
        Page list = (Page<TicketReservation>) TicketList;
        PageResult pageResult = new PageResult(list.getTotal(), list.getResult());
        return pageResult;
    }

    @Override
    public TicketReservation queryTicketReservationById(Integer id) {
        if(id == null){
            log.error("传入的景点预约ID为空");
            return null;
        }
        return ticketReservationMapper.queryTicketReservationById(id);
    }

    @Override
    public Result<Void> confirmPurchase(Integer reservationId) {
        try {
            int updatedRows = ticketReservationMapper.updateStatusById(reservationId);
            if(updatedRows > 0){
                return Result.success();
            }
            else{
                return Result.error("支付失败");
            }
        } catch (Exception e) {
            log.error("更新预约状态时出现异常，预约ID：" + reservationId, e);
            return Result.error("更新预约状态时出现异常，预约ID：" + reservationId);
        }
    }


}
