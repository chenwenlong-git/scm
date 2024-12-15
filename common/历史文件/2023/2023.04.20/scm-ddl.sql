use
    cn_scm;

ALTER TABLE `cn_scm`.`sample_child_order_result`
    ADD COLUMN `sample_result_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '状态' AFTER `sample_result`;


