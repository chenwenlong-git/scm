package com.hete.supply.scm.server.scm.builder;

import cn.hutool.core.collection.CollectionUtil;
import com.hete.supply.mc.api.tables.entity.dto.FeiShuTablesBatchCreateDto;
import com.hete.supply.mc.api.tables.entity.dto.FeiShuTablesCreateDto;

import java.util.*;

/**
 * @author yanjiawei
 * Created on 2024/3/15.
 */
public class FeiShuTableBuilder {
    public static FeiShuTablesBatchCreateDto buildFeiShuTablesBatchCreateDto(List<Map<String, Object>> dataList) {
        if (CollectionUtil.isEmpty(dataList)) {
            return null;
        }

        List<FeiShuTablesCreateDto> records = new ArrayList<>();
        for (Map<String, Object> fields : dataList) {
            FeiShuTablesCreateDto feiShuTablesCreateDto = new FeiShuTablesCreateDto();
            feiShuTablesCreateDto.setFields(fields);
            records.add(feiShuTablesCreateDto);
        }
        return new FeiShuTablesBatchCreateDto(records);
    }
}
