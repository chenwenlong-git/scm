package com.hete.supply.scm.server.supplier.entity.dto;

import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/6/29 15:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ReceiveOrderRejectMqDto extends BaseMqMessageDto {


    /**
     * 入库类型
     */
    private WmsEnum.ReceiveType receiveType;

    /**
     * 收货单号
     */
    private String receiveOrderNo;

    /**
     * 供应链单据号：采购子单号/样品子单号/加工单号/采购发货单号
     */
    private String scmBizNo;

    /**
     * 供应商代码
     */
    private String supplierCode;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 不良品总数量
     */
    private Integer totalReturnAmount;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作人
     */
    private String operatorName;


    /**
     * 整单拒收/部分拒收
     */
    private List<ReturnGood> returnGoodList;


    /**
     * 拒收列表
     */
    @Data
    public static class ReturnGood {
        /**
         * 发货明细ID
         */
        private Long bizDetailId;

        /**
         * sku
         */
        private String skuCode;

        /**
         * sku批次码
         */
        private String batchCode;

        /**
         * 拒收数量
         */
        private Integer returnAmount;
    }

}
