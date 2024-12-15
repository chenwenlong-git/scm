package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.plm.api.goods.entity.vo.PlmAttrSkuVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author RockyHuas
 * @date 2022/11/29 19:20
 */
@Data
@NoArgsConstructor
public class ProcessOrderPrintSkuCodeVo {

    @ApiModelProperty(value = "加工单 ID")
    private Long processOrderId;

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "加工数量")
    private Integer processNum;

    @ApiModelProperty(value = "正品数量")
    private Integer qualityGoodsCnt;

    @ApiModelProperty(value = "变体属性")
    private List<PlmAttrSkuVo> variantSkuList;

    @ApiModelProperty("版本号")
    private Integer version;

}
