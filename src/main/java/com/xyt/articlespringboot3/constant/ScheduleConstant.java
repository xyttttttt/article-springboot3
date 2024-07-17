package com.xyt.articlespringboot3.constant;

public interface ScheduleConstant {


    //task状态
    int SCHEDULED = 0;   //初始化状态

    int EXECUTED = 1;       //已执行状态

    int CANCELLED = 2;   //已取消状态

    String FUTURE = "future_";   //未来数据key前缀

    String TOPIC = "topic_";     //当前数据key前缀
}
