update purchase_raw_receipt_order
set del_timestamp = 1
where purchase_raw_receipt_order_no in
      ('YLSH0501065357', 'YLSH1105045133', 'YLSH5400012429', 'YLSH3252528781', 'YLSH7547496077', 'YLSH0970827405',
       'YLSH5265794701');

update purchase_raw_receipt_order_item
set del_timestamp = 1
where purchase_raw_receipt_order_no in
      ('YLSH0501065357', 'YLSH1105045133', 'YLSH5400012429', 'YLSH3252528781', 'YLSH7547496077', 'YLSH0970827405',
       'YLSH5265794701');

update sample_child_order_raw
set delivery_cnt = 1
where sku = "T6-55LW-16M-NC-L"
  AND sku_batch_code = "AONN2557"
  AND sample_child_order_no = "YPPO7002236557-01"
  AND sample_raw_biz_type = "ACTUAL_DELIVER";