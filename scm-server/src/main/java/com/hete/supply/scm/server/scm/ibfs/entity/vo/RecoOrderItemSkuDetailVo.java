package com.hete.supply.scm.server.scm.ibfs.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.CollectOrderType;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoFundType;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoPayType;
import com.hete.supply.scm.api.scm.entity.enums.RecoOrderItemSkuStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/5/14 13:47
 */
@Data
@NoArgsConstructor
public class RecoOrderItemSkuDetailVo {

    @ApiModelProperty(value = "id")
    private Long financeRecoOrderItemSkuId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "关联财务对账单明细ID")
    private Long financeRecoOrderItemId;

    @ApiModelProperty(value = "对账单号")
    private String financeRecoOrderNo;

    @ApiModelProperty(value = "收单单据")
    private String collectOrderNo;

    @ApiModelProperty(value = "款项类型")
    private FinanceRecoFundType financeRecoFundType;

    @ApiModelProperty(value = "收单类型")
    private CollectOrderType collectOrderType;

    @ApiModelProperty(value = "收单关联时间")
    private LocalDateTime associationTime;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "收单金额（单价*数量）")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "收付类型")
    private FinanceRecoPayType financeRecoPayType;

    @ApiModelProperty(value = "校验异常条目")
    private Integer inspectTotalNum;

    @ApiModelProperty(value = "状态")
    private RecoOrderItemSkuStatus recoOrderItemSkuStatus;

    @ApiModelProperty(value = "检验数据信息列表")
    private List<RecoOrderItemInspectVo> recoOrderItemInspectList;

    @ApiModelProperty(value = "关联补扣款单列表")
    private List<RecoOrderItemRelationVo> recoOrderItemRelationList;

    @ApiModelProperty(value = "附件")
    private List<String> fileCodeList;


}
