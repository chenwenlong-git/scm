package com.hete.supply.scm.server.supplier.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.supplier.entity.po.EmployeeGradeProcessPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 员工职级工序能力关系表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-24
 */
@Component
@Validated
public class EmployeeGradeProcessDao extends BaseDao<EmployeeGradeProcessMapper, EmployeeGradeProcessPo> {

    public List<EmployeeGradeProcessPo> getListByEmployeeGradeIdList(List<Long> employeeGradeIdList) {
        if (CollectionUtils.isEmpty(employeeGradeIdList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<EmployeeGradeProcessPo>lambdaQuery()
                .in(EmployeeGradeProcessPo::getEmployeeGradeId, employeeGradeIdList));
    }

    public void removeByEmployeeGradeId(Long employeeGradeId) {
        baseMapper.deleteSkipCheck(Wrappers.<EmployeeGradeProcessPo>lambdaUpdate()
                .eq(EmployeeGradeProcessPo::getEmployeeGradeId, employeeGradeId));
    }
}
