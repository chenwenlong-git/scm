package com.hete.supply.scm.server.scm.ibfs.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoFundType;
import com.hete.supply.scm.api.scm.entity.enums.RecoOrderItemSkuStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2024/5/14 13:47
 */
@Data
@NoArgsConstructor
public class RecoOrderDetailFundTypeVo {

    @ApiModelProperty(value = "款项类型")
    private FinanceRecoFundType financeRecoFundType;

    @ApiModelProperty(value = "对账")
    private BigDecimal settlePrice;

    @ApiModelProperty(value = "应收")
    private BigDecimal receivePrice;

    @ApiModelProperty(value = "应付")
    private BigDecimal payPrice;

    @ApiModelProperty(value = "异常")
    private BigDecimal abnormalPrice;

    @ApiModelProperty(value = "状态")
    private RecoOrderItemSkuStatus recoOrderItemSkuStatus;

}
