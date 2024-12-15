package com.hete.supply.scm.server.scm.process.dao;


import com.hete.supply.scm.server.scm.entity.po.EmployeeProcessAbilityPo;
import com.hete.supply.scm.server.scm.process.entity.bo.EmployeeProcessAbilityBo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p>
 * 人员工序产能信息表(产能池) 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-07-27
 */
@Component
@Validated
public class EmployeeProcessAbilityDao extends BaseDao<EmployeeProcessAbilityMapper, EmployeeProcessAbilityPo> {

    /**
     * 根据员工编号集合获取员工加工能力信息列表
     *
     * @param employeeNos 员工编号集合
     * @return 员工加工能力信息列表
     */
    public List<EmployeeProcessAbilityPo> getEmployeeProcessAbilityPoList(Set<String> employeeNos) {
        return baseMapper.getEmployeeProcessAbilityByEmployeeNos(employeeNos);
    }

    /**
     * 根据产能池编号、工序编号集合和排产日期集合获取员工加工能力信息列表
     *
     * @param productionPoolCode 产能池编号
     * @param processIds         工序编号集合
     * @param processPlanDates   排产日期集合
     * @return 员工加工能力信息列表
     */
    public List<EmployeeProcessAbilityPo> getEmployeeProcessAbilityPoList(String productionPoolCode, Set<Long> processIds,
                                                                          TreeSet<LocalDate> processPlanDates) {
        return baseMapper.getEmployeeProcessAbilityByPoolIdAndProcessIdAndSchedulingDate(productionPoolCode, processIds, processPlanDates);
    }

    /**
     * 根据产能池编号、员工编号集合和排产日期集合获取员工加工能力信息列表
     *
     * @param productionPoolCode 产能池编号
     * @param employeeNos        员工编号集合
     * @param processPlanDates   排产日期集合
     * @return 员工加工能力信息列表
     */
    public List<EmployeeProcessAbilityBo> getEmployeeProcessAbilityBoList(String productionPoolCode, Set<String> employeeNos,
                                                                          TreeSet<LocalDate> processPlanDates) {
        return baseMapper.getEmployeeProcessAbilityByPoolIdAndEmployeeIdAndSchedulingDate(productionPoolCode, employeeNos, processPlanDates);
    }

    /**
     * 根据产能池编号和排产日期集合获取员工加工能力信息列表
     *
     * @param poolCode         产能池编号
     * @param processPlanDates 排产日期集合
     * @return 员工加工能力信息列表
     */
    public List<EmployeeProcessAbilityPo> getEmployeeProcessAbilityBoList(String poolCode, TreeSet<LocalDate> processPlanDates) {
        return baseMapper.getEmployeeProcessAbilityByPoolAndDate(poolCode, processPlanDates);
    }


}
