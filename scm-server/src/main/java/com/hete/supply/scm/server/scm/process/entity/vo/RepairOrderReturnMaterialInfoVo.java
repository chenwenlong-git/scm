package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/2/4.
 */
@Data
@ApiModel(value = "返修单原料归还信息VO")
public class RepairOrderReturnMaterialInfoVo {
    @ApiModelProperty(value = "返修单号", example = "R123456")
    private String repairOrderNo;

    @ApiModelProperty(value = "归还仓库编码", example = "WH001")
    private String returnWarehouseCode;

    @ApiModelProperty(value = "归还仓库名称", example = "WH001")
    private String returnWarehouseName;

    @ApiModelProperty(value = "原料归还信息列表")
    private List<MaterialReturnInfoVo> materialReturnInfoList;

    @Data
    public static class MaterialReturnInfoVo {
        @ApiModelProperty(value = "SKU编码", example = "SKU123")
        private String sku;

        @ApiModelProperty(value = "批次码", example = "BATCH789")
        private String batchCode;

        @ApiModelProperty(value = "可归还数量")
        private Integer returnableQuantity;
    }
}
