package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ProcessFirst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "加工描述创建参数", description = "加工描述创建参数")
public class ProcessDescCreateDto {

    @ApiModelProperty("加工描述")
    @NotBlank(message = "加工描述不能为空")
    @Length(max = 32, message = "描述字符长度不能超过 32 位")
    private String name;

    @ApiModelProperty("工序环节")
    @NotNull(message = "一级工序不能为空")
    private ProcessFirst processFirst;

    @ApiModelProperty("描述值")
    @NotEmpty(message = "描述值不能为空")
    private List<@NotBlank(message = "描述值不能为空") String> values;


}
