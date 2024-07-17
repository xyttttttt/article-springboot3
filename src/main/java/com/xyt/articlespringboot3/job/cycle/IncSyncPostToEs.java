package com.xyt.articlespringboot3.job.cycle;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 增量同步帖子到 es
 *
 */
@Component
@Slf4j
public class IncSyncPostToEs {



    /**
     * 每天凌晨一点更新  文章推荐内容
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void run() {
    }


    /**
     * 未来数据定时刷新  文章延迟发布
     */
    //@Scheduled(cron = "0 */1 * * * ?")
    public void refresh(){
        //获取所有未来数据的集合
    }
}
