package com.hete.supply.scm.server.scm.develop.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/4 14:28
 */
@Data
@NoArgsConstructor
public class DevelopPamphletDetailVo {

    @ApiModelProperty(value = "版单id")
    private Long developPamphletOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "版单号")
    private String developPamphletOrderNo;

    @ApiModelProperty(value = "所需原料")
    private List<DevelopPamphletRawDetailVo> developPamphletRawDetailVoList;

    @ApiModelProperty(value = "打样数量（所需原料列表原料数量求和）")
    private Integer sampleCnt;

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
}
