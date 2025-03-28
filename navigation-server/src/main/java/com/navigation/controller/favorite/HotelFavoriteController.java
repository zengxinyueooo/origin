package com.navigation.controller.favorite;

import com.navigation.entity.HotelFavorite;
import com.navigation.result.Result;
import com.navigation.service.HotelFavoriteService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/hotel/favorite")
public class HotelFavoriteController {

    @Resource
    private HotelFavoriteService hotelFavoriteService;


    /**
     * 保存酒店收藏
     * @param hotelId 酒店ID
     * @return 操作结果
     */
    @PostMapping("/save/{hotelId}")
    public Result<Void> save(@PathVariable Integer hotelId) {
        return hotelFavoriteService.save(hotelId);
    }

    /**
     * 取消酒店收藏
     * @param hotelId 酒店ID
     * @return 操作结果
     */
    @PostMapping("/cancel/{hotelId}")
    public Result<Void> cancel(@PathVariable Integer hotelId) {
        return hotelFavoriteService.cancel(hotelId);
    }

    /**
     * 根据用户ID查询酒店收藏列表
     * @param userId 用户ID
     * @return 酒店收藏记录列表结果
     */
    @GetMapping("/user/{userId}")
    public Result<List<HotelFavorite>> getHotelFavoritesByUserId(@PathVariable Integer userId) {
        return hotelFavoriteService.getHotelFavoritesByUserId(userId);
    }

    /**
     * 根据用户ID和酒店ID查询是否已收藏
     * @param userId 用户ID
     * @param hotelId 酒店ID
     * @return true表示已收藏，false表示未收藏
     */
    @GetMapping("/isFavorite/{userId}/{hotelId}")
    public boolean isHotelFavorite(@PathVariable Integer userId, @PathVariable Integer hotelId) {
        return hotelFavoriteService.isHotelFavorite(userId, hotelId);
    }

    /**
     * 根据用户ID和酒店ID查询该酒店收藏信息
     * @param userId 用户ID
     * @param hotelId 酒店ID
     * @return 包含酒店收藏信息的Result对象
     */
    @GetMapping("/info/{userId}/{hotelId}")
    public Result<HotelFavorite> getHotelFavoriteInfo(
            @PathVariable Integer userId,
            @PathVariable Integer hotelId) {
        return hotelFavoriteService.getHotelFavoriteInfoByUserIdAndHotelId(userId, hotelId);
    }
}