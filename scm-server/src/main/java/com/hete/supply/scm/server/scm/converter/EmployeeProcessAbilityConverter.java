package com.hete.supply.scm.server.scm.converter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.entity.po.EmployeeProcessAbilityPo;
import com.hete.supply.scm.server.scm.process.entity.bo.EmployeeProcessAbilityBo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * @date 2023年07月30日 17:40
 */
public class EmployeeProcessAbilityConverter {

    public static List<EmployeeProcessAbilityPo> convertToPo(List<EmployeeProcessAbilityBo> employeeProcessAbilityBos) {
        if (CollectionUtils.isEmpty(employeeProcessAbilityBos)) {
            return Collections.emptyList();
        }
        return employeeProcessAbilityBos.stream().map(employeeProcessAbilityBo -> {
            EmployeeProcessAbilityPo employeeProcessAbilityPo = new EmployeeProcessAbilityPo();
            employeeProcessAbilityPo.setEmployeeProcessAbilityId(employeeProcessAbilityBo.getEmployeeProcessAbilityId());
            employeeProcessAbilityPo.setAvailableProcessedNum(employeeProcessAbilityBo.getAvailableProcessedNum());
            employeeProcessAbilityPo.setVersion(employeeProcessAbilityBo.getVersion());
            return employeeProcessAbilityPo;
        }).collect(Collectors.toList());
    }

}
