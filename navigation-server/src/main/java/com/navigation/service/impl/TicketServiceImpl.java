package com.navigation.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.navigation.entity.Ticket;
import com.navigation.mapper.TicketMapper;
import com.navigation.result.PageResult;
import com.navigation.result.Result;
import com.navigation.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.OptimisticLockException;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Ticket> implements TicketService {


    @Resource
    private TicketMapper ticketMapper;

    @Autowired
    private Validator validator;

    @Override
    public Result<Void> saveTicket(Ticket ticket) {
        // 参数校验
        if (ticket == null) {
            log.error("传入的 Ticket 对象为空，无法保存门票信息");
            return Result.error("传入的门票信息为空");
        }

        // 检查景点id是否为空
        if (ticket.getScenicSpotId() == null) {
            log.error("传入的景点id为空，无法保存门票信息");
            return Result.error("传入的景点id为空");
        }

        // 获取票中的景点id
        Integer scenicId = ticket.getScenicSpotId();
        // 判断景点id是否存在
        int count = ticketMapper.countScenicById(scenicId);
        if (count == 0) {
            log.error("票对应的景点id: {} 不存在", scenicId);
            return Result.error("景点Id不存在");
        }
        // 使用Validator校验Ticket对象的其他字段
        Set<ConstraintViolation<Ticket>> violations = validator.validate(ticket);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("以下必填参数未传入: ");
            for (ConstraintViolation<Ticket> violation : violations) {
                errorMessage.append(violation.getMessage()).append("; ");
            }
            log.error(errorMessage.toString());
            return Result.error(errorMessage.toString());
        }

        try {
            ticket.setVersion(String.valueOf(1));
            ticket.setCreateTime(LocalDateTime.now());
            ticket.setUpdateTime(LocalDateTime.now());
            ticketMapper.saveTicket(ticket);
            return Result.success();
        } catch (Exception e) {
            log.error("保存门票信息时出现异常", e);
            return Result.error("保存门票信息失败，请稍后重试");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // 添加事务管理，确保原子性
    public Result<Void> update(Ticket ticket) {
        if (ticket == null) {
            log.error("【更新门票】传入的Ticket对象为空");
            return Result.error("参数错误：门票信息不能为空");
        }

        // 必须校验ID存在性
        if (ticket.getId() == null) {
            return Result.error("参数错误：门票ID不能为空");
        }

        try {
            // 查询当前版本号（建议用selectForUpdate加锁，但需根据业务权衡）
            Ticket originalTicket = ticketMapper.queryTicketById(ticket.getId());
            if (originalTicket == null) {
                log.error("【更新门票】原始门票不存在");
                return Result.error("门票不存在");
            }

            // 设置乐观锁版本号（必须与数据库当前值一致）
            ticket.setVersion(originalTicket.getVersion());
            // 强制更新时间戳（避免依赖前端传递）
            ticket.setUpdateTime(LocalDateTime.now());

            // 执行更新并检查影响行数
            int updatedRows = ticketMapper.update(ticket);
            if (updatedRows == 0) {
                // 更明确的异常类型，便于上层捕获处理
                log.warn("【更新门票】并发冲突更新失败");
                throw new OptimisticLockException("并发冲突，更新失败，请重试");
            }

            log.debug("【更新门票】更新成功");
            return Result.success();
        } catch (OptimisticLockException e) {
            // 明确抛出乐观锁异常，供全局异常处理器处理
            throw e;
        } catch (Exception e) {
            log.error("【更新门票】系统异常 ");
            throw new RuntimeException("系统繁忙，请稍后重试");
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
            List<Integer> allExistingIds = ticketMapper.getAllExistingIds();
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
            int rows = ticketMapper.batchDelete(ids);
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
    public Result<Ticket> queryByScenicId(Integer id) {
        if (id == null) {
            log.error("传入的景点ID为空");
            return Result.error("景点id为空！");
        }

        try {
            Ticket ticket = ticketMapper.queryTicketById(id);
            if (ticket == null) {
                log.error("景点ID为 {} 的门票记录不存在", id);
                return Result.error("景点id不存在！");
            }
            return Result.success(ticket);
        } catch (Exception e) {
            log.error("根据景点ID查询门票信息时发生异常", e);
            return Result.error("根据景点ID查询门票信息失败，请稍后重试");
        }
    }


}
