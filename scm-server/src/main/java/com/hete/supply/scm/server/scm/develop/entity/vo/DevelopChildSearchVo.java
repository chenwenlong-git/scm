package com.hete.supply.scm.server.scm.develop.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/3 14:15
 */
@Data
@NoArgsConstructor
public class DevelopChildSearchVo {
    @ApiModelProperty(value = "id")
    private Long developChildOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "基础信息")
    private DevelopChildBaseMsgVo developChildBaseMsgVo;

    @ApiModelProperty(value = "供应商/价格")
    private DevelopSupplierPriceVo developSupplierPriceVo;

    @ApiModelProperty(value = "打版信息")
    private DevelopPamphletMsgVo developPamphletMsgVo;

    @ApiModelProperty(value = "审版信息")
    private DevelopReviewMsgVo developReviewMsgVo;

    @ApiModelProperty(value = "核价信息")
    private DevelopPricingMsgVo developPricingMsgVo;

    @ApiModelProperty(value = "产前样/首单")
    private DevelopPrenatalFirstMsgVo developPrenatalFirstMsgVo;

    @ApiModelProperty(value = "上架信息")
    private DevelopOnShelvesMsgVo developOnShelvesMsgVo;

    @ApiModelProperty(value = "人员信息")
    private DevelopUserMsgVo developUserMsgVo;
}
