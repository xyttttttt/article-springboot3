package com.xyt.articlespringboot3.utils;


import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class WebUtil {

    public static String renderString(HttpServletResponse response, String str){

        try{
            response.setStatus(0);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(str);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
