package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2024/1/8 09:50
 */
@Data
@NoArgsConstructor
public class RepairOrderItemDetailVo {

    @ApiModelProperty(value = "id")
    private Long repairOrderItemId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "批次码")
    private String batchCode;

    @ApiModelProperty(value = "预计加工数")
    private Integer expectProcessNum;

    @ApiModelProperty(value = "实际加工完成数")
    private Integer actProcessedCompleteCnt;

    @ApiModelProperty(value = "实际加工报废数")
    private Integer actProcessScrapCnt;

    @ApiModelProperty(value = "发货数量")
    private Integer deliveryNum;


}
