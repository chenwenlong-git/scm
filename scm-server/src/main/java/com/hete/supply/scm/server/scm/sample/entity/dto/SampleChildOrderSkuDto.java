package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/4/28 17:19
 */
@Data
@NoArgsConstructor
public class SampleChildOrderSkuDto {

    @ApiModelProperty("sku批量")
    @NotEmpty(message = "sku不能为空")
    private List<@NotBlank(message = "列表中sku不能为空") String> skuList;

}
