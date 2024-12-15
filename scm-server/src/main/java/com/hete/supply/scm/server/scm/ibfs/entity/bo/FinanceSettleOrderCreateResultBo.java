package com.hete.supply.scm.server.scm.ibfs.entity.bo;

import com.hete.supply.scm.server.scm.ibfs.enums.FinanceSettleOrderItemType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/5/28.
 */
@Data
@ApiModel(description = "结算单创建结果")
public class FinanceSettleOrderCreateResultBo {

    @ApiModelProperty(value = "结算单号", example = "SETTLE123456")
    private String financeSettleOrderNo;

    @ApiModelProperty(value = "结算明细列表")
    private List<FinanceSettleOrderItemBo> settleOrderItems;

    @Data
    public static class FinanceSettleOrderItemBo {
        @ApiModelProperty(value = "关联单据")
        private String businessNo;

        @ApiModelProperty(value = "明细类型")
        private FinanceSettleOrderItemType financeSettleOrderItemType;
    }

}
