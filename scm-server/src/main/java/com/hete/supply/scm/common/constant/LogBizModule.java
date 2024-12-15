package com.hete.supply.scm.common.constant;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: RockyHuas
 * @date: 2022/12/9 16:23
 */
@AllArgsConstructor
@Getter
public enum LogBizModule implements IRemark {

    /**
     * 加工单
     */
    PRO("加工单"),

    /**
     * 加工单状态
     */
    PROSTATUS("加工单状态"),

    /**
     * 原料收货单状态
     */
    PMRSTATUS("原料收货单状态"),

    /**
     * 采购结算单状态
     */
    PURCHASE_SETTLE_STATUS("采购结算单状态"),

    /**
     * 加工结算单状态
     */
    PROCESS_SETTLE_STATUS("加工结算单状态"),

    /**
     * 补款单状态
     */
    SUPSTATUS("补款单状态"),


    /**
     * 扣款单状态
     */
    DEDUCTSTATUS("扣款单状态"),

    PURCHASE_PARENT_SIMPLE("采购母单列表"),

    PURCHASE_CHILD_SIMPLE("采购子单列表"),

    SAMPLE_PARENT_SIMPLE("样品采购母单列表"),

    SAMPLE_CHILD_SIMPLE("样品采购子单列表"),

    SAMPLE_RECEIPT_SIMPLE("样品收货列表"),

    SUPPLIER_RAW_RECEIPT_SIMPLE("供应商原料收货列表"),

    SUPPLIER_PURCHASE_RETURN_SIMPLE("供应商采购退货列表"),

    PURCHASE_RETURN_SIMPLE("供应链采购退货列表"),

    SUPPLIER_PURCHASE_DELIVER_SIMPLE("供应商采购发货列表"),

    SUPPLIER_SAMPLE_RETURN_SIMPLE("供应商样品退货列表"),

    SUPPLIER_SAMPLE_DELIVER_SIMPLE("供应商样品发货列表"),

    PURCHASE_PARENT_VERSION("采购母单版本管理"),

    PURCHASE_CHILD_VERSION("采购子单版本管理"),

    SAMPLE_PARENT_VERSION("样品母单版本管理"),

    SAMPLE_CHILD_VERSION("样品子单版本管理"),

    SM_SIMPLE("辅料"),

    /**
     * 供应商的状态
     */
    SUPPLIER_STATUS("供应商状态"),

    SHIPPING_MARK("箱唛详情"),

    /**
     * 样品结算单状态
     */
    SAMPLE_SETTLE_STATUS("样品结算单状态"),

    /**
     * 开发子单状态
     */
    DEVELOP_CHILD_ORDER_STATUS("开发子单状态"),

    SUPPLIER_PAYMENT_ACCOUNT_STATUS("供应商收款账号状态"),

    QC_ORDER_STATUS("质检单状态"),

    PREPAYMENT_LIST_SIMPLE("预付款列表"),
    FINANCE_RECO_ORDER_STATUS("对账单"),
    FINANCE_SETTLE_ORDER_STATUS("结算单");


    /**
     * 描述
     */
    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }
}
