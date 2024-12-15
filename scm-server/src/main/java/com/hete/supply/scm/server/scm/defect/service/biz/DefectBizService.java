package com.hete.supply.scm.server.scm.defect.service.biz;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.plm.api.goods.entity.vo.PlmSkuVo;
import com.hete.supply.scm.api.scm.entity.dto.CostSkuDto;
import com.hete.supply.scm.api.scm.entity.dto.CostSkuItemDto;
import com.hete.supply.scm.api.scm.entity.dto.DefectHandingSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.DefectHandlingProgramme;
import com.hete.supply.scm.api.scm.entity.enums.DefectHandlingStatus;
import com.hete.supply.scm.api.scm.entity.enums.DefectHandlingType;
import com.hete.supply.scm.api.scm.entity.vo.CostSkuItemVo;
import com.hete.supply.scm.api.scm.entity.vo.DefectHandingExportVo;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.converter.ReceiveConverter;
import com.hete.supply.scm.server.scm.cost.service.base.CostBaseService;
import com.hete.supply.scm.server.scm.defect.converter.DefectHandlingConverter;
import com.hete.supply.scm.server.scm.defect.dao.DefectHandlingDao;
import com.hete.supply.scm.server.scm.defect.entity.bo.DefectHandlingPageBo;
import com.hete.supply.scm.server.scm.defect.entity.dto.*;
import com.hete.supply.scm.server.scm.defect.service.base.DefectBaseService;
import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderNoDto;
import com.hete.supply.scm.server.scm.entity.dto.WmsOnShelvesMqDto;
import com.hete.supply.scm.server.scm.entity.po.DefectHandlingPo;
import com.hete.supply.scm.server.scm.entity.vo.DefectHandingSearchVo;
import com.hete.supply.scm.server.scm.entity.vo.ReceiveOrderPrintVo;
import com.hete.supply.scm.server.scm.handler.WmsOnShelvesHandler;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.purchase.service.ref.PurchaseRefService;
import com.hete.supply.scm.server.scm.qc.service.ref.QcDefectRefService;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.supplier.converter.SupplierReturnConverter;
import com.hete.supply.scm.server.supplier.entity.bo.ReturnOrderBo;
import com.hete.supply.scm.server.supplier.entity.dto.OnShelvesOrderCreateResultMqDto;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import com.hete.supply.scm.server.supplier.purchase.service.ref.PurchaseDeliverRefService;
import com.hete.supply.scm.server.supplier.service.ref.ReturnRefService;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.basic.entity.dto.SkuBatchCreate4defectNoListDto;
import com.hete.supply.wms.api.basic.entity.vo.SimpleSkuBatchVo;
import com.hete.supply.wms.api.basic.entity.vo.SkuVo;
import com.hete.supply.wms.api.entry.entity.dto.ReceiveDeliveryOrderNoQueryDto;
import com.hete.supply.wms.api.entry.entity.dto.ReceiveOrderGetDto;
import com.hete.supply.wms.api.entry.entity.vo.ReceiveDeliveryOrderVo;
import com.hete.supply.wms.api.entry.entity.vo.ReceiveOrderForScmVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/6/21 09:37
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DefectBizService {
    private final DefectHandlingDao defectHandlingDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final DefectBaseService defectBaseService;
    private final ReturnRefService returnRefService;
    private final PlmRemoteService plmRemoteService;
    private final IdGenerateService idGenerateService;
    private final WmsRemoteService wmsRemoteService;
    private final PurchaseDeliverRefService purchaseDeliverRefService;
    private final SupplierDao supplierDao;
    private final PurchaseRefService purchaseRefService;
    private final QcDefectRefService qcDefectRefService;
    private final CostBaseService costBaseService;
    private final PurchaseChildOrderDao purchaseChildOrderDao;

    public CommonPageResult.PageInfo<DefectHandingSearchVo> searchDefect(DefectHandingSearchDto dto) {
        DefectHandlingPageBo defectHandlingPageBo = defectBaseService.searchDefectPage(dto);
        final IPage<DefectHandlingPo> pageResult = defectHandlingPageBo.getPageResult();
        final List<DefectHandlingPo> records = pageResult.getRecords();
        final Map<Long, List<String>> idFileCodeMap = defectHandlingPageBo.getIdFileCodeMap();

        final List<String> relatedOrderNoList = records.stream()
                .map(DefectHandlingPo::getRelatedOrderNo)
                .filter(StringUtils::isNotBlank)
                .filter(relatedOrderNo -> relatedOrderNo.startsWith(ScmConstant.WMS_RECEIVE_ORDER_NO_PREFIX))
                .distinct()
                .collect(Collectors.toList());
        final ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
        receiveOrderGetDto.setReceiveOrderNoList(relatedOrderNoList);
        final List<ReceiveOrderForScmVo> receiveOrderList = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto);
        final Map<String, String> receiveOrderNoWarehouseNameMap = receiveOrderList.stream()
                .collect(Collectors.toMap(ReceiveOrderForScmVo::getReceiveOrderNo,
                        ReceiveOrderForScmVo::getWarehouseName, (item1, item2) -> item1));

        final List<String> skuBatchCodeList = records.stream()
                .map(DefectHandlingPo::getSkuBatchCode)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());

        final List<SimpleSkuBatchVo> simpleSkuBatchVoList = wmsRemoteService.getSkuBatchByBatchCodes(skuBatchCodeList);
        final Map<String, String> batchCodeSupplierCodeMap = simpleSkuBatchVoList.stream()
                .collect(Collectors.toMap(SimpleSkuBatchVo::getBatchCode, SimpleSkuBatchVo::getSupplierCode));
        List<DefectHandingSearchVo> defectHandingSearchVoList = DefectHandlingConverter.defectHandlingPoToVo(records,
                idFileCodeMap, receiveOrderNoWarehouseNameMap, batchCodeSupplierCodeMap);

        return PageInfoUtil.getPageInfo(pageResult, defectHandingSearchVoList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void returnSupply(DefectHandlingNoListDto dto) {
        final List<String> defectHandlingNoList = dto.getDefectHandlingReturnInsideItemList()
                .stream()
                .map(DefectHandlingNoDto::getDefectHandlingNo)
                .collect(Collectors.toList());
        final List<DefectHandlingPo> defectHandlingPoList = this.checkDefectHandling(defectHandlingNoList);
        if (CollectionUtils.isEmpty(defectHandlingPoList)) {
            throw new ParamIllegalException("查找不到对应的次品处理记录，请刷新后重试!");
        }

        final DefectHandlingPo defectHandlingPo = defectHandlingPoList.get(0);
        final String defectBizNo = defectHandlingPo.getDefectBizNo();
        // 校验单据类型、来源单据号是否相同、收货单是否相同
        defectHandlingPoList.forEach(po -> {
            if (!DefectHandlingType.BULK_DEFECT.equals(po.getDefectHandlingType())) {
                throw new BizException("当前次品处理记录:{}，不为{}类型，无法操作!", po.getDefectHandlingNo(),
                        DefectHandlingType.BULK_DEFECT.getRemark());
            }
            if (!defectBizNo.equals(po.getDefectBizNo())) {
                throw new ParamIllegalException("当前次品处理记录:{}，来源单据号为:{}与其他单据不一致，无法操作!",
                        po.getDefectHandlingNo(), po.getDefectBizNo());
            }
        });
        final PurchaseDeliverOrderPo purchaseDeliverOrderPo = purchaseDeliverRefService.getPurchaseDeliverOrderByNo(defectBizNo);
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(purchaseDeliverOrderPo.getPurchaseChildOrderNo());
        // spm 生成退货单
        final List<String> skuList = defectHandlingPoList.stream()
                .map(DefectHandlingPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        ReturnOrderBo returnOrderBo = SupplierReturnConverter.defectHandlingPoToReturnBo(defectHandlingPoList, skuEncodeMap, null);
        returnOrderBo.setPurchaseChildOrderNo(purchaseDeliverOrderPo.getPurchaseChildOrderNo());
        returnOrderBo.setPlatform(purchaseChildOrderPo.getPlatform());
        final PurchaseReturnOrderPo returnOrderPo = returnRefService.createReturnOrder(returnOrderBo);
        // 释放可发货数
        purchaseRefService.addPurchaseShippableCntByNo(purchaseDeliverOrderPo.getPurchaseChildOrderNo(), returnOrderBo.getExpectedReturnCnt());

        // 更新退货单
        defectHandlingPoList.forEach(po -> po.setRelatedOrderNo(returnOrderPo.getReturnOrderNo()));
        // 更新次品处理
        defectBaseService.updateDefectHandlingPoList(defectHandlingPoList, DefectHandlingProgramme.RETURN_SUPPLY, returnOrderPo.getReturnOrderNo());

        // 处理次品记录后，变更质检单状态
        final List<Long> qcDetailIdList = defectHandlingPoList.stream()
                .map(DefectHandlingPo::getBizDetailId)
                .distinct()
                .collect(Collectors.toList());
        qcDefectRefService.afterDefectDeal(defectHandlingPoList.get(0).getQcOrderNo(), qcDetailIdList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void compromise(DefectCompromiseDto dto) {
        final List<DefectHandlingPo> defectHandlingPoList = this.checkDefectHandling(dto.getDefectHandlingNoList());

        final DefectHandlingPo defectHandlingPo = defectHandlingPoList.get(0);
        final String defectBizNo = defectHandlingPo.getDefectBizNo();
        final String qcOrderNo = defectHandlingPo.getQcOrderNo();
        // 校验单据类型
        // 校验单据是否来源于同一个收货单与质检单
        defectHandlingPoList.forEach(po -> {
            if (!DefectHandlingType.BULK_DEFECT.equals(po.getDefectHandlingType())
                    && !DefectHandlingType.REPAIR.equals(po.getDefectHandlingType())) {
                throw new BizException("当前次品处理记录:{}，不为{}、{}类型，无法操作!", po.getDefectHandlingNo(),
                        DefectHandlingType.BULK_DEFECT.getRemark(), DefectHandlingType.REPAIR.getRemark());
            }
            if (!defectBizNo.equals(po.getDefectBizNo())) {
                throw new ParamIllegalException("当前次品处理记录:{}，来源单据号为:{}与其他单据不一致，无法操作!",
                        po.getDefectHandlingNo(), po.getDefectBizNo());
            }
            if (!qcOrderNo.equals(po.getQcOrderNo())) {
                throw new ParamIllegalException("当前次品处理记录:{}，质检单号为:{}与其他单据不一致，无法操作!",
                        po.getDefectHandlingNo(), po.getQcOrderNo());
            }
        });

        // 更新次品处理
        defectBaseService.updateDefectHandlingPoList(defectHandlingPoList, DefectHandlingProgramme.COMPROMISE, null);

        WmsOnShelvesMqDto wmsOnShelvesMqDto = DefectHandlingConverter.convertPoToWmsOnShelvesDto(dto, defectHandlingPoList);
        wmsOnShelvesMqDto.setKey(idGenerateService.getSnowflakeCode(wmsOnShelvesMqDto.getReceiveOrderNo() + "-"));

        consistencySendMqService.execSendMq(WmsOnShelvesHandler.class, wmsOnShelvesMqDto);
        // 处理次品记录后，变更质检单状态
        final List<Long> qcDetailIdList = defectHandlingPoList.stream()
                .map(DefectHandlingPo::getBizDetailId)
                .distinct()
                .collect(Collectors.toList());
        qcDefectRefService.afterDefectDeal(defectHandlingPoList.get(0).getQcOrderNo(), qcDetailIdList);
    }

    private List<DefectHandlingPo> checkDefectHandling(List<String> defectHandlingNoList) {
        // 校验单据存在
        final List<DefectHandlingPo> defectHandlingPoList = defectHandlingDao.selectByDefectHandlingNoList(defectHandlingNoList);

        if (CollectionUtils.isEmpty(defectHandlingPoList) || defectHandlingPoList.size() != defectHandlingNoList.size()) {
            throw new ParamIllegalException("次品处理不存在，请刷新后重试！");
        }

        // 校验单据状态
        defectHandlingPoList.forEach(po -> {
            if (!DefectHandlingStatus.WAIT_CONFIRM.equals(po.getDefectHandlingStatus())
                    && !DefectHandlingStatus.CONFIRMED_FAIL.equals(po.getDefectHandlingStatus())) {
                throw new ParamIllegalException("当前次品处理记录:{}，不为{}状态，无法操作!", po.getDefectHandlingNo(),
                        DefectHandlingStatus.WAIT_CONFIRM.getRemark());
            }
        });

        List<String> qcOrderNoList = defectHandlingPoList.stream()
                .map(DefectHandlingPo::getQcOrderNo)
                .distinct()
                .collect(Collectors.toList());
        if (qcOrderNoList.size() > 1) {
            throw new ParamIllegalException("质检单号不一致，无法操作!");
        }

        return defectHandlingPoList;
    }

    /**
     * 次品换货
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/6/27 19:30
     */
    @Transactional(rollbackFor = Exception.class)
    public void exchangeGoods(DefectHandlingExchangeGoodsDto dto) {
        List<String> defectHandlingNoList = dto.getDefectHandlingExchangeSkuList()
                .stream()
                .map(DefectHandlingExchangeGoodsDto.DefectHandlingExchangeSkuDto::getDefectHandlingNo)
                .collect(Collectors.toList());
        List<String> skuList = dto.getDefectHandlingExchangeSkuList()
                .stream()
                .map(DefectHandlingExchangeGoodsDto.DefectHandlingExchangeSkuDto::getSku)
                .distinct()
                .collect(Collectors.toList());
        Map<String, String> defectHandlingNoSkuMap = dto.getDefectHandlingExchangeSkuList()
                .stream().collect(Collectors.toMap(DefectHandlingExchangeGoodsDto.DefectHandlingExchangeSkuDto::getDefectHandlingNo,
                        DefectHandlingExchangeGoodsDto.DefectHandlingExchangeSkuDto::getSku));
        final List<DefectHandlingPo> defectHandlingPoList = this.checkDefectHandling(defectHandlingNoList);

        final DefectHandlingType defectHandlingType = defectHandlingPoList.get(0).getDefectHandlingType();
        // 校验单据类型是否相同
        defectHandlingPoList.forEach(po -> {
            if (!defectHandlingType.equals(po.getDefectHandlingType())) {
                throw new BizException("当前次品处理记录:{}，不为{}类型，无法操作!", po.getDefectHandlingNo(),
                        defectHandlingType.getRemark());
            }
            // 验证批次码不能为空
            if (StringUtils.isBlank(po.getSkuBatchCode())) {
                throw new BizException("当前次品处理记录:{}的批次码为空，无法操作！请联系管理员", po.getDefectHandlingNo());
            }
        });


        final List<PlmSkuVo> plmSkuVoList = plmRemoteService.getSkuEncodeBySku(skuList);
        if (CollectionUtils.isEmpty(plmSkuVoList)) {
            throw new BizException("存在sku不合法，无法操作!");
        }
        List<String> plmSkuList = plmSkuVoList.stream().map(PlmSkuVo::getSkuCode).distinct().collect(Collectors.toList());
        if (plmSkuList.size() != skuList.size()) {
            throw new BizException("存在sku不合法，无法操作!");
        }

        // 组装查询sku价格入参
        List<CostSkuItemDto> costSkuItemList = new ArrayList<>();
        for (String sku : skuList) {
            CostSkuItemDto costSkuItemDto = new CostSkuItemDto();
            costSkuItemDto.setSku(sku);
            costSkuItemList.add(costSkuItemDto);
        }
        // 查询sku价格
        List<CostSkuItemVo> costSkuItemVoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(costSkuItemList)) {
            CostSkuDto costSkuDto = new CostSkuDto();
            costSkuDto.setCostSkuItemList(costSkuItemList);
            costSkuItemVoList = costBaseService.getCostBySku(costSkuDto);
            log.info("查询sku价格的返回costSkuItemVoList=>{}", costSkuItemVoList);
        }

        // 获取原批次码的单价
        List<String> skuBatchCodeList = defectHandlingPoList.stream().map(DefectHandlingPo::getSkuBatchCode).distinct().collect(Collectors.toList());
        Map<String, BigDecimal> skuBatchPriceWmsMap = wmsRemoteService.getSkuBatchPriceMapBySkuBatchList(skuBatchCodeList);
        Map<String, BigDecimal> skuBatchPriceMap = new HashMap<>();
        // 验证价格是否取到
        for (DefectHandlingPo defectHandlingPo : defectHandlingPoList) {
            String skuBatchCode = defectHandlingPo.getSkuBatchCode();
            if (skuBatchPriceWmsMap.containsKey(skuBatchCode)
                    && skuBatchPriceWmsMap.get(skuBatchCode).compareTo(BigDecimal.ZERO) > 0) {
                skuBatchPriceMap.put(skuBatchCode, skuBatchPriceWmsMap.get(skuBatchCode));
                continue;
            }

            // 获取换货后的sku来获取对应价格
            String sku = defectHandlingNoSkuMap.get(defectHandlingPo.getDefectHandlingNo());
            costSkuItemVoList.stream()
                    .filter(itemVo -> itemVo.getSku().equals(sku))
                    .findFirst()
                    .ifPresent(costSkuItemVo -> skuBatchPriceMap.put(skuBatchCode, costSkuItemVo.getInventoryPrice()));

            if (!skuBatchPriceMap.containsKey(skuBatchCode)) {
                throw new BizException("批次码:{}获取不到对应价格，请联系管理员!", skuBatchCode);
            }

        }

        //验证入库仓
        BooleanType warehouseVerify = wmsRemoteService.mateWarehouseCodeAndReceiveType(WmsEnum.ReceiveType.CHANGE_GOODS, dto.getWarehouseCode());
        Assert.isTrue(warehouseVerify.isBooleanVal(), () -> new ParamIllegalException(ScmConstant.VERIFY_WAREHOUSE_PROMPTS));

        //请求wms获取批次码
        SkuBatchCreate4defectNoListDto skuBatchCreate4defectNoListDto = new SkuBatchCreate4defectNoListDto();
        List<SkuBatchCreate4defectNoListDto.DefectHandlingDto> wmsSkuBatchDtoList = new ArrayList<>();
        for (DefectHandlingExchangeGoodsDto.DefectHandlingExchangeSkuDto defectHandlingExchangeSkuDto : dto.getDefectHandlingExchangeSkuList()) {
            SkuBatchCreate4defectNoListDto.DefectHandlingDto defectHandlingDto = new SkuBatchCreate4defectNoListDto.DefectHandlingDto();
            defectHandlingDto.setDefectHandlingNo(defectHandlingExchangeSkuDto.getDefectHandlingNo());
            defectHandlingDto.setSkuCodeList(List.of(defectHandlingExchangeSkuDto.getSku()));
            wmsSkuBatchDtoList.add(defectHandlingDto);
        }
        skuBatchCreate4defectNoListDto.setDefectHandlingNoList(wmsSkuBatchDtoList);
        Map<String, List<SkuVo>> skuBatchCodeMap = wmsRemoteService.createBatchCode4DefectHandlingList(skuBatchCreate4defectNoListDto);


        //推送MQ、更新数据
        defectBaseService.exchangeGoodsMq(dto, defectHandlingPoList, skuBatchCodeMap, defectHandlingNoSkuMap, skuBatchPriceMap);

        // 处理次品记录后，变更质检单状态
        final List<Long> qcDetailIdList = defectHandlingPoList.stream()
                .map(DefectHandlingPo::getBizDetailId)
                .distinct()
                .collect(Collectors.toList());
        qcDefectRefService.afterDefectDeal(defectHandlingPoList.get(0).getQcOrderNo(), qcDetailIdList);
    }

    /**
     * 次品报废
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/6/28 09:53
     */
    @Transactional(rollbackFor = Exception.class)
    public void scrapped(DefectHandlingScrappedDto dto) {
        final List<DefectHandlingPo> defectHandlingPoList = this.checkDefectHandling(dto.getDefectHandlingNoList());

        final DefectHandlingType defectHandlingType = defectHandlingPoList.get(0).getDefectHandlingType();
        // 校验单据类型是否相同
        defectHandlingPoList.forEach(po -> {
            if (!defectHandlingType.equals(po.getDefectHandlingType())) {
                throw new BizException("当前次品处理记录:{}，不为{}类型，无法操作!", po.getDefectHandlingNo(),
                        defectHandlingType.getRemark());
            }
        });
        //验证入库仓
        BooleanType warehouseVerify = wmsRemoteService.mateWarehouseCodeAndReceiveType(WmsEnum.ReceiveType.DEFECTIVE_PROCESS_PRODUCT, dto.getWarehouseCode());
        Assert.isTrue(warehouseVerify.isBooleanVal(), () -> new ParamIllegalException(ScmConstant.VERIFY_WAREHOUSE_PROMPTS));

        //推送MQ更新数据
        defectBaseService.scrappedMq(dto, defectHandlingPoList);
        // 处理次品记录后，变更质检单状态
        final List<Long> qcDetailIdList = defectHandlingPoList.stream()
                .map(DefectHandlingPo::getBizDetailId)
                .distinct()
                .collect(Collectors.toList());
        qcDefectRefService.afterDefectDeal(defectHandlingPoList.get(0).getQcOrderNo(), qcDetailIdList);
    }

    /**
     * 次品退供
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/6/28 10:52
     */
    @Transactional(rollbackFor = Exception.class)
    public void returnSupply(DefectHandlingReturnInsideDto dto) {
        List<String> defectHandlingNoList = dto.getDefectHandlingReturnInsideItemList()
                .stream()
                .map(DefectHandlingReturnInsideDto.DefectHandlingReturnInsideItemDto::getDefectHandlingNo)
                .collect(Collectors.toList());
        final List<DefectHandlingPo> defectHandlingPoList = this.checkDefectHandling(defectHandlingNoList);
        if (CollectionUtils.isEmpty(defectHandlingPoList)) {
            throw new ParamIllegalException("查找不到对应的次品处理记录，请刷新后重试!");
        }

        final DefectHandlingPo defectHandlingPo = defectHandlingPoList.get(0);
        final String supplierCode = defectHandlingPo.getSupplierCode();
        final String qcOrderNo = defectHandlingPo.getQcOrderNo();
        final DefectHandlingType defectHandlingType = defectHandlingPo.getDefectHandlingType();
        // 校验单据类型、供应商是否相同、收货单是否相同
        defectHandlingPoList.forEach(po -> {
            if (!defectHandlingType.equals(po.getDefectHandlingType())) {
                throw new BizException("当前次品处理记录:{}，不为{}类型，无法操作!", po.getDefectHandlingNo(),
                        defectHandlingType.getRemark());
            }
            if (!supplierCode.equals(po.getSupplierCode())) {
                throw new ParamIllegalException("当前次品处理记录:{}，供应商为:{}与其他单据不一致，无法操作!",
                        po.getDefectHandlingNo(), po.getSupplierCode());
            }
            if (!qcOrderNo.equals(po.getQcOrderNo())) {
                throw new ParamIllegalException("当前次品处理记录:{}，质检单为:{}与其他单据不一致，无法操作!",
                        po.getDefectHandlingNo(), po.getQcOrderNo());
            }
        });

        SupplierPo supplierPo = supplierDao.getBySupplierCode(dto.getSupplierCode());
        if (supplierPo == null) {
            throw new BizException("查找不到对应的供应商，请刷新后重试!");
        }

        // spm 生成退货单
        final List<String> skuList = defectHandlingPoList.stream()
                .map(DefectHandlingPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        ReturnOrderBo returnOrderBo = SupplierReturnConverter.defectHandlingPoToReturnBo(defectHandlingPoList, skuEncodeMap, supplierPo);
        returnOrderBo.setOperator(GlobalContext.getUserKey());
        returnOrderBo.setOperatorUsername(GlobalContext.getUsername());
        returnOrderBo.setPlatform(defectHandlingPo.getPlatform());
        final PurchaseReturnOrderPo returnOrderPo = returnRefService.createReturnOrder(returnOrderBo);

        // 更新次品处理
        defectBaseService.updateDefectHandlingPoList(defectHandlingPoList, DefectHandlingProgramme.RETURN_SUPPLY,
                returnOrderPo.getReturnOrderNo());
        // 处理次品记录后，变更质检单状态
        final List<Long> qcDetailIdList = defectHandlingPoList.stream()
                .map(DefectHandlingPo::getBizDetailId)
                .distinct()
                .collect(Collectors.toList());
        qcDefectRefService.afterDefectDeal(defectHandlingPoList.get(0).getQcOrderNo(), qcDetailIdList);
    }

    /**
     * 加工单次品退供
     *
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    public void returnSupplyProcess(DefectHandlingReturnProcessDto dto) {
        List<String> defectHandlingNoList = dto.getDefectHandlingReturnInsideItemList()
                .stream()
                .map(DefectHandlingReturnProcessDto.DefectHandlingReturnInsideItemDto::getDefectHandlingNo)
                .collect(Collectors.toList());
        List<DefectHandlingPo> defectHandlingPoList = this.checkDefectHandling(defectHandlingNoList);
        if (CollectionUtils.isEmpty(defectHandlingPoList)) {
            throw new ParamIllegalException("查找不到对应的次品处理记录，请刷新后重试!");
        }

        final DefectHandlingPo defectHandlingPo = defectHandlingPoList.get(0);
        if (!DefectHandlingType.PROCESS_DEFECT.equals(defectHandlingPo.getDefectHandlingType())) {
            throw new BizException("当前次品处理记录:{}，不为{}类型，无法操作!", defectHandlingPo.getDefectBizNo(),
                    DefectHandlingType.PROCESS_DEFECT.getRemark());
        }

        // 校验单据类型、收货单是否相同
        List<DefectHandlingType> defectHandlingTypeList = defectHandlingPoList.stream()
                .map(DefectHandlingPo::getDefectHandlingType)
                .distinct()
                .collect(Collectors.toList());
        if (defectHandlingTypeList.size() > 1) {
            throw new ParamIllegalException("次品记录类型不一致，无法操作!");
        }

        // spm 生成退货单
        final List<String> skuList = defectHandlingPoList.stream()
                .map(DefectHandlingPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        List<DefectHandlingPo> newDefectHandlingPoList = defectHandlingPoList.stream().map(po -> {
            DefectHandlingPo newDefectHandlingPo = new DefectHandlingPo();
            newDefectHandlingPo.setDefectHandlingNo(po.getDefectHandlingNo());
            newDefectHandlingPo.setDefectHandlingStatus(po.getDefectHandlingStatus());
            newDefectHandlingPo.setSku(po.getSku());
            newDefectHandlingPo.setSkuBatchCode(po.getSkuBatchCode());
            newDefectHandlingPo.setDefectHandlingProgramme(po.getDefectHandlingProgramme());
            newDefectHandlingPo.setDefectHandlingId(po.getDefectHandlingId());
            newDefectHandlingPo.setDefectHandlingType(po.getDefectHandlingType());
            newDefectHandlingPo.setQcCnt(po.getQcCnt());
            newDefectHandlingPo.setPassCnt(po.getPassCnt());
            newDefectHandlingPo.setNotPassCnt(po.getNotPassCnt());
            newDefectHandlingPo.setBizDetailId(po.getBizDetailId());
            newDefectHandlingPo.setQcOrderNo(po.getQcOrderNo());
            newDefectHandlingPo.setDefectHandlingNo(po.getDefectHandlingNo());
            newDefectHandlingPo.setDefectBizNo(po.getDefectBizNo());
            newDefectHandlingPo.setSettlePrice(po.getSettlePrice());
            newDefectHandlingPo.setReceiveOrderNo(po.getReceiveOrderNo());
            newDefectHandlingPo.setReturnOrderNo(po.getReturnOrderNo());
            newDefectHandlingPo.setRelatedOrderNo(po.getRelatedOrderNo());
            newDefectHandlingPo.setSupplierCode(dto.getSupplierCode());
            newDefectHandlingPo.setSupplierName(dto.getSupplierName());
            newDefectHandlingPo.setWarehouseCode(po.getWarehouseCode());
            newDefectHandlingPo.setPlatform(po.getPlatform());
            return newDefectHandlingPo;
        }).collect(Collectors.toList());

        ReturnOrderBo returnOrderBo = SupplierReturnConverter.defectHandlingPoToReturnBo(newDefectHandlingPoList,
                skuEncodeMap, null);

        returnOrderBo.setOperator(GlobalContext.getUserKey());
        returnOrderBo.setOperatorUsername(GlobalContext.getUsername());
        returnOrderBo.setPlatform(defectHandlingPo.getPlatform());
        PurchaseReturnOrderPo returnOrder = returnRefService.createReturnOrder(returnOrderBo);

        // 更新关联的单据号
        defectHandlingPoList = defectHandlingPoList.stream().peek(po -> {
            po.setDefectHandlingStatus(DefectHandlingStatus.CONFIRMED);
            po.setDefectHandlingProgramme(DefectHandlingProgramme.RETURN_SUPPLY);
            po.setConfirmUser(GlobalContext.getUserKey());
            po.setConfirmUsername(GlobalContext.getUsername());
            po.setConfirmTime(LocalDateTimeUtil.now());
            po.setRelatedOrderNo(returnOrder.getReturnOrderNo());
        }).collect(Collectors.toList());
        defectHandlingDao.updateBatchByIdVersion(defectHandlingPoList);

        // 处理次品记录后，变更质检单状态
        final List<Long> qcDetailIdList = defectHandlingPoList.stream()
                .map(DefectHandlingPo::getBizDetailId)
                .distinct()
                .collect(Collectors.toList());
        qcDefectRefService.afterDefectDeal(defectHandlingPoList.get(0).getQcOrderNo(), qcDetailIdList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateDefectCompromise(OnShelvesOrderCreateResultMqDto message) {
        final List<String> defectHandlingNoList = message.getDefectHandlingNoList();
        final List<DefectHandlingPo> defectHandlingPoList = defectHandlingDao.selectByDefectHandlingNoList(defectHandlingNoList);
        if (CollectionUtils.isEmpty(defectHandlingPoList) || defectHandlingPoList.size() != defectHandlingNoList.size()) {
            throw new BizException("次品处理不存在，请刷新后重试！");
        }

        if (WmsEnum.OnShelvesOrderCreateResult.BUSSINESS_ERROR.equals(message.getResult())) {
            defectHandlingPoList.forEach(po -> {
                po.setDefectHandlingStatus(po.getDefectHandlingStatus().toFailStatus());
                po.setFailReason(message.getFailReason());
            });
            defectHandlingDao.updateBatchByIdVersion(defectHandlingPoList);
            // 更新质检单状态回到次品处理中
            qcDefectRefService.updateQcOrderStatusToFail(defectHandlingPoList.get(0).getQcOrderNo());
        } else if (WmsEnum.OnShelvesOrderCreateResult.SUCCESS.equals(message.getResult())) {
            defectHandlingPoList.forEach(po -> {
                po.setRelatedOrderNo(message.getOnShelvesOrderNo());
            });
            defectHandlingDao.updateBatchByIdVersion(defectHandlingPoList);
            qcDefectRefService.afterSuccessNotify(defectHandlingPoList.get(0).getQcOrderNo());
        }
    }

    public ReceiveOrderPrintVo printReceiveOrderByNo(ReceiveOrderNoDto dto) {
        final ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
        receiveOrderGetDto.setReceiveOrderNoList(Collections.singletonList(dto.getReceiptOrderNo()));
        final List<ReceiveOrderForScmVo> receiveOrderList = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto);

        return ReceiveConverter.wmsVoToReceiveOrderVo(receiveOrderList.get(0));
    }

    /**
     * 导出
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/3/28 14:00
     */
    @Transactional(rollbackFor = Exception.class)
    public void defectExport(DefectHandingSearchDto dto) {
        Integer exportTotals = this.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && exportTotals > 0, () -> new ParamIllegalException("导出数据为空"));

        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                FileOperateBizType.SCM_DEFECT_HANDING_EXPORT.getCode(), dto));
    }

    /**
     * 列表导出统计
     *
     * @param dto:
     * @return Integer
     * @author ChenWenLong
     * @date 2024/3/28 13:49
     */
    public Integer getExportTotals(DefectHandingSearchDto dto) {
        return this.getExportListInfo(dto).size();
    }

    /**
     * 导出基础数据
     *
     * @param dto:
     * @return List<DefectHandingExportVo>
     * @author ChenWenLong
     * @date 2024/2/28 16:20
     */
    public List<DefectHandingExportVo> getExportListInfo(DefectHandingSearchDto dto) {
        DefectHandlingPageBo defectHandlingPageBo = defectBaseService.searchDefectPage(dto);
        final IPage<DefectHandlingPo> pageResult = defectHandlingPageBo.getPageResult();
        final List<DefectHandlingPo> records = pageResult.getRecords();

        if (CollectionUtils.isEmpty(records)) {
            return new ArrayList<>();
        }
        final List<String> relatedOrderNoList = records.stream()
                .map(DefectHandlingPo::getRelatedOrderNo)
                .filter(StringUtils::isNotBlank)
                .filter(relatedOrderNo -> relatedOrderNo.startsWith(ScmConstant.WMS_RECEIVE_ORDER_NO_PREFIX))
                .distinct()
                .collect(Collectors.toList());
        final ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
        receiveOrderGetDto.setReceiveOrderNoList(relatedOrderNoList);
        final List<ReceiveOrderForScmVo> receiveOrderList = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto);
        final Map<String, String> receiveOrderNoWarehouseNameMap = receiveOrderList.stream()
                .collect(Collectors.toMap(ReceiveOrderForScmVo::getReceiveOrderNo,
                        ReceiveOrderForScmVo::getWarehouseName, (item1, item2) -> item1));

        final List<String> receiveOrderNoList = records.stream()
                .map(DefectHandlingPo::getReceiveOrderNo)
                .collect(Collectors.toList());
        final ReceiveDeliveryOrderNoQueryDto receiveDeliveryOrderNoQueryDto = new ReceiveDeliveryOrderNoQueryDto();
        receiveDeliveryOrderNoQueryDto.setReceiverOrderNoList(receiveOrderNoList);
        final List<ReceiveDeliveryOrderVo> receiveDeliveryOrderList = wmsRemoteService.queryDeliveryOrderNoList(receiveDeliveryOrderNoQueryDto);
        final Map<String, String> receiveDeliveryNoMap = receiveDeliveryOrderList.stream()
                .collect(Collectors.toMap(ReceiveDeliveryOrderVo::getReceiveOrderNo,
                        ReceiveDeliveryOrderVo::getDeliveryOrderNo));
        final List<String> qcOrderNoList = records.stream()
                .map(DefectHandlingPo::getQcOrderNo)
                .distinct()
                .collect(Collectors.toList());

        return DefectHandlingConverter.defectHandlingPoToExportVo(records, receiveOrderNoWarehouseNameMap,
                receiveDeliveryNoMap);

    }

    /**
     * 导出数据
     *
     * @param dto:
     * @return List<DefectHandingExportVo>
     * @author ChenWenLong
     * @date 2024/2/28 16:20
     */
    public CommonResult<ExportationListResultBo<DefectHandingExportVo>> getExportList(DefectHandingSearchDto dto) {
        ExportationListResultBo<DefectHandingExportVo> resultBo = new ExportationListResultBo<>();
        List<DefectHandingExportVo> defectHandingExportVoList = this.getExportListInfo(dto);
        resultBo.setRowDataList(defectHandingExportVoList);
        return CommonResult.success(resultBo);
    }
}
