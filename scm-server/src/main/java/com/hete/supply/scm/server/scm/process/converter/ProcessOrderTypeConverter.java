package com.hete.supply.scm.server.scm.process.converter;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Sets;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * @date 2023年08月15日 16:31
 */
public class ProcessOrderTypeConverter {

    public static TreeSet<ProcessOrderType> convertToProcessOrderTypeEnums(String processOrderTypes) {
        if (StrUtil.isBlank(processOrderTypes)) {
            return Sets.newTreeSet();
        }

        List<String> remarks = Arrays.asList(processOrderTypes.split(","));
        if (CollectionUtils.isEmpty(remarks)) {
            return Sets.newTreeSet();
        }
        return remarks.stream().map(ProcessOrderType::valueOf).collect(Collectors.toCollection(TreeSet::new));
    }
}
