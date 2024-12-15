package com.hete.supply.scm.server.scm.sample.entity.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/11/23 16:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SampleStatusDto extends BaseMqMessageDto {

    @JsonAlias(value = "spuCode")
    private String spuCode;

    private SampleOrderStatus sampleOrderStatus;

    @ApiModelProperty(value = "操作人")
    private String userKey;

    @ApiModelProperty(value = "操作人名称")
    private String username;

}
