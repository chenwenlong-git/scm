use
    cn_scm;

CREATE TABLE `supplier_product_compare`
(
    `supplier_product_compare_id` bigint unsigned                                               NOT NULL COMMENT 'ID',
    `supplier_product_name`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商产品名称',
    `plm_sku_id`                  bigint                                                        NOT NULL COMMENT 'plm的产品信息表ID',
    `sku`                         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'SKU',
    `supplier_code`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商编码',
    `supplier_name`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商名称',
    `create_time`                 datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                 datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`               bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                     int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`supplier_product_compare_id`) USING BTREE,
    KEY `idx_supplier_product_compare_1` (`supplier_product_name`) USING BTREE,
    KEY `idx_supplier_product_compare_2` (`sku`) USING BTREE,
    KEY `idx_supplier_product_compare_3` (`supplier_code`) USING BTREE,
    KEY `idx_supplier_product_compare_4` (`plm_sku_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='供应商产品对照表';

CREATE TABLE `plm_sku`
(
    `plm_sku_id`               bigint unsigned                                               NOT NULL COMMENT 'ID',
    `sku`                      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'SKU',
    `binding_supplier_product` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '绑定供应商产品',
    `create_time`              datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`              datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`            bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                  int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`plm_sku_id`) USING BTREE,
    KEY `idx_plm_sku_1` (`sku`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='plm的产品表';

CREATE TABLE `process_template_material`
(
    `process_template_material_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `process_template_id`          bigint                                                        NOT NULL DEFAULT '0' COMMENT '工序模版 ID',
    `name`                         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '中文名称',
    `sku`                          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'sku',
    `num`                          int                                                           NOT NULL DEFAULT '0' COMMENT '数量',
    `create_time`                  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                      int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_template_material_id`) USING BTREE,
    KEY `idx_process_template_material_1` (`sku`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='工序模版原料表';