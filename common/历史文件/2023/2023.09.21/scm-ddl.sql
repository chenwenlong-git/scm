CREATE TABLE `process_template_process_order_description`
(
    `process_template_process_order_description_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `process_template_id`                           bigint                                                       NOT NULL DEFAULT '0' COMMENT '工序模版主键id',
    `process_desc_name`                             varchar(255) COLLATE utf8mb4_general_ci                      NOT NULL DEFAULT '' COMMENT '加工描述名称',
    `process_desc_value`                            varchar(255) COLLATE utf8mb4_general_ci                      NOT NULL DEFAULT '' COMMENT '加工描述值',
    `create_time`                                   datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`                               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                                   datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`                               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                                 bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                                       int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_template_process_order_description_id`) USING BTREE,
    KEY `idx_process_template_id` (`process_template_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='工序模板-加工单工序描述';

alter table process_template
    add column
        `details_last_updated_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '详情最后更新时间'
        after `type_value_name`;

ALTER TABLE process_template_material
    ADD COLUMN material_sku_type VARCHAR(255) DEFAULT '' NOT NULL COMMENT '原料SKU所属类型（商品SKU/辅料SKU）' AFTER `num`;

ALTER TABLE `cn_scm`.`process_order`
    ADD COLUMN `product_quality` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '产品质量，GOOD-良品，DEFECTIVE-不良品' AFTER `container_code`;

ALTER TABLE `cn_scm`.`process_order_material`
    ADD COLUMN `create_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建类型：CREATE(首次创建),SUPERPOSITION(叠加),' AFTER `back_num`;