package com.hete.supply.scm.server.scm.purchase.service.biz;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.google.common.collect.Lists;
import com.hete.supply.plm.api.goods.entity.vo.*;
import com.hete.supply.scm.api.scm.entity.dto.*;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseSkuCntVo;
import com.hete.supply.scm.api.scm.entity.vo.SampleChildOrderInfoVo;
import com.hete.supply.scm.api.scm.entity.vo.SampleInfoVo;
import com.hete.supply.scm.api.scm.entity.vo.SampleProductVo;
import com.hete.supply.scm.api.scm.importation.entity.dto.PurchaseChildEditImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.PurchaseParentImportationDto;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.common.util.ScmFormatUtil;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.common.util.ScmWarehouseUtil;
import com.hete.supply.scm.common.util.StringUtil;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.SdaRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.adjust.config.ScmAdjustProp;
import com.hete.supply.scm.server.scm.adjust.dao.GoodsPriceDao;
import com.hete.supply.scm.server.scm.adjust.entity.bo.AdjustApproveBo;
import com.hete.supply.scm.server.scm.adjust.entity.bo.OrderAdjustDetailItemBo;
import com.hete.supply.scm.server.scm.adjust.entity.dto.GoodsPriceGetPurchaseListDto;
import com.hete.supply.scm.server.scm.adjust.entity.po.AdjustPriceApprovePo;
import com.hete.supply.scm.server.scm.adjust.entity.vo.GoodsPriceGetPurchaseListVo;
import com.hete.supply.scm.server.scm.adjust.enums.ApproveType;
import com.hete.supply.scm.server.scm.adjust.enums.GoodsPriceUniversal;
import com.hete.supply.scm.server.scm.adjust.service.base.AdjustApproveBaseService;
import com.hete.supply.scm.server.scm.dao.ProduceDataDao;
import com.hete.supply.scm.server.scm.dao.SkuInfoDao;
import com.hete.supply.scm.server.scm.entity.bo.*;
import com.hete.supply.scm.server.scm.entity.dto.RawProductItemDto;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataPo;
import com.hete.supply.scm.server.scm.entity.po.SkuInfoPo;
import com.hete.supply.scm.server.scm.entity.vo.*;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.enums.LogVersionValueType;
import com.hete.supply.scm.server.scm.enums.SkuAvgPriceBizType;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderCancelEventDto;
import com.hete.supply.scm.server.scm.process.handler.WmsProcessCancelHandler;
import com.hete.supply.scm.server.scm.purchase.converter.PurchaseConverter;
import com.hete.supply.scm.server.scm.purchase.dao.*;
import com.hete.supply.scm.server.scm.purchase.entity.bo.*;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseChildNoListDto;
import com.hete.supply.scm.server.scm.purchase.entity.dto.*;
import com.hete.supply.scm.server.scm.purchase.entity.po.*;
import com.hete.supply.scm.server.scm.purchase.entity.vo.*;
import com.hete.supply.scm.server.scm.purchase.handler.PurchaseChangeHandler;
import com.hete.supply.scm.server.scm.purchase.handler.WmsDeliverDispatchHandler;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseBaseService;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseRawBaseService;
import com.hete.supply.scm.server.scm.purchase.service.ref.PurchaseRawRefService;
import com.hete.supply.scm.server.scm.qc.service.ref.QcOrderRefService;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderInfoPo;
import com.hete.supply.scm.server.scm.sample.service.base.SampleBaseService;
import com.hete.supply.scm.server.scm.service.base.*;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleSimpleVo;
import com.hete.supply.scm.server.scm.settle.service.base.PurchaseSettleOrderBaseService;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierInventoryRecordDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierProductCompareDao;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupOpCapacityBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryRecordPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierUrgentStatus;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierBaseService;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierInventoryBaseService;
import com.hete.supply.scm.server.scm.supplier.service.ref.SupplierCapacityRefService;
import com.hete.supply.scm.server.supplier.dao.OverseasWarehouseMsgDao;
import com.hete.supply.scm.server.supplier.dao.OverseasWarehouseMsgItemDao;
import com.hete.supply.scm.server.supplier.entity.dto.OverseasWarehouseMsgDto;
import com.hete.supply.scm.server.supplier.entity.dto.OverseasWarehouseMsgItemDto;
import com.hete.supply.scm.server.supplier.entity.po.OverseasWarehouseMsgItemPo;
import com.hete.supply.scm.server.supplier.entity.po.OverseasWarehouseMsgPo;
import com.hete.supply.scm.server.supplier.entity.vo.HeteCodeVo;
import com.hete.supply.scm.server.supplier.entity.vo.OverseasWarehouseMsgVo;
import com.hete.supply.scm.server.supplier.entity.vo.SkuBatchCodeVo;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.scm.server.supplier.purchase.dao.*;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.*;
import com.hete.supply.scm.server.supplier.purchase.entity.po.*;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.ConfirmCommissioningMsgVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseDeliverVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseDemandRawDetailVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseDemandRawVo;
import com.hete.supply.scm.server.supplier.purchase.enums.PurchaseCtrlType;
import com.hete.supply.scm.server.supplier.purchase.enums.PurchaseRawReceived;
import com.hete.supply.scm.server.supplier.purchase.service.base.PurchaseDeliverBaseService;
import com.hete.supply.scm.server.supplier.purchase.service.base.PurchaseReturnBaseService;
import com.hete.supply.scm.server.supplier.service.biz.OverseasBizService;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.basic.entity.dto.SkuBatchCreateDto;
import com.hete.supply.wms.api.basic.entity.vo.WarehouseVo;
import com.hete.supply.wms.api.interna.entity.dto.SkuInstockInventoryQueryDto;
import com.hete.supply.wms.api.interna.entity.vo.InventoryVo;
import com.hete.supply.wms.api.interna.entity.vo.SkuInventoryVo;
import com.hete.supply.wms.api.leave.entity.vo.ProcessDeliveryOrderVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.FormatStringUtil;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/4 11:53
 */
@RequiredArgsConstructor
@Service
@Slf4j
@Validated
public class PurchaseBizService {
    private final PurchaseParentOrderDao purchaseParentOrderDao;
    private final IdGenerateService idGenerateService;
    private final PurchaseParentOrderItemDao purchaseParentOrderItemDao;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;
    private final PurchaseChildOrderRawDao purchaseChildOrderRawDao;
    private final PurchaseBaseService purchaseBaseService;
    private final PurchaseParentOrderChangeDao purchaseParentOrderChangeDao;
    private final PurchaseChildOrderChangeDao purchaseChildOrderChangeDao;
    private final ScmImageBaseService scmImageBaseService;
    private final PurchaseDeliverBaseService purchaseDeliverBaseService;
    private final PurchaseSettleOrderBaseService purchaseSettleOrderBaseService;
    private final PurchaseReturnBaseService purchaseReturnOrderBaseService;
    private final PurchaseModifyOrderDao purchaseModifyOrderDao;
    private final PurchaseModifyOrderItemDao purchaseModifyOrderItemDao;
    private final PurchaseRawRefService purchaseRawRefService;
    private final SampleBaseService sampleBaseService;
    private final SupplierBaseService supplierBaseService;
    private final WmsRemoteService wmsRemoteService;
    private final LogBaseService logBaseService;
    private final PlmRemoteService plmRemoteService;
    private final OverseasBizService overseasBizService;
    private final OverseasWarehouseMsgDao overseasWarehouseMsgDao;
    private final OverseasWarehouseMsgItemDao overseasWarehouseMsgItemDao;
    private final PurchaseDeliverOrderDao purchaseDeliverOrderDao;
    private final PurchaseRawReceiptOrderDao purchaseRawReceiptOrderDao;
    private final PurchaseRawReceiptOrderItemDao purchaseRawReceiptOrderItemDao;
    private final SupplierProductCompareDao supplierProductCompareDao;
    private final PurchaseReturnOrderDao purchaseReturnOrderDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final PurchaseDeliverOrderItemDao purchaseDeliverOrderItemDao;
    private final ProduceDataDao produceDataDao;
    private final SupplierInventoryBaseService supplierInventoryBaseService;
    private final PurchaseRawBizService purchaseRawBizService;
    private final ProduceDataBaseService produceDataBaseService;
    private final SkuAvgPriceBaseService skuAvgPriceBaseService;
    private final SdaRemoteService sdaRemoteService;
    private final PurchaseRawBaseService purchaseRawBaseService;
    private final PurchaseChildOrderRawDeliverDao purchaseChildOrderRawDeliverDao;
    private final QcOrderRefService qcOrderRefService;
    private final AdjustApproveBaseService adjustApproveBaseService;
    private final GoodsPriceDao goodsPriceDao;
    private final ScmAdjustProp scmAdjustProp;
    private final SkuInfoDao skuInfoDao;
    private final SupplierCapacityRefService supplierCapacityRefService;
    private final SupplierInventoryRecordDao supplierInventoryRecordDao;


    private final static String PASS_CTRL_NAME = "跳转至--";
    private final static String BACK_CTRL_NAME = "回退至--";

    /**
     * 期望上架时间间隔
     */
    private final static long EXPECTED_DATE_SPAN = 8;

    /**
     * 创建采购需求单
     *
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    public PurchaseParentOrderPo createPurchase(PurchaseCreateDto dto) {
        Assert.notBlank(dto.getSpu(), () -> new BizException("商品sku类型的spu不能为空"));
        // 网红类型判断是否网红仓
        if (PurchaseDemandType.WH.equals(dto.getPurchaseDemandType()) && !ScmWarehouseUtil.WH_WAREHOUSE_CODE.equals(dto.getWarehouseCode())) {
            throw new ParamIllegalException("采购需求类型为网红时，只能选择网红仓创建，请重新选择仓库后再提交！");
        }
        if (PurchaseDemandType.NORMAL.equals(dto.getPurchaseDemandType()) && ScmWarehouseUtil.WH_WAREHOUSE_CODE.equals(dto.getWarehouseCode())) {
            throw new ParamIllegalException("采购需求类型为常规时，不能选择网红仓创建，请重新选择仓库后再提交！");
        }

        // 获取sku是否需要管理
        final List<PurchaseProductDemandItemDto> purchaseProductDemandItemList = dto.getPurchaseProductDemandItemList();
        final List<String> skuList = purchaseProductDemandItemList.stream()
                .map(PurchaseProductDemandItemDto::getSku)
                .distinct()
                .collect(Collectors.toList());
        final List<ProduceDataPo> produceDataPoList = produceDataDao.getListBySkuList(skuList);
        final Map<String, BooleanType> skuRawManageMap = produceDataPoList.stream()
                .collect(Collectors.toMap(ProduceDataPo::getSku, ProduceDataPo::getRawManage));
        final List<String> rawManageSkuList = skuList.stream().filter(sku -> {
            final BooleanType rawManage = skuRawManageMap.get(sku);
            if (null == rawManage) {
                throw new ParamIllegalException("sku:{}没有配置原料是否需要管理", sku);
            }
            return BooleanType.TRUE.equals(rawManage);
        }).collect(Collectors.toList());

        // sku类型为商品sku时，校验sku是否维护了bom数据
        if (SkuType.SKU.equals(dto.getSkuType())) {
            final SampleSkuListDto sampleSkuListDto = new SampleSkuListDto();
            sampleSkuListDto.setSkuList(rawManageSkuList);
            final List<SkuBomListVo> skuBomListVoList = produceDataBaseService.getBomListBySkuList(sampleSkuListDto);
            final List<String> bomSkuList = skuBomListVoList.stream()
                    .map(SkuBomListVo::getSku)
                    .distinct()
                    .collect(Collectors.toList());
            // 获取不存在的list
            List<String> absentSkuList = rawManageSkuList.stream()
                    .filter(item -> !bomSkuList.contains(item))
                    .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(absentSkuList)) {
                throw new ParamIllegalException("sku:{}没有维护bom信息！", absentSkuList);
            }
        } else if (SkuType.SM_SKU.equals(dto.getSkuType())) {
            if (StringUtils.isBlank(dto.getOrderRemarks())) {
                throw new ParamIllegalException("辅料类型的采购需求单必须填写备注，请填写后重新导入！");
            }
        }


        PurchaseParentOrderPo purchaseParentOrderPo = PurchaseConverter.createDtoToPo(dto);

        purchaseParentOrderPo.setIsDirectSend(WarehouseBaseService.getIsDirectSendByWarehouseType(dto.getWarehouseTypeList()));

        final String purchaseParentOrderNo = idGenerateService.getConfuseCode(ScmConstant.PURCHASE_PARENT_NO_PREFIX, TimeType.CN_DAY_YYYY, ConfuseLength.L_4);
        purchaseParentOrderPo.setPurchaseParentOrderNo(purchaseParentOrderNo);
        purchaseParentOrderDao.insert(purchaseParentOrderPo);
        PurchaseParentOrderChangePo purchaseParentOrderChangePo = PurchaseConverter.parentPoToChangePo(purchaseParentOrderPo);
        purchaseParentOrderChangeDao.insert(purchaseParentOrderChangePo);

        final List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList = purchaseBaseService.createDtoToPoItemList(dto.getPurchaseProductDemandItemList(), purchaseParentOrderNo);

        purchaseParentOrderItemDao.insertBatch(purchaseParentOrderItemPoList);

        List<ScmImageBo> scmImageBoList = PurchaseConverter.createDtoToImageList(dto.getPurchaseProductDemandItemList());
        if (CollectionUtils.isNotEmpty(scmImageBoList)) {
            scmImageBaseService.insertBatchImageBo(scmImageBoList, ImageBizType.PURCHASE_PARENT_ORDER);
        }

        // 创建
        purchaseParentOrderPo.setUpdateTime(LocalDateTime.now());
        logBaseService.simpleLog(LogBizModule.PURCHASE_PARENT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION, purchaseParentOrderPo.getPurchaseParentOrderNo(), PurchaseParentOrderStatus.WAIT_APPROVE.getRemark(), Collections.emptyList());
        logBaseService.purchaseParentVersionLog(LogBizModule.PURCHASE_PARENT_VERSION, ScmConstant.PURCHASE_LOG_VERSION, purchaseParentOrderPo.getPurchaseParentOrderNo(), purchaseParentOrderPo);

        return purchaseParentOrderPo;
    }

    public CommonPageResult.PageInfo<PurchaseSearchNewVo> searchPurchaseNew(PurchaseSearchNewDto dto) {
        PurchaseSearchBo purchaseSearchBo = purchaseBaseService.getItemParentNoList(dto);
        if (purchaseSearchBo == null) {
            return new CommonPageResult.PageInfo<>();
        }
        final CommonPageResult.PageInfo<PurchaseSearchNewVo> pageInfo = purchaseParentOrderDao.searchPurchaseNew(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto, purchaseSearchBo);
        final List<PurchaseSearchNewVo> records = pageInfo.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new CommonPageResult.PageInfo<>();
        }

        final List<String> spuList = records.stream().map(PurchaseSearchNewVo::getSpu).distinct().collect(Collectors.toList());
        final Map<String, String> spuCategoriesMap = plmRemoteService.getSpuCategoriesMapBySpuList(spuList);

        final List<String> purchaseParentNoList = records.stream().map(PurchaseSearchNewVo::getPurchaseParentOrderNo).collect(Collectors.toList());

        // 母单明细
        final List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList = purchaseParentOrderItemDao.getListByParentNoList(purchaseParentNoList);
        final List<String> skuList = purchaseParentOrderItemPoList.stream().map(PurchaseParentOrderItemPo::getSku).distinct().collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        final Map<String, List<String>> purchaseParentNoSkuListMap = purchaseParentOrderItemPoList.stream().collect(Collectors.groupingBy(PurchaseParentOrderItemPo::getPurchaseParentOrderNo, Collectors.mapping(PurchaseParentOrderItemPo::getSku, Collectors.toList())));

        final Map<String, List<String>> platformPurchaseParentNoListMap = records.stream().collect(Collectors.groupingBy(PurchaseSearchNewVo::getPlatform, Collectors.mapping(PurchaseSearchNewVo::getPurchaseParentOrderNo, Collectors.toList())));


        Map<String, List<String>> skuImageMap = new HashMap<>();
        platformPurchaseParentNoListMap.forEach((key, mapPurchaseParentNoList) -> {
            List<String> mapSkuList = purchaseParentNoSkuListMap.entrySet().stream().filter(mapItem -> mapPurchaseParentNoList.contains(mapItem.getKey())).flatMap(mapItem -> mapItem.getValue().stream()).collect(Collectors.toList());

            List<PlmSkuImage> skuImageList = plmRemoteService.getSkuImage(mapSkuList, key);
            Optional.ofNullable(skuImageList).orElse(Collections.emptyList()).forEach(skuImageItem -> {
                skuImageMap.putIfAbsent(key + skuImageItem.getSkuCode(), skuImageItem.getSaleFileCodeList());
            });
        });

        final Map<String, List<PurchaseSkuCntVo>> purchaseParentNoSkuMap = purchaseParentOrderItemPoList.stream().collect(Collectors.groupingBy(PurchaseParentOrderItemPo::getPurchaseParentOrderNo, Collectors.mapping(item -> {
            final PurchaseSkuCntVo purchaseSkuCntVo = new PurchaseSkuCntVo();
            purchaseSkuCntVo.setSkuCode(item.getSku());
            purchaseSkuCntVo.setSkuCnt(item.getPurchaseCnt());
            purchaseSkuCntVo.setSkuEncode(skuEncodeMap.get(item.getSku()));
            return purchaseSkuCntVo;
        }, Collectors.toList())));

        // 查找子单明细
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByParentNoList(purchaseParentNoList);
        final Map<String, PurchaseChildOrderPo> purchaseChildNoMap = purchaseChildOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));
        final List<String> purchaseChildOrderNoList = purchaseChildOrderPoList.stream()
                .map(PurchaseChildOrderPo::getPurchaseChildOrderNo)
                .collect(Collectors.toList());

        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByParentNoList(purchaseParentNoList);
        final List<PurchaseChildOrderChangePo> purchaseChildOrderChangePoList = purchaseChildOrderChangeDao.getListByChildNoList(purchaseChildOrderNoList);
        final Map<String, PurchaseChildOrderChangePo> purchaseChildOrderNoChangePoMap = purchaseChildOrderChangePoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderChangePo::getPurchaseChildOrderNo, Function.identity()));

        final List<PurchaseSearchNewItemVo> purchaseSearchNewItemList = purchaseChildOrderItemPoList.stream()
                .filter(itemPo -> null != purchaseChildNoMap.get(itemPo.getPurchaseChildOrderNo()))
                .map(itemPo -> {
                    final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildNoMap.get(itemPo.getPurchaseChildOrderNo());
                    final PurchaseSearchNewItemVo purchaseSearchNewItemVo = new PurchaseSearchNewItemVo();
                    purchaseSearchNewItemVo.setPurchaseChildOrderNo(itemPo.getPurchaseChildOrderNo());
                    purchaseSearchNewItemVo.setSku(itemPo.getSku());
                    purchaseSearchNewItemVo.setPurchaseCnt(itemPo.getPurchaseCnt());
                    purchaseSearchNewItemVo.setDeliverCnt(itemPo.getDeliverCnt());
                    purchaseSearchNewItemVo.setQualityGoodsCnt(itemPo.getQualityGoodsCnt());
                    purchaseSearchNewItemVo.setPurchaseParentOrderNo(itemPo.getPurchaseParentOrderNo());
                    purchaseSearchNewItemVo.setPurchaseOrderStatus(purchaseChildOrderPo.getPurchaseOrderStatus());
                    purchaseSearchNewItemVo.setExpectedOnShelvesDate(purchaseChildOrderPo.getExpectedOnShelvesDate());
                    purchaseSearchNewItemVo.setCreateTime(purchaseChildOrderPo.getCreateTime());
                    purchaseSearchNewItemVo.setIsUrgentOrder(purchaseChildOrderPo.getIsUrgentOrder());
                    purchaseSearchNewItemVo.setPurchaseOrderType(purchaseChildOrderPo.getPurchaseOrderType());
                    purchaseSearchNewItemVo.setIsOverdue(purchaseChildOrderPo.getIsOverdue());
                    final PurchaseChildOrderChangePo purchaseChildOrderChangePo = purchaseChildOrderNoChangePoMap.get(itemPo.getPurchaseChildOrderNo());
                    purchaseSearchNewItemVo.setPlaceOrderTime(purchaseChildOrderChangePo.getPlaceOrderTime());
                    return purchaseSearchNewItemVo;
                }).collect(Collectors.toList());

        final Map<String, List<PurchaseSearchNewItemVo>> purchaseParentNoVoMap = purchaseSearchNewItemList.stream().collect(Collectors.groupingBy(PurchaseSearchNewItemVo::getPurchaseParentOrderNo));

        //查询生产信息获取封样图
        List<ProduceDataPo> produceDataPoList = produceDataDao.getListBySkuList(skuList);
        Map<String, List<String>> sealImageFileCodeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(produceDataPoList)) {
            List<Long> produceDataIdList = produceDataPoList.stream().map(ProduceDataPo::getProduceDataId).collect(Collectors.toList());
            Map<Long, List<String>> fileCodeMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.SEAL_IMAGE, produceDataIdList);
            for (ProduceDataPo produceDataPo : produceDataPoList) {
                List<String> fileCode = fileCodeMap.get(produceDataPo.getProduceDataId());
                sealImageFileCodeMap.put(produceDataPo.getSku(), fileCode);
            }
        }

        records.forEach(record -> {
            record.setCategoryName(spuCategoriesMap.get(record.getSpu()));
            record.setPurchaseSkuCntList(purchaseParentNoSkuMap.get(record.getPurchaseParentOrderNo()));
            final List<PurchaseSearchNewItemVo> purchaseSearchNewItemVoList = purchaseParentNoVoMap.get(record.getPurchaseParentOrderNo());
            record.setPurchaseSearchNewItemList(purchaseSearchNewItemVoList);

            List<String> fileCodeList = new ArrayList<>();
            Optional.ofNullable(purchaseSearchNewItemVoList).orElse(new ArrayList<>()).forEach(vo -> {
                List<String> fileCodes = skuImageMap.get(record.getPlatform() + vo.getSku());
                if (CollectionUtils.isNotEmpty(fileCodes)) {
                    fileCodeList.addAll(fileCodes);
                }
            });


            Optional.ofNullable(purchaseSearchNewItemVoList).orElse(Collections.emptyList()).stream().findFirst().ifPresent(newItemVo -> record.setSealImageFileCodeList(sealImageFileCodeMap.get(newItemVo.getSku())));

            record.setFileCodeList(fileCodeList);
        });

        return pageInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportPurchaseParent(PurchaseSearchNewDto dto) {
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(), FileOperateBizType.SCM_PURCHASE_PARENT_EXPORT.getCode(), dto));
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportPurchaseParentBySku(PurchaseSearchNewDto dto) {
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(), FileOperateBizType.SCM_PURCHASE_PARENT_SKU.getCode(), dto));
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportPurchaseChild(PurchaseSearchNewDto dto) {
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(), FileOperateBizType.SCM_PURCHASE_CHILD_EXPORT.getCode(), dto));
    }

    public PurchaseDetailNewVo purchaseDetailNew(PurchaseParentNoDto dto) {
        PurchaseParentOrderPo purchaseParentOrderPo = purchaseParentOrderDao.getOneByNo(dto.getPurchaseParentOrderNo());
        Assert.notNull(purchaseParentOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList = purchaseParentOrderItemDao.getListByParentNo(dto.getPurchaseParentOrderNo());
        if (CollectionUtils.isEmpty(purchaseParentOrderItemPoList)) {
            throw new BizException("找不到对应的采购需求单子项,打开详情失败");
        }

        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByParentNo(dto.getPurchaseParentOrderNo());
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByParent(dto.getPurchaseParentOrderNo());
        final Map<String, PurchaseChildOrderItemPo> purchaseChildNoItemMap = purchaseChildOrderItemPoList.stream().collect(Collectors.toMap(PurchaseChildOrderItemPo::getPurchaseChildOrderNo, Function.identity(), (item1, item2) -> item1));


        final List<String> parentOrderItemSkuList = purchaseParentOrderItemPoList.stream().map(PurchaseParentOrderItemPo::getSku).distinct().collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(parentOrderItemSkuList);


        final Map<String, PurchaseOrderStatus> purchaseChildOrderNoStatusMap = purchaseChildOrderPoList.stream().collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, PurchaseChildOrderPo::getPurchaseOrderStatus));

        // 母单已下单数
        final Map<String, Integer> skuPlacedCntMap = purchaseChildOrderItemPoList.stream().filter(purchaseChildOrderItemPo -> {
            final PurchaseOrderStatus purchaseOrderStatus = purchaseChildOrderNoStatusMap.get(purchaseChildOrderItemPo.getPurchaseChildOrderNo());
            if (null == purchaseOrderStatus) {
                return false;
            }
            return !PurchaseOrderStatus.DELETE.equals(purchaseOrderStatus);
        }).collect(Collectors.groupingBy(PurchaseChildOrderItemPo::getSku, Collectors.summingInt(PurchaseChildOrderItemPo::getPurchaseCnt)));

        final Map<String, Integer> skuWaitDeliverMap = purchaseChildOrderItemPoList.stream().filter(itemPo -> {
            final PurchaseOrderStatus purchaseOrderStatus = purchaseChildOrderNoStatusMap.get(itemPo.getPurchaseChildOrderNo());
            return PurchaseOrderStatus.getWaitDeliverCntStatus().contains(purchaseOrderStatus);
        }).collect(Collectors.groupingBy(PurchaseChildOrderItemPo::getSku, Collectors.summingInt(PurchaseChildOrderItemPo::getPurchaseCnt)));
        // 母单sku维度数据
        final List<String> purchaseChildOrderNoList = purchaseChildOrderPoList.stream().map(PurchaseChildOrderPo::getPurchaseChildOrderNo).collect(Collectors.toList());
        // 已发货的发货单
        final List<PurchaseDeliverOrderPo> purchaseDeliveredOrderPoList = purchaseDeliverOrderDao.getListByChildOrderNoListNotStatusList(purchaseChildOrderNoList, Arrays.asList(DeliverOrderStatus.WAIT_DELIVER, DeliverOrderStatus.DELETED));
        final List<String> purchaseDeliverNoList = purchaseDeliveredOrderPoList.stream().map(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo).collect(Collectors.toList());
        final List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList = purchaseDeliverOrderItemDao.getListByDeliverOrderNoList(purchaseDeliverNoList);
        // 计算sku维度已发货数
        final Map<String, Integer> skuDeliveredCntMap = purchaseDeliverOrderItemPoList.stream().collect(Collectors.groupingBy(PurchaseDeliverOrderItemPo::getSku, Collectors.summingInt(PurchaseDeliverOrderItemPo::getDeliverCnt)));
        //  计算sku维度在途数，【待收货】发货数 + 【已收货】、【待质检】、【待入库】收货数
        final Map<String, DeliverOrderStatus> purchaseDeliverNoStatusMap = purchaseDeliveredOrderPoList.stream().collect(Collectors.toMap(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo, PurchaseDeliverOrderPo::getDeliverOrderStatus));
        final Map<String, Integer> skuInTransitCntMap = purchaseDeliverOrderItemPoList.stream().collect(Collectors.groupingBy(PurchaseDeliverOrderItemPo::getSku, Collectors.summingInt(itemPo -> {
            final DeliverOrderStatus deliverOrderStatus = purchaseDeliverNoStatusMap.get(itemPo.getPurchaseDeliverOrderNo());
            if (DeliverOrderStatus.WAIT_RECEIVE.equals(deliverOrderStatus)) {
                return itemPo.getDeliverCnt();
            } else if (DeliverOrderStatus.RECEIVED.equals(deliverOrderStatus) || DeliverOrderStatus.WAIT_QC.equals(deliverOrderStatus) || DeliverOrderStatus.WAIT_WAREHOUSING.equals(deliverOrderStatus)) {
                return itemPo.getReceiptCnt();
            }
            return 0;
        })));


        final List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverBaseService.getDeliverPoByPurchaseNo(purchaseChildOrderNoList);
        final List<String> purchaseDeliverOrderNoList = purchaseDeliverOrderPoList.stream().map(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo).collect(Collectors.toList());
        final Map<String, Integer> returnCntMap = purchaseReturnOrderBaseService.getReturnCntByReturnBizNoList(purchaseDeliverOrderNoList);

        // 子单维度数据
        final Map<String, Integer> childOrderDeliveredCntMap = purchaseDeliverBaseService.getChildOrderDeliveredCntByPurchaseNo(purchaseChildOrderNoList);
        final Map<String, String> deliverChildNoMap = purchaseDeliverOrderPoList.stream().collect(Collectors.toMap(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo, PurchaseDeliverOrderPo::getPurchaseChildOrderNo));
        final List<PurchaseReturnOrderPo> purchaseReturnOrderPoList = purchaseReturnOrderDao.getListByReturnBizNoList(purchaseDeliverOrderNoList);

        final Map<String, List<String>> deliverNoReturnNoMap = purchaseReturnOrderPoList.stream().collect(Collectors.groupingBy(PurchaseReturnOrderPo::getReturnBizNo, Collectors.mapping(PurchaseReturnOrderPo::getReturnOrderNo, Collectors.toList())));

        final Map<String, Integer> purchaseNoReturnCntMap = purchaseReturnOrderBaseService.getChildOrderReturnCntByReturnBizNoMap(deliverChildNoMap, deliverNoReturnNoMap);

        final List<String> skuList = purchaseChildOrderItemPoList.stream().map(PurchaseChildOrderItemPo::getSku).distinct().collect(Collectors.toList());
        final List<PlmSkuImage> skuImage = plmRemoteService.getSkuImage(skuList, purchaseParentOrderPo.getPlatform());
        final Map<String, List<String>> skuImageMap = Optional.ofNullable(skuImage).orElse(Collections.emptyList()).stream().collect(Collectors.toMap(PlmSkuImage::getSkuCode, PlmSkuImage::getSaleFileCodeList, (item1, item2) -> item1));

        return PurchaseConverter.poToDetailNewVo(purchaseParentOrderPo, purchaseParentOrderItemPoList, purchaseChildOrderPoList, skuEncodeMap, purchaseChildNoItemMap, skuPlacedCntMap, skuDeliveredCntMap, returnCntMap, childOrderDeliveredCntMap, purchaseNoReturnCntMap, skuImageMap, skuInTransitCntMap, skuWaitDeliverMap);
    }

    private PurchaseExtraBo getPurchaseExtraInfoPo(List<String> childOrderNoList) {
        final PurchaseExtraBo purchaseExtraBo = new PurchaseExtraBo();
        // 发货信息
        final List<PurchaseDeliverVo> purchaseDeliverList = purchaseDeliverBaseService.getDeliverDetailByNoList(childOrderNoList);
        // 结算信息
        final List<PurchaseSettleSimpleVo> purchaseSettleSimpleList = purchaseSettleOrderBaseService.getPurchaseSettleByBusinessNo(childOrderNoList);
        // 退货信息
        final List<PurchaseReturnSimpleVo> purchaseReturnSimpleList = purchaseReturnOrderBaseService.getPurchaseReturnByPurchaseChildNo(childOrderNoList);
        // 需求变更信息
        final List<PurchaseModifyVo> purchaseModifyList = purchaseReturnOrderBaseService.getPurchaseModifyByPurchaseChildNo(childOrderNoList);

        purchaseExtraBo.setPurchaseDeliverList(purchaseDeliverList);
        purchaseExtraBo.setPurchaseSettleSimpleList(purchaseSettleSimpleList);
        purchaseExtraBo.setPurchaseReturnSimpleList(purchaseReturnSimpleList);
        purchaseExtraBo.setPurchaseModifyList(purchaseModifyList);

        return purchaseExtraBo;
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(key = "#dto.purchaseParentOrderNo", prefix = ScmRedisConstant.SCM_PURCHASE_CHILD_ORDER_NO_CREATE)
    public void splitChildOrderNew(PurchaseSplitNewDto dto) {
        PurchaseParentOrderPo purchaseParentOrderPo = purchaseParentOrderDao.getOneByNo(dto.getPurchaseParentOrderNo());
        Assert.notNull(purchaseParentOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        final List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList = purchaseParentOrderItemDao.getListByParentNo(dto.getPurchaseParentOrderNo());
        Assert.notEmpty(purchaseParentOrderItemPoList, () -> new BizException("找不到对应的采购需求单,拆分子单失败！"));

        if (!PurchaseParentOrderStatus.IN_PROGRESS.equals(purchaseParentOrderPo.getPurchaseParentOrderStatus())) {
            throw new ParamIllegalException("当前采购需求单不处于可拆分状态，数据已被修改或删除，请刷新页面后重试！");
        }

        final List<PurchaseChildOrderPo> dbPurchaseChildOrderPoList = purchaseChildOrderDao.getListByParentNo(dto.getPurchaseParentOrderNo());
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByParent(dto.getPurchaseParentOrderNo());
        final List<PurchaseSplitItemDto> purchaseSplitItemList = dto.getPurchaseSplitItemList();
        // 校验采购数
        this.checkPurchaseCnt(purchaseSplitItemList, purchaseParentOrderItemPoList, dbPurchaseChildOrderPoList, purchaseChildOrderItemPoList);

        // 校验sku范围
        final List<String> skuList = purchaseSplitItemList.stream().map(PurchaseSplitItemDto::getSku).distinct().collect(Collectors.toList());
        this.checkSplitSkuRange(skuList, purchaseParentOrderItemPoList);

        purchaseBaseService.getLatestPurchaseChildNo(purchaseParentOrderPo.getPurchaseParentOrderNo(), dto);
        final Map<String, List<PurchaseChildOrderItemPo>> skuChildItemMap = purchaseBaseService.getChildItemMapBySkuList(skuList);

        // 校验网红仓
        purchaseSplitItemList.forEach(purchaseSplitItemDto -> {
            if (PurchaseDemandType.WH.equals(purchaseParentOrderPo.getPurchaseDemandType()) && !ScmWarehouseUtil.WH_WAREHOUSE_CODE.equals(purchaseSplitItemDto.getWarehouseCode())) {
                throw new ParamIllegalException("采购需求类型为网红时，只能选择网红仓创建，请重新选择仓库后再提交！");
            }
            if (PurchaseDemandType.NORMAL.equals(purchaseParentOrderPo.getPurchaseDemandType()) && ScmWarehouseUtil.WH_WAREHOUSE_CODE.equals(purchaseSplitItemDto.getWarehouseCode())) {
                throw new ParamIllegalException("采购需求类型为常规时，不能选择网红仓创建，请重新选择仓库后再提交！");
            }

            // 检验开发子单关联母单创建采购子单
            if (StringUtils.isNotBlank(purchaseParentOrderPo.getDevelopChildOrderNo())) {
                if (!(PurchaseOrderType.FIRST_ORDER.equals(purchaseSplitItemDto.getPurchaseOrderType())
                        || PurchaseOrderType.PRENATAL.equals(purchaseSplitItemDto.getPurchaseOrderType()))) {
                    throw new ParamIllegalException("开发子单创建的采购需求单，订单类型只允许选择首单或者产前样，请重新选择后再提交！");
                }
            }

        });


        final PurchaseChildAndItemBo purchaseChildAndItemBo = PurchaseConverter.splitDtoItemToPurchaseChildPo(purchaseSplitItemList, purchaseParentOrderPo, skuList.size(), skuChildItemMap);
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildAndItemBo.getPurchaseChildOrderPoList();
        purchaseChildOrderDao.insertBatch(purchaseChildOrderPoList);
        purchaseChildOrderItemDao.insertBatch(purchaseChildAndItemBo.getPurchaseChildOrderItemPoList());
        final List<PurchaseChildOrderChangePo> purchaseChildOrderChangePoList = PurchaseConverter.childPoToChangePo(purchaseChildOrderPoList);
        purchaseChildOrderChangeDao.insertBatch(purchaseChildOrderChangePoList);

        for (PurchaseChildOrderPo po : purchaseChildOrderPoList) {
            logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION, po.getPurchaseChildOrderNo(), PurchaseOrderStatus.WAIT_CONFIRM.getRemark(), Collections.emptyList());
        }
        // 创建单据后推送单号给wms
        final PurchaseChildNoMqDto purchaseChildNoMqDto = new PurchaseChildNoMqDto();
        final List<String> purchaseChildOrderNoList = purchaseChildOrderPoList.stream()
                .map(PurchaseChildOrderPo::getPurchaseChildOrderNo)
                .collect(Collectors.toList());
        purchaseChildNoMqDto.setPurchaseChildOrderNoList(purchaseChildOrderNoList);
        consistencySendMqService.execSendMq(PurchaseChangeHandler.class, purchaseChildNoMqDto);

        // 扣减母单的可拆单数
        final Map<String, Integer> skuPurchaseCntMap = purchaseSplitItemList.stream().collect(Collectors.groupingBy(PurchaseSplitItemDto::getSku, Collectors.summingInt(PurchaseSplitItemDto::getPurchaseCnt)));
        purchaseParentOrderItemPoList.forEach(purchaseParentOrderItemPo -> {
            // 采购数
            final Integer purchaseCnt = skuPurchaseCntMap.getOrDefault(purchaseParentOrderItemPo.getSku(), 0);
            // 可拆分数扣减采购数，最小扣减到0
            final int canSplitCnt = Math.max(purchaseParentOrderItemPo.getCanSplitCnt() - purchaseCnt, 0);
            purchaseParentOrderItemPo.setCanSplitCnt(canSplitCnt);
            purchaseParentOrderItemPo.setUndeliveredCnt(purchaseParentOrderItemPo.getUndeliveredCnt() + purchaseCnt);
        });
        purchaseParentOrderItemDao.updateBatchByIdVersion(purchaseParentOrderItemPoList);

        final int totalPurchaseCnt = purchaseSplitItemList.stream().mapToInt(PurchaseSplitItemDto::getPurchaseCnt).sum();

        // 计算可拆分数
        final int canSplitCnt = Math.max(purchaseParentOrderPo.getCanSplitCnt() - totalPurchaseCnt, 0);
        purchaseParentOrderPo.setCanSplitCnt(canSplitCnt);
        purchaseParentOrderPo.setUndeliveredCnt(purchaseParentOrderPo.getUndeliveredCnt() + totalPurchaseCnt);
        purchaseParentOrderDao.updateByIdVersion(purchaseParentOrderPo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void planConfirm(PurchasePlanConfirmDto dto) {
        final List<PurchasePlanConfirmItemDto> purchasePlanConfirmItemList = dto.getPurchasePlanConfirmItemList();
        final List<String> purchaseChildOrderNoList = purchasePlanConfirmItemList.stream().map(PurchasePlanConfirmItemDto::getPurchaseChildOrderNo).collect(Collectors.toList());

        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList);
        if (purchaseChildOrderNoList.size() != purchaseChildOrderPoList.size()) {
            throw new BizException("查找不到对应的采购子单，请联系系统管理员！");
        }
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNoList(purchaseChildOrderNoList);
        if (purchaseChildOrderNoList.size() != purchaseChildOrderItemPoList.size()) {
            throw new BizException("查找不到对应的采购子单，请联系系统管理员！");
        }
        final List<PurchaseChildOrderChangePo> purchaseChildOrderChangePoList = purchaseChildOrderChangeDao.getListByChildNoList(purchaseChildOrderNoList);
        if (purchaseChildOrderNoList.size() != purchaseChildOrderChangePoList.size()) {
            throw new BizException("查找不到对应的采购子单，请联系系统管理员！");
        }

        final List<String> supplierCodeList = purchasePlanConfirmItemList.stream()
                .map(PurchasePlanConfirmItemDto::getSupplierCode)
                .collect(Collectors.toList());
        final List<SupplierPo> supplierPoList = supplierBaseService.getBySupplierCodeList(supplierCodeList);
        final Map<String, SupplierPo> supplierCodeMap = supplierPoList.stream()
                .collect(Collectors.toMap(SupplierPo::getSupplierCode, Function.identity()));

        final Map<String, PurchaseChildOrderPo> purchaseChildOrderNoMap = purchaseChildOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));

        // 更新消耗产能
        final List<String> skuList = purchaseChildOrderItemPoList.stream()
                .map(PurchaseChildOrderItemPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final List<SkuInfoPo> skuInfoPoList = skuInfoDao.getListBySkuList(skuList);
        final Map<String, SkuInfoPo> skuInfoPoMap = skuInfoPoList.stream()
                .collect(Collectors.toMap(SkuInfoPo::getSku, Function.identity()));
        final Map<String, PurchaseChildOrderItemPo> purchaseChildNoPoMap = purchaseChildOrderItemPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderItemPo::getPurchaseChildOrderNo, Function.identity()));

        purchasePlanConfirmItemList.forEach(itemDto -> {
            final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderNoMap.get(itemDto.getPurchaseChildOrderNo());
            if (null == purchaseChildOrderPo) {
                throw new BizException("查找不到对应的采购子单，请联系系统管理员！");
            }
            final SupplierPo supplierPo = supplierCodeMap.get(itemDto.getSupplierCode());
            if (null == supplierPo) {
                throw new BizException("找不到对应的供应商：{}，请联系系统管理员！", itemDto.getSupplierCode());
            }
            final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildNoPoMap.get(purchaseChildOrderPo.getPurchaseChildOrderNo());
            BigDecimal singleCapacity;
            final SkuInfoPo skuInfoPo = skuInfoPoMap.get(purchaseChildOrderItemPo.getSku());
            if (null == skuInfoPo) {
                singleCapacity = BigDecimal.ZERO;
                log.info("sku:{}没有配置sku_info信息", purchaseChildOrderItemPo.getSku());
            } else {
                singleCapacity = skuInfoPo.getSingleCapacity();
            }

            final BigDecimal capacity = singleCapacity.multiply(new BigDecimal(purchaseChildOrderItemPo.getPurchaseCnt()))
                    .setScale(2, RoundingMode.HALF_UP);
            // 判断供应商有变化
            // 新供应商扣减产能
            final List<SupOpCapacityBo> supOpCapacityList = new ArrayList<>();
            final LocalDate capacityDate = purchaseBaseService.getCapacityDate(purchaseChildOrderPo.getPromiseDate(), itemDto.getSupplierCode());
            final SupOpCapacityBo supOpCapacityBo = new SupOpCapacityBo();
            supOpCapacityBo.setSupplierCode(itemDto.getSupplierCode());
            supOpCapacityBo.setOperateDate(capacityDate);
            supOpCapacityBo.setOperateValue(capacity.negate());
            supOpCapacityBo.setBizNo(itemDto.getPurchaseChildOrderNo());
            supOpCapacityList.add(supOpCapacityBo);

            if (StringUtils.isNotBlank(purchaseChildOrderPo.getSupplierCode())) {
                // 旧供应商增加产能
                final LocalDate capacityDate1 = purchaseBaseService.getCapacityDate(purchaseChildOrderPo.getPromiseDate(), purchaseChildOrderPo.getSupplierCode());
                final SupOpCapacityBo supOpCapacityBo1 = new SupOpCapacityBo();
                supOpCapacityBo1.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
                supOpCapacityBo1.setOperateDate(capacityDate1);
                supOpCapacityBo1.setOperateValue(purchaseChildOrderPo.getCapacity());
                supOpCapacityBo1.setBizNo(itemDto.getPurchaseChildOrderNo());
                supOpCapacityList.add(supOpCapacityBo1);
            }
            supplierCapacityRefService.operateSupplierCapacityBatch(supOpCapacityList);
            purchaseChildOrderPo.setSupplierCode(supplierPo.getSupplierCode());
            purchaseChildOrderPo.setSupplierName(supplierPo.getSupplierName());
            purchaseChildOrderPo.setOrderRemarks(itemDto.getOrderRemarks());
            purchaseChildOrderPo.setPurchaseOrderStatus(purchaseChildOrderPo.getPurchaseOrderStatus().planConfirm());
            purchaseChildOrderPo.setIsUrgentOrder(itemDto.getIsUrgentOrder());
            purchaseChildOrderPo.setDeliverDate(itemDto.getDeliverDate());
            purchaseChildOrderPo.setCapacity(capacity);

            // 产能变更日志
            logBaseService.purchaseCapacityLog(LogBizModule.PURCHASE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                    purchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseChildOrderPo, capacity);

            logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION, purchaseChildOrderPo.getPurchaseChildOrderNo(), PurchaseOrderStatus.WAIT_FOLLOWER_CONFIRM.getRemark(), Collections.emptyList());
        });

        purchaseChildOrderDao.updateBatchByIdVersion(purchaseChildOrderPoList);

        // 设置计划确认人与计划确认时间
        purchaseChildOrderChangePoList.forEach(purchaseChildOrderChangePo -> {
            purchaseChildOrderChangePo.setPlanConfirmUser(GlobalContext.getUserKey());
            purchaseChildOrderChangePo.setPlanConfirmUsername(GlobalContext.getUsername());
            purchaseChildOrderChangePo.setPlanConfirmTime(LocalDateTime.now());
        });
        purchaseChildOrderChangeDao.updateBatchByIdVersion(purchaseChildOrderChangePoList);

        // 单据变更状态后推送单号给wms
        final PurchaseChildNoMqDto purchaseChildNoMqDto = new PurchaseChildNoMqDto();
        purchaseChildNoMqDto.setPurchaseChildOrderNoList(purchaseChildOrderNoList);
        consistencySendMqService.execSendMq(PurchaseChangeHandler.class, purchaseChildNoMqDto);
    }

    private void checkPurchaseCnt(List<PurchaseSplitItemDto> purchaseSplitItemList, List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList, List<PurchaseChildOrderPo> dbPurchaseChildOrderPoList, List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList) {
        final Map<String, Integer> skuPurchaseCntDtoMap = purchaseSplitItemList.stream().collect(Collectors.groupingBy(PurchaseSplitItemDto::getSku, Collectors.summingInt(PurchaseSplitItemDto::getPurchaseCnt)));
        final Map<String, PurchaseChildOrderPo> purchaseChildNoPoMap = Optional.ofNullable(dbPurchaseChildOrderPoList).orElse(Collections.emptyList()).stream().collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));
        final Map<String, Integer> skuPurchaseCntMap = Optional.ofNullable(purchaseChildOrderItemPoList).orElse(Collections.emptyList()).stream().filter(itemPo -> {
            final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildNoPoMap.get(itemPo.getPurchaseChildOrderNo());
            if (null == purchaseChildOrderPo) {
                throw new BizException("数据异常，请联系系统管理员");
            }
            return !PurchaseOrderStatus.DELETE.equals(purchaseChildOrderPo.getPurchaseOrderStatus()) && !PurchaseOrderStatus.RETURN.equals(purchaseChildOrderPo.getPurchaseOrderStatus());
        }).collect(Collectors.groupingBy(PurchaseChildOrderItemPo::getSku, Collectors.summingInt(PurchaseChildOrderItemPo::getPurchaseCnt)));


        skuPurchaseCntMap.forEach((key, value) -> skuPurchaseCntDtoMap.merge(key, value, Integer::sum));


        final Map<String, PurchaseParentOrderItemPo> skuPurchaseParentItemMap = purchaseParentOrderItemPoList.stream().collect(Collectors.toMap(PurchaseParentOrderItemPo::getSku, Function.identity()));

        purchaseSplitItemList.forEach(splitItem -> {
            final PurchaseParentOrderItemPo purchaseParentOrderItemPo = skuPurchaseParentItemMap.get(splitItem.getSku());

            if (null == purchaseParentOrderItemPo) {
                throw new BizException("sku：{}不存在于采购母单，无法拆分该子单项，请重新填写后再提交！");
            }
            if (skuPurchaseCntDtoMap.get(splitItem.getSku()) > purchaseParentOrderItemPo.getPurchaseCnt()) {
                throw new ParamIllegalException("sku:{}的采购数为{}超过了采购上限数:{}，请重新填写后再提交!", splitItem.getSku(), skuPurchaseCntDtoMap.get(splitItem.getSku()), purchaseParentOrderItemPo.getPurchaseCnt());
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void followConfirm(PurchaseFollowConfirmDto dto) {
        final List<PurchaseFollowConfirmItemDto> purchaseFollowConfirmItemList = dto.getPurchaseFollowConfirmItemList();
        final List<String> purchaseChildOrderNoList = purchaseFollowConfirmItemList.stream().map(PurchaseFollowConfirmItemDto::getPurchaseChildOrderNo).collect(Collectors.toList());

        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList);
        if (purchaseChildOrderNoList.size() != purchaseChildOrderPoList.size()) {
            throw new BizException("查找不到对应的采购子单，请联系系统管理员！");
        }

        final Map<String, SkuType> purchaseChildOrderNoSkuTypeMap = purchaseChildOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, PurchaseChildOrderPo::getSkuType));

        // 处理前端入参数据
        purchaseFollowConfirmItemList.forEach(itemDto -> {
            // 全部原料来源为供应商时，则我司原料价格只能填0
            // 判断采购订单是否存在有供应商来源的原料
            final BooleanType allSupplier = this.isAllSupplierRaw(itemDto.getRawProductItemList());
            if (BooleanType.TRUE.equals(allSupplier) && itemDto.getSubstractPrice().compareTo(BigDecimal.ZERO) != 0) {
                throw new ParamIllegalException("原料提供方为{}时，我司原料单价必须为0，请重新修改后提交!",
                        RawSupplier.SUPPLIER.getRemark());
            }
            if (itemDto.getPurchasePrice().subtract(itemDto.getSubstractPrice()).compareTo(itemDto.getSettlePrice()) != 0) {
                throw new BizException("sku：{}，计算结算金额异常，采购价:{}，优惠金额:{}，结算金额:{}，请联系系统管理员！", itemDto.getSku(), itemDto.getPurchasePrice(), itemDto.getSubstractPrice(), itemDto.getSettlePrice());
            }
            final SkuType skuType = purchaseChildOrderNoSkuTypeMap.get(itemDto.getPurchaseChildOrderNo());
            if (!SkuType.SM_SKU.equals(skuType) && BigDecimal.ZERO.compareTo(itemDto.getPurchasePrice()) == 0) {
                throw new ParamIllegalException("sku:{}采购价为0，请先去维护后再进行提交！", itemDto.getSku());
            }
        });


        final Map<String, PurchaseChildOrderPo> purchaseChildOrderNoPoMap = purchaseChildOrderPoList.stream().collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));
        // 校验网红仓
        purchaseFollowConfirmItemList.forEach(itemDto -> {
            final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderNoPoMap.get(itemDto.getPurchaseChildOrderNo());
            if (PurchaseOrderType.WH.equals(purchaseChildOrderPo.getPurchaseOrderType()) && !ScmWarehouseUtil.WH_WAREHOUSE_CODE.equals(itemDto.getWarehouseCode())) {
                throw new ParamIllegalException("采购需求类型为网红时，只能选择网红仓创建，请重新选择仓库后再提交！");
            }
            if (PurchaseOrderType.NORMAL.equals(purchaseChildOrderPo.getPurchaseOrderType()) && ScmWarehouseUtil.WH_WAREHOUSE_CODE.equals(itemDto.getWarehouseCode())) {
                throw new ParamIllegalException("采购需求类型为常规时，不能选择网红仓创建，请重新选择仓库后再提交！");
            }

        });

        final List<String> purchaseParentOrderNoList = purchaseChildOrderPoList.stream().map(PurchaseChildOrderPo::getPurchaseParentOrderNo).distinct().collect(Collectors.toList());
        final List<PurchaseParentOrderPo> purchaseParentOrderPoList = purchaseParentOrderDao.getListByNoList(purchaseParentOrderNoList);

        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNoList(purchaseChildOrderNoList);
        final Map<String, PurchaseFollowConfirmItemDto> purchaseChildNoItemDtoMap = dto.getPurchaseFollowConfirmItemList().stream().collect(Collectors.toMap(PurchaseFollowConfirmItemDto::getPurchaseChildOrderNo, Function.identity()));

        // 采购子单赋值
        purchaseChildOrderPoList.forEach(childPo -> {
                    final PurchaseFollowConfirmItemDto itemDto = purchaseChildNoItemDtoMap.get(childPo.getPurchaseChildOrderNo());
                    // 网红类型判断是否选择网红仓
                    if (PurchaseOrderType.WH.equals(childPo.getPurchaseOrderType())
                            && !ScmWarehouseUtil.WH_WAREHOUSE_CODE.equals(itemDto.getWarehouseCode())) {
                        throw new ParamIllegalException("采购需求类型为网红时，只能选择网红仓创建，请重新选择仓库后再提交！");
                    }
                    childPo.setWarehouseCode(itemDto.getWarehouseCode());
                    childPo.setWarehouseName(itemDto.getWarehouseName());
                    childPo.setWarehouseTypes(String.join(",", Optional.ofNullable(itemDto.getWarehouseTypeList())
                            .orElse(new ArrayList<>())));
                    childPo.setIsDirectSend(WarehouseBaseService.getIsDirectSendByWarehouseType(itemDto.getWarehouseTypeList()));
                    childPo.setOrderRemarks(itemDto.getOrderRemarks());
                    // 根据原料提供方判断，若所有原料来源为供应商时，则采购类型为大货采购，否则为加工采购
                    final BooleanType allSupplier = this.isAllSupplierRaw(itemDto.getRawProductItemList());
                    if (BooleanType.TRUE.equals(allSupplier)) {
                        childPo.setPurchaseBizType(PurchaseBizType.PRODUCT);
                    } else {
                        childPo.setPurchaseBizType(PurchaseBizType.PROCESS);
                    }
                    childPo.setShippableCnt(childPo.getPurchaseTotal());
                }
        );

        // 采购子单item赋值
        final Map<String, PurchaseChildOrderItemPo> purchaseChildNoItemPoMap = purchaseChildOrderItemPoList.stream().collect(Collectors.toMap(itemPo -> itemPo.getPurchaseChildOrderNo() + "-" + itemPo.getSku(), Function.identity()));
        purchaseFollowConfirmItemList.forEach(itemDto -> {
            final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildNoItemPoMap.get(itemDto.getPurchaseChildOrderNo() + "-" + itemDto.getSku());
            Assert.notNull(purchaseChildOrderItemPo, () -> new BizException("采购子单号:{}的sku:{}没有找到对应的采购项，拆分大货采购子单失败！", itemDto.getPurchaseChildOrderNo(), itemDto.getSku()));
            purchaseChildOrderItemPo.setSku(itemDto.getSku());
            purchaseChildOrderItemPo.setPurchaseCnt(itemDto.getPurchaseCnt());
            purchaseChildOrderItemPo.setPurchasePrice(itemDto.getPurchasePrice());
            purchaseChildOrderItemPo.setDiscountType(this.getDiscountTypeBySubstractPrice(itemDto.getSubstractPrice()));
            purchaseChildOrderItemPo.setSubstractPrice(itemDto.getSubstractPrice());
            purchaseChildOrderItemPo.setSettlePrice(itemDto.getSettlePrice());
        });

        purchaseChildOrderItemDao.updateBatchByIdVersion(purchaseChildOrderItemPoList);

        // 状态与日志
        purchaseBaseService.updateStatusAfterCreateChildOrder(purchaseChildOrderPoList, purchaseParentOrderPoList);

        // 跟单确认原料处理
        purchaseRawBizService.followConfirmRaw(purchaseChildOrderNoList, purchaseFollowConfirmItemList,
                purchaseParentOrderPoList, purchaseChildOrderPoList);

        // 单据变更状态后推送单号给wms
        final PurchaseChildNoMqDto purchaseChildNoMqDto = new PurchaseChildNoMqDto();
        purchaseChildNoMqDto.setPurchaseChildOrderNoList(purchaseChildOrderNoList);
        consistencySendMqService.execSendMq(PurchaseChangeHandler.class, purchaseChildNoMqDto);
    }

    /**
     * 判断原料来源是否都为供应商
     *
     * @param rawProductItemList
     * @return
     */
    private BooleanType isAllSupplierRaw(List<RawProductItemDto> rawProductItemList) {
        if (CollectionUtils.isEmpty(rawProductItemList)) {
            return BooleanType.TRUE;
        }
        for (RawProductItemDto rawProductItemDto : rawProductItemList) {
            if (!RawSupplier.SUPPLIER.equals(rawProductItemDto.getRawSupplier())) {
                return BooleanType.FALSE;
            }
        }
        return BooleanType.TRUE;
    }

    private DiscountType getDiscountTypeBySubstractPrice(BigDecimal substractPrice) {
        return substractPrice.compareTo(BigDecimal.ZERO) > 0 ? DiscountType.PROVIDE_RAW : DiscountType.NO_DISCOUNT;
    }

    private void checkSplitSkuRange(List<String> skuList, List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList) {

        final List<String> parentSkuList = purchaseParentOrderItemPoList.stream().map(PurchaseParentOrderItemPo::getSku).collect(Collectors.toList());

        if (!parentSkuList.containsAll(skuList)) {
            throw new BizException("拆分子单的sku必须在母单的sku范围内");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchSubmitApprove(PurchaseParentNoListDto dto) {
        final List<PurchaseParentOrderPo> purchaseParentOrderPoList = purchaseParentOrderDao.getByNoList(dto.getPurchaseParentOrderNoList());
        if (CollectionUtils.isEmpty(purchaseParentOrderPoList) || purchaseParentOrderPoList.size() != dto.getPurchaseParentOrderNoList().size()) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }

        purchaseParentOrderPoList.forEach(po -> {
            final PurchaseParentOrderStatus purchaseOrderStatus = po.getPurchaseParentOrderStatus();
            PurchaseParentOrderStatus targetStatus = purchaseOrderStatus.toWaitApprove();
            po.setPurchaseParentOrderStatus(targetStatus);
        });

        purchaseParentOrderDao.updateBatchByIdVersion(purchaseParentOrderPoList);

        purchaseParentOrderPoList.forEach(po -> {
            logBaseService.simpleLog(LogBizModule.PURCHASE_PARENT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION, po.getPurchaseParentOrderNo(), PurchaseParentOrderStatus.WAIT_APPROVE.getRemark(), Collections.emptyList());
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchApprove(PurchaseApproveDto dto) {
        if (BooleanType.FALSE.equals(dto.getIsPass())) {
            Assert.notBlank(dto.getApproveRemarks(), () -> new BizException("审核不通过时必须填写审批原因"));
        }

        final List<PurchaseParentOrderPo> purchaseParentOrderPoList = purchaseParentOrderDao.getByNoList(dto.getPurchaseParentOrderNoList());
        if (CollectionUtils.isEmpty(purchaseParentOrderPoList) || purchaseParentOrderPoList.size() != dto.getPurchaseParentOrderNoList().size()) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        final List<Long> idList = purchaseParentOrderPoList.stream().map(PurchaseParentOrderPo::getPurchaseParentOrderId).collect(Collectors.toList());
        final List<PurchaseParentOrderChangePo> purchaseParentOrderChangePoList = purchaseParentOrderChangeDao.getListByParentIdList(idList);
        if (CollectionUtils.isEmpty(purchaseParentOrderChangePoList)) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        final List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList = purchaseParentOrderItemDao.getListByParentNoList(dto.getPurchaseParentOrderNoList());
        if (CollectionUtils.isEmpty(purchaseParentOrderItemPoList)) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }

        purchaseParentOrderPoList.forEach(po -> {
            final PurchaseParentOrderStatus purchaseOrderStatus = po.getPurchaseParentOrderStatus();

            PurchaseParentOrderStatus resultStatus;
            if (BooleanType.TRUE.equals(dto.getIsPass())) {
                resultStatus = purchaseOrderStatus.toInProgress();
                po.setCanSplitCnt(po.getPurchaseTotal());
            } else {
                resultStatus = purchaseOrderStatus.toRefuseApprove();
            }

            po.setPurchaseParentOrderStatus(resultStatus);
        });

        purchaseParentOrderDao.updateBatchByIdVersion(purchaseParentOrderPoList);

        // 更新审核时间、审核人
        purchaseParentOrderChangePoList.forEach(po -> {
            po.setApproveTime(new DateTime().toLocalDateTime());
            po.setApproveUser(GlobalContext.getUserKey());
            po.setApproveUsername(GlobalContext.getUsername());
        });

        purchaseParentOrderChangeDao.updateBatchByIdVersion(purchaseParentOrderChangePoList);

        LogVersionBo logVersionBo = null;
        if (StringUtils.isNotBlank(dto.getApproveRemarks())) {
            logVersionBo = new LogVersionBo();
            logVersionBo.setKey("审批原因:");
            logVersionBo.setValue(dto.getApproveRemarks());
            logVersionBo.setValueType(LogVersionValueType.STRING);
        }

        for (PurchaseParentOrderPo purchaseParentOrderPo : purchaseParentOrderPoList) {
            logBaseService.simpleLog(LogBizModule.PURCHASE_PARENT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION, purchaseParentOrderPo.getPurchaseParentOrderNo(), purchaseParentOrderPo.getPurchaseParentOrderStatus().getRemark(), null == logVersionBo ? Collections.emptyList() : Collections.singletonList(logVersionBo));
        }

        if (BooleanType.TRUE.equals(dto.getIsPass())) {
            purchaseParentOrderItemPoList.forEach(po -> po.setCanSplitCnt(po.getPurchaseCnt()));
        }

        purchaseParentOrderItemDao.updateBatchByIdVersion(purchaseParentOrderItemPoList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchConfirmPurchaseOrder(PurchaseBatchConfirmDto dto) {
        // 查找子单
        final List<Long> idList = dto.getPurchaseChildOrderIdList();

        List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getByIdList(idList);

        if (purchaseChildOrderPoList.size() != idList.size()) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }

        List<PurchaseChildOrderChangePo> purchaseChildOrderChangePoList = purchaseChildOrderChangeDao.getByChildOrderId(idList);
        if (purchaseChildOrderChangePoList.size() != idList.size()) {
            throw new BizException("找不到对应的采购需求单,采购需求单取消失败！");
        }

        // 查找母单
        final PurchaseParentOrderPo purchaseParentOrderPo = purchaseParentOrderDao.getOneByNo(dto.getPurchaseParentOrderNo());
        if (null == purchaseParentOrderPo) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }

        final PurchaseParentOrderChangePo purchaseParentOrderChangePo = purchaseParentOrderChangeDao.getByParentId(purchaseParentOrderPo.getPurchaseParentOrderId());
        if (null == purchaseParentOrderChangePo) {
            throw new BizException("找不到对应的采购需求单,采购需求单取消失败！");
        }

        // 更新子单状态
        purchaseChildOrderPoList.forEach(po -> {
            PurchaseOrderStatus purchaseOrderStatus = po.getPurchaseOrderStatus();
            PurchaseOrderStatus waitReceiveOrder = purchaseOrderStatus.toWaitReceiveOrder();
            po.setPurchaseOrderStatus(waitReceiveOrder);

            logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION, po.getPurchaseChildOrderNo(), PurchaseOrderStatus.WAIT_RECEIVE_ORDER.getRemark(), Collections.emptyList());
        });


        // 更新子单change
        purchaseChildOrderDao.updateBatchByIdVersion(purchaseChildOrderPoList);

        purchaseChildOrderChangePoList.forEach(po -> {
            po.setConfirmTime(new DateTime().toLocalDateTime());
            po.setConfirmUser(GlobalContext.getUserKey());
            po.setConfirmUsername(GlobalContext.getUsername());
        });
        purchaseChildOrderChangeDao.updateBatchByIdVersion(purchaseChildOrderChangePoList);

        // 更新母单change
        purchaseParentOrderChangePo.setConfirmTime(new DateTime().toLocalDateTime());
        purchaseParentOrderChangeDao.updateByIdVersion(purchaseParentOrderChangePo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void editPurchase(PurchaseEditDto dto) {
        PurchaseParentOrderPo purchaseParentOrderPo = purchaseParentOrderDao.getByIdVersion(dto.getPurchaseParentOrderId(), dto.getVersion());
        if (null == purchaseParentOrderPo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        // 网红类型判断是否选择网红仓
        if (PurchaseDemandType.WH.equals(dto.getPurchaseDemandType()) && !ScmWarehouseUtil.WH_WAREHOUSE_CODE.equals(dto.getWarehouseCode())) {
            throw new ParamIllegalException("采购需求类型为网红时，只能选择网红仓创建，请重新选择仓库后再提交！");
        }
        // dto赋值给po
        PurchaseConverter.editDtoConvertPo(dto, purchaseParentOrderPo);
        // 更新po
        purchaseParentOrderDao.updateByIdVersion(purchaseParentOrderPo);

        purchaseParentOrderPo.setUpdateTime(LocalDateTime.now());
        logBaseService.purchaseParentVersionLog(LogBizModule.PURCHASE_PARENT_VERSION, ScmConstant.PURCHASE_LOG_VERSION, purchaseParentOrderPo.getPurchaseParentOrderNo(), purchaseParentOrderPo);


        // 删除旧的parentItem
        final String purchaseParentOrderNo = purchaseParentOrderPo.getPurchaseParentOrderNo();
        purchaseParentOrderItemDao.deleteByParentOrderNo(purchaseParentOrderNo);

        // 新增新的parentItem
        final List<PurchaseProductDemandItemDto> purchaseProductDemandItemList = dto.getPurchaseProductDemandItemList();
        final List<PurchaseParentOrderItemPo> newItemList = purchaseBaseService.createDtoToPoItemList(purchaseProductDemandItemList, purchaseParentOrderNo);
        List<ScmImageBo> scmImageBoList = PurchaseConverter.createDtoToImageList(dto.getPurchaseProductDemandItemList());
        scmImageBaseService.insertBatchImageBo(scmImageBoList, ImageBizType.PURCHASE_PARENT_ORDER);

        purchaseParentOrderItemDao.insertBatch(newItemList);
    }

    public PurchaseSplitDetailVo getSplitDetail(PurchaseChildNoListDto dto) {
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(dto.getPurchaseChildOrderNoList());
        Assert.notEmpty(purchaseChildOrderPoList, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNoList(dto.getPurchaseChildOrderNoList());
        Assert.notEmpty(purchaseChildOrderItemPoList, () -> new BizException("找不到对应的采购子单,编辑子单失败！"));
        final List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList = purchaseChildOrderRawDao.getListByChildNoList(dto.getPurchaseChildOrderNoList());

        List<String> skuList = purchaseChildOrderItemPoList.stream().map(PurchaseChildOrderItemPo::getSku).collect(Collectors.toList());
        final List<String> rawSkuList = purchaseChildOrderRawPoList.stream().map(PurchaseChildOrderRawPo::getSku).collect(Collectors.toList());
        skuList.addAll(rawSkuList);
        skuList = skuList.stream().distinct().collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        final List<PurchaseChildOrderVo> purchaseChildOrderList = PurchaseConverter.purchaseChildPoToVo(purchaseChildOrderPoList, purchaseChildOrderItemPoList, skuEncodeMap);
        final List<RawProductItemVo> rawProductItemVoList = PurchaseConverter.rawPoListToVoList(purchaseChildOrderRawPoList, skuEncodeMap);


        return PurchaseSplitDetailVo.builder().purchaseChildOrderList(purchaseChildOrderList).rawProductItemList(rawProductItemVoList).build();
    }

    @Transactional(rollbackFor = Exception.class)
    public void editSplitDetail(PurchaseChildEditDto dto) {

        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getByIdVersion(dto.getPurchaseChildOrderId(), dto.getVersion());
        Assert.notNull(purchaseChildOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildOrderItemDao.getOneByChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        Assert.notNull(purchaseChildOrderItemPo, () -> new BizException("找不到对应的采购子单,编辑子单失败！"));
        final PurchaseChildOrderChangePo purchaseChildOrderChangePo = purchaseChildOrderChangeDao.getByChildOrderId(purchaseChildOrderPo.getPurchaseChildOrderId());
        Assert.notNull(purchaseChildOrderChangePo, () -> new BizException("找不到对应的采购子单,编辑子单失败！"));
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByParentNoAndSku(purchaseChildOrderPo.getPurchaseParentOrderNo(), purchaseChildOrderItemPo.getSku());
        final PurchaseParentOrderItemPo purchaseParentOrderItemPo = purchaseParentOrderItemDao.getOneByParentNoAndSku(purchaseChildOrderPo.getPurchaseParentOrderNo(), purchaseChildOrderItemPo.getSku());
        Assert.notNull(purchaseParentOrderItemPo, () -> new BizException("采购母单不存在该sku：{}的采购项,编辑子单失败！", purchaseChildOrderItemPo.getSku()));
        final PurchaseParentOrderPo purchaseParentOrderPo = purchaseParentOrderDao.getOneByNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
        Assert.notNull(purchaseParentOrderPo, () -> new BizException("采购母单不存在,编辑子单失败！"));
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByParentNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
        final Map<String, PurchaseChildOrderPo> purchaseChildNoMap = purchaseChildOrderPoList.stream().collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));


        final PurchaseOrderStatus purchaseOrderStatus = purchaseChildOrderPo.getPurchaseOrderStatus();
        if (!PurchaseOrderStatus.getEditStatus().contains(purchaseOrderStatus)) {
            throw new BizException("当前采购需求单不处于可编辑状态，不允许编辑");
        }

        if (PurchaseOrderStatus.WAIT_DELIVER.equals(purchaseOrderStatus)) {
            final List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
            if (CollectionUtils.isNotEmpty(purchaseDeliverOrderPoList)) {
                throw new ParamIllegalException("当前采购单已经生成发货单，不允许编辑");
            }
        }

        if (null != dto.getExpectedOnShelvesDate() && !PurchaseOrderStatus.getEditDeliverDateStatus().contains(purchaseOrderStatus)) {
            throw new ParamIllegalException("【{}】状态不可以修改约定交期", purchaseOrderStatus.getRemark());
        }

        if (StringUtils.isNotBlank(dto.getWarehouseCode()) && !purchaseChildOrderPo.getWarehouseCode().equals(dto.getWarehouseCode()) && !PurchaseOrderStatus.getEditWarehouseCodeStatus().contains(purchaseOrderStatus)) {
            throw new ParamIllegalException("【{}】状态不可以修改收货仓库", purchaseOrderStatus.getRemark());
        }

        if (PurchaseOrderStatus.getEditWarehouseCodeStatus().contains(purchaseOrderStatus) && StringUtils.isBlank(dto.getWarehouseCode())) {
            throw new ParamIllegalException("收货仓库不能为空，请选择收货仓库后重新提交！", purchaseOrderStatus.getRemark());
        }
        int skuPurchaseSum = purchaseChildOrderItemPoList.stream().filter(itemPo -> !itemPo.getPurchaseChildOrderNo().equals(purchaseChildOrderPo.getPurchaseChildOrderNo())).filter(itemPo -> {
            final PurchaseChildOrderPo purchaseChildOrderPo1 = purchaseChildNoMap.get(itemPo.getPurchaseChildOrderNo());
            return !PurchaseOrderStatus.DELETE.equals(purchaseChildOrderPo1.getPurchaseOrderStatus()) && !PurchaseOrderStatus.RETURN.equals(purchaseChildOrderPo1.getPurchaseOrderStatus());
        }).mapToInt(PurchaseChildOrderItemPo::getPurchaseCnt).sum();
        skuPurchaseSum += dto.getPurchaseCnt();
        if (skuPurchaseSum > purchaseParentOrderItemPo.getPurchaseCnt()) {
            throw new ParamIllegalException("sku:{}的采购数为{}超过了采购上限数:{}，请重新填写后再提交!", purchaseChildOrderItemPo.getSku(), skuPurchaseSum, purchaseParentOrderItemPo.getPurchaseCnt());
        }
        // 校验是否变更期望上架时间
        final LocalDateTime spanMinDate = ScmTimeUtil.getAfterDate(EXPECTED_DATE_SPAN);
        if (!purchaseChildOrderPo.getExpectedOnShelvesDate().equals(dto.getExpectedOnShelvesDate()) && dto.getExpectedOnShelvesDate().isBefore(spanMinDate)) {
            throw new ParamIllegalException("期望上架时间修改必须在当前时间{}天之后（{}），请重新修改后再提交！", EXPECTED_DATE_SPAN, LocalDateTimeUtil.format(spanMinDate, DatePattern.NORM_DATE_PATTERN));
        }

        // 校验网红仓
        if (PurchaseOrderType.WH.equals(purchaseChildOrderPo.getPurchaseOrderType()) && !ScmWarehouseUtil.WH_WAREHOUSE_CODE.equals(dto.getWarehouseCode())) {
            throw new ParamIllegalException("采购需求类型为网红时，只能选择网红仓创建，请重新选择仓库后再提交！");
        }
        if (PurchaseOrderType.NORMAL.equals(purchaseChildOrderPo.getPurchaseOrderType()) && ScmWarehouseUtil.WH_WAREHOUSE_CODE.equals(dto.getWarehouseCode())) {
            throw new ParamIllegalException("采购需求类型为常规时，不能选择网红仓创建，请重新选择仓库后再提交！");
        }

        // 判断期望上架时间是否修改时才更新
        if (ScmTimeUtil.determineLocalDateTimeEqual(purchaseChildOrderPo.getExpectedOnShelvesDate(), dto.getExpectedOnShelvesDate())) {
            purchaseChildOrderChangePo.setLastModifyUser(GlobalContext.getUserKey());
            purchaseChildOrderChangePo.setLastModifyUsername(GlobalContext.getUsername());
            purchaseChildOrderChangePo.setLastModifyTime(LocalDateTime.now());
            purchaseChildOrderChangeDao.updateByIdVersion(purchaseChildOrderChangePo);
            // 重新计算加急标签
            this.resetUrgentLabel(purchaseChildOrderPo, purchaseChildOrderItemPo, dto.getExpectedOnShelvesDate());
            // 计算延期天数
            final long delayDays = ChronoUnit.DAYS.between(purchaseChildOrderPo.getExpectedOnShelvesDate(), dto.getExpectedOnShelvesDate());
            purchaseChildOrderPo.setDelayDays(Math.toIntExact(delayDays));
        }

        // 进行编辑时，答交日期变化或下单数量变化，推送mq给wms
        if (!dto.getPromiseDate().equals(purchaseChildOrderPo.getPromiseDate())
                || dto.getPurchaseCnt().compareTo(purchaseChildOrderPo.getPurchaseTotal()) != 0) {
            final PurchaseChildNoMqDto purchaseChildNoMqDto = new PurchaseChildNoMqDto();
            purchaseChildNoMqDto.setPurchaseChildOrderNoList(Collections.singletonList(purchaseChildOrderPo.getPurchaseChildOrderNo()));
            consistencySendMqService.execSendMq(PurchaseChangeHandler.class, purchaseChildNoMqDto);
        }

        // 变更了答交时间，并且状态处于待接单之后
        if (!dto.getPromiseDate().equals(purchaseChildOrderPo.getPromiseDate())
                && purchaseChildOrderPo.getPurchaseOrderStatus().getSort() >= PurchaseOrderStatus.WAIT_RECEIVE_ORDER.getSort()) {
            purchaseChildOrderPo.setPromiseDateChg(BooleanType.TRUE);
        }

        final SkuInfoPo skuInfoPo = skuInfoDao.getBySku(purchaseChildOrderItemPo.getSku());

        if (StringUtils.isNotBlank(purchaseChildOrderPo.getSupplierCode())
                && (!dto.getPurchaseCnt().equals(purchaseChildOrderPo.getPurchaseTotal())
                || !dto.getPromiseDate().equals(purchaseChildOrderPo.getPromiseDate()))) {
            // 产能变更， 加上原产能，减去新产能
            final LocalDate capacityDate = purchaseBaseService.getCapacityDate(purchaseChildOrderPo.getPromiseDate(), purchaseChildOrderPo.getSupplierCode());
            BigDecimal singleCapacity;
            if (null == skuInfoPo) {
                singleCapacity = BigDecimal.ZERO;
                log.info("sku:{}没有配置sku_info信息", purchaseChildOrderItemPo.getSku());
            } else {
                singleCapacity = skuInfoPo.getSingleCapacity();
            }
            final BigDecimal capacity = singleCapacity.multiply(new BigDecimal(purchaseChildOrderItemPo.getPurchaseCnt()))
                    .setScale(2, RoundingMode.HALF_UP);
            final SupOpCapacityBo supOpCapacityBo = new SupOpCapacityBo();
            supOpCapacityBo.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
            supOpCapacityBo.setOperateDate(capacityDate);
            supOpCapacityBo.setOperateValue(capacity);
            supOpCapacityBo.setBizNo(purchaseChildOrderPo.getPurchaseChildOrderNo());

            final LocalDate capacityDate1 = purchaseBaseService.getCapacityDate(dto.getPromiseDate(), purchaseChildOrderPo.getSupplierCode());
            final BigDecimal capacity1 = singleCapacity.multiply(new BigDecimal(dto.getPurchaseCnt()))
                    .setScale(2, RoundingMode.HALF_UP);
            final SupOpCapacityBo supOpCapacityBo1 = new SupOpCapacityBo();
            supOpCapacityBo1.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
            supOpCapacityBo1.setOperateDate(capacityDate1);
            supOpCapacityBo1.setOperateValue(capacity1.negate());
            supOpCapacityBo1.setBizNo(purchaseChildOrderPo.getPurchaseChildOrderNo());

            supplierCapacityRefService.operateSupplierCapacityBatch(Arrays.asList(supOpCapacityBo, supOpCapacityBo1));
            // 产能变更日志
            logBaseService.purchaseCapacityLog(LogBizModule.PURCHASE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                    purchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseChildOrderPo, capacity1);

            // 更新单据产能
            purchaseChildOrderPo.setCapacity(capacity1);
        }

        // 更新母单的可拆单数和未交数
        purchaseParentOrderPo.setCanSplitCnt(purchaseParentOrderPo.getCanSplitCnt() + purchaseChildOrderItemPo.getPurchaseCnt() - dto.getPurchaseCnt());
        purchaseParentOrderPo.setUndeliveredCnt(purchaseParentOrderPo.getUndeliveredCnt() - purchaseChildOrderItemPo.getPurchaseCnt() + dto.getPurchaseCnt());
        purchaseParentOrderItemPo.setCanSplitCnt(purchaseParentOrderItemPo.getCanSplitCnt() + purchaseChildOrderItemPo.getPurchaseCnt() - dto.getPurchaseCnt());
        purchaseParentOrderItemPo.setUndeliveredCnt(purchaseParentOrderItemPo.getUndeliveredCnt() - purchaseChildOrderItemPo.getPurchaseCnt() + dto.getPurchaseCnt());
        purchaseParentOrderDao.updateByIdVersion(purchaseParentOrderPo);
        purchaseParentOrderItemDao.updateByIdVersion(purchaseParentOrderItemPo);

        // 更新子单未交数
        purchaseChildOrderItemPo.setUndeliveredCnt(dto.getPurchaseCnt());
        purchaseChildOrderItemPo.setSku(dto.getSku());
        purchaseChildOrderItemPo.setPurchaseCnt(dto.getPurchaseCnt());
        purchaseChildOrderItemPo.setInitPurchaseCnt(dto.getPurchaseCnt());

        purchaseChildOrderPo.setExpectedOnShelvesDate(dto.getExpectedOnShelvesDate());
        purchaseChildOrderPo.setPurchaseBizType(dto.getPurchaseBizType());
        purchaseChildOrderPo.setOrderRemarks(dto.getOrderRemarks());
        purchaseChildOrderPo.setPurchaseTotal(dto.getPurchaseCnt());
        purchaseChildOrderPo.setWarehouseCode(dto.getWarehouseCode());
        purchaseChildOrderPo.setWarehouseName(dto.getWarehouseName());
        purchaseChildOrderPo.setWarehouseTypes(String.join(",", Optional.ofNullable(dto.getWarehouseTypeList())
                .orElse(new ArrayList<>())));
        purchaseChildOrderPo.setShippableCnt(dto.getPurchaseCnt());
        purchaseChildOrderPo.setPromiseDate(dto.getPromiseDate());
        purchaseChildOrderDao.updateByIdVersion(purchaseChildOrderPo);
        purchaseChildOrderItemDao.updateByIdVersion(purchaseChildOrderItemPo);

        // 日志
        purchaseChildOrderPo.setUpdateTime(LocalDateTime.now());
        logBaseService.purchaseChildVersionLog(LogBizModule.PURCHASE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION, purchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseChildOrderPo);

        // 创建单据后推送单号给wms
        final PurchaseChildNoMqDto purchaseChildNoMqDto = new PurchaseChildNoMqDto();
        purchaseChildNoMqDto.setPurchaseChildOrderNoList(Collections.singletonList(purchaseChildOrderPo.getPurchaseChildOrderNo()));
        consistencySendMqService.execSendMq(PurchaseChangeHandler.class, purchaseChildNoMqDto);
    }

    public CommonPageResult.PageInfo<PurchaseProductSearchVo> searchProductPurchase(PurchaseProductSearchDto dto) {
        //条件过滤
        // todo 优化为子查询
        if (null == purchaseBaseService.getSearchPurchaseChildWhere(dto)) {
            return new CommonPageResult.PageInfo<>();
        }

        final CommonPageResult.PageInfo<PurchaseProductSearchVo> pageInfo = purchaseChildOrderDao.searchProductPurchase(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<PurchaseProductSearchVo> records = pageInfo.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new CommonPageResult.PageInfo<>();
        }

        final List<String> purchaseChildNoList = records.stream().map(PurchaseProductSearchVo::getPurchaseChildOrderNo).collect(Collectors.toList());
        // 获取子单明细
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNoList(purchaseChildNoList);
        final Map<String, PurchaseChildOrderItemPo> purchaseChildOrderNoSkuMap = purchaseChildOrderItemPoList.stream().collect(Collectors.toMap(PurchaseChildOrderItemPo::getPurchaseChildOrderNo, Function.identity(), (item1, item2) -> item2));
        final List<String> skuList = purchaseChildOrderItemPoList.stream().map(PurchaseChildOrderItemPo::getSku).distinct().collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        // 获取发货单
        final List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverBaseService.getDeliverPoByPurchaseNo(purchaseChildNoList);
        final Map<String, List<PurchaseDeliverOrderPo>> purchaseChildOrderNoPoListMap = purchaseDeliverOrderPoList.stream().collect(Collectors.groupingBy(PurchaseDeliverOrderPo::getPurchaseChildOrderNo));
        final List<String> purchaseDeliverOrderNoList = purchaseDeliverOrderPoList.stream().map(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo).distinct().collect(Collectors.toList());
        final List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList = purchaseDeliverOrderItemDao.getListByDeliverOrderNoList(purchaseDeliverOrderNoList);
        final Map<String, Integer> purchaseDeliverOrderNoReceiptCntMap = purchaseDeliverOrderItemPoList.stream().collect(Collectors.groupingBy(PurchaseDeliverOrderItemPo::getPurchaseDeliverOrderNo, Collectors.summingInt(PurchaseDeliverOrderItemPo::getReceiptCnt)));

        Map<String, SupplierProductComparePo> supplierProductCompareMap = supplierProductCompareDao.getBatchSkuMap(skuList);
        Map<String, PurchaseProductSearchVo> purchaseProductSearchVoMap = records.stream().collect(Collectors.toMap(PurchaseProductSearchVo::getPurchaseChildOrderNo, Function.identity(), (item1, item2) -> item1));
        final Map<String, List<PurchaseSkuCntVo>> purchaseChildNoSkuMap = purchaseChildOrderItemPoList.stream().collect(Collectors.groupingBy(PurchaseChildOrderItemPo::getPurchaseChildOrderNo, Collectors.mapping(item -> {
            final PurchaseSkuCntVo purchaseSkuCntVo = new PurchaseSkuCntVo();
            purchaseSkuCntVo.setSkuCode(item.getSku());
            purchaseSkuCntVo.setSkuCnt(item.getPurchaseCnt());
            PurchaseProductSearchVo purchaseProductVo = purchaseProductSearchVoMap.get(item.getPurchaseChildOrderNo());
            if (null != purchaseProductVo) {
                SupplierProductComparePo supplierProductComparePo = supplierProductCompareMap.get(purchaseProductVo.getSupplierCode() + item.getSku());
                if (null != supplierProductComparePo) {
                    purchaseSkuCntVo.setSupplierProductName(supplierProductComparePo.getSupplierProductName());
                }
            }
            purchaseSkuCntVo.setSkuEncode(skuEncodeMap.get(item.getSku()));
            return purchaseSkuCntVo;
        }, Collectors.toList())));
        supplierBaseService.batchSetSupplierGrade(records);
        final List<String> spuList = records.stream().map(PurchaseProductSearchVo::getSpu).distinct().collect(Collectors.toList());
        final Map<String, String> spuCategoriesMap = plmRemoteService.getSpuCategoriesMapBySpuList(spuList);

        final List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList = purchaseChildOrderRawDao.getListByChildNoList(purchaseChildNoList, PurchaseRawBizType.DEMAND);
        final List<String> rawSkuList = purchaseChildOrderRawPoList.stream()
                .map(PurchaseChildOrderRawPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> rawSkuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(rawSkuList);

        // 获取退货信息
        final List<PurchaseReturnSimpleVo> purchaseReturnSimpleList = purchaseReturnOrderBaseService.getPurchaseReturnByPurchaseChildNo(purchaseChildNoList);
        final Map<String, List<PurchaseReturnSimpleVo>> purchaseChildNoReturnVoMap = purchaseReturnSimpleList.stream().collect(Collectors.groupingBy(PurchaseReturnSimpleVo::getPurchaseChildOrderNo));

        // 获取平台名称
        final List<String> platCodeList = records.stream().map(PurchaseProductSearchVo::getPlatform).distinct().collect(Collectors.toList());
        final Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);
        // 返回sku是否需管理
        final List<ProduceDataPo> produceDataPoList = produceDataDao.getListBySkuList(skuList);
        final Map<String, BooleanType> skuRawManageMap = produceDataPoList.stream()
                .collect(Collectors.toMap(ProduceDataPo::getSku, ProduceDataPo::getRawManage));

        records.forEach(record -> {
            final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildOrderNoSkuMap.get(record.getPurchaseChildOrderNo());
            if (null != purchaseChildOrderItemPo) {
                record.setSku(purchaseChildOrderItemPo.getSku());
                record.setSkuEncode(skuEncodeMap.get(purchaseChildOrderItemPo.getSku()));
                record.setSettlePrice(purchaseChildOrderItemPo.getSettlePrice());
                record.setPurchasePrice(purchaseChildOrderItemPo.getPurchasePrice());
                record.setUndeliveredCnt(purchaseChildOrderItemPo.getUndeliveredCnt());

                record.setRawManage(skuRawManageMap.getOrDefault(purchaseChildOrderItemPo.getSku(), BooleanType.FALSE));
            }
            record.setPlatformName(platCodeNameMap.get(record.getPlatform()));
            record.setWarehouseTypeList(FormatStringUtil.string2List(record.getWarehouseTypes(), ","));
            final List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList1 = purchaseChildOrderNoPoListMap.get(record.getPurchaseChildOrderNo());
            if (CollectionUtils.isNotEmpty(purchaseDeliverOrderPoList1)) {
                final List<PurchaseDeliverSimpleVo> purchaseDeliverSimpleVoList = PurchaseConverter.deliverPoToSimpleVo(purchaseDeliverOrderPoList1, purchaseDeliverOrderNoReceiptCntMap);
                record.setPurchaseDeliverOrderList(purchaseDeliverSimpleVoList);
                final PurchaseDeliverOrderPo earliestPurchaseDeliverOrderPo = purchaseDeliverOrderPoList1.stream().filter(purchaseDeliverOrderPo -> purchaseDeliverOrderPo.getDeliverTime() != null).min(Comparator.comparing(PurchaseDeliverOrderPo::getDeliverTime)).orElse(new PurchaseDeliverOrderPo());
                record.setFirstDeliverTime(earliestPurchaseDeliverOrderPo.getDeliverTime());
            }

            record.setSkuList(purchaseChildNoSkuMap.get(record.getPurchaseChildOrderNo()));
            record.setCategoryName(spuCategoriesMap.get(record.getSpu()));

            // 赋值原料信息
            final List<RawProductItemVo> rawProductItemVoList = PurchaseConverter.rawPoListToVoList(purchaseChildOrderRawPoList,
                    rawSkuEncodeMap);
            record.setRawProductItemList(rawProductItemVoList);

            // 判断采购订单是否存在有供应商来源的原料
            BooleanType isExistSupplierRaw = BooleanType.FALSE;
            for (RawProductItemVo rawProductItemVo : rawProductItemVoList) {
                if (RawSupplier.SUPPLIER.equals(rawProductItemVo.getRawSupplier())) {
                    isExistSupplierRaw = BooleanType.TRUE;
                    break;
                }
            }
            record.setIsExistSupplierRaw(isExistSupplierRaw);


            // 退货信息
            record.setPurchaseReturnSimpleList(purchaseChildNoReturnVoMap.get(record.getPurchaseChildOrderNo()));
        });

        // 按照平台分组，请求plm获取图片设置
        records.stream().collect(Collectors.groupingBy(PurchaseProductSearchVo::getPlatform)).forEach((platform, list) -> {
            List<String> skuList2 = list.stream().map(PurchaseProductSearchVo::getSku).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
            List<PlmSkuImage> skuImage = plmRemoteService.getSkuImage(skuList2, platform);
            Map<String, PlmSkuImage> skuImageMap = Optional.ofNullable(skuImage).orElse(Collections.emptyList()).stream().collect(Collectors.toMap(PlmSkuImage::getSkuCode, Function.identity()));
            for (PurchaseProductSearchVo purchaseProductSearchVo : list) {
                String sku = purchaseProductSearchVo.getSku();
                PlmSkuImage plmSkuImage = skuImageMap.get(sku);
                purchaseProductSearchVo.setFileCodeList(Optional.ofNullable(plmSkuImage).map(PlmSkuImage::getSaleFileCodeList).orElse(Collections.emptyList()));
            }
        });

        return pageInfo;
    }

    public PurchaseChildDetailVo childOrderPurchaseDetail(PurchaseChildDetailDto dto) {
        PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNoAndType(dto.getPurchaseChildOrderNo(), dto.getPurchaseBizType(), dto.getAuthSupplierCode());
        if (null == purchaseChildOrderPo) {
            final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildOrderItemDao.getOneBySkuBatchCode(dto.getPurchaseChildOrderNo());
            Assert.notNull(purchaseChildOrderItemPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
            purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNoAndType(purchaseChildOrderItemPo.getPurchaseChildOrderNo(), dto.getPurchaseBizType(), dto.getAuthSupplierCode());
        }
        Assert.notNull(purchaseChildOrderPo, () -> new ParamIllegalException("找不到对应的采购子单,获取子单详情失败!"));
        final PurchaseParentOrderPo purchaseParentOrderPo = purchaseParentOrderDao.getOneByNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
        Assert.notNull(purchaseParentOrderPo, () -> new ParamIllegalException("找不到对应的采购需求母单,获取子单详情失败!"));
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        Assert.notEmpty(purchaseChildOrderItemPoList, () -> new ParamIllegalException("找不到对应的采购子单,获取子单详情失败!"));
        final List<PurchaseChildOrderVo> purchaseChildOrderVoList = purchaseChildOrderDao.getListByParentNoAndStatus(purchaseChildOrderPo.getPurchaseParentOrderNo(), dto.getPurchaseOrderStatusList(), dto.getAuthSupplierCode());
        Assert.notEmpty(purchaseChildOrderVoList, () -> new ParamIllegalException("找不到对应的采购子单,获取子单详情失败!"));
        purchaseBaseService.convertSkuForPurchaseChildVo(purchaseChildOrderVoList);
        PurchaseModifyOrderPo purchaseModifyOrderPo = purchaseModifyOrderDao.getOneByChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        String downReturnOrderNo = StringUtils.EMPTY;
        if (null != purchaseModifyOrderPo) {
            downReturnOrderNo = purchaseModifyOrderPo.getDownReturnOrderNo();
        }
        final List<PurchaseModifyOrderItemPo> purchaseModifyOrderItemPoList = purchaseModifyOrderItemDao.getListByNoList(Collections.singletonList(downReturnOrderNo));
        // 降档map
        final Map<String, Integer> modifySkuMap = purchaseModifyOrderItemPoList.stream().collect(Collectors.toMap(PurchaseModifyOrderItemPo::getSku, PurchaseModifyOrderItemPo::getNewPurchaseCnt));


        // 根据供应商与母单号查找母单明细，再根据sku聚合
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList1 = purchaseChildOrderDao.getListByParentNoAndSupplier(purchaseChildOrderPo.getSupplierCode(), purchaseChildOrderPo.getPurchaseParentOrderNo(), PurchaseOrderStatus.getSupplierPurchaseStatusList());
        final List<String> purchaseChildNoList = purchaseChildOrderPoList1.stream().map(PurchaseChildOrderPo::getPurchaseChildOrderNo).collect(Collectors.toList());

        final List<PurchaseChildOrderItemPo> purchaseParentOrderItemPoList = purchaseChildOrderItemDao.getListByChildNoList(purchaseChildNoList);

        final List<PurchaseChildOrderItemPo> skuMergeItemPoList = new ArrayList<>(purchaseParentOrderItemPoList.stream().collect(Collectors.toMap(PurchaseChildOrderItemPo::getSku, a -> a, (item1, item2) -> {
            item1.setPurchaseCnt(item1.getPurchaseCnt() + item2.getPurchaseCnt());
            item1.setDeliverCnt(item1.getDeliverCnt() + item2.getDeliverCnt());
            item1.setQualityGoodsCnt(item1.getQualityGoodsCnt() + item2.getQualityGoodsCnt());
            item1.setDefectiveGoodsCnt(item1.getDefectiveGoodsCnt() + item2.getDefectiveGoodsCnt());

            return item1;
        })).values());

        //组装数据获取供应商产品名称
        final List<String> childOrderItemSkuList = purchaseChildOrderItemPoList.stream().map(PurchaseChildOrderItemPo::getSku).distinct().collect(Collectors.toList());
        Map<String, SupplierProductComparePo> supplierProductCompareMap = supplierProductCompareDao.getBatchSkuMap(childOrderItemSkuList);
        final List<String> childOrderSkuList = purchaseChildOrderVoList.stream().map(PurchaseChildOrderVo::getSku).distinct().collect(Collectors.toList());

        childOrderItemSkuList.addAll(childOrderSkuList);
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(childOrderItemSkuList);


        // 获取sku是否需管理
        final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildOrderItemPoList.get(0);
        final ProduceDataPo produceDataPo = produceDataDao.getBySku(purchaseChildOrderItemPo.getSku());

        final PurchaseChildDetailVo purchaseChildDetailVo = PurchaseConverter.poToChildDetailVo(purchaseChildOrderPo,
                purchaseParentOrderPo, purchaseChildOrderItemPoList, purchaseChildOrderVoList, modifySkuMap,
                skuMergeItemPoList, supplierProductCompareMap, skuEncodeMap, produceDataPo);

        // 获取生产信息
        final SampleProductVo sampleProductVo = sampleBaseService.getSampleProductVoByChildNo(purchaseChildOrderPo.getSampleChildOrderNo());
        purchaseChildDetailVo.setSampleProductVo(sampleProductVo);
        // 根据子单号列表获取扩展信息
        PurchaseExtraBo purchaseExtraBo = this.getPurchaseExtraInfoPo(Collections.singletonList(purchaseChildOrderPo.getPurchaseChildOrderNo()));
        purchaseChildDetailVo.setPurchaseDeliverList(purchaseExtraBo.getPurchaseDeliverList());
        purchaseChildDetailVo.setPurchaseSettleSimpleList(purchaseExtraBo.getPurchaseSettleSimpleList());
        purchaseChildDetailVo.setPurchaseReturnSimpleList(purchaseExtraBo.getPurchaseReturnSimpleList());
        purchaseChildDetailVo.setPurchaseModifyList(purchaseExtraBo.getPurchaseModifyList());
        //  原料列表出参
        final List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList = purchaseChildOrderRawDao.getListByChildNo(purchaseChildOrderPo.getPurchaseChildOrderNo(),
                PurchaseRawBizType.ACTUAL_DELIVER);
        final List<PurchaseChildOrderRawPo> demandPurchaseChildOrderRawPoList = purchaseChildOrderRawDao.getListByChildNo(purchaseChildOrderPo.getPurchaseChildOrderNo(),
                PurchaseRawBizType.DEMAND);
        final List<PurchaseChildOrderRawPo> bomPurchaseChildOrderRawPoList = purchaseChildOrderRawDao.getListByChildNo(purchaseChildOrderPo.getPurchaseChildOrderNo(),
                PurchaseRawBizType.FORMULA);
        List<String> skuList = purchaseChildOrderRawPoList.stream()
                .map(PurchaseChildOrderRawPo::getSku)
                .collect(Collectors.toList());
        final List<String> bomSkuList = bomPurchaseChildOrderRawPoList.stream()
                .map(PurchaseChildOrderRawPo::getSku)
                .collect(Collectors.toList());
        skuList.addAll(bomSkuList);
        skuList = skuList.stream()
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> rawSkuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        final int qualityGoodsCnt = purchaseChildOrderItemPoList.stream()
                .mapToInt(PurchaseChildOrderItemPo::getQualityGoodsCnt)
                .sum();
        final List<RawProductItemVo> rawProductItemVoList = PurchaseConverter.rawPoListToVoList(purchaseChildOrderRawPoList,
                rawSkuEncodeMap);
        purchaseChildDetailVo.setRawProductItemList(rawProductItemVoList);

        final List<RawProductItemVo> bomRawProductItemVoList = PurchaseConverter.rawPoListToVoList(bomPurchaseChildOrderRawPoList, rawSkuEncodeMap);

        purchaseChildDetailVo.setPurchaseRawList(bomRawProductItemVoList);

        final List<RawRemainItemVo> rawRemainItemList = PurchaseConverter.rawPoListToRemainVoList(bomPurchaseChildOrderRawPoList, demandPurchaseChildOrderRawPoList,
                qualityGoodsCnt, rawSkuEncodeMap);
        purchaseChildDetailVo.setRawRemainItemList(rawRemainItemList);

        supplierBaseService.batchSetSupplierGrade(Collections.singletonList(purchaseChildDetailVo));

        // 查询海外仓信息
        if (BooleanType.TRUE.equals(purchaseChildOrderPo.getIsDirectSend())) {
            final OverseasWarehouseMsgVo overseasWarehouseMsgVo = overseasBizService.getOverseasMsgByPurchaseChildNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
            purchaseChildDetailVo.setOverseasWarehouseMsgVo(overseasWarehouseMsgVo);
        }
        final BigDecimal settlePrice = purchaseChildOrderItemPoList.stream().map(PurchaseChildOrderItemPo::getSettlePrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        purchaseChildDetailVo.setFinishRemark(purchaseChildOrderPo.getFinishRemark());
        purchaseChildDetailVo.setSkuType(purchaseChildOrderPo.getSkuType());
        purchaseChildDetailVo.setDeliverDate(purchaseChildOrderPo.getDeliverDate());
        purchaseChildDetailVo.setSettlePrice(settlePrice);
        purchaseChildDetailVo.setAdjustPriceApproveNo(purchaseChildOrderPo.getAdjustPriceApproveNo());
        purchaseChildDetailVo.setReturnOrderNo(purchaseChildOrderPo.getReturnOrderNo());

        // 根据供应商库存出库记录判断是否可归还至备货仓
        final List<SupplierInventoryRecordPo> supplierInventoryRecordPoList = supplierInventoryRecordDao.getListByRelateNo(dto.getPurchaseChildOrderNo(),
                SupplierInventoryCtrlType.OUTBOUND, SupplierWarehouse.STOCK_UP);
        if (CollectionUtils.isEmpty(supplierInventoryRecordPoList)) {
            purchaseChildDetailVo.setReturnStockUp(BooleanType.FALSE);
        } else {
            purchaseChildDetailVo.setReturnStockUp(BooleanType.TRUE);
        }
        return purchaseChildDetailVo;
    }

    /**
     * 批量打印采购子单
     *
     * @param dto
     * @return
     */
    public List<PurchaseChildPrintVo> batchPrintProcessPurchase(PurchaseChildNoListDto dto) {
        List<String> purchaseChildOrderNoList = dto.getPurchaseChildOrderNoList();
        List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList);
        if (CollectionUtils.isEmpty(purchaseChildOrderPoList)) {
            throw new ParamIllegalException("采购子单号不存在");
        }

        // 获取所有的采购详情
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNoList(purchaseChildOrderNoList);
        Assert.notEmpty(purchaseChildOrderItemPoList, () -> new ParamIllegalException("找不到对应的采购子单,获取子单详情失败!"));

        // 获取生产信息
        SampleSkuListDto sampleSkuListDto = new SampleSkuListDto();
        List<String> skuList = purchaseChildOrderItemPoList.stream().map(PurchaseChildOrderItemPo::getSku).distinct().collect(Collectors.toList());
        sampleSkuListDto.setSkuList(skuList);
        List<SampleInfoVo> sampleInfoBySkuList = sampleBaseService.getSampleInfoBySkuList(sampleSkuListDto);
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        final Map<String, List<String>> skuFileCodeListMap = produceDataBaseService.getBomImageBySkuList(skuList);

        return purchaseChildOrderPoList.stream().map(item -> {
            PurchaseChildPrintVo purchaseChildPrintVo = new PurchaseChildPrintVo();
            purchaseChildPrintVo.setPurchaseChildOrderId(item.getPurchaseChildOrderId());
            purchaseChildPrintVo.setPurchaseChildOrderNo(item.getPurchaseChildOrderNo());
            purchaseChildPrintVo.setVersion(item.getVersion());
            purchaseChildPrintVo.setPrintUsername(GlobalContext.getUsername());
            purchaseChildPrintVo.setPrintTime(new DateTime().toLocalDateTime());
            purchaseChildPrintVo.setSupplierCode(item.getSupplierCode());
            purchaseChildPrintVo.setSupplierName(item.getSupplierName());
            purchaseChildPrintVo.setPurchaseTotal(item.getPurchaseTotal());
            purchaseChildPrintVo.setWarehouseCode(item.getWarehouseCode());
            purchaseChildPrintVo.setWarehouseName(item.getWarehouseName());
            purchaseChildPrintVo.setExpectedOnShelvesDate(item.getExpectedOnShelvesDate());
            purchaseChildPrintVo.setWarehouseTypeList(FormatStringUtil.string2List(item.getWarehouseTypes(), ","));
            List<PurchaseChildOrderItemPo> needPurchaseChildOrderItemPoList = purchaseChildOrderItemPoList.stream().filter(it -> item.getPurchaseChildOrderNo().equals(it.getPurchaseChildOrderNo())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(needPurchaseChildOrderItemPoList)) {
                throw new ParamIllegalException("采购子单{}不存在sku", item.getPurchaseParentOrderNo());
            }
            List<PurchaseChildPrintVo.ChildOrderPurchaseItem> childOrderPurchaseItemList = needPurchaseChildOrderItemPoList.stream().map(itemPo -> {
                PurchaseChildPrintVo.ChildOrderPurchaseItem childOrderPurchaseItem = new PurchaseChildPrintVo.ChildOrderPurchaseItem();
                childOrderPurchaseItem.setPurchaseChildOrderItemId(itemPo.getPurchaseChildOrderItemId());
                childOrderPurchaseItem.setSku(itemPo.getSku());
                childOrderPurchaseItem.setSkuBatchCode(itemPo.getSkuBatchCode());
                childOrderPurchaseItem.setPurchaseCnt(itemPo.getPurchaseCnt());
                childOrderPurchaseItem.setSkuEncode(skuEncodeMap.get(itemPo.getSku()));
                childOrderPurchaseItem.setFileCodeList(skuFileCodeListMap.get(itemPo.getSku()));

                return childOrderPurchaseItem;
            }).collect(Collectors.toList());

            purchaseChildPrintVo.setPurchaseProductItemList(childOrderPurchaseItemList);

            if (CollectionUtils.isNotEmpty(sampleInfoBySkuList)) {
                List<SampleInfoVo> sampleInfoVoList = sampleInfoBySkuList.stream().filter(it -> skuList.contains(it.getSku())).collect(Collectors.toList());
                purchaseChildPrintVo.setSampleInfoVoList(sampleInfoVoList);
            }

            return purchaseChildPrintVo;

        }).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void forceFinishPurchase(PurchaseFinishDto dto) {
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getByIdVersion(dto.getPurchaseChildOrderId(), dto.getVersion());
        Assert.notNull(purchaseChildOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildOrderItemDao.getOneByChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        Assert.notNull(purchaseChildOrderItemPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        final PurchaseParentOrderPo purchaseParentOrderPo = purchaseParentOrderDao.getOneByNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
        Assert.notNull(purchaseParentOrderPo, () -> new BizException("找不到对应的采购需求母单,强制完成失败!"));
        final List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList = purchaseParentOrderItemDao.getListByParentNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
        Assert.notEmpty(purchaseParentOrderItemPoList, () -> new BizException("找不到对应的采购需求母单,强制完成失败!"));

        // 释放供应商对应的产能
        final List<SupOpCapacityBo> supOpCapacityBoList = new ArrayList<>();
        final LocalDate capacityDate = purchaseBaseService.getCapacityDate(purchaseChildOrderPo.getPromiseDate(), purchaseChildOrderPo.getSupplierCode());
        final SkuInfoPo skuInfoPo = skuInfoDao.getBySku(purchaseChildOrderItemPo.getSku());
        BigDecimal singleCapacity;
        if (null == skuInfoPo) {
            singleCapacity = BigDecimal.ZERO;
            log.info("sku:{}没有配置sku_info信息", purchaseChildOrderItemPo.getSku());
        } else {
            singleCapacity = skuInfoPo.getSingleCapacity();
        }
        final BigDecimal capacity = singleCapacity.multiply(new BigDecimal(purchaseChildOrderPo.getShippableCnt()))
                .setScale(2, RoundingMode.HALF_UP);
        final SupOpCapacityBo supOpCapacityBo = new SupOpCapacityBo();
        supOpCapacityBo.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
        supOpCapacityBo.setOperateDate(capacityDate);
        supOpCapacityBo.setOperateValue(capacity);
        supOpCapacityBo.setBizNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        supOpCapacityBoList.add(supOpCapacityBo);
        supplierCapacityRefService.operateSupplierCapacityBatch(supOpCapacityBoList);
        // 产能变更日志
        logBaseService.purchaseCapacityLog(LogBizModule.PURCHASE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseChildOrderPo, purchaseChildOrderPo.getCapacity());

        purchaseParentOrderPo.setUndeliveredCnt(Math.max(0, purchaseParentOrderPo.getUndeliveredCnt() - purchaseChildOrderItemPo.getUndeliveredCnt()));
        purchaseParentOrderDao.updateByIdVersion(purchaseParentOrderPo);

        purchaseParentOrderItemPoList.forEach(purchaseParentOrderItemPo -> {
            if (purchaseParentOrderItemPo.getSku().equals(purchaseChildOrderItemPo.getSku())) {
                purchaseParentOrderItemPo.setUndeliveredCnt(Math.max(0, purchaseParentOrderItemPo.getUndeliveredCnt() - purchaseChildOrderItemPo.getUndeliveredCnt()));
            }
        });
        purchaseParentOrderItemDao.updateBatchByIdVersion(purchaseParentOrderItemPoList);

        final PurchaseOrderStatus purchaseOrderStatus = purchaseChildOrderPo.getPurchaseOrderStatus().forceToFinish();
        purchaseChildOrderPo.setPurchaseOrderStatus(purchaseOrderStatus);
        purchaseChildOrderPo.setShippableCnt(0);
        purchaseChildOrderPo.setFinishRemark(dto.getFinishRemark());
        purchaseChildOrderPo.setCapacity(BigDecimal.ZERO);
        purchaseChildOrderDao.updateByIdVersion(purchaseChildOrderPo);

        purchaseChildOrderItemPo.setUndeliveredCnt(0);
        purchaseChildOrderItemDao.updateByIdVersion(purchaseChildOrderItemPo);

        // 如果采购子单状态变更为已完结，需要更新生产资料信息的商品采购价格
        if (purchaseChildOrderItemPo.getPurchasePrice() != null && StringUtils.isNotBlank(purchaseChildOrderItemPo.getSku())) {
            ProduceDataUpdatePurchasePriceBo produceDataUpdatePurchasePriceBo = new ProduceDataUpdatePurchasePriceBo();
            produceDataUpdatePurchasePriceBo.setSku(purchaseChildOrderItemPo.getSku());
            produceDataUpdatePurchasePriceBo.setGoodsPurchasePrice(purchaseChildOrderItemPo.getPurchasePrice());
            produceDataBaseService.updateGoodsPurchasePriceBySku(produceDataUpdatePurchasePriceBo);
        }

        logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION, purchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseOrderStatus.getRemark(), Collections.emptyList());

        // 产能变更日志
        logBaseService.purchaseCapacityLog(LogBizModule.PURCHASE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseChildOrderPo, purchaseChildOrderPo.getCapacity());

        // 单据状态变更后推送单号给wms
        final PurchaseChildNoMqDto purchaseChildNoMqDto = new PurchaseChildNoMqDto();
        purchaseChildNoMqDto.setPurchaseChildOrderNoList(Collections.singletonList(purchaseChildOrderPo.getPurchaseChildOrderNo()));
        consistencySendMqService.execSendMq(PurchaseChangeHandler.class, purchaseChildNoMqDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchReceive(PurchaseChildBatchReceiveDto dto) {
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(dto.getPurchaseChildOrderNoList());
        Assert.notEmpty(purchaseChildOrderPoList, () -> new BizException("找不到对应的采购子单,接单操作失败"));
        if (purchaseChildOrderPoList.size() != dto.getPurchaseChildOrderNoList().size()) {
            throw new ParamIllegalException("找不到对应的采购单，请刷新页面后重试！");
        }
        // 判断是否都为同一种类型（大货、加工）
        final PurchaseChildOrderPo anyPurchaseChildOrderPo = purchaseChildOrderPoList.get(0);

        // 判断采购子单是否同一个供应商
        final boolean allMatchSupplier = purchaseChildOrderPoList.stream().allMatch(purchaseChildOrderPo -> purchaseChildOrderPo.getSupplierCode().equals(anyPurchaseChildOrderPo.getSupplierCode()));
        if (!allMatchSupplier) {
            throw new ParamIllegalException("请选择相同的供应商的采购子单进行接单，不同供应商的采购子单不允许同时接单，请重试！");
        }

        final boolean allMatchPurchaseType = purchaseChildOrderPoList.stream().allMatch(purchaseChildOrderPo -> purchaseChildOrderPo.getPurchaseBizType().equals(anyPurchaseChildOrderPo.getPurchaseBizType()));
        if (!allMatchPurchaseType) {
            throw new ParamIllegalException("请选择相同的采购类型的采购子单进行接单，不同采购类型的采购子单不允许同时接单，请重试！");
        }

        final Map<String, String> purchaseChildNoPlatformMap = purchaseChildOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, PurchaseChildOrderPo::getPlatform));
        // 确认接单时才校验库存，对来源为其他供应商的原料进行库存校验
        if (BooleanType.TRUE.equals(dto.getIsReceived())) {
            final List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList = purchaseChildOrderRawDao.getListByChildNoList(dto.getPurchaseChildOrderNoList(),
                    PurchaseRawBizType.DEMAND, Collections.singletonList(RawSupplier.OTHER_SUPPLIER));
            purchaseChildOrderRawPoList.forEach(rawPo -> {
                final String platform = purchaseChildNoPlatformMap.get(rawPo.getPurchaseChildOrderNo());
                // 校验原料库存
                final SkuStockBo skuStockBo = new SkuStockBo();
                skuStockBo.setRawWarehouseCode(rawPo.getRawWarehouseCode());
                skuStockBo.setProductQuality(WmsEnum.ProductQuality.GOOD);
                skuStockBo.setDeliveryType(WmsEnum.DeliveryType.PURCHASE_RAW_MATERIAL);
                skuStockBo.setPurchaseChildOrderRawPoList(Collections.singletonList(rawPo));
                skuStockBo.setPlatform(platform);
                final BooleanType isStocked = purchaseBaseService.skuStockInventory(skuStockBo);
                if (BooleanType.FALSE.equals(isStocked)) {
                    throw new ParamIllegalException("采购子单:{}的原料:{}库存不足，请联系仓储人员对库存进行补充！",
                            rawPo.getPurchaseChildOrderNo(), rawPo.getSku());
                }
            });
        }

        // 兼容旧数据，若加工类型的采购单接单时不存在原料出库单，则需要校验库存
        if (BooleanType.TRUE.equals(dto.getIsReceived()) && PurchaseBizType.PROCESS.equals(anyPurchaseChildOrderPo.getPurchaseBizType())) {
            final List<String> purchaseChildOrderNoList = purchaseChildOrderPoList.stream()
                    .map(PurchaseChildOrderPo::getPurchaseChildOrderNo)
                    .distinct()
                    .collect(Collectors.toList());
            final List<PurchaseChildOrderRawDeliverPo> purchaseChildOrderRawDeliverPoList = purchaseChildOrderRawDeliverDao.getListByChildOrderNoList(purchaseChildOrderNoList);
            if (CollectionUtils.isEmpty(purchaseChildOrderRawDeliverPoList)) {
                final List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList = purchaseChildOrderRawDao.getListByChildNoList(dto.getPurchaseChildOrderNoList(),
                        PurchaseRawBizType.DEMAND, Collections.singletonList(RawSupplier.HETE));
                final Map<String, List<PurchaseChildOrderRawPo>> rawWarehouseCodePoListMap = purchaseChildOrderRawPoList.stream()
                        .collect(Collectors.groupingBy(rawPo -> rawPo.getRawWarehouseCode()
                                + "," + purchaseChildNoPlatformMap.getOrDefault(rawPo.getPurchaseChildOrderNo(), "")));
                rawWarehouseCodePoListMap.forEach((key, rawPoList) -> {
                    final String rawWarehouseCode = Arrays.stream(key.split(",")).findFirst().orElse("");
                    final String platform = Arrays.stream(key.split(",")).skip(1).findFirst().orElse("");

                    // 校验原料库存
                    final SkuStockBo skuStockBo = new SkuStockBo();
                    skuStockBo.setRawWarehouseCode(rawWarehouseCode);
                    skuStockBo.setProductQuality(WmsEnum.ProductQuality.GOOD);
                    skuStockBo.setDeliveryType(WmsEnum.DeliveryType.PURCHASE_RAW_MATERIAL);
                    skuStockBo.setPurchaseChildOrderRawPoList(rawPoList);
                    skuStockBo.setPlatform(platform);
                    final BooleanType isStocked = purchaseBaseService.skuStockInventory(skuStockBo);
                    if (BooleanType.FALSE.equals(isStocked)) {
                        throw new ParamIllegalException("采购子单的原料库存不足，请联系仓储人员对库存进行补充！");
                    }
                });
            }
        }

        final List<String> purchaseParentOrderNoList = purchaseChildOrderPoList.stream().map(PurchaseChildOrderPo::getPurchaseParentOrderNo).distinct().collect(Collectors.toList());

        final List<PurchaseParentOrderPo> purchaseParentOrderPoList = purchaseParentOrderDao.getByNoList(purchaseParentOrderNoList);
        Assert.notEmpty(purchaseParentOrderPoList, () -> new BizException("找不到对应的采购母单,接单操作失败"));
        final List<Long> purchaseParentIdList = purchaseParentOrderPoList.stream().map(PurchaseParentOrderPo::getPurchaseParentOrderId).collect(Collectors.toList());
        final List<PurchaseParentOrderChangePo> purchaseParentOrderChangePoList = purchaseParentOrderChangeDao.getListByParentIdList(purchaseParentIdList);
        Assert.notEmpty(purchaseParentOrderChangePoList, () -> new BizException("找不到对应的采购母单,接单操作失败"));
        final Map<String, PurchaseParentOrderChangePo> purchaseParentOrderNoChangePoMap = purchaseParentOrderChangePoList.stream().collect(Collectors.toMap(PurchaseParentOrderChangePo::getPurchaseParentOrderNo, Function.identity()));

        final List<String> purchaseChildOrderNoList = purchaseChildOrderPoList.stream().map(PurchaseChildOrderPo::getPurchaseChildOrderNo).collect(Collectors.toList());
        List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNoList(purchaseChildOrderNoList);
        Assert.notEmpty(purchaseChildOrderItemPoList, () -> new BizException("找不到对应的采购子单明细,接单失败"));

        final List<Long> purchaseChildOrderIdList = purchaseChildOrderPoList.stream().map(PurchaseChildOrderPo::getPurchaseChildOrderId).collect(Collectors.toList());
        final List<PurchaseChildOrderChangePo> purchaseChildOrderChangePoList = purchaseChildOrderChangeDao.getByChildOrderId(purchaseChildOrderIdList);
        Assert.notEmpty(purchaseChildOrderChangePoList, () -> new BizException("找不到对应的采购需求子单,接单失败"));
        final Map<String, PurchaseChildOrderChangePo> purchaseChildOrderNoChangePoMap = purchaseChildOrderChangePoList.stream().collect(Collectors.toMap(PurchaseChildOrderChangePo::getPurchaseChildOrderNo, Function.identity()));

        // 创建批次码
        final Map<String, PurchaseChildOrderPo> purchaseChildOrderNoPoMap = purchaseChildOrderPoList.stream().collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));
        final Map<String, List<String>> purchaseChildOrderNoSkuListMap = purchaseChildOrderItemPoList.stream().collect(Collectors.groupingBy(PurchaseChildOrderItemPo::getPurchaseChildOrderNo, Collectors.mapping(PurchaseChildOrderItemPo::getSku, Collectors.toList())));
        final Map<String, PurchaseChildOrderItemPo> purchaseChildOrderNoItemMap = purchaseChildOrderItemPoList.stream().collect(Collectors.toMap(PurchaseChildOrderItemPo::getPurchaseChildOrderNo, Function.identity(), (item1, item2) -> item2));
        purchaseChildOrderNoSkuListMap.forEach((key, value) -> {
            final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderNoPoMap.get(key);
            if (null == purchaseChildOrderPo) {
                throw new BizException("采购子单号:{}获取不到对应的数据，数据异常，请联系系统管理员！", key);
            }
            final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildOrderNoItemMap.get(key);
            if (null == purchaseChildOrderItemPo) {
                throw new BizException("采购子单号:{}获取不到对应的数据，数据异常，请联系系统管理员！", key);
            }

            Map<String, String> skuBatchMap = new HashMap<>();
            if (BooleanType.TRUE.equals(dto.getIsReceived())) {
                final SkuBatchCreateDto skuBatchCreateDto = new SkuBatchCreateDto();
                skuBatchCreateDto.setPurchaseChildOrderNo(key);
                skuBatchCreateDto.setSkuCodeList(value);
                skuBatchCreateDto.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
                skuBatchCreateDto.setSupplierName(purchaseChildOrderPo.getSupplierName());
                skuBatchMap = wmsRemoteService.batchCreateBatchCode(skuBatchCreateDto);
            }

            // 批量接单更新操作
            purchaseBaseService.batchReceiveUpdateCtrl(dto, purchaseChildOrderNoItemMap.get(key), skuBatchMap,
                    purchaseChildOrderPo, purchaseParentOrderNoChangePoMap.get(purchaseChildOrderPo.getPurchaseParentOrderNo()),
                    purchaseChildOrderNoChangePoMap.get(purchaseChildOrderPo.getPurchaseChildOrderNo()));
        });

        // 单据状态变更后推送单号给wms
        final PurchaseChildNoMqDto purchaseChildNoMqDto = new PurchaseChildNoMqDto();
        purchaseChildNoMqDto.setPurchaseChildOrderNoList(purchaseChildOrderNoList);
        consistencySendMqService.execSendMq(PurchaseChangeHandler.class, purchaseChildNoMqDto);

        // 确认接单-wms下发出库单至待分拣
        if (BooleanType.TRUE.equals(dto.getIsReceived())) {
            for (String purchaseChildOrderNo : purchaseChildOrderNoList) {
                final WmsDeliveryDispatchDto wmsDeliveryDispatchDto = new WmsDeliveryDispatchDto();
                wmsDeliveryDispatchDto.setRelatedOrderNo(purchaseChildOrderNo);
                wmsDeliveryDispatchDto.setDeliveryType(WmsEnum.DeliveryType.PURCHASE_RAW_MATERIAL);
                wmsDeliveryDispatchDto.setKey(purchaseChildOrderNo);
                wmsDeliveryDispatchDto.setOperator(GlobalContext.getUserKey());
                wmsDeliveryDispatchDto.setOperatorName(GlobalContext.getUsername());
                consistencySendMqService.execSendMq(WmsDeliverDispatchHandler.class, wmsDeliveryDispatchDto);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchSupplierCtrl(PurchaseSupplierCtrlDto dto) {
        final List<Long> idList = dto.getPurchaseChildOrderIdList();
        List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getByIdList(idList);
        Assert.notEmpty(purchaseChildOrderPoList, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        Assert.equals(idList.size(), purchaseChildOrderPoList.size(), () -> new BizException("找不到对应的采购子单,操作失败"));

        final List<String> purchaseParentOrderNoList = purchaseChildOrderPoList.stream().map(PurchaseChildOrderPo::getPurchaseParentOrderNo).distinct().collect(Collectors.toList());
        List<PurchaseParentOrderPo> purchaseParentOrderPoList = purchaseParentOrderDao.getByNoList(purchaseParentOrderNoList);
        Assert.notEmpty(purchaseParentOrderPoList, () -> new BizException("找不到对应的采购需求母单,操作失败"));
        Assert.equals(purchaseParentOrderNoList.size(), purchaseParentOrderPoList.size(), () -> new BizException("找不到对应的采购需求母单,操作失败"));

        // 查询采购子单明细
        boolean isCompleteQc = Objects.equals(PurchaseCtrlType.COMPLETE_QC, dto.getPurchaseCtrlType());
        List<PurchaseChildOrderItemPo> childOrderItemPos = Lists.newArrayList();
        if (isCompleteQc) {
            List<String> childOrderNos = purchaseChildOrderPoList.stream().map(PurchaseChildOrderPo::getPurchaseChildOrderNo).distinct().collect(Collectors.toList());
            childOrderItemPos = purchaseChildOrderItemDao.getListByChildNoList(childOrderNos);
        }
        List<PurchaseChildOrderItemPo> allChildOrderItemPos = childOrderItemPos;

        // 更新采购子单状态，记录操作日志
        purchaseChildOrderPoList.forEach(childOrderPo -> {
            final PurchaseOrderStatus purchaseOrderStatus = dto.getPurchaseCtrlType().getFunction().apply(childOrderPo.getPurchaseOrderStatus());
            childOrderPo.setPurchaseOrderStatus(purchaseOrderStatus);

            logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION, childOrderPo.getPurchaseChildOrderNo(), purchaseOrderStatus.getRemark(), Collections.emptyList());

            // 完成质检后置操作
            if (isCompleteQc) {
                purchaseBaseService.doCompleteQc(childOrderPo, allChildOrderItemPos);
            }
        });
        purchaseChildOrderDao.updateBatchByIdVersion(purchaseChildOrderPoList);

        // 单据状态变更后推送单号给wms
        final List<String> purchaseChildOrderNoList = purchaseChildOrderPoList.stream()
                .map(PurchaseChildOrderPo::getPurchaseChildOrderNo)
                .collect(Collectors.toList());
        final PurchaseChildNoMqDto purchaseChildNoMqDto = new PurchaseChildNoMqDto();
        purchaseChildNoMqDto.setPurchaseChildOrderNoList(purchaseChildOrderNoList);
        consistencySendMqService.execSendMq(PurchaseChangeHandler.class, purchaseChildNoMqDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void passOrBackProcess(PurchasePassDto dto) {
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getByIdVersion(dto.getPurchaseChildOrderId(), dto.getVersion());
        Assert.notNull(purchaseChildOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        final PurchaseParentOrderPo purchaseParentOrderPo = purchaseParentOrderDao.getOneByNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
        Assert.notNull(purchaseParentOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));

        final PurchaseOrderStatus targetStatus = dto.getTargetStatus();
        final PurchaseOrderStatus purchaseOrderStatus = purchaseChildOrderPo.getPurchaseOrderStatus();

        if (BooleanType.TRUE.equals(dto.getIsPass())) {
            if (targetStatus.getSort() < PurchaseOrderStatus.COMMISSION.getSort() || targetStatus.getSort() > PurchaseOrderStatus.POST_QC.getSort()) {
                throw new ParamIllegalException("目标状态【{}】,不在【{}】【{}】之间，跳转操作失败", targetStatus.getRemark(), PurchaseOrderStatus.COMMISSION.getRemark(), PurchaseOrderStatus.POST_QC.getRemark());
            }

            if (purchaseOrderStatus.getSort() < PurchaseOrderStatus.COMMISSION.getSort() || purchaseOrderStatus.getSort() > PurchaseOrderStatus.AFTER_TREATMENT.getSort()) {
                throw new ParamIllegalException("当前订单状态【{}】,不在【{}】【{}】之间，跳转操作失败", purchaseOrderStatus.getRemark(), PurchaseOrderStatus.COMMISSION.getRemark(), PurchaseOrderStatus.AFTER_TREATMENT.getRemark());
            }
        } else {
            if (targetStatus.getSort() < PurchaseOrderStatus.COMMISSION.getSort() || targetStatus.getSort() > PurchaseOrderStatus.POST_QC.getSort()) {
                throw new ParamIllegalException("目标状态【{}】,不在【{}】【{}】之间，回退操作失败", targetStatus.getRemark(), PurchaseOrderStatus.COMMISSION.getRemark(), PurchaseOrderStatus.POST_QC.getRemark());
            }

            if (purchaseOrderStatus.getSort() < PurchaseOrderStatus.WAIT_SCHEDULING.getSort() || purchaseOrderStatus.getSort() > PurchaseOrderStatus.POST_QC.getSort()) {
                throw new ParamIllegalException("当前订单状态【{}】,不在【{}】【{}】之间，回退操作失败", purchaseOrderStatus.getRemark(), PurchaseOrderStatus.WAIT_SCHEDULING.getRemark(), PurchaseOrderStatus.POST_QC.getRemark());
            }
        }

        purchaseChildOrderPo.setPurchaseOrderStatus(targetStatus);
        purchaseChildOrderDao.updateByIdVersion(purchaseChildOrderPo);

        logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION, purchaseChildOrderPo.getPurchaseChildOrderNo(), PASS_CTRL_NAME + targetStatus.getRemark(), Collections.emptyList());

        // 单据状态变更后推送单号给wms
        final PurchaseChildNoMqDto purchaseChildNoMqDto = new PurchaseChildNoMqDto();
        purchaseChildNoMqDto.setPurchaseChildOrderNoList(Collections.singletonList(purchaseChildOrderPo.getPurchaseChildOrderNo()));
        consistencySendMqService.execSendMq(PurchaseChangeHandler.class, purchaseChildNoMqDto);
    }

    public HeteCodeVo printHeteCode(PurchaseChildNoDto dto) {
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNo(dto.getPurchaseChildOrderNo());
        final List<String> skuList = purchaseChildOrderItemPoList.stream().map(PurchaseChildOrderItemPo::getSku).distinct().collect(Collectors.toList());
        final SampleSkuListDto sampleSkuListDto = new SampleSkuListDto();
        sampleSkuListDto.setSkuList(skuList);
        final List<SampleInfoVo> sampleInfoBySkuList = sampleBaseService.getSampleInfoBySkuList(sampleSkuListDto);
        final Map<String, List<SampleChildOrderInfoVo>> skuInfoMap = sampleInfoBySkuList.stream().collect(Collectors.toMap(SampleInfoVo::getSku, SampleInfoVo::getSampleChildOrderInfoList));

        final List<HeteCodeVo.HeteCodeItem> heteCodeItemList = purchaseChildOrderItemPoList.stream().map(po -> {
            final HeteCodeVo.HeteCodeItem heteCodeItem = new HeteCodeVo.HeteCodeItem();
            heteCodeItem.setSku(po.getSku());
            heteCodeItem.setPurchaseCnt(po.getPurchaseCnt());
            heteCodeItem.setSkuBatchCode(po.getSkuBatchCode());
            heteCodeItem.setSampleChildOrderInfoList(skuInfoMap.get(po.getSku()));

            return heteCodeItem;
        }).collect(Collectors.toList());

        final HeteCodeVo heteCodeVo = new HeteCodeVo();
        heteCodeVo.setPurchaseChildOrderNo(dto.getPurchaseChildOrderNo());
        heteCodeVo.setHeteCodeItemList(heteCodeItemList);
        heteCodeVo.setSupplierCode(GlobalContext.getSupplierCode());
        heteCodeVo.setSupplierName(GlobalContext.getSupplierName());


        return heteCodeVo;
    }

    public List<String> getSkuListByPurchaseParentNo(PurchaseParentNoDto dto) {
        final List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList = purchaseParentOrderItemDao.getListByParentNo(dto.getPurchaseParentOrderNo());
        return purchaseParentOrderItemPoList.stream().map(PurchaseParentOrderItemPo::getSku).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelPurchase(PurchaseParentNoListDto dto) {
        final List<String> purchaseParentOrderNoList = dto.getPurchaseParentOrderNoList();
        final List<PurchaseParentOrderPo> purchaseParentOrderPoList = purchaseParentOrderDao.getListByNoList(purchaseParentOrderNoList);
        Assert.notEmpty(purchaseParentOrderPoList, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        final List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList = purchaseParentOrderItemDao.getListByParentNoList(purchaseParentOrderNoList);
        Assert.notEmpty(purchaseParentOrderItemPoList, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));

        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByParentNoAndNeStatus(purchaseParentOrderNoList, PurchaseOrderStatus.DELETE);

        if (CollectionUtils.isNotEmpty(purchaseChildOrderPoList)) {
            final List<String> existPurchaseParentOrderNoList = purchaseChildOrderPoList.stream().map(PurchaseChildOrderPo::getPurchaseParentOrderNo).distinct().collect(Collectors.toList());
            throw new ParamIllegalException("当前采购需求单：{}存在未作废的采购订单，不允许作废该需求单！", existPurchaseParentOrderNoList);
        }

        purchaseParentOrderPoList.forEach(purchaseParentOrderPo -> {
            if (!PurchaseParentOrderStatus.cancelStatusList().contains(purchaseParentOrderPo.getPurchaseParentOrderStatus())) {
                throw new ParamIllegalException("采购需求单：{}不处于可取消状态，请刷新后重试！", purchaseParentOrderPo.getPurchaseParentOrderNo());
            }
            purchaseParentOrderPo.setCanSplitCnt(0);
            purchaseParentOrderPo.setPurchaseParentOrderStatus(PurchaseParentOrderStatus.DELETED);
        });
        purchaseParentOrderDao.updateBatchByIdVersion(purchaseParentOrderPoList);

        purchaseParentOrderItemPoList.forEach(itemPo -> itemPo.setCanSplitCnt(0));
        purchaseParentOrderItemDao.updateBatchByIdVersion(purchaseParentOrderItemPoList);

        // 同步取消子单
        purchaseChildOrderDao.removeByParentOrderNo(purchaseParentOrderNoList);
        purchaseParentOrderNoList.forEach(purchaseParentOrderNo -> {
            logBaseService.simpleLog(LogBizModule.PURCHASE_PARENT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION, purchaseParentOrderNo, PurchaseParentOrderStatus.DELETED.getRemark(), Collections.emptyList());
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void importParentData(@Valid @NotNull PurchaseParentImportationDto req) {

        try {
            final LocalDateTime localDateTime = ScmTimeUtil.dateStrToLocalDateTime(req.getExpectedOnShelvesDateStr(), DatePattern.NORM_DATETIME_PATTERN);
            req.setExpectedOnShelvesDate(ScmTimeUtil.getLastSecondTimeOfDayForTime(localDateTime));
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            throw new ParamIllegalException("时间格式错误，请重新校验后再导入");
        }

        final String platformCode = sdaRemoteService.getCodeByName(req.getPlatform());
        if (StringUtils.isBlank(platformCode)) {
            throw new ParamIllegalException("平台参数错误");
        }
        if (req.getPurchaseCnt() <= 0) {
            throw new ParamIllegalException("采购数量参数错误");
        }
        if (req.getExpectedOnShelvesDate().isBefore(LocalDate.now().atStartOfDay())) {
            throw new ParamIllegalException("期望上架时间不能小于当前时间");
        }
        // 网红类型判断是否网红仓
        final PurchaseDemandType purchaseDemandType = PurchaseDemandType.getByRemark(req.getPurchaseDemandType());
        if (null == purchaseDemandType) {
            throw new ParamIllegalException("采购需求类型填写错误，请填写网红/常规");
        }
        if (PurchaseDemandType.WH.equals(purchaseDemandType) && !ScmWarehouseUtil.WH_WAREHOUSE_CODE.equals(req.getWarehouseCode())) {
            throw new ParamIllegalException("采购需求类型为网红时，只能选择网红仓创建，请重新选择仓库后再提交！");
        }
        if (PurchaseDemandType.NORMAL.equals(purchaseDemandType) && ScmWarehouseUtil.WH_WAREHOUSE_CODE.equals(req.getWarehouseCode())) {
            throw new ParamIllegalException("采购需求类型为常规时，不能选择网红仓创建，请重新选择仓库后再提交！");
        }
        final SkuType skuType = SkuType.getByRemark(req.getSkuType());
        if (null == skuType) {
            throw new ParamIllegalException("sku类型参数错误");
        }
        // 获取sku是否需要管理
        final ProduceDataPo produceDataPo = produceDataDao.getBySku(req.getSku());
        if (null == produceDataPo || null == produceDataPo.getRawManage()) {
            throw new ParamIllegalException("sku:{}没有配置原料是否需要管理", req.getSku());
        }

        // sku类型为商品sku时，校验sku是否维护了bom数据
        if (SkuType.SKU.equals(skuType) && BooleanType.TRUE.equals(produceDataPo.getRawManage())) {
            final SampleSkuListDto sampleSkuListDto = new SampleSkuListDto();
            sampleSkuListDto.setSkuList(Collections.singletonList(req.getSku()));
            final List<SkuBomListVo> skuBomListVoList = produceDataBaseService.getBomListBySkuList(sampleSkuListDto);
            if (CollectionUtils.isEmpty(skuBomListVoList)) {
                throw new ParamIllegalException("sku:{}没有维护bom信息，请维护后重新导入！", req.getSku());
            }
        } else if (SkuType.SM_SKU.equals(skuType)) {
            if (StringUtils.isBlank(req.getOrderRemarks())) {
                throw new ParamIllegalException("辅料类型的采购需求单必须填写备注，请填写后重新导入！");
            }
        }

        final List<PlmGoodsSkuVo> plmSkuInfoVoList = plmRemoteService.getSkuDetailListBySkuList(Collections.singletonList(req.getSku()));
        if (CollectionUtils.isEmpty(plmSkuInfoVoList)) {
            throw new ParamIllegalException("sku：{}不存在", req.getSku());
        }

        final PlmGoodsSkuVo plmGoodsSkuVo = plmSkuInfoVoList.get(0);
        final List<PlmGoodsPlatVo> goodsPlatVoList = plmGoodsSkuVo.getGoodsPlatVoList();
        if (CollectionUtils.isEmpty(goodsPlatVoList)) {
            throw new ParamIllegalException("sku：{}的不存在", req.getSku());
        }

        final List<WarehouseVo> warehouseVoList = wmsRemoteService.getWarehouseByCode(Collections.singletonList(req.getWarehouseCode()));
        if (CollectionUtils.isEmpty(warehouseVoList) || warehouseVoList.size() > 1) {
            throw new ParamIllegalException("仓库：{}不存在", req.getWarehouseCode());
        }

        final WarehouseVo warehouseVo = warehouseVoList.get(0);

        // 时间先转时区
        final LocalDateTime cnLocalDateTime = TimeUtil.convertZone(req.getExpectedOnShelvesDate(), TimeZoneId.CN, TimeZoneId.UTC);
        req.setExpectedOnShelvesDate(cnLocalDateTime);

        PurchaseParentOrderPo purchaseParentOrderPo = new PurchaseParentOrderPo();
        final String purchaseParentOrderNo = idGenerateService.getConfuseCode(ScmConstant.PURCHASE_PARENT_NO_PREFIX, TimeType.CN_DAY_YYYY, ConfuseLength.L_4);
        purchaseParentOrderPo.setPurchaseParentOrderNo(purchaseParentOrderNo);
        purchaseParentOrderPo.setSkuType(skuType);
        purchaseParentOrderPo.setWarehouseCode(req.getWarehouseCode());
        purchaseParentOrderPo.setWarehouseName(warehouseVo.getWarehouseName());
        purchaseParentOrderPo.setWarehouseTypes(warehouseVo.getWarehouseTypeName());
        purchaseParentOrderPo.setPlatform(platformCode);
        purchaseParentOrderPo.setOrderRemarks(req.getOrderRemarks());
        purchaseParentOrderPo.setPurchaseTotal(req.getPurchaseCnt());
        purchaseParentOrderPo.setPurchaseParentOrderStatus(PurchaseParentOrderStatus.IN_PROGRESS);
        purchaseParentOrderPo.setSpu(plmRemoteService.getSpuBySku(req.getSku()));
        purchaseParentOrderPo.setUndeliveredCnt(req.getPurchaseCnt());
        purchaseParentOrderPo.setIsDirectSend(BooleanType.FALSE);
        purchaseParentOrderPo.setIsImportation(BooleanType.TRUE);
        purchaseParentOrderPo.setPurchaseDemandType(purchaseDemandType);
        purchaseParentOrderPo.setPlaceOrderUser(GlobalContext.getUserKey());
        purchaseParentOrderPo.setPlaceOrderUsername(GlobalContext.getUsername());
        purchaseParentOrderDao.insert(purchaseParentOrderPo);

        PurchaseParentOrderChangePo purchaseParentOrderChangePo = PurchaseConverter.parentPoToChangePo(purchaseParentOrderPo);
        purchaseParentOrderChangeDao.insert(purchaseParentOrderChangePo);

        final PurchaseParentOrderItemPo purchaseParentOrderItemPo = new PurchaseParentOrderItemPo();
        purchaseParentOrderItemPo.setPurchaseParentOrderNo(purchaseParentOrderNo);
        purchaseParentOrderItemPo.setSku(req.getSku());
        purchaseParentOrderItemPo.setPurchaseCnt(req.getPurchaseCnt());
        purchaseParentOrderItemPo.setUndeliveredCnt(req.getPurchaseCnt());
        purchaseParentOrderItemDao.insert(purchaseParentOrderItemPo);

        // 获取sku已经生成的采购子单
        final Map<String, List<PurchaseChildOrderItemPo>> skuChildItemMap = purchaseBaseService.getChildItemMapBySkuList(Collections.singletonList(req.getSku()));

        final String purchaseChildOrderNo = purchaseBaseService.getLatestPurchaseChildNo(purchaseParentOrderNo);
        PurchaseChildOrderPo purchaseChildOrderPo = new PurchaseChildOrderPo();
        purchaseChildOrderPo.setPromiseDateChg(BooleanType.FALSE);
        purchaseChildOrderPo.setPurchaseParentOrderNo(purchaseParentOrderPo.getPurchaseParentOrderNo());
        purchaseChildOrderPo.setPurchaseChildOrderNo(purchaseChildOrderNo);
        purchaseChildOrderPo.setPurchaseOrderStatus(PurchaseOrderStatus.WAIT_CONFIRM);
        if (PurchaseDemandType.WH.getRemark().equals(req.getPurchaseDemandType())) {
            purchaseChildOrderPo.setPurchaseOrderType(PurchaseOrderType.WH);
        } else if (PurchaseDemandType.SPECIAL.getRemark().equals(req.getPurchaseDemandType())) {
            purchaseChildOrderPo.setPurchaseOrderType(skuChildItemMap.get(req.getSku()) == null ? PurchaseOrderType.FIRST_ORDER : PurchaseOrderType.SPECIAL);
        } else {
            purchaseChildOrderPo.setPurchaseOrderType(skuChildItemMap.get(req.getSku()) == null ? PurchaseOrderType.FIRST_ORDER : PurchaseOrderType.NORMAL);
        }
        purchaseChildOrderPo.setSpu(purchaseParentOrderPo.getSpu());
        purchaseChildOrderPo.setPlatform(platformCode);
        purchaseChildOrderPo.setPurchaseBizType(PurchaseBizType.NO_TYPE);
        purchaseChildOrderPo.setOrderRemarks(req.getOrderRemarks());
        purchaseChildOrderPo.setExpectedOnShelvesDate(ScmTimeUtil.getLastSecondTimeOfDayForTime(req.getExpectedOnShelvesDate()));
        purchaseChildOrderPo.setIsUploadOverseasMsg(BooleanType.FALSE);
        purchaseChildOrderPo.setSkuCnt(1);
        purchaseChildOrderPo.setPurchaseTotal(req.getPurchaseCnt());
        purchaseChildOrderPo.setIsImportation(BooleanType.TRUE);
        purchaseChildOrderPo.setRawRemainTab(BooleanType.TRUE);
        purchaseChildOrderPo.setIsOverdue(BooleanType.FALSE);
        purchaseChildOrderPo.setShippableCnt(req.getPurchaseCnt());
        purchaseChildOrderPo.setWarehouseCode(req.getWarehouseCode());
        purchaseChildOrderPo.setWarehouseName(warehouseVo.getWarehouseName());
        purchaseChildOrderPo.setWarehouseTypes(warehouseVo.getWarehouseTypeName());
        purchaseChildOrderPo.setSkuType(skuType);
        purchaseChildOrderPo.setSplitType(SplitType.GOODS_SPLIT);
        purchaseChildOrderPo.setPromiseDate(ScmTimeUtil.getLastSecondTimeOfDayForTime(req.getExpectedOnShelvesDate()));
        purchaseChildOrderPo.setPlaceOrderUser(GlobalContext.getUserKey());
        purchaseChildOrderPo.setPlaceOrderUsername(GlobalContext.getUsername());
        purchaseChildOrderPo.setOrderSource(OrderSource.SCM);
        purchaseChildOrderDao.insert(purchaseChildOrderPo);

        // 创建单据后推送单号给wms
        final PurchaseChildNoMqDto purchaseChildNoMqDto = new PurchaseChildNoMqDto();
        purchaseChildNoMqDto.setPurchaseChildOrderNoList(Collections.singletonList(purchaseChildOrderNo));
        consistencySendMqService.execSendMq(PurchaseChangeHandler.class, purchaseChildNoMqDto);


        final PurchaseChildOrderItemPo purchaseChildOrderItemPo = new PurchaseChildOrderItemPo();
        purchaseChildOrderItemPo.setPurchaseChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        purchaseChildOrderItemPo.setPurchaseParentOrderNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
        purchaseChildOrderItemPo.setSku(req.getSku());
        purchaseChildOrderItemPo.setPurchaseCnt(req.getPurchaseCnt());
        purchaseChildOrderItemPo.setInitPurchaseCnt(req.getPurchaseCnt());
        purchaseChildOrderItemPo.setUndeliveredCnt(req.getPurchaseCnt());
        purchaseChildOrderItemDao.insert(purchaseChildOrderItemPo);

        final PurchaseChildOrderChangePo purchaseChildOrderChangePo = new PurchaseChildOrderChangePo();
        purchaseChildOrderChangePo.setPurchaseChildOrderId(purchaseChildOrderPo.getPurchaseChildOrderId());
        purchaseChildOrderChangePo.setPurchaseChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        purchaseChildOrderChangePo.setPlaceOrderTime(new DateTime().toLocalDateTime());
        purchaseChildOrderChangeDao.insert(purchaseChildOrderChangePo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchCancel(PurchaseBatchCancelDto dto) {
        dto.getPurchaseCancelList().forEach(purchaseBaseService::cancel);
    }

    @Transactional(rollbackFor = Exception.class)
    public void editWarehouse(PurchaseEditWarehouseDto dto) {
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getByIdVersion(dto.getPurchaseChildOrderId(), dto.getVersion());
        Assert.notNull(purchaseChildOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        final PurchaseOrderStatus purchaseOrderStatus = purchaseChildOrderPo.getPurchaseOrderStatus();
        if (purchaseOrderStatus.getSort() < PurchaseOrderStatus.WAIT_SCHEDULING.getSort() || purchaseOrderStatus.getSort() > PurchaseOrderStatus.WAIT_DELIVER.getSort()) {
            throw new ParamIllegalException("当前采购单状态不处于【{}】到【{}】状态之间，修改收货仓库失败", PurchaseOrderStatus.WAIT_SCHEDULING.getRemark(), PurchaseOrderStatus.WAIT_DELIVER.getRemark());
        }

        if (PurchaseOrderStatus.WAIT_DELIVER.equals(purchaseOrderStatus)) {
            final List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
            if (CollectionUtils.isNotEmpty(purchaseDeliverOrderPoList)) {
                throw new ParamIllegalException("采购单已经存在发货单，不允许修改收货仓，请先作废发货单后再修改");
            }
        }
        // 网红类型判断是否选择网红仓
        if (PurchaseOrderType.WH.equals(purchaseChildOrderPo.getPurchaseOrderType()) && !ScmWarehouseUtil.WH_WAREHOUSE_CODE.equals(dto.getWarehouseCode())) {
            throw new ParamIllegalException("采购需求类型为网红时，只能选择网红仓创建，请重新选择仓库后再提交！");
        }

        purchaseChildOrderPo.setWarehouseCode(dto.getWarehouseCode());
        purchaseChildOrderPo.setWarehouseName(dto.getWarehouseName());
        purchaseChildOrderPo.setWarehouseTypes(String.join(",", Optional.ofNullable(dto.getWarehouseTypeList()).orElse(new ArrayList<>())));
        purchaseChildOrderPo.setIsDirectSend(WarehouseBaseService.getIsDirectSendByWarehouseType(dto.getWarehouseTypeList()));

        purchaseChildOrderDao.updateByIdVersion(purchaseChildOrderPo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void uploadOverseasFile(OverseasWarehouseMsgDto dto) {
        OverseasWarehouseMsgPo overseasWarehouseMsgPo = overseasWarehouseMsgDao.getByNo(dto.getOverseasShippingMarkNo());
        if (null != overseasWarehouseMsgPo) {
            throw new ParamIllegalException("当前箱唛号:{}已经存在，请修改箱唛号后重新上传!", dto.getOverseasShippingMarkNo());
        }

        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(dto.getPurchaseChildOrderNo());
        Assert.notNull(purchaseChildOrderPo, () -> new ParamIllegalException("查找不到对应的采购子单，请刷新后重试！"));
        if (!PurchaseOrderStatus.WAIT_DELIVER.equals(purchaseChildOrderPo.getPurchaseOrderStatus())) {
            throw new ParamIllegalException("当前采购子单不处于【{}】状态，无法进行上传海外仓文件操作，请刷新后重试！", PurchaseOrderStatus.WAIT_DELIVER.getRemark());
        }
        if (BooleanType.FALSE.equals(purchaseChildOrderPo.getIsDirectSend())) {
            throw new ParamIllegalException("当前采购子单不是海外直发类型，不需要上传海外仓文件!");
        }

        overseasWarehouseMsgPo = overseasWarehouseMsgDao.getByPurchaseChildNo(dto.getPurchaseChildOrderNo());
        if (null != overseasWarehouseMsgPo) {
            throw new ParamIllegalException("当前采购子单:{}已经上传海外文件,不允许重复上传海外仓文件!", dto.getPurchaseChildOrderNo());
        }

        // 更新上传海外仓文件状态
        purchaseChildOrderPo.setIsUploadOverseasMsg(BooleanType.TRUE);
        purchaseChildOrderDao.updateByIdVersion(purchaseChildOrderPo);

        final OverseasWarehouseMsgPo overseasWarehouseMsgPo1 = new OverseasWarehouseMsgPo();
        overseasWarehouseMsgPo1.setOverseasShippingMarkNo(dto.getOverseasShippingMarkNo());
        overseasWarehouseMsgPo1.setTrackingNo(dto.getTrackingNo());
        overseasWarehouseMsgPo1.setPurchaseChildOrderNo(dto.getPurchaseChildOrderNo());
        overseasWarehouseMsgDao.insert(overseasWarehouseMsgPo1);

        final Long overseasWarehouseMsgId = overseasWarehouseMsgPo1.getOverseasWarehouseMsgId();
        this.createOverseasWarehouseItem(dto.getOverseasWarehouseMsgItemList(), overseasWarehouseMsgPo1);


        scmImageBaseService.insertBatchImage(dto.getOverseasShippingFileCode(), ImageBizType.OVERSEAS_SHIPPING_MARK, overseasWarehouseMsgId);
        scmImageBaseService.insertBatchImage(dto.getOverseasBarCodeFileCode(), ImageBizType.OVERSEAS_BAR_CODE, overseasWarehouseMsgId);
        scmImageBaseService.insertBatchImage(dto.getTrackingNoFileCode(), ImageBizType.OVERSEAS_TRACKING, overseasWarehouseMsgId);
    }

    private void createOverseasWarehouseItem(List<OverseasWarehouseMsgItemDto> overseasWarehouseMsgItemList, OverseasWarehouseMsgPo overseasWarehouseMsgPo) {
        final List<OverseasWarehouseMsgItemPo> overseasWarehouseMsgItemPoList = overseasWarehouseMsgItemList.stream().map(item -> {
            final OverseasWarehouseMsgItemPo overseasWarehouseMsgItemPo = new OverseasWarehouseMsgItemPo();
            overseasWarehouseMsgItemPo.setOverseasWarehouseMsgId(overseasWarehouseMsgPo.getOverseasWarehouseMsgId());
            overseasWarehouseMsgItemPo.setOverseasShippingMarkNo(overseasWarehouseMsgPo.getOverseasShippingMarkNo());
            overseasWarehouseMsgItemPo.setPurchaseChildOrderNo(overseasWarehouseMsgPo.getPurchaseChildOrderNo());
            overseasWarehouseMsgItemPo.setSku(item.getSku());
            overseasWarehouseMsgItemPo.setSkuBatchCode(item.getSkuBatchCode());
            overseasWarehouseMsgItemPo.setOverseasWarehouseBarCode(item.getOverseasWarehouseBarCode());

            return overseasWarehouseMsgItemPo;
        }).collect(Collectors.toList());
        overseasWarehouseMsgItemDao.insertBatch(overseasWarehouseMsgItemPoList);

    }

    public List<String> printOverseasBarcode(PurchaseChildNoDto dto) {
        final OverseasWarehouseMsgPo overseasWarehouseMsgPo = overseasWarehouseMsgDao.getByPurchaseChildNo(dto.getPurchaseChildOrderNo());
        Assert.notNull(overseasWarehouseMsgPo, () -> new ParamIllegalException("找不到对应的海外仓文件信息，请联系采购员上传！"));

        return scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.OVERSEAS_BAR_CODE, Collections.singletonList(overseasWarehouseMsgPo.getOverseasWarehouseMsgId()));
    }

    public List<SkuBatchCodeVo> printBatchCode(PurchaseChildNoListDto dto) {
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(dto.getPurchaseChildOrderNoList());
        Assert.notEmpty(purchaseChildOrderPoList, () -> new BizException("获取不到采购子单，打印批次码失败，请联系系统管理员！"));
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNoList(dto.getPurchaseChildOrderNoList());
        Assert.notEmpty(purchaseChildOrderItemPoList, () -> new BizException("获取不到采购子单，打印批次码失败，请联系系统管理员！"));
        final Map<String, PurchaseChildOrderPo> purchaseChildOrderPoNoMap = purchaseChildOrderPoList.stream().collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));
        Map<String, List<SampleChildOrderInfoPo>> purchaseChildNoMap = sampleBaseService.getSampleInfoByPurchaseChildNoList(dto.getPurchaseChildOrderNoList());
        final List<String> skuList = purchaseChildOrderItemPoList.stream().map(PurchaseChildOrderItemPo::getSku).collect(Collectors.toList());

        final List<PlmVariantVo> variantAttrList = plmRemoteService.getVariantAttr(skuList);
        final Map<String, List<PlmAttrSkuVo>> skuVariantAttrMap = variantAttrList.stream()
                .collect(Collectors.toMap(PlmVariantVo::getSkuCode, PlmVariantVo::getVariantSkuList));
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        return purchaseChildOrderItemPoList.stream()
                .map(po -> {
                    final SkuBatchCodeVo skuBatchCodeVo = new SkuBatchCodeVo();
                    skuBatchCodeVo.setSku(po.getSku());
                    skuBatchCodeVo.setSkuBatchCode(po.getSkuBatchCode());
                    final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderPoNoMap.get(po.getPurchaseChildOrderNo());
                    skuBatchCodeVo.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
                    final List<SampleChildOrderInfoPo> sampleChildOrderInfoPoList = purchaseChildNoMap.get(po.getPurchaseChildOrderNo());
                    skuBatchCodeVo.setSampleParentOrderInfoPoList(sampleChildOrderInfoPoList);
                    final List<PlmAttrSkuVo> variantSkuList = skuVariantAttrMap.get(po.getSku());
                    skuBatchCodeVo.setVariantSkuList(variantSkuList);
                    skuBatchCodeVo.setPurchaseCnt(po.getPurchaseCnt());
                    skuBatchCodeVo.setSkuEncode(skuEncodeMap.get(po.getSku()));

                    return skuBatchCodeVo;
                }).collect(Collectors.toList());
    }


    public List<PurchaseEndStatusVo> isPurchaseEndStatus(PurchaseChildNoListDto dto) {
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(dto.getPurchaseChildOrderNoList());
        Assert.notEmpty(purchaseChildOrderPoList, () -> new ParamIllegalException("查找不到对应的采购子单，请刷新后重试"));
        final List<String> purchaseParentOrderNoList = purchaseChildOrderPoList.stream().map(PurchaseChildOrderPo::getPurchaseParentOrderNo).distinct().collect(Collectors.toList());
        final List<PurchaseParentOrderPo> purchaseParentOrderPoList = purchaseParentOrderDao.getListByNoList(purchaseParentOrderNoList);
        Assert.notEmpty(purchaseParentOrderPoList, () -> new ParamIllegalException("查找不到对应的采购母单，请刷新后重试"));
        final Map<String, PurchaseParentOrderPo> purchaseParentOrderNoPoMap = purchaseParentOrderPoList.stream().collect(Collectors.toMap(PurchaseParentOrderPo::getPurchaseParentOrderNo, Function.identity()));


        final List<PurchaseChildOrderPo> allChildOrderPoList = purchaseChildOrderDao.getListByParentNoList(purchaseParentOrderNoList);
        final Map<String, List<PurchaseChildOrderPo>> parentOrderNoChildPoListMap = allChildOrderPoList.stream().collect(Collectors.groupingBy(PurchaseChildOrderPo::getPurchaseParentOrderNo));


        List<PurchaseEndStatusVo> purchaseEndStatusList = new ArrayList<>();
        purchaseChildOrderPoList.forEach(purchaseChildOrderPo -> {
            final List<PurchaseChildOrderPo> childOrderPoList = parentOrderNoChildPoListMap.get(purchaseChildOrderPo.getPurchaseParentOrderNo());

            final List<PurchaseChildOrderPo> remainChildOrderPoList = childOrderPoList.stream().filter(po -> !po.getPurchaseChildOrderNo().equals(purchaseChildOrderPo.getPurchaseChildOrderNo())).filter(po -> !PurchaseOrderStatus.DELETE.equals(po.getPurchaseOrderStatus())).collect(Collectors.toList());

            if (CollectionUtils.isEmpty(remainChildOrderPoList)) {
                final PurchaseEndStatusVo purchaseEndStatusVo = new PurchaseEndStatusVo();
                purchaseEndStatusVo.setPurchaseChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
                purchaseEndStatusVo.setPurchaseParentOrderPo(purchaseParentOrderNoPoMap.get(purchaseChildOrderPo.getPurchaseParentOrderNo()));
            }
        });

        return purchaseEndStatusList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void supplyRaw(PurchaseSupplyRawDto dto) {
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(dto.getPurchaseChildOrderNo());
        Assert.notNull(purchaseChildOrderPo, () -> new BizException("查找不到对应的采购子单，补充原料失败"));
        if (!PurchaseBizType.PROCESS.equals(purchaseChildOrderPo.getPurchaseBizType())) {
            throw new BizException("只有【{}】类型的采购单才可以补充原料", PurchaseBizType.PROCESS.getRemark());
        }
        // 状态校验
        final PurchaseOrderStatus purchaseOrderStatus = purchaseChildOrderPo.getPurchaseOrderStatus();
        if (purchaseOrderStatus.getSort() < PurchaseOrderStatus.WAIT_SCHEDULING.getSort() || purchaseOrderStatus.getSort() > PurchaseOrderStatus.AFTER_TREATMENT.getSort()) {
            throw new BizException("当前采购单状态为【{}】,不处于【{}】和【{}】之间,无法进行补充原料操作", purchaseOrderStatus.getRemark(), PurchaseOrderStatus.WAIT_SCHEDULING.getRemark(), PurchaseOrderStatus.AFTER_TREATMENT.getRemark());
        }

        final List<WarehouseVo> warehouseVoList = wmsRemoteService.getWarehouseByCode(Collections.singletonList(dto.getRawWarehouseCode()));
        if (CollectionUtils.isEmpty(warehouseVoList)) {
            throw new BizException("仓库：{}不存在，请联系系统管理员", dto.getRawWarehouseCode());
        }

        // wms 生成原料发货单
        final List<RawDeliverBo> purchaseRawDeliverBoList = dto.getRawProductItemList().stream()
                .filter(item -> RawSupplier.HETE.equals(item.getRawSupplier())
                        || RawSupplier.OTHER_SUPPLIER.equals(item.getRawSupplier()))
                .map(item -> {
                    final RawDeliverBo purchaseRawDeliverBo = new RawDeliverBo();
                    purchaseRawDeliverBo.setDeliveryCnt(item.getDeliveryCnt());
                    purchaseRawDeliverBo.setSku(item.getSku());
                    purchaseRawDeliverBo.setParticularLocation(BooleanType.FALSE);
                    if (CollectionUtils.isNotEmpty(item.getRawLocationItemList())) {
                        final List<RawDeliverBo.WareLocationDelivery> wareLocationDeliveryList = item.getRawLocationItemList().stream().map(rawLocationItemDto -> {
                            final RawDeliverBo.WareLocationDelivery wareLocationDelivery = new RawDeliverBo.WareLocationDelivery();
                            wareLocationDelivery.setWarehouseLocationCode(rawLocationItemDto.getWarehouseLocationCode());
                            wareLocationDelivery.setDeliveryAmount(rawLocationItemDto.getDeliveryAmount());
                            wareLocationDelivery.setBatchCode(rawLocationItemDto.getBatchCode());
                            return wareLocationDelivery;
                        }).collect(Collectors.toList());

                        purchaseRawDeliverBo.setWareLocationDeliveryList(wareLocationDeliveryList);
                        purchaseRawDeliverBo.setParticularLocation(BooleanType.TRUE);
                    }
                    return purchaseRawDeliverBo;
                }).collect(Collectors.toList());
        final WmsDeliverBo wmsDeliverBo = new WmsDeliverBo();
        wmsDeliverBo.setRelatedOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        wmsDeliverBo.setRawWarehouseCode(dto.getRawWarehouseCode());
        wmsDeliverBo.setDeliveryType(WmsEnum.DeliveryType.PURCHASE_RAW_MATERIAL);
        wmsDeliverBo.setRawDeliverMode(RawDeliverMode.SUPPLY_RAW);
        wmsDeliverBo.setDispatchNow(BooleanType.TRUE);
        wmsDeliverBo.setPlatform(purchaseChildOrderPo.getPlatform());
        purchaseRawRefService.wmsRawDeliver(wmsDeliverBo, purchaseRawDeliverBoList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchFinishPurchase(PurchaseParentNoListDto dto) {
        final List<PurchaseParentOrderPo> purchaseParentOrderPoList = purchaseParentOrderDao.getByNoList(dto.getPurchaseParentOrderNoList());
        if (CollectionUtils.isEmpty(purchaseParentOrderPoList) || purchaseParentOrderPoList.size() != dto.getPurchaseParentOrderNoList().size()) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        final List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList = purchaseParentOrderItemDao.getListByParentNoList(dto.getPurchaseParentOrderNoList());
        if (CollectionUtils.isEmpty(purchaseParentOrderItemPoList)) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }

        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByParentNoList(dto.getPurchaseParentOrderNoList());
        if (CollectionUtils.isEmpty(purchaseChildOrderPoList)) {
            throw new ParamIllegalException("未拆分采购订单，无法完成该采购需求单");
        }

        final List<PurchaseOrderStatus> canFinishStatus = PurchaseOrderStatus.getCanFinishStatus();
        purchaseChildOrderPoList.forEach(po -> {
            if (!canFinishStatus.contains(po.getPurchaseOrderStatus())) {
                throw new ParamIllegalException("采购需求单:{}存在未完结的采购订单:{}，请先确保所有订单都已经完结，再操作完结该需求单！", po.getPurchaseParentOrderNo(), po.getPurchaseChildOrderNo());
            }
        });

        purchaseParentOrderPoList.forEach(po -> {
            po.setPurchaseParentOrderStatus(po.getPurchaseParentOrderStatus().toCompleted());
            po.setCanSplitCnt(0);
            logBaseService.simpleLog(LogBizModule.PURCHASE_PARENT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION, po.getPurchaseParentOrderNo(), PurchaseParentOrderStatus.COMPLETED.getRemark(), Collections.emptyList());
        });
        purchaseParentOrderItemPoList.forEach(itemPo -> itemPo.setCanSplitCnt(0));
        //更新可发货数
        purchaseBaseService.updatePurchaseShippableCnt(purchaseChildOrderPoList);
        purchaseParentOrderDao.updateBatchByIdVersion(purchaseParentOrderPoList);
        purchaseParentOrderItemDao.updateBatchByIdVersion(purchaseParentOrderItemPoList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void finishPurchase(PurchaseParentIdAndVersionDto dto) {
        final PurchaseParentOrderPo purchaseParentOrderPo = purchaseParentOrderDao.getOneByIdAndVersion(dto.getPurchaseParentOrderId(), dto.getVersion());
        Assert.notNull(purchaseParentOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));

        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByParentNo(purchaseParentOrderPo.getPurchaseParentOrderNo());
        final List<PurchaseOrderStatus> workingStatusList = PurchaseOrderStatus.getWorkingStatusList();
        purchaseChildOrderPoList.forEach(po -> {
            if (workingStatusList.contains(po.getPurchaseOrderStatus())) {
                throw new ParamIllegalException("该母单存在未完结的子单，请先确保所有子单都已经完结，再操作完结该母单！");
            }
        });
        //更新可发货数
        purchaseBaseService.updatePurchaseShippableCnt(purchaseChildOrderPoList);
        purchaseParentOrderPo.setPurchaseParentOrderStatus(PurchaseParentOrderStatus.COMPLETED);
        purchaseParentOrderDao.updateByIdVersion(purchaseParentOrderPo);
    }

    public List<RawDeliverVo> rawDeliveryOrder(PurchaseChildNoDto dto) {
        final List<PurchaseChildOrderRawPo> demandPurchaseChildRawPoList = purchaseChildOrderRawDao.getListByChildNo(dto.getPurchaseChildOrderNo(), PurchaseRawBizType.DEMAND, Arrays.asList(RawSupplier.HETE, RawSupplier.OTHER_SUPPLIER));

        List<PurchaseRawReceiptOrderPo> purchaseRawReceiptOrderPoList = purchaseRawReceiptOrderDao.getListByPurchaseChildNo(dto.getPurchaseChildOrderNo());

        final List<String> purchaseRawReceiptOrderNoList = Optional.ofNullable(purchaseRawReceiptOrderPoList).orElse(Collections.emptyList()).stream().map(PurchaseRawReceiptOrderPo::getPurchaseRawReceiptOrderNo).collect(Collectors.toList());

        final List<PurchaseRawReceiptOrderItemPo> purchaseRawReceiptOrderItemPoList = purchaseRawReceiptOrderItemDao.getListByNoList(purchaseRawReceiptOrderNoList);

        List<ProcessDeliveryOrderVo> processDeliveryOrderList = wmsRemoteService.getProcessDeliveryOrder(dto.getPurchaseChildOrderNo(), WmsEnum.DeliveryType.PURCHASE_RAW_MATERIAL);

        final Map<String, List<PurchaseRawReceiptOrderItemPo>> skuPurchaseRawItemMap = Optional.ofNullable(purchaseRawReceiptOrderItemPoList).orElse(Collections.emptyList()).stream().collect(Collectors.groupingBy(PurchaseRawReceiptOrderItemPo::getSku));

        final List<RawDeliverOrderBo> rawDeliverOrderBoList = Optional.ofNullable(processDeliveryOrderList).orElse(Collections.emptyList()).stream().map(vo -> {
            final List<ProcessDeliveryOrderVo.DeliveryProduct> products = vo.getProducts();
            if (CollectionUtils.isEmpty(products)) {
                return new ArrayList<RawDeliverOrderBo>();
            }

            return products.stream().map(product -> {
                final RawDeliverOrderBo rawDeliverOrderBo = new RawDeliverOrderBo();
                rawDeliverOrderBo.setSku(product.getSkuCode());
                rawDeliverOrderBo.setSkuBatchCode(product.getBatchCode());
                rawDeliverOrderBo.setDeliveryOrderNo(vo.getDeliveryOrderNo());
                rawDeliverOrderBo.setDeliveryAmount(product.getAmount());
                rawDeliverOrderBo.setDeliveryState(vo.getDeliveryState());
                rawDeliverOrderBo.setWarehouseName(vo.getWarehouseName());

                return rawDeliverOrderBo;
            }).collect(Collectors.toList());
        }).flatMap(Collection::stream).collect(Collectors.toList());

        final Map<String, List<RawDeliverOrderBo>> skuProcessOrderDeliverBoMap = Optional.of(rawDeliverOrderBoList).orElse(Collections.emptyList()).stream().collect(Collectors.groupingBy(RawDeliverOrderBo::getSku));

        final Map<String, PurchaseChildOrderRawPo> skuPurchaseRawMap = Optional.ofNullable(demandPurchaseChildRawPoList).orElse(Collections.emptyList()).stream().collect(Collectors.toMap(PurchaseChildOrderRawPo::getSku, Function.identity()));

        return skuPurchaseRawMap.keySet().stream().map(sku -> {
            final PurchaseChildOrderRawPo purchaseChildOrderRawPo = skuPurchaseRawMap.get(sku);
            final RawDeliverVo rawDeliverVo = new RawDeliverVo();
            rawDeliverVo.setSku(sku);
            rawDeliverVo.setAmount(purchaseChildOrderRawPo.getDeliveryCnt());

            final List<PurchaseRawReceiptOrderItemPo> purchaseRawReceiptOrderItemPoList1 = skuPurchaseRawItemMap.get(sku);
            if (CollectionUtils.isNotEmpty(purchaseRawReceiptOrderItemPoList1)) {
                final PurchaseRawReceiptOrderItemPo purchaseRawReceiptOrderItemPo = purchaseRawReceiptOrderItemPoList1.get(0);
                final int deliverCntSum = purchaseRawReceiptOrderItemPoList1.stream().mapToInt(PurchaseRawReceiptOrderItemPo::getDeliverCnt).sum();
                final int receiptCntSum = purchaseRawReceiptOrderItemPoList1.stream().mapToInt(PurchaseRawReceiptOrderItemPo::getReceiptCnt).sum();
                rawDeliverVo.setSku(purchaseRawReceiptOrderItemPo.getSku());
                rawDeliverVo.setAmount(deliverCntSum);
                rawDeliverVo.setReceiptNum(receiptCntSum);
            }

            final List<RawDeliverVo.DeliveryOrderVo> deliveryOrderVoList = PurchaseConverter.purchaseOrderDeliverBoToRawDeliverVo(skuProcessOrderDeliverBoMap.get(sku));
            rawDeliverVo.setDeliveryOrderVoList(deliveryOrderVoList);

            return rawDeliverVo;
        }).collect(Collectors.toList());
    }


    public PurchaseRawReceived isPurchaseRawReceived(String purchaseChildOrderNo) {
        // 若采购单没有原料，则无需判断收货单情况
        final List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList = purchaseChildOrderRawDao.getListByChildNo(purchaseChildOrderNo, PurchaseRawBizType.DEMAND, Arrays.asList(RawSupplier.HETE, RawSupplier.OTHER_SUPPLIER));
        if (CollectionUtils.isEmpty(purchaseChildOrderRawPoList)) {
            return PurchaseRawReceived.NO_NEED_TO_RECEIVE;
        }

        final List<PurchaseChildOrderRawDeliverPo> purchaseChildOrderRawDeliverPoList = purchaseChildOrderRawDeliverDao.getListByChildOrderNo(purchaseChildOrderNo);
        final List<String> purchaseRawDeliverOrderNoList = purchaseChildOrderRawDeliverPoList.stream()
                .map(PurchaseChildOrderRawDeliverPo::getPurchaseRawDeliverOrderNo)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        final List<PurchaseRawReceiptOrderPo> purchaseRawReceiptOrderPoList = purchaseRawReceiptOrderDao.getListByDeliver(purchaseRawDeliverOrderNoList);
        final List<PurchaseRawReceiptOrderPo> purchaseRawReceiptOrderPoList1 = purchaseRawReceiptOrderDao.getListByPurchaseChildNo(purchaseChildOrderNo);
        purchaseRawReceiptOrderPoList.addAll(purchaseRawReceiptOrderPoList1);
        // 查找不到原料
        if (CollectionUtils.isEmpty(purchaseRawReceiptOrderPoList)) {
            return PurchaseRawReceived.ABSENT_ORDER;
        }
        // 采购单有原料，但是没有原料收货单（wms未签出的情况），不允许进入已投产
        final List<ProcessDeliveryOrderVo> processDeliveryOrderVoList = wmsRemoteService.getDeliveryOrderByDeliverNo(purchaseRawDeliverOrderNoList);
        for (ProcessDeliveryOrderVo processDeliveryOrderVo : processDeliveryOrderVoList) {
            if (!WmsEnum.DeliveryState.SIGNED_OFF.equals(processDeliveryOrderVo.getDeliveryState()) && !WmsEnum.DeliveryState.CANCELED.equals(processDeliveryOrderVo.getDeliveryState())) {
                return PurchaseRawReceived.ABSENT_ORDER;
            }
        }

        // 采购单有原料并且也存在已经收货的原料收货单，且当前不存在未收货的原料收货单，则可以进入已投产
        for (PurchaseRawReceiptOrderPo purchaseRawReceiptOrderPo : purchaseRawReceiptOrderPoList) {
            if (ReceiptOrderStatus.WAIT_RECEIVE.equals(purchaseRawReceiptOrderPo.getReceiptOrderStatus())) {
                return PurchaseRawReceived.UNRECEIVED;
            }
        }

        return PurchaseRawReceived.RECEIVED;
    }

    @Transactional(rollbackFor = Exception.class)
    public void purchaseBackStatus(PurchaseChildNoDto dto) {
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(dto.getPurchaseChildOrderNo());
        Assert.notNull(purchaseChildOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        final PurchaseParentOrderPo purchaseParentOrderPo = purchaseParentOrderDao.getOneByNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
        Assert.notNull(purchaseParentOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));

        final PurchaseOrderStatus purchaseOrderStatus = purchaseChildOrderPo.getPurchaseOrderStatus();
        if (!PurchaseOrderStatus.WAIT_FOLLOWER_CONFIRM.equals(purchaseOrderStatus) && !PurchaseOrderStatus.WAIT_RECEIVE_ORDER.equals(purchaseOrderStatus)) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }

        // 待接单回退状态要作废wms的原料出库单
        if (PurchaseOrderStatus.WAIT_RECEIVE_ORDER.equals(purchaseOrderStatus)) {
            final ProcessOrderCancelEventDto processOrderCancelEventDto = new ProcessOrderCancelEventDto();
            processOrderCancelEventDto.setProcessOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
            processOrderCancelEventDto.setDeliveryType(WmsEnum.DeliveryType.PURCHASE_RAW_MATERIAL);
            processOrderCancelEventDto.setKey(purchaseChildOrderPo.getPurchaseChildOrderNo());
            processOrderCancelEventDto.setOperator(GlobalContext.getUserKey());
            processOrderCancelEventDto.setOperatorName(GlobalContext.getUsername());
            consistencySendMqService.execSendMq(WmsProcessCancelHandler.class, processOrderCancelEventDto);
        }

        final PurchaseOrderStatus targetOrderStatus = purchaseOrderStatus.preStatus();
        purchaseChildOrderPo.setPurchaseOrderStatus(targetOrderStatus);
        purchaseChildOrderPo.setPurchaseBizType(PurchaseBizType.NO_TYPE);
        purchaseChildOrderDao.updateByIdVersion(purchaseChildOrderPo);


        logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseChildOrderPo.getPurchaseChildOrderNo(), BACK_CTRL_NAME + targetOrderStatus.getRemark(), Collections.emptyList());


        // 供应商原料库存释放
        purchaseRawBaseService.releaseSupplierRawInventory(purchaseChildOrderPo, SupplierInventoryCtrlReason.PURCHASE_BACK);

        // 删除对应的原料
        purchaseChildOrderRawDao.deleteByChildNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        purchaseChildOrderRawDeliverDao.deleteByChildNo(purchaseChildOrderPo.getPurchaseChildOrderNo());

        // 单据状态变更后推送单号给wms
        final PurchaseChildNoMqDto purchaseChildNoMqDto = new PurchaseChildNoMqDto();
        purchaseChildNoMqDto.setPurchaseChildOrderNoList(Collections.singletonList(purchaseChildOrderPo.getPurchaseChildOrderNo()));
        consistencySendMqService.execSendMq(PurchaseChangeHandler.class, purchaseChildNoMqDto);
    }


    public PurchaseDemandRawDetailVo getDemandRawList(PurchaseChildNoDto dto) {
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(dto.getPurchaseChildOrderNo());
        Assert.notNull(purchaseChildOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));

        final List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList = purchaseChildOrderRawDao.getListByChildNo(dto.getPurchaseChildOrderNo(), PurchaseRawBizType.DEMAND, Arrays.asList(RawSupplier.HETE, RawSupplier.OTHER_SUPPLIER));
        if (CollectionUtils.isEmpty(purchaseChildOrderRawPoList)) {
            return new PurchaseDemandRawDetailVo();
        }

        final Map<String, List<PurchaseChildOrderRawPo>> rawWarehouseCodeRawPoListMap = purchaseChildOrderRawPoList.stream()
                .collect(Collectors.groupingBy(PurchaseChildOrderRawPo::getRawWarehouseCode));

        final List<String> allSkuList = purchaseChildOrderRawPoList.stream()
                .map(PurchaseChildOrderRawPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(allSkuList);

        final List<SkuInventoryVo> skuInventoryList = new ArrayList<>();
        rawWarehouseCodeRawPoListMap.forEach((rawWarehouseCode, rawPoList) -> {
            final List<String> skuList = rawPoList.stream()
                    .map(PurchaseChildOrderRawPo::getSku)
                    .distinct()
                    .collect(Collectors.toList());
            SkuInstockInventoryQueryDto skuInstockInventoryQueryDto = new SkuInstockInventoryQueryDto();
            skuInstockInventoryQueryDto.setWarehouseCode(rawWarehouseCode);
            skuInstockInventoryQueryDto.setSkuCodes(skuList);
            skuInstockInventoryQueryDto.setProductQuality(WmsEnum.ProductQuality.GOOD);
            skuInstockInventoryQueryDto.setDeliveryType(WmsEnum.DeliveryType.PURCHASE_RAW_MATERIAL);
            skuInstockInventoryQueryDto.setPlatCode(purchaseChildOrderPo.getPlatform());
            skuInventoryList.addAll(wmsRemoteService.getSkuInventoryList(skuInstockInventoryQueryDto));
        });


        List<PurchaseDemandRawVo> purchaseDemandRawVoList = PurchaseConverter.rawAndSkuStockToVo(purchaseChildOrderRawPoList,
                skuInventoryList, skuEncodeMap);
        return PurchaseDemandRawDetailVo.builder()
                .purchaseDemandRawList(purchaseDemandRawVoList)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public void splitOrderSupply(PurchaseSplitSupplyDto dto) {
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(dto.getPurchaseChildOrderNo());
        Assert.notNull(purchaseChildOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildOrderItemDao.getOneByChildOrderNo(dto.getPurchaseChildOrderNo());
        Assert.notNull(purchaseChildOrderItemPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        final PurchaseChildOrderChangePo purchaseChildOrderChangePo = purchaseChildOrderChangeDao.getByChildOrderId(purchaseChildOrderPo.getPurchaseChildOrderId());
        Assert.notNull(purchaseChildOrderChangePo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));


        final PurchaseSupplyCntVo purchaseSupplyCntVo = this.getSupplyCntByChildOrderNo(dto.getPurchaseChildOrderNo());
        final Integer supplyPurchaseCnt = purchaseSupplyCntVo.getSupplyPurchaseCnt();
        if (dto.getSupplyPurchaseCnt() > supplyPurchaseCnt) {
            throw new ParamIllegalException("该采购子单拆分数量超过可拆分数，请调整拆分数后再提交！");
        }
        // 网红类型判断是否网红仓
        if (PurchaseOrderType.WH.equals(purchaseChildOrderPo.getPurchaseOrderType()) && !ScmWarehouseUtil.WH_WAREHOUSE_CODE.equals(dto.getWarehouseCode())) {
            throw new ParamIllegalException("采购需求类型为网红时，只能选择网红仓创建，请重新选择仓库后再提交！");
        }
        if (PurchaseOrderType.NORMAL.equals(purchaseChildOrderPo.getPurchaseOrderType()) && ScmWarehouseUtil.WH_WAREHOUSE_CODE.equals(dto.getWarehouseCode())) {
            throw new ParamIllegalException("采购需求类型为常规时，不能选择网红仓创建，请重新选择仓库后再提交！");
        }

        // 记录原来的下单数
        final Integer purchaseTotal = purchaseChildOrderPo.getPurchaseTotal();
        // 获取sku单件产能
        final SkuInfoPo skuInfoPo = skuInfoDao.getBySku(purchaseChildOrderItemPo.getSku());
        // 更新原的采购单
        this.updateOldPurchase(purchaseChildOrderPo, purchaseChildOrderItemPo, dto.getSupplyPurchaseCnt(), skuInfoPo);

        final SupplierPo supplierPo = supplierBaseService.getSupplierByCode(purchaseChildOrderPo.getSupplierCode());
        final String purchaseChildOrderNo = purchaseBaseService.getLatestPurchaseChildNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
        final PurchaseChildOrderPo newPurchaseChildOrderPo = PurchaseConverter.getInitSplitSupply(dto, purchaseChildOrderNo, purchaseChildOrderPo, supplierPo, SplitType.FOLLOW_SPLIT);
        // 创建新的拆分采购单
        this.createNewSplitPurchase(purchaseChildOrderPo, newPurchaseChildOrderPo, purchaseChildOrderItemPo, purchaseChildOrderChangePo,
                dto.getSupplyPurchaseCnt(), dto.getExpectedOnShelvesDate(), SplitType.FOLLOW_SPLIT, skuInfoPo);

        // 拆单补交处理原料逻辑
        purchaseRawBaseService.splitOrderSupplyRaw(purchaseChildOrderPo, newPurchaseChildOrderPo, purchaseTotal);

        logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                newPurchaseChildOrderPo.getPurchaseChildOrderNo(), PurchaseOrderStatus.WAIT_SCHEDULING.getRemark(), Collections.emptyList());
    }

    /**
     * 创建新的拆分采购单
     *
     * @param purchaseChildOrderPo
     * @param newPurchaseChildOrderPo
     * @param purchaseChildOrderItemPo
     * @param purchaseChildOrderChangePo
     * @param supplyPurchaseCnt          拆分数量
     * @param expectedOnShelvesDate      期望上架时间
     * @param splitType
     * @param skuInfoPo
     */
    private void createNewSplitPurchase(PurchaseChildOrderPo purchaseChildOrderPo,
                                        PurchaseChildOrderPo newPurchaseChildOrderPo,
                                        PurchaseChildOrderItemPo purchaseChildOrderItemPo,
                                        PurchaseChildOrderChangePo purchaseChildOrderChangePo,
                                        Integer supplyPurchaseCnt,
                                        LocalDateTime expectedOnShelvesDate,
                                        SplitType splitType, SkuInfoPo skuInfoPo) {
        // 重新计算要求发货时间
        final PurchaseSkuSupplierItemDto purchaseSkuSupplierItemDto = new PurchaseSkuSupplierItemDto();
        purchaseSkuSupplierItemDto.setSupplierCode(newPurchaseChildOrderPo.getSupplierCode());
        purchaseSkuSupplierItemDto.setSku(purchaseChildOrderItemPo.getSku());
        purchaseSkuSupplierItemDto.setExpectedOnShelvesDate(expectedOnShelvesDate);
        purchaseSkuSupplierItemDto.setCreateTime(LocalDateTime.now());
        purchaseSkuSupplierItemDto.setPurchaseChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        final PurchaseSkuSupplierDto purchaseSkuSupplierDto = new PurchaseSkuSupplierDto();
        purchaseSkuSupplierDto.setPurchaseSkuSupplierItemList(Collections.singletonList(purchaseSkuSupplierItemDto));
        final List<PurchaseSkuSupplierItemVo> supplierDateDetail = purchaseBaseService.getSupplierDateDetail(purchaseSkuSupplierDto);
        final PurchaseSkuSupplierItemVo purchaseSkuSupplierItemVo = supplierDateDetail.get(0);
        if (purchaseSkuSupplierItemVo.getDeliverDate().equals(expectedOnShelvesDate)) {
            throw new ParamIllegalException("该供应商:{}未维护物流时效", purchaseChildOrderPo.getSupplierCode());
        }
        final SupplierUrgentStatus supplierUrgentStatus = purchaseSkuSupplierItemVo.getSupplierUrgentStatus();
        if (SupplierUrgentStatus.URGENT.equals(supplierUrgentStatus)) {
            newPurchaseChildOrderPo.setIsUrgentOrder(BooleanType.TRUE);
        } else if (SupplierUrgentStatus.NOT_URGENT.equals(supplierUrgentStatus)) {
            newPurchaseChildOrderPo.setIsUrgentOrder(BooleanType.FALSE);
        }
        newPurchaseChildOrderPo.setDeliverDate(purchaseSkuSupplierItemVo.getDeliverDate());
        newPurchaseChildOrderPo.setSkuType(purchaseChildOrderPo.getSkuType());
        newPurchaseChildOrderPo.setSplitType(splitType);
        newPurchaseChildOrderPo.setPromiseDate(expectedOnShelvesDate);
        BigDecimal singleCapacity;
        if (null == skuInfoPo) {
            singleCapacity = BigDecimal.ZERO;
            log.info("sku:{}没有配置sku_info信息", purchaseChildOrderItemPo.getSku());
        } else {
            singleCapacity = skuInfoPo.getSingleCapacity();
        }

        final BigDecimal capacity = singleCapacity.multiply(new BigDecimal(supplyPurchaseCnt))
                .setScale(2, RoundingMode.HALF_UP);
        newPurchaseChildOrderPo.setCapacity(capacity);
        purchaseChildOrderDao.insert(newPurchaseChildOrderPo);

        // 创建单据后推送单号给wms
        final PurchaseChildNoMqDto purchaseChildNoMqDto = new PurchaseChildNoMqDto();
        purchaseChildNoMqDto.setPurchaseChildOrderNoList(Arrays.asList(purchaseChildOrderPo.getPurchaseChildOrderNo(),
                newPurchaseChildOrderPo.getPurchaseChildOrderNo()));
        consistencySendMqService.execSendMq(PurchaseChangeHandler.class, purchaseChildNoMqDto);

        // 若日期变更，更新产能
        if (!purchaseChildOrderPo.getPromiseDate().equals(newPurchaseChildOrderPo.getPromiseDate())) {
            final LocalDate newCapacityDate = purchaseBaseService.getCapacityDate(newPurchaseChildOrderPo.getPromiseDate(), newPurchaseChildOrderPo.getSupplierCode());
            final SupOpCapacityBo supOpCapacityBo = new SupOpCapacityBo();
            supOpCapacityBo.setSupplierCode(newPurchaseChildOrderPo.getSupplierCode());
            supOpCapacityBo.setOperateDate(newCapacityDate);
            supOpCapacityBo.setOperateValue(capacity.negate());
            supOpCapacityBo.setBizNo(newPurchaseChildOrderPo.getPurchaseChildOrderNo());

            final LocalDate capacityDate = purchaseBaseService.getCapacityDate(purchaseChildOrderPo.getPromiseDate(), purchaseChildOrderPo.getSupplierCode());
            final SupOpCapacityBo supOpCapacityBo1 = new SupOpCapacityBo();
            supOpCapacityBo1.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
            supOpCapacityBo1.setOperateDate(capacityDate);
            supOpCapacityBo1.setOperateValue(capacity);
            supOpCapacityBo1.setBizNo(purchaseChildOrderPo.getPurchaseChildOrderNo());

            supplierCapacityRefService.operateSupplierCapacityBatch(Arrays.asList(supOpCapacityBo, supOpCapacityBo1));
        }
        // 产能变更日志
        logBaseService.purchaseCapacityLog(LogBizModule.PURCHASE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseChildOrderPo, purchaseChildOrderPo.getCapacity());
        // 产能变更日志
        logBaseService.purchaseCapacityLog(LogBizModule.PURCHASE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                newPurchaseChildOrderPo.getPurchaseChildOrderNo(), newPurchaseChildOrderPo, newPurchaseChildOrderPo.getCapacity());


        final SkuBatchCreateDto skuBatchCreateDto = new SkuBatchCreateDto();
        skuBatchCreateDto.setPurchaseChildOrderNo(newPurchaseChildOrderPo.getPurchaseChildOrderNo());
        skuBatchCreateDto.setSkuCodeList(Collections.singletonList(purchaseChildOrderItemPo.getSku()));
        skuBatchCreateDto.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
        skuBatchCreateDto.setSupplierName(purchaseChildOrderPo.getSupplierName());
        final Map<String, String> skuBatchMap = wmsRemoteService.batchCreateBatchCode(skuBatchCreateDto);

        final PurchaseChildOrderItemPo newPurchaseChildOrderItemPo = PurchaseConverter.getInitSplitSupplyItem(supplyPurchaseCnt,
                newPurchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseChildOrderItemPo, skuBatchMap);
        purchaseChildOrderItemDao.insert(newPurchaseChildOrderItemPo);
        final PurchaseChildOrderChangePo newPurchaseChildOrderChangePo = new PurchaseChildOrderChangePo();
        newPurchaseChildOrderChangePo.setPurchaseChildOrderId(newPurchaseChildOrderPo.getPurchaseChildOrderId());
        newPurchaseChildOrderChangePo.setPurchaseChildOrderNo(newPurchaseChildOrderPo.getPurchaseChildOrderNo());
        if (SplitType.SUPPLIER_SPLIT.equals(splitType)) {
            newPurchaseChildOrderChangePo.setConfirmUser(purchaseChildOrderChangePo.getConfirmUser());
            newPurchaseChildOrderChangePo.setConfirmUsername(purchaseChildOrderChangePo.getConfirmUsername());
            newPurchaseChildOrderChangePo.setPlanConfirmUser(purchaseChildOrderChangePo.getPlanConfirmUser());
            newPurchaseChildOrderChangePo.setPlanConfirmUsername(purchaseChildOrderChangePo.getPlanConfirmUsername());
        } else {
            newPurchaseChildOrderChangePo.setConfirmUser(GlobalContext.getUserKey());
            newPurchaseChildOrderChangePo.setConfirmUsername(GlobalContext.getUsername());
        }
        newPurchaseChildOrderChangePo.setPlaceOrderTime(purchaseChildOrderChangePo.getPlaceOrderTime());
        newPurchaseChildOrderChangePo.setReceiveOrderTime(purchaseChildOrderChangePo.getReceiveOrderTime());
        newPurchaseChildOrderChangePo.setPlanConfirmTime(purchaseChildOrderChangePo.getPlanConfirmTime());
        newPurchaseChildOrderChangePo.setConfirmTime(purchaseChildOrderChangePo.getConfirmTime());
        purchaseChildOrderChangeDao.insert(newPurchaseChildOrderChangePo);
    }

    /**
     * 更新旧的采购单
     *
     * @param purchaseChildOrderPo
     * @param purchaseChildOrderItemPo
     * @param supplyPurchaseCnt        拆分数
     * @param skuInfoPo
     */
    private void updateOldPurchase(PurchaseChildOrderPo purchaseChildOrderPo,
                                   PurchaseChildOrderItemPo purchaseChildOrderItemPo,
                                   Integer supplyPurchaseCnt, SkuInfoPo skuInfoPo) {
        BigDecimal singleCapacity;
        // 重新计算消耗产能
        if (null == skuInfoPo) {
            singleCapacity = BigDecimal.ZERO;
            log.info("sku:{}没有配置sku_info信息", purchaseChildOrderItemPo.getSku());
        } else {
            singleCapacity = skuInfoPo.getSingleCapacity();
        }
        final BigDecimal capacity = singleCapacity.multiply(new BigDecimal(purchaseChildOrderPo.getPurchaseTotal() - supplyPurchaseCnt))
                .setScale(2, RoundingMode.HALF_UP);

        purchaseChildOrderPo.setPurchaseTotal(purchaseChildOrderPo.getPurchaseTotal() - supplyPurchaseCnt);
        purchaseChildOrderPo.setShippableCnt(purchaseChildOrderPo.getShippableCnt() - supplyPurchaseCnt);
        BigDecimal timelyDeliveryRate = BigDecimal.valueOf(purchaseChildOrderPo.getTimelyDeliveryCnt())
                .divide(BigDecimal.valueOf(purchaseChildOrderPo.getPurchaseTotal()), 2, RoundingMode.HALF_UP);
        purchaseChildOrderPo.setTimelyDeliveryRate(timelyDeliveryRate);
        purchaseChildOrderItemPo.setPurchaseCnt(purchaseChildOrderItemPo.getPurchaseCnt() - supplyPurchaseCnt);
        purchaseChildOrderItemPo.setInitPurchaseCnt(purchaseChildOrderItemPo.getInitPurchaseCnt() - supplyPurchaseCnt);
        purchaseChildOrderItemPo.setUndeliveredCnt(purchaseChildOrderItemPo.getUndeliveredCnt() - supplyPurchaseCnt);
        purchaseChildOrderPo.setCapacity(capacity);

        if (purchaseChildOrderItemPo.getPurchaseCnt() <= purchaseChildOrderItemPo.getQualityGoodsCnt()) {
            purchaseChildOrderPo.setPurchaseOrderStatus(PurchaseOrderStatus.FINISH);
            purchaseChildOrderPo.setCapacity(BigDecimal.ZERO);

            logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                    purchaseChildOrderPo.getPurchaseChildOrderNo(), PurchaseOrderStatus.FINISH.getRemark(), Collections.emptyList());

            // 产能变更日志
            logBaseService.purchaseCapacityLog(LogBizModule.PURCHASE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                    purchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseChildOrderPo, purchaseChildOrderPo.getCapacity());
        }

        purchaseChildOrderDao.updateByIdVersion(purchaseChildOrderPo);
        purchaseChildOrderItemDao.updateByIdVersion(purchaseChildOrderItemPo);
    }

    public PurchaseSupplyCntVo getSupplyCntByChildOrderNo(String purchaseChildOrderNo) {
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(purchaseChildOrderNo);
        Assert.notNull(purchaseChildOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));

        return PurchaseSupplyCntVo.builder().supplyPurchaseCnt(this.getSupplyCnt(purchaseChildOrderPo)).build();
    }

    private Integer getSupplyCnt(PurchaseChildOrderPo purchaseChildOrderPo) {
        //当可发货数=采购数时，可补交数=可发货数-1
        //当可发货数小于采购数时，可补交数=可发货数
        if (purchaseChildOrderPo.getShippableCnt().equals(purchaseChildOrderPo.getPurchaseTotal())) {
            return purchaseChildOrderPo.getPurchaseTotal() - 1;
        } else if (purchaseChildOrderPo.getShippableCnt() < purchaseChildOrderPo.getPurchaseTotal()) {
            return purchaseChildOrderPo.getShippableCnt();
        } else {
            throw new BizException("获取可拆分数错误，请联系系统管理员!");
        }
    }

    public PurchaseSkuSplitCntVo getSkuSpiltCnt(PurchaseParentNoDto dto) {
        final PurchaseParentOrderPo purchaseParentOrderPo = purchaseParentOrderDao.getOneByNo(dto.getPurchaseParentOrderNo());
        Assert.notNull(purchaseParentOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        final List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList = purchaseParentOrderItemDao.getListByParentNo(dto.getPurchaseParentOrderNo());
        Assert.notEmpty(purchaseParentOrderItemPoList, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));

        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByParentNo(dto.getPurchaseParentOrderNo());

        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByParent(dto.getPurchaseParentOrderNo());

        final Map<String, PurchaseChildOrderPo> purchaseChildNoMap = purchaseChildOrderPoList.stream().collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));
        final Map<String, Integer> purchaseSkuMap = purchaseParentOrderItemPoList.stream().collect(Collectors.toMap(PurchaseParentOrderItemPo::getSku, PurchaseParentOrderItemPo::getPurchaseCnt));

        final Map<String, Integer> skuAndSplitCntMap = purchaseChildOrderItemPoList.stream().filter(itemPo -> {
            final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildNoMap.get(itemPo.getPurchaseChildOrderNo());
            final PurchaseOrderStatus purchaseOrderStatus = purchaseChildOrderPo.getPurchaseOrderStatus();
            return !PurchaseOrderStatus.DELETE.equals(purchaseOrderStatus) && !PurchaseOrderStatus.RETURN.equals(purchaseOrderStatus);
        }).collect(Collectors.groupingBy(PurchaseChildOrderItemPo::getSku, Collectors.summingInt(PurchaseChildOrderItemPo::getPurchaseCnt)));

        final List<PurchaseSkuSplitCntItemVo> purchaseSkuSplitCntItemVoList = purchaseSkuMap.entrySet().stream().map(entry -> {
            final Integer childPurchaseCnt = skuAndSplitCntMap.getOrDefault(entry.getKey(), 0);
            return new PurchaseSkuSplitCntItemVo(entry.getKey(), entry.getValue(), childPurchaseCnt, entry.getValue() - childPurchaseCnt);
        }).collect(Collectors.toList());

        return PurchaseSkuSplitCntVo.builder().purchaseSkuSplitCntItemList(purchaseSkuSplitCntItemVoList).build();
    }

    @Transactional(rollbackFor = Exception.class)
    public void importEditPurchaseChild(PurchaseChildEditImportationDto dto) {
        if (StringUtils.isNotBlank(dto.getDeliverDateStr())) {
            if (!ScmFormatUtil.isLocalDateTimeStandardFormat(dto.getDeliverDateStr(), DatePattern.NORM_DATETIME_PATTERN)) {
                throw new ParamIllegalException("期望上架时间格式不符合日期格式，请重新填写后再操作");
            }
            final LocalDateTime deliverDate = ScmTimeUtil.dateStrToLocalDateTime(dto.getDeliverDateStr(), DatePattern.NORM_DATETIME_PATTERN);
            dto.setDeliverDate(ScmTimeUtil.getLastSecondTimeOfDayForTime(deliverDate));
        }

        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(dto.getPurchaseChildOrderNo());
        Assert.notNull(purchaseChildOrderPo, () -> new ParamIllegalException("采购订单号错误"));
        final PurchaseChildOrderChangePo purchaseChildOrderChangePo = purchaseChildOrderChangeDao.getByChildOrderId(purchaseChildOrderPo.getPurchaseChildOrderId());
        Assert.notNull(purchaseChildOrderChangePo, () -> new ParamIllegalException("采购订单号错误"));
        final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildOrderItemDao.getOneByChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        Assert.notNull(purchaseChildOrderItemPo, () -> new ParamIllegalException("采购订单号错误"));

        final PurchaseOrderStatus purchaseOrderStatus = purchaseChildOrderPo.getPurchaseOrderStatus();
        if (null != dto.getDeliverDate() && !PurchaseOrderStatus.getEditDeliverDateStatus().contains(purchaseOrderStatus)) {
            throw new ParamIllegalException("【{}】状态不可以修改约定交期", purchaseOrderStatus.getRemark());
        }

        if (null != dto.getDeliverDate() && dto.getDeliverDate().isBefore(LocalDateTime.now())) {
            throw new ParamIllegalException("约定交期不可早于当前时间");
        }

        if (StringUtils.isNotBlank(dto.getWarehouseCode()) && !purchaseChildOrderPo.getWarehouseCode().equals(dto.getWarehouseCode()) && !PurchaseOrderStatus.getEditWarehouseCodeStatus().contains(purchaseOrderStatus)) {
            throw new ParamIllegalException("【{}】状态不可以修改收货仓库", purchaseOrderStatus.getRemark());
        }

        if (StringUtils.isNotBlank(dto.getWarehouseCode())) {
            // 网红类型判断是否网红仓
            if (PurchaseOrderType.WH.equals(purchaseChildOrderPo.getPurchaseOrderType()) && !ScmWarehouseUtil.WH_WAREHOUSE_CODE.equals(dto.getWarehouseCode())) {
                throw new ParamIllegalException("采购需求类型为网红时，只能选择网红仓创建，请重新选择仓库后再提交！");
            }
            if (PurchaseOrderType.NORMAL.equals(purchaseChildOrderPo.getPurchaseOrderType()) && ScmWarehouseUtil.WH_WAREHOUSE_CODE.equals(dto.getWarehouseCode())) {
                throw new ParamIllegalException("采购需求类型为常规时，不能选择网红仓创建，请重新选择仓库后再提交！");
            }
            final List<WarehouseVo> warehouseVoList = wmsRemoteService.getWarehouseByCode(Collections.singletonList(dto.getWarehouseCode()));
            if (StringUtils.isNotBlank(dto.getWarehouseCode()) &&
                    (CollectionUtils.isEmpty(warehouseVoList) || warehouseVoList.size() > 1)) {
                throw new ParamIllegalException("收货仓库错误");
            }
            final WarehouseVo warehouseVo = warehouseVoList.get(0);
            purchaseChildOrderPo.setWarehouseCode(warehouseVo.getWarehouseCode());
            purchaseChildOrderPo.setWarehouseName(warehouseVo.getWarehouseName());
            purchaseChildOrderPo.setWarehouseTypes(warehouseVo.getWarehouseTypeName());
        }

        // 获取数据库旧期望上架时间
        LocalDateTime expectedOnShelvesDate = purchaseChildOrderPo.getExpectedOnShelvesDate();

        if (null != dto.getDeliverDate()) {
            // 校验是否变更期望上架时间
            final LocalDateTime spanMinDate = ScmTimeUtil.getAfterDate(EXPECTED_DATE_SPAN);
            if (!purchaseChildOrderPo.getExpectedOnShelvesDate().equals(dto.getDeliverDate()) && dto.getDeliverDate().isBefore(spanMinDate)) {
                throw new ParamIllegalException("期望上架时间修改必须在当前时间{}天之后（{}），请重新修改后再提交！", EXPECTED_DATE_SPAN, LocalDateTimeUtil.format(spanMinDate, DatePattern.NORM_DATE_PATTERN));
            }

            // 重新计算加急标签
            this.resetUrgentLabel(purchaseChildOrderPo, purchaseChildOrderItemPo, dto.getDeliverDate());

            // 计算延期天数
            final long delayDays = ChronoUnit.DAYS.between(purchaseChildOrderPo.getExpectedOnShelvesDate(), dto.getDeliverDate());
            purchaseChildOrderPo.setDelayDays(Math.toIntExact(delayDays));
            purchaseChildOrderPo.setExpectedOnShelvesDate(dto.getDeliverDate());
        }
        if (StringUtils.isNotBlank(dto.getPromiseDateStr())) {
            if (!ScmFormatUtil.isLocalDateTimeStandardFormat(dto.getPromiseDateStr(), DatePattern.NORM_DATETIME_PATTERN)) {
                throw new ParamIllegalException("答交时间格式不符合日期格式，请重新填写后再操作");
            }

            if (PurchaseOrderStatus.WAIT_FOLLOWER_CONFIRM.equals(purchaseOrderStatus) || PurchaseOrderStatus.WAIT_CONFIRM.equals(purchaseOrderStatus)) {
                throw new ParamIllegalException("{}与{}状态不允许变更答交时间，请重新填写后再提交！",
                        PurchaseOrderStatus.WAIT_FOLLOWER_CONFIRM.getRemark(), PurchaseOrderStatus.WAIT_CONFIRM.getRemark());
            }
            final LocalDateTime promiseDate = ScmTimeUtil.dateStrToLocalDateTime(dto.getPromiseDateStr(), DatePattern.NORM_DATETIME_PATTERN);
            dto.setPromiseDate(ScmTimeUtil.getLastSecondTimeOfDayForTime(promiseDate));
            // 变更了答交时间，并且状态处于待接单之后
            if (!dto.getPromiseDate().equals(purchaseChildOrderPo.getPromiseDate())
                    && purchaseChildOrderPo.getPurchaseOrderStatus().getSort() >= PurchaseOrderStatus.WAIT_RECEIVE_ORDER.getSort()) {
                purchaseChildOrderPo.setPromiseDateChg(BooleanType.TRUE);
            }

            // 若答交时间与原来不同，产能变更
            if (!dto.getPromiseDate().equals(purchaseChildOrderPo.getPromiseDate())) {
                BigDecimal singleCapacity;
                final SkuInfoPo skuInfoPo = skuInfoDao.getBySku(purchaseChildOrderItemPo.getSku());
                if (null == skuInfoPo) {
                    singleCapacity = BigDecimal.ZERO;
                    log.info("sku:{}没有配置sku_info信息", purchaseChildOrderItemPo.getSku());
                } else {
                    singleCapacity = skuInfoPo.getSingleCapacity();
                }
                final BigDecimal capacity = singleCapacity.multiply(new BigDecimal(purchaseChildOrderItemPo.getPurchaseCnt()))
                        .setScale(2, RoundingMode.HALF_UP);
                // 增加原来日期的产能
                final LocalDate capacityDate = purchaseBaseService.getCapacityDate(purchaseChildOrderPo.getPromiseDate(), purchaseChildOrderPo.getSupplierCode());
                final SupOpCapacityBo supOpCapacityBo = new SupOpCapacityBo();
                supOpCapacityBo.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
                supOpCapacityBo.setOperateDate(capacityDate);
                supOpCapacityBo.setOperateValue(capacity);
                supOpCapacityBo.setBizNo(purchaseChildOrderPo.getPurchaseChildOrderNo());

                // 扣减新日期的产能
                final LocalDate capacityDate1 = purchaseBaseService.getCapacityDate(dto.getPromiseDate(), purchaseChildOrderPo.getSupplierCode());
                final SupOpCapacityBo supOpCapacityBo1 = new SupOpCapacityBo();
                supOpCapacityBo1.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
                supOpCapacityBo1.setOperateDate(capacityDate1);
                supOpCapacityBo1.setOperateValue(capacity.negate());
                supOpCapacityBo1.setBizNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
                supplierCapacityRefService.operateSupplierCapacityBatch(Arrays.asList(supOpCapacityBo, supOpCapacityBo1));
                // 产能变更日志
                logBaseService.purchaseCapacityLog(LogBizModule.PURCHASE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                        purchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseChildOrderPo, purchaseChildOrderPo.getCapacity());
            }
            // 进行编辑时，答交日期变化或下单数量变化，推送mq给wms
            if (!dto.getPromiseDate().equals(purchaseChildOrderPo.getPromiseDate())) {
                final PurchaseChildNoMqDto purchaseChildNoMqDto = new PurchaseChildNoMqDto();
                purchaseChildNoMqDto.setPurchaseChildOrderNoList(Collections.singletonList(purchaseChildOrderPo.getPurchaseChildOrderNo()));
                consistencySendMqService.execSendMq(PurchaseChangeHandler.class, purchaseChildNoMqDto);
            }

            purchaseChildOrderPo.setPromiseDate(dto.getPromiseDate());
        }

        purchaseChildOrderDao.updateByIdVersion(purchaseChildOrderPo);


        if (PurchaseOrderStatus.WAIT_DELIVER.equals(purchaseOrderStatus)) {
            final List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
            if (CollectionUtils.isNotEmpty(purchaseDeliverOrderPoList)) {
                throw new ParamIllegalException("当前采购单已经生成发货单，不允许编辑");
            }
        }

        // 判断期望上架时间是否修改时才更新
        if (ScmTimeUtil.determineLocalDateTimeEqual(expectedOnShelvesDate, dto.getDeliverDate())) {
            purchaseChildOrderChangePo.setLastModifyUser(GlobalContext.getUserKey());
            purchaseChildOrderChangePo.setLastModifyUsername(GlobalContext.getUsername());
            purchaseChildOrderChangePo.setLastModifyTime(LocalDateTime.now());
            purchaseChildOrderChangeDao.updateByIdVersion(purchaseChildOrderChangePo);
        }

        purchaseChildOrderItemDao.updateByIdVersion(purchaseChildOrderItemPo);
    }

    /**
     * 重新计算加急标签
     *
     * @param purchaseChildOrderPo
     * @param purchaseChildOrderItemPo
     * @param expectedOnShelvesDate
     */
    private void resetUrgentLabel(PurchaseChildOrderPo purchaseChildOrderPo, PurchaseChildOrderItemPo purchaseChildOrderItemPo, LocalDateTime expectedOnShelvesDate) {
        if (!purchaseChildOrderPo.getExpectedOnShelvesDate().equals(expectedOnShelvesDate) && StringUtils.isNotBlank(purchaseChildOrderPo.getSupplierCode())) {
            final PurchaseSkuSupplierItemDto purchaseSkuSupplierItemDto = new PurchaseSkuSupplierItemDto();
            purchaseSkuSupplierItemDto.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
            purchaseSkuSupplierItemDto.setSku(purchaseChildOrderItemPo.getSku());
            purchaseSkuSupplierItemDto.setExpectedOnShelvesDate(expectedOnShelvesDate);
            purchaseSkuSupplierItemDto.setCreateTime(LocalDateTime.now());
            purchaseSkuSupplierItemDto.setPurchaseChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
            final PurchaseSkuSupplierDto purchaseSkuSupplierDto = new PurchaseSkuSupplierDto();
            purchaseSkuSupplierDto.setPurchaseSkuSupplierItemList(Collections.singletonList(purchaseSkuSupplierItemDto));
            final List<PurchaseSkuSupplierItemVo> supplierDateDetail = purchaseBaseService.getSupplierDateDetail(purchaseSkuSupplierDto);
            final PurchaseSkuSupplierItemVo purchaseSkuSupplierItemVo = supplierDateDetail.get(0);
            final SupplierUrgentStatus supplierUrgentStatus = purchaseSkuSupplierItemVo.getSupplierUrgentStatus();
            if (SupplierUrgentStatus.URGENT.equals(supplierUrgentStatus)) {
                purchaseChildOrderPo.setIsUrgentOrder(BooleanType.TRUE);
            } else if (SupplierUrgentStatus.NOT_URGENT.equals(supplierUrgentStatus)) {
                purchaseChildOrderPo.setIsUrgentOrder(BooleanType.FALSE);
            }
            purchaseChildOrderPo.setDeliverDate(purchaseSkuSupplierItemVo.getDeliverDate());
        }
    }

    public void cancelDeliver(PurchaseDeliverIdAndVersionDto dto) {
        final PurchaseDeliverOrderPo purchaseDeliverOrderPo = purchaseDeliverOrderDao.getByIdVersion(dto.getPurchaseDeliverOrderId(), dto.getVersion());
        Assert.notNull(purchaseDeliverOrderPo, () -> new ParamIllegalException("数据已被更新,请刷新页面后重试"));
        final String purchaseChildOrderNo = purchaseDeliverOrderPo.getPurchaseChildOrderNo();
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(purchaseChildOrderNo);
        Assert.notNull(purchaseChildOrderPo, () -> new BizException("找不到采购子单，取消发货失败！"));
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        Assert.notEmpty(purchaseChildOrderItemPoList, () -> new BizException("找不到采购子单，取消发货失败！"));
        final DeliverOrderStatus deliverOrderStatus = purchaseDeliverOrderPo.getDeliverOrderStatus();
        if (!DeliverOrderStatus.WAIT_RECEIVE.equals(deliverOrderStatus)) {
            throw new ParamIllegalException("采购发货单状态为:{}，不允许取消发货，请刷新后重试!", deliverOrderStatus.getRemark());
        }

        final PurchaseParentOrderPo purchaseParentOrderPo = purchaseParentOrderDao.getOneByNo(purchaseChildOrderPo.getPurchaseParentOrderNo());

        purchaseDeliverBaseService.cancelDeliver(purchaseDeliverOrderPo, purchaseParentOrderPo, purchaseChildOrderPo, purchaseChildOrderItemPoList, SystemType.SCM);
    }

    public PurchaseChildOrderNoListVo getPrenatalSampleOrderByNo(PurchaseChildNoDto dto) {
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoListAndPurchaseOrderType(dto.getPurchaseChildOrderNo(), PurchaseOrderType.PRENATAL);
        final List<String> purchaseChildOrderNoList = purchaseChildOrderPoList.stream().map(PurchaseChildOrderPo::getPurchaseChildOrderNo).collect(Collectors.toList());
        final PurchaseChildOrderNoListVo purchaseChildOrderNoListVo = new PurchaseChildOrderNoListVo();
        purchaseChildOrderNoListVo.setPurchaseChildOrderNoList(purchaseChildOrderNoList);
        return purchaseChildOrderNoListVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportPurchaseChildDeliver(PurchaseProductSearchDto dto) {
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(), FileOperateBizType.SCM_PURCHASE_DELIVER_EXPORT.getCode(), dto));
    }

    @Transactional(rollbackFor = Exception.class)
    public void purchaseInitAvgPriceTask() {
        final Long purchaseChildOrderItemCnt = purchaseChildOrderItemDao.getCnt();
        for (int i = 1; i * 50L < purchaseChildOrderItemCnt; i++) {
            final CommonPageResult.PageInfo<PurchaseChildOrderItemPo> page = purchaseChildOrderItemDao.getPage(PageDTO.of(i, 50));
            final List<PurchaseChildOrderItemPo> records = page.getRecords();
            if (CollectionUtils.isEmpty(records)) {
                continue;
            }
            final SkuAndBatchCodeBo skuAndBatchCodeBo = new SkuAndBatchCodeBo();
            skuAndBatchCodeBo.setSkuAvgPriceBizType(SkuAvgPriceBizType.PURCHASE);
            final List<SkuAndBatchCodeItemBo> skuAndBatchCodeItemBoList = records.stream().map(record -> {
                final SkuAndBatchCodeItemBo skuAndBatchCodeItemBo = new SkuAndBatchCodeItemBo();
                skuAndBatchCodeItemBo.setSku(record.getSku());
                skuAndBatchCodeItemBo.setSkuBatchCode(record.getSkuBatchCode());
                skuAndBatchCodeItemBo.setAccrueCnt(record.getPurchaseCnt());
                skuAndBatchCodeItemBo.setAccruePrice(record.getPurchasePrice().multiply(new BigDecimal(record.getPurchaseCnt())));
                return skuAndBatchCodeItemBo;
            }).collect(Collectors.toList());

            skuAndBatchCodeBo.setSkuAndBatchCodeItemBoList(skuAndBatchCodeItemBoList);
            skuAvgPriceBaseService.getSkuAvgPrice(skuAndBatchCodeBo);
        }
    }

    public List<ConfirmCommissioningMsgVo> confirmCommissioningMsg(PurchaseChildNoDto dto) {
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(dto.getPurchaseChildOrderNo());
        if (null == purchaseChildOrderPo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }

        final List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList = purchaseChildOrderRawDao.getListByChildNo(dto.getPurchaseChildOrderNo(), PurchaseRawBizType.DEMAND);
        if (CollectionUtils.isEmpty(purchaseChildOrderRawPoList)) {
            return Collections.emptyList();
        }
        final List<String> skuList = purchaseChildOrderRawPoList.stream().map(PurchaseChildOrderRawPo::getSku).distinct().collect(Collectors.toList());
        final List<SupplierInventoryPo> supplierInventoryPoList = supplierInventoryBaseService.getInventoryBySkuAndSupplier(skuList, purchaseChildOrderPo.getSupplierCode());


        return PurchaseConverter.rawPoToConfirmMsgVoList(purchaseChildOrderRawPoList, supplierInventoryPoList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void confirmCommissioning(ConfirmCommissioningDto dto) {
        // 变更状态
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(dto.getPurchaseChildOrderNo());
        Assert.notNull(purchaseChildOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildOrderItemDao.getOneByChildOrderNo(dto.getPurchaseChildOrderNo());
        Assert.notNull(purchaseChildOrderItemPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        final PurchaseChildOrderChangePo purchaseChildOrderChangePo = purchaseChildOrderChangeDao.getByChildOrderId(purchaseChildOrderPo.getPurchaseChildOrderId());
        Assert.notNull(purchaseChildOrderChangePo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));

        // 若投产数量为空，默认赋值为采购数
        if (null == dto.getCommissionCnt()) {
            dto.setCommissionCnt(purchaseChildOrderPo.getPurchaseTotal());
        }
        // 校验投产数量
        if (purchaseChildOrderPo.getPurchaseTotal() < dto.getCommissionCnt() || dto.getCommissionCnt() <= 0) {
            throw new ParamIllegalException("投产数量有误，请重新填写！");
        }
        // 校验原料发货单是否全部完成收货
        final PurchaseRawReceived purchaseRawReceived = this.isPurchaseRawReceived(dto.getPurchaseChildOrderNo());
        if (PurchaseRawReceived.ABSENT_ORDER.equals(purchaseRawReceived)) {
            throw new ParamIllegalException("采购原料发货单未签出，请联系仓储尽快签出采购原料发货单");
        }
        if (PurchaseRawReceived.UNRECEIVED.equals(purchaseRawReceived)) {
            throw new ParamIllegalException("采购原料发货单未收货，请到原料收货列表进行收获操作");
        }

        // 判断投产数量选择是否拆单
        if (purchaseChildOrderPo.getPurchaseTotal() > dto.getCommissionCnt()) {
            // 记录原来的下单数
            final Integer purchaseTotal = purchaseChildOrderPo.getPurchaseTotal();

            // 拆单数等于下单数减去投产数
            final Integer supplyPurchaseCnt = purchaseTotal - dto.getCommissionCnt();
            // 更新原的采购单
            final SkuInfoPo skuInfoPo = skuInfoDao.getBySku(purchaseChildOrderItemPo.getSku());
            this.updateOldPurchase(purchaseChildOrderPo, purchaseChildOrderItemPo, supplyPurchaseCnt, skuInfoPo);

            final SupplierPo supplierPo = supplierBaseService.getSupplierByCode(purchaseChildOrderPo.getSupplierCode());
            final String purchaseChildOrderNo = purchaseBaseService.getLatestPurchaseChildNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
            final PurchaseChildOrderPo newPurchaseChildOrderPo = PurchaseConverter.getInitSplitSupply(purchaseChildOrderNo,
                    purchaseChildOrderPo, supplierPo, supplyPurchaseCnt, SplitType.SUPPLIER_SPLIT);
            // 创建新的拆分采购单
            this.createNewSplitPurchase(purchaseChildOrderPo, newPurchaseChildOrderPo, purchaseChildOrderItemPo,
                    purchaseChildOrderChangePo, supplyPurchaseCnt, purchaseChildOrderPo.getExpectedOnShelvesDate(), SplitType.SUPPLIER_SPLIT, skuInfoPo);

            // 拆单补交处理原料逻辑
            purchaseRawBaseService.splitOrderSupplyRaw(purchaseChildOrderPo, newPurchaseChildOrderPo, purchaseTotal);

            logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                    newPurchaseChildOrderPo.getPurchaseChildOrderNo(), PurchaseOrderStatus.WAIT_SCHEDULING.getRemark(), Collections.emptyList());

            // 产能变更日志
            logBaseService.purchaseCapacityLog(LogBizModule.PURCHASE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                    purchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseChildOrderPo, purchaseChildOrderPo.getCapacity());
            // 产能变更日志
            logBaseService.purchaseCapacityLog(LogBizModule.PURCHASE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                    newPurchaseChildOrderPo.getPurchaseChildOrderNo(), newPurchaseChildOrderPo, newPurchaseChildOrderPo.getCapacity());
        }

        final PurchaseOrderStatus purchaseOrderStatus = PurchaseCtrlType.COMMISSIONING.getFunction().apply(purchaseChildOrderPo.getPurchaseOrderStatus());
        purchaseChildOrderPo.setPurchaseOrderStatus(purchaseOrderStatus);
        purchaseChildOrderDao.updateByIdVersion(purchaseChildOrderPo);

        purchaseChildOrderChangePo.setCommissioningTime(LocalDateTime.now());
        purchaseChildOrderChangeDao.updateByIdVersion(purchaseChildOrderChangePo);

        logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseOrderStatus.getRemark(), Collections.emptyList());

        // 扣减库存
        final List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList = purchaseChildOrderRawDao.getListByChildNo(dto.getPurchaseChildOrderNo(),
                PurchaseRawBizType.DEMAND, Collections.singletonList(RawSupplier.SUPPLIER));
        // 不存在原料时，结束该流程，兼容历史数据
        if (CollectionUtils.isEmpty(purchaseChildOrderRawPoList)) {
            return;
        }

        // 过滤掉非供应商来源的原料入参
        final List<ConfirmCommissioningItemDto> confirmCommissioningItemList = dto.getConfirmCommissioningItemList().stream()
                .filter(itemDto -> RawSupplier.SUPPLIER.equals(itemDto.getRawSupplier()))
                .collect(Collectors.toList());
        // 若不存在需要处理的原料，则方法结束
        if (CollectionUtils.isEmpty(confirmCommissioningItemList)) {
            return;
        }

        // 释放库存
        purchaseRawBaseService.releaseSupplierRawInventory(purchaseChildOrderPo, SupplierInventoryCtrlReason.PRODUCT_COMMISSIONING);
        // 投产原料处理
        purchaseRawBaseService.confirmCommissionRaw(confirmCommissioningItemList, purchaseChildOrderPo, purchaseChildOrderRawPoList);

        // 单据状态变更后推送单号给wms
        final PurchaseChildNoMqDto purchaseChildNoMqDto = new PurchaseChildNoMqDto();
        purchaseChildNoMqDto.setPurchaseChildOrderNoList(Collections.singletonList(dto.getPurchaseChildOrderNo()));
        consistencySendMqService.execSendMq(PurchaseChangeHandler.class, purchaseChildNoMqDto);
    }

    public List<PurchaseDeliverRawVo> getRawConsumeByPurchaseNo(PurchaseChildNoDto dto) {
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(dto.getPurchaseChildOrderNo());
        if (null == purchaseChildOrderPo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }

        final List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList = purchaseChildOrderRawDao.getListByChildNo(dto.getPurchaseChildOrderNo(), PurchaseRawBizType.DEMAND);
        if (CollectionUtils.isEmpty(purchaseChildOrderRawPoList)) {
            return Collections.emptyList();
        }
        final List<String> skuList = purchaseChildOrderRawPoList.stream()
                .filter(rawPo -> RawSupplier.SUPPLIER.equals(rawPo.getRawSupplier()))
                .map(PurchaseChildOrderRawPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final List<SupplierInventoryPo> supplierInventoryPoList = supplierInventoryBaseService.getInventoryBySkuAndSupplier(skuList, purchaseChildOrderPo.getSupplierCode());
        final Map<String, Integer> skuInventoryCntMap = supplierInventoryPoList.stream()
                .collect(Collectors.toMap(SupplierInventoryPo::getSku, po -> po.getStockUpInventory() + po.getSelfProvideInventory()));

        return PurchaseConverter.rawPoAndInventoryToVo(purchaseChildOrderRawPoList, skuInventoryCntMap);
    }

    @Transactional(rollbackFor = Exception.class)
    public void fastSupply(PurchaseFastSupplyDto dto) {
        final PurchaseChildOrderPo originPurchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(dto.getPurchaseChildOrderNo());
        Assert.notNull(originPurchaseChildOrderPo, () -> new ParamIllegalException("找不到对应的采购子单,获取子单详情失败!"));
        final PurchaseChildOrderChangePo originPurchaseChildOrderChangePo = purchaseChildOrderChangeDao.getByChildOrderId(originPurchaseChildOrderPo.getPurchaseChildOrderId());
        Assert.notNull(originPurchaseChildOrderChangePo, () -> new ParamIllegalException("找不到对应的采购子单,获取子单详情失败!"));

        // 校验原单据状态
        if (!PurchaseOrderStatus.fastSupplyStatus().contains(originPurchaseChildOrderPo.getPurchaseOrderStatus())) {
            throw new ParamIllegalException("当前采购订单状态不支持快速补单操作，请刷新后重试！");
        }
        if (!SkuType.SM_SKU.equals(originPurchaseChildOrderPo.getSkuType())) {
            throw new ParamIllegalException("当前采购订单sku类型不是【】类型，无法进行快速补单操作！", SkuType.SM_SKU.getRemark());
        }

        final PurchaseParentOrderPo originPurchaseParentOrderPo = purchaseParentOrderDao.getOneByNo(originPurchaseChildOrderPo.getPurchaseParentOrderNo());
        Assert.notNull(originPurchaseParentOrderPo, () -> new ParamIllegalException("找不到对应的采购需求母单,获取子单详情失败!"));
        final PurchaseChildOrderItemPo originPurchaseChildOrderItemPo = purchaseChildOrderItemDao.getOneByChildOrderNo(dto.getPurchaseChildOrderNo());
        Assert.notNull(originPurchaseChildOrderItemPo, () -> new ParamIllegalException("找不到对应的采购子单,获取子单详情失败!"));
        // 判断网红仓
        if (PurchaseOrderType.WH.equals(originPurchaseChildOrderPo.getPurchaseOrderType()) && !ScmWarehouseUtil.WH_WAREHOUSE_CODE.equals(dto.getWarehouseCode())) {
            throw new ParamIllegalException("采购需求类型为网红时，只能选择网红仓创建，请重新选择仓库后再提交！");
        }
        if (PurchaseOrderType.NORMAL.equals(originPurchaseChildOrderPo.getPurchaseOrderType()) && ScmWarehouseUtil.WH_WAREHOUSE_CODE.equals(dto.getWarehouseCode())) {
            throw new ParamIllegalException("采购需求类型为常规时，不能选择网红仓创建，请重新选择仓库后再提交！");
        }
        if (dto.getPurchaseCnt() <= 0) {
            throw new ParamIllegalException("采购数必须大于0，请重新输入后再提交！");
        }

        final String sku = originPurchaseChildOrderItemPo.getSku();
        PurchaseParentOrderPo purchaseParentOrderPo = new PurchaseParentOrderPo();
        final String purchaseParentOrderNo = idGenerateService.getConfuseCode(ScmConstant.PURCHASE_PARENT_NO_PREFIX, TimeType.CN_DAY_YYYY, ConfuseLength.L_4);
        purchaseParentOrderPo.setPurchaseParentOrderNo(purchaseParentOrderNo);
        purchaseParentOrderPo.setSkuType(originPurchaseParentOrderPo.getSkuType());
        purchaseParentOrderPo.setWarehouseCode(dto.getWarehouseCode());
        purchaseParentOrderPo.setWarehouseName(dto.getWarehouseName());
        purchaseParentOrderPo.setWarehouseTypes(String.join(",", dto.getWarehouseTypeList()));
        purchaseParentOrderPo.setPlatform(originPurchaseParentOrderPo.getPlatform());
        purchaseParentOrderPo.setPurchaseTotal(dto.getPurchaseCnt());
        purchaseParentOrderPo.setPurchaseParentOrderStatus(PurchaseParentOrderStatus.IN_PROGRESS);
        purchaseParentOrderPo.setSpu(plmRemoteService.getSpuBySku(sku));
        purchaseParentOrderPo.setUndeliveredCnt(dto.getPurchaseCnt());
        purchaseParentOrderPo.setIsDirectSend(BooleanType.FALSE);
        purchaseParentOrderPo.setSupplierCode(originPurchaseParentOrderPo.getSupplierCode());
        purchaseParentOrderPo.setSupplierName(originPurchaseParentOrderPo.getSupplierName());
        purchaseParentOrderPo.setPurchaseDemandType(originPurchaseParentOrderPo.getPurchaseDemandType());
        purchaseParentOrderPo.setPlaceOrderUser(GlobalContext.getUserKey());
        purchaseParentOrderPo.setPlaceOrderUsername(GlobalContext.getUsername());
        purchaseParentOrderDao.insert(purchaseParentOrderPo);

        PurchaseParentOrderChangePo purchaseParentOrderChangePo = PurchaseConverter.parentPoToChangePo(purchaseParentOrderPo);
        purchaseParentOrderChangePo.setReceiveOrderTime(LocalDateTime.now());
        purchaseParentOrderChangeDao.insert(purchaseParentOrderChangePo);

        final PurchaseParentOrderItemPo purchaseParentOrderItemPo = new PurchaseParentOrderItemPo();
        purchaseParentOrderItemPo.setPurchaseParentOrderNo(purchaseParentOrderNo);
        purchaseParentOrderItemPo.setSku(sku);
        purchaseParentOrderItemPo.setPurchaseCnt(dto.getPurchaseCnt());
        purchaseParentOrderItemPo.setUndeliveredCnt(dto.getPurchaseCnt());
        purchaseParentOrderItemDao.insert(purchaseParentOrderItemPo);

        final SkuInfoPo skuInfoPo = skuInfoDao.getBySku(sku);
        final String purchaseChildOrderNo = purchaseBaseService.getLatestPurchaseChildNo(purchaseParentOrderNo);
        PurchaseChildOrderPo purchaseChildOrderPo = new PurchaseChildOrderPo();
        purchaseChildOrderPo.setPromiseDateChg(BooleanType.FALSE);
        purchaseChildOrderPo.setPurchaseParentOrderNo(purchaseParentOrderPo.getPurchaseParentOrderNo());
        purchaseChildOrderPo.setPurchaseChildOrderNo(purchaseChildOrderNo);
        purchaseChildOrderPo.setPurchaseOrderStatus(PurchaseOrderStatus.WAIT_DELIVER);
        purchaseChildOrderPo.setPurchaseOrderType(originPurchaseChildOrderPo.getPurchaseOrderType());
        purchaseChildOrderPo.setSpu(purchaseParentOrderPo.getSpu());
        purchaseChildOrderPo.setPlatform(originPurchaseChildOrderPo.getPlatform());
        purchaseChildOrderPo.setPurchaseBizType(PurchaseBizType.PRODUCT);
        purchaseChildOrderPo.setExpectedOnShelvesDate(originPurchaseChildOrderPo.getExpectedOnShelvesDate());
        purchaseChildOrderPo.setIsUploadOverseasMsg(BooleanType.FALSE);
        purchaseChildOrderPo.setSkuCnt(1);
        purchaseChildOrderPo.setPurchaseTotal(dto.getPurchaseCnt());
        purchaseChildOrderPo.setRawRemainTab(BooleanType.TRUE);
        purchaseChildOrderPo.setIsOverdue(BooleanType.FALSE);
        purchaseChildOrderPo.setShippableCnt(dto.getPurchaseCnt());
        purchaseChildOrderPo.setWarehouseCode(dto.getWarehouseCode());
        purchaseChildOrderPo.setWarehouseName(dto.getWarehouseName());
        purchaseChildOrderPo.setWarehouseTypes(String.join(",", dto.getWarehouseTypeList()));
        purchaseChildOrderPo.setSkuType(originPurchaseChildOrderPo.getSkuType());
        purchaseChildOrderPo.setDeliverDate(originPurchaseChildOrderPo.getDeliverDate());
        purchaseChildOrderPo.setTotalSettlePrice(dto.getSettlePrice().multiply(new BigDecimal(dto.getPurchaseCnt())));
        purchaseChildOrderPo.setIsUrgentOrder(originPurchaseChildOrderPo.getIsUrgentOrder());
        purchaseChildOrderPo.setIsNormalOrder(originPurchaseChildOrderPo.getIsNormalOrder());
        purchaseChildOrderPo.setSupplierCode(originPurchaseChildOrderPo.getSupplierCode());
        purchaseChildOrderPo.setSupplierName(originPurchaseChildOrderPo.getSupplierName());
        purchaseChildOrderPo.setIsDirectSend(originPurchaseChildOrderPo.getIsDirectSend());
        purchaseChildOrderPo.setPromiseDate(originPurchaseChildOrderPo.getPromiseDate());
        purchaseChildOrderPo.setPlaceOrderUser(GlobalContext.getUserKey());
        purchaseChildOrderPo.setPlaceOrderUsername(GlobalContext.getUsername());
        purchaseChildOrderPo.setOrderSource(OrderSource.SCM);
        BigDecimal singleCapacity;
        if (null == skuInfoPo) {
            singleCapacity = BigDecimal.ZERO;
            log.info("sku:{}没有配置sku_info信息", sku);
        } else {
            singleCapacity = skuInfoPo.getSingleCapacity();
        }
        final BigDecimal capacity = singleCapacity.multiply(new BigDecimal(dto.getPurchaseCnt()))
                .setScale(2, RoundingMode.HALF_UP);
        purchaseChildOrderPo.setCapacity(capacity);
        purchaseChildOrderDao.insert(purchaseChildOrderPo);
        // 创建单据后推送单号给wms
        final PurchaseChildNoMqDto purchaseChildNoMqDto = new PurchaseChildNoMqDto();
        purchaseChildNoMqDto.setPurchaseChildOrderNoList(Collections.singletonList(purchaseChildOrderNo));
        consistencySendMqService.execSendMq(PurchaseChangeHandler.class, purchaseChildNoMqDto);


        // 产能扣减
        final LocalDate capacityDate = purchaseBaseService.getCapacityDate(purchaseChildOrderPo.getPromiseDate(), purchaseChildOrderPo.getSupplierCode());
        final SupOpCapacityBo supOpCapacityBo = new SupOpCapacityBo();
        supOpCapacityBo.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
        supOpCapacityBo.setOperateDate(capacityDate);
        supOpCapacityBo.setOperateValue(capacity.negate());
        supOpCapacityBo.setBizNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        supplierCapacityRefService.operateSupplierCapacityBatch(Collections.singletonList(supOpCapacityBo));
        // 产能变更日志
        logBaseService.purchaseCapacityLog(LogBizModule.PURCHASE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseChildOrderPo, purchaseChildOrderPo.getCapacity());

        // 生成批次码
        final SkuBatchCreateDto skuBatchCreateDto = new SkuBatchCreateDto();
        skuBatchCreateDto.setPurchaseChildOrderNo(purchaseChildOrderNo);
        skuBatchCreateDto.setSkuCodeList(Collections.singletonList(sku));
        skuBatchCreateDto.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
        skuBatchCreateDto.setSupplierName(purchaseChildOrderPo.getSupplierName());
        final Map<String, String> skuBatchMap = wmsRemoteService.batchCreateBatchCode(skuBatchCreateDto);

        final PurchaseChildOrderItemPo purchaseChildOrderItemPo = new PurchaseChildOrderItemPo();
        purchaseChildOrderItemPo.setPurchaseChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        purchaseChildOrderItemPo.setPurchaseParentOrderNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
        purchaseChildOrderItemPo.setSku(sku);
        purchaseChildOrderItemPo.setPurchaseCnt(dto.getPurchaseCnt());
        purchaseChildOrderItemPo.setInitPurchaseCnt(dto.getPurchaseCnt());
        purchaseChildOrderItemPo.setUndeliveredCnt(dto.getPurchaseCnt());
        purchaseChildOrderItemPo.setSettlePrice(dto.getSettlePrice());
        purchaseChildOrderItemPo.setPurchasePrice(dto.getSettlePrice());
        purchaseChildOrderItemPo.setDiscountType(DiscountType.NO_DISCOUNT);
        purchaseChildOrderItemPo.setSkuBatchCode(skuBatchMap.get(sku));
        purchaseChildOrderItemDao.insert(purchaseChildOrderItemPo);

        final PurchaseChildOrderChangePo purchaseChildOrderChangePo = new PurchaseChildOrderChangePo();
        purchaseChildOrderChangePo.setPurchaseChildOrderId(purchaseChildOrderPo.getPurchaseChildOrderId());
        purchaseChildOrderChangePo.setPurchaseChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        purchaseChildOrderChangePo.setPlaceOrderTime(originPurchaseChildOrderChangePo.getPlaceOrderTime());
        purchaseChildOrderChangePo.setReceiveOrderTime(LocalDateTime.now());
        purchaseChildOrderChangePo.setReceiveOrderUser(GlobalContext.getUserKey());
        purchaseChildOrderChangePo.setReceiveOrderUsername(GlobalContext.getUsername());
        purchaseChildOrderChangeDao.insert(purchaseChildOrderChangePo);

        // 日志
        logBaseService.simpleLog(LogBizModule.PURCHASE_PARENT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION, purchaseParentOrderPo.getPurchaseParentOrderNo(), PurchaseParentOrderStatus.IN_PROGRESS.getRemark(), Collections.emptyList());
        logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION, purchaseChildOrderPo.getPurchaseChildOrderNo(), PurchaseOrderStatus.WAIT_DELIVER.getRemark(), Collections.emptyList());
    }

    public QcCheckVo qcCheck(QcCheckRequestDto dto) {
        QcCheckVo qcCheckVo = new QcCheckVo();

        String purchaseChildOrderNo = dto.getPurchaseChildOrderNo().trim();
        List<PurchaseOrderStatus> canCreateQcStatus = Arrays.asList(PurchaseOrderStatus.WAIT_SCHEDULING, PurchaseOrderStatus.WAIT_COMMISSIONING, PurchaseOrderStatus.COMMISSION, PurchaseOrderStatus.PRETREATMENT, PurchaseOrderStatus.SEWING, PurchaseOrderStatus.AFTER_TREATMENT, PurchaseOrderStatus.POST_QC, PurchaseOrderStatus.WAIT_DELIVER, PurchaseOrderStatus.WAIT_RECEIPT, PurchaseOrderStatus.RECEIPTED, PurchaseOrderStatus.WAIT_QC, PurchaseOrderStatus.WAIT_WAREHOUSING, PurchaseOrderStatus.WAREHOUSED, PurchaseOrderStatus.RETURN);

        List<PurchaseChildOrderPo> purchaseChildOrderPos = purchaseChildOrderDao.queryPurchaseChildOrders(purchaseChildOrderNo, canCreateQcStatus);
        if (CollectionUtils.isEmpty(purchaseChildOrderPos)) {
            return qcCheckVo;
        }
        PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderPos.stream().findFirst().orElse(null);
        if (Objects.isNull(purchaseChildOrderPo)) {
            return qcCheckVo;
        }

        qcCheckVo.setPurchaseChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        qcCheckVo.setQcOrigin(QcOrigin.REWORK_RESIDENT);

        PurchaseOrderType purchaseOrderType = purchaseChildOrderPo.getPurchaseOrderType();
        QcOriginProperty qcOriginProperty = qcOrderRefService.mapPurchaseOrderTypeToQcOriginProperty(purchaseOrderType);
        qcCheckVo.setQcOriginProperty(qcOriginProperty);
        return qcCheckVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void adjustPrice(PurchaseAdjustPriceDto dto) {
        final List<PurchaseAdjustPriceItemDto> purchaseAdjustPriceItemList = dto.getPurchaseAdjustPriceItemList();
        purchaseAdjustPriceItemList.forEach(itemDto -> {
            if (itemDto.getAdjustPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ParamIllegalException("采购订单:{}调整后价格小于0，请重新填写后再提交！", itemDto.getPurchaseChildOrderNo());
            }
        });

        final List<String> purchaseChildOrderNoList = purchaseAdjustPriceItemList.stream()
                .map(PurchaseAdjustPriceItemDto::getPurchaseChildOrderNo)
                .collect(Collectors.toList());
        final List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverBaseService.getDeliverPoByPurchaseNo(purchaseChildOrderNoList);
        purchaseDeliverOrderPoList.forEach(po -> {
            if (!DeliverOrderStatus.DELETED.equals(po.getDeliverOrderStatus())) {
                throw new ParamIllegalException("采购单：{}已生成发货单:{}，无法发起调价审批!",
                        po.getPurchaseChildOrderNo(), po.getPurchaseDeliverOrderNo());
            }
        });

        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList);
        if (purchaseChildOrderPoList.size() != purchaseChildOrderNoList.size()) {
            throw new ParamIllegalException("部分采购订单不存在，请刷新后重试！");
        }
        purchaseChildOrderPoList.forEach(po -> {
            if (po.getPurchaseOrderStatus().getSort() < PurchaseOrderStatus.WAIT_RECEIVE_ORDER.getSort()
                    || po.getPurchaseOrderStatus().getSort() > PurchaseOrderStatus.WAIT_DELIVER.getSort()) {
                throw new ParamIllegalException("当前采购订单:{}，不处于{} ~ {}状态，无法发起调价审批，请刷新后重试！",
                        po.getPurchaseChildOrderNo(), PurchaseOrderStatus.WAIT_RECEIVE_ORDER.getRemark(),
                        PurchaseOrderStatus.WAIT_DELIVER.getRemark());
            }
            if (StringUtils.isNotBlank(po.getAdjustPriceApproveNo())) {
                throw new ParamIllegalException("采购订单：{}已发起调价审批，无法重复发起！", po.getPurchaseChildOrderNo());
            }
        });
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNoList(purchaseChildOrderNoList);

        final Map<String, List<PurchaseChildOrderPo>> supplierCodePurchaseListMap = purchaseChildOrderPoList.stream()
                .collect(Collectors.groupingBy(PurchaseChildOrderPo::getSupplierCode));
        final Map<String, PurchaseAdjustPriceItemDto> purchaseChildOrderNoItemDtoMap = purchaseAdjustPriceItemList.stream()
                .collect(Collectors.toMap(PurchaseAdjustPriceItemDto::getPurchaseChildOrderNo, Function.identity()));
        final Map<String, PurchaseChildOrderItemPo> purchaseChildOrderNoItemPoMap = purchaseChildOrderItemPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderItemPo::getPurchaseChildOrderNo,
                        Function.identity(), (item1, item2) -> item1));

        final List<String> platCodeList = purchaseChildOrderPoList.stream()
                .map(PurchaseChildOrderPo::getPlatform).
                distinct()
                .collect(Collectors.toList());
        final Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);
        final List<String> skuList = purchaseChildOrderItemPoList.stream()
                .map(PurchaseChildOrderItemPo::getSku)
                .distinct()
                .collect(Collectors.toList());

        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        supplierCodePurchaseListMap.forEach((supplierCode, purchaseChildPoList) -> {
            final AdjustApproveBo adjustApproveBo = new AdjustApproveBo();
            adjustApproveBo.setSupplierCode(supplierCode);
            adjustApproveBo.setApproveType(ApproveType.PURCHASE_ADJUST);
            adjustApproveBo.setApproveUser(scmAdjustProp.getPurchaseApproveUser());
            adjustApproveBo.setApproveUsername(scmAdjustProp.getPurchaseApproveUsername());
            adjustApproveBo.setNodeApproverUserCodeList(Collections.singletonList(scmAdjustProp.getPurchaseApproveUser()));

            final List<OrderAdjustDetailItemBo> orderAdjustDetailItemBoList = purchaseChildPoList.stream().map(purchaseChildPo -> {
                final PurchaseAdjustPriceItemDto itemDto = purchaseChildOrderNoItemDtoMap.get(purchaseChildPo.getPurchaseChildOrderNo());
                final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildOrderNoItemPoMap.get(purchaseChildPo.getPurchaseChildOrderNo());

                final OrderAdjustDetailItemBo orderAdjustDetailItemBo = new OrderAdjustDetailItemBo();
                orderAdjustDetailItemBo.setSku(purchaseChildOrderItemPo.getSku());
                orderAdjustDetailItemBo.setSkuMsg("SKU: " + purchaseChildOrderItemPo.getSku() + "\n产品名称: " + skuEncodeMap.get(purchaseChildOrderItemPo.getSku()));
                orderAdjustDetailItemBo.setPurchaseMsg("订单号: " + purchaseChildOrderItemPo.getPurchaseChildOrderNo()
                        + "\n采购数: " + purchaseChildOrderItemPo.getPurchaseCnt()
                        + "\n平台: " + platCodeNameMap.get(purchaseChildPo.getPlatform()));
                orderAdjustDetailItemBo.setPurchaseChildOrderNo(purchaseChildOrderItemPo.getPurchaseChildOrderNo());
                orderAdjustDetailItemBo.setPurchaseCnt(purchaseChildOrderItemPo.getPurchaseCnt());
                orderAdjustDetailItemBo.setPlatform(purchaseChildPo.getPlatform());
                orderAdjustDetailItemBo.setOriginalPrice(purchaseChildOrderItemPo.getSettlePrice());
                orderAdjustDetailItemBo.setOrderAdjust(itemDto.getAdjustPrice());
                orderAdjustDetailItemBo.setOrderAdjustStr(ScmFormatUtil.convertToThousandFormat(itemDto.getAdjustPrice()));
                orderAdjustDetailItemBo.setAdjustReason(itemDto.getAdjustReason());
                orderAdjustDetailItemBo.setRemark(itemDto.getAdjustRemark());
                return orderAdjustDetailItemBo;
            }).collect(Collectors.toList());
            adjustApproveBo.setOrderAdjustList(orderAdjustDetailItemBoList);

            final AdjustPriceApprovePo adjustPriceApprovePo = adjustApproveBaseService.submitForApproval(adjustApproveBo);
            purchaseChildPoList.forEach(purchaseChildPo -> purchaseChildPo.setAdjustPriceApproveNo(adjustPriceApprovePo.getAdjustPriceApproveNo()));
        });

        purchaseChildOrderDao.updateBatchByIdVersion(purchaseChildOrderPoList);
    }

    public List<PurchaseDefaultPriceItemVo> getDefaultPrice(PurchaseDefaultPriceDto dto) {
        final List<PurchaseDefaultPriceItemDto> purchaseDefaultPriceItemList = dto.getPurchaseDefaultPriceItemList();
        // 以sku+供应商为维度，默认去寻同母单下的第一个采购订单的结算单价
        final List<PurchaseDefaultPriceItemVo> purchaseDefaultPriceItemVoList = purchaseChildOrderDao.getDefaultPriceByNoList(purchaseDefaultPriceItemList);
        final Map<String, PurchaseDefaultPriceItemVo> purchaseChildOrderNoVoMap = purchaseDefaultPriceItemVoList.stream()
                .collect(Collectors.toMap(vo -> vo.getPurchaseParentOrderNo() + vo.getSupplierCode(), Function.identity(),
                        (item1, item2) -> item1));
        // 获取采购子单信息
        List<String> purchaseChildOrderNoList = purchaseDefaultPriceItemList.stream()
                .map(PurchaseDefaultPriceItemDto::getPurchaseChildOrderNo)
                .distinct().collect(Collectors.toList());
        final Map<String, PurchaseChildOrderPo> purchaseChildOrderPoMap = purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList).stream()
                .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, purchaseChildOrderPo -> purchaseChildOrderPo));

        List<GoodsPriceGetPurchaseListDto.SkuUniversalDto> channelPriceItemDtoList = new ArrayList<>();
        final List<PurchaseDefaultPriceItemVo> resultPriceItemVoList = new ArrayList<>();
        purchaseDefaultPriceItemList.forEach(itemDto -> {
            final PurchaseDefaultPriceItemVo purchaseDefaultPriceItemVo = purchaseChildOrderNoVoMap.get(itemDto.getPurchaseParentOrderNo() + itemDto.getSupplierCode());
            if (null == purchaseDefaultPriceItemVo || null == purchaseDefaultPriceItemVo.getSettlePrice()
                    || BigDecimal.ZERO.compareTo(purchaseDefaultPriceItemVo.getSettlePrice()) == 0) {
                final GoodsPriceGetPurchaseListDto.SkuUniversalDto skuUniversalDto = new GoodsPriceGetPurchaseListDto.SkuUniversalDto();
                skuUniversalDto.setSku(itemDto.getSku());
                PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderPoMap.get(itemDto.getPurchaseChildOrderNo());
                if (null == purchaseChildOrderPo) {
                    throw new BizException("采购子单号：{}关联不到对应的信息，请联系系统管理员！", itemDto.getPurchaseChildOrderNo());
                }
                if (StringUtils.isBlank(purchaseChildOrderPo.getSupplierCode())) {
                    throw new BizException("采购子单号：{}关联不到对应的供应商，请联系系统管理员！", purchaseChildOrderPo.getPurchaseChildOrderNo());
                }
                skuUniversalDto.setSupplierCode(purchaseChildOrderPo.getSupplierCode());

                skuUniversalDto.setGoodsPriceUniversal(GoodsPriceUniversal.UNIVERSAL);
                channelPriceItemDtoList.add(skuUniversalDto);

                final PurchaseDefaultPriceItemVo resultPriceItemVo = new PurchaseDefaultPriceItemVo();
                resultPriceItemVo.setPurchaseChildOrderNo(itemDto.getPurchaseChildOrderNo());
                resultPriceItemVo.setSettlePrice(BigDecimal.ZERO);
                resultPriceItemVo.setSku(itemDto.getSku());
                resultPriceItemVo.setSupplierCode(itemDto.getSupplierCode());
                resultPriceItemVoList.add(resultPriceItemVo);
            } else {
                final PurchaseDefaultPriceItemVo resultPriceItemVo = new PurchaseDefaultPriceItemVo();
                resultPriceItemVo.setPurchaseChildOrderNo(itemDto.getPurchaseChildOrderNo());
                resultPriceItemVo.setSettlePrice(purchaseDefaultPriceItemVo.getSettlePrice());
                resultPriceItemVo.setSku(purchaseDefaultPriceItemVo.getSku());
                resultPriceItemVo.setSupplierCode(purchaseDefaultPriceItemVo.getSupplierCode());
                resultPriceItemVoList.add(resultPriceItemVo);
            }
        });

        // 以sku维度获取通用渠道价格，为0或空默认为0，允许编辑价格
        if (CollectionUtils.isNotEmpty(channelPriceItemDtoList)) {
            final GoodsPriceGetPurchaseListDto goodsPriceGetPurchaseListDto = new GoodsPriceGetPurchaseListDto();
            goodsPriceGetPurchaseListDto.setSkuUniversalList(channelPriceItemDtoList);
            final List<GoodsPriceGetPurchaseListVo> goodsPricePurchaseList = goodsPriceDao.getGoodsPricePurchaseList(goodsPriceGetPurchaseListDto);
            final Map<String, BigDecimal> skuChannelPriceMap = goodsPricePurchaseList.stream()
                    .collect(Collectors.toMap(vo -> vo.getSku() + vo.getSupplierCode(), GoodsPriceGetPurchaseListVo::getChannelPrice,
                            (item1, item2) -> item1));

            resultPriceItemVoList.stream()
                    .filter(vo -> BigDecimal.ZERO.compareTo(vo.getSettlePrice()) == 0)
                    .forEach(vo -> vo.setSettlePrice(skuChannelPriceMap.getOrDefault(vo.getSku() + vo.getSupplierCode(), BigDecimal.ZERO)));
        }

        return resultPriceItemVoList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void wmsCreatePurchase(@NotNull @Valid PurchaseWmsCreateDto message) {
        final List<PurchaseWmsCreateItemDto> purchaseWmsCreateItemList = message.getPurchaseWmsCreateItemList();
        final Map<String, List<PurchaseWmsCreateItemDto>> itemDtoMap = purchaseWmsCreateItemList.stream()
                .collect(Collectors.groupingBy(itemDto -> itemDto.getSpu() + itemDto.getWarehouseCode()
                        + itemDto.getPlatform() + itemDto.getSupplierCode()));

        final List<String> skuList = purchaseWmsCreateItemList.stream()
                .map(PurchaseWmsCreateItemDto::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, SkuInfoPo> skuInfoPoMap = skuInfoDao.getMapBySkuList(skuList);
        final List<String> supplierCodeList = purchaseWmsCreateItemList.stream()
                .map(PurchaseWmsCreateItemDto::getSupplierCode)
                .distinct()
                .collect(Collectors.toList());
        // 获取sku已经生成的采购子单
        final Map<String, List<PurchaseChildOrderItemPo>> skuChildItemMap = purchaseBaseService.getChildItemMapBySkuList(skuList);
        final List<SupplierPo> supplierPoList = supplierBaseService.getBySupplierCodeList(supplierCodeList);
        final Map<String, SupplierPo> supplierCodePoMap = supplierPoList.stream()
                .collect(Collectors.toMap(SupplierPo::getSupplierCode, Function.identity()));

        // 产能列表
        final List<SupOpCapacityBo> supOpCapacityList = new ArrayList<>();
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = new ArrayList<>();
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = new ArrayList<>();
        final List<PurchaseChildItemMqDto> purchaseChildItemMqList = new ArrayList<>();
        final List<String> purchaseChildOrderNoList = new ArrayList<>();

        itemDtoMap.forEach((key, valueList) -> {
            final PurchaseWmsCreateItemDto purchaseWmsCreateItemDto = valueList.get(0);
            final int purchaseTotal = valueList.stream()
                    .mapToInt(PurchaseWmsCreateItemDto::getPurchaseCnt)
                    .sum();
            PurchaseParentOrderPo purchaseParentOrderPo = new PurchaseParentOrderPo();
            final String purchaseParentOrderNo = idGenerateService.getConfuseCode(ScmConstant.PURCHASE_PARENT_NO_PREFIX, TimeType.CN_DAY_YYYY, ConfuseLength.L_4);
            purchaseParentOrderPo.setPurchaseParentOrderNo(purchaseParentOrderNo);
            purchaseParentOrderPo.setSkuType(purchaseWmsCreateItemDto.getSkuType());
            purchaseParentOrderPo.setWarehouseCode(purchaseWmsCreateItemDto.getWarehouseCode());
            purchaseParentOrderPo.setWarehouseName(purchaseWmsCreateItemDto.getWarehouseName());
            purchaseParentOrderPo.setWarehouseTypes(purchaseWmsCreateItemDto.getWarehouseTypes());
            purchaseParentOrderPo.setPlatform(purchaseWmsCreateItemDto.getPlatform());
            purchaseParentOrderPo.setOrderRemarks(purchaseWmsCreateItemDto.getOrderRemarks());
            purchaseParentOrderPo.setPurchaseTotal(purchaseTotal);
            purchaseParentOrderPo.setPurchaseParentOrderStatus(PurchaseParentOrderStatus.IN_PROGRESS);
            purchaseParentOrderPo.setSpu(purchaseWmsCreateItemDto.getSpu());
            purchaseParentOrderPo.setUndeliveredCnt(purchaseTotal);
            purchaseParentOrderPo.setIsDirectSend(BooleanType.FALSE);
            purchaseParentOrderPo.setSkuCnt(1);
            PurchaseDemandType purchaseDemandType;
            if (purchaseWmsCreateItemDto.getWarehouseCode().equals(ScmWarehouseUtil.WH_WAREHOUSE_CODE)) {
                purchaseDemandType = PurchaseDemandType.WH;
            } else {
                purchaseDemandType = PurchaseDemandType.NORMAL;
            }
            purchaseParentOrderPo.setPurchaseDemandType(purchaseDemandType);
            purchaseParentOrderPo.setPlaceOrderUser(purchaseWmsCreateItemDto.getPlaceOrderUser());
            purchaseParentOrderPo.setPlaceOrderUsername(purchaseWmsCreateItemDto.getPlaceOrderUsername());
            purchaseParentOrderDao.insert(purchaseParentOrderPo);
            PurchaseParentOrderChangePo purchaseParentOrderChangePo = PurchaseConverter.parentPoToChangePo(purchaseParentOrderPo);
            purchaseParentOrderChangeDao.insert(purchaseParentOrderChangePo);

            final PurchaseParentOrderItemPo purchaseParentOrderItemPo = new PurchaseParentOrderItemPo();
            purchaseParentOrderItemPo.setPurchaseParentOrderNo(purchaseParentOrderNo);
            purchaseParentOrderItemPo.setSku(purchaseWmsCreateItemDto.getSku());
            purchaseParentOrderItemPo.setPurchaseCnt(purchaseTotal);
            purchaseParentOrderItemPo.setUndeliveredCnt(purchaseTotal);
            purchaseParentOrderItemDao.insert(purchaseParentOrderItemPo);

            // 赋值采购订单号
            PurchaseChildOrderPo existPurchaseChildOrderPo = purchaseChildOrderDao.getLatestPurchaseChild(purchaseParentOrderNo);
            int indexNo = 0;
            if (null != existPurchaseChildOrderPo) {
                final String purchaseChildOrderNo = existPurchaseChildOrderPo.getPurchaseChildOrderNo();
                final String childOrderNoIndex = purchaseChildOrderNo.split("-")[1];
                indexNo = Integer.parseInt(childOrderNoIndex);
            }
            final List<PurchaseWmsCreateItemDto> sortedList = valueList.stream()
                    .sorted(Comparator.comparing(PurchaseWmsCreateItemDto::getExpectedOnShelvesDate))
                    .collect(Collectors.toList());
            for (PurchaseWmsCreateItemDto itemDto : sortedList) {
                itemDto.setPurchaseChildOrderNo(purchaseParentOrderNo + "-" + StringUtil.toTwoDigitFormat(++indexNo));
            }

            List<String> historySkuList = new ArrayList<>();
            valueList.stream()
                    .sorted(Comparator.comparing(PurchaseWmsCreateItemDto::getExpectedOnShelvesDate))
                    .forEach(value -> {
                        final SupplierPo supplierPo = supplierCodePoMap.get(value.getSupplierCode());
                        if (null == supplierPo) {
                            throw new BizException("不存在的供应商：{}，创建采购单失败！", value.getSupplierCode());
                        }

                        PurchaseChildOrderPo purchaseChildOrderPo = new PurchaseChildOrderPo();
                        purchaseChildOrderPo.setPromiseDateChg(BooleanType.FALSE);
                        purchaseChildOrderPo.setPurchaseParentOrderNo(purchaseParentOrderPo.getPurchaseParentOrderNo());
                        purchaseChildOrderPo.setPurchaseChildOrderNo(value.getPurchaseChildOrderNo());
                        purchaseChildOrderPo.setPurchaseOrderStatus(PurchaseOrderStatus.WAIT_CONFIRM);
                        if (PurchaseDemandType.WH.equals(purchaseParentOrderPo.getPurchaseDemandType())) {
                            purchaseChildOrderPo.setPurchaseOrderType(PurchaseOrderType.WH);
                        } else if (!historySkuList.contains(value.getSku()) && skuChildItemMap.get(value.getSku()) == null) {
                            purchaseChildOrderPo.setPurchaseOrderType(PurchaseOrderType.FIRST_ORDER);
                        } else {
                            purchaseChildOrderPo.setPurchaseOrderType(PurchaseOrderType.NORMAL);
                        }
                        historySkuList.add(value.getSku());
                        purchaseChildOrderPo.setSpu(purchaseParentOrderPo.getSpu());
                        purchaseChildOrderPo.setPlatform(value.getPlatform());
                        purchaseChildOrderPo.setPurchaseBizType(PurchaseBizType.NO_TYPE);
                        purchaseChildOrderPo.setOrderRemarks(value.getOrderRemarks());
                        purchaseChildOrderPo.setExpectedOnShelvesDate(ScmTimeUtil.getLastSecondTimeOfDayForTime(value.getExpectedOnShelvesDate()));
                        purchaseChildOrderPo.setIsUploadOverseasMsg(BooleanType.FALSE);
                        purchaseChildOrderPo.setSkuCnt(1);
                        purchaseChildOrderPo.setPurchaseTotal(value.getPurchaseCnt());
                        purchaseChildOrderPo.setRawRemainTab(BooleanType.TRUE);
                        purchaseChildOrderPo.setIsOverdue(BooleanType.FALSE);
                        purchaseChildOrderPo.setShippableCnt(value.getPurchaseCnt());
                        purchaseChildOrderPo.setWarehouseCode(value.getWarehouseCode());
                        purchaseChildOrderPo.setWarehouseName(value.getWarehouseName());
                        purchaseChildOrderPo.setWarehouseTypes(value.getWarehouseTypes());
                        purchaseChildOrderPo.setSkuType(value.getSkuType());
                        purchaseChildOrderPo.setSplitType(SplitType.SUGGEST_SPLIT);
                        purchaseChildOrderPo.setPromiseDate(value.getExpectedOnShelvesDate());
                        purchaseChildOrderPo.setPlaceOrderUser(value.getPlaceOrderUser());
                        purchaseChildOrderPo.setPlaceOrderUsername(value.getPlaceOrderUsername());
                        purchaseChildOrderPo.setOrderSource(value.getOrderSource());
                        purchaseChildOrderPo.setIsUrgentOrder(value.getIsUrgentOrder());
                        purchaseChildOrderPo.setSupplierCode(supplierPo.getSupplierCode());
                        purchaseChildOrderPo.setSupplierName(supplierPo.getSupplierName());
                        BigDecimal singleCapacity;
                        final SkuInfoPo skuInfoPo = skuInfoPoMap.get(value.getSku());
                        if (null == skuInfoPo) {
                            singleCapacity = BigDecimal.ZERO;
                            log.info("sku:{}没有配置sku_info信息", value.getSku());
                        } else {
                            singleCapacity = skuInfoPo.getSingleCapacity();
                        }
                        final BigDecimal capacity = singleCapacity.multiply(new BigDecimal(value.getPurchaseCnt()))
                                .setScale(2, RoundingMode.HALF_UP);
                        purchaseChildOrderPo.setCapacity(capacity);
                        purchaseChildOrderPoList.add(purchaseChildOrderPo);

                        final PurchaseChildOrderItemPo purchaseChildOrderItemPo = new PurchaseChildOrderItemPo();
                        purchaseChildOrderItemPo.setPurchaseChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
                        purchaseChildOrderItemPo.setPurchaseParentOrderNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
                        purchaseChildOrderItemPo.setSku(value.getSku());
                        purchaseChildOrderItemPo.setPurchaseCnt(value.getPurchaseCnt());
                        purchaseChildOrderItemPo.setInitPurchaseCnt(value.getPurchaseCnt());
                        purchaseChildOrderItemPo.setUndeliveredCnt(value.getPurchaseCnt());
                        purchaseChildOrderItemPoList.add(purchaseChildOrderItemPo);

                        // 扣减供应商对应产能
                        final LocalDate capacityDate = purchaseBaseService.getCapacityDate(purchaseChildOrderPo.getPromiseDate(), value.getSupplierCode());
                        final SupOpCapacityBo supOpCapacityBo = new SupOpCapacityBo();
                        supOpCapacityBo.setSupplierCode(value.getSupplierCode());
                        supOpCapacityBo.setOperateDate(capacityDate);
                        supOpCapacityBo.setOperateValue(capacity.negate());
                        supOpCapacityBo.setBizNo(value.getPurchaseChildOrderNo());
                        supOpCapacityList.add(supOpCapacityBo);
                        // 产能变更日志
                        logBaseService.purchaseCapacityLog(LogBizModule.PURCHASE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                                purchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseChildOrderPo, purchaseChildOrderPo.getCapacity());

                        final PurchaseChildItemMqDto purchaseChildItemMqDto = new PurchaseChildItemMqDto();
                        purchaseChildItemMqDto.setPurchaseChildOrderNo(value.getPurchaseChildOrderNo());
                        purchaseChildItemMqDto.setDaySaleAmount(value.getDaySaleAmount());
                        purchaseChildItemMqDto.setCanSaleAmount(value.getCanSaleAmount());
                        purchaseChildItemMqList.add(purchaseChildItemMqDto);
                        purchaseChildOrderNoList.add(value.getPurchaseChildOrderNo());
                    });
        });

        // 创建单据后推送单号给wms
        final PurchaseChildNoMqDto purchaseChildNoMqDto = new PurchaseChildNoMqDto();
        purchaseChildNoMqDto.setPurchaseChildItemMqList(purchaseChildItemMqList);
        purchaseChildNoMqDto.setPurchaseChildOrderNoList(purchaseChildOrderNoList);
        consistencySendMqService.execSendMq(PurchaseChangeHandler.class, purchaseChildNoMqDto);

        purchaseChildOrderDao.insertBatch(purchaseChildOrderPoList);
        final List<PurchaseChildOrderChangePo> purchaseChildOrderChangePoList = PurchaseConverter.childPoToChangePo(purchaseChildOrderPoList);
        purchaseChildOrderChangeDao.insertBatch(purchaseChildOrderChangePoList);
        purchaseChildOrderItemDao.insertBatch(purchaseChildOrderItemPoList);

        supplierCapacityRefService.operateSupplierCapacityBatch(supOpCapacityList);
    }

    public List<InventoryVo> getAvailableInventory(PurchaseAIQDto dto) {
        List<InventoryVo> resultList = wmsRemoteService.getAvailableInventory(dto);

        if (StringUtils.isNotBlank(dto.getSupplierCode())) {
            resultList = resultList.stream()
                    .filter(vo -> vo.getSupplierCode()
                            .equals(dto.getSupplierCode()))
                    .collect(Collectors.toList());
        }

        if (StringUtils.isNotBlank(dto.getWarehouseAreaCode())) {
            resultList = resultList.stream()
                    .filter(vo -> vo.getWarehouseAreaCode()
                            .equals(dto.getWarehouseAreaCode()))
                    .collect(Collectors.toList());
        }

        if (StringUtils.isNotBlank(dto.getBatchCode())) {
            resultList = resultList.stream()
                    .filter(vo -> vo.getBatchCode()
                            .equals(dto.getBatchCode()))
                    .collect(Collectors.toList());
        }

        return resultList;
    }

    /**
     * 采购子单的BOM原料导出
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/11/27 13:52
     */
    @Transactional(rollbackFor = Exception.class)
    public void exportPurchaseChildBomRaw(PurchaseProductSearchDto dto) {
        if (null == purchaseBaseService.getSearchPurchaseChildWhere(dto)) {
            throw new ParamIllegalException("导出数据为空");
        }
        Integer exportTotals = purchaseChildOrderDao.getRawSkuChildExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                FileOperateBizType.SCM_PURCHASE_SKU_RAW_EXPORT.getCode(), dto));

    }
}
