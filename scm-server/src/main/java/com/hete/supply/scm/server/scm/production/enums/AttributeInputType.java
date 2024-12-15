package com.hete.supply.scm.server.scm.production.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author yanjiawei
 * Created on 2024/9/14.
 */
@AllArgsConstructor
@Getter
public enum AttributeInputType implements IRemark {
    //单选下垃
    SINGLE_SELECT("单选下拉"),
    //多选下拉
    MULTIPLE_SELECT("多选下拉"),
    //单行文本框
    SINGLE_LINE_TEXT("单行文本框"),
    //多行文本框
    MULTIPLE_LINE_TEXT("多行文本框"),
    //图片
    IMAGE("图片"),
    ;

    private final String desc;

    public static AttributeInputType getByDesc(String desc) {
        return Arrays.stream(AttributeInputType.values())
                .filter(item -> item.getDesc().equals(desc))
                .findFirst().orElse(null);
    }

    @Override
    public String getRemark() {
        return this.desc;
    }
}
