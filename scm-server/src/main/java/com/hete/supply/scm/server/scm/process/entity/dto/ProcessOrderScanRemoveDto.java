package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "删除加工扫码记录参数", description = "删除加工扫码记录参数")
public class ProcessOrderScanRemoveDto {

    @ApiModelProperty(value = "加工扫码记录 ID")
    @NotEmpty(message = "加工扫码记录 ID 不能为空")
    @Valid
    private List<ProcessOrderScanItem> processOrderScanItems;

    @Data
    @ApiModel(value = "加工扫码记录 id", description = "加工扫码记录 id")
    public static class ProcessOrderScanItem {

        @ApiModelProperty(value = "加工扫码记录 id")
        private Long processOrderScanId;

        @ApiModelProperty("版本号")
        private Integer version;

    }

}
