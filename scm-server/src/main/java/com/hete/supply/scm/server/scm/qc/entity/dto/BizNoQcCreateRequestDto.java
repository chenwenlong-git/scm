package com.hete.supply.scm.server.scm.qc.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.scm.api.scm.entity.enums.QcOrigin;
import com.hete.supply.scm.api.scm.entity.enums.QcOriginProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/4/16.
 */
@Data
@ApiModel(description = "业务单据创建质检单DTO")
public class BizNoQcCreateRequestDto {

    @NotBlank(message = "出库单号不能为空")
    @ApiModelProperty(value = "出库单号", example = "PO123456", required = true)
    private String outBoundNo;

    @JsonIgnore
    @ApiModelProperty(value = "平台编码")
    private String platCode;

    @NotNull(message = "质检类型不能为空")
    @ApiModelProperty(value = "质检类型", example = "PO123456", required = true)
    private QcOrigin origin;

    @NotNull(message = "质检标识不能为空")
    @ApiModelProperty(value = "质检标识", example = "PO123456", required = true)
    private QcOriginProperty qcOriginProperty;

    @NotBlank(message = "仓库编码不能为空")
    @ApiModelProperty(value = "仓库编码", example = "PO123456")
    private String warehouseCode;

    @NotEmpty(message = "质检商品信息不能为空")
    @ApiModelProperty(value = "质检商品信息", required = true)
    @Valid
    private List<CreateBizQcDetailDto> createBizQcDetailDtoList;

    @Data
    public static class CreateBizQcDetailDto {
        @ApiModelProperty(value = "容器编码", required = true, example = "C123456")
        @NotBlank(message = "容器编码不能为空")
        private String containerCode;

        @ApiModelProperty(value = "SKU", required = true, example = "SKU12345")
        @NotBlank(message = "SKU不能为空")
        private String sku;

        @ApiModelProperty(value = "批次码", required = true, example = "SKU12345")
        @NotBlank(message = "批次码不能为空")
        private String batchCode;

        @ApiModelProperty(value = "数量", required = true, example = "100")
        @NotNull(message = "数量不能为空")
        @Min(value = 1, message = "数量必须大于0")
        private Integer quantity;

        @ApiModelProperty(value = "供应商编码", example = "PO123456")
        private String supplierCode;
    }
}
