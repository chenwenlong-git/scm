package com.hete.supply.scm.server.scm.production.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 供应商商品工艺属性表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-20
 */
@Data
public class SupSkuCraftAttrDetailVo {

    @ApiModelProperty(value = "主键id")
    private Long supplierSkuCraftAttrId;

    @ApiModelProperty(value = "缠管")
    private String tubeWrapping;


    @ApiModelProperty(value = "根数")
    private Integer rootsCnt;


    @ApiModelProperty(value = "层数")
    private Integer layersCnt;


    @ApiModelProperty(value = "特殊处理")
    private String specialHandling;
}
