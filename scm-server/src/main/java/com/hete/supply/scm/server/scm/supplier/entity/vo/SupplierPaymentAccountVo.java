package com.hete.supply.scm.server.scm.supplier.entity.vo;


import com.hete.supply.scm.server.scm.supplier.enums.SupplierPaymentAccountStatus;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierPaymentAccountType;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierPaymentCurrencyType;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


/**
 * @author ChenWenLong
 * @date 2023/12/5 18:14
 */
@Data
@NoArgsConstructor
public class SupplierPaymentAccountVo {

    @ApiModelProperty(value = "id")
    private Long supplierPaymentAccountId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "账号状态")
    private SupplierPaymentAccountStatus supplierPaymentAccountStatus;

    @ApiModelProperty(value = "是否默认")
    private BooleanType isDefault;

    @ApiModelProperty(value = "账号类型")
    private SupplierPaymentAccountType supplierPaymentAccountType;

    @ApiModelProperty(value = "货币类型")
    private SupplierPaymentCurrencyType supplierPaymentCurrencyType;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "户名")
    private String accountUsername;

    @ApiModelProperty(value = "账号银行支行")
    private String bankSubbranchName;

    @ApiModelProperty(value = "Swift code")
    private String swiftCode;

    @ApiModelProperty(value = "银行所在地区 省份/州")
    private String bankProvince;

    @ApiModelProperty(value = "银行所在地区 城市")
    private String bankCity;

    @ApiModelProperty(value = "银行所在地区 区")
    private String bankArea;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "账号银行")
    private String bankName;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "主体信息")
    private String subject;

    @ApiModelProperty(value = "企业营业执照")
    private List<String> companyFileCodeList;

    @ApiModelProperty(value = "身份证照片正反面")
    private List<String> personalFileCodeList;

    @ApiModelProperty(value = "收款授权书")
    private List<String> authFileCodeList;


}
