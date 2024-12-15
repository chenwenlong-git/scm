package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessFirst;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: RockyHuas
 * @date: 2022/11/28 13:50
 */
@Data
public class ProcessOrderScanExportVo {

    /**
     * 主键id
     */
    private Long processOrderScanId;

    /**
     * 扫码创建时间
     */
    private LocalDateTime createTime;

    /**
     * 扫码完成时间
     */
    private LocalDateTime completeTime;


    /**
     * 扫码完成人名称
     */
    private String completeUsername;


    /**
     * 一级工序
     */
    private ProcessFirst processFirst;

    /**
     * 工序
     */
    private String processName;

    /**
     * 工序标签
     */
    private ProcessLabel processLabel;

    /**
     * 工序代码
     */
    private String processCode;

    /**
     * 二级工序
     */
    private String processSecondName;

    /**
     * 正品数量
     */
    private Integer qualityGoodsCnt;


    /**
     * 次品数量
     */
    private Integer defectiveGoodsCnt;

    /**
     * 工序提成（工序基础单价）
     */
    private BigDecimal processCommission;

    /**
     * 总提成
     */
    private BigDecimal totalProcessCommission;

    /**
     * 加工单号
     */
    private String processOrderNo;

    /**
     * 平台
     */
    private String platform;

    /**
     * 下单人
     */
    private String orderUsername;

    /**
     * 接货人
     */
    private String receiptUsername;

    /**
     * 接货数量
     */
    private Integer receiptNum;

    /**
     * 接货时间
     */
    private LocalDateTime receiptTime;

    /**
     * 一级提成总金额
     */
    private BigDecimal firstLevelCommissionAmount;

    /**
     * 二级提成总金额
     */
    private BigDecimal secondLevelCommissionAmount;

}
