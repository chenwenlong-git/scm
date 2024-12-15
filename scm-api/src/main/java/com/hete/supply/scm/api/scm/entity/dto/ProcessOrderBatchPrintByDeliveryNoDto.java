package com.hete.supply.scm.api.scm.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2023/02/21 14:29
 */
@Data
@ApiModel(value = "通过出库单批量打印加工单", description = "通过出库单批量打印加工单")
public class ProcessOrderBatchPrintByDeliveryNoDto {

    @ApiModelProperty("出库单号")
    @Valid
    private List<RelatedNos> relatedNosList;

    @Data
    public static class RelatedNos {

        @ApiModelProperty(value = "加工单号")
        @NotBlank(message = "加工单号不能为空")
        private String processOrderNo;

        @ApiModelProperty(value = "出库单号")
        @NotBlank(message = "出库单号不能为空")
        private String deliveryNo;

        @ApiModelProperty(value = "分拣单号")
        private String pickOrderNo;

        @ApiModelProperty(value = "发货仓库编码")
        private String warehouseCode;

        @ApiModelProperty(value = "发货仓库名称")
        private String warehouseName;


    }

}
