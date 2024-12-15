package com.hete.supply.scm.server.scm.process.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/1/3.
 */
@Data
@ApiModel(description = "返修单提交质检结果参数DTO")
public class InspectionResultRequestDto {

    @ApiModelProperty(value = "返修单号", required = true)
    @NotBlank(message = "质检单号不能为空")
    private String repairOrderNo;

    @NotNull(message = "版本号不能为空")
    @ApiModelProperty(value = "版本号", required = true)
    private Integer version;

    @ApiModelProperty(value = "质检单明细信息列表", required = true)
    @NotEmpty(message = "质检单明细信息列表不能为空")
    @JsonProperty("repairOrderResultDetailList")
    private List<InspectionDetailRequestDto> inspectionDetails;

    @Data
    @ApiModel(description = "质检单明细信息DTO")
    public static class InspectionDetailRequestDto {

        @NotNull(message = "返修明细结果ID不能为空")
        @ApiModelProperty(value = "返修明细结果ID", required = true)
        private Long repairOrderResultId;

        @NotNull(message = "正品数不能为空")
        @ApiModelProperty(value = "正品数量", required = true)
        @JsonProperty("qcPassQuantity")
        private Integer goodQuantity;

        @NotNull(message = "次品数不能为空")
        @ApiModelProperty(value = "次品数量", required = true)
        @JsonProperty("qcFailQuantity")
        private Integer defectiveQuantity;

        @NotNull(message = "版本号不能为空")
        @ApiModelProperty(value = "版本号", required = true)
        private Integer version;
    }
}
