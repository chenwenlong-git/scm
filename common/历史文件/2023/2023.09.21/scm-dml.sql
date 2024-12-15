UPDATE cn_scm.process_material_back as A
    JOIN cn_wms.receive_order as B
    ON CONCAT(A.message_key, '@#PROCESS_RAW') = B.union_key
SET A.receipt_no = B.receive_order_no
WHERE A.receipt_no = '';