ALTER TABLE `cn_scm`.`purchase_child_order_raw`
    ADD COLUMN `raw_warehouse_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '原料仓库' AFTER `dispense_cnt`,
    ADD COLUMN `raw_warehouse_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '原料仓库' AFTER `raw_warehouse_code`;

ALTER TABLE `cn_scm`.`purchase_child_order_raw_deliver`
    ADD COLUMN `dispense_cnt` int NOT NULL DEFAULT 0 COMMENT '分配数量' AFTER `purchase_raw_deliver_order_no`;

ALTER TABLE `cn_scm`.`purchase_child_order_raw`
    ADD COLUMN `receipt_cnt` int NOT NULL DEFAULT 0 COMMENT '收货数' AFTER `delivery_cnt`;

ALTER TABLE `cn_scm`.`produce_data`
    ADD COLUMN `raw_manage` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '原料管理:TRUE(需管理),FALSE(无需管理)' AFTER `goods_purchase_price_time`;