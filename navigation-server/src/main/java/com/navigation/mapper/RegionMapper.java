package com.navigation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.navigation.entity.Region;
import com.navigation.entity.Scenic;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RegionMapper extends BaseMapper<Region> {

    @Insert("insert into region(region_name, region_description, create_time, update_time) " +
            "values(#{regionName}, #{regionDescription}, #{createTime}, #{updateTime})")
    void saveRegion(Region region);


    @Select("select * from region where region_id = #{id}")
    Region queryRegionById(Integer id);


    void update(Region region);


    int batchDelete(@Param("ids") List<Integer> ids);


    @Select("select * from region ")
    List<Region> queryRegion(Integer pageNum, Integer pageSize);

    @Select("SELECT COUNT(*) FROM region WHERE region_id = #{id}")
    int countRegionById(Integer id);

    @Select("SELECT region_id FROM region")
    List<Integer> getAllExistingRegionIds();


}
