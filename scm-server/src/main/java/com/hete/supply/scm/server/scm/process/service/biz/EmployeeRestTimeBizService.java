package com.hete.supply.scm.server.scm.process.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.entity.po.EmployeeRestTimePo;
import com.hete.supply.scm.server.scm.process.dao.EmployeeRestTimeDao;
import com.hete.supply.scm.server.scm.process.entity.bo.EmployeeRestTimeBo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * @date 2023年09月15日 14:39
 */
@Service
@RequiredArgsConstructor
public class EmployeeRestTimeBizService {

    private final EmployeeRestTimeDao employeeRestTimeDao;

    /**
     * 批量获取员工停工时间列表。
     *
     * @param employeeNos 员工编号集合，不能为空。
     * @return 包含员工停工时间信息的列表。
     * @throws IllegalArgumentException 如果 employeeNumbers 为 null 或为空集合。
     */
    public List<EmployeeRestTimeBo> getEmployeeRestTimes(Set<String> employeeNos) {
        // 判空校验
        if (CollectionUtils.isEmpty(employeeNos)) {
            return Collections.emptyList();
        }

        // 获取员工停工时间信息的逻辑，根据员工编号从数据库或其他数据源查询相关数据。
        List<EmployeeRestTimePo> employeeRestTimePos = employeeRestTimeDao.batchQueryEmployeeRestTime(employeeNos);
        return employeeRestTimePos.stream().map(employeeRestTimePo -> {
            EmployeeRestTimeBo bo = new EmployeeRestTimeBo();
            bo.setEmployeeNo(employeeRestTimePo.getEmployeeNo());
            bo.setEmployeeName(employeeRestTimePo.getEmployeeName());
            bo.setRestStartTime(employeeRestTimePo.getRestStartTime());
            bo.setRestEndTime(employeeRestTimePo.getRestEndTime());
            return bo;
        }).collect(Collectors.toList());
    }
}
