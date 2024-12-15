package com.hete.supply.scm.demo;

import com.hete.supply.scm.api.scm.entity.enums.SplitType;
import com.hete.supply.scm.api.scm.entity.vo.PrepaymentExportVo;
import com.hete.supply.scm.api.scm.importation.entity.dto.SkuAttrImportationDto;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.server.scm.entity.bo.TimeRangeBo;
import io.swagger.annotations.ApiModelProperty;
import org.junit.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/16 11:17
 */
public class SimpleTest {

    /**
     * @Description 整备时间要在边界时间，例如12点，18.30
     * @author yanjiawei
     * @Date 2023/8/28 10:56
     */
    @Test
    public void testOne() {

    }

    /**
     * @Description 跨天要整点
     * @author yanjiawei
     * @Date 2023/8/28 09:22
     */
    @Test
    public void testTwo() {
        TreeSet<TimeRangeBo> timeRangeBos = new TreeSet<>(Comparator.comparing(TimeRangeBo::getStartTime));
        timeRangeBos.add(new TimeRangeBo(LocalTime.of(9, 0), LocalTime.of(12, 0)));
        timeRangeBos.add(new TimeRangeBo(LocalTime.of(13, 30), LocalTime.of(18, 30)));

        Set<LocalDate> holidays = Set.of(LocalDate.of(2023, 8, 28));
        LocalDateTime firstBegin = LocalDateTime.of(2023, 8, 26, 18, 25, 0);
        LocalDateTime firstEnd = ScmTimeUtil.calculateEndTime(firstBegin, 0, 200,
                timeRangeBos, holidays);
        System.out.println("第一道工序开始时间：" + firstBegin);
        System.out.println("第一道工序结束时间：" + firstEnd);
    }

    @Test
    public void test1() {
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("key1", 1);
        map1.put("key2", 2);

        Map<String, Integer> map2 = new HashMap<>();
        map2.put("key1", 3);
        map2.put("key3", 4);

        Map<String, Integer> result = map1.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> map2.containsKey(entry.getKey()) ?
                                map2.get(entry.getKey()) + entry.getValue() :
                                entry.getValue()
                ));

        System.out.println(result);

    }


    /**
     * 枚举注释
     */
    @Test
    public void test2() {
        Arrays.stream(SplitType.values()).forEach(e -> System.out.print(e + "(" + e.getRemark() + "),"));
    }

    /**
     * 文件导出yaml参数配置
     */
    @Test
    public void test3() {
        PrepaymentExportVo myClass = new PrepaymentExportVo();
        Field[] fields = myClass.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            if (field.isAnnotationPresent(ApiModelProperty.class)) {
                ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                System.out.println(field.getName() + ": " + annotation.value());
            } else {
                System.out.println(field.getName());
            }
        });
    }

    /**
     * 文件导入yaml参数配置
     */
    @Test
    public void test4() {
        SkuAttrImportationDto myClass = new SkuAttrImportationDto();
        Field[] fields = myClass.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            if (field.isAnnotationPresent(ApiModelProperty.class)) {
                ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                System.out.println("\"[" + annotation.value() + "]\": " + field.getName());
            } else {
                System.out.println(field.getName());
            }
        });
    }

    @Test
    public void test5() {
        final ArrayList<Integer> integers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        System.out.println(integers);

    }

    @Test
    public void test6() {
        LocalDateTime startTime = LocalDateTime.of(2023, 7, 28, 10, 0, 0);
        Duration duration = Duration.ofHours(4).plusMinutes(30);
        LocalDateTime endTime = ScmTimeUtil.calculateEndTime(startTime, duration, 12, 0, 13, 30);
        System.out.println("Start time: " + startTime);
        System.out.println("End time: " + endTime);
    }

    @Test
    public void test7() {
        final int i = ScmTimeUtil.convertToMinutes(new BigDecimal("0.57"));
        System.out.println(i);
    }


    public static void main(String[] args) {
        LocalDateTime startTime = LocalDateTime.of(2023, 8, 25, 16, 30, 0); // 设置起始时间
        int durationHours = 0; // 设置耗时（小时）
        int durationMinutes = 200; // 设置耗时（分钟）

        LocalDateTime endTime = calculateEndTime(startTime, durationHours, durationMinutes);

        System.out.println("起始时间：" + startTime);
        System.out.println("结束时间：" + endTime);
    }

    private static LocalDateTime calculateEndTime(LocalDateTime startTime, int durationHours, int durationMinutes) {
        LocalDateTime endTime = startTime;
        int remainingHours = durationHours;
        int remainingMinutes = durationMinutes;

        while (remainingHours > 0 || remainingMinutes > 0) {
            endTime = endTime.plusMinutes(1);

            // 跳过非工作时间
            if (!isWorkingTime(endTime.toLocalTime())) {
                continue;
            }

            // 跳过节假日
            if (isHoliday(endTime.toLocalDate())) {
                continue;
            }

            remainingMinutes--;
            if (remainingMinutes == -1) {
                remainingHours--;
                remainingMinutes = 59;
            }
        }

        return endTime;
    }

    private static boolean isWorkingTime(LocalTime time) {
        LocalTime workStartTime = LocalTime.of(9, 0); // 上班时间起始
        LocalTime workEndTime1 = LocalTime.of(11, 59); // 下班时间结束（午餐）
        LocalTime workStartTime2 = LocalTime.of(13, 0); // 上班时间起始（午餐结束）
        LocalTime workEndTime2 = LocalTime.of(18, 29); // 下班时间结束

        return !(time.isBefore(workStartTime) || (time.isAfter(workEndTime1) && time.isBefore(workStartTime2)) || time.isAfter(workEndTime2));
    }

    private static boolean isHoliday(LocalDate date) {
        Set<LocalDate> holidays = new HashSet<>();
        holidays.add(LocalDate.of(2023, 8, 19));
        holidays.add(LocalDate.of(2023, 8, 20));

        return holidays.contains(date);
    }

    @Test
    public void test666() {
        List<String> combinations = new ArrayList<>();
        combinations.add("B1");
        combinations.add("B");
        combinations.add("A2");
        combinations.add("B3");
        combinations.add("A");
        combinations.add("A1");
        combinations.add("B2");
        combinations.add("A3");
        combinations.add("A12");
        combinations.add("B23");
        combinations.add("B45");
        combinations.add("B12");

        List<String> sortedCombinations = combinations.stream()
                .sorted(Comparator.comparing(SimpleTest::getSortKey))
                .collect(Collectors.toList());

        sortedCombinations.forEach(System.out::println);
    }

    private static String getSortKey(String combination) {
        Pattern pattern = Pattern.compile("(\\D+)(\\d*)");
        Matcher matcher = pattern.matcher(combination);
        if (matcher.matches()) {
            String letters = matcher.group(1);
            String numbers = matcher.group(2);

            if (numbers.isEmpty()) {
                return letters + "00000";  // 没有数字部分时，用0填充
            } else {
                return letters + String.format("%05d", Integer.parseInt(numbers));
            }
        }
        return combination;
    }


    @Test
    public void test() {
        int a = 3;
        int b = 10;
        int d = 5;

        final BigDecimal divide = BigDecimal.valueOf(a)
                .divide(BigDecimal.valueOf(b), 2, RoundingMode.HALF_UP);

        BigDecimal result = divide
                .multiply(BigDecimal.valueOf(d));

        int e = result.setScale(0, RoundingMode.HALF_UP).intValue();

        System.out.println("Result: " + divide);
        System.out.println("Result: " + result);
        System.out.println("e: " + e);
    }
}
