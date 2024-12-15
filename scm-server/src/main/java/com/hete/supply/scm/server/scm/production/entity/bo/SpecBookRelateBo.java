package com.hete.supply.scm.server.scm.production.entity.bo;

import com.hete.supply.scm.server.scm.production.entity.vo.SpecPlatformVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 规格书关联业务对象
 *
 * @author yanjiawei
 * Created on 2024/11/1.
 */
@Data
public class SpecBookRelateBo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "开发平台列表")
    private List<String> devPlatformList;

    @ApiModelProperty(value = "平台列表")
    private List<SpecPlatformVo> specPlatformVoList;

    @ApiModelProperty(value = "当前登录用户名称")
    private String loginUsername;

    @ApiModelProperty(value = "产品图片")
    private List<String> fileCodeList;
}
