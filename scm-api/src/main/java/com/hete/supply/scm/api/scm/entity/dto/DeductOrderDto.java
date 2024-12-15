package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.DeductStatus;
import com.hete.supply.scm.api.scm.entity.enums.DeductType;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DeductOrderDto extends ComPageDto {

    @ApiModelProperty(value = "扣款单ID")
    private Long deductOrderId;

    @ApiModelProperty(value = "扣款单IDS")
    private List<Long> deductOrderIds;

    @ApiModelProperty(value = "采购结算单号")
    private String purchaseSettleOrderNo;

    @ApiModelProperty(value = "扣款单状态")
    private List<DeductStatus> deductStatusList;

    @ApiModelProperty(value = "加工结算单号")
    private String processSettleOrderNo;

    @ApiModelProperty(value = "扣款单号")
    private String deductOrderNo;

    @ApiModelProperty(value = "提交人")
    private String submitUser;

    @ApiModelProperty(value = "提交人名称")
    private String submitUsername;

    @ApiModelProperty(value = "审核人")
    private String examineUser;

    @ApiModelProperty(value = "审核人名称")
    private String examineUsername;

    @ApiModelProperty(value = "确认人")
    private String confirmUser;

    @ApiModelProperty(value = "确认人")
    private String confirmUsername;

    @ApiModelProperty(value = "扣款员工")
    private String deductUser;

    @ApiModelProperty(value = "扣款员工名称")
    private String deductUsername;

    @ApiModelProperty(value = "供应商")
    private String supplierName;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商代码批量")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "提交时间开始")
    private LocalDateTime submitTimeStart;

    @ApiModelProperty(value = "提交时间结束")
    private LocalDateTime submitTimeEnd;

    @ApiModelProperty(value = "确认时间开始")
    private LocalDateTime confirmTimeStart;

    @ApiModelProperty(value = "确认时间结束")
    private LocalDateTime confirmTimeEnd;

    @ApiModelProperty(value = "支付完成时间开始")
    private LocalDateTime payTimeStart;

    @ApiModelProperty(value = "支付完成时间结束")
    private LocalDateTime payTimeEnd;

    @ApiModelProperty(value = "审核时间开始")
    private LocalDateTime examineTimeStart;

    @ApiModelProperty(value = "审核时间结束")
    private LocalDateTime examineTimeEnd;

    @ApiModelProperty(value = "创建时间开始")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "创建时间结束")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "类型")
    private List<DeductType> deductTypeList;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCode;

    @ApiModelProperty(value = "排除扣款单状态")
    private List<DeductStatus> notDeductStatusList;

    @ApiModelProperty(value = "处理人")
    private String handleUser;

    @ApiModelProperty(value = "处理人名称")
    private String handleUsername;

}
