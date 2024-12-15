package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "加工单批量打印参数", description = "加工单批量打印参数")
public class ProcessOrderBatchPrintDto {

    @ApiModelProperty("加工单")
    @Valid
    private List<ProcessItem> processItems;

    @Data
    @ApiModel(value = "加工单打印参数", description = "加工单打印参数")
    public static class ProcessItem {
        @ApiModelProperty(value = "加工单id")
        @NotNull(message = "加工单id不能为空")
        private Long processOrderId;

        @ApiModelProperty("版本号")
        @NotNull(message = "版本号不能为空")
        private Integer version;
    }


}
