use
    scm;

ALTER TABLE `cn_scm`.`purchase_parent_order`
    ADD COLUMN `undelivered_cnt` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '采购未交数' AFTER `is_direct_send`;

ALTER TABLE `cn_scm`.`purchase_parent_order_item`
    ADD COLUMN `undelivered_cnt` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '采购未交数' AFTER `defective_goods_cnt`;

ALTER TABLE `cn_scm`.`purchase_child_order_item`
    ADD COLUMN `purchase_deliver_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购发货单号' AFTER `defective_goods_cnt`;