use
    cn_scm;

ALTER TABLE `cn_scm`.`process_order_extra`
    ADD COLUMN `defective_receipt_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '次品收货单号' AFTER `receipt_username`,
    ADD COLUMN `defective_receipt_user`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '次品收货人 id' AFTER `defective_receipt_order_no`,
    ADD COLUMN `defective_receipt_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '次品收货人名称' AFTER `defective_receipt_user`;

