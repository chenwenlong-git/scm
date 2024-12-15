use
    cn_scm;

UPDATE sample_child_order_result
SET sample_result_status = 'WAIT_HANDLE'
WHERE relate_order_no = '';

UPDATE sample_child_order_result
SET sample_result_status = 'HANDLED'
WHERE relate_order_no != '';




