CREATE TABLE `scm_purchase_cnt`
(
    `scm_purchase_cnt_id`        bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    `create_date`                date                     DEFAULT NULL COMMENT '创建时间',
    `tt_mo_purchase_cnt`         int                      DEFAULT '0' COMMENT '头套本月下单数',
    `ntt_mo_purchase_cnt`        int                      DEFAULT '0' COMMENT '非头套本月下单数',
    `tt_week_purchase_cnt`       int                      DEFAULT '0' COMMENT '头套七天下单数',
    `ntt_week_purchase_cnt`      int                      DEFAULT '0' COMMENT '非头套七天下单数',
    `tt_mo_back_purchase_cnt`    int                      DEFAULT '0' COMMENT '头套本月回货数',
    `ntt_mo_back_purchase_cnt`   int                      DEFAULT '0' COMMENT '非头套本月回货数',
    `tt_week_back_purchase_cnt`  int                      DEFAULT '0' COMMENT '头套七天回货数',
    `ntt_week_back_purchase_cnt` int                      DEFAULT '0' COMMENT '非头套七天回货数',
    `tt_mo_act_back_cnt`         int                      DEFAULT '0' COMMENT '头套本月实际回货数',
    `ntt_mo_act_back_cnt`        int                      DEFAULT '0' COMMENT '非头套本月实际回货数',
    `tt_week_act_back_cnt`       int                      DEFAULT '0' COMMENT '头套七天实际回货数',
    `ntt_week_act_back_cnt`      int                      DEFAULT '0' COMMENT '非头套七天实际回货数',
    `update_time`                datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_timestamp`              bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除标识，0正常、非0删除',
    PRIMARY KEY (`scm_purchase_cnt_id`) USING BTREE,
    UNIQUE KEY `uk_scm_purchase_cnt_1` (`create_date`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='scm-采购订单-下单数回货数统计';