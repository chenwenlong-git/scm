-- 初始化采购单对应的质检单平台
UPDATE qc_detail AS qd
    INNER JOIN qc_order AS qo
    ON qd.qc_order_no = qo.qc_order_no
    INNER JOIN purchase_child_order AS pco
    ON qo.purchase_child_order_no = pco.purchase_child_order_no
SET qd.platform = pco.platform
WHERE qd.del_timestamp = 0
  AND qo.del_timestamp = 0
  AND qo.purchase_child_order_no != ''
  AND pco.del_timestamp = 0;

-- 初始化加工单对应的质检单平台
UPDATE qc_detail AS qd
    INNER JOIN qc_order AS qo
    ON qd.qc_order_no = qo.qc_order_no
    INNER JOIN process_order AS po
    ON qo.process_order_no = po.process_order_no
SET qd.platform = po.platform
WHERE qd.del_timestamp = 0
  AND qo.del_timestamp = 0
  AND qo.process_order_no != ''
  AND po.del_timestamp = 0;

-- 初始化返修单对应的质检单平台
UPDATE qc_detail AS qd
    INNER JOIN qc_order AS qo
    ON qd.qc_order_no = qo.qc_order_no
    INNER JOIN repair_order AS ro
    ON qo.repair_order_no = ro.repair_order_no
SET qd.platform = ro.platform
WHERE qd.del_timestamp = 0
  AND qo.del_timestamp = 0
  AND qo.repair_order_no != ''
  AND ro.del_timestamp = 0;

-- 初始化收货单对应的质检单平台
UPDATE qc_detail AS qd
    INNER JOIN qc_order AS qo
    ON qd.qc_order_no = qo.qc_order_no
    INNER JOIN purchase_deliver_order AS pdo
    ON qo.receive_order_no = pdo.purchase_receipt_order_no
    INNER JOIN purchase_child_order AS pco
    ON pco.purchase_child_order_no = pdo.purchase_child_order_no
SET qd.platform = pco.platform
WHERE qd.del_timestamp = 0
  AND qo.del_timestamp = 0
  AND qo.receive_order_no != ''
  AND pdo.del_timestamp = 0
  AND pco.del_timestamp = 0;

-- 初始化出库质检单的平台（未完结质检单）
UPDATE cn_scm.qc_detail qd
    JOIN cn_scm.qc_order qo
    ON qd.qc_order_no = qo.qc_order_no AND qd.del_timestamp = 0
    JOIN cn_wms.delivery_order de ON qo.qc_source_order_no = de.delivery_order_no AND de.del_timestamp = 0
SET qd.platform=de.plat_code
where qd.del_timestamp = 0
  and qo.qc_state != 'FINISHED'
  and qo.qc_source_order_type = 'OUTBOUND_ORDER_NO'
  and qd.platform = '';


-- 根据收货单初始化质检单对应的平台
UPDATE cn_scm.qc_detail AS qd
    INNER JOIN cn_scm.qc_order AS qo
    ON qd.qc_order_no = qo.qc_order_no
    INNER JOIN cn_wms.receive_detail AS rd ON qo.receive_order_no = rd.receive_order_no
SET qd.platform = rd.plat_code
WHERE qd.platform = ''
  AND qd.del_timestamp = 0
  AND rd.del_timestamp = 0
  AND qo.del_timestamp = 0;

-- 根据wms质检平台初始化
UPDATE cn_scm.qc_detail AS qd1
    INNER JOIN cn_wms.qc_detail AS qd2
    ON qd1.qc_detail_id = qd2.qc_detail_id
SET qd1.platform = qd2.plat_code
WHERE qd1.del_timestamp = 0
  AND qd2.del_timestamp = 0
  AND qd1.platform = '';


-- 初始化库内抽查次品处理记录
UPDATE cn_scm.defect_handling AS dh
    INNER JOIN cn_scm.qc_detail AS qd
    ON dh.qc_order_no = qd.qc_order_no
    INNER JOIN qc_order AS qo
    ON qd.qc_order_no = qo.qc_order_no
    INNER JOIN cn_wms.receive_order AS ro ON ro.receive_order_no = qo.receive_order_no
    INNER JOIN cn_wms.delivery_order AS do1 ON do1.delivery_order_no = ro.delivery_order_no
SET dh.platform = do1.plat_code
WHERE dh.del_timestamp = 0
  AND dh.defect_handling_type = 'INSIDE_DEFECT';

-- 初始化原料次品类型的次品处理记录
UPDATE defect_handling AS dh
    INNER JOIN purchase_child_order AS pco
    ON dh.defect_biz_no = pco.purchase_child_order_no
SET dh.platform = pco.platform
WHERE dh.del_timestamp = 0
  AND pco.del_timestamp = 0
  AND dh.defect_handling_type = 'MATERIAL_DEFECT';

-- 根据质检单初始化次品处理记录
UPDATE defect_handling AS dh
    INNER JOIN qc_detail AS qd
    ON dh.qc_order_no = qd.qc_order_no
SET dh.platform = qd.platform
WHERE dh.del_timestamp = 0
  AND qd.del_timestamp = 0
  AND dh.defect_handling_type IN ('BULK_DEFECT', 'PROCESS_DEFECT', 'INSIDE_DEFECT', 'MATERIAL_DEFECT', 'REPAIR')
  AND dh.platform = '';


-- 根据采购发货单号初始化退货单平台
UPDATE purchase_return_order AS pro
    INNER JOIN purchase_deliver_order AS pdo
    ON pro.return_biz_no = pdo.purchase_deliver_order_no
    INNER JOIN purchase_child_order AS pco ON pco.purchase_child_order_no = pdo.purchase_child_order_no
SET pro.platform = pco.platform
WHERE pro.del_timestamp = 0
  AND pdo.del_timestamp = 0
  AND pco.del_timestamp = 0;

-- 根据采购单号初始化退货单平台
UPDATE purchase_return_order AS pro
    INNER JOIN purchase_child_order AS pco
    ON pro.return_biz_no = pco.purchase_child_order_no
SET pro.platform = pco.platform
WHERE pro.del_timestamp = 0
  AND pco.del_timestamp = 0;

-- 根据加工单号初始化退货单平台
UPDATE purchase_return_order AS pro
    INNER JOIN process_order AS po
    ON pro.return_biz_no = po.process_order_no
SET pro.platform = po.platform
WHERE pro.del_timestamp = 0
  AND po.del_timestamp = 0;

-- 根据质检单号初始化退货单平台
UPDATE purchase_return_order AS pro
    INNER JOIN qc_order AS qo
    ON pro.return_biz_no = qo.qc_order_no
    INNER JOIN qc_detail AS qd
    ON qd.qc_order_no = qo.qc_order_no
SET pro.platform = qd.platform
WHERE pro.del_timestamp = 0
  AND qo.del_timestamp = 0;

-- 初始化拒绝收货退货单的平台
UPDATE cn_scm.purchase_return_order AS pro
    INNER JOIN cn_wms.receive_deliver
        AS rd
    ON pro.return_biz_no = rd.receive_order_no
SET pro.platform = rd.plat_code
WHERE pro.return_type = 'RECEIVE_REJECT'
  AND pro.del_timestamp = 0
  AND rd.del_timestamp = 0
  AND pro.platform = '';

-- 初始化质检部分不合格退货单的平台
UPDATE purchase_return_order AS pro
    INNER JOIN defect_handling AS dh
    ON pro.return_biz_no = dh.defect_handling_no
SET pro.platform = dh.platform
WHERE pro.return_type = 'QC_NOT_PASSED_PART'
  AND pro.del_timestamp = 0
  AND dh.del_timestamp = 0
  AND pro.platform = '';

-- 根据次品处理记录初始化加工次品的退货单
UPDATE purchase_return_order AS pro
    INNER JOIN defect_handling AS dh
    ON pro.return_biz_no = dh.defect_handling_no
SET pro.platform = dh.platform
WHERE pro.return_type = 'PROCESS_DEFECT'
  AND pro.del_timestamp = 0
  AND dh.del_timestamp = 0
  AND pro.platform = '';
-- 根据次品处理记录初始化加工次品的退货单2
UPDATE purchase_return_order AS pro
    INNER JOIN process_defective_record AS pdr
    ON pro.return_biz_no = pdr.process_defective_record_no
    INNER JOIN defect_handling AS dh
    ON dh.defect_biz_no = pdr.process_defective_record_no
SET pro.platform = dh.platform
WHERE pro.return_type = 'PROCESS_DEFECT'
  AND pro.del_timestamp = 0
  AND dh.del_timestamp = 0
  AND pro.platform = '';


-- 初始化库内质检对应的退货单
UPDATE purchase_return_order AS pro
    INNER JOIN qc_detail AS qd
    ON pro.return_biz_no = qd.qc_order_no
SET pro.platform = qd.platform
WHERE pro.return_type = 'INSIDE_CHECK'
  AND pro.del_timestamp = 0
  AND qd.del_timestamp = 0
  AND pro.platform = '';

-- 初始化次品类型对应的退货单
UPDATE purchase_return_order AS pro
    INNER JOIN defect_handling AS dh
    ON pro.return_biz_no = dh.defect_handling_no
SET pro.platform = dh.platform
WHERE pro.return_type = 'INSIDE_CHECK'
  AND pro.del_timestamp = 0
  AND dh.del_timestamp = 0
  AND pro.platform = '';

-- 初始化原料次品对应的退货单
UPDATE purchase_return_order AS pro
    INNER JOIN sample_child_order AS sco
    ON pro.return_biz_no = sco.sample_child_order_no
SET pro.platform = sco.platform
WHERE pro.return_type = 'MATERIAL_DEFECT'
  AND pro.del_timestamp = 0
  AND sco.del_timestamp = 0
  AND pro.platform = '';

-- 初始化原料次品对应的退货单2
UPDATE purchase_return_order AS pro
    INNER JOIN develop_pamphlet_order AS dpo
    ON pro.return_biz_no = dpo.develop_pamphlet_order_no
    INNER JOIN develop_child_order AS dco
    ON dco.develop_child_order_no = dpo.develop_child_order_no
SET pro.platform = dco.platform
WHERE pro.return_type = 'MATERIAL_DEFECT'
  AND pro.del_timestamp = 0
  AND dco.del_timestamp = 0
  AND dpo.del_timestamp = 0
  AND pro.platform = '';
