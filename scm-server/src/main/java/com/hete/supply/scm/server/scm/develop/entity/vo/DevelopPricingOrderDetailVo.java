package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopPricingOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplierType;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/2 19:21
 */
@Data
@NoArgsConstructor
public class DevelopPricingOrderDetailVo {

    @ApiModelProperty(value = "核价单号")
    private String developPricingOrderNo;

    @ApiModelProperty(value = "状态")
    private DevelopPricingOrderStatus developPricingOrderStatus;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商类型")
    private SupplierType supplierType;

    @ApiModelProperty(value = "是否打样")
    private BooleanType isSample;

    @ApiModelProperty(value = "样品单列表")
    private List<DevelopPricingOrderInfoVo> developPricingOrderInfoList;

    @ApiModelProperty(value = "开发子单的生产属性")
    private List<DevelopChildOrderAttrVo> developChildOrderAttrList;

}
