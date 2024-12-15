ALTER TABLE cn_scm.purchase_child_order
    MODIFY COLUMN timely_delivery_cnt int UNSIGNED NOT NULL DEFAULT 0 COMMENT '准交数';