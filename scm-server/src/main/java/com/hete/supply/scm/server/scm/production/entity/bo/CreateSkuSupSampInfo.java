package com.hete.supply.scm.server.scm.production.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * @author yanjiawei
 * Created on 2024/9/29.
 */
@Data
public class CreateSkuSupSampInfo {
    @ApiModelProperty("来源单号")
    private String sourceOrderNo;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "sku编码")
    private String sku;

    @ApiModelProperty(value = "封样图文件编码列表")
    private Set<String> samplePicFileCodeList;
}
