package com.hete.supply.scm.server.scm.service.base;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hete.supply.scm.server.scm.entity.bo.FeiShuSheetConfigBo;
import com.hete.supply.scm.server.scm.enums.ReportStatisticStrategyType;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yanjiawei
 * Created on 2024/3/18.
 */
@Service
@RequiredArgsConstructor
public class FeiShuOnlineSheetBaseService {
    private final Environment environment;


    public String getColumnName(ReportStatisticStrategyType strategyType,
                                String javaFieldName) {
        String propertyName = "feishu.sheetTable." + strategyType.name();
        return environment.getProperty(propertyName + ".columnNames." + javaFieldName);
    }

    public FeiShuSheetConfigBo getConfigByReportBizType(ReportStatisticStrategyType reportStatisticStrategyType) {
        String propertyName = "feishu.sheetTable." + reportStatisticStrategyType.name();
        String appToken = environment.getProperty(propertyName + ".appToken");
        String tableId = environment.getProperty(propertyName + ".tableId");

        if (appToken == null || tableId == null) {
            throw new IllegalStateException(
                    "Missing configuration for ReportStatisticStrategyType: " + reportStatisticStrategyType);
        }

        return new FeiShuSheetConfigBo(appToken, tableId);
    }

    public List<Map<String, Object>> parseObjectList(ReportStatisticStrategyType strategyType,
                                                     List<?> reportDataList) {
        List<Map<String, Object>> resultList = Lists.newArrayList();

        for (Object reportData : reportDataList) {
            Map<String, Object> resultMap = parseObject(strategyType, reportData);
            resultList.add(resultMap);
        }
        return resultList;
    }

    public Map<String, Object> parseObject(ReportStatisticStrategyType strategyType,
                                           Object reportData) {
        Map<String, Object> resultMap = new HashMap<>();

        if (reportData instanceof List) {
            throw new IllegalArgumentException("报表数据不应该是列表");
        }

        Class<?> clazz = reportData.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            String columnName = getColumnName(strategyType, fieldName);
            if (StrUtil.isBlank(columnName)) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object columnValue = field.get(reportData);
                resultMap.put(columnName, columnValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return resultMap;
    }
}
