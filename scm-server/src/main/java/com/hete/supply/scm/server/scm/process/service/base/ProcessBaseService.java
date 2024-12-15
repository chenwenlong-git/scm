package com.hete.supply.scm.server.scm.process.service.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.server.scm.process.builder.ProcessBuilder;
import com.hete.supply.scm.server.scm.process.dao.ProcessCommissionRuleDao;
import com.hete.supply.scm.server.scm.process.dao.ProcessCompositeDao;
import com.hete.supply.scm.server.scm.process.dao.ProcessDao;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessCompositePo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessCommissionRuleBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessWithCommissionRuleBo;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessCommissionRuleDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessCommissionRulePo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessPo;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2023/12/16.
 */
@Service
@RequiredArgsConstructor
public class ProcessBaseService {

    private final ProcessDao processDao;
    private final ProcessCommissionRuleDao processCommissionRuleDao;
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private final ProcessCompositeDao processCompositeDao;

    /**
     * 根据工序编码集合查询工序及其提成规则信息列表。
     *
     * @param processCodes 工序编码集合
     * @return 包含工序及其提成规则信息的集合
     */
    public List<ProcessWithCommissionRuleBo> getProcessWithCommissionRule(Collection<String> processCodes) {
        // 查询工序信息列表
        List<ProcessPo> processPos = processDao.getByProcessCodes(processCodes);

        // 若工序信息为空，则返回空集合
        if (CollectionUtils.isEmpty(processPos)) {
            return Collections.emptyList();
        }

        // 查询工序提成规则信息列表
        List<ProcessCommissionRulePo> processCommissionRulePos = processCommissionRuleDao.listByProcessCodes(
                processCodes);

        // 构建包含工序及其提成规则信息的集合
        return processPos.stream()
                .map(processPo -> {
                    // 基础单价 & 工序编码
                    String processCode = processPo.getProcessCode();
                    BigDecimal baseCommission = processPo.getCommission();

                    ProcessWithCommissionRuleBo resultBo = new ProcessWithCommissionRuleBo();
                    resultBo.setProcessCode(processCode);

                    ProcessBo processBo = new ProcessBo();
                    processBo.setProcessId(processPo.getProcessId());
                    processBo.setProcessFirst(processPo.getProcessFirst());
                    processBo.setProcessLabel(processPo.getProcessLabel());
                    processBo.setProcessType(processPo.getProcessType());
                    processBo.setProcessSecondCode(processPo.getProcessSecondCode());
                    processBo.setProcessSecondName(processPo.getProcessSecondName());
                    processBo.setProcessCode(processCode);
                    processBo.setProcessName(processPo.getProcessName());
                    processBo.setCommission(processPo.getCommission());
                    processBo.setExtraCommission(processPo.getExtraCommission());
                    processBo.setSetupDuration(processPo.getSetupDuration());
                    processBo.setProcessStatus(processPo.getProcessStatus());
                    processBo.setComplexCoefficient(processPo.getComplexCoefficient());
                    resultBo.setProcess(processBo);

                    // 过滤匹配的提成规则
                    List<ProcessCommissionRulePo> matchRules = processCommissionRulePos.stream()
                            .filter(rule -> Objects.equals(processCode, rule.getProcessCode()))
                            .collect(Collectors.toList());

                    // 若匹配的提成规则不为空，则构建规则集合
                    if (CollectionUtil.isNotEmpty(matchRules)) {
                        TreeSet<ProcessCommissionRuleBo> rules = matchRules.stream()
                                .map(matchRule -> {
                                    BigDecimal commissionCoefficient = matchRule.getCommissionCoefficient();

                                    ProcessCommissionRuleBo rule = new ProcessCommissionRuleBo();
                                    rule.setProcessCommissionRuleId(matchRule.getProcessCommissionRuleId());
                                    rule.setProcessCode(matchRule.getProcessCode());
                                    rule.setCommissionLevel(matchRule.getCommissionLevel());
                                    rule.setStartQuantity(matchRule.getStartQuantity());
                                    rule.setEndQuantity(matchRule.getEndQuantity());
                                    rule.setCommissionCoefficient(commissionCoefficient);

                                    // 计算并设置提成单价，保留两位小数，使用四舍五入
                                    BigDecimal commissionPrice = baseCommission.multiply(commissionCoefficient)
                                            .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
                                    rule.setCommissionPrice(commissionPrice);
                                    rule.setVersion(matchRule.getVersion());
                                    return rule;
                                })
                                .collect(Collectors.toCollection(() -> new TreeSet<>(
                                        Comparator.comparing(ProcessCommissionRuleBo::getCommissionLevel))));
                        resultBo.setRules(rules);
                    }
                    return resultBo;
                })
                .collect(Collectors.toList());
    }


    /**
     * 幂等保存工序提成规则方法，使用分布式锁确保在多个实例环境下的同步执行。
     *
     * @param processCode                  工序编码
     * @param processCommissionRuleDtoList 工序提成规则DTO列表
     * @throws ParamIllegalException 当工序信息不存在时抛出此异常
     */
    @RedisLock(key = "#processCode", prefix = ScmRedisConstant.UPDATE_PROCESS_RULE, waitTime = 1, leaseTime = -1)
    public void idempotentSaveProcessRule(String processCode,
                                          List<ProcessCommissionRuleDto> processCommissionRuleDtoList) {
        // 校验工序信息
        ProcessPo processPo = processDao.getByProcessCode(processCode);
        if (Objects.isNull(processPo)) {
            throw new ParamIllegalException("工序信息被删除，请刷新页面！");
        }

        // 删除已存在的工序提成规则
        List<ProcessCommissionRulePo> existRules = processCommissionRuleDao.listByProcessCode(processCode);
        if (CollectionUtil.isNotEmpty(existRules)) {
            List<Long> existRuleIds = existRules.stream()
                    .map(ProcessCommissionRulePo::getProcessCommissionRuleId)
                    .collect(Collectors.toList());
            processCommissionRuleDao.removeBatchByIds(existRuleIds);
        }

        // 构建新的工序提成规则列表
        List<ProcessCommissionRulePo> processCommissionRulePos = processCommissionRuleDtoList.stream()
                .map(configRule -> {
                    ProcessCommissionRulePo processCommissionRulePo = new ProcessCommissionRulePo();
                    processCommissionRulePo.setProcessCode(processCode);
                    processCommissionRulePo.setCommissionLevel(configRule.getCommissionLevel());
                    processCommissionRulePo.setStartQuantity(configRule.getStartQuantity());
                    processCommissionRulePo.setEndQuantity(configRule.getEndQuantity());
                    processCommissionRulePo.setCommissionCoefficient(configRule.getCommissionCoefficient());

                    validateProcessCommissionRule(processCommissionRulePo);
                    return processCommissionRulePo;
                })
                .collect(Collectors.toList());

        validateRules(processCommissionRulePos, errorMessage -> {
            throw new ParamIllegalException(errorMessage);
        });
        // 批量插入新的工序提成规则
        processCommissionRuleDao.insertBatch(processCommissionRulePos);
    }

    /**
     * 校验 ProcessCommissionRulePo 对象是否符合规定的条件。
     *
     * @param rule 待校验的 ProcessCommissionRulePo 对象
     */
    public void validateProcessCommissionRule(ProcessCommissionRulePo rule) {
        Objects.requireNonNull(rule, "工序提成规则不能为空");
        Objects.requireNonNull(rule.getProcessCode(), "工序id不能为空");
        Objects.requireNonNull(rule.getCommissionLevel(), "提成等级不能为空");
        Objects.requireNonNull(rule.getCommissionCoefficient(), "提成系数百分比不能为空");

        // 数量起始值必须大于0
        Integer startQuantity = rule.getStartQuantity();
        if (Objects.isNull(startQuantity) || startQuantity <= 0) {
            throw new ParamIllegalException("数量起始值必须大于0");
        }

        // 数量结束值必须大于0
        Integer endQuantity = rule.getEndQuantity();
        if (Objects.isNull(endQuantity) || endQuantity <= 0) {
            throw new ParamIllegalException("数量结束值必须大于0");
        }

        if (startQuantity >= endQuantity) {
            throw new ParamIllegalException("数量结束值必须大于数量起始值");
        }
    }

    public void validateRules(List<ProcessCommissionRulePo> rules,
                              RuleValidationFunction validationFunction) {
        if (CollectionUtil.isEmpty(rules)) {
            validationFunction.validate("提成规则列表不能为空");
        }

        // 先按 commissionLevel 排序
        rules.sort(Comparator.comparingInt(ProcessCommissionRulePo::getCommissionLevel));

        // 验证第一个规则的数量起始值必须是1
        ProcessCommissionRulePo firstRule = rules.get(0);
        if (firstRule.getStartQuantity() != 1) {
            validationFunction.validate("第一个规则的数量起始值必须是1");
        }

        Set<Integer> commissionLevels = new HashSet<>();

        for (int i = 0; i < rules.size(); i++) {
            ProcessCommissionRulePo rule = rules.get(i);

            // 校验 commissionLevel 不能重复
            if (!commissionLevels.add(rule.getCommissionLevel())) {
                validationFunction.validate(StrUtil.format("阶梯等级重复:{}", rule.getCommissionLevel()));
            }

            for (int j = i + 1; j < rules.size(); j++) {
                ProcessCommissionRulePo nextRule = rules.get(j);

                // 校验重叠区间
                if (isOverlap(rule, nextRule)) {
                    validationFunction.validate(StrUtil.format("重叠区间：[{},{}] 与 [{},{}]", rule.getStartQuantity(),
                            rule.getEndQuantity(), nextRule.getStartQuantity(),
                            nextRule.getEndQuantity()));
                }

                // 校验相邻元素结束值和起始值相差1
                if (!isAdjacentDifferenceOne(rule, nextRule)) {
                    validationFunction.validate(
                            StrUtil.format("存在不连续的区间：[{},{}] 与 [{},{}]", rule.getStartQuantity(),
                                    rule.getEndQuantity(), nextRule.getStartQuantity(),
                                    nextRule.getEndQuantity()));
                }
            }
        }
    }


    /**
     * 判断两个区间是否重叠。
     *
     * @param rule1 区间规则1
     * @param rule2 区间规则2
     * @return 如果区间重叠返回 true，否则返回 false
     */
    private static boolean isOverlap(ProcessCommissionRulePo rule1,
                                     ProcessCommissionRulePo rule2) {
        return rule1.getEndQuantity() >= rule2.getStartQuantity() && rule2.getEndQuantity() >= rule1.getStartQuantity();
    }

    /**
     * 判断两个相邻区间的结束值和起始值是否相差1。
     *
     * @param rule1 区间规则1
     * @param rule2 区间规则2
     * @return 如果相差1返回 true，否则返回 false
     */
    private static boolean isAdjacentDifferenceOne(ProcessCommissionRulePo rule1,
                                                   ProcessCommissionRulePo rule2) {
        return rule2.getStartQuantity() - rule1.getEndQuantity() == 1;
    }

    /**
     * 获取复合工序到单独工序的映射关系
     *
     * @param processCodes 工序编码集合
     * @return 复合工序到单独工序的映射关系对象
     */
    public Map<String, List<String>> getCompoundToIndependentMap(Set<String> processCodes) {
        List<ProcessCompositePo> compoundToIndependents = processCompositeDao.listBySubProcessCodes(processCodes);
        return compoundToIndependents.stream()
                .collect(Collectors.groupingBy(ProcessCompositePo::getSubProcessCode,
                        Collectors.mapping(ProcessCompositePo::getParentProcessCode,
                                Collectors.toList())));
    }

    /**
     * 获取单独工序到复合工序的映射关系
     *
     * @param processCodes 工序编码集合
     * @return 单独工序到复合工序的映射关系对象
     */
    public Map<String, List<String>> getIndependentToCompoundMap(Set<String> processCodes) {
        List<ProcessCompositePo> independentToCompounds = processCompositeDao.listByParentProcessCodes(processCodes);
        return independentToCompounds.stream()
                .collect(Collectors.groupingBy(ProcessCompositePo::getParentProcessCode,
                        Collectors.mapping(ProcessCompositePo::getSubProcessCode,
                                Collectors.toList())));
    }

    public Set<String> getRelProcCodes(String procCode) {
        Set<String> relProcCodes = new HashSet<>(Set.of(procCode));

        Map<String, List<String>> independentToCompoundMap
                = getIndependentToCompoundMap(Collections.singleton(procCode));
        List<String> indProcCodes = independentToCompoundMap.get(procCode);
        if (CollectionUtils.isNotEmpty(indProcCodes)) {
            relProcCodes.addAll(indProcCodes);
            return relProcCodes;
        }

        Map<String, List<String>> compoundToIndependentMap = getCompoundToIndependentMap(
                Collections.singleton(procCode));
        List<String> cpdProcCodes = compoundToIndependentMap.get(procCode);
        if (CollectionUtils.isNotEmpty(cpdProcCodes)) {
            relProcCodes.addAll(cpdProcCodes);
            return relProcCodes;
        }

        return relProcCodes;
    }

    /**
     * 保存组合工序并维护与之相关的非组合工序。
     * 使用Redis锁确保幂等性。
     *
     * @param cpdProcCode  组合工序的工序编码
     * @param idnProcCodes 与组合工序相关的非组合工序的工序编码列表
     */
    @RedisLock(key = "#cpdProcCode", prefix = ScmRedisConstant.UPDATE_PROCESS_CPD, waitTime = 1, leaseTime = -1)
    public void idempotentSaveProcessComposite(String cpdProcCode,
                                               List<String> idnProcCodes) {
        // 确保组合工序的工序编码不为空
        ParamValidUtils.requireNotBlank(cpdProcCode, "操作失败！请填写组合工序的工序编码，以维护与之相关的非组合工序。");
        // 确保非组合工序的工序编码列表不为空
        ParamValidUtils.requireNotEmpty(idnProcCodes,
                "操作失败！请填写非组合工序的工序编码，以维护与之相关的非组合工序。");

        // 查询已存在的与组合工序相关的组合工序信息
        List<ProcessCompositePo> existCpdProcPos
                = processCompositeDao.listByParentProcessCodes(Collections.singleton(cpdProcCode));
        // 如果已存在相关信息，则将其标记为已删除
        if (CollectionUtils.isNotEmpty(existCpdProcPos)) {
            existCpdProcPos.forEach(existCpdProcPo -> existCpdProcPo.setDelTimestamp(DateUtil.current()));
            processCompositeDao.updateBatchByIdVersion(existCpdProcPos);
        }

        // 构建待查询的工序编码集合，包括组合工序和非组合工序
        Set<String> queryProcCodes = new HashSet<>(idnProcCodes) {{
            add(cpdProcCode);
        }};
        // 根据工序编码查询工序信息
        List<ProcessPo> processPos = processDao.getByProcessCodes(queryProcCodes);

        // 获取组合工序的ID和编码
        Long compoundProcessId = ParamValidUtils.requireNotNull(processPos.stream()
                        .filter(processPo -> Objects.equals(cpdProcCode,
                                processPo.getProcessCode()))
                        .findFirst()
                        .map(ProcessPo::getProcessId)
                        .orElse(null),
                "操作失败！组合工序信息不存在，请先创建组合工序信息，然后再维护与之相关的非组合工序。");
        String compoundProcessCode = ParamValidUtils.requireNotBlank(processPos.stream()
                        .filter(processPo -> Objects.equals(
                                cpdProcCode,
                                processPo.getProcessCode()))
                        .findFirst()
                        .map(ProcessPo::getProcessCode)
                        .orElse(""),
                "操作失败！组合工序编码不存在，请先创建组合工序信息，然后再维护与之相关的非组合工序。");
        // 构建组合工序与非组合工序的关联关系，并插入数据库
        List<ProcessCompositePo> processCompositePos = ProcessBuilder.buildCompositeProcesses(compoundProcessId,
                compoundProcessCode,
                idnProcCodes,
                processPos);
        processCompositeDao.insertBatch(processCompositePos);
    }
}
