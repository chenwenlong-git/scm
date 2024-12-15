package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.plm.api.developorder.enums.DevelopCreateType;
import com.hete.supply.scm.api.scm.entity.enums.DevelopChildOrderStatus;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/4 09:30
 */
@Data
@NoArgsConstructor
public class DevelopChildDetailVo {

    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "开发母单号")
    private String developParentOrderNo;

    @ApiModelProperty(value = "id")
    private Long developChildOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "开款需求(PLM提供枚举)")
    private DevelopCreateType developCreateType;

    @ApiModelProperty(value = "是否加急(PLM提供枚举)")
    private BooleanType isUrgent;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "开发人")
    private String devUser;

    @ApiModelProperty(value = "开发人")
    private String devUsername;

    @ApiModelProperty(value = "跟单人")
    private String followUser;

    @ApiModelProperty(value = "跟单人")
    private String followUsername;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @ApiModelProperty(value = "开发子单状态")
    private DevelopChildOrderStatus developChildOrderStatus;

    @ApiModelProperty(value = "取消原因")
    private String cancelReason;

    @ApiModelProperty(value = "基础信息")
    private DevelopChildBaseMsgVo developChildBaseMsgVo;

    @ApiModelProperty(value = "版单信息")
    private DevelopPamphletMsgVo developPamphletMsgVo;

    @ApiModelProperty(value = "样品单信息")
    private List<DevelopSampleOrderDetailVo> developSampleOrderDetailList;

    @ApiModelProperty(value = "核价信息")
    private DevelopPricingMsgVo developPricingMsgVo;


    @ApiModelProperty(value = "产前样/首单")
    private DevelopPrenatalFirstMsgVo developPrenatalFirstMsgVo;

    @ApiModelProperty(value = "上架信息")
    private DevelopOnShelvesMsgVo developOnShelvesMsgVo;

    @ApiModelProperty(value = "审版信息")
    private DevelopReviewMsgVo developReviewMsgVo;


}
