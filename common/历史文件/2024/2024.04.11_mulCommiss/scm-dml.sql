UPDATE purchase_child_order
SET split_type = 'FOLLOW_SPLIT'
WHERE split_type = ''
  AND source_purchase_child_order_no != '';

UPDATE purchase_child_order
SET split_type = 'GOODS_SPLIT'
WHERE split_type = ''
  AND source_purchase_child_order_no = '';



UPDATE supplier_inventory_record
SET supplier_inventory_record_status='EFFECTIVE'