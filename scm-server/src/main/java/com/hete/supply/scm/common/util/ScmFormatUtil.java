package com.hete.supply.scm.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * @author weiwenxin
 * @date 2023/12/12 10:47
 */
@Slf4j
public class ScmFormatUtil {

    /**
     * 定义合法的数字字符串的正则表达式：只允许数字、小数点和可选的负号
     */
    private static final Pattern VALID_NUMBER_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");

    /**
     * 验证字符串是否符合时间格式
     *
     * @param dateTimeString:
     * @param expectedFormat:
     * @return boolean
     * @author ChenWenLong
     * @date 2023/11/13 14:12
     */
    public static boolean isLocalDateTimeStandardFormat(String dateTimeString, String expectedFormat) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(expectedFormat);
            LocalDateTime parsedDateTime = LocalDateTime.from(formatter.parse(dateTimeString));

            // 验证字段是否匹配期望格式
            return dateTimeString.equals(parsedDateTime.format(formatter));
        } catch (DateTimeParseException e) {
            // 记录异常信息到日志
            log.warn("验证字符串是否符合时间格式入参1：{}，入参2：{}。错误信息：{}", dateTimeString, expectedFormat, e.getMessage());
            // 解析失败，返回 false
            return false;
        }
    }


    public static BigDecimal bigDecimalFormat(String str) {
        try {
            if (StringUtils.isBlank(str)) {
                return null;
            }
            // 检查字符串是否是合法的数字格式
            if (!VALID_NUMBER_PATTERN.matcher(str).matches()) {
                return null;
            }
            return new BigDecimal(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 千分位分隔符格式化
     *
     * @param number
     * @return
     */
    public static String convertToThousandFormat(BigDecimal number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(number);
    }

    /**
     * 汉语中数字大写
     */
    private static final String[] CN_UPPER_NUMBER = {"零", "壹", "贰", "叁", "肆",
            "伍", "陆", "柒", "捌", "玖"};
    /**
     * 汉语中货币单位大写，这样的设计类似于占位符
     */
    private static final String[] CN_UPPER_MONETRAY_UNIT = {"分", "角", "元",
            "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾",
            "佰", "仟"};
    /**
     * 特殊字符：整
     */
    private static final String CN_FULL = "整";
    /**
     * 特殊字符：负
     */
    private static final String CN_NEGATIVE = "负";
    /**
     * 金额的精度，默认值为2
     */
    private static final int MONEY_PRECISION = 2;
    /**
     * 特殊字符：零元整
     */
    private static final String CN_ZERO_FULL = "零元" + CN_FULL;

    /**
     * 把输入的金额转换为汉语中人民币的大写
     *
     * @param numberOfMoney 输入的金额
     * @return 对应的汉语大写
     */
    public static String convertToChinese(BigDecimal numberOfMoney) {
        StringBuffer sb = new StringBuffer();
        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
        // positive.
        int signum = numberOfMoney.signum();
        // 零元整的情况
        if (signum == 0) {
            return CN_ZERO_FULL;
        }
        // 这里会进行金额的四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION)
                .setScale(0, 4).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0)) {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero)) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
        if (!(scale > 0)) {
            sb.append(CN_FULL);
        }
        return sb.toString();
    }

    private final static Integer SUB_STRING_CNT = 3;

    public static String subStringLastThree(String str) {
        if (str.length() >= SUB_STRING_CNT) {
            return str.substring(str.length() - SUB_STRING_CNT);
        } else {
            StringBuilder sb = new StringBuilder(str);
            while (sb.length() < SUB_STRING_CNT) {
                sb.insert(0, "0");
            }
            return sb.toString();
        }
    }
}
