package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2023/5/17 14:43
 */
@Data
@NoArgsConstructor
public class PurchaseSplitNewItemDto {
    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    @Length(max = 100, message = "原料sku长度不能超过100个字符")
    private String sku;

    @NotNull(message = "采购数不能为空")
    @ApiModelProperty(value = "采购数")
    private Integer purchaseCnt;

    @NotNull(message = "业务约定交期不能为空")
    @ApiModelProperty(value = "业务约定交期")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "采购单备注")
    @Length(max = 255, message = "采购单备注长度不能超过255个字符")
    private String orderRemarks;
}
