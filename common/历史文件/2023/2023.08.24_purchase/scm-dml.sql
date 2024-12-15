UPDATE purchase_return_order AS pro
    JOIN purchase_deliver_order AS pdo
    ON pro.return_biz_no = pdo.purchase_deliver_order_no
SET pro.purchase_child_order_no = pdo.purchase_child_order_no
WHERE pro.return_type in ('RECEIVE_REJECT', 'QC_NOT_PASSED_PART', 'QC_NOT_PASSED_ALL');



UPDATE purchase_deliver_order
SET deliver_order_status = 'RECEIVING'
WHERE purchase_receipt_order_no IN ();