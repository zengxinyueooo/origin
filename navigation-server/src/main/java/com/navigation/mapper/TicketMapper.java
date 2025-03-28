package com.navigation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.navigation.entity.Ticket;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TicketMapper extends BaseMapper<Ticket> {


    @Insert("insert into ticket(scenic_spot_id, ticket_type, price, availability, " +
            " valid_from, valid_to, version, create_time, update_time) " +
            "values(#{scenicSpotId},#{ticketType},#{price},#{availability},#{validFrom},#{validTo}," +
            "#{version}, #{createTime}, #{updateTime})")
    void saveTicket(Ticket ticket);


    int update(Ticket ticket);

    int batchDelete(@Param("ids") List<Integer> ids);


    @Select("select * from Ticket where scenic_spot_id = #{id}")
    Ticket queryTicketById(Integer id);

    @Update("UPDATE ticket " +
            "SET availability = availability - #{num} " +
            "WHERE id = #{id} " +
            "AND availability > 0 " +
            "AND (availability - #{num}) >= 0")
    int updateTicketAvailability(Integer id, Integer num);

    @Select("SELECT COUNT(1) FROM scenic WHERE id = #{id}")
    int countScenicById(Integer scenicId);

    @Select("SELECT id FROM ticket")
    List<Integer> getAllExistingIds();

    @Select("SELECT COUNT(1) FROM user WHERE user_id = #{userId}")
    int countUserById(Integer userId);

    @Update("UPDATE ticket SET availability = #{availability} WHERE id = #{id}")
    int updateTicketStock(Ticket ticket);

}
