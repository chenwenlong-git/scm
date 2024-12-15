package com.hete.supply.scm.server.scm.process.converter;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Sets;
import com.hete.supply.scm.api.scm.entity.enums.MissingInformation;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * @date 2023年07月24日 10:09
 */
public class MissInformationConverter {

    public static String convertToRemarks(String missInformation) {
        if (StrUtil.isBlank(missInformation)) {
            return "";
        }
        Set<MissingInformation> missingInformationEnums = convertToMissingInformationEnums(missInformation);
        if (CollectionUtils.isNotEmpty(missingInformationEnums)) {
            return missingInformationEnums.stream().map(MissingInformation::getRemark).collect(Collectors.joining(","));
        }
        return "";
    }

    public static Set<MissingInformation> convertToMissingInformationEnums(String missInformation) {
        if (StrUtil.isBlank(missInformation)) {
            return Sets.newHashSet();
        }

        List<String> remarks = Arrays.asList(missInformation.split(","));
        if (CollectionUtils.isEmpty(remarks)) {
            return Sets.newHashSet();
        }
        return remarks.stream().map(MissingInformation::valueOf).collect(Collectors.toSet());
    }

    public static String convertToMissInformation(Set<MissingInformation> insertMissingInformationList) {
        if (CollectionUtils.isEmpty(insertMissingInformationList)) {
            return "";
        }
        return insertMissingInformationList.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
}
