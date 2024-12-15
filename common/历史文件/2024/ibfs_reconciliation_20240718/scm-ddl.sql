ALTER TABLE `cn_scm`.`finance_reco_order`
    ADD COLUMN `remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注' AFTER `comment`,
    MODIFY COLUMN `comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '工厂确认意见' AFTER `follow_username`;
ALTER TABLE `cn_scm`.`finance_settle_order`
    ADD COLUMN `remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注' AFTER `settle_finish_time`;