package com.hete.supply.scm.server.scm.process.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ProcessProgressStatus;
import com.hete.supply.scm.api.scm.entity.enums.ProcessScanOperateStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * H5工作台对象
 *
 * @author yanjiawei
 * Created on 2024/12/5.
 */
@Data
public class H5WorkbenchVo {
    @ApiModelProperty(value = "扫码记录id")
    private Long processOrderScanId;

    @ApiModelProperty(value = "加工单扫码信息")
    private H5ScanWorkbenchVo h5ProcedureScanWorkbench;

    @ApiModelProperty(value = "加工单信息")
    private H5ProcessOrderWorkbenchVo h5ProcessOrderWorkbench;

    @Data
    public static class H5ProcessOrderWorkbenchVo {
        @ApiModelProperty(value = "加工单号")
        private String processOrderNo;

        @ApiModelProperty(value = "加工单状态")
        private ProcessOrderStatus processOrderStatus;

        @ApiModelProperty(value = "加工spu")
        private String spu;

        @ApiModelProperty(value = "加工sku")
        private String sku;

        @ApiModelProperty(value = "加工数量")
        private Integer processNum;

        @ApiModelProperty(value = "容器编码")
        private String containerCode;
    }

    @Data
    public static class H5ScanWorkbenchVo {
        @ApiModelProperty(value = "扫码记录id")
        private Long processOrderScanId;

        @ApiModelProperty(value = "加工单工序id")
        private Long processOrderProcedureId;

        @NotNull(message = "工序扫码进度状态不能为空")
        private ProcessProgressStatus processProgressStatus;

        @ApiModelProperty(value = "当前工序操作状态")
        private ProcessScanOperateStatus scanOperateStatus;

        @ApiModelProperty(value = "工序id")
        private Long processId;

        @ApiModelProperty(value = "工序编码")
        private String processCode;

        @ApiModelProperty(value = "工序标签")
        private ProcessLabel processLabel;

        @ApiModelProperty(value = "工序名称")
        private String processName;

        @ApiModelProperty(value = "二级工序名称")
        private String processSecondName;

        @ApiModelProperty(value = "关联的工序提成")
        private BigDecimal processCommission;

        @ApiModelProperty(value = "额外提成单价")
        private BigDecimal extraCommission;

        @JsonProperty("commission")
        @ApiModelProperty(value = "预计总提成", example = "200.00")
        private BigDecimal expectedTotalCommission;

        @ApiModelProperty(value = "接货数量")
        private Integer receiptNum;

        @ApiModelProperty(value = "正品数量")
        private Integer qualityGoodsCnt;

        @ApiModelProperty(value = "次品数量")
        private Integer defectiveGoodsCnt;

        @ApiModelProperty(value = "平台")
        private String platform;

        @ApiModelProperty(value = "接货人")
        private String receiptUser;

        @ApiModelProperty(value = "接货人名称")
        private String receiptUsername;

        @ApiModelProperty(value = "收货时间")
        private LocalDateTime receiptTime;

        @ApiModelProperty(value = "加工人")
        private String processingUser;

        @ApiModelProperty(value = "加工时间")
        private LocalDateTime processingTime;

        @ApiModelProperty(value = "加工人名称")
        private String processingUsername;

        @ApiModelProperty(value = "扫码完成人")
        private String completeUser;

        @ApiModelProperty(value = "扫码完成人名称")
        private String completeUsername;

        @ApiModelProperty(value = "扫码完成时间")
        private LocalDateTime completeTime;
    }
}
