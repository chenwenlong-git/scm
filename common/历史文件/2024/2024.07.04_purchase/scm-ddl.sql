ALTER TABLE `cn_scm`.`purchase_child_order`
    MODIFY COLUMN `shippable_cnt` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '可发货数' AFTER `timely_delivery_cnt`;