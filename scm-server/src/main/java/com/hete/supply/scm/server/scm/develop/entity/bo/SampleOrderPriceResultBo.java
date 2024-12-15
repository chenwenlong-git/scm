package com.hete.supply.scm.server.scm.develop.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopOrderPriceVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/8/27 15:59
 */
@Data
@NoArgsConstructor
public class SampleOrderPriceResultBo {

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "样品处理方式")
    private DevelopSampleMethod developSampleMethod;

    @ApiModelProperty(value = "样品价格")
    private BigDecimal samplePrice;

    @ApiModelProperty(value = "渠道大货价格列表")
    private List<DevelopOrderPriceVo> developOrderPriceList;

}
