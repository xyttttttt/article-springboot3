package com.xyt.articlespringboot3.handler;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyt.articlespringboot3.common.ErrorCode;
import com.xyt.articlespringboot3.common.ResultUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
@Slf4j
public class AuthEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        log.error("用户未登录:{}，请求头:{}",authException, JSONUtil.toJsonStr(request));
        String value = new ObjectMapper().writeValueAsString(ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR));
        response.getWriter().write(value);
    }
}

