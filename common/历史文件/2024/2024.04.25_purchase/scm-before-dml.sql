UPDATE purchase_child_order_raw AS pcor1
    INNER JOIN (SELECT pcor2.purchase_child_order_no, pcor2.sku, pcor2.raw_supplier, pcor2.dispense_cnt
                FROM purchase_child_order_raw AS pcor2
                WHERE pcor2.purchase_raw_biz_type = 'DEMAND'
                  AND pcor2.del_timestamp = 0) AS tmp
    ON pcor1.purchase_child_order_no = tmp.purchase_child_order_no
        AND pcor1.sku = tmp.sku
        AND pcor1.raw_supplier = tmp.raw_supplier
SET pcor1.dispense_cnt = tmp.dispense_cnt
WHERE pcor1.purchase_raw_biz_type = 'ACTUAL_DELIVER'
  AND pcor1.del_timestamp = 0;

UPDATE purchase_child_order_raw AS pcor
SET pcor.receipt_cnt = pcor.delivery_cnt
WHERE pcor.del_timestamp = 0
  AND pcor.purchase_raw_biz_type = 'ACTUAL_DELIVER'
  AND pcor.sku_batch_code != '';