package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author yanjiawei
 * @date 2023年06月26日 18:28
 */
@Data
@ApiModel(value = "plm商品信息", description = "plm商品信息")
public class PlmCreateDto {

    @ApiModelProperty("plm商品sku")
    @NotBlank(message = "sku不能为空")
    private String sku;
}
