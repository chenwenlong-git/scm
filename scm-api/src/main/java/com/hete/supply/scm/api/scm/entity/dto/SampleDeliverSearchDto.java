package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.SampleDeliverOrderStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/14 11:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SampleDeliverSearchDto extends ComPageDto {
    @ApiModelProperty(value = "样品采购母单号")
    private String sampleParentOrderNo;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "样品发货单号")
    private String sampleDeliverOrderNo;

    @ApiModelProperty(value = "发货人名称")
    private String deliverUsername;

    @ApiModelProperty(value = "发货单状态")
    private List<SampleDeliverOrderStatus> sampleDeliverOrderStatusList;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTimeStart;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTimeEnd;

    @ApiModelProperty(value = "物流")
    private String logistics;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCode;

    @ApiModelProperty(value = "供应商代码")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "供应商名称")
    private List<String> supplierNameList;

    @ApiModelProperty(value = "样品发货单号批量")
    private List<String> sampleDeliverOrderNoList;

}
