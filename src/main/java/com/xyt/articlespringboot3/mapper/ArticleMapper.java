package com.xyt.articlespringboot3.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xyt.articlespringboot3.entity.Article;
import org.springframework.stereotype.Repository;

/**
* @author 16048
* @description 针对表【news(学校新闻)】的数据库操作Mapper
* @createDate 2024-02-11 17:41:52
* @Entity com.xyt.dronehotspotspringboot3.entity.News
*/
@Repository
public interface ArticleMapper extends BaseMapper<Article> {

}




