package com.hete.supply.scm.server.scm.qc.converter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.qc.entity.bo.OnShelvesOrderBo;
import com.hete.supply.wms.api.entry.entity.vo.OnShelvesOrderScmVo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2023/10/20.
 */
public class OnShelvesOrderConverter {

    public static List<OnShelvesOrderBo> toOnShelvesOrderBos(List<OnShelvesOrderScmVo.OnShelvesOrder> onShelvesOrderByQcOrderNos) {
        if (CollectionUtils.isEmpty(onShelvesOrderByQcOrderNos)) {
            return Collections.emptyList();
        }

        return onShelvesOrderByQcOrderNos.stream().map(onShelvesOrder -> {
            OnShelvesOrderBo bo = new OnShelvesOrderBo();
            bo.setQcOrderNo(onShelvesOrder.getQcOrderNo());
            bo.setOnShelvesOrderNo(onShelvesOrder.getOnShelvesOrderNo());
            return bo;
        }).collect(Collectors.toList());
    }
}
