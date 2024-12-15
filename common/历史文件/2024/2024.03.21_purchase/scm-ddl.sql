ALTER TABLE `cn_scm`.`produce_data_item`
    MODIFY COLUMN `sort` int NOT NULL DEFAULT 0 COMMENT '排序（数值越大优先级越高）' AFTER `business_no`;