ALTER TABLE `cn_scm`.`purchase_raw_receipt_order`
    MODIFY COLUMN `receipt_order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收货状态:WAIT_DELIVER("待发货"),WAIT_RECEIVE(待收货),RECEIPTED(已收货),CANCEL("已取消"),' AFTER `purchase_raw_receipt_order_no`;

ALTER TABLE `cn_scm`.`purchase_raw_receipt_order`
    MODIFY COLUMN `raw_receipt_biz_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '原料收货类型:PURCHASE(采购原料),SAMPLE(样品原料),DEVELOP(样品原料),' AFTER `supplier_name`;