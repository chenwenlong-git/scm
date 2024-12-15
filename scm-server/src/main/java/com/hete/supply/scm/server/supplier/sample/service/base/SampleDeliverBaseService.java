package com.hete.supply.scm.server.supplier.sample.service.base;

import com.hete.supply.scm.server.supplier.sample.converter.SupplierSampleConverter;
import com.hete.supply.scm.server.supplier.sample.dao.SampleDeliverOrderDao;
import com.hete.supply.scm.server.supplier.sample.dao.SampleDeliverOrderItemDao;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleDeliverOrderItemPo;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleDeliverOrderPo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleDeliverSimpleVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/12 17:28
 */
@Service
@RequiredArgsConstructor
@Validated
public class SampleDeliverBaseService {
    private final SampleDeliverOrderDao sampleDeliverOrderDao;
    private final SampleDeliverOrderItemDao sampleDeliverOrderItemDao;


    public void insertPo(SampleDeliverOrderPo po) {
        sampleDeliverOrderDao.insert(po);
    }

    public void batchInsertItemPo(List<SampleDeliverOrderItemPo> sampleDeliverOrderItemPoList) {
        sampleDeliverOrderItemDao.insertBatch(sampleDeliverOrderItemPoList);
    }

    public List<SampleDeliverSimpleVo> getSampleSimpleListByChildList(List<String> childOrderNoList) {
        List<SampleDeliverOrderItemPo> sampleDeliverOrderItemPoList = sampleDeliverOrderItemDao.getListByChildOrderNoList(childOrderNoList);
        final List<String> sampleDeliverOrderNoList = sampleDeliverOrderItemPoList.stream()
                .map(SampleDeliverOrderItemPo::getSampleDeliverOrderNo)
                .collect(Collectors.toList());

        List<SampleDeliverOrderPo> sampleDeliverOrderPoList = sampleDeliverOrderDao.getDeliverPoListByNoList(sampleDeliverOrderNoList);

        return SupplierSampleConverter.deliverPoListToSimpleVoList(sampleDeliverOrderPoList);
    }
}
