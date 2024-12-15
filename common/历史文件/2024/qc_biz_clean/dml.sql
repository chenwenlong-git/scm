-- 待处理的加工单数据(清洗后)
update qc_order
set qc_source_order_no   = process_order_no,
    qc_source_order_type = 'PROCESS_ORDER_NO'
where del_timestamp = 0
  and process_order_no != ''
  and qc_source_order_no = '';

-- 待处理的返修单数据(清洗后)
update qc_order
set qc_source_order_no   = repair_order_no,
    qc_source_order_type = 'REPAIR_ORDER_NO'
where del_timestamp = 0
  and repair_order_no != ''
  and qc_source_order_no = '';

-- 待处理的收货单数据(清洗后)
update qc_order
set qc_source_order_no   = receive_order_no,
    qc_source_order_type = 'RECEIVE_ORDER_NO'
where del_timestamp = 0
  and receive_order_no != ''
  and qc_source_order_no = '';

-- 待处理的采购单数据(清洗后)
update qc_order
set qc_source_order_no   = purchase_child_order_no,
    qc_source_order_type = 'PURCHASE_CHILD_ORDER_NO'
where del_timestamp = 0
  and purchase_child_order_no != ''
  and qc_source_order_no = '';

