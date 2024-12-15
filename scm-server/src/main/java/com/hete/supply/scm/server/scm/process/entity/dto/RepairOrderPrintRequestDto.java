package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/1/18.
 */
@Data
@ApiModel(value = "返修单打印信息请求DTO")
public class RepairOrderPrintRequestDto {

    @ApiModelProperty(value = "出库单号列表")
    @NotEmpty(message = "出库单号不能为空")
    @Size(max = 30, message = "返修出库单单次打印最多30条")
    private List<String> deliveryNos;
}
