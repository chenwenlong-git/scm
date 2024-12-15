ALTER TABLE `cn_scm`.`purchase_child_order_change`
    ADD COLUMN `last_modify_time`     datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '最后修改期望上架时间' AFTER `warehousing_time`,
    ADD COLUMN `last_modify_user`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '最后修改期望上架时间人' AFTER `warehousing_username`,
    ADD COLUMN `last_modify_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '最后修改期望上架时间人' AFTER `last_modify_user`;