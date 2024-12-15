package com.hete.supply.scm.server.scm.sample.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/2 22:04
 */
@Data
@NoArgsConstructor
public class SampleChildNoDto {
    @NotBlank(message = "样品采购子单号不能为空")
    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @JsonIgnore
    @ApiModelProperty(value = "样品单状态")
    private List<SampleOrderStatus> sampleOrderStatusList;

    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCode;
}
