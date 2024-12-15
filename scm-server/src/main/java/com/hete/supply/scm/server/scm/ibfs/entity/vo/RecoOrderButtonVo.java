package com.hete.supply.scm.server.scm.ibfs.entity.vo;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2024/5/14 13:47
 */
@Data
@NoArgsConstructor
public class RecoOrderButtonVo {

    @ApiModelProperty(value = "作废按钮")
    private BooleanType invalidButton;

    @ApiModelProperty(value = "跟单提交按钮")
    private BooleanType submitButton;

    @ApiModelProperty(value = "确认所有条目按钮")
    private BooleanType confirmAllButton;

}
