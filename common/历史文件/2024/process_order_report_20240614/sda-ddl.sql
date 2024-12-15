-- 加工部每日平台需求表
CREATE TABLE cn_sda.`scm_process_daily_pd`
(
    `scm_process_daily_pd_id` bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    `create_date`             date            NULL COMMENT '加工单创建日期',
    `limited`                 int                      DEFAULT '0' COMMENT 'limited',
    `wei_chuang_xin`          int                      DEFAULT '0' COMMENT '微创新平台的加工需求数量',
    `b2b`                     int                      DEFAULT '0' COMMENT 'B2B平台的加工需求数量',
    `si_chuang`               int                      DEFAULT '0' COMMENT '思创平台的加工需求数量',
    `jiao_zhi`                int                      DEFAULT '0' COMMENT '娇致平台的加工需求数量',
    `amazon`                  int                      DEFAULT '0' COMMENT '亚马逊平台的加工需求数量',
    create_time               datetime                 DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `del_timestamp`           bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除标识，0正常、非0删除',
    PRIMARY KEY (`scm_process_daily_pd_id`) USING BTREE,
    UNIQUE KEY `uk_scm_process_daily_pd_1` (`create_date`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='加工部每日各平台需求数统计';

-- 加工部每日各平台出货表
create table cn_sda.`scm_process_daily_ps`
(
    `scm_process_daily_ps_id` bigint                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `stored_date`             date                                      NULL COMMENT '入库日期',
    `limited`                 int             default 0                 NULL COMMENT 'limited',
    `wei_chuang_xin`          int             default 0                 NULL COMMENT '微创新平台的加工需求数量',
    `b2b`                     int             default 0                 NULL COMMENT 'B2B平台的加工需求数量',
    `si_chuang`               int             default 0                 NULL COMMENT '思创平台的加工需求数量',
    `jiao_zhi`                int             default 0                 NULL COMMENT '娇致平台的加工需求数量',
    `amazon`                  int             default 0                 NULL COMMENT '亚马逊平台的加工需求数量',
    `create_time`             datetime        default CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `del_timestamp`           bigint unsigned default '0'               NOT NULL COMMENT '删除标识，0正常、非0删除',
    PRIMARY KEY (`scm_process_daily_ps_id`) USING BTREE,
    UNIQUE KEY `uk_scm_process_daily_ps_1` (`stored_date`) USING BTREE
) COMMENT '加工部每日各平台出库数统计';