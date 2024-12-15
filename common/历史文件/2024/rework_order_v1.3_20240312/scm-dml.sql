UPDATE repair_order_item roi
SET roi.delivery_num = (SELECT SUM(ror.qc_pass_quantity)
                        FROM repair_order_result ror
                        WHERE ror.repair_order_item_id = roi.repair_order_item_id
                          and ror.del_timestamp = 0)
WHERE roi.repair_order_item_id IN (SELECT ror.repair_order_item_id
                                   FROM repair_order_result ror
                                   where ror.del_timestamp = 0);