package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author RockyHuas
 * @date 2022/11/12 12:00
 */
@Data
@NoArgsConstructor
public class ProcessOrderProcedureByH5Vo {

    @ApiModelProperty(value = "加工工序明细 id")
    private Long processOrderProcedureId;

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


    @ApiModelProperty(value = "正品数量")
    private Integer qualityGoodsCnt;


    @ApiModelProperty(value = "次品数量")
    private Integer defectiveGoodsCnt;


    @ApiModelProperty(value = "接货时间")
    private LocalDateTime receiptTime;

    @ApiModelProperty(value = "接货人用户编码")
    private String receiptUser;

    @ApiModelProperty(value = "接货人名称")
    private String receiptUsername;

    @ApiModelProperty(value = "加工时间")
    private LocalDateTime processingTime;


    @ApiModelProperty(value = "加工人 id")
    private String processingUser;


    @ApiModelProperty(value = "加工人名称")
    private String processingUsername;


    @ApiModelProperty(value = "扫码完成时间")
    private LocalDateTime completeTime;

    @ApiModelProperty(value = "扫码完成人名称")
    private String completeUsername;

    @ApiModelProperty(value = "工序排序")
    private Integer sort;

    @ApiModelProperty(value = "工序提成")
    private BigDecimal commission;

    @ApiModelProperty("版本号")
    private Integer version;
}
