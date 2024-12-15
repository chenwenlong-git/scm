package com.hete.supply.scm.server.scm.process.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.supply.scm.server.scm.process.enums.CommissionAttribute;
import com.hete.supply.scm.server.scm.process.enums.ProcessType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2023/01/12 15:50
 */
@Data
public class ProcessOrderScanStatListVo {

    @ApiModelProperty(value = "主键id")
    private Long processOrderScanId;

    @ApiModelProperty(value = "扫码完成时间")
    private LocalDateTime completeTime;

    @ApiModelProperty(value = "工序编码")
    private String processCode;

    @ApiModelProperty(value = "工序类型")
    private ProcessType processType;

    @ApiModelProperty(value = "工序名称")
    private String processName;

    @ApiModelProperty(value = "工序")
    private ProcessLabel processLabel;

    @ApiModelProperty(value = "二级工序名称")
    private String processSecondName;

    @ApiModelProperty(value = "工序提成（基础单价）")
    private BigDecimal processCommission;

    @ApiModelProperty(value = "额外提成单价")
    private BigDecimal extraCommission;

    @ApiModelProperty(value = "总提成")
    private BigDecimal totalProcessCommission;

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "正品数量")
    private Integer qualityGoodsCnt;

    @ApiModelProperty(value = "次品数量")
    private Integer defectiveGoodsCnt;

    @ApiModelProperty(value = "当前工序总正品数")
    private Integer currentProcessTotalQualityGoodsCnt;

    @JsonProperty(value = "scanCommissionDetails")
    @ApiModelProperty(value = "工序扫码提成明细")
    private List<ScanCommissionDetailVo> scanCommissionDetailVos;

    @Data
    public static class ScanCommissionDetailVo {
        @ApiModelProperty(value = "主键id")
        private Long scanCommissionDetailId;

        @ApiModelProperty(value = "提成属性")
        private CommissionAttribute commissionAttribute;

        @ApiModelProperty(value = "参考单位提成（四舍五入后结果）")
        private BigDecimal unitCommission;

        @ApiModelProperty(value = "提成总金额")
        private BigDecimal totalAmount;
    }
}
