package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopPricingOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplierType;
import com.hete.supply.scm.server.scm.entity.vo.PricingDevelopSampleOrderListVo;
import com.hete.supply.scm.server.scm.entity.vo.PricingDevelopSampleOrderSearchVo;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/2 18:15
 */
@Data
@NoArgsConstructor
public class DevelopPricingOrderSearchVo {

    @ApiModelProperty(value = "核价单")
    private String developPricingOrderNo;

    @ApiModelProperty(value = "状态")
    private DevelopPricingOrderStatus developPricingOrderStatus;

    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "提交人")
    private String submitUsername;

    @ApiModelProperty(value = "提交时间")
    private LocalDateTime submitTime;

    @ApiModelProperty(value = "核价人")
    private String nuclearPriceUsername;

    @ApiModelProperty(value = "核价时间")
    private LocalDateTime nuclearPriceTime;

    @ApiModelProperty(value = "核价样品信息列表")
    private List<PricingDevelopSampleOrderSearchVo> pricingDevelopSampleOrderSearchVoList;

    @ApiModelProperty(value = "打样图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "供应商类型")
    private SupplierType supplierType;

    @ApiModelProperty(value = "样品列表")
    private List<PricingDevelopSampleOrderListVo> pricingDevelopSampleOrderList;

    @ApiModelProperty(value = "是否打样")
    private BooleanType isSample;

}
