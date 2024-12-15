package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/1/3.
 */
@Data
@ApiModel(description = "返修单加工结果VO")
public class RepairOrderProcessResultVo {

    @ApiModelProperty(value = "返修单加工结果详情")
    private RepairOrderProcessResultInfo repairOrderProcessResult;

    @Data
    @ApiModel(description = "返修单加工结果详情")
    public static class RepairOrderProcessResultInfo {
        @ApiModelProperty(value = "返修单号")
        private String repairOrderNo;

        @ApiModelProperty(value = "正品信息列表")
        private List<FinishedProductInfo> finishedProductList;

    }

    @Data
    @ApiModel(description = "正品信息")
    public static class FinishedProductInfo {

        @ApiModelProperty(value = "SKU")
        private String sku;

        @ApiModelProperty(value = "数量")
        private int quantity;

        @ApiModelProperty(value = "文件编码列表")
        private List<String> fileCodeList;
    }

}
