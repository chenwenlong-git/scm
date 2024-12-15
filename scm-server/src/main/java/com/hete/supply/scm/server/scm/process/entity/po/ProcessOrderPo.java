package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 加工单
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_order")
@ApiModel(value = "ProcessOrderPo对象", description = "加工单")
public class ProcessOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_order_id", type = IdType.ASSIGN_ID)
    private Long processOrderId;


    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;


    @ApiModelProperty(value = "加工单状态")
    private ProcessOrderStatus processOrderStatus;


    @ApiModelProperty(value = "类型，常规(NORMAL)，补单(EXTRA)，默认常规")
    private ProcessOrderType processOrderType;

    @ApiModelProperty(value = "当前工序")
    private ProcessLabel currentProcessLabel;

    @ApiModelProperty(value = "原料归还状态")
    private MaterialBackStatus materialBackStatus;


    @ApiModelProperty(value = "平台")
    private String platform;


    @ApiModelProperty(value = "需求编号")
    private String requireNo;


    @ApiModelProperty(value = "订单编号，仓储系统销售单号")
    private String orderNo;


    @ApiModelProperty(value = "客户姓名")
    private String customerName;


    @ApiModelProperty(value = "加工单备注")
    private String processOrderNote;


    @ApiModelProperty(value = "出库备注")
    private String deliveryNote;


    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;


    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private String warehouseTypes;

    @ApiModelProperty(value = "原料发货仓库编码")
    private String deliveryWarehouseCode;

    @ApiModelProperty(value = "原料发货仓库名称")
    private String deliveryWarehouseName;


    @ApiModelProperty(value = "spu")
    private String spu;


    @ApiModelProperty(value = "约定日期")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "答交日期", notes = "经过重新评估和调整后提出新的约定日期（deliverDate）")
    private LocalDateTime promiseDate;

    @ApiModelProperty(value = "答交日期是否延期", notes = "延期：答交时间-1天小于当前时间")
    private PromiseDateDelayed promiseDateDelayed;

    @ApiModelProperty(value = "总 sku 数量")
    private Integer totalSkuNum;


    @ApiModelProperty(value = "总加工数量")
    private Integer totalProcessNum;

    @ApiModelProperty(value = "可加工成品数")
    private Integer availableProductNum;

    @ApiModelProperty(value = "总结算金额")
    private BigDecimal totalSettlePrice;

    @ApiModelProperty(value = "生产图片")
    private String fileCode;


    @ApiModelProperty(value = "投产时间")
    private LocalDateTime producedTime;


    @ApiModelProperty(value = "质检时间")
    private LocalDateTime checkedTime;


    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;


    @ApiModelProperty(value = "入库时间")
    private LocalDateTime storedTime;

    @ApiModelProperty(value = "打印时间")
    private LocalDateTime printTime;

    @ApiModelProperty(value = "返工类型关联的加工单")
    private String parentProcessOrderNo;

    @ApiModelProperty(value = "加工波次 ID")
    private Long processWaveId;

    @ApiModelProperty(value = "缺失信息")
    private String missingInformation;

    @ApiModelProperty(value = "是否回料")
    private IsReceiveMaterial isReceiveMaterial;

    @ApiModelProperty(value = "是否超额")
    private OverPlan overPlan;

    @ApiModelProperty(value = "是否需要排产")
    private NeedProcessPlan needProcessPlan;

    @ApiModelProperty(value = "排产时间")
    private LocalDateTime processPlanTime;

    @ApiModelProperty(value = "排产工序最早开始时间")
    private LocalDateTime processPlanEarliestExpectBeginTime;

    @ApiModelProperty(value = "排产工序最晚完成时间")
    private LocalDateTime processPlanLatestExpectEndTime;

    @ApiModelProperty(value = "工序完成时间")
    private LocalDateTime processCompletionTime;

    @ApiModelProperty(value = "工序是否延误")
    private ProcessPlanDelay processPlanDelay;

    @ApiModelProperty(value = "容器编码")
    private String containerCode;

    @ApiModelProperty(value = "产品质量")
    private WmsEnum.ProductQuality productQuality;

    @ApiModelProperty(value = "回料时间")
    private LocalDateTime receiveMaterialTime;
}
