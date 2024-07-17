package com.xyt.articlespringboot3.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 已登录用户视图（脱敏）
 *
 **/
@Data
public class LoginUserVO implements Serializable {

    /**
     * id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户角色
     */
    private String userRole;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 修改时间
     */
    private Date lastModified;

private static final long serialVersionUID = 1L;
}