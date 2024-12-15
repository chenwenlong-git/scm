use cn_scm;
ALTER TABLE `sample_child_order_result`
    ADD COLUMN `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '选样结果说明' AFTER `sample_cnt`;

ALTER TABLE `cn_scm`.`sample_return_order`
    ADD COLUMN `receipt_cnt` int NOT NULL DEFAULT 0 COMMENT '收货数' AFTER `receipt_time`;