ALTER TABLE `cn_scm`.`purchase_child_order`
    ADD COLUMN `timely_delivery_cnt`  int           NOT NULL DEFAULT 0 COMMENT '准交数' AFTER `expected_on_shelves_date`,
    ADD COLUMN `timely_delivery_rate` decimal(5, 2) NOT NULL DEFAULT 0 COMMENT '准交率' AFTER `timely_delivery_cnt`,
    ADD COLUMN `timely_delivery_time` datetime      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '最后准交时间' AFTER `timely_delivery_rate`;
