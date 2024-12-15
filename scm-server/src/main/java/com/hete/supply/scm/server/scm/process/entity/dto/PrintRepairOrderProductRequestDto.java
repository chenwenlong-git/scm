package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/3/25.
 */
@Data
@ApiModel(description = "打印返修单成品收货信息的请求数据传输对象")
public class PrintRepairOrderProductRequestDto {

    @Size(max = 200, message = "最多允许包含200个返修单号")
    @ApiModelProperty(value = "返修单号")
    @NotEmpty(message = "返修单号列表不能为空")
    private List<String> repairOrderNos;
}
