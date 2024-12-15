package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/2
 */
@Data
@ApiModel(value = "采购母单产品明细", description = "采购母单产品明细")
public class PurchaseProductDemandItemDto {
    @ApiModelProperty(value = "id")
    private Long purchaseParentOrderItemId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "sku图片")
    private List<String> fileCodeList;

    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    @Length(max = 100, message = "原料sku长度不能超过100个字符")
    private String sku;

    @ApiModelProperty(value = "sku变体属性")
    private String variantProperties;

    @NotNull(message = "采购数不能为空")
    @ApiModelProperty(value = "采购数")
    @Min(value = 1, message = "采购数不能小于0")
    private Integer purchaseCnt;


}
