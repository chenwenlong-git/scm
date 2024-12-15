ALTER TABLE `cn_scm`.`purchase_deliver_order`
    MODIFY COLUMN `deliver_cnt` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '发货数' AFTER `tracking_no`;


ALTER TABLE `cn_scm`.`purchase_deliver_order_item`
    MODIFY COLUMN `deliver_cnt` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '发货数' AFTER `variant_properties`,
    MODIFY COLUMN `receipt_cnt` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '收货数' AFTER `deliver_cnt`;