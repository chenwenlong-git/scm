package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ProcessProgressStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * H5工作台分页查询对象
 *
 * @author yanjiawei
 * Created on 2024/12/5.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class H5WorkbenchPageDto extends ComPageDto {
    @ApiModelProperty("工序扫码进度状态")
    @NotNull(message = "工序扫码进度状态不能为空")
    private ProcessProgressStatus processProgressStatus;

    @ApiModelProperty(value = "扫码完成时间-开始时间")
    private LocalDateTime completeTimeStart;

    @ApiModelProperty(value = "扫码完成时间-结束时间")
    private LocalDateTime completeTimeEnd;
}
