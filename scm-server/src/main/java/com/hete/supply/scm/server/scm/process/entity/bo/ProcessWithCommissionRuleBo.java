package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.TreeSet;

/**
 * @author yanjiawei
 * Created on 2023/12/14.
 */
@Data
@ApiModel(description = "包含工序信息和工序提成规则信息的BO")
public class ProcessWithCommissionRuleBo {
    private String processCode;
    private ProcessBo process;
    private TreeSet<ProcessCommissionRuleBo> rules = new TreeSet<>();
}
