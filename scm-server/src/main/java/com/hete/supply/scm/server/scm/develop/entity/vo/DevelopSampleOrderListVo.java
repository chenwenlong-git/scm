package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleStatus;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/29 09:34
 */
@Data
@NoArgsConstructor
public class DevelopSampleOrderListVo {

    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "核价单号")
    private String developPricingOrderNo;

    @ApiModelProperty(value = "是否打样")
    private BooleanType isSample;

    @ApiModelProperty(value = "样品单信息")
    private List<DevelopSampleOrderItemList> developSampleOrderItemList;

    @Data
    public static class DevelopSampleOrderItemList {

        @ApiModelProperty(value = "样品单号")
        private String developSampleOrderNo;

        @ApiModelProperty(value = "样品处理方式")
        private DevelopSampleMethod developSampleMethod;

        @ApiModelProperty(value = "状态")
        private DevelopSampleStatus developSampleStatus;
    }


}
