use
    cn_scm;

ALTER TABLE `cn_scm`.`goods_process_relation`
    DROP INDEX `uk_goods_process_relation_1`,
    ADD INDEX `idx_goods_process_relation_2` (`process_id` ASC, `goods_process_id` ASC, `del_timestamp` ASC) USING BTREE;