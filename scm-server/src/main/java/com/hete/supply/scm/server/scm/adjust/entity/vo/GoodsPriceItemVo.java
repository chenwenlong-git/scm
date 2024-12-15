package com.hete.supply.scm.server.scm.adjust.entity.vo;

import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceUniversal;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/6/18 16:00
 */
@Data
@NoArgsConstructor
public class GoodsPriceItemVo {

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "关联渠道ID")
    private Long channelId;

    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "设置通用")
    private GoodsPriceUniversal goodsPriceUniversal;

    @ApiModelProperty(value = "商品价格操作记录列表")
    private List<GoodsPriceApproveVo> goodsPriceApproveList;

}
