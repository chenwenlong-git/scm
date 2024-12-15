use
    cn_scm;

ALTER TABLE `cn_scm`.`process_order`
    ADD COLUMN `parent_process_order_no` varchar(32) NOT NULL DEFAULT '' COMMENT '加工单父节点' AFTER `print_time`,
    ADD COLUMN `process_wave_id`         bigint      NOT NULL DEFAULT 0 COMMENT '加工单波次 id' AFTER `parent_process_order_no`,
    MODIFY COLUMN `process_order_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '类型，常规(NORMAL)，补单(EXTRA)，默认常规' AFTER `process_order_status`;

CREATE TABLE `sample_parent_order_process`
(
    `sample_parent_order_process_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `sample_parent_order_id`         bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '样品采购母单id',
    `sample_parent_order_no`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品采购母单号',
    `process_second_code`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '二级工序代码',
    `process_second_name`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '二级工序名称',
    `process_code`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '工序代码',
    `process_name`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '工序名称',
    `process_label`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '工序标签',
    `create_time`                    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                  bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                        int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sample_parent_order_process_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='样品需求母单工序';

CREATE TABLE `sample_child_order_process`
(
    `sample_child_order_process_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `sample_child_order_id`         bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '样品采购子单id',
    `sample_parent_order_no`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品采购母单号',
    `sample_child_order_no`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品采购子单号',
    `process_second_code`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '二级工序代码',
    `process_second_name`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '二级工序名称',
    `process_code`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '工序代码',
    `process_name`                  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '工序名称',
    `process_label`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '工序标签',
    `create_time`                   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                 bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                       int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sample_child_order_process_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='样品需求子单工序';

CREATE TABLE `sample_parent_order_process_desc`
(
    `sample_parent_order_process_desc_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `sample_parent_order_id`              bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '样品采购母单id',
    `sample_parent_order_no`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品采购母单号',
    `name`                                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '名称',
    `desc_value`                          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述值，多个以英文逗号分开',
    `create_time`                         datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                         datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                       bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                             int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sample_parent_order_process_desc_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='样品需求母单工序描述';

CREATE TABLE `sample_child_order_process_desc`
(
    `sample_child_order_process_desc_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `sample_child_order_id`              bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '样品采购子单id',
    `sample_parent_order_no`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品采购母单号',
    `sample_child_order_no`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品采购子单号',
    `name`                               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '名称',
    `desc_value`                         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述值，多个以英文逗号分开',
    `create_time`                        datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                        datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                      bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                            int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sample_child_order_process_desc_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='样品需求子单工序描述';

CREATE TABLE `sample_parent_order_raw`
(
    `sample_parent_order_raw_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `sample_parent_order_id`     bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '样品采购母单id',
    `sample_parent_order_no`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品采购母单号',
    `sku`                        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `delivery_cnt`               int                                                           NOT NULL DEFAULT '0' COMMENT '出库数',
    `create_time`                datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`              bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                    int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sample_parent_order_raw_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='样品需求母单原料';

CREATE TABLE `sample_child_order_raw`
(
    `sample_child_order_raw_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `sample_child_order_id`     bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '样品采购子单id',
    `sample_parent_order_no`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品采购母单号',
    `sample_child_order_no`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品采购子单号',
    `sku`                       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `sku_batch_code`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'sku批次码',
    `delivery_cnt`              int                                                           NOT NULL DEFAULT '0' COMMENT '出库数',
    `sample_raw_biz_type`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品原料业务类型',
    `create_time`               datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`               datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`             bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                   int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sample_child_order_raw_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='样品需求子单原料';

ALTER TABLE `cn_scm`.`purchase_raw_receipt_order`
    ADD COLUMN `raw_receipt_biz_type`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '原料收货业务类型' AFTER `supplier_name`,
    ADD COLUMN `sample_child_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '样品子单单号' AFTER `raw_receipt_biz_type`;

ALTER TABLE `cn_scm`.`sample_parent_order`
    ADD COLUMN `is_sample` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否打样' AFTER `defective_sample_child_order_no`;


ALTER TABLE `cn_scm`.`sample_parent_order`
    ADD COLUMN `raw_warehouse_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '原料仓库' AFTER `is_sample`,
    ADD COLUMN `raw_warehouse_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '原料仓库' AFTER `raw_warehouse_code`;

ALTER TABLE `cn_scm`.`sample_child_order`
    ADD COLUMN `raw_warehouse_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '原料仓库' AFTER `sample_receipt_order_no`,
    ADD COLUMN `raw_warehouse_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '原料仓库' AFTER `raw_warehouse_code`,
    ADD COLUMN `sample_dev_type`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '开发类型' AFTER `raw_warehouse_name`;

ALTER TABLE `cn_scm`.`purchase_child_order_raw`
    MODIFY COLUMN `delivery_cnt` int NOT NULL DEFAULT 0 COMMENT '出库数' AFTER `sku_batch_code`;


ALTER TABLE `cn_scm`.`deduct_order_purchase`
    MODIFY COLUMN `sku` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku' AFTER `deduct_order_purchase_type`,
    MODIFY COLUMN `spu` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'spu' AFTER `sku`;

ALTER TABLE `cn_scm`.`deduct_order_quality`
    MODIFY COLUMN `sku` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku' AFTER `deduct_order_purchase_type`,
    MODIFY COLUMN `spu` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'spu' AFTER `sku`;

ALTER TABLE `cn_scm`.`supplement_order_purchase`
    MODIFY COLUMN `spu` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'spu' AFTER `supplement_order_purchase_type`,
    MODIFY COLUMN `sku` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku' AFTER `spu`;

ALTER TABLE `cn_scm`.`process_material_receipt_item`
    MODIFY COLUMN `sku` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku' AFTER `process_material_receipt_id`;
ALTER TABLE `cn_scm`.`process_order_item`
    MODIFY COLUMN `sku` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku' AFTER `process_order_no`;
ALTER TABLE `cn_scm`.`process_order_material`
    MODIFY COLUMN `sku` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku' AFTER `process_order_no`;
ALTER TABLE `cn_scm`.`process_template_material`
    MODIFY COLUMN `sku` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku' AFTER `name`;