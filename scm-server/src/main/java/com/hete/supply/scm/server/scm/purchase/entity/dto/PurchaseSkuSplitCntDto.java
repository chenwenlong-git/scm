package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/6/12 10:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseSkuSplitCntDto extends PurchaseParentNoDto {
    @ApiModelProperty(value = "sku列表")
    @NotEmpty(message = "sku列表不能为空")
    private List<String> skuList;

}
