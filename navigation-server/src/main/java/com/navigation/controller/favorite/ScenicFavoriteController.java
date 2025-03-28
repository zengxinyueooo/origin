package com.navigation.controller.favorite;

import com.navigation.entity.ScenicFavorite;
import com.navigation.result.Result;
import com.navigation.service.ScenicFavoriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/scenic/favorite")
public class ScenicFavoriteController {

    @Resource
    private ScenicFavoriteService scenicFavoriteService;


    @PostMapping("/save/{scenicId}")
    public Result<Void> save( @PathVariable Integer scenicId) {
        return scenicFavoriteService.save(scenicId);
    }


    @PostMapping("/cancel/{scenicId}")
    public Result<Void> cancelFavoriteScenic(@PathVariable Integer scenicId) {
        return scenicFavoriteService.cancel(scenicId);
    }

    // 根据用户ID和景点ID查询是否已收藏
    @GetMapping("/isFavorite/{userId}/{scenicId}")
    public boolean isScenicFavorite(@PathVariable Integer userId, @PathVariable Integer scenicId) {
        return scenicFavoriteService.isScenicFavorite(userId, scenicId);
    }

    // 根据用户ID和景点ID查询该景点收藏信息
    @GetMapping("/info/{userId}/{scenicId}")
    public Result<ScenicFavorite> getFavoriteInfo(
            @PathVariable Integer userId,
            @PathVariable Integer scenicId) {
        return scenicFavoriteService.getFavoriteInfoByUserIdAndScenicId(userId, scenicId);
    }

    // 根据用户ID查询该用户的所有收藏景点记录
    @GetMapping("/user/{userId}")
    public Result<List<ScenicFavorite>> getScenicFavoritesByUserId(@PathVariable Integer userId) {
        return scenicFavoriteService.getScenicFavoritesByUserId(userId);
    }
}