package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yanjiawei
 * @date 2023年07月03日 13:52
 */
@Data
public class MaterialBackVo {
    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "可归还数量,当不存在时，可归还数为0")
    private Integer availableBackNum;
}
