package com.hete.supply.scm.server.supplier.service.base;
/**
 * 员工职级能力公用服务类
 *
 * @author yanjiawei
 * Created on 2023/9/14.
 */

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.process.entity.dto.EmployeeRestTimeDto;
import com.hete.supply.scm.server.supplier.entity.dto.ProcessSimpleDto;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * @date 2023年09月14日 13:53
 */
@Service
@RequiredArgsConstructor
public class EmployeeBaseService {

    /**
     * 校验给定的工序列表中是否存在重复的工序ID。
     *
     * @param processSimpleList 要校验的工序列表
     * @return 如果存在重复的工序ID则返回 true，否则返回 false
     */
    public boolean checkDuplicateProcessIds(List<ProcessSimpleDto> processSimpleList) {
        // 使用流和Collectors.groupingBy收集具有相同processId的项
        Map<Long, List<ProcessSimpleDto>> processIdGroups = processSimpleList.stream()
                .collect(Collectors.groupingBy(ProcessSimpleDto::getProcessId));
        // 查找具有多个项的分组，即重复的processId
        List<Long> duplicateProcessIds = processIdGroups.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1).map(Map.Entry::getKey).collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(duplicateProcessIds);
    }

    /**
     * 检查员工休息时间列表中的开始时间和结束时间是否有重复。
     *
     * @param employeeRestTimeList 包含员工休息时间信息的列表
     * @return 如果存在重复的开始时间或结束时间，则返回 true；否则返回 false
     */
    public boolean checkDuplicateRestTimes(List<EmployeeRestTimeDto> employeeRestTimeList) {
        // 如果列表为空，直接返回 false
        if (CollectionUtils.isEmpty(employeeRestTimeList)) {
            return false;
        }

        // 创建一个 HashSet 用于存储已经遍历的休息时间对象
        Set<EmployeeRestTimeDto> restTimeSet = new HashSet<>();

        // 使用流遍历员工休息时间列表，检查是否有重复的开始时间或结束时间
        for (EmployeeRestTimeDto restTime : employeeRestTimeList) {
            // 如果已经包含了相同的休息时间对象，返回 true 表示有重复
            if (!restTimeSet.add(restTime)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验员工的停工时间是否合法，不允许选择过去的时间作为停工的开始时间或结束时间。
     *
     * @param employeeRestTimeList 包含员工停工时间信息的列表
     * @throws ParamIllegalException 如果选择了过去的时间作为停工的开始时间或结束时间，将抛出此异常
     */
    public void validateRestTimes(List<EmployeeRestTimeDto> employeeRestTimeList) {
        // 检查输入列表是否为空
        if (CollectionUtils.isEmpty(employeeRestTimeList)) {
            // 如果为空，无需继续校验，直接返回
            return;
        }

        LocalDateTime currentTime = LocalDateTime.now();
        for (EmployeeRestTimeDto restTime : employeeRestTimeList) {
            if (Objects.nonNull(restTime.getRestStartTime()) && Objects.nonNull(restTime.getRestEndTime())) {
                // 如果开始时间和结束时间都不为空
                if (restTime.getRestStartTime().isBefore(currentTime) || restTime.getRestEndTime().isBefore(currentTime)) {
                    // 如果开始时间或结束时间在当前时间之前，抛出异常
                    throw new ParamIllegalException("不允许选择一个过去的时间作为停工的开始时间/结束时间，请检查！");
                }
                if (restTime.getRestStartTime().isEqual(restTime.getRestEndTime())) {
                    // 如果开始时间等于结束时间，抛出异常
                    throw new ParamIllegalException("停工开始时间不能与停工结束时间相同，请检查！");
                }
            }
        }
    }

}
