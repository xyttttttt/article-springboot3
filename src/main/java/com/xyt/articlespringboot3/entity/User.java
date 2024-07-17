package com.xyt.articlespringboot3.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {


    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 用户头像
     */
    @TableField("userAvatar")
    private String userAvatar;

    /**
     * 用户角色
     */
    @TableField("userRole")
    private String userRole;

    /**
     * 创建时间
     */
    @TableField("created")
    private Date created;

    /**
     * 修改时间
     */
    @TableField("last_modified")
    private Date lastModified;

    /**
     * 是否删除
     */
    @TableLogic
    @TableField("isDelete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}