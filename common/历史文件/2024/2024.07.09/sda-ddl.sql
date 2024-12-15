CREATE TABLE `scm_sku_stockout_data`
(
    `scm_sku_stockout_data_id` bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    `create_date`              date                                                          DEFAULT NULL COMMENT '创建时间',
    `sku`                      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'SKU',
    `stockout_amount`          int unsigned                                                  DEFAULT '0' COMMENT '总缺货数量',
    `update_time`              datetime        NOT NULL                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_timestamp`            bigint unsigned NOT NULL                                      DEFAULT '0' COMMENT '删除标识，0正常、非0删除',
    PRIMARY KEY (`scm_sku_stockout_data_id`) USING BTREE,
    UNIQUE KEY `uk_scm_sku_stockout_data_1` (`create_date`, `sku`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='scm-sku缺货数据(日维度)';

CREATE TABLE `scm_sku_stockout`
(
    `scm_sku_stockout_id` bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    `create_date`         date                                                          DEFAULT NULL COMMENT '创建时间',
    `sku`                 varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'SKU',
    `order_no`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '单据号',
    `order_type`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '单据类型',
    `order_create_time`   datetime        NOT NULL                                      DEFAULT CURRENT_TIMESTAMP COMMENT '单据创建时间',
    `deliver_date`        datetime        NOT NULL                                      DEFAULT '1970-01-01 00:00:00' COMMENT '业务约定交期',
    `update_time`         datetime        NOT NULL                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_timestamp`       bigint unsigned NOT NULL                                      DEFAULT '0' COMMENT '删除标识，0正常、非0删除',
    PRIMARY KEY (`scm_sku_stockout_id`) USING BTREE,
    UNIQUE KEY `uk_scm_sku_stockout_1` (`create_date`, `sku`, `order_type`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='scm-sku缺货数据';

CREATE TABLE `scm_sku_stockout_reason`
(
    `scm_sku_stockout_reason_id` bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    `create_date`                date                                                          DEFAULT NULL COMMENT '创建时间',
    `sku`                        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'SKU',
    `reason`                     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '缺货原因',
    `stockout_amount`            int unsigned                                                  DEFAULT '0' COMMENT '缺货数量',
    `stockout_day`               int unsigned                                                  DEFAULT '0' COMMENT '本月缺货天数',
    `duty_dept`                  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '责任部门',
    `update_time`                datetime        NOT NULL                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_timestamp`              bigint unsigned NOT NULL                                      DEFAULT '0' COMMENT '删除标识，0正常、非0删除',
    PRIMARY KEY (`scm_sku_stockout_reason_id`) USING BTREE,
    UNIQUE KEY `uk_scm_sku_stockout_reason_1` (`create_date`, `sku`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='scm-sku缺货原因';

CREATE TABLE `scm_sku_order_stockout_reason`
(
    `scm_sku_order_stockout_reason_id` bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    `create_date`                      date                                                          DEFAULT NULL COMMENT '创建时间',
    `sku`                              varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'SKU',
    `order_no`                         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '单据号',
    `reason`                           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '缺货原因',
    `stockout_amount`                  int unsigned                                                  DEFAULT '0' COMMENT '缺货数量',
    `duty_dept`                        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '责任部门',
    `update_time`                      datetime        NOT NULL                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_timestamp`                    bigint unsigned NOT NULL                                      DEFAULT '0' COMMENT '删除标识，0正常、非0删除',
    PRIMARY KEY (`scm_sku_order_stockout_reason_id`) USING BTREE,
    UNIQUE KEY `uk_scm_sku_order_stockout_reason_1` (`create_date`, `sku`, `order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='scm-sku+订单缺货原因';

CREATE TABLE `scm_sku_stockout_month_reason`
(
    `scm_sku_stockout_month_reason_id` bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    `create_date`                      date                                                          DEFAULT NULL COMMENT '创建时间',
    `sku`                              varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'SKU',
    `reason`                           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '缺货原因',
    `duty_dept`                        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '责任部门',
    `stockout_day`                     int unsigned                                                  DEFAULT '0' COMMENT '本月缺货天数',
    `update_time`                      datetime        NOT NULL                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_timestamp`                    bigint unsigned NOT NULL                                      DEFAULT '0' COMMENT '删除标识，0正常、非0删除',
    PRIMARY KEY (`scm_sku_stockout_month_reason_id`) USING BTREE,
    UNIQUE KEY `uk_scm_sku_stockout_month_reason_1` (`create_date`, `sku`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='scm-月度sku缺货原因';