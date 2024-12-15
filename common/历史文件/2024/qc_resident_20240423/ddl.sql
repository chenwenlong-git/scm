alter table qc_order
    add column purchase_child_order_no varchar(32) default '' not null comment '采购订单号（采购子单号）'
        after supplier_name;

ALTER TABLE qc_order
    ADD COLUMN qc_source_order_no   varchar(32) default '' not null comment '来源单据号' after process_order_no,
    ADD COLUMN qc_source_order_type varchar(32) default '' not null comment '来源单据类型' after qc_source_order_no