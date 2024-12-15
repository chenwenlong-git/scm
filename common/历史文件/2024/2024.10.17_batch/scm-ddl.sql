ALTER TABLE `cn_scm`.`defect_handling`
    ADD COLUMN `handle_sku`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '处理sku' AFTER `defect_create_username`,
    ADD COLUMN `handle_sku_batch_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '处理sku批次码' AFTER `handle_sku`;