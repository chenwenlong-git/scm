package com.hete.supply.scm.server.supplier.converter;

import com.hete.supply.scm.server.supplier.entity.dto.ProcessSimpleDto;
import com.hete.supply.scm.server.supplier.entity.po.EmployeeGradePo;
import com.hete.supply.scm.server.supplier.entity.po.EmployeeGradeProcessPo;
import com.hete.supply.scm.server.supplier.entity.vo.EmployeeGradeSearchVo;
import com.hete.supply.scm.server.supplier.entity.vo.ProcessSimpleVo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/7/25 00:08
 */
public class SupplierEmployeeConverter {
    public static List<EmployeeGradeSearchVo> employeeGradePoToSearchVo(List<EmployeeGradePo> employeeGradePoList) {
        return Optional.ofNullable(employeeGradePoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(po -> {
                    final EmployeeGradeSearchVo employeeGradeSearchVo = new EmployeeGradeSearchVo();
                    employeeGradeSearchVo.setEmployeeGradeId(po.getEmployeeGradeId());
                    employeeGradeSearchVo.setVersion(po.getVersion());
                    employeeGradeSearchVo.setGradeType(po.getGradeType());
                    employeeGradeSearchVo.setGradeName(po.getGradeName());
                    employeeGradeSearchVo.setGradeLevel(po.getGradeLevel());

                    return employeeGradeSearchVo;
                }).collect(Collectors.toList());
    }

    public static List<ProcessSimpleVo> employeeGradeProcessPoToVo(List<EmployeeGradeProcessPo> employeeGradeProcessPoList) {
        return employeeGradeProcessPoList.stream().map(po -> {
            final ProcessSimpleVo processSimpleVo = new ProcessSimpleVo();
            processSimpleVo.setProcessId(po.getProcessId());
            processSimpleVo.setProcessSecondName(po.getProcessName());
            processSimpleVo.setProcessNum(po.getProcessNum());
            return processSimpleVo;
        }).collect(Collectors.toList());
    }

    public static List<EmployeeGradeProcessPo> processSimpleDtoToPo(Long employeeGradeId, List<ProcessSimpleDto> processSimpleList) {
        return processSimpleList.stream()
                .map(processSimpleDto -> {
                    final EmployeeGradeProcessPo employeeGradeProcessPo = new EmployeeGradeProcessPo();
                    employeeGradeProcessPo.setEmployeeGradeId(employeeGradeId);
                    employeeGradeProcessPo.setProcessId(processSimpleDto.getProcessId());
                    employeeGradeProcessPo.setProcessName(processSimpleDto.getProcessSecondName());
                    employeeGradeProcessPo.setProcessNum(processSimpleDto.getProcessNum());
                    return employeeGradeProcessPo;
                }).collect(Collectors.toList());
    }
}
