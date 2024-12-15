ALTER TABLE `cn_scm`.`goods_price_item`
    ADD COLUMN `goods_price_effective_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '生效状态：WAIT_EXAMINE("待审核"),WAIT_EFFECTIVE("待生效"),EFFECTIVE("当前生效"),INVALID("失效")' AFTER `goods_price_item_status`,
    ADD INDEX `idx_goods_price_item_1` (`goods_price_id` ASC) USING BTREE,
    ADD INDEX `idx_goods_price_item_2` (`sku` ASC) USING BTREE;

ALTER TABLE `cn_scm`.`goods_price_approve`
    ADD INDEX `idx_goods_price_approve_1` (`adjust_price_approve_no` ASC, `supplier_code` ASC) USING BTREE;

ALTER TABLE `cn_scm`.`produce_data`
    ADD INDEX `idx_produce_data_1` (`sku` ASC) USING BTREE,
    ADD INDEX `idx_produce_data_2` (`spu` ASC) USING BTREE;

ALTER TABLE `cn_scm`.`produce_data_attr`
    ADD INDEX `idx_produce_data_attr_1` (`sku` ASC) USING BTREE,
    ADD INDEX `idx_produce_data_attr_2` (`spu` ASC) USING BTREE;

ALTER TABLE `cn_scm`.`produce_data_item`
    ADD INDEX `idx_produce_data_item_2` (`spu` ASC) USING BTREE;

ALTER TABLE `cn_scm`.`produce_data_item_process`
    ADD INDEX `idx_produce_data_item_process_1` (`sku` ASC) USING BTREE,
    ADD INDEX `idx_produce_data_item_process_2` (`spu` ASC) USING BTREE,
    ADD INDEX `idx_produce_data_item_process_3` (`produce_data_item_id` ASC) USING BTREE;

ALTER TABLE `cn_scm`.`produce_data_item_process_desc`
    ADD INDEX `idx_produce_data_item_process_desc_1` (`sku` ASC) USING BTREE,
    ADD INDEX `idx_produce_data_item_process_desc_2` (`spu` ASC) USING BTREE,
    ADD INDEX `idx_produce_data_item_process_desc_3` (`produce_data_item_id` ASC) USING BTREE;

ALTER TABLE `cn_scm`.`produce_data_item_raw`
    DROP INDEX `idx_produce_data_item_raw_1`,
    ADD INDEX `idx_produce_data_item_raw_1` (`sku`) USING BTREE,
    ADD INDEX `idx_produce_data_item_raw_2` (`spu` ASC) USING BTREE,
    ADD INDEX `idx_produce_data_item_raw_3` (`produce_data_item_id` ASC) USING BTREE,
    ADD INDEX `idx_produce_data_item_raw_4` (`material_type` ASC) USING BTREE;

ALTER TABLE `cn_scm`.`produce_data_spec`
    ADD INDEX `idx_produce_data_spec_1` (`sku` ASC) USING BTREE,
    ADD INDEX `idx_produce_data_spec_2` (`spu` ASC) USING BTREE;

ALTER TABLE `cn_scm`.`produce_data_spu`
    ADD INDEX `idx_produce_data_spu_1` (`spu` ASC) USING BTREE;

ALTER TABLE `cn_scm`.`goods_price_item`
    MODIFY COLUMN `goods_price_item_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '审批状态：TO_BE_APPROVE(待审批)APPROVE_PASSED(审批通过),APPROVE_REJECT(审批拒绝)' AFTER `adjust_price_approve_no`;