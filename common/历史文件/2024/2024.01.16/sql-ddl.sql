ALTER TABLE `cn_scm`.`deduct_order_defective`
    MODIFY COLUMN `deduct_remarks` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扣款原因' AFTER `deduct_price`;

ALTER TABLE `cn_scm`.`deduct_order_other`
    MODIFY COLUMN `deduct_remarks` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扣款备注' AFTER `deduct_price`;

ALTER TABLE `cn_scm`.`deduct_order_pay`
    MODIFY COLUMN `deduct_remarks` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扣款备注' AFTER `deduct_price`;

ALTER TABLE `cn_scm`.`deduct_order_process`
    MODIFY COLUMN `deduct_remarks` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扣款备注' AFTER `settle_price`;

ALTER TABLE `cn_scm`.`deduct_order_purchase`
    MODIFY COLUMN `deduct_remarks` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扣款备注' AFTER `settle_price`;

ALTER TABLE `cn_scm`.`deduct_order_quality`
    MODIFY COLUMN `deduct_remarks` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扣款备注' AFTER `settle_price`;


ALTER TABLE `cn_scm`.`supplement_order_other`
    MODIFY COLUMN `supplement_remarks` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '补款备注' AFTER `supplement_price`;

ALTER TABLE `cn_scm`.`supplement_order_process`
    MODIFY COLUMN `supplement_remarks` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '补款备注' AFTER `settle_price`;

ALTER TABLE `cn_scm`.`supplement_order_purchase`
    MODIFY COLUMN `supplement_remarks` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '补款备注' AFTER `settle_price`;