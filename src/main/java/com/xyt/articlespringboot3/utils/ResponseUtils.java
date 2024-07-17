package com.xyt.articlespringboot3.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyt.articlespringboot3.common.BaseResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.PrintWriter;


public class ResponseUtils {

    public static void write(BaseResponse<String> baseResponse, HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(baseResponse));
        writer.close();
    }
}
