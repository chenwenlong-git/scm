package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.IsCapacitySatisfy;
import com.hete.supply.scm.api.scm.entity.enums.IsMaterialStockSatisfy;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.ParamIllegalException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * @author ChenWenLong
 * @date 2024/8/5 16:25
 */
@Data
@NoArgsConstructor
public class PurchasePreOrderVo {

    @ApiModelProperty(value = "供应商列表信息")
    private List<PurchasePreOrderInfoVo> preOrderInfoVoList;

    @Data
    public static class PurchasePreOrderInfoVo {
        @ApiModelProperty(value = "业务ID", notes = "不为空，唯一键")
        private Long businessId;

        @ApiModelProperty(value = "SKU", notes = "不为空")
        private String sku;

        @ApiModelProperty(value = "供应商代码", notes = "预下单供应商代码")
        private String supplierCode;

        @ApiModelProperty(value = "预计上架日期", notes = "存在为空")
        private LocalDate preShelfTime;

        @ApiModelProperty(value = "预计上架数量", notes = "不为空")
        private Integer preShelfQty;

        @ApiModelProperty(value = "产能是否满足", notes = "可能为空")
        private IsCapacitySatisfy isCapacitySatisfy;

        @ApiModelProperty(value = "原料库存是否满足", notes = "不为空")
        private IsMaterialStockSatisfy isMaterialStockSatisfy;

        @ApiModelProperty(value = "原料查询失败原因", notes = "存在为空 最大字符500")
        private String materialFailReason;

        @ApiModelProperty(value = "产能查询失败原因", notes = "存在为空 最大字符500")
        private String capacityFailReason;

        @ApiModelProperty(value = "查询结果", notes = "不为空")
        private BooleanType queryResult;

        @ApiModelProperty(value = "失败原因", notes = "存在为空 最大字符500")
        private String failReason;
    }
}
