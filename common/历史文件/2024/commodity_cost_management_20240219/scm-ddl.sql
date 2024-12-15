CREATE TABLE `cost_coefficients`
(
    `cost_coefficients_id` bigint unsigned                        NOT NULL COMMENT '主键id',
    `effective_time`       datetime                               NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '生效日期',
    `coefficient`          decimal(10, 2)                         NOT NULL DEFAULT '0.00' COMMENT '固定系数',
    `create_time`          datetime                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`          varchar(32) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`      varchar(32) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`          datetime                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`          varchar(32) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`      varchar(32) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`        bigint unsigned                        NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`              int unsigned                           NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`cost_coefficients_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='固定成本系数';


ALTER TABLE `cn_scm`.`produce_data`
    ADD COLUMN `goods_purchase_price`      decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '商品采购价格' AFTER `weight`,
    ADD COLUMN `goods_purchase_price_time` datetime       NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '商品采购价格更新时间' AFTER `goods_purchase_price`;

CREATE TABLE `cost_of_goods`
(
    `cost_of_goods_id`     bigint unsigned                                               NOT NULL COMMENT '主键id',
    `sku`                  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `warehouse_code`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '仓库编码',
    `warehouse_name`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '仓库名称',
    `warehouse_types`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '仓库类型',
    `polymerize_type`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '聚合类型：单仓、多仓',
    `polymerize_warehouse` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '聚合仓库维度',
    `cost_time_type`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '成本时间类型:月、日',
    `cost_time`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '成本时间',
    `inventory`            int                                                           NOT NULL DEFAULT '0' COMMENT '库存数量',
    `inventory_price`      decimal(20, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '库存价格',
    `weighting_price`      decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '加权单价',
    `create_time`          datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`          datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`        bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`              int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`cost_of_goods_id`) USING BTREE,
    UNIQUE KEY `uk_cost_of_goods_1` (`sku`, `warehouse_code`, `polymerize_warehouse`, `cost_time`) USING BTREE,
    KEY `idx_cost_of_goods_1` (`polymerize_type`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='商品成本表';

CREATE TABLE `cost_of_goods_item`
(
    `cost_of_goods_item_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `cost_of_goods_id`      bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '商品成本id',
    `sku`                   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `warehouse_code`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '仓库编码',
    `warehouse_name`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '仓库名称',
    `business_no`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '单据号',
    `sku_batch_code`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'sku批次码',
    `sku_batch_code_time`   datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '批次码创建时间',
    `inventory`             int                                                           NOT NULL DEFAULT '0' COMMENT '库存数量',
    `sku_batch_price`       decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '批次码价格',
    `inventory_price`       decimal(20, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '库存金额',
    `create_time`           datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`           datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`         bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`               int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`cost_of_goods_item_id`) USING BTREE,
    KEY `idx_cost_of_goods_item_1` (`cost_of_goods_id`) USING BTREE,
    KEY `idx_cost_of_goods_item_2` (`sku`) USING BTREE,
    KEY `idx_cost_of_goods_item_3` (`warehouse_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='商品成本详情表';

ALTER TABLE `cn_scm`.`purchase_parent_order`
    ADD COLUMN `purchase_demand_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购需求类型' AFTER `supplier_name`;

ALTER table qc_order
    ADD COLUMN
        qc_origin          varchar(32) default '' not null comment '质检来源' after qc_type,
    ADD COLUMN
        qc_origin_property varchar(32) default '' not null comment '质检来源属性' after qc_origin;
