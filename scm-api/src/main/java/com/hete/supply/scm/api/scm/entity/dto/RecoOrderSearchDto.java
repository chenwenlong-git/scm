package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/5/13 14:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RecoOrderSearchDto extends ComPageDto {

    @ApiModelProperty(value = "对账单号")
    private String financeRecoOrderNo;

    @ApiModelProperty(value = "对账单号批量")
    private List<String> financeRecoOrderNoList;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "单据号")
    private String collectOrderNo;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "当前审批人（数据权限）(前端忽略该参数)")
    private String ctrlUser;

    @ApiModelProperty(value = "当前审批人(前端忽略该参数)")
    private String approveUser;

    @ApiModelProperty(value = "sku批量")
    private List<String> skuList;

    @ApiModelProperty(value = "供应商代码批量")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCode;

    @ApiModelProperty(value = "spm授权供应商代码(前端忽略该参数)")
    private List<String> spmAuthSupplierCode;

    @ApiModelProperty(value = "状态批量")
    private List<FinanceRecoOrderStatus> financeRecoOrderStatusList;

    @ApiModelProperty(value = "对账单号详情SKU的ID批量")
    private List<Long> financeRecoOrderItemSkuId;

    @ApiModelProperty(value = "收单类型")
    private List<CollectOrderType> collectOrderTypeList;

    @ApiModelProperty(value = "款项类型")
    private List<FinanceRecoFundType> financeRecoFundTypeList;

    @ApiModelProperty(value = "应付应收批量")
    private List<FinanceRecoPayType> financeRecoPayTypeList;

    @ApiModelProperty(value = "单据号批量")
    private List<String> collectOrderNoList;

    @ApiModelProperty(value = "状态批量")
    private List<RecoOrderItemSkuStatus> recoOrderItemSkuStatusList;

    @ApiModelProperty(value = "对账周期时间开始")
    private LocalDateTime reconciliationStartTimeStart;

    @ApiModelProperty(value = "对账周期时间结束")
    private LocalDateTime reconciliationStartTimeEnd;

}
