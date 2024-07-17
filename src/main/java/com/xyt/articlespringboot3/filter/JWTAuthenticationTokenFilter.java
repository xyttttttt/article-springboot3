package com.xyt.articlespringboot3.filter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import com.xyt.articlespringboot3.common.ErrorCode;
import com.xyt.articlespringboot3.common.ResultUtils;
import com.xyt.articlespringboot3.entity.LoginUser;
import com.xyt.articlespringboot3.exception.BusinessException;
import com.xyt.articlespringboot3.service.impl.MyUserDetailsService;
import com.xyt.articlespringboot3.utils.JwtUtils;
import com.xyt.articlespringboot3.utils.ResponseUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class JWTAuthenticationTokenFilter extends OncePerRequestFilter {


    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        //获取token
        String token = httpServletRequest.getHeader("Authorization");
        if (StringUtils.isBlank(token) || token.equals("null")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        log.info("token,{}",token);
        boolean validateToken = JwtUtils.isValidToken(token);
        if (!validateToken){
            ResponseUtils.write(ResultUtils.error(ErrorCode.TOKEN_EXPIRES,"token已过期"),httpServletResponse);
            return;
        }
        //解析token
        Claims claims = null;
        claims = JwtUtils.parseJWT(token);
        if (claims == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 将Claims转换为Map<String, Object>
        Map<String, Object> mapClaims = claims;


        Object username = mapClaims.get("username");

        LoginUser loginUser = (LoginUser) myUserDetailsService.loadUserByUsername(username.toString());
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
