ALTER TABLE `cn_scm`.`qc_order`
    DROP INDEX `idx_qc_order_1`;
ALTER TABLE `cn_scm`.`qc_order`
    DROP INDEX `idx_qc_order_4`;

ALTER TABLE `cn_scm`.`qc_order`
    ADD INDEX `idx_qc_order_1` (`qc_source_order_no`) USING BTREE;
ALTER TABLE `cn_scm`.`qc_order`
    ADD INDEX `idx_qc_order_4` (`qc_source_order_type`) USING BTREE;