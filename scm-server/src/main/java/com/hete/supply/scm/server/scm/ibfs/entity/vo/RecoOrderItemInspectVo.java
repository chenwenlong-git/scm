package com.hete.supply.scm.server.scm.ibfs.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.RecoOrderInspectType;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2024/5/13 15:39
 */
@Data
@NoArgsConstructor
public class RecoOrderItemInspectVo {

    @ApiModelProperty(value = "检验类型")
    private RecoOrderInspectType recoOrderInspectType;

    @ApiModelProperty(value = "收单数据")
    private BigDecimal originalValue;

    @ApiModelProperty(value = "检验数据")
    private BigDecimal inspectValue;

    @ApiModelProperty(value = "检验结果")
    private BooleanType inspectResult;

    @ApiModelProperty(value = "检验结果规则")
    private String inspectResultRule;

}
