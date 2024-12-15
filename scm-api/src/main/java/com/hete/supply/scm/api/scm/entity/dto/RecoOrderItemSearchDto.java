package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.CollectOrderType;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoFundType;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoPayType;
import com.hete.supply.scm.api.scm.entity.enums.RecoOrderItemSkuStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/5/13 14:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RecoOrderItemSearchDto extends ComPageDto {

    @ApiModelProperty(value = "对账单号")
    private String financeRecoOrderNo;

    @ApiModelProperty(value = "sku批量")
    private List<String> skuList;

    @ApiModelProperty(value = "收单单据批量")
    private List<String> collectOrderNoList;

    @ApiModelProperty(value = "id批量")
    private List<Long> financeRecoOrderItemSkuIdList;

    @ApiModelProperty(value = "收单类型批量")
    private List<CollectOrderType> collectOrderTypeList;

    @ApiModelProperty(value = "款项类型批量")
    private List<FinanceRecoFundType> financeRecoFundTypeList;

    @ApiModelProperty(value = "状态批量")
    private List<RecoOrderItemSkuStatus> recoOrderItemSkuStatusList;

    @ApiModelProperty(value = "应付应收批量")
    private List<FinanceRecoPayType> financeRecoPayTypeList;

}
