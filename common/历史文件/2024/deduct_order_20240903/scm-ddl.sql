ALTER TABLE `cn_scm`.`deduct_order`
    ADD COLUMN `handle_user`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '处理人' AFTER `price_confirm_time`,
    ADD COLUMN `handle_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '处理人' AFTER `handle_user`,
    ADD COLUMN `handle_time`     datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '处理时间' AFTER `handle_username`;

ALTER TABLE `cn_scm`.`supplement_order`
    ADD COLUMN `handle_user`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '处理人' AFTER `price_confirm_time`,
    ADD COLUMN `handle_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '处理人' AFTER `handle_user`,
    ADD COLUMN `handle_time`     datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '处理时间' AFTER `handle_username`;