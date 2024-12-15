package com.hete.supply.scm.server.supplier.api.task;

import com.hete.supply.scm.server.supplier.service.biz.EmployeeBizService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author weiwenxin
 * @date 2023/7/31 11:14
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class EmployeeJob {
    private final EmployeeBizService employeeBizService;


    @XxlJob(value = "employeeChangeTask")
    public void employeeChangeTask() {
        employeeBizService.employeeChangeTask();
    }
}
