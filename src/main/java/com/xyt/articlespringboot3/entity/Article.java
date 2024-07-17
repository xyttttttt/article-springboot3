package com.xyt.articlespringboot3.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName(value ="articles")
@Data
@EqualsAndHashCode
public class Article {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long postId;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 创建用户
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField("created")
    private Integer created;

    /**
     * 修改时间
     */
    @TableField("last_modified")
    private String lastModified;

    /**
     * 是否删除
     */
    @TableLogic
    @TableField("isDelete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
