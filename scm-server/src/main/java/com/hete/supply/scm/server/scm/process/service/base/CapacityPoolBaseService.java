package com.hete.supply.scm.server.scm.process.service.base;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import com.hete.supply.scm.server.scm.entity.bo.ProductionPoolConfigBo;
import com.hete.supply.scm.server.scm.process.entity.bo.BreakTimeSegmentBo;
import com.hete.supply.scm.server.scm.properties.CapacityPoolConfigReader;
import com.hete.supply.scm.server.scm.properties.ProductionPoolConfigReader;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * @date 2023年08月17日 11:17
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class CapacityPoolBaseService {
    private final CapacityPoolConfigReader capacityPoolConfigReader;
    private final ProductionPoolConfigReader productionPoolConfigReader;

    public ProductionPoolConfigBo getProductionPoolConfig(ProcessOrderType processOrderTypeEnum) {
        TreeSet<String> productionPoolCodes = productionPoolConfigReader.getProductionPoolCodes();
        for (String productionPoolCode : productionPoolCodes) {
            ProductionPoolConfigBo productionPoolConfig = getProductionPoolConfig(productionPoolCode);
            if (Objects.nonNull(productionPoolConfig)
                    && CollectionUtils.isNotEmpty(productionPoolConfig.getOrderTypes())
                    && productionPoolConfig.getOrderTypes().contains(processOrderTypeEnum)) {
                return getProductionPoolConfig(productionPoolCode);
            }
        }
        return null;
    }

    public ProductionPoolConfigBo getProductionPoolConfig(String poolCode) {
        CapacityPoolConfigReader.CapacityPoolConfig capacityPoolConfig = capacityPoolConfigReader.readCapacityPoolConfig(poolCode);
        if (Objects.isNull(capacityPoolConfig)) {
            throw new ParamIllegalException("无法获取产能池配置信息！产能池编号：[{}]", poolCode);
        }

        final Set<String> orderTypesStr = capacityPoolConfig.getOrderTypes();
        final Set<String> employeeNos = capacityPoolConfig.getEmployeeNos();
        final Integer productionCycle = capacityPoolConfig.getProductionCycle();
        final String startTimeStr = capacityPoolConfig.getStartTime();
        final String endTimeStr = capacityPoolConfig.getEndTime();
        final TreeSet<String> holidaysStr = capacityPoolConfig.getHolidays();
        final List<CapacityPoolConfigReader.CapacityPoolConfig.BreakTimeSegment> breakTimeSegmentsStr = capacityPoolConfig.getBreakTimeSegments();
        final String workDuration = capacityPoolConfig.getWorkDuration();
        final Long maxProductionOrders = capacityPoolConfig.getMaxProductionOrders();

        ProductionPoolConfigBo bo = new ProductionPoolConfigBo();
        bo.setProductionPoolCode(poolCode);
        Set<ProcessOrderType> orderTypes = orderTypesStr.stream().map(ProcessOrderType::valueOf).collect(Collectors.toSet());
        bo.setOrderTypes(orderTypes);
        bo.setEmployeeNos(employeeNos);
        bo.setProductionCycle(productionCycle);

        LocalTime startTime = LocalTime.parse(startTimeStr);
        LocalTime startTimeEtc = TimeUtil.convertZone(LocalDateTime.now().toLocalDate().atTime(startTime), TimeZoneId.CN, TimeZoneId.UTC).toLocalTime();
        bo.setStartTime(startTimeEtc);

        LocalTime endTime = LocalTime.parse(endTimeStr);
        LocalTime endTimeEtc = TimeUtil.convertZone(LocalDateTime.now().toLocalDate().atTime(endTime), TimeZoneId.CN, TimeZoneId.UTC).toLocalTime();
        bo.setEndTime(endTimeEtc);

        TreeSet<LocalDate> holidays
                = holidaysStr.stream().map(holiday -> LocalDate.parse(holiday, DateTimeFormatter.ofPattern("yyyy/M/d"))
                .atStartOfDay().toLocalDate()).collect(Collectors.toCollection(TreeSet::new));
        bo.setHolidays(holidays);

        List<BreakTimeSegmentBo> breakTimeSegments = breakTimeSegmentsStr.stream().map(breakTimeSegment -> {
            BreakTimeSegmentBo breakTimeSegmentBo = new BreakTimeSegmentBo();

            LocalTime sTime = LocalTime.parse(breakTimeSegment.getStartTime());
            LocalTime startEtcTime = TimeUtil.convertZone(LocalDateTime.now().toLocalDate().atTime(sTime), TimeZoneId.CN, TimeZoneId.UTC).toLocalTime();
            breakTimeSegmentBo.setStartTime(startEtcTime);

            LocalTime eTime = LocalTime.parse(breakTimeSegment.getEndTime());
            LocalTime endEtcTime = TimeUtil.convertZone(LocalDateTime.now().toLocalDate().atTime(eTime), TimeZoneId.CN, TimeZoneId.UTC).toLocalTime();
            breakTimeSegmentBo.setEndTime(endEtcTime);

            return breakTimeSegmentBo;
        }).collect(Collectors.toList());
        bo.setBreakTimeSegments(breakTimeSegments);

        bo.setWorkHourDuration(new BigDecimal(workDuration));
        bo.setMaxProductionOrders(maxProductionOrders);
        return bo;
    }
}
