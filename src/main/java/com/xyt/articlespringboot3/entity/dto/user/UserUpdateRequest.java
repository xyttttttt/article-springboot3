package com.xyt.articlespringboot3.entity.dto.user;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户查询请求
 *
 */

@Data
public class UserUpdateRequest  implements Serializable {


    private Long id;

    /**
     * 账号
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
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}