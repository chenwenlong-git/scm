package com.hete.supply.scm.server.supplier.sample.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SampleDeliverOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/3 13:06
 */
@Data
@NoArgsConstructor
public class SampleDeliverDetailVo {
    @ApiModelProperty(value = "id")
    private Long sampleDeliverOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "样品发货单号")
    private String sampleDeliverOrderNo;


    @ApiModelProperty(value = "发货单状态")
    private SampleDeliverOrderStatus sampleDeliverOrderStatus;


    @ApiModelProperty(value = "物流")
    private String logistics;


    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private List<String> warehouseTypeList;

    @ApiModelProperty(value = "发货人名称")
    private String deliverUsername;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;

    @ApiModelProperty(value = "发货单明细列表")
    private List<SampleDeliverItemVo> sampleDeliverItemList;

    @ApiModelProperty(value = "样品退货信息")
    private List<SampleReturnSimpleVo> sampleReturnSimpleVoList;

}
