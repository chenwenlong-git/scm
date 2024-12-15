ALTER TABLE `cn_scm`.`purchase_child_order`
    ADD COLUMN `shippable_cnt` int NOT NULL DEFAULT 0 COMMENT '可发货数' AFTER `timely_delivery_cnt`;

ALTER TABLE `cn_scm`.`purchase_return_order`
    ADD COLUMN `purchase_child_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购子单单号' AFTER `return_create_username`;