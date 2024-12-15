package com.hete.supply.scm.server.scm.ibfs.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2024/6/14 11:37
 */
@Data
@NoArgsConstructor
public class RecoOrderItemRelationCheckVo {

    @ApiModelProperty(value = "对账单号")
    private String financeRecoOrderNo;

    @ApiModelProperty(value = "关联单据 ID")
    private Long businessId;

    @ApiModelProperty(value = "关联单据号")
    private String businessNo;


}
