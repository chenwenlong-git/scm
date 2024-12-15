ALTER TABLE `cn_scm`.`develop_sample_order`
    ADD COLUMN `develop_sample_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '样品单类型：NORMAL(常规),PRENATAL_SAMPLE(产前样),' AFTER `purchase_price`;

ALTER TABLE `cn_scm`.`develop_sample_order`
    ADD COLUMN `send_user`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '寄送人' AFTER `develop_sample_type`,
    ADD COLUMN `send_username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '寄送人' AFTER `send_user`,
    ADD COLUMN `send_time`     datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '寄送时间' AFTER `send_username`;

ALTER TABLE `cn_scm`.`develop_review_order`
    ADD COLUMN `develop_review_related` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '审版关联单据类型:PURCHASE(采购),DEVELOP_SAMPLE(开发样品),NORMAL(普通),' AFTER `poor_amount`;