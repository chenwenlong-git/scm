package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.entity.po.EmployeeRestTimePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 员工休息时间表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-09-15
 */
@Component
@Validated
public class EmployeeRestTimeDao extends BaseDao<EmployeeRestTimeMapper, EmployeeRestTimePo> {

    /**
     * 批量查询员工停工时间信息。
     *
     * @param employeeNos 员工编号集合
     * @return 包含员工停工时间信息的列表
     */
    public List<EmployeeRestTimePo> batchQueryEmployeeRestTime(Set<String> employeeNos) {
        if (CollectionUtils.isEmpty(employeeNos)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<EmployeeRestTimePo>lambdaQuery()
                .in(EmployeeRestTimePo::getEmployeeNo, employeeNos));
    }

    public void removeByEmployeeNo(String employeeNo) {
        baseMapper.deleteSkipCheck(Wrappers.<EmployeeRestTimePo>lambdaUpdate()
                .eq(EmployeeRestTimePo::getEmployeeNo, employeeNo));
    }
}
