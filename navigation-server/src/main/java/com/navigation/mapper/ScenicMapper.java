package com.navigation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.navigation.dto.ScenicQueryDto;
import com.navigation.entity.Scenic;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ScenicMapper extends BaseMapper<Scenic> {


    @Insert("insert into scenic(scenic_name, scenic_cover, location, scenic_description, " +
            "max_capacity, open_start_time, open_end_time, scenic_status, create_time, update_time) " +
            "values(#{scenicName}, #{scenicCover}, #{location}, #{scenicDescription}," +
            "        #{maxCapacity}, #{openStartTime}, #{openEndTime}, #{scenicStatus},#{createTime},#{updateTime})")
    void saveScenic(Scenic scenic);


    void update(Scenic scenic);

    int batchDelete(@Param("ids") List<Integer> ids);


    @Select("select * from scenic ")
    List<Scenic> queryScenic(Integer pageNum, Integer pageSize);

    @Select("select * from scenic where id = #{id}")
    Scenic queryScenicById(Integer id);
}
