package com.hete.supply.scm.server.scm.process.dao;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.process.entity.bo.EmployeePlanCountBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessOrderPlanQueryBo;
import com.hete.supply.scm.server.scm.process.entity.dto.GetProcessProcedureEmployeePlanDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderPlanQueryDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessProcedureEmployeePlanPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderPlanVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
@Mapper
interface ProcessProcedureEmployeePlanMapper extends BaseDataMapper<ProcessProcedureEmployeePlanPo> {

    List<EmployeePlanCountBo> getEmployeePlanCountBo(@Param("employeeNos") Set<String> employeeNos);

    IPage<ProcessOrderPlanVo> getByPage(Page<Void> page,
                                        @Param("params") ProcessOrderPlanQueryDto processOrderPlanQueryDto,
                                        @Param("bo") ProcessOrderPlanQueryBo processOrderPlanQueryBo,
                                        LocalDateTime defaultLocalDateTime);

    List<ProcessProcedureEmployeePlanPo> getEmployeePlansByCreateTimeAndDefaultTime(LocalDate today, LocalDate defaultDate);

    List<ProcessProcedureEmployeePlanPo> getProcessProcedureEmployeePlans(@Param("params") List<GetProcessProcedureEmployeePlanDto> dtoList);

    List<ProcessProcedureEmployeePlanPo> getSchedulingPlansByPoolIdAndSchedulingDate(String poolCode, TreeSet<LocalDate> processPlanDates);

    List<ProcessProcedureEmployeePlanPo> getSchedulingPlansByPoolIdAndEmployeeIdAndSchedulingDate(String poolCode,
                                                                                                  Set<String> employeeNos,
                                                                                                  TreeSet<LocalDate> processPlanDates);

    List<ProcessProcedureEmployeePlanPo> getDistinctByProcessOrderNos(List<String> processOrderNos);
}
