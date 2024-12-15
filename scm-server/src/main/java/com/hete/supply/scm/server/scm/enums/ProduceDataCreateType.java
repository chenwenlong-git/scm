package com.hete.supply.scm.server.scm.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2023/9/20 16:35
 */
@Getter
@AllArgsConstructor
public enum ProduceDataCreateType implements IRemark {
    //生产资料创建类型
    CREATE("创建"),
    EDIT("编辑"),
    COPY("快速复制");

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }

}
