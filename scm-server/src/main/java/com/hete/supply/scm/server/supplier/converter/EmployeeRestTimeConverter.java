package com.hete.supply.scm.server.supplier.converter;

import com.hete.supply.scm.server.scm.entity.po.EmployeeRestTimePo;
import com.hete.supply.scm.server.scm.process.entity.dto.EmployeeRestTimeDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * @date 2023年09月15日 16:18
 */
public class EmployeeRestTimeConverter {

    /**
     * 将员工休息时间DTO列表转换为PO列表。
     *
     * @param dtoList      员工休息时间DTO列表
     * @param employeeNo   员工编号
     * @param employeeName 员工名称
     * @return 员工休息时间PO列表
     */
    public static List<EmployeeRestTimePo> convertToPoList(List<EmployeeRestTimeDto> dtoList,
                                                           String employeeNo, String employeeName) {
        return dtoList.stream()
                .map(dto -> convertToPo(dto, employeeNo, employeeName))
                .collect(Collectors.toList());
    }

    /**
     * 将单个员工休息时间DTO转换为PO。
     *
     * @param dto          员工休息时间DTO
     * @param employeeNo   员工编号
     * @param employeeName 员工名称
     * @return 员工休息时间PO
     */
    private static EmployeeRestTimePo convertToPo(EmployeeRestTimeDto dto,
                                                  String employeeNo, String employeeName) {
        EmployeeRestTimePo po = new EmployeeRestTimePo();
        po.setEmployeeNo(employeeNo);
        po.setEmployeeName(employeeName);
        po.setRestStartTime(dto.getRestStartTime());
        po.setRestEndTime(dto.getRestEndTime());
        return po;
    }
}
