package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/12 17:22
 */
@Data
@NoArgsConstructor
public class DevelopPricingOrderNoListDto {

    @NotNull(message = "核价单不能为空")
    @ApiModelProperty(value = "核价单号")
    private List<String> developPricingOrderNoList;

}
