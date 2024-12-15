package com.hete.supply.scm.server.supplier.settle.entity.dto;

import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 质检不良品推送scm mq参数
 * </p>
 *
 * @author zhoudonglin
 * @since 2023-06-25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DefectLogMqDto extends BaseMqMessageDto {

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
     * 入库类型
     */
    private WmsEnum.ReceiveType receiveType;

    /**
     * 退货类型：收货拒收、质检不合格、其他
     */
    private WmsEnum.ReturnType returnType;

    /**
     * 质检信息
     */
    private QcOrderDto qcOrderDto;

    /**
     * 质检单列表
     */
    @Data
    public static class QcOrderDetail {

        /**
         * 收货明细id或质检明细id，此处是质检明细id
         */
        private Long bizDetailId;

        /**
         * 批次码
         */
        private String batchCode;

        /**
         * 赫特sku
         */
        private String skuCode;

        /**
         * 不通过数量
         */
        private Integer notPassAmount;


        /**
         * 质检不合格原因
         */
        private String qcNotPassedReason;

        /**
         * 备注
         */
        private String remark;

        /**
         * 图片文件ID列表
         */
        private List<String> pictureUrlList;

    }


    @Data
    public static class QcOrderDto {
        /**
         * 质检单号
         */
        private String qcOrderNo;

        /**
         * 质检状态
         */
        private WmsEnum.QcState qcState;

        /**
         * 质检数量
         */
        private Integer qcAmount;
        /**
         * 质检合格
         */
        private Integer passAmount;

        /**
         * 不通过数量
         */
        private Integer notPassAmount;
        /**
         * 创建时间
         */
        private LocalDateTime createTime;

        /**
         * 质检完成时间
         */
        private LocalDateTime taskFinishTime;

        /**
         * 质检人
         */
        private String operator;

        /**
         * 质检人
         */
        private String operatorName;

        /**
         * 审核人名称
         */
        private String auditor;

        /**
         * 审核人名称
         */
        private String auditorName;

        /**
         * 质检不良品
         */
        private List<QcOrderDetail> qcOrderDetailList;
    }
}
