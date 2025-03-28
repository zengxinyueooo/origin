package com.navigation.mapper;

import com.navigation.entity.HotelFavorite;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface HotelFavoriteMapper {

    /**
     * 插入酒店收藏记录
     * @param hotelFavorite 酒店收藏实体
     * @return 插入成功返回1，失败返回0
     */
    @Insert("insert into hotel_favorite (status, create_time, update_time, user_id, hotel_id) " +
            "values (#{status}, #{createTime}, #{updateTime}, #{userId}, #{hotelId})")
    int save(HotelFavorite hotelFavorite);

    /**
     * 根据用户ID和酒店ID删除收藏记录
     * @param userId 用户ID
     * @param hotelId 酒店ID
     * @return 删除成功返回1，失败返回0
     */
    @Delete("DELETE FROM hotel_favorite WHERE user_id = #{userId} AND hotel_id = #{hotelId}")
    int deleteByUserIdAndHotelId(@Param("userId") Integer userId, @Param("hotelId") Integer hotelId);

    /**
     * 根据用户ID查询其所有酒店收藏记录
     * @param userId 用户ID
     * @return 酒店收藏记录列表
     */
    @Select("SELECT id, status, create_time, update_time, user_id, hotel_id " +
            "FROM hotel_favorite " +
            "WHERE user_id = #{userId}")
    List<HotelFavorite> selectByUserId(@Param("userId") Integer userId);

    /**
     * 根据用户ID和酒店ID查询收藏记录
     * @param userId 用户ID
     * @param hotelId 酒店ID
     * @return 酒店收藏记录，若不存在则返回null
     */
    @Select("SELECT id, status, create_time, update_time, user_id, hotel_id " +
            "FROM hotel_favorite " +
            "WHERE user_id = #{userId} AND hotel_id = #{hotelId}")
    HotelFavorite selectByUserIdAndHotelId(@Param("userId") Integer userId, @Param("hotelId") Integer hotelId);

    /**
     * 根据用户ID统计用户数量，用于检查用户是否存在
     * @param userId 用户ID
     * @return 用户数量，若为0表示用户不存在
     */
    @Select("SELECT COUNT(*) FROM user WHERE user_id = #{userId}")
    int countUserById(@Param("userId") Integer userId);

    /**
     * 根据酒店ID统计酒店数量，用于检查酒店是否存在
     * @param hotelId 酒店ID
     * @return 酒店数量，若为0表示酒店不存在
     */
    @Select("SELECT COUNT(*) FROM hotel WHERE id = #{hotelId}")
    int countHotelById(@Param("hotelId") Integer hotelId);
}
