package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: RockyHuas
 * @date: 2022/11/29 11:19
 */
@Data
public class ProcessOrderSyncStatusDto {

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "单据编号（如收货单号，入库单号）")
    private String no;

    @ApiModelProperty(value = "操作人")
    private String operator;


    @ApiModelProperty(value = "操作人")
    private String operatorName;

    @ApiModelProperty(value = "加工单状态")
    private ProcessOrderStatus processOrderStatus;


}
