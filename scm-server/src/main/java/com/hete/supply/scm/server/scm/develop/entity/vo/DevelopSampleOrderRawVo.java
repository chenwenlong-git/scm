package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.plm.api.developorder.enums.MaterialType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2023/8/14 16:04
 */
@Data
@NoArgsConstructor
public class DevelopSampleOrderRawVo {

    @ApiModelProperty(value = "类型")
    private MaterialType materialType;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku数量")
    private Integer skuCnt;

}
