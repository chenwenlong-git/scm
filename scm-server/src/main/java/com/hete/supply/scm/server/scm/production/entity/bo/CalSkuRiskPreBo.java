package com.hete.supply.scm.server.scm.production.entity.bo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hete.supply.scm.api.scm.entity.enums.SkuRisk;
import com.hete.supply.scm.server.scm.production.entity.po.*;
import com.hete.supply.scm.server.scm.production.enums.AttributeInputType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author yanjiawei
 * Created on 2024/9/26.
 */
@Data
public class CalSkuRiskPreBo {
    @ApiModelProperty(value = "属性录入类型")
    private List<AttributeInputType> attrInputTypes = Lists.newArrayList();

    @ApiModelProperty(value = "属性信息列表")
    private List<AttributePo> attributePoList = Lists.newArrayList();
    @ApiModelProperty(value = "属性可选项信息列表")
    private List<AttributeOptionPo> attributeOptPoList = Lists.newArrayList();

    @ApiModelProperty(value = "商品属性信息列表")
    private List<SkuAttributePo> skuAttributePoList = Lists.newArrayList();
    @ApiModelProperty(value = "商品属性值信息列表")
    private List<SkuAttributeValuePo> skuAttributeValuePoList = Lists.newArrayList();

    @ApiModelProperty(value = "供应商商品属性信息列表")
    private List<SupplierSkuAttributePo> supplierSkuAttributePoList = Lists.newArrayList();
    @ApiModelProperty(value = "供应商商品属性值信息列表")
    private List<SupplierSkuAttributeValuePo> supplierSkuAttributeValuePoList = Lists.newArrayList();

    @ApiModelProperty(value = "属性风险配置列表")
    private List<AttributeRiskPo> attributeRiskPoList = Lists.newArrayList();
    @ApiModelProperty(value = "属性配置项风险配置列表")
    private List<AttributeOptionRiskPo> attributeOptionRiskPoList = Lists.newArrayList();

    @ApiModelProperty(value = "商品风险评分结果")
    private TreeMap<String, BigDecimal> skuScoreMap = Maps.newTreeMap();

    @ApiModelProperty(value = "商品风险等级结果")
    private Map<String, SkuRisk> skuRiskMap = Maps.newHashMap();
    @ApiModelProperty(value = "风险等级日志列表")
    private List<SkuRiskLogPo> skuRiskLogPoList = Lists.newArrayList();
}
