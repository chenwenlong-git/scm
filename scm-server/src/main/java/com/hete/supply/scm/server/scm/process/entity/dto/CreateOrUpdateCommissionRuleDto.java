package com.hete.supply.scm.server.scm.process.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2023/12/14.
 */
@Data
public class CreateOrUpdateCommissionRuleDto {

    @NotBlank(message = "工序编码不能为空")
    @ApiModelProperty(value = "工序编码")
    private String processCode;

    @ApiModelProperty(value = "工序规则")
    @JsonProperty(value = "processCommissionRules")
    @NotEmpty(message = "工序规则不能为空")
    private List<@Valid ProcessCommissionRuleDto> processCommissionRuleDtoList;
}
