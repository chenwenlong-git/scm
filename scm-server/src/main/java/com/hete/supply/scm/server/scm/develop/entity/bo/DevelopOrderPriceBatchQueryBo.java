package com.hete.supply.scm.server.scm.develop.entity.bo;

import com.hete.supply.scm.server.scm.develop.enums.DevelopOrderPriceType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2024/8/15 14:47
 */
@Data
@NoArgsConstructor
public class DevelopOrderPriceBatchQueryBo {

    @ApiModelProperty(value = "相关单据单号")
    @NotBlank(message = "相关单据单号不能为空")
    private String developOrderNo;

    @ApiModelProperty(value = "单据价格类型")
    @NotNull(message = "单据价格类型不能为空")
    private DevelopOrderPriceType developOrderPriceType;

}
