package com.hete.supply.scm.server.scm.production.entity.bo;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 生产资料主表创建BO
 *
 * @author ChenWenLong
 * @date 2024/12/2 16:50
 */
@Data
public class ProduceDataPoCreateBo {

    @ApiModelProperty(value = "sku")
    @NotBlank(message = "sku不能为空")
    private String sku;

    @ApiModelProperty(value = "spu")
    @NotBlank(message = "spu不能为空")
    private String spu;

    @ApiModelProperty(value = "原料管理")
    private BooleanType rawManage;

    @ApiModelProperty(value = "商品采购价格")
    private BigDecimal goodsPurchasePrice;

    @ApiModelProperty(value = "商品采购价格等空或0时是否取定价(注意:开启时需要查询生产属性)")
    private Boolean goodsPurchasePriceIsGetPricing = false;

}
