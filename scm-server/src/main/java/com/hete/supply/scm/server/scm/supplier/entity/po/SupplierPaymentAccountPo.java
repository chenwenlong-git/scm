package com.hete.supply.scm.server.scm.supplier.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierPaymentAccountStatus;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierPaymentAccountType;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierPaymentCurrencyType;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 供应商收款账号
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-12-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplier_payment_account")
@ApiModel(value = "SupplierPaymentAccountPo对象", description = "供应商收款账号")
public class SupplierPaymentAccountPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "supplier_payment_account_id", type = IdType.ASSIGN_ID)
    private Long supplierPaymentAccountId;


    @ApiModelProperty(value = "账号")
    private String account;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "账号状态")
    private SupplierPaymentAccountStatus supplierPaymentAccountStatus;


    @ApiModelProperty(value = "账号类型")
    private SupplierPaymentAccountType supplierPaymentAccountType;


    @ApiModelProperty(value = "货币类型")
    private SupplierPaymentCurrencyType supplierPaymentCurrencyType;


    @ApiModelProperty(value = "账号银行")
    private String bankName;


    @ApiModelProperty(value = "户名")
    private String accountUsername;


    @ApiModelProperty(value = "银行支行")
    private String bankSubbranchName;


    @ApiModelProperty(value = "Swift code")
    private String swiftCode;

    @ApiModelProperty(value = "银行所在地区 省份/州")
    private String bankProvince;

    @ApiModelProperty(value = "银行所在地区 城市")
    private String bankCity;

    @ApiModelProperty(value = "银行所在地区 区")
    private String bankArea;

    @ApiModelProperty(value = "是否默认:是(TRUE),否(FALSE)")
    private BooleanType isDefault;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "当前使用飞书审批编号")
    private String feishuAuditOrderNo;

    @ApiModelProperty(value = "主体")
    private String subject;

}
