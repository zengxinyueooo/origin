package com.navigation.service;

import com.navigation.entity.HotelFavorite;
import com.navigation.result.Result;

import java.util.List;

public interface HotelFavoriteService {

    /**
     * 保存酒店收藏
     * @param hotelId 酒店ID
     * @return 操作结果
     */
    Result<Void> save( Integer hotelId);

    /**
     * 取消酒店收藏
     * @param hotelId 酒店ID
     * @return 操作结果
     */
    Result<Void> cancel(Integer hotelId);

    /**
     * 根据用户ID查询酒店收藏列表
     * @param userId 用户ID
     * @return 酒店收藏记录列表结果
     */
    Result<List<HotelFavorite>> getHotelFavoritesByUserId(Integer userId);

    /**
     * 检查用户是否收藏了某酒店
     * @param userId 用户ID
     * @param hotelId 酒店ID
     * @return 若已收藏返回true，否则返回false
     */
    boolean isHotelFavorite(Integer userId, Integer hotelId);


    /**
     * 根据用户ID和酒店ID查询酒店收藏信息
     * @param userId 用户ID
     * @param hotelId 酒店ID
     * @return 包含酒店收藏信息的Result对象
     */
    Result<HotelFavorite> getHotelFavoriteInfoByUserIdAndHotelId(Integer userId, Integer hotelId);
}