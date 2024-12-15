package com.hete.supply.scm.server.scm.qc.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/10/11 16:51
 */
@Data
@NoArgsConstructor
public class QcDetailHandItemVo {
    @ApiModelProperty(value = "质检单详情id")
    private Long qcDetailId;

    @ApiModelProperty(value = "容器编码")
    private String containerCode;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "商品类目")
    private String goodsCategory;

    @ApiModelProperty(value = "质检总数/交接数")
    private Integer amount;

    @ApiModelProperty(value = "正品数")
    private Integer passAmount;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "平台")
    private String platform;
}
