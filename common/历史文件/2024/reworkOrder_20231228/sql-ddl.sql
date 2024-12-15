CREATE TABLE `repair_order`
(
    `repair_order_id`              bigint unsigned                                               NOT NULL COMMENT '主键id',
    `repair_order_no`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '返修单号',
    `expect_process_num`           int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '预计加工数',
    `expect_complete_process_time` datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '预计加工完成时间',
    `is_receive_material`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '是否回料（是/否）',
    `missing_information`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '缺失信息（无库存）',
    `repair_order_status`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '返修单状态',
    `confirm_complete_user`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '确认返修单完成人id',
    `confirm_complete_username`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '确认返修单完成人名称',
    `confirm_complete_time`        datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '确认返修单完成时间',
    `delivery_num`                 int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '发货数量',
    `expect_warehouse_code`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '预期入库仓库编码',
    `expect_warehouse_name`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '预期入库仓库名称',
    `receive_order_no`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '收货单号',
    `plan_no`                      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '计划单号',
    `plan_type`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '计划单类型（单品单件/单品多件）',
    `plan_title`                   varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '计划单号标题',
    `platform`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '需求平台编码',
    `plan_create_user`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '计划单创建人id',
    `plan_create_username`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '计划单创建人名称',
    `plan_create_time`             datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '计划单创建时间',
    `sale_price`                   decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '销售价格',
    `plan_remark`                  varchar(500) COLLATE utf8mb4_general_ci                       NOT NULL DEFAULT '' COMMENT '计划单备注',
    `create_time`                  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                      int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`repair_order_id`) USING BTREE,
    UNIQUE KEY `uk_repair_order_no` (`repair_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='返修单';


CREATE TABLE `repair_order_item`
(
    `repair_order_item_id`       bigint unsigned                                              NOT NULL COMMENT '主键id',
    `repair_order_no`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '返修单号',
    `spu`                        varchar(32) COLLATE utf8mb4_general_ci                       NOT NULL DEFAULT '' COMMENT 'spu',
    `sku`                        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `batch_code`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '批次码',
    `expect_process_num`         int unsigned                                                 NOT NULL DEFAULT '0' COMMENT '预计加工数',
    `act_processed_complete_cnt` int unsigned                                                 NOT NULL DEFAULT '0' COMMENT '实际加工完成数',
    `act_process_scrap_cnt`      int unsigned                                                 NOT NULL DEFAULT '0' COMMENT '实际加工报废数',
    `create_time`                datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`              bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                    int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`repair_order_item_id`) USING BTREE,
    KEY `idx_repair_order_item_1` (`repair_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='返修单明细';

CREATE TABLE `repair_order_result`
(
    `repair_order_result_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `repair_order_no`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '返修单号',
    `repair_order_item_id`   bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '返修明细id',
    `batch_code`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '批次码',
    `material_batch_code`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '使用原料批次码',
    `spu`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'spu',
    `sku`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `completed_quantity`     int unsigned                                                 NOT NULL DEFAULT '0' COMMENT '完成数量',
    `repair_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '返修人id',
    `repair_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '返修人名称',
    `repair_time`            datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '返修时间',
    `qc_pass_quantity`       int unsigned                                                 NOT NULL DEFAULT '0' COMMENT '质检通过数量',
    `qc_fail_quantity`       int unsigned                                                 NOT NULL DEFAULT '0' COMMENT '质检不通过数量',
    `create_time`            datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`            datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`          bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`repair_order_result_id`) USING BTREE,
    KEY `idx_repair_order_result_1` (`repair_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='返修单结果表';


ALTER TABLE process_order_material
    COMMENT '加工原料表';

alter table process_order_material
    ADD COLUMN repair_order_no VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '返修单号' after process_order_no,
    ADD COLUMN warehouse_code  VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '原料仓库编码' after sku_batch_code,
    ADD COLUMN shelf_code      VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '库位号/货架号' after warehouse_code,
    ADD COLUMN product_quality VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '产品质量，GOOD-良品，DEFECTIVE-不良品' after shelf_code;

ALTER TABLE `process_order_material`
    ADD INDEX `idx_process_order_material_2` (`delivery_no`) USING BTREE;

alter table process_material_receipt
    ADD COLUMN
        repair_order_no              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '返修单号' after process_order_no,
    ADD COLUMN material_receipt_type VARCHAR(50)                                                  NOT NULL DEFAULT '' COMMENT '原料收货类型（加工原料/REPAIR_MATERIAL）' after repair_order_no;
ALTER TABLE `process_material_receipt`
    ADD INDEX `idx_process_material_receipt_3` (`repair_order_no`) USING BTREE;

alter table qc_order
    add column `repair_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '返修单号' after receive_order_no;
ALTER TABLE `qc_order`
    ADD INDEX `idx_qc_order_4` (`repair_order_no`) USING BTREE;