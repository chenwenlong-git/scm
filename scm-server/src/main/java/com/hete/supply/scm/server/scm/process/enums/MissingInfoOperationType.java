package com.hete.supply.scm.server.scm.process.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 缺失信息操作类型
 *
 * @author yanjiawei
 * @date 2023年08月04日 08:57
 */
@Getter
@AllArgsConstructor
public enum MissingInfoOperationType implements IRemark {
    /**
     * @Description 指的是校验加工单的原料是否存在，如果不存在则会通过策略获取原料并保存，
     * 同时校验库存，如果原料库存都存在则会生成原料出库单。
     */
    PERFORM_MATERIAL_OPERATIONS("原料操作"),

    /**
     * @Description 指的是通过策略获取原料并更新，覆盖之前的原料信息
     */
    PERFORM_MATERIAL_UPDATE("原料覆盖更新"),

    /**
     * @Description 指的是校验加工单的工序是否存在，如果不存在则会通过策略获取工序并保存。
     */
    PERFORM_PROCEDURE_OPERATIONS("工序操作"),

    /**
     * @Description 指的是把原料刷新和工序刷新的逻辑合并起来。
     */
    PERFORM_MATERIAL_PROCEDURE_OPERATIONS("原料工序操作"),

    /**
     * @Description 指的是检查加工单有无原料，如果无则更新缺失信息=无原料。
     */
    CHECK_MATERIAL("检查原料"),

    /**
     * @Description 指的是检查加工单有工序信息，如果无则更新缺失信息=无工序信息。
     */
    CHECK_PROCEDURE("检查工序"),

    /**
     * @Description 指的是把原料检查和工序检查的逻辑合并起来。
     */
    CHECK_MATERIAL_PROCEDURE("检查原料工序是否存在");
    private final String remark;
}
