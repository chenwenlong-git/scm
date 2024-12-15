package com.hete.supply.scm.server.scm.process.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ProcessPlanDelay;
import com.hete.supply.scm.server.scm.enums.ProductionProcessStatus;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderScanPo;
import com.hete.supply.scm.server.scm.process.enums.ProcessingStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * H5页面扫码/排产信息实体
 *
 * @author yanjiawei
 * @date 2023年08月08日 23:50
 */
@Data
@NoArgsConstructor
public class H5ProcessOrderOperationScanVo {
    @ApiModelProperty(value = "加工单 ID")
    private Long processOrderId;

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "加工单状态")
    private ProcessOrderStatus processOrderStatus;

    @ApiModelProperty(value = "工序加工状态", example = "待开始")
    private ProcessingStatus processStatus;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "加工数量")
    private Integer processNum;

    @ApiModelProperty(value = "提成")
    private BigDecimal commission;

    @ApiModelProperty("参考图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "上一道工序排产人名称")
    private String previousEmployeeName;

    @ApiModelProperty(value = "上一道工序扫码记录")
    private ProcessOrderScanPo lastProcessOrderScanPo;

    @ApiModelProperty(value = "最大接货数")
    private Integer maxAvailableReceiptNum;

    @ApiModelProperty(value = "容器编码", example = "CT123456")
    private String containerCode;

    @ApiModelProperty("加工产品明细")
    @JsonIgnore
    private List<ProcessOrderItemVo> processOrderItems;

    @ApiModelProperty(value = "加工工序明细 id")
    private Long processOrderProcedureId;

    @ApiModelProperty("加工工序信息")
    private H5ProcessOperationScanVo processOrderProcedure;

    @ApiModelProperty("版本号")
    private Integer version;

    @ApiModelProperty("是否延误")
    private ProcessPlanDelay processPlanDelay;

    @ApiModelProperty("加工状态，用于筛选")
    private ProductionProcessStatus productionProcessStatus;

    @JsonIgnore
    private int sort;
}
