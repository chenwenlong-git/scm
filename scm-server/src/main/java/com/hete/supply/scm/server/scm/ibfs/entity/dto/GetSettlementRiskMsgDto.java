package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author yanjiawei
 * Created on 2024/5/29.
 */
@Data
@NoArgsConstructor
public class GetSettlementRiskMsgDto {
    @NotBlank(message = "结算单号不能为空")
    @ApiModelProperty(value = "结算单号")
    private String settleOrderNo;
}
