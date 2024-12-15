package com.hete.supply.scm.server.scm.sample.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.SamplePurchaseSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.SampleSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.SampleDevType;
import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.SampleChildExportVo;
import com.hete.supply.scm.api.scm.entity.vo.SampleParentExportVo;
import com.hete.supply.scm.remote.dubbo.SdaRemoteService;
import com.hete.supply.scm.server.scm.sample.dao.SampleChildOrderDao;
import com.hete.supply.scm.server.scm.sample.dao.SampleParentOrderDao;
import com.hete.supply.scm.server.scm.sample.service.base.SampleBaseService;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.result.CommonPageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/12/12 09:12
 */
@Service
@RequiredArgsConstructor
public class SampleExportService {
    private final SampleChildOrderDao sampleChildOrderDao;
    private final SampleParentOrderDao sampleParentOrderDao;
    private final SampleBaseService sampleBaseService;
    private final SdaRemoteService sdaRemoteService;

    public Integer getSampleParentExportTotals(SampleSearchDto dto) {
        List<String> skuParentOrderNoList = sampleBaseService.getSkuParentOrderNoList(dto);
        if (CollectionUtils.isNotEmpty(dto.getSkuList()) && CollectionUtils.isEmpty(skuParentOrderNoList)) {
            return 0;
        }

        return sampleParentOrderDao.getSampleParentExportTotals(dto, skuParentOrderNoList);
    }

    public CommonPageResult.PageInfo<SampleParentExportVo> getSampleParentExportList(SampleSearchDto dto) {
        List<String> skuParentOrderNoList = sampleBaseService.getSkuParentOrderNoList(dto);
        if (CollectionUtils.isNotEmpty(dto.getSkuList()) && CollectionUtils.isEmpty(skuParentOrderNoList)) {
            return new CommonPageResult.PageInfo<>();
        }

        final CommonPageResult.PageInfo<SampleParentExportVo> exportList = sampleParentOrderDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto, skuParentOrderNoList);
        final List<SampleParentExportVo> records = exportList.getRecords();
        // 获取平台名称
        final List<String> platCodeList = records.stream()
                .map(SampleParentExportVo::getPlatform)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);
        records.forEach(record -> {
            record.setSampleOrderStatus(SampleOrderStatus.valueOf(record.getSampleOrderStatus()).getRemark());
            if (StringUtils.isNotBlank(record.getSampleDevType())) {
                record.setSampleDevType(SampleDevType.valueOf(record.getSampleDevType()).getRemark());
            }
            record.setIsFirstOrder(BooleanType.valueOf(record.getIsFirstOrder()).getValue());
            record.setIsUrgentOrder(BooleanType.valueOf(record.getIsUrgentOrder()).getValue());
            record.setPlatform(platCodeNameMap.get(record.getPlatform()));
        });
        return exportList;
    }

    public Integer getSampleChildExportTotals(SamplePurchaseSearchDto dto) {
        //条件过滤
        if (null == sampleBaseService.getSearchSampleChildWhere(dto)) {
            return 0;
        }
        return sampleChildOrderDao.getSampleChildExportTotals(dto);
    }

    public CommonPageResult.PageInfo<SampleChildExportVo> getSampleChildExportList(SamplePurchaseSearchDto dto) {
        //条件过滤
        if (null == sampleBaseService.getSearchSampleChildWhere(dto)) {
            return new CommonPageResult.PageInfo<>();
        }
        final CommonPageResult.PageInfo<SampleChildExportVo> sampleChildExportList = sampleChildOrderDao.getSampleChildExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<SampleChildExportVo> records = sampleChildExportList.getRecords();
        // 获取平台名称
        final List<String> platCodeList = records.stream()
                .map(SampleChildExportVo::getPlatform)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);
        records.forEach(record -> {
            record.setSampleOrderStatus(SampleOrderStatus.valueOf(record.getSampleOrderStatus()).getRemark());
            record.setPlatform(platCodeNameMap.get(record.getPlatform()));
        });
        return sampleChildExportList;
    }
}
