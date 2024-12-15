package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2024/8/8 15:18
 */
@Getter
@AllArgsConstructor
public enum OrderSource implements IRemark {
    // 下单方式：SCM(scm系统),WMS_REPLENISH(wms备货中心-轮询监控页面),WMS_REPLENISH_RECOMMEND(wms备货中心-推荐下单页面),
    SCM("scm系统"),
    WMS_REPLENISH("wms备货中心-轮询监控页面"),
    WMS_REPLENISH_RECOMMEND("wms备货中心-推荐下单页面"),
    ;
    private final String remark;
}
