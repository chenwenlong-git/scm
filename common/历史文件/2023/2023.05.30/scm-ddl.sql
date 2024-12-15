use
    cn_scm;

CREATE TABLE `cn_scm`.`process_order_sample`
(
    `process_order_sample_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `process_order_no`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '关联的加工单',
    `sample_child_order_no`   varchar(32) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '样品采购子单号',
    `sample_info_key`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '属性key',
    `sample_info_value`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '属性值',
    `create_time`             datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`             datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`           bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                 int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_order_sample_id`) USING BTREE,
    KEY `idx_process_order_sample_1` (`process_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='加工单生产信息表';


ALTER TABLE `cn_scm`.`plm_sku`
    ADD COLUMN `cycle` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '生产周期' AFTER `binding_supplier_product`;

ALTER TABLE `cn_scm`.`purchase_modify_order`
    MODIFY COLUMN `down_return_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货状态:WAIT_RECEIVE(待收货),RECEIPTED(已收货),' AFTER `purchase_child_order_no`;