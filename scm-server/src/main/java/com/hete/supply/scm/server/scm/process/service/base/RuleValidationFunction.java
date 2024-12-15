package com.hete.supply.scm.server.scm.process.service.base;

/**
 * @author yanjiawei
 * Created on 2023/12/21.
 */
@FunctionalInterface
public interface RuleValidationFunction {
    /**
     * 函数式接口，用于执行验证逻辑并根据结果抛出异常。
     *
     * @param errorMessage 验证失败时的错误消息
     */
    void validate(String errorMessage);
}
