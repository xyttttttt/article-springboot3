package com.xyt.articlespringboot3.controller;


import com.xyt.articlespringboot3.common.BaseResponse;
import com.xyt.articlespringboot3.common.ResultUtils;
import com.xyt.articlespringboot3.entity.User;
import com.xyt.articlespringboot3.service.UserService;
import com.xyt.articlespringboot3.service.impl.MyUserDetailsService;
import com.xyt.articlespringboot3.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class RefreshTokenController {

    @Autowired
    private UserService userService;




    @PostMapping("/refresh-token")
    public BaseResponse<Map<String,String>> refreshToken() {

        User user = userService.getLoginUser();
        String username = user.getUsername();
        // 如果刷新令牌有效，则生成新的token

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("role", user.getUserRole());
        String token = JwtUtils.createToken(claims,String.valueOf(user.getUserId()));
        String refreshToken = JwtUtils.createRefreshToken(claims,String.valueOf(user.getUserId()));

        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("refresh-token",refreshToken);
        // 更新或生成新的刷新令牌逻辑...
        return ResultUtils.success(map); // 返回包含新令牌的对象
    }

}

