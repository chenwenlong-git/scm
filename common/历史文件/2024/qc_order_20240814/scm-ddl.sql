ALTER TABLE `cn_scm`.`qc_receive_order`
    ADD INDEX `idx_qc_receive_order_3` (`supplier_code` ASC) USING BTREE,
    ADD INDEX `idx_qc_receive_order_4` (`scm_biz_no` ASC) USING BTREE;