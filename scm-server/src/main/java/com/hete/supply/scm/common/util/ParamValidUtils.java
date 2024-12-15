package com.hete.supply.scm.common.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.hete.support.api.exception.ParamIllegalException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author yanjiawei
 * Created on 2024/1/4.
 */
public class ParamValidUtils {

    /**
     * 检查两个整数是否相等。如果两者不相等，则抛出一个带有指定错误消息的 {@link ParamIllegalException} 异常。
     *
     * @param number1  第一个整数。
     * @param number2  第二个整数。
     * @param notEqMsg 如果两个整数不相等，则在异常中使用的错误消息。
     * @throws ParamIllegalException 如果两个整数不相等。
     */
    public static void requireEqual(int number1,
                                    int number2,
                                    String notEqMsg) {
        if (number1 != number2) {
            throw new ParamIllegalException(notEqMsg);
        }
    }

    /**
     * 检查 number1 是否小于等于 number2。如果不是，则抛出一个带有指定错误消息的 {@link ParamIllegalException} 异常。
     *
     * @param number1  第一个整数。
     * @param number2  第二个整数。
     * @param errorMsg 如果 number1 不小于等于 number2，则在异常中使用的错误消息。
     * @throws ParamIllegalException 如果 number1 不小于等于 number2。
     */
    public static void requireLessThanOrEqual(int number1,
                                              int number2,
                                              String errorMsg) {
        if (number1 > number2) {
            throw new ParamIllegalException(errorMsg);
        }
    }

    /**
     * 检查两个 BigDecimal 是否小于等于 number2。如果不是，则抛出一个带有指定错误消息的 {@link ParamIllegalException} 异常。
     *
     * @param bigDecimal1 第一个 BigDecimal。
     * @param bigDecimal2 第二个 BigDecimal。
     * @param errorMsg    如果 number1 不小于等于 number2，则在异常中使用的错误消息。
     * @throws ParamIllegalException 如果 number1 不小于等于 number2。
     */
    public static void requireLessThanOrEqual(BigDecimal bigDecimal1, BigDecimal bigDecimal2, String errorMsg) {
        if (bigDecimal1.compareTo(bigDecimal2) > 0) {
            throw new ParamIllegalException(errorMsg);
        }
    }

    /**
     * 检查 number1 是否大于 number2。如果不是，则抛出一个带有指定错误消息的 {@link ParamIllegalException} 异常。
     *
     * @param number1  第一个整数。
     * @param number2  第二个整数。
     * @param errorMsg 如果 number1 不大于 number2，则在异常中使用的错误消息。
     * @throws ParamIllegalException 如果 number1 不大于 number2。
     */
    public static void requireGreaterThan(int number1,
                                          int number2,
                                          String errorMsg) {
        if (number1 <= number2) {
            throw new ParamIllegalException(errorMsg);
        }
    }

    /**
     * 检查两个 BigDecimal 是否大于 number2。如果不是，则抛出一个带有指定错误消息的 {@link ParamIllegalException} 异常。
     *
     * @param bigDecimal1 第一个 BigDecimal。
     * @param bigDecimal2 第二个 BigDecimal。
     * @param errorMsg    如果 number1 不大于 number2，则在异常中使用的错误消息。
     * @throws ParamIllegalException 如果 number1 不大于 number2。
     */
    public static void requireGreaterThan(BigDecimal bigDecimal1, BigDecimal bigDecimal2, String errorMsg) {
        if (bigDecimal1.compareTo(bigDecimal2) <= 0) {
            throw new ParamIllegalException(errorMsg);
        }
    }

    /**
     * 检查两个 BigDecimal 是否大于等于 number2。如果不是，则抛出一个带有指定错误消息的 {@link ParamIllegalException} 异常。
     *
     * @param bigDecimal1 第一个 BigDecimal。
     * @param bigDecimal2 第二个 BigDecimal。
     * @param errorMsg    如果 number1 不大于等于 number2，则在异常中使用的错误消息。
     * @throws ParamIllegalException 如果 number1 不大于等于 number2。
     */
    public static void requireGreaterThanOrEqual(BigDecimal bigDecimal1, BigDecimal bigDecimal2, String errorMsg) {
        if (bigDecimal1.compareTo(bigDecimal2) < 0) {
            throw new ParamIllegalException(errorMsg);
        }
    }


    /**
     * 检查两个对象是否相等。如果对象不相等，则抛出一个带有指定错误消息的 {@link ParamIllegalException} 异常。
     *
     * @param obj1         要比较的第一个对象。
     * @param obj2         要比较的第二个对象。
     * @param notEqualsMsg 如果对象不相等，则在异常中使用的错误消息。
     * @throws ParamIllegalException 如果两个对象不相等。
     */
    public static void requireEquals(Object obj1,
                                     Object obj2,
                                     String notEqualsMsg) {
        if (!Objects.equals(obj1, obj2)) {
            throw new ParamIllegalException(notEqualsMsg);
        }
    }

    /**
     * 检查两个对象是否不相等。如果对象相等，则抛出一个带有指定错误消息的 {@link ParamIllegalException} 异常。
     *
     * @param obj1      要比较的第一个对象。
     * @param obj2      要比较的第二个对象。
     * @param equalsMsg 如果两个对象相等，则在异常中使用的错误消息。
     * @throws ParamIllegalException 如果两个对象相等。
     */
    public static void requireNotEquals(Object obj1,
                                        Object obj2,
                                        String equalsMsg) {
        if (Objects.equals(obj1, obj2)) {
            throw new ParamIllegalException(equalsMsg);
        }
    }


    /**
     * 检查值是否为null，如果为null，则抛出ParamIllegalException异常。
     *
     * @param value     要检查的值
     * @param ifNullMsg 异常消息
     * @param <T>       值的类型
     * @return 值本身（非null）
     * @throws ParamIllegalException 如果值为null
     */
    public static <T> T requireNotNull(T value,
                                       String ifNullMsg) {
        if (null == value) {
            throw new ParamIllegalException(ifNullMsg);
        }
        return value;
    }

    /**
     * 检查提供的集合是否不为空（不为null且包含至少一个元素）。如果集合为空，则抛出一个带有指定错误消息的 {@link ParamIllegalException} 异常。
     *
     * @param collection 要检查是否为空的集合。
     * @param ifEmptyMsg 如果集合为空，则在异常中使用的错误消息。
     * @param <E>        集合的元素类型。
     * @return 非空的集合。
     * @throws ParamIllegalException 如果提供的集合为空。
     */
    public static <E> List<E> requireNotEmpty(List<E> collection,
                                              String ifEmptyMsg) {
        if (CollectionUtil.isEmpty(collection)) {
            throw new ParamIllegalException(ifEmptyMsg);
        }
        return collection;
    }

    /**
     * 检查提供的集合是否为空（为null或不包含任何元素）。如果集合不为空，则抛出一个带有指定错误消息的 {@link ParamIllegalException} 异常。
     *
     * @param collection    要检查是否为空的集合。
     * @param ifNotEmptyMsg 如果集合不为空，则在异常中使用的错误消息。
     * @param <E>           集合的元素类型。
     * @throws ParamIllegalException 如果提供的集合不为空。
     */
    public static <E> List<E> requireEmpty(List<E> collection, String ifNotEmptyMsg) {
        if (CollectionUtil.isNotEmpty(collection)) {
            throw new ParamIllegalException(ifNotEmptyMsg);
        }
        return collection;
    }

    /**
     * 检查提供的集合是否不为空（不为null且包含至少一个元素）。如果集合为空，则抛出一个带有指定错误消息的 {@link ParamIllegalException} 异常。
     *
     * @param collection 要检查是否为空的集合。
     * @param ifEmptyMsg 如果集合为空，则在异常中使用的错误消息。
     * @param <E>        集合的元素类型。
     * @return 非空的集合。
     * @throws ParamIllegalException 如果提供的集合为空。
     */
    public static <E> Set<E> requireNotEmpty(Set<E> collection,
                                             String ifEmptyMsg) {
        if (CollectionUtil.isEmpty(collection)) {
            throw new ParamIllegalException(ifEmptyMsg);
        }
        return collection;
    }

    /**
     * 检查提供的集合是否包含指定元素。如果集合为空或不包含指定元素，则抛出一个带有指定错误消息的 {@link ParamIllegalException} 异常。
     *
     * @param collection 要检查是否包含指定元素的集合。
     * @param element    要检查是否包含的元素。
     * @param errorMsg   如果集合为空或不包含指定元素，则在异常中使用的错误消息。
     * @param <E>        集合的元素类型。
     * @return 要检查是否包含的元素。
     * @throws ParamIllegalException 如果提供的集合为空或不包含指定元素。
     */
    public static <E> E requireContains(E element,
                                        List<E> collection,
                                        String errorMsg) {
        if (CollectionUtil.isEmpty(collection) || !collection.contains(element)) {
            throw new ParamIllegalException(errorMsg);
        }
        return element;
    }

    /**
     * 检查提供的集合是否不包含指定元素。如果集合为空或包含指定元素，则抛出一个带有指定错误消息的 {@link ParamIllegalException} 异常。
     *
     * @param element    要检查是否包含的元素。
     * @param collection 要检查是否包含指定元素的集合。
     * @param errorMsg   如果集合为空或包含指定元素，则在异常中使用的错误消息。
     * @param <E>        集合的元素类型。
     * @return 要检查是否包含的元素。
     * @throws ParamIllegalException 如果提供的集合为空或包含指定元素。
     */
    public static <E> E requireNotContains(E element,
                                           List<E> collection,
                                           String errorMsg) {
        if (CollectionUtil.isEmpty(collection) || collection.contains(element)) {
            throw new ParamIllegalException(errorMsg);
        }
        return element;
    }


    /**
     * 检查提供的字符串是否不为空且不为空白。如果字符串为空或为空白，则抛出一个带有指定错误消息的 {@link ParamIllegalException} 异常。
     *
     * @param str      要检查的字符串。
     * @param errorMsg 如果字符串为空或为空白，则在异常中使用的错误消息。
     * @return 非空且非空白的字符串。
     * @throws ParamIllegalException 如果提供的字符串为空或为空白。
     */
    public static String requireNotBlank(String str,
                                         String errorMsg) {
        if (StrUtil.isBlank(str)) {
            throw new ParamIllegalException(errorMsg);
        }
        return str;
    }

}
