package com.navigation.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.navigation.context.BaseContext;
import com.navigation.entity.Scenic;
import com.navigation.entity.ScenicFavorite;
import com.navigation.mapper.ScenicFavoriteMapper;
import com.navigation.mapper.ScenicMapper;
import com.navigation.result.Result;
import com.navigation.service.ScenicFavoriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@Slf4j
public class ScenicFavoriteServiceImpl implements ScenicFavoriteService {

    @Resource
    private ScenicFavoriteMapper scenicFavoriteMapper;


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result<Void> save(Integer scenicId) {
        // 手动设置用户ID为1
        //Integer userId = 2;
        // 从BaseContext获取当前用户ID
        Integer userId = BaseContext.getUserId();

        // 参数非空判断
        if (userId == null || scenicId == null) {
            log.error("用户ID或景点ID不能为空");
            return Result.error("用户ID或景点ID不能为空");
        }

        // 从Redis中检查scenicId是否存在，并获取正确的键
        String keyPattern = "scenic:" + scenicId + ":*";
        Set<String> keys = stringRedisTemplate.keys(keyPattern);
        if (keys == null || keys.isEmpty()) {
            log.error("在Redis中未找到景点ID为 {} 的记录", scenicId);
            return Result.error("景点ID不存在");
        }
        // 获取实际存储的键名（例如：scenic:1:西湖）
        String actualRedisKey = keys.iterator().next();

        // 检查用户是否存在
        int userCount = scenicFavoriteMapper.countUserById(userId);
        if (userCount == 0) {
            log.error("用户ID不存在");
            return Result.error("用户ID不存在");
        }

        ScenicFavorite scenicFavorite = new ScenicFavorite();
        scenicFavorite.setUserId(userId);
        scenicFavorite.setScenicId(scenicId);
        scenicFavorite.setStatus(1);
        scenicFavorite.setCreateTime(LocalDateTime.now());
        scenicFavorite.setUpdateTime(LocalDateTime.now());

        try {
            // 使用正确的键获取景点信息
            String scenicJson = stringRedisTemplate.opsForValue().get(actualRedisKey);
            if (scenicJson != null) {
                JSONObject scenicObj = JSON.parseObject(scenicJson);
                log.info("获取到Redis中景点信息: {}", scenicObj);
                String currentFavoriteIds = scenicObj.getString("scenicFavoriteIds");
                String userIdStr = userId.toString();

                // 处理收藏用户ID列表
                if (currentFavoriteIds == null || currentFavoriteIds.isEmpty()) {
                    scenicObj.put("scenicFavoriteIds", userIdStr);
                } else {
                    // 使用Set避免重复，并转换为逗号分隔的字符串
                    Set<String> favoriteIds = new HashSet<>(Arrays.asList(currentFavoriteIds.split(",")));
                    if (!favoriteIds.contains(userIdStr)) {
                        favoriteIds.add(userIdStr);
                        String updatedIds = String.join(",", favoriteIds);
                        scenicObj.put("scenicFavoriteIds", updatedIds);
                    }
                }

                // 保存回Redis
                stringRedisTemplate.opsForValue().set(actualRedisKey, scenicObj.toJSONString());
                log.info("用户ID: {} 已添加到景点ID: {} 的收藏列表", userId, scenicId);
            }

            // 数据库操作
            Integer num = scenicFavoriteMapper.save(scenicFavorite);
            if (num == 0) {
                log.error("收藏失败，请稍后重试");
                return Result.error("收藏失败，请稍后重试");
            }
            return Result.success();
        } catch (DataIntegrityViolationException e) {
            log.warn("用户 {} 已收藏景点 {}", userId, scenicId, e);
            return Result.error("您已收藏过该景点");
        } catch (Exception e) {
            log.error("保存景点收藏信息时发生异常", e);
            return Result.error("保存景点收藏信息失败，请稍后重试");
        }
    }


    @Override
    public Result<Void> cancel(Integer scenicId) {
        // 手动设置用户ID为1
        //Integer userId = 1;
        // 从BaseContext获取当前用户ID
        Integer userId = BaseContext.getUserId();

        // 参数非空判断
        if (userId == null || scenicId == null) {
            log.error("用户ID或景点ID不能为空");
            return Result.error("用户ID或景点ID不能为空");
        }

        // 检查用户是否存在
        int userCount = scenicFavoriteMapper.countUserById(userId);
        if (userCount == 0) {
            log.error("用户ID不存在");
            return Result.error("用户ID不存在");
        }

        // 从Redis中检查scenicId是否存在，并获取正确的键
        String keyPattern = "scenic:" + scenicId + ":*";
        Set<String> keys = stringRedisTemplate.keys(keyPattern);
        if (keys == null || keys.isEmpty()) {
            log.error("在Redis中未找到景点ID为 {} 的记录", scenicId);
            return Result.error("景点ID不存在");
        }
        String actualRedisKey = keys.iterator().next(); // 获取实际存储的键名

        try {
            // 先执行数据库删除操作
            int result = scenicFavoriteMapper.deleteByUserIdAndScenicId(userId, scenicId);
            if (result > 0) {
                // 数据库删除成功后再处理Redis
                String scenicJson = stringRedisTemplate.opsForValue().get(actualRedisKey);
                if (scenicJson != null) {
                    JSONObject scenicObj = JSON.parseObject(scenicJson);
                    String currentFavoriteIds = scenicObj.getString("scenicFavoriteIds");
                    String userIdStr = userId.toString();

                    if (currentFavoriteIds != null && !currentFavoriteIds.isEmpty()) {
                        // 使用Set处理用户ID列表
                        Set<String> favoriteIds = new HashSet<>(Arrays.asList(currentFavoriteIds.split(",")));
                        if (favoriteIds.remove(userIdStr)) {
                            // 更新后的ID列表
                            String updatedIds = String.join(",", favoriteIds);

                            if (updatedIds.isEmpty()) {
                                scenicObj.remove("scenicFavoriteIds"); // 如果为空则移除字段
                            } else {
                                scenicObj.put("scenicFavoriteIds", updatedIds);
                            }

                            // 保存修改后的数据到Redis
                            stringRedisTemplate.opsForValue().set(actualRedisKey, scenicObj.toJSONString());
                            log.info("已从景点ID: {} 的收藏列表中移除用户ID: {}", scenicId, userId);
                        }
                    }
                }
                return Result.success();
            }
            log.warn("未找到用户ID: {} 对景点ID: {} 的收藏记录", userId, scenicId);
            return Result.error("取消收藏失败，未找到对应记录");
        } catch (Exception e) {
            log.error("取消收藏时发生异常", e);
            // 可以根据需要添加事务回滚逻辑
            return Result.error("取消收藏失败，请稍后重试");
        }
    }

    @Override
    public boolean isScenicFavorite(Integer userId, Integer scenicId) {
        // 参数非空判断
        if (userId == null || scenicId == null) {
            log.error("用户ID或景点ID为空，无法判断是否收藏");
            return false;
        }

        // 检查用户是否存在
        int userCount = scenicFavoriteMapper.countUserById(userId);
        if (userCount == 0) {
            log.error("用户ID: {} 不存在，无法判断是否收藏", userId);
            return false;
        }

        // 从Redis中检查scenicId是否存在
        Set<String> keys = stringRedisTemplate.keys("scenic:" + scenicId + ":*");
        if (keys.isEmpty()) {
            log.error("在Redis中未找到景点ID为 {} 的记录", scenicId);
            return false;
        }

        try {
            // 调用Mapper方法查询是否存在收藏记录
            ScenicFavorite favorite = scenicFavoriteMapper.selectByUserIdAndScenicId(userId, scenicId);
            boolean isFavorite = favorite != null;
            if (isFavorite) {
                log.info("用户ID: {} 已收藏景点ID: {}", userId, scenicId);
            } else {
                log.info("用户ID: {} 未收藏景点ID: {}", userId, scenicId);
            }
            return isFavorite;
        } catch (Exception e) {
            log.error("判断用户是否收藏景点时发生异常", e);
            return false;
        }
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

        // 从Redis中检查scenicId是否存在
        Set<String> keys = stringRedisTemplate.keys("scenic:" + scenicId + ":*");
        if (keys.isEmpty()) {
            log.error("在Redis中未找到景点ID为 {} 的记录", scenicId);
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