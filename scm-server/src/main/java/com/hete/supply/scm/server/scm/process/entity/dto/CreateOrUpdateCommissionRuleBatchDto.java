package com.hete.supply.scm.server.scm.process.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2023/12/14.
 */
@Data
public class CreateOrUpdateCommissionRuleBatchDto {

    @NotEmpty(message = "提成规则不能为空")
    @JsonProperty("configProcessCommissionRules")
    private List<@Valid CreateOrUpdateCommissionRuleDto> createOrUpdateProcessCommissionRuleList;
}
