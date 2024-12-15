package com.hete.supply.scm.server.scm.production.entity.vo;

import com.hete.supply.scm.server.scm.adjust.entity.vo.GoodsPriceInfoVo;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/9/24 10:44
 */
@Data
public class SupplierProductCompareItemVo {

    @ApiModelProperty(value = "供应商绑定ID")
    private Long supplierProductCompareId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商产品名称")
    private String supplierProductName;

    @ApiModelProperty(value = "是否启用状态:启用(TRUE)、禁用(FALSE)")
    private BooleanType supplierProductCompareStatus;

    @ApiModelProperty(value = "渠道价格列表")
    private List<GoodsPriceInfoVo> goodsPriceInfoList;

}
