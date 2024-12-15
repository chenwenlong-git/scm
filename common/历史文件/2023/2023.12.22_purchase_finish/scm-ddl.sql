ALTER TABLE `cn_scm`.`purchase_child_order_item`
    ADD COLUMN `undelivered_cnt` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '采购未交数' AFTER `purchase_deliver_order_no`;

ALTER TABLE `cn_scm`.`purchase_parent_order_item`
    ADD COLUMN `can_split_cnt` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '采购可拆单数' AFTER `undelivered_cnt`;

ALTER TABLE `cn_scm`.`purchase_parent_order`
    ADD COLUMN `can_split_cnt` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '采购可拆单数' AFTER `is_importation`;

ALTER TABLE `cn_scm`.`purchase_child_order`
    ADD COLUMN `finish_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '来货终止备注' AFTER `timely_delivery_time`;

ALTER TABLE `cn_scm`.`purchase_child_order_change`
    ADD COLUMN `plan_confirm_user`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '计划确认人' AFTER `purchase_settle_order_no`,
    ADD COLUMN `plan_confirm_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '计划确认人' AFTER `plan_confirm_user`,
    ADD COLUMN `plan_confirm_time`     datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '计划确认时间' AFTER `plan_confirm_username`;
