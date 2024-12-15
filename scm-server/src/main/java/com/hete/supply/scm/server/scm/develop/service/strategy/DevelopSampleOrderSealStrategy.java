package com.hete.supply.scm.server.scm.develop.service.strategy;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleStatus;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleType;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.adjust.entity.bo.GoodsPriceAddBo;
import com.hete.supply.scm.server.scm.adjust.service.base.GoodsPriceBaseService;
import com.hete.supply.scm.server.scm.dao.PlmSkuDao;
import com.hete.supply.scm.server.scm.develop.dao.DevelopChildOrderDao;
import com.hete.supply.scm.server.scm.develop.dao.DevelopPamphletOrderDao;
import com.hete.supply.scm.server.scm.develop.dao.DevelopSampleOrderDao;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopChildBatchCodeCostPriceBo;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopOrderPriceCreateBo;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopOrderPriceSaveDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleOrderSubmitHandleDto;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopChildOrderPo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPamphletOrderPo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderPo;
import com.hete.supply.scm.server.scm.develop.enums.DevelopOrderPriceType;
import com.hete.supply.scm.server.scm.develop.enums.DevelopSampleDirection;
import com.hete.supply.scm.server.scm.develop.enums.DevelopSampleSaleHandle;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopChildBaseService;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopSampleOrderBaseService;
import com.hete.supply.scm.server.scm.entity.bo.ProduceDataUpdatePurchasePriceBo;
import com.hete.supply.scm.server.scm.entity.po.PlmSkuPo;
import com.hete.supply.scm.server.scm.process.entity.dto.UpdateBatchCodePriceDto;
import com.hete.supply.scm.server.scm.service.base.ProduceDataBaseService;
import com.hete.supply.scm.server.scm.service.base.WmsMqBaseService;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.basic.entity.dto.SkuBatchCreate4defectNoListDto;
import com.hete.supply.wms.api.basic.entity.vo.SkuVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.consistency.core.service.ConsistencyService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 闪售样品单处理策略
 *
 * @author ChenWenLong
 * @date 2024/3/23 22:24
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DevelopSampleOrderSealStrategy implements DevelopSampleOrderHandleStrategy {

    private final WmsRemoteService wmsRemoteService;
    private final DevelopSampleOrderDao developSampleOrderDao;
    private final DevelopSampleOrderBaseService developSampleOrderBaseService;
    private final ProduceDataBaseService produceDataBaseService;
    private final PlmSkuDao plmSkuDao;
    private final WmsMqBaseService wmsMqBaseService;
    private final DevelopChildOrderDao developChildOrderDao;
    private final DevelopPamphletOrderDao developPamphletOrderDao;
    private final ConsistencyService consistencyService;
    private final DevelopChildBaseService developChildBaseService;
    private final GoodsPriceBaseService goodsPriceBaseService;

    @Override
    public void submitHandleVerify(List<DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto> dtoList,
                                   List<DevelopSampleOrderPo> developSampleOrderPoList) {

        if (CollectionUtils.isEmpty(developSampleOrderPoList)) {
            throw new ParamIllegalException("样品单数据不能为空，请刷新页面后重试！");
        }

        // 入库时验证入库仓和入库类型是否正确
        for (DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto itemDto : dtoList) {

            Assert.isTrue(StringUtils.isNotBlank(itemDto.getWarehouseCode()), () -> new BizException("收货仓库编码不能为空！"));
            Assert.isTrue(StringUtils.isNotBlank(itemDto.getWarehouseName()), () -> new BizException("收货仓库名称不能为空！"));

            //验证入库仓
            BooleanType warehouseVerify = wmsRemoteService.mateWarehouseCodeAndReceiveType(WmsEnum.ReceiveType.FAST_SALE, itemDto.getWarehouseCode());
            Assert.isTrue(warehouseVerify.isBooleanVal(), () -> new ParamIllegalException(ScmConstant.VERIFY_WAREHOUSE_PROMPTS));


            // 验证货物走向
            Assert.isTrue(DevelopSampleDirection.WAREHOUSING.equals(itemDto.getDevelopSampleDirection()),
                    () -> new ParamIllegalException("闪售样品单的货物走向只能选:{}，请修改后重试！", DevelopSampleDirection.WAREHOUSING.getRemark()));

            // 验证操作枚举
            Assert.isTrue(null != itemDto.getDevelopSampleSaleHandle(),
                    () -> new BizException("闪售的样品单关联操作不能为空，请联系管理员后重试！"));

            // 验证样品单的sku以及关联操作
            String developSampleOrderNo = itemDto.getDevelopSampleOrderNo();
            DevelopSampleOrderPo developSampleOrderPo = developSampleOrderPoList.stream()
                    .filter(po -> po.getDevelopSampleOrderNo().equals(developSampleOrderNo))
                    .filter(po -> po.getVersion().equals(itemDto.getVersion()))
                    .findFirst()
                    .orElse(null);
            if (null == developSampleOrderPo) {
                throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
            }

            // 全新开款
            if (DevelopSampleSaleHandle.DISBURSEMENT_NEW.equals(itemDto.getDevelopSampleSaleHandle())) {
                if (StringUtils.isNotBlank(developSampleOrderPo.getSku())) {
                    throw new ParamIllegalException("样品单号:{}已存在SKU:{}，禁止进行{}操作！",
                            developSampleOrderNo,
                            developSampleOrderPo.getSku(),
                            DevelopSampleSaleHandle.DISBURSEMENT_NEW.getRemark());
                }
            }

            // 绑定已有SKU
            if (DevelopSampleSaleHandle.BINDING_SKU.equals(itemDto.getDevelopSampleSaleHandle())) {
                Assert.isTrue(StringUtils.isNotBlank(itemDto.getSku()),
                        () -> new BizException("绑定SKU的操作时，SKU不能为空，请填写SKU后重试！"));
                if (StringUtils.isNotBlank(developSampleOrderPo.getSku())) {
                    throw new ParamIllegalException("样品单号:{}已存在SKU:{}，禁止进行{}操作！",
                            developSampleOrderNo,
                            developSampleOrderPo.getSku(),
                            DevelopSampleSaleHandle.BINDING_SKU.getRemark());

                }
            }

            // 样品单存在SKU
            if (DevelopSampleSaleHandle.ALREADY_BINDING.equals(itemDto.getDevelopSampleSaleHandle())) {
                if (StringUtils.isBlank(developSampleOrderPo.getSku())) {
                    throw new ParamIllegalException("样品单号:{}不存在SKU，禁止进行{}操作！",
                            developSampleOrderNo,
                            DevelopSampleSaleHandle.ALREADY_BINDING.getRemark());
                }
            }


            // 判断样品单状态是否正确
            developSampleOrderPo.getDevelopSampleStatus().submitHandleVerify();

        }

    }

    @Override
    public void developSampleOrderSubmitHandle(List<DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto> dtoList,
                                               List<DevelopSampleOrderPo> developSampleOrderPoList) {
        log.info("样品单确认处理-闪售的DTO={},PO={}", JacksonUtil.parse2Str(dtoList), JacksonUtil.parse2Str(developSampleOrderPoList));
        if (CollectionUtils.isEmpty(developSampleOrderPoList)) {
            throw new ParamIllegalException("样品单数据不能为空，请刷新页面后重试！");
        }

        // 用于验证存储一个样品单只能添加一个默认渠道大货价格
        Map<String, BigDecimal> developSampleOrderNoPriceMap = new HashMap<>();
        for (DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto itemDto : dtoList) {
            // 验证一个样品单只能设置一个渠道大货价格
            if (CollectionUtils.isNotEmpty(itemDto.getDevelopOrderPriceList())) {
                for (DevelopOrderPriceSaveDto developOrderPriceSaveDto : itemDto.getDevelopOrderPriceList()) {
                    if (BooleanType.TRUE.equals(developOrderPriceSaveDto.getIsDefaultPrice())) {
                        developSampleOrderNoPriceMap.put(itemDto.getDevelopSampleOrderNo(), developOrderPriceSaveDto.getPrice());
                    }
                }
            }
        }


        // 按操作进行处理逻辑
        Map<DevelopSampleSaleHandle, List<DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto>> developSampleSaleHandleMap = dtoList.stream()
                .collect(Collectors.groupingBy(DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto::getDevelopSampleSaleHandle));


        //全新开款 处理逻辑 请求plm创建sku
        List<DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto> disbursementNewList = developSampleSaleHandleMap.get(DevelopSampleSaleHandle.DISBURSEMENT_NEW);
        if (CollectionUtils.isNotEmpty(disbursementNewList)) {
            this.disbursementNewUpdate(disbursementNewList);
        }

        //绑定已有SKU 处理逻辑 入参填入sku
        List<DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto> bindingSkuList = developSampleSaleHandleMap.get(DevelopSampleSaleHandle.BINDING_SKU);
        if (CollectionUtils.isNotEmpty(bindingSkuList)) {
            this.bindingSkuUpdate(bindingSkuList, developSampleOrderNoPriceMap);
        }

        //已存在sku的样品单 处理逻辑 原旧样品单已存在sku
        List<DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto> alreadyBindingList = developSampleSaleHandleMap.get(DevelopSampleSaleHandle.ALREADY_BINDING);
        if (CollectionUtils.isNotEmpty(alreadyBindingList)) {
            this.bindingSkuUpdate(alreadyBindingList, developSampleOrderNoPriceMap);
        }

    }

    /**
     * 绑定已有SKU 处理逻辑
     *
     * @param bindingSkuList:
     * @return void
     * @author ChenWenLong
     * @date 2024/3/25 18:04
     */
    private void bindingSkuUpdate(List<DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto> bindingSkuList,
                                  Map<String, BigDecimal> developSampleOrderNoPriceMap) {
        List<String> developSampleOrderNoList = bindingSkuList.stream()
                .map(DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto::getDevelopSampleOrderNo)
                .distinct()
                .collect(Collectors.toList());
        List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByNoList(developSampleOrderNoList);
        if (CollectionUtils.isEmpty(developSampleOrderPoList)) {
            throw new ParamIllegalException("绑定SKU的样品单数据不能为空，请刷新页面后重试！");
        }

        // 查询sku对应spu
        List<String> skuList = bindingSkuList.stream()
                .map(DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto::getSku)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        List<PlmSkuPo> plmSkuPoList = plmSkuDao.getList(skuList);

        //请求wms获取批次码
        SkuBatchCreate4defectNoListDto skuBatchCreate4defectNoListDto = new SkuBatchCreate4defectNoListDto();
        List<SkuBatchCreate4defectNoListDto.DefectHandlingDto> wmsSkuBatchDtoList = new ArrayList<>();
        for (DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto developSampleOrderSubmitHandleItemDto : bindingSkuList) {
            SkuBatchCreate4defectNoListDto.DefectHandlingDto defectHandlingDto = new SkuBatchCreate4defectNoListDto.DefectHandlingDto();
            defectHandlingDto.setDefectHandlingNo(developSampleOrderSubmitHandleItemDto.getDevelopSampleOrderNo());
            defectHandlingDto.setSkuCodeList(List.of(developSampleOrderSubmitHandleItemDto.getSku()));
            DevelopSampleOrderPo developSampleOrderPo = developSampleOrderPoList.stream()
                    .filter(po -> po.getDevelopSampleOrderNo().equals(developSampleOrderSubmitHandleItemDto.getDevelopSampleOrderNo()))
                    .findFirst()
                    .orElse(null);
            if (null == developSampleOrderPo) {
                throw new BizException("查询不到对应样品单:{}的信息，请联系系统管理员！",
                        developSampleOrderSubmitHandleItemDto.getDevelopSampleOrderNo());
            }
            defectHandlingDto.setSupplierCode(developSampleOrderPo.getSupplierCode());
            defectHandlingDto.setSupplierName(developSampleOrderPo.getSupplierName());
            wmsSkuBatchDtoList.add(defectHandlingDto);
        }
        Map<String, List<SkuVo>> skuBatchCodeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(wmsSkuBatchDtoList)) {
            skuBatchCreate4defectNoListDto.setDefectHandlingNoList(wmsSkuBatchDtoList);
            skuBatchCodeMap = wmsRemoteService.createBatchCode4DefectHandlingList(skuBatchCreate4defectNoListDto);
        }

        // 更新WMS的批次码单价
        List<DevelopChildBatchCodeCostPriceBo> skuBatchCodePriceBoList = new ArrayList<>();

        // 更新样品单渠道大货价格的信息
        List<DevelopOrderPriceCreateBo> developOrderPriceCreateBoList = new ArrayList<>();

        // 更新商品调价的价格
        List<GoodsPriceAddBo> goodsPriceAddBoList = new ArrayList<>();

        //更新样品单的信息
        for (DevelopSampleOrderPo sampleOrderPo : developSampleOrderPoList) {
            Map<String, List<SkuVo>> finalSkuBatchCodeMap = skuBatchCodeMap;
            bindingSkuList.stream().filter(bindingSku -> bindingSku.getDevelopSampleOrderNo().equals(sampleOrderPo.getDevelopSampleOrderNo()))
                    .findFirst()
                    .ifPresent(bindingSkuDto -> {
                        String sku = bindingSkuDto.getSku();
                        PlmSkuPo plmSkuPo = plmSkuPoList.stream()
                                .filter(po -> po.getSku().equals(sku))
                                .findFirst()
                                .orElse(null);
                        if (plmSkuPo == null) {
                            throw new BizException("sku:{}还没同步到scm系统", sku);
                        }
                        if (StringUtils.isBlank(plmSkuPo.getSpu())) {
                            throw new BizException("sku:{}对应的spu还没同步到scm系统", sku);
                        }

                        if (!developSampleOrderNoPriceMap.containsKey(sampleOrderPo.getDevelopSampleOrderNo())) {
                            throw new BizException("样品单确认处理时数据错误，获取不到样品单号:{}的渠道大货价格，请联系管理员后重试！", sampleOrderPo.getDevelopSampleOrderNo());
                        }

                        String spu = plmSkuPo.getSpu();
                        sampleOrderPo.setSpu(spu);
                        sampleOrderPo.setSku(sku);
                        sampleOrderPo.setDevelopSampleStatus(DevelopSampleStatus.WAIT_RECEIVE);
                        sampleOrderPo.setSkuBatchSamplePrice(bindingSkuDto.getSkuBatchSamplePrice());
                        sampleOrderPo.setWarehouseCode(bindingSkuDto.getWarehouseCode());
                        sampleOrderPo.setWarehouseName(bindingSkuDto.getWarehouseName());
                        sampleOrderPo.setDevelopSampleDirection(bindingSkuDto.getDevelopSampleDirection());
                        sampleOrderPo.setHandleUser(GlobalContext.getUserKey());
                        sampleOrderPo.setHandleUsername(GlobalContext.getUsername());
                        sampleOrderPo.setHandleTime(LocalDateTime.now());
                        // 获取sku批次码
                        if (finalSkuBatchCodeMap.containsKey(bindingSkuDto.getDevelopSampleOrderNo())) {
                            sampleOrderPo.setSkuBatchCode(finalSkuBatchCodeMap.get(bindingSkuDto.getDevelopSampleOrderNo()).get(0).getBatchCode());
                            // 更新WMS的批次码单价
                            DevelopChildBatchCodeCostPriceBo developChildBatchCodeCostPriceBo = new DevelopChildBatchCodeCostPriceBo();
                            developChildBatchCodeCostPriceBo.setSkuBatchCode(sampleOrderPo.getSkuBatchCode());
                            developChildBatchCodeCostPriceBo.setPrice(developSampleOrderNoPriceMap.get(sampleOrderPo.getDevelopSampleOrderNo()));
                            developChildBatchCodeCostPriceBo.setSku(bindingSkuDto.getSku());
                            skuBatchCodePriceBoList.add(developChildBatchCodeCostPriceBo);
                        }

                        // 更新渠道大货价格
                        DevelopOrderPriceCreateBo developOrderPriceCreateBo = new DevelopOrderPriceCreateBo();
                        developOrderPriceCreateBo.setDevelopOrderNo(sampleOrderPo.getDevelopSampleOrderNo());
                        developOrderPriceCreateBo.setDevelopOrderPriceType(DevelopOrderPriceType.SAMPLE_PURCHASE_PRICE);
                        List<DevelopOrderPriceCreateBo.DevelopOrderPriceCreateItemBo> developOrderPriceCreateItemBoList = Optional.ofNullable(bindingSkuDto.getDevelopOrderPriceList())
                                .orElse(new ArrayList<>())
                                .stream()
                                .map(item -> {
                                    DevelopOrderPriceCreateBo.DevelopOrderPriceCreateItemBo developOrderPriceCreateItemBo = new DevelopOrderPriceCreateBo.DevelopOrderPriceCreateItemBo();
                                    developOrderPriceCreateItemBo.setChannelId(item.getChannelId());
                                    developOrderPriceCreateItemBo.setPrice(item.getPrice());
                                    developOrderPriceCreateItemBo.setIsDefaultPrice(item.getIsDefaultPrice());

                                    // 增加商品调价信息
                                    GoodsPriceAddBo goodsPriceAddBo = new GoodsPriceAddBo();
                                    goodsPriceAddBo.setSku(sampleOrderPo.getSku());
                                    goodsPriceAddBo.setSupplierCode(sampleOrderPo.getSupplierCode());
                                    goodsPriceAddBo.setChannelId(item.getChannelId());
                                    goodsPriceAddBo.setChannelPrice(item.getPrice());
                                    goodsPriceAddBoList.add(goodsPriceAddBo);


                                    return developOrderPriceCreateItemBo;
                                }).collect(Collectors.toList());
                        developOrderPriceCreateBo.setDevelopOrderPriceCreateItemBoList(developOrderPriceCreateItemBoList);
                        developOrderPriceCreateBoList.add(developOrderPriceCreateBo);

                    });
        }

        // 推送批次码价格给wms
        if (CollectionUtils.isNotEmpty(skuBatchCodePriceBoList)) {
            UpdateBatchCodePriceDto updateBatchCodePriceDto = new UpdateBatchCodePriceDto();
            updateBatchCodePriceDto.setBatchCodePriceList(developSampleOrderBaseService.calculatedSkuBatchSamplePrice(skuBatchCodePriceBoList));
            wmsMqBaseService.execSendUpdateBatchCodePriceMq(updateBatchCodePriceDto);
        }

        // 创建WMS的MQ入库
        developSampleOrderBaseService.createReceiveOrder(developSampleOrderPoList);

        // 批量更新样品单
        developSampleOrderDao.updateBatchByIdVersion(developSampleOrderPoList);

        //新增生产信息(常规的样品单)
        List<DevelopSampleOrderPo> developSampleOrderPoFilterProduceDataList = developSampleOrderPoList.stream()
                .filter(po -> DevelopSampleType.NORMAL.equals(po.getDevelopSampleType()))
                .filter(w -> StringUtils.isNotBlank(w.getSku()))
                .collect(Collectors.toList());
        List<String> developChildOrderNoList = developSampleOrderPoFilterProduceDataList.stream()
                .map(DevelopSampleOrderPo::getDevelopChildOrderNo)
                .distinct().collect(Collectors.toList());
        List<String> developPamphletOrderNoList = developSampleOrderPoFilterProduceDataList.stream()
                .map(DevelopSampleOrderPo::getDevelopPamphletOrderNo)
                .distinct().collect(Collectors.toList());
        Map<String, DevelopChildOrderPo> developChildOrderPoMap = developChildOrderDao.getMapByNoList(developChildOrderNoList);
        Map<String, DevelopPamphletOrderPo> developPamphletOrderPoMap = developPamphletOrderDao.getMapByNoList(developPamphletOrderNoList);
        if (CollectionUtils.isNotEmpty(developSampleOrderPoFilterProduceDataList)) {
            for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoFilterProduceDataList) {
                DevelopChildOrderPo developChildOrderPo = developChildOrderPoMap.get(developSampleOrderPo.getDevelopChildOrderNo());
                DevelopPamphletOrderPo developPamphletOrderPo = developPamphletOrderPoMap.get(developSampleOrderPo.getDevelopPamphletOrderNo());
                produceDataBaseService.addDevelopProduceData(developChildOrderPo, List.of(developSampleOrderPo), developPamphletOrderPo);
            }
        }

        // 更新生产资料的商品采购价格
        List<ProduceDataUpdatePurchasePriceBo> produceDataUpdatePurchasePriceBoList = developSampleOrderPoList.stream()
                .filter(po -> developSampleOrderNoPriceMap.containsKey(po.getDevelopSampleOrderNo()) && StringUtils.isNotBlank(po.getSku()))
                .map(po -> {
                    ProduceDataUpdatePurchasePriceBo produceDataUpdatePurchasePriceBo = new ProduceDataUpdatePurchasePriceBo();
                    produceDataUpdatePurchasePriceBo.setSku(po.getSku());
                    produceDataUpdatePurchasePriceBo.setGoodsPurchasePrice(developSampleOrderNoPriceMap.get(po.getDevelopSampleOrderNo()));
                    return produceDataUpdatePurchasePriceBo;
                }).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(produceDataUpdatePurchasePriceBoList)) {
            produceDataBaseService.updateGoodsPurchasePriceBySkuBatch(produceDataUpdatePurchasePriceBoList);
        }

        // 更新样品单渠道大货价格
        developChildBaseService.developOrderPriceBatchSave(developOrderPriceCreateBoList);

        // 更新商品调价的价格
        goodsPriceBaseService.addGoodsPrice(goodsPriceAddBoList);


    }

    /**
     * 闪售全新开款处理逻辑
     *
     * @param disbursementNewList:
     * @return void
     * @author ChenWenLong
     * @date 2024/3/25 15:34
     */
    private void disbursementNewUpdate(List<DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto> disbursementNewList) {
        List<String> developSampleOrderNoList = disbursementNewList.stream()
                .map(DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto::getDevelopSampleOrderNo)
                .distinct()
                .collect(Collectors.toList());
        List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByNoList(developSampleOrderNoList);
        if (CollectionUtils.isEmpty(developSampleOrderPoList)) {
            throw new ParamIllegalException("全新开款的样品单数据不能为空，请刷新页面后重试！");
        }

        // 更新样品单渠道大货价格的信息
        List<DevelopOrderPriceCreateBo> developOrderPriceCreateBoList = new ArrayList<>();

        // 更新商品调价的价格
        List<GoodsPriceAddBo> goodsPriceAddBoList = new ArrayList<>();

        for (DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto itemDto : disbursementNewList) {
            String developSampleOrderNo = itemDto.getDevelopSampleOrderNo();
            DevelopSampleOrderPo developSampleOrderPo = developSampleOrderPoList.stream()
                    .filter(po -> po.getDevelopSampleOrderNo().equals(developSampleOrderNo))
                    .filter(po -> po.getVersion().equals(itemDto.getVersion()))
                    .findFirst()
                    .orElse(null);
            if (null == developSampleOrderPo) {
                throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
            }


            developSampleOrderPo.setWarehouseCode(itemDto.getWarehouseCode());
            developSampleOrderPo.setWarehouseName(itemDto.getWarehouseName());
            developSampleOrderPo.setSkuBatchSamplePrice(itemDto.getSkuBatchSamplePrice());
            developSampleOrderPo.setDevelopSampleStatus(DevelopSampleStatus.WAIT_RECEIVE);
            developSampleOrderPo.setDevelopSampleDirection(itemDto.getDevelopSampleDirection());
            developSampleOrderPo.setHandleUser(GlobalContext.getUserKey());
            developSampleOrderPo.setHandleUsername(GlobalContext.getUsername());
            developSampleOrderPo.setHandleTime(LocalDateTime.now());

            // 更新渠道大货价格
            DevelopOrderPriceCreateBo developOrderPriceCreateBo = new DevelopOrderPriceCreateBo();
            developOrderPriceCreateBo.setDevelopOrderNo(developSampleOrderPo.getDevelopSampleOrderNo());
            developOrderPriceCreateBo.setDevelopOrderPriceType(DevelopOrderPriceType.SAMPLE_PURCHASE_PRICE);
            List<DevelopOrderPriceCreateBo.DevelopOrderPriceCreateItemBo> developOrderPriceCreateItemBoList = Optional.ofNullable(itemDto.getDevelopOrderPriceList())
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(item -> {
                        DevelopOrderPriceCreateBo.DevelopOrderPriceCreateItemBo developOrderPriceCreateItemBo = new DevelopOrderPriceCreateBo.DevelopOrderPriceCreateItemBo();
                        developOrderPriceCreateItemBo.setChannelId(item.getChannelId());
                        developOrderPriceCreateItemBo.setPrice(item.getPrice());
                        developOrderPriceCreateItemBo.setIsDefaultPrice(item.getIsDefaultPrice());

                        // 增加商品调价信息
                        if (StringUtils.isNotBlank(developSampleOrderPo.getSku())) {
                            GoodsPriceAddBo goodsPriceAddBo = new GoodsPriceAddBo();
                            goodsPriceAddBo.setSku(developSampleOrderPo.getSku());
                            goodsPriceAddBo.setSupplierCode(developSampleOrderPo.getSupplierCode());
                            goodsPriceAddBo.setChannelId(item.getChannelId());
                            goodsPriceAddBo.setChannelPrice(item.getPrice());
                            goodsPriceAddBoList.add(goodsPriceAddBo);
                        }

                        return developOrderPriceCreateItemBo;
                    }).collect(Collectors.toList());
            developOrderPriceCreateBo.setDevelopOrderPriceCreateItemBoList(developOrderPriceCreateItemBoList);
            developOrderPriceCreateBoList.add(developOrderPriceCreateBo);

        }
        // 更新样品单数据
        developSampleOrderDao.updateBatchByIdVersion(developSampleOrderPoList);

        // 闪售PLM创建sku
        developSampleOrderBaseService.submitHandlePushMq(developSampleOrderPoList, DevelopSampleMethod.SALE);

        // 更新样品单渠道大货价格
        developChildBaseService.developOrderPriceBatchSave(developOrderPriceCreateBoList);

        // 更新商品调价的价格
        goodsPriceBaseService.addGoodsPrice(goodsPriceAddBoList);

    }

    @Override
    public DevelopSampleMethod getHandlerType() {
        return DevelopSampleMethod.SALE;
    }

}
