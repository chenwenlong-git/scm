#
该文件用于记录测试环境的表结构变更,上线前同步给运维执行到验收以及生产环境.
# 若已经执行过的代码段,需要用分割线隔开,并且备注执行时间,避免重复执行.


---------------------------2023-01-08 已经执行到验收与生产环境-------------------------------
use `cn_scm`
ALTER TABLE `purchase_child_order`
    ADD INDEX `idx_purchase_child_order_2` (`purchase_order_status`) USING BTREE,
    ADD INDEX `idx_purchase_child_order_3` (`supplier_code`) USING BTREE;

ALTER TABLE `purchase_child_order_change`
    ADD UNIQUE INDEX `uk_purchase_child_order_change_1` (`purchase_child_order_id`) USING BTREE;

ALTER TABLE `purchase_child_order_item`
    ADD INDEX `idx_purchase_child_order_item_1` (`purchase_parent_order_no`) USING BTREE,
    ADD INDEX `idx_purchase_child_order_item_2` (`purchase_child_order_no`) USING BTREE,
    ADD INDEX `idx_purchase_child_order_item_3` (`sku`) USING BTREE;

ALTER TABLE `purchase_child_order_raw`
    ADD INDEX `idx_purchase_child_order_raw_1` (`purchase_child_order_no`) USING BTREE,
    ADD INDEX `idx_purchase_child_order_raw_2` (`sku`) USING BTREE,
    ADD INDEX `idx_purchase_child_order_raw_3` (`sku_batch_code`) USING BTREE;

ALTER TABLE `purchase_deliver_order`
    ADD UNIQUE INDEX `uk_purchase_deliver_order_1` (`purchase_deliver_order_no`) USING BTREE,
    ADD INDEX `idx_purchase_deliver_order_1` (`deliver_order_status`) USING BTREE,
    ADD INDEX `idx_purchase_deliver_order_2` (`supplier_code`) USING BTREE;

ALTER TABLE `purchase_deliver_order_item`
    ADD INDEX `idx_purchase_deliver_order_item_1` (`purchase_deliver_order_no`) USING BTREE,
    ADD INDEX `idx_purchase_deliver_order_item_2` (`sku`) USING BTREE;

ALTER TABLE `purchase_modify_order`
    ADD UNIQUE INDEX `uk_purchase_modify_order_1` (`down_return_order_no`) USING BTREE,
    ADD INDEX `purchase_child_order_no` (`purchase_child_order_no`) USING BTREE;

ALTER TABLE `purchase_modify_order_item`
    ADD INDEX `idx_purchase_modify_order_item_1` (`down_return_order_no`) USING BTREE,
    ADD INDEX `idx_purchase_modify_order_item_2` (`sku`) USING BTREE,
    ADD INDEX `idx_purchase_modify_order_item_3` (`new_sku`) USING BTREE;

ALTER TABLE `purchase_parent_order`
    ADD INDEX `idx_purchase_parent_order_1` (`spu`) USING BTREE,
    ADD INDEX `idx_purchase_parent_order_2` (`supplier_code`) USING BTREE;

ALTER TABLE `purchase_parent_order_change`
    ADD INDEX `uk_purchase_parent_order_change_1` (`purchase_parent_order_id`) USING BTREE;

ALTER TABLE `purchase_parent_order_item`
    ADD INDEX `idx_purchase_parent_order_item_1` (`purchase_parent_order_no`) USING BTREE,
    ADD INDEX `idx_purchase_parent_order_item_2` (`sku`) USING BTREE;

ALTER TABLE `purchase_raw_receipt_order`
    ADD INDEX `idx_purchase_raw_receipt_order_1` (`receipt_order_status`) USING BTREE,
    ADD INDEX `idx_purchase_raw_receipt_order_2` (`supplier_code`) USING BTREE;


ALTER TABLE `purchase_raw_receipt_order_item`
    ADD INDEX `idx_purchase_raw_receipt_order_item_1` (`purchase_raw_receipt_order_no`) USING BTREE,
    ADD INDEX `idx_purchase_raw_receipt_order_item_2` (`sku_batch_code`) USING BTREE,
    ADD INDEX `idx_purchase_raw_receipt_order_item_3` (`sku`) USING BTREE;


ALTER TABLE `purchase_return_order`
    ADD UNIQUE INDEX `uk_purchase_return_order_1` (`purchase_return_order_no`) USING BTREE,
    ADD INDEX `idx_purchase_return_order_1` (`return_order_status`) USING BTREE;

ALTER TABLE `purchase_return_order_item`
    ADD INDEX `idx_purchase_return_order_item_1` (`purchase_return_order_no`) USING BTREE,
    ADD INDEX `idx_purchase_return_order_item_2` (`sku`) USING BTREE,
    ADD INDEX `idx_purchase_return_order_item_3` (`sku_batch_code`) USING BTREE;

ALTER TABLE `sample_child_order`
    ADD INDEX `idx_sample_child_order_1` (`sample_parent_order_no`) USING BTREE,
    ADD INDEX `idx_sample_child_order_2` (`sample_order_status`) USING BTREE,
    ADD INDEX `idx_sample_child_order_3` (`sku`) USING BTREE,
    ADD INDEX `idx_sample_child_order_4` (`supplier_code`) USING BTREE;

ALTER TABLE `sample_child_order_change`
    ADD UNIQUE INDEX `uk_sample_child_order_change_1` (`sample_child_order_id`) USING BTREE;

ALTER TABLE `sample_child_order_info`
    ADD INDEX `idx_sample_child_order_no_1` (`sample_child_order_no`) USING BTREE;

ALTER TABLE `sample_child_order_result`
    ADD UNIQUE INDEX `uk_sample_child_order_result_1` (`sample_result_no`) USING BTREE,
    ADD INDEX `idx_sample_child_order_result_1` (`sample_parent_order_no`) USING BTREE,
    ADD INDEX `idx_sample_child_order_result_2` (`sample_child_order_no`) USING BTREE,
    ADD INDEX `idx_sample_child_order_result_3` (`sample_result`) USING BTREE;


ALTER TABLE `sample_deliver_order`
    ADD UNIQUE INDEX `uk_sample_deliver_order_1` (`sample_deliver_order_no`) USING BTREE,
    ADD INDEX `idx_sample_deliver_order_1` (`sample_deliver_order_status`) USING BTREE,
    ADD INDEX `idx_sample_deliver_order_2` (`supplier_code`) USING BTREE;

ALTER TABLE `sample_deliver_order_item`
    ADD INDEX `idx_sample_deliver_order_item_1` (`sample_deliver_order_no`) USING BTREE,
    ADD INDEX `idx_sample_deliver_order_item_2` (`spu`) USING BTREE,
    ADD INDEX `idx_sample_deliver_order_item_3` (`sample_parent_order_no`) USING BTREE,
    ADD INDEX `idx_sample_deliver_order_item_4` (`sample_child_order_no`) USING BTREE;

ALTER TABLE `sample_parent_order`
    ADD INDEX `idx_sample_parent_order_1` (`sample_order_status`) USING BTREE,
    ADD INDEX `idx_sample_parent_order_2` (`spu`) USING BTREE;

ALTER TABLE `sample_parent_order_change`
    ADD UNIQUE INDEX `uk_sample_parent_order_change_1` (`sample_parent_order_id`);

ALTER TABLE `sample_parent_order_info`
    ADD INDEX `idx_sample_parent_order_info_1` (`sample_parent_order_no`) USING BTREE;

ALTER TABLE `sample_receipt_order`
    ADD UNIQUE INDEX `uk_sample_receipt_order_1` (`sample_receipt_order_no`) USING BTREE,
    ADD INDEX `idx_sample_receipt_order_1` (`supplier_code`) USING BTREE;


ALTER TABLE `sample_receipt_order_item`
    ADD INDEX `idx_sample_receipt_order_item_1` (`sample_receipt_order_no`) USING BTREE,
    ADD INDEX `idx_sample_receipt_order_item_2` (`sample_parent_order_no`) USING BTREE,
    ADD INDEX `idx_sample_receipt_order_item_3` (`sample_child_order_no`) USING BTREE;


ALTER TABLE `sample_return_order`
    ADD UNIQUE INDEX `uk_sample_return_order_1` (`sample_return_order_no`) USING BTREE,
    ADD INDEX `idx_sample_return_order_1` (`return_order_status`) USING BTREE,
    ADD INDEX `idx_sample_return_order_2` (`supplier_code`) USING BTREE;

ALTER TABLE `sample_return_order_item`
    ADD INDEX `idx_sample_return_order_item_1` (`sample_return_order_no`) USING BTREE,
    ADD INDEX `idx_sample_return_order_item_2` (`sample_child_order_no`) USING BTREE,
    ADD INDEX `idx_sample_return_order_item_3` (`return_order_status`) USING BTREE;

ALTER TABLE `scm_image`
    ADD INDEX `idx_scm_image_1` (`file_code`) USING BTREE,
    ADD INDEX `idx_scm_image_2` (`image_biz_type`, `image_biz_id`) USING BTREE;

ALTER TABLE `sm_category`
    ADD UNIQUE INDEX `uk_sm_category_2` (`category_code`) USING BTREE,
    ADD INDEX `idx_sm_category_1` (`parent_category_code`) USING BTREE,
    ADD INDEX `idx_sm_category_2` (`category_level`) USING BTREE;

ALTER TABLE `sm_product`
    ADD INDEX `idx_sm_product_1` (`subsidiary_material_id`) USING BTREE;

ALTER TABLE `sm_supplier`
    ADD INDEX `idx_sm_supplier_1` (`subsidiary_material_id`) USING BTREE;

ALTER TABLE `subsidiary_material`
    ADD INDEX `idx_subsidiary_material_1` (`sm_sku`) USING BTREE;
---------------------------2023-01-08 已经执行到验收与生产环境-------------------------------


---------------------------2023-01-10 调整测试环境表结构（2023-01-12 已执行到验收与生产环境）-------------------------------
USE
    `cn_scm`;
ALTER TABLE `process_material_receipt`
    CHANGE COLUMN `receipt_username` `receipt_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货人' AFTER `delivery_time`,
    ADD COLUMN `delivery_user`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发货人' AFTER `delivery_num`,
    ADD COLUMN `receipt_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货人' AFTER `receipt_user`;

ALTER TABLE `purchase_child_order_raw`
    ADD COLUMN `purchase_parent_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购母单单号' AFTER `purchase_child_order_raw_id`;
---------------------------2023-01-10 调整测试环境表结构（2023-01-12 已执行到验收与生产环境）-------------------------------


ALTER TABLE `sample_parent_order_info`
    MODIFY COLUMN `sample_info_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '属性key';

ALTER TABLE `sample_child_order_info`
    MODIFY COLUMN `sample_info_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '属性key';

---------------------------2023-01-16 调整测试环境表结构（2023-01-12 已执行到验收与生产环境）-------------------------------
USE
    `cn_scm`;
ALTER TABLE `process_order_extra`
    ADD COLUMN `check_user`     varchar(32) NOT NULL DEFAULT '' COMMENT '质检人' AFTER `check_order_no`,
    ADD COLUMN `check_username` varchar(32) NOT NULL DEFAULT '' COMMENT '质检人' AFTER `check_user`;
---------------------------2023-01-16 调整测试环境表结构（2023-01-12 已执行到验收与生产环境）-------------------------------


---------------------------2023-01-16 调整测试环境表结构-------------------------------
USE `cn_scm`;

CREATE TABLE `process_user`
(
    `process_user_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `process_id`      bigint                                                       NOT NULL DEFAULT '0' COMMENT '工序 id',
    `user_code`       varchar(32) COLLATE utf8mb4_general_ci                       NOT NULL DEFAULT '0' COMMENT '用户编码',
    `username`        varchar(32) COLLATE utf8mb4_general_ci                       NOT NULL DEFAULT '0' COMMENT '用户名称',
    `create_time`     datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人 id',
    `create_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`     datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人 id',
    `update_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`   bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`         int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`process_user_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='工序用户关系绑定';

ALTER TABLE `purchase_parent_order`
    DROP
        COLUMN `supplier_code`,
    DROP
        COLUMN `supplier_name`;

ALTER TABLE `sample_child_order`
    ADD COLUMN `supplier_production` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商生产信息' AFTER `sample_improve`;

ALTER TABLE `supplement_order`
    ADD COLUMN `settle_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联采购/加工结算单号' AFTER `pay_time`;

ALTER TABLE `deduct_order`
    ADD COLUMN `settle_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联采购/加工结算单号' AFTER `pay_time`;
ALTER TABLE `process_order_scan`
    ADD COLUMN `process_first` varchar(32) NOT NULL DEFAULT '' COMMENT '关联的一级工序' AFTER `process_order_no`;

ALTER TABLE `process_desc`
    ADD COLUMN `process_first` varchar(32) NOT NULL DEFAULT '' COMMENT '一级工序' AFTER `name`;

ALTER TABLE `sample_child_order`
    ADD COLUMN `cost_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '样品成本价' AFTER `proofing_price`;

ALTER TABLE `sample_deliver_order`
    ADD COLUMN `warehouse_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '仓库编码' AFTER `supplier_name`,
    ADD COLUMN `warehouse_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '仓库名称' AFTER `warehouse_code`;

ALTER TABLE `process_order_scan`
    ADD COLUMN `process_order_procedure_id` bigint NOT NULL DEFAULT 0 COMMENT '关联的加工工序 ID' AFTER `process_order_scan_id`;

ALTER TABLE `process_order`
    ADD COLUMN `file_code` varchar(255) NOT NULL DEFAULT '' COMMENT '生产图片' AFTER `total_settle_price`;


ALTER TABLE `purchase_settle_order_item`
    ADD COLUMN `status_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '单据状态' AFTER `settle_price`;

ALTER TABLE `process_settle_order_bill`
    ADD COLUMN `status_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '单据状态' AFTER `price`;

ALTER TABLE `purchase_settle_order`
    ADD UNIQUE INDEX `uk_purchase_settle_order_1` (`purchase_settle_status` ASC, `supplier_code` ASC, `month` ASC) USING BTREE;

ALTER TABLE `process_settle_order`
    ADD UNIQUE INDEX `uk_process_settle_order_1` (`month` ASC) USING BTREE;
--------------------------- 调整测试环境表结构（2023-02-06 已执行到验收与生产环境）-------------------------------

ALTER TABLE `process_settle_order`
    MODIFY COLUMN `month` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '月份' AFTER `pay_price`;

ALTER TABLE `purchase_settle_order`
    MODIFY COLUMN `month` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '月份' AFTER `supplier_name`;

ALTER TABLE `purchase_return_order`
    ADD COLUMN `return_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '退货类型' AFTER `receipt_time`;
--------------------------- 调整测试环境表结构（2023-02-08 已执行到验收与生产环境）-------------------------------


--------------------------- 调整测试环境表结构（2023-02-11）-------------------------------
ALTER TABLE `process_order`
    ADD COLUMN `delivery_warehouse_code` varchar(32) NOT NULL DEFAULT '' COMMENT '原料发货仓库编码' AFTER `warehouse_types`,
    ADD COLUMN `delivery_warehouse_name` varchar(32) NOT NULL DEFAULT '' COMMENT '原料发货仓库名称' AFTER `delivery_warehoue_code`;

CREATE TABLE `overseas_warehouse_msg`
(
    `overseas_warehouse_msg_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `overseas_shipping_mark_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '海外仓箱唛号',
    `tracking_no`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '运单号',
    `purchase_child_order_no`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购子单单号',
    `create_time`               datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`               datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`             bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                   int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`overseas_warehouse_msg_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='海外仓信息表';

CREATE TABLE `overseas_warehouse_msg_item`
(
    `overseas_warehouse_msg_item_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `overseas_warehouse_msg_id`      bigint unsigned                                               NOT NULL COMMENT '海外仓信息',
    `overseas_shipping_mark_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '海外仓箱唛号',
    `purchase_child_order_no`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '采购子单单号',
    `sku`                            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `sku_batch_code`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'sku批次码',
    `overseas_warehouse_bar_code`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '海外仓条码',
    `create_time`                    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`                    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`                    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`                  bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                        int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`overseas_warehouse_msg_item_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='海外仓条码子项表';

CREATE TABLE `shipping_mark`
(
    `shipping_mark_id`       bigint unsigned                                               NOT NULL COMMENT '主键id',
    `shipping_mark_no`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '箱唛号',
    `shipping_mark_status`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '箱唛状态',
    `shipping_mark_biz_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '箱唛业务类型',
    `warehouse_code`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '仓库编码',
    `warehouse_name`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '仓库名称',
    `warehouse_types`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '仓库类型',
    `is_direct_send`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '是否直发',
    `supplier_code`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商编码',
    `supplier_name`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商名称',
    `total_deliver`          int                                                           NOT NULL DEFAULT '0' COMMENT '总发货数',
    `box_cnt`                int                                                           NOT NULL DEFAULT '0' COMMENT '箱数',
    `deliver_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '发货人id',
    `deliver_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '发货人名称',
    `deliver_time`           datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '发货时间',
    `tracking_no`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '运单号',
    `create_time`            datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`            datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`          bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`shipping_mark_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='箱唛表';

CREATE TABLE `shipping_mark_item`
(
    `shipping_mark_item_id` bigint unsigned                                              NOT NULL COMMENT '主键id',
    `shipping_mark_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '箱唛号',
    `shipping_mark_num`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '箱唛箱号（序号）',
    `deliver_order_no`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发货单号',
    `biz_child_order_no`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '业务子单单号',
    `deliver_cnt`           int                                                          NOT NULL DEFAULT '0' COMMENT '发货数',
    `create_time`           datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`           datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`         bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`               int unsigned                                                 NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`shipping_mark_item_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='箱唛分箱表';

ALTER TABLE `purchase_child_order`
    ADD COLUMN `is_direct_send` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否直发' AFTER `raw_warehouse_name`;

ALTER TABLE `purchase_deliver_order`
    ADD COLUMN `deliver_date`      datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '业务约定交期' AFTER `receipt_time`,
    ADD COLUMN `is_direct_send`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否直发' AFTER `deliver_date`,
    ADD COLUMN `has_shipping_mark` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否直发' AFTER `is_direct_send`,
    ADD COLUMN `shipping_mark_no`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '箱唛号' AFTER `has_shipping_mark`;

--------------------------- 调整测试环境表结构（2023-02-11）-------------------------------


ALTER TABLE `deduct_order_process`
    ADD COLUMN `settle_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '结算金额' AFTER `deduct_price`;
ALTER TABLE `deduct_order_purchase`
    ADD COLUMN `settle_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '结算金额' AFTER `deduct_price`;
ALTER TABLE `deduct_order_quality`
    ADD COLUMN `settle_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '结算金额' AFTER `deduct_price`;
ALTER TABLE `supplement_order_process`
    ADD COLUMN `settle_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '结算金额' AFTER `supplement_price`;
ALTER TABLE `supplement_order_purchase`
    ADD COLUMN `settle_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '结算金额' AFTER `supplement_price`;
ALTER TABLE `purchase_settle_order`
    MODIFY COLUMN `confirm_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '对账时间' AFTER `confirm_username`;
ALTER TABLE `purchase_settle_order`
    MODIFY COLUMN `settle_refuse_remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '确认拒绝备注' AFTER `about_settle_time`;
ALTER TABLE `purchase_settle_order`
    MODIFY COLUMN `examine_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '供应商确认时间' AFTER `examine_username`;
ALTER TABLE `purchase_settle_order`
    MODIFY COLUMN `settle_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '财务审核时间' AFTER `settle_username`;
ALTER TABLE `purchase_settle_order`
    MODIFY COLUMN `purchase_settle_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购结算单状态' AFTER `purchase_settle_order_no`;
ALTER TABLE `purchase_settle_order`
    MODIFY COLUMN `confirm_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '对账人的用户' AFTER `examine_refuse_remarks`;
ALTER TABLE `purchase_settle_order`
    MODIFY COLUMN `confirm_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '对账人的用户名' AFTER `confirm_user`;
ALTER TABLE `purchase_settle_order`
    MODIFY COLUMN `examine_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商确认人的用户' AFTER `confirm_time`,
    MODIFY COLUMN `examine_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商确认人的用户名' AFTER `examine_user`;
ALTER TABLE `purchase_settle_order`
    MODIFY COLUMN `total_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '对账金额' AFTER `month`,
    MODIFY COLUMN `deduct_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '应扣款金额' AFTER `total_price`,
    MODIFY COLUMN `pay_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '应付金额' AFTER `deduct_price`;


ALTER TABLE `sample_deliver_order`
    ADD COLUMN `has_shipping_mark` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '是否直发' AFTER `warehouse_name`,
    ADD COLUMN `warehouse_types`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '仓库类型' AFTER `warehouse_name`,
    ADD COLUMN `shipping_mark_no`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '箱唛号' AFTER `has_shipping_mark`;

-----------------------------------------已同步至验收与生产环境

ALTER TABLE `purchase_child_order`
    ADD COLUMN `is_upload_overseas_msg` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否直发' AFTER `is_direct_send`;

ALTER TABLE `shipping_mark`
    DROP INDEX `idx_shipping_mark_1`,
    ADD UNIQUE INDEX `uk_shipping_mark_1` (`shipping_mark_no` ASC) USING BTREE;
