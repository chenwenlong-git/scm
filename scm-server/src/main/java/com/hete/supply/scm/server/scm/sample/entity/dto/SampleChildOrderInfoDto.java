package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2022/12/5 16:57
 */
@Data
@NoArgsConstructor
public class SampleChildOrderInfoDto {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "key")
    @NotBlank(message = "key不能为空")
    @Length(max = 100, message = "属性key长度不能超过 100 位")
    private String sampleInfoKey;

    @ApiModelProperty(value = "value")
    @NotBlank(message = "value不能为空")
    private String sampleInfoValue;
}
