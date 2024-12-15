CREATE TABLE `develop_order_price`
(
    `develop_order_price_id`   bigint unsigned                                               NOT NULL COMMENT '主键id',
    `develop_order_no`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '相关单据单号',
    `develop_order_price_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '单据价格类型：SUPPLIER_SAMPLE_PURCHASE_PRICE(样品单供应商报价大货价格),DEVELOP_PURCHASE_PRICE(开发子单大货价格),PRICING_PURCHASE_PRICE(核价单需要打样大货价格),PRICING_NOT_PURCHASE_PRICE(核价单无需打样大货价格),SAMPLE_PURCHASE_PRICE(样品单大货价格、产前样审版单大货价格)',
    `channel_id`               bigint                                                        NOT NULL DEFAULT '0' COMMENT '关联渠道ID（旧数据时为空）',
    `channel_name`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '渠道名称（旧数据时为空）',
    `price`                    decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '价格',
    `is_default_price`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '默认价格：是(TRUE)、否(FALSE)',
    `create_time`              datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`              datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`            bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                  int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`develop_order_price_id`) USING BTREE,
    KEY `idx_develop_order_price_1` (`develop_order_no`, `develop_order_price_type`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='开发单相关单据大货价格';