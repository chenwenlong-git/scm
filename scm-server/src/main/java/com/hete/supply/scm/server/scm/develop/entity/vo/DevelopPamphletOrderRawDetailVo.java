package com.hete.supply.scm.server.scm.develop.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/17 17:07
 */
@Data
@NoArgsConstructor
public class DevelopPamphletOrderRawDetailVo {

    @ApiModelProperty(value = "版单号")
    private String developPamphletOrderNo;

    @ApiModelProperty(value = "原料bom表")
    private List<DevelopPamphletOrderRawListVo> rawList;


}
