package com.navigation.service.impl;

import com.navigation.context.BaseContext;
import com.navigation.entity.ScenicFavorite;
import com.navigation.mapper.ScenicFavoriteMapper;
import com.navigation.result.Result;
import com.navigation.service.ScenicFavoriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
public class ScenicFavoriteServiceImpl implements ScenicFavoriteService {

    @Resource
    private ScenicFavoriteMapper scenicFavoriteMapper;


    @Override
    public Result<Void> save( Integer scenicId) {
        // 从BaseContext获取当前用户ID
        Integer userId = BaseContext.getUserId();
        // 参数非空判断
        if (userId == null || scenicId == null) {
            return Result.error("用户ID或景点ID不能为空");
        }

        // 检查用户是否存在
        int userCount = scenicFavoriteMapper.countUserById(userId);
        if (userCount == 0) {
            return Result.error("用户ID不存在");
        }

        // 检查景点是否存在
        int scenicCount = scenicFavoriteMapper.countScenicById(scenicId);
        if (scenicCount == 0) {
            return Result.error("景点ID不存在");
        }

        ScenicFavorite scenicFavorite = new ScenicFavorite();
        scenicFavorite.setUserId(userId);
        scenicFavorite.setScenicId(scenicId);
        scenicFavorite.setStatus(1);
        scenicFavorite.setCreateTime(LocalDateTime.now());
        scenicFavorite.setUpdateTime(LocalDateTime.now());

        try {
            Integer num = scenicFavoriteMapper.save(scenicFavorite);
            if (num == 0) {
                return Result.error("收藏失败，请稍后重试");
            }
            return Result.success();
        } catch (DataIntegrityViolationException e) {
            // 捕获因为唯一约束导致的异常，说明已经收藏过
            log.warn("用户 {} 已收藏景点 {}", userId, scenicId, e);
            return Result.success(); // 直接返回成功
        } catch (Exception e) {
            log.error("保存景点收藏信息时发生异常", e);
            return Result.error("保存景点收藏信息失败，请稍后重试");
        }
    }

    public Result<Void> cancel(Integer scenicId) {
        // 从BaseContext获取当前用户ID
        Integer userId = BaseContext.getUserId();
        // 参数非空判断
        if (userId == null || scenicId == null) {
            return Result.error("用户ID或景点ID不能为空");
        }

        // 检查用户是否存在
        int userCount = scenicFavoriteMapper.countUserById(userId);
        if (userCount == 0) {
            return Result.error("用户ID不存在");
        }

        // 检查景点是否存在
        int scenicCount = scenicFavoriteMapper.countScenicById(scenicId);
        if (scenicCount == 0) {
            return Result.error("景点ID不存在");
        }
        try {
            int result = scenicFavoriteMapper.deleteByUserIdAndScenicId(userId, scenicId);
            if (result > 0) {
                return Result.success(); // 删除成功
            }
            return Result.error("取消收藏失败，请稍后重试"); // 未找到对应收藏记录，删除失败
        } catch (Exception e) {
            log.error("取消收藏时发生异常", e);
            return Result.error("取消收藏时出现异常，请稍后重试");
        }
    }

    @Override
    public boolean isScenicFavorite(Integer userId, Integer scenicId) {
        // 参数非空判断
        if (userId == null || scenicId == null) {
            return false;
        }

        // 检查用户是否存在
        int userCount = scenicFavoriteMapper.countUserById(userId);
        if (userCount == 0) {
            return false;
        }

        // 检查景点是否存在
        int scenicCount = scenicFavoriteMapper.countScenicById(scenicId);
        if (scenicCount == 0) {
            return false;
        }
        // 调用Mapper方法查询是否存在收藏记录
        return scenicFavoriteMapper.selectByUserIdAndScenicId(userId, scenicId) != null;
    }

    @Override
    public Result<List<ScenicFavorite>> getScenicFavoritesByUserId(Integer userId) {
        // 参数非空判断
        if (userId == null) {
            return Result.error("用户ID不能为空"); // 假设1表示参数错误状态码
        }

        // 检查用户是否存在
        int userCount = scenicFavoriteMapper.countUserById(userId);
        if (userCount == 0) {
            return Result.error( "用户ID不存在"); // 假设2表示用户不存在状态码
        }

        try {
            List<ScenicFavorite> favorites = scenicFavoriteMapper.selectByUserId(userId);
            return Result.success(favorites);
        } catch (Exception e) {
            // 记录日志，方便排查问题
            e.printStackTrace();
            return Result.error( "查询景点收藏信息失败，请稍后重试"); // 假设3表示查询失败状态码
        }
    }


    @Override
    public Result<ScenicFavorite> getFavoriteInfoByUserIdAndScenicId(Integer userId, Integer scenicId) {
        // 参数非空判断
        if (userId == null || scenicId == null) {
            return Result.error("用户ID或景点ID不能为空");
        }

        // 检查用户是否存在
        int userCount = scenicFavoriteMapper.countUserById(userId);
        if (userCount == 0) {
            return Result.error("用户ID不存在");
        }

        // 检查景点是否存在
        int scenicCount = scenicFavoriteMapper.countScenicById(scenicId);
        if (scenicCount == 0) {
            return Result.error("景点ID不存在");
        }
        try {
            ScenicFavorite favorite = scenicFavoriteMapper.selectByUserIdAndScenicId(userId, scenicId);
            if (favorite != null) {
                return Result.success(favorite);
            }
            return Result.error("未找到对应的收藏信息");
        } catch (Exception e) {
            log.error("查询收藏信息时发生异常", e);
            return Result.error("查询收藏信息失败，请稍后重试");
        }
    }
}