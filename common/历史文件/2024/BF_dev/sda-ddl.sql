CREATE TABLE `scm_qc_exc_data`
(
    `scm_qc_exc_data_id` bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    `create_date`        date                                                          DEFAULT NULL COMMENT '创建时间',
    `receive_order_no`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收货单号',
    `receive_amount`     int                                                           DEFAULT '0' COMMENT '收货数',
    `not_pass_amount`    int unsigned    NOT NULL                                      DEFAULT '0' COMMENT '次品数',
    `supplier_code`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '供应商编码',
    `confirm_user`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟单人',
    `confirm_username`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟单人',
    `update_time`        datetime        NOT NULL                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_timestamp`      bigint unsigned NOT NULL                                      DEFAULT '0' COMMENT '删除标识，0正常、非0删除',
    PRIMARY KEY (`scm_qc_exc_data_id`) USING BTREE,
    UNIQUE KEY `uk_scm_qc_exc_data_1` (`create_date`, `receive_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='scm-质检异常数据';

CREATE TABLE `scm_receive_exc_data`
(
    `scm_receive_exc_data_id` bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    `create_date`             date                                                          DEFAULT NULL COMMENT '创建时间',
    `receive_order_no`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收货单号',
    `supplier_code`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '供应商编码',
    `confirm_user`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟单人',
    `confirm_username`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟单人',
    `update_time`             datetime        NOT NULL                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_timestamp`           bigint unsigned NOT NULL                                      DEFAULT '0' COMMENT '删除标识，0正常、非0删除',
    PRIMARY KEY (`scm_receive_exc_data_id`) USING BTREE,
    UNIQUE KEY `uk_scm_receive_exc_data_1` (`create_date`, `receive_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='scm-收货异常数据';

CREATE TABLE `scm_purchase_exc_data`
(
    `scm_purchase_exc_data_id` bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    `create_date`              date                                                          DEFAULT NULL COMMENT '创建时间',
    `supplier_code`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '供应商代码',
    `confirm_user`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟单人',
    `confirm_username`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跟单确认人',
    `timely_delivery_cnt`      int                                                           DEFAULT '0' COMMENT '准交数',
    `purchase_total`           int                                                           DEFAULT '0' COMMENT '采购数',
    `update_time`              datetime        NOT NULL                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_timestamp`            bigint unsigned NOT NULL                                      DEFAULT '0' COMMENT '删除标识，0正常、非0删除',
    PRIMARY KEY (`scm_purchase_exc_data_id`) USING BTREE,
    UNIQUE KEY `uk_scm_purchase_exc_data_1` (`create_date`, `supplier_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='scm-采购异常数据';