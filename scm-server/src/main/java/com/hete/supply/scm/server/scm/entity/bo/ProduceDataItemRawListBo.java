package com.hete.supply.scm.server.scm.entity.bo;

import com.hete.supply.plm.api.developorder.enums.MaterialType;
import com.hete.supply.scm.server.scm.process.entity.bo.ProduceDataItemRawCompareBo;
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
public class ProduceDataItemRawListBo {

    @ApiModelProperty(value = "类型")
    private MaterialType materialType;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku数量")
    private Integer skuCnt;

    @ApiModelProperty(value = "生产资料原料对照关系列表")
    private List<ProduceDataItemRawCompareBo> produceDataItemRawCompareBoList;
}
