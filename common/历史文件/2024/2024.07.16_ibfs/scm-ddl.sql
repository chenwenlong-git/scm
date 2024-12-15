ALTER TABLE `cn_scm`.`finance_prepayment_order_item`
    MODIFY COLUMN `exchange_rate` decimal(13, 2) NOT NULL DEFAULT 0.00 COMMENT '汇率（废弃）' AFTER `currency`,
    MODIFY COLUMN `rmb_prepayment_money` decimal(13, 2) NOT NULL DEFAULT 0.00 COMMENT '收款金额(rmb)(废弃)' AFTER `exchange_rate`,
    ADD COLUMN `target_prepayment_money` decimal(13, 2) NOT NULL DEFAULT 0.00 COMMENT '目标收款金额' AFTER `rmb_prepayment_money`;

ALTER TABLE `cn_scm`.`finance_payment_item`
    MODIFY COLUMN `exchange_rate` decimal(13, 4) NOT NULL DEFAULT 0.0000 COMMENT '目标汇率' AFTER `currency`,
    MODIFY COLUMN `rmb_exchange_rate` decimal(13, 4) NOT NULL DEFAULT 0.0000 COMMENT '兑人民币汇率' AFTER `target_payment_money`;
