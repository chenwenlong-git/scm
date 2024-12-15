package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/2/4.
 */
@Data
@Api(tags = "返修原料批次码查询")
public class RepairMaterialBatchCodeRequestDto {

    @ApiModelProperty(value = "返修单号批量", required = true)
    @NotEmpty(message = "返修单号不能为空")
    @Size(max = 20, message = "查询条数最大为20")
    private List<String> repairOrderNoList;
}
