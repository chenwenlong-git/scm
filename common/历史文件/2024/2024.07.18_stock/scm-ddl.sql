ALTER TABLE `cn_scm`.`supplier_inventory_record`
    ADD COLUMN `approve_user`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '审批人' AFTER `supplier_inventory_record_status`,
    ADD COLUMN `approve_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '审批人' AFTER `approve_user`,
    ADD COLUMN `effective_time`   datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '生效时间' AFTER `approve_username`;