ALTER TABLE `cn_scm`.`develop_child_order_attr`
    ADD COLUMN `attribute_name_id` bigint NOT NULL DEFAULT 0 COMMENT '属性id' AFTER `develop_parent_order_no`;

ALTER TABLE `cn_scm`.`produce_data_attr`
    ADD COLUMN `attribute_name_id` bigint NOT NULL DEFAULT 0 COMMENT '属性id' AFTER `sku`;

ALTER TABLE `cn_scm`.`develop_review_sample_order_info`
    ADD COLUMN `attribute_name_id` bigint NOT NULL DEFAULT 0 COMMENT '属性id' AFTER `develop_sample_order_no`;

ALTER TABLE process_order_sample
    ADD COLUMN source_document_number VARCHAR(255) NOT NULL DEFAULT '0' COMMENT '来源单据号(PLM生产属性主键id)' after sample_child_order_no;