package com.hete.supply.scm.server.scm.qc.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/11/1.
 */
@Data
public class QcSampleVo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "封样图列表")
    private List<String> samplePicList;
}
