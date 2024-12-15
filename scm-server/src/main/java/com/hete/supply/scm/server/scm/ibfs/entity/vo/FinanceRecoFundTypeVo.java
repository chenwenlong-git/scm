package com.hete.supply.scm.server.scm.ibfs.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.CollectOrderType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2024/5/23 10:19
 */
@Data
@NoArgsConstructor
public class FinanceRecoFundTypeVo {
    @ApiModelProperty(value = "收单类型")
    private CollectOrderType collectOrderType;
}
