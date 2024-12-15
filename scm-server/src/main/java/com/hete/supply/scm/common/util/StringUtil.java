package com.hete.supply.scm.common.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.regex.Pattern;

/**
 * @Author: RockyHuas
 * @date: 2022/11/4 10:38
 */
public class StringUtil {
    private static final Pattern PATTERN = Pattern.compile("[0-9]*");
    private static final Pattern VALID_CHARACTERS_PATTERN = Pattern.compile("[A-Z0-9]+");

    /**
     * 整型数字格式化为两位字符串，不够位数前面填充0
     *
     * @param params
     * @return
     */
    public static String toTwoDigitFormat(int params) {
        return String.format("%02d", params);
    }

    /**
     * 整型数字格式化为三位字符串，不够位数前面填充0
     *
     * @param params
     * @return
     */
    public static String toThreeDigitFormat(int params) {
        return String.format("%03d", params);
    }

    public static boolean isNumeric(String str) {
        return Pattern.compile("[0-9]*").matcher(str).matches();

    }

    public static String extractContentFromMultipleStrings(String input) {
        String regex = ".*?-(.*)";
        String replacement = "$1";

        String[] split = input.split(",");
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].replaceAll(regex, replacement);
        }

        return String.join(",", split);
    }

    /**
     * 验证字符串是否是一个有效的11位手机号
     *
     * @param phoneNumber 要验证的字符串
     * @return 如果字符串是一个有效的11位手机号，返回true；否则返回false
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        // 检查字符串是否为null或长度不为11
        if (phoneNumber == null || phoneNumber.length() != 11) {
            return false;
        }
        // 检查每个字符是否都是数字
        for (char c : phoneNumber.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证字符串是否仅包含数字和大写字母
     *
     * @param input 输入的字符串
     * @return 如果字符串仅包含阿拉伯数字和大写英文字母，返回true；否则返回false
     */
    public static boolean isValidNumberEnglishString(String input) {
        if (StringUtils.isBlank(input)) {
            return false;
        }
        return VALID_CHARACTERS_PATTERN.matcher(input).matches();
    }

}
