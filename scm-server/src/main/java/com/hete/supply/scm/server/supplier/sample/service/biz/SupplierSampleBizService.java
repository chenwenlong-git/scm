package com.hete.supply.scm.server.supplier.sample.service.biz;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.SampleDeliverOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SampleDevType;
import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SampleRawBizType;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderCreateMqDto;
import com.hete.supply.scm.server.scm.handler.WmsReceiptHandler;
import com.hete.supply.scm.server.scm.purchase.service.ref.PurchaseRawRefService;
import com.hete.supply.scm.server.scm.sample.converter.SampleConverter;
import com.hete.supply.scm.server.scm.sample.dao.*;
import com.hete.supply.scm.server.scm.sample.entity.dto.SampleChildIdAndVersionDto;
import com.hete.supply.scm.server.scm.sample.entity.dto.SampleRawDto;
import com.hete.supply.scm.server.scm.sample.entity.dto.SampleStatusDto;
import com.hete.supply.scm.server.scm.sample.entity.dto.SampleSupplyRawDto;
import com.hete.supply.scm.server.scm.sample.entity.po.*;
import com.hete.supply.scm.server.scm.sample.handler.SampleStatusHandler;
import com.hete.supply.scm.server.scm.sample.service.base.SampleBaseService;
import com.hete.supply.scm.server.scm.sample.service.biz.SampleDingService;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.supplier.entity.dto.RawReturnSampleItemDto;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchasePriceDto;
import com.hete.supply.scm.server.supplier.sample.converter.SupplierSampleConverter;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleDeliverDto;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleDeliverItemDto;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleReceiveDto;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleReturnRawDto;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleDeliverOrderItemPo;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleDeliverOrderPo;
import com.hete.supply.scm.server.supplier.sample.service.base.SampleDeliverBaseService;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.interna.entity.dto.SkuInstockInventoryQueryDto;
import com.hete.supply.wms.api.interna.entity.vo.SkuInventoryVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.mybatis.plus.entity.bo.CompareResult;
import com.hete.support.mybatis.plus.util.DataCompareUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/11 00:15
 */
@Service
@RequiredArgsConstructor
public class SupplierSampleBizService {
    private final SampleChildOrderDao sampleChildOrderDao;
    private final SampleChildOrderChangeDao sampleChildOrderChangeDao;
    private final SampleParentOrderDao sampleParentOrderDao;
    private final SampleParentOrderChangeDao sampleParentOrderChangeDao;
    private final SampleBaseService sampleBaseService;
    private final SampleDeliverBaseService sampleDeliverBaseService;
    private final IdGenerateService idGenerateService;
    private final LogBaseService logBaseService;
    private final ConsistencySendMqService consistencySendMqService;
    private final SampleDingService sampleDingService;
    private final SampleChildOrderRawDao sampleChildOrderRawDao;
    private final PurchaseRawRefService purchaseRawRefService;
    private final WmsRemoteService wmsRemoteService;
    private final SampleChildOrderProcessDao sampleChildOrderProcessDao;
    private final SampleChildOrderProcessDescDao sampleChildOrderProcessDescDao;


    public void receiveOrder(SampleReceiveDto dto) {
        final SampleChildOrderPo sampleChildOrderPo = sampleChildOrderDao.getByIdVersion(dto.getSampleChildOrderId(), dto.getVersion());
        if (null == sampleChildOrderPo) {
            throw new BizException("找不到样品采购单，接单失败!");
        }
        final SampleChildOrderChangePo sampleChildOrderChangePo = sampleChildOrderChangeDao.getByChildOrderId(sampleChildOrderPo.getSampleChildOrderId());
        if (null == sampleChildOrderChangePo) {
            throw new BizException("找不到样品采购单，接单失败!");
        }
        final SampleParentOrderPo sampleParentOrderPo = sampleParentOrderDao.getByParentNo(sampleChildOrderPo.getSampleParentOrderNo());
        if (null == sampleParentOrderPo) {
            throw new BizException("找不到样品需求单，接单失败!");
        }
        final SampleParentOrderChangePo sampleParentOrderChangePo = sampleParentOrderChangeDao.getByParentId(sampleParentOrderPo.getSampleParentOrderId());
        if (null == sampleParentOrderChangePo) {
            throw new BizException("找不到样品需求单，接单失败!");
        }

        if (BooleanType.TRUE.equals(dto.getIsReceived()) && SampleDevType.confirmSubmitAudited(sampleChildOrderPo.getSampleDevType())) {
            if (CollectionUtils.isEmpty(dto.getSampleProcessList())) {
                throw new ParamIllegalException("{}、{}、{}类型单据必须填写加工信息和加工描述信息，请重新填写后再提交！",
                        SampleDevType.LIMITED.getRemark(), SampleDevType.SMALL_INNOVATION.getRemark(), SampleDevType.OUTSOURCING_SAMPLE.getRemark());
            }
            //验证库存是否充足
            List<SampleChildOrderRawPo> sampleChildOrderRawPoList = sampleChildOrderRawDao.getListByChildNoAndType(sampleChildOrderPo.getSampleChildOrderNo(), SampleRawBizType.DEMAND);
            if (CollectionUtils.isEmpty(sampleChildOrderRawPoList)) {
                throw new BizException("找不到原料信息，接单失败!");
            }
            if (StringUtils.isBlank(sampleChildOrderPo.getRawWarehouseCode())) {
                throw new BizException("没有设置发货仓库，接单失败!");
            }
            List<String> skuList = sampleChildOrderRawPoList.stream().map(SampleChildOrderRawPo::getSku).distinct().collect(Collectors.toList());
            SkuInstockInventoryQueryDto skuInstockInventoryQueryDto = new SkuInstockInventoryQueryDto();
            skuInstockInventoryQueryDto.setWarehouseCode(sampleChildOrderPo.getRawWarehouseCode());
            skuInstockInventoryQueryDto.setSkuCodes(skuList);
            skuInstockInventoryQueryDto.setProductQuality(WmsEnum.ProductQuality.GOOD);
            skuInstockInventoryQueryDto.setDeliveryType(WmsEnum.DeliveryType.SAMPLE_RAW_MATERIAL);
            List<SkuInventoryVo> skuInventoryList = wmsRemoteService.getSkuInventoryList(skuInstockInventoryQueryDto);
            if (CollectionUtils.isEmpty(skuInventoryList)) {
                throw new ParamIllegalException("查询不到对应库存信息，禁止接单!");
            } else {
                // 判断是否有缺货的
                List<String> wmsSkuList = skuInventoryList.stream().map(SkuInventoryVo::getSkuCode).distinct().collect(Collectors.toList());
                sampleChildOrderRawPoList.forEach(item -> {
                    if (!wmsSkuList.contains(item.getSku())) {
                        throw new ParamIllegalException("sku{}库存不足，禁止接单!", item.getSku());
                    }
                });
                skuInventoryList.forEach(item -> {
                    Integer inStockAmount = item.getInStockAmount();
                    int sum = sampleChildOrderRawPoList.stream().filter(it -> it.getSku().equals(item.getSkuCode())).mapToInt(SampleChildOrderRawPo::getDeliveryCnt).sum();
                    if (inStockAmount < sum) {
                        throw new ParamIllegalException("sku{}库存不足，禁止接单!", item.getSkuCode());
                    }
                });
            }
        }
        SampleOrderStatus targetStatus = sampleChildOrderPo.getSampleOrderStatus();
        if (BooleanType.TRUE.equals(dto.getIsReceived())) {
            targetStatus = targetStatus.toReceivedOrder();
        } else {
            targetStatus = targetStatus.toRefused();
        }

        sampleBaseService.updateReceiveOrder(dto,
                sampleChildOrderPo,
                targetStatus,
                sampleChildOrderChangePo,
                sampleParentOrderPo,
                sampleParentOrderChangePo);

    }


    public SampleParentOrderPo isSampleEndStatus(SampleReceiveDto dto) {
        if (BooleanType.TRUE.equals(dto.getIsReceived())) {
            return null;
        }

        final SampleChildOrderPo sampleChildOrderPo = sampleChildOrderDao.getByIdVersion(dto.getSampleChildOrderId(), dto.getVersion());
        if (null == sampleChildOrderPo) {
            throw new BizException("找不到样品采购单，接单失败!");
        }
        final SampleParentOrderPo sampleParentOrderPo = sampleParentOrderDao.getByParentNo(sampleChildOrderPo.getSampleParentOrderNo());
        if (null == sampleParentOrderPo) {
            throw new BizException("找不到样品需求单，接单失败!");
        }

        final List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getChildOrderByParentNo(sampleChildOrderPo.getSampleParentOrderNo());

        final List<SampleChildOrderPo> remainChildOrderPoList = sampleChildOrderPoList.stream()
                .filter(po -> !po.getSampleChildOrderNo().equals(sampleChildOrderPo.getSampleChildOrderNo()))
                .filter(po -> !SampleOrderStatus.REFUSED.equals(po.getSampleOrderStatus()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(remainChildOrderPoList)) {
            return sampleParentOrderPo;
        }

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deliver(SampleDeliverDto dto) {
        final List<SampleDeliverItemDto> sampleDeliverItemList = dto.getSampleDeliverItemList();
        this.checkDeliverCnt(sampleDeliverItemList);

        final Map<Long, SampleDeliverItemDto> sampleDeliverItemMap = sampleDeliverItemList.stream()
                .collect(Collectors.toMap(SampleDeliverItemDto::getSampleChildOrderId, Function.identity()));

        final Set<Long> idList = sampleDeliverItemMap.keySet();

        final List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getByIdList(idList);

        Assert.equals(sampleChildOrderPoList.size(), idList.size(), () -> new BizException("部分样品采购子单查找失败"));

        if (sampleChildOrderPoList.size() > 1) {
            throw new BizException("禁止多个不同样品采购子单一起发货，请重新选择单个采购子单进行发货！");
        }

        // 校验是否同一发货仓库
        final List<String> warehouseCodeList = sampleChildOrderPoList.stream()
                .map(SampleChildOrderPo::getWarehouseCode)
                .distinct()
                .collect(Collectors.toList());
        if (warehouseCodeList.size() > 1) {
            throw new ParamIllegalException("无法选择不同收货仓库的单据进行发货，请重新选择单据进行发货！");
        }

        final List<String> supplierCodeList = sampleChildOrderPoList.stream()
                .map(SampleChildOrderPo::getSupplierCode)
                .distinct()
                .collect(Collectors.toList());
        if (supplierCodeList.size() > 1) {
            throw new ParamIllegalException("无法选择不同供应商的单据进行发货，请重新选择单据进行发货！");
        }

        //验证是否limited/委外打样/微创新验证工序
        for (SampleChildOrderPo sampleChildOrderPo : sampleChildOrderPoList) {
            if (SampleDevType.confirmSubmitAudited(sampleChildOrderPo.getSampleDevType())
                    && CollectionUtils.isEmpty(dto.getSampleProcessList())) {
                throw new ParamIllegalException("样品工序信息必填！");
            }
        }

        final SampleChildOrderPo supplierChildOrderPo = sampleChildOrderPoList.get(0);
        // 更新状态
        sampleChildOrderPoList.forEach(po -> {
            final SampleDeliverItemDto sampleDeliverItemDto = sampleDeliverItemMap.get(po.getSampleChildOrderId());
            final SampleOrderStatus targetStatus = po.getSampleOrderStatus().toWaitReceivedSample();
            po.setSampleOrderStatus(targetStatus);
            po.setProofingPrice(sampleDeliverItemDto.getProofingPrice());
            logBaseService.simpleLog(LogBizModule.SAMPLE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                    po.getSampleChildOrderNo(), targetStatus.getRemark(), Collections.emptyList());
            final SampleStatusDto sampleStatusDto = new SampleStatusDto();
            sampleStatusDto.setSpuCode(po.getSpu());
            sampleStatusDto.setSampleOrderStatus(targetStatus);
            sampleStatusDto.setKey(po.getSampleChildOrderNo());
            sampleStatusDto.setUserKey(GlobalContext.getUserKey());
            sampleStatusDto.setUsername(GlobalContext.getUsername());
            consistencySendMqService.execSendMq(SampleStatusHandler.class, sampleStatusDto);

            // 推送钉钉消息
            sampleDingService.sendChildDeliverDingTalkMsg(po, dto);
        });
        sampleChildOrderDao.updateBatchByIdVersion(sampleChildOrderPoList);

        // 生成发货单
        SampleDeliverOrderPo sampleDeliverOrderPo = SupplierSampleConverter.deliverDtoToPo(supplierChildOrderPo);
        final Map<Long, SampleChildOrderPo> sampleChildOrderPoMap = sampleChildOrderPoList.stream()
                .collect(Collectors.toMap(SampleChildOrderPo::getSampleChildOrderId, Function.identity()));
        // 赋值单号
        final String sampleDeliverOrderNo = idGenerateService.getConfuseCode(ScmConstant.SAMPLE_DELIVER_ORDER_NO_PREFIX, ConfuseLength.L_10);
        sampleDeliverOrderPo.setSampleDeliverOrderNo(sampleDeliverOrderNo);
        sampleDeliverOrderPo.setTotalDeliver(dto.getSampleDeliverItemList()
                .stream()
                .mapToInt(SampleDeliverItemDto::getDeliverCnt)
                .sum());

        List<SampleDeliverOrderItemPo> sampleDeliverOrderItemPoList = SupplierSampleConverter.deliverDtoToItemPoList(dto,
                sampleChildOrderPoMap, sampleDeliverOrderNo);
        sampleDeliverBaseService.insertPo(sampleDeliverOrderPo);
        sampleDeliverBaseService.batchInsertItemPo(sampleDeliverOrderItemPoList);
        logBaseService.simpleLog(LogBizModule.SUPPLIER_SAMPLE_DELIVER_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                sampleDeliverOrderPo.getSampleDeliverOrderNo(), SampleDeliverOrderStatus.WAIT_RECEIVED_SAMPLE.getRemark(), Collections.emptyList());


        // 更新子单change
        List<SampleChildOrderChangePo> sampleChildOrderChangePoList = sampleChildOrderChangeDao.getByChildOrderIdList(idList);
        sampleChildOrderChangePoList.forEach(po -> {
            po.setDeliverUser(GlobalContext.getUserKey());
            po.setDeliverUsername(GlobalContext.getUsername());
            po.setDeliverTime(new DateTime().toLocalDateTime());
            po.setSampleDeliverOrderNo(sampleDeliverOrderNo);
        });
        sampleChildOrderChangeDao.updateBatchByIdVersion(sampleChildOrderChangePoList);
        // 更新母单
        final Map<String, List<SampleOrderStatus>> parentNoChildStatusMap = new HashMap<>();
        for (SampleChildOrderPo sampleChildOrderPo : sampleChildOrderPoList) {
            parentNoChildStatusMap.computeIfAbsent(sampleChildOrderPo.getSampleParentOrderNo(), k -> new ArrayList<>())
                    .add(sampleChildOrderPo.getSampleOrderStatus());
        }
        final List<String> sampleParentNoList = sampleChildOrderPoList.stream()
                .map(SampleChildOrderPo::getSampleParentOrderNo)
                .collect(Collectors.toList());
        List<SampleParentOrderPo> sampleParentOrderPoList = sampleParentOrderDao.getByParentNoList(sampleParentNoList);
        List<SampleParentOrderPo> updatePoList = new ArrayList<>();
        sampleParentOrderPoList.forEach(po -> {
            final SampleOrderStatus earliestStatus = sampleBaseService.getEarliestStatus(po.getSampleParentOrderNo(),
                    parentNoChildStatusMap.get(po.getSampleParentOrderNo()));
            if (!earliestStatus.equals(po.getSampleOrderStatus())) {
                po.setSampleOrderStatus(earliestStatus);
                updatePoList.add(po);
            }
        });
        sampleParentOrderDao.updateBatchByIdVersion(updatePoList);

        // 更新母单change
        final List<Long> parentIdList = sampleParentOrderPoList.stream()
                .map(SampleParentOrderPo::getSampleParentOrderId)
                .collect(Collectors.toList());
        List<SampleParentOrderChangePo> sampleParentOrderChangePoList = sampleParentOrderChangeDao.getByParentIdList(parentIdList);
        sampleParentOrderChangePoList.forEach(po -> po.setDeliverTime(new DateTime().toLocalDateTime()));
        sampleParentOrderChangeDao.updateBatchByIdVersion(sampleParentOrderChangePoList);

        //编辑工序信息
        final List<SampleChildOrderProcessPo> sampleChildOrderProcessPoList = sampleChildOrderProcessDao.getListByNo(supplierChildOrderPo.getSampleChildOrderNo());
        final List<SampleChildOrderProcessPo> sampleChildOrderProcessPoDtoList = SampleConverter.editProcessDtoToPo(supplierChildOrderPo, dto.getSampleProcessList());
        CompareResult<SampleChildOrderProcessPo> processResult = DataCompareUtil.compare(sampleChildOrderProcessPoDtoList, sampleChildOrderProcessPoList, SampleChildOrderProcessPo::getSampleChildOrderProcessId);
        List<SampleChildOrderProcessPo> processCollect = new ArrayList<>(processResult.getNewItems());
        sampleChildOrderProcessDao.insertBatch(processCollect);
        sampleChildOrderProcessDao.updateBatchByIdVersion(processResult.getExistingItems());
        sampleChildOrderProcessDao.removeBatchByIds(processResult.getDeletedItems());

        //编辑工序描述信息
        final List<SampleChildOrderProcessDescPo> sampleChildOrderProcessDescPoList = sampleChildOrderProcessDescDao.getListByNo(supplierChildOrderPo.getSampleChildOrderNo());
        final List<SampleChildOrderProcessDescPo> sampleChildOrderProcessDescPoDtoList = SampleConverter.editProcessDescDtoToPo(supplierChildOrderPo, dto.getSampleProcessDescList());
        CompareResult<SampleChildOrderProcessDescPo> processDescResult = DataCompareUtil.compare(sampleChildOrderProcessDescPoDtoList, sampleChildOrderProcessDescPoList, SampleChildOrderProcessDescPo::getSampleChildOrderProcessDescId);
        List<SampleChildOrderProcessDescPo> processDescCollect = new ArrayList<>(processDescResult.getNewItems());
        sampleChildOrderProcessDescDao.insertBatch(processDescCollect);
        sampleChildOrderProcessDescDao.updateBatchByIdVersion(processDescResult.getExistingItems());
        sampleChildOrderProcessDescDao.removeBatchByIds(processDescResult.getDeletedItems());

    }

    private void checkDeliverCnt(List<SampleDeliverItemDto> sampleDeliverItemList) {
        sampleDeliverItemList.forEach(item -> Assert.isTrue(item.getDeliverCnt() <= item.getPurchaseCnt(),
                () -> new ParamIllegalException("发货数不能大于采购数，请重新填写发货数！"))
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void typesetting(SampleChildIdAndVersionDto dto) {
        final SampleChildOrderPo sampleChildOrderPo = sampleChildOrderDao.getByIdVersion(dto.getSampleChildOrderId(), dto.getVersion());
        Assert.notNull(sampleChildOrderPo, () -> new BizException("找不到样品采购单，打版失败！"));
        final SampleChildOrderChangePo sampleChildOrderChangePo = sampleChildOrderChangeDao.getByChildOrderId(sampleChildOrderPo.getSampleChildOrderId());
        Assert.notNull(sampleChildOrderChangePo, () -> new BizException("找不到样品采购单，打版失败！"));
        final SampleParentOrderPo sampleParentOrderPo = sampleParentOrderDao.getByParentNo(sampleChildOrderPo.getSampleParentOrderNo());
        Assert.notNull(sampleParentOrderPo, () -> new BizException("找不到样品需求单，打版失败！"));
        final SampleParentOrderChangePo sampleParentOrderChangePo = sampleParentOrderChangeDao.getByParentId(sampleParentOrderPo.getSampleParentOrderId());
        Assert.notNull(sampleParentOrderChangePo, () -> new BizException("找不到样品需求单，打版失败！"));


        final SampleOrderStatus targetStatus = sampleChildOrderPo.getSampleOrderStatus().toTypesetting();
        sampleChildOrderPo.setSampleOrderStatus(targetStatus);

        sampleChildOrderDao.updateByIdVersion(sampleChildOrderPo);
        final LocalDateTime typesettingTime = new DateTime().toLocalDateTime();
        sampleChildOrderChangePo.setTypesettingTime(typesettingTime);
        sampleChildOrderChangeDao.updateByIdVersion(sampleChildOrderChangePo);

        // 更新母单
        final SampleOrderStatus earliestStatus = sampleBaseService.getEarliestStatus(sampleParentOrderPo.getSampleParentOrderNo(),
                Collections.singletonList(targetStatus));
        if (!sampleParentOrderPo.getSampleOrderStatus().equals(earliestStatus)) {
            sampleParentOrderPo.setSampleOrderStatus(earliestStatus);
            sampleParentOrderDao.updateByIdVersion(sampleParentOrderPo);
        }

        // 更新母单change
        sampleParentOrderChangePo.setTypesettingTime(typesettingTime);
        sampleParentOrderChangeDao.updateByIdVersion(sampleParentOrderChangePo);

        logBaseService.simpleLog(LogBizModule.SAMPLE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                sampleChildOrderPo.getSampleChildOrderNo(), targetStatus.getRemark(), Collections.emptyList());
        final SampleStatusDto sampleStatusDto = new SampleStatusDto();
        sampleStatusDto.setSpuCode(sampleParentOrderPo.getSpu());
        sampleStatusDto.setSampleOrderStatus(targetStatus);
        sampleStatusDto.setKey(sampleChildOrderPo.getSampleChildOrderNo());
        sampleStatusDto.setUserKey(GlobalContext.getUserKey());
        sampleStatusDto.setUsername(GlobalContext.getUsername());
        consistencySendMqService.execSendMq(SampleStatusHandler.class, sampleStatusDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updatePrice(PurchasePriceDto dto) {
        final SampleChildOrderPo sampleChildOrderPo = sampleChildOrderDao.getByIdVersion(dto.getSampleChildOrderId(), dto.getVersion());
        Assert.notNull(sampleChildOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));

        if (!SampleOrderStatus.RECEIVED_ORDER.equals(sampleChildOrderPo.getSampleOrderStatus())
                && !SampleOrderStatus.TYPESETTING.equals(sampleChildOrderPo.getSampleOrderStatus())
                && !SampleOrderStatus.WAIT_RECEIVED_SAMPLE.equals(sampleChildOrderPo.getSampleOrderStatus())
                && !SampleOrderStatus.WAIT_SAMPLE.equals(sampleChildOrderPo.getSampleOrderStatus())) {
            throw new ParamIllegalException("当前样品子单状态不处于【{}】【{}】【{}】【{}】,无法修改打样价格",
                    SampleOrderStatus.RECEIVED_ORDER, SampleOrderStatus.TYPESETTING,
                    SampleOrderStatus.WAIT_RECEIVED_SAMPLE, SampleOrderStatus.WAIT_SAMPLE);
        }

        sampleChildOrderPo.setProofingPrice(dto.getProofingPrice());
        sampleChildOrderDao.updateByIdVersion(sampleChildOrderPo);
    }


    public void supplyRaw(SampleSupplyRawDto dto) {
        final SampleChildOrderPo sampleChildOrderPo = sampleChildOrderDao.getOneByChildOrderNo(dto.getSampleChildOrderNo());
        Assert.notNull(sampleChildOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        if (!SampleDevType.confirmSubmitAudited(sampleChildOrderPo.getSampleDevType())) {
            throw new ParamIllegalException("只有【{}、{}、{}】类型的样品单才可以补充原料！",
                    SampleDevType.LIMITED.getRemark(), SampleDevType.SMALL_INNOVATION.getRemark(), SampleDevType.OUTSOURCING_SAMPLE.getRemark());
        }
        if (!SampleOrderStatus.RECEIVED_ORDER.equals(sampleChildOrderPo.getSampleOrderStatus())
                && !SampleOrderStatus.TYPESETTING.equals(sampleChildOrderPo.getSampleOrderStatus())) {
            throw new BizException("当前样品单状态为:{}，不处于【{}】【{}】，无法补充原料",
                    sampleChildOrderPo.getSampleOrderStatus().getRemark(),
                    SampleOrderStatus.RECEIVED_ORDER.getRemark(), SampleOrderStatus.TYPESETTING.getRemark());
        }

        //验证库存是否充足
        List<String> skuList = dto.getSampleRawList().stream().map(SampleRawDto::getSku).distinct().collect(Collectors.toList());
        SkuInstockInventoryQueryDto skuInstockInventoryQueryDto = new SkuInstockInventoryQueryDto();
        skuInstockInventoryQueryDto.setWarehouseCode(dto.getRawWarehouseCode());
        skuInstockInventoryQueryDto.setSkuCodes(skuList);
        skuInstockInventoryQueryDto.setProductQuality(WmsEnum.ProductQuality.GOOD);
        skuInstockInventoryQueryDto.setDeliveryType(WmsEnum.DeliveryType.SAMPLE_RAW_MATERIAL);
        List<SkuInventoryVo> skuInventoryList = wmsRemoteService.getSkuInventoryList(skuInstockInventoryQueryDto);
        if (CollectionUtils.isEmpty(skuInventoryList)) {
            throw new ParamIllegalException("查询不到对应库存信息，禁止补充原料!");
        } else {
            // 判断是否有缺货的
            List<String> wmsSkuList = skuInventoryList.stream().map(SkuInventoryVo::getSkuCode).distinct().collect(Collectors.toList());
            dto.getSampleRawList().forEach(item -> {
                if (!wmsSkuList.contains(item.getSku())) {
                    throw new ParamIllegalException("sku{}库存不足，禁止补充原料!", item.getSku());
                }
            });
            skuInventoryList.forEach(item -> {
                Integer inStockAmount = item.getInStockAmount();
                int sum = dto.getSampleRawList().stream().filter(it -> it.getSku().equals(item.getSkuCode())).mapToInt(SampleRawDto::getDeliveryCnt).sum();
                if (inStockAmount < sum) {
                    throw new ParamIllegalException("sku{}库存不足，禁止补充原料!", item.getSkuCode());
                }
            });
        }

        sampleBaseService.updateSupplyRaw(dto, sampleChildOrderPo);

    }

    @Transactional(rollbackFor = Exception.class)
    public void returnRaw(SampleReturnRawDto dto) {
        final SampleChildOrderPo sampleChildOrderPo = sampleChildOrderDao.getOneByChildOrderNo(dto.getSampleChildOrderNo());
        Assert.notNull(sampleChildOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        if (!SampleDevType.confirmSubmitAudited(sampleChildOrderPo.getSampleDevType())) {
            throw new ParamIllegalException("只有【{}、{}、{}】类型的样品单才可以归还原料！",
                    SampleDevType.LIMITED.getRemark(), SampleDevType.SMALL_INNOVATION.getRemark(), SampleDevType.OUTSOURCING_SAMPLE.getRemark());
        }
        if (!SampleOrderStatus.returnRawCheck(sampleChildOrderPo.getSampleOrderStatus())) {
            throw new BizException("当前样品单状态为:{}，不处于【{}】【{}】【{}】，无法归还原料",
                    sampleChildOrderPo.getSampleOrderStatus().getRemark(),
                    SampleOrderStatus.RECEIVED_ORDER.getRemark(),
                    SampleOrderStatus.TYPESETTING.getRemark(),
                    SampleOrderStatus.DELETED.getRemark());
        }

        final List<RawReturnSampleItemDto> rawProductItemList = dto.getRawProductItemList();
        final List<Long> idList = rawProductItemList.stream()
                .map(RawReturnSampleItemDto::getSampleChildOrderRawId)
                .collect(Collectors.toList());

        final List<SampleChildOrderRawPo> sampleChildOrderRawPoList = sampleChildOrderRawDao.getListByIdList(idList);
        if (idList.size() != sampleChildOrderRawPoList.size()) {
            throw new BizException("找不到对应的原料明细，归还原料失败！");
        }

        final Map<Long, SampleChildOrderRawPo> sampleChildOrderRawPoMap = sampleChildOrderRawPoList.stream()
                .collect(Collectors.toMap(SampleChildOrderRawPo::getSampleChildOrderRawId, Function.identity()));

        final List<SampleChildOrderRawPo> updatePoList = rawProductItemList.stream()
                .map(item -> {
                    final SampleChildOrderRawPo sampleChildOrderRawPo = new SampleChildOrderRawPo();
                    sampleChildOrderRawPo.setSampleParentOrderNo(sampleChildOrderPo.getSampleParentOrderNo());
                    sampleChildOrderRawPo.setSampleChildOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
                    sampleChildOrderRawPo.setSampleChildOrderId(sampleChildOrderPo.getSampleChildOrderId());
                    sampleChildOrderRawPo.setSku(item.getSku());
                    sampleChildOrderRawPo.setSkuBatchCode(item.getSkuBatchCode());
                    sampleChildOrderRawPo.setDeliveryCnt(item.getReturnCnt());
                    sampleChildOrderRawPo.setSampleRawBizType(SampleRawBizType.ACTUAL_DELIVER);
                    sampleChildOrderRawPo.setSampleChildOrderRawId(item.getSampleChildOrderRawId());
                    sampleChildOrderRawPo.setVersion(item.getVersion());

                    final SampleChildOrderRawPo dbSampleChildOrderRawPo = sampleChildOrderRawPoMap.get(item.getSampleChildOrderRawId());
                    final int deliverCnt = dbSampleChildOrderRawPo.getDeliveryCnt() - item.getReturnCnt();
                    if (deliverCnt < 0) {
                        throw new BizException("归还数不能超过发货数");
                    }
                    sampleChildOrderRawPo.setDeliveryCnt(deliverCnt);
                    return sampleChildOrderRawPo;
                }).collect(Collectors.toList());

        sampleChildOrderRawDao.updateBatchByIdVersion(updatePoList);
        long snowflakeId = idGenerateService.getSnowflakeId();
        // wms生成原料收货单
        final ReceiveOrderCreateMqDto receiveOrderCreateMqDto = SupplierSampleConverter.rawDtoToReceiptMqDto(dto, sampleChildOrderPo, snowflakeId);
        receiveOrderCreateMqDto.setKey(idGenerateService.getSnowflakeCode(dto.getSampleChildOrderNo() + "-"));

        consistencySendMqService.execSendMq(WmsReceiptHandler.class, receiveOrderCreateMqDto);
    }
}
