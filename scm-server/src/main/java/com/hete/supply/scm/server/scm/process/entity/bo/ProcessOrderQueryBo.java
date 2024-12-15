package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/25 16:36
 */
@Data
@NoArgsConstructor
public class ProcessOrderQueryBo {

    @ApiModelProperty(value = "加工单号")
    private List<String> processOrderNosBySku;

    @ApiModelProperty(value = "通过分类获取加工单号")
    private List<String> processOrderNosByCategoryId;

    @ApiModelProperty(value = "通过属性获取加工单号")
    private List<String> processOrderNosBySkuAttribute;

    @ApiModelProperty(value = "通过产品名称获取加工单号")
    private List<String> processOrderNosBySkuEncode;

    @ApiModelProperty(value = "通过次品记录获取加工单号")
    private List<String> processOrderNosByDefective;

    @ApiModelProperty(value = "中间单据查询是否为空")
    private Boolean isEmpty;

    @ApiModelProperty(value = "通过原料sku获取加工单号")
    private List<String> processOrderNosByMaterialSku;

}
