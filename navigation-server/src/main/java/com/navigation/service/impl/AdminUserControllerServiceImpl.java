package com.navigation.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.navigation.entity.User;
import com.navigation.mapper.AdminUserMapper;
import com.navigation.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminUserControllerServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    /**
     * 管理员根据条件（邮箱或昵称）进行分页查询
     */
    @Override
    public PageInfo<User> getUsersByCondition(int pageNum, int pageSize, String email, String nickName) {
        // 启动分页功能
        PageHelper.startPage(pageNum, pageSize);

        // 查询符合条件的用户
        List<User> userList = adminUserMapper.findUsersByCondition(email, nickName);

        // 使用 PageInfo 封装分页数据
        return new PageInfo<>(userList);
    }

    /**
     * 管理员查询所有用户并分页，按账号创建时间排序
     */
    @Override
    public PageInfo<User> getAllUsers(int pageNum, int pageSize) {
        // 启动分页功能
        PageHelper.startPage(pageNum, pageSize);

        // 查询所有用户并按账号创建时间排序
        List<User> userList = adminUserMapper.findAllUsers();

        // 使用 PageInfo 封装分页数据
        return new PageInfo<>(userList);
    }
    /**
     * 管理员根据用户ID删除用户
     * @param userId 用户ID
     * @return 是否删除成功
     */
    @Override
    public boolean deleteUserById(Integer userId) {
        try {
            // 删除用户，假设 deleteById 是 Mapper 中的删除方法
            int result = adminUserMapper.deleteById(userId);
            return result > 0;  // 返回 true 表示删除成功，返回 false 表示删除失败
        } catch (Exception e) {
            e.printStackTrace();
            return false;  // 出现异常时返回删除失败
        }
    }
    /**
     * 管理员根据用户ID修改用户的昵称、性别、年龄、是否有效状态和角色
     * 自动更新时间戳
     * @param user 包含用户ID和要更新的字段（昵称、性别、年龄、是否有效状态、角色）
     * @return 是否更新成功
     */
    @Override
    public boolean updateUserInfo(User user) {
        try {
            // 设置更新时间为当前时间
            user.setUpdateTime(LocalDateTime.now());

            // 调用 Mapper 的更新方法
            int result = adminUserMapper.updateUser(user);

            // 如果更新了至少一条记录，返回 true
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常信息
            return false; // 发生异常时返回 false
        }
    }

}
