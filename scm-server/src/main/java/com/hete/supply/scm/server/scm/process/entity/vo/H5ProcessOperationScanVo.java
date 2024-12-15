package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderStatus;
import com.hete.supply.scm.server.scm.enums.EndDelayStatus;
import com.hete.supply.scm.server.scm.enums.StartDelayStatus;
import com.hete.supply.scm.server.scm.process.enums.ProcessingStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yanjiawei
 * @date 2023年08月08日 23:54
 */
@Data
@NoArgsConstructor
public class H5ProcessOperationScanVo {


    @ApiModelProperty(value = "加工工序明细 id")
    private Long processOrderProcedureId;

    @ApiModelProperty(value = "工序加工状态", example = "待开始")
    private ProcessingStatus processStatus;

    @ApiModelProperty(value = "容器编码", example = "CT123456")
    private String containerCode;

    @ApiModelProperty(value = "工序 id")
    private Long processId;

    @ApiModelProperty(value = "工序代码")
    private String processCode;

    @ApiModelProperty(value = "工序标签")
    private ProcessLabel processLabel;

    @ApiModelProperty(value = "二级工序名称")
    private String processSecondName;

    @ApiModelProperty(value = "工序名称")
    private String processName;

    @ApiModelProperty(value = "接货数")
    private Integer receiptNum;

    @ApiModelProperty(value = "加工单状态")
    private ProcessOrderStatus processOrderStatus;


    @ApiModelProperty(value = "正品数量")
    private Integer qualityGoodsCnt;


    @ApiModelProperty(value = "次品数量")
    private Integer defectiveGoodsCnt;


    @ApiModelProperty(value = "接货完成时间")
    private LocalDateTime receiptTime;

    @ApiModelProperty(value = "接货人用户编码")
    private String receiptUser;

    @ApiModelProperty(value = "接货人名称")
    private String receiptUsername;

    @ApiModelProperty(value = "加工时间")
    private LocalDateTime processingTime;

    @ApiModelProperty(value = "加工人名称")
    private String processingUsername;

    @ApiModelProperty(value = "扫码完成时间")
    private LocalDateTime completeTime;

    @ApiModelProperty(value = "扫码完成人名称")
    private String completeUsername;

    @ApiModelProperty(value = "员工编号")
    private String employeeNo;

    @ApiModelProperty(value = "员工名称")
    private String employeeName;

    @ApiModelProperty(value = "要求开始时间", example = "2023-08-07T10:30:00")
    private LocalDateTime estimatedStartTime;

    @ApiModelProperty(value = "要求完成时间", example = "2023-08-07T11:15:00")
    private LocalDateTime estimatedEndTime;

    @ApiModelProperty(value = "实际开始时间", example = "2023-08-07T10:30:00")
    private LocalDateTime actualStartTime;

    @ApiModelProperty(value = "实际结束时间", example = "2023-08-07T10:30:00")
    private LocalDateTime actualEndTime;

    @ApiModelProperty(value = "工序排序字段", example = "1")
    private Integer sort;

    @ApiModelProperty(value = "实时排序字段", example = "1")
    private Integer currentSort;

    @ApiModelProperty(value = "提成")
    private BigDecimal commission;

    @ApiModelProperty("版本号")
    private Integer version;

    @ApiModelProperty("开始时间延误")
    private StartDelayStatus startDelayStatus;

    @ApiModelProperty("结束时间延误")
    private EndDelayStatus endDelayStatus;
}
