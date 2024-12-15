package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleSettleStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/1 14:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DevelopSampleSettleSearchDto extends ComPageDto {

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "结算单号")
    private String developSampleSettleOrderNo;

    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku批量")
    private List<String> skuList;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "对账人的用户名")
    private String confirmUsername;

    @ApiModelProperty(value = "供应商确认人的用户名")
    private String examineUsername;

    @ApiModelProperty(value = "财务审核人的用户名")
    private String settleUsername;

    @ApiModelProperty(value = "创建时间开始")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "创建时间结束")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "对账时间开始")
    private LocalDateTime confirmTimeStart;

    @ApiModelProperty(value = "对账时间结束")
    private LocalDateTime confirmTimeEnd;

    @ApiModelProperty(value = "供应商确认时间开始")
    private LocalDateTime examineTimeStart;

    @ApiModelProperty(value = "供应商确认时间结束")
    private LocalDateTime examineTimeEnd;

    @ApiModelProperty(value = "财务审核时间开始")
    private LocalDateTime settleTimeStart;

    @ApiModelProperty(value = "财务审核时间结束")
    private LocalDateTime settleTimeEnd;

    @ApiModelProperty(value = "支付完成时间开始")
    private LocalDateTime payTimeStart;

    @ApiModelProperty(value = "支付完成时间结束")
    private LocalDateTime payTimeEnd;

    @ApiModelProperty(value = "结算单状态")
    private List<DevelopSampleSettleStatus> developSampleSettleStatusList;

    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCodeList;

    @ApiModelProperty(value = "结算单号批量")
    private List<String> developSampleSettleOrderNoList;

    @ApiModelProperty(value = "排除结算单状态")
    private List<DevelopSampleSettleStatus> notDevelopSampleSettleStatusList;

    @ApiModelProperty(value = "供应商代码批量")
    private List<String> supplierCodeList;


}
