package com.hete.supply.scm.server.supplier.sample.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ReceiptOrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/3 10:32
 */
@Data
@NoArgsConstructor
public class SampleReturnDetailVo {
    @ApiModelProperty(value = "id")
    private Long sampleReturnOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "样品退货单号")
    private String sampleReturnOrderNo;


    @ApiModelProperty(value = "退货单状态")
    private ReceiptOrderStatus returnOrderStatus;


    @ApiModelProperty(value = "物流")
    private String logistics;


    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "退货时间")
    private LocalDateTime createTime;


    @ApiModelProperty(value = "样品退货单明细列表")
    private List<SampleReturnItem> sampleReturnItemList;

    @Data
    @ApiModel(value = "样品退货单明细")
    public static class SampleReturnItem {
        @ApiModelProperty(value = "id")
        private Long sampleReturnOrderItemId;

        @ApiModelProperty(value = "version")
        private Integer version;

        @ApiModelProperty(value = "spu")
        private String spu;


        @ApiModelProperty(value = "样品采购子单号")
        private String sampleChildOrderNo;


        @ApiModelProperty(value = "收货结果")
        private ReceiptOrderStatus returnOrderStatus;

        @ApiModelProperty(value = "退货数")
        private Integer returnCnt;

        @ApiModelProperty(value = "收货数")
        private Integer receiptCnt;

        @ApiModelProperty(value = "选样时间")
        private LocalDateTime sampleTime;
    }

}
