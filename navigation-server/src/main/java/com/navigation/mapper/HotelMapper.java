package com.navigation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.navigation.entity.Hotel;
import com.navigation.entity.Hotel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HotelMapper extends BaseMapper<Hotel> {

    @Insert("insert into hotel(cover, address, hotel_name, hotel_description, phone_number, create_time, update_time) " +
            "values(#{cover}, #{address}, #{hotelName}, #{hotelDescription}, #{phoneNumber}, #{createTime}, #{updateTime})")
    void saveHotel(Hotel hotel);


    @Select("select * from hotel where id = #{id}")
    Hotel queryHotelById(Integer id);


    void update(Hotel hotel);


    int batchDelete(@Param("ids") List<Integer> ids);


    @Select("select * from hotel ")
    List<Hotel> queryHotel(Integer pageNum, Integer pageSize);
}
