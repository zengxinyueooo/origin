package com.navigation.mapper;

import com.navigation.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    /**
     * 新增账号
     * @param user
     * @return
     */

    @Insert("INSERT INTO user (nick_name, email, password, salt, age, gender, activation_time, create_time, is_vaild, confirm_code, update_time, head, role) " +
            "VALUES (#{nickName}, #{email}, #{password}, #{salt}, #{age}, #{gender}, #{activationTime}, #{createTime}, #{isVaild}, #{confirmCode}, #{updateTime}, #{head}, #{role})")
    int insertUser(User user);

    /**
     * 根据确认码查询用户
     * @param confirmCode
     * @return
     */
    @Select("SELECT email,activation_time FROM user WHERE confirm_code =#{confirmCode} AND is_vaild=0")
    User selectUserByConfirmCode(@Param("confirmCode") String confirmCode);

    /**
     * 根据确认码查询用户并修改状态值为1（可用）
     * @param confirmCode
     * @return
     */
    @Update("UPDATE user SET is_valid =1 WHERE confirm_code =#{confirmCode}")
    int updateUserByConfirmCode(@Param("confirmCode") String confirmCode);

//    @Select()






//    @Select("select * from navigation_system.user where username = #{username} " +
//            "and password = #{password}")
//    User login(String username, String password);
//    @Insert("insert into user (nickname, username, password, gender, age, telephone, status, create_time,  update_time) VALUES " +
//                             "(#{nickname},#{username},#{password},#{gender},#{age},#{telephone},#{status},#{createTime},#{updateTime})")
//    void register(User user);
//
//    void update(User user);
//
//    @Select("select * from user where username = #{username}")
//    User exist(String username);
}
