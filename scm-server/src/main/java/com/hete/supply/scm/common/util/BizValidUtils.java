package com.hete.supply.scm.common.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.hete.support.api.exception.BizException;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/1/31.
 */
public class BizValidUtils {

    /**
     * 检查值是否为null，如果为null，则抛出BizException异常。
     *
     * @param value     要检查的值
     * @param ifNullMsg 异常消息
     * @param <T>       值的类型
     * @return 值本身（非null）
     * @throws BizException 如果值为null
     */
    public static <T> T requireNotNull(T value,
                                       String ifNullMsg) {
        if (null == value) {
            throw new BizException(ifNullMsg);
        }
        return value;
    }

    /**
     * 检查提供的字符串是否不为空且不为空白。如果字符串为空或为空白，则抛出一个带有指定错误消息的 {@link BizException} 异常。
     *
     * @param str      要检查的字符串。
     * @param errorMsg 如果字符串为空或为空白，则在异常中使用的错误消息。
     * @return 非空且非空白的字符串。
     * @throws BizException 如果提供的字符串为空或为空白。
     */
    public static String requireNotBlank(String str,
                                         String errorMsg) {
        if (StrUtil.isBlank(str)) {
            throw new BizException(errorMsg);
        }
        return str;
    }

    /**
     * 检查提供的集合是否不为空（不为null且包含至少一个元素）。如果集合为空，则抛出一个带有指定错误消息的 {@link BizException} 异常。
     *
     * @param collection 要检查是否为空的集合。
     * @param ifEmptyMsg 如果集合为空，则在异常中使用的错误消息。
     * @param <E>        集合的元素类型。
     * @return 非空的集合。
     * @throws BizException 如果提供的集合为空。
     */
    public static <E> List<E> requireNotEmpty(List<E> collection,
                                              String ifEmptyMsg) {
        if (CollectionUtil.isEmpty(collection)) {
            throw new BizException(ifEmptyMsg);
        }
        return collection;
    }
}
