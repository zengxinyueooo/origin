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
    @Insert("INSERT INTO user (nick_name, email, password, salt, age, gender, activation_time, create_time, is_valid, confirm_code, update_time, head, role) " +
            "VALUES (#{nickName}, #{email}, #{password}, #{salt}, #{age}, #{gender}, #{activationTime}, #{createTime}, #{isValid}, #{confirmCode}, #{updateTime}, #{head}, #{role})")
    int insertUser(User user);

    /**
     * 根据确认码查询用户
     * @param confirmCode
     * @return
     */
    @Select("SELECT email, activation_time FROM user WHERE confirm_code = #{confirmCode} AND is_valid = 0")
    User selectUserByConfirmCode(@Param("confirmCode") String confirmCode);

    /**
     * 根据确认码查询用户并修改状态值为1（可用）
     * @param confirmCode
     * @return
     */
    @Update("UPDATE user SET is_valid = 1 WHERE confirm_code = #{confirmCode}")
    int updateUserByConfirmCode(@Param("confirmCode") String confirmCode);

    /**
     * 根据邮箱查询账户
     * @param email
     * @return
     */
    @Select("SELECT * FROM user WHERE email = #{email} AND is_valid = 1")
    User selectUserByEmail(@Param("email") String email);
}
