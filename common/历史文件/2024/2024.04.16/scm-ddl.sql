ALTER TABLE `cn_scm`.`purchase_child_order`
    ADD COLUMN `promise_date` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '答交时间' AFTER `split_type`;

ALTER table process_order
    ADD COLUMN
        `promise_date` datetime DEFAULT '1970-01-01 00:00:00' NOT NULL COMMENT '答交时间：经过重新评估和调整后提出新的deliver_date'
        AFTER `deliver_date`;
ALTER table process_order
    ADD COLUMN
        promise_date_delayed varchar(32) default '' not null comment '答交时间是否延期 DELAYED（延期） NOT_DELAYED （正常）'
        AFTER `promise_date`;