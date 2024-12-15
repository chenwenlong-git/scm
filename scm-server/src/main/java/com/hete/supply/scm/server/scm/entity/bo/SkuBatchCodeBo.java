package com.hete.supply.scm.server.scm.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author yanjiawei
 * @Description SKU-批次码实体
 * @Date 2023/9/27 09:46
 */
@Data
@AllArgsConstructor
public class SkuBatchCodeBo {

    @ApiModelProperty(value = "原料SKU列表")
    private String materialSku;

    @ApiModelProperty(value = "批次码")
    private String batchCode;
}
