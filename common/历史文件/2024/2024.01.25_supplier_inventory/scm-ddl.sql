CREATE TABLE `supplier_inventory`
(
    `supplier_inventory_id`  bigint unsigned                                               NOT NULL COMMENT '主键id',
    `supplier_code`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商代码',
    `supplier_name`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商名称',
    `spu`                    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'spu',
    `sku`                    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'SKU',
    `category_id`            bigint                                                        NOT NULL DEFAULT '0' COMMENT '商品品类 ID',
    `category_name`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类名(一级或二级名称)',
    `stock_up_inventory`     int                                                           NOT NULL DEFAULT '0' COMMENT '备货库存',
    `self_provide_inventory` int                                                           NOT NULL DEFAULT '0' COMMENT '自备库存',
    `create_time`            datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`            datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`          bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`supplier_inventory_id`) USING BTREE,
    UNIQUE KEY `uk_supplier_inventory_1` (`supplier_code`, `sku`) USING BTREE,
    KEY `idx_supplier_inventory_1` (`supplier_code`, `supplier_name`) USING BTREE,
    KEY `idx_supplier_inventory_2` (`sku`) USING BTREE,
    KEY `idx_supplier_inventory_3` (`category_name`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='供应商库存表';

CREATE TABLE `supplier_inventory_record`
(
    `supplier_inventory_record_id`   bigint unsigned                                               NOT NULL COMMENT '主键id',
    `supplier_code`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商代码',
    `supplier_name`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商名称',
    `supplier_warehouse`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商仓库：STOCK_UP(备货仓),SELF_PROVIDE(自备仓),',
    `spu`                            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'SPU',
    `sku`                            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'SKU',
    `category_id`                    bigint                                                        NOT NULL DEFAULT '0' COMMENT '商品品类 ID',
    `category_name`                  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类名(一级或二级名称)',
    `supplier_inventory_ctrl_type`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '操作类型：CHECK(盘点),OUTBOUND(出库),WAREHOUSING(入库),',
    `before_inventory`               int                                                           NOT NULL DEFAULT '0' COMMENT '操作前库存',
    `ctrl_cnt`                       int                                                           NOT NULL DEFAULT '0' COMMENT '操作数量',
    `after_inventory`                int                                                           NOT NULL DEFAULT '0' COMMENT '操作后库存',
    `supplier_inventory_ctrl_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作原因',
    `relate_no`                      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '关联单据号',
    `record_remark`                  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '库存变更备注',
    `create_time`                    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                  bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                        int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`supplier_inventory_record_id`) USING BTREE,
    KEY `idx_supplier_inventory_record_1` (`supplier_code`, `supplier_name`) USING BTREE,
    KEY `idx_supplier_inventory_record_2` (`sku`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='供应商库存记录表';

CREATE TABLE `stock_up_order`
(
    `stock_up_order_id`         bigint unsigned                                               NOT NULL COMMENT '主键id',
    `stock_up_order_no`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '备货单号',
    `stock_up_order_status`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '备货单状态:TO_BE_FOLLOW_CONFIRM(待跟单确认),TO_BE_ACCEPT(待接单),IN_PROGRESS(进行中),FINISH(已完结),CANCEL(已取消),',
    `sku`                       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'SKU',
    `category_name`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类名(一级或二级名称)',
    `stock_up_price`            decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '备货单价',
    `place_order_cnt`           int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '下单数',
    `request_return_goods_date` datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '要求回货时间',
    `supplier_code`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商代码',
    `supplier_name`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商名称',
    `follow_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '跟单人',
    `follow_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '跟单人',
    `follow_remark`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '跟单备注',
    `follow_date`               datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '跟单时间',
    `finish_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '完结人',
    `finish_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '完结人',
    `finish_date`               datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '完结时间',
    `create_time`               datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`               datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`             bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                   int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`stock_up_order_id`) USING BTREE,
    UNIQUE KEY `uk_stock_up_order_1` (`stock_up_order_no`) USING BTREE,
    KEY `idx_stock_up_order_1` (`supplier_code`, `supplier_name`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='备货单表';

CREATE TABLE `stock_up_order_item`
(
    `stock_up_order_item_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `stock_up_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '备货单号',
    `sku`                    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'SKU',
    `warehousing_cnt`        int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '入库数',
    `return_goods_cnt`       int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '回货数',
    `return_goods_date`      datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '回货时间',
    `create_time`            datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`            datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`          bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`stock_up_order_item_id`) USING BTREE,
    KEY `idx_stock_up_order_item_1` (`stock_up_order_no`) USING BTREE,
    KEY `idx_stock_up_order_item_2` (`sku`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='备货单项目表';

CREATE TABLE `supplier_warehouse`
(
    `supplier_warehouse_id` bigint unsigned                                              NOT NULL COMMENT '主键ID',
    `supplier_code`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商代码',
    `supplier_name`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商名称',
    `warehouse_code`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '仓库编码',
    `warehouse_name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '仓库名称',
    `supplier_warehouse`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '仓库类型',
    `create_time`           datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`           datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`         bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`               int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`supplier_warehouse_id`) USING BTREE,
    UNIQUE KEY `uk_supplier_warehouse_1` (`supplier_code`, `warehouse_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='供应商仓库表';

ALTER TABLE `cn_scm`.`purchase_child_order`
    ADD COLUMN `sku_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku类型' AFTER `spu`;


ALTER TABLE `cn_scm`.`purchase_child_order_raw`
    ADD COLUMN `actual_consume_cnt` int NOT NULL DEFAULT 0 COMMENT '实际出库数' AFTER `raw_supplier`,
    ADD COLUMN `extra_cnt`          int NOT NULL DEFAULT 0 COMMENT '额外消耗数' AFTER `actual_consume_cnt`;

ALTER TABLE `cn_scm`.`purchase_parent_order`
    ADD COLUMN `supplier_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商编码' AFTER `can_split_cnt`,
    ADD COLUMN `supplier_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商名称' AFTER `supplier_code`;

CREATE TABLE `raw_purchase_parent_relate`
(
    `raw_purchase_parent_relate_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `purchase_child_order_no`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购子单单号',
    `raw_purchase_parent_order_no`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '原料采购母单单号',
    `create_time`                   datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                   datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                 bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                       int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`raw_purchase_parent_relate_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='采购子单与原料采购母单关联表';