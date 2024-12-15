package com.hete.supply.scm.server.scm.process.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.enums.ProcessFirst;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.common.util.StringUtil;
import com.hete.supply.scm.server.scm.process.builder.ProcessBuilder;
import com.hete.supply.scm.server.scm.process.converter.ProcessCommissionRuleConverter;
import com.hete.supply.scm.server.scm.process.converter.ProcessConverter;
import com.hete.supply.scm.server.scm.process.dao.ProcessCompositeDao;
import com.hete.supply.scm.server.scm.process.dao.ProcessDao;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessWithCommissionRuleBo;
import com.hete.supply.scm.server.scm.process.entity.dto.*;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessCommissionRuleVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessVo;
import com.hete.supply.scm.server.scm.process.enums.ProcessStatus;
import com.hete.supply.scm.server.scm.process.enums.ProcessType;
import com.hete.supply.scm.server.scm.process.service.base.ProcessBaseService;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: RockyHuas
 * @date: 2022/11/4 09:50
 */
@Service
@RequiredArgsConstructor
public class ProcessBizService {
    private final ProcessDao processDao;
    private final static int SETUP_DURATION_MULTIPLE = 10;
    private final ProcessBaseService processBaseService;
    private final ProcessCompositeDao compositeDao;

    /**
     * 分页查询工序信息。
     *
     * @param processQueryDto 包含分页查询条件的 DTO 对象。
     * @return 包含工序信息的分页结果对象 {@link CommonPageResult.PageInfo}。
     * 如果查询结果为空，返回一个空的分页结果对象。
     */
    public CommonPageResult.PageInfo<ProcessVo> getByPage(ProcessQueryDto processQueryDto) {
        // 调用底层数据访问层的方法进行分页查询
        CommonPageResult.PageInfo<ProcessVo> pageResult = processDao.getByPage(
                PageDTO.of(processQueryDto.getPageNo(), processQueryDto.getPageSize()), processQueryDto);

        // 获取查询结果中的工序记录列表，如果工序记录列表为空，返回一个空的分页结果对象
        if (CollectionUtils.isEmpty(pageResult.getRecords())) {
            return new CommonPageResult.PageInfo<>();
        }

        // 在查询结果的基础上赋值新字段
        assignNewFields(pageResult);

        // 如果有工序记录，直接返回查询结果
        return pageResult;
    }

    /**
     * 获取所有的工序
     *
     * @return
     */
    public List<ProcessVo> getAll() {
        List<ProcessPo> processPos = processDao.getAll();
        return processPos.stream()
                .map(ProcessConverter.INSTANCE::convert)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(key = "#dto.processFirst.code", prefix = ScmRedisConstant.SCM_PROCESS_CHILD_NO_CREATE)
    public void createProcess(ProcessCreateDto dto) {
        final Integer setupDuration = dto.getSetupDuration();
        List<ProcessCommissionRuleDto> processCommissionRuleDtoList = dto.getProcessCommissionRuleDtoList();
        if (setupDuration % SETUP_DURATION_MULTIPLE != 0) {
            throw new ParamIllegalException("整备时间必须为10的倍数");
        }
        // 通过二级工序名称名称以及一级工序编码判断是否重复、工序字段是否重复
        ProcessPo processPo = processDao.getByProcedureFirstAndSecondName(dto.getProcessFirst(),
                dto.getProcessSecondName(),
                dto.getProcessLabel());
        if (processPo != null) {
            throw new ParamIllegalException("{}已存在", dto.getProcessSecondName());
        }
        // 查询最新的二级工序
        String formatSecondCode = this.generateSecondCode(dto.getProcessFirst());

        final ProcessPo newProcessPo = new ProcessPo();
        newProcessPo.setProcessFirst(dto.getProcessFirst());
        newProcessPo.setProcessLabel(dto.getProcessLabel());
        newProcessPo.setProcessSecondCode(formatSecondCode);
        newProcessPo.setProcessSecondName(dto.getProcessSecondName());
        newProcessPo.setProcessCode(dto.getProcessFirst()
                .getCode() + formatSecondCode);
        newProcessPo.setProcessName(dto.getProcessFirst()
                .getDesc() + "-" + dto.getProcessSecondName());
        newProcessPo.setCommission(dto.getCommission());
        newProcessPo.setExtraCommission(dto.getExtraCommission());
        newProcessPo.setProcessStatus(ProcessStatus.ENABLED);
        newProcessPo.setComplexCoefficient(dto.getComplexCoefficient());
        newProcessPo.setSetupDuration(dto.getSetupDuration());
        newProcessPo.setProcessType(ProcessType.INDEPENDENT_PROCESS);
        processDao.insert(newProcessPo);

        processBaseService.idempotentSaveProcessRule(newProcessPo.getProcessCode(), processCommissionRuleDtoList);
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(key = "#dto.processFirst.code", prefix = ScmRedisConstant.SCM_PROCESS_CHILD_NO_CREATE)
    public void editProcess(ProcessEditDto dto) {
        ProcessType processType = dto.getProcessType();
        boolean isCompoundProcess = Objects.equals(ProcessType.COMPOUND_PROCESS, processType);

        // 前置校验
        BigDecimal commission = dto.getCommission();
        BigDecimal extraCommission = dto.getExtraCommission();
        if (!isCompoundProcess) {
            if (Objects.isNull(commission) || commission.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ParamIllegalException("工序提成单价不能为空且必须大于 0");
            }
            if (Objects.isNull(extraCommission) || extraCommission.compareTo(BigDecimal.ZERO) < 0) {
                throw new ParamIllegalException("额外提成单价不能为空且必须大于等于 0");
            }
        }

        Integer setupDuration = dto.getSetupDuration();
        List<ProcessCommissionRuleDto> processCommissionRuleDtoList = dto.getProcessCommissionRuleDtoList();
        if (setupDuration % SETUP_DURATION_MULTIPLE != 0) {
            throw new ParamIllegalException("整备时间必须为10的倍数");
        }

        ProcessPo processPo = processDao.getByIdVersion(dto.getProcessId(), dto.getVersion());
        if (null == processPo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }

        // 校验修改后的工序信息是否已存在
        ProcessPo repeatProcessPo = processDao.getByProcedureFirstAndSecondName(dto.getProcessFirst(),
                dto.getProcessSecondName(),
                dto.getProcessLabel());
        if (repeatProcessPo != null && !repeatProcessPo.getProcessId()
                .equals(dto.getProcessId())) {
            throw new ParamIllegalException("{}已存在", dto.getProcessSecondName());
        }

        // 查询最新的二级工序
        String formatSecondCode = "";
        if (processPo.getProcessFirst() == dto.getProcessFirst()) {
            formatSecondCode = processPo.getProcessSecondCode();
        } else {
            formatSecondCode = this.generateSecondCode(dto.getProcessFirst());
        }

        processPo.setProcessFirst(dto.getProcessFirst());
        processPo.setProcessLabel(dto.getProcessLabel());
        processPo.setProcessSecondCode(formatSecondCode);
        processPo.setProcessSecondName(dto.getProcessSecondName());
        processPo.setProcessCode(dto.getProcessFirst()
                .getCode() + formatSecondCode);
        processPo.setProcessName(dto.getProcessFirst()
                .getDesc() + "-" + dto.getProcessSecondName());
        if (!isCompoundProcess) {
            processPo.setCommission(commission);
            processPo.setExtraCommission(extraCommission);
        }
        processPo.setComplexCoefficient(dto.getComplexCoefficient());
        processPo.setSetupDuration(dto.getSetupDuration());
        processDao.updateByIdVersion(processPo);

        if (!isCompoundProcess) {
            processBaseService.idempotentSaveProcessRule(processPo.getProcessCode(), processCommissionRuleDtoList);
        } else {
            String cpdProcCode = processPo.getProcessCode();
            List<CompoundProcessCreateDto.IndependentProcessDto> independentProcessDtoList
                    = dto.getIndependentProcessDtoList();
            List<String> indProcCodes = independentProcessDtoList.stream()
                    .map(CompoundProcessCreateDto.IndependentProcessDto::getProcessCode)
                    .collect(Collectors.toList());
            processBaseService.idempotentSaveProcessComposite(cpdProcCode, indProcCodes);
        }
    }

    /**
     * 生成编号
     *
     * @param processFirst
     * @return
     */

    public String generateSecondCode(ProcessFirst processFirst) {
        ProcessPo latest = processDao.getLatest(processFirst);
        int procedureSecondCode = latest != null ? Integer.parseInt(latest.getProcessSecondCode()) : 0;
        procedureSecondCode++;
        return StringUtil.toTwoDigitFormat(procedureSecondCode);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean changeStatus(ProcessChangeStatusDto dto) {
        List<ProcessChangeStatusDto.Process> processes = dto.getProcesses();

        List<ProcessPo> processPos = processes.stream()
                .map(item -> {
                    ProcessPo processPo = new ProcessPo();
                    processPo.setProcessId(item.getProcessId());
                    processPo.setVersion(item.getVersion());
                    processPo.setProcessStatus(dto.getProcessStatus());
                    return processPo;
                })
                .collect(Collectors.toList());

        return processDao.updateBatchByIdVersion(processPos);
    }

    /**
     * 查询工序列表
     *
     * @return
     */
    public List<ProcessVo> getQueryList(ProcessQueryListDto dto) {
        List<ProcessPo> processPos = processDao.getByList(dto);
        return ProcessConverter.INSTANCE.convert(processPos);
    }

    /**
     * 为工序记录列表中的每个工序分配新的字段。
     * 新字段包括工序提成规则信息列表。
     *
     * @param pageResult 包含工序记录的分页结果对象
     */
    private void assignNewFields(CommonPageResult.PageInfo<ProcessVo> pageResult) {
        List<ProcessVo> records = pageResult.getRecords();

        // 如果工序记录列表为空，不进行操作
        if (CollectionUtils.isEmpty(records)) {
            return;
        }

        // 获取工序编码集合
        Set<String> processCodes = records.stream()
                .map(ProcessVo::getProcessCode)
                .collect(Collectors.toSet());

        // 查询工序及其提成规则信息
        List<ProcessWithCommissionRuleBo> processWithCommissionRuleList
                = processBaseService.getProcessWithCommissionRule(processCodes);

        // 获取关联非组合工序信息
        Map<String, List<String>> independentToCompoundMap
                = processBaseService.getIndependentToCompoundMap(processCodes);
        Set<String> independentProcessCodes = independentToCompoundMap.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        List<ProcessPo> independentProcessPos = processDao.getByProcessCodes(independentProcessCodes);

        // 为每个工序记录分配新字段
        records.forEach(record -> {
            String processCode = record.getProcessCode();
            BigDecimal extraCommission = record.getExtraCommission();

            // 查找匹配的工序及其提成规则信息
            ProcessWithCommissionRuleBo matchProcessWithCommissionRuleBo = processWithCommissionRuleList.stream()
                    .filter(processWithCommissionRuleBo -> Objects.equals(processCode,
                            processWithCommissionRuleBo.getProcessCode()))
                    .findFirst()
                    .orElse(null);

            // 如果找到匹配的工序及其提成规则信息，并且规则列表不为空，则将规则列表转换为对应的视图对象，并设置到工序记录中
            if (Objects.nonNull(matchProcessWithCommissionRuleBo) && CollectionUtils.isNotEmpty(
                    matchProcessWithCommissionRuleBo.getRules())) {
                List<ProcessCommissionRuleVo> processCommissionRuleVoList
                        = ProcessCommissionRuleConverter.toProcessCommissionRuleVos(
                        matchProcessWithCommissionRuleBo.getRules(), extraCommission);
                record.setProcessCommissionRuleVoList(processCommissionRuleVoList);
            }

            List<String> curIndependentProcessCodes = independentToCompoundMap.get(processCode);
            if (CollectionUtils.isNotEmpty(curIndependentProcessCodes)) {
                List<ProcessPo> matchIndependentProcessPos = curIndependentProcessCodes.stream()
                        .map(curIndependentProcessCode -> independentProcessPos.stream()
                                .filter(independentProcessPo -> Objects.equals(curIndependentProcessCode,
                                        independentProcessPo.getProcessCode()))
                                .findFirst()
                                .orElse(null))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                List<ProcessVo.IndependentProcessVo> independentProcessVoList
                        = ProcessBuilder.buildIndependentProcessVos(matchIndependentProcessPos);
                record.setIndependentProcessVoList(independentProcessVoList);
            }

        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void createOrUpdateCommissionRules(CreateOrUpdateCommissionRuleBatchDto dto) {
        List<CreateOrUpdateCommissionRuleDto> createOrUpdateProcessCommissionRuleList
                = dto.getCreateOrUpdateProcessCommissionRuleList();
        for (CreateOrUpdateCommissionRuleDto createOrUpdateCommissionRuleDto :
                createOrUpdateProcessCommissionRuleList) {
            // 待配置的工序id & 待配置的工序规则
            String processCode = createOrUpdateCommissionRuleDto.getProcessCode();
            List<ProcessCommissionRuleDto> processCommissionRuleDtoList
                    = createOrUpdateCommissionRuleDto.getProcessCommissionRuleDtoList();
            processBaseService.idempotentSaveProcessRule(processCode, processCommissionRuleDtoList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(key = "#dto.processFirst.code", prefix = ScmRedisConstant.SCM_PROCESS_CHILD_NO_CREATE)
    public void createCompoundProcess(CompoundProcessCreateDto dto) {
        // 前置校验：整备时间 & 重复创建 & 关联工序信息
        final Integer setupDuration = dto.getSetupDuration();
        if (setupDuration % SETUP_DURATION_MULTIPLE != 0) {
            throw new ParamIllegalException("整备时间必须为10的倍数");
        }

        // 通过二级工序名称名称以及一级工序编码判断是否重复、工序字段是否重复
        ProcessPo existProcessPo = processDao.getByProcedureFirstAndSecondName(dto.getProcessFirst(),
                dto.getProcessSecondName(),
                dto.getProcessLabel());
        if (Objects.nonNull(existProcessPo)) {
            throw new ParamIllegalException("{}已存在", dto.getProcessSecondName());
        }

        // 查询最新的二级工序
        String formatSecondCode = this.generateSecondCode(dto.getProcessFirst());
        ProcessPo newCompoundProcessPo = ProcessBuilder.buildCompoundProcessPo(dto, formatSecondCode,
                ProcessType.COMPOUND_PROCESS);
        processDao.insert(newCompoundProcessPo);

        // 保存复合工序关联关系
        String cpdProcCode = newCompoundProcessPo.getProcessCode();
        // 校验关联工序信息是否存在
        List<CompoundProcessCreateDto.IndependentProcessDto> independentProcessDtoList
                = dto.getIndependentProcessDtoList();
        List<String> indProcCodes = independentProcessDtoList.stream()
                .map(CompoundProcessCreateDto.IndependentProcessDto::getProcessCode)
                .collect(Collectors.toList());
        processBaseService.idempotentSaveProcessComposite(cpdProcCode, indProcCodes);
    }
}
