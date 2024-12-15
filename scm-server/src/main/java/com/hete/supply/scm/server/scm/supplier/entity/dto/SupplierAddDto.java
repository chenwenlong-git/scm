package com.hete.supply.scm.server.scm.supplier.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ReconciliationCycle;
import com.hete.supply.scm.api.scm.entity.enums.SupplierGrade;
import com.hete.supply.scm.api.scm.entity.enums.SupplierType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2022/11/25 14:34
 */
@Data
@NoArgsConstructor
public class SupplierAddDto {

    @NotBlank(message = "供应商代码不能为空")
    @Length(max = 32, message = "供应商代码字符长度不能超过 32 位")
    @Pattern(regexp = "^[a-zA-Z0-9-]*$", message = "供应商代码只允许输入数字、英文及特殊字符-")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotBlank(message = "供应商名称不能为空")
    @Length(max = 32, message = "供应商名称字符长度不能超过 32 位")
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @NotNull(message = "供应商类型不能为空")
    @ApiModelProperty(value = "供应商类型")
    private SupplierType supplierType;


    @ApiModelProperty(value = "供应商等级")
    private SupplierGrade supplierGrade;

    @NotNull(message = "产能不能为空")
    @ApiModelProperty(value = "产能")
    @Max(999999)
    private Integer capacity;

    @NotBlank(message = "开发采购员不能为空")
    @ApiModelProperty(value = "开发采购员")
    private String devUser;

    @NotBlank(message = "跟单采购员不能为空")
    @ApiModelProperty(value = "跟单采购员")
    private String followUser;

    @ApiModelProperty(value = "收货地址国家")
    private String shipToCountry;


    @ApiModelProperty(value = "收货地址省份/州")
    private String shipToProvince;


    @ApiModelProperty(value = "收货地址城市")
    private String shipToCity;


    @ApiModelProperty(value = "收货地址详细地址")
    @Length(max = 255, message = "收货地址详细地址字符长度不能超过 255 位")
    private String shipToAddress;

    @ApiModelProperty(value = "备注")
    @Length(max = 255, message = "供应商名称字符长度不能超过 255 位")
    private String remarks;

    @NotNull(message = "入驻时间不能为空")
    @ApiModelProperty(value = "入驻时间")
    private LocalDateTime joinTime;

    @NotNull(message = "物流时效不能为空")
    @ApiModelProperty(value = "物流时效")
    private Integer logisticsAging;

    @ApiModelProperty(value = "对账周期")
    @NotNull(message = "对账周期不能为空")
    private ReconciliationCycle reconciliationCycle;

    @ApiModelProperty(value = "结算日期")
    @NotBlank(message = "结算日期不能为空")
    @Length(max = 3, message = "结算日期字符长度不能超过 3 位")
    private String settleTime;

    @NotBlank(message = "供应商别称不能为空")
    @Length(max = 32, message = "供应商别称字符长度不能超过 32 位")
    @Pattern(regexp = "^[a-zA-Z0-9-]*$", message = "供应商别称只允许输入数字、英文及特殊字符-")
    @ApiModelProperty(value = "供应商别称")
    private String supplierAlias;

    @ApiModelProperty(value = "收款账号列表")
    @Valid
    private List<SupplierPaymentAccountCreateDto> supplierPaymentAccountList;

    @ApiModelProperty(value = "主体信息列表")
    @Valid
    private List<SupplierSubjectCreateDto> supplierSubjectList;


}
