UPDATE purchase_parent_order
SET purchase_parent_order_status = 'IN_PROGRESS'
WHERE purchase_parent_order_status = 'WAIT_CONFIRM'
   OR purchase_parent_order_status = 'WAIT_FOLLOWER_CONFIRM'
   OR purchase_parent_order_status = 'WAIT_RECEIVE_ORDER'
   OR purchase_parent_order_status = 'WAIT_SCHEDULING'
   OR purchase_parent_order_status = 'WAIT_COMMISSIONING'
   OR purchase_parent_order_status = 'COMMISSION'
   OR purchase_parent_order_status = 'PRETREATMENT'
   OR purchase_parent_order_status = 'SEWING'
   OR purchase_parent_order_status = 'AFTER_TREATMENT'
   OR purchase_parent_order_status = 'POST_QC'
   OR purchase_parent_order_status = 'WAIT_DELIVER'
   OR purchase_parent_order_status = 'WAIT_RECEIPT'
   OR purchase_parent_order_status = 'RECEIPTED'
   OR purchase_parent_order_status = 'WAIT_QC'
   OR purchase_parent_order_status = 'WAIT_WAREHOUSING';


UPDATE purchase_parent_order
SET purchase_parent_order_status = 'COMPLETED'
WHERE purchase_parent_order_status = 'WAREHOUSED'
   OR purchase_parent_order_status = 'SETTLE'
   OR purchase_parent_order_status = 'RETURN'
   OR purchase_parent_order_status = 'DELETE'
   OR purchase_parent_order_status = 'FINISH';

UPDATE purchase_child_order
SET expected_on_shelves_date = deliver_date;

UPDATE purchase_child_order
SET is_overdue = 'FALSE';


