package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import com.hete.supply.scm.api.scm.entity.vo.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author RockyHuas
 * @date 2022/11/29 19:20
 */
@Data
@NoArgsConstructor
public class ProcessOrderPrintVo {

    @ApiModelProperty(value = "加工单 ID")
    private Long processOrderId;

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "发货仓库编码")
    private String deliveryWarehouseCode;

    @ApiModelProperty(value = "发货仓库名称")
    private String deliveryWarehouseName;

    @ApiModelProperty(value = "出库单号")
    private String deliveryNo;

    @ApiModelProperty(value = "类型，常规(NORMAL)，补单(EXTRA)，默认常规")
    private ProcessOrderType processOrderType;

    @ApiModelProperty(value = "约定日期")
    private LocalDateTime deliverDate;


    @ApiModelProperty(value = "加工单状态")
    private ProcessOrderStatus processOrderStatus;


    @ApiModelProperty(value = "加工单备注")
    private String processOrderNote;

    @ApiModelProperty(value = "生产图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "下单人")
    private String createUsername;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "打印时间")
    private LocalDateTime printTime;

    @ApiModelProperty(value = "拣货单号")
    private String pickOrderNo;

    @ApiModelProperty(value = "原料仓库编码")
    private String rawWarehouseCode;

    @ApiModelProperty(value = "原料仓库名称")
    private String rawWarehouseName;

    @ApiModelProperty("加工产品明细")
    private List<ProcessOrderItemPrintVo> processOrderItems;

    @ApiModelProperty("单位原料配比")
    private List<ProcessOrderMaterialPrintVo> processOrderMaterials;

    @ApiModelProperty("加工工序")
    private List<ProcessOrderProcedureVo> processOrderProcedures;

    @ApiModelProperty("加工描述")
    private List<ProcessOrderDescVo> processOrderDescs;

    @ApiModelProperty("出库明细列表")
    private List<ProcessOrderPrintDeliveryOrderVo> processOrderPrintDeliveryOrderList;

    @ApiModelProperty("版本号")
    private Integer version;

    @ApiModelProperty(value = "垛口信息")
    private List<String> pickingCartStackCodeList;
}
