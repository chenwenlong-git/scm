ALTER TABLE `cn_scm`.`supplier`
    ADD COLUMN `logistics_aging` int NOT NULL DEFAULT 0 COMMENT '物流时效' AFTER `remarks`;


ALTER TABLE `cn_scm`.`purchase_parent_order`
    CHANGE COLUMN `purchase_order_status` `purchase_parent_order_status` varchar(32) NOT NULL DEFAULT '' COMMENT '采购单状态' AFTER `deliver_date`,
    MODIFY COLUMN `is_normal_order` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否正常采购' AFTER `is_urgent_order`;


ALTER TABLE `cn_scm`.`purchase_child_order`
    ADD COLUMN `is_overdue`               varchar(32) NOT NULL DEFAULT '' COMMENT '是否超期:TRUE(是),FALSE(否),' AFTER `source_purchase_child_order_no`,
    ADD COLUMN `expected_on_shelves_date` datetime    NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '业务约定交期' AFTER `is_overdue`;