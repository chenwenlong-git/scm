-- 以下dml有强烈的顺序要求

-- 1. 是否回料字段初始化
update process_order
set is_receive_material='TRUE'
where process_order_no = 'JG2309063826';
update process_order
set is_receive_material='TRUE'
where process_order_status in ('STORED', 'DELETED', 'WAIT_MOVING');

-- 2.回料时间字段初始化
UPDATE process_order
SET receive_material_time =
        CASE
            WHEN produced_time <> '1970-01-01 00:00:00' THEN produced_time
            ELSE (SELECT ifnull(MAX(receipt_time), '1970-01-01 00:00:00')
                  FROM process_material_receipt
                  WHERE process_material_receipt.process_order_no = process_order.process_order_no)
            END
where del_timestamp = 0
  and is_receive_material = 'TRUE';




