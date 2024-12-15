package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2022/12/19 22:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseChildConfirmImportationDto extends BaseImportationRowDto {
    @ApiModelProperty(value = "采购子单单号")
    @NotBlank(message = "采购子单单号不能为空")
    private String purchaseChildOrderNo;

    @NotBlank(message = "收货仓库编码不能为空")
    @ApiModelProperty(value = "收货仓库编码")
    @Length(max = 32, message = "收货仓库编码长度不能超过32个字符")
    private String warehouseCode;

    @NotNull(message = "结算单价不能为空")
    @ApiModelProperty(value = "结算单价")
    private BigDecimal purchasePrice;
}
