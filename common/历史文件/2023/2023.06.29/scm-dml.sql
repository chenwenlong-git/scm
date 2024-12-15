update purchase_return_order
set return_type="QC_NOT_PASSED_PART"
where return_type = "质检不合格";
update purchase_return_order
set return_type="RECEIVE_REJECT"
where return_type = "收货拒收";

UPDATE purchase_deliver_order
SET deliver_order_type = "BULK";

UPDATE purchase_deliver_order pdo JOIN purchase_child_order pco
    ON pdo.purchase_child_order_no = pco.purchase_child_order_no
SET pdo.deliver_order_status = "SETTLE"
WHERE pco.purchase_order_status = "SETTLE"
  AND pco.del_timestamp = 0;

UPDATE purchase_deliver_order pdo
    JOIN purchase_child_order pco
    ON pco.purchase_child_order_no = pdo.purchase_child_order_no
    JOIN purchase_child_order_change pcoc ON pco.purchase_child_order_id = pcoc.purchase_child_order_id
SET pdo.warehousing_time = pcoc.warehousing_time
WHERE pcoc.del_timestamp = 0;

UPDATE purchase_deliver_order pdo JOIN purchase_child_order pco
    ON pdo.purchase_child_order_no = pco.purchase_child_order_no
SET pdo.deliver_order_status = "RECEIVED"
WHERE pco.purchase_order_status = "RECEIVED"
  AND pco.del_timestamp = 0;