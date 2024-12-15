package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author rockyHuas
 * @date 2023/03/28 09:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ProcessWaveCreateMqDto extends BaseMqMessageDto {

    @ApiModelProperty(value = "加工波次 id")
    @NotNull(message = "加工波次 id 不能为空")
    private Long processWaveId;

    @ApiModelProperty(value = "订单来源平台")
    @NotBlank(message = "订单来源平台不能为空")
    private String platCode;


    @ApiModelProperty(value = "sku")
    @NotBlank(message = "sku 不能为空")
    private String skuCode;

    @ApiModelProperty(value = "数量")
    @NotNull(message = "数量不能为空")
    private Integer amount;

    @ApiModelProperty(value = "交接日期")
    @NotNull(message = "交接日期不能为空")
    private LocalDateTime deliveryTime;

    @ApiModelProperty(value = "收货仓库编码")
    @NotBlank(message = "收货仓库不能为空")
    private String warehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    @NotBlank(message = "收货仓库不能为空")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库类型")
    private String warehouseType;
}
