package com.hete.supply.scm.common.constant;

import java.math.BigDecimal;

public interface ScmConstant {

    /**
     * 系统编号
     */
    String SYSTEM_CODE = "SCM";

    /**
     * 采购需求单前缀
     */
    String PURCHASE_PARENT_NO_PREFIX = "PO";

    /**
     * 补款单前缀
     */
    String SUPPLEMENT_ORDER_NO_PREFIX = "DAP";

    /**
     * 扣款单前缀
     */
    String DEDUCT_ORDER_NO_PREFIX = "DDI";

    /**
     * 样品需求单前缀
     */
    String SAMPLE_PARENT_NO_PREFIX = "YPPO";

    /**
     * 加工单前缀
     */
    String PROCESS_ORDER_NO_PREFIX = "JG";

    /**
     * 样品发货单前缀
     */
    String SAMPLE_DELIVER_ORDER_NO_PREFIX = "YPFH";

    /**
     * 样品收货单前缀
     */
    String SAMPLE_RECEIPT_ORDER_NO_PREFIX = "YPSH";

    /**
     * 样品退货单前缀
     */
    String SAMPLE_RETURN_ORDER_NO_PREFIX = "YPTH";

    /**
     * 发货单前缀
     */
    String PURCHASE_DELIVER_ORDER_NO_PREFIX = "FH";

    /**
     * 采购原料收货单
     */
    String PURCHASE_RAW_ORDER_NO_PREFIX = "YLSH";

    /**
     * 辅料品类编码前缀
     */
    String SM_CATEGORY_CODE_PREFIX = "F";

    /**
     * 质检单号code前缀
     */
    String QC_CODE = "ZJ";


    /**
     * 加工单版本号
     */
    Integer PROCESS_ORDER_LOG_VERSION = 1;

    /**
     * 加工原料收货版本号
     */
    Integer PROCESS_MATERIAL_RECEIPT_LOG_VERSION = 1;


    /**
     * 补款单版本号
     */
    Integer SUPPLEMENT_ORDER_LOG_VERSION = 1;

    /**
     * 扣款单版本号
     */
    Integer DEDUCT_ORDER_LOG_VERSION = 1;

    /**
     * 采购结算单版本号
     */
    Integer PURCHASE_SETTLE_ORDER_LOG_VERSION = 1;

    /**
     * 加工结算单版本号
     */
    Integer PROCESS_SETTLE_ORDER_LOG_VERSION = 1;

    /**
     * 采购日志版本
     */
    Integer PURCHASE_LOG_VERSION = 1;

    /**
     * 预付款日志版本
     */
    Integer PREPAYMENT_LOG_VERSION = 1;

    /**
     * 对账单日志版本
     */
    Integer RECO_ORDER_LOG_VERSION = 1;

    /**
     * 结算单日志版本
     */
    Integer SETTLE_ORDER_LOG_VERSION = 1;

    /**
     * 辅料日志版本
     */
    Integer SM_LOG_VERSION = 1;

    /**
     * 商品工序 spu同步锁前缀
     */
    String GOODS_PROCESS_SPU_LOCK_PREFIX = "spu:sync:";

    /**
     * 原料收货单创建锁前缀
     */
    String PROCESS_MATERIAL_RECEIPT_LOCK_PREFIX = "processMaterialReceipt:create:";


    /**
     * 加工原料数量修改锁前缀
     */
    String PROCESS_MATERIAL_UPDATE_LOCK_PREFIX = "processMaterialNum:update:";

    /**
     * 供应商版本号
     */
    Integer SUPPLIER_LOG_VERSION = 1;

    /**
     * 加工仓编号
     */
    String PROCESS_WAREHOUSE_CODE = "JG01";


    /**
     * 系统默认用户
     */
    String SYSTEM_USER = "SYSTEM";

    /**
     * 采购单结算编号（注意不会写数据库）
     */
    String PURCHASE_SETTLE_ORDER_CODE = "AA";

    /**
     * 加工单结算编号
     */
    String PROCESS_SETTLE_ORDER_CODE = "AB";

    /**
     * mq同步日志时，第三方系统没有传操作人时的默认值
     */
    String MQ_DEFAULT_USER = "系统用户";

    /**
     * 箱唛号前缀
     */
    String SHIPPING_MARK_PREFIX = "H";

    /**
     * 箱唛号前缀
     */
    String SETTLE_ORDER_MONTH = "yyyy-MM";

    /**
     * 正品前缀
     */
    String QUALITY_GOODS = "QG";

    /**
     * 次品前缀
     */
    String DEFECTIVE_GOODS = "DG";

    /**
     * 分割查询线程池名称
     */
    String THREAD_POOL_NAME = "splitGetFuturePool";

    /**
     * 分割查询大小
     */
    Integer SPLIT_SIZE = 50;

    /**
     * 加工次品记录前缀
     */
    String PROCESS_DEFECTIVE_RECORD_NO_PREFIX = "PDR";

    /**
     * 次品处理单号前缀
     */
    String DEFECT_HANDLING_NO_PREFIX = "C";

    /**
     * 质检单信息变更唯一前缀
     */
    String QC_UPDATE_PREFIX = "QCU";

    /**
     * 退货单号前缀
     */
    String RETURN_ORDER_NO = "TH";

    /**
     * 退货单确认收货时间长度（天）
     */
    Integer RETURN_ORDER_DAYS = 10;

    /**
     * WMS入库收货单的业务标识连接符
     */
    String SCM_BIZ_RECEIVE_TYPE_LINK = "@#";

    /**
     * 供应商时效值
     */
    BigDecimal SUPPLIER_TIMELINESS_VALUE = BigDecimal.valueOf(2);

    /**
     * 加工单排产日志前缀
     */
    String PRODUCTION_SCHEDULE_LOG_PREFIX = "JG-ProductionSchedule-";

    /**
     * 筛选加工单排产日志前缀
     */
    String FILTERED_PRODUCTION_SCHEDULE_LOG_PREFIX = "JG-Filtered-ProductionSchedule-";

    /**
     * 样品结算单版本号
     */
    Integer SAMPLE_SETTLE_ORDER_LOG_VERSION = 1;


    /**
     * 样品单结算编号
     */
    String SAMPLE_SETTLE_ORDER_CODE = "Y";

    /**
     * 开发样品单入库数量默认1
     */
    Integer DEVELOP_SAMPLE_RECEIVE_NUM = 1;

    /**
     * 开发版单编号
     */
    String DEVELOP_PAMPHLET_ORDER_NO = "BD";

    /**
     * 核价单编号
     */
    String DEVELOP_PRICING_ORDER_NO = "HJ";


    /**
     * 开发子单日志版本
     */
    Integer DEVELOP_CHILD_LOG_VERSION = 1;

    /**
     * 开发版单每次重新创建累加1
     */
    Integer DEVELOP_PAMPHLET_TIMES = 1;

    /**
     * 开发样品单编号
     */
    String DEVELOP_SAMPLE_ORDER_NO = "YP";

    /**
     * 开发审版单表编号
     */
    String DEVELOP_REVIEW_ORDER_NO = "SD";

    /**
     * 生产信息优先级的基系数
     */
    Integer PRODUCE_DATA_ITEM_MAX_SORT = 1;

    /**
     * 开发母单一键取消的说明
     */
    String DEVELOP_PARENT_ORDER_CANCEL = "开发母单进行一键取消";


    /**
     * 生产信息优先级的最小基系数
     */
    Integer PRODUCE_DATA_ITEM_MIN_SORT = 0;

    /**
     * 加工部的供应商代码
     */
    String PROCESS_SUPPLIER_CODE = "99";

    /**
     * 重置采购子单可发货数为0
     */
    Integer PURCHASE_CHILD_SHIPPABLE_CNT = 0;


    /**
     * 检验仓库编码与收货类型是否符合提示语
     */
    String VERIFY_WAREHOUSE_PROMPTS = "该仓库不支持该业务操作，请选择其他仓库";

    /**
     * 供应商收款账号日志版本号
     */
    Integer SUPPLIER_PAYMENT_ACCOUNT_LOG_VERSION = 1;

    /**
     * 飞书审批表编号
     */
    String FEISHU_ORDER_NO = "FS";

    /**
     * 调价审批前缀
     */
    String ADJUST_PRICE_APPROVE_NO = "SP";

    /******************飞书审批单供应商收款账号模版内容***********************/

    /**
     * 账号
     */
    String WORKFLOW_PAYMENT_ACCOUNT = "account";
    /**
     * 供应商
     */
    String WORKFLOW_PAYMENT_SUPPLIER_CODE = "supplierCode";
    /**
     * 银行
     */
    String WORKFLOW_PAYMENT_BANK_NAME = "bankName";
    /**
     * 银行支行
     */
    String WORKFLOW_PAYMENT_BANK_SUBBRANCH_NAME = "bankSubbranchName";
    /**
     * 银行所在地区
     */
    String WORKFLOW_PAYMENT_BANK_AREA = "bankArea";
    /**
     * 户名
     */
    String WORKFLOW_PAYMENT_ACCOUNT_USERNAME = "accountUsername";
    /**
     * 账号类型
     */
    String WORKFLOW_PAYMENT_TYPE = "supplierPaymentAccountType";
    /**
     * 货币类型
     */
    String WORKFLOW_PAYMENT_CURRENCY_TYPE = "supplierPaymentCurrencyType";
    /**
     * Swift code
     */
    String WORKFLOW_PAYMENT_SWIFT_CODE = "swiftCode";
    /**
     * 备注
     */
    String WORKFLOW_PAYMENT_REMARKS = "remarks";
    /**
     * 申请人
     */
    String WORKFLOW_PAYMENT_APPLICANT = "applicant";
    /**
     * 申请人时间
     */
    String WORKFLOW_PAYMENT_APPLICATION_TIME = "applicationTime";

    /**
     * 企业营业执照
     */
    String WORKFLOW_PAYMENT_COMPANY_FILE_CODE = "companyFileCodeList";

    /**
     * 身份证照片正反面
     */
    String WORKFLOW_PAYMENT_PERSONAL_FILE_CODE = "personalFileCodeList";

    /**
     * 收款授权书
     */
    String WORKFLOW_PAYMENT_COMPANY_AUTH_FILE_CODE = "authFileCodeList";

    /**
     * 供应商主体
     */
    String SUPPLIER_SUBJECT = "supplierSubject";

    /**
     * 返修单前缀
     */
    String REPAIR_ORDER_NO_PREFIX = "FX";

    String WORKFLOW_TYPE_TEXT = "textarea";
    String WORKFLOW_TYPE_IMAGE = "image";
    String WORKFLOW_TYPE_ATTACHMENT = "attachmentV2";

    /******************飞书审批单供应商收款账号模版内容***********************/


    /**
     * 质检单日志版本号
     */
    Integer QC_ORDER_LOG_VERSION = 1;

    // 返修单创建结果消息键前缀
    String REPAIR_ORDER_CREATE_RESULT = "repairOrderCreateResult";

    // 返修单取消消息键前缀
    String REPAIR_ORDER_CANCEL = "repairOrderCancel";

    // 返修单完成加工消息键前缀
    String REPAIR_ORDER_PROCESS_COMPLETED = "repairOrderProcessCompleted";

    // 返修单完成质检消息键前缀
    String REPAIR_ORDER_QUALITY_INSPECTION_COMPLETED = "repairOrderQualityInspectionCompleted";

    // 返修单入库消息键前缀
    String REPAIR_ORDER_IN_STORAGE = "repairOrderInStorage";

    /**
     * 备货单前缀
     */
    String STOCK_UP_ORDER_NO = "BO";

    /**
     * 供应商仓库前缀
     */
    String SUPPLIER_WAREHOUSE_PREFIX = "C";

    /**
     * 备货仓后缀
     */
    String STOCK_UP_WAREHOUSE_SUFFIX = "01";

    /**
     * 自备仓后缀
     */
    String SELF_PROVIDE_WAREHOUSE_SUFFIX = "02";

    /**
     * 不良仓后缀
     */
    String DEFECTIVE_WAREHOUSE_SUFFIX = "03";

    /**
     * 备货仓后缀
     */
    String STOCK_UP_WAREHOUSE_SUFFIX_NAME = "备货";

    /**
     * 自备仓后缀
     */
    String SELF_PROVIDE_WAREHOUSE_SUFFIX_NAME = "自备";

    /**
     * 不良仓后缀
     */
    String DEFECTIVE_WAREHOUSE_SUFFIX_NAME = "不良";

    /**
     * 计算加工单成本价锁前缀。
     */
    String PROCESSING_ORDER_COST_CALCULATION_PREFIX = "calculateProcessingOrderCost:";

    /**
     * 更新批次码成本价MQ防重key前缀。
     */
    String UPDATE_BATCH_CODE_COST_PREFIX = "updateBatchCodeCost:";

    /**
     * 生产信息的BOM名称后缀
     */
    String PRODUCE_DATA_ITEM_BOM_SUFFIX_NAME = "BOM";

    /**
     * 是否删除生产信息的BOM的标识
     */
    String PRODUCE_DATA_ITEM_BOM_DELETE = "1";

    /**
     * 开发子单中采购需求单sku数量默认值
     */
    Integer PURCHASE_PARENT_ORDER_SKU_CNT = 1;

    /**
     * 预付款单号前缀
     */
    String PREPAYMENT_ORDER_NO_PREFIX = "YF";

    /**
     * 结算单号前缀
     */
    String SETTLEMENT_ORDER_NO_PREFIX = "JS";

    /**
     * 人民币后缀单位
     */
    String RMB_MONEY_UNIT = "CNY-人民币元";

    /**
     * 结算单号前缀
     */
    String FINANCE_RECO_ORDER_NO_PREFIX = "DZ";

    /**
     * 结转单号
     */
    String CARRY_OVER_ORDER_NO_PREFIX = "JZ";

    /**
     * 对账单自定义款项单的编号
     */
    String FINANCE_RECO_ORDER_ITEM_NO_PREFIX = "CT";

    /**
     * 定时任务执行时，系统没有传操作人时的默认值操作用户
     */
    String JOB_DEFAULT_USER_NAME = "定时任务";

    /**
     * wms收货单前缀
     */
    String WMS_RECEIVE_ORDER_NO_PREFIX = "SH";
}
