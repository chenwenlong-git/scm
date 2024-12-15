package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopPamphletOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/3 15:38
 */
@Data
@NoArgsConstructor
public class DevelopPamphletMsgVo {

    @ApiModelProperty(value = "版单id")
    private Long developPamphletOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "版单号")
    private String developPamphletOrderNo;

    @ApiModelProperty(value = "版单状态")
    private DevelopPamphletOrderStatus developPamphletOrderStatus;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "样品数量")
    private Integer developSampleNum;

    @ApiModelProperty(value = "打版次数")
    private Integer pamphletTimes;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "所需原料")
    private List<DevelopPamphletRawDetailVo> developPamphletRawDetailVoList;

    @ApiModelProperty(value = "参考价格")
    private BigDecimal proposedPrice;

    @ApiModelProperty(value = "要求打版完成时间")
    private LocalDateTime expectedOnShelvesDate;

    @ApiModelProperty(value = "需求描述")
    private String demandDesc;

    @ApiModelProperty(value = "拒绝原因")
    private String refuseReason;

    @ApiModelProperty(value = "款式参考图片")
    private List<String> styleReferenceFileCodeList;

    @ApiModelProperty(value = "颜色参考图片")
    private List<String> colorReferenceFileCodeList;

    @ApiModelProperty(value = "供应商样品报价")
    private BigDecimal supplierSamplePrice;

    @ApiModelProperty(value = "供应商大货报价")
    private BigDecimal supplierBulkPrice;

}
