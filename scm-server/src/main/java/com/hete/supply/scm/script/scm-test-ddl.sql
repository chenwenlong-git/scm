CREATE TABLE `deduct_order`
(
    `deduct_order_id`        bigint unsigned                                               NOT NULL COMMENT '主键id',
    `deduct_order_no`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '扣款单号',
    `deduct_type`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT 'PRICE' COMMENT '扣款类型：价差扣款(PRICE)、加工扣款(PROCESS)、品质扣款(QUALITY)',
    `deduct_status`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT 'WAIT_SUBMIT' COMMENT '扣款状态：待提交(WAIT_SUBMIT)、待确认(WAIT_CONFIRM)、待审核(WAIT_EXAMINE)、已审核(AUDITED)、已结算(SETTLE)',
    `supplier_code`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商编码',
    `supplier_name`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商名称',
    `deduct_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '扣款员工',
    `deduct_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '扣款员工名称',
    `deduct_price`           decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '扣款金额',
    `about_settle_time`      datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '约定结算时间',
    `confirm_refuse_remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '确认拒绝备注',
    `examine_refuse_remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '审核拒绝备注',
    `submit_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '提交人',
    `submit_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '提交人的名称',
    `submit_time`            datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '提交时间',
    `confirm_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '确认人',
    `confirm_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '确认人的名称',
    `confirm_time`           datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '确认时间',
    `examine_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '审核人',
    `examine_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '审核人的名称',
    `examine_time`           datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '审核时间',
    `pay_time`               datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '支付完成时间',
    `create_time`            datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`            datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`          bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`deduct_order_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='扣款单';

CREATE TABLE `deduct_order_process`
(
    `deduct_order_process_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `deduct_order_id`         bigint                                                        NOT NULL COMMENT '扣款单ID',
    `process_order_id`        bigint                                                        NOT NULL COMMENT '关联加工单ID',
    `process_order_no`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '关联加工单号',
    `deduct_price`            decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '扣款金额',
    `deduct_remarks`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扣款备注',
    `create_time`             datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`             datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`           bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                 int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`deduct_order_process_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='扣款单加工明细';

CREATE TABLE `deduct_order_purchase`
(
    `deduct_order_purchase_id`   bigint unsigned                                               NOT NULL COMMENT '主键id',
    `deduct_order_id`            bigint                                                        NOT NULL COMMENT '扣款单ID',
    `business_no`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '关联单据号',
    `deduct_order_purchase_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT 'CARGO' COMMENT '关联单据类型：大货采购单(PRODUCT_PURCHASE)、加工采购单(PROCESS_PURCHASE)、采购退货单(PURCHASE_RETURN)、样品退货单SAMPLE_RETURN、样品采购单(SAMPLE)',
    `sku`                        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'sku',
    `spu`                        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'spu',
    `sku_num`                    int                                                           NOT NULL DEFAULT '0' COMMENT 'SKU数量',
    `deduct_price`               decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '扣款金额',
    `deduct_remarks`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扣款备注',
    `create_time`                datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`              bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                    int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`deduct_order_purchase_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='扣款单采购明细';

CREATE TABLE `deduct_order_quality`
(
    `deduct_order_quality_id`    bigint unsigned                                               NOT NULL COMMENT '主键id',
    `deduct_order_id`            bigint                                                        NOT NULL COMMENT '扣款单ID',
    `business_no`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '关联单据号',
    `deduct_order_purchase_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT 'CARGO' COMMENT '关联单据类型：大货采购单(PRODUCT_PURCHASE)、加工采购单(PROCESS_PURCHASE)、采购退货单(PURCHASE_RETURN)、样品退货单SAMPLE_RETURN、样品采购单(SAMPLE)',
    `sku`                        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'sku',
    `spu`                        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'spu',
    `sku_num`                    int                                                           NOT NULL DEFAULT '0' COMMENT 'SKU数量',
    `deduct_price`               decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '扣款金额',
    `deduct_remarks`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扣款备注',
    `create_time`                datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`              bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                    int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`deduct_order_quality_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='扣款单品质扣款明细';

CREATE TABLE `goods_process`
(
    `goods_process_id`     bigint unsigned                                               NOT NULL COMMENT '主键id',
    `sku`                  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `goods_category_id`    bigint                                                        NOT NULL DEFAULT '0' COMMENT '商品品类 ID',
    `category_name`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '商品品类名称',
    `goods_process_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '状态，BINDED：绑定、UNBINDED：未绑定',
    `create_time`          datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`          datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`        bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`              int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`goods_process_id`) USING BTREE,
    KEY `idx_goods_process_1` (`sku`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='商品工序管理表';

CREATE TABLE `goods_process_relation`
(
    `goods_process_relation_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `process_id`                bigint                                                       NOT NULL DEFAULT '0' COMMENT '工序 id',
    `goods_process_id`          bigint                                                       NOT NULL DEFAULT '0' COMMENT '商品工序id',
    `sort`                      int                                                          NOT NULL DEFAULT '0' COMMENT '排序',
    `create_time`               datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`               datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`             bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                   int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`goods_process_relation_id`) USING BTREE,
    UNIQUE KEY `uk_goods_process_relation_1` (`process_id`, `goods_process_id`, `del_timestamp`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='商品工序关系表';

CREATE TABLE `process`
(
    `process_id`          bigint unsigned                                               NOT NULL COMMENT '主键id',
    `process_first`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '一级工序',
    `process_second_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '二级工序代码',
    `process_second_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '二级工序名称',
    `process_code`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '工序代码',
    `process_name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '工序名称',
    `commission`          decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '工序提成',
    `process_status`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '状态，ENABLED：启用、DISABLED：禁用',
    `create_time`         datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`         varchar(32) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`     varchar(32) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`         datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`         varchar(32) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`     varchar(32) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`       bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`             int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_id`) USING BTREE,
    UNIQUE KEY `uk_process_1` (`process_first`, `process_second_code`, `del_timestamp`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='工序管理表';

CREATE TABLE `process_desc`
(
    `process_desc_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `name`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '名称',
    `desc_values`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述值，多个以英文逗号分开',
    `create_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`   bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`         int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_desc_id`) USING BTREE,
    UNIQUE KEY `uk_process_desc_1` (`name`, `del_timestamp`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='加工描述表';

CREATE TABLE `process_material_receipt`
(
    `process_material_receipt_id`     bigint unsigned                                               NOT NULL COMMENT '主键id',
    `process_order_no`                varchar(32) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '关联的加工单',
    `delivery_no`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '出库单编号',
    `process_material_receipt_status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '状态，待收货（WAIT_RECEIVE），已收货（RECEIVED）',
    `delivery_num`                    int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '发货数量',
    `delivery_username`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '发货人',
    `delivery_time`                   datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '发货时间',
    `receipt_username`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '收货人',
    `receipt_time`                    datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '收货时间',
    `delivery_warehouse_code`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '发货仓库编码',
    `delivery_warehouse_name`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '发货仓库名称',
    `delivery_note`                   varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '出库备注',
    `place_order_time`                datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
    `place_order_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '下单人 id',
    `place_order_username`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '下单人名称',
    `platform`                        varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '平台',
    `create_time`                     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                   bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                         int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_material_receipt_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='加工原料收货单';

CREATE TABLE `process_material_receipt_item`
(
    `process_material_receipt_item_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `process_material_receipt_id`      bigint unsigned                                              NOT NULL COMMENT '关联的原料收货单',
    `sku`                              varchar(32) COLLATE utf8mb4_general_ci                       NOT NULL DEFAULT '' COMMENT 'sku',
    `delivery_num`                     int unsigned                                                 NOT NULL DEFAULT '0' COMMENT '出库数量',
    `receipt_num`                      int unsigned                                                 NOT NULL DEFAULT '0' COMMENT '收货数量',
    `create_time`                      datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                      datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                    bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                          int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_material_receipt_item_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='加工单原料表';

CREATE TABLE `process_order`
(
    `process_order_id`     bigint unsigned                                               NOT NULL COMMENT '主键id',
    `process_order_no`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '加工单号',
    `process_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '加工单状态，缺货(LACK)，待下单(WAIT_ORDER)，待投产(WAIT_PRODUCE)，已投产(PRODUCED)，前处理(WAIT_HANDLE)，缝制中（HANDLING），后处理（HANDLED），完工待交接（WAIT_MOVING），后整质检中(CHECKING)，待发货(WAIT_DELIVERY)，待收货(WAIT_RECEIPT)，待入库(WAIT_STORE)，已入库(STORED)',
    `process_order_type`   varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '类型，常规(NORMAL)，补单(EXTRA)，默认常规',
    `platform`             varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '平台',
    `require_no`           varchar(32) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '需求编号',
    `order_no`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '订单编号，仓储系统销售单号',
    `customer_name`        varchar(32) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '客户姓名',
    `process_order_note`   varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '加工单备注',
    `delivery_note`        varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '出库备注',
    `warehouse_code`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '仓库编码',
    `warehouse_name`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '仓库名称',
    `warehouse_types`      varchar(255) COLLATE utf8mb4_general_ci                       NOT NULL DEFAULT '' COMMENT '仓库标签',
    `spu`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'spu',
    `deliver_date`         datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '约定日期',
    `total_sku_num`        int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '总 sku 数量',
    `total_process_num`    int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '总加工数量',
    `total_settle_price`   decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '总结算金额',
    `produced_time`        datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '投产时间',
    `checked_time`         datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '质检时间',
    `receipt_time`         datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '收货时间',
    `stored_time`          datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '入库时间',
    `print_time`           datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '打印时间',
    `create_time`          datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`          datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`        bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`              int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_order_id`) USING BTREE,
    UNIQUE KEY `uk_process_order_1` (`process_order_no`, `del_timestamp`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='加工单';

CREATE TABLE `process_order_desc`
(
    `process_order_desc_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `process_order_no`      varchar(32) COLLATE utf8mb4_general_ci                       NOT NULL DEFAULT '' COMMENT '关联的加工单',
    `process_desc_name`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联的加工描述名称',
    `process_desc_value`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联的加工描述值',
    `create_time`           datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`           datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`         bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`               int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_order_desc_id`) USING BTREE,
    KEY `idx_process_order_material_1` (`process_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='加工单加工描述';

CREATE TABLE `process_order_extra`
(
    `process_order_extra_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `process_order_no`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '加工单号',
    `produced_user`          varchar(32) COLLATE utf8mb4_general_ci                                DEFAULT NULL COMMENT '投产人 ID',
    `produced_username`      varchar(32) COLLATE utf8mb4_general_ci                                DEFAULT NULL COMMENT '投产人名称',
    `receipt_order_no`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货单号',
    `receipt_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货人 id',
    `receipt_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货人名称',
    `deliver_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发货人 id',
    `deliver_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货人名称',
    `deliver_time`           datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '发货时间',
    `store_order_no`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '入库单号',
    `store_user`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '入库人 id',
    `store_username`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '入库人名称',
    `settle_order_no`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '结算单号',
    `settle_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '结算人 id',
    `settle_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '结算人名称',
    `settle_time`            datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '结算时间',
    `wait_handle_user`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '前处理人 id',
    `wait_handle_username`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '前处理人名称',
    `wait_handle_time`       datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '前处理时间',
    `handling_user`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '缝制中处理人 id',
    `handling_username`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '缝制中处理人名称',
    `handling_time`          datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '缝制中时间',
    `handled_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '后处理人 id',
    `handled_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '后处理人名称',
    `handled_time`           datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '后处理时间',
    `complete_scan_user`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '后处理人 id',
    `complete_scan_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '后处理人名称',
    `complete_scan_time`     datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '后处理时间',
    `check_order_no`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '质检单号',
    `checking_user`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '后整质检中处理人 id',
    `checking_username`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '后整质检中处理人名称',
    `checking_time`          datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '后整质检中处理时间',
    `create_time`            datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`            datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`          bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_order_extra_id`) USING BTREE,
    UNIQUE KEY `uk_process_order_extra_1` (`process_order_no`, `del_timestamp`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='加工单';

CREATE TABLE `process_order_item`
(
    `process_order_item_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `process_order_no`      varchar(32) COLLATE utf8mb4_general_ci                       NOT NULL DEFAULT '' COMMENT '关联的加工单',
    `sku`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `sku_batch_code`        varchar(32) COLLATE utf8mb4_general_ci                       NOT NULL DEFAULT '' COMMENT 'sku 批次码',
    `variant_properties`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '变体属性',
    `purchase_price`        decimal(10, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '采购单价',
    `process_num`           int unsigned                                                 NOT NULL DEFAULT '0' COMMENT '加工数量',
    `quality_goods_cnt`     int unsigned                                                 NOT NULL DEFAULT '0' COMMENT '正品数量',
    `defective_goods_cnt`   int unsigned                                                 NOT NULL DEFAULT '0' COMMENT '次品数量',
    `is_first`              varchar(32) COLLATE utf8mb4_general_ci                       NOT NULL DEFAULT '' COMMENT '是否是首次创建，是：''true''，次品赋码设置为 "false"',
    `create_time`           datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`           datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`         bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`               int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_order_item_id`) USING BTREE,
    KEY `idx_process_order_item_1` (`process_order_no`, `del_timestamp`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='加工单明细表';

CREATE TABLE `process_order_material`
(
    `process_order_material_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `process_order_no`          varchar(32) COLLATE utf8mb4_general_ci                       NOT NULL DEFAULT '' COMMENT '关联的加工单',
    `sku`                       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `delivery_num`              int unsigned                                                 NOT NULL DEFAULT '0' COMMENT '出库数量',
    `create_time`               datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`               datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`             bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                   int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_order_material_id`) USING BTREE,
    KEY `idx_process_order_material_1` (`process_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='加工单原料表';

CREATE TABLE `process_order_procedure`
(
    `process_order_procedure_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `process_order_no`           varchar(32) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '关联的加工单',
    `sort`                       int                                                           NOT NULL DEFAULT '0' COMMENT '工序排序',
    `process_id`                 bigint                                                        NOT NULL DEFAULT '0' COMMENT '工序 ID',
    `process_code`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '工序代码',
    `process_name`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '工序名称',
    `commission`                 decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '人工提成',
    `create_time`                datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`              bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                    int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_order_procedure_id`) USING BTREE,
    KEY `idx_process_order_procedure_1` (`process_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='加工单工序';

CREATE TABLE `process_order_scan`
(
    `process_order_scan_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `process_order_no`      varchar(32) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '关联的加工单',
    `process_code`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '关联的工序代码',
    `process_name`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联的工序名称',
    `process_commission`    decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '关联的工序提成',
    `receipt_num`           int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '接货数量',
    `quality_goods_cnt`     int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '正品数量',
    `defective_goods_cnt`   int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '次品数量',
    `platform`              varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '平台',
    `order_time`            datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '下单时间',
    `order_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '下单人 id',
    `order_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '下单人名称',
    `receipt_time`          datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '收货时间',
    `receipt_user`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '收货人 id',
    `receipt_username`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '接货人名称',
    `complete_time`         datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '扫码完成时间',
    `complete_user`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '扫码完成人 id',
    `complete_username`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '扫码完成人名称',
    `create_time`           datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`           datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`         bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`               int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_order_scan_id`) USING BTREE,
    KEY `idx_process_order_scan_1` (`process_order_no`, `del_timestamp`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='工序扫码单';

CREATE TABLE `process_settle_order`
(
    `process_settle_order_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `process_settle_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '加工结算单号',
    `process_settle_status`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '加工结算单状态：待核算(WAIT_SETTLE)、结算待审核(SETTLE_WAIT_EXAMINE)、已审核(AUDITED)',
    `total_price`             decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '总付款金额',
    `deduct_price`            decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '总扣款金额',
    `pay_price`               decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '应付总金额',
    `month`                   varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '月份',
    `examine_refuse_remarks`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '审核拒绝原因',
    `examine_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '核算人的用户',
    `examine_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '核算人的用户名',
    `examine_time`            datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '核算时间',
    `settle_user`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '结算审核人的用户',
    `settle_username`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '结算审核人的用户名',
    `settle_time`             datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '结算审核人时间',
    `create_time`             datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`             datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`           bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                 int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_settle_order_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='加工结算单';

CREATE TABLE `process_settle_order_bill`
(
    `process_settle_order_bill_id`   bigint unsigned                                              NOT NULL COMMENT '主键id',
    `process_settle_order_item_id`   bigint                                                       NOT NULL COMMENT '关联结算单详情ID',
    `process_settle_order_bill_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'CARGO' COMMENT '关联单据类型：补款单(REPLENISH)、扣款单(DEDUCT)',
    `business_no`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联单据号',
    `supplement_deduct_type`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'PRICE' COMMENT '单据类型：价差扣款(PRICE)、加工扣款(PROCESS)、品质扣款(QUALITY)',
    `examine_time`                   datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '审核时间',
    `price`                          decimal(10, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '补款/扣款金额',
    `create_time`                    datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                    datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                  bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                        int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_settle_order_bill_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='加工结算单明细补款/扣款';

CREATE TABLE `process_settle_order_item`
(
    `process_settle_order_item_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `process_settle_order_id`      bigint                                                       NOT NULL COMMENT '关联结算单ID',
    `process_settle_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '加工结算单号',
    `complete_user`                varchar(32) COLLATE utf8mb4_general_ci                       NOT NULL DEFAULT '0' COMMENT '完成人的用户',
    `complete_username`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '完成人的用户名',
    `process_num`                  int                                                          NOT NULL DEFAULT '0' COMMENT '加工单数',
    `sku_num`                      int                                                          NOT NULL DEFAULT '0' COMMENT '加工产品数(正品)',
    `settle_price`                 decimal(10, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '结算金额',
    `create_time`                  datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                  datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                      int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_settle_order_item_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='加工结算单明细';

CREATE TABLE `process_settle_order_scan`
(
    `process_settle_order_scan_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `process_settle_order_item_id` bigint                                                        NOT NULL COMMENT '关联结算单详情ID',
    `total_process_commission`     decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '总提成',
    `complete_time`                datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '扫码完成时间',
    `process_order_no`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '关联的加工单',
    `process_code`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '关联的工序代码',
    `process_name`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联的工序名称',
    `process_commission`           decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '关联的工序提成',
    `quality_goods_cnt`            int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '正品数量',
    `order_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '下单人',
    `order_username`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '下单人名称',
    `complete_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '扫码完成人',
    `complete_username`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '扫码完成人名称',
    `create_time`                  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                      int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_settle_order_scan_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='加工结算单明细工序扫码记录';

CREATE TABLE `process_template`
(
    `process_template_id`   bigint unsigned                                               NOT NULL COMMENT '主键id',
    `name`                  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名称',
    `process_template_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '类型，CATEGORY：品类，SKU：商品sku',
    `type_value`            varchar(32) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '类型值 id',
    `type_value_name`       varchar(32) COLLATE utf8mb4_general_ci                                 DEFAULT NULL COMMENT '类型值名称',
    `create_time`           datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`           datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`         bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`               int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_template_id`) USING BTREE,
    UNIQUE KEY `uk_process_template_1` (`name`, `del_timestamp`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='工序模版表';

CREATE TABLE `process_template_relation`
(
    `process_template_relation_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `process_template_id`          bigint                                                       NOT NULL DEFAULT '0' COMMENT '工序模版 ID',
    `process_id`                   bigint                                                       NOT NULL DEFAULT '0' COMMENT '工序 ID',
    `sort`                         int                                                          NOT NULL DEFAULT '0' COMMENT '序号',
    `create_time`                  datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                  datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                      int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_template_relation_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='工序模版关系表';

CREATE TABLE `purchase_child_order`
(
    `purchase_child_order_id`  bigint unsigned                                               NOT NULL COMMENT '主键id',
    `purchase_child_order_no`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购子单单号',
    `purchase_parent_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购母单单号',
    `sample_child_order_no`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品单子单号',
    `purchase_order_status`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购单状态',
    `is_first_order`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '是否首单',
    `is_urgent_order`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '是否加急',
    `is_normal_order`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '是否正常采购',
    `spu`                      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'spu',
    `platform`                 varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '平台',
    `purchase_biz_type`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '拆分类型',
    `supplier_code`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商编码',
    `supplier_name`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商名称',
    `warehouse_code`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '仓库编码',
    `warehouse_name`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '仓库名称',
    `warehouse_types`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '仓库类型',
    `order_remarks`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购单备注',
    `sku_cnt`                  int                                                           NOT NULL DEFAULT '0' COMMENT 'sku数量',
    `purchase_total`           int                                                           NOT NULL DEFAULT '0' COMMENT '采购数量',
    `deliver_date`             datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '业务约定交期',
    `total_settle_price`       decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '总结算金额',
    `raw_warehouse_code`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '原料仓库',
    `raw_warehouse_name`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '原料仓库',
    `create_time`              datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`              datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`            bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                  int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`purchase_child_order_id`) USING BTREE,
    UNIQUE KEY `uk_purchase_child_order_1` (`purchase_child_order_no`, `del_timestamp`) USING BTREE,
    KEY `idx_purchase_child_order_1` (`purchase_parent_order_no`) USING BTREE,
    KEY `idx_purchase_child_order_2` (`purchase_order_status`) USING BTREE,
    KEY `idx_purchase_child_order_3` (`supplier_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='采购需求子单';

CREATE TABLE `purchase_child_order_change`
(
    `purchase_child_order_change_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `purchase_child_order_id`        bigint unsigned                                              NOT NULL COMMENT '采购子单id',
    `approve_time`                   datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '审核时间',
    `pay_time`                       datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '支付完成时间',
    `return_time`                    datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '退货时间',
    `scheduling_time`                datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '排产时间',
    `commissioning_time`             datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '投产时间',
    `pretreatment_time`              datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '前处理时间',
    `sewing_time`                    datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '缝制中时间',
    `aftertreatment_time`            datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '后处理时间',
    `wms_receipt_time`               datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT 'wms收货时间',
    `wms_qc_time`                    datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT 'wms质检时间',
    `wms_warehousing_time`           datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT 'wms入库时间',
    `wms_return_time`                datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT 'wms退货时间',
    `place_order_time`               datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '下单时间',
    `confirm_time`                   datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '确认时间',
    `receive_order_time`             datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '接单时间',
    `deliver_time`                   datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '发货时间',
    `receipt_time`                   datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '收货时间',
    `qc_time`                        datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '质检时间',
    `warehousing_time`               datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '入库时间',
    `confirm_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '确认人',
    `confirm_username`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '确认人',
    `receipt_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货人',
    `receipt_username`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货人',
    `qc_user`                        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '质检人',
    `qc_username`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '质检人',
    `receive_order_user`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '接单人',
    `receive_order_username`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '接单人',
    `warehousing_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '入库人',
    `warehousing_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '入库人',
    `purchase_deliver_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发货单号',
    `purchase_receipt_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货单号',
    `purchase_return_order_no`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '退货单号',
    `purchase_settle_order_no`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '结算单号',
    `create_time`                    datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                    datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                  bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                        int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`purchase_child_order_change_id`) USING BTREE,
    KEY `idx_purchase_child_order_change_1` (`purchase_child_order_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='采购需求子单变更记录';

CREATE TABLE `purchase_child_order_item`
(
    `purchase_child_order_item_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `purchase_parent_order_no`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购需求母单号',
    `purchase_child_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购子单单号',
    `sku`                          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `sku_batch_code`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'sku批次码',
    `variant_properties`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '变体属性',
    `purchase_cnt`                 int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '采购数',
    `init_purchase_cnt`            int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '初始采购数',
    `purchase_price`               decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '采购价',
    `discount_type`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '优惠类型',
    `substract_price`              decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '扣减金额',
    `settle_price`                 decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '结算金额',
    `deliver_cnt`                  int                                                           NOT NULL DEFAULT '0' COMMENT '发货数',
    `quality_goods_cnt`            int                                                           NOT NULL DEFAULT '0' COMMENT '正品数',
    `defective_goods_cnt`          int                                                           NOT NULL DEFAULT '0' COMMENT '次品数',
    `create_time`                  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                      int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`purchase_child_order_item_id`) USING BTREE,
    KEY `idx_purchase_child_order_item_1` (`purchase_parent_order_no`) USING BTREE,
    KEY `idx_purchase_child_order_item_2` (`purchase_child_order_no`) USING BTREE,
    KEY `idx_purchase_child_order_item_3` (`sku`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='采购需求子单明细';

CREATE TABLE `purchase_child_order_raw`
(
    `purchase_child_order_raw_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `purchase_child_order_no`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购子单单号',
    `sku`                         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `sku_batch_code`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'sku批次码',
    `delivery_cnt`                int                                                           NOT NULL COMMENT '出库数',
    `create_time`                 datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                 datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`               bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                     int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`purchase_child_order_raw_id`) USING BTREE,
    KEY `idx_purchase_child_order_raw_1` (`purchase_child_order_no`) USING BTREE,
    KEY `idx_purchase_child_order_raw_2` (`sku`) USING BTREE,
    KEY `idx_purchase_child_order_raw_3` (`sku_batch_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='采购需求子单原料';

CREATE TABLE `purchase_deliver_order`
(
    `purchase_deliver_order_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `purchase_deliver_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购发货单号',
    `purchase_receipt_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购收货单号',
    `deliver_order_status`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '发货单状态',
    `logistics`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '物流',
    `tracking_no`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '运单号',
    `deliver_cnt`               int                                                           NOT NULL DEFAULT '0' COMMENT '发货数',
    `deliver_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '发货人id',
    `deliver_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '发货人名称',
    `deliver_time`              datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '发货时间',
    `purchase_child_order_no`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购子单单号',
    `supplier_code`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商编码',
    `supplier_name`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商名称',
    `warehouse_code`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '仓库编码',
    `warehouse_name`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '仓库名称',
    `warehouse_types`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '仓库类型',
    `receipt_time`              datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '收货时间',
    `create_time`               datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`               datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`             bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                   int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`purchase_deliver_order_id`) USING BTREE,
    UNIQUE KEY `uk_purchase_deliver_order_1` (`purchase_deliver_order_no`) USING BTREE,
    KEY `idx_purchase_deliver_order_1` (`deliver_order_status`) USING BTREE,
    KEY `idx_purchase_deliver_order_2` (`supplier_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='采购发货单';

CREATE TABLE `purchase_deliver_order_item`
(
    `purchase_deliver_order_item_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `purchase_deliver_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购发货单号',
    `sku`                            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `variant_properties`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'sku变体属性',
    `deliver_cnt`                    int                                                           NOT NULL DEFAULT '0' COMMENT '发货数',
    `receipt_cnt`                    int                                                           NOT NULL DEFAULT '0' COMMENT '收货数',
    `quality_goods_cnt`              int                                                           NOT NULL DEFAULT '0' COMMENT '正品数',
    `defective_goods_cnt`            int                                                           NOT NULL DEFAULT '0' COMMENT '次品数',
    `spu`                            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'spu',
    `sku_batch_code`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'sku批次码',
    `purchase_cnt`                   int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '采购数',
    `create_time`                    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                  bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                        int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`purchase_deliver_order_item_id`) USING BTREE,
    KEY `idx_purchase_deliver_order_item_1` (`purchase_deliver_order_no`) USING BTREE,
    KEY `idx_purchase_deliver_order_item_2` (`sku`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='采购发货单明细';

CREATE TABLE `purchase_modify_order`
(
    `purchase_modify_order_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `down_return_order_no`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '降档退货单',
    `purchase_child_order_no`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购子单号',
    `down_return_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '降档退货单状态',
    `create_time`              datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`              datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`            bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                  int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`purchase_modify_order_id`) USING BTREE,
    UNIQUE KEY `uk_purchase_modify_order_1` (`down_return_order_no`) USING BTREE,
    KEY `purchase_child_order_no` (`purchase_child_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='采购需求变更单';

CREATE TABLE `purchase_modify_order_item`
(
    `purchase_modify_order_item_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `down_return_order_no`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '降档退货单号',
    `sku`                           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '原sku',
    `purchase_cnt`                  int                                                           NOT NULL DEFAULT '0' COMMENT '原sku数量',
    `new_sku`                       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '新sku',
    `new_purchase_cnt`              int                                                           NOT NULL DEFAULT '0' COMMENT '新sku数量',
    `create_time`                   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                 bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                       int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`purchase_modify_order_item_id`) USING BTREE,
    KEY `idx_purchase_modify_order_item_1` (`down_return_order_no`) USING BTREE,
    KEY `idx_purchase_modify_order_item_2` (`sku`) USING BTREE,
    KEY `idx_purchase_modify_order_item_3` (`new_sku`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='采购需求变更单明细';

CREATE TABLE `purchase_parent_order`
(
    `purchase_parent_order_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `purchase_parent_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购母单单号',
    `spu`                      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'spu',
    `sku_type`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'sku类型',
    `platform`                 varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '平台',
    `warehouse_code`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '仓库编码',
    `warehouse_name`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '仓库名称',
    `warehouse_types`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '仓库类型',
    `deliver_date`             datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '业务约定交期',
    `purchase_order_status`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购单状态',
    `sku_cnt`                  int unsigned                                                  NOT NULL DEFAULT '0' COMMENT 'sku数量',
    `purchase_total`           int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '采购总数',
    `order_remarks`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购单备注',
    `supplier_code`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商编码',
    `supplier_name`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商名称',
    `is_first_order`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '是否首单',
    `is_urgent_order`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '是否加急',
    `is_normal_order`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '是否正常采购',
    `is_direct_send`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '是否直发',
    `create_time`              datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`              datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`            bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                  int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`purchase_parent_order_id`) USING BTREE,
    UNIQUE KEY `uk_purchase_parent_order_1` (`purchase_parent_order_no`) USING BTREE,
    KEY `idx_purchase_parent_order_1` (`spu`) USING BTREE,
    KEY `idx_purchase_parent_order_2` (`supplier_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='采购需求母单';

CREATE TABLE `purchase_parent_order_change`
(
    `purchase_parent_order_change_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `purchase_parent_order_id`        bigint unsigned                                              NOT NULL COMMENT '采购母单id',
    `approve_time`                    datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '审核时间',
    `pay_time`                        datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '支付完成时间',
    `return_time`                     datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '退货时间',
    `scheduling_time`                 datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '排产时间',
    `commissioning_time`              datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '投产时间',
    `pretreatment_time`               datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '前处理时间',
    `sewing_time`                     datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '缝制中时间',
    `aftertreatment_time`             datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '后处理时间',
    `place_order_time`                datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '下单时间',
    `confirm_time`                    datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '确认时间',
    `receive_order_time`              datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '接单时间',
    `deliver_time`                    datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '发货时间',
    `receipt_time`                    datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '收货时间',
    `qc_time`                         datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '质检时间',
    `warehousing_time`                datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '入库时间',
    `approve_user`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '审核人',
    `approve_username`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '审核人',
    `place_order_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '下单人',
    `place_order_username`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '下单人',
    `create_time`                     datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                     datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                   bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                         int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`purchase_parent_order_change_id`) USING BTREE,
    KEY `uk_purchase_parent_order_change_1` (`purchase_parent_order_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='采购需求母单变更记录';

CREATE TABLE `purchase_parent_order_item`
(
    `purchase_parent_order_item_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `purchase_parent_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购母单单号',
    `sku`                           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `variant_properties`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '变体属性',
    `purchase_cnt`                  int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '采购数',
    `deliver_cnt`                   int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '发货数',
    `quality_goods_cnt`             int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '正品数',
    `defective_goods_cnt`           int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '次品数',
    `create_time`                   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                 bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                       int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`purchase_parent_order_item_id`) USING BTREE,
    KEY `idx_purchase_parent_order_item_1` (`purchase_parent_order_no`) USING BTREE,
    KEY `idx_purchase_parent_order_item_2` (`sku`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='采购需求母单明细';

CREATE TABLE `purchase_raw_receipt_order`
(
    `purchase_raw_receipt_order_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `purchase_raw_receipt_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购原料收货单号',
    `receipt_order_status`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货单状态',
    `deliver_cnt`                   int unsigned                                                 NOT NULL DEFAULT '0' COMMENT '发货数量',
    `deliver_time`                  datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '发货时间',
    `receipt_time`                  datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '收货时间',
    `receipt_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货人id',
    `receipt_username`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货人名称',
    `warehouse_code`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '仓库编码',
    `warehouse_name`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '仓库名称',
    `purchase_child_order_no`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购子单单号',
    `receipt_cnt`                   int unsigned                                                 NOT NULL DEFAULT '0' COMMENT '收货数量',
    `logistics`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '物流',
    `tracking_no`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '运单号',
    `purchase_raw_deliver_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购原料发货单号',
    `supplier_code`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商编码',
    `supplier_name`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商名称',
    `create_time`                   datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                   datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                 bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                       int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`purchase_raw_receipt_order_id`) USING BTREE,
    UNIQUE KEY `uk_purchase_raw_receipt_order_1` (`purchase_raw_receipt_order_no`) USING BTREE,
    KEY `idx_purchase_raw_receipt_order_1` (`receipt_order_status`) USING BTREE,
    KEY `idx_purchase_raw_receipt_order_2` (`supplier_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='采购原料收货单';

CREATE TABLE `purchase_raw_receipt_order_item`
(
    `purchase_raw_receipt_order_item_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `purchase_raw_receipt_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购原料收货单号',
    `sku_batch_code`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'sku批次码',
    `sku`                                varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `deliver_cnt`                        int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '发货数量',
    `receipt_cnt`                        int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '收货数量',
    `create_time`                        datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                        datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                      bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                            int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`purchase_raw_receipt_order_item_id`) USING BTREE,
    KEY `idx_purchase_raw_receipt_order_item_1` (`purchase_raw_receipt_order_no`) USING BTREE,
    KEY `idx_purchase_raw_receipt_order_item_2` (`sku_batch_code`) USING BTREE,
    KEY `idx_purchase_raw_receipt_order_item_3` (`sku`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='采购原料收货单明细';

CREATE TABLE `purchase_return_order`
(
    `purchase_return_order_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `purchase_return_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购退货单号',
    `return_order_status`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '退货单状态',
    `logistics`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '物流',
    `tracking_no`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '运单号',
    `return_cnt`               int                                                          NOT NULL DEFAULT '0' COMMENT '退货数',
    `supplier_code`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商编码',
    `supplier_name`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商名称',
    `receipt_user`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货人id',
    `receipt_username`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货人名称',
    `receipt_time`             datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '收货时间',
    `create_time`              datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`              datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`            bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                  int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`purchase_return_order_id`) USING BTREE,
    UNIQUE KEY `uk_purchase_return_order_1` (`purchase_return_order_no`) USING BTREE,
    KEY `idx_purchase_return_order_1` (`return_order_status`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='采购退货单';

CREATE TABLE `purchase_return_order_item`
(
    `purchase_return_order_item_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `purchase_return_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购退货单号',
    `sku`                           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `sku_batch_code`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'sku批次码',
    `purchase_child_order_no`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购子单单号',
    `return_cnt`                    int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '退货数量',
    `receipt_cnt`                   int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '收货数量',
    `create_time`                   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                 bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                       int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`purchase_return_order_item_id`) USING BTREE,
    KEY `idx_purchase_return_order_item_1` (`purchase_return_order_no`) USING BTREE,
    KEY `idx_purchase_return_order_item_2` (`sku`) USING BTREE,
    KEY `idx_purchase_return_order_item_3` (`sku_batch_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='采购退货单明细';

CREATE TABLE `purchase_settle_order`
(
    `purchase_settle_order_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `purchase_settle_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购结算单号',
    `purchase_settle_status`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购结算单状态：待确认(WAIT_CONFIRM)、待核算(WAIT_SETTLE)、结算待审核(SETTLE_WAIT_EXAMINE)、已审核(AUDITED)、部分结算(PART_SETTLE)、已结算(SETTLE)',
    `supplier_code`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商编码',
    `supplier_name`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商名称',
    `month`                    varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '月份',
    `total_price`              decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '总付款金额',
    `deduct_price`             decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '总扣款金额',
    `pay_price`                decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '应付总金额',
    `about_settle_time`        datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '约定结算时间',
    `settle_refuse_remarks`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '核算拒绝备注',
    `examine_refuse_remarks`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '审核拒绝备注',
    `confirm_user`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '确认人的用户',
    `confirm_username`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '确认人的用户名',
    `confirm_time`             datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '确认时间',
    `examine_user`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '核算人的用户',
    `examine_username`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '核算人的用户名',
    `examine_time`             datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '核算时间',
    `settle_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '结算审核人的用户',
    `settle_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '结算审核人的用户名',
    `settle_time`              datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '结算审核时间',
    `pay_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '支付人的用户',
    `pay_username`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '支付人的用户名',
    `pay_time`                 datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '支付完成时间',
    `create_time`              datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`              datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`            bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                  int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`purchase_settle_order_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='采购结算单';

CREATE TABLE `purchase_settle_order_item`
(
    `purchase_settle_order_item_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `purchase_settle_order_id`      bigint                                                       NOT NULL COMMENT '关联结算单ID',
    `purchase_settle_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购结算单号',
    `business_no`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联单据号',
    `purchase_settle_item_type`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联单据类型：大货采购单(PRODUCT_PURCHASE)、加工采购单(PROCESS_PURCHASE)、补款单(REPLENISH)、扣款单(DEDUCT)，样品采购单(SAMPLE)',
    `settle_time`                   datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '单据时间(入库时间)',
    `sku_num`                       int                                                          NOT NULL DEFAULT '0' COMMENT 'SKU数量',
    `settle_price`                  decimal(10, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '结算金额',
    `create_time`                   datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                   datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                 bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                       int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`purchase_settle_order_item_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='采购结算单明细';

CREATE TABLE `purchase_settle_order_pay`
(
    `purchase_settle_order_pay_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `purchase_settle_order_id`     bigint                                                        NOT NULL COMMENT '关联结算单ID',
    `purchase_settle_order_no`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购结算单号',
    `transaction_no`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '交易号',
    `pay_time`                     datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '支付时间',
    `pay_price`                    decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '支付金额',
    `remarks`                      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
    `create_time`                  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                      int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`purchase_settle_order_pay_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='采购结算单支付明细';

CREATE TABLE `sample_child_order`
(
    `sample_child_order_id`   bigint unsigned                                               NOT NULL COMMENT '主键id',
    `sample_parent_order_no`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品采购母单号',
    `sample_child_order_no`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品采购子单号',
    `sample_order_status`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品单状态',
    `spu`                     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'spu',
    `sku`                     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `sku_batch_code`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'sku批次码',
    `supplier_code`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商编码',
    `supplier_name`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商名称',
    `purchase_cnt`            int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '采购数',
    `receipt_cnt`             int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '收货数',
    `purchase_predict_price`  decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '采购预估价',
    `warehouse_code`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '仓库编码',
    `warehouse_name`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '仓库名称',
    `warehouse_types`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '仓库类型',
    `deliver_date`            datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '业务约定交期',
    `demand_describe`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '需求描述',
    `platform`                varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '平台',
    `sample_result`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '选样结果',
    `proofing_price`          decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '打样单价',
    `sample_describe`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '卖点描述',
    `sample_improve`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '样品改善要求',
    `settle_price`            decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '结算金额',
    `sample_receipt_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '成品入仓收货单号',
    `create_time`             datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`             datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`           bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                 int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sample_child_order_id`) USING BTREE,
    UNIQUE KEY `uk_sample_child_order_1` (`sample_child_order_no`, `del_timestamp`) USING BTREE,
    KEY `idx_sample_child_order_1` (`sample_parent_order_no`) USING BTREE,
    KEY `idx_sample_child_order_2` (`sample_order_status`) USING BTREE,
    KEY `idx_sample_child_order_3` (`sku`) USING BTREE,
    KEY `idx_sample_child_order_4` (`supplier_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='样品需求子单';

CREATE TABLE `sample_child_order_change`
(
    `sample_child_order_change_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `sample_child_order_id`        bigint unsigned                                              NOT NULL COMMENT '样品采购子单id',
    `sample_deliver_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发货单号',
    `sample_receipt_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货单号',
    `sample_qc_order_no`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '质检单号',
    `sample_warehousing_order_no`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '入库单号',
    `sample_settle_order_no`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '结算单号',
    `place_order_user`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '下单人',
    `place_order_username`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '下单人',
    `approve_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '审核人',
    `approve_username`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '审核人',
    `receive_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '接单人',
    `receive_username`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '接单人',
    `receipt_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货人',
    `receipt_username`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货人',
    `sample_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '选样人',
    `sample_username`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '选样人',
    `deliver_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发货人',
    `deliver_username`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发货人',
    `typeset_time`                 datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '下发打版时间',
    `place_order_time`             datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '下单时间',
    `approve_time`                 datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '审核时间',
    `receive_order_time`           datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '接单时间',
    `typesetting_time`             datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '确认打版时间',
    `deliver_time`                 datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '发货时间',
    `receipt_time`                 datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '收货时间',
    `sample_time`                  datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '选样时间',
    `create_time`                  datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                  datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                      int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sample_child_order_change_id`) USING BTREE,
    UNIQUE KEY `uk_sample_child_order_change_1` (`sample_child_order_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='样品需求子单变更记录';

CREATE TABLE `sample_child_order_info`
(
    `sample_child_order_info_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `sample_child_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品采购子单号',
    `sample_info_key`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '属性key',
    `sample_info_value`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '属性值',
    `create_time`                datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`              bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                    int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sample_child_order_info_id`) USING BTREE,
    KEY `idx_sample_child_order_no_1` (`sample_child_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='样品采购子单详细信息';

CREATE TABLE `sample_deliver_order`
(
    `sample_deliver_order_id`     bigint unsigned                                              NOT NULL COMMENT '主键id',
    `sample_deliver_order_no`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '样品发货单号',
    `sample_deliver_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '样品发货单状态',
    `logistics`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '物流',
    `tracking_no`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '运单号',
    `total_deliver`               int                                                          NOT NULL DEFAULT '0' COMMENT '总发货数',
    `deliver_user`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发货人id',
    `deliver_username`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发货人名称',
    `deliver_time`                datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '发货时间',
    `supplier_code`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商编码',
    `supplier_name`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商名称',
    `create_time`                 datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                 datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`               bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                     int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sample_deliver_order_id`) USING BTREE,
    UNIQUE KEY `uk_sample_deliver_order_1` (`sample_deliver_order_no`) USING BTREE,
    KEY `idx_sample_deliver_order_1` (`sample_deliver_order_status`) USING BTREE,
    KEY `idx_sample_deliver_order_2` (`supplier_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='样品发货单';

CREATE TABLE `sample_deliver_order_item`
(
    `sample_deliver_order_item_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `sample_deliver_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品发货单号',
    `spu`                          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'spu',
    `sample_parent_order_no`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品母单',
    `sample_child_order_no`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品采购子单号',
    `deliver_cnt`                  int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '发货数',
    `create_time`                  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                      int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sample_deliver_order_item_id`) USING BTREE,
    KEY `idx_sample_deliver_order_item_1` (`sample_deliver_order_no`) USING BTREE,
    KEY `idx_sample_deliver_order_item_2` (`spu`) USING BTREE,
    KEY `idx_sample_deliver_order_item_3` (`sample_parent_order_no`) USING BTREE,
    KEY `idx_sample_deliver_order_item_4` (`sample_child_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='样品发货单明细';

CREATE TABLE `sample_parent_order`
(
    `sample_parent_order_id`          bigint unsigned                                               NOT NULL COMMENT '主键id',
    `sample_parent_order_no`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品采购单号',
    `sample_order_status`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品单状态',
    `sample_dev_type`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '开发类型',
    `category_name`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '商品品类',
    `spu`                             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'spu',
    `platform`                        varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '平台',
    `warehouse_code`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '仓库编码',
    `warehouse_name`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '仓库名称',
    `warehouse_types`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '仓库类型',
    `deliver_date`                    datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '业务约定交期',
    `supply_sample_type`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '是否主动寄样',
    `purchase_total`                  int                                                           NOT NULL DEFAULT '0' COMMENT '采购总数',
    `purchase_predict_price`          decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '采购预估价',
    `is_first_order`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '是否首单',
    `is_urgent_order`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '是否加急',
    `is_normal_order`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '是否正常采购',
    `source_material`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '素材需求',
    `sample_describe`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '卖点描述',
    `defective_sample_child_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '次品样品单号',
    `create_time`                     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`                     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`                   bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                         int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sample_parent_order_id`) USING BTREE,
    UNIQUE KEY `uk_sample_parent_order_1` (`sample_parent_order_no`) USING BTREE,
    KEY `idx_sample_parent_order_1` (`sample_order_status`) USING BTREE,
    KEY `idx_sample_parent_order_2` (`spu`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='样品需求母单';

CREATE TABLE `sample_parent_order_change`
(
    `sample_parent_order_change_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `sample_parent_order_id`        bigint unsigned                                              NOT NULL COMMENT '样品采购母单id',
    `approve_time`                  datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '审核时间',
    `deliver_time`                  datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '发货时间',
    `receipt_time`                  datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '收货时间',
    `place_order_time`              datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '下单时间',
    `disbursement_time`             datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '开款时间',
    `typesetting_time`              datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '确认打版时间',
    `receive_order_time`            datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '接单时间',
    `sample_time`                   datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '选样时间',
    `approve_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '审核人',
    `approve_username`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '审核人',
    `place_order_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '下单人',
    `place_order_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '下单人',
    `disburse_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '开款人',
    `disburse_username`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '开款人',
    `create_time`                   datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                   datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                 bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                       int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sample_parent_order_change_id`) USING BTREE,
    UNIQUE KEY `uk_sample_parent_order_change_1` (`sample_parent_order_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='样品需求母单变更记录';

CREATE TABLE `sample_parent_order_info`
(
    `sample_parent_order_info_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `sample_parent_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品采购母单号',
    `sample_info_key`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '属性key',
    `sample_info_value`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '属性值',
    `create_time`                 datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                 datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`               bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                     int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sample_parent_order_info_id`) USING BTREE,
    KEY `idx_sample_parent_order_info_1` (`sample_parent_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='样品需求母单详细信息';

CREATE TABLE `sample_receipt_order`
(
    `sample_receipt_order_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `sample_receipt_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '样品收货单号',
    `tracking_no`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '运单号',
    `logistics`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '物流',
    `total_deliver`           int                                                          NOT NULL DEFAULT '0' COMMENT '总发货数',
    `total_receipt`           int                                                          NOT NULL DEFAULT '0' COMMENT '总收货数',
    `receipt_order_status`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '样品收货单状态',
    `supplier_code`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商编码',
    `supplier_name`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商名称',
    `receipt_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货人id',
    `receipt_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货人名称',
    `receipt_time`            datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '收货时间',
    `deliver_time`            datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '发货时间',
    `sample_deliver_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '样品发货单号',
    `create_time`             datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`             datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`           bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                 int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sample_receipt_order_id`) USING BTREE,
    UNIQUE KEY `uk_sample_receipt_order_1` (`sample_receipt_order_no`) USING BTREE,
    KEY `idx_sample_receipt_order_1` (`supplier_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='样品收货单';

CREATE TABLE `sample_receipt_order_item`
(
    `sample_receipt_order_item_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `sample_receipt_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品收货单号',
    `sample_parent_order_no`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品母单号',
    `sample_child_order_no`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品采购子单号',
    `spu`                          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'spu',
    `deliver_cnt`                  int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '发货数',
    `receipt_cnt`                  int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '收货数',
    `create_time`                  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                      int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sample_receipt_order_item_id`) USING BTREE,
    KEY `idx_sample_receipt_order_item_1` (`sample_receipt_order_no`) USING BTREE,
    KEY `idx_sample_receipt_order_item_2` (`sample_parent_order_no`) USING BTREE,
    KEY `idx_sample_receipt_order_item_3` (`sample_child_order_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='样品收货单明细';

CREATE TABLE `sample_return_order`
(
    `sample_return_order_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `sample_return_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '样品退货单号',
    `return_order_status`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '退货单状态',
    `logistics`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '物流',
    `tracking_no`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '运单号',
    `return_cnt`             int                                                          NOT NULL DEFAULT '0' COMMENT '退货数',
    `receipt_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货人id',
    `receipt_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货人名称',
    `supplier_code`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商编码',
    `supplier_name`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商名称',
    `receipt_time`           datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '收货时间',
    `create_time`            datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`            datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`          bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sample_return_order_id`) USING BTREE,
    UNIQUE KEY `uk_sample_return_order_1` (`sample_return_order_no`) USING BTREE,
    KEY `idx_sample_return_order_1` (`return_order_status`) USING BTREE,
    KEY `idx_sample_return_order_2` (`supplier_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='样品退货单';

CREATE TABLE `sample_return_order_item`
(
    `sample_return_order_item_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `sample_return_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品退货单号',
    `spu`                         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'spu',
    `sample_child_order_no`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '样品采购子单号',
    `return_order_status`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '收货结果',
    `receipt_cnt`                 int                                                           NOT NULL DEFAULT '0' COMMENT '收货数',
    `return_cnt`                  int                                                           NOT NULL DEFAULT '0' COMMENT '退货数',
    `create_time`                 datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                 datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`               bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                     int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sample_return_order_item_id`) USING BTREE,
    KEY `idx_sample_return_order_item_1` (`sample_return_order_no`) USING BTREE,
    KEY `idx_sample_return_order_item_2` (`sample_child_order_no`) USING BTREE,
    KEY `idx_sample_return_order_item_3` (`return_order_status`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='样品退货单明细';

CREATE TABLE `scm_image`
(
    `scm_image_id`   bigint unsigned                                               NOT NULL COMMENT '主键id',
    `file_code`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '图片文件编码',
    `image_biz_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '业务枚举',
    `image_biz_id`   bigint                                                        NOT NULL DEFAULT '0' COMMENT '业务id',
    `create_time`    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人 id',
    `update_time`    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人 id',
    `del_timestamp`  bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`        int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`scm_image_id`) USING BTREE,
    KEY `idx_scm_image_1` (`file_code`) USING BTREE,
    KEY `idx_scm_image_2` (`image_biz_type`, `image_biz_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='scm图片表';

CREATE TABLE `sm_category`
(
    `sm_category_id`       bigint unsigned                                              NOT NULL COMMENT '主键id',
    `category_code`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '类目代码',
    `category_name`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '类目名称',
    `parent_category_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '父级类目代码',
    `category_level`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '类目级别',
    `create_time`          datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`          datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`        bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`              int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sm_category_id`) USING BTREE,
    UNIQUE KEY `uk_sm_category_1` (`category_name`, `del_timestamp`),
    UNIQUE KEY `uk_sm_category_2` (`category_code`) USING BTREE,
    KEY `idx_sm_category_1` (`parent_category_code`) USING BTREE,
    KEY `idx_sm_category_2` (`category_level`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='辅料-类目';

CREATE TABLE `sm_product`
(
    `sm_product_id`          bigint unsigned                                              NOT NULL COMMENT '主键id',
    `subsidiary_material_id` bigint unsigned                                              NOT NULL COMMENT '辅料id',
    `sku`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `count`                  int                                                          NOT NULL DEFAULT '0' COMMENT '数量',
    `create_time`            datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`            datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`          bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sm_product_id`) USING BTREE,
    KEY `idx_sm_product_1` (`subsidiary_material_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='辅料-产品信息';

CREATE TABLE `sm_supplier`
(
    `sm_supplier_id`         bigint unsigned                                              NOT NULL COMMENT '主键id',
    `subsidiary_material_id` bigint unsigned                                              NOT NULL COMMENT '辅料id',
    `supplier_code`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商编码',
    `supplier_name`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商名称',
    `purchase_price`         decimal(10, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '采购价',
    `capacity`               int                                                          NOT NULL DEFAULT '0' COMMENT '产能',
    `join_time`              datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '入驻时间',
    `create_time`            datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`            datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`          bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sm_supplier_id`) USING BTREE,
    KEY `idx_sm_supplier_1` (`subsidiary_material_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='辅料-供应商信息';

CREATE TABLE `subsidiary_material`
(
    `subsidiary_material_id`   bigint unsigned                                              NOT NULL COMMENT '主键id',
    `subsidiary_material_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '辅料名称',
    `category_code`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '辅料类目编码',
    `material_type`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '辅料类型',
    `measurement`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '计量单位',
    `unit`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '最小单位',
    `use_type`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '使用类型',
    `sm_sku`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '辅料sku',
    `shelves_type`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '上下架类型',
    `create_time`              datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`              datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`            bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                  int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`subsidiary_material_id`) USING BTREE,
    UNIQUE KEY `uk_subsidiary_material_1` (`subsidiary_material_name`, `del_timestamp`) USING BTREE,
    KEY `idx_subsidiary_material_1` (`sm_sku`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='辅料';

CREATE TABLE `supplement_order`
(
    `supplement_order_id`    bigint unsigned                                               NOT NULL COMMENT '主键id',
    `supplement_order_no`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '补款单号',
    `supplement_type`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '补款类型：价差补款(PRICE)、加工补款(PROCESS)',
    `supplement_status`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT 'WAIT_SUBMIT' COMMENT '补款状态：待提交(WAIT_SUBMIT)、待确认(WAIT_CONFIRM)、待审核(WAIT_EXAMINE)、已审核(AUDITED)、已结算(SETTLE)',
    `supplier_code`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商编码',
    `supplier_name`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商名称',
    `supplement_user`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '补款员工',
    `supplement_username`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '补款员工用户名',
    `supplement_price`       decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '补款金额',
    `about_settle_time`      datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '约定结算时间',
    `confirm_refuse_remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '确认拒绝备注',
    `examine_refuse_remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '审核拒绝备注',
    `submit_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '提交人的用户',
    `submit_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '提交人的用户名',
    `submit_time`            datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '提交时间',
    `confirm_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '确认人的用户',
    `confirm_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '确认人的用户名',
    `confirm_time`           datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '确认时间',
    `examine_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '审核人的用户',
    `examine_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '审核人的用户名',
    `examine_time`           datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '审核时间',
    `pay_time`               datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '支付完成时间',
    `create_time`            datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`            datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`          bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`supplement_order_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='补款单';

CREATE TABLE `supplement_order_process`
(
    `supplement_order_process_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `supplement_order_id`         bigint                                                        NOT NULL COMMENT '补款单ID',
    `process_order_no`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '关联加工单号',
    `supplement_price`            decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '补款金额',
    `supplement_remarks`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '补款备注',
    `create_time`                 datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                 datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`               bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                     int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`supplement_order_process_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='补款单加工明细';

CREATE TABLE `supplement_order_purchase`
(
    `supplement_order_purchase_id`   bigint unsigned                                               NOT NULL COMMENT '主键id',
    `supplement_order_id`            bigint                                                        NOT NULL COMMENT '补款单ID',
    `business_no`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '关联单据号',
    `supplement_order_purchase_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '关联单据类型：大货采购单(PRODUCT_PURCHASE)、加工采购单(PROCESS_PURCHASE)、样品采购单(SAMPLE)',
    `spu`                            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'spu',
    `sku`                            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'sku',
    `sku_num`                        int                                                           NOT NULL DEFAULT '0' COMMENT 'SKU数量',
    `supplement_price`               decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '补款金额',
    `supplement_remarks`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '补款备注',
    `create_time`                    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                  bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                        int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`supplement_order_purchase_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='补款单采购明细';

CREATE TABLE `supplier`
(
    `supplier_id`         bigint unsigned                                               NOT NULL COMMENT '主键id',
    `supplier_code`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商代码',
    `supplier_name`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商名称',
    `supplier_type`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT 'ONESELF_BUSINESS' COMMENT '供应商类型：自营供应商(ONESELF_BUSINESS)、合作供应商(COOPERATION)',
    `supplier_status`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT 'ENABLED' COMMENT '供应商状态：启用(ENABLED)、禁用(DISABLED)',
    `supplier_grade`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT 'S' COMMENT '供应商等级：S、A、B、C、D',
    `capacity`            int                                                           NOT NULL DEFAULT '0' COMMENT '产能',
    `supplier_invoicing`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT 'NO' COMMENT '开票资质：否(NO)、是(YES)',
    `tax_point`           decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '税点',
    `credit_code`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '社会信用代码',
    `corporate_name`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '公司名称',
    `legal_person`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '法定代表人',
    `business_time_start` datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '营业期限开始',
    `business_time_end`   datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '营业期限结束',
    `dev_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '开发采购员',
    `dev_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '开发采购员的名称',
    `follow_user`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '跟进采购员',
    `follow_username`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '跟进采购员的名称',
    `supplier_export`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT 'NO' COMMENT '进出口资质：否(NO)、是(YES)',
    `join_time`           datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '入驻时间',
    `country`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '国家',
    `province`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '省份/州',
    `city`                varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '城市',
    `address`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '详细地址',
    `ship_to_country`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '收货地址国家',
    `ship_to_province`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '收货地址省份/州',
    `ship_to_city`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '收货地址城市',
    `ship_to_address`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货地址详细地址',
    `remarks`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
    `create_time`         datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`         datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`       bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`             int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`supplier_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='供应商信息表';

CREATE TABLE `supplier_account`
(
    `supplier_account_id` bigint unsigned                                              NOT NULL COMMENT '主键ID',
    `network_address`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '网点',
    `registration_people` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '开户人',
    `account`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '账号',
    `account_bank`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '开户行',
    `supplier_code`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商编码',
    `supplier_name`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商名称',
    `create_time`         datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`         datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`       bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`             int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`supplier_account_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='供应商账号信息';

CREATE TABLE `supplier_contact`
(
    `supplier_contact_id` bigint unsigned                                               NOT NULL COMMENT '主键ID',
    `name`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '联系人姓名',
    `phone`               varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '联系人电话',
    `position`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '职位',
    `remarks`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
    `supplier_code`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商编码',
    `supplier_name`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商名称',
    `create_time`         datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`         datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`       bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`             int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`supplier_contact_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='供应商联系人';

CREATE TABLE `supplier_product`
(
    `supplier_product_id` bigint unsigned                                              NOT NULL COMMENT '主键ID',
    `supplier_code`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商编码',
    `supplier_name`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商名称',
    `spu`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'SPU',
    `variant_properties`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '变体属性',
    `create_time`         datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`         datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`       bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`             int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`supplier_product_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='供应商生产产品信息';

CREATE TABLE `support_consistency`
(
    `support_consistency_id` bigint unsigned                        NOT NULL COMMENT 'id',
    `consumer_bean_name`     varchar(32) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '消费本地消息的bean的名称',
    `consistency_state`      varchar(16) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '消息状态：PENDING(待执行）,SUCCESS（成功）,FAIL（失败）',
    `msg_type`               varchar(16) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '消息类型',
    `exec_count`             int unsigned                           NOT NULL DEFAULT '0' COMMENT '执行次数（默认为0，当消息发送一次 进行加1）',
    `max_exec_count`         int unsigned                           NOT NULL DEFAULT '3' COMMENT '最大重试次数',
    `exec_priority`          int unsigned                           NOT NULL DEFAULT '100' COMMENT '执行优先级，数字越小，优先级越高',
    `expected_exec_time`     datetime                               NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '预期执行时间',
    `latest_exec_time`       datetime                               NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '最后一次执行时间',
    `success_time`           datetime                               NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '执行成功时间',
    `fail_time`              datetime                               NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '失败时间',
    `exec_user`              varchar(32) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '执行人',
    `create_time`            datetime                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`            varchar(32) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`            datetime                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`            varchar(32) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`          bigint unsigned                        NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                int unsigned                           NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`support_consistency_id`),
    KEY `idx_support_consistency_1` (`consistency_state`, `msg_type`, `exec_priority`,
                                     `expected_exec_time`) USING BTREE,
    KEY `idx_support_consistency_2` (`consumer_bean_name`, `consistency_state`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='一致性本地消息-主表';

CREATE TABLE `support_consistency_text`
(
    `support_consistency_text_id` bigint unsigned                         NOT NULL COMMENT 'id',
    `support_consistency_id`      bigint unsigned                         NOT NULL DEFAULT '0' COMMENT '主表id',
    `error_msg`                   varchar(255) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '执行错误信息',
    `msg_text`                    text COLLATE utf8mb4_general_ci COMMENT '消息文本',
    `create_time`                 datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                 varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                 datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                 varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`               bigint unsigned                         NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                     int unsigned                            NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`support_consistency_text_id`),
    KEY `idx_support_consistency_text_1` (`support_consistency_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='一致性本地消息-报文表';


CREATE TABLE `sample_child_order_result`
(
    `sample_child_order_result_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `sample_result_no`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '样品结果编号',
    `sample_parent_order_no`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '样品采购母单号',
    `sample_child_order_no`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '样品采购子单号',
    `sample_result`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '选样结果',
    `relate_order_no`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联单据号',
    `sample_cnt`                   int                                                          NOT NULL DEFAULT '0' COMMENT '选样数量',
    `create_time`                  datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                  datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                      int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`sample_child_order_result_id`) USING BTREE,
    UNIQUE KEY `uk_sample_child_order_result_1` (`sample_result_no`) USING BTREE,
    KEY `idx_sample_child_order_result_1` (`sample_parent_order_no`) USING BTREE,
    KEY `idx_sample_child_order_result_2` (`sample_child_order_no`) USING BTREE,
    KEY `idx_sample_child_order_result_3` (`sample_result`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='样品子单结果列表';

