package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2022/12/28 10:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SpuDto extends ComPageDto {
    @NotBlank(message = "spu不能为空")
    @ApiModelProperty(value = "spu")
    private String spuCode;
}
