ALTER TABLE `cn_scm`.`supplier_payment_account`
    MODIFY COLUMN `account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '账号' AFTER `supplier_payment_account_id`,
    MODIFY COLUMN `bank_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '账号银行' AFTER `supplier_payment_currency_type`;


ALTER TABLE `cn_scm`.`finance_settle_order_receive`
    MODIFY COLUMN `account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收款账户' AFTER `supplier_code`,
    MODIFY COLUMN `bank_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '银行账号' AFTER `supplier_payment_account_type`;

ALTER TABLE `cn_scm`.`finance_payment_item`
    MODIFY COLUMN `account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收款账户' AFTER `supplier_code`,
    MODIFY COLUMN `bank_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '开户银行' AFTER `supplier_payment_account_type`;


ALTER TABLE `cn_scm`.`finance_prepayment_order_item`
    MODIFY COLUMN `account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收款账户' AFTER `supplier_code`,
    MODIFY COLUMN `bank_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '银行账号' AFTER `supplier_payment_account_type`;