package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.PrepaymentType;
import com.hete.supply.scm.server.scm.ibfs.enums.Currency;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/5/10 17:08
 */
@Data
@NoArgsConstructor
public class PrepaymentAddDto {
    @NotBlank(message = "预付款对象不能为空")
    @ApiModelProperty(value = "预付款对象(供应商code)")
    private String supplierCode;

    @NotNull(message = "预付款类型不能为空")
    @ApiModelProperty(value = "预付款类型：")
    private PrepaymentType prepaymentType;

    @Length(max = 500, message = "预付款事由超过255个字符")
    @NotBlank(message = "预付款事由不能为空")
    @ApiModelProperty(value = "预付款事由")
    private String prepaymentReason;

    @NotNull(message = "预付金额不能为空")
    @ApiModelProperty(value = "预付金额")
    private BigDecimal prepaymentMoney;

    @NotNull(message = "币种不能为空")
    @ApiModelProperty(value = "币种")
    private Currency currency;

    @Valid
    @NotEmpty(message = "收款账户列表不能为空")
    @ApiModelProperty(value = "收款账户列表")
    private List<PrepaymentAddItemDto> prepaymentAddItemList;

    @ApiModelProperty(value = "附件")
    private List<String> fileCodeList;

    @Length(max = 500, message = "预付款备注超过500个字符")
    @ApiModelProperty(value = "预付款备注")
    private String prepaymentRemark;
}
