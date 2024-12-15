package com.hete.supply.scm.server.scm.process.service.biz;

import com.hete.supply.scm.server.scm.process.entity.dto.ProcessEmployeeQueryRequestDto;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessEmployeeVo;
import com.hete.supply.scm.server.supplier.dao.EmployeeGradeRelationDao;
import com.hete.support.api.result.CommonPageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author yanjiawei
 * Created on 2024/1/11.
 */
@Service
@RequiredArgsConstructor
public class ProcessEmployeeBizService {

    private final EmployeeGradeRelationDao employeeGradeRelationDao;

    public CommonPageResult.PageInfo<ProcessEmployeeVo> getByPage(ProcessEmployeeQueryRequestDto request) {
        return employeeGradeRelationDao.getByPage(request);
    }
}
