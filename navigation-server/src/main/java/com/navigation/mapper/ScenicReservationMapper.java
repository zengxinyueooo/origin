package com.navigation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.navigation.entity.Scenic;
import com.navigation.entity.ScenicReservation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ScenicReservationMapper extends BaseMapper<ScenicReservation> {


    @Insert("insert into scenic_reservation(user_id, scenic_id, people_count, is_congested, " +
            "reservation_date, create_time, update_time) " +
            "values(#{userId}, #{scenicId}, #{peopleCount}, #{isCongested}," +
            "        #{reservationDate}, #{createTime},#{updateTime})")
    void saveScenicReservation(ScenicReservation scenicReservation);


    void update(ScenicReservation scenicReservation);

    int batchDelete(@Param("ids") List<Integer> ids);


    @Select("select * from scenic_reservation ")
    List<ScenicReservation> queryScenicReservation(Integer pageNum, Integer pageSize);

    @Select("select * from scenic_reservation where reservation_id = #{reservationId}")
    ScenicReservation queryScenicReservationById(Integer id);
}
