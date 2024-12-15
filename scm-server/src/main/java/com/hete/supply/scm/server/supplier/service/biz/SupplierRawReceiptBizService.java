package com.hete.supply.scm.server.supplier.service.biz;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseRawReceiptSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseRawBizType;
import com.hete.supply.scm.api.scm.entity.enums.RawReceiptBizType;
import com.hete.supply.scm.api.scm.entity.enums.ReceiptOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SampleRawBizType;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.develop.dao.DevelopPamphletOrderDao;
import com.hete.supply.scm.server.scm.develop.dao.DevelopPamphletOrderRawDao;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPamphletOrderPo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPamphletOrderRawPo;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderRawDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderRawDeliverDao;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderRawDeliverPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderRawPo;
import com.hete.supply.scm.server.scm.purchase.enums.RawExtra;
import com.hete.supply.scm.server.scm.sample.dao.SampleChildOrderDao;
import com.hete.supply.scm.server.scm.sample.dao.SampleChildOrderRawDao;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderPo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderRawPo;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierProductCompareDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.scm.server.supplier.purchase.converter.SupplierPurchaseConverter;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseRawReceiptOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseRawReceiptOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseRawCommitDto;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseRawReceiptOrderNoDto;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseRawReceiptOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseRawReceiptOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseRawReceiptDetailVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseRawReceiptVo;
import com.hete.supply.scm.server.supplier.purchase.service.base.PurchaseRawReceiptBaseService;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/28 15:43
 */
@Service
@RequiredArgsConstructor
public class SupplierRawReceiptBizService {
    private final PurchaseRawReceiptOrderDao purchaseRawReceiptOrderDao;
    private final PurchaseRawReceiptOrderItemDao purchaseRawReceiptOrderItemDao;
    private final LogBaseService logBaseService;
    private final SupplierProductCompareDao supplierProductCompareDao;
    private final PurchaseChildOrderRawDao purchaseChildOrderRawDao;
    private final SampleChildOrderRawDao sampleChildOrderRawDao;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final SampleChildOrderDao sampleChildOrderDao;
    private final PlmRemoteService plmRemoteService;
    private final PurchaseRawReceiptBaseService purchaseRawReceiptBaseService;
    private final ConsistencySendMqService consistencySendMqService;
    private final DevelopPamphletOrderDao developPamphletOrderDao;
    private final DevelopPamphletOrderRawDao developPamphletOrderRawDao;
    private final PurchaseChildOrderRawDeliverDao purchaseChildOrderRawDeliverDao;
    private final SupplierRawReceiptExportService rawReceiptExportService;

    public CommonPageResult.PageInfo<PurchaseRawReceiptVo> search(PurchaseRawReceiptSearchDto dto) {
        //条件过滤
        if (null == purchaseRawReceiptBaseService.getSearchRawReceiptWhere(dto)) {
            return new CommonPageResult.PageInfo<>();
        }
        return purchaseRawReceiptOrderDao.search(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
    }

    public PurchaseRawReceiptDetailVo detail(PurchaseRawReceiptOrderNoDto dto) {
        PurchaseRawReceiptOrderPo purchaseRawReceiptOrderPo = purchaseRawReceiptOrderDao.getOneByDeliverNo(dto.getPurchaseRawReceiptOrderNo());
        if (null == purchaseRawReceiptOrderPo) {
            purchaseRawReceiptOrderPo = purchaseRawReceiptOrderDao.getOneByNo(dto.getPurchaseRawReceiptOrderNo());
        }

        Assert.notNull(purchaseRawReceiptOrderPo, () -> new BizException("查找不到对应的采购原料收货单，打开详情失败"));
        final List<PurchaseRawReceiptOrderItemPo> purchaseRawReceiptOrderItemPoList = purchaseRawReceiptOrderItemDao.getListByNo(purchaseRawReceiptOrderPo.getPurchaseRawReceiptOrderNo());
        Assert.notEmpty(purchaseRawReceiptOrderItemPoList, () -> new BizException("查找不到对应的采购原料收货单，打开详情失败"));

        //组装数据获取供应商产品名称
        final List<String> skuList = purchaseRawReceiptOrderItemPoList.stream()
                .map(PurchaseRawReceiptOrderItemPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        Map<String, SupplierProductComparePo> supplierProductCompareMap = supplierProductCompareDao.getBatchSkuMap(skuList);
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        return SupplierPurchaseConverter.rawPoToDetailVo(purchaseRawReceiptOrderPo, purchaseRawReceiptOrderItemPoList,
                supplierProductCompareMap, skuEncodeMap);
    }


    public PurchaseRawReceiptDetailVo h5detail(PurchaseRawReceiptOrderNoDto dto) {
        final PurchaseRawReceiptOrderPo purchaseRawReceiptOrderPo = purchaseRawReceiptOrderDao.getOneByReceiptNoOrTrackingNo(dto.getPurchaseRawReceiptOrderNo());
        Assert.notNull(purchaseRawReceiptOrderPo, () -> new BizException("查找不到对应的采购原料收货单，打开详情失败"));
        final List<PurchaseRawReceiptOrderItemPo> purchaseRawReceiptOrderItemPoList = purchaseRawReceiptOrderItemDao.getListByNo(purchaseRawReceiptOrderPo.getPurchaseRawReceiptOrderNo());
        Assert.notEmpty(purchaseRawReceiptOrderItemPoList, () -> new BizException("查找不到对应的采购原料收货单，打开详情失败"));
        //组装数据获取供应商产品名称
        final List<String> skuList = purchaseRawReceiptOrderItemPoList.stream()
                .map(PurchaseRawReceiptOrderItemPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        Map<String, SupplierProductComparePo> supplierProductCompareMap = supplierProductCompareDao.getBatchSkuMap(skuList);
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        return SupplierPurchaseConverter.rawPoToDetailVo(purchaseRawReceiptOrderPo, purchaseRawReceiptOrderItemPoList,
                supplierProductCompareMap, skuEncodeMap);
    }

    @Transactional(rollbackFor = Exception.class)
    public void commit(PurchaseRawCommitDto dto) {
        final List<PurchaseRawCommitDto.PurchaseRawReceiptItemDto> purchaseRawReceiptItemList = dto.getPurchaseRawReceiptItemList();

        final int receiptCnt = purchaseRawReceiptItemList.stream()
                .mapToInt(PurchaseRawCommitDto.PurchaseRawReceiptItemDto::getReceiptCnt)
                .sum();

        final PurchaseRawReceiptOrderPo purchaseRawReceiptOrderPo = purchaseRawReceiptOrderDao.getByIdVersion(dto.getPurchaseRawReceiptOrderId(), dto.getVersion());
        if (null == purchaseRawReceiptOrderPo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        final ReceiptOrderStatus receiptOrderStatus = purchaseRawReceiptOrderPo.getReceiptOrderStatus().toReceipted();
        purchaseRawReceiptOrderPo.setReceiptOrderStatus(receiptOrderStatus);
        purchaseRawReceiptOrderPo.setReceiptTime(LocalDateTime.now());
        purchaseRawReceiptOrderPo.setReceiptUser(GlobalContext.getUserKey());
        purchaseRawReceiptOrderPo.setReceiptUsername(GlobalContext.getUsername());
        purchaseRawReceiptOrderPo.setReceiptCnt(receiptCnt);
        purchaseRawReceiptOrderDao.updateByIdVersion(purchaseRawReceiptOrderPo);

        final List<PurchaseRawReceiptOrderItemPo> updatePurchaseRawReceiptOrderItemPoList = SupplierPurchaseConverter.rawReceiptItemDtoToPoList(purchaseRawReceiptItemList);
        purchaseRawReceiptOrderItemDao.updateBatchByIdVersion(updatePurchaseRawReceiptOrderItemPoList);

        // 增加采购原料实际发货类型
        final List<Long> rawReceiptItemIdList = purchaseRawReceiptItemList.stream()
                .map(PurchaseRawCommitDto.PurchaseRawReceiptItemDto::getPurchaseRawReceiptOrderItemId)
                .collect(Collectors.toList());
        final List<PurchaseRawReceiptOrderItemPo> purchaseRawReceiptOrderItemPoList = purchaseRawReceiptOrderItemDao.getListByIdList(rawReceiptItemIdList);
        if (purchaseRawReceiptOrderItemPoList.size() != rawReceiptItemIdList.size()) {
            throw new BizException("查找不到对应的原料收货单，收货失败！");
        }
        if (RawReceiptBizType.PURCHASE.equals(purchaseRawReceiptOrderPo.getRawReceiptBizType())) {
            this.updatePurchaseRaw(purchaseRawReceiptOrderPo.getPurchaseChildOrderNo(), purchaseRawReceiptOrderItemPoList, purchaseRawReceiptOrderPo);
        } else if (RawReceiptBizType.SAMPLE.equals(purchaseRawReceiptOrderPo.getRawReceiptBizType())
                && StringUtils.isNotBlank(purchaseRawReceiptOrderPo.getSampleChildOrderNo())) {
            this.updateSampleRaw(purchaseRawReceiptOrderPo.getSampleChildOrderNo(), purchaseRawReceiptOrderItemPoList);

        } else if (RawReceiptBizType.DEVELOP.equals(purchaseRawReceiptOrderPo.getRawReceiptBizType())
                && StringUtils.isNotBlank(purchaseRawReceiptOrderPo.getDevelopPamphletOrderNo())) {
            this.updatePamphletRaw(purchaseRawReceiptOrderPo.getDevelopPamphletOrderNo(), purchaseRawReceiptOrderItemPoList);
        } else {
            throw new BizException("异常的原料收货单类型，收货失败！");
        }

        logBaseService.simpleLog(LogBizModule.SUPPLIER_RAW_RECEIPT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseRawReceiptOrderPo.getPurchaseRawReceiptOrderNo(), receiptOrderStatus.getRemark(), Collections.emptyList());

    }

    private void updatePamphletRaw(String developPamphletOrderNo, List<PurchaseRawReceiptOrderItemPo> purchaseRawReceiptOrderItemPoList) {
        final DevelopPamphletOrderPo developPamphletOrderPo = developPamphletOrderDao.getByDevelopPamphletOrderNo(developPamphletOrderNo);
        Assert.notNull(developPamphletOrderPo, () -> new BizException("版单号：{}找不到对应的信息，收货失败！", developPamphletOrderNo));

        final List<DevelopPamphletOrderRawPo> developPamphletOrderRawPoList = developPamphletOrderRawDao.getListByDevelopPamphletOrderNoAndType(developPamphletOrderNo,
                SampleRawBizType.ACTUAL_DELIVER);
        final Map<String, DevelopPamphletOrderRawPo> developPamphletOrderRawPoMap = developPamphletOrderRawPoList.stream()
                .collect(Collectors.toMap(DevelopPamphletOrderRawPo::getSkuBatchCode, Function.identity()));

        List<DevelopPamphletOrderRawPo> updatePamphletRawList = new ArrayList<>();
        purchaseRawReceiptOrderItemPoList.forEach(item -> {
            DevelopPamphletOrderRawPo developPamphletOrderRawPo = developPamphletOrderRawPoMap.get(item.getSkuBatchCode());
            if (null == developPamphletOrderRawPo) {
                developPamphletOrderRawPo = new DevelopPamphletOrderRawPo();
                developPamphletOrderRawPo.setDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
                developPamphletOrderRawPo.setDevelopParentOrderNo(developPamphletOrderPo.getDevelopParentOrderNo());
                developPamphletOrderRawPo.setDevelopChildOrderNo(developPamphletOrderPo.getDevelopChildOrderNo());
                developPamphletOrderRawPo.setSku(item.getSku());
                developPamphletOrderRawPo.setSkuCnt(item.getReceiptCnt());
                developPamphletOrderRawPo.setSkuBatchCode(item.getSkuBatchCode());
                developPamphletOrderRawPo.setSampleRawBizType(SampleRawBizType.ACTUAL_DELIVER);
            } else {
                developPamphletOrderRawPo.setSkuCnt(developPamphletOrderRawPo.getSkuCnt() + item.getReceiptCnt());
            }
            updatePamphletRawList.add(developPamphletOrderRawPo);
        });
        developPamphletOrderRawDao.insertOrUpdateBatch(updatePamphletRawList);
    }

    private void updateSampleRaw(String sampleChildOrderNo, List<PurchaseRawReceiptOrderItemPo> purchaseRawReceiptOrderItemPoList) {
        final SampleChildOrderPo sampleChildOrderPo = sampleChildOrderDao.getOneByChildOrderNo(sampleChildOrderNo);
        Assert.notNull(sampleChildOrderPo, () -> new BizException("样品子单号：{}找不到对应的样品子单，收货失败！", sampleChildOrderNo));

        final List<SampleChildOrderRawPo> sampleChildOrderRawPoList = sampleChildOrderRawDao.getListByChildNoAndType(sampleChildOrderNo,
                SampleRawBizType.ACTUAL_DELIVER);
        final Map<String, SampleChildOrderRawPo> skuBatchSampleChildRawMap = sampleChildOrderRawPoList.stream()
                .collect(Collectors.toMap(SampleChildOrderRawPo::getSkuBatchCode, Function.identity()));

        List<SampleChildOrderRawPo> updateSampleChildRawList = new ArrayList<>();
        purchaseRawReceiptOrderItemPoList.forEach(item -> {
            SampleChildOrderRawPo sampleChildOrderRawPo = skuBatchSampleChildRawMap.get(item.getSkuBatchCode());
            if (null == sampleChildOrderRawPo) {
                sampleChildOrderRawPo = new SampleChildOrderRawPo();
                sampleChildOrderRawPo.setSampleChildOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
                sampleChildOrderRawPo.setSampleParentOrderNo(sampleChildOrderPo.getSampleParentOrderNo());
                sampleChildOrderRawPo.setSampleChildOrderId(sampleChildOrderPo.getSampleChildOrderId());
                sampleChildOrderRawPo.setSku(item.getSku());
                sampleChildOrderRawPo.setDeliveryCnt(item.getReceiptCnt());
                sampleChildOrderRawPo.setSkuBatchCode(item.getSkuBatchCode());
                sampleChildOrderRawPo.setSampleRawBizType(SampleRawBizType.ACTUAL_DELIVER);
            } else {
                sampleChildOrderRawPo.setDeliveryCnt(sampleChildOrderRawPo.getDeliveryCnt() + item.getReceiptCnt());
            }
            updateSampleChildRawList.add(sampleChildOrderRawPo);
        });
        sampleChildOrderRawDao.insertOrUpdateBatch(updateSampleChildRawList);

    }

    private void updatePurchaseRaw(String purchaseChildOrderNo,
                                   List<PurchaseRawReceiptOrderItemPo> purchaseRawReceiptOrderItemPoList,
                                   PurchaseRawReceiptOrderPo purchaseRawReceiptOrderPo) {

        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(purchaseChildOrderNo);
        Assert.notNull(purchaseChildOrderPo, () -> new BizException("采购子单号：{}找不到对应的采购子单，收货失败！", purchaseChildOrderNo));

        final List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList = purchaseChildOrderRawDao.getListByChildNo(purchaseChildOrderNo,
                PurchaseRawBizType.ACTUAL_DELIVER);
        final Map<String, PurchaseChildOrderRawPo> skuBatchPurchaseChildRawMap = purchaseChildOrderRawPoList.stream()
                .filter(rawPo -> StringUtils.isNotBlank(rawPo.getSkuBatchCode()))
                .collect(Collectors.toMap(rawPo -> rawPo.getSku() + rawPo.getSkuBatchCode(), Function.identity()));

        final PurchaseChildOrderRawDeliverPo purchaseChildOrderRawDeliverPo = purchaseChildOrderRawDeliverDao.getOneByRawDeliverNo(purchaseRawReceiptOrderPo.getPurchaseRawDeliverOrderNo());
        if (null == purchaseChildOrderRawDeliverPo) {
            throw new BizException("出库单:{}找不到对应的原料数据，收货失败，请联系系统管理员", purchaseRawReceiptOrderPo.getPurchaseRawDeliverOrderNo());
        }
        List<PurchaseChildOrderRawPo> updatePurchaseChildRawList = new ArrayList<>();
        purchaseRawReceiptOrderItemPoList.forEach(item -> {
            PurchaseChildOrderRawPo purchaseChildOrderRawPo = skuBatchPurchaseChildRawMap.get(item.getSku() + item.getSkuBatchCode());
            if (null == purchaseChildOrderRawPo) {
                purchaseChildOrderRawPo = new PurchaseChildOrderRawPo();
                purchaseChildOrderRawPo.setPurchaseParentOrderNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
                purchaseChildOrderRawPo.setPurchaseChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
                purchaseChildOrderRawPo.setSku(item.getSku());
                purchaseChildOrderRawPo.setSkuBatchCode(item.getSkuBatchCode());
                purchaseChildOrderRawPo.setPurchaseRawBizType(PurchaseRawBizType.ACTUAL_DELIVER);
                purchaseChildOrderRawPo.setRawSupplier(purchaseChildOrderRawDeliverPo.getRawSupplier());
                purchaseChildOrderRawPo.setRawExtra(RawExtra.NORMAL);
                purchaseChildOrderRawPo.setReceiptCnt(item.getReceiptCnt());
            } else {
                purchaseChildOrderRawPo.setReceiptCnt(purchaseChildOrderRawPo.getReceiptCnt() + item.getReceiptCnt());
            }
            updatePurchaseChildRawList.add(purchaseChildOrderRawPo);
        });
        purchaseChildOrderRawDao.insertOrUpdateBatch(updatePurchaseChildRawList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void getNewRawReceiptExportList(PurchaseRawReceiptSearchDto dto) {
        //条件过滤
        if (null == purchaseRawReceiptBaseService.getSearchRawReceiptWhere(dto)) {
            throw new ParamIllegalException("导出数据为空");
        }
        Integer exportTotals = rawReceiptExportService.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && exportTotals > 0, () -> new ParamIllegalException("导出数据为空"));
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                FileOperateBizType.PURCHASE_RAW_RECEIPT_EXPORT.getCode(), dto));
    }

    @Transactional(rollbackFor = Exception.class)
    public void getScmNewRawReceiptExportList(PurchaseRawReceiptSearchDto dto) {
        //条件过滤
        if (null == purchaseRawReceiptBaseService.getSearchRawReceiptWhere(dto)) {
            throw new ParamIllegalException("导出数据为空");
        }
        Integer exportTotals = rawReceiptExportService.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && exportTotals > 0, () -> new ParamIllegalException("导出数据为空"));
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                FileOperateBizType.SCM_PURCHASE_RAW_RECEIPT_EXPORT.getCode(), dto));
    }
}
