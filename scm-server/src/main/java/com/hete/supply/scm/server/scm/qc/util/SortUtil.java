package com.hete.supply.scm.server.scm.qc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author weiwenxin
 * @date 2023/10/13 10:51
 */
public class SortUtil {
    private static final Pattern COMBINATION_PATTERN = Pattern.compile("(\\D+)(\\d*)");

    /**
     * 对字母+数字的组合进行排序，如A,A1,A2,A3,A12,B,B1,B2,B3,B23,
     *
     * @param combination
     * @return
     */
    public static String getSortKey(String combination) {
        Matcher matcher = COMBINATION_PATTERN.matcher(combination);
        if (matcher.matches()) {
            String letters = matcher.group(1);
            String numbers = matcher.group(2);

            if (numbers.isEmpty()) {
                // 没有数字部分时，用0填充
                return letters + "00000";
            } else {
                return letters + String.format("%05d", Integer.parseInt(numbers));
            }
        }
        return combination;
    }
}
