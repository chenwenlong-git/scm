package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.GoodsPriceMaintain;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/6/18 15:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class GoodsPriceDto extends ComPageDto {

    @ApiModelProperty(value = "sku批量")
    private List<String> skuList;

    @ApiModelProperty(value = "产品名称批量")
    private List<String> skuEncodeList;

    @ApiModelProperty("商品品类 ID")
    private Long categoryId;

    @ApiModelProperty(value = "供应商代码批量")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "渠道id批量")
    private List<Long> channelIdList;

    @ApiModelProperty(value = "是否维护价格")
    private List<GoodsPriceMaintain> goodsPriceMaintainList;

    @ApiModelProperty(value = "大于生效时间")
    private LocalDateTime effectiveTimeGt;

    @ApiModelProperty(value = "sku不存在批量")
    private List<String> notSkuList;
}
