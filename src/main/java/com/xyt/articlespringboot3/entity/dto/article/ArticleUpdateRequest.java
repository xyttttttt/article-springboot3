package com.xyt.articlespringboot3.entity.dto.article;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新文章请求
 *
 * @author 薛宇彤
 * @from 1604899092
 */
@Data
public class ArticleUpdateRequest implements Serializable {

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



    private static final long serialVersionUID = 1L;
}