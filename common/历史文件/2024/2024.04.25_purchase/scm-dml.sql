update produce_data
set raw_manage = 'TRUE'
where raw_manage = '';

update purchase_child_order_raw as pcor
    INNER JOIN purchase_child_order as pco
    on pcor.purchase_child_order_no = pco.purchase_child_order_no
set pcor.raw_warehouse_code = pco.raw_warehouse_code,
    pcor.raw_warehouse_name = pco.raw_warehouse_name
WHERE (pcor.purchase_raw_biz_type = 'DEMAND'
    OR pcor.purchase_raw_biz_type = 'ACTUAL_DELIVER')
  AND pcor.del_timestamp = 0
  AND pco.del_timestamp = 0;


-- 只更新部分(归还过原料的)单据
UPDATE purchase_child_order_raw pcor1
    JOIN (SELECT purchase_child_order_no, sku, delivery_cnt
          FROM purchase_child_order_raw
          WHERE purchase_raw_biz_type = 'DEMAND') pcor2
    ON pcor1.purchase_child_order_no = pcor2.purchase_child_order_no
        AND pcor1.sku = pcor2.sku
SET pcor1.delivery_cnt = pcor2.delivery_cnt
WHERE pcor1.purchase_raw_biz_type = 'ACTUAL_DELIVER'
  AND pcor1.purchase_child_order_no IN ('');

-- 查找处于接单后，投产之前的单
SELECT pco.*
FROM purchase_child_order AS pco
where pco.purchase_order_status in ('WAIT_SCHEDULING', 'WAIT_COMMISSIONING')
  AND pco.purchase_biz_type = 'PROCESS'
  AND pco.del_timestamp = 0;

-- 根据单号更新出库数等于收货数
update purchase_child_order_raw AS pcor
set pcor.delivery_cnt = pcor.receipt_cnt
where purchase_child_order_no in ('')
  AND purchase_raw_biz_type = 'ACTUAL_DELIVER'
  AND pcor.del_timestamp = 0;