package com.hete.supply.scm.server.supplier.sample.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.SampleDeliverSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.SampleDeliverExportVo;
import com.hete.supply.scm.server.supplier.sample.dao.SampleDeliverOrderDao;
import com.hete.supply.scm.server.supplier.sample.dao.SampleDeliverOrderItemDao;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleDeliverOrderItemPo;
import com.hete.support.api.result.CommonPageResult;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/12/18 23:32
 */
@Service
@RequiredArgsConstructor
public class SampleDeliverExportBizService {
    private final SampleDeliverOrderDao sampleDeliverOrderDao;
    private final SampleDeliverOrderItemDao sampleDeliverOrderItemDao;

    public Integer getExportTotals(SampleDeliverSearchDto dto) {
        List<String> deliverNoList = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getSampleParentOrderNo())) {
            List<SampleDeliverOrderItemPo> sampleDeliverOrderItemPoList = sampleDeliverOrderItemDao.getListByLikeParentNo(dto.getSampleParentOrderNo());
            if (CollectionUtils.isEmpty(sampleDeliverOrderItemPoList)) {
                return 0;
            }
            deliverNoList.addAll(sampleDeliverOrderItemPoList.stream()
                    .map(SampleDeliverOrderItemPo::getSampleDeliverOrderNo)
                    .collect(Collectors.toList()));
        }

        if (StringUtils.isNotBlank(dto.getSampleChildOrderNo())) {
            List<SampleDeliverOrderItemPo> sampleDeliverOrderItemPoList = sampleDeliverOrderItemDao.getListByLikeChildNo(dto.getSampleChildOrderNo());
            if (CollectionUtils.isEmpty(sampleDeliverOrderItemPoList)) {
                return 0;
            }
            deliverNoList.addAll(sampleDeliverOrderItemPoList.stream()
                    .map(SampleDeliverOrderItemPo::getSampleDeliverOrderNo)
                    .collect(Collectors.toList()));
        }
        return sampleDeliverOrderDao.getExportTotals(dto, deliverNoList);
    }

    public CommonPageResult.PageInfo<SampleDeliverExportVo> getExportList(SampleDeliverSearchDto dto) {
        List<String> deliverNoList = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getSampleParentOrderNo())) {
            List<SampleDeliverOrderItemPo> sampleDeliverOrderItemPoList = sampleDeliverOrderItemDao.getListByLikeParentNo(dto.getSampleParentOrderNo());
            if (CollectionUtils.isEmpty(sampleDeliverOrderItemPoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            deliverNoList.addAll(sampleDeliverOrderItemPoList.stream()
                    .map(SampleDeliverOrderItemPo::getSampleDeliverOrderNo)
                    .collect(Collectors.toList()));
        }

        if (StringUtils.isNotBlank(dto.getSampleChildOrderNo())) {
            List<SampleDeliverOrderItemPo> sampleDeliverOrderItemPoList = sampleDeliverOrderItemDao.getListByLikeChildNo(dto.getSampleChildOrderNo());
            if (CollectionUtils.isEmpty(sampleDeliverOrderItemPoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            deliverNoList.addAll(sampleDeliverOrderItemPoList.stream()
                    .map(SampleDeliverOrderItemPo::getSampleDeliverOrderNo)
                    .collect(Collectors.toList()));
        }
        return sampleDeliverOrderDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto, deliverNoList);
    }
}
