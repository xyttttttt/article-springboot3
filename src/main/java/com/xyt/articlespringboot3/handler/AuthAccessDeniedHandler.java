package com.xyt.articlespringboot3.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyt.articlespringboot3.common.ErrorCode;
import com.xyt.articlespringboot3.common.ResultUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        String value = new ObjectMapper().writeValueAsString(ResultUtils.error(ErrorCode.NO_AUTH_ERROR));
        response.getWriter().write(value);
    }
}

