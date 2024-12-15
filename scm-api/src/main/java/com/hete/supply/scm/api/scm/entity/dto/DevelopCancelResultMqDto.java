package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2023/8/12 16:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DevelopCancelResultMqDto extends BaseMqMessageDto {

    @ApiModelProperty(value = "操作人")
    private String userKey;

    @ApiModelProperty(value = "操作人名称")
    private String username;

    @ApiModelProperty(value = "开发母单号")
    private String developParentOrderNo;


}
