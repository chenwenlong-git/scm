package com.hete.supply.scm.server.scm.develop.entity.dto;

import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2023/8/12 09:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DevelopCompleteInfoReturnMqDto extends BaseMqMessageDto {

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "操作人")
    private String userKey;

    @ApiModelProperty(value = "操作人名称")
    private String username;

    @ApiModelProperty(value = "开发母单号")
    private String developParentOrderNo;

    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "spu")
    private String spuCode;

    @ApiModelProperty(value = "sku")
    private String skuCode;

    @ApiModelProperty(value = "平台spuId")
    private Long goodsPlatSpuId;


}
