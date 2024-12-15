package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 加工单波次创建 mq 参数
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-03-28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@ApiModel(value = "加工单波次创建 mq dto", description = "加工单波次创建 mq dto")
public class ProcessWaveCreateResultMqDto extends BaseMqMessageDto {

    @ApiModelProperty(value = "加工波次 id")
    @NotNull(message = "加工波次 id 不能为空")
    private Long processWaveId;

    @ApiModelProperty(value = "加工单号")
    @NotBlank(message = "加工单号不能为空")
    private String processOrderNo;

}
