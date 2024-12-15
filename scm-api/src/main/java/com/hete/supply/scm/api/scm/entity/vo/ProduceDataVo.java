package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/2/29 15:08
 */
@Data
@ApiModel(description = "生产资料信息VO")
public class ProduceDataVo {

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "重量")
    private BigDecimal weight;

    @ApiModelProperty(value = "生产属性")
    private List<ProduceDataAttrItemVo> produceDataAttrItemList;

    @ApiModelProperty(value = "是否存在Bom信息")
    private BooleanType isExistBom;

}
