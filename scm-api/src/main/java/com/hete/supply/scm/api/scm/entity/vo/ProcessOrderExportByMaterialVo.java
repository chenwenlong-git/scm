package com.hete.supply.scm.api.scm.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.scm.api.scm.entity.enums.IsReceiveMaterial;
import com.hete.supply.scm.api.scm.entity.enums.OverPlan;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: RockyHuas
 * @date: 2022/11/23 17:07
 */
@Data
public class ProcessOrderExportByMaterialVo {

    /**
     * 加工单号
     */
    private String processOrderNo;

    /**
     * 加工单状态
     */
    private ProcessOrderStatus processOrderStatus;


    /**
     * 类型
     */
    private ProcessOrderType processOrderType;


    /**
     * 仓库名称
     */
    private String warehouseName;


    /**
     * spu
     */
    private String spu;


    /**
     * 所属平台
     */
    private String platform;

    /**
     * 业务约定日期
     */
    private LocalDateTime deliverDate;

    /**
     * 关联的销售订单
     */
    private String orderNo;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 出库单号
     */
    private String deliveryNo;

    /**
     * 原料sku
     */
    private String sku;

    /**
     * 原料sku名称
     */
    private String materialSkuName;

    /**
     * 出库数量
     */
    private Integer deliveryNum;

    /**
     * 出库时间
     */
    private LocalDateTime deliveryTime;

    /**
     * 接货人
     */
    private String receiptUsername;


    /**
     * 接货时间
     */
    private LocalDateTime receiptTime;

    /**
     * 接货数量
     */
    private Integer receiptNum;

    /**
     * 加工 sku
     */
    private String processSku;

    /**
     * 加工 sku名称
     */
    private String processSkuName;

    /**
     * 加工数量
     */
    private Integer processNum;

    /**
     * 送检数量
     */
    private Integer checkingNum;

    /**
     * 送检时间
     */
    private LocalDateTime checkingTime;

    /**
     * 完工收货数量
     */
    private Integer receiveAmount;

    /**
     * 完工收货时间
     */
    private LocalDateTime finishReceiveTime;

    /**
     * 上架数量
     */
    private Integer onShelvesAmount;

    /**
     * 上架时间
     */
    private LocalDateTime finishOnShelfTime;

    /**
     * 收货单号
     */
    private String receiptOrderNo;

    /**
     * 缺失信息备注信息
     */
    private String missingInformationRemarks;

    /**
     * 缺失信息枚举
     */
    @JsonIgnore
    private String missingInformationEnums;

    /**
     * 是否超额枚举
     */
    @JsonIgnore
    private OverPlan overPlanEnum;

    /**
     * 是否超額备注
     */
    private String overPlanRemark;

    /**
     * 是否回料枚举
     */
    @JsonIgnore
    private IsReceiveMaterial isReceiveMaterialEnum;

    /**
     * 是否回料备注
     */
    private String isReceiveMaterialRemark;

    /**
     * 出库仓库仓名称
     */
    private String deliveryWarehouseName;

    /**
     * 原料下单数
     */
    private Integer materialNum;
}
