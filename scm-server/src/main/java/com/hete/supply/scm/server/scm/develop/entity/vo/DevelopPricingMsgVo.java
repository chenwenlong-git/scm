package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopPricingOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/3 15:56
 */
@Data
@NoArgsConstructor
public class DevelopPricingMsgVo {
    @ApiModelProperty(value = "核价单号")
    private String developPricingOrderNo;

    @ApiModelProperty(value = "核价状态")
    private DevelopPricingOrderStatus developPricingOrderStatus;

    @ApiModelProperty(value = "样品单列表")
    private List<DevelopPricingMsgSampleVo> developPricingMsgSampleList;

    @Data
    public static class DevelopPricingMsgSampleVo {
        @ApiModelProperty(value = "样品单号")
        private String developSampleOrderNo;

        @ApiModelProperty(value = "样品处理方式")
        private DevelopSampleMethod developSampleMethod;

        @ApiModelProperty(value = "状态")
        private DevelopSampleStatus developSampleStatus;

    }
}
