package com.navigation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.navigation.entity.Ticket;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
}
