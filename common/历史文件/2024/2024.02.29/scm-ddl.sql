ALTER TABLE `cn_scm`.`supplier`
    ADD COLUMN `supplier_alias` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商别名' AFTER `logistics_aging`;

ALTER TABLE `cn_scm`.`produce_data_item`
    ADD COLUMN `bom_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'BOM名称' AFTER `purchase_price`;


CREATE TABLE `produce_data_item_supplier`
(
    `produce_data_item_supplier_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `produce_data_item_id`          bigint                                                        NOT NULL COMMENT '生产信息详情ID',
    `spu`                           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'spu',
    `sku`                           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `supplier_code`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商代码',
    `supplier_name`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商名称',
    `create_time`                   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                 bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                       int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`produce_data_item_supplier_id`) USING BTREE,
    KEY `idx_produce_data_item_supplier_1` (`produce_data_item_id`) USING BTREE,
    KEY `idx_produce_data_item_supplier_2` (`sku`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='生产信息详情关联供应商';

ALTER TABLE `cn_scm`.`supplier_product_compare`
    MODIFY COLUMN `supplier_product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商产品名称' AFTER `supplier_product_compare_id`;