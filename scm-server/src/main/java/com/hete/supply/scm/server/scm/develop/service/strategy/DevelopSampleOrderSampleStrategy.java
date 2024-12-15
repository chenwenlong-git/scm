package com.hete.supply.scm.server.scm.develop.service.strategy;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleStatus;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.adjust.entity.bo.GoodsPriceAddBo;
import com.hete.supply.scm.server.scm.adjust.service.base.GoodsPriceBaseService;
import com.hete.supply.scm.server.scm.develop.dao.DevelopChildOrderDao;
import com.hete.supply.scm.server.scm.develop.dao.DevelopPamphletOrderDao;
import com.hete.supply.scm.server.scm.develop.dao.DevelopSampleOrderDao;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopChildBatchCodeCostPriceBo;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopOrderPriceCreateBo;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopOrderPriceSaveDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleOrderSubmitHandleDto;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderPo;
import com.hete.supply.scm.server.scm.develop.enums.DevelopOrderPriceType;
import com.hete.supply.scm.server.scm.develop.enums.DevelopSampleDirection;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopChildBaseService;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopSampleOrderBaseService;
import com.hete.supply.scm.server.scm.entity.bo.ProduceDataUpdatePurchasePriceBo;
import com.hete.supply.scm.server.scm.entity.dto.ProduceDataPurchasePriceDto;
import com.hete.supply.scm.server.scm.handler.ProduceDataPurchasePriceHandler;
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
 * 封样入库样品单处理策略
 *
 * @author ChenWenLong
 * @date 2024/3/23 22:24
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DevelopSampleOrderSampleStrategy implements DevelopSampleOrderHandleStrategy {

    private final WmsRemoteService wmsRemoteService;
    private final DevelopSampleOrderDao developSampleOrderDao;
    private final DevelopSampleOrderBaseService developSampleOrderBaseService;
    private final ConsistencyService consistencyService;
    private final WmsMqBaseService wmsMqBaseService;
    private final ProduceDataBaseService produceDataBaseService;
    private final DevelopChildOrderDao developChildOrderDao;
    private final DevelopPamphletOrderDao developPamphletOrderDao;
    private final DevelopChildBaseService developChildBaseService;
    private final GoodsPriceBaseService goodsPriceBaseService;

    @Override
    public void submitHandleVerify(List<DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto> dtoList,
                                   List<DevelopSampleOrderPo> developSampleOrderPoList) {
        // 入库时验证入库仓和入库类型是否正确
        for (DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto itemDto : dtoList) {
            if (DevelopSampleDirection.WAREHOUSING.equals(itemDto.getDevelopSampleDirection())) {
                Assert.isTrue(StringUtils.isNotBlank(itemDto.getWarehouseCode()), () -> new BizException("收货仓库编码不能为空！"));
                Assert.isTrue(StringUtils.isNotBlank(itemDto.getWarehouseName()), () -> new BizException("收货仓库名称不能为空！"));
                //验证入库仓
                BooleanType warehouseVerify = wmsRemoteService.mateWarehouseCodeAndReceiveType(WmsEnum.ReceiveType.SAMPLE, itemDto.getWarehouseCode());
                Assert.isTrue(warehouseVerify.isBooleanVal(), () -> new ParamIllegalException(ScmConstant.VERIFY_WAREHOUSE_PROMPTS));
                // 货物走向:入库，验证必须存在sku
                Assert.isTrue(StringUtils.isNotBlank(itemDto.getSku()), () -> new BizException("样品单:{}入库时必须存在sku，入参的sku字段错误，请联系系统管理员！", itemDto.getDevelopSampleOrderNo()));
            }
        }
    }

    @Override
    public void developSampleOrderSubmitHandle(List<DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto> dtoList,
                                               List<DevelopSampleOrderPo> developSampleOrderPoList) {
        log.info("样品单确认处理-封样入库的DTO={},PO={}", JacksonUtil.parse2Str(dtoList), JacksonUtil.parse2Str(developSampleOrderPoList));
        if (CollectionUtils.isEmpty(developSampleOrderPoList)) {
            throw new ParamIllegalException("样品单数据不能为空，请刷新页面后重试！");
        }

        // 用于验证存储一个样品单只能添加一个默认渠道大货价格
        Map<String, BigDecimal> developSampleOrderNoPriceMap = new HashMap<>();

        //请求wms获取批次码
        SkuBatchCreate4defectNoListDto skuBatchCreate4defectNoListDto = new SkuBatchCreate4defectNoListDto();
        List<SkuBatchCreate4defectNoListDto.DefectHandlingDto> wmsSkuBatchDtoList = new ArrayList<>();
        for (DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto itemDto : dtoList) {
            DevelopSampleOrderPo developSampleOrderPo = developSampleOrderPoList.stream()
                    .filter(po -> po.getDevelopSampleOrderNo().equals(itemDto.getDevelopSampleOrderNo()))
                    .filter(po -> po.getVersion().equals(itemDto.getVersion()))
                    .findFirst()
                    .orElse(null);
            if (null == developSampleOrderPo) {
                throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
            }
            if (StringUtils.isNotBlank(itemDto.getSku()) && DevelopSampleDirection.WAREHOUSING.equals(itemDto.getDevelopSampleDirection())) {
                SkuBatchCreate4defectNoListDto.DefectHandlingDto defectHandlingDto = new SkuBatchCreate4defectNoListDto.DefectHandlingDto();
                defectHandlingDto.setDefectHandlingNo(itemDto.getDevelopSampleOrderNo());
                defectHandlingDto.setSkuCodeList(List.of(itemDto.getSku()));
                defectHandlingDto.setSupplierCode(developSampleOrderPo.getSupplierCode());
                defectHandlingDto.setSupplierName(developSampleOrderPo.getSupplierName());
                wmsSkuBatchDtoList.add(defectHandlingDto);
            }
            // 验证一个样品单只能设置一个渠道大货价格
            if (CollectionUtils.isNotEmpty(itemDto.getDevelopOrderPriceList()) && DevelopSampleDirection.WAREHOUSING.equals(itemDto.getDevelopSampleDirection())) {
                for (DevelopOrderPriceSaveDto developOrderPriceSaveDto : itemDto.getDevelopOrderPriceList()) {
                    if (BooleanType.TRUE.equals(developOrderPriceSaveDto.getIsDefaultPrice())) {
                        developSampleOrderNoPriceMap.put(developSampleOrderPo.getDevelopSampleOrderNo(), developOrderPriceSaveDto.getPrice());
                    }
                }
            }
        }

        // 获取批次码
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

        for (DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto itemDto : dtoList) {
            String developSampleOrderNo = itemDto.getDevelopSampleOrderNo();
            DevelopSampleOrderPo developSampleOrderPo = developSampleOrderPoList.stream()
                    .filter(po -> po.getDevelopSampleOrderNo().equals(developSampleOrderNo))
                    .filter(po -> po.getVersion().equals(itemDto.getVersion()))
                    .findFirst()
                    .orElse(null);
            if (null == developSampleOrderPo) {
                throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
            }

            // 判断状态是否正确
            developSampleOrderPo.getDevelopSampleStatus().submitHandleVerify();

            // 退样的数据处理
            if (DevelopSampleDirection.RETURN_SAMPLES.equals(itemDto.getDevelopSampleDirection())) {
                developSampleOrderPo.setDevelopSampleStatus(DevelopSampleStatus.RETURN_SAMPLES);
                developSampleOrderPo.setReturnTrackingNo(itemDto.getReturnTrackingNo());
            } else {
                developSampleOrderPo.setWarehouseCode(itemDto.getWarehouseCode());
                developSampleOrderPo.setWarehouseName(itemDto.getWarehouseName());
                developSampleOrderPo.setDevelopSampleStatus(DevelopSampleStatus.WAIT_RECEIVE);
            }


            if (!developSampleOrderNoPriceMap.containsKey(developSampleOrderPo.getDevelopSampleOrderNo())
                    && DevelopSampleDirection.WAREHOUSING.equals(itemDto.getDevelopSampleDirection())) {
                throw new BizException("样品单确认处理时数据错误，获取不到样品单号:{}的渠道大货价格，请联系管理员后重试！", developSampleOrderPo.getDevelopSampleOrderNo());
            }

            // 获取sku批次码
            if (skuBatchCodeMap.containsKey(itemDto.getDevelopSampleOrderNo())
                    && DevelopSampleDirection.WAREHOUSING.equals(itemDto.getDevelopSampleDirection())) {
                developSampleOrderPo.setSkuBatchCode(skuBatchCodeMap.get(itemDto.getDevelopSampleOrderNo()).get(0).getBatchCode());

                // 更新WMS的批次码单价
                DevelopChildBatchCodeCostPriceBo developChildBatchCodeCostPriceBo = new DevelopChildBatchCodeCostPriceBo();
                developChildBatchCodeCostPriceBo.setSkuBatchCode(developSampleOrderPo.getSkuBatchCode());
                developChildBatchCodeCostPriceBo.setPrice(developSampleOrderNoPriceMap.get(itemDto.getDevelopSampleOrderNo()));
                developChildBatchCodeCostPriceBo.setSku(itemDto.getSku());
                skuBatchCodePriceBoList.add(developChildBatchCodeCostPriceBo);
            }
            developSampleOrderPo.setSkuBatchSamplePrice(itemDto.getSkuBatchSamplePrice());
            developSampleOrderPo.setDevelopSampleDirection(itemDto.getDevelopSampleDirection());
            developSampleOrderPo.setHandleUser(GlobalContext.getUserKey());
            developSampleOrderPo.setHandleUsername(GlobalContext.getUsername());
            developSampleOrderPo.setHandleTime(LocalDateTime.now());

            // 更新渠道大货价格
            if (DevelopSampleDirection.WAREHOUSING.equals(itemDto.getDevelopSampleDirection())) {
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
                            GoodsPriceAddBo goodsPriceAddBo = new GoodsPriceAddBo();
                            goodsPriceAddBo.setSku(developSampleOrderPo.getSku());
                            goodsPriceAddBo.setSupplierCode(developSampleOrderPo.getSupplierCode());
                            goodsPriceAddBo.setChannelId(item.getChannelId());
                            goodsPriceAddBo.setChannelPrice(item.getPrice());
                            goodsPriceAddBoList.add(goodsPriceAddBo);

                            return developOrderPriceCreateItemBo;
                        }).collect(Collectors.toList());
                developOrderPriceCreateBo.setDevelopOrderPriceCreateItemBoList(developOrderPriceCreateItemBoList);
                developOrderPriceCreateBoList.add(developOrderPriceCreateBo);
            }

        }

        if (CollectionUtils.isNotEmpty(skuBatchCodePriceBoList)) {
            UpdateBatchCodePriceDto updateBatchCodePriceDto = new UpdateBatchCodePriceDto();
            updateBatchCodePriceDto.setBatchCodePriceList(developSampleOrderBaseService.calculatedSkuBatchSamplePrice(skuBatchCodePriceBoList));
            wmsMqBaseService.execSendUpdateBatchCodePriceMq(updateBatchCodePriceDto);
        }

        // 封样入库进行创建MQ入库
        List<DevelopSampleOrderPo> developSampleOrderPoWarehousingList = developSampleOrderPoList.stream()
                .filter(po -> DevelopSampleDirection.WAREHOUSING.equals(po.getDevelopSampleDirection()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(developSampleOrderPoWarehousingList)) {
            developSampleOrderBaseService.createReceiveOrder(developSampleOrderPoWarehousingList);
        }

        // 批量更新样品单
        developSampleOrderDao.updateBatchByIdVersion(developSampleOrderPoList);


        // 封样入库更新生产资料的商品采购价格
        List<ProduceDataUpdatePurchasePriceBo> produceDataUpdatePurchasePriceBoList = developSampleOrderPoList.stream()
                .filter(po -> developSampleOrderNoPriceMap.containsKey(po.getDevelopSampleOrderNo()) && StringUtils.isNotBlank(po.getSku()))
                .filter(po -> DevelopSampleDirection.WAREHOUSING.equals(po.getDevelopSampleDirection()))
                .map(po -> {
                    ProduceDataUpdatePurchasePriceBo produceDataUpdatePurchasePriceBo = new ProduceDataUpdatePurchasePriceBo();
                    produceDataUpdatePurchasePriceBo.setSku(po.getSku());
                    produceDataUpdatePurchasePriceBo.setGoodsPurchasePrice(developSampleOrderNoPriceMap.get(po.getDevelopSampleOrderNo()));
                    return produceDataUpdatePurchasePriceBo;
                }).collect(Collectors.toList());
        // 触发工作流异步任务
        if (CollectionUtils.isNotEmpty(produceDataUpdatePurchasePriceBoList)) {
            ProduceDataPurchasePriceDto produceDataPurchasePriceDto = new ProduceDataPurchasePriceDto();
            produceDataPurchasePriceDto.setProduceDataUpdatePurchasePriceBoList(produceDataUpdatePurchasePriceBoList);
            consistencyService.execAsyncTask(ProduceDataPurchasePriceHandler.class, produceDataPurchasePriceDto);
        }

        // 更新样品单渠道大货价格
        developChildBaseService.developOrderPriceBatchSave(developOrderPriceCreateBoList);

        // 更新商品调价的价格
        goodsPriceBaseService.addGoodsPrice(goodsPriceAddBoList);


    }

    @Override
    public DevelopSampleMethod getHandlerType() {
        return DevelopSampleMethod.SEAL_SAMPLE;
    }
}
