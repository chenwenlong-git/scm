package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * 加工单答交时间延期刷新业务对象。
 *
 * @author yanjiawei
 * Created on 2024/4/12.
 */
@Data
public class RefreshPromiseDateDelayedBo {

    @ApiModelProperty(value = "加工单号")
    private Set<String> processOrderNos;
}
