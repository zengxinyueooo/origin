package com.navigation.service;

import com.navigation.dto.LoginDto;
import com.navigation.dto.RegisterDto;
import com.navigation.entity.User;
import java.util.Map;

public interface UserService {

    /**
     * 用户注册（仅适用于普通用户）
     * <p>
     * 注册成功后会发送激活邮件；管理员账号不支持此注册激活流程，需要提前在数据库中配置。
     * </p>
     *
     * @param registerDto 注册信息数据传输对象
     * @return 操作结果及附加信息，如注册成功、失败、邮箱已存在等
     */
    Map<String, Object> RegisterUser(RegisterDto registerDto);

    /**
     * 用户登录（支持普通用户和管理员）
     * <p>
     * 普通用户需经过注册激活且密码经过加密比对；<br/>
     * 管理员账号直接使用明文密码进行比对，无需激活。<br/>
     * 登录成功后返回 JWT 令牌和用户角色信息，用于后续接口验证。
     * </p>
     *
     * @param loginDto 登录信息数据传输对象
     * @return 操作结果及附加信息，如登录成功、失败、token、用户角色等
     */
    Map<String, Object> LoginUser(LoginDto loginDto);

    /**
     * 账号激活（仅适用于普通用户）
     * <p>
     * 根据激活码确认用户身份，并完成账户激活操作；如果激活码过期则需重新注册。
     * </p>
     *
     * @param confirmCode 激活码
     * @return 操作结果及附加信息，如激活成功、失败、链接失效等
     */
    Map<String, Object> activationAccount(String confirmCode);
}
