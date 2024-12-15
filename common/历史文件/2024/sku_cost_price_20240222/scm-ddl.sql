CREATE TABLE `batch_code_cost`
(
    `batch_code_cost_id`   bigint unsigned NOT NULL COMMENT '主键id',
    `relate_order_no`      varchar(100)    NOT NULL DEFAULT '' COMMENT '关联单号',
    `relate_order_no_type` varchar(100)    NOT NULL DEFAULT '' COMMENT '关联单号类型（加工/返修）',
    `relate_order_id`      bigint unsigned NOT NULL COMMENT '关联主键id',
    `relate_order_id_type` varchar(100)    NOT NULL DEFAULT '' COMMENT '关联主键id类型（加工/返修）',
    `sku`                  varchar(100)    NOT NULL DEFAULT '' COMMENT 'sku',
    `batch_code`           varchar(100)    NOT NULL DEFAULT '' COMMENT 'sku 批次码',
    `cost_type`            varchar(100)    NOT NULL DEFAULT '' COMMENT '成本类型（原料/人力/固定）',
    `total_amount`         decimal(10, 2)  NOT NULL DEFAULT '0.00' COMMENT '成本总金额',
    `create_time`          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`          varchar(32)     NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`      varchar(32)     NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`          varchar(32)     NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`      varchar(32)     NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`        bigint unsigned NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`              int unsigned    NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`batch_code_cost_id`) USING BTREE,
    KEY `idx_batch_code_cost_1` (`relate_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='SKU批次成本信息';

ALTER TABLE `cn_scm`.`develop_sample_order`
    ADD COLUMN `sku_batch_sample_price`   decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '批次码样品价格' AFTER `send_time`,
    ADD COLUMN `sku_batch_purchase_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '批次码大货价格' AFTER `sku_batch_sample_price`;

ALTER TABLE `cn_scm`.`develop_sample_order`
    ADD INDEX `idx_develop_sample_order_5` (`sku_batch_code` ASC) USING BTREE;

CREATE TABLE `sku_avg_price`
(
    `sku_avg_price_id`       bigint unsigned                                               NOT NULL COMMENT '主键id',
    `sku`                    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `sku_batch_code`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'sku批次码',
    `accrue_cnt`             int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '累积数',
    `accrue_price`           decimal(15, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '累积总价',
    `sku_avg_price_biz_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'sku均价业务类型',
    `create_time`            datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`            datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`          bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sku_avg_price_id`) USING BTREE,
    UNIQUE KEY `uk_sku_avg_price_id_1` (`sku`, `sku_avg_price_biz_type`) USING BTREE,
    UNIQUE KEY `uk_sku_avg_price_id_2` (`sku_batch_code`, `sku_avg_price_biz_type`) USING BTREE,
    KEY `idx_sku_avg_price_id_1` (`sku_avg_price_biz_type`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='sku均价表';