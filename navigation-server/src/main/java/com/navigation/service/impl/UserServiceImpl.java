package com.navigation.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.navigation.dto.RegisterDto;
import com.navigation.entity.User;
import com.navigation.mapper.UserMapper;
import com.navigation.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 注册账号
     * @param registerDto 注册信息数据传输对象
     * @return
     */
    @Transactional
    public Map<String, Object> RegisterUser(RegisterDto registerDto) {
        // 1. 检查邮箱是否已存在
        User existingUser = userMapper.selectUserByEmail(registerDto.getEmail());
        if (existingUser != null) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("status", "failure");
            resultMap.put("message", "邮箱已存在");
            return resultMap;
        }

        // 2. 雪花算法生成确认码
        String confirmCode = IdUtil.getSnowflake(1, 1).nextIdStr();

        // 3. 生成盐值（随机字符串）
        String salt = RandomUtil.randomString(6);

        // 4. 加密密码：原始密码 + 盐
        String md5Pwd = SecureUtil.md5(registerDto.getPassword() + salt);

        // 5. 激活失效时间：24小时后
        LocalDateTime ldt = LocalDateTime.now().plusDays(1);

        // 6. 设置 DTO 中的数据
        registerDto.setPassword(md5Pwd);  // 密码加密
        registerDto.setConfirmCode(confirmCode);
        registerDto.setActivationTime(ldt);
        registerDto.setIsValid(0);

        // 7. 创建 User 对象
        User user = new User();
        user.setNickName(registerDto.getNickName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(md5Pwd);  // 设置加密后的密码
        user.setSalt(salt);  // 保存盐值
        user.setAge(registerDto.getAge());
        user.setGender(registerDto.getGender());
        user.setConfirmCode(registerDto.getConfirmCode());
        user.setActivationTime(registerDto.getActivationTime());
        user.setIsValid(registerDto.getIsValid());
        user.setRole("user");  // 默认角色
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        // 8. 新增账号并捕获可能的异常
        Map<String, Object> resultMap = new HashMap<>();
        try {
            // 插入之前先再次检查邮箱是否存在
            User checkExistingUser = userMapper.selectUserByEmail(registerDto.getEmail());
            if (checkExistingUser != null) {
                resultMap.put("status", "failure");
                resultMap.put("message", "邮箱已存在");
                return resultMap;
            }

            int result = userMapper.insertUser(user);

            // 9. 注册成功，返回成功信息
            if (result > 0) {
                resultMap.put("status", "success");
                resultMap.put("message", "注册成功");
            } else {
                // 注册失败，返回失败信息
                resultMap.put("status", "failure");
                resultMap.put("message", "注册失败");
            }
        } catch (DuplicateKeyException e) {
            // 捕获插入重复键错误
            resultMap.put("status", "failure");
            resultMap.put("message", "邮箱已存在");
        } catch (Exception e) {
            // 捕获其他错误
            resultMap.put("status", "failure");
            resultMap.put("message", "数据库错误，请稍后再试");
        }

        // 返回结果
        return resultMap;
    }
}
