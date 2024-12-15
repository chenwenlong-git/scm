use
    cn_scm;


ALTER TABLE `cn_scm`.`process_order_extra`
    ADD COLUMN `processing_user`     varchar(32) NOT NULL DEFAULT '' COMMENT '进入加工中操作人' AFTER `handled_time`,
    ADD COLUMN `processing_username` varchar(32) NOT NULL DEFAULT '' COMMENT '进入加工中操作人名称' AFTER `processing_user`,
    ADD COLUMN `processing_time`     datetime    NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '进入加工中时间' AFTER `processing_username`;

ALTER TABLE `cn_scm`.`process_order_procedure`
    ADD COLUMN `process_label` varchar(32) NOT NULL DEFAULT '' COMMENT '工序标签' AFTER `process_name`;

ALTER TABLE `cn_scm`.`process_order`
    ADD COLUMN `current_process_label` varchar(32) NOT NULL DEFAULT '' COMMENT '当前工序' AFTER `file_code`,
    ADD COLUMN `material_back_status`  varchar(32) NOT NULL DEFAULT '' COMMENT '原料归还状态' AFTER `current_process_label`;


ALTER TABLE `cn_scm`.`process_order_material`
    ADD COLUMN `back_num`       int         NOT NULL DEFAULT 0 COMMENT '归还数量' AFTER `delivery_num`,
    ADD COLUMN `sku_batch_code` varchar(32) NOT NULL DEFAULT '' COMMENT 'sku 批次码' AFTER `sku`;


ALTER TABLE `cn_scm`.`process_order_scan`
    ADD COLUMN `process_label` varchar(32) NOT NULL DEFAULT '' COMMENT '关联的工序标签' AFTER `process_order_no`;

CREATE TABLE `process_material_back`
(
    `process_material_back_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `message_key`              varchar(100) COLLATE utf8mb4_general_ci                      NOT NULL DEFAULT '' COMMENT '消息 key',
    `process_order_no`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联的加工单',
    `receipt_no`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货单编号',
    `back_status`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '归还状态',
    `receipt_user`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货人',
    `receipt_username`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货人',
    `receipt_time`             datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '收货时间',
    `create_time`              datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`              datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`            bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                  int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_material_back_id`) USING BTREE,
    KEY `idx_process_material_back_1` (`process_order_no`) USING BTREE,
    KEY `idx_process_material_back_2` (`receipt_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='加工原料归还表';

CREATE TABLE `process_material_back_item`
(
    `process_material_back_item_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `process_material_back_id`      bigint unsigned                                              NOT NULL COMMENT '关联的原料归还表',
    `sku_batch_code`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku 批次码',
    `delivery_num`                  int                                                          NOT NULL DEFAULT '0' COMMENT '发货数量',
    `receipt_num`                   int unsigned                                                 NOT NULL DEFAULT '0' COMMENT '收货数量',
    `create_time`                   datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                   datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                 bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                       int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_material_back_item_id`) USING BTREE,
    KEY `idx_process_material_back_item_1` (`process_material_back_id`),
    KEY `idx_process_material_back_item_2` (`sku_batch_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='加工单原料归还明细表';


ALTER TABLE `cn_scm`.`purchase_settle_order`
    DROP INDEX `uk_purchase_settle_order_1`,
    ADD UNIQUE INDEX `uk_purchase_settle_order_1` (`supplier_code` ASC, `month` ASC, `del_timestamp` ASC) USING BTREE;

ALTER TABLE `cn_scm`.`process_settle_order`
    DROP INDEX `uk_process_settle_order_1`,
    ADD UNIQUE INDEX `uk_process_settle_order_1` (`month` ASC, `del_timestamp` ASC) USING BTREE;


ALTER TABLE `cn_scm`.`process`
    ADD COLUMN `process_label` varchar(32) NOT NULL DEFAULT '' COMMENT '工序标签' AFTER `process_id`;
