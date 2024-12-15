package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.support.api.exception.ParamIllegalException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * @Description 采购预下单查询实体
 * @Date 2024/8/8 09:42
 */
@Data
@NoArgsConstructor
public class PurchasePreOrderDto {

    @ApiModelProperty(value = "预下单明细信息")
    @NotEmpty(message = "预下单明细信息不能为空")
    @Size(max = 20, message = "预下单明细信息不能超过20条")
    private List<@Valid PreOrderInfoDto> preOrderInfoDtoList;

    @Data
    public static class PreOrderInfoDto {
        @ApiModelProperty(value = "业务ID", required = true, notes = "预下单唯一标识,不可重复")
        @NotNull(message = "业务ID不能为空")
        private Long businessId;

        @ApiModelProperty(value = "SKU", required = true, notes = "预下单SKU编码")
        @NotNull(message = "SKU不能为空")
        private String sku;

        @ApiModelProperty(value = "供应商代码", required = true, notes = "预下单供应商代码")
        @NotNull(message = "供应商代码不能为空")
        private String supplierCode;

        @ApiModelProperty(value = "预计下单数量", required = true, notes = "预下单数量")
        @NotNull(message = "预计下单数量不能为空")
        @Positive(message = "预计下单数量必须为正整数")
        private Integer placeOrderCnt;

        @ApiModelProperty(value = "最晚上架时间", required = true, notes = "预下单最晚上架时间")
        @NotNull(message = "最晚上架时间不能为空")
        private LocalDate latestOnShelfTime;
    }
}
