ALTER TABLE `cn_scm`.`supplier_product_compare`
    ADD COLUMN `supplier_product_compare_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '状态:启用(TRUE)、禁用(FALSE)' AFTER `supplier_name`,
    MODIFY COLUMN `plm_sku_id` bigint NOT NULL DEFAULT 0 COMMENT 'plm的产品信息表ID（不维护）' AFTER `supplier_product_name`;

ALTER TABLE produce_data
    add tolerance decimal(10, 2) default 0.00 not null comment '公差' after raw_manage;

-- 1.供应链属性分类表
CREATE TABLE `attribute_category`
(
    `attribute_category_id`        bigint unsigned                                               NOT NULL COMMENT '主键id',
    `attribute_category_name`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '属性类型名称',
    `parent_attribute_category_id` bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '父级属性类型主键id',
    `create_time`                  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                      int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`attribute_category_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='供应链属性分类表';

-- 2.供应链属性表
CREATE TABLE `attribute`
(
    `attribute_id`            bigint unsigned                                               NOT NULL COMMENT '主键id',
    `attribute_name`          varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '属性名称',
    `attribute_category_id`   bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '属性分类ID',
    `attribute_category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '属性分类名称',
    `input_type`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '录入类型',
    `is_required`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '是否必填',
    `status`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '状态',
    `scope`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '数据作用范围（数据维度）',
    `create_time`             datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`             datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`           bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                 int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`attribute_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='供应链属性表';

-- 3.供应链属性可选值表
CREATE TABLE `attribute_option`
(
    `attribute_option_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `attribute_id`        bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '属性分类ID',
    `attribute_name`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '属性名称',
    `attribute_value`     varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '属性值',
    `create_time`         datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`         datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`       bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`             int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`attribute_option_id`) USING BTREE,
    KEY `idx_attribute_option_1` (`attribute_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='供应链属性可选值表';

-- 4.商品分类与业务关联表
CREATE TABLE `plm_category_relate`
(
    `plm_category_relate_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `category_id`            bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT 'plm商品分类id',
    `category_name`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'plm商品分类名称',
    `biz_id`                 bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '业务id',
    `biz_type`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '业务类型',
    `create_time`            datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`            datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`          bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`plm_category_relate_id`) USING BTREE,
    KEY `idx_plm_category_relate_1` (`biz_type`, `category_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='商品分类与业务关联表';

-- 5.商品属性表
CREATE TABLE `sku_attribute`
(
    `sku_attribute_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `sku`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku编码',
    `attribute_id`     bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '供应链属性主键id',
    `attribute_name`   varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应链属性名称',
    `create_time`      datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`      datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`    bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`          int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sku_attribute_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='商品属性表';

-- 6.商品属性值表
CREATE TABLE `sku_attribute_value`
(
    `sku_attribute_value_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `sku_attribute_id`       bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '商品属性id',
    `value_id`               bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '属性值来源id',
    `value`                  mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '属性值',
    `create_time`            datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`            datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`          bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sku_attribute_value_id`) USING BTREE,
    KEY `idx_sku_attribute_value_1` (`sku_attribute_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='商品属性值表';

-- 7.供应商商品属性表
CREATE TABLE `supplier_sku_attribute`
(
    `supplier_sku_attribute_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `supplier_code`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商代码',
    `sku`                       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku编码',
    `attribute_id`              bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '供应链属性主键id',
    `attribute_name`            varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应链属性名称',
    `create_time`               datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`               datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`             bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                   int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`supplier_sku_attribute_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='供应商商品属性表';

-- 8.供应商商品属性值表
CREATE TABLE `supplier_sku_attribute_value`
(
    `supplier_sku_attribute_value_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `supplier_sku_attribute_id`       bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '供应商商品属性id',
    `value_id`                        bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '属性值来源id',
    `value`                           mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '属性值',
    `create_time`                     datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                     datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                   bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                         int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`supplier_sku_attribute_value_id`) USING BTREE,
    KEY `idx_supplier_sku_attribute_value_1` (`supplier_sku_attribute_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='供应商商品属性值表';

-- 9.供应商商品原料属性
CREATE TABLE `supplier_sku_material_attribute`
(
    `supplier_sku_material_attribute_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `supplier_code`                      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商代码',
    `sku`                                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku编码',
    `crotch_length`                      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '裆长尺寸',
    `crotch_position`                    varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '裆长部位',
    `dark_weight`                        decimal(8, 2)                                                 NOT NULL DEFAULT '0.00' COMMENT '深色克重',
    `light_weight`                       decimal(8, 2)                                                 NOT NULL DEFAULT '0.00' COMMENT '浅色克重',
    `crotch_length_ratio`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '裆长配比',
    `weight`                             decimal(8, 2)                                                 NOT NULL DEFAULT '0.00' COMMENT '克重',
    `create_time`                        datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                        datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                      bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                            int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`supplier_sku_material_attribute_id`) USING BTREE,
    KEY `idx_supplier_sku_material_attribute_1` (`supplier_code`, `sku`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='供应商商品原料属性表';

-- 10.供应商商品工艺属性
CREATE TABLE `supplier_sku_craft_attribute`
(
    `supplier_sku_craft_attribute_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `supplier_code`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商代码',
    `sku`                             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku编码',
    `tube_wrapping`                   varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '缠管',
    `roots_cnt`                       int                                                           NOT NULL DEFAULT '0' COMMENT '根数',
    `layers_cnt`                      int                                                           NOT NULL DEFAULT '0' COMMENT '层数',
    `special_handling`                varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '特殊处理',
    `create_time`                     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                   bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                         int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`supplier_sku_craft_attribute_id`) USING BTREE,
    KEY `idx_supplier_sku_craft_attribute_1` (`supplier_code`, `sku`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='供应商商品工艺属性表';

-- 11.供应商商品样品表
CREATE TABLE `supplier_sku_sample`
(
    `supplier_sku_sample_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `supplier_code`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商代码',
    `sku`                    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku编码',
    `source_order_no`        varchar(32) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '来源单据号',
    `create_time`            datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`            datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`          bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`supplier_sku_sample_id`) USING BTREE,
    KEY `idx_supplier_sku_sample_1` (`supplier_code`, `sku`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='供应商商品样品表';

-- 12.供应链属性风险表
CREATE TABLE `attribute_risk`
(
    `attribute_risk_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `attribute_id`      bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '供应链属性主键id',
    `attribute_name`    varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应链属性名称',
    `coefficient`       decimal(8, 2)                                                 NOT NULL DEFAULT '0.00' COMMENT '风险系数',
    `create_time`       datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`       datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`     bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`           int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`attribute_risk_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='供应链属性风险表';

-- 13.供应链属性可选项风险表
CREATE TABLE `attribute_option_risk`
(
    `attribute_option_risk_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `attribute_risk_id`        bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '供应链属性风险配置表主键id',
    `attribute_option_id`      bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '供应链属性可选项主键id',
    `attribute_option_value`   varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应链属性可选项值',
    `score`                    decimal(8, 2)                                                 NOT NULL DEFAULT '0.00' COMMENT '风险评分',
    `create_time`              datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`              datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`            bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                  int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`attribute_option_risk_id`) USING BTREE,
    KEY `idx_attribute_option_risk_1` (`attribute_risk_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='供应链属性可选项风险表';

--  14.商品风险表
CREATE TABLE `sku_risk`
(
    `sku_risk_id`     bigint unsigned                                               NOT NULL COMMENT '主键id',
    `sku`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku编码',
    `score`           decimal(8, 2)                                                 NOT NULL DEFAULT '0.00' COMMENT '风险评分',
    `level`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '风险等级',
    `create_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`   bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`         int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sku_risk_id`) USING BTREE,
    KEY `idx_sku_risk_1` (`sku`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='商品风险表';

-- 15.商品风险日志表
CREATE TABLE `sku_risk_log`
(
    `sku_risk_log_id`        bigint unsigned                                              NOT NULL COMMENT '主键id',
    `sku`                    varchar(255) COLLATE utf8mb4_general_ci                      NOT NULL DEFAULT '' COMMENT 'sku编码',
    `supplier_code`          varchar(32) COLLATE utf8mb4_general_ci                       NOT NULL DEFAULT '' COMMENT '供应商代码',
    `sku_risk_id`            bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '属性风险id',
    `attribute_id`           bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '供应链属性主键id',
    `attribute_name`         varchar(500) COLLATE utf8mb4_general_ci                      NOT NULL DEFAULT '' COMMENT '供应链属性名称',
    `coefficient`            decimal(8, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '风险系数',
    `attribute_option_id`    bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '供应链属性可选项主键id',
    `attribute_option_value` varchar(500) COLLATE utf8mb4_general_ci                      NOT NULL DEFAULT '' COMMENT '供应链属性可选项值',
    `score`                  decimal(8, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '风险评分',
    `create_time`            datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`            datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`          bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sku_risk_log_id`) USING BTREE,
    KEY `idx_sku_risk_log_1` (`sku_risk_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='商品风险日志表';


-- 一致性框架新增字段
ALTER TABLE `support_consistency`
    ADD COLUMN `biz_key`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '业务key' after trace_id,
    ADD COLUMN `exec_user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '执行人名称' after biz_key;