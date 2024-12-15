package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "加工单回退参数", description = "加工单回退参数")
public class ProcessOrderBackDto {

    @ApiModelProperty(value = "加工单id")
    @NotNull(message = "加工单id不能为空")
    private Long processOrderId;

    @ApiModelProperty(value = "回退状态")
    @NotNull(message = "回退状态不能为空")
    private ProcessOrderStatus processOrderStatus;

    @ApiModelProperty("需要删除的扫码记录")
    private List<ProcessOrderScan> processOrderScans;

    @Data
    public static class ProcessOrderScan {

        @ApiModelProperty(value = "扫码记录 id")
        @NotNull(message = "扫码记录不能为空")
        private Long processOrderScanId;

        @ApiModelProperty("版本号")
        @NotNull(message = "版本号不能为空")
        private Integer version;
    }

    @ApiModelProperty("版本号")
    @NotNull(message = "版本号不能为空")
    private Integer version;
}
