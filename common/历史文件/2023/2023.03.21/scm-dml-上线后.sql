use
    cn_scm;

update `cn_scm`.`process_order`
set process_order_status="PROCESSING"
where process_order_status in ("WAIT_HANDLE", "HANDLING", "HANDLED");

update `cn_scm`.`purchase_settle_order`
set del_timestamp = 20230321060643
where purchase_settle_status in ("WAIT_CONFIRM");

update `cn_scm`.`purchase_settle_order_item` as item inner join `cn_scm`.`purchase_settle_order` as purchase
    on purchase.purchase_settle_order_id = item.purchase_settle_order_id
set item.del_timestamp = 20230321060643
where purchase.purchase_settle_status in ("WAIT_CONFIRM");

update `cn_scm`.`purchase_settle_order_pay` as item inner join `cn_scm`.`purchase_settle_order` as purchase
    on purchase.purchase_settle_order_id = item.purchase_settle_order_id
set item.del_timestamp = 20230321060643
where purchase.purchase_settle_status in ("WAIT_CONFIRM");


