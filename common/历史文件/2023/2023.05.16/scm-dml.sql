use
    cn_scm;

UPDATE sample_child_order
SET sample_produce_label = 'EFFECTIVE'
WHERE sample_order_status IN ('SELECTED', 'SETTLE');




