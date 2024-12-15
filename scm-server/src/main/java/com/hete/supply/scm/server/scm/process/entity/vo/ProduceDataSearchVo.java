package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/22 14:45
 */
@Data
@NoArgsConstructor
public class ProduceDataSearchVo {

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "商品类目")
    private String categoryName;

    @ApiModelProperty(value = "主图")
    private List<String> spuFileCodeList;

    @ApiModelProperty(value = "sku列表")
    private List<ProduceDataSkuVo> produceDataSkuList;


}
