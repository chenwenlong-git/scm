package com.hete.supply.scm.server.scm.settle.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessSettleStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:04
 */
@Data
@NoArgsConstructor
public class ProcessSettleOrderDetailVo {

    @ApiModelProperty(value = "加工结算ID")
    private Long processSettleOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "加工结算单号")
    private String processSettleOrderNo;

    @ApiModelProperty(value = "创建人名称")
    private String createUsername;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "结算单状态")
    private ProcessSettleStatus processSettleStatus;

    @ApiModelProperty(value = "核算人")
    private String examineUser;

    @ApiModelProperty(value = "核算人名称")
    private String examineUsername;

    @ApiModelProperty(value = "核算时间")
    private LocalDateTime examineTime;

    @ApiModelProperty(value = "结算审核人")
    private String settleUser;

    @ApiModelProperty(value = "结算审核人名称")
    private String settleUsername;

    @ApiModelProperty(value = "结算审核时间")
    private LocalDateTime settleTime;

    @ApiModelProperty(value = "核算拒绝原因")
    private String settleRefuseRemarks;

    @ApiModelProperty(value = "审核拒绝原因")
    private String examineRefuseRemarks;

    @ApiModelProperty(value = "加工结算单明细列表")
    private List<ProcessSettleOrderItem> processSettleOrderItemList;

    @Data
    public static class ProcessSettleOrderItem {

        @ApiModelProperty(value = "加工结算单明细ID")
        private Long processSettleOrderItemId;

        @ApiModelProperty(value = "完成人")
        private String completeUser;

        @ApiModelProperty(value = "完成人名称")
        private String completeUsername;

        @ApiModelProperty(value = "加工单数")
        private String processNum;

        @ApiModelProperty(value = "加工产品数(正品)")
        private String skuNum;

        @ApiModelProperty(value = "结算金额")
        private BigDecimal settlePrice;

    }

}
