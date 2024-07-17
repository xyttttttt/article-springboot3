package com.xyt.articlespringboot3.mapper;

import com.xyt.articlespringboot3.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* @author 16048
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2024-02-11 17:42:14
* @Entity com.xyt.dronehotspotspringboot3.entity.User
*/
@Repository
public interface UserMapper extends BaseMapper<User> {

}




