ALTER TABLE `cn_scm`.`process_order_material`
    ADD COLUMN `delivery_no` varchar(32) NOT NULL DEFAULT '' COMMENT '出库单编号' AFTER `process_order_no`;