ALTER TABLE `cn_scm`.`finance_prepayment_order`
    ADD COLUMN `currency` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '币种' AFTER `prepayment_money`,
    MODIFY COLUMN `prepayment_money` decimal(13, 2) NOT NULL DEFAULT 0.00 COMMENT '预付金额' AFTER `prepayment_reason`;

ALTER TABLE `cn_scm`.`finance_prepayment_order_item`
    MODIFY COLUMN `prepayment_money` decimal(13, 2) NOT NULL DEFAULT 0.00 COMMENT '预付金额' AFTER `bank_area`;

ALTER TABLE `cn_scm`.`finance_payment_item`
    MODIFY COLUMN `exchange_rate` decimal(13, 2) NOT NULL DEFAULT 0.00 COMMENT '目标汇率' AFTER `currency`,
    ADD COLUMN `target_payment_money` decimal(13, 2) NOT NULL DEFAULT 0.00 COMMENT '目标付款金额' AFTER `exchange_rate`,
    ADD COLUMN `rmb_exchange_rate`    decimal(13, 2) NOT NULL DEFAULT 0.00 COMMENT '兑人民币汇率' AFTER `target_payment_money`;

ALTER TABLE `cn_scm`.`finance_prepayment_order`
    ADD COLUMN `target_payment_money` decimal(13, 2) NOT NULL DEFAULT 0.00 COMMENT '目标付款金额金额(rmb)' AFTER `follow_user`;