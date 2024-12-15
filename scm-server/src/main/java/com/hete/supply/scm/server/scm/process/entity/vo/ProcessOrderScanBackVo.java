package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessFirst;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author RockyHuas
 * @date 2022/11/15 10:00
 */
@Data
@NoArgsConstructor
public class ProcessOrderScanBackVo {

    @ApiModelProperty(value = "id")
    private Long processOrderScanId;

    @ApiModelProperty(value = "一级工序")
    private ProcessFirst processFirst;

    @ApiModelProperty(value = "关联的工序代码")
    private String processCode;

    @ApiModelProperty(value = "关联的工序名称")
    private String processName;

    @ApiModelProperty(value = "二级工序名称")
    private String processSecondName;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;


    @ApiModelProperty(value = "接货人 id")
    private String receiptUser;


    @ApiModelProperty(value = "接货人名称")
    private String receiptUsername;


    @ApiModelProperty(value = "扫码完成时间")
    private LocalDateTime completeTime;


    @ApiModelProperty(value = "扫码完成人")
    private String completeUser;


    @ApiModelProperty(value = "扫码完成人名称")
    private String completeUsername;

    @ApiModelProperty("版本号")
    private Integer version;

}
