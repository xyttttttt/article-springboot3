package com.xyt.articlespringboot3.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xyt.articlespringboot3.common.ErrorCode;
import com.xyt.articlespringboot3.entity.LoginUser;
import com.xyt.articlespringboot3.entity.User;
import com.xyt.articlespringboot3.exception.BusinessException;
import com.xyt.articlespringboot3.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;


    // 假设你有一个方法来查找数据库中的用户
    public User findUserByUsername(String username) {
        // 实现查找逻辑
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,username);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户信息
        User user = findUserByUsername(username);
        if (user == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"用户账号或密码不正确");
        }
        //
        String userRole = user.getUserRole();
        List<String> list = new ArrayList<>(Collections.singletonList(userRole));
        //把数据封装成UserDetails返回
        return new LoginUser(user,list);
    }
}
