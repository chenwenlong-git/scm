ALTER TABLE `cn_scm`.`supplement_order_purchase`
    MODIFY COLUMN `supplement_order_purchase_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联单据类型：PRODUCT_PURCHASE(大货采购单),PROCESS_PURCHASE(加工采购单),SAMPLE(样品采购单),DELIVER(发货单),PURCHASE_RETURN(退货单)' AFTER `business_no`,
    ADD COLUMN `sku_batch_code`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku批次码' AFTER `supplement_remarks`,
    ADD COLUMN `settle_unit_price` decimal(10, 2)                                               NOT NULL DEFAULT 0.00 COMMENT '结算单价' AFTER `sku_batch_code`;

ALTER TABLE `cn_scm`.`deduct_order_purchase`
    ADD COLUMN `sku_batch_code`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sku批次码' AFTER `deduct_remarks`,
    ADD COLUMN `settle_unit_price` decimal(10, 2)                                               NOT NULL DEFAULT 0.00 COMMENT '结算单价' AFTER `sku_batch_code`;


ALTER TABLE `cn_scm`.`deduct_order`
    MODIFY COLUMN `deduct_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扣款状态：WAIT_SUBMIT(待提交),WAIT_PRICE(价格确认),WAIT_CONFIRM(待确认),WAIT_EXAMINE(待审核),AUDITED(已审核),SETTLE(已结算),REFUSED(已拒绝)' AFTER `deduct_type`,
    ADD COLUMN `price_refuse_remarks`   varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '价格拒绝备注' AFTER `settle_order_no`,
    ADD COLUMN `price_confirm_user`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '价格确认人的用户' AFTER `price_refuse_remarks`,
    ADD COLUMN `price_confirm_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '价格确认人的用户名' AFTER `price_confirm_user`,
    ADD COLUMN `price_confirm_time`     datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '价格确认时间' AFTER `price_confirm_username`;

ALTER TABLE `cn_scm`.`supplement_order`
    MODIFY COLUMN `supplement_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '补款状态：待提交(WAIT_SUBMIT)、WAIT_PRICE(价格确认)、待确认(WAIT_CONFIRM)、待审核(WAIT_EXAMINE)、已审核(AUDITED)、已结算(SETTLE)' AFTER `supplement_type`,
    ADD COLUMN `price_refuse_remarks`   varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '价格拒绝备注' AFTER `settle_order_no`,
    ADD COLUMN `price_confirm_user`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '价格确认人的用户' AFTER `price_refuse_remarks`,
    ADD COLUMN `price_confirm_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '价格确认人的用户名' AFTER `price_confirm_user`,
    ADD COLUMN `price_confirm_time`     datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '价格确认时间' AFTER `price_confirm_username`;

ALTER TABLE `cn_scm`.`deduct_order_defective`
    MODIFY COLUMN `deduct_remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扣款原因' AFTER `deduct_price`;
ALTER TABLE `cn_scm`.`deduct_order_other`
    MODIFY COLUMN `deduct_remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扣款备注' AFTER `deduct_price`;
ALTER TABLE `cn_scm`.`deduct_order_pay`
    MODIFY COLUMN `deduct_remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扣款备注' AFTER `deduct_price`;
ALTER TABLE `cn_scm`.`deduct_order_process`
    MODIFY COLUMN `deduct_remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扣款备注' AFTER `settle_price`;
ALTER TABLE `cn_scm`.`deduct_order_purchase`
    MODIFY COLUMN `deduct_remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扣款备注' AFTER `settle_price`;
ALTER TABLE `cn_scm`.`deduct_order_quality`
    MODIFY COLUMN `deduct_remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扣款备注' AFTER `settle_price`;
ALTER TABLE `cn_scm`.`supplement_order_other`
    MODIFY COLUMN `supplement_remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '补款备注' AFTER `supplement_price`;
ALTER TABLE `cn_scm`.`supplement_order_process`
    MODIFY COLUMN `supplement_remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '补款备注' AFTER `settle_price`;
ALTER TABLE `cn_scm`.`supplement_order_purchase`
    MODIFY COLUMN `supplement_remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '补款备注' AFTER `settle_price`;


ALTER TABLE `cn_scm`.`deduct_order`
    MODIFY COLUMN `confirm_refuse_remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '确认拒绝备注' AFTER `about_settle_time`,
    MODIFY COLUMN `examine_refuse_remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '审核拒绝备注' AFTER `confirm_refuse_remarks`;
ALTER TABLE `cn_scm`.`supplement_order`
    MODIFY COLUMN `confirm_refuse_remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '确认拒绝备注' AFTER `about_settle_time`,
    MODIFY COLUMN `examine_refuse_remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '审核拒绝备注' AFTER `confirm_refuse_remarks`;