package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.CollectOrderType;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoFundType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/5/14 11:52
 */
@Data
@NoArgsConstructor
public class RecoOrderItemCreateDto extends RecoOrderNoAndVersionDto {

    @ApiModelProperty(value = "供应商代码")
    @NotBlank(message = "供应商代码不能为空")
    private String supplierCode;

    @ApiModelProperty(value = "款项类型")
    @NotNull(message = "款项类型不能为空")
    private FinanceRecoFundType financeRecoFundType;

    @ApiModelProperty(value = "收单类型")
    @NotNull(message = "收单类型不能为空")
    private CollectOrderType collectOrderType;

    @ApiModelProperty(value = "数量")
    @NotNull(message = "数量不能为空")
    @Positive(message = "数量必须为正整数")
    private Integer num;

    @ApiModelProperty(value = "金额")
    @NotNull(message = "金额不能为空")
    @Positive(message = "金额必须大于0")
    @Digits(integer = 13, fraction = 2, message = "金额小数位数不能超过两位")
    private BigDecimal price;

    @ApiModelProperty(value = "总金额")
    @NotNull(message = "总金额不能为空")
    @Positive(message = "总金额必须大于0")
    @Digits(integer = 13, fraction = 2, message = "金额小数位数不能超过两位")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "备注")
    @Length(max = 500, message = "描述字符长度不能超过 500 位")
    private String remarks;

    @ApiModelProperty(value = "附件")
    @Size(max = 3, message = "最多只能有三个附件")
    private List<String> fileCodeList;

}
