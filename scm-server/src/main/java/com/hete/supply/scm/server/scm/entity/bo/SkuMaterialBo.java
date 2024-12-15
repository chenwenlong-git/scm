package com.hete.supply.scm.server.scm.entity.bo;

import com.hete.supply.scm.server.scm.process.entity.bo.ProcessOrderMaterialCompareBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * sku原料信息
 *
 * @author yanjiawei
 * @date 2023/07/24 18:13
 */
@Data
@NoArgsConstructor
public class SkuMaterialBo {

    @ApiModelProperty(value = "sku")
    private String sku;
    @ApiModelProperty(value = "单件用量")
    private Integer singleNum;
    @ApiModelProperty(value = "原料对照关系列表")
    private List<ProcessOrderMaterialCompareBo> processOrderMaterialCompareBoList;
}