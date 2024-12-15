package com.hete.supply.scm.server.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/2/14 15:50
 */
@Data
@NoArgsConstructor
public class OverseasWarehouseMsgDto {
    @NotBlank(message = "海外仓箱唛号不能为空")
    @ApiModelProperty(value = "海外仓箱唛号")
    private String overseasShippingMarkNo;


    @NotEmpty(message = "海外仓箱唛PDF不能为空")
    @ApiModelProperty(value = "海外仓箱唛PDF")
    private List<String> overseasShippingFileCode;

    @NotEmpty(message = "海外仓条码PDF不能为空")
    @ApiModelProperty(value = "海外仓条码PDF")
    private List<String> overseasBarCodeFileCode;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "运单号PDF")
    private List<String> trackingNoFileCode;

    @NotBlank(message = "采购子单单号不能为空")
    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;


    @NotEmpty(message = "海外仓条码明细信息不能为空")
    @ApiModelProperty(value = "海外仓条码明细信息")
    @Valid
    private List<OverseasWarehouseMsgItemDto> overseasWarehouseMsgItemList;
}
