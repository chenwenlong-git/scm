package com.hete.supply.scm.server.scm.develop.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.DevelopChildOrderStatus;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2023/8/12 16:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DevelopStatusDto extends BaseMqMessageDto {

    @ApiModelProperty(value = "操作人")
    private String userKey;

    @ApiModelProperty(value = "操作人名称")
    private String username;

    @ApiModelProperty(value = "开发母单号")
    private String developParentOrderNo;

    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "状态更新时间")
    private LocalDateTime statusUpdateTime;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "开发子单状态")
    private DevelopChildOrderStatus developChildOrderStatus;


}
