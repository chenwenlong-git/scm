use
    cn_scm;

ALTER TABLE `cn_scm`.`sample_child_order`
    ADD COLUMN `sample_produce_label` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '生产标签' AFTER `sample_dev_type`;
