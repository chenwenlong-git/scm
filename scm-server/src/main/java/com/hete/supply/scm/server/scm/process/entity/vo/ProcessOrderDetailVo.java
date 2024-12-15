package com.hete.supply.scm.server.scm.process.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.plm.api.goods.entity.vo.PlmCategoryVo;
import com.hete.supply.scm.api.scm.entity.enums.MaterialBackStatus;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderDescVo;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderProcedureVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author RockyHuas
 * @date 2022/11/12 12:00
 */
@Data
@NoArgsConstructor
public class ProcessOrderDetailVo {

    @ApiModelProperty(value = "加工单 ID")
    private Long processOrderId;

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "仓库标签")
    private List<String> warehouseTypeList;

    @ApiModelProperty(value = "原料发货仓库编码")
    private String deliveryWarehouseCode;

    @ApiModelProperty(value = "原料发货仓库名称")
    private String deliveryWarehouseName;

    @ApiModelProperty(value = "关联销售单号")
    private String orderNo;

    @ApiModelProperty(value = "客户姓名")
    private String customerName;

    @ApiModelProperty(value = "加工单状态")
    private ProcessOrderStatus processOrderStatus;

    @ApiModelProperty(value = "商品类目")
    private List<PlmCategoryVo> spuCategoryList;


    @ApiModelProperty(value = "类型")
    private ProcessOrderType processOrderType;


    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "加工单备注")
    private String processOrderNote;

    @ApiModelProperty(value = "出库备注")
    private String deliveryNote;

    @ApiModelProperty(value = "当前工序")
    private ProcessLabel currentProcessLabel;

    @ApiModelProperty(value = "原料归还状态")
    private MaterialBackStatus materialBackStatus;


    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "总加工数量")
    private Integer totalProcessNum;

    @ApiModelProperty(value = "总sku")
    private Integer totalSkuNum;

    @ApiModelProperty(value = "总结算金额")
    private BigDecimal totalSettlePrice;

    @ApiModelProperty(value = "生产图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "下单人")
    private String createUsername;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "投产时间")
    private LocalDateTime producedTime;

    @ApiModelProperty(value = "质检时间")
    private LocalDateTime checkedTime;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;


    @ApiModelProperty(value = "入库时间")
    private LocalDateTime storedTime;

    @ApiModelProperty(value = "业务约定日期")
    private LocalDateTime deliverDate;


    @ApiModelProperty(value = "收货单号")
    private String receiptOrderNo;


    @ApiModelProperty(value = "收货人名称")
    private String receiptUsername;


    @ApiModelProperty(value = "入库单号")
    private String storeOrderNo;


    @ApiModelProperty(value = "入库人名称")
    private String storeUsername;


    @ApiModelProperty(value = "结算单号")
    private String settleOrderNo;


    @ApiModelProperty(value = "结算人名称")
    private String settleUsername;


    @ApiModelProperty(value = "结算时间")
    private LocalDateTime settleTime;


    @ApiModelProperty(value = "进入加工操作人名称")
    private String processingUsername;


    @ApiModelProperty(value = "进入加工操作时间")
    private LocalDateTime processingTime;


    @ApiModelProperty(value = "质检单号")
    private String checkOrderNo;

    @ApiModelProperty(value = "后整质检中处理人名称")
    private String checkingUsername;


    @ApiModelProperty(value = "后整质检中处理时间")
    private LocalDateTime checkingTime;


    @ApiModelProperty("加工产品明细")
    private List<ProcessOrderItemVo> processOrderItems;

    @ApiModelProperty("原料产品明细")
    private List<ProcessOrderMaterialVo> processOrderMaterials;

    @ApiModelProperty("加工工序")
    private List<ProcessOrderProcedureVo> processOrderProcedures;

    @ApiModelProperty("加工进度")
    private List<ProcessOrderProcedureByH5Vo> processOrderProcess;

    @ApiModelProperty("加工描述")
    private List<ProcessOrderDescVo> processOrderDescs;

    @ApiModelProperty("版本号")
    private Integer version;

    @ApiModelProperty(value = "可加工成品数")
    private Integer availableProductNum;

    @ApiModelProperty(value = "缺失信息remark")
    private Set<String> missingInformationRemarks;

    @ApiModelProperty(value = "缺失信息code")
    @JsonIgnore
    private String missingInformationEnums;

    @ApiModelProperty(value = "原加工单")
    private String parentProcessOrderNo;
}
