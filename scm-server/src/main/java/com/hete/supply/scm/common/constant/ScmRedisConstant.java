package com.hete.supply.scm.common.constant;

public interface ScmRedisConstant {

    /**
     * 采购子单号创建分布式锁
     */
    String SCM_PURCHASE_CHILD_ORDER_NO_CREATE = "scm:purchase:";


    /**
     * 工序单号
     */
    String SCM_PROCESS_CHILD_NO_CREATE = "scm:process:";

    /**
     * 采购收货数据更新
     */
    String SCM_PURCHASE_RECEIVE = "scm:purchase:receive";

    /**
     * 样品需求拆分子单
     */
    String SCM_SAMPLE_SPLIT = "scm:sample:split";

    /**
     * 采购退货
     */
    String SCM_QC_DEFECT = "scm:qc:defect";

    /**
     * 收货拒收
     */
    String SCM_RECEIVE_REJECT = "scm:receive:reject";

    /**
     * scm原料收货(wms原料出库单签出)
     */
    String SCM_RAW_CREATE = "scm:raw:create";

    /**
     * 加工单创建锁前缀
     */
    String PROCESS_ORDER_CREATE_LOCK_PREFIX = "processOrder:create:";

    /**
     * 加工单完成质检锁前缀
     */
    String PROCESS_COMPLETE_CHECK_LOCK_PREFIX = "processCheck:complete:";

    /**
     * 加工单收货状态锁前缀
     */
    String PROCESS_RECEIVE_STATUS_LOCK_PREFIX = "processReceiveStatus:change:";

    /**
     * scm创建开发需求子单
     */
    String SCM_DEVELOP_CREATE = "scm:develop:create";

    /**
     * 采购单修改
     */
    String PURCHASE_UPDATE_PREFIX = "purchase:update:";

    /**
     * 处理齐备信息返回
     */
    String SCM_DEVELOP_COMPLETE_RETURN = "developCompleteInfoReturn:update";

    String SCM_REFRESH_MISSING_INFO_PREFIX = "scm:refreshMissingInfo:";

    /**
     * 质检次品配置信息更新
     */
    String QC_CONFIG_DEFECT_UPDATE = "qc:config:defect:update";

    /**
     * 次品处理更新质检单
     */
    String QC_DEFECT_DEAL_UPDATE = "qc:defect:deal:update";

    /**
     * 飞书审批单
     */
    String FEISHU_AUDIT_ORDER_PREFIX = "scm:feishuAuditOrder:lock:";

    /**
     * 更新工序提成规则
     */
    String UPDATE_PROCESS_RULE = "update:process:rule";
    /**
     * 提交返修结果
     */
    String SCM_UPDATE_REPAIR_RESULT = "scm:update:repair:process:result:";

    /**
     * 提交返修单质检结果
     */
    String SCM_SUB_REPAIR_QC_RESULT = "scm:submit:repair:qc:result:";

    /**
     * 完成返修
     */
    String SCM_REPAIR_COMPLETE = "scm:repair:complete:";

    /**
     * 计划单库存刷新任务的 Redis 前缀
     */
    String PLAN_REFRESH_STOCK_PREFIX = "plan:refreshStock:";

    /**
     * 计划单创建返修单Redis 前缀
     */
    String SCM_PLAN_REPAIR_CREATE = "scm:plan:repair:create:";

    /**
     * 刷新工序提成规则
     */
    String REFRESH_COMMISSION = "refresh:commission";

    /**
     * 归还原料
     */
    String SCM_REPAIR_RETURN_MATERIAL = "scm:repair:returnMaterial:";

    /**
     * 更新工序组合关系
     */
    String UPDATE_PROCESS_CPD = "update:process:cpd";

    /**
     * 操作扫码记录关联关系
     */
    String OPERATE_PROCESS_ORDER_SCAN_RELATE = "operate:processOrderScanRelate:";

    /**
     * 更新固定成本系数
     */
    String COST_COEFFICIENT_UPDATE = "cost:coefficient:update";

    /**
     * 更新月初加权价
     */
    String UPDATE_MONTH_START_WEIGHTED_AVERAGE = "update:month:start:weighted:average:";

    /**
     * 采购订单创建质检单
     */
    String PURCHASE_CHILD_ORDER_CREATE_QC = "purchase:child:order:create:qc";
    /**
     * 创建结算单
     */
    String CREATE_SETTLE_ORDER = "create:settle:order:";

    /**
     * 结算单流转
     */
    String SETTLE_ORDER_FLOWING = "submit:settle:order:";

    /**
     * 结算单收款账号信息
     */
    String SETTLE_ORDER_ACCOUNT = "settle:order:account:";

    /**
     * 结算单收款账号信息
     */
    String SETTLE_ORDER_PAYMENT_RECORD = "settle:order:payment:record:";

    /**
     * 更新结转单状态
     */
    String SETTLE_CARRYOVER_ORDER_UPDATE_STATUS = "settle:carryover:order:update:status:";

    /**
     * 批量操作结转单
     */
    String BATCH_OPERATION_SETTLE_ORDER = "batch:operation:settle:order:";

    /**
     * 创建对账单
     */
    String CREATE_RECO_ORDER = "create:reco:order:";

    /**
     * 收单对账单
     */
    String COLLECT_RECO_ORDER = "collect:reco:order:";

    /**
     * 创建对账单条目
     */
    String RECO_ORDER_CREATE_ITEM = "reco:order:create:item:";

    /**
     * 删除对账单条目
     */
    String RECO_ORDER_DEL_ITEM = "reco:order:del:item:";

    /**
     * 调价审批回调
     */
    String ADJUST_PRICE_CALLBACK = "adjust:callback";

    /**
     * 业务单据创建质检单
     */
    String BIZ_ORDER_CREATE_QC = "biz:order:create:qc";

    /**
     * 加工单次品处理
     */
    String PROC_DEFECTIVE_HANDLE = "proc:defective:handle:";

    /**
     * 供应商产能
     */
    String SUPPLIER_CAPACITY = "supplier:capacity:";

    /**
     * 供应商停工
     */
    String SUPPLIER_REST = "supplier:rest:";

    /**
     * 查询供应商产能
     */
    String SUPPLIER_CAPACITY_QUERY = "supplier:capQuery:";

    /**
     * 更新商品生产属性信息
     */
    String UPDATE_SKU_PRODUCTION_ATTR = "update:sku:production:attr";

    /**
     * 操作属性信息
     */
    String OPERATE_ATTR = "operate:attr";

    /**
     * 操作属性风险信息
     */
    String OPERATE_ATTR_RISK = "operate:attr:risk";

    /**
     * 生产信息的商品属性操作锁
     */
    String SCM_PRODUCE_DATA_ATTR_UPDATE = "operate:produceDataAttr:update";


    /**
     * 通过加工工序id删除扫码记录
     */
    String REMOVE_SCAN_PROCEDURE_ID = "remove:scan:procedureId:";

    /**
     * 生产资料的商品采购价格
     */
    String SCM_PRODUCE_DATA_PURCHASE_PRICE = "scm:produceData:purchasePrice:";
}
