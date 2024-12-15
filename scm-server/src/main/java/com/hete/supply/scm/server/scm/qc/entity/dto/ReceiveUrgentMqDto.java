package com.hete.supply.scm.server.scm.qc.entity.dto;

import com.hete.support.api.enums.BooleanType;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/4/15 15:47
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "批量变更收货单加急状态同步scm入参")
@Data
public class ReceiveUrgentMqDto extends BaseMqMessageDto {
    @ApiModelProperty(value = "收货单号列表")
    @NotEmpty(message = "收货单号列表不能为空")
    private List<String> receiveOrderNoList;


    @ApiModelProperty(value = "是否加急，True-加急，False-取消加急")
    @NotNull(message = "是否加急不能为空")
    private BooleanType isUrgent;
}
