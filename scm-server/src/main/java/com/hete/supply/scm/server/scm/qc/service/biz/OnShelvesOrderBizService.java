package com.hete.supply.scm.server.scm.qc.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.qc.converter.OnShelvesOrderConverter;
import com.hete.supply.scm.server.scm.qc.entity.bo.OnShelvesOrderBo;
import com.hete.supply.wms.api.entry.entity.vo.OnShelvesOrderScmVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 仓储服务上架单服务类
 *
 * @author yanjiawei
 * Created on 2023/10/13.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class OnShelvesOrderBizService {

    private final WmsRemoteService wmsRemoteService;

    /**
     * 根据一组质检单号列表，获取相应的上架单信息。
     *
     * @param qcOrderNos 一组质检单号，用于检索对应的上架单信息。
     * @return 包含上架单信息的 OnShelvesOrderBo 对象列表。
     */
    public List<OnShelvesOrderBo> getOnShelvesOrderByQcOrderNos(Collection<String> qcOrderNos) {
        if (CollectionUtil.isEmpty(qcOrderNos)) {
            return Collections.emptyList();
        }
        List<OnShelvesOrderScmVo.OnShelvesOrder> onShelvesOrderByQcOrderNos = wmsRemoteService.getOnShelvesOrderByQcOrderNos(qcOrderNos);
        return OnShelvesOrderConverter.toOnShelvesOrderBos(onShelvesOrderByQcOrderNos);
    }

    /**
     * 根据上架单号查询对应的上架单信息。
     *
     * @param onShelvesOrderNo 上架单号，用于检索相应的上架单信息。
     * @return 包含上架单信息的 OnShelvesOrderBo 对象，如果未找到匹配的上架单则返回 null。
     */
    public OnShelvesOrderBo getOnShelvesOrderByOnShelvesOrderNo(String onShelvesOrderNo) {
        if (StrUtil.isBlank(onShelvesOrderNo)) {
            return null;
        }
        List<OnShelvesOrderScmVo.OnShelvesOrder> onShelvesOrderByQcOrders
                = wmsRemoteService.getOnShelvesOrderByOnShelvesOrderNos(Collections.singleton(onShelvesOrderNo));
        if (CollectionUtil.isEmpty(onShelvesOrderByQcOrders)) {
            return null;
        }

        final OnShelvesOrderScmVo.OnShelvesOrder onShelvesOrder = onShelvesOrderByQcOrders.stream()
                .filter(onShelvesOrderByQcOrder -> Objects.equals(onShelvesOrderNo, onShelvesOrderByQcOrder.getOnShelvesOrderNo())).findFirst().orElse(null);
        if (Objects.isNull(onShelvesOrder)) {
            return null;
        }

        List<OnShelvesOrderScmVo.OnShelvesOrder> onShelvesOrderByQcOrderVoList = Lists.newArrayList();
        onShelvesOrderByQcOrderVoList.add(onShelvesOrder);
        return OnShelvesOrderConverter.toOnShelvesOrderBos(onShelvesOrderByQcOrderVoList).stream().findFirst().orElse(null);
    }

    /**
     * 根据一组上架单号列表，获取相应的上架单信息列表。
     *
     * @param onShelvesOrderNos 一组上架单号，用于检索对应的上架单信息。
     * @return 包含上架单信息的 OnShelvesOrderBo 对象列表。如果未找到匹配的上架单则返回空列表。
     */
    public List<OnShelvesOrderBo> getOnShelvesOrderByOnShelvesOrderNos(Collection<String> onShelvesOrderNos) {
        if (CollectionUtil.isEmpty(onShelvesOrderNos)) {
            return Collections.emptyList();
        }
        List<OnShelvesOrderScmVo.OnShelvesOrder> onShelvesOrderByQcOrderNos = wmsRemoteService.getOnShelvesOrderByOnShelvesOrderNos(onShelvesOrderNos);
        return OnShelvesOrderConverter.toOnShelvesOrderBos(onShelvesOrderByQcOrderNos);
    }
}
