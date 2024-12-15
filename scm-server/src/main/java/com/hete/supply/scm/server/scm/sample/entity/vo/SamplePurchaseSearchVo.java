package com.hete.supply.scm.server.scm.sample.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SampleProduceLabel;
import com.hete.supply.scm.api.scm.entity.vo.SupplierSimpleVo;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/2 19:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SamplePurchaseSearchVo extends SupplierSimpleVo {
    @ApiModelProperty(value = "id")
    private Long sampleChildOrderId;

    @ApiModelProperty(value = "母单id")
    private Long sampleParentOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private String warehouseTypes;

    @ApiModelProperty(value = "收货仓库标签")
    private List<String> warehouseTypeList;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "样品单状态")
    private SampleOrderStatus sampleOrderStatus;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "采购数")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "下发打版时间")
    private LocalDateTime typesetTime;

    @ApiModelProperty(value = "接单时间")
    private LocalDateTime receiveOrderTime;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;


    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;


    @ApiModelProperty(value = "选样时间")
    private LocalDateTime sampleTime;

    @ApiModelProperty(value = "业务约定交期")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "是否首单")
    private BooleanType isFirstOrder;


    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;


    @ApiModelProperty(value = "是否正常采购")
    private BooleanType isNormalOrder;

    @ApiModelProperty(value = "发货人")
    private String deliverUser;

    @ApiModelProperty(value = "发货人")
    private String deliverUsername;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "确认打版时间")
    private LocalDateTime typesettingTime;

    @ApiModelProperty(value = "参照图片")
    private String contrastFileUrl;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "生产标签")
    private SampleProduceLabel sampleProduceLabel;

}
