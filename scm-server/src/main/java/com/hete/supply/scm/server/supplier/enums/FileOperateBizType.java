package com.hete.supply.scm.server.supplier.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2022/11/19 14:24
 */
@Getter
@AllArgsConstructor
public enum FileOperateBizType implements IRemark {
    // 文件导出类型：PURCHASE_RAW_RECEIPT_EXPORT:采购原料收货导出
    PURCHASE_RAW_RECEIPT_EXPORT("purchase_raw_receipt_export", "采购原料收货导出"),
    // 文件导出类型：RETURN_ORDER_EXPORT:退货单导出
    SCM_RETURN_ORDER_EXPORT("scm_return_order_export", "SCM退货单导出"),
    SCM_DEDUCT_ORDER_SKU_EXPORT("scm_deduct_order_sku_export", "SCM扣款单sku导出"),
    SCM_SUPPLEMENT_ORDER_SKU_EXPORT("scm_supplement_order_sku_export", "SCM补款单sku导出"),
    SPM_DEDUCT_ORDER_SKU_EXPORT("spm_deduct_order_sku_export", "SPM扣款单sku导出"),
    SPM_SUPPLEMENT_ORDER_SKU_EXPORT("spm_supplement_order_sku_export", "SPM补款单sku导出"),
    SPM_RETURN_ORDER_EXPORT("spm_return_order_export", "SPM退货单导出"),
    SCM_PURCHASE_PARENT_EXPORT("scm_purchase_parent_export", "SCM采购需求单导出"),
    SCM_PURCHASE_CHILD_EXPORT("scm_purchase_child_export", "SCM采购订单导出"),
    SCM_SAMPLE_SETTLE_ORDER_EXPORT("scm_sample_settle_order_export", "SCM样品结算单导出"),
    SPM_SAMPLE_SETTLE_ORDER_EXPORT("spm_sample_settle_order_export", "SPM样品结算单导出"),
    SCM_DEVELOP_PRICING_ORDER_EXPORT("scm_develop_pricing_order_export", "SCM核价单导出"),
    SCM_DEVELOP_SAMPLE_ORDER_EXPORT("scm_develop_sample_order_export", "SCM开发样品单导出"),
    SPM_DEVELOP_SAMPLE_ORDER_EXPORT("spm_develop_sample_order_export", "SPM开发样品单导出"),
    SCM_DEVELOP_ORDER_EXPORT("scm_develop_order_export", "SCM开发子单导出"),
    SCM_GOODS_PROCESS_EXPORT("scm_goods_process_export", "SCM商品工序导出"),
    SCM_PURCHASE_DELIVER_EXPORT("scm_purchase_deliver_export", "SCM采购发货单导出"),
    SCM_DEV_REVIEW_EXPORT("scm_dev_review_export", "SCM审版单导出"),
    SCM_PURCHASE_PRE_CONFIRM_EXPORT("scm_purchase_pre_confirm", "SCM采购单待确认列表导出"),
    SCM_PROCESS_ORDER_INVENTORY_SHORTAGE_REPORT_EXPORT("scm_process_order_inventory_shortage_report_export", "SCM加工单库存报表导出"),
    SCM_QC_ORDER_EXPORT("scm_qc_export", "SCM质检单导出"),
    SCM_QC_DETAIL_EXPORT("scm_qc_detail_export", "SCM质检单明细导出"),
    SCM_PROCESS_ORDER_SCAN_MONTH_STATISTICS("scm_process_order_scan_month_statistics", "导出月度扫码加工明细统计"),
    SCM_PURCHASE_PARENT_SKU("scm_purchase_parent_sku", "SCM采购母单按sku导出"),
    SCM_REPAIR_ORDER_EXPORT("scm_repair_order_export", "SCM返修单导出"),
    SCM_REPAIR_ORDER_RESULT_EXPORT("scm_repair_order_result_export", "SCM返修单记录导出"),
    SCM_PROCESS_ORDER_SETTLE_DETAIL("scm_process_order_settle_detail", "结算明细导出"),
    SCM_PRODUCE_DATA_PROCESS_EXPORT("scm_produce_data_process_export", "SCM生产资料原料工序导出"),
    SCM_QC_ORDER_CONFIG_EXPORT("scm_qc_order_config_export", "SCM次品原因导出"),
    SCM_STOCK_UP_EXPORT("scm_stock_up_export", "SCM备货单导出"),
    SCM_SUPPLIER_INVENTORY_EXPORT("scm_supplier_inventory_export", "SCM备货单导出"),
    SCM_SUPPLIER_INVENTORY_RECORD_EXPORT("scm_supplier_inventory_record_export", "SCM备货单导出"),
    SPM_STOCK_UP_EXPORT("spm_stock_up_export", "SPM备货单导出"),
    SPM_SUPPLIER_INVENTORY_EXPORT("spm_supplier_inventory_export", "SPM备货单导出"),
    SPM_SUPPLIER_INVENTORY_RECORD_EXPORT("spm_supplier_inventory_record_export", "SPM备货单导出"),
    SCM_STOCK_UP_ITEM_EXPORT("scm_stock_up_item_export", "SCM备货单明细导出"),
    SPM_STOCK_UP_ITEM_EXPORT("spm_stock_up_item_export", "SPM备货单明细导出"),
    IBFS_GOODS_OF_COST_ONE_EXPORT("ibfs_goods_of_cost_one_export", "IBFS商品成本单仓导出"),
    IBFS_GOODS_OF_COST_MANY_EXPORT("ibfs_goods_of_cost_many_export", "IBFS商品成本多仓导出"),
    SCM_DEFECT_HANDING_EXPORT("scm_defect_handing_export", "SCM次品记录列表导出"),
    SCM_FINANCE_RECO_ORDER_EXPORT("scm_finance_reco_order_export", "SCM对账单导出"),
    SCM_FINANCE_RECO_ORDER_ITEM_EXPORT("scm_finance_reco_order_item_export", "SCM对账单详情导出"),
    SCM_FINANCE_PREPAYMENT_ORDER_EXPORT("scm_finance_prepayment_order_export", "SCM对账单导出"),
    SPM_FINANCE_PREPAYMENT_ORDER_EXPORT("spm_finance_prepayment_order_export", "SCM对账单导出"),
    SCM_FINANCE_SETTLE_ORDER_EXPORT("scm_finance_settle_order_export", "SCM结算单导出"),
    SPM_FINANCE_SETTLE_ORDER_EXPORT("spm_finance_settle_order_export", "SPM结算单导出"),
    SCM_FINANCE_SETTLE_ORDER_ITEM_EXPORT("scm_finance_settle_order_item_export", "SCM结算单明细导出"),
    SPM_FINANCE_SETTLE_ORDER_ITEM_EXPORT("spm_finance_settle_order_item_export", "SPM结算单明细导出"),
    SPM_FINANCE_RECO_ORDER_EXPORT("spm_finance_reco_order_export", "SPM对账单导出"),
    SPM_FINANCE_RECO_ORDER_ITEM_EXPORT("spm_finance_reco_order_item_export", "SPM对账单详情导出"),
    SCM_PRODUCE_DATA_ATTR_EXPORT("scm_produce_data_attr_export", "SCM生产资料的生产属性导出"),
    SCM_GOODS_PRICE_EXPORT("scm_goods_price_export", "SCM商品价格导出"),
    SCM_SUPPLIER_REST_EXPORT("scm_supplier_rest_export", "SCM供应商停工时间导出"),
    SCM_SUPPLIER_CAPACITY_EXPORT("scm_supplier_capacity_export", "SCM供应商产能导出"),
    SCM_SKU_PROD_SKU_EXPORT("scm_sku_prod_sku_export", "SCM商品信息sku导出"),
    SCM_SKU_PROD_SKU_COMPARE_EXPORT("scm_sku_prod_sku_compare_export", "SCM商品信息sku对照关系导出"),
    SCM_PURCHASE_SKU_RAW_EXPORT("scm_purchase_sku_raw_export", "SCM采购订单的BOM原料导出"),
    SCM_SKU_PROD_SUPPLIER_PROCESS_EXPORT("scm_sku_prod_supplier_process_export", "SCM商品信息与供应商原料工序信息导出"),
    SCM_PURCHASE_RAW_RECEIPT_EXPORT("scm_purchase_raw_receipt_export", "采购原料收货导出"),

    ;

    private final String code;
    private final String remark;

}
