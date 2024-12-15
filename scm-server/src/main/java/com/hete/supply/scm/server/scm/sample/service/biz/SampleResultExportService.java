package com.hete.supply.scm.server.scm.sample.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.SampleChildOrderResultSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.SampleResult;
import com.hete.supply.scm.api.scm.entity.vo.SampleChildResultExportVo;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.SdaRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.sample.dao.SampleChildOrderDao;
import com.hete.supply.scm.server.scm.sample.dao.SampleChildOrderResultDao;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderPo;
import com.hete.supply.scm.server.scm.sample.service.base.SampleBaseService;
import com.hete.supply.scm.server.supplier.sample.dao.SampleReturnOrderDao;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderPo;
import com.hete.supply.wms.api.entry.entity.dto.ReceiveOrderGetDto;
import com.hete.supply.wms.api.entry.entity.vo.ReceiveOrderForScmVo;
import com.hete.support.api.result.CommonPageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2023/5/15 16:43
 */
@Service
@RequiredArgsConstructor
public class SampleResultExportService {
    private final SampleChildOrderResultDao sampleChildOrderResultDao;
    private final SampleBaseService sampleBaseService;
    private final WmsRemoteService wmsRemoteService;
    private final SampleReturnOrderDao sampleReturnOrderDao;
    private final SampleChildOrderDao sampleChildOrderDao;
    private final PlmRemoteService plmRemoteService;
    private final SdaRemoteService sdaRemoteService;


    public Integer getExportTotals(SampleChildOrderResultSearchDto dto) {
        //条件过滤
        if (null == sampleBaseService.getSampleResultSearchWhere(dto)) {
            return 0;
        }
        return sampleChildOrderResultDao.getExportTotals(dto);
    }

    public CommonPageResult.PageInfo<SampleChildResultExportVo> getExportList(SampleChildOrderResultSearchDto dto) {
        //条件过滤
        if (null == sampleBaseService.getSampleResultSearchWhere(dto)) {
            return new CommonPageResult.PageInfo<>();
        }

        final CommonPageResult.PageInfo<SampleChildResultExportVo> pageResult = sampleChildOrderResultDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);

        final List<SampleChildResultExportVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageResult;
        }

        //关联单据
        List<String> relateOrderNoList = records.stream().map(SampleChildResultExportVo::getRelateOrderNo).collect(Collectors.toList());
        //获取打样成功关联收货单
        ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
        receiveOrderGetDto.setReceiveOrderNoList(relateOrderNoList);
        Map<String, ReceiveOrderForScmVo> receiveOrderForScmVoMap = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto).stream().collect(Collectors.toMap(ReceiveOrderForScmVo::getReceiveOrderNo, receiveOrderForScmVo -> receiveOrderForScmVo));

        //获取打样失败关联
        Map<String, SampleReturnOrderPo> sampleReturnOrderPoMap = sampleReturnOrderDao.getListByNoList(relateOrderNoList).stream().collect(Collectors.toMap(SampleReturnOrderPo::getSampleReturnOrderNo, sampleReturnOrderPo -> sampleReturnOrderPo));

        //获取失败闪售
        Map<String, List<SampleChildOrderPo>> sampleChildOrderPoMap = sampleChildOrderDao.getMapByParentNoList(relateOrderNoList);

        //获取SKU
        List<String> skuList = records.stream().map(SampleChildResultExportVo::getSku).collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        // 获取平台名称
        final List<String> platCodeList = records.stream()
                .map(SampleChildResultExportVo::getPlatform)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);
        records.forEach(record -> {
            if (SampleResult.SAMPLE_SUCCESS.equals(record.getSampleResult())) {
                ReceiveOrderForScmVo receiveOrderForScmVo = receiveOrderForScmVoMap.get(record.getRelateOrderNo());
                if (receiveOrderForScmVo != null) {
                    record.setRelateOrderAmount(receiveOrderForScmVo.getDeliveryAmount());
                    record.setHandlesTime(receiveOrderForScmVo.getCreateTime());
                }
            }

            if (SampleResult.SAMPLE_RETURN.equals(record.getSampleResult())) {
                SampleReturnOrderPo sampleReturnOrderPo = sampleReturnOrderPoMap.get(record.getRelateOrderNo());
                if (sampleReturnOrderPo != null) {
                    record.setRelateOrderAmount(sampleReturnOrderPo.getReturnCnt());
                    record.setHandlesTime(sampleReturnOrderPo.getCreateTime());
                }
            }

            if (SampleResult.FAIL_SALE.equals(record.getSampleResult())) {
                List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderPoMap.get(record.getRelateOrderNo());
                if (CollectionUtils.isNotEmpty(sampleChildOrderPoList)) {
                    record.setRelateOrderAmount(sampleChildOrderPoList.stream().mapToInt(SampleChildOrderPo::getPurchaseCnt).sum());//取采购数
                    record.setHandlesTime(sampleChildOrderPoList.stream().sorted(Comparator.comparing(SampleChildOrderPo::getCreateTime)).collect(Collectors.toList()).get(0).getCreateTime());
                }
            }

            record.setPlatform(platCodeNameMap.get(record.getPlatform()));
            if (null != record.getSampleResult()) {
                record.setSampleResultName(record.getSampleResult().getRemark());
            }
            if (null != record.getSampleResultStatus()) {
                record.setSampleResultStatusName(record.getSampleResultStatus().getRemark());
            }

            record.setSkuEncode(skuEncodeMap.get(record.getSku()));


        });

        return pageResult;

    }
}
