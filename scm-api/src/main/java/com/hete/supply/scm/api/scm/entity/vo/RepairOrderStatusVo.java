package com.hete.supply.scm.api.scm.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.api.scm.entity.enums.RepairOrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/1/3.
 */
@Data
@ApiModel(description = "返修单状态VO")
public class RepairOrderStatusVo {

    @ApiModelProperty(value = "返修单状态列表")
    private List<RepairOrderStatusInfo> repairOrderStatusInfos;

    @Data
    @ApiModel(description = "返修单状态详情")
    public static class RepairOrderStatusInfo {
        @ApiModelProperty(value = "计划单号")
        private String planOrderNo;

        @ApiModelProperty(value = "返修单号")
        private String repairOrderNo;

        @ApiModelProperty(value = "返修单状态")
        private RepairOrderStatus repairOrderStatus;
    }
}
