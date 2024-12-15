package com.hete.supply.scm.server.scm.supplier.entity.vo;

import com.hete.supply.scm.server.scm.ibfs.enums.Currency;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierPaymentAccountType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/5/21 14:37
 */
@Data
@NoArgsConstructor
public class SupplierPaymentAccountDropDownVo {
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "收款账号明细列表")
    public List<PaymentAccountDropDownItemVo> paymentAccountDropDownItemVoList;

    @Data
    public static class PaymentAccountDropDownItemVo {

        @ApiModelProperty(value = "收款账号的id")
        private Long supplierPaymentAccountId;

        @ApiModelProperty(value = "收款账号(账号)")
        private String account;

        @ApiModelProperty(value = "主体")
        private String subject;

        @ApiModelProperty(value = "账号类型")
        private SupplierPaymentAccountType supplierPaymentAccountType;

        @ApiModelProperty(value = "账号名称(户名)")
        private String accountUsername;

        @ApiModelProperty(value = "开户银行(账号银行)")
        private String bankName;

        @ApiModelProperty(value = "支行名称(银行支行)")
        private String bankSubbranchName;

        @ApiModelProperty(value = "账号备注")
        private String remarks;

        @ApiModelProperty(value = "币种")
        private Currency currency;

        @ApiModelProperty(value = "银行所在地区 省份/州")
        private String bankProvince;

        @ApiModelProperty(value = "银行所在地区 城市")
        private String bankCity;

        @ApiModelProperty(value = "银行所在地区 区")
        private String bankArea;

    }

}
