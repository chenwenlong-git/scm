package com.hete.supply.scm.server.scm.develop.entity.bo;

import com.hete.supply.scm.server.scm.develop.enums.DevelopOrderPriceType;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/8/15 14:47
 */
@Data
@NoArgsConstructor
public class DevelopOrderPriceCreateBo {

    @ApiModelProperty(value = "相关单据单号")
    @NotBlank(message = "相关单据单号不能为空")
    private String developOrderNo;

    @ApiModelProperty(value = "单据价格类型")
    @NotNull(message = "单据价格类型不能为空")
    private DevelopOrderPriceType developOrderPriceType;

    @ApiModelProperty(value = "渠道大货价格列表")
    private List<@Valid DevelopOrderPriceCreateItemBo> developOrderPriceCreateItemBoList;

    @Data
    public static class DevelopOrderPriceCreateItemBo {
        @ApiModelProperty(value = "关联渠道ID")
        @NotNull(message = "关联渠道ID不能为空")
        private Long channelId;

        @ApiModelProperty(value = "大货价格")
        @NotNull(message = "大货价格不能为空")
        @DecimalMax(value = "99999999.99", message = "大货报价不能超过1亿")
        private BigDecimal price;

        @ApiModelProperty(value = "默认设置样品价格")
        private BooleanType isDefaultPrice;

    }


}
