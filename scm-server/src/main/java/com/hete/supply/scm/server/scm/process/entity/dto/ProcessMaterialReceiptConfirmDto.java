package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "加工原料收货确认收货参数", description = "加工原料收货确认收货参数")
public class ProcessMaterialReceiptConfirmDto {

    @ApiModelProperty(value = "加工原料收货id")
    @NotNull(message = "加工原料收货id不能为空")
    private Long processMaterialReceiptId;

    @ApiModelProperty("加工原料收货明细")
    @Valid
    private List<ProcessMaterialReceiptItem> processMaterialReceiptItems;

    @ApiModelProperty("版本号")
    @NotNull(message = "版本号不能为空")
    private Integer version;

    @Data
    @ApiModel(value = "加工原料收货明细参数", description = "加工原料收货明细参数")
    public static class ProcessMaterialReceiptItem {

        @ApiModelProperty(value = "加工原料收货明细 id")
        @NotNull(message = "加工原料收货明细 id 不能为空")
        private Long processMaterialReceiptItemId;

        @ApiModelProperty(value = "收货数量")
        @NotNull(message = "收货数量不能为空")
        @Positive(message = "收货数量必须为正整数")
        private Integer receiptNum;

        @ApiModelProperty("版本号")
        @NotNull(message = "版本号不能为空")
        private Integer version;
    }
}
