-- 1. 执行SQL，导出EXCEL,保存数据（数据备份）

select process_order_no, receipt_time
from process_order
where DATE_FORMAT(receive_material_time, '%Y-%m-%d %H:%i:%s') = DATE_FORMAT(receipt_time, '%Y-%m-%d %H:%i:%s')
  and del_timestamp = 0
  and year(receive_material_time) > 1970;

-- 2. 修复异常数据（步骤1查询的加工单号）
update process_order
set receipt_time='1970-01-01 00:00:00'
where del_timestamp = 0
  and process_order_no in ('');