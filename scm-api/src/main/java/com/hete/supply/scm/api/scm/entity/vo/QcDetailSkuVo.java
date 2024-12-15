package com.hete.supply.scm.api.scm.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.api.scm.entity.enums.QcState;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/12/12 15:15
 */
@Data
@NoArgsConstructor
public class QcDetailSkuVo {
    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;

    @ApiModelProperty(value = "质检状态")
    private QcState qcState;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "赫特sku")
    private String skuCode;

    @JsonProperty(value = "platCode")
    @ApiModelProperty(value = "平台编码")
    private String platform;

    @ApiModelProperty(value = "(应)质检总数量")
    private Integer amount;


    @ApiModelProperty(value = "待质检数量")
    private Integer waitAmount;
}
