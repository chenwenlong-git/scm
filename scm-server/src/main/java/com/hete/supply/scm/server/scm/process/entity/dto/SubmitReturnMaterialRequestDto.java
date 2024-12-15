package com.hete.supply.scm.server.scm.process.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/2/4.
 */
@Data
@ApiModel(value = "提交原料归还请求DTO")
public class SubmitReturnMaterialRequestDto {

    @ApiModelProperty(value = "返修单号", example = "R123456")
    @NotBlank(message = "返修单号不能为空")
    private String repairOrderNo;

    @ApiModelProperty(value = "归还仓库编码", example = "WH001")
    @NotBlank(message = "归还仓库编码不能为空")
    private String returnWarehouseCode;

    @ApiModelProperty(value = "原料归还信息列表")
    private List<@Valid MaterialReturnInfoVo> materialReturnInfoList;

    @Data
    public static class MaterialReturnInfoVo {
        @ApiModelProperty(value = "SKU编码", example = "SKU123")
        private String sku;

        @ApiModelProperty(value = "原料批次码", example = "BATCH789")
        @NotBlank(message = "归还原料批次码不能为空")
        private String batchCode;

        @ApiModelProperty(value = "归还数量")
        @JsonProperty("returnableQuantity")
        @Min(value = 1, message = "归还数量必须大于0")
        private Integer returnQuantity;
    }
}
