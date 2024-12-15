package com.hete.supply.scm.server.supplier.service.biz;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseReturnDto;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseSkuCntVo;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.dao.SkuInfoDao;
import com.hete.supply.scm.server.scm.entity.po.SkuInfoPo;
import com.hete.supply.scm.server.scm.ibfs.service.base.RecoOrderBaseService;
import com.hete.supply.scm.server.scm.purchase.converter.PurchaseConverter;
import com.hete.supply.scm.server.scm.purchase.dao.*;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseChildNoMqDto;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseSkuSupplierDto;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseSkuSupplierItemDto;
import com.hete.supply.scm.server.scm.purchase.entity.po.*;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseReturnSearchVo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseSkuSupplierItemVo;
import com.hete.supply.scm.server.scm.purchase.handler.PurchaseChangeHandler;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseBaseService;
import com.hete.supply.scm.server.scm.purchase.service.ref.PurchaseRefService;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierProductCompareDao;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupOpCapacityBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierUrgentStatus;
import com.hete.supply.scm.server.scm.supplier.service.ref.SupplierCapacityRefService;
import com.hete.supply.scm.server.supplier.converter.SupplierReturnConverter;
import com.hete.supply.scm.server.supplier.entity.bo.ReturnOrderBo;
import com.hete.supply.scm.server.supplier.entity.dto.ReceiveOrderRejectMqDto;
import com.hete.supply.scm.server.supplier.entity.dto.ReturnOrderEventMqDto;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.scm.server.supplier.purchase.converter.SupplierPurchaseConverter;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.*;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseReturnDetailVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseReturnPrintVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseReturnVo;
import com.hete.supply.scm.server.supplier.service.ref.ReturnRefService;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.basic.entity.dto.SkuBatchCreateDto;
import com.hete.supply.wms.api.basic.entity.vo.WarehouseVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/20 16:09
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierReturnBizService {
    private final PurchaseReturnOrderDao purchaseReturnOrderDao;
    private final PurchaseReturnOrderItemDao purchaseReturnOrderItemDao;
    private final LogBaseService logBaseService;
    private final SupplierProductCompareDao supplierProductCompareDao;
    private final PlmRemoteService plmRemoteService;
    private final PurchaseRefService purchaseRefService;
    private final ReturnRefService returnRefService;
    private final ConsistencySendMqService consistencySendMqService;
    private final RecoOrderBaseService recoOrderBaseService;
    private final WmsRemoteService wmsRemoteService;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final PurchaseParentOrderDao purchaseParentOrderDao;
    private final IdGenerateService idGenerateService;
    private final PurchaseParentOrderChangeDao purchaseParentOrderChangeDao;
    private final PurchaseChildOrderChangeDao purchaseChildOrderChangeDao;
    private final PurchaseParentOrderItemDao purchaseParentOrderItemDao;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;
    private final PurchaseBaseService purchaseBaseService;
    private final SkuInfoDao skuInfoDao;
    private final SupplierCapacityRefService supplierCapacityRefService;


    /**
     * 退货单列表查询
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<PurchaseReturnVo> purchaseReturnList(PurchaseReturnDto dto) {

        IPage<PurchaseReturnSearchVo> pageResult = purchaseReturnOrderDao.searchPurchaseReturnPage(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<PurchaseReturnSearchVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new CommonPageResult.PageInfo<>();
        }

        final List<String> recordsReturnNoList = records.stream()
                .map(PurchaseReturnSearchVo::getReturnOrderNo)
                .collect(Collectors.toList());
        final List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList = purchaseReturnOrderItemDao.getListByReturnNoList(recordsReturnNoList);
        final Map<String, List<PurchaseReturnOrderItemPo>> returnNoItemListMap = purchaseReturnOrderItemPoList.stream()
                .collect(Collectors.groupingBy(PurchaseReturnOrderItemPo::getReturnOrderNo));
        // 获取批次码结算单价
        Map<String, BigDecimal> purchaseReturnSettlePriceMap = recoOrderBaseService.getPurchaseReturnSettlePrice(recordsReturnNoList);

        // 采购信息
        final List<String> purchaseChildOrderNoList = records.stream()
                .map(PurchaseReturnSearchVo::getPurchaseChildOrderNo).distinct()
                .collect(Collectors.toList());
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList);
        final Map<String, PurchaseChildOrderPo> purchaseChildOrderNoPoMap = purchaseChildOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));

        //组装数据获取供应商产品名称
        final List<String> skuList = purchaseReturnOrderItemPoList.stream().map(PurchaseReturnOrderItemPo::getSku).collect(Collectors.toList());
        Map<String, SupplierProductComparePo> supplierProductCompareMap = supplierProductCompareDao.getBatchSkuMap(skuList);
        Map<String, PurchaseReturnSearchVo> purchaseReturnOrderVoMap = records.stream().collect(Collectors.toMap(PurchaseReturnSearchVo::getReturnOrderNo, purchaseReturnSearchVo -> purchaseReturnSearchVo));

        final Map<String, List<PurchaseSkuCntVo>> returnNoMap = Optional.of(purchaseReturnOrderItemPoList)
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.groupingBy(PurchaseReturnOrderItemPo::getReturnOrderNo, Collectors.mapping(item -> {
                    final PurchaseSkuCntVo purchaseSkuCntVo = new PurchaseSkuCntVo();
                    purchaseSkuCntVo.setSkuCode(item.getSku());
                    purchaseSkuCntVo.setSkuBatchCode(item.getSkuBatchCode());
                    purchaseSkuCntVo.setSkuCnt(item.getRealityReturnCnt());
                    purchaseSkuCntVo.setSkuEncode(item.getSkuEncode());
                    purchaseSkuCntVo.setRealityReturnCnt(item.getRealityReturnCnt());
                    PurchaseReturnSearchVo purchaseReturnSearchVo = purchaseReturnOrderVoMap.get(item.getReturnOrderNo());
                    if (null != purchaseReturnSearchVo) {
                        SupplierProductComparePo supplierProductComparePo = supplierProductCompareMap.get(purchaseReturnSearchVo.getSupplierCode() + item.getSku());
                        if (null != supplierProductComparePo) {
                            purchaseSkuCntVo.setSupplierProductName(supplierProductComparePo.getSupplierProductName());
                        }
                    }
                    BigDecimal settleRecoOrderPrice = Optional.ofNullable(purchaseReturnSettlePriceMap.get(item.getReturnOrderNo() + item.getSkuBatchCode()))
                            .orElse(BigDecimal.ZERO);
                    purchaseSkuCntVo.setReceiptCnt(item.getReceiptCnt());
                    purchaseSkuCntVo.setSettleRecoOrderPrice(settleRecoOrderPrice);
                    if (null != item.getReceiptCnt()
                            && purchaseReturnOrderVoMap.containsKey(item.getReturnOrderNo())
                            && ReturnOrderStatus.RECEIPTED.equals(purchaseReturnOrderVoMap.get(item.getReturnOrderNo()).getReturnOrderStatus())) {
                        purchaseSkuCntVo.setSettleRecoOrderPriceTotal(settleRecoOrderPrice.multiply(new BigDecimal(item.getReceiptCnt())));
                    }
                    return purchaseSkuCntVo;
                }, Collectors.toList())));

        List<PurchaseReturnVo> purchaseReturnVoList = SupplierPurchaseConverter.returnPoListToVoList(records,
                returnNoMap, purchaseChildOrderNoPoMap, returnNoItemListMap);


        return PageInfoUtil.getPageInfo(pageResult, purchaseReturnVoList);
    }

    /**
     * 退货单详情
     *
     * @param dto
     * @return
     */
    public PurchaseReturnDetailVo purchaseReturnDetail(PurchaseReturnNoDto dto) {
        PurchaseReturnOrderPo purchaseReturnOrderPo = purchaseReturnOrderDao.getOneByNo(dto.getReturnOrderNo());
        if (null == purchaseReturnOrderPo) {
            final List<PurchaseReturnOrderPo> purchaseReturnOrderPoList = purchaseReturnOrderDao.getListByTrackingNo(dto.getReturnOrderNo());
            if (CollectionUtils.isEmpty(purchaseReturnOrderPoList) || purchaseReturnOrderPoList.size() > 1) {
                throw new ParamIllegalException("当前单号{}无法查找到采购退货单，获取采购退货单详情失败",
                        dto.getReturnOrderNo());
            }
            purchaseReturnOrderPo = purchaseReturnOrderPoList.get(0);
        }
        Assert.notNull(purchaseReturnOrderPo, () -> new ParamIllegalException("当前单号{}无法找到采购退货单，获取采购退货单详情失败",
                dto.getReturnOrderNo()));

        final List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList = purchaseReturnOrderItemDao.getListByReturnOrderNo(purchaseReturnOrderPo.getReturnOrderNo());

        //组装数据获取供应商产品名称
        final List<String> skuList = purchaseReturnOrderItemPoList.stream()
                .map(PurchaseReturnOrderItemPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        Map<String, SupplierProductComparePo> supplierProductCompareMap = supplierProductCompareDao.getBatchSkuMap(skuList);

        // 获取批次码结算单价
        Map<String, BigDecimal> purchaseReturnSettlePriceMap = recoOrderBaseService.getPurchaseReturnSettlePrice(List.of(purchaseReturnOrderPo.getReturnOrderNo()));

        return SupplierPurchaseConverter.returnPoToDetailVo(purchaseReturnOrderPo, purchaseReturnOrderItemPoList,
                supplierProductCompareMap, skuEncodeMap, purchaseReturnSettlePriceMap);
    }

    /**
     * 退货单详情
     *
     * @param dto
     * @return
     */
    public List<PurchaseReturnPrintVo> batchPrintReturn(PurchaseReturnPrintDto dto) {
        List<String> returnOrderNoList = dto.getReturnOrderNoList();
        List<PurchaseReturnOrderPo> purchaseReturnOrderPoList = purchaseReturnOrderDao.getListByReturnOrderNoList(returnOrderNoList);
        if (CollectionUtils.isEmpty(purchaseReturnOrderPoList)) {
            throw new BizException("退货单不存在");
        }
        List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList = purchaseReturnOrderItemDao.getListByReturnNoList(returnOrderNoList);
        if (CollectionUtils.isEmpty(purchaseReturnOrderItemPoList)) {
            throw new BizException("退货单详情不存在");
        }
        Map<String, List<PurchaseReturnOrderItemPo>> groupedItemPo = purchaseReturnOrderItemPoList.stream()
                .collect(Collectors.groupingBy(PurchaseReturnOrderItemPo::getReturnOrderNo));

        return SupplierReturnConverter.returnToPrintVo(purchaseReturnOrderPoList, groupedItemPo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportSku(PurchaseReturnDto dto) {
        dto.setIsExport(true);
        Integer exportTotals = purchaseReturnOrderDao.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                FileOperateBizType.SCM_RETURN_ORDER_EXPORT.getCode(), dto));
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportSkuSupplier(PurchaseReturnDto dto) {
        dto.setIsExport(true);
        Integer exportTotals = purchaseReturnOrderDao.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                FileOperateBizType.SPM_RETURN_ORDER_EXPORT.getCode(), dto));
    }


    /**
     * 确认收货
     *
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    public void returnConfirm(PurchaseReturnConfirmDto dto) {
        this.checkReceiptCnt(dto.getPurchaseReturnItemList());

        final PurchaseReturnOrderPo purchaseReturnOrderPo = purchaseReturnOrderDao.getByIdVersion(dto.getPurchaseReturnOrderId(), dto.getVersion());
        Assert.notNull(purchaseReturnOrderPo, () -> new ParamIllegalException("单据发生变更，请刷新页面重试!"));
        Assert.isTrue(ReturnOrderStatus.WAIT_RECEIVE.equals(purchaseReturnOrderPo.getReturnOrderStatus()), () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        purchaseReturnOrderPo.setReturnOrderStatus(ReturnOrderStatus.RECEIPTED);
        purchaseReturnOrderPo.setReceiptUser(GlobalContext.getUserKey());
        purchaseReturnOrderPo.setReceiptUsername(GlobalContext.getUsername());
        purchaseReturnOrderPo.setReceiptTime(LocalDateTime.now());
        // 汇总收货数量
        List<PurchaseReturnItemDto> purchaseReturnItemList = dto.getPurchaseReturnItemList();
        int totalReceiptCnt = purchaseReturnItemList.stream().mapToInt(PurchaseReturnItemDto::getReceiptCnt).sum();
        purchaseReturnOrderPo.setReceiptCnt(totalReceiptCnt);
        purchaseReturnOrderDao.updateByIdVersion(purchaseReturnOrderPo);

        List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemUpdateList = SupplierPurchaseConverter.returnConfirmDtoListToPo(dto.getPurchaseReturnItemList());
        purchaseReturnOrderItemDao.updateBatchByIdVersion(purchaseReturnOrderItemUpdateList);

        // 供应链状态变更日志
        logBaseService.simpleLog(LogBizModule.PURCHASE_RETURN_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseReturnOrderPo.getReturnOrderNo(), ReturnOrderStatus.RECEIPTED.getRemark(), Collections.emptyList());

        // 供应商状态变更日志
        logBaseService.simpleLog(LogBizModule.SUPPLIER_PURCHASE_RETURN_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseReturnOrderPo.getReturnOrderNo(), ReturnOrderStatus.RECEIPTED.getRemark(), Collections.emptyList());
    }

    private void checkReceiptCnt(List<PurchaseReturnItemDto> purchaseReturnItemList) {
        List<Long> returnItemIdList = purchaseReturnItemList.stream()
                .map(PurchaseReturnItemDto::getPurchaseReturnOrderItemId)
                .collect(Collectors.toList());
        List<PurchaseReturnOrderItemPo> returnOrderItemPoList = purchaseReturnOrderItemDao.getListByIdList(returnItemIdList);
        if (CollectionUtils.isEmpty(returnOrderItemPoList)) {
            throw new BizException("退货单产品不存在");
        }
        Map<Long, PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoMap = returnOrderItemPoList.stream()
                .collect(Collectors.toMap(PurchaseReturnOrderItemPo::getPurchaseReturnOrderItemId, item -> item));
        purchaseReturnItemList.forEach(item -> {
            PurchaseReturnOrderItemPo purchaseReturnOrderItemPo = purchaseReturnOrderItemPoMap.get(item.getPurchaseReturnOrderItemId());
            if (null == purchaseReturnOrderItemPo) {
                throw new BizException("退货单产品不存在");
            }
            if (item.getReceiptCnt() > purchaseReturnOrderItemPo.getRealityReturnCnt()) {
                throw new ParamIllegalException("收货数不能大于退货数，请校验填写的收货数量重新提交！");
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateReturnOrder(ReturnOrderEventMqDto returnOrderEventMqDto) {
        final PurchaseReturnOrderPo purchaseReturnOrderPo = purchaseReturnOrderDao.getOneByNo(returnOrderEventMqDto.getReturnOrderNo());
        if (null == purchaseReturnOrderPo) {
            throw new BizException("查找不到对应的退货单:{}，更新退货单信息失败!", returnOrderEventMqDto.getReturnOrderNo());
        }
        // 已收货状态不需要更新
        if (ReturnOrderStatus.RECEIPTED.equals(purchaseReturnOrderPo.getReturnOrderStatus())) {
            return;
        }

        final List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList = purchaseReturnOrderItemDao.getListByReturnOrderNo(returnOrderEventMqDto.getReturnOrderNo());
        if (CollectionUtils.isEmpty(purchaseReturnOrderItemPoList)) {
            throw new BizException("查找不到对应的退货单:{}，更新退货单信息失败!", returnOrderEventMqDto.getReturnOrderNo());
        }

        final ReturnOrderStatus returnOrderStatus = SupplierReturnConverter.updateReturnMsgToNextStatus(returnOrderEventMqDto.getReturnCnt(),
                returnOrderEventMqDto.getReturnState());
        // 更新退货单信息
        purchaseReturnOrderPo.setReturnOrderStatus(returnOrderStatus);
        purchaseReturnOrderPo.setLogistics(returnOrderEventMqDto.getLogistics());
        purchaseReturnOrderPo.setTrackingNo(returnOrderEventMqDto.getTrackingNo());
        purchaseReturnOrderPo.setSupplierCode(returnOrderEventMqDto.getSupplierCode());
        purchaseReturnOrderPo.setSupplierName(returnOrderEventMqDto.getSupplierName());
        purchaseReturnOrderPo.setRealityReturnCnt(returnOrderEventMqDto.getReturnCnt());
        purchaseReturnOrderPo.setNote(returnOrderEventMqDto.getRemark());
        if (WmsEnum.ReturnState.HANDLED.equals(returnOrderEventMqDto.getReturnState())) {
            purchaseReturnOrderPo.setReturnUser(returnOrderEventMqDto.getOperator());
            purchaseReturnOrderPo.setReturnUsername(returnOrderEventMqDto.getOperatorName());
            purchaseReturnOrderPo.setReturnTime(LocalDateTime.now());
        }

        final Map<String, PurchaseReturnOrderItemPo> skuReturnItemPoMap = purchaseReturnOrderItemPoList.stream()
                .collect(Collectors.toMap(itemPo -> itemPo.getSku() + "_" + itemPo.getBizDetailId(), Function.identity(),
                        (item1, item2) -> item1));
        // 兼容加工类型的分组
        final Map<String, PurchaseReturnOrderItemPo> skuReturnBizNoItemPoMap = purchaseReturnOrderItemPoList.stream()
                .collect(Collectors.toMap(itemPo -> itemPo.getSku() + "_" + itemPo.getReturnBizNo(), Function.identity(),
                        (item1, item2) -> item1));
        final List<ReturnOrderEventMqDto.PurchaseReturnItemDto> purchaseReturnCreateItemList = returnOrderEventMqDto.getPurchaseReturnCreateItemList();
        purchaseReturnCreateItemList.forEach(itemDto -> {
            PurchaseReturnOrderItemPo purchaseReturnOrderItemPo = skuReturnBizNoItemPoMap.get(itemDto.getSku() + "_" + itemDto.getDefectHandlingNo());
            if (purchaseReturnOrderItemPo == null) {
                purchaseReturnOrderItemPo = skuReturnItemPoMap.get(itemDto.getSku() + "_" + itemDto.getBizDetailId());
            }
            if (null == purchaseReturnOrderItemPo) {
                throw new BizException("退货单:{}不包含该sku:{}，退货数据错误，请联系管理员！",
                        returnOrderEventMqDto.getReturnOrderNo(), itemDto.getSku());
            }
            purchaseReturnOrderItemPo.setRealityReturnCnt(itemDto.getReturnCnt());
        });

        purchaseReturnOrderDao.updateByIdVersion(purchaseReturnOrderPo);
        purchaseReturnOrderItemDao.updateBatchByIdVersion(purchaseReturnOrderItemPoList);

        // 待交接状态不更新日志
        if (!WmsEnum.ReturnState.WAIT_HAND_OVER.equals(returnOrderEventMqDto.getReturnState())) {
            // 更新退货单操作日志
            logBaseService.simpleLog(LogBizModule.PURCHASE_RETURN_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                    purchaseReturnOrderPo.getReturnOrderNo(), returnOrderStatus.getRemark(), new ArrayList<>(),
                    returnOrderEventMqDto.getOperator(), returnOrderEventMqDto.getOperatorName());


        }
        // 供应商只看到待收货的日志
        if (WmsEnum.ReturnState.HANDLED.equals(returnOrderEventMqDto.getReturnState())) {
            // 更新退货单操作日志
            logBaseService.simpleLog(LogBizModule.SUPPLIER_PURCHASE_RETURN_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                    purchaseReturnOrderPo.getReturnOrderNo(), returnOrderStatus.getRemark(), new ArrayList<>(),
                    returnOrderEventMqDto.getOperator(), returnOrderEventMqDto.getOperatorName());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.PURCHASE_UPDATE_PREFIX, key = "#purchaseChildOrderPo.purchaseParentOrderNo", waitTime = 1, leaseTime = -1)
    public void receiveReject(ReceiveOrderRejectMqDto dto, PurchaseDeliverOrderPo purchaseDeliverOrderPo, PurchaseChildOrderPo purchaseChildOrderPo) {
        final List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList = purchaseReturnOrderItemDao.getListByReturnBizNo(dto.getScmBizNo());
        if (CollectionUtils.isNotEmpty(purchaseReturnOrderItemPoList)) {
            log.error("采购发货单:{}已经生成退货单:{},wms推送mq内容重复!", dto.getScmBizNo(),
                    purchaseReturnOrderItemPoList.get(0).getReturnOrderNo());
            return;
        }


        // 获取批次码的价格
        final List<ReceiveOrderRejectMqDto.ReturnGood> returnGoodList = Optional.ofNullable(dto.getReturnGoodList()).orElse(new ArrayList<>());
        List<String> skuBatchCodeList = returnGoodList.stream()
                .map(ReceiveOrderRejectMqDto.ReturnGood::getBatchCode)
                .filter(StringUtils::isNotBlank)
                .distinct().collect(Collectors.toList());
        Map<String, BigDecimal> skuBatchCodePriceMap = wmsRemoteService.getSkuBatchPriceMapBySkuBatchList(skuBatchCodeList);


        // 拒收处理,生成退货单
        final ReturnOrderBo returnOrderBo = SupplierReturnConverter.receiveDefectDtoToBo(dto, ReturnType.RECEIVE_REJECT,
                skuBatchCodePriceMap, purchaseDeliverOrderPo, purchaseChildOrderPo.getPlatform());
        returnRefService.createReturnOrder(returnOrderBo);
        // 释放可发货数
        purchaseRefService.addPurchaseShippableCntByNo(purchaseDeliverOrderPo.getPurchaseChildOrderNo(), returnOrderBo.getExpectedReturnCnt());
    }

    @Transactional(rollbackFor = Exception.class)
    public void repair(PurchaseReturnRepairDto dto) {
        final List<PurchaseReturnRepairItemDto> purchaseReturnRepairItemList = dto.getPurchaseReturnRepairItemList();
        purchaseReturnRepairItemList.forEach(itemDto -> {
            if (itemDto.getPurchaseCnt() <= 0) {
                throw new ParamIllegalException("采购数不能小于等于0，请重新填写后再提交！");
            }
            if (itemDto.getSettlePrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ParamIllegalException("结算价不能小于等于0，请重新填写后再提交！");
            }
        });
        final List<String> returnOrderNoList = purchaseReturnRepairItemList.stream()
                .map(PurchaseReturnRepairItemDto::getReturnOrderNo)
                .distinct()
                .collect(Collectors.toList());
        final List<PurchaseReturnOrderPo> purchaseReturnOrderPoList = purchaseReturnOrderDao.getListByReturnOrderNoList(returnOrderNoList);
        if (CollectionUtils.isEmpty(purchaseReturnOrderPoList)) {
            throw new ParamIllegalException("退货单不存在，请刷新后重试！");
        }
        purchaseReturnOrderPoList.forEach(po -> {
            if (!ReturnOrderStatus.RECEIPTED.equals(po.getReturnOrderStatus())) {
                throw new ParamIllegalException("退货单:{}不处于{}状态，请刷新后重试！",
                        po.getReturnOrderNo(), ReturnOrderStatus.RECEIPTED.getRemark());
            }
            if (!ReturnType.PROCESS_DEFECT.equals(po.getReturnType())
                    && !ReturnType.INSIDE_CHECK.equals(po.getReturnType())
                    && !ReturnType.MATERIAL_DEFECT.equals(po.getReturnType())) {
                throw new ParamIllegalException("退货单:{}不属于{}、{}、{}类型，无法发起返修！",
                        po.getReturnOrderNo(), ReturnType.PROCESS_DEFECT.getRemark(),
                        ReturnType.INSIDE_CHECK.getRemark(), ReturnType.MATERIAL_DEFECT.getRemark());
            }
        });

        final Map<String, PurchaseReturnOrderPo> purchaseReturnOrderNoPoMap = purchaseReturnOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseReturnOrderPo::getReturnOrderNo, Function.identity()));

        final List<String> skuList = dto.getPurchaseReturnRepairItemList().stream()
                .map(PurchaseReturnRepairItemDto::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> skuSpuMap = plmRemoteService.getSpuMapBySkuList(skuList);
        final List<SkuInfoPo> skuInfoPoList = skuInfoDao.getListBySkuList(skuList);
        final Map<String, SkuInfoPo> skuInfoPoMap = skuInfoPoList.stream()
                .collect(Collectors.toMap(SkuInfoPo::getSku, Function.identity()));

        for (PurchaseReturnRepairItemDto itemDto : dto.getPurchaseReturnRepairItemList()) {


            final PurchaseReturnOrderPo purchaseReturnOrderPo = purchaseReturnOrderNoPoMap.get(itemDto.getReturnOrderNo());
            if (null == purchaseReturnOrderPo) {
                throw new ParamIllegalException("退货单:{}不存在，请刷新后重试！", itemDto.getReturnOrderNo());
            }
            if (itemDto.getPurchaseCnt() > purchaseReturnOrderPo.getRealityReturnCnt()) {
                throw new ParamIllegalException("退货单:{}采购数:{}大于真实退货数:{}，请重新填写后再提交！",
                        itemDto.getReturnOrderNo(), itemDto.getPurchaseCnt(), purchaseReturnOrderPo.getRealityReturnCnt());
            }

            final List<WarehouseVo> warehouseVoList = wmsRemoteService.getWarehouseByCode(Collections.singletonList(dto.getWarehouseCode()));
            if (CollectionUtils.isEmpty(warehouseVoList) || warehouseVoList.size() > 1) {
                throw new ParamIllegalException("仓库：{}不存在", dto.getWarehouseCode());
            }

            final WarehouseVo warehouseVo = warehouseVoList.get(0);
            PurchaseParentOrderPo purchaseParentOrderPo = new PurchaseParentOrderPo();
            purchaseParentOrderPo.setSpu(skuSpuMap.get(itemDto.getSku()));
            purchaseParentOrderPo.setPlatform(dto.getPlatform());
            purchaseParentOrderPo.setWarehouseName(warehouseVo.getWarehouseName());
            purchaseParentOrderPo.setWarehouseCode(dto.getWarehouseCode());
            purchaseParentOrderPo.setWarehouseTypes(warehouseVo.getWarehouseTypeName());
            purchaseParentOrderPo.setPurchaseParentOrderStatus(PurchaseParentOrderStatus.IN_PROGRESS);
            purchaseParentOrderPo.setSkuCnt(1);
            purchaseParentOrderPo.setPurchaseTotal(itemDto.getPurchaseCnt());
            purchaseParentOrderPo.setOrderRemarks(itemDto.getOrderRemarks());
            purchaseParentOrderPo.setSkuType(SkuType.SKU);
            purchaseParentOrderPo.setPurchaseDemandType(PurchaseDemandType.REPAIR);
            purchaseParentOrderPo.setPlaceOrderUser(GlobalContext.getUserKey());
            purchaseParentOrderPo.setPlaceOrderUsername(GlobalContext.getUsername());
            purchaseParentOrderPo.setIsDirectSend(BooleanType.FALSE);
            purchaseParentOrderPo.setUndeliveredCnt(itemDto.getPurchaseCnt());
            final String purchaseParentOrderNo = idGenerateService.getConfuseCode(ScmConstant.PURCHASE_PARENT_NO_PREFIX, TimeType.CN_DAY_YYYY, ConfuseLength.L_4);
            purchaseParentOrderPo.setPurchaseParentOrderNo(purchaseParentOrderNo);
            purchaseParentOrderDao.insert(purchaseParentOrderPo);

            PurchaseParentOrderChangePo purchaseParentOrderChangePo = PurchaseConverter.parentPoToChangePo(purchaseParentOrderPo);
            purchaseParentOrderChangePo.setApproveTime(LocalDateTime.now());
            purchaseParentOrderChangePo.setApproveUser(GlobalContext.getUserKey());
            purchaseParentOrderChangePo.setApproveUsername(GlobalContext.getUsername());
            purchaseParentOrderChangePo.setReceiveOrderTime(LocalDateTime.now());
            purchaseParentOrderChangeDao.insert(purchaseParentOrderChangePo);

            PurchaseParentOrderItemPo purchaseParentOrderItemPo = new PurchaseParentOrderItemPo();
            purchaseParentOrderItemPo.setPurchaseParentOrderNo(purchaseParentOrderNo);
            purchaseParentOrderItemPo.setSku(itemDto.getSku());
            purchaseParentOrderItemPo.setPurchaseCnt(itemDto.getPurchaseCnt());
            purchaseParentOrderItemPo.setUndeliveredCnt(itemDto.getPurchaseCnt());
            purchaseParentOrderItemDao.insert(purchaseParentOrderItemPo);


            logBaseService.simpleLog(LogBizModule.PURCHASE_PARENT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                    purchaseParentOrderPo.getPurchaseParentOrderNo(), PurchaseParentOrderStatus.WAIT_APPROVE.getRemark(),
                    Collections.emptyList());
            logBaseService.purchaseParentVersionLog(LogBizModule.PURCHASE_PARENT_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                    purchaseParentOrderPo.getPurchaseParentOrderNo(), purchaseParentOrderPo);

            final String purchaseChildOrderNo = purchaseBaseService.getLatestPurchaseChildNo(purchaseParentOrderNo);

            final PurchaseSkuSupplierItemDto purchaseSkuSupplierItemDto = new PurchaseSkuSupplierItemDto();
            purchaseSkuSupplierItemDto.setSupplierCode(purchaseReturnOrderPo.getSupplierCode());
            purchaseSkuSupplierItemDto.setSku(itemDto.getSku());
            purchaseSkuSupplierItemDto.setExpectedOnShelvesDate(itemDto.getExpectedOnShelvesDate());
            purchaseSkuSupplierItemDto.setCreateTime(LocalDateTime.now());
            purchaseSkuSupplierItemDto.setPurchaseChildOrderNo(purchaseChildOrderNo);

            final PurchaseChildOrderPo newPurchaseChildOrderPo = new PurchaseChildOrderPo();
            newPurchaseChildOrderPo.setPromiseDateChg(BooleanType.FALSE);
            newPurchaseChildOrderPo.setPurchaseChildOrderNo(purchaseChildOrderNo);
            newPurchaseChildOrderPo.setPurchaseParentOrderNo(purchaseParentOrderNo);
            newPurchaseChildOrderPo.setPurchaseOrderStatus(PurchaseOrderStatus.POST_QC);
            newPurchaseChildOrderPo.setPurchaseOrderType(PurchaseOrderType.REPAIR);
            newPurchaseChildOrderPo.setIsNormalOrder(BooleanType.TRUE);
            newPurchaseChildOrderPo.setSpu(skuSpuMap.get(itemDto.getSku()));
            newPurchaseChildOrderPo.setPlatform(dto.getPlatform());
            newPurchaseChildOrderPo.setPurchaseBizType(PurchaseBizType.PRODUCT);
            newPurchaseChildOrderPo.setSupplierCode(purchaseReturnOrderPo.getSupplierCode());
            newPurchaseChildOrderPo.setSupplierName(purchaseReturnOrderPo.getSupplierName());
            newPurchaseChildOrderPo.setWarehouseCode(dto.getWarehouseCode());
            newPurchaseChildOrderPo.setWarehouseName(warehouseVo.getWarehouseName());
            newPurchaseChildOrderPo.setWarehouseTypes(warehouseVo.getWarehouseTypeName());
            newPurchaseChildOrderPo.setOrderRemarks(itemDto.getOrderRemarks());
            newPurchaseChildOrderPo.setSkuCnt(1);
            newPurchaseChildOrderPo.setPurchaseTotal(itemDto.getPurchaseCnt());
            newPurchaseChildOrderPo.setExpectedOnShelvesDate(itemDto.getExpectedOnShelvesDate());
            newPurchaseChildOrderPo.setIsDirectSend(BooleanType.FALSE);
            newPurchaseChildOrderPo.setIsUploadOverseasMsg(BooleanType.FALSE);
            newPurchaseChildOrderPo.setShippableCnt(itemDto.getPurchaseCnt());
            newPurchaseChildOrderPo.setPlaceOrderUser(GlobalContext.getUserKey());
            newPurchaseChildOrderPo.setPlaceOrderUsername(GlobalContext.getUsername());
            newPurchaseChildOrderPo.setOrderSource(OrderSource.SCM);
            newPurchaseChildOrderPo.setSkuType(SkuType.SKU);
            newPurchaseChildOrderPo.setSplitType(SplitType.FOLLOW_SPLIT);
            newPurchaseChildOrderPo.setPromiseDate(itemDto.getExpectedOnShelvesDate());
            newPurchaseChildOrderPo.setReturnOrderNo(purchaseReturnOrderPo.getReturnOrderNo());
            newPurchaseChildOrderPo.setIsOverdue(BooleanType.FALSE);
            newPurchaseChildOrderPo.setIsUrgentOrder(BooleanType.FALSE);

            final SkuInfoPo skuInfoPo = skuInfoPoMap.get(itemDto.getSku());
            BigDecimal singleCapacity;
            if (null == skuInfoPo) {
                singleCapacity = BigDecimal.ZERO;
                log.info("sku:{}没有配置sku_info信息", itemDto.getSku());
            } else {
                singleCapacity = skuInfoPo.getSingleCapacity();
            }
            final BigDecimal capacity = singleCapacity.multiply(new BigDecimal(itemDto.getPurchaseCnt()))
                    .setScale(2, RoundingMode.HALF_UP);
            newPurchaseChildOrderPo.setCapacity(capacity);
            purchaseChildOrderDao.insert(newPurchaseChildOrderPo);

            final PurchaseSkuSupplierDto purchaseSkuSupplierDto = new PurchaseSkuSupplierDto();
            purchaseSkuSupplierDto.setPurchaseSkuSupplierItemList(Collections.singletonList(purchaseSkuSupplierItemDto));
            final List<PurchaseSkuSupplierItemVo> supplierDateDetail = purchaseBaseService.getSupplierDateDetail(purchaseSkuSupplierDto);
            final PurchaseSkuSupplierItemVo purchaseSkuSupplierItemVo = supplierDateDetail.get(0);
            final SupplierUrgentStatus supplierUrgentStatus = purchaseSkuSupplierItemVo.getSupplierUrgentStatus();
            if (SupplierUrgentStatus.URGENT.equals(supplierUrgentStatus)) {
                newPurchaseChildOrderPo.setIsUrgentOrder(BooleanType.TRUE);
            } else if (SupplierUrgentStatus.NOT_URGENT.equals(supplierUrgentStatus)) {
                newPurchaseChildOrderPo.setIsUrgentOrder(BooleanType.FALSE);
            }
            newPurchaseChildOrderPo.setDeliverDate(purchaseSkuSupplierItemVo.getDeliverDate());
            purchaseChildOrderDao.updateByIdVersion(newPurchaseChildOrderPo);

            // 创建单据后推送单号给wms
            final PurchaseChildNoMqDto purchaseChildNoMqDto = new PurchaseChildNoMqDto();
            purchaseChildNoMqDto.setPurchaseChildOrderNoList(Collections.singletonList(purchaseChildOrderNo));
            consistencySendMqService.execSendMq(PurchaseChangeHandler.class, purchaseChildNoMqDto);

            // 产能扣减
            final LocalDate capacityDate = purchaseBaseService.getCapacityDate(newPurchaseChildOrderPo.getPromiseDate(), newPurchaseChildOrderPo.getSupplierCode());
            final SupOpCapacityBo supOpCapacityBo = new SupOpCapacityBo();
            supOpCapacityBo.setSupplierCode(newPurchaseChildOrderPo.getSupplierCode());
            supOpCapacityBo.setOperateDate(capacityDate);
            supOpCapacityBo.setOperateValue(capacity.negate());
            supOpCapacityBo.setBizNo(newPurchaseChildOrderPo.getPurchaseChildOrderNo());
            supplierCapacityRefService.operateSupplierCapacityBatch(Collections.singletonList(supOpCapacityBo));
            // 产能变更日志
            logBaseService.purchaseCapacityLog(LogBizModule.PURCHASE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                    newPurchaseChildOrderPo.getPurchaseChildOrderNo(), newPurchaseChildOrderPo, newPurchaseChildOrderPo.getCapacity());

            final SkuBatchCreateDto skuBatchCreateDto = new SkuBatchCreateDto();
            skuBatchCreateDto.setPurchaseChildOrderNo(purchaseChildOrderNo);
            skuBatchCreateDto.setSkuCodeList(Collections.singletonList(itemDto.getSku()));
            skuBatchCreateDto.setSupplierCode(purchaseReturnOrderPo.getSupplierCode());
            skuBatchCreateDto.setSupplierName(purchaseReturnOrderPo.getSupplierName());
            Map<String, String> skuBatchMap = wmsRemoteService.batchCreateBatchCode(skuBatchCreateDto);

            final PurchaseChildOrderItemPo newPurchaseChildOrderItemPo = new PurchaseChildOrderItemPo();
            newPurchaseChildOrderItemPo.setPurchaseChildOrderNo(purchaseChildOrderNo);
            newPurchaseChildOrderItemPo.setPurchaseParentOrderNo(purchaseParentOrderNo);
            newPurchaseChildOrderItemPo.setSku(itemDto.getSku());
            newPurchaseChildOrderItemPo.setSkuBatchCode(skuBatchMap.get(itemDto.getSku()));
            newPurchaseChildOrderItemPo.setPurchaseCnt(itemDto.getPurchaseCnt());
            newPurchaseChildOrderItemPo.setInitPurchaseCnt(itemDto.getPurchaseCnt());
            newPurchaseChildOrderItemPo.setPurchasePrice(itemDto.getSettlePrice());
            newPurchaseChildOrderItemPo.setDiscountType(DiscountType.NO_DISCOUNT);
            newPurchaseChildOrderItemPo.setSubstractPrice(BigDecimal.ZERO);
            newPurchaseChildOrderItemPo.setDeliverCnt(0);
            newPurchaseChildOrderItemPo.setQualityGoodsCnt(0);
            newPurchaseChildOrderItemPo.setDefectiveGoodsCnt(0);
            newPurchaseChildOrderItemPo.setSettlePrice(itemDto.getSettlePrice());
            newPurchaseChildOrderItemPo.setUndeliveredCnt(itemDto.getPurchaseCnt());
            purchaseChildOrderItemDao.insert(newPurchaseChildOrderItemPo);

            final PurchaseChildOrderChangePo purchaseChildOrderChangePo = new PurchaseChildOrderChangePo();
            purchaseChildOrderChangePo.setPurchaseChildOrderId(newPurchaseChildOrderPo.getPurchaseChildOrderId());
            purchaseChildOrderChangePo.setPurchaseChildOrderNo(newPurchaseChildOrderPo.getPurchaseChildOrderNo());
            purchaseChildOrderChangePo.setPlaceOrderTime(new DateTime().toLocalDateTime());
            purchaseChildOrderChangePo.setPlanConfirmTime(new DateTime().toLocalDateTime());
            purchaseChildOrderChangePo.setPlanConfirmUser(GlobalContext.getUserKey());
            purchaseChildOrderChangePo.setPlanConfirmUsername(GlobalContext.getUsername());
            purchaseChildOrderChangePo.setConfirmTime(new DateTime().toLocalDateTime());
            purchaseChildOrderChangePo.setConfirmUser(GlobalContext.getUserKey());
            purchaseChildOrderChangePo.setConfirmUsername(GlobalContext.getUsername());
            purchaseChildOrderChangePo.setReceiveOrderTime(new DateTime().toLocalDateTime());
            purchaseChildOrderChangePo.setReceiveOrderUser(GlobalContext.getUserKey());
            purchaseChildOrderChangePo.setReceiveOrderUsername(GlobalContext.getUsername());
            purchaseChildOrderChangePo.setCommissioningTime(new DateTime().toLocalDateTime());
            purchaseChildOrderChangePo.setSchedulingTime(new DateTime().toLocalDateTime());


            purchaseChildOrderChangeDao.insert(purchaseChildOrderChangePo);

            logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                    purchaseChildOrderNo, PurchaseOrderStatus.POST_QC.getRemark(), Collections.emptyList());
        }

    }
}
