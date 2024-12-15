package com.hete.supply.scm.server.scm.process.converter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.hete.supply.scm.server.scm.process.entity.bo.EmployeePlanTimeBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessProcedureEmployeePlanBo;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessProcedureEmployeePlanDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessProcedureEmployeePlanPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessProcedureEmployeePlanVo;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * @date 2023年07月30日 18:32
 */
public class ProcessProcedureEmployeePlanConverter {

    public static List<ProcessProcedureEmployeePlanPo> bosToPos(List<ProcessProcedureEmployeePlanBo> processProcedureEmployeePlanBos) {
        if (CollectionUtils.isEmpty(processProcedureEmployeePlanBos)) {
            return Collections.emptyList();
        }

        return processProcedureEmployeePlanBos.stream().map(processProcedureEmployeePlanBo -> {
            ProcessProcedureEmployeePlanPo processProcedureEmployeePlanPo = new ProcessProcedureEmployeePlanPo();
            processProcedureEmployeePlanPo.setProductionPoolCode(processProcedureEmployeePlanBo.getProductionPoolCode());
            processProcedureEmployeePlanPo.setProcessOrderNo(processProcedureEmployeePlanBo.getProcessOrderNo());
            processProcedureEmployeePlanPo.setProcessOrderProcedureId(processProcedureEmployeePlanBo.getProcessOrderProcedureId());
            processProcedureEmployeePlanPo.setProcessId(processProcedureEmployeePlanBo.getProcessId());
            processProcedureEmployeePlanPo.setProcessName(processProcedureEmployeePlanBo.getProcessName());
            processProcedureEmployeePlanPo.setEmployeeNo(processProcedureEmployeePlanBo.getEmployeeNo());
            processProcedureEmployeePlanPo.setEmployeeName(processProcedureEmployeePlanBo.getEmployeeName());
            processProcedureEmployeePlanPo.setProcessNum(processProcedureEmployeePlanBo.getProcessNum());
            processProcedureEmployeePlanPo.setCommission(processProcedureEmployeePlanBo.getCommission());
            processProcedureEmployeePlanPo.setExpectBeginTime(processProcedureEmployeePlanBo.getExpectBeginDateTime());
            processProcedureEmployeePlanPo.setExpectEndTime(processProcedureEmployeePlanBo.getExpectEndDateTime());
            return processProcedureEmployeePlanPo;
        }).collect(Collectors.toList());
    }

    public static List<ProcessProcedureEmployeePlanBo> toBos(List<ProcessProcedureEmployeePlanPo> processProcedureEmployeePlanPos) {
        if (CollectionUtils.isEmpty(processProcedureEmployeePlanPos)) {
            return Lists.newArrayList();
        }
        return processProcedureEmployeePlanPos.stream().map(processProcedureEmployeePlanPo -> {
            ProcessProcedureEmployeePlanBo processPlanRelateInfoBo = new ProcessProcedureEmployeePlanBo();
            processPlanRelateInfoBo.setProcessProcedureEmployeePlanId(processProcedureEmployeePlanPo.getProcessProcedureEmployeePlanId());
            processPlanRelateInfoBo.setProductionPoolCode(processProcedureEmployeePlanPo.getProductionPoolCode());
            processPlanRelateInfoBo.setProcessOrderProcedureId(processProcedureEmployeePlanPo.getProcessOrderProcedureId());
            processPlanRelateInfoBo.setProcessOrderNo(processProcedureEmployeePlanPo.getProcessOrderNo());
            processPlanRelateInfoBo.setEmployeeNo(processProcedureEmployeePlanPo.getEmployeeNo());
            processPlanRelateInfoBo.setEmployeeName(processProcedureEmployeePlanPo.getEmployeeName());
            processPlanRelateInfoBo.setProcessId(processProcedureEmployeePlanPo.getProcessId());
            processPlanRelateInfoBo.setProcessName(processProcedureEmployeePlanPo.getProcessName());
            processPlanRelateInfoBo.setProcessNum(processProcedureEmployeePlanPo.getProcessNum());
            processPlanRelateInfoBo.setExpectBeginDateTime(processProcedureEmployeePlanPo.getExpectBeginTime());
            processPlanRelateInfoBo.setExpectEndDateTime(processProcedureEmployeePlanPo.getExpectEndTime());
            return processPlanRelateInfoBo;
        }).collect(Collectors.toList());
    }

    public static ProcessProcedureEmployeePlanBo toBo(String productionPoolCode, String processOrderNo,
                                                      Long processOrderProcedureId, Long processId,
                                                      String processName, Integer processNum, BigDecimal commission,
                                                      EmployeePlanTimeBo employeePlanTimeBo) {
        if (Objects.isNull(employeePlanTimeBo)) {
            return null;
        }
        ProcessProcedureEmployeePlanBo processEmployeePlanTimeBo = new ProcessProcedureEmployeePlanBo();
        processEmployeePlanTimeBo.setProductionPoolCode(productionPoolCode);
        processEmployeePlanTimeBo.setProcessOrderNo(processOrderNo);
        processEmployeePlanTimeBo.setProcessOrderProcedureId(processOrderProcedureId);
        processEmployeePlanTimeBo.setProcessId(processId);
        processEmployeePlanTimeBo.setProcessName(processName);
        processEmployeePlanTimeBo.setEmployeeNo(employeePlanTimeBo.getEmployeeNo());
        processEmployeePlanTimeBo.setEmployeeName(employeePlanTimeBo.getEmployeeName());
        processEmployeePlanTimeBo.setProcessNum(processNum);
        processEmployeePlanTimeBo.setCommission(commission);
        processEmployeePlanTimeBo.setExpectBeginDateTime(employeePlanTimeBo.getExpectBeginDateTime());
        processEmployeePlanTimeBo.setExpectEndDateTime(employeePlanTimeBo.getExpectEndDateTime());
        return processEmployeePlanTimeBo;
    }

    public static List<ProcessProcedureEmployeePlanPo> dtoToPos(List<ProcessProcedureEmployeePlanDto> processProcedureEmployeePlanDtoList) {
        if (CollectionUtils.isEmpty(processProcedureEmployeePlanDtoList)) {
            return Collections.emptyList();
        }

        return processProcedureEmployeePlanDtoList.stream().map(processProcedureEmployeePlanDto -> {
            ProcessProcedureEmployeePlanPo processProcedureEmployeePlanPo = new ProcessProcedureEmployeePlanPo();
            // 设置工序ID，从 DTO 中获取并赋值给 processProcedureEmployeePlanPo
            processProcedureEmployeePlanPo.setProcessId(processProcedureEmployeePlanDto.getProcessId());
            // 设置工序名称，从 DTO 中获取并赋值给 processProcedureEmployeePlanPo
            processProcedureEmployeePlanPo.setProcessName(processProcedureEmployeePlanDto.getProcessSecondName());
            // 设置员工编号，从 DTO 中获取并赋值给 processProcedureEmployeePlanPo
            processProcedureEmployeePlanPo.setEmployeeNo(processProcedureEmployeePlanDto.getEmployeeNo());
            // 设置员工姓名，从 DTO 中获取并赋值给 processProcedureEmployeePlanPo
            processProcedureEmployeePlanPo.setEmployeeName(processProcedureEmployeePlanDto.getEmployeeName());
            // 设置加工单工序表主键 ID，从 DTO 中获取并赋值给 processProcedureEmployeePlanPo
            processProcedureEmployeePlanPo.setProcessOrderProcedureId(processProcedureEmployeePlanDto.getProcessOrderProcedureId());
            // 设置预计开始时间，从 DTO 中获取并赋值给 processProcedureEmployeePlanPo
            processProcedureEmployeePlanPo.setExpectBeginTime(processProcedureEmployeePlanDto.getExpectBeginDateTime());
            // 设置预计结束时间，从 DTO 中获取并赋值给 processProcedureEmployeePlanPo
            processProcedureEmployeePlanPo.setExpectEndTime(processProcedureEmployeePlanDto.getExpectEndDateTime());
            return processProcedureEmployeePlanPo;
        }).collect(Collectors.toList());
    }

    public static List<ProcessProcedureEmployeePlanVo> posToVos(List<ProcessProcedureEmployeePlanPo> employeePlanPos) {
        if (CollectionUtils.isEmpty(employeePlanPos)) {
            return Collections.emptyList();
        }
        return employeePlanPos.stream().map(po -> {
            ProcessProcedureEmployeePlanVo vo = new ProcessProcedureEmployeePlanVo();
            vo.setProcessProcedureEmployeePlanId(po.getProcessProcedureEmployeePlanId());
            vo.setEmployeeNo(po.getEmployeeNo());
            vo.setEmployeeName(po.getEmployeeName());
            vo.setExpectBeginDateTime(po.getExpectBeginTime());
            vo.setExpectEndDateTime(po.getExpectEndTime());
            return vo;
        }).collect(Collectors.toList());
    }
}
