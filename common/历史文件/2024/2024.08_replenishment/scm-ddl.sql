ALTER TABLE `cn_scm`.`sku_info`
    ADD COLUMN `single_capacity` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '单件产能' AFTER `goods_price_maintain`;

ALTER TABLE `cn_scm`.`supplier_inventory`
    MODIFY COLUMN `stock_up_inventory` int NOT NULL DEFAULT 0 COMMENT '可用-备货库存' AFTER `category_name`,
    MODIFY COLUMN `self_provide_inventory` int NOT NULL DEFAULT 0 COMMENT '可用-自备库存' AFTER `stock_up_inventory`,
    MODIFY COLUMN `defective_inventory` int NOT NULL DEFAULT 0 COMMENT '可用-不良库存' AFTER `self_provide_inventory`,
    ADD COLUMN `frz_stock_up_inventory`     int NOT NULL DEFAULT 0 COMMENT '冻结-备货库存' AFTER `defective_inventory`,
    ADD COLUMN `frz_self_provide_inventory` int NOT NULL DEFAULT 0 COMMENT '冻结-自备库存' AFTER `frz_stock_up_inventory`,
    ADD COLUMN `frz_defective_inventory`    int NOT NULL DEFAULT 0 COMMENT '冻结-不良库存' AFTER `frz_self_provide_inventory`;


ALTER TABLE `cn_scm`.`purchase_child_order`
    ADD COLUMN `capacity`             decimal(10, 2)                                               NOT NULL DEFAULT 0.00 COMMENT '供应商产能' AFTER `adjust_price_approve_no`,
    ADD COLUMN `order_source`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '下单方式：SCM(scm系统),WMS_REPLENISH(wms备货中心-轮询监控页面),WMS_REPLENISH_RECOMMEND(wms备货中心-推荐下单页面),' AFTER `capacity`,
    ADD COLUMN `place_order_user`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '下单人' AFTER `order_source`,
    ADD COLUMN `place_order_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '下单人名称' AFTER `place_order_user`;

ALTER TABLE `cn_scm`.`purchase_parent_order`
    ADD COLUMN `place_order_user`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '下单人' AFTER `develop_child_order_no`,
    ADD COLUMN `place_order_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '下单人名称' AFTER `place_order_user`;

ALTER TABLE `cn_scm`.`purchase_child_order_raw_deliver`
    ADD COLUMN `particular_location` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否指定库位:TRUE,FALSE' AFTER `dispense_cnt`;

CREATE TABLE `supplier_capacity_rule`
(
    `supplier_capacity_rule_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `supplier_code`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商代码',
    `capacity_type`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '产能类型，常规(NORMAL)',
    `capacity`                  decimal(10, 2)                                               NOT NULL DEFAULT 0.00 COMMENT '产能',
    `period`                    int                                                          NOT NULL DEFAULT '0' COMMENT '规则循环周期',
    `create_time`               datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`               datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`             bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                   int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`supplier_capacity_rule_id`) USING BTREE,
    KEY `idx_supplier_capacity_rule_1` (`supplier_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='供应商产能规则表';

CREATE TABLE `supplier_capacity`
(
    `supplier_capacity_id`      bigint unsigned                                              NOT NULL COMMENT '主键id',
    `supplier_capacity_rule_id` bigint unsigned                                              NOT NULL COMMENT '供应商产能规则id',
    `supplier_code`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商代码',
    `capacity_date`             date                                                         NOT NULL DEFAULT '1970-01-01' COMMENT '产能日期',
    `total_normal_capacity`     decimal(10, 2)                                               NOT NULL DEFAULT 0.00 COMMENT '总标准产能',
    `normal_available_capacity` decimal(10, 2)                                               NOT NULL DEFAULT 0.00 COMMENT '可用标准产能',
    `create_time`               datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`               datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`             bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                   int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`supplier_capacity_id`) USING BTREE,
    KEY `idx_supplier_capacity_1` (`supplier_code`, `capacity_date`) USING BTREE,
    UNIQUE KEY `uk_supplier_capacity_1` (`supplier_code`, `capacity_date`, `del_timestamp`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='供应商产能表';

CREATE TABLE `supplier_capacity_log`
(
    `supplier_capacity_log_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `supplier_capacity_id`     bigint unsigned                                              NOT NULL COMMENT '供应商产能id',
    `biz_type`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '来源类型',
    `biz_no`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '来源单号',
    `operate_capacity`         decimal(10, 2)                                               NOT NULL DEFAULT 0.00 COMMENT '操作产能',
    `create_time`              datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`              datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`            bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                  int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`supplier_capacity_log_id`) USING BTREE,
    KEY `idx_supplier_capacity_log_1` (`supplier_capacity_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='供应商产能日志表';

CREATE TABLE `supplier_rest`
(
    `supplier_rest_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `supplier_code`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商代码',
    `rest_date`        date                                                         NOT NULL DEFAULT '1970-01-01' COMMENT '停工日期',
    `create_time`      datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`      datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`    bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`          int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`supplier_rest_id`) USING BTREE,
    KEY `idx_supplier_rest_1` (`supplier_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='供应商停工时间表';
