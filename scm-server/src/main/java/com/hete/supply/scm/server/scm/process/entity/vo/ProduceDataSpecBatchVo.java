package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/11/1 11:08
 */
@Data
@NoArgsConstructor
public class ProduceDataSpecBatchVo {

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "封样图片")
    private List<String> sealImageFileCodeList;

    @ApiModelProperty(value = "规格书信息详情列表")
    private List<ProduceDataSpecVo> produceDataSpecList;


}
