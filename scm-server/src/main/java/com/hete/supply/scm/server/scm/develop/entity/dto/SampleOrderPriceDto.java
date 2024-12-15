package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/1/25 11:01
 */
@Data
@NoArgsConstructor
public class SampleOrderPriceDto {

    @ApiModelProperty(value = "版单号")
    private String developPamphletOrderNo;

    @ApiModelProperty(value = "样品单号批量")
    private List<String> developSampleOrderNoList;
}
