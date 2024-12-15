package com.hete.supply.scm.server.scm.qc.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统计待待质检数据
 *
 * @author ChenWenLong
 * @date 2023/12/26 14:59
 */
@Data
public class QcWaitDetailBo {

    @ApiModelProperty(value = "sku")
    private String skuCode;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "待质检总数")
    private Integer amount;

}
