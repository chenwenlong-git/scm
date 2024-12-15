ALTER TABLE `cn_scm`.`qc_detail`
    ADD INDEX `idx_qc_detail_2` (`category_name` ASC) USING BTREE;
ALTER TABLE `cn_scm`.`qc_order`
    ADD INDEX `idx_qc_order_1` (`create_time` ASC) USING BTREE;