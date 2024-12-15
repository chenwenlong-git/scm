package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/12/18 15:49
 */
@Data
@NoArgsConstructor
public class SampleReturnExportVo {
    @ApiModelProperty(value = "样品退货单号")
    private String sampleReturnOrderNo;

    @ApiModelProperty(value = "退货单状态")
    private String returnOrderStatus;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "退货数")
    private Integer returnCnt;

    @ApiModelProperty(value = "收货数")
    private Integer receiptCnt;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "收货人名称")
    private String receiptUsername;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;
}
