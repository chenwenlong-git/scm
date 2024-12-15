ALTER TABLE `cn_scm`.`qc_detail`
    ADD COLUMN `platform` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '平台' AFTER `qc_order_no`;

ALTER TABLE `cn_scm`.`defect_handling`
    ADD COLUMN `platform` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '平台' AFTER `defect_handling_type`;


ALTER TABLE `cn_scm`.`purchase_return_order`
    ADD COLUMN `platform` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '平台' AFTER `return_order_status`;