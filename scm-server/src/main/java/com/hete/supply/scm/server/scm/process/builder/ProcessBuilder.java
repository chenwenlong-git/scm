package com.hete.supply.scm.server.scm.process.builder;

import cn.hutool.core.collection.CollectionUtil;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessCompositePo;
import com.hete.supply.scm.server.scm.process.entity.dto.CompoundProcessCreateDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessVo;
import com.hete.supply.scm.server.scm.process.enums.ProcessStatus;
import com.hete.supply.scm.server.scm.process.enums.ProcessType;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/2/26.
 */
public class ProcessBuilder {
    public static List<ProcessCompositePo> buildCompositeProcesses(Long compoundProcessId,
                                                                   String compoundProcessCode,
                                                                   List<String> independentProcessCodes,
                                                                   List<ProcessPo> independentProcessPos) {
        if (CollectionUtil.isEmpty(independentProcessPos)) {
            return Collections.emptyList();
        }


        return independentProcessCodes.stream()
                .map(independentProcessCode -> {
                    ProcessCompositePo processCompositePo = new ProcessCompositePo();
                    processCompositePo.setParentProcessId(compoundProcessId);
                    processCompositePo.setParentProcessCode(compoundProcessCode);

                    ProcessPo matchProcess = ParamValidUtils.requireNotNull(independentProcessPos.stream()
                                    .filter(independentProcessPo -> Objects.equals(
                                            independentProcessCode,
                                            independentProcessPo.getProcessCode()))
                                    .findFirst()
                                    .orElse(null),
                            "操作失败！非组合工序编码不存在，请先创建非组合工序信息，然后再维护与之相关的组合工序。");
                    processCompositePo.setSubProcessId(matchProcess.getProcessId());
                    processCompositePo.setSubProcessCode(matchProcess.getProcessCode());
                    return processCompositePo;
                })
                .collect(Collectors.toList());

    }

    public static ProcessPo buildCompoundProcessPo(CompoundProcessCreateDto dto,
                                                   String formatSecondCode,
                                                   ProcessType processType) {
        ProcessPo processPo = new ProcessPo();
        processPo.setProcessSecondCode(formatSecondCode);
        processPo.setProcessCode(dto.getProcessFirst()
                .getCode() + formatSecondCode);

        processPo.setProcessFirst(dto.getProcessFirst());
        processPo.setProcessLabel(dto.getProcessLabel());
        processPo.setProcessSecondName(dto.getProcessSecondName());
        processPo.setProcessName(dto.getProcessFirst()
                .getDesc() + "-" + dto.getProcessSecondName());
        processPo.setProcessStatus(ProcessStatus.ENABLED);
        processPo.setComplexCoefficient(dto.getComplexCoefficient());
        processPo.setSetupDuration(dto.getSetupDuration());
        processPo.setProcessType(processType);
        return processPo;
    }

    public static List<ProcessVo.IndependentProcessVo> buildIndependentProcessVos(List<ProcessPo> matchIndependentProcessPos) {
        if (CollectionUtil.isEmpty(matchIndependentProcessPos)) {
            return Collections.emptyList();
        }
        return matchIndependentProcessPos.stream()
                .map(matchIndependentProcessPo -> {
                    ProcessVo.IndependentProcessVo vo = new ProcessVo.IndependentProcessVo();
                    vo.setProcessCode(matchIndependentProcessPo.getProcessCode());
                    vo.setProcessLabel(matchIndependentProcessPo.getProcessLabel());
                    vo.setProcessSecondName(matchIndependentProcessPo.getProcessSecondName());
                    return vo;
                })
                .collect(Collectors.toList());
    }
}
