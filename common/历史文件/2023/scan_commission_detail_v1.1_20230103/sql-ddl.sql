alter table process
    add column `extra_commission` decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '额外提成单价' after commission;
alter table process_order_scan
    add column `extra_commission` decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '额外提成单价' after process_commission;