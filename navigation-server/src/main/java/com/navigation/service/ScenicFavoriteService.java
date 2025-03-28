package com.navigation.service;

import com.navigation.entity.ScenicFavorite;
import com.navigation.result.Result;

import java.util.List;

public interface ScenicFavoriteService {

    Result<Void> save(Integer scenicId);


    Result<Void> cancel(Integer scenicId);


    // 根据用户ID和景点ID查询是否已收藏
    boolean isScenicFavorite(Integer userId, Integer scenicId);


    // 根据用户ID查询该用户的所有收藏景点记录
    Result<List<ScenicFavorite>> getScenicFavoritesByUserId(Integer userId);


    Result<ScenicFavorite> getFavoriteInfoByUserIdAndScenicId(Integer userId, Integer scenicId);


}