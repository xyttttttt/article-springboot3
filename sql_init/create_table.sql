# 数据库初始化

-- 创建库
create database if not exists articles;

-- 切换库
use articles;

-- 用户表
create table if not exists user
(
    user_id           bigint             auto_increment                          comment 'id' primary key,
    username     varchar(256)                                   null        comment '用户名',
    password     varchar(512)                                   not null    comment '密码',
    email        varchar(256)                                   not null    comment '邮箱',
    userAvatar   varchar(1024)                                  null        comment '用户头像',
    userRole     varchar(256)       default 'user'              not null    comment '用户角色：user/admin/ban',
    created         datetime           default CURRENT_TIMESTAMP   not null    comment '创建时间',
    last_modified   datetime           default CURRENT_TIMESTAMP   not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint            default 0                   not null    comment '是否删除'
) comment '用户' collate = utf8mb4_unicode_ci;



-- 学校通知
create table if not exists article
(
    post_id         bigint auto_increment                                   comment 'id'          primary key,
    title           varchar(512)                                not null    comment '标题',
    content         text                                        not null    comment '内容',
    user_id          bigint                                      not null    comment '创建用户 id',
    created         datetime           default CURRENT_TIMESTAMP   not null    comment '创建时间',
    last_modified   datetime           default CURRENT_TIMESTAMP   not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete       tinyint         default 0                   not null    comment '是否删除',
    index idx_userId(user_id)
) comment '学校新闻';






