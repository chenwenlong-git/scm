package com.hete.supply.scm.server.scm.purchase.entity.dto;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2023/5/17 23:38
 */
@Data
@NoArgsConstructor
public class PurchasePlanConfirmItemDto {
    @ApiModelProperty(value = "采购子单单号")
    @NotBlank(message = "采购子单单号不能为空")
    private String purchaseChildOrderNo;

    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "采购单备注")
    @Length(max = 255, message = "采购单备注长度不能超过255个字符")
    private String orderRemarks;

    @NotNull(message = "是否加急不能为空")
    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;

    @NotNull(message = "要求发货时间不能为空")
    @ApiModelProperty(value = "要求发货时间")
    private LocalDateTime deliverDate;
}
