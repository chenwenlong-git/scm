package com.hete.supply.scm.server.scm.develop.service.base;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.DevelopPricingOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.DevelopPricingOrderExportVo;
import com.hete.supply.scm.remote.dubbo.SdaRemoteService;
import com.hete.supply.scm.server.scm.develop.dao.DevelopPricingOrderDao;
import com.hete.supply.scm.server.scm.develop.dao.DevelopPricingOrderInfoDao;
import com.hete.supply.scm.server.scm.develop.dao.DevelopSampleOrderDao;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopPricingOrderInfoByPriceTimeBo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPricingOrderInfoPo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderPo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2023/8/7 14:34
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class DevelopPricingOrderBaseService {

    private final DevelopPricingOrderInfoDao developPricingOrderInfoDao;
    private final DevelopPricingOrderDao developPricingOrderDao;
    private final DevelopSampleOrderDao developSampleOrderDao;
    private final SdaRemoteService sdaRemoteService;


    /**
     * 搜索条件
     *
     * @param dto:
     * @return DevelopPricingOrderSearchDto
     * @author ChenWenLong
     * @date 2023/8/7 14:59
     */
    public DevelopPricingOrderSearchDto getSearchWhere(DevelopPricingOrderSearchDto dto) {
        if (CollectionUtils.isEmpty(dto.getDevelopPricingOrderNoList())) {
            dto.setDevelopPricingOrderNoList(new ArrayList<>());
        }

        if (StringUtils.isNotBlank(dto.getDevelopSampleOrderNo())) {
            List<DevelopPricingOrderInfoPo> developPricingOrderInfoPoList = developPricingOrderInfoDao.getListByLikeDevelopSampleOrderNo(dto.getDevelopSampleOrderNo());
            if (CollectionUtils.isEmpty(developPricingOrderInfoPoList)) {
                return null;
            }
            List<String> developPricingOrderNoList = developPricingOrderInfoPoList.stream().map(DevelopPricingOrderInfoPo::getDevelopPricingOrderNo).distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(dto.getDevelopPricingOrderNoList())) {
                dto.getDevelopPricingOrderNoList().addAll(developPricingOrderNoList);
            } else {
                dto.getDevelopPricingOrderNoList().retainAll(developPricingOrderNoList);
            }
        }

        return dto;
    }

    /**
     * 导出统计
     *
     * @param dto:
     * @return Integer
     * @author ChenWenLong
     * @date 2023/8/7 18:37
     */
    public Integer getExportTotals(DevelopPricingOrderSearchDto dto) {
        //条件过滤
        if (null == this.getSearchWhere(dto)) {
            throw new ParamIllegalException("导出数据为空");
        }
        Integer exportTotals = developPricingOrderDao.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        return exportTotals;
    }

    /**
     * 导出列表
     *
     * @param dto:
     * @return CommonResult<ExportationListResultBo < DevelopPricingOrderExportVo>>
     * @author ChenWenLong
     * @date 2023/8/7 18:37
     */
    public CommonResult<ExportationListResultBo<DevelopPricingOrderExportVo>> getExportList(DevelopPricingOrderSearchDto dto) {
        ExportationListResultBo<DevelopPricingOrderExportVo> resultBo = new ExportationListResultBo<>();
        //条件过滤
        if (null == this.getSearchWhere(dto)) {
            return CommonResult.success(resultBo);
        }

        CommonPageResult.PageInfo<DevelopPricingOrderExportVo> exportList = developPricingOrderDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize(), false), dto);
        List<DevelopPricingOrderExportVo> records = exportList.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }

        List<String> developSampleOrderNoList = records.stream()
                .map(DevelopPricingOrderExportVo::getDevelopSampleOrderNo)
                .distinct()
                .collect(Collectors.toList());

        Map<String, DevelopSampleOrderPo> developSampleOrderPoMap = developSampleOrderDao.getMapByNoList(developSampleOrderNoList);

        // 获取平台名称
        final List<String> platCodeList = records.stream()
                .map(DevelopPricingOrderExportVo::getPlatform)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);
        for (DevelopPricingOrderExportVo record : records) {
            DevelopSampleOrderPo developSampleOrderPo = developSampleOrderPoMap.get(record.getDevelopSampleOrderNo());
            if (developSampleOrderPo != null) {
                record.setDevelopSampleMethod(developSampleOrderPo.getDevelopSampleMethod());
                record.setDevelopSampleMethodName(developSampleOrderPo.getDevelopSampleMethod().getRemark());
                record.setPlatform(platCodeNameMap.get(record.getPlatform()));
            }
        }

        resultBo.setRowDataList(records);

        return CommonResult.success(resultBo);
    }

    /**
     * 通过sku列表查询核价信息
     *
     * @param skuList:
     * @return List<DevelopPricingOrderInfoByPriceTimeBo>
     * @author ChenWenLong
     * @date 2024/2/28 15:19
     */
    public List<DevelopPricingOrderInfoByPriceTimeBo> getPriceTimeBoListBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return new ArrayList<>();
        }
        List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListBySku(skuList);
        List<String> developSampleOrderNoList = developSampleOrderPoList.stream()
                .map(DevelopSampleOrderPo::getDevelopSampleOrderNo)
                .distinct().collect(Collectors.toList());
        List<DevelopPricingOrderInfoByPriceTimeBo> boList = developPricingOrderInfoDao.getListBySampleOrderNoAndPriceTime(developSampleOrderNoList);
        for (DevelopPricingOrderInfoByPriceTimeBo developPricingOrderInfoByPriceTimeBo : boList) {
            developSampleOrderPoList.stream()
                    .filter(developSampleOrderPo -> developSampleOrderPo.getDevelopSampleOrderNo().equals(developPricingOrderInfoByPriceTimeBo.getDevelopSampleOrderNo()))
                    .findFirst()
                    .ifPresent(developSampleOrderPo -> {
                        developPricingOrderInfoByPriceTimeBo.setSku(developSampleOrderPo.getSku());
                    });
        }
        return boList;
    }


}
