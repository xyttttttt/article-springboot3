package com.xyt.articlespringboot3.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyt.articlespringboot3.common.ErrorCode;
import com.xyt.articlespringboot3.constant.CommonConstant;
import com.xyt.articlespringboot3.entity.Article;
import com.xyt.articlespringboot3.entity.User;
import com.xyt.articlespringboot3.entity.dto.article.ArticleQueryRequest;
import com.xyt.articlespringboot3.entity.vo.ArticleVO;
import com.xyt.articlespringboot3.entity.vo.UserVO;
import com.xyt.articlespringboot3.exception.ThrowUtils;

import com.xyt.articlespringboot3.mapper.ArticleMapper;
import com.xyt.articlespringboot3.service.ArticleService;
import com.xyt.articlespringboot3.service.UserService;
import com.xyt.articlespringboot3.utils.SqlUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 文章服务实现
 *
 * @author 薛宇彤
 * @from 1604899092
 */
@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param article
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validArticle(Article article, boolean add) {
        ThrowUtils.throwIf(article == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        String title = article.getTitle();
        // 创建数据时，参数不能为空
        if (add) {
            // 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        // 补充校验规则
        if (StringUtils.isNotBlank(title)) {
            ThrowUtils.throwIf(title.length() > 80, ErrorCode.PARAMS_ERROR, "标题过长");
        }
    }

    /**
     * 获取查询条件
     *
     * @param articleQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Article> getQueryWrapper(ArticleQueryRequest articleQueryRequest) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        if (articleQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = articleQueryRequest.getPostId();
        String title = articleQueryRequest.getTitle();
        String content = articleQueryRequest.getContent();
        String sortField = articleQueryRequest.getSortField();
        String sortOrder = articleQueryRequest.getSortOrder();
        Long userId = articleQueryRequest.getUserId();
        // 补充需要的查询条件
        // 从多字段中搜索
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "post_id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "user_id", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取文章封装
     *
     * @param article
     * @return
     */
    @Override
    public ArticleVO getArticleVO(Article article) {
        // 对象转封装类
        ArticleVO articleVO = ArticleVO.objToVo(article);

        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = article.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        articleVO.setUser(userVO);
        // endregion

        return articleVO;
    }

    /**
     * 分页获取文章封装
     *
     * @param articlePage
     * @return
     */
    @Override
    public Page<ArticleVO> getArticleVOPage(Page<Article> articlePage) {
        List<Article> articleList = articlePage.getRecords();
        Page<ArticleVO> articleVOPage = new Page<>(articlePage.getCurrent(), articlePage.getSize(), articlePage.getTotal());
        if (CollUtil.isEmpty(articleList)) {
            return articleVOPage;
        }
        // 对象列表 => 封装对象列表
        List<ArticleVO> articleVOList = articleList.stream().map(article -> {
            return ArticleVO.objToVo(article);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = articleList.stream().map(Article::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getUserId));
        // 2. 已登录，获取用户点赞、收藏状态

        // 填充信息
        articleVOList.forEach(articleVO -> {
            Long userId = articleVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            articleVO.setUser(userService.getUserVO(user));
        });
        // endregion

        articleVOPage.setRecords(articleVOList);
        return articleVOPage;
    }

}
