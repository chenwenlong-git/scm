-- 待处理的加工质检单数据(清洗前)
select qc_order_id,
       process_order_no,
       qc_source_order_no,
       qc_source_order_type,
       update_user,
       update_username
from qc_order
where del_timestamp = 0
  and process_order_no != ''
  and qc_source_order_no = '';

-- 待处理的返修质检单数据(清洗前)
select qc_order_id,
       repair_order_no,
       qc_source_order_no,
       qc_source_order_type,
       update_user,
       update_username
from qc_order
where del_timestamp = 0
  and repair_order_no != ''
  and qc_source_order_no = '';

-- 待处理的收货质检单数据(清洗前)
select qc_order_id,
       receive_order_no,
       qc_source_order_no,
       qc_source_order_type,
       update_user,
       update_username
from qc_order
where del_timestamp = 0
  and receive_order_no != ''
  and qc_source_order_no = '';

-- 待处理的采购质检单数据(清洗前)
select qc_order_id,
       purchase_child_order_no,
       qc_source_order_no,
       qc_source_order_type,
       update_user,
       update_username
from qc_order
where del_timestamp = 0
  and purchase_child_order_no != ''
  and qc_source_order_no = '';

