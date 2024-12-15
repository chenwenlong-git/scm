ALTER TABLE `cn_scm`.`supplier_payment_account`
    modify column `account_username` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '户名';

ALTER TABLE `cn_scm`.`finance_settle_order_receive`
    MODIFY COLUMN account_username varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '户名';

ALTER TABLE `cn_scm`.`finance_payment_item`
    MODIFY COLUMN account_username varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '户名';

ALTER TABLE `cn_scm`.`finance_prepayment_order_item`
    MODIFY COLUMN account_username varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '户名';