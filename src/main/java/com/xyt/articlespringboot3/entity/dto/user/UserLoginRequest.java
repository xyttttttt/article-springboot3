package com.xyt.articlespringboot3.entity.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求
 *
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;


}
