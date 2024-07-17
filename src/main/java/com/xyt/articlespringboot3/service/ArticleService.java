package com.xyt.articlespringboot3.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyt.articlespringboot3.entity.Article;
import com.xyt.articlespringboot3.entity.dto.article.ArticleQueryRequest;
import com.xyt.articlespringboot3.entity.vo.ArticleVO;
import jakarta.servlet.http.HttpServletRequest;


/**
 * 文章服务
 *
 * @author 薛宇彤
 * @from 1604899092
 */
public interface ArticleService extends IService<Article> {

    /**
     * 校验数据
     *
     * @param article
     * @param add 对创建的数据进行校验
     */
    void validArticle(Article article, boolean add);

    /**
     * 获取查询条件
     *
     * @param articleQueryRequest
     * @return
     */
    QueryWrapper<Article> getQueryWrapper(ArticleQueryRequest articleQueryRequest);
    
    /**
     * 获取文章封装
     *
     * @param article
     * @param request
     * @return
     */
    ArticleVO getArticleVO(Article article);

    /**
     * 分页获取文章封装
     *
     * @param articlePage
     * @param request
     * @return
     */
    Page<ArticleVO> getArticleVOPage(Page<Article> articlePage);
}
