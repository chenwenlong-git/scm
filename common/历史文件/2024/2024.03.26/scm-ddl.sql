ALTER TABLE `cn_scm`.`purchase_child_order_raw`
    ADD COLUMN `raw_extra`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '额外原料出库:NORMAL(常规),NO_NEED_EXTRA(无需额外原料出库),' AFTER `extra_cnt`,
    ADD COLUMN `dispense_cnt` int                                                          NOT NULL DEFAULT 0 COMMENT '分配数量' AFTER `raw_extra`;



CREATE TABLE `purchase_child_order_raw_deliver`
(
    `purchase_child_order_raw_deliver_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `purchase_parent_order_no`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购母单单号',
    `purchase_child_order_no`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购子单单号',
    `sku`                                 varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `delivery_cnt`                        int                                                           NOT NULL DEFAULT '0' COMMENT '出库数',
    `raw_supplier`                        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '原料提供方:HETE(赫特),SUPPLIER(供应商),',
    `supplier_inventory_record_id`        bigint unsigned                                               NOT NULL COMMENT '库存变更id',
    `purchase_raw_deliver_order_no`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购原料发货单号',
    `create_time`                         datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                         datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                       bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                             int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`purchase_child_order_raw_deliver_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='采购需求子单原料出库单关联';