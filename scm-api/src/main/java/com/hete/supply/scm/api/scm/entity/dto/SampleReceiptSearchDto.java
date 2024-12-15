package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.SampleReceiptOrderStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/2 22:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SampleReceiptSearchDto extends ComPageDto {
    @ApiModelProperty(value = "样品采购母单号")
    private String sampleParentOrderNo;


    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "收货人")
    private String receiptUser;

    @ApiModelProperty(value = "收货人")
    private String receiptUsername;

    @ApiModelProperty(value = "选样人")
    private String sampleUser;

    @ApiModelProperty(value = "选样人")
    private String sampleUsername;

    @ApiModelProperty(value = "供应商代码")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "供应商名称")
    private List<String> supplierNameList;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTimeStart;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTimeEnd;


    @ApiModelProperty(value = "选样时间")
    private LocalDateTime sampleTime;

    @ApiModelProperty(value = "支付完成时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "样品收货单状态")
    private List<SampleReceiptOrderStatus> receiptOrderStatusList;

    @ApiModelProperty(value = "样品收货单号")
    private String sampleReceiptOrderNo;

    @ApiModelProperty(value = "物流")
    private String logistics;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "样品发货单号")
    private String sampleDeliverOrderNo;

}
