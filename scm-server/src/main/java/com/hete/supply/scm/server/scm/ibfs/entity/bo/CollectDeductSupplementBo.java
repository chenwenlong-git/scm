package com.hete.supply.scm.server.scm.ibfs.entity.bo;

import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderItemRelationPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/6/14 17:05
 */
@Data
@NoArgsConstructor
public class CollectDeductSupplementBo {

    @ApiModelProperty(value = "金额")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "创建关联使用单据信息")
    List<FinanceRecoOrderItemRelationPo> insertRecoOrderItemRelationPoList;

}
