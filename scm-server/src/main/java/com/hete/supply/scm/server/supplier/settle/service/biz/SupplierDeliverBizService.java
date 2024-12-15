package com.hete.supply.scm.server.supplier.settle.service.biz;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseDeliverListDto;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.dao.SkuInfoDao;
import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderChangeMqDto;
import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderCreateMqDto;
import com.hete.supply.scm.server.scm.entity.dto.SkuBatchCodeQuickSearchDto;
import com.hete.supply.scm.server.scm.entity.po.SkuInfoPo;
import com.hete.supply.scm.server.scm.entity.vo.SkuBatchCodeQuickSearchVo;
import com.hete.supply.scm.server.scm.handler.WmsReceiptHandler;
import com.hete.supply.scm.server.scm.purchase.converter.PurchaseConverter;
import com.hete.supply.scm.server.scm.purchase.dao.*;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseChildNoMqDto;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseDeliverDto;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseDeliverItemDto;
import com.hete.supply.scm.server.scm.purchase.entity.po.*;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseReturnSimpleVo;
import com.hete.supply.scm.server.scm.purchase.enums.RawExtra;
import com.hete.supply.scm.server.scm.purchase.handler.PurchaseChangeHandler;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseBaseService;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseRawBaseService;
import com.hete.supply.scm.server.scm.qc.entity.bo.QcOrderBo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.supply.scm.server.scm.qc.service.ref.QcOrderRefService;
import com.hete.supply.scm.server.scm.sample.dao.SampleChildOrderDao;
import com.hete.supply.scm.server.scm.sample.dao.SampleReceiptOrderDao;
import com.hete.supply.scm.server.scm.sample.dao.SampleReceiptOrderItemDao;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderPo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleReceiptOrderItemPo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleReceiptOrderPo;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.scm.service.base.WarehouseBaseService;
import com.hete.supply.scm.server.scm.settle.dao.PurchaseSettleOrderDao;
import com.hete.supply.scm.server.scm.settle.dao.PurchaseSettleOrderItemDao;
import com.hete.supply.scm.server.scm.settle.entity.po.PurchaseSettleOrderItemPo;
import com.hete.supply.scm.server.scm.settle.entity.po.PurchaseSettleOrderPo;
import com.hete.supply.scm.server.scm.supplier.converter.SupplierInventoryConverter;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierProductCompareDao;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupOpCapacityBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierInventoryBaseService;
import com.hete.supply.scm.server.scm.supplier.service.ref.SupplierCapacityRefService;
import com.hete.supply.scm.server.supplier.converter.SupplierShippingMarkConverter;
import com.hete.supply.scm.server.supplier.dao.ShippingMarkDao;
import com.hete.supply.scm.server.supplier.dao.ShippingMarkItemDao;
import com.hete.supply.scm.server.supplier.entity.dto.*;
import com.hete.supply.scm.server.supplier.entity.po.OverseasWarehouseMsgPo;
import com.hete.supply.scm.server.supplier.entity.po.ShippingMarkItemPo;
import com.hete.supply.scm.server.supplier.entity.po.ShippingMarkPo;
import com.hete.supply.scm.server.supplier.entity.vo.*;
import com.hete.supply.scm.server.supplier.enums.ShippingMarkBizType;
import com.hete.supply.scm.server.supplier.enums.ShippingMarkStatus;
import com.hete.supply.scm.server.supplier.purchase.converter.SupplierPurchaseConverter;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderDao;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.*;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseDeliverDetailVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseDeliverPrintVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseDeliverVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseWaitDeliverVo;
import com.hete.supply.scm.server.supplier.purchase.service.base.PurchaseDeliverBaseService;
import com.hete.supply.scm.server.supplier.purchase.service.base.PurchaseReturnBaseService;
import com.hete.supply.scm.server.supplier.purchase.service.base.ShippingMarkBaseService;
import com.hete.supply.scm.server.supplier.sample.converter.SupplierSampleConverter;
import com.hete.supply.scm.server.supplier.sample.dao.SampleDeliverOrderDao;
import com.hete.supply.scm.server.supplier.sample.dao.SampleDeliverOrderItemDao;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleDeliverOrderItemPo;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleDeliverOrderPo;
import com.hete.supply.scm.server.supplier.service.biz.OverseasBizService;
import com.hete.supply.scm.server.supplier.supplier.entity.dto.InventoryChangeItemDto;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.entry.entity.dto.ReceiveOrderGetDto;
import com.hete.supply.wms.api.entry.entity.vo.ReceiveOrderForScmVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.FormatStringUtil;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
 * @date 2022/11/20 14:13
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierDeliverBizService {
    private final PurchaseDeliverOrderDao purchaseDeliverOrderDao;
    private final PurchaseDeliverOrderItemDao purchaseDeliverOrderItemDao;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final PurchaseChildOrderChangeDao purchaseChildOrderChangeDao;
    private final PurchaseParentOrderDao purchaseParentOrderDao;
    private final IdGenerateService idGenerateService;
    private final ConsistencySendMqService consistencySendMqService;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;
    private final PurchaseChildOrderRawDao purchaseChildOrderRawDao;
    private final LogBaseService logBaseService;
    private final PurchaseReturnBaseService purchaseReturnOrderBaseService;
    private final OverseasBizService overseasBizService;
    private final ShippingMarkDao shippingMarkDao;
    private final ShippingMarkItemDao shippingMarkItemDao;
    private final PlmRemoteService plmRemoteService;
    private final ShippingMarkBaseService shippingMarkBaseService;
    private final SampleDeliverOrderDao sampleDeliverOrderDao;
    private final SampleDeliverOrderItemDao sampleDeliverOrderItemDao;
    private final SampleReceiptOrderDao sampleReceiptOrderDao;
    private final SampleReceiptOrderItemDao sampleReceiptOrderItemDao;
    private final WmsRemoteService wmsRemoteService;
    private final PurchaseParentOrderItemDao purchaseParentOrderItemDao;
    private final PurchaseDeliverBaseService purchaseDeliverBaseService;
    private final SupplierProductCompareDao supplierProductCompareDao;
    private final SampleChildOrderDao sampleChildOrderDao;
    private final PurchaseModifyOrderDao purchaseModifyOrderDao;
    private final PurchaseReturnOrderDao purchaseReturnOrderDao;
    private final PurchaseSettleOrderItemDao purchaseSettleOrderItemDao;
    private final PurchaseSettleOrderDao purchaseSettleOrderDao;
    private final QcOrderRefService qcOrderRefService;
    private final SupplierInventoryBaseService supplierInventoryBaseService;
    private final PurchaseRawBaseService purchaseRawBaseService;
    private final PurchaseBaseService purchaseBaseService;
    private final SupplierCapacityRefService supplierCapacityRefService;
    private final SkuInfoDao skuInfoDao;

    /**
     * 箱唛"-"切分标准长度
     */
    private final static Integer SHIPPING_STANDARD_LENGTH = 2;

    /**
     * 最小可发货数
     */
    private final static Integer MIN_SHIPPABLE_CNT = 0;


    public CommonPageResult.PageInfo<PurchaseDeliverVo> deliverList(PurchaseDeliverListDto dto) {

        //条件过滤
        if (null == purchaseDeliverBaseService.getSearchPurchaseDeliverWhere(dto)) {
            return new CommonPageResult.PageInfo<>();
        }

        final CommonPageResult.PageInfo<PurchaseDeliverVo> pageResult = purchaseDeliverOrderDao.getDeliverList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<PurchaseDeliverVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new CommonPageResult.PageInfo<>();
        }

        purchaseDeliverBaseService.fillPurchaseDeliverVo(records);


        return pageResult;
    }

    public PurchaseDeliverDetailVo deliverDetail(PurchaseDeliverOrderNoDto dto) {
        PurchaseDeliverOrderPo purchaseDeliverOrderPo = purchaseDeliverOrderDao.getOneByNo(dto.getPurchaseDeliverOrderNo());
        Assert.notNull(purchaseDeliverOrderPo, () -> new ParamIllegalException("该单号:{}查找不到采购发货单，获取采购发货单详情失败",
                dto.getPurchaseDeliverOrderNo()));

        List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList = purchaseDeliverOrderItemDao.getListByDeliverOrderNo(dto.getPurchaseDeliverOrderNo());
        List<String> skuList = purchaseDeliverOrderItemPoList.stream().map(PurchaseDeliverOrderItemPo::getSku).distinct().collect(Collectors.toList());
        Map<String, String> skuEncodeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(skuList)) {
            skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        }
        Map<String, String> supplierProductCompareMap = supplierProductCompareDao.getBatchSkuMapAndSupplierCode(skuList, purchaseDeliverOrderPo.getSupplierCode());
        // 退货信息
        final List<PurchaseReturnSimpleVo> purchaseReturnSimpleList = purchaseReturnOrderBaseService.getPurchaseReturnByPurchaseChildNo(Collections.singletonList(purchaseDeliverOrderPo.getPurchaseChildOrderNo()));
        final PurchaseDeliverDetailVo purchaseDeliverDetailVo = PurchaseConverter.detailPoToVo(purchaseDeliverOrderPo,
                purchaseDeliverOrderItemPoList,
                skuEncodeMap,
                supplierProductCompareMap);
        purchaseDeliverDetailVo.setPurchaseReturnSimpleList(purchaseReturnSimpleList);

        //关联单据的数据
        ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
        receiveOrderGetDto.setScmBizNoList(List.of(purchaseDeliverOrderPo.getPurchaseDeliverOrderNo()));
        List<ReceiveOrderForScmVo> receiveOrderList = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto);
        List<PurchaseDeliverDetailVo.AssociateDocumentsItem> associateDocumentsItemList = new ArrayList<>();
        final List<String> receiveOrderNoList = receiveOrderList.stream()
                .map(ReceiveOrderForScmVo::getReceiveOrderNo)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, List<QcOrderBo>> receiveQcOrderNoQcBoListMap = qcOrderRefService.getReceiveQcOrderByReceiveOrderNoList(receiveOrderNoList);
        for (ReceiveOrderForScmVo receiveOrderForScmVo : receiveOrderList) {
            PurchaseDeliverDetailVo.AssociateDocumentsItem associateDocumentsItem = new PurchaseDeliverDetailVo.AssociateDocumentsItem();
            associateDocumentsItem.setDeliverOrderItemType(DeliverOrderItemType.BULK);
            associateDocumentsItem.setBusinessNo(receiveOrderForScmVo.getReceiveOrderNo());
            associateDocumentsItem.setStatusName(receiveOrderForScmVo.getReceiveOrderState().getRemark());
            associateDocumentsItem.setReceiptCnt(receiveOrderForScmVo.getReceiveAmount());
            associateDocumentsItem.setFinishTime(receiveOrderForScmVo.getFinishReceiveTime());
            associateDocumentsItemList.add(associateDocumentsItem);
            // 质检单列表
            final List<QcOrderBo> qcOrderBoList = receiveQcOrderNoQcBoListMap.getOrDefault(receiveOrderForScmVo.getReceiveOrderNo(),
                    new ArrayList<>());
            for (QcOrderBo qcOrderBo : qcOrderBoList) {
                final QcOrderPo qcOrderPo = qcOrderBo.getQcOrderPo();
                PurchaseDeliverDetailVo.AssociateDocumentsItem qcItem = new PurchaseDeliverDetailVo.AssociateDocumentsItem();
                qcItem.setDeliverOrderItemType(DeliverOrderItemType.QUALITY_INSPECTION);
                qcItem.setBusinessNo(qcOrderPo.getQcOrderNo());
                qcItem.setStatusName(qcOrderPo.getQcState().getRemark());
                qcItem.setQcAmount(qcOrderPo.getQcAmount());

                final List<QcDetailPo> qcDetailPoList = qcOrderBo.getQcDetailPoList();
                final int qualityGoodsCnt = qcDetailPoList.stream().mapToInt(QcDetailPo::getPassAmount).sum();
                qcItem.setQualityGoodsCnt(qualityGoodsCnt);
                final int notPassAmount = qcDetailPoList.stream().mapToInt(QcDetailPo::getNotPassAmount).sum();
                qcItem.setDefectiveGoodsCnt(notPassAmount);
                qcItem.setFinishTime(qcOrderPo.getTaskFinishTime());
                associateDocumentsItemList.add(qcItem);
            }
            //上架单列表
            for (ReceiveOrderForScmVo.OnShelfOrder onOrder : receiveOrderForScmVo.getOnShelfList()) {
                PurchaseDeliverDetailVo.AssociateDocumentsItem onItem = new PurchaseDeliverDetailVo.AssociateDocumentsItem();
                onItem.setDeliverOrderItemType(DeliverOrderItemType.SHELVES_ORDER);
                onItem.setBusinessNo(onOrder.getOnShelvesOrderNo());
                onItem.setStatusName(onOrder.getOnShelfState().getRemark());
                onItem.setOnShelvesAmount(onOrder.getOnShelvesAmount());
                onItem.setFinishTime(onOrder.getFinishOnTime());
                associateDocumentsItemList.add(onItem);
            }

        }
        //退货单列表
        List<PurchaseReturnOrderPo> purchaseReturnOrderPoList = purchaseReturnOrderDao.getListByReturnBizNo(purchaseDeliverOrderPo.getPurchaseDeliverOrderNo());
        for (PurchaseReturnOrderPo purchaseReturnOrderPo : purchaseReturnOrderPoList) {
            PurchaseDeliverDetailVo.AssociateDocumentsItem returnItem = new PurchaseDeliverDetailVo.AssociateDocumentsItem();
            returnItem.setDeliverOrderItemType(DeliverOrderItemType.RETURN_ORDER);
            returnItem.setBusinessNo(purchaseReturnOrderPo.getReturnOrderNo());
            returnItem.setStatusName(purchaseReturnOrderPo.getReturnOrderStatus().getRemark());
            returnItem.setReturnAmount(purchaseReturnOrderPo.getRealityReturnCnt());
            returnItem.setFinishTime(purchaseReturnOrderPo.getReceiptTime());
            associateDocumentsItemList.add(returnItem);

        }
        //结算单列表
        List<PurchaseSettleOrderItemPo> purchaseSettleOrderItemPoList = purchaseSettleOrderItemDao.getByBusinessNoList(List.of(purchaseDeliverOrderPo.getPurchaseDeliverOrderNo()));
        Map<String, PurchaseSettleOrderPo> purchaseSettleOrderPoMap = purchaseSettleOrderDao.getMapByNoList(purchaseSettleOrderItemPoList.stream().map(PurchaseSettleOrderItemPo::getPurchaseSettleOrderNo).collect(Collectors.toList()));
        for (PurchaseSettleOrderItemPo purchaseSettleOrderItemPo : purchaseSettleOrderItemPoList) {
            PurchaseDeliverDetailVo.AssociateDocumentsItem settleItem = new PurchaseDeliverDetailVo.AssociateDocumentsItem();
            settleItem.setDeliverOrderItemType(DeliverOrderItemType.SETTLE_ORDER);
            settleItem.setBusinessNo(purchaseSettleOrderItemPo.getPurchaseSettleOrderNo());
            settleItem.setStatusName(purchaseSettleOrderPoMap.get(purchaseSettleOrderItemPo.getPurchaseSettleOrderNo()).getPurchaseSettleStatus().getRemark());
            settleItem.setReturnAmount(purchaseSettleOrderItemPo.getSkuNum());
            settleItem.setFinishTime(purchaseSettleOrderPoMap.get(purchaseSettleOrderItemPo.getPurchaseSettleOrderNo()).getPayTime());
            associateDocumentsItemList.add(settleItem);
        }

        purchaseDeliverDetailVo.setAssociateDocumentsItemList(associateDocumentsItemList);

        return purchaseDeliverDetailVo;
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
        if (!DeliverOrderStatus.WAIT_DELIVER.equals(deliverOrderStatus)) {
            throw new ParamIllegalException("采购发货单状态为:{}，不允许取消发货，请刷新后重试!", deliverOrderStatus.getRemark());
        }

        final PurchaseParentOrderPo purchaseParentOrderPo = purchaseParentOrderDao.getOneByNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
        shippingMarkBaseService.checkDeliverNoHasShippingMark(purchaseDeliverOrderPo.getPurchaseDeliverOrderNo());

        purchaseDeliverBaseService.cancelDeliver(purchaseDeliverOrderPo, purchaseParentOrderPo, purchaseChildOrderPo,
                purchaseChildOrderItemPoList, SystemType.SPM);
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.PURCHASE_UPDATE_PREFIX, key = "#dto.purchaseParentOrderNo", waitTime = 1, leaseTime = -1)
    public void createDeliver(PurchaseDeliverCreateDto dto) {
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(dto.getPurchaseChildOrderNo());
        Assert.notNull(purchaseChildOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        if (StringUtils.isNotBlank(purchaseChildOrderPo.getAdjustPriceApproveNo())) {
            throw new ParamIllegalException("采购单:{}关联了审批单:{}未审批完成，无法进行发货操作", dto.getPurchaseChildOrderNo(), purchaseChildOrderPo.getAdjustPriceApproveNo());
        }
        PurchaseChildOrderChangePo purchaseChildOrderChangePo = purchaseChildOrderChangeDao.getByChildOrderId(purchaseChildOrderPo.getPurchaseChildOrderId());
        Assert.notNull(purchaseChildOrderChangePo, () -> new BizException("查找不到对应的采购子单，发货失败"));
        final PurchaseParentOrderPo purchaseParentOrderPo = purchaseParentOrderDao.getOneByNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
        Assert.notNull(purchaseParentOrderPo, () -> new BizException("查找不到对应的采购母单，发货失败"));
        final List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList = purchaseParentOrderItemDao.getListByParentNo(purchaseParentOrderPo.getPurchaseParentOrderNo());
        Assert.notEmpty(purchaseParentOrderItemPoList, () -> new BizException("查找不到对应的采购母单，发货失败"));
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNo(dto.getPurchaseChildOrderNo());
        Assert.notEmpty(purchaseChildOrderItemPoList, () -> new BizException("查找不到对应的采购子单，发货失败"));
        OverseasWarehouseMsgPo overseasWarehouseMsgPo = null;
        // 若为海外直发仓类型的采购单，需要判断文件是否上传
        if (BooleanType.TRUE.equals(purchaseChildOrderPo.getIsDirectSend())) {
            overseasWarehouseMsgPo = overseasBizService.hasOverseasMsgByPurchaseChildNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
            Assert.notNull(overseasWarehouseMsgPo, () -> new ParamIllegalException("该采购单是海外直发类型，尚未上传海外仓相关条码和箱唛号pdf文件，" +
                    "请联系采购进行上传才能生成发货单！"));

            if (BooleanType.FALSE.equals(wmsRemoteService.overseasCanDeliver(purchaseChildOrderPo.getPurchaseChildOrderNo()))) {
                throw new ParamIllegalException("该采购单没有生成海外调拨单，请联系采购员创建相关海外调拨单并上传海外仓文件后再操作生成发货单！");
            }
        }

        // 校验采购子单是否处于可发货状态
        if (!PurchaseOrderStatus.getPurchaseDeliverStatus().contains(purchaseChildOrderPo.getPurchaseOrderStatus())) {
            throw new ParamIllegalException("当前采购单状态为：{}，不可生成发货单，请刷新后重新操作！",
                    purchaseChildOrderPo.getPurchaseOrderStatus().getRemark());
        }

        final List<PurchaseDeliverCreateItemDto> purchaseDeliverItemDtoList = dto.getPurchaseDeliverItemDtoList();
        this.checkPurchaseCntAndDeliverCnt(purchaseDeliverItemDtoList);

        final List<String> childSkuList = purchaseChildOrderItemPoList.stream()
                .map(PurchaseChildOrderItemPo::getSku)
                .collect(Collectors.toList());
        final List<String> skuList = purchaseDeliverItemDtoList.stream()
                .map(PurchaseDeliverCreateItemDto::getSku)
                .collect(Collectors.toList());

        if (!childSkuList.containsAll(skuList)) {
            throw new BizException("发货的sku必须在子单的sku范围内");
        }

        final int totalDeliverCnt = dto.getPurchaseDeliverItemDtoList()
                .stream()
                .mapToInt(PurchaseDeliverCreateItemDto::getDeliverCnt)
                .sum();
        // 判断发货数是否会超过可发货数
        if (totalDeliverCnt > purchaseChildOrderPo.getShippableCnt()) {
            throw new ParamIllegalException("发货数超过可发货数，请重新填写后再提交！");
        }


        // 生成发货单
        final String deliverOrderNo = idGenerateService.getConfuseCode(ScmConstant.PURCHASE_DELIVER_ORDER_NO_PREFIX, TimeType.CN_DAY, ConfuseLength.L_4);

        PurchaseDeliverOrderPo purchaseDeliverOrderPo = SupplierPurchaseConverter.deliverDtoToPo(dto, deliverOrderNo, purchaseChildOrderPo,
                overseasWarehouseMsgPo);
        final List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByChildOrderNo(dto.getPurchaseChildOrderNo());
        if (CollectionUtils.isNotEmpty(purchaseDeliverOrderPoList)) {
            purchaseDeliverOrderPo.setDeliverOrderType(DeliverOrderType.SUPPLEMENTARY);
        }

        purchaseDeliverOrderDao.insert(purchaseDeliverOrderPo);
        logBaseService.simpleLog(LogBizModule.SUPPLIER_PURCHASE_DELIVER_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseDeliverOrderPo.getPurchaseDeliverOrderNo(), DeliverOrderStatus.WAIT_DELIVER.getRemark(), Collections.emptyList());

        List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList = SupplierPurchaseConverter.deliverDtoToItemPoList(purchaseDeliverItemDtoList, deliverOrderNo);
        purchaseDeliverOrderItemDao.insertBatch(purchaseDeliverOrderItemPoList);

        // 更新采购母单change
        purchaseChildOrderChangePo.setPurchaseDeliverOrderNo(deliverOrderNo);
        purchaseChildOrderChangeDao.updateByIdVersion(purchaseChildOrderChangePo);

        final Map<String, Integer> skuDeliverCntMap = purchaseDeliverItemDtoList.stream()
                .collect(Collectors.toMap(PurchaseDeliverCreateItemDto::getSku, PurchaseDeliverCreateItemDto::getDeliverCnt));
        // 更新母单item发货数
        purchaseParentOrderItemPoList.forEach(po -> {
            if (null == skuDeliverCntMap.get(po.getSku())) {
                return;
            }
            po.setDeliverCnt(po.getDeliverCnt() + skuDeliverCntMap.get(po.getSku()));
        });
        purchaseParentOrderItemDao.updateBatchByIdVersion(purchaseParentOrderItemPoList);
        // 更新子单item发货数
        purchaseChildOrderItemPoList.forEach(po -> {
            if (null == skuDeliverCntMap.get(po.getSku())) {
                return;
            }
            po.setDeliverCnt(po.getDeliverCnt() + skuDeliverCntMap.get(po.getSku()));
            po.setPurchaseDeliverOrderNo(deliverOrderNo);
        });
        purchaseChildOrderItemDao.updateBatchById(purchaseChildOrderItemPoList);

        // 更新采购子单状态,待发货为最先状态，无需判断
        purchaseChildOrderPo.setPurchaseOrderStatus(PurchaseOrderStatus.WAIT_DELIVER);
        purchaseChildOrderPo.setShippableCnt(Math.max(purchaseChildOrderPo.getShippableCnt() - purchaseDeliverOrderPo.getDeliverCnt(), MIN_SHIPPABLE_CNT));
        purchaseChildOrderDao.updateByIdVersion(purchaseChildOrderPo);

        // 创建发货单额外消耗原料
        purchaseRawBaseService.createDeliverExtraRawConsume(dto.getPurchaseDeliverRawList(), purchaseChildOrderPo);

        // 单据状态变更后推送单号给wms
        final PurchaseChildNoMqDto purchaseChildNoMqDto = new PurchaseChildNoMqDto();
        purchaseChildNoMqDto.setPurchaseChildOrderNoList(Collections.singletonList(dto.getPurchaseChildOrderNo()));
        consistencySendMqService.execSendMq(PurchaseChangeHandler.class, purchaseChildNoMqDto);

    }

    private void checkPurchaseCntAndDeliverCnt(List<PurchaseDeliverCreateItemDto> purchaseDeliverItemDtoList) {
        purchaseDeliverItemDtoList.forEach(dto -> {
            if (dto.getDeliverCnt() > dto.getPurchaseCnt()) {
                throw new ParamIllegalException("填写的发货数不能超过采购数，请重新校正数据后再提交");
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void deliver(PurchaseDeliverDto dto) {
        final List<String> deliverOrderNoList = dto.getPurchaseDeliverItemList()
                .stream()
                .map(PurchaseDeliverItemDto::getPurchaseDeliverOrderNo)
                .distinct()
                .collect(Collectors.toList());

        final ShippingMarkPo trackingNoShippingMarkPo = shippingMarkDao.getOneByTrackingNo(dto.getTrackingNo());
        if (null != trackingNoShippingMarkPo) {
            throw new ParamIllegalException("该运单号{}已经绑定了箱唛{},请重新填写运单号后重试！", dto.getTrackingNo(),
                    trackingNoShippingMarkPo.getShippingMarkNo());
        }

        final ShippingMarkPo shippingMarkPo = shippingMarkDao.getOneByNo(dto.getShippingMarkNo());
        if (null == shippingMarkPo || !ShippingMarkStatus.WAIT_DELIVER.equals(shippingMarkPo.getShippingMarkStatus())) {
            throw new ParamIllegalException("找不到对应的箱唛，请刷新后重试！");
        }

        String deliverUser = GlobalContext.getUserKey();
        String deliverUsername = GlobalContext.getUsername();
        LocalDateTime deliverTime = LocalDateTime.now();
        if (ShippingMarkBizType.PURCHASE_CHILD.equals(shippingMarkPo.getShippingMarkBizType())) {
            this.purchaseDeliver(deliverUser, deliverUsername, deliverTime, deliverOrderNoList, dto);
        } else if (ShippingMarkBizType.SAMPLE_CHILD.equals(shippingMarkPo.getShippingMarkBizType())) {
            this.sampleDeliver(deliverOrderNoList, dto);
        } else {
            throw new BizException("错误的箱唛类型，箱唛号:{}，请联系系统管理员！", shippingMarkPo.getShippingMarkNo());
        }

        // 更新箱唛状态
        shippingMarkPo.setDeliverUser(deliverUser);
        shippingMarkPo.setDeliverUsername(deliverUsername);
        shippingMarkPo.setDeliverTime(deliverTime);
        shippingMarkPo.setTrackingNo(dto.getTrackingNo());
        shippingMarkPo.setShippingMarkStatus(ShippingMarkStatus.DELIVERED);
        shippingMarkDao.updateByIdVersion(shippingMarkPo);

        logBaseService.simpleLog(LogBizModule.SHIPPING_MARK, ScmConstant.PURCHASE_LOG_VERSION,
                dto.getShippingMarkNo(), ShippingMarkStatus.WAIT_DELIVER.getRemark(), Collections.emptyList());
    }

    /**
     * 采购确认发货
     *
     * @param deliverUser
     * @param deliverUsername
     * @param deliverTime
     * @param deliverOrderNoList
     * @param dto
     */
    private void purchaseDeliver(String deliverUser, String deliverUsername, LocalDateTime deliverTime,
                                 List<String> deliverOrderNoList, PurchaseDeliverDto dto) {
        final List<PurchaseDeliverOrderPo> trackingNoDeliverList = purchaseDeliverOrderDao.getListByTrackingNo(dto.getTrackingNo());
        if (CollectionUtils.isNotEmpty(trackingNoDeliverList)) {
            throw new ParamIllegalException("该运单号{}已经绑定了发货单,请重新填写运单号后重试！", dto.getTrackingNo());
        }

        List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByNoList(deliverOrderNoList);
        Assert.equals(purchaseDeliverOrderPoList.size(), deliverOrderNoList.size(), () -> new BizException("查找不到对应的发货单，发货失败"));
        // 校验所有发货单是否同一仓库
        final List<String> warehouseCodeList = purchaseDeliverOrderPoList.stream()
                .map(PurchaseDeliverOrderPo::getWarehouseCode)
                .distinct()
                .collect(Collectors.toList());
        if (warehouseCodeList.size() > 1) {
            throw new BizException("该发货操作选择了不同的接收仓库，发货失败");
        }

        final List<String> purchaseChildOrderNoList = purchaseDeliverOrderPoList.stream()
                .map(PurchaseDeliverOrderPo::getPurchaseChildOrderNo)
                .distinct()
                .collect(Collectors.toList());

        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList);
        Assert.equals(purchaseChildOrderPoList.size(), purchaseChildOrderNoList.size(), () -> new BizException("查找不到对应的采购子单，发货失败"));
        final Map<String, PurchaseChildOrderPo> purchaseChildOrderNoPoMap = purchaseChildOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));
        final List<String> purchaseDeliverOrderNoList = purchaseDeliverOrderPoList.stream()
                .map(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo)
                .collect(Collectors.toList());
        final List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList = purchaseDeliverOrderItemDao.getListByDeliverOrderNoList(purchaseDeliverOrderNoList);
        final Map<String, PurchaseDeliverOrderItemPo> purchaseDeliverNoItemPoMap = purchaseDeliverOrderItemPoList.stream()
                .collect(Collectors.toMap(PurchaseDeliverOrderItemPo::getPurchaseDeliverOrderNo,
                        Function.identity(), (item1, item2) -> item1));

        // 更新采购发货单
        final List<SupOpCapacityBo> supOpCapacityBoList = new ArrayList<>();
        for (PurchaseDeliverOrderPo purchaseDeliverOrderPo : purchaseDeliverOrderPoList) {
            purchaseDeliverOrderPo.setTrackingNo(dto.getTrackingNo());
            purchaseDeliverOrderPo.setDeliverUser(deliverUser);
            purchaseDeliverOrderPo.setDeliverUsername(deliverUsername);
            purchaseDeliverOrderPo.setDeliverTime(deliverTime);
            final DeliverOrderStatus targetStats = purchaseDeliverOrderPo.getDeliverOrderStatus().toWaitReceive();
            purchaseDeliverOrderPo.setDeliverOrderStatus(targetStats);
            logBaseService.simpleLog(LogBizModule.SUPPLIER_PURCHASE_DELIVER_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                    purchaseDeliverOrderPo.getPurchaseDeliverOrderNo(), targetStats.getRemark(), Collections.emptyList());

            // 产能变更
            final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderNoPoMap.get(purchaseDeliverOrderPo.getPurchaseChildOrderNo());
            if (null == purchaseChildOrderPo) {
                continue;
            }
            final PurchaseDeliverOrderItemPo purchaseDeliverOrderItemPo = purchaseDeliverNoItemPoMap.get(purchaseDeliverOrderPo.getPurchaseDeliverOrderNo());
            // 扣减供应商发货日期的剩余产能，增加供应商答交日期的剩余产能。
            final SkuInfoPo skuInfoPo = skuInfoDao.getBySku(purchaseDeliverOrderItemPo.getSku());
            BigDecimal singleCapacity;
            if (null == skuInfoPo) {
                singleCapacity = BigDecimal.ZERO;
                log.info("sku:{}没有配置sku_info信息", purchaseDeliverOrderItemPo.getSku());
            } else {
                singleCapacity = skuInfoPo.getSingleCapacity();
            }
            final BigDecimal capacity = singleCapacity.multiply(new BigDecimal(purchaseDeliverOrderPo.getDeliverCnt()))
                    .setScale(2, RoundingMode.HALF_UP);

            final LocalDate capacityDate = purchaseBaseService.getCapacityDate(purchaseChildOrderPo.getPromiseDate(), purchaseChildOrderPo.getSupplierCode());
            final SupOpCapacityBo supOpCapacityBo = new SupOpCapacityBo();
            supOpCapacityBo.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
            supOpCapacityBo.setOperateDate(capacityDate);
            supOpCapacityBo.setOperateValue(capacity);
            supOpCapacityBo.setBizNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
            supOpCapacityBoList.add(supOpCapacityBo);
            final SupOpCapacityBo supOpCapacityBo1 = new SupOpCapacityBo();
            supOpCapacityBo1.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
            supOpCapacityBo1.setOperateDate(LocalDate.now());
            supOpCapacityBo1.setOperateValue(capacity.negate());
            supOpCapacityBo1.setBizNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
            supOpCapacityBoList.add(supOpCapacityBo1);
            // 产能变更日志
            logBaseService.purchaseCapacityLog(LogBizModule.PURCHASE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                    purchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseChildOrderPo, purchaseChildOrderPo.getCapacity());
        }

        purchaseDeliverOrderDao.updateBatchByIdVersion(purchaseDeliverOrderPoList);
        supplierCapacityRefService.operateSupplierCapacityBatch(supOpCapacityBoList);

        // 推送发货单给wms
        final List<ShippingMarkItemPo> shippingMarkItemPoList = shippingMarkItemDao.getListByNo(dto.getShippingMarkNo());
        final Map<String, PurchaseChildOrderPo> purchaseChildOrderPoMap = purchaseChildOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));

        final Map<String, List<String>> deliverNoShippingMap = shippingMarkItemPoList.stream()
                .collect(Collectors.groupingBy(ShippingMarkItemPo::getDeliverOrderNo,
                        Collectors.mapping(itemPo -> dto.getShippingMarkNo() + "-" + itemPo.getShippingMarkNum(), Collectors.toList())));

        final Set<String> warehouseCodes = purchaseDeliverOrderPoList.stream()
                .map(PurchaseDeliverOrderPo::getWarehouseCode)
                .collect(Collectors.toSet());
        final Map<String, WmsEnum.QcType> warehouseQcTypeMap
                = this.buildWarehouseQcTypeMap(warehouseCodes, WmsEnum.QcType.ALL_CHECK);

        final ReceiveOrderCreateMqDto receiveOrderCreateMqDto
                = SupplierPurchaseConverter.deliverPoToMqDto(dto, purchaseDeliverOrderItemPoList,
                purchaseDeliverOrderPoList, purchaseChildOrderPoMap, deliverNoShippingMap, warehouseQcTypeMap);

        receiveOrderCreateMqDto.setKey(idGenerateService.getSnowflakeCode(dto.getTrackingNo() + "-"));
        consistencySendMqService.execSendMq(WmsReceiptHandler.class, receiveOrderCreateMqDto);
    }

    /**
     * 构建仓库编码与对应质检类型的映射关系。
     *
     * @param warehouseCodes 仓库编码集合，用于确定每个仓库的质检类型。
     * @return 一个映射，其中键是仓库编码，值是质检类型。
     */
    public Map<String, WmsEnum.QcType> buildWarehouseQcTypeMap(Collection<String> warehouseCodes, WmsEnum.QcType defaultQcType) {
        Map<String, WmsEnum.QcType> warehouseQcTypeMap = new HashMap<>(warehouseCodes.size());

        warehouseCodes.forEach(warehouseCode -> {
            // 检查仓库是否属于 FOREIGN_SELF_RUN 或 VIRTUAL_WAREHOUSE 类型
            boolean notCheckWarehouse = wmsRemoteService.isMatchWarehouseTypes(
                    warehouseCode, Set.of(WmsEnum.WarehouseType.FOREIGN_SELF_RUN,
                            WmsEnum.WarehouseType.VIRTUAL_WAREHOUSE));

            // 根据仓库类型分配相应的质检类型
            warehouseQcTypeMap.put(warehouseCode, notCheckWarehouse ? WmsEnum.QcType.NOT_CHECK : defaultQcType);
        });

        return warehouseQcTypeMap;
    }

    /**
     * 样品发货校验
     *
     * @param deliverOrderNoList
     * @param dto
     */
    private void sampleDeliver(List<String> deliverOrderNoList, PurchaseDeliverDto dto) {
        final List<SampleDeliverOrderPo> trackingNoDeliverList = sampleDeliverOrderDao.getListByTrackingNo(dto.getTrackingNo());
        if (CollectionUtils.isNotEmpty(trackingNoDeliverList)) {
            throw new ParamIllegalException("该运单号{}已经绑定了发货单,请重新填写运单号后重试！", dto.getTrackingNo());
        }
        final List<SampleDeliverOrderPo> sampleDeliverOrderPoList = sampleDeliverOrderDao.getDeliverPoListByNoList(deliverOrderNoList);
        Assert.equals(sampleDeliverOrderPoList.size(), deliverOrderNoList.size(), () -> new BizException("查找不到对应的发货单，发货失败"));
        // 校验所有发货单是否同一仓库
        final List<String> warehouseCodeList = sampleDeliverOrderPoList.stream()
                .map(SampleDeliverOrderPo::getWarehouseCode)
                .distinct()
                .collect(Collectors.toList());

        if (warehouseCodeList.size() > 1) {
            throw new BizException("该发货操作选择了不同的接收仓库，发货失败");
        }

        // 生成收货单
        List<SampleReceiptOrderPo> sampleReceiptOrderPoList = new ArrayList<>();
        List<SampleReceiptOrderItemPo> sampleReceiptOrderItemPoList = new ArrayList<>();

        sampleDeliverOrderPoList.forEach(po -> {
            final SampleReceiptOrderPo sampleReceiptOrderPo = SupplierSampleConverter.deliverPoToReceiptPo(po);
            final String sampleReceiptOrderNo = idGenerateService.getConfuseCode(ScmConstant.SAMPLE_RECEIPT_ORDER_NO_PREFIX, ConfuseLength.L_10);
            sampleReceiptOrderPo.setSampleReceiptOrderNo(sampleReceiptOrderNo);
            final List<SampleDeliverOrderItemPo> sampleDeliverOrderItemPoList = sampleDeliverOrderItemDao.getListByDeliverOrderNoList(deliverOrderNoList);

            sampleReceiptOrderPoList.add(sampleReceiptOrderPo);
            sampleReceiptOrderItemPoList.addAll(SupplierSampleConverter.deliverItemPoListToItemPoList(sampleDeliverOrderItemPoList, sampleReceiptOrderNo));

            logBaseService.simpleLog(LogBizModule.SAMPLE_RECEIPT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                    sampleReceiptOrderNo, SampleReceiptOrderStatus.WAIT_RECEIVED_SAMPLE.getRemark(), Collections.emptyList());
        });

        sampleReceiptOrderDao.insertBatch(sampleReceiptOrderPoList);
        sampleReceiptOrderItemDao.insertBatch(sampleReceiptOrderItemPoList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void returnRaw(PurchaseReturnRawDto dto) {
        // 过滤掉归还数为0或者为空的数据
        final List<RawReturnProductItemDto> resultItemDtoList = dto.getRawProductItemList()
                .stream()
                .filter(itemDto -> itemDto.getReturnCnt() != null && itemDto.getReturnCnt() != 0)
                .collect(Collectors.toList());
        dto.setRawProductItemList(resultItemDtoList);

        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(dto.getPurchaseChildOrderNo());
        Assert.notNull(purchaseChildOrderPo, () -> new BizException("查找不到对应的采购子单，归还原料失败"));
        final List<RawReturnProductItemDto> rawProductItemList = dto.getRawProductItemList();
        rawProductItemList.forEach(itemDto -> {
            if (!RawSupplier.HETE.equals(itemDto.getRawSupplier())
                    && !RawSupplier.OTHER_SUPPLIER.equals(itemDto.getRawSupplier())) {
                return;
            }
            final BooleanType isMateWarehouse = wmsRemoteService.mateWarehouseCodeAndReceiveType(WmsEnum.ReceiveType.PROCESS_MATERIAL, itemDto.getWarehouseCode());
            if (BooleanType.FALSE.equals(isMateWarehouse)) {
                throw new ParamIllegalException("选择的仓库{}与收货类型{}不匹配，请重新选择仓库！", itemDto.getWarehouseCode(),
                        WmsEnum.ReceiveType.PROCESS_MATERIAL.getRemark());
            }
        });

        final List<Long> idList = rawProductItemList.stream()
                .map(RawReturnProductItemDto::getPurchaseChildOrderRawId)
                .collect(Collectors.toList());
        final List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList = purchaseChildOrderRawDao.getListByIdList(idList);
        if (idList.size() != purchaseChildOrderRawPoList.size()) {
            throw new BizException("找不到对应的原料明细，归还原料失败！");
        }

        // 扣减原料sku数据的原料数量
        final Map<Long, PurchaseChildOrderRawPo> purchaseChildOrderRawPoMap = purchaseChildOrderRawPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderRawPo::getPurchaseChildOrderRawId, Function.identity()));

        final List<PurchaseChildOrderRawPo> updatePoList = rawProductItemList.stream()
                .map(item -> {
                    final PurchaseChildOrderRawPo purchaseChildOrderRawPo = new PurchaseChildOrderRawPo();
                    purchaseChildOrderRawPo.setPurchaseChildOrderRawId(item.getPurchaseChildOrderRawId());
                    purchaseChildOrderRawPo.setVersion(item.getVersion());
                    purchaseChildOrderRawPo.setSku(item.getSku());
                    final PurchaseChildOrderRawPo dbPurchaseChildOrderRawPo = purchaseChildOrderRawPoMap.get(item.getPurchaseChildOrderRawId());
                    final int remainCnt = dbPurchaseChildOrderRawPo.getReceiptCnt() - item.getReturnCnt();
                    if (remainCnt < 0) {
                        throw new BizException("归还数不能超过可归还数");
                    }
                    purchaseChildOrderRawPo.setReceiptCnt(remainCnt);

                    purchaseChildOrderRawPo.setRawExtra(RawExtra.NORMAL);
                    return purchaseChildOrderRawPo;
                }).collect(Collectors.toList());

        purchaseChildOrderRawDao.updateBatchByIdVersion(updatePoList);

        // 区分原料来源处理
        // 我司/其他供应商 -- wms生成原料收货单
        this.heteAndOtherSupplierReturnRaw(rawProductItemList, purchaseChildOrderPo);

        // 供应商 -- 增加供应商对应仓库库存
        this.supplierReturnRaw(rawProductItemList, purchaseChildOrderPo);
    }

    /**
     * 供应商 -- 增加供应商对应仓库库存
     *
     * @param rawProductItemList
     * @param purchaseChildOrderPo
     */
    private void supplierReturnRaw(List<RawReturnProductItemDto> rawProductItemList, PurchaseChildOrderPo purchaseChildOrderPo) {
        // 过滤获取供应商的原料归还数据
        final List<RawReturnProductItemDto> resultRawList = rawProductItemList.stream()
                .filter(itemDto -> RawSupplier.SUPPLIER.equals(itemDto.getRawSupplier()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(resultRawList)) {
            return;
        }

        final List<InventoryChangeItemDto> selfProvideDtoList = SupplierInventoryConverter.convertInventoryDtoToItemDto(rawProductItemList,
                SupplierWarehouse.SELF_PROVIDE, purchaseChildOrderPo.getSupplierCode());
        final List<InventoryChangeItemDto> stockUpDtoList = SupplierInventoryConverter.convertInventoryDtoToItemDto(rawProductItemList,
                SupplierWarehouse.STOCK_UP, purchaseChildOrderPo.getSupplierCode());
        final List<InventoryChangeItemDto> defectiveDtoList = SupplierInventoryConverter.convertInventoryDtoToItemDto(rawProductItemList,
                SupplierWarehouse.DEFECTIVE_WAREHOUSE, purchaseChildOrderPo.getSupplierCode());
        supplierInventoryBaseService.inventoryChange(selfProvideDtoList, SupplierInventoryCtrlType.WAREHOUSING,
                SupplierWarehouse.SELF_PROVIDE, SupplierInventoryCtrlReason.PURCHASE_RAW_RETURN, purchaseChildOrderPo.getPurchaseChildOrderNo());
        supplierInventoryBaseService.inventoryChange(stockUpDtoList, SupplierInventoryCtrlType.WAREHOUSING,
                SupplierWarehouse.STOCK_UP, SupplierInventoryCtrlReason.PURCHASE_RAW_RETURN, purchaseChildOrderPo.getPurchaseChildOrderNo());
        supplierInventoryBaseService.inventoryChange(defectiveDtoList, SupplierInventoryCtrlType.WAREHOUSING,
                SupplierWarehouse.DEFECTIVE_WAREHOUSE, SupplierInventoryCtrlReason.PURCHASE_RAW_RETURN, purchaseChildOrderPo.getPurchaseChildOrderNo());
    }

    /**
     * 我司/其他供应商 -- wms生成原料收货单
     *
     * @param rawProductItemList
     * @param purchaseChildOrderPo
     */
    private void heteAndOtherSupplierReturnRaw(List<RawReturnProductItemDto> rawProductItemList, PurchaseChildOrderPo purchaseChildOrderPo) {
        // 过滤获取我司或者其他供应商的原料归还数据
        final List<RawReturnProductItemDto> resultRawList = rawProductItemList.stream()
                .filter(itemDto -> RawSupplier.HETE.equals(itemDto.getRawSupplier()) || RawSupplier.OTHER_SUPPLIER.equals(itemDto.getRawSupplier()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(resultRawList)) {
            return;
        }

        // wms生成原料收货单
        final List<String> warehouseCodeList = resultRawList.stream()
                .map(RawReturnProductItemDto::getWarehouseCode)
                .distinct()
                .collect(Collectors.toList());
        Map<String, WmsEnum.QcType> warehouseQcTypeMap
                = this.buildWarehouseQcTypeMap(warehouseCodeList, WmsEnum.QcType.SAMPLE_CHECK);

        final List<ReceiveOrderCreateMqDto.ReceiveOrderCreateItem> receiveOrderCreateItemList = resultRawList.stream()
                .map(itemDto -> SupplierPurchaseConverter.rawDtoToReceiptMqDto(itemDto, purchaseChildOrderPo,
                        idGenerateService.getSnowflakeId(), warehouseQcTypeMap))
                .collect(Collectors.toList());
        final ReceiveOrderCreateMqDto receiveOrderCreateMqDto = new ReceiveOrderCreateMqDto();
        receiveOrderCreateMqDto.setReceiveOrderCreateItemList(receiveOrderCreateItemList);
        receiveOrderCreateMqDto.setKey(idGenerateService.getSnowflakeCode(purchaseChildOrderPo.getPurchaseChildOrderNo() + "-"));
        consistencySendMqService.execSendMq(WmsReceiptHandler.class, receiveOrderCreateMqDto);
    }

    public List<PurchaseDeliverPrintVo> printDeliverOrder(PurchaseDeliverNoListDto dto) {
        final List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByNoList(dto.getPurchaseDeliverOrderNoList());
        Assert.notEmpty(purchaseDeliverOrderPoList, () -> new BizException("查找不到对应的采购发货单，请联系系统管理员！"));
        final Map<String, List<PurchaseDeliverOrderItemPo>> deliverNoItemPoMap = purchaseDeliverOrderItemDao.getMapByDeliverOrderNoList(dto.getPurchaseDeliverOrderNoList());
        Assert.notNull(deliverNoItemPoMap, () -> new BizException("查找不到对应的采购发货单，请联系系统管理员！"));

        final List<String> shippingMarkNoList = purchaseDeliverOrderPoList.stream()
                .map(PurchaseDeliverOrderPo::getShippingMarkNo)
                .distinct()
                .collect(Collectors.toList());


        final Map<String, ShippingMarkPo> shippingMarkNoMap = shippingMarkDao.getShippingMarkNoMapByNoList(shippingMarkNoList);

        return purchaseDeliverOrderPoList.stream()
                .map(po -> {
                    final PurchaseDeliverPrintVo deliverPrintVo = new PurchaseDeliverPrintVo();
                    deliverPrintVo.setPurchaseDeliverOrderNo(po.getPurchaseDeliverOrderNo());
                    deliverPrintVo.setPurchaseChildOrderNo(po.getPurchaseChildOrderNo());
                    deliverPrintVo.setShippingMarkNo(po.getShippingMarkNo());
                    final ShippingMarkPo shippingMarkPo = shippingMarkNoMap.get(po.getShippingMarkNo());
                    deliverPrintVo.setBoxCnt(null != shippingMarkPo ? shippingMarkPo.getBoxCnt() : null);
                    deliverPrintVo.setSupplierCode(po.getSupplierCode());
                    deliverPrintVo.setSupplierName(po.getSupplierName());
                    deliverPrintVo.setWarehouseCode(po.getWarehouseCode());
                    deliverPrintVo.setWarehouseName(po.getWarehouseName());
                    deliverPrintVo.setPrintUsername(GlobalContext.getUsername());

                    final List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList = deliverNoItemPoMap.get(po.getPurchaseDeliverOrderNo());
                    final List<PrintDeliverPrintItemVo> deliverPrintItemList = Optional.ofNullable(purchaseDeliverOrderItemPoList)
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(item -> {
                                final PrintDeliverPrintItemVo deliverPrintItemVo = new PrintDeliverPrintItemVo();
                                deliverPrintItemVo.setSku(item.getSku());
                                deliverPrintItemVo.setSkuBatchCode(item.getSkuBatchCode());
                                deliverPrintItemVo.setDeliverCnt(item.getDeliverCnt());
                                return deliverPrintItemVo;
                            }).collect(Collectors.toList());
                    deliverPrintVo.setDeliverPrintItemList(deliverPrintItemList);

                    return deliverPrintVo;
                }).collect(Collectors.toList());
    }


    public void syncReceiptStatus(ReceiveOrderChangeMqDto dto) {
        PurchaseDeliverOrderPo purchaseDeliverOrderPo = purchaseDeliverOrderDao.getOneByNo(dto.getScmBizNo());
        Assert.notNull(purchaseDeliverOrderPo, () -> new BizException("查找不到对应的发货单，同步wms收货单数据失败"));
        PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(purchaseDeliverOrderPo.getPurchaseChildOrderNo());
        Assert.notNull(purchaseChildOrderPo, () -> new BizException("查找不到对应的采购子单，同步wms收货单数据失败"));

        purchaseDeliverBaseService.dealOrderData(dto, purchaseChildOrderPo);
    }

    public CommonPageResult.PageInfo<PurchaseWaitDeliverVo> waitDeliverOrderList(PurchaseWaitDeliverDto dto) {
        if (CollectionUtils.isNotEmpty(dto.getSkuEncodeList())) {
            List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(dto.getSkuEncodeList());
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return new CommonPageResult.PageInfo<>();
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                skuListByEncode.retainAll(dto.getSkuList());
                dto.setSkuList(skuListByEncode);
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                return new CommonPageResult.PageInfo<>();
            }
        }

        if (StringUtils.isNotBlank(dto.getSkuEncode())) {
            List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(List.of(dto.getSkuEncode()));
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return new CommonPageResult.PageInfo<>();
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                skuListByEncode.retainAll(dto.getSkuList());
                dto.setSkuList(skuListByEncode);
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                return new CommonPageResult.PageInfo<>();
            }
        }

        List<String> purchaseDeliverNoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dto.getSkuList())) {
            List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList = purchaseDeliverOrderItemDao.getListBySkuList(dto.getSkuList());

            if (CollectionUtils.isEmpty(purchaseDeliverOrderItemPoList)) {
                return new CommonPageResult.PageInfo<>();
            }

            purchaseDeliverNoList = purchaseDeliverOrderItemPoList.stream()
                    .map(PurchaseDeliverOrderItemPo::getPurchaseDeliverOrderNo)
                    .distinct()
                    .collect(Collectors.toList());

        }
        if (StringUtils.isNotBlank(dto.getSku())) {
            List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList = purchaseDeliverOrderItemDao.getListByLikeSku(dto.getSku());

            if (CollectionUtils.isEmpty(purchaseDeliverOrderItemPoList)) {
                return new CommonPageResult.PageInfo<>();
            }

            List<String> purchaseDeliverNoSkuList = purchaseDeliverOrderItemPoList.stream()
                    .map(PurchaseDeliverOrderItemPo::getPurchaseDeliverOrderNo)
                    .distinct()
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(purchaseDeliverNoList)) {
                purchaseDeliverNoList.addAll(purchaseDeliverNoSkuList);
            } else {
                purchaseDeliverNoList.retainAll(purchaseDeliverNoSkuList);
            }
            if (CollectionUtils.isEmpty(purchaseDeliverNoList)) {
                return new CommonPageResult.PageInfo<>();
            }
        }

        if (StringUtils.isNotEmpty(dto.getSkuBatchCode())) {
            List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPos = purchaseDeliverOrderItemDao.getListByLikeSkuBatchCode(dto.getSkuBatchCode());
            if (CollectionUtils.isEmpty(purchaseDeliverOrderItemPos)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<String> purchaseDeliverNoSkuBatchList = purchaseDeliverOrderItemPos.stream()
                    .map(PurchaseDeliverOrderItemPo::getPurchaseDeliverOrderNo)
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(purchaseDeliverNoList)) {
                purchaseDeliverNoList.addAll(purchaseDeliverNoSkuBatchList);
            } else {
                purchaseDeliverNoList.retainAll(purchaseDeliverNoSkuBatchList);
            }
            if (CollectionUtils.isEmpty(purchaseDeliverNoList)) {
                return new CommonPageResult.PageInfo<>();
            }

        }

        if (CollectionUtils.isNotEmpty(dto.getSkuBatchCodeList())) {
            List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPos = purchaseDeliverOrderItemDao.getListBySkuBatchCodeList(dto.getSkuBatchCodeList());
            if (CollectionUtils.isEmpty(purchaseDeliverOrderItemPos)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<String> purchaseDeliverNoSkuBatchList = purchaseDeliverOrderItemPos.stream()
                    .map(PurchaseDeliverOrderItemPo::getPurchaseDeliverOrderNo)
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(purchaseDeliverNoList)) {
                purchaseDeliverNoList.addAll(purchaseDeliverNoSkuBatchList);
            } else {
                purchaseDeliverNoList.retainAll(purchaseDeliverNoSkuBatchList);
            }
            if (CollectionUtils.isEmpty(purchaseDeliverNoList)) {
                return new CommonPageResult.PageInfo<>();
            }
        }

        if (CollectionUtils.isNotEmpty(dto.getSupplierProductNameList())) {
            List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getBatchProductNameOrSupplierCode(dto.getSupplierProductNameList(), dto.getAuthSupplierCode());
            if (CollectionUtils.isEmpty(supplierProductComparePoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<String> supplierNos = purchaseDeliverBaseService.getNoListBySupplierProduct(supplierProductComparePoList);
            if (CollectionUtils.isEmpty(purchaseDeliverNoList)) {
                purchaseDeliverNoList.addAll(supplierNos);
            } else {
                purchaseDeliverNoList.retainAll(supplierNos);
            }
            if (CollectionUtils.isEmpty(purchaseDeliverNoList)) {
                return new CommonPageResult.PageInfo<>();
            }
        }

        if (StringUtils.isNotBlank(dto.getSupplierProductName())) {
            List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getByLikeProductNameOrSupplierCode(dto.getSupplierProductName(), dto.getAuthSupplierCode());
            if (CollectionUtils.isEmpty(supplierProductComparePoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<String> supplierNos = purchaseDeliverBaseService.getNoListBySupplierProduct(supplierProductComparePoList);
            if (CollectionUtils.isEmpty(purchaseDeliverNoList)) {
                purchaseDeliverNoList.addAll(supplierNos);
            } else {
                purchaseDeliverNoList.retainAll(supplierNos);
            }
            if (CollectionUtils.isEmpty(purchaseDeliverNoList)) {
                return new CommonPageResult.PageInfo<>();
            }
        }

        final IPage<PurchaseDeliverOrderPo> page = purchaseDeliverOrderDao.waitDeliverOrderList(PageDTO.of(dto.getPageNo(), dto.getPageSize()),
                dto, purchaseDeliverNoList);

        final List<PurchaseDeliverOrderPo> records = page.getRecords();
        final List<String> purchaseDeliverOrderNoList = records.stream()
                .map(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo)
                .distinct()
                .collect(Collectors.toList());
        final List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList = purchaseDeliverOrderItemDao.getListByDeliverOrderNoList(purchaseDeliverOrderNoList);
        final Map<String, List<PurchaseDeliverOrderItemPo>> purchaseDeliverOrderNoMap = purchaseDeliverOrderItemPoList.stream()
                .collect(Collectors.groupingBy(PurchaseDeliverOrderItemPo::getPurchaseDeliverOrderNo));
        final List<String> skuList = purchaseDeliverOrderItemPoList.stream()
                .map(PurchaseDeliverOrderItemPo::getSku)
                .distinct()
                .collect(Collectors.toList());

        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        Map<String, SupplierProductComparePo> supplierProductCompareMap = supplierProductCompareDao.getBatchSkuMap(skuList);

        final List<PurchaseWaitDeliverVo> purchaseWaitDeliverVoList = SupplierPurchaseConverter.waitDeliverPoListToVoList(records, purchaseDeliverOrderNoMap, skuEncodeMap, supplierProductCompareMap);

        return PageInfoUtil.getPageInfo(page, purchaseWaitDeliverVoList);
    }

    @Transactional(rollbackFor = Exception.class)
    public ShippingMarkNoVo createShippingMark(ShippingMarkDto dto) {
        final String shippingMarkNo = idGenerateService.getConfuseCode(ScmConstant.SHIPPING_MARK_PREFIX, TimeType.CN_DAY_YYYY, ConfuseLength.L_4);

        final List<ShippingMarkItemDto> shippingMarkItemList = dto.getShippingMarkItemList();
        final List<String> deliverOrderNoList = shippingMarkItemList.stream()
                .map(ShippingMarkItemDto::getDeliverOrderNo)
                .distinct()
                .collect(Collectors.toList());

        if (ShippingMarkBizType.PURCHASE_CHILD.equals(dto.getShippingMarkBizType())) {
            this.updatePurchaseDeliver(shippingMarkNo, shippingMarkItemList, deliverOrderNoList);
        } else if (ShippingMarkBizType.SAMPLE_CHILD.equals(dto.getShippingMarkBizType())) {
            this.updateSampleShippingMark(shippingMarkNo, shippingMarkItemList, deliverOrderNoList);
        } else {
            throw new BizException("错误的箱唛类型，请联系系统管理员！");
        }

        final ShippingMarkPo shippingMarkPo = new ShippingMarkPo();
        shippingMarkPo.setShippingMarkNo(shippingMarkNo);
        shippingMarkPo.setShippingMarkStatus(ShippingMarkStatus.WAIT_DELIVER);
        shippingMarkPo.setShippingMarkBizType(dto.getShippingMarkBizType());
        shippingMarkPo.setWarehouseCode(dto.getWarehouseCode());
        shippingMarkPo.setWarehouseName(dto.getWarehouseName());
        shippingMarkPo.setWarehouseTypes(String.join(",", Optional.ofNullable(dto.getWarehouseTypeList())
                .orElse(new ArrayList<>())));
        shippingMarkPo.setIsDirectSend(WarehouseBaseService.getIsDirectSendByWarehouseType(dto.getWarehouseTypeList()));
        shippingMarkPo.setSupplierCode(dto.getSupplierCode());
        shippingMarkPo.setSupplierName(dto.getSupplierName());
        shippingMarkPo.setTotalDeliver(dto.getTotalDeliver());
        shippingMarkPo.setBoxCnt(dto.getBoxCnt());

        shippingMarkDao.insert(shippingMarkPo);

        logBaseService.simpleLog(LogBizModule.SHIPPING_MARK, ScmConstant.PURCHASE_LOG_VERSION,
                shippingMarkNo, ShippingMarkStatus.WAIT_DELIVER.getRemark(), Collections.emptyList());


        final List<ShippingMarkItemPo> shippingMarkItemPoList = shippingMarkItemList.stream()
                .map(item -> {
                    final ShippingMarkItemPo shippingMarkItemPo = new ShippingMarkItemPo();
                    shippingMarkItemPo.setShippingMarkNo(shippingMarkNo);
                    shippingMarkItemPo.setShippingMarkNum(item.getShippingMarkNum());
                    shippingMarkItemPo.setDeliverOrderNo(item.getDeliverOrderNo());
                    shippingMarkItemPo.setBizChildOrderNo(item.getBizChildOrderNo());
                    shippingMarkItemPo.setDeliverCnt(item.getDeliverCnt());
                    return shippingMarkItemPo;
                }).collect(Collectors.toList());

        shippingMarkItemDao.insertBatch(shippingMarkItemPoList);

        final ShippingMarkNoVo shippingMarkNoVo = new ShippingMarkNoVo();
        shippingMarkNoVo.setShippingMarkNo(shippingMarkNo);
        return shippingMarkNoVo;
    }


    private void updatePurchaseDeliver(String shippingMarkNo, List<ShippingMarkItemDto> shippingMarkItemList, List<String> deliverOrderNoList) {
        final List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByNoList(deliverOrderNoList);
        if (deliverOrderNoList.size() != purchaseDeliverOrderPoList.size()) {
            throw new BizException("查找不到对应的发货单，创建箱唛失败，请联系系统管理员！");
        }
        // 校验拆箱后的发货数是否正确
        this.checkPurchaseDeliverCnt(shippingMarkItemList, purchaseDeliverOrderPoList);
        // 校验发货单是否已经生成箱唛
        this.checkPurchaseDeliverOrderHasShippingMark(purchaseDeliverOrderPoList);

        // 更新发货单
        purchaseDeliverOrderPoList.forEach(po -> {
            po.setHasShippingMark(BooleanType.TRUE);
            po.setShippingMarkNo(shippingMarkNo);
        });
        purchaseDeliverOrderDao.updateBatchByIdVersion(purchaseDeliverOrderPoList);
    }

    private void updateSampleShippingMark(String shippingMarkNo, List<ShippingMarkItemDto> shippingMarkItemList, List<String> deliverOrderNoList) {
        final List<SampleDeliverOrderPo> sampleDeliverOrderPoList = sampleDeliverOrderDao.getDeliverPoListByNoList(deliverOrderNoList);
        if (deliverOrderNoList.size() != sampleDeliverOrderPoList.size()) {
            throw new BizException("查找不到对应的发货单，创建箱唛失败，请联系系统管理员！");
        }
        // 校验拆箱后的发货数是否正确
        this.checkSampleDeliverCnt(shippingMarkItemList, sampleDeliverOrderPoList);
        // 校验发货单是否已经生成箱唛
        this.checkSampleDeliverOrderHasShippingMark(sampleDeliverOrderPoList);

        // 更新发货单
        sampleDeliverOrderPoList.forEach(po -> {
            po.setHasShippingMark(BooleanType.TRUE);
            po.setShippingMarkNo(shippingMarkNo);
        });
        sampleDeliverOrderDao.updateBatchByIdVersion(sampleDeliverOrderPoList);
    }


    @Transactional(rollbackFor = Exception.class)
    public ShippingMarkNoVo createOverSeasShippingMark(OverSeasShippingMarkDto dto) {
        final PurchaseDeliverOrderPo purchaseDeliverOrderPo = purchaseDeliverOrderDao.getOneByNo(dto.getDeliverOrderNo());
        Assert.notNull(purchaseDeliverOrderPo, () -> new BizException("找不到对应的发货单，请联系系统管理员处理！"));
        final List<ShippingMarkItemPo> shippingMarkItemPoList = shippingMarkItemDao.getListByDeliverOrderNo(dto.getDeliverOrderNo());
        if (CollectionUtils.isNotEmpty(shippingMarkItemPoList)) {
            throw new ParamIllegalException("当前发货单已经生成了箱唛，请勿重复操作！");
        }
        if (StringUtils.isBlank(dto.getShippingMarkNo())) {
            throw new ParamIllegalException("箱唛号不能为空，请联系采购员更新海外仓箱唛号以及海外仓文件！");
        }

        final ShippingMarkPo existShippingMarkPo = shippingMarkDao.getOneByNo(dto.getShippingMarkNo());
        if (null != existShippingMarkPo) {
            throw new ParamIllegalException("当前箱唛号{}已经生成过箱唛，请联系采购员更新海外仓箱唛号以及海外仓文件！", dto.getShippingMarkNo());
        }
        if (StringUtils.isBlank(purchaseDeliverOrderPo.getShippingMarkNo())) {
            throw new ParamIllegalException("当前发货单没有关联箱唛号，请联系采购员更新海外仓箱唛号以及海外仓文件！");
        }

        purchaseDeliverOrderPo.setHasShippingMark(BooleanType.TRUE);
        purchaseDeliverOrderDao.updateByIdVersion(purchaseDeliverOrderPo);

        final ShippingMarkPo shippingMarkPo = new ShippingMarkPo();
        shippingMarkPo.setShippingMarkNo(dto.getShippingMarkNo());
        shippingMarkPo.setShippingMarkStatus(ShippingMarkStatus.WAIT_DELIVER);
        shippingMarkPo.setShippingMarkBizType(dto.getShippingMarkBizType());
        shippingMarkPo.setWarehouseCode(dto.getWarehouseCode());
        shippingMarkPo.setWarehouseName(dto.getWarehouseName());
        shippingMarkPo.setWarehouseTypes(String.join(",", Optional.ofNullable(dto.getWarehouseTypeList())
                .orElse(new ArrayList<>())));
        shippingMarkPo.setIsDirectSend(WarehouseBaseService.getIsDirectSendByWarehouseType(dto.getWarehouseTypeList()));
        shippingMarkPo.setSupplierCode(dto.getSupplierCode());
        shippingMarkPo.setSupplierName(dto.getSupplierName());

        shippingMarkDao.insert(shippingMarkPo);

        final ShippingMarkItemPo shippingMarkItemPo = new ShippingMarkItemPo();
        shippingMarkItemPo.setShippingMarkNo(dto.getShippingMarkNo());
        shippingMarkItemPo.setDeliverOrderNo(dto.getDeliverOrderNo());
        shippingMarkItemPo.setBizChildOrderNo(dto.getBizChildOrderNo());

        shippingMarkItemDao.insert(shippingMarkItemPo);

        final ShippingMarkNoVo shippingMarkNoVo = new ShippingMarkNoVo();
        shippingMarkNoVo.setShippingMarkNo(dto.getShippingMarkNo());
        return shippingMarkNoVo;
    }

    private void checkPurchaseDeliverOrderHasShippingMark(List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList) {
        purchaseDeliverOrderPoList.forEach(po -> {
            if (BooleanType.TRUE.equals(po.getHasShippingMark())) {
                throw new ParamIllegalException("该发货单:{}，已经生成了箱唛，请刷新后重新操作一次！", po.getPurchaseDeliverOrderNo());
            }
        });
    }

    private void checkSampleDeliverOrderHasShippingMark(List<SampleDeliverOrderPo> purchaseDeliverOrderPoList) {
        purchaseDeliverOrderPoList.forEach(po -> {
            if (BooleanType.TRUE.equals(po.getHasShippingMark())) {
                throw new ParamIllegalException("该发货单:{}，已经生成了箱唛，请刷新后重新操作一次！", po.getSampleDeliverOrderNo());
            }
        });
    }

    private void checkPurchaseDeliverCnt(List<ShippingMarkItemDto> shippingMarkItemList,
                                         List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList) {
        final Map<String, Integer> deliverNoCntMap = shippingMarkItemList.stream()
                .collect(Collectors.groupingBy(ShippingMarkItemDto::getDeliverOrderNo,
                        Collectors.summingInt(ShippingMarkItemDto::getDeliverCnt)));

        purchaseDeliverOrderPoList.forEach(po -> {
            final Integer deliverCnt = deliverNoCntMap.get(po.getPurchaseDeliverOrderNo());
            Assert.notNull(deliverCnt, () -> new ParamIllegalException("填写拆箱发货数有误，请重新填写！"));
            if (!deliverCnt.equals(po.getDeliverCnt())) {
                throw new ParamIllegalException("采购发货单号：{}的箱唛发货总数有误，请检查一下该单号的总发货数后，重新填写！",
                        po.getPurchaseDeliverOrderNo());
            }
        });
    }

    private void checkSampleDeliverCnt(List<ShippingMarkItemDto> shippingMarkItemList,
                                       List<SampleDeliverOrderPo> sampleDeliverOrderPoList) {
        final Map<String, Integer> deliverNoCntMap = shippingMarkItemList.stream()
                .collect(Collectors.groupingBy(ShippingMarkItemDto::getDeliverOrderNo,
                        Collectors.summingInt(ShippingMarkItemDto::getDeliverCnt)));

        sampleDeliverOrderPoList.forEach(po -> {
            final Integer deliverCnt = deliverNoCntMap.get(po.getSampleDeliverOrderNo());
            Assert.notNull(deliverCnt, () -> new ParamIllegalException("填写拆箱发货数有误，请重新填写！"));
            if (!deliverCnt.equals(po.getTotalDeliver())) {
                throw new ParamIllegalException("样品发货单号：{}的箱唛发货总数有误，请检查一下该单号的总发货数后，重新填写！",
                        po.getSampleDeliverOrderNo());
            }
        });
    }

    public CommonPageResult.PageInfo<ShippingMarkListVo> shippingMarkList(ShippingMarkListDto dto) {
        List<String> shippingMarkNoList = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getDeliverOrderNo())) {
            List<ShippingMarkItemPo> shippingMarkItemPoList = shippingMarkItemDao.getListByLikeDeliverOrderNo(dto.getDeliverOrderNo());
            if (CollectionUtils.isEmpty(shippingMarkItemPoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            shippingMarkNoList = shippingMarkItemPoList.stream()
                    .map(ShippingMarkItemPo::getShippingMarkNo)
                    .collect(Collectors.toList());
        }

        List<String> bizChildOrderNoList = CollectionUtils.isEmpty(dto.getBizChildOrderNoList()) ? new ArrayList<>() : dto.getBizChildOrderNoList();

        //批次码批量查询
        if (CollectionUtils.isNotEmpty(dto.getSkuBatchCodeList())) {
            List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListBySkuBatchCode(dto.getSkuBatchCodeList());
            if (CollectionUtils.isNotEmpty(purchaseChildOrderItemPoList)) {
                List<String> purchaseChildOrderNoList = purchaseChildOrderItemPoList.stream().map(PurchaseChildOrderItemPo::getPurchaseChildOrderNo).distinct().collect(Collectors.toList());
                bizChildOrderNoList.addAll(purchaseChildOrderNoList);
            }

            List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getListBySkuBatchCode(dto.getSkuBatchCodeList());
            if (CollectionUtils.isNotEmpty(sampleChildOrderPoList)) {
                bizChildOrderNoList.addAll(sampleChildOrderPoList.stream().map(SampleChildOrderPo::getSampleChildOrderNo).distinct().collect(Collectors.toList()));
            }
            if (CollectionUtils.isEmpty(bizChildOrderNoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<ShippingMarkItemPo> shippingMarkItemPos = shippingMarkItemDao.getListByBizChildOrderNo(bizChildOrderNoList);
            if (CollectionUtils.isEmpty(shippingMarkItemPos)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<String> shippingMarkNos = shippingMarkItemPos.stream()
                    .map(ShippingMarkItemPo::getShippingMarkNo)
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(shippingMarkNoList)) {
                shippingMarkNoList.addAll(shippingMarkNos);
            } else {
                shippingMarkNoList.retainAll(shippingMarkNos);
            }
        }

        //批次码查询
        if (StringUtils.isNotBlank(dto.getSkuBatchCode())) {
            List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByLikeSkuBatchCode(dto.getSkuBatchCode());
            if (CollectionUtils.isNotEmpty(purchaseChildOrderItemPoList)) {
                List<String> purchaseChildOrderNoList = purchaseChildOrderItemPoList.stream().map(PurchaseChildOrderItemPo::getPurchaseChildOrderNo).distinct().collect(Collectors.toList());
                bizChildOrderNoList.addAll(purchaseChildOrderNoList);
            }

            List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getListByLikeSkuBatchCode(dto.getSkuBatchCode());
            if (CollectionUtils.isNotEmpty(sampleChildOrderPoList)) {
                bizChildOrderNoList.addAll(sampleChildOrderPoList.stream().map(SampleChildOrderPo::getSampleChildOrderNo).distinct().collect(Collectors.toList()));
            }
            if (CollectionUtils.isEmpty(bizChildOrderNoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<ShippingMarkItemPo> shippingMarkItemPos = shippingMarkItemDao.getListByBizChildOrderNo(bizChildOrderNoList);
            if (CollectionUtils.isEmpty(shippingMarkItemPos)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<String> shippingMarkNos = shippingMarkItemPos.stream()
                    .map(ShippingMarkItemPo::getShippingMarkNo)
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(shippingMarkNoList)) {
                shippingMarkNoList.addAll(shippingMarkNos);
            } else {
                shippingMarkNoList.retainAll(shippingMarkNos);
            }
        }


        final IPage<ShippingMarkPo> page = shippingMarkDao.shippingMarkList(PageDTO.of(dto.getPageNo(), dto.getPageSize()),
                dto, shippingMarkNoList);


        final List<ShippingMarkListVo> shippingMarkListVoList = page.getRecords().stream()
                .map(record -> {
                    final ShippingMarkListVo shippingMarkListVo = new ShippingMarkListVo();
                    shippingMarkListVo.setShippingMarkId(record.getShippingMarkId());
                    shippingMarkListVo.setVersion(record.getVersion());
                    shippingMarkListVo.setShippingMarkNo(record.getShippingMarkNo());
                    shippingMarkListVo.setShippingMarkStatus(record.getShippingMarkStatus());
                    shippingMarkListVo.setWarehouseCode(record.getWarehouseCode());
                    shippingMarkListVo.setWarehouseName(record.getWarehouseName());
                    shippingMarkListVo.setWarehouseTypeList(FormatStringUtil.string2List(record.getWarehouseTypes(), ","));
                    shippingMarkListVo.setIsDirectSend(record.getIsDirectSend());
                    shippingMarkListVo.setSupplierCode(record.getSupplierCode());
                    shippingMarkListVo.setSupplierName(record.getSupplierName());
                    shippingMarkListVo.setTotalDeliver(record.getTotalDeliver());
                    shippingMarkListVo.setBoxCnt(record.getBoxCnt());
                    shippingMarkListVo.setCreateTime(record.getCreateTime());
                    shippingMarkListVo.setDeliverUser(record.getDeliverUser());
                    shippingMarkListVo.setDeliverUsername(record.getDeliverUsername());
                    shippingMarkListVo.setDeliverTime(record.getDeliverTime());
                    return shippingMarkListVo;
                }).collect(Collectors.toList());

        return PageInfoUtil.getPageInfo(page, shippingMarkListVoList);
    }


    public List<ShippingMarkDetailVo> printShippingMark(ShippingMarkNoListDto dto) {
        List<ShippingMarkPo> shippingMarkPoList = shippingMarkDao.getListByNoList(dto.getShippingMarkNoList());
        Assert.notEmpty(shippingMarkPoList, () -> new ParamIllegalException("查找不到对应的箱唛，请刷新后重试！"));
        final Map<String, List<ShippingMarkItemPo>> shippingMarkNoItemMap = shippingMarkItemDao.getShippingMarkNoItemMapByNoList(dto.getShippingMarkNoList());

        return shippingMarkPoList.stream().map(po -> {
            final ShippingMarkDetailVo shippingMarkDetailVo = SupplierShippingMarkConverter.getShippingMarkDetailVoByPo(po);

            final List<ShippingMarkItemPo> shippingMarkItemPoList = shippingMarkNoItemMap.get(po.getShippingMarkNo());
            final List<ShippingMarkItemDetailVo> shippingMarkItemDetailList = SupplierShippingMarkConverter.getPrintShippingMarkItemDetailVoListByPoList(shippingMarkItemPoList);
            shippingMarkDetailVo.setShippingMarkItemList(shippingMarkItemDetailList);
            shippingMarkDetailVo.setPrintUsername(GlobalContext.getUsername());

            return shippingMarkDetailVo;

        }).collect(Collectors.toList());
    }

    public ShippingMarkDetailVo shippingMarkDetail(ShippingMarkNoDto dto) {
        // 兼容运单号查找箱唛
        ShippingMarkPo shippingMarkPo = shippingMarkDao.getOneByNoAndStatus(dto.getShippingMarkNo(),
                Arrays.asList(ShippingMarkStatus.WAIT_DELIVER, ShippingMarkStatus.DELIVERED));
        if (null == shippingMarkPo) {
            shippingMarkPo = shippingMarkDao.getOneByTrackingNo(dto.getShippingMarkNo());
        }
        Assert.notNull(shippingMarkPo, () -> new ParamIllegalException("查找不到对应的箱唛，请刷新后重试！"));


        final ShippingMarkDetailVo shippingMarkDetailVo = SupplierShippingMarkConverter.getShippingMarkDetailVoByPo(shippingMarkPo);

        final List<ShippingMarkItemPo> shippingMarkItemPoList = shippingMarkItemDao.getListByNo(dto.getShippingMarkNo());
        if (CollectionUtils.isEmpty(shippingMarkItemPoList)) {
            return shippingMarkDetailVo;
        }
        final List<String> deliverOrderNoList = shippingMarkItemPoList.stream()
                .map(ShippingMarkItemPo::getDeliverOrderNo)
                .distinct()
                .collect(Collectors.toList());
        final List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList = purchaseDeliverOrderItemDao.getListByDeliverOrderNoList(deliverOrderNoList);
        final Map<String, List<PurchaseDeliverOrderItemPo>> purchaseDeliverOrderNoMap = purchaseDeliverOrderItemPoList.stream()
                .collect(Collectors.groupingBy(PurchaseDeliverOrderItemPo::getPurchaseDeliverOrderNo));
        final List<String> skuList = purchaseDeliverOrderItemPoList.stream()
                .map(PurchaseDeliverOrderItemPo::getSku)
                .distinct()
                .collect(Collectors.toList());

        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        Map<String, SupplierProductComparePo> supplierProductCompareMap = supplierProductCompareDao.getBatchSkuMap(skuList);


        final List<ShippingMarkItemDetailVo> shippingMarkItemDetailList = SupplierShippingMarkConverter.getShippingMarkItemDetailVoListByPoList(shippingMarkItemPoList,
                purchaseDeliverOrderNoMap, shippingMarkPo.getSupplierCode(), skuEncodeMap, supplierProductCompareMap);

        shippingMarkDetailVo.setShippingMarkItemList(shippingMarkItemDetailList);
        shippingMarkDetailVo.setPrintUsername(GlobalContext.getUsername());

        final OverseasWarehouseMsgVo overseasWarehouseMsgVo = overseasBizService.getOverseasMsgByOverseasShippingMarkNo(shippingMarkPo.getShippingMarkNo());
        shippingMarkDetailVo.setOverseasWarehouseMsgVo(overseasWarehouseMsgVo);

        // 是否加急
        final List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByNoList(deliverOrderNoList);
        final List<String> purchaseChildOrderNoList = purchaseDeliverOrderPoList.stream()
                .map(PurchaseDeliverOrderPo::getPurchaseChildOrderNo)
                .distinct()
                .collect(Collectors.toList());
        shippingMarkDetailVo.setIsUrgentOrder(BooleanType.FALSE);
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList);
        for (PurchaseChildOrderPo purchaseChildOrderPo : purchaseChildOrderPoList) {
            if (BooleanType.TRUE.equals(purchaseChildOrderPo.getIsUrgentOrder())) {
                shippingMarkDetailVo.setIsUrgentOrder(BooleanType.TRUE);
                break;
            }
        }

        return shippingMarkDetailVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelShippingMark(ShippingMarkIdAndVersionDto dto) {
        final ShippingMarkPo shippingMarkPo = shippingMarkDao.getByIdVersion(dto.getShippingMarkId(), dto.getVersion());
        Assert.notNull(shippingMarkPo, () -> new ParamIllegalException("查找不到箱唛，请刷新后重试！"));
        final List<ShippingMarkItemPo> shippingMarkItemPoList = shippingMarkItemDao.getListByNo(shippingMarkPo.getShippingMarkNo());
        Assert.notEmpty(shippingMarkItemPoList, () -> new ParamIllegalException("查找不到箱唛，请刷新后重试！"));

        final List<String> deliverOrderNoList = shippingMarkItemPoList.stream()
                .map(ShippingMarkItemPo::getDeliverOrderNo)
                .distinct()
                .collect(Collectors.toList());
        if (ShippingMarkBizType.PURCHASE_CHILD.equals(shippingMarkPo.getShippingMarkBizType())) {
            this.removePurchaseDeliver(deliverOrderNoList);
        } else if (ShippingMarkBizType.SAMPLE_CHILD.equals(shippingMarkPo.getShippingMarkBizType())) {
            this.removeSampleDeliver(deliverOrderNoList);
        }


        shippingMarkPo.setShippingMarkStatus(ShippingMarkStatus.DELETED);
        shippingMarkDao.updateByIdVersion(shippingMarkPo);
        final List<Long> idList = shippingMarkItemPoList.stream()
                .map(ShippingMarkItemPo::getShippingMarkItemId)
                .collect(Collectors.toList());
        shippingMarkItemDao.removeBatchByIds(idList);

        logBaseService.simpleLog(LogBizModule.SHIPPING_MARK, ScmConstant.PURCHASE_LOG_VERSION,
                shippingMarkPo.getShippingMarkNo(), ShippingMarkStatus.DELETED.getRemark(), Collections.emptyList());
    }

    /**
     * 作废采购发货单
     *
     * @param deliverOrderNoList
     */
    private void removeSampleDeliver(List<String> deliverOrderNoList) {
        final List<SampleDeliverOrderPo> sampleDeliverOrderPoList = sampleDeliverOrderDao.getDeliverPoListByNoList(deliverOrderNoList);
        if (deliverOrderNoList.size() != sampleDeliverOrderPoList.size()) {
            throw new BizException("查找不到对应的样品发货单！");
        }

        // 清空发货单的箱号以及生成箱唛标记
        sampleDeliverOrderPoList.forEach(po -> {
            po.setShippingMarkNo(StringUtils.EMPTY);
            po.setHasShippingMark(BooleanType.FALSE);
        });
        sampleDeliverOrderDao.updateBatchByIdVersion(sampleDeliverOrderPoList);
    }

    /**
     * 作废采购发货单
     *
     * @param deliverOrderNoList
     */
    private void removePurchaseDeliver(List<String> deliverOrderNoList) {

        final List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByNoList(deliverOrderNoList);
        if (deliverOrderNoList.size() != purchaseDeliverOrderPoList.size()) {
            throw new BizException("查找不到对应的采购发货单！");
        }
        // 清空发货单的箱号以及生成箱唛标记
        purchaseDeliverOrderPoList.forEach(po -> {
            // 国内仓清空发货单的箱唛号
            if (BooleanType.FALSE.equals(po.getIsDirectSend())) {
                po.setShippingMarkNo(StringUtils.EMPTY);
            }
            po.setHasShippingMark(BooleanType.FALSE);
        });
        purchaseDeliverOrderDao.updateBatchByIdVersion(purchaseDeliverOrderPoList);
    }

    public ShippingMarkDetailVo h5ShippingMarkDetail(ShippingMarkNumDto dto) {
        final String[] shippingMarkNum = dto.getShippingMarkNum().split("-");
        // 兼容旧的条形码扫码
        if (shippingMarkNum.length < SHIPPING_STANDARD_LENGTH) {
            final ShippingMarkNoDto shippingMarkNoDto = new ShippingMarkNoDto();
            shippingMarkNoDto.setShippingMarkNo(shippingMarkNum[0]);
            return this.shippingMarkDetail(shippingMarkNoDto);
        }

        final ShippingMarkItemPo shippingMarkItemPo = shippingMarkItemDao.getOneByShippingMarkNum(shippingMarkNum[0], shippingMarkNum[1]);
        if (null == shippingMarkItemPo) {
            throw new ParamIllegalException("箱唛号：{}，找不到对应的箱唛，请重新扫码！", dto.getShippingMarkNum());
        }

        final ShippingMarkNoDto shippingMarkNoDto = new ShippingMarkNoDto();
        shippingMarkNoDto.setShippingMarkNo(shippingMarkNum[0]);
        return this.shippingMarkDetail(shippingMarkNoDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void syncModifyReceiptStatus(ReceiveOrderChangeMqDto dto) {
        final PurchaseModifyOrderPo purchaseModifyOrderPo = purchaseModifyOrderDao.getOneByChildOrderNo(dto.getScmBizNo());
        Assert.notNull(purchaseModifyOrderPo, () -> new BizException("查找不到对应的采购变更需求单，同步wms收货单数据失败"));
        purchaseModifyOrderPo.setDownReturnOrderStatus(ReceiptOrderStatus.RECEIPTED);
        purchaseModifyOrderDao.updateByIdVersion(purchaseModifyOrderPo);
    }

    public List<SkuBatchCodeQuickSearchVo> getSkuBatchCodeQuickSearch(SkuBatchCodeQuickSearchDto dto) {
        List<SkuBatchCodeQuickSearchVo> list = new ArrayList<>();
        Set<String> skuBatchCodeSet = new TreeSet<>();
        List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByLikeSkuBatchCode(dto.getSearchContent());
        Map<String, List<ShippingMarkItemPo>> shippingMarkItemPoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(purchaseChildOrderItemPoList)) {
            shippingMarkItemPoMap = shippingMarkItemDao.getListByBizChildOrderNo(purchaseChildOrderItemPoList.stream()
                            .map(PurchaseChildOrderItemPo::getPurchaseChildOrderNo)
                            .distinct().collect(Collectors.toList())).stream()
                    .collect(Collectors.groupingBy(ShippingMarkItemPo::getBizChildOrderNo));
        }
        for (PurchaseChildOrderItemPo purchaseChildOrderItemPo : purchaseChildOrderItemPoList) {
            if (StringUtils.isNotBlank(purchaseChildOrderItemPo.getSkuBatchCode())
                    && shippingMarkItemPoMap.containsKey(purchaseChildOrderItemPo.getPurchaseChildOrderNo())) {
                skuBatchCodeSet.add(purchaseChildOrderItemPo.getSkuBatchCode());
            }
        }
        List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getListByLikeSkuBatchCode(dto.getSearchContent());
        if (CollectionUtils.isNotEmpty(sampleChildOrderPoList)) {
            shippingMarkItemPoMap = shippingMarkItemDao.getListByBizChildOrderNo(sampleChildOrderPoList.stream()
                            .map(SampleChildOrderPo::getSampleChildOrderNo)
                            .distinct().collect(Collectors.toList())).stream()
                    .collect(Collectors.groupingBy(ShippingMarkItemPo::getBizChildOrderNo));
        }
        for (SampleChildOrderPo sampleChildOrderPo : sampleChildOrderPoList) {
            if (StringUtils.isNotBlank(sampleChildOrderPo.getSkuBatchCode())
                    && shippingMarkItemPoMap.containsKey(sampleChildOrderPo.getSampleChildOrderNo())) {
                skuBatchCodeSet.add(sampleChildOrderPo.getSkuBatchCode());
            }
        }
        for (String skuBatchCode : skuBatchCodeSet) {
            SkuBatchCodeQuickSearchVo skuBatchCodeQuickSearchVo = new SkuBatchCodeQuickSearchVo();
            skuBatchCodeQuickSearchVo.setSearchFieldName(skuBatchCode);
            skuBatchCodeQuickSearchVo.setConditionFieldName(skuBatchCode);
            list.add(skuBatchCodeQuickSearchVo);
        }
        return list;
    }


}
