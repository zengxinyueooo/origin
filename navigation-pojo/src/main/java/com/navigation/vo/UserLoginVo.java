package com.navigation.vo;


import lombok.Data;

/**
 * 视图对象（VO），用于封装从业务层返回给前端展示的数据，可能包含一些处理后的用户登录信息。
 */
@Data
public class UserLoginVo {

    private Integer id;
    private String username;
    private String token;
}
