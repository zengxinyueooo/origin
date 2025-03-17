package com.navigation.mapper;

import com.navigation.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from navigation_system.user where username = #{username} " +
            "and password = #{password}")
    User login(String username, String password);
    @Insert("insert into user (nickname, username, password, gender, age, telephone, status, create_time,  update_time) VALUES " +
                             "(#{nickname},#{username},#{password},#{gender},#{age},#{telephone},#{status},#{createTime},#{updateTime})")
    void register(User user);

    void update(User user);

    @Select("select * from user where username = #{username}")
    User exist(String username);
}
