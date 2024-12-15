package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/11 23:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SampleSearchDto extends ComPageDto {
    @ApiModelProperty(value = "样品采购母单号")
    private String sampleParentOrderNo;

    @ApiModelProperty(value = "商品类目")
    private String categoryName;

    @ApiModelProperty(value = "审核人")
    private String approveUser;

    @ApiModelProperty(value = "下单人")
    private String placeOrderUser;

    @ApiModelProperty(value = "审核人")
    private String approveUsername;

    @ApiModelProperty(value = "下单人")
    private String placeOrderUsername;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime placeOrderTimeStart;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime placeOrderTimeEnd;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime approveTimeStart;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime approveTimeEnd;

    @ApiModelProperty(value = "接单时间")
    private LocalDateTime receiveOrderTimeStart;

    @ApiModelProperty(value = "接单时间")
    private LocalDateTime receiveOrderTimeEnd;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTimeStart;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTimeEnd;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTimeStart;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTimeEnd;

    @ApiModelProperty(value = "选样时间")
    private LocalDateTime sampleTimeStart;

    @ApiModelProperty(value = "选样时间")
    private LocalDateTime sampleTimeEnd;

    @ApiModelProperty(value = "业务约定交期")
    private LocalDateTime deliverDateStart;

    @ApiModelProperty(value = "业务约定交期")
    private LocalDateTime deliverDateEnd;

    @ApiModelProperty(value = "确认打版时间")
    private LocalDateTime typesettingTimeStart;

    @ApiModelProperty(value = "确认打版时间")
    private LocalDateTime typesettingTimeEnd;

    @ApiModelProperty(value = "是否首单")
    private BooleanType isFirstOrder;

    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;

    @ApiModelProperty(value = "是否正常采购")
    private BooleanType isNormalOrder;

    @ApiModelProperty(value = "样品单状态")
    private List<SampleOrderStatus> sampleOrderStatusList;

    @ApiModelProperty(value = "spu")
    private List<String> spuList;

    @ApiModelProperty(value = "sku")
    private List<String> skuList;

    @ApiModelProperty(value = "平台")
    private List<String> platformList;

    @ApiModelProperty(value = "开款人")
    private String disburseUser;

    @ApiModelProperty(value = "开款人")
    private String disburseUsername;

}
