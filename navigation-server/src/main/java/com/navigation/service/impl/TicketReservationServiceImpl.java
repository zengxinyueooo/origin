package com.navigation.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.navigation.context.BaseContext;
import com.navigation.entity.Ticket;
import com.navigation.entity.TicketReservation;
import com.navigation.mapper.TicketMapper;
import com.navigation.mapper.TicketReservationMapper;
import com.navigation.mapper.UserMapper;
import com.navigation.result.PageResult;
import com.navigation.result.Result;
import com.navigation.service.TicketReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TicketReservationServiceImpl extends ServiceImpl<TicketReservationMapper, TicketReservation> implements TicketReservationService {


    @Resource
    private TicketReservationMapper ticketReservationMapper;

    @Resource
    private TicketMapper ticketMapper;

    @Autowired
    private Validator validator;

    @Override
    public Result<Void> saveTicketReservation(TicketReservation ticketReservation) {
        // 参数校验
        if (ticketReservation == null) {
            log.error("传入的 TicketReservation 对象为空，无法保存景点预约信息");
            return Result.error("传入的景点预约信息为空");
        }

        // 从UserHolder获取userId并设置到ticketReservation对象
        Integer userId = BaseContext.getUserId();
        ticketReservation.setUserId(userId);

        // 检查用户是否存在
        int count = ticketMapper.countUserById(userId);
        if (count == 0) {
            log.error("用户ID: {} 不存在，无法保存景点预订信息", userId);
            return Result.error("用户ID对应的用户不存在，请检查");
        }

        // 检查ticketId是否存在
        Integer ticketId = ticketReservation.getTicketId();
        if (ticketId == null) {
            log.error("门票ID为空，无法获取门票单价计算总价");
            return Result.error("门票ID不能为空");
        }
        Ticket ticket = ticketMapper.queryTicketById(ticketId);
        if (ticket == null) {
            log.error("未找到ID为 {} 的门票信息，无法计算总价", ticketId);
            return Result.error("未找到对应的门票信息");
        }

        // 检查购买数量是否为空
        Integer buyNumber = ticketReservation.getQuantity();
        if (buyNumber == null) {
            log.error("购买数量为空，无法计算总价");
            return Result.error("购买数量不能为空");
        }

        // 计算总价
        BigDecimal ticketPrice = ticket.getPrice();
        BigDecimal totalPrice = ticketPrice.multiply(new BigDecimal(buyNumber));

        // 获取门票库存
        int stock = ticket.getAvailability();
        if (buyNumber > stock) {
            log.error("购买数量 {} 超过门票库存 {}", buyNumber, stock);
            return Result.error("购买数量超过门票库存");
        }

        // 使用Validator校验除userId和ticketId外的其他字段
        Set<ConstraintViolation<TicketReservation>> violations = validator.validate(ticketReservation);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("以下字段校验不通过: ");
            for (ConstraintViolation<TicketReservation> violation : violations) {
                errorMessage.append(violation.getPropertyPath()).append(" - ").append(violation.getMessage()).append("; ");
            }
            log.error(errorMessage.toString());
            return Result.error(errorMessage.toString());
        }

        try {
            ticketReservation.setTotalPrice(totalPrice);
            ticketReservation.setStatus(0);
            ticketReservation.setCreateTime(LocalDateTime.now());
            ticketReservation.setUpdateTime(LocalDateTime.now());
            ticketReservationMapper.saveTicketReservation(ticketReservation);

            // 更新门票库存
            ticket.setAvailability(stock - buyNumber);
            ticketMapper.updateTicketStock(ticket);

            return Result.success();
        } catch (Exception e) {
            log.error("保存景点预约信息时出现异常", e);
            return Result.error("保存景点预约信息失败，请稍后重试");
        }
    }

    @Override
    public Result<Void> update(TicketReservation ticketReservation) {
        if (ticketReservation == null) {
            log.error("传入的 TicketReservation 对象为空，无法更新景点预约信息");
            return Result.error("传入的景点预约信息为空");
        }

        try {
            // 从BaseContext获取当前用户ID
            Integer userId = BaseContext.getUserId();
            if (userId == null) {
                log.error("无法获取当前用户ID，更新操作失败");
                return Result.error("无法获取当前用户ID，请检查");
            }

            // 获取传入的门票ID
            Integer ticketId = ticketReservation.getTicketId();
            if (ticketId == null) {
                log.error("传入的门票ID为空，无法定位预约订单");
                return Result.error("传入的门票ID为空，请检查");
            }

            // 检查该用户对该门票是否已存在预约记录
            boolean isExist = ticketReservationMapper.existsByUserIdAndTicketId(userId, ticketId);
            if (isExist) {
                log.error("用户ID: {} 已存在对门票ID: {} 的预约记录，不允许重复预约", userId, ticketId);
                return Result.error("您已预约过该门票，不允许重复预约");
            }

            // 获取原预订数量和当前预订数量
            TicketReservation originalTicketReservation = ticketReservationMapper.queryTicketReservationById(ticketReservation.getTicketId());
            Integer originalQuantity = originalTicketReservation.getQuantity();
            Integer currentQuantity = ticketReservation.getQuantity();

            if (!originalQuantity.equals(currentQuantity)) {
                // 预订数量改变，重新计算总价
                Ticket ticket = ticketMapper.queryTicketById(ticketId);
                BigDecimal ticketPrice = ticket.getPrice();
                BigDecimal totalPrice = ticketPrice.multiply(new BigDecimal(currentQuantity));
                ticketReservation.setTotalPrice(totalPrice);
            }

            ticketReservation.setUpdateTime(LocalDateTime.now());
            ticketReservationMapper.update(ticketReservation);
            return Result.success();
        } catch (Exception e) {
            log.error("更新景点预约信息时出现异常", e);
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
            List<Integer> allExistingIds = ticketReservationMapper.getAllExistingIds();
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
            int rows = ticketReservationMapper.batchDelete(ids);
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
    public Result<TicketReservation> queryTicketReservationById(Integer id) {
        if (id == null) {
            log.error("传入的景点预约ID为空");
            return Result.error("id为空！");
        }

        try {
            TicketReservation ticketReservation = ticketReservationMapper.queryTicketReservationById(id);
            if (ticketReservation == null) {
                log.error("景点预约id为 {} 的记录不存在", id);
                return Result.error("id不存在！");
            }
            return Result.success(ticketReservation);
        } catch (Exception e) {
            log.error("根据id查询景点预约信息时发生异常", e);
            return Result.error("根据id查询景点预约信息失败，请稍后重试");
        }
    }

    @Override
    public Result<Void> confirmPurchase(Integer reservationId) {
        if (reservationId == null) {
            log.error("传入的预订ID为空");
            return Result.error("预订ID不能为空");
        }
        try {
            // 检查预订ID是否存在
            TicketReservation existingReservation = ticketReservationMapper.queryTicketReservationById(reservationId);
            if (existingReservation == null) {
                log.error("预订ID为 {} 的记录不存在", reservationId);
                return Result.error("预订ID对应的记录不存在");
            }
            int updatedRows = ticketReservationMapper.updateStatusById(reservationId);
            if(updatedRows > 0){
                //门票预约Id-->门票Id-->门票数量减量
                TicketReservation ticketReservation = ticketReservationMapper.queryTicketReservationById(reservationId);
                Integer ticketId = ticketReservation.getTicketId();
                if (ticketId == null) {
                    log.error("门票ID为空，无法获取门票单价计算总价");
                    return Result.error("门票ID不能为空");
                }
                Ticket ticket = ticketMapper.queryTicketById(ticketId); // 根据ID查询门票信息
                if (ticket == null) {
                    return Result.error("未找到对应的门票信息");
                }
                Integer buyNumber = ticketReservation.getQuantity();
                Integer num = ticketMapper.updateTicketAvailability(ticketId, buyNumber); //减少票的数量
                if(num == 0){
                    return Result.error("剩余门票数量不足！");
                }
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
