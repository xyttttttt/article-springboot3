package com.xyt.articlespringboot3;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@MapperScan(basePackages = "com.xyt.articlespringboot3.mapper")
@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy
@SpringBootApplication
public class ArticleSpringboot3Application {

    public static void main(String[] args) {
        SpringApplication.run(ArticleSpringboot3Application.class, args);
    }

}
