package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/2/4.
 */
@Data
@ApiModel(value = "返修单号打印原料批次码信息")
public class RepairOrderPrintMaterialBatchCodeVo {

    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;

    @ApiModelProperty(value = "返修单原料绑定信息列表")
    private List<MaterialBindingInfoVo> materialBindingInfoVos;

    @Data
    public static class MaterialBindingInfoVo {
        @ApiModelProperty(value = "SKU编码", example = "SKU123")
        private String sku;

        @ApiModelProperty(value = "批次码", example = "BATCH789")
        private String batchCode;
    }
}
