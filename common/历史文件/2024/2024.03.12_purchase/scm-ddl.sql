ALTER TABLE `cn_scm`.`purchase_child_order`
    ADD COLUMN `delay_days` int NOT NULL DEFAULT 0 COMMENT '编辑后延期天数' AFTER `finish_remark`;
