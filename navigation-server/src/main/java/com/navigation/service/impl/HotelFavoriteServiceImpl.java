package com.navigation.service.impl;

import com.navigation.context.BaseContext;
import com.navigation.entity.HotelFavorite;
import com.navigation.mapper.HotelFavoriteMapper;
import com.navigation.result.Result;
import com.navigation.service.HotelFavoriteService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
public class HotelFavoriteServiceImpl implements HotelFavoriteService {

    private final HotelFavoriteMapper hotelFavoriteMapper;

    public HotelFavoriteServiceImpl(HotelFavoriteMapper hotelFavoriteMapper) {
        this.hotelFavoriteMapper = hotelFavoriteMapper;
    }

    @Override
    public Result<Void> save(Integer hotelId) {
        // 从BaseContext获取当前用户ID
        Integer userId = BaseContext.getUserId();
        // 参数非空判断
        if (userId == null || hotelId == null) {
            return Result.error("用户ID或酒店ID不能为空");
        }

        // 检查用户是否存在
        int userCount = hotelFavoriteMapper.countUserById(userId);
        if (userCount == 0) {
            return Result.error("用户ID不存在");
        }

        // 检查酒店是否存在
        int hotelCount = hotelFavoriteMapper.countHotelById(hotelId);
        if (hotelCount == 0) {
            return Result.error("酒店ID不存在");
        }

        HotelFavorite hotelFavorite = new HotelFavorite();
        hotelFavorite.setUserId(userId);
        hotelFavorite.setHotelId(hotelId);
        hotelFavorite.setStatus(1);
        hotelFavorite.setCreateTime(LocalDateTime.now());
        hotelFavorite.setUpdateTime(LocalDateTime.now());

        try {
            int num = hotelFavoriteMapper.save(hotelFavorite);
            if (num == 0) {
                return Result.error("收藏失败，请稍后重试");
            }
            return Result.success();
        } catch (DataIntegrityViolationException e) {
            log.warn("用户 {} 已收藏酒店 {}", userId, hotelId, e);
            return Result.success();
        } catch (Exception e) {
            log.error("保存酒店收藏信息时发生异常", e);
            return Result.error("保存酒店收藏信息失败，请稍后重试");
        }
    }

    @Override
    public Result<Void> cancel( Integer hotelId) {
        // 从BaseContext获取当前用户ID
        Integer userId = BaseContext.getUserId();
        // 参数非空判断
        if (userId == null || hotelId == null) {
            return Result.error("用户ID或酒店ID不能为空");
        }

        // 检查用户是否存在
        int userCount = hotelFavoriteMapper.countUserById(userId);
        if (userCount == 0) {
            return Result.error("用户ID不存在");
        }

        // 检查酒店是否存在
        int hotelCount = hotelFavoriteMapper.countHotelById(hotelId);
        if (hotelCount == 0) {
            return Result.error("酒店ID不存在");
        }
        try {
            int result = hotelFavoriteMapper.deleteByUserIdAndHotelId(userId, hotelId);
            if (result > 0) {
                return Result.success();
            }
            return Result.error("取消收藏失败，请稍后重试");
        } catch (Exception e) {
            log.error("取消酒店收藏时发生异常", e);
            return Result.error("取消酒店收藏时出现异常，请稍后重试");
        }
    }

    @Override
    public Result<List<HotelFavorite>> getHotelFavoritesByUserId(Integer userId) {
        // 参数非空判断
        if (userId == null ) {
            return Result.error("用户ID不能为空");
        }

        // 检查用户是否存在
        int userCount = hotelFavoriteMapper.countUserById(userId);
        if (userCount == 0) {
            return Result.error("用户ID不存在");
        }

        try {
            List<HotelFavorite> favorites = hotelFavoriteMapper.selectByUserId(userId);
            return Result.success(favorites);
        } catch (Exception e) {
            log.error("查询酒店收藏信息时发生异常", e);
            return Result.error("查询酒店收藏信息失败，请稍后重试");
        }
    }

    @Override
    public boolean isHotelFavorite(Integer userId, Integer hotelId) {
        // 参数非空判断
        if (userId == null || hotelId == null) {
            return false;
        }

        // 检查用户是否存在
        int userCount = hotelFavoriteMapper.countUserById(userId);
        if (userCount == 0) {
            return false;
        }

        // 检查酒店是否存在
        int hotelCount = hotelFavoriteMapper.countHotelById(hotelId);
        if (hotelCount == 0) {
            return false;
        }
        return hotelFavoriteMapper.selectByUserIdAndHotelId(userId, hotelId) != null;
    }

    @Override
    public Result<HotelFavorite> getHotelFavoriteInfoByUserIdAndHotelId(Integer userId, Integer hotelId) {
        // 参数非空判断
        if (userId == null || hotelId == null) {
            return Result.error("用户ID或酒店ID不能为空");
        }

        // 检查用户是否存在
        int userCount = hotelFavoriteMapper.countUserById(userId);
        if (userCount == 0) {
            return Result.error("用户ID不存在");
        }

        // 检查酒店是否存在
        int hotelCount = hotelFavoriteMapper.countHotelById(hotelId);
        if (hotelCount == 0) {
            return Result.error("酒店ID不存在");
        }
        try {
            HotelFavorite hotelFavorite = hotelFavoriteMapper.selectByUserIdAndHotelId(userId, hotelId);
            if (hotelFavorite != null) {
                return Result.success(hotelFavorite);
            }
            return Result.error("未找到对应的酒店收藏信息");
        } catch (Exception e) {
            log.error("查询酒店收藏信息时发生异常", e);
            return Result.error("查询酒店收藏信息失败，请稍后重试");
        }
    }
}