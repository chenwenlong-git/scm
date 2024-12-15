package com.hete.supply.scm.server.scm.settle.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseSettleOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseSettleOrderExportVo;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseSettleOrderSkuExportVo;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.entity.bo.ReceiveOrderForScmBo;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.purchase.converter.PurchaseSettleOrderPayConverter;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseChildOrderExportBo;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseBaseService;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderPo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderResultPo;
import com.hete.supply.scm.server.scm.sample.service.base.SampleBaseService;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.settle.config.SettleConfig;
import com.hete.supply.scm.server.scm.settle.dao.*;
import com.hete.supply.scm.server.scm.settle.entity.bo.DeductOrderExportBo;
import com.hete.supply.scm.server.scm.settle.entity.bo.SupplementOrderPurchaseExportBo;
import com.hete.supply.scm.server.scm.settle.entity.dto.*;
import com.hete.supply.scm.server.scm.settle.entity.po.*;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleOrderDetailVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleOrderProductVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleOrderVo;
import com.hete.supply.scm.server.scm.settle.service.base.DeductOrderBaseService;
import com.hete.supply.scm.server.scm.settle.service.base.PurchaseSettleOrderBaseService;
import com.hete.supply.scm.server.scm.settle.service.base.SupplementOrderBaseService;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.entry.entity.dto.ReceiveOrderGetDto;
import com.hete.supply.wms.api.entry.entity.vo.ReceiveOrderForScmVo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.core.holder.GlobalContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2022/11/1 13:44
 */
@Service
@RequiredArgsConstructor
public class PurchaseSettleOrderBizService {

    private final PurchaseSettleOrderDao purchaseSettleOrderDao;
    private final PurchaseSettleOrderPayDao purchaseSettleOrderPayDao;
    private final PurchaseSettleOrderItemDao purchaseSettleOrderItemDao;
    private final PurchaseSettleOrderBaseService purchaseSettleOrderBaseService;
    private final ScmImageBaseService scmImageBaseService;
    private final PurchaseBaseService purchaseBaseService;
    private final SupplementOrderBaseService supplementOrderBaseService;
    private final DeductOrderBaseService deductOrderBaseService;
    private final SampleBaseService sampleBaseService;
    private final AuthBaseService authBaseService;
    private final WmsRemoteService wmsRemoteService;
    private final PurchaseDeliverOrderDao purchaseDeliverOrderDao;
    private final PurchaseDeliverOrderItemDao purchaseDeliverOrderItemDao;
    private final PlmRemoteService plmRemoteService;
    private final PurchaseReturnOrderDao purchaseReturnOrderDao;
    private final DeductOrderDao deductOrderDao;
    private final SupplementOrderDao supplementOrderDao;
    private final SettleConfig settleConfig;

    public CommonPageResult.PageInfo<PurchaseSettleOrderVo> searchPurchaseSettleOrder(PurchaseSettleOrderSearchDto purchaseSettleOrderSearchDto) {
        return purchaseSettleOrderBaseService.searchPurchaseSettleOrder(purchaseSettleOrderSearchDto);
    }

    public PurchaseSettleOrderDetailVo getPurchaseSettleOrderDetail(PurchaseSettleOrderDetailDto dto) {
        return purchaseSettleOrderBaseService.getPurchaseSettleOrderDetail(dto);
    }


    public PurchaseSettleOrderProductVo searchPurchaseSettleOrderProduct(PurchaseSettleOrderProductDto dto) {
        return purchaseSettleOrderBaseService.searchPurchaseSettleOrderProduct(dto);
    }

    public Boolean examine(PurchaseSettleOrderExamineDto dto) {
        return purchaseSettleOrderBaseService.examine(dto);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean addPurchaseSettleOrderPay(PurchaseSettleOrderPayAddDto dto) {
        PurchaseSettleOrderPo purchaseSettleOrderPo = purchaseSettleOrderDao.getByIdVersion(dto.getPurchaseSettleOrderId(), dto.getVersion());
        if (purchaseSettleOrderPo == null) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }
        if ((purchaseSettleOrderPo.getPurchaseSettleStatus() == PurchaseSettleStatus.AUDITED) || (purchaseSettleOrderPo.getPurchaseSettleStatus() == PurchaseSettleStatus.PART_SETTLE)) {
            PurchaseSettleOrderPayPo purchaseSettleOrderPayPo = PurchaseSettleOrderPayConverter.INSTANCE.purchaseSettleOrderPay(dto);
            purchaseSettleOrderPayPo.setPurchaseSettleOrderNo(purchaseSettleOrderPo.getPurchaseSettleOrderNo());
            purchaseSettleOrderPayDao.insert(purchaseSettleOrderPayPo);

            //增加图片凭证
            scmImageBaseService.insertBatchImage(dto.getFileCodeList(), ImageBizType.PURCHASE_SETTLE_PAY, purchaseSettleOrderPayPo.getPurchaseSettleOrderPayId());
            //改变状态
            this.updatePurchaseSettleStatus(dto.getPurchaseSettleOrderId());
        } else {
            throw new ParamIllegalException("状态处于" + PurchaseSettleStatus.AUDITED.getRemark() + "或" + PurchaseSettleStatus.PART_SETTLE.getRemark() + "才能进行确认支付");
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean delPurchaseSettleOrderPay(PurchaseSettleOrderPayDelDto dto) {
        PurchaseSettleOrderPayPo purchaseSettleOrderPayPo = purchaseSettleOrderPayDao.getByPurchaseSettleOrderIdAndVersion(dto.getPurchaseSettleOrderPayId(), dto.getVersion());
        if (purchaseSettleOrderPayPo == null) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }
        PurchaseSettleOrderPo purchaseSettleOrderPo = purchaseSettleOrderDao.getById(purchaseSettleOrderPayPo.getPurchaseSettleOrderId());
        if (purchaseSettleOrderPo == null) {
            throw new BizException("查询不到采购结算单信息，请刷新页面后重试！");
        }
        if (purchaseSettleOrderPo.getPurchaseSettleStatus() == PurchaseSettleStatus.SETTLE) {
            throw new ParamIllegalException("采购结算单状态{}，禁止删除操作", PurchaseSettleStatus.SETTLE.getRemark());
        }
        purchaseSettleOrderPayDao.removeByIdVersion(dto.getPurchaseSettleOrderPayId(), dto.getVersion());
        //改变状态
        this.updatePurchaseSettleStatus(purchaseSettleOrderPayPo.getPurchaseSettleOrderId());
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean editPurchaseSettleOrderItem(EditPurchaseSettleOrderItemDto dto) {
        PurchaseSettleOrderPo purchaseSettleOrderPo = purchaseSettleOrderDao.getByIdVersion(dto.getPurchaseSettleOrderId(), dto.getVersion());
        if (purchaseSettleOrderPo == null) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }
        if (purchaseSettleOrderPo.getPurchaseSettleStatus() != PurchaseSettleStatus.WAIT_CONFIRM) {
            throw new ParamIllegalException("状态处于{}才能进行编辑", PurchaseSettleStatus.WAIT_CONFIRM.getRemark());
        }
        //重新统计数据
        if (dto.getPurchaseSettleOrderItemIds().size() > 0) {
            List<PurchaseSettleOrderItemPo> purchaseSettleOrderItemPos = purchaseSettleOrderItemDao.getByBatchIds(dto.getPurchaseSettleOrderItemIds());
            if (purchaseSettleOrderItemPos.size() == 0) {
                throw new ParamIllegalException("查询不到信息");
            }
            BigDecimal totalSettlePrice = purchaseSettleOrderItemPos.stream().map(PurchaseSettleOrderItemPo::getSettlePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            purchaseSettleOrderPo.setTotalPrice(purchaseSettleOrderPo.getTotalPrice().subtract(totalSettlePrice));
            purchaseSettleOrderPo.setPayPrice(purchaseSettleOrderPo.getPayPrice().subtract(totalSettlePrice));
            //删除记录
            purchaseSettleOrderItemDao.removeBatchByIds(dto.getPurchaseSettleOrderItemIds());
        }
        purchaseSettleOrderPo.setAboutSettleTime(dto.getAboutSettleTime());
        purchaseSettleOrderDao.updateByIdVersion(purchaseSettleOrderPo);

        return true;
    }

    public Boolean update(PurchaseSettleOrderUpdateDto dto) {
        return purchaseSettleOrderBaseService.update(dto);
    }

    /**
     * 统计采购单支付金额修改状态
     *
     * @author ChenWenLong
     * @date 2022/11/9 16:49
     */
    public void updatePurchaseSettleStatus(Long purchaseSettleOrderId) {
        //改变状态
        PurchaseSettleOrderPo purchaseSettleOrderPo = purchaseSettleOrderDao.getById(purchaseSettleOrderId);
        List<PurchaseSettleOrderPayPo> purchaseSettleOrderPays = purchaseSettleOrderPayDao.getByPurchaseSettleOrderId(purchaseSettleOrderId);
        if (CollectionUtil.isNotEmpty(purchaseSettleOrderPays)) {
            BigDecimal totalPayPrice = purchaseSettleOrderPays.stream().map(PurchaseSettleOrderPayPo::getPayPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (totalPayPrice.compareTo(purchaseSettleOrderPo.getPayPrice()) > 0) {
                throw new ParamIllegalException("支付金额总和不能应付金额");
            }
            if (totalPayPrice.compareTo(purchaseSettleOrderPo.getPayPrice()) == 0) {
                purchaseSettleOrderPo.setPurchaseSettleStatus(PurchaseSettleStatus.SETTLE);
                purchaseSettleOrderPo.setPayTime(LocalDateTime.now());
                purchaseSettleOrderPo.setPayUser(GlobalContext.getUserKey());
                purchaseSettleOrderPo.setPayUsername(GlobalContext.getUsername());

                //已结算修改其他单据的状态
                PurchaseSettleOrderProductDto purchaseSettleOrderProductDto = new PurchaseSettleOrderProductDto();
                purchaseSettleOrderProductDto.setPurchaseSettleOrderId(purchaseSettleOrderId);
                List<PurchaseSettleOrderItemPo> purchaseSettleOrderItemPos = purchaseSettleOrderItemDao.searchPurchaseSettleOrderItem(purchaseSettleOrderProductDto);
                //要更新的采购单
                List<String> purchaseChildOrderPoNos = new ArrayList<>();

                //要更新的补款单
                List<String> supplementOrderNos = new ArrayList<>();

                //要更新的扣款单
                List<String> deductOrderNos = new ArrayList<>();

                //要更新的样品单
                List<String> sampleChildOrderNos = new ArrayList<>();

                //要更新的发货单
                List<String> purchaseDeliverOrderNos = new ArrayList<>();

                for (PurchaseSettleOrderItemPo purchaseSettleOrderItemPo : purchaseSettleOrderItemPos) {

                    switch (purchaseSettleOrderItemPo.getPurchaseSettleItemType()) {
                        case PRODUCT_PURCHASE:
                        case PROCESS_PURCHASE:
                            //执行采购
                            purchaseChildOrderPoNos.add(purchaseSettleOrderItemPo.getBusinessNo());
                            break;
                        case REPLENISH:
                            //修改补款单
                            supplementOrderNos.add(purchaseSettleOrderItemPo.getBusinessNo());
                            break;
                        case DEDUCT:
                            //修改扣款单
                            deductOrderNos.add(purchaseSettleOrderItemPo.getBusinessNo());
                            break;
                        case SAMPLE:
                            //修改样品采购单
                            sampleChildOrderNos.add(purchaseSettleOrderItemPo.getBusinessNo());
                            break;
                        case DELIVER:
                            //修改发货单
                            purchaseDeliverOrderNos.add(purchaseSettleOrderItemPo.getBusinessNo());
                            break;
                        default:
                            //其他单据暂时不处理
                    }
                }

                if (CollectionUtil.isNotEmpty(purchaseChildOrderPoNos)) {
                    purchaseBaseService.updateBatchPurchaseChildOrderNo(purchaseChildOrderPoNos, PurchaseOrderStatus.FINISH);
                }

                if (CollectionUtil.isNotEmpty(supplementOrderNos)) {
                    List<SupplementOrderPo> supplementOrderPoList = supplementOrderDao.getBySupplementOrderNoList(supplementOrderNos);
                    for (SupplementOrderPo supplementOrderPo : supplementOrderPoList) {
                        supplementOrderPo.setSupplementStatus(SupplementStatus.AUDITED);
                        supplementOrderPo.setPayTime(LocalDateTime.now());
                        supplementOrderBaseService.updateHandleUser(supplementOrderPo, settleConfig);
                    }
                    supplementOrderDao.updateBatchById(supplementOrderPoList);
                }

                if (CollectionUtil.isNotEmpty(deductOrderNos)) {
                    List<DeductOrderPo> deductOrderPoList = deductOrderDao.getByDeductOrderNoList(deductOrderNos);
                    for (DeductOrderPo deductOrderPo : deductOrderPoList) {
                        deductOrderPo.setDeductStatus(DeductStatus.AUDITED);
                        deductOrderPo.setPayTime(LocalDateTime.now());
                        deductOrderBaseService.updateHandleUser(deductOrderPo, settleConfig);
                    }
                    deductOrderDao.updateBatchById(deductOrderPoList);
                }

                if (CollectionUtil.isNotEmpty(sampleChildOrderNos)) {
                    sampleBaseService.updateBatchSampleChildOrderNo(sampleChildOrderNos, SampleOrderStatus.SETTLE);
                }

                if (CollectionUtil.isNotEmpty(purchaseDeliverOrderNos)) {
                    purchaseBaseService.updateBatchPurchaseDeliverOrderNo(purchaseDeliverOrderNos, DeliverOrderStatus.SETTLE);
                }

            }
            if (totalPayPrice.compareTo(purchaseSettleOrderPo.getPayPrice()) < 0) {
                purchaseSettleOrderPo.setPurchaseSettleStatus(PurchaseSettleStatus.PART_SETTLE);
            }

        } else {
            purchaseSettleOrderPo.setPurchaseSettleStatus(PurchaseSettleStatus.AUDITED);
        }
        purchaseSettleOrderDao.updateByIdVersion(purchaseSettleOrderPo);
        //写日志
        purchaseSettleOrderBaseService.createStatusChangeLog(purchaseSettleOrderPo, null);
    }

    /**
     * 统计导出的总数
     *
     * @author ChenWenLong
     * @date 2022/12/16 17:59
     */
    public Integer getExportTotals(PurchaseSettleOrderSearchDto dto) {

        if (StringUtils.isNotBlank(dto.getSupplementOrderNo()) || StringUtils.isNotBlank(dto.getDeductOrderNo())) {
            String businessNo = StringUtils.isNotBlank(dto.getSupplementOrderNo()) ? dto.getSupplementOrderNo() : dto.getDeductOrderNo();
            List<PurchaseSettleOrderItemPo> itemPoList = purchaseSettleOrderItemDao.getByBusinessNo(businessNo);
            if (CollectionUtil.isEmpty(itemPoList)) {
                return 0;
            }
            dto.setPurchaseSettleOrderIds(itemPoList.stream().map(PurchaseSettleOrderItemPo::getPurchaseSettleOrderId).collect(Collectors.toList()));
        }

        if (StringUtils.isNotBlank(dto.getBusinessNo())) {
            List<PurchaseSettleOrderItemPo> itemPoList = purchaseSettleOrderItemDao.getByBusinessNo(dto.getBusinessNo());
            if (CollectionUtil.isEmpty(itemPoList)) {
                return 0;
            }
            dto.setPurchaseSettleOrderIds(itemPoList.stream().map(PurchaseSettleOrderItemPo::getPurchaseSettleOrderId).collect(Collectors.toList()));
        }

        return purchaseSettleOrderDao.getExportTotals(dto);
    }

    /**
     * 统计导出的总数
     *
     * @author ChenWenLong
     * @date 2023/2/7 15:07
     */
    public Integer getSupplierExportTotals(PurchaseSettleOrderSearchDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        if (CollectionUtil.isNotEmpty(supplierCodeList)) {
            List<PurchaseSettleStatus> notPurchaseSettleStatusList = new ArrayList<>();
            notPurchaseSettleStatusList.add(PurchaseSettleStatus.WAIT_CONFIRM);
            dto.setNotPurchaseSettleStatusList(notPurchaseSettleStatusList);
        }
        return this.getExportTotals(dto);
    }

    /**
     * 查询导出列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<PurchaseSettleOrderExportVo> getExportList(PurchaseSettleOrderSearchDto dto) {

        if (StringUtils.isNotBlank(dto.getSupplementOrderNo()) || StringUtils.isNotBlank(dto.getDeductOrderNo())) {
            String businessNo = StringUtils.isNotBlank(dto.getSupplementOrderNo()) ? dto.getSupplementOrderNo() : dto.getDeductOrderNo();
            List<PurchaseSettleOrderItemPo> itemPoList = purchaseSettleOrderItemDao.getByBusinessNo(businessNo);
            if (CollectionUtil.isEmpty(itemPoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            dto.setPurchaseSettleOrderIds(itemPoList.stream().map(PurchaseSettleOrderItemPo::getPurchaseSettleOrderId).collect(Collectors.toList()));
        }

        if (StringUtils.isNotBlank(dto.getBusinessNo())) {
            List<PurchaseSettleOrderItemPo> itemPoList = purchaseSettleOrderItemDao.getByBusinessNo(dto.getBusinessNo());
            if (CollectionUtil.isEmpty(itemPoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            dto.setPurchaseSettleOrderIds(itemPoList.stream().map(PurchaseSettleOrderItemPo::getPurchaseSettleOrderId).collect(Collectors.toList()));
        }
        return purchaseSettleOrderDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
    }

    /**
     * 查询导出SKU列表
     *
     * @author ChenWenLong
     * @date 2023/3/6 14:55
     */
    public List<PurchaseSettleOrderSkuExportVo> getExportSkuList(PurchaseSettleOrderSearchDto dto) {
        //导出的数据
        List<PurchaseSettleOrderSkuExportVo> list = new ArrayList<>();

        CommonPageResult.PageInfo<PurchaseSettleOrderExportVo> selectList = this.getExportList(dto);
        List<PurchaseSettleOrderExportVo> records = selectList.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return list;
        }

        //详情列表根据不同类型进行数据查询
        Map<PurchaseSettleItemType, List<PurchaseSettleOrderExportVo>> itemPoGroup = records.stream().collect(Collectors.groupingBy(PurchaseSettleOrderExportVo::getPurchaseSettleItemType));

        itemPoGroup.forEach((PurchaseSettleItemType key, List<PurchaseSettleOrderExportVo> itemList) -> {
            List<String> noList = itemList.stream().map(PurchaseSettleOrderExportVo::getBusinessNo).collect(Collectors.toList());
            switch (key) {
                //采购处理逻辑
                case PRODUCT_PURCHASE:
                case PROCESS_PURCHASE:
                    List<PurchaseChildOrderExportBo> purchaseChildOrderList = purchaseBaseService.getBatchPurchaseChildOrderNo(noList);
                    List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseBaseService.getListByChildOrderNoListNotStatus(noList, DeliverOrderStatus.DELETED);
                    List<String> purchaseDeliverOrderNoList = purchaseDeliverOrderPoList.stream().map(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo).distinct().collect(Collectors.toList());

                    ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
                    receiveOrderGetDto.setScmBizNoList(purchaseDeliverOrderNoList);
                    List<ReceiveOrderForScmVo> receiveOrderForScmVoList = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto);

                    Map<String, ReceiveOrderForScmBo> receiveOrderForScmBoMap = new HashMap<>();

                    for (ReceiveOrderForScmVo receiveOrderForScmVo : receiveOrderForScmVoList) {
                        if (!WmsEnum.ReceiveOrderState.ALL_RETURN.equals(receiveOrderForScmVo.getReceiveOrderState())) {
                            for (ReceiveOrderForScmVo.ReceiveDeliver receiveDeliver : receiveOrderForScmVo.getReceiveDeliverList()) {
                                ReceiveOrderForScmBo receiveOrderForScmBo = new ReceiveOrderForScmBo();
                                receiveOrderForScmBo.setReceiveOrderNo(receiveOrderForScmVo.getReceiveOrderNo());
                                receiveOrderForScmBo.setReceiveType(receiveOrderForScmVo.getReceiveType());
                                receiveOrderForScmBo.setReceiveOrderState(receiveOrderForScmVo.getReceiveOrderState());
                                receiveOrderForScmBo.setScmBizNo(receiveOrderForScmVo.getScmBizNo());
                                receiveOrderForScmBo.setShippingMarkNo(receiveOrderForScmVo.getShippingMarkNo());
                                receiveOrderForScmBo.setPurchaseChildOrderNo(receiveOrderForScmVo.getPurchaseChildOrderNo());
                                receiveOrderForScmBo.setTrackingNumber(receiveOrderForScmVo.getTrackingNumber());
                                receiveOrderForScmBo.setWarehouseCode(receiveOrderForScmVo.getWarehouseCode());
                                receiveOrderForScmBo.setWarehouseName(receiveOrderForScmVo.getWarehouseName());
                                receiveOrderForScmBo.setBatchCode(receiveDeliver.getBatchCode());
                                receiveOrderForScmBo.setSkuCode(receiveDeliver.getSkuCode());
                                receiveOrderForScmBo.setDeliveryAmount(receiveDeliver.getDeliveryAmount());
                                receiveOrderForScmBo.setReceiveAmount(receiveDeliver.getReceiveAmount());
                                receiveOrderForScmBo.setRejectAmount(receiveDeliver.getRejectAmount());
                                receiveOrderForScmBo.setOnShelvesAmount(receiveDeliver.getOnShelvesAmount());
                                receiveOrderForScmBoMap.put(receiveOrderForScmVo.getPurchaseChildOrderNo() + receiveDeliver.getBatchCode(), receiveOrderForScmBo);
                            }
                        }
                    }


                    for (PurchaseChildOrderExportBo item : purchaseChildOrderList) {
                        PurchaseSettleOrderExportVo purchaseSettleOrderExportVo = itemList.stream().filter(customer -> item.getPurchaseChildOrderNo().equals(customer.getBusinessNo())).findAny().orElse(null);
                        if (purchaseSettleOrderExportVo != null) {
                            //要组装的数据
                            PurchaseSettleOrderSkuExportVo data = new PurchaseSettleOrderSkuExportVo();

                            data.setPurchaseSettleOrderNo(purchaseSettleOrderExportVo.getPurchaseSettleOrderNo());
                            data.setSupplierCode(purchaseSettleOrderExportVo.getSupplierCode());
                            data.setCreateTime(purchaseSettleOrderExportVo.getCreateTime());
                            data.setTotalPrice(purchaseSettleOrderExportVo.getTotalPrice());
                            data.setDeductPrice(purchaseSettleOrderExportVo.getDeductPrice());
                            data.setPayPrice(purchaseSettleOrderExportVo.getPayPrice());
                            if (purchaseSettleOrderExportVo.getPurchaseSettleItemType().equals(PurchaseSettleItemType.PRODUCT_PURCHASE)) {
                                data.setPurchaseSettleItemTypeName(PurchaseSettleItemType.PRODUCT_PURCHASE.getRemark());
                            } else {
                                data.setPurchaseSettleItemTypeName(PurchaseSettleItemType.PROCESS_PURCHASE.getRemark());
                            }

                            data.setBusinessNo(purchaseSettleOrderExportVo.getBusinessNo());
                            data.setPurchaseSettleItemStatusName(purchaseSettleOrderExportVo.getStatusName());
                            data.setItemSettleTime(purchaseSettleOrderExportVo.getItemSettleTime());
                            data.setPurchaseSettleItemSettlePrice(purchaseSettleOrderExportVo.getSettlePrice());

                            Integer qualityGoodsCnt = item.getQualityGoodsCnt();
                            data.setTotalSettlePrice(item.getSettlePrice().multiply(new BigDecimal(Integer.parseInt(qualityGoodsCnt.toString()))));

                            data.setWarehouseName(item.getWarehouseName());
                            data.setSku(item.getSku());
                            data.setSkuBatchCode(item.getSkuBatchCode());
                            data.setSettlePrice(item.getSettlePrice());
                            data.setQualityGoodsCnt(item.getQualityGoodsCnt());

                            data.setDefectiveGoodsCnt(item.getDefectiveGoodsCnt());
                            ReceiveOrderForScmBo receiveOrderForScmBo = receiveOrderForScmBoMap.get(item.getPurchaseChildOrderNo() + item.getSkuBatchCode());
                            if (null != receiveOrderForScmBo) {
                                data.setPurchaseReceiptOrderNo(receiveOrderForScmBo.getReceiveOrderNo());
                                data.setReceiveOrderStateName(null != receiveOrderForScmBo.getReceiveOrderState() ? receiveOrderForScmBo.getReceiveOrderState().getRemark() : null);
                                data.setPurchaseReceiptOrderNo(receiveOrderForScmBo.getReceiveOrderNo());
                                data.setDeliveryAmount(receiveOrderForScmBo.getDeliveryAmount());
                                data.setReceiveAmount(receiveOrderForScmBo.getReceiveAmount());
                                data.setOnShelvesAmount(receiveOrderForScmBo.getOnShelvesAmount());
                            }

                            list.add(data);
                        }

                    }

                    break;
                case REPLENISH:
                    List<SupplementOrderOtherPo> supplementOrderOtherPoList = supplementOrderBaseService.getOtherBatchSupplementOrderNo(noList);
                    for (SupplementOrderOtherPo supplementOrderOtherPo : supplementOrderOtherPoList) {
                        PurchaseSettleOrderExportVo purchaseSettleOrderExportVo = itemList.stream().filter(customer -> supplementOrderOtherPo.getSupplementOrderNo().equals(customer.getBusinessNo())).findAny().orElse(null);
                        if (purchaseSettleOrderExportVo != null) {
                            //要组装的数据
                            PurchaseSettleOrderSkuExportVo data = new PurchaseSettleOrderSkuExportVo();

                            data.setPurchaseSettleOrderNo(purchaseSettleOrderExportVo.getPurchaseSettleOrderNo());
                            data.setSupplierCode(purchaseSettleOrderExportVo.getSupplierCode());
                            data.setCreateTime(purchaseSettleOrderExportVo.getCreateTime());
                            data.setTotalPrice(purchaseSettleOrderExportVo.getTotalPrice());
                            data.setDeductPrice(purchaseSettleOrderExportVo.getDeductPrice());
                            data.setPayPrice(purchaseSettleOrderExportVo.getPayPrice());
                            data.setPurchaseSettleItemTypeName(PurchaseSettleItemType.REPLENISH.getRemark());

                            data.setBusinessNo(purchaseSettleOrderExportVo.getBusinessNo());
                            data.setPurchaseSettleItemStatusName(purchaseSettleOrderExportVo.getStatusName());
                            data.setItemSettleTime(purchaseSettleOrderExportVo.getItemSettleTime());
                            data.setPurchaseSettleItemSettlePrice(purchaseSettleOrderExportVo.getSettlePrice());

                            data.setSupplementDeductRemarks(supplementOrderOtherPo.getSupplementRemarks());
                            data.setTotalSettlePrice(supplementOrderOtherPo.getSupplementPrice());

                            list.add(data);
                        }
                    }

                    List<SupplementOrderPurchaseExportBo> supplementOrderPurchaseExportBoList = supplementOrderBaseService.getPurchaseBatchSupplementOrderNo(noList);

                    for (SupplementOrderPurchaseExportBo supplementOrderPurchaseExportBo : supplementOrderPurchaseExportBoList) {
                        PurchaseSettleOrderExportVo purchaseSettleOrderExportVo = itemList.stream().filter(customer -> supplementOrderPurchaseExportBo.getSupplementOrderNo().equals(customer.getBusinessNo())).findAny().orElse(null);
                        if (purchaseSettleOrderExportVo != null) {
                            //要组装的数据
                            PurchaseSettleOrderSkuExportVo data = new PurchaseSettleOrderSkuExportVo();

                            data.setPurchaseSettleOrderNo(purchaseSettleOrderExportVo.getPurchaseSettleOrderNo());
                            data.setSupplierCode(purchaseSettleOrderExportVo.getSupplierCode());
                            data.setCreateTime(purchaseSettleOrderExportVo.getCreateTime());
                            data.setTotalPrice(purchaseSettleOrderExportVo.getTotalPrice());
                            data.setDeductPrice(purchaseSettleOrderExportVo.getDeductPrice());
                            data.setPayPrice(purchaseSettleOrderExportVo.getPayPrice());
                            data.setPurchaseSettleItemTypeName(PurchaseSettleItemType.REPLENISH.getRemark());

                            data.setBusinessNo(purchaseSettleOrderExportVo.getBusinessNo());
                            data.setPurchaseSettleItemStatusName(purchaseSettleOrderExportVo.getStatusName());
                            data.setItemSettleTime(purchaseSettleOrderExportVo.getItemSettleTime());
                            data.setPurchaseSettleItemSettlePrice(purchaseSettleOrderExportVo.getSettlePrice());

                            data.setSupplementDeductRemarks(supplementOrderPurchaseExportBo.getSupplementRemarks());
                            data.setTotalSettlePrice(supplementOrderPurchaseExportBo.getSupplementPrice());

                            list.add(data);
                        }
                    }

                    break;
                case DEDUCT:
                    //扣款单其他单据明细
                    List<DeductOrderOtherPo> deductOrderOtherPoList = deductOrderBaseService.getOtherBatchDeductOrderNo(noList);
                    for (DeductOrderOtherPo deductOrderOtherPo : deductOrderOtherPoList) {
                        PurchaseSettleOrderExportVo purchaseSettleOrderExportVo = itemList.stream().filter(customer -> deductOrderOtherPo.getDeductOrderNo().equals(customer.getBusinessNo())).findAny().orElse(null);
                        if (purchaseSettleOrderExportVo != null) {
                            //要组装的数据
                            PurchaseSettleOrderSkuExportVo data = new PurchaseSettleOrderSkuExportVo();

                            data.setPurchaseSettleOrderNo(purchaseSettleOrderExportVo.getPurchaseSettleOrderNo());
                            data.setSupplierCode(purchaseSettleOrderExportVo.getSupplierCode());
                            data.setCreateTime(purchaseSettleOrderExportVo.getCreateTime());
                            data.setTotalPrice(purchaseSettleOrderExportVo.getTotalPrice());
                            data.setDeductPrice(purchaseSettleOrderExportVo.getDeductPrice());
                            data.setPayPrice(purchaseSettleOrderExportVo.getPayPrice());
                            data.setPurchaseSettleItemTypeName(PurchaseSettleItemType.DEDUCT.getRemark());

                            data.setBusinessNo(purchaseSettleOrderExportVo.getBusinessNo());
                            data.setPurchaseSettleItemStatusName(purchaseSettleOrderExportVo.getStatusName());
                            data.setItemSettleTime(purchaseSettleOrderExportVo.getItemSettleTime());
                            data.setPurchaseSettleItemSettlePrice(purchaseSettleOrderExportVo.getSettlePrice());

                            data.setSupplementDeductRemarks(deductOrderOtherPo.getDeductRemarks());
                            data.setTotalSettlePrice(deductOrderOtherPo.getDeductPrice());

                            list.add(data);
                        }
                    }

                    //扣款单预付款明细
                    List<DeductOrderPayPo> deductOrderPayPoList = deductOrderBaseService.getPayBatchDeductOrderNo(noList);
                    for (DeductOrderPayPo deductOrderPayPo : deductOrderPayPoList) {
                        PurchaseSettleOrderExportVo purchaseSettleOrderExportVo = itemList.stream().filter(customer -> deductOrderPayPo.getDeductOrderNo().equals(customer.getBusinessNo())).findAny().orElse(null);
                        if (purchaseSettleOrderExportVo != null) {
                            //要组装的数据
                            PurchaseSettleOrderSkuExportVo data = new PurchaseSettleOrderSkuExportVo();

                            data.setPurchaseSettleOrderNo(purchaseSettleOrderExportVo.getPurchaseSettleOrderNo());
                            data.setSupplierCode(purchaseSettleOrderExportVo.getSupplierCode());
                            data.setCreateTime(purchaseSettleOrderExportVo.getCreateTime());
                            data.setTotalPrice(purchaseSettleOrderExportVo.getTotalPrice());
                            data.setDeductPrice(purchaseSettleOrderExportVo.getDeductPrice());
                            data.setPayPrice(purchaseSettleOrderExportVo.getPayPrice());
                            data.setPurchaseSettleItemTypeName(PurchaseSettleItemType.DEDUCT.getRemark());

                            data.setBusinessNo(purchaseSettleOrderExportVo.getBusinessNo());
                            data.setPurchaseSettleItemStatusName(purchaseSettleOrderExportVo.getStatusName());
                            data.setItemSettleTime(purchaseSettleOrderExportVo.getItemSettleTime());
                            data.setPurchaseSettleItemSettlePrice(purchaseSettleOrderExportVo.getSettlePrice());

                            data.setSupplementDeductRemarks(deductOrderPayPo.getDeductRemarks());
                            data.setTotalSettlePrice(deductOrderPayPo.getDeductPrice());

                            list.add(data);
                        }
                    }

                    //扣款单采购明细
                    List<DeductOrderExportBo> deductOrderPurchaseExportBoList = deductOrderBaseService.getPurchaseBatchDeductOrderNo(noList);

                    for (DeductOrderExportBo deductOrderExportBo : deductOrderPurchaseExportBoList) {
                        PurchaseSettleOrderExportVo purchaseSettleOrderExportVo = itemList.stream().filter(customer -> deductOrderExportBo.getDeductOrderNo().equals(customer.getBusinessNo())).findAny().orElse(null);
                        if (purchaseSettleOrderExportVo != null) {
                            //要组装的数据
                            PurchaseSettleOrderSkuExportVo data = new PurchaseSettleOrderSkuExportVo();

                            data.setPurchaseSettleOrderNo(purchaseSettleOrderExportVo.getPurchaseSettleOrderNo());
                            data.setSupplierCode(purchaseSettleOrderExportVo.getSupplierCode());
                            data.setCreateTime(purchaseSettleOrderExportVo.getCreateTime());
                            data.setTotalPrice(purchaseSettleOrderExportVo.getTotalPrice());
                            data.setDeductPrice(purchaseSettleOrderExportVo.getDeductPrice());
                            data.setPayPrice(purchaseSettleOrderExportVo.getPayPrice());
                            data.setPurchaseSettleItemTypeName(PurchaseSettleItemType.DEDUCT.getRemark());

                            data.setBusinessNo(purchaseSettleOrderExportVo.getBusinessNo());
                            data.setPurchaseSettleItemStatusName(purchaseSettleOrderExportVo.getStatusName());
                            data.setItemSettleTime(purchaseSettleOrderExportVo.getItemSettleTime());
                            data.setPurchaseSettleItemSettlePrice(purchaseSettleOrderExportVo.getSettlePrice());

                            data.setSupplementDeductRemarks(deductOrderExportBo.getDeductRemarks());
                            data.setTotalSettlePrice(deductOrderExportBo.getDeductPrice());

                            list.add(data);
                        }
                    }

                    //扣款单品质扣款明细
                    List<DeductOrderExportBo> deductOrderQualityExportBoList = deductOrderBaseService.getQualityBatchDeductOrderNo(noList);

                    for (DeductOrderExportBo deductOrderExportBo : deductOrderQualityExportBoList) {
                        PurchaseSettleOrderExportVo purchaseSettleOrderExportVo = itemList.stream().filter(customer -> deductOrderExportBo.getDeductOrderNo().equals(customer.getBusinessNo())).findAny().orElse(null);
                        if (purchaseSettleOrderExportVo != null) {
                            //要组装的数据
                            PurchaseSettleOrderSkuExportVo data = new PurchaseSettleOrderSkuExportVo();

                            data.setPurchaseSettleOrderNo(purchaseSettleOrderExportVo.getPurchaseSettleOrderNo());
                            data.setSupplierCode(purchaseSettleOrderExportVo.getSupplierCode());
                            data.setCreateTime(purchaseSettleOrderExportVo.getCreateTime());
                            data.setTotalPrice(purchaseSettleOrderExportVo.getTotalPrice());
                            data.setDeductPrice(purchaseSettleOrderExportVo.getDeductPrice());
                            data.setPayPrice(purchaseSettleOrderExportVo.getPayPrice());
                            data.setPurchaseSettleItemTypeName(PurchaseSettleItemType.DEDUCT.getRemark());

                            data.setBusinessNo(purchaseSettleOrderExportVo.getBusinessNo());
                            data.setPurchaseSettleItemStatusName(purchaseSettleOrderExportVo.getStatusName());
                            data.setItemSettleTime(purchaseSettleOrderExportVo.getItemSettleTime());
                            data.setPurchaseSettleItemSettlePrice(purchaseSettleOrderExportVo.getSettlePrice());

                            data.setSupplementDeductRemarks(deductOrderExportBo.getDeductRemarks());
                            data.setTotalSettlePrice(deductOrderExportBo.getDeductPrice());

                            list.add(data);
                        }
                    }


                    break;
                case SAMPLE:
                    List<SampleChildOrderPo> sampleChildOrderPoList = sampleBaseService.getBatchSampleChildOrderNo(noList);
                    Map<String, List<SampleChildOrderResultPo>> sampleChildOrderResultPoMap = sampleBaseService.getSampleCntBatchSampleChildOrderNo(noList, SampleResult.SAMPLE_SUCCESS);
                    for (SampleChildOrderPo sampleChildOrderPo : sampleChildOrderPoList) {
                        PurchaseSettleOrderExportVo purchaseSettleOrderExportVo = itemList.stream().filter(customer -> sampleChildOrderPo.getSampleChildOrderNo().equals(customer.getBusinessNo())).findAny().orElse(null);
                        if (purchaseSettleOrderExportVo != null) {
                            //要组装的数据
                            PurchaseSettleOrderSkuExportVo data = new PurchaseSettleOrderSkuExportVo();

                            data.setPurchaseSettleOrderNo(purchaseSettleOrderExportVo.getPurchaseSettleOrderNo());
                            data.setSupplierCode(purchaseSettleOrderExportVo.getSupplierCode());
                            data.setCreateTime(purchaseSettleOrderExportVo.getCreateTime());
                            data.setTotalPrice(purchaseSettleOrderExportVo.getTotalPrice());
                            data.setDeductPrice(purchaseSettleOrderExportVo.getDeductPrice());
                            data.setPayPrice(purchaseSettleOrderExportVo.getPayPrice());
                            data.setPurchaseSettleItemTypeName(PurchaseSettleItemType.SAMPLE.getRemark());

                            data.setBusinessNo(purchaseSettleOrderExportVo.getBusinessNo());
                            data.setPurchaseSettleItemStatusName(purchaseSettleOrderExportVo.getStatusName());
                            data.setItemSettleTime(purchaseSettleOrderExportVo.getItemSettleTime());
                            data.setPurchaseSettleItemSettlePrice(purchaseSettleOrderExportVo.getSettlePrice());

                            data.setTotalSettlePrice(sampleChildOrderPo.getSettlePrice());

                            data.setWarehouseName(sampleChildOrderPo.getWarehouseName());
                            data.setSku(sampleChildOrderPo.getSku());
                            data.setSkuBatchCode(sampleChildOrderPo.getSkuBatchCode());
                            data.setSettlePrice(sampleChildOrderPo.getProofingPrice());

                            if (sampleChildOrderResultPoMap.containsKey(sampleChildOrderPo.getSampleChildOrderNo())) {
                                List<SampleChildOrderResultPo> sampleChildOrderResultPos = sampleChildOrderResultPoMap.get(sampleChildOrderPo.getSampleChildOrderNo());
                                data.setQualityGoodsCnt(sampleChildOrderResultPos.get(0).getSampleCnt());
                            }

                            list.add(data);
                        }
                    }

                    break;
                //发货单处理逻辑
                case DELIVER:
                    List<PurchaseDeliverOrderPo> purchaseDeliverOrderPos = purchaseDeliverOrderDao.getListByDeliverOrderNoListNotStatus(noList, DeliverOrderStatus.DELETED);
                    List<String> purchaseDeliverOrderNos = purchaseDeliverOrderPos.stream().map(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo).distinct().collect(Collectors.toList());
                    List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList = purchaseDeliverOrderItemDao.getListByDeliverOrderNoList(purchaseDeliverOrderNos);
                    List<String> skuList = purchaseDeliverOrderItemPoList.stream().map(PurchaseDeliverOrderItemPo::getSku).distinct().collect(Collectors.toList());
                    final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
                    ReceiveOrderGetDto receiveOrderDeliverDto = new ReceiveOrderGetDto();
                    receiveOrderDeliverDto.setScmBizNoList(purchaseDeliverOrderNos);
                    List<ReceiveOrderForScmVo> receiveOrderForScmVos = wmsRemoteService.getReceiveOrderList(receiveOrderDeliverDto);
                    Map<String, PurchaseDeliverOrderPo> purchaseDeliverOrderPoMap = purchaseDeliverOrderPos.stream()
                            .collect(Collectors.toMap(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo, purchaseDeliverOrderPo -> purchaseDeliverOrderPo));
                    List<String> purchaseChildOrderNoList = purchaseDeliverOrderPos.stream().map(PurchaseDeliverOrderPo::getPurchaseChildOrderNo).distinct().collect(Collectors.toList());
                    Map<String, PurchaseChildOrderExportBo> purchaseChildOrderExportMap = purchaseBaseService.getBatchPurchaseChildOrderNo(purchaseChildOrderNoList)
                            .stream()
                            .collect(Collectors.groupingBy(PurchaseChildOrderExportBo::getPurchaseChildOrderNo, Collectors.collectingAndThen(Collectors.toList(), value -> value.get(0))));

                    Map<String, ReceiveOrderForScmBo> receiveOrderForScmBoTwoMap = new HashMap<>();

                    for (ReceiveOrderForScmVo receiveOrderForScmVo : receiveOrderForScmVos) {
                        if (!WmsEnum.ReceiveOrderState.ALL_RETURN.equals(receiveOrderForScmVo.getReceiveOrderState())) {
                            for (ReceiveOrderForScmVo.ReceiveDeliver receiveDeliver : receiveOrderForScmVo.getReceiveDeliverList()) {
                                ReceiveOrderForScmBo receiveOrderForScmBo = new ReceiveOrderForScmBo();
                                receiveOrderForScmBo.setReceiveOrderNo(receiveOrderForScmVo.getReceiveOrderNo());
                                receiveOrderForScmBo.setReceiveType(receiveOrderForScmVo.getReceiveType());
                                receiveOrderForScmBo.setReceiveOrderState(receiveOrderForScmVo.getReceiveOrderState());
                                receiveOrderForScmBo.setScmBizNo(receiveOrderForScmVo.getScmBizNo());
                                receiveOrderForScmBo.setShippingMarkNo(receiveOrderForScmVo.getShippingMarkNo());
                                receiveOrderForScmBo.setPurchaseChildOrderNo(receiveOrderForScmVo.getPurchaseChildOrderNo());
                                receiveOrderForScmBo.setTrackingNumber(receiveOrderForScmVo.getTrackingNumber());
                                receiveOrderForScmBo.setWarehouseCode(receiveOrderForScmVo.getWarehouseCode());
                                receiveOrderForScmBo.setWarehouseName(receiveOrderForScmVo.getWarehouseName());
                                receiveOrderForScmBo.setBatchCode(receiveDeliver.getBatchCode());
                                receiveOrderForScmBo.setSkuCode(receiveDeliver.getSkuCode());
                                receiveOrderForScmBo.setDeliveryAmount(receiveDeliver.getDeliveryAmount());
                                receiveOrderForScmBo.setReceiveAmount(receiveDeliver.getReceiveAmount());
                                receiveOrderForScmBo.setRejectAmount(receiveDeliver.getRejectAmount());
                                receiveOrderForScmBo.setOnShelvesAmount(receiveDeliver.getOnShelvesAmount());
                                receiveOrderForScmBoTwoMap.put(receiveOrderForScmVo.getScmBizNo() + receiveDeliver.getBatchCode(), receiveOrderForScmBo);

                            }
                        }
                    }

                    Map<String, List<PurchaseReturnOrderPo>> purchaseReturnOrderPoMap = purchaseReturnOrderDao.getListByReturnBizNoList(purchaseDeliverOrderNos)
                            .stream().collect(Collectors.groupingBy(PurchaseReturnOrderPo::getReturnBizNo));

                    for (PurchaseDeliverOrderItemPo item : purchaseDeliverOrderItemPoList) {
                        PurchaseDeliverOrderPo purchaseDeliverOrderPo = purchaseDeliverOrderPoMap.get(item.getPurchaseDeliverOrderNo());
                        PurchaseChildOrderExportBo purchaseChildOrderExportBo = Optional
                                .ofNullable(purchaseChildOrderExportMap.get(purchaseDeliverOrderPo.getPurchaseChildOrderNo()))
                                .orElse(new PurchaseChildOrderExportBo());
                        PurchaseSettleOrderExportVo purchaseSettleOrderExportVo = itemList.stream().filter(customer -> item.getPurchaseDeliverOrderNo().equals(customer.getBusinessNo())).findAny().orElse(null);
                        if (purchaseSettleOrderExportVo != null) {
                            //要组装的数据
                            PurchaseSettleOrderSkuExportVo data = new PurchaseSettleOrderSkuExportVo();

                            data.setPurchaseSettleOrderNo(purchaseSettleOrderExportVo.getPurchaseSettleOrderNo());
                            data.setSupplierCode(purchaseSettleOrderExportVo.getSupplierCode());
                            data.setCreateTime(purchaseSettleOrderExportVo.getCreateTime());
                            data.setTotalPrice(purchaseSettleOrderExportVo.getTotalPrice());
                            data.setDeductPrice(purchaseSettleOrderExportVo.getDeductPrice());
                            data.setPayPrice(purchaseSettleOrderExportVo.getPayPrice());
                            data.setPurchaseSettleItemTypeName(PurchaseSettleItemType.DELIVER.getRemark());

                            data.setBusinessNo(purchaseSettleOrderExportVo.getBusinessNo());
                            data.setPurchaseSettleItemStatusName(purchaseSettleOrderExportVo.getStatusName());
                            data.setItemSettleTime(purchaseSettleOrderExportVo.getItemSettleTime());
                            data.setPurchaseSettleItemSettlePrice(purchaseSettleOrderExportVo.getSettlePrice());

                            Integer qualityGoodsCnt = item.getQualityGoodsCnt();
                            if (null != purchaseChildOrderExportBo.getSettlePrice()) {
                                data.setTotalSettlePrice(purchaseChildOrderExportBo.getSettlePrice().multiply(new BigDecimal(Integer.parseInt(qualityGoodsCnt.toString()))));
                                data.setSettlePrice(purchaseChildOrderExportBo.getSettlePrice());
                            }
                            if (StringUtils.isNotBlank(purchaseChildOrderExportBo.getWarehouseName())) {
                                data.setWarehouseName(purchaseChildOrderExportBo.getWarehouseName());
                            }
                            data.setSku(item.getSku());
                            data.setSkuBatchCode(item.getSkuBatchCode());
                            data.setQualityGoodsCnt(item.getQualityGoodsCnt());

                            data.setDefectiveGoodsCnt(item.getDefectiveGoodsCnt());
                            ReceiveOrderForScmBo receiveOrderForScmBo = receiveOrderForScmBoTwoMap.get(item.getPurchaseDeliverOrderNo() + item.getSkuBatchCode());
                            if (null != receiveOrderForScmBo) {
                                data.setPurchaseReceiptOrderNo(receiveOrderForScmBo.getReceiveOrderNo());
                                data.setReceiveOrderStateName(null != receiveOrderForScmBo.getReceiveOrderState() ? receiveOrderForScmBo.getReceiveOrderState().getRemark() : null);
                                data.setPurchaseReceiptOrderNo(receiveOrderForScmBo.getReceiveOrderNo());
                                data.setDeliveryAmount(receiveOrderForScmBo.getDeliveryAmount());
                                data.setReceiveAmount(receiveOrderForScmBo.getReceiveAmount());
                                data.setOnShelvesAmount(receiveOrderForScmBo.getOnShelvesAmount());
                            }

                            data.setDeliverTime(purchaseDeliverOrderPo.getDeliverTime());
                            data.setPurchaseChildOrderNo(purchaseDeliverOrderPo.getPurchaseChildOrderNo());
                            data.setPurchaseBizTypeName(purchaseChildOrderExportBo.getPurchaseBizType().getRemark());
                            data.setSkuEncode(skuEncodeMap.get(item.getSku()));
                            List<PurchaseReturnOrderPo> purchaseReturnOrderPoList = purchaseReturnOrderPoMap.get(item.getPurchaseDeliverOrderNo());
                            if (CollectionUtil.isNotEmpty(purchaseReturnOrderPoList)) {
                                data.setRealityReturnCnt(purchaseReturnOrderPoList.stream().mapToInt(PurchaseReturnOrderPo::getRealityReturnCnt).sum());
                            }

                            list.add(data);
                        }

                    }

                    break;
                default:
                    //其他单据暂时不处理
            }


        });

        return list;
    }

    /**
     * 获取供应商导出列表
     *
     * @author ChenWenLong
     * @date 2023/2/7 15:08
     */
    public CommonPageResult.PageInfo<PurchaseSettleOrderExportVo> getSupplierExportList(PurchaseSettleOrderSearchDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        if (CollectionUtil.isNotEmpty(supplierCodeList)) {
            List<PurchaseSettleStatus> notPurchaseSettleStatusList = new ArrayList<>();
            notPurchaseSettleStatusList.add(PurchaseSettleStatus.WAIT_CONFIRM);
            dto.setNotPurchaseSettleStatusList(notPurchaseSettleStatusList);
        }
        return this.getExportList(dto);
    }

}
