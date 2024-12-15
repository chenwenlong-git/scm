package com.hete.supply.scm.server.scm.develop.service.base;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.DevelopReviewSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.DevelopSampleSettleSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.DevelopReviewOrderExportVo;
import com.hete.supply.scm.api.scm.entity.vo.DevelopSampleSettleOrderExportVo;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.SdaRemoteService;
import com.hete.supply.scm.server.scm.develop.converter.DevelopChildConverter;
import com.hete.supply.scm.server.scm.develop.dao.DevelopReviewOrderDao;
import com.hete.supply.scm.server.scm.develop.dao.DevelopReviewSampleOrderDao;
import com.hete.supply.scm.server.scm.develop.dao.DevelopReviewSampleOrderInfoDao;
import com.hete.supply.scm.server.scm.develop.dao.DevelopSampleOrderDao;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopReviewSampleOrderInfoPo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopReviewSampleOrderPo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderPo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopReviewSearchVo;
import com.hete.supply.scm.server.scm.entity.po.DevelopSampleSettlePayPo;
import com.hete.supply.scm.server.scm.settle.dao.DevelopSampleSettleOrderDao;
import com.hete.supply.scm.server.scm.settle.dao.DevelopSampleSettlePayDao;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.core.util.TimeZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2023/8/1 14:35
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class DevelopExportBizService {

    private final DevelopSampleSettleOrderDao developSampleSettleOrderDao;
    private final DevelopSampleSettlePayDao developSampleSettlePayDao;
    private final DevelopSampleSettleBaseService developSampleSettleBaseService;
    private final DevelopReviewOrderDao developReviewOrderDao;
    private final DevelopReviewSampleOrderDao developReviewSampleOrderDao;
    private final DevelopReviewSampleOrderInfoDao developReviewSampleOrderInfoDao;
    private final SdaRemoteService sdaRemoteService;
    private final DevelopSampleOrderDao developSampleOrderDao;
    private final PlmRemoteService plmRemoteService;

    /**
     * 开发样品结算导出总数
     *
     * @param dto:
     * @return Integer
     * @author ChenWenLong
     * @date 2023/8/5 13:58
     */
    public Integer getExportTotals(DevelopSampleSettleSearchDto dto) {
        //条件过滤
        if (null == developSampleSettleBaseService.getSearchWhere(dto)) {
            throw new ParamIllegalException("导出数据为空");
        }
        Integer exportTotals = developSampleSettleOrderDao.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        return exportTotals;
    }


    /**
     * 开发样品结算导出
     *
     * @param dto:
     * @return CommonResult<ExportationListResultBo < DevelopSampleSettleOrderExportVo>>
     * @author ChenWenLong
     * @date 2023/8/5 11:01
     */
    public CommonResult<ExportationListResultBo<DevelopSampleSettleOrderExportVo>> getExportList(DevelopSampleSettleSearchDto dto) {
        ExportationListResultBo<DevelopSampleSettleOrderExportVo> resultBo = new ExportationListResultBo<>();
        //条件过滤
        if (null == developSampleSettleBaseService.getSearchWhere(dto)) {
            return CommonResult.success(resultBo);
        }

        CommonPageResult.PageInfo<DevelopSampleSettleOrderExportVo> exportList = developSampleSettleOrderDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize(), false), dto);
        List<DevelopSampleSettleOrderExportVo> records = exportList.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }

        List<String> developSampleSettleOrderNoList = records.stream()
                .map(DevelopSampleSettleOrderExportVo::getDevelopSampleSettleOrderNo)
                .distinct()
                .collect(Collectors.toList());

        // 获取样品单号
        List<String> developSampleOrderNoList = records.stream()
                .map(DevelopSampleSettleOrderExportVo::getDevelopSampleOrderNo)
                .distinct()
                .collect(Collectors.toList());

        Map<String, List<DevelopSampleSettlePayPo>> developSampleSettlePayPoMap = developSampleSettlePayDao.getListByNoList(developSampleSettleOrderNoList);

        // 获取样品单PO
        List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByNoList(developSampleOrderNoList);
        List<String> skuList = developSampleOrderPoList.stream().map(DevelopSampleOrderPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        for (DevelopSampleSettleOrderExportVo item : records) {

            List<DevelopSampleSettlePayPo> developSampleSettlePayPoList = developSampleSettlePayPoMap.get(item.getDevelopSampleSettleOrderNo());
            if (CollectionUtils.isNotEmpty(developSampleSettlePayPoList)) {
                BigDecimal paidPrice = developSampleSettlePayPoList.stream().map(DevelopSampleSettlePayPo::getPayPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                item.setPaidPrice(paidPrice);
                item.setWaitPayPrice(item.getPayPrice().subtract(paidPrice));
                item.setItemTotal(developSampleSettlePayPoList.size());
            } else {
                item.setWaitPayPrice(item.getPayPrice());
            }
            item.setDevelopSampleSettleStatusName(item.getDevelopSampleSettleStatus().getRemark());
            // 转时间
            item.setCreateTimeStr(ScmTimeUtil.localDateTimeToStr(item.getCreateTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            item.setConfirmTimeStr(ScmTimeUtil.localDateTimeToStr(item.getConfirmTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            item.setExamineTimeStr(ScmTimeUtil.localDateTimeToStr(item.getExamineTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            item.setSettleTimeStr(ScmTimeUtil.localDateTimeToStr(item.getSettleTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            item.setPayTimeStr(ScmTimeUtil.localDateTimeToStr(item.getPayTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            item.setItemSettleTimeStr(ScmTimeUtil.localDateTimeToStr(item.getItemSettleTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            DevelopSampleOrderPo developSampleOrderPo = developSampleOrderPoList.stream()
                    .filter(sampleOrderPo -> sampleOrderPo.getDevelopSampleOrderNo().equals(item.getDevelopSampleOrderNo()))
                    .filter(sampleOrderPo -> StringUtils.isNotBlank(sampleOrderPo.getSku()))
                    .findFirst()
                    .orElse(null);
            if (null != developSampleOrderPo) {
                item.setSku(developSampleOrderPo.getSku());
                item.setSkuEncode(skuEncodeMap.get(developSampleOrderPo.getSku()));
            }
        }

        resultBo.setRowDataList(records);

        return CommonResult.success(resultBo);
    }

    public Integer getReviewExportTotals(DevelopReviewSearchDto dto) {
        return developReviewOrderDao.getReviewExportTotals(dto);
    }

    public CommonResult<ExportationListResultBo<DevelopReviewOrderExportVo>> getReviewExportList(DevelopReviewSearchDto dto) {
        ExportationListResultBo<DevelopReviewOrderExportVo> resultBo = new ExportationListResultBo<>();
        final CommonPageResult.PageInfo<DevelopReviewSearchVo> developReviewSearchVoPageInfo = developReviewOrderDao.searchDevelopReview(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<DevelopReviewSearchVo> records = developReviewSearchVoPageInfo.getRecords();

        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }

        final List<String> developReviewOrderNoList = records.stream()
                .map(DevelopReviewSearchVo::getDevelopReviewOrderNo)
                .collect(Collectors.toList());

        final List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoList = developReviewSampleOrderDao.getListByReviewNoList(developReviewOrderNoList);
        final Map<String, DevelopReviewSearchVo> developReviewSearchNoVoMap = records.stream()
                .collect(Collectors.toMap(DevelopReviewSearchVo::getDevelopReviewOrderNo, Function.identity()));

        final List<String> developSampleOrderNoList = developReviewSampleOrderPoList.stream()
                .map(DevelopReviewSampleOrderPo::getDevelopSampleOrderNo)
                .collect(Collectors.toList());

        final List<DevelopReviewSampleOrderInfoPo> developReviewSampleOrderInfoPoList = developReviewSampleOrderInfoDao.getListByDevelopSampleOrderNoList(developSampleOrderNoList);
        final Map<String, String> devReviewSampleNoInfoMap = developReviewSampleOrderInfoPoList.stream()
                .collect(Collectors.groupingBy(DevelopReviewSampleOrderInfoPo::getDevelopSampleOrderNo,
                        Collectors.mapping(infoPo -> infoPo.getSampleInfoKey() + ":" + infoPo.getSampleInfoValue(), Collectors.joining("/"))));

        // 获取平台名称
        final List<String> platCodeList = records.stream()
                .map(DevelopReviewSearchVo::getPlatform)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);

        final List<DevelopReviewOrderExportVo> developReviewOrderExportVoList = DevelopChildConverter.convertDevSamplePoToExportVo(developReviewSampleOrderPoList,
                developReviewSearchNoVoMap, devReviewSampleNoInfoMap, platCodeNameMap);
        resultBo.setRowDataList(developReviewOrderExportVoList);

        return CommonResult.success(resultBo);
    }
}
