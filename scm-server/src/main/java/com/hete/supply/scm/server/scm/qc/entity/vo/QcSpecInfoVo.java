package com.hete.supply.scm.server.scm.qc.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.server.scm.production.entity.vo.SkuAttributeInfoVo;
import com.hete.supply.scm.server.scm.production.entity.vo.SpecPlatformVo;
import com.hete.supply.scm.server.scm.production.entity.vo.SupSkuProdInfoVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 质检规格书
 *
 * @author yanjiawei
 * Created on 2024/10/28.
 */
@Data
public class QcSpecInfoVo {
    @ApiModelProperty(value = "sku编码")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "平台列表")
    @JsonProperty("platformList")
    private List<SpecPlatformVo> specPlatformVoList;

    @ApiModelProperty(value = "当前登录用户名称")
    private String loginUsername;

    @ApiModelProperty(value = "产品图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "商品属性列表")
    @JsonProperty("skuAttrInfoList")
    private List<SkuAttributeInfoVo> skuAttributeInfoVoList;

    @ApiModelProperty(value = "供应商商品生产信息列表(规格书共用)")
    @JsonProperty("supplierSkuProdInfoList")
    private List<SupSkuProdInfoVo> supSkuProdInfoVoList;
}
