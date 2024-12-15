package com.hete.supply.scm.server.scm.properties;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author yanjiawei
 * @date 2023年08月17日 14:11
 */
@Component
public class CapacityPoolConfigReader {
    @Autowired
    private Environment environment;

    public CapacityPoolConfig readCapacityPoolConfig(String poolCode) {
        CapacityPoolConfig config = new CapacityPoolConfig();
        config.setOrderTypes(getOrderTypes(poolCode));
        config.setEmployeeNos(getEmployeeNos(poolCode));
        config.setProductionCycle(environment.getProperty(poolCode + ".production-cycle", Integer.class));
        config.setStartTime(environment.getProperty(poolCode + ".start-time"));
        config.setEndTime(environment.getProperty(poolCode + ".end-time"));
        config.setHolidays(getHolidays(poolCode));
        config.setBreakTimeSegments(getBreakTimeSegments(poolCode));
        config.setWorkDuration(environment.getProperty(poolCode + ".work-duration"));
        config.setMaxProductionOrders(environment.getProperty(poolCode + ".max-production-orders", Long.class));
        return config;
    }


    public Set<String> getOrderTypes(String poolCode) {
        Set<String> orderTypes = Sets.newHashSet();
        int index = 0;
        String orderType = "";
        while ((orderType = environment.getProperty(StrUtil.format("{}.order-types[{}]", poolCode, index))) != null) {
            orderTypes.add(orderType);
            index++;
        }
        return orderTypes;
    }

    public Set<String> getEmployeeNos(String poolCode) {
        Set<String> employeeNos = Sets.newHashSet();
        int index = 0;
        String employeeNo = "";
        while ((employeeNo = environment.getProperty(StrUtil.format("{}.employee-nos[{}]", poolCode, index))) != null) {
            employeeNos.add(employeeNo);
            index++;
        }
        return employeeNos;
    }

    public TreeSet<String> getHolidays(String poolCode) {
        TreeSet<String> holidays = Sets.newTreeSet();
        int index = 0;
        String holiday = "";
        while ((holiday = environment.getProperty(StrUtil.format("{}.holidays[{}]", poolCode, index))) != null) {
            holidays.add(holiday);
            index++;
        }
        return holidays;
    }

    public List<CapacityPoolConfig.BreakTimeSegment> getBreakTimeSegments(String poolCode) {
        List<CapacityPoolConfig.BreakTimeSegment> breakTimeSegments = Lists.newArrayList();
        int index = 0;
        String startTime = "";
        String endTime = "";
        while ((startTime = environment.getProperty(StrUtil.format("{}.break-time-segments[{}].start-time", poolCode, index))) != null &&
                (endTime = environment.getProperty(StrUtil.format("{}.break-time-segments[{}].end-time", poolCode, index))) != null) {
            CapacityPoolConfig.BreakTimeSegment breakTimeSegment = new CapacityPoolConfig.BreakTimeSegment();
            breakTimeSegment.setStartTime(startTime);
            breakTimeSegment.setEndTime(endTime);
            breakTimeSegments.add(breakTimeSegment);
            index++;
        }
        return breakTimeSegments;
    }


    @Data
    public static class CapacityPoolConfig {
        private Set<String> orderTypes;
        private Set<String> employeeNos;
        private Integer productionCycle;
        private String startTime;
        private String endTime;
        private TreeSet<String> holidays;
        private List<BreakTimeSegment> breakTimeSegments;
        private String workDuration;
        private Long maxProductionOrders;

        @Data
        public static class BreakTimeSegment {
            private String startTime;
            private String endTime;
        }
    }

}
