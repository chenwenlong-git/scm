ALTER TABLE `cn_scm`.`purchase_child_order_raw`
    ADD COLUMN `raw_supplier` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '原料提供方:HETE(赫特),SUPPLIER(供应商),' AFTER `purchase_raw_biz_type`;

ALTER TABLE `cn_scm`.`purchase_parent_order`
    ADD COLUMN `is_importation` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否导入生成:TRUE(是),FALSE(否),' AFTER `undelivered_cnt`;
ALTER TABLE `cn_scm`.`purchase_child_order`
    ADD COLUMN `is_importation` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否导入生成:TRUE(是),FALSE(否),' AFTER `is_upload_overseas_msg`,
    ADD COLUMN `raw_remain_tab` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '原料是否剩余:TRUE(是),FALSE(否),' AFTER `is_importation`;