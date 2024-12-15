package com.hete.supply.scm.server.scm.qc.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 获取质检规格书
 *
 * @author yanjiawei
 * Created on 2024/10/28.
 */
@Data
public class GetQcSpecBookDto {
    @NotBlank(message = "供应商编码不能为空")
    @ApiModelProperty("供应商编码")
    private String supplierCode;

    @NotEmpty(message = "sku编码列表不能为空")
    @ApiModelProperty("sku编码列表")
    private List<String> skuList;
}
