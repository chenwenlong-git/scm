package com.hete.supply.scm.server.scm.ibfs.entity.vo;

import com.hete.supply.scm.server.scm.ibfs.enums.RecoOrderItemRelationType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2024/6/14 10:21
 */
@Data
@NoArgsConstructor
public class RecoOrderItemRelationVo {

    @ApiModelProperty(value = "关联单据类型")
    private RecoOrderItemRelationType recoOrderItemRelationType;

    @ApiModelProperty(value = "关联单据号")
    private String businessNo;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "金额(总金额)")
    private BigDecimal totalPrice;

}
