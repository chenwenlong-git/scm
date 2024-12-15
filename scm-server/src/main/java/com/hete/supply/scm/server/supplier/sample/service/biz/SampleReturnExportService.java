package com.hete.supply.scm.server.supplier.sample.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.SampleReturnDto;
import com.hete.supply.scm.api.scm.entity.vo.SampleReturnExportVo;
import com.hete.supply.scm.server.supplier.sample.dao.SampleReturnOrderDao;
import com.hete.supply.scm.server.supplier.sample.dao.SampleReturnOrderItemDao;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderItemPo;
import com.hete.support.api.result.CommonPageResult;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/12/18 15:55
 */
@Service
@RequiredArgsConstructor
public class SampleReturnExportService {
    private final SampleReturnOrderDao sampleReturnOrderDao;
    private final SampleReturnOrderItemDao sampleReturnOrderItemDao;

    public Integer getExportTotals(SampleReturnDto dto) {
        List<String> sampleReturnOrderNoList = new ArrayList<>();

        if (StringUtils.isNotBlank(dto.getSampleChildOrderNo())) {
            List<SampleReturnOrderItemPo> sampleReturnOrderItemPoList = sampleReturnOrderItemDao.getListLikeChildOrderNo(dto.getSampleChildOrderNo());
            final List<String> itemReturnOrderNoList = sampleReturnOrderItemPoList.stream()
                    .map(SampleReturnOrderItemPo::getSampleReturnOrderNo)
                    .distinct()
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(itemReturnOrderNoList)) {
                return 0;
            }

            sampleReturnOrderNoList.addAll(itemReturnOrderNoList);
        }

        return sampleReturnOrderDao.getExportTotals(dto, sampleReturnOrderNoList);
    }

    public CommonPageResult.PageInfo<SampleReturnExportVo> getExportList(SampleReturnDto dto) {
        List<String> sampleReturnOrderNoList = new ArrayList<>();

        if (StringUtils.isNotBlank(dto.getSampleChildOrderNo())) {
            List<SampleReturnOrderItemPo> sampleReturnOrderItemPoList = sampleReturnOrderItemDao.getListLikeChildOrderNo(dto.getSampleChildOrderNo());
            final List<String> itemReturnOrderNoList = sampleReturnOrderItemPoList.stream()
                    .map(SampleReturnOrderItemPo::getSampleReturnOrderNo)
                    .distinct()
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(itemReturnOrderNoList)) {
                return new CommonPageResult.PageInfo<>();
            }

            sampleReturnOrderNoList.addAll(itemReturnOrderNoList);
        }
        return sampleReturnOrderDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto, sampleReturnOrderNoList);
    }
}
