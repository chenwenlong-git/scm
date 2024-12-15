use
    cn_scm;

UPDATE purchase_raw_receipt_order
SET raw_receipt_biz_type = 'PURCHASE';
UPDATE sample_parent_order
SET is_sample = 'TRUE'
WHERE is_sample = '';
UPDATE sample_child_order AS sco
    INNER JOIN sample_parent_order AS spo
    ON sco.sample_parent_order_no = spo.sample_parent_order_no
SET sco.sample_dev_type = spo.sample_dev_type;




