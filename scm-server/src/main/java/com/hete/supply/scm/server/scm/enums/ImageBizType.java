package com.hete.supply.scm.server.scm.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:46
 */
@Getter
@AllArgsConstructor
public enum ImageBizType implements IRemark {
    //业务枚举:PURCHASE_SETTLE_PAY(采购结算单凭证),SUPPLEMENT_ORDER(补款单凭证),DEDUCT_ORDER(扣款单凭证),PURCHASE_PARENT_ORDER(采购需求单),SAMPLE_PARENT_ORDER(样品需求单),SAMPLE_CTRL_SUCCESS(打样成功),SUPPLIER_LICENSE(供应商企业营业执照),SAMPLE_CHANGE(样品改善图片),OVERSEAS_SHIPPING_MARK(海外仓箱唛pdf),OVERSEAS_BAR_CODE(海外仓条码pdf),OVERSEAS_TRACKING(海外运单pdf),
    PURCHASE_SETTLE_PAY("采购结算单凭证"),
    SUPPLEMENT_ORDER("补款单凭证"),
    DEDUCT_ORDER("扣款单凭证"),
    PURCHASE_PARENT_ORDER("采购需求单"),
    SAMPLE_PARENT_ORDER("样品需求单"),
    SAMPLE_CTRL_SUCCESS("打样成功"),
    SUPPLIER_LICENSE("供应商企业营业执照"),
    SAMPLE_CHANGE("样品改善图片"),
    OVERSEAS_SHIPPING_MARK("海外仓箱唛pdf"),
    OVERSEAS_BAR_CODE("海外仓条码pdf"),
    OVERSEAS_TRACKING("海外运单pdf"),
    PROCESS_DEFECTIVE_RECORD("次品处理记录图片"),
    DEFECT_HANDLING("次品处理图片"),
    SAMPLE_SETTLE_PAY("样品结算单凭证"),
    DEVELOP_CHILD_STYLE("开发子单款式参考图片"),
    DEVELOP_CHILD_COLOR("开发子单颜色参考图片"),
    DEVELOP_PAMPHLET_STYLE("开发版单款式参考图片"),
    DEVELOP_PAMPHLET_COLOR("开发版单颜色参考图片"),
    DEVELOP_SAMPLE("开发样品图片"),
    DEVELOP_SAMPLE_SPECIFICATIONS("开发样品单规格书"),
    DEVELOP_SAMPLE_QUOTATION("开发样品单报价单"),
    DEVELOP_REVIEW_SAMPLE_EFFECT("开发审版效果图"),
    DEVELOP_REVIEW_SAMPLE_DETAIL("开发审版细节图"),
    PRODUCE_DATA_ITEM_EFFECT("生产信息详情效果图"),
    PRODUCE_DATA_ITEM_DETAIL("生产信息详情细节图"),
    DEVELOP_REVIEW_SAMPLE_EXCEPTION("开发审版异常报告异常图片"),
    PRODUCE_DATA_SPU("生产信息SPU图片"),
    PRODUCT_IMAGE("产品规格书图片"),
    SEAL_IMAGE("封样图"),
    DEDUCT_ORDER_FILE("扣款单文件凭证"),
    SUPPLEMENT_ORDER_FILE("补款单文件凭证"),
    SUPPLIER_PAYMENT_PERSONAL("供应商收款账号身份证正反面图片"),
    SUPPLIER_PAYMENT_AUTH("供应商收款账号收款授权书"),
    SUPPLIER_PAYMENT_COMPANY("供应商收款账号企业营业执照"),
    REPAIR_RESULT("返修结果"),
    SUPPLIER_SUBJECT_LICENSE("供应商主体营业执照图片"),
    PREPAYMENT_FILE("预付款附件"),
    PAYMENT_FILE("付款附件"),
    RECO_ORDER_ITEM_SKU_FILE("对账单款项附件"),
    SUPPLIER_SKU_SAMPLE_PIC("供应商商品封样图");

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }
}
