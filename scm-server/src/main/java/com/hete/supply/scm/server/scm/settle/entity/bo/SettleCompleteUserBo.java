package com.hete.supply.scm.server.scm.settle.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2022/11/24 20:38
 */
@Data
@NoArgsConstructor
public class SettleCompleteUserBo {

    @ApiModelProperty(value = "加工结算单详情id")
    private Long processSettleOrderItemId;

    @ApiModelProperty(value = "完成的用户")
    private String completeUser;

    @ApiModelProperty(value = "完成的用户名称")
    private String completeUserName;
}
