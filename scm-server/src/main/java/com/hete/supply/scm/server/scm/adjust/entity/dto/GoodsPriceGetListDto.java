package com.hete.supply.scm.server.scm.adjust.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/6/18 15:31
 */
@Data
@NoArgsConstructor
public class GoodsPriceGetListDto {

    @ApiModelProperty(value = "检索条件")
    private List<GoodsPriceGetListItemDto> goodsPriceGetListItemList;

    @Data
    public static class GoodsPriceGetListItemDto {

        @NotBlank(message = "供应商编码不能为空")
        @ApiModelProperty(value = "供应商编码")
        private String supplierCode;

        @NotBlank(message = "sku不能为空")
        @ApiModelProperty(value = "sku")
        private String sku;

        @NotNull(message = "关联渠道ID不能为空")
        @ApiModelProperty(value = "关联渠道ID")
        private Long channelId;


    }

}
