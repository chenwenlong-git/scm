package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.SupplementStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplementType;
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
public class SupplementOrderDto extends ComPageDto {

    @ApiModelProperty(value = "补款单ID")
    private Long supplementOrderId;

    @ApiModelProperty(value = "补款单IDS")
    private List<Long> supplementOrderIds;

    @ApiModelProperty(value = "采购结算单号")
    private String purchaseSettleOrderNo;

    @ApiModelProperty(value = "补款单状态")
    private List<SupplementStatus> supplementStatusList;

    @ApiModelProperty(value = "加工结算单号")
    private String processSettleOrderNo;

    @ApiModelProperty(value = "补款单号")
    private String supplementOrderNo;

    @ApiModelProperty(value = "提交人")
    private String submitUser;

    @ApiModelProperty(value = "提交人用户名")
    private String submitUsername;

    @ApiModelProperty(value = "审核人")
    private String examineUser;

    @ApiModelProperty(value = "审核人用户名")
    private String examineUsername;

    @ApiModelProperty(value = "确认人")
    private String confirmUser;

    @ApiModelProperty(value = "确认人用户名")
    private String confirmUsername;

    @ApiModelProperty(value = "补款员工")
    private String supplementUser;

    @ApiModelProperty(value = "补款员工用户名")
    private String supplementUsername;

    @ApiModelProperty(value = "供应商名称")
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
    private List<SupplementType> supplementTypeList;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCode;

    @ApiModelProperty(value = "排除补款单状态")
    private List<SupplementStatus> notSupplementStatusList;

    @ApiModelProperty(value = "处理人")
    private String handleUser;

    @ApiModelProperty(value = "处理人名称")
    private String handleUsername;

}
