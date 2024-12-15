package com.hete.supply.scm.server.scm.settle.entity.vo;

import com.hete.supply.scm.server.scm.settle.enums.SupplementDeduct;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/3/28 09:52
 */
@Data
@NoArgsConstructor
public class SupplementDeductOrderListVo {

    @ApiModelProperty(value = "补扣款单号")
    private String supplementDeductOrderNo;

    @ApiModelProperty(value = "补扣单类型")
    private SupplementDeduct supplementDeduct;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "金额")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "加工明细列表")
    private List<ProcessOrder> processOrderList;

    @Data
    public static class ProcessOrder {

        @ApiModelProperty(value = "加工单")
        private String processOrderNo;

        @ApiModelProperty(value = "金额")
        private BigDecimal price;

        @ApiModelProperty(value = "备注")
        private String remarks;

    }


}
