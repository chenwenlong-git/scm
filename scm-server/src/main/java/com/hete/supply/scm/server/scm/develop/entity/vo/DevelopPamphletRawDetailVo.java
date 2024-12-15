package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.plm.api.developorder.enums.MaterialType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/8/4 14:43
 */
@Data
@NoArgsConstructor
public class DevelopPamphletRawDetailVo {
    @ApiModelProperty(value = "类型")
    private MaterialType materialType;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku数量")
    private Integer skuCnt;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "关联单据")
    private String purchaseRawDeliverOrderNo;
}
