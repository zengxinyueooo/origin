package com.navigation.service;

import com.navigation.dto.LoginDto;
import com.navigation.dto.RegisterDto;
import com.navigation.entity.User;

import java.util.Map;

public interface UserService {

    /**
     * 注册用户的方法
     * @param registerDto 注册信息数据传输对象
     * @return 操作结果（成功或失败）和附加信息
     */
    Map<String, Object> RegisterUser(RegisterDto registerDto);




    //    @Autowired
    //    private UserMapper userMapper;
    //    private String password;
    //    @Override
    //    public User login(LoginDto loginDto) {
    //        password= loginDto.getPassword();
    //        String password = DigestUtils.md5DigestAsHex(loginDto.getPassword().getBytes());
    //        return userMapper.login(loginDto.getUsername(),password);
    //    }
    //
    //    @Override
    //    public void register(LoginDto loginDto) {
    //        password= loginDto.getPassword();
    //        String password = DigestUtils.md5DigestAsHex(loginDto.getPassword().getBytes());
    //        User userExist = userMapper.exist(loginDto.getUsername());
    //        if(userExist == null){
    //        User user = new User();
    //        user.setNickname(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
    //        user.setUsername(loginDto.getUsername());
    //        user.setPassword(password);
    //        user.setCreateTime(LocalDateTime.now());
    //        user.setUpdateTime(LocalDateTime.now());
    //        userMapper.register(user);
    //        }else {
    //            throw new AccountExist("账号已被占用,请重新填写");
    //        }
    //    }
    //Map<String,Object> createAccount(User user);
//    User login(LoginDto loginDto);
//    void register(LoginDto loginDto);

}
