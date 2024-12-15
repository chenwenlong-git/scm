package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderStatus;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderScanPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author RockyHuas
 * @date 2022/11/15 10:00
 */
@Data
@NoArgsConstructor
public class ProcessOrderScanByH5Vo {

    @ApiModelProperty(value = "加工单 ID")
    private Long processOrderId;

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "加工单状态")
    private ProcessOrderStatus processOrderStatus;

    @ApiModelProperty("参考图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "上一道工序扫码记录")
    private ProcessOrderScanPo lastProcessOrderScanPo;

    @ApiModelProperty(value = "最大接货数")
    private Integer maxAvailableReceiptNum;

    @ApiModelProperty("加工产品明细")
    private List<ProcessOrderItemVo> processOrderItems;

    @ApiModelProperty("加工工序信息")
    private List<ProcessOrderProcedureByH5Vo> processOrderProcedures;

    @ApiModelProperty("版本号")
    private Integer version;
}
