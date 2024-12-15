ALTER TABLE `cn_scm`.`develop_child_order`
    ADD COLUMN `purchase_parent_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购母单单号' AFTER `first_sample_order_no`;

ALTER TABLE `cn_scm`.`purchase_parent_order`
    ADD COLUMN `develop_child_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '开发子单号' AFTER `purchase_demand_type`;