package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/1/8 09:50
 */
@Data
@NoArgsConstructor
public class RepairOrderItemAndResultVo {

    @ApiModelProperty(value = "详情id")
    private Long repairOrderItemId;

    @ApiModelProperty(value = "详情version")
    private Integer repairOrderItemVersion;

    @ApiModelProperty(value = "结果id")
    private Long repairOrderResultId;

    @ApiModelProperty(value = "结果version")
    private Integer repairOrderResultVersion;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "批次码")
    private String batchCode;

    @ApiModelProperty(value = "返修完成图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "返修完成数量")
    private Integer completedQuantity;

    @ApiModelProperty(value = "返修人id")
    private String repairUser;

    @ApiModelProperty(value = "返修人名称")
    private String repairCreateUsername;

    @ApiModelProperty(value = "返修时间")
    private LocalDateTime repairTime;

    @ApiModelProperty(value = "质检正品数")
    private Integer qcPassQuantity;

    @ApiModelProperty(value = "质检次品数")
    private Integer qcFailQuantity;

    @ApiModelProperty(value = "使用原料批次码")
    private String materialBatchCode;

    @ApiModelProperty(value = "预计加工数")
    private Integer expectProcessNum;

    @ApiModelProperty(value = "发货数量")
    private Integer deliveryNum;
}
