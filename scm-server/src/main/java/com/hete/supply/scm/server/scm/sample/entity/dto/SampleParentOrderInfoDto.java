package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2022/11/7 11:26
 */
@Data
@NoArgsConstructor
public class SampleParentOrderInfoDto {
    @ApiModelProperty(value = "属性key")
    @NotBlank(message = "属性key不能为空")
    @Length(max = 100, message = "属性key长度不能超过 100 位")
    private String sampleInfoKey;


    @ApiModelProperty(value = "属性值")
    @Length(max = 255, message = "属性值长度不能超过 255 位")
    private String sampleInfoValue;

}
