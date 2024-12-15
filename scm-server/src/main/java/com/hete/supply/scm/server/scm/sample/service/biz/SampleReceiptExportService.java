package com.hete.supply.scm.server.scm.sample.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.SampleReceiptSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.SampleReceiptExportVo;
import com.hete.supply.scm.server.scm.sample.dao.SampleReceiptOrderDao;
import com.hete.supply.scm.server.scm.sample.service.base.SampleReceiptBaseService;
import com.hete.support.api.result.CommonPageResult;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/12 23:46
 */
@Service
@RequiredArgsConstructor
public class SampleReceiptExportService {
    private final SampleReceiptBaseService sampleReceiptBaseService;
    private final SampleReceiptOrderDao sampleReceiptOrderDao;


    public Integer getExportTotals(SampleReceiptSearchDto dto) {
        final List<String> sampleReceiptOrderNoList = sampleReceiptBaseService.getSampleReceiptOrderNoList(dto);
        if ((StringUtils.isNotBlank(dto.getSampleChildOrderNo()) || StringUtils.isNotBlank(dto.getSpu()))
                && CollectionUtils.isEmpty(sampleReceiptOrderNoList)) {
            return 0;
        }

        return sampleReceiptOrderDao.getExportTotals(dto, sampleReceiptOrderNoList);
    }

    public CommonPageResult.PageInfo<SampleReceiptExportVo> getExportList(SampleReceiptSearchDto dto) {
        final List<String> sampleReceiptOrderNoList = sampleReceiptBaseService.getSampleReceiptOrderNoList(dto);
        if ((StringUtils.isNotBlank(dto.getSampleChildOrderNo()) || StringUtils.isNotBlank(dto.getSpu()))
                && CollectionUtils.isEmpty(sampleReceiptOrderNoList)) {
            return new CommonPageResult.PageInfo<>();
        }

        return sampleReceiptOrderDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto, sampleReceiptOrderNoList);
    }
}
