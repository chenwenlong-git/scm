package com.hete.supply.scm.server.scm.supplier.entity.dto;

import com.hete.supply.scm.server.scm.supplier.enums.SupplierPaymentAccountType;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierPaymentCurrencyType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/12/6 11:27
 */
@Data
@NoArgsConstructor
public class SupplierPaymentAccountEditDto extends SupplierPaymentAccountIdAndVersionDto {

    @NotBlank(message = "供应商代码不能为空")
    @Length(max = 32, message = "供应商代码字符长度不能超过 32 位")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotBlank(message = "账号不能为空")
    @Length(max = 100, message = "账号字符长度不能超过 100 位")
    @Pattern(regexp = "^[a-zA-Z0-9- /]*$", message = "账号只能包含数字和大小写字母或-和/或空格")
    @ApiModelProperty(value = "账号")
    private String account;

    @NotNull(message = "账号类型不能为空")
    @ApiModelProperty(value = "账号类型")
    private SupplierPaymentAccountType supplierPaymentAccountType;

    @NotNull(message = "货币类型不能为空")
    @ApiModelProperty(value = "货币类型")
    private SupplierPaymentCurrencyType supplierPaymentCurrencyType;

    @ApiModelProperty(value = "Swift code")
    private String swiftCode;

    @NotBlank(message = "银行不能为空")
    @Length(max = 100, message = "银行字符长度不能超过 100 位")
    @ApiModelProperty(value = "银行")
    private String bankName;

    @NotBlank(message = "银行支行不能为空")
    @Length(max = 255, message = "银行支行字符长度不能超过 255 位")
    @ApiModelProperty(value = "银行支行")
    private String bankSubbranchName;

    @NotBlank(message = "银行所在地区省份/州不能为空")
    @Length(max = 32, message = "银行所在地区省份/州字符长度不能超过 32 位")
    @ApiModelProperty(value = "银行所在地区 省份/州")
    private String bankProvince;

    @NotBlank(message = "银行所在地区城市不能为空")
    @Length(max = 32, message = "银行所在地区城市字符长度不能超过 32 位")
    @ApiModelProperty(value = "银行所在地区 城市")
    private String bankCity;

    @NotBlank(message = "银行所在区不能为空")
    @Length(max = 32, message = "银行所在区字符长度不能超过 32 位")
    @ApiModelProperty(value = "银行所在区")
    private String bankArea;

    @NotBlank(message = "户名不能为空")
    @Length(max = 150, message = "户名字符长度不能超过 150 位")
    @ApiModelProperty(value = "户名")
    private String accountUsername;

    @ApiModelProperty(value = "备注")
    @Length(max = 255, message = "备注字符长度不能超过 255 位")
    private String remarks;

    @NotBlank(message = "主体信息不能为空")
    @ApiModelProperty(value = "主体信息")
    @Length(max = 255, message = "主体长度不能超过 255 位")
    private String subject;

    @ApiModelProperty(value = "企业营业执照")
    private List<String> companyFileCodeList;

    @ApiModelProperty(value = "身份证照片正反面")
    private List<String> personalFileCodeList;

    @ApiModelProperty(value = "收款授权书")
    private List<String> authFileCodeList;


}
