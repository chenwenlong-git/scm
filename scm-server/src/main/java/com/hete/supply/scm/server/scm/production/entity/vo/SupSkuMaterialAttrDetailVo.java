package com.hete.supply.scm.server.scm.production.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.CrotchLength;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 供应商商品原料属性
 *
 * @author yanjiawei
 * Created on 2024/9/20.
 */
@Data
public class SupSkuMaterialAttrDetailVo {
    @ApiModelProperty(value = "主键id")
    private Long supplierSkuMaterialAttrId;


    @ApiModelProperty(value = "裆长尺寸")
    private CrotchLength crotchLength;

    @ApiModelProperty(value = "裆长部位")
    private String crotchPosition;

    @ApiModelProperty(value = "深色克重")
    private BigDecimal darkWeight;


    @ApiModelProperty(value = "浅色克重")
    private BigDecimal lightWeight;


    @ApiModelProperty(value = "裆长配比")
    private String crotchLengthRatio;


    @ApiModelProperty(value = "克重")
    private BigDecimal weight;
}
