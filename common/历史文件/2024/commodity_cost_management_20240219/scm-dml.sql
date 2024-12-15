update purchase_parent_order
set purchase_demand_type = 'NORMAL';

insert into cost_coefficients(cost_coefficients_id, effective_time, coefficient, create_time, create_user,
                              create_username, update_time, update_user, update_username, del_timestamp, version)
values (1762418604646645761, '2024-01-31 16:00:00', 1.5, current_timestamp, 'SYSTEM', 'System', CURRENT_TIME, 'SYSTEM',
        'System', 0, 1);


UPDATE cn_scm.qc_order qc
    INNER JOIN cn_wms.receive_order ro ON qc.receive_order_no = ro.receive_order_no
SET qc.qc_origin_property = 'NORMAL',
    qc.qc_origin          = ro.receive_type
WHERE ro.receive_type IS NOT NULL
  AND ro.receive_type IN
      ("TRANSFER", "PROCESS_MATERIAL", "SAMPLE", "SALE_RETURN", "PROFIT", "RETURN", "DEFECTIVE_PROCESS_PRODUCT",
       "OTHER", "CHANGE_GOODS", "INSIDE_CHECK", "FAST_SALE", "PREPARE_ORDER")
  AND qc.del_timestamp = 0
  AND qc.receive_order_no != "";


UPDATE qc_order AS qo
    INNER JOIN qc_receive_order AS qro
    ON qro.qc_order_no = qo.qc_order_no
    INNER JOIN purchase_deliver_order AS pdo
    ON qro.scm_biz_no = pdo.purchase_deliver_order_no
    INNER JOIN purchase_child_order AS pco
    ON pco.purchase_child_order_no = pdo.purchase_child_order_no
SET qo.qc_origin = CASE
                       WHEN pco.purchase_biz_type = 'PRODUCT' THEN 'PRODUCT'
                       WHEN pco.purchase_biz_type = 'PROCESS' THEN 'PROCESS'
                       ELSE qo.qc_origin
    END;

UPDATE qc_order AS qo
    INNER JOIN qc_receive_order AS qro
    ON qro.qc_order_no = qo.qc_order_no
    INNER JOIN purchase_deliver_order AS pdo
    ON qro.scm_biz_no = pdo.purchase_deliver_order_no
    INNER JOIN purchase_child_order AS pco
    ON pco.purchase_child_order_no = pdo.purchase_child_order_no
SET qo.qc_origin_property = CASE
                                WHEN pco.purchase_order_type = 'FIRST_ORDER' THEN 'FIRST_ORDER'
                                WHEN pco.purchase_order_type = 'PRENATAL' THEN 'PRENATAL'
                                WHEN pco.purchase_order_type = 'NORMAL' THEN 'NORMAL'
                                WHEN pco.purchase_order_type = 'WH' THEN 'WH'
                                ELSE qo.qc_origin_property
    END;

UPDATE qc_order AS qo
    INNER JOIN qc_receive_order AS qro
    ON qro.qc_order_no = qo.qc_order_no
    INNER JOIN purchase_child_order AS pco
    ON pco.purchase_child_order_no = qro.scm_biz_no
SET qo.qc_origin = CASE
                       WHEN pco.purchase_biz_type = 'PRODUCT' THEN 'PRODUCT'
                       WHEN pco.purchase_biz_type = 'PROCESS' THEN 'PROCESS'
                       ELSE qo.qc_origin
    END;

UPDATE qc_order AS qo
    INNER JOIN qc_receive_order AS qro
    ON qro.qc_order_no = qo.qc_order_no
    INNER JOIN purchase_child_order AS pco
    ON pco.purchase_child_order_no = qro.scm_biz_no
SET qo.qc_origin_property = CASE
                                WHEN pco.purchase_order_type = 'FIRST_ORDER' THEN 'FIRST_ORDER'
                                WHEN pco.purchase_order_type = 'PRENATAL' THEN 'PRENATAL'
                                WHEN pco.purchase_order_type = 'NORMAL' THEN 'NORMAL'
                                WHEN pco.purchase_order_type = 'WH' THEN 'WH'
                                ELSE qo.qc_origin_property
    END;