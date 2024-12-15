package com.hete.supply.scm.server.scm.process.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.api.scm.entity.enums.IsReceiveMaterial;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/1/3.
 */
@Data
@ApiModel(description = "提交返修明细结果的DTO")
public class RepairDetailResultRequestDto {

    @ApiModelProperty(value = "返修人id")
    private String repairUser;

    @ApiModelProperty(value = "返修人名称")
    private String repairUsername;

    @ApiModelProperty(value = "返修时间")
    private LocalDateTime repairTime;

    @ApiModelProperty(value = "批次码", required = true)
    @NotBlank(message = "批次码不能为空")
    private String batchCode;

    @ApiModelProperty(value = "原料批次码", required = true)
    @NotBlank(message = "原料批次码不能为空")
    private String materialBatchCode;

    @ApiModelProperty(value = "图片地址列表", required = true)
    @Size(max = 3, message = "正品图片最多三张")
    @JsonProperty("fileCodeList")
    @NotEmpty(message = "返修图片不能为空")
    private List<String> imageCodes;

    @ApiModelProperty(value = "返修成功数量", required = true)
    @NotNull(message = "返修成功数量不能为空")
    @Min(value = 1, message = "返修成功数量必须大于0")
    @JsonProperty("completedQuantity")
    private Integer successfulRepairQuantity;
}