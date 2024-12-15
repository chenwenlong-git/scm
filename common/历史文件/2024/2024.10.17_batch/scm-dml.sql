- 初始化新增字段
update `cn_scm`.`defect_handling` AS dh
    INNER JOIN `cn_wms`.`sku_batch` AS sb
    on dh.`defect_handling_no` = sb.`purchase_child_order_no`
set dh.handle_sku            = sb.sku_code,
    dh.handle_sku_batch_code = sb.batch_code
where dh.`del_timestamp` = 0
  and dh.defect_handling_programme = 'EXCHANGE_GOODS';

- 查询无法初始化的次品处理记录
select *
from cn_scm.`defect_handling` as a
         LEFT JOIN `cn_wms`.`sku_batch` as b
                   on a.`defect_handling_no` = b.`purchase_child_order_no`
where a.`del_timestamp` = 0
  and a.defect_handling_programme = 'EXCHANGE_GOODS'
  and b.`batch_code` is null
    - 无法初始化的次品处理单独初始化
update `cn_scm`.`defect_handling` AS dh
set dh.handle_sku            = '',
    dh.handle_sku_batch_code = ''
where dh.`del_timestamp` = 0
  and dh.defect_handling_no = '';
