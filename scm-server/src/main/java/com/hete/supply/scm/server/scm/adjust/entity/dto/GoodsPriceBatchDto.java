package com.hete.supply.scm.server.scm.adjust.entity.dto;

import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceUniversal;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/6/18 16:41
 */
@Data
@NoArgsConstructor
public class GoodsPriceBatchDto {

    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotEmpty(message = "批量调价提交记录不能为空")
    @ApiModelProperty(value = "批量调价提交记录")
    @Size(max = 50, message = "SKU的数量最大选择数量为50")
    @Valid
    private List<GoodsPriceItemBatchDto> goodsPriceItemBatchList;

    @Data
    public static class GoodsPriceItemBatchDto {

        @ApiModelProperty(value = "id")
        private Long goodsPriceId;

        @NotBlank(message = "sku不能为空")
        @ApiModelProperty(value = "sku")
        private String sku;

        @NotNull(message = "关联渠道ID不能为空")
        @ApiModelProperty(value = "关联渠道ID")
        private Long channelId;

        @NotNull(message = "渠道价格不能为空")
        @ApiModelProperty(value = "渠道价格(调价后的价格)")
        private BigDecimal channelPrice;

        @NotNull(message = "生效时间不能为空")
        @ApiModelProperty(value = "生效时间")
        private LocalDateTime effectiveTime;

        @ApiModelProperty(value = "生效备注")
        private String effectiveRemark;

        @NotNull(message = "设置通用不能为空")
        @ApiModelProperty(value = "设置通用")
        private GoodsPriceUniversal goodsPriceUniversal;

    }


}
