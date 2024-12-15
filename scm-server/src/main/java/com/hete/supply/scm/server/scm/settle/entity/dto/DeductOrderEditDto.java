package com.hete.supply.scm.server.scm.settle.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.DeductType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:34
 */
@Data
@NoArgsConstructor
public class DeductOrderEditDto {

    @NotNull(message = "扣款单ID不能为空")
    @ApiModelProperty(value = "扣款单ID")
    private Long deductOrderId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;

    @NotNull(message = "扣款类型不能为空")
    @ApiModelProperty(value = "扣款类型")
    private DeductType deductType;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "扣款员工")
    private String deductUser;

    @ApiModelProperty(value = "扣款员工名称")
    private String deductUsername;

    @ApiModelProperty(value = "约定结算时间")
    private LocalDateTime aboutSettleTime;

    @ApiModelProperty(value = "支付图片凭证")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "文件凭证")
    private List<String> documentCodeList;


    @ApiModelProperty(value = "价差扣款明细列表")
    private List<DeductOrderPurchaseDto> deductOrderPurchaseList;


    @ApiModelProperty(value = "加工扣款明细列表")
    private List<DeductOrderProcessDto> deductOrderProcessList;


    @ApiModelProperty(value = "品质扣款明细列表")
    private List<DeductOrderQualityDto> deductOrderQualityList;


    @ApiModelProperty(value = "其他明细列表")
    private List<DeductOrderOtherDto> deductOrderOtherList;


    @ApiModelProperty(value = "预付款明细列表")
    private List<DeductOrderPayDto> deductOrderPayList;

    @ApiModelProperty(value = "次品退供明细列表")
    private List<DeductOrderDefectiveEditDto> deductOrderDefectiveList;

}
