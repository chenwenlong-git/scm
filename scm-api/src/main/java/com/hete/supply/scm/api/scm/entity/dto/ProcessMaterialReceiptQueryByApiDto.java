package com.hete.supply.scm.api.scm.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.scm.api.scm.entity.enums.MaterialReceiptType;
import com.hete.supply.scm.api.scm.entity.enums.ProcessMaterialReceiptStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/21 16:28
 */
@Data
public class ProcessMaterialReceiptQueryByApiDto extends ComPageDto {

    @ApiModelProperty(value = "加工编号")
    private String processOrderNo;

    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;

    @ApiModelProperty(value = "原料收货类型")
    private List<MaterialReceiptType> materialReceiptTypes;

    @ApiModelProperty(value = "出库单号")
    private String deliveryNo;

    @ApiModelProperty(value = "下单人")
    private String placeOrderUser;

    @ApiModelProperty(value = "下单人")
    private String placeOrderUsername;

    @ApiModelProperty(value = "下单时间-开始时间")
    private LocalDateTime placeOrderTimeStart;

    @ApiModelProperty(value = "下单时间-结束时间")
    private LocalDateTime placeOrderTimeEnd;

    @ApiModelProperty(value = "平台")
    private List<String> platformList;

    @ApiModelProperty(value = "发货时间-开始时间")
    private LocalDateTime deliveryTimeStart;

    @ApiModelProperty(value = "发货时间-结束时间")
    private LocalDateTime deliveryTimeEnd;

    @ApiModelProperty(value = "收货时间-开始时间")
    private LocalDateTime receiptTimeStart;

    @ApiModelProperty(value = "收货时间-结束时间")
    private LocalDateTime receiptTimeEnd;

    @ApiModelProperty(value = "状态")
    private List<ProcessMaterialReceiptStatus> processMaterialReceiptStatuses;

    @ApiModelProperty(value = "sku")
    private List<String> skus;

    @ApiModelProperty(value = "原料收货单id")
    @JsonIgnore
    private List<Long> processMaterialReceiptIds;
}
