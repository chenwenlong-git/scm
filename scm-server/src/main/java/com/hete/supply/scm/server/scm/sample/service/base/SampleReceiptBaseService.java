package com.hete.supply.scm.server.scm.sample.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.SampleReceiptSearchDto;
import com.hete.supply.scm.server.scm.sample.dao.SampleReceiptOrderDao;
import com.hete.supply.scm.server.scm.sample.dao.SampleReceiptOrderItemDao;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleReceiptOrderItemPo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleReceiptOrderPo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleReceiptSimpleVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/13 22:44
 */
@Service
@RequiredArgsConstructor
@Validated
public class SampleReceiptBaseService {
    private final SampleReceiptOrderDao sampleReceiptOrderDao;
    private final SampleReceiptOrderItemDao sampleReceiptOrderItemDao;

    public void insertReceiptPo(SampleReceiptOrderPo sampleReceiptOrderPo) {
        sampleReceiptOrderDao.insert(sampleReceiptOrderPo);
    }

    public void insertBatchReceiptItemPoList(List<SampleReceiptOrderItemPo> sampleReceiptOrderItemPoList) {
        sampleReceiptOrderItemDao.insertBatch(sampleReceiptOrderItemPoList);
    }

    public List<SampleReceiptSimpleVo> getSimpleVoListByChildList(List<String> childOrderNoList) {

        List<SampleReceiptOrderItemPo> sampleReceiptOrderItemPoList = sampleReceiptOrderItemDao.getListByChildOrderNoList(childOrderNoList);

        final List<String> receiptOrderNoList = sampleReceiptOrderItemPoList.stream()
                .map(SampleReceiptOrderItemPo::getSampleReceiptOrderNo)
                .collect(Collectors.toList());
        List<SampleReceiptOrderPo> sampleReceiptOrderPoList = sampleReceiptOrderDao.getListByReceiptNoList(receiptOrderNoList);
        final Map<String, SampleReceiptOrderPo> receiptMap = sampleReceiptOrderPoList.stream()
                .collect(Collectors.toMap(SampleReceiptOrderPo::getSampleReceiptOrderNo, Function.identity()));

        return sampleReceiptOrderItemPoList.stream()
                .map(po -> {
                    final SampleReceiptOrderPo sampleReceiptOrderPo = receiptMap.get(po.getSampleReceiptOrderNo());
                    final SampleReceiptSimpleVo sampleReceiptSimpleVo = new SampleReceiptSimpleVo();
                    sampleReceiptSimpleVo.setSampleReceiptOrderNo(po.getSampleReceiptOrderNo());
                    sampleReceiptSimpleVo.setReceiptOrderStatus(sampleReceiptOrderPo.getReceiptOrderStatus());
                    sampleReceiptSimpleVo.setReceiptTime(sampleReceiptOrderPo.getReceiptTime());
                    sampleReceiptSimpleVo.setTotalReceipt(po.getReceiptCnt());
                    return sampleReceiptSimpleVo;
                }).collect(Collectors.toList());
    }

    public List<String> getSampleReceiptOrderNoList(SampleReceiptSearchDto dto) {
        final List<String> sampleReceiptOrderNoList = new ArrayList<>();

        if (StringUtils.isNotBlank(dto.getSampleChildOrderNo())) {
            List<SampleReceiptOrderItemPo> sampleReceiptOrderItemPoList = sampleReceiptOrderItemDao.getListBySampleChildOrderNo(dto.getSampleChildOrderNo());
            if (CollectionUtils.isEmpty(sampleReceiptOrderItemPoList)) {
                return Collections.emptyList();
            }
            sampleReceiptOrderNoList.addAll(sampleReceiptOrderItemPoList.stream()
                    .map(SampleReceiptOrderItemPo::getSampleReceiptOrderNo)
                    .distinct()
                    .collect(Collectors.toList()));
        }

        if (StringUtils.isNotBlank(dto.getSpu())) {
            List<SampleReceiptOrderItemPo> sampleReceiptOrderItemPoList = sampleReceiptOrderItemDao.getListBySpu(dto.getSpu());
            if (CollectionUtils.isEmpty(sampleReceiptOrderItemPoList)) {
                return Collections.emptyList();
            }
            sampleReceiptOrderNoList.addAll(sampleReceiptOrderItemPoList.stream()
                    .map(SampleReceiptOrderItemPo::getSampleReceiptOrderNo)
                    .distinct()
                    .collect(Collectors.toList()));
        }

        if (StringUtils.isNotBlank(dto.getSampleParentOrderNo())) {
            List<SampleReceiptOrderItemPo> sampleReceiptOrderItemPoList = sampleReceiptOrderItemDao.getListByParentNo(dto.getSampleParentOrderNo());
            if (CollectionUtils.isEmpty(sampleReceiptOrderItemPoList)) {
                return Collections.emptyList();
            }
            sampleReceiptOrderNoList.addAll(sampleReceiptOrderItemPoList.stream()
                    .map(SampleReceiptOrderItemPo::getSampleReceiptOrderNo)
                    .distinct()
                    .collect(Collectors.toList()));
        }

        return sampleReceiptOrderNoList;
    }

    public SampleReceiptOrderItemPo getLatestDeliverPoByChildNo(String sampleChildOrderNo) {
        return sampleReceiptOrderItemDao.getLatestDeliverPoByChildNo(sampleChildOrderNo);
    }
}
