package com.xyt.articlespringboot3.entity.dto.article;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xyt.articlespringboot3.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询文章请求
 *
 * @author 薛宇彤
 * @from 1604899092
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long postId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建用户
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Integer created;

    /**
     * 修改时间
     */
    private String lastModified;


    private static final long serialVersionUID = 1L;
}