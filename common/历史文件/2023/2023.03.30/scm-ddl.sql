use
    cn_scm;

ALTER TABLE `cn_scm`.`purchase_child_order_raw`
    ADD COLUMN `purchase_raw_biz_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '采购原料业务类型' AFTER `delivery_cnt`;
