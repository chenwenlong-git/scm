package com.hete.supply.scm.server.scm.supplier.entity.dto;

import com.hete.supply.scm.server.scm.supplier.enums.SupplierSubjectType;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/5/15 18:21
 */
@Data
@NoArgsConstructor
public class SupplierSubjectEditDto {

    @ApiModelProperty(value = "主键id")
    private Long supplierSubjectId;

    @ApiModelProperty(value = "版本")
    private int version;

    @ApiModelProperty(value = "供应商代码")
    @NotBlank(message = "供应商代码不能为空")
    @Length(max = 32, message = "供应商代码字符长度不能超过 32 位")
    private String supplierCode;

    @ApiModelProperty(value = "主体类型")
    @NotNull(message = "主体类型不能为空")
    private SupplierSubjectType supplierSubjectType;

    @ApiModelProperty(value = "公司名称")
    @NotBlank(message = "公司名称不能为空")
    @Length(max = 255, message = "公司名称字符长度不能超过 255 位")
    private String subject;

    @ApiModelProperty(value = "法人")
    @NotBlank(message = "法人不能为空")
    @Length(max = 255, message = "法人字符长度不能超过 255 位")
    private String legalPerson;

    @ApiModelProperty(value = "联系人")
    @NotBlank(message = "联系人不能为空")
    @Length(max = 255, message = "联系人字符长度不能超过 255 位")
    private String contactsName;

    @ApiModelProperty(value = "联系电话")
    @NotBlank(message = "联系电话不能为空")
    @Length(max = 11, message = "联系电话字符长度不能超过 11 位")
    private String contactsPhone;

    @ApiModelProperty(value = "注册资金")
    @NotBlank(message = "注册资金不能为空")
    @Length(max = 255, message = "注册资金字符长度不能超过 255 位")
    private String registerMoney;

    @ApiModelProperty(value = "经营范围")
    @NotBlank(message = "经营范围不能为空")
    @Length(max = 255, message = "经营范围字符长度不能超过 255 位")
    private String businessScope;

    @ApiModelProperty(value = "经营地址")
    @NotBlank(message = "经营地址不能为空")
    @Length(max = 255, message = "经营地址字符长度不能超过 255 位")
    private String businessAddress;

    @ApiModelProperty(value = "社会信用代码")
    @Length(max = 18, message = "社会信用代码字符长度不能超过 18 位")
    private String creditCode;

    @ApiModelProperty(value = "进出口资质")
    private BooleanType supplierExport;

    @ApiModelProperty(value = "开票资质")
    private BooleanType supplierInvoicing;

    @ApiModelProperty(value = "税点")
    @Digits(integer = 6, fraction = 2, message = "税点的整数部分不能超过6位，小数部分不能超过2位")
    private BigDecimal taxPoint;

    @ApiModelProperty(value = "营业执照")
    @Size(max = 2, message = "营业执照只允许最多上传两张")
    private List<String> businessFileCodeList;


}
