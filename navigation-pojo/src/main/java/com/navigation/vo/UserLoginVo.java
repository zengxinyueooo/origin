package com.navigation.vo;


import lombok.Data;

/**
 * 视图对象（VO），用于封装从业务层返回给前端展示的数据，可能包含一些处理后的用户登录信息。
 */
@Data
public class UserLoginVo {

    private Integer userId; // 用户ID

    private String userName; // 用户名

    private String nickName; // 昵称

    private String email; // 邮箱

    private String role; // 用户角色（user/admin）

    private String token; // 认证 Token（JWT）
}
