package com.hete.supply.scm.server.scm.settle.entity.vo;

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
public class SettleProcessOrderScanVo {


    @ApiModelProperty(value = "总金额")
    private BigDecimal totalSettlePrice;

    @ApiModelProperty(value = "列表明细")
    private List<SettleProcessOrderScanDetail> settleProcessOrderScanDetailList;

    @Data
    public static class SettleProcessOrderScanDetail {

        @ApiModelProperty(value = "扫码完成时间")
        private LocalDateTime completeTime;

        @ApiModelProperty(value = "加工单号")
        private String processOrderNo;

        @ApiModelProperty(value = "工序")
        private String processName;

        @ApiModelProperty(value = "正品数量")
        private Integer qualityGoodsCnt;

        @ApiModelProperty(value = "提成")
        private BigDecimal processCommission;

        @ApiModelProperty(value = "总提成")
        private BigDecimal totalProcessCommission;

        @ApiModelProperty(value = "下单人名称")
        private String orderUsername;

    }


}
