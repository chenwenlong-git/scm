package com.hete.supply.scm.server.scm.entity.bo;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/3/29 14:49
 */
@Data
@NoArgsConstructor
public class RawDeliverBo {
    @NotBlank(message = "原料sku不能为空")
    @ApiModelProperty(value = "原料sku")
    private String sku;

    @NotNull(message = "出库数不能为空")
    @ApiModelProperty(value = "出库数")
    private Integer deliveryCnt;

    @Valid
    @ApiModelProperty(value = "库位出库信息")
    private List<WareLocationDelivery> wareLocationDeliveryList;

    @ApiModelProperty(value = "是否指定库位")
    private BooleanType particularLocation;

    @Data
    public static class WareLocationDelivery {
        @NotNull(message = "库位出库数量不能为空")
        @Positive(message = "库位出库数量必须为正整数")
        @ApiModelProperty(value = "库位出库数量")
        private Integer deliveryAmount;

        @NotBlank(message = "库位不能为空")
        @ApiModelProperty(value = "库位")
        private String warehouseLocationCode;

        @NotBlank(message = "批次码不能为空")
        @ApiModelProperty(value = "批次码")
        private String batchCode;
    }
}
