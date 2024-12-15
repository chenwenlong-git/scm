package com.hete.supply.scm.server.scm.ibfs.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2024/5/24 15:08
 */
@Data
@NoArgsConstructor
public class RecoOrderDetailApproveBo {

    @ApiModelProperty(value = "单据类型")
    private String detailType;

    @ApiModelProperty(value = "金额转换后单位")
    private String rmbText;

    @ApiModelProperty(value = "金额")
    private BigDecimal rmb;
}
