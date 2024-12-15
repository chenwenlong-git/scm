CREATE TABLE `finance_reco_order_item_relation`
(
    `finance_reco_order_item_relation_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `finance_reco_order_item_id`          bigint                                                        NOT NULL COMMENT '关联财务对账单明细ID',
    `finance_reco_order_item_sku_id`      bigint                                                        NOT NULL COMMENT '关联财务对账单明细SKU ID',
    `finance_reco_order_no`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '对账单号',
    `reco_order_item_relation_type`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '关联单据类型',
    `business_id`                         bigint                                                        NOT NULL COMMENT '关联单据 ID',
    `business_no`                         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '关联单据号',
    `sku`                                 varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `num`                                 int                                                           NOT NULL DEFAULT '0' COMMENT '数量',
    `total_price`                         decimal(13, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '金额(总金额)',
    `create_time`                         datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                         datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                       bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                             int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`finance_reco_order_item_relation_id`) USING BTREE,
    KEY `idx_finance_reco_order_item_relation_1` (`finance_reco_order_item_id`) USING BTREE,
    KEY `idx_finance_reco_order_item_relation_2` (`finance_reco_order_item_sku_id`) USING BTREE,
    KEY `idx_finance_reco_order_item_relation_3` (`finance_reco_order_no`) USING BTREE,
    KEY `idx_finance_reco_order_item_relation_4` (`business_id`) USING BTREE,
    KEY `idx_finance_reco_order_item_relation_5` (`business_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='财务对账单明细SKU关联使用单据表';