ALTER TABLE `cn_scm`.`develop_sample_order`
    ADD COLUMN `return_tracking_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '退样运单号' AFTER `sku_batch_purchase_price`;
ALTER TABLE `cn_scm`.`develop_sample_order`
    ADD COLUMN `develop_sample_direction` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '货物走向:WAREHOUSING(入库),RETURN_SAMPLES(退样)' AFTER `return_tracking_no`;