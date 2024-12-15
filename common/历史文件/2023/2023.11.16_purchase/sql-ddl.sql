# 删除唯一索引之前的校验
select count(1)
from `purchase_parent_order_change`
GROUP BY `purchase_parent_order_id`
having count(1) > 1;

select count(1)
from `purchase_child_order_change`
GROUP BY `purchase_child_order_id`
having count(1) > 1;


ALTER TABLE `cn_scm`.`purchase_parent_order_change`
    ADD COLUMN `purchase_parent_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购母单单号' AFTER `purchase_parent_order_id`,
    DROP INDEX `uk_purchase_parent_order_change_1`,
    ADD UNIQUE INDEX `uk_purchase_parent_order_change_1` (`purchase_parent_order_id` ASC) USING BTREE;

ALTER TABLE `cn_scm`.`purchase_child_order_change`
    ADD COLUMN `purchase_child_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购子单单号' AFTER `purchase_child_order_id`;


ALTER TABLE `cn_scm`.`purchase_child_order`
    MODIFY COLUMN `expected_on_shelves_date` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '期望上架时间' AFTER `is_overdue`;
