package com.xyt.articlespringboot3.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyt.articlespringboot3.common.BaseResponse;
import com.xyt.articlespringboot3.common.ErrorCode;
import com.xyt.articlespringboot3.common.ResultUtils;
import com.xyt.articlespringboot3.entity.Article;
import com.xyt.articlespringboot3.entity.User;
import com.xyt.articlespringboot3.entity.dto.article.ArticleAddRequest;

import com.xyt.articlespringboot3.entity.dto.article.ArticleEditRequest;
import com.xyt.articlespringboot3.entity.dto.article.ArticleQueryRequest;
import com.xyt.articlespringboot3.entity.dto.article.ArticleUpdateRequest;
import com.xyt.articlespringboot3.entity.vo.ArticleVO;
import com.xyt.articlespringboot3.exception.BusinessException;
import com.xyt.articlespringboot3.exception.ThrowUtils;
import com.xyt.articlespringboot3.service.ArticleService;
import com.xyt.articlespringboot3.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 文章接口
 *
 * @author 薛宇彤
 * @from 1604899092
 */
@RestController
@RequestMapping("/posts")
@Slf4j
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建文章
     *
     * @param articleAddRequest
     * @return
     */
    @PostMapping("/")
    public BaseResponse<Long> addArticle(@RequestBody ArticleAddRequest articleAddRequest) {
        ThrowUtils.throwIf(articleAddRequest == null, ErrorCode.PARAMS_ERROR);
        //在此处将实体类和 DTO 进行转换
        Article article = new Article();
        BeanUtils.copyProperties(articleAddRequest, article);
        // 数据校验
        articleService.validArticle(article, true);
        // 填充默认值
        User loginUser = userService.getLoginUser();
        article.setUserId(loginUser.getUserId());
        // 写入数据库
        boolean result = articleService.save(article);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newArticleId = article.getPostId();
        return ResultUtils.success(newArticleId);
    }

    /**
     * 删除文章
     *
     * @param id
     * @return
     */
    @DeleteMapping("/")
    public BaseResponse<Boolean> deleteArticle(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser();
        // 判断是否存在
        Article oldArticle = articleService.getById(id);
        ThrowUtils.throwIf(oldArticle == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldArticle.getUserId().equals(user.getUserId()) && !userService.isAdmin(user)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = articleService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新文章（仅管理员可用）
     *
     * @param articleUpdateRequest
     * @return
     */
    @PutMapping("/")
    @PreAuthorize("hasAuthority('admin')")
    public BaseResponse<Boolean> updateArticle(@RequestBody ArticleUpdateRequest articleUpdateRequest) {
        if (articleUpdateRequest == null || articleUpdateRequest.getPostId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 在此处将实体类和 DTO 进行转换
        Article article = new Article();
        BeanUtils.copyProperties(articleUpdateRequest, article);
        // 数据校验
        articleService.validArticle(article, false);
        // 判断是否存在
        long id = articleUpdateRequest.getPostId();
        Article oldArticle = articleService.getById(id);
        ThrowUtils.throwIf(oldArticle == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = articleService.updateById(article);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取文章（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/")
    public BaseResponse<ArticleVO> getArticleVOById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Article article = articleService.getById(id);
        ThrowUtils.throwIf(article == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(articleService.getArticleVO(article));
    }

    /**
     * 分页获取文章列表（仅管理员可用）
     *
     * @param articleQueryRequest
     * @return
     */
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('admin')")
    public BaseResponse<Page<Article>> listArticleByPage(@RequestBody ArticleQueryRequest articleQueryRequest) {
        long current = articleQueryRequest.getCurrent();
        long size = articleQueryRequest.getPageSize();
        // 查询数据库
        Page<Article> articlePage = articleService.page(new Page<>(current, size),
                articleService.getQueryWrapper(articleQueryRequest));
        return ResultUtils.success(articlePage);
    }

    /**
     * 分页获取文章列表（封装类）
     *
     * @param articleQueryRequest
     * @return
     */
    @PostMapping("/page/vo")
    public BaseResponse<Page<ArticleVO>> listArticleVOByPage(@RequestBody ArticleQueryRequest articleQueryRequest) {
        long current = articleQueryRequest.getCurrent();
        long size = articleQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Article> articlePage = articleService.page(new Page<>(current, size),
                articleService.getQueryWrapper(articleQueryRequest));
        // 获取封装类
        return ResultUtils.success(articleService.getArticleVOPage(articlePage));
    }

    /**
     * 分页获取当前登录用户创建的文章列表
     *
     * @param articleQueryRequest
     * @return
     */
    @PostMapping("/my/page/vo")
    public BaseResponse<Page<ArticleVO>> listMyArticleVOByPage(@RequestBody ArticleQueryRequest articleQueryRequest) {
        ThrowUtils.throwIf(articleQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser();
        articleQueryRequest.setUserId(loginUser.getUserId());
        long current = articleQueryRequest.getCurrent();
        long size = articleQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Article> articlePage = articleService.page(new Page<>(current, size),
                articleService.getQueryWrapper(articleQueryRequest));
        // 获取封装类
        return ResultUtils.success(articleService.getArticleVOPage(articlePage));
    }

    /**
     * 编辑文章（给用户使用）
     *
     * @param articleEditRequest
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editArticle(@RequestBody ArticleEditRequest articleEditRequest) {
        if (articleEditRequest == null || articleEditRequest.getPostId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //在此处将实体类和 DTO 进行转换
        Article article = new Article();
        BeanUtils.copyProperties(articleEditRequest, article);
        // 数据校验
        articleService.validArticle(article, false);
        User loginUser = userService.getLoginUser();
        // 判断是否存在
        long id = articleEditRequest.getPostId();
        Article oldArticle = articleService.getById(id);
        ThrowUtils.throwIf(oldArticle == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldArticle.getUserId().equals(loginUser.getUserId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = articleService.updateById(article);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
