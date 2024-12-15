package com.hete.supply.scm.util;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author yanjiawei
 * Created on 2024/3/22.
 */
public class HeteCollectionsUtil {
    public static List<String> filterIntersectingKeys(List<String> list,
                                                      Map<String, List<String>> map) {
        List<String> intersectingKeys = Lists.newArrayList();

        if (CollectionUtils.isEmpty(list) || CollectionUtils.isEmpty(map)) {
            return intersectingKeys;
        }

        // 遍历 map
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();

            // 检查是否与 laceAreas 中的属性列表相同
            if (isSameList(values, list)) {
                intersectingKeys.add(key);
            }
        }

        return intersectingKeys;
    }

    public static boolean isSameList(List<String> list1,
                                     List<String> list2) {
        // 如果任一列表为空，返回false
        if (list1 == null || list2 == null || list1.isEmpty() || list2.isEmpty()) {
            return false;
        }

        // 首先检查大小是否相同
        if (list1.size() != list2.size()) {
            return false;
        }

        // 检查内容是否完全匹配
        return new HashSet<>(list1).containsAll(list2) && new HashSet<>(list2).containsAll(list1);
    }
}
