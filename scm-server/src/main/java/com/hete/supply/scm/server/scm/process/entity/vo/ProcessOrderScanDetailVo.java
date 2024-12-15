package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.IsReceiveMaterial;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ProcessPlanDelay;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderScanPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yanjiawei
 * @date 2023年08月09日 07:55
 */
@Data
@NoArgsConstructor
public class ProcessOrderScanDetailVo {
    @ApiModelProperty(value = "加工单 ID")
    private Long processOrderId;

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "是否回料")
    private IsReceiveMaterial isReceiveMaterial;

    @ApiModelProperty(value = "加工单状态")
    private ProcessOrderStatus processOrderStatus;

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

    @ApiModelProperty(value = "上一道工序扫码记录")
    private ProcessOrderScanPo lastProcessOrderScanPo;

    @ApiModelProperty(value = "最大接货数")
    private Integer maxAvailableReceiptNum;

    @ApiModelProperty(value = "容器编码", example = "CT123456")
    private String containerCode;

    @ApiModelProperty("加工产品明细")
    private List<ProcessOrderItemVo> processOrderItems;

    @ApiModelProperty("加工工序信息")
    private List<H5ProcessOperationScanVo> processOrderProcedures;

    @ApiModelProperty("是否延误")
    private ProcessPlanDelay processPlanDelay;

    @ApiModelProperty("版本号")
    private Integer version;
}
