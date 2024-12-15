package com.hete.supply.scm.server.scm.ibfs.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/5/27 18:34
 */
@Data
@NoArgsConstructor
public class RecoOrderBindingSettleOrderBo {

    @ApiModelProperty(value = "结算单号")
    @NotBlank(message = "结算单号不能为空")
    private String financeSettleOrderNo;

    @ApiModelProperty(value = "对账单列表")
    @NotEmpty(message = "对账单号不能为空")
    private List<RecoOrderBindingSettleOrderItemBo> recoOrderBindingSettleOrderItemList;

    @Data
    public static class RecoOrderBindingSettleOrderItemBo {
        @ApiModelProperty(value = "对账单号")
        @NotBlank(message = "对账单号不能为空")
        private String financeRecoOrderNo;
    }
}
