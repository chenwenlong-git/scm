package com.hete.supply.scm.server.scm.process.dao;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.enums.DefaultDatabaseTime;
import com.hete.supply.scm.server.scm.process.entity.bo.EmployeePlanCountBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessOrderPlanQueryBo;
import com.hete.supply.scm.server.scm.process.entity.dto.GetProcessProcedureEmployeePlanDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderPlanQueryDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessProcedureEmployeePlanPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderPlanVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p>
 * 工序人员排产计划表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-28
 */
@Component
@Validated
public class ProcessProcedureEmployeePlanDao extends BaseDao<ProcessProcedureEmployeePlanMapper, ProcessProcedureEmployeePlanPo> {

    public List<EmployeePlanCountBo> getEmployeePlanCount(Set<String> employeeNos) {
        return baseMapper.getEmployeePlanCountBo(employeeNos);
    }

    public CommonPageResult.PageInfo<ProcessOrderPlanVo> getByPage(Page<Void> page, ProcessOrderPlanQueryDto processOrderPlanQueryDto, ProcessOrderPlanQueryBo processOrderPlanQueryBo) {
        IPage<ProcessOrderPlanVo> pageResult = baseMapper.getByPage(page, processOrderPlanQueryDto, processOrderPlanQueryBo, DefaultDatabaseTime.DEFAULT_TIME.getDateTime());
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public List<ProcessProcedureEmployeePlanPo> getCheckPlans(LocalDate today) {
        return baseMapper.getEmployeePlansByCreateTimeAndDefaultTime(today, DefaultDatabaseTime.DEFAULT_TIME.getDateTime().toLocalDate());
    }

    public List<ProcessProcedureEmployeePlanPo> getEmployeeProcessPlanByEmployeeNoAndStartTime(Set<String> employeeNos, LocalDateTime planDateTime) {
        return list(Wrappers.<ProcessProcedureEmployeePlanPo>lambdaQuery()
                .in(ProcessProcedureEmployeePlanPo::getEmployeeNo, employeeNos)
                .ge(ProcessProcedureEmployeePlanPo::getExpectBeginTime, planDateTime.withHour(0).withMinute(0).withSecond(0))
                .le(ProcessProcedureEmployeePlanPo::getExpectBeginTime, planDateTime.withHour(23).withMinute(59).withSecond(59))
                .orderByAsc(ProcessProcedureEmployeePlanPo::getExpectBeginTime));
    }

    public List<ProcessProcedureEmployeePlanPo> getByProcessOrderNo(String processOrderNo) {
        return list(Wrappers.<ProcessProcedureEmployeePlanPo>lambdaQuery().eq(ProcessProcedureEmployeePlanPo::getProcessOrderNo, processOrderNo));
    }

    public ProcessProcedureEmployeePlanPo getByProcessOrderProcedureId(Long processOrderProcedureId) {
        return getOne(Wrappers.<ProcessProcedureEmployeePlanPo>lambdaQuery().eq(ProcessProcedureEmployeePlanPo::getProcessOrderProcedureId, processOrderProcedureId));
    }


    public List<ProcessProcedureEmployeePlanPo> getByProcessOrderProcedureIds(Set<Long> processOrderProcedureIds) {
        return list(Wrappers.<ProcessProcedureEmployeePlanPo>lambdaQuery()
                .in(ProcessProcedureEmployeePlanPo::getProcessOrderProcedureId, processOrderProcedureIds)
                .orderByAsc(ProcessProcedureEmployeePlanPo::getExpectBeginTime));
    }

    public List<ProcessProcedureEmployeePlanPo> getByIds(List<Long> ids) {
        return list(Wrappers.<ProcessProcedureEmployeePlanPo>lambdaQuery().in(ProcessProcedureEmployeePlanPo::getProcessProcedureEmployeePlanId, ids));
    }

    public List<ProcessProcedureEmployeePlanPo> getProcessProcedureEmployeePlanPos(List<GetProcessProcedureEmployeePlanDto> dtoList) {
        return baseMapper.getProcessProcedureEmployeePlans(dtoList);
    }

    /**
     * 根据产能池编号和排产日期集合获取加工工序员工排产计划列表
     *
     * @param poolCode         产能池编号
     * @param processPlanDates 排产日期集合
     * @return 加工工序员工排产计划列表
     */
    public List<ProcessProcedureEmployeePlanPo> getProcessProcedureEmployeePlanPos(String poolCode, TreeSet<LocalDate> processPlanDates) {
        return baseMapper.getSchedulingPlansByPoolIdAndSchedulingDate(poolCode, processPlanDates);
    }

    /**
     * 根据产能池编号、员工编号集合和排产日期集合获取加工工序员工排产计划列表
     *
     * @param poolCode         产能池编号
     * @param employeeNos      员工编号集合
     * @param processPlanDates 排产日期集合
     * @return 加工工序员工排产计划列表
     */
    public List<ProcessProcedureEmployeePlanPo> getProcessProcedureEmployeePlanPos(String poolCode, Set<String> employeeNos, TreeSet<LocalDate> processPlanDates) {
        // 如果员工编号集合为空，直接返回空列表
        if (CollectionUtils.isEmpty(employeeNos)) {
            return Collections.emptyList();
        }

        // 根据产能池编码、员工编号集合、工序计划日期集合从数据库中查询工序人员排产计划信息
        return baseMapper.getSchedulingPlansByPoolIdAndEmployeeIdAndSchedulingDate(poolCode, employeeNos, processPlanDates);
    }

    /**
     * 根据工序单号列表获取不重复的流程员工计划信息列表。
     *
     * @param processOrderNos 工序单号列表，不能为空。
     * @return 不重复的流程员工计划信息列表，如果输入的工序单号列表为空，则返回空列表。
     */
    public List<ProcessProcedureEmployeePlanPo> getDistinctByProcessOrderNos(List<String> processOrderNos) {
        // 如果工序单号列表为空，直接返回空列表
        if (CollectionUtils.isEmpty(processOrderNos)) {
            return Collections.emptyList();
        }

        // 使用LambdaQuery构建查询条件，查询流程员工计划信息
        return baseMapper.getDistinctByProcessOrderNos(processOrderNos);
    }

}
