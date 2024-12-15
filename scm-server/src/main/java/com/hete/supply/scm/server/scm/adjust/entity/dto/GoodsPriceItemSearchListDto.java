package com.hete.supply.scm.server.scm.adjust.entity.dto;

import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceEffectiveStatus;
import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceItemStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/7/23 15:17
 */
@Data
@NoArgsConstructor
public class GoodsPriceItemSearchListDto {

    @ApiModelProperty(value = "sku批量")
    @NotEmpty(message = "sku批量不能为空")
    private List<String> skuList;

    @ApiModelProperty(value = "供应商代码批量")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "渠道id批量")
    private List<Long> channelIdList;

    @ApiModelProperty(value = "生效状态批量(前端忽略该参数)")
    private List<GoodsPriceEffectiveStatus> goodsPriceEffectiveStatusList;

    @ApiModelProperty(value = "审批状态批量(前端忽略该参数)")
    private List<GoodsPriceItemStatus> goodsPriceItemStatusList;

}
