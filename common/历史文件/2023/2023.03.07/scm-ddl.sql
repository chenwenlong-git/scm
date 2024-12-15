use
    cn_scm;
ALTER TABLE `process_order`
    MODIFY COLUMN `file_code` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '生产图片' AFTER `total_settle_price`;