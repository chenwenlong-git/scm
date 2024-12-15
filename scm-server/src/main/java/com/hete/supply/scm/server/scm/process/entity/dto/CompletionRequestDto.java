package com.hete.supply.scm.server.scm.process.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/1/3.
 */
@Data
@ApiModel(description = "完成返修单加工参数DTO")
public class CompletionRequestDto {

    @ApiModelProperty(value = "返修单号", required = true)
    @NotBlank(message = "返修单号不能为空")
    private String repairOrderNo;

    @ApiModelProperty(value = "版本号")
    @NotNull(message = "版本号不能为空")
    private Integer version;

    @ApiModelProperty(value = "返修结果明细列表", required = true)
    @NotEmpty(message = "返修结果明细列表不能为空")
    @JsonProperty("repairOrderResultDetailList")
    private List<@Valid CompletionDetailRequestDto> completionDetails;

    @Data
    public static class CompletionDetailRequestDto {
        @ApiModelProperty(value = "返修结果ID")
        private Long repairOrderResultId;

        @ApiModelProperty(value = "返修人id")
        private String repairUser;

        @ApiModelProperty(value = "返修人名称")
        @JsonProperty("repairCreateUsername")
        private String repairUsername;

        @ApiModelProperty(value = "返修时间")
        private LocalDateTime repairTime;

        @ApiModelProperty(value = "返修完成数量", required = true)
        @Min(value = 0, message = "返修成功数量必须大于等于0")
        private Integer completedQuantity;

        @ApiModelProperty(value = "批次码", required = true)
        @NotBlank(message = "批次码不能为空")
        private String batchCode;

        @ApiModelProperty(value = "原料批次码")
        private String materialBatchCode;

        @ApiModelProperty(value = "图片地址列表")
        @JsonProperty("fileCodeList")
        private List<String> imageCodes;
    }
}
