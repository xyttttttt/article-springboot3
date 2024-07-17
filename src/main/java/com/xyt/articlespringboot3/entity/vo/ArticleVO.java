package com.xyt.articlespringboot3.entity.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xyt.articlespringboot3.entity.Article;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 文章视图
 *
 * @author 薛宇彤
 * @from 1604899092
 */
@Data
public class ArticleVO implements Serializable {

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
     * 用户id
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


    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 封装类转对象
     *
     * @param articleVO
     * @return
     */
    public static Article voToObj(ArticleVO articleVO) {
        if (articleVO == null) {
            return null;
        }
        Article article = new Article();
        BeanUtils.copyProperties(articleVO, article);
        return article;
    }

    /**
     * 对象转封装类
     *
     * @param article
     * @return
     */
    public static ArticleVO objToVo(Article article) {
        if (article == null) {
            return null;
        }
        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(article, articleVO);
        return articleVO;
    }
}
