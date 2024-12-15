ALTER TABLE `cn_scm`.`deduct_order`
    MODIFY COLUMN `deduct_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扣款类型：PRICE(价差扣款),PROCESS(加工扣款),QUALITY(品质扣款),OTHER(其他),PAY(预付款),DEFECTIVE_RETURN(次品退供)' AFTER `deduct_order_no`,
    MODIFY COLUMN `deduct_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扣款状态：WAIT_SUBMIT(待提交),WAIT_CONFIRM(待确认),WAIT_EXAMINE(待审核),AUDITED(已审核),SETTLE(已结算),REFUSED(已拒绝)' AFTER `deduct_type`;

ALTER TABLE `cn_scm`.`deduct_order_purchase`
    MODIFY COLUMN `deduct_order_purchase_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联单据类型：PRODUCT_PURCHASE(大货采购单),PROCESS_PURCHASE(加工采购单),PURCHASE_RETURN(采购退货单),SAMPLE_RETURN(样品退货单),SAMPLE(样品采购单),DELIVER(发货单)' AFTER `business_no`;

ALTER TABLE `cn_scm`.`supplement_order_purchase`
    MODIFY COLUMN `supplement_order_purchase_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联单据类型：PRODUCT_PURCHASE(大货采购单),PROCESS_PURCHASE(加工采购单),SAMPLE(样品采购单),DELIVER(发货单)' AFTER `business_no`;

ALTER TABLE `cn_scm`.`purchase_settle_order_item`
    MODIFY COLUMN `purchase_settle_item_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联单据类型：PRODUCT_PURCHASE(大货采购单),PROCESS_PURCHASE(加工采购单),SAMPLE(样品采购单),REPLENISH(补款单),DEDUCT(扣款单),DELIVER(发货单)' AFTER `business_no`;

CREATE TABLE `deduct_order_defective`
(
    `deduct_order_defective_id` bigint unsigned                                               NOT NULL COMMENT '主键id',
    `deduct_order_id`           bigint                                                        NOT NULL COMMENT '扣款单ID',
    `business_no`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '关联单据号',
    `sku`                       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `sku_batch_code`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'sku批次码',
    `deduct_num`                int                                                           NOT NULL DEFAULT '0' COMMENT '扣款数量',
    `settle_price`              decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '原结算单价',
    `deduct_unit_price`         decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '需扣款单价',
    `deduct_price`              decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '扣款总价',
    `deduct_remarks`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扣款原因',
    `create_time`               datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `update_time`               datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `del_timestamp`             bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                   int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`deduct_order_defective_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='扣款单表次品退供明细';


CREATE TABLE `defect_handling`
(
    `defect_handling_id`        bigint unsigned                                               NOT NULL COMMENT '主键id',
    `defect_handling_no`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '次品处理单号',
    `defect_handling_status`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '次品处理状态:WAIT_CONFIRM(待确认),CONFIRMED(已确认),CONFIRMED_FAIL(处理失败),',
    `defect_handling_programme` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '次品方案:RETURN_SUPPLY(次品退供),SCRAPPED(次品报废),EXCHANGE_GOODS(次品换货),COMPROMISE(次品让步),',
    `defect_handling_type`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '次品类型:BULK_DEFECT(质检不合格),PROCESS_DEFECT(加工次品),INSIDE_DEFECT(库内抽查),',
    `receive_order_no`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '收货单号',
    `defect_biz_no`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '次品来源单据号：采购单号、加工单号',
    `qc_order_no`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '质检单号',
    `biz_detail_id`             bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '质检id',
    `related_order_no`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '关联单号：采购发货单号、加工次品记录单号',
    `supplier_code`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商编码',
    `supplier_name`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '供应商名称',
    `return_order_no`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '退货单号',
    `sku`                       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku',
    `sku_batch_code`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'sku批次码',
    `return_cnt`                int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '退货数量',
    `qc_cnt`                    int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '质检数量',
    `pass_cnt`                  int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '合格数',
    `not_pass_cnt`              int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '不合格数',
    `adverse_reason`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '不良原因',
    `confirm_user`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '确认人',
    `confirm_username`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '确认人',
    `confirm_time`              datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '确认时间',
    `settle_price`              decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '结算金额',
    `fail_reason`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '处理失败原因',
    `warehouse_code`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '收货仓库编码',
    `defect_create_user`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '次品处理创建人',
    `defect_create_username`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '次品处理创建人名称',
    `create_time`               datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`               datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_username`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `del_timestamp`             bigint unsigned                                               NOT NULL DEFAULT '0' COMMENT '是否删除(0：未删除，非0（14位时间戳）：已删除)',
    `version`                   int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '版本号',
    PRIMARY KEY (`defect_handling_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='次品处理';

ALTER TABLE `cn_scm`.`purchase_return_order`
    CHANGE COLUMN `purchase_return_order_no` `return_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '退货单号' AFTER `purchase_return_order_id`,
    CHANGE COLUMN `return_cnt` `reality_return_cnt` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '实际退货数',
    ADD COLUMN `return_biz_no`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '退货单来源单据号' AFTER `return_order_status`,
    ADD COLUMN `return_user`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '退货信息确认人' AFTER `return_biz_no`,
    ADD COLUMN `return_username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '退货信息确认人' AFTER `return_user`,
    ADD COLUMN `return_time`            datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '退货时间' AFTER `return_username`,
    ADD COLUMN `expected_return_cnt`    int UNSIGNED                                                 NOT NULL DEFAULT 0 COMMENT '预计退货总数' AFTER `tracking_no`,
    ADD COLUMN `receipt_cnt`            int UNSIGNED                                                 NOT NULL DEFAULT 0 COMMENT '收货总数' AFTER `expected_return_cnt`,
    ADD COLUMN `related_biz_no`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联单据号' AFTER `receipt_time`,
    ADD COLUMN `related_biz_type`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联单据类型:DEDUCT_ORDER(扣款单),DELIVER_ORDER(发货单),' AFTER `related_biz_no`,
    ADD COLUMN `related_biz_time`       datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '关联单据创建时间' AFTER `related_biz_type`,
    ADD COLUMN `note`                   varchar(255)                                                 NOT NULL DEFAULT '' COMMENT '退货单备注' AFTER `related_biz_time`,
    ADD COLUMN `return_create_user`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '退货创建人' AFTER `return_type`,
    ADD COLUMN `return_create_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '退货创建人名称' AFTER `return_create_user`,
    COMMENT = '退货单';

ALTER TABLE `cn_scm`.`purchase_return_order_item`
    CHANGE COLUMN `purchase_return_order_no` `return_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '退货单号' AFTER `purchase_return_order_item_id`,
    CHANGE COLUMN `purchase_child_order_no` `return_biz_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '退货单来源单据号' AFTER `sku_batch_code`,
    CHANGE COLUMN `return_cnt` `reality_return_cnt` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '实际退货数' AFTER `return_biz_no`,
    ADD COLUMN `expected_return_cnt` int UNSIGNED    NOT NULL DEFAULT 0 COMMENT '预计退货数' AFTER `reality_return_cnt`,
    ADD COLUMN `settle_price`        decimal(10, 2)  NOT NULL DEFAULT 0.00 COMMENT '结算金额' AFTER `receipt_cnt`,
    ADD COLUMN `deduct_price`        decimal(10, 2)  NOT NULL DEFAULT 0.00 COMMENT '需扣款金额' AFTER `settle_price`,
    ADD COLUMN `sku_encode`          varchar(255)    NOT NULL DEFAULT '' COMMENT '产品名称' AFTER `sku`,
    ADD COLUMN `biz_detail_id`       bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '质检id' AFTER `deduct_price`,
    COMMENT = '退货单明细';

ALTER TABLE `cn_scm`.`purchase_deliver_order`
    MODIFY COLUMN `deliver_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发货状态:WAIT_DELIVER(待发货),WAIT_RECEIVE(待收货),RECEIVED(已收货),WAIT_QC(待质检),WAIT_WAREHOUSING(待入库),WAREHOUSED(已入库),DELETED(作废),SETTLE(已结算)' AFTER `purchase_receipt_order_no`,
    ADD COLUMN `warehousing_time`   datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '入库时间' AFTER `shipping_mark_no`,
    ADD COLUMN `deliver_order_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发货单类型：BULK(正常大货),RETURN_RESEND(退货重发)' AFTER `warehousing_time`;
