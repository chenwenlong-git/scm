package com.hete.supply.scm.api.scm.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.scm.api.scm.entity.enums.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: RockyHuas
 * @date: 2022/11/21 17:07
 */
@Data
public class ProcessOrderExportByOrderVo {

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
     * 答交日期:经过重新评估和调整后提出新的约定日期（deliverDate）
     */
    private LocalDateTime promiseDate;

    /**
     * 答交日期是否延期
     */
    private PromiseDateDelayed promiseDateDelayed;

    /**
     * 关联的销售订单
     */
    private String orderNo;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 总加工数
     */
    private Integer totalProcessNum;

    /**
     * 总采购价
     */
    private BigDecimal totalPurchasePrice;

    /**
     * 总正品数量
     */
    private Integer totalQualityGoodsCnt;

    /**
     * 总次品数量
     */
    private Integer totalDefectiveGoodsCnt;

    /**
     * 下单人
     */
    private String createUsername;

    /**
     * 下单时间
     */
    private LocalDateTime createTime;

    /**
     * 投产人
     */
    private String producedUsername;

    /**
     * 投产时间
     */
    private LocalDateTime producedTime;

    /**
     * 质检时间
     */
    private LocalDateTime checkedTime;

    /**
     * 发货时间
     */
    private LocalDateTime deliverTime;

    /**
     * 收货时间
     */
    private LocalDateTime receiptTime;


    /**
     * 入库时间
     */
    private LocalDateTime storedTime;

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
     * SKU
     */
    private String sku;

    /**
     * 下单数
     */
    private Integer processNum;

    /**
     * 排产人姓名
     */
    private String processPlanUserName;

    /**
     * 排产时间
     */
    private LocalDateTime processPlanTime;

    /**
     * 回料时间
     */
    private LocalDateTime receiveMaterialTime;

    /**
     * 是否需要排产计划
     */
    private NeedProcessPlan needProcessPlan;

    /**
     * 是否需要排产计划备注
     */
    private String needProcessPlanRemark;

    /**
     * 进入加工中时间
     */
    private LocalDateTime processingTime;

    /**
     * 后整质检中处理时间
     */
    private LocalDateTime checkingTime;

    /**
     * 加工备注
     */
    private String processOrderNote;
}
