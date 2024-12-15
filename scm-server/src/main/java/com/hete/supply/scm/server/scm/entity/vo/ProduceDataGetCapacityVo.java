package com.hete.supply.scm.server.scm.entity.vo;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2024/8/9 17:14
 */
@Data
@NoArgsConstructor
public class ProduceDataGetCapacityVo {

    @ApiModelProperty(value = "业务ID")
    private Long businessId;

    @ApiModelProperty(value = "原料库存是否满足")
    private BooleanType isContentment;

    @ApiModelProperty(value = "查询结果")
    private BooleanType result;

    @ApiModelProperty(value = "失败原因")
    private String failReason;


}
