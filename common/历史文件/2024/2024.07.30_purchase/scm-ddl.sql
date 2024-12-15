CREATE TABLE `scm_purchase_deliver_data`
(
    `scm_purchase_deliver_data_id` bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    `create_date`                  date                                                          DEFAULT NULL COMMENT '创建时间',
    `purchase_child_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '采购订单号',
    `purchase_total`               int unsigned                                                  DEFAULT NULL COMMENT '采购数量',
    `purchase_parent_order_no`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '采购母单单号',
    `purchase_deliver_order_no`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '采购发货单号',
    `purchase_receipt_order_no`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '采购收货单号',
    `warehouse_code`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '仓库编码',
    `warehouse_name`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '仓库名称',
    `supplier_code`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '供应商编码',
    `sku`                          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'sku',
    `sku_encode`                   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'cn_plm.goods_sku.sku_encode',
    `purchase_order_status`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '采购单状态',
    `platform`                     varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '平台',
    `warehousing_time`             datetime                                                      DEFAULT NULL COMMENT '入库时间',
    `expected_on_shelves_date`     datetime                                                      DEFAULT NULL COMMENT '期望上架时间',
    `deliver_cnt`                  int unsigned                                                  DEFAULT NULL COMMENT '发货数',
    `receipt_cnt`                  int unsigned                                                  DEFAULT NULL COMMENT '收货数',
    `quality_goods_cnt`            int unsigned                                                  DEFAULT NULL COMMENT '正品数',
    `update_time`                  datetime        NOT NULL                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_timestamp`                bigint unsigned NOT NULL                                      DEFAULT '0' COMMENT '删除标识，0正常、非0删除',
    PRIMARY KEY (`scm_purchase_deliver_data_id`) USING BTREE,
    UNIQUE KEY `uk_scm_purchase_deliver_data_1` (`create_date`, `purchase_deliver_order_no`, `sku`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='scm-采购订单-发货单维度统计';