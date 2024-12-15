package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.DevelopPricingOrderStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/2 18:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DevelopPricingOrderSearchDto extends ComPageDto {

    @ApiModelProperty(value = "核价单号")
    private String developPricingOrderNo;

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "核价人")
    private String nuclearPriceUsername;

    @ApiModelProperty(value = "提交人")
    private String submitUsername;

    @ApiModelProperty(value = "创建时间开始")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "创建时间结束")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "提交时间开始")
    private LocalDateTime submitTimeStart;

    @ApiModelProperty(value = "提交时间结束")
    private LocalDateTime submitTimeEnd;

    @ApiModelProperty(value = "核价时间开始")
    private LocalDateTime nuclearPriceTimeStart;

    @ApiModelProperty(value = "核价时间结束")
    private LocalDateTime nuclearPriceTimeEnd;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "核价单号批量")
    private List<String> developPricingOrderNoList;

    @ApiModelProperty(value = "状态")
    private List<DevelopPricingOrderStatus> developPricingOrderStatusList;

}
