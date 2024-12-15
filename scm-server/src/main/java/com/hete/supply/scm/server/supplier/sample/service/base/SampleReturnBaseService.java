package com.hete.supply.scm.server.supplier.sample.service.base;

import com.hete.supply.scm.server.supplier.sample.dao.SampleReturnOrderDao;
import com.hete.supply.scm.server.supplier.sample.dao.SampleReturnOrderItemDao;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderPo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleReturnSimpleVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/22 11:49
 */
@Service
@RequiredArgsConstructor
@Validated
public class SampleReturnBaseService {
    private final SampleReturnOrderDao sampleReturnOrderDao;
    private final SampleReturnOrderItemDao sampleReturnOrderItemDao;

    public List<SampleReturnSimpleVo> getSimpleVoListByChildList(List<String> childOrderNoList) {

        List<SampleReturnOrderItemPo> sampleReturnOrderItemPoList = sampleReturnOrderItemDao.getListByChildOrderNoList(childOrderNoList);
        final List<String> sampleReturnOrderNoList = sampleReturnOrderItemPoList.stream()
                .map(SampleReturnOrderItemPo::getSampleReturnOrderNo)
                .collect(Collectors.toList());
        final List<SampleReturnOrderPo> sampleReturnOrderPoList = sampleReturnOrderDao.getListByNoList(sampleReturnOrderNoList);

        return sampleReturnOrderPoList.stream()
                .map(po -> {
                    final SampleReturnSimpleVo sampleReturnSimpleVo = new SampleReturnSimpleVo();
                    sampleReturnSimpleVo.setSampleReturnOrderNo(po.getSampleReturnOrderNo());
                    sampleReturnSimpleVo.setReturnOrderStatus(po.getReturnOrderStatus());
                    sampleReturnSimpleVo.setCreateTime(po.getCreateTime());
                    sampleReturnSimpleVo.setReturnCnt(po.getReturnCnt());
                    sampleReturnSimpleVo.setTrackingNo(po.getTrackingNo());
                    sampleReturnSimpleVo.setLogistics(po.getLogistics());
                    return sampleReturnSimpleVo;
                }).collect(Collectors.toList());
    }
}
