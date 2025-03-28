package com.navigation.mapper;



import com.navigation.entity.Manager;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ManagerMapper {
    // 根据用户名和密码查询管理员
    @Select("SELECT * FROM manager WHERE user_name = #{userName} AND password = #{managerPassword}")
    Manager findByUserNameAndPassword(@Param("userName") String userName, @Param("managerPassword") String managerPassword);
}