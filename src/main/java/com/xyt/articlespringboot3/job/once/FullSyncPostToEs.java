package com.xyt.articlespringboot3.job.once;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * 全量新闻到 es
 *
 */
@Component
@Slf4j
public class FullSyncPostToEs implements CommandLineRunner {
    @Override
    public void run(String... args) {
    }
}
