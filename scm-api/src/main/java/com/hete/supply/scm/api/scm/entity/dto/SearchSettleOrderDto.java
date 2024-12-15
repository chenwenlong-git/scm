package com.hete.supply.scm.api.scm.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.scm.api.scm.entity.enums.FinanceSettleOrderStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/5/24.
 */
@Data
@ApiModel(description = "分页搜索结算单请求数据传输对象")
public class SearchSettleOrderDto extends ComPageDto {

    @JsonIgnore
    @ApiModelProperty(value = "当前审批人")
    private String ctrlUser;
    @JsonIgnore
    @ApiModelProperty(value = "当前用户供应商编码列表")
    private List<String> authSupplierCodes;

    @ApiModelProperty(value = "供应商编码", notes = "页面搜索的code入参字段")
    private String supplierCode;
    @ApiModelProperty(value = "结算单号")
    private String settleOrderNo;

    @ApiModelProperty(value = "对账单号")
    private String recoOrderNo;
    @ApiModelProperty(value = "结算单号列表")
    private List<String> settleOrderNos;

    @ApiModelProperty(value = "结算单状态列表")
    private List<FinanceSettleOrderStatus> settleOrderStatusList;

    @ApiModelProperty(value = "供应商编码列表", notes = "获取供应商列表入参字段")
    private List<String> supplierCodes;

    @JsonIgnore
    @ApiModelProperty(value = "可结转金额起始值")
    private BigDecimal availableCarryoverAmountBegin;

    @JsonIgnore
    @ApiModelProperty(value = "可结转金额结束值")
    private BigDecimal availableCarryoverAmountEnd;

    @JsonIgnore
    @ApiModelProperty(value = "是否存在结转单")
    private Boolean existCarryoverOrder;

    @ApiModelProperty(value = "结算单创建开始时间")
    private LocalDateTime settleOrderCreateBeginTime;

    @ApiModelProperty(value = "结算单创建结束时间")
    private LocalDateTime settleOrderCreateEndTime;
}
