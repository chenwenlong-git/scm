package com.hete.supply.scm.server.scm.settle.entity.vo;

import com.hete.supply.scm.server.scm.settle.enums.SupplementDeductType;
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
public class SettleSupplementOrderVo {

    @ApiModelProperty(value = "总金额")
    private BigDecimal totalSettlePrice;

    @ApiModelProperty(value = "列表明细")
    private List<SupplementOrderDetail> supplementOrderDetailList;

    @Data
    public static class SupplementOrderDetail {
        @ApiModelProperty(value = "补款单号")
        private String businessNo;

        @ApiModelProperty(value = "补款类型")
        private SupplementDeductType supplementDeductType;

        @ApiModelProperty(value = "审核时间")
        private LocalDateTime examineTime;

        @ApiModelProperty(value = "补款金额")
        private BigDecimal price;

        @ApiModelProperty(value = "补款状态")
        private String statusName;
    }


}