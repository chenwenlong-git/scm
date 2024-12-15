package com.hete.supply.scm.server.scm.process.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: RockyHuas
 * @date: 2023/01/13 13:50
 */
@Data
public class ProcessOrderScanStatNumVo {

    /**
     * 本月提成
     */
    private BigDecimal totalProcessCommission = BigDecimal.ZERO;

    /**
     * 本月总正品数
     */
    private Integer totalQualityGoodsCnt = 0;


    /**
     * 今日提成
     */
    private BigDecimal todayProcessCommission = BigDecimal.ZERO;

    /**
     * 今日加工产品
     */
    private Integer todayQualityGoodsCnt = 0;


    /**
     * 上月提成
     */
    private BigDecimal lastMonthProcessCommission = BigDecimal.ZERO;

}
