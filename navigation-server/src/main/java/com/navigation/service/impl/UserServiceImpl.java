package com.navigation.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.navigation.dto.LoginDto;
import com.navigation.dto.RegisterDto;
import com.navigation.entity.User;
import com.navigation.mapper.UserMapper;
import com.navigation.service.MailService;
import com.navigation.service.UserService;
import com.navigation.utils.JwtUtils;
import com.navigation.utils.JwtUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private MailService mailService;

    private UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 注册账号（普通用户）
     * 管理员账号无需注册激活，需要提前在数据库中添加
     */
    @Transactional
    public Map<String, Object> RegisterUser(RegisterDto registerDto) {
        // 检查邮箱是否已存在
        User existingUser = userMapper.selectUserByEmail(registerDto.getEmail());
        if (existingUser != null) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("status", "failure");
            resultMap.put("message", "邮箱已存在");
            return resultMap;
        }

        // 雪花算法生成确认码
        String confirmCode = IdUtil.getSnowflake(1, 1).nextIdStr();

        // 生成盐值（随机字符串）
        String salt = RandomUtil.randomString(6);

        // 加密密码：原始密码 + 盐
        String md5Pwd = SecureUtil.md5(registerDto.getPassword() + salt);

        // 激活失效时间：24小时后
        LocalDateTime ldt = LocalDateTime.now().plusDays(1);

        // 设置 DTO 中的数据
        registerDto.setPassword(md5Pwd);  // 密码加密
        registerDto.setConfirmCode(confirmCode);
        registerDto.setActivationTime(ldt);
        registerDto.setIsValid(0);

        // 创建 User 对象
        User user = new User();
        user.setNickName(registerDto.getNickName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(md5Pwd);  // 设置加密后的密码
        user.setSalt(salt);         // 保存盐值
        user.setAge(registerDto.getAge());
        user.setGender(registerDto.getGender());
        user.setConfirmCode(registerDto.getConfirmCode());
        user.setActivationTime(registerDto.getActivationTime());
        user.setIsValid(registerDto.getIsValid());
        user.setRole("user");       // 默认角色为普通用户
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        Map<String, Object> resultMap = new HashMap<>();
        try {
            // 再次检查邮箱是否存在
            User checkExistingUser = userMapper.selectUserByEmail(registerDto.getEmail());
            if (checkExistingUser != null) {
                resultMap.put("status", "failure");
                resultMap.put("message", "邮箱已存在");
                return resultMap;
            }

            int result = userMapper.insertUser(user);

            // 注册成功后，发送激活邮件
            if (result > 0) {
                String activationUrl = "localhost:8080/users/activation?confirmCode=" + confirmCode;
                System.out.println(activationUrl);
                mailService.sendMailForActivationAccount(activationUrl, user.getEmail());
                resultMap.put("status", "success");
                resultMap.put("message", "注册成功");
            } else {
                resultMap.put("status", "failure");
                resultMap.put("message", "注册失败");
            }
        } catch (DuplicateKeyException e) {
            resultMap.put("status", "failure");
            resultMap.put("message", "邮箱已存在");
        } catch (Exception e) {
            resultMap.put("status", "failure");
            resultMap.put("message", "数据库错误，请稍后再试");
        }

        return resultMap;
    }

    /**
     * 登录账号（支持普通用户和管理员）
     * 管理员账号不经过注册激活，且密码不进行加密比对（明文比较）
     */
    public Map<String, Object> LoginUser(LoginDto loginDto) {
        Map<String, Object> resultMap = new HashMap<>();
        // 根据邮箱查询账户
        User user = userMapper.selectUserByEmail(loginDto.getEmail());
        if (user == null) {
            resultMap.put("code", 400);
            resultMap.put("message", "该账户不存在");
            return resultMap;
        }

        // 如果为普通用户，需检查激活状态并密码加密比对
        if (!"admin".equals(user.getRole())) {
            if (user.getIsValid() == 0) {
                resultMap.put("code", 400);
                resultMap.put("message", "该账户未激活");
                return resultMap;
            }
            // 加密比对密码
            String md5Pwd = SecureUtil.md5(loginDto.getPassword() + user.getSalt());
            if (!md5Pwd.equals(user.getPassword())) {
                resultMap.put("code", 400);
                resultMap.put("message", "用户名或密码错误");
                return resultMap;
            }
        } else {
            // 管理员直接使用明文密码比对
            if (!loginDto.getPassword().equals(user.getPassword())) {
                resultMap.put("code", 400);
                resultMap.put("message", "用户名或密码错误");
                return resultMap;
            }
        }

        // 登录成功，生成 JWT 令牌
        String token = JwtUtils.generateToken(user.getEmail(), user.getRole());
        resultMap.put("code", 200);
        resultMap.put("message", "登录成功");
        resultMap.put("token", token);
        resultMap.put("role", user.getRole());
        return resultMap;
    }

    /**
     * 账号激活（仅适用于普通用户）
     */
    @RequestMapping(value = "/activation", method = RequestMethod.GET)
    public Map<String, Object> activationAccount(String confirmCode) {
        Map<String, Object> resultMap = new HashMap<>();
        if (confirmCode == null || confirmCode.isEmpty()) {
            resultMap.put("code", 400);
            resultMap.put("message", "激活码无效");
            return resultMap;
        }
        User user = userMapper.selectUserByConfirmCode(confirmCode);
        if (user == null) {
            resultMap.put("code", 400);
            resultMap.put("message", "用户未找到");
            return resultMap;
        }
        // 检查激活时间是否过期（这里假设激活时间为注册后24小时有效）
        boolean isExpired = LocalDateTime.now().isAfter(user.getActivationTime().plusHours(24));
        if (isExpired) {
            resultMap.put("code", 400);
            resultMap.put("message", "链接已失效，请重新注册");
            return resultMap;
        }
        // 更新用户激活状态
        int result = userMapper.updateUserByConfirmCode(confirmCode);
        if (result > 0) {
            resultMap.put("code", 200);
            resultMap.put("message", "激活成功");
        } else {
            resultMap.put("code", 400);
            resultMap.put("message", "激活失败，数据库更新错误");
        }
        return resultMap;
    }
}
