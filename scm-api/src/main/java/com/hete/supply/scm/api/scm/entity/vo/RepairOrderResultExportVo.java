package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2024/1/10 10:44
 */
@Data
@NoArgsConstructor
public class RepairOrderResultExportVo {

    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "批次码")
    private String batchCode;

    @ApiModelProperty(value = "原料批次码")
    private String materialBatchCode;

    @ApiModelProperty(value = "完成数量")
    private Integer completedQuantity;

    @ApiModelProperty(value = "返修人")
    private String repairCreateUsername;

    @ApiModelProperty(value = "返修时间")
    private String repairTimeStr;

    @ApiModelProperty(value = "返修时间")
    private LocalDateTime repairTime;

    @ApiModelProperty(value = "质检正品数")
    private Integer qcPassQuantity;

    @ApiModelProperty(value = "质检次品数")
    private Integer qcFailQuantity;


}
