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
     * 根据确认码查询用户并修改状态值为 1（可用），只有在确认码有效且未激活的情况下才进行更新
     * @param confirmCode
     * @return
     */
    @Update("UPDATE user SET is_valid = 1 WHERE confirm_code = #{confirmCode} AND is_valid = 0")
    int updateUserByConfirmCode(@Param("confirmCode") String confirmCode);

    /**
     * 根据邮箱查询账户
     * 对于普通用户，is_valid 必须为 1；对于管理员账户无需激活状态的限制
     * @param email
     * @return
     */
    @Select("SELECT * FROM user WHERE email = #{email} AND (is_valid = 1 OR role = 'admin')")
    User selectUserByEmail(@Param("email") String email);

    /**
     * 根据id查询账户
     * @param userId
     * @return
     */
    @Select("SELECT * FROM user WHERE user_id = #{userId}")
    User selectUserById(@Param("userId") Integer userId);

    /**
     * 更新用户个人信息（支持动态更新）
     * @param user 用户信息
     * @return 更新操作影响的行数
     */
    @Update("<script>" +
            "UPDATE user " +
            "<set>" +
            "<if test='nickName != null'>nick_name = #{nickName},</if>" +
            "<if test='age != null'>age = #{age},</if>" +
            "<if test='gender != null'>gender = #{gender},</if>" +
            "<if test='head != null'>head = #{head},</if>" +
            "<if test='password != null'>password = #{password},</if>" +
            "update_time = #{updateTime} " +  // 设置更新时间为当前时间
            "</set>" +
            "WHERE user_id = #{userId}" +  // 使用 userId 替换 id
            "</script>")
    int updateUserPersonalInfo(User user);
}
