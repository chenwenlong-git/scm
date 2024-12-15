package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author RockyHuas
 * @date 2022/11/12 12:00
 */
@Data
@NoArgsConstructor
public class ProcessOrderAvailableBackStatusVo {

    @ApiModelProperty(value = "加工单 ID")
    private Long processOrderId;

    @ApiModelProperty(value = "上一个可回退状态")
    private ProcessOrderStatus preBackStatus;

    @ApiModelProperty(value = "可回退状态")
    private List<BackStatus> availableBackStatuses;

    @ApiModelProperty("版本号")
    private Integer version;

    @Data
    @ApiModel(value = "加工单状态", description = "加工单状态")
    public static class BackStatus {

        @ApiModelProperty("加工单状态")
        private ProcessOrderStatus processOrderStatus;

        @ApiModelProperty("可删除的工序扫码")
        private List<ProcessOrderScanBackVo> processOrderScanBackVos;
    }


}
