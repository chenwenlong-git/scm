package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/2 19:17
 */
@Data
@NoArgsConstructor
public class DevelopPricingOrderSubmitDto {

    @NotNull(message = "核价单信息不能为空")
    @ApiModelProperty(value = "核价单信息")
    @Valid
    private List<DevelopPricingOrderDtoList> developPricingOrderDtoList;

    @Data
    public static class DevelopPricingOrderDtoList {

        @NotBlank(message = "核价单号不能为空")
        @ApiModelProperty(value = "核价单号")
        private String developPricingOrderNo;

        @NotBlank(message = "核价人不能为空")
        @ApiModelProperty(value = "核价人")
        private String nuclearPriceUser;

        @NotBlank(message = "核价人不能为空")
        @ApiModelProperty(value = "核价人的名称")
        private String nuclearPriceUsername;

        @ApiModelProperty(value = "样品单号列表")
        private List<String> developSampleOrderNoList;

    }


}
