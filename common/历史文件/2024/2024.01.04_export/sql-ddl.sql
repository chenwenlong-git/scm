ALTER TABLE `cn_scm`.`qc_order`
    ADD COLUMN `is_urgent_order` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否加急' AFTER `sku_dev_type`,
    ADD COLUMN `supplier_code`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商代码(收货单)' AFTER `is_urgent_order`,
    ADD COLUMN `supplier_name`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '供应商名称(收货单)' AFTER `supplier_code`;

ALTER TABLE `cn_scm`.`qc_detail`
    ADD COLUMN `category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类名(一级或二级名称)' AFTER `relation_qc_detail_id`;