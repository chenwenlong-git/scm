ALTER TABLE `cn_scm`.`produce_data_item_process`
    MODIFY COLUMN `process_second_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '二级工序代码' AFTER `process_first`,
    MODIFY COLUMN `process_second_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '二级工序名称' AFTER `process_second_code`,
    ADD COLUMN `process_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '工序代码' AFTER `process_second_name`,
    ADD COLUMN `process_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '工序名称' AFTER `process_code`;

ALTER TABLE `cn_scm`.`develop_sample_order_process`
    ADD COLUMN `process_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '工序代码' AFTER `process_first`,
    ADD COLUMN `process_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '工序名称' AFTER `process_code`,
    MODIFY COLUMN `process_second_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '二级工序代码' AFTER `process_first`,
    MODIFY COLUMN `process_second_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '二级工序名称' AFTER `process_second_code`;