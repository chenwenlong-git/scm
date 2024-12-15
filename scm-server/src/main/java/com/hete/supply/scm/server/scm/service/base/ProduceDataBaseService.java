package com.hete.supply.scm.server.scm.service.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.hete.supply.plm.api.basedata.entity.vo.PlmAttributeVo;
import com.hete.supply.plm.api.developorder.enums.MaterialType;
import com.hete.supply.plm.api.goods.entity.vo.*;
import com.hete.supply.scm.api.scm.entity.dto.ProduceDataSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.SampleSkuListDto;
import com.hete.supply.scm.api.scm.entity.enums.BindingProduceData;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.supply.scm.api.scm.importation.entity.dto.ProduceDataItemRawImportationDto;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.common.util.StringUtil;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.converter.ProduceDataAttrConverter;
import com.hete.supply.scm.server.scm.converter.ProduceDataConverter;
import com.hete.supply.scm.server.scm.converter.ProduceDataSpecConverter;
import com.hete.supply.scm.server.scm.dao.*;
import com.hete.supply.scm.server.scm.develop.dao.*;
import com.hete.supply.scm.server.scm.develop.entity.po.*;
import com.hete.supply.scm.server.scm.entity.bo.*;
import com.hete.supply.scm.server.scm.entity.dto.*;
import com.hete.supply.scm.server.scm.entity.po.*;
import com.hete.supply.scm.server.scm.entity.vo.ProduceDataAttrValueBySkuVo;
import com.hete.supply.scm.server.scm.entity.vo.ProduceDataAttrValueVo;
import com.hete.supply.scm.server.scm.entity.vo.SkuBomListVo;
import com.hete.supply.scm.server.scm.entity.vo.SkuBomVo;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.handler.ProduceDataPriceByAttrHandler;
import com.hete.supply.scm.server.scm.nacosconfig.ProduceDataProp;
import com.hete.supply.scm.server.scm.process.dao.ProcessDao;
import com.hete.supply.scm.server.scm.process.dao.ProduceDataItemRawCompareDao;
import com.hete.supply.scm.server.scm.process.entity.bo.ProduceDataItemRawCompareBo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProduceDataItemRawComparePo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProduceDataSpecBatchVo;
import com.hete.supply.scm.server.scm.production.builder.ProdBuilder;
import com.hete.supply.scm.server.scm.production.converter.SkuProdConverter;
import com.hete.supply.scm.server.scm.production.entity.bo.CreateSkuSupSampInfo;
import com.hete.supply.scm.server.scm.production.service.ref.SkuProdRefService;
import com.hete.supply.scm.server.scm.production.service.ref.SupSkuSampleRefService;
import com.hete.supply.scm.server.scm.service.ref.SkuAttrPriceRefService;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.service.ref.SupplierProductCompareRefService;
import com.hete.supply.scm.util.HeteCollectionsUtil;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.consistency.core.service.ConsistencyService;
import com.hete.support.core.util.JacksonUtil;
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2023/8/10 16:46
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProduceDataBaseService {

    private final DevelopChildOrderDao developChildOrderDao;
    private final ProduceDataDao produceDataDao;
    private final ProduceDataItemDao produceDataItemDao;
    private final ProduceDataAttrDao produceDataAttrDao;
    private final ProduceDataItemProcessDao produceDataItemProcessDao;
    private final ProduceDataItemProcessDescDao produceDataItemProcessDescDao;
    private final ProduceDataItemRawDao produceDataItemRawDao;
    private final DevelopChildOrderAttrDao developChildOrderAttrDao;
    private final DevelopSampleOrderProcessDao developSampleOrderProcessDao;
    private final DevelopSampleOrderProcessDescDao developSampleOrderProcessDescDao;
    private final DevelopSampleOrderRawDao developSampleOrderRawDao;
    private final IdGenerateService idGenerateService;
    private final DevelopReviewSampleOrderDao developReviewSampleOrderDao;
    private final ScmImageBaseService scmImageBaseService;
    private final DevelopReviewSampleOrderInfoDao developReviewSampleOrderInfoDao;
    private final ProduceDataSpuDao produceDataSpuDao;
    private final PlmSkuDao plmSkuDao;
    private final PlmRemoteService plmRemoteService;
    private final ProduceDataSpecDao produceDataSpecDao;
    private final ProduceDataItemSupplierDao produceDataItemSupplierDao;
    private final SupplierDao supplierDao;
    private final ProduceDataSpecSupplierDao produceDataSpecSupplierDao;
    private final Environment environment;
    private final ProcessDao processDao;
    private final SkuAttrPriceRefService skuAttrPriceRefService;
    private final ProduceDataProp produceDataProp;
    private final ConsistencyService consistencyService;
    private final DevelopSampleOrderDao developSampleOrderDao;
    private final SupSkuSampleRefService supSkuSampleRefService;
    private final SupplierProductCompareRefService supplierProductCompareRefService;
    private final SkuProdRefService skuProdRefService;
    private final ProduceDataItemRawCompareDao prodRawCompareDao;


    /**
     * 开发子单创建数据
     *
     * @param developChildOrderPo:
     * @param developSampleOrderPoDtoList:
     * @param developPamphletOrderPo:注意：产前样的样品单的版单是空的
     * @return void
     * @author ChenWenLong
     * @date 2024/11/1 16:57
     */
    public void addDevelopProduceData(@NotNull DevelopChildOrderPo developChildOrderPo,
                                      List<DevelopSampleOrderPo> developSampleOrderPoDtoList,
                                      DevelopPamphletOrderPo developPamphletOrderPo) {
        log.info("开发子单创建生产资料数据入参developChildOrderPo=>{}", developChildOrderPo);
        log.info("开发子单创建生产资料数据入参developSampleOrderPoDtoList=>{}", developSampleOrderPoDtoList);
        log.info("开发子单创建生产资料数据入参developPamphletOrderPo=>{}", developPamphletOrderPo);
        List<DevelopSampleOrderPo> developSampleOrderPoList = new ArrayList<>(developSampleOrderPoDtoList);
        List<String> spuList;
        List<String> skuList;
        List<DevelopReviewSampleOrderInfoPo> developReviewSampleOrderInfoPoList = new ArrayList<>();
        List<DevelopSampleOrderProcessPo> developSampleOrderProcessPoList;
        List<DevelopSampleOrderProcessDescPo> developSampleOrderProcessDescPoList;
        List<DevelopSampleOrderRawPo> developSampleOrderRawPoList;
        List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoList = new ArrayList<>();
        List<DevelopChildOrderAttrPo> developChildOrderAttrPoList = new ArrayList<>();

        //无需打样处理
        if (this.isNoProofingRequired(developChildOrderPo, developPamphletOrderPo, developSampleOrderPoDtoList)) {
            spuList = List.of(developChildOrderPo.getSpu());
            skuList = List.of(developChildOrderPo.getSku());
            developSampleOrderProcessPoList = developSampleOrderProcessDao.getListByDevelopPamphletOrderNo(
                    developPamphletOrderPo.getDevelopPamphletOrderNo());
            developSampleOrderProcessDescPoList = developSampleOrderProcessDescDao.getListByDevelopPamphletOrderNo(
                    developPamphletOrderPo.getDevelopPamphletOrderNo());
            developSampleOrderRawPoList = developSampleOrderRawDao.getListByDevelopPamphletOrderNo(
                    developPamphletOrderPo.getDevelopPamphletOrderNo());
            developSampleOrderPoList = List.of(new DevelopSampleOrderPo());
            developChildOrderAttrPoList = developChildOrderAttrDao.getListByDevelopChildOrderNo(
                    developChildOrderPo.getDevelopChildOrderNo());
            //非封样入库处理
        } else if (this.isUnsealedSampleStorage(developChildOrderPo, developPamphletOrderPo, developSampleOrderPoDtoList)) {
            spuList = List.of(developChildOrderPo.getSpu());
            skuList = List.of(developChildOrderPo.getSku());
            developSampleOrderProcessPoList = new ArrayList<>();
            developSampleOrderProcessDescPoList = new ArrayList<>();
            developSampleOrderRawPoList = new ArrayList<>();
            developSampleOrderPoList = List.of(new DevelopSampleOrderPo());
            developChildOrderAttrPoList = developChildOrderAttrDao.getListByDevelopChildOrderNo(
                    developChildOrderPo.getDevelopChildOrderNo());
        } else {
            spuList = developSampleOrderPoList.stream()
                    .map(DevelopSampleOrderPo::getSpu)
                    .distinct()
                    .collect(Collectors.toList());
            skuList = developSampleOrderPoList.stream()
                    .map(DevelopSampleOrderPo::getSku)
                    .distinct()
                    .collect(Collectors.toList());

            List<String> developSampleOrderNoList = developSampleOrderPoList.stream()
                    .map(DevelopSampleOrderPo::getDevelopSampleOrderNo)
                    .collect(Collectors.toList());
            developReviewSampleOrderInfoPoList = developReviewSampleOrderInfoDao.getListByDevelopSampleOrderNoList(
                    developSampleOrderNoList);
            developSampleOrderProcessPoList = developSampleOrderProcessDao.getListByDevelopSampleOrderNoList(
                    developSampleOrderNoList);
            developSampleOrderProcessDescPoList = developSampleOrderProcessDescDao.getListByDevelopSampleOrderNoList(
                    developSampleOrderNoList);
            developSampleOrderRawPoList = developSampleOrderRawDao.getListByDevelopSampleOrderNoList(
                    developSampleOrderNoList);
            //获取审版单重量
            developReviewSampleOrderPoList = developReviewSampleOrderDao.getListByDevelopSampleOrderNoList(
                    developSampleOrderNoList);
        }
        List<ProduceDataItemPo> produceDataItemPoList = produceDataItemDao.getListBySkuList(skuList);
        Map<String, List<ProduceDataItemPo>> produceDataItemPoMap = produceDataItemPoList.stream()
                .collect(Collectors.groupingBy(ProduceDataItemPo::getSku));
        //获取已创建spu
        List<ProduceDataSpuPo> produceDataSpuPoList = produceDataSpuDao.getListBySpuList(spuList);
        Map<String, ProduceDataSpuPo> produceDataSpuPoMap = produceDataSpuPoList.stream()
                .collect(Collectors.toMap(ProduceDataSpuPo::getSpu, Function.identity(),
                        (existing, replacement) -> existing));

        //获取已创建sku
        List<ProduceDataPo> produceDataPoList = produceDataDao.getListBySkuList(skuList);
        Map<String, ProduceDataPo> produceDataPoMap = produceDataPoList.stream()
                .collect(Collectors.toMap(ProduceDataPo::getSku, Function.identity(),
                        (existing, replacement) -> existing));

        List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.getListBySkuList(skuList);

        //获取图片
        Map<Long, List<String>> effectMap = new HashMap<>();
        Map<Long, List<String>> detailMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(developReviewSampleOrderPoList)) {
            effectMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_REVIEW_SAMPLE_EFFECT,
                    developReviewSampleOrderPoList
                            .stream()
                            .map(DevelopReviewSampleOrderPo::getDevelopReviewSampleOrderId)
                            .collect(Collectors.toList()));
            detailMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_REVIEW_SAMPLE_DETAIL,
                    developReviewSampleOrderPoList
                            .stream()
                            .map(DevelopReviewSampleOrderPo::getDevelopReviewSampleOrderId)
                            .collect(Collectors.toList()));
        }

        // 新增信息
        List<ProduceDataPo> insertProduceDataPoList = new ArrayList<>();
        List<ProduceDataPo> updateProduceDataPoList = new ArrayList<>();
        List<ProduceDataItemPo> insertProduceDataItemPoList = new ArrayList<>();
        List<ProduceDataAttrPo> insertProduceDataAttrPoList = new ArrayList<>();
        List<ProduceDataItemProcessPo> insertProduceDataItemProcessList = new ArrayList<>();
        List<ProduceDataItemProcessDescPo> insertProduceDataItemProcessDescList = new ArrayList<>();
        List<ProduceDataItemRawPo> insertProduceDataItemRawList = new ArrayList<>();
        List<ProduceDataSpuPo> insertProduceDataSpuPoList = new ArrayList<>();
        List<ProduceDataItemSupplierPo> insertProduceDataItemSupplierPoList = new ArrayList<>();

        for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoList) {
            String itemNo;
            String spu;
            String sku;
            String supplierCode;
            String supplierName;
            // 创建BOM信息
            ProduceDataItemPo insertProduceDataItemPo = new ProduceDataItemPo();
            //无需打样处理 //非封样入库处理
            if (this.isNoProofingRequired(developChildOrderPo, developPamphletOrderPo, developSampleOrderPoDtoList)) {
                itemNo = developPamphletOrderPo.getDevelopPamphletOrderNo();
                spu = developChildOrderPo.getSpu();
                sku = developChildOrderPo.getSku();
                Optional.ofNullable(developChildOrderAttrPoList)
                        .orElse(Collections.emptyList())
                        .forEach(developChildOrderAttrPo -> {
                            ProduceDataAttrPo produceDataAttrPo = new ProduceDataAttrPo();
                            produceDataAttrPo.setSku(sku);
                            produceDataAttrPo.setSpu(spu);
                            produceDataAttrPo.setAttributeNameId(developChildOrderAttrPo.getAttributeNameId());
                            produceDataAttrPo.setAttrName(developChildOrderAttrPo.getAttrName());
                            produceDataAttrPo.setAttrValue(developChildOrderAttrPo.getAttrValue());
                            insertProduceDataAttrPoList.add(produceDataAttrPo);
                        });
                supplierCode = developPamphletOrderPo.getSupplierCode();
                supplierName = developPamphletOrderPo.getSupplierName();
                insertProduceDataItemPo.setBusinessNo(developChildOrderPo.getDevelopChildOrderNo());
                //非封样入库处理
            } else if (this.isUnsealedSampleStorage(developChildOrderPo, developPamphletOrderPo, developSampleOrderPoDtoList)) {
                itemNo = developPamphletOrderPo.getDevelopPamphletOrderNo();
                spu = developChildOrderPo.getSpu();
                sku = developChildOrderPo.getSku();
                Optional.ofNullable(developChildOrderAttrPoList)
                        .orElse(Collections.emptyList())
                        .forEach(developChildOrderAttrPo -> {
                            ProduceDataAttrPo produceDataAttrPo = new ProduceDataAttrPo();
                            produceDataAttrPo.setSku(sku);
                            produceDataAttrPo.setSpu(spu);
                            produceDataAttrPo.setAttributeNameId(developChildOrderAttrPo.getAttributeNameId());
                            produceDataAttrPo.setAttrName(developChildOrderAttrPo.getAttrName());
                            produceDataAttrPo.setAttrValue(developChildOrderAttrPo.getAttrValue());
                            insertProduceDataAttrPoList.add(produceDataAttrPo);
                        });
                supplierCode = developPamphletOrderPo.getSupplierCode();
                supplierName = developPamphletOrderPo.getSupplierName();
                insertProduceDataItemPo.setBusinessNo(developChildOrderPo.getDevelopChildOrderNo());
            } else {
                itemNo = developSampleOrderPo.getDevelopSampleOrderNo();
                spu = developSampleOrderPo.getSpu();
                sku = developSampleOrderPo.getSku();
                //先删除相同的原sku生产属性
                insertProduceDataAttrPoList.removeIf(w -> w.getSku()
                        .equals(sku));
                supplierCode = developSampleOrderPo.getSupplierCode();
                supplierName = developSampleOrderPo.getSupplierName();
                insertProduceDataItemPo.setBusinessNo(itemNo);
            }

            DevelopReviewSampleOrderPo developReviewSampleOrderPo = developReviewSampleOrderPoList.stream()
                    .filter(w -> w.getDevelopSampleOrderNo()
                            .equals(itemNo))
                    .findFirst()
                    .orElse(null);

            List<DevelopReviewSampleOrderInfoPo> infoPoList = developReviewSampleOrderInfoPoList.stream()
                    .filter(w -> w.getDevelopSampleOrderNo()
                            .equals(itemNo))
                    .collect(Collectors.toList());

            List<DevelopSampleOrderProcessDescPo> processDescPoList = developSampleOrderProcessDescPoList.stream()
                    .filter(w -> w.getDevelopSampleOrderNo()
                            .equals(itemNo) || w.getDevelopPamphletOrderNo()
                            .equals(itemNo))
                    .collect(Collectors.toList());

            List<DevelopSampleOrderRawPo> processRawPoList = developSampleOrderRawPoList.stream()
                    .filter(w -> w.getDevelopSampleOrderNo()
                            .equals(itemNo) || w.getDevelopPamphletOrderNo()
                            .equals(itemNo))
                    .collect(Collectors.toList());

            List<DevelopSampleOrderProcessPo> processPoList = developSampleOrderProcessPoList.stream()
                    .filter(w -> w.getDevelopSampleOrderNo()
                            .equals(itemNo) || w.getDevelopPamphletOrderNo()
                            .equals(itemNo))
                    .collect(Collectors.toList());
            BindingProduceData bindingProduceData = BindingProduceData.FALSE;
            if (produceDataPoMap.containsKey(sku)) {
                ProduceDataPo produceDataPo = produceDataPoMap.get(sku);
                if (developReviewSampleOrderPo != null) {
                    produceDataPo.setWeight(developReviewSampleOrderPo.getGramWeight());
                }
                produceDataPo.setCategoryId(developChildOrderPo.getCategoryId());
                if (produceDataPo.getWeight() != null
                        && produceDataPo.getWeight()
                        .compareTo(BigDecimal.ZERO) > 0
                        && CollectionUtils.isNotEmpty(processRawPoList)
                        && CollectionUtils.isNotEmpty(processPoList)) {
                    bindingProduceData = BindingProduceData.TRUE;
                }
                produceDataPo.setBindingProduceData(bindingProduceData);
                updateProduceDataPoList.add(produceDataPo);
            } else {
                ProduceDataPo insertProduceDataPo = new ProduceDataPo();
                Optional<ProduceDataPo> existProduceDataPo = insertProduceDataPoList.stream()
                        .filter(w -> w.getSku()
                                .equals(sku))
                        .findFirst();
                if (existProduceDataPo.isPresent()) {
                    ProduceDataPo existingProduceDataPo = existProduceDataPo.get();
                    insertProduceDataPoList.remove(existingProduceDataPo);
                }
                insertProduceDataPo.setSku(sku);
                insertProduceDataPo.setSpu(spu);
                insertProduceDataPo.setCategoryId(developChildOrderPo.getCategoryId());
                if (developReviewSampleOrderPo != null) {
                    insertProduceDataPo.setWeight(developReviewSampleOrderPo.getGramWeight());
                    if (developReviewSampleOrderPo.getGramWeight() != null &&
                            developReviewSampleOrderPo.getGramWeight()
                                    .compareTo(BigDecimal.ZERO) > 0 &&
                            CollectionUtils.isNotEmpty(processRawPoList) &&
                            CollectionUtils.isNotEmpty(processPoList)) {
                        bindingProduceData = BindingProduceData.TRUE;
                    }
                }
                insertProduceDataPo.setBindingProduceData(bindingProduceData);
                insertProduceDataPo.setRawManage(BooleanType.TRUE);
                insertProduceDataPoList.add(insertProduceDataPo);
            }

            List<ProduceDataItemPo> produceDataItemPos = produceDataItemPoMap.get(sku);
            Integer produceDataItemMaxSort = ScmConstant.PRODUCE_DATA_ITEM_MAX_SORT;
            if (CollectionUtils.isNotEmpty(produceDataItemPos)) {
                ProduceDataItemPo produceDataItemPo = produceDataItemPos.stream()
                        .max(Comparator.comparingInt(ProduceDataItemPo::getSort))
                        .orElse(null);
                if (produceDataItemPo != null) {
                    produceDataItemMaxSort = produceDataItemPo.getSort();
                }
            }


            long snowflakeId = idGenerateService.getSnowflakeId();
            insertProduceDataItemPo.setProduceDataItemId(snowflakeId);
            insertProduceDataItemPo.setSku(sku);
            insertProduceDataItemPo.setSpu(spu);
            insertProduceDataItemPo.setSort(produceDataItemMaxSort);

            // BOM的名称和关联供应商
            if (StringUtils.isNotBlank(supplierCode)) {
                insertProduceDataItemPo.setBomName(
                        supplierCode + ScmConstant.PRODUCE_DATA_ITEM_BOM_SUFFIX_NAME);
                ProduceDataItemSupplierPo produceDataItemSupplierPo = new ProduceDataItemSupplierPo();
                produceDataItemSupplierPo.setProduceDataItemId(insertProduceDataItemPo.getProduceDataItemId());
                produceDataItemSupplierPo.setSpu(spu);
                produceDataItemSupplierPo.setSku(sku);
                produceDataItemSupplierPo.setSupplierCode(supplierCode);
                produceDataItemSupplierPo.setSupplierName(supplierName);
                insertProduceDataItemSupplierPoList.add(produceDataItemSupplierPo);
            }
            insertProduceDataItemPoList.add(insertProduceDataItemPo);

            if (developReviewSampleOrderPo != null && effectMap.containsKey(
                    developReviewSampleOrderPo.getDevelopReviewSampleOrderId())) {
                // 增加效果图
                scmImageBaseService.insertBatchImage(
                        effectMap.get(developReviewSampleOrderPo.getDevelopReviewSampleOrderId()),
                        ImageBizType.PRODUCE_DATA_ITEM_EFFECT,
                        snowflakeId);
            }
            if (developReviewSampleOrderPo != null && detailMap.containsKey(
                    developReviewSampleOrderPo.getDevelopReviewSampleOrderId())) {
                // 增加细节图
                scmImageBaseService.insertBatchImage(
                        detailMap.get(developReviewSampleOrderPo.getDevelopReviewSampleOrderId()),
                        ImageBizType.PRODUCE_DATA_ITEM_DETAIL,
                        snowflakeId);
            }

            // 属性
            for (DevelopReviewSampleOrderInfoPo developReviewSampleOrderInfoPo : infoPoList) {
                ProduceDataAttrPo produceDataAttrPo = new ProduceDataAttrPo();
                produceDataAttrPo.setSku(sku);
                produceDataAttrPo.setSpu(spu);
                produceDataAttrPo.setAttributeNameId(developReviewSampleOrderInfoPo.getAttributeNameId());
                produceDataAttrPo.setAttrName(developReviewSampleOrderInfoPo.getSampleInfoKey());
                produceDataAttrPo.setAttrValue(developReviewSampleOrderInfoPo.getSampleInfoValue());
                insertProduceDataAttrPoList.add(produceDataAttrPo);
            }

            // 工序
            if (CollectionUtils.isNotEmpty(processPoList)) {
                for (DevelopSampleOrderProcessPo developSampleOrderProcessPo : processPoList) {
                    ProduceDataItemProcessPo produceDataItemProcessPo = new ProduceDataItemProcessPo();
                    produceDataItemProcessPo.setProduceDataItemId(insertProduceDataItemPo.getProduceDataItemId());
                    produceDataItemProcessPo.setSku(sku);
                    produceDataItemProcessPo.setSpu(spu);
                    produceDataItemProcessPo.setProcessFirst(developSampleOrderProcessPo.getProcessFirst());
                    produceDataItemProcessPo.setProcessCode(developSampleOrderProcessPo.getProcessCode());
                    produceDataItemProcessPo.setProcessName(developSampleOrderProcessPo.getProcessName());
                    produceDataItemProcessPo.setProcessSecondCode(developSampleOrderProcessPo.getProcessSecondCode());
                    produceDataItemProcessPo.setProcessSecondName(developSampleOrderProcessPo.getProcessSecondName());
                    produceDataItemProcessPo.setProcessLabel(developSampleOrderProcessPo.getProcessLabel());
                    insertProduceDataItemProcessList.add(produceDataItemProcessPo);
                }
            }

            // 工序描述
            if (CollectionUtils.isNotEmpty(processDescPoList)) {
                for (DevelopSampleOrderProcessDescPo developSampleOrderProcessDescPo : processDescPoList) {
                    ProduceDataItemProcessDescPo produceDataItemProcessDescPo = new ProduceDataItemProcessDescPo();
                    produceDataItemProcessDescPo.setProduceDataItemId(insertProduceDataItemPo.getProduceDataItemId());
                    produceDataItemProcessDescPo.setSku(sku);
                    produceDataItemProcessDescPo.setSpu(spu);
                    produceDataItemProcessDescPo.setName(developSampleOrderProcessDescPo.getName());
                    produceDataItemProcessDescPo.setDescValue(developSampleOrderProcessDescPo.getDescValue());
                    insertProduceDataItemProcessDescList.add(produceDataItemProcessDescPo);
                }
            }

            // 原料
            if (CollectionUtils.isNotEmpty(processRawPoList)) {
                for (DevelopSampleOrderRawPo developSampleOrderRawPo : processRawPoList) {
                    ProduceDataItemRawPo produceDataItemRawPo = new ProduceDataItemRawPo();
                    produceDataItemRawPo.setProduceDataItemId(insertProduceDataItemPo.getProduceDataItemId());
                    produceDataItemRawPo.setSpu(spu);
                    produceDataItemRawPo.setMaterialType(developSampleOrderRawPo.getMaterialType());
                    produceDataItemRawPo.setSku(developSampleOrderRawPo.getSku());
                    produceDataItemRawPo.setSkuCnt(developSampleOrderRawPo.getSkuCnt());
                    insertProduceDataItemRawList.add(produceDataItemRawPo);
                }
            }

            //spu主图
            Long produceDataSpuId;
            //删除旧主图
            List<Long> produceDataSpuIdDelList = new ArrayList<>();
            if (produceDataSpuPoMap.containsKey(spu)) {
                produceDataSpuId = produceDataSpuPoMap.get(spu).getProduceDataSpuId();
                produceDataSpuIdDelList.add(produceDataSpuId);
            } else {
                ProduceDataSpuPo insertSpuPo = new ProduceDataSpuPo();
                Optional<ProduceDataSpuPo> existProduceDataSpuPo = insertProduceDataSpuPoList.stream()
                        .filter(w -> w.getSpu().equals(spu))
                        .findFirst();
                if (existProduceDataSpuPo.isPresent()) {
                    ProduceDataSpuPo existingProduceDataSkuPo = existProduceDataSpuPo.get();
                    insertProduceDataSpuPoList.remove(existingProduceDataSkuPo);
                }

                produceDataSpuId = idGenerateService.getSnowflakeId();
                insertSpuPo.setProduceDataSpuId(produceDataSpuId);
                insertSpuPo.setSpu(spu);
                insertProduceDataSpuPoList.add(insertSpuPo);
            }

            //绑定时更新主图
            if (BindingProduceData.TRUE.equals(bindingProduceData)
                    && developReviewSampleOrderPo != null
                    && effectMap.containsKey(developReviewSampleOrderPo.getDevelopReviewSampleOrderId())) {

                if (CollectionUtils.isNotEmpty(produceDataSpuIdDelList)) {
                    scmImageBaseService.removeAllImageList(ImageBizType.PRODUCE_DATA_SPU, produceDataSpuIdDelList);
                }
                //增加新主图
                scmImageBaseService.insertBatchImage(
                        effectMap.get(developReviewSampleOrderPo.getDevelopReviewSampleOrderId()),
                        ImageBizType.PRODUCE_DATA_SPU, produceDataSpuId);
            }


        }


        if (CollectionUtils.isNotEmpty(produceDataAttrPoList)) {
            produceDataAttrDao.removeBatchByIds(produceDataAttrPoList.stream()
                    .map(ProduceDataAttrPo::getProduceDataAttrId)
                    .collect(Collectors.toList()));
        }

        if (CollectionUtils.isNotEmpty(updateProduceDataPoList)) {
            produceDataDao.updateBatchByIdVersion(updateProduceDataPoList);
        }

        if (CollectionUtils.isNotEmpty(insertProduceDataPoList)) {
            produceDataDao.insertBatch(insertProduceDataPoList);
        }

        // 同步绑定供应商对照关系
        if (CollectionUtils.isNotEmpty(insertProduceDataItemSupplierPoList)) {
            Map<String, List<ProduceDataItemSupplierPo>> produceDataItemSupplierPoMap = insertProduceDataItemSupplierPoList.stream()
                    .filter(produceDataItemSupplierPo -> StringUtils.isNotBlank(produceDataItemSupplierPo.getSupplierCode()))
                    .filter(produceDataItemSupplierPo -> StringUtils.isNotBlank(produceDataItemSupplierPo.getSku()))
                    .collect(Collectors.groupingBy(ProduceDataItemSupplierPo::getSupplierCode));
            produceDataItemSupplierPoMap.forEach((String supplierCode, List<ProduceDataItemSupplierPo> produceDataItemSupplierPos) -> {
                List<String> skuSupplierList = produceDataItemSupplierPos.stream()
                        .map(ProduceDataItemSupplierPo::getSku)
                        .distinct()
                        .collect(Collectors.toList());
                supplierProductCompareRefService.insertSupplierProductCompareAndInventory(supplierCode, skuSupplierList);
            });
        }

        produceDataItemDao.insertBatch(insertProduceDataItemPoList);
        produceDataAttrDao.insertBatch(insertProduceDataAttrPoList);
        produceDataItemProcessDao.insertBatch(insertProduceDataItemProcessList);
        produceDataItemProcessDescDao.insertBatch(insertProduceDataItemProcessDescList);
        produceDataItemRawDao.insertBatch(insertProduceDataItemRawList);
        produceDataSpuDao.insertBatch(insertProduceDataSpuPoList);
        produceDataItemSupplierDao.insertBatch(insertProduceDataItemSupplierPoList);

    }

    /**
     * 通过批量sku获取生产资料的bom信息
     *
     * @param dtoList:
     * @return List<ProduceDataDetailBo>
     * @author ChenWenLong
     * @date 2023/10/13 09:58
     */
    public List<ProduceDataDetailBo> getProduceDataBySkuList(List<ProduceDataBySkuListDto> dtoList) {
        log.info("根据sku获取生产资料工序与原料bom表。dto={}", JacksonUtil.parse2Str(dtoList));
        List<ProduceDataDetailBo> detailBoList = new ArrayList<>();
        List<String> skuList = dtoList.stream()
                .map(ProduceDataBySkuListDto::getSku)
                .distinct()
                .collect(Collectors.toList());

        List<PlmSkuPo> plmSkuPoList = plmSkuDao.getList(skuList);

        List<PlmSkuVo> plmSkuVoList = plmRemoteService.getSkuEncodeBySku(skuList);
        List<String> spuList = plmSkuVoList.stream()
                .map(PlmSkuVo::getSpuCode)
                .distinct()
                .collect(Collectors.toList());
        //生产信息
        List<ProduceDataPo> produceDataPoList = produceDataDao.getListBySkuList(skuList);
        //获取生产属性
        List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.getListBySkuList(skuList);

        //获取生产信息详情列表
        List<ProduceDataItemPo> produceDataItemPoList = produceDataItemDao.getListBySkuList(skuList);
        List<Long> produceDataItemIdList = produceDataItemPoList
                .stream()
                .map(ProduceDataItemPo::getProduceDataItemId)
                .distinct()
                .collect(Collectors.toList());

        //原料信息
        List<ProduceDataItemRawPo> produceDataItemRawPoList
                = produceDataItemRawDao.getByProduceDataItemIdList(produceDataItemIdList);
        //生产资料->bom->原料->原料对照关系
        List<Long> prodRawIdList = produceDataItemRawPoList.stream()
                .map(ProduceDataItemRawPo::getProduceDataItemRawId)
                .collect(Collectors.toList());
        List<ProduceDataItemRawComparePo> prodRawComparePoList = prodRawCompareDao.listByProdRawIdList(prodRawIdList);

        //获取工序
        List<ProduceDataItemProcessPo> produceDataItemProcessPoList = produceDataItemProcessDao.getListBySkuList(skuList);

        //获取工序描述
        List<ProduceDataItemProcessDescPo> produceDataItemProcessDescPoList = produceDataItemProcessDescDao.getBySkuList(skuList);

        // spu主图
        List<ProduceDataSpuPo> produceDataSpuPoList = produceDataSpuDao.getListBySpuList(spuList);
        List<Long> produceDataSpuIdList = produceDataSpuPoList.stream()
                .map(ProduceDataSpuPo::getProduceDataSpuId)
                .collect(Collectors.toList());
        Map<String, Long> spuMap = produceDataSpuPoList.stream()
                .collect(Collectors.toMap(ProduceDataSpuPo::getSpu, ProduceDataSpuPo::getProduceDataSpuId));
        Map<Long, List<String>> spuFileCodeListMap = scmImageBaseService.getFileCodeMapByIdAndType(
                ImageBizType.PRODUCE_DATA_SPU, produceDataSpuIdList);


        //获取生产资料效果/细节图片
        Map<Long, List<String>> produceDataItemEffectMap = new HashMap<>();
        Map<Long, List<String>> produceDataItemDetailMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(produceDataItemIdList)) {
            produceDataItemEffectMap = scmImageBaseService.getFileCodeMapByIdAndType(
                    ImageBizType.PRODUCE_DATA_ITEM_EFFECT, produceDataItemIdList);
            produceDataItemDetailMap = scmImageBaseService.getFileCodeMapByIdAndType(
                    ImageBizType.PRODUCE_DATA_ITEM_DETAIL, produceDataItemIdList);
        }
        for (PlmSkuPo plmSkuPo : plmSkuPoList) {
            String sku = plmSkuPo.getSku();
            ProduceDataDetailBo produceDataDetailBo = new ProduceDataDetailBo();
            produceDataDetailBo.setSku(sku);

            plmSkuVoList.stream()
                    .filter(po -> po.getSkuCode()
                            .equals(sku))
                    .findFirst()
                    .ifPresent(plmSkuVo -> produceDataDetailBo.setSpu(plmSkuVo.getSpuCode()));

            //spu主图
            Long produceDataSpuId = spuMap.get(produceDataDetailBo.getSpu());
            if (produceDataSpuId != null) {
                produceDataDetailBo.setSpuFileCodeList(spuFileCodeListMap.get(produceDataSpuId));
            }

            //sku重量绑定信息
            produceDataPoList.stream()
                    .filter(po -> po.getSku().equals(sku))
                    .findFirst()
                    .ifPresent(produceDataPo -> {
                        produceDataDetailBo.setBindingProduceData(produceDataPo.getBindingProduceData());
                        produceDataDetailBo.setWeight(produceDataPo.getWeight());
                        produceDataDetailBo.setRawManage(produceDataPo.getRawManage());
                    });

            //生产属性
            List<ProduceDataAttrPo> produceDataAttrPoSkuList = produceDataAttrPoList.stream()
                    .filter(po -> po.getSku().equals(sku))
                    .collect(Collectors.toList());
            produceDataDetailBo.setProduceDataAttrBoList(ProduceDataAttrConverter.INSTANCE.convertBo(produceDataAttrPoSkuList));

            //生产信息详情列表
            List<ProduceDataItemBo> produceDataItemBoList = new ArrayList<>();

            //生产信息详情
            List<ProduceDataItemPo> produceDataItemPoSkuList = produceDataItemPoList.stream()
                    .filter(po -> po.getSku().equals(sku))
                    .collect(Collectors.toList());

            //组装生产信息详情列表
            for (ProduceDataItemPo produceDataItemPo : produceDataItemPoSkuList) {
                Long produceDataItemId = produceDataItemPo.getProduceDataItemId();

                ProduceDataItemBo produceDataItemBo = new ProduceDataItemBo();
                produceDataItemBo.setProduceDataItemId(produceDataItemPo.getProduceDataItemId());
                produceDataItemBo.setVersion(produceDataItemPo.getVersion());
                produceDataItemBo.setBusinessNo(produceDataItemPo.getBusinessNo());
                produceDataItemBo.setSort(produceDataItemPo.getSort());
                produceDataItemBo.setCreateTime(produceDataItemPo.getCreateTime());
                produceDataItemBo.setEffectFileCodeList(produceDataItemEffectMap.get(produceDataItemPo.getProduceDataItemId()));
                produceDataItemBo.setDetailFileCodeList(produceDataItemDetailMap.get(produceDataItemPo.getProduceDataItemId()));

                //原料
                List<ProduceDataItemRawPo> produceDataItemRawPoIdList = produceDataItemRawPoList.stream()
                        .filter(po -> Objects.equals(produceDataItemId, po.getProduceDataItemId()))
                        .collect(Collectors.toList());
                List<ProduceDataItemRawListBo> produceDataItemRawBoList = new ArrayList<>();
                for (ProduceDataItemRawPo produceDataItemRawPo : produceDataItemRawPoIdList) {
                    ProduceDataItemRawListBo produceDataItemRawListBo = new ProduceDataItemRawListBo();
                    produceDataItemRawListBo.setSku(produceDataItemRawPo.getSku());
                    produceDataItemRawListBo.setMaterialType(produceDataItemRawPo.getMaterialType());
                    produceDataItemRawListBo.setSkuCnt(produceDataItemRawPo.getSkuCnt());

                    //匹配生产资料原料对照关系
                    Long prodRawId = produceDataItemRawPo.getProduceDataItemRawId();
                    List<ProduceDataItemRawComparePo> matchProdRawComparePoList = prodRawComparePoList.stream()
                            .filter(prodRawComparePo -> Objects.equals(prodRawId, prodRawComparePo.getProduceDataItemRawId()))
                            .collect(Collectors.toList());

                    //赋值生产资料原料对照关系
                    if (CollectionUtils.isNotEmpty(matchProdRawComparePoList)) {
                        List<String> compareSkuList = matchProdRawComparePoList.stream()
                                .map(ProduceDataItemRawComparePo::getSku)
                                .collect(Collectors.toList());
                        Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(compareSkuList);
                        List<ProduceDataItemRawCompareBo> prodRawCompareBoList
                                = SkuProdConverter.toProdRawCompareBoList(matchProdRawComparePoList, skuEncodeMap);
                        produceDataItemRawListBo.setProduceDataItemRawCompareBoList(prodRawCompareBoList);
                    }

                    produceDataItemRawBoList.add(produceDataItemRawListBo);
                }
                produceDataItemBo.setProduceDataItemRawBoList(produceDataItemRawBoList);

                //工序
                List<ProduceDataItemProcessPo> produceDataItemProcessPoIdList = produceDataItemProcessPoList.stream()
                        .filter(po -> po.getProduceDataItemId()
                                .equals(produceDataItemPo.getProduceDataItemId()))
                        .collect(Collectors.toList());
                List<ProduceDataItemProcessListBo> produceDataItemProcessBoList = new ArrayList<>();
                for (ProduceDataItemProcessPo produceDataItemProcessPo : produceDataItemProcessPoIdList) {
                    ProduceDataItemProcessListBo produceDataItemProcessListBo = new ProduceDataItemProcessListBo();
                    produceDataItemProcessListBo.setProcessCode(produceDataItemProcessPo.getProcessCode());
                    produceDataItemProcessListBo.setProcessName(produceDataItemProcessPo.getProcessName());
                    produceDataItemProcessListBo.setProcessSecondCode(produceDataItemProcessPo.getProcessSecondCode());
                    produceDataItemProcessListBo.setProcessSecondName(produceDataItemProcessPo.getProcessSecondName());
                    produceDataItemProcessListBo.setProcessFirst(produceDataItemProcessPo.getProcessFirst());
                    produceDataItemProcessListBo.setProcessLabel(produceDataItemProcessPo.getProcessLabel());
                    produceDataItemProcessBoList.add(produceDataItemProcessListBo);
                }
                produceDataItemBo.setProduceDataItemProcessBoList(produceDataItemProcessBoList);

                //工序描述
                List<ProduceDataItemProcessDescPo> produceDataItemProcessDescPoIdList
                        = produceDataItemProcessDescPoList.stream()
                        .filter(po -> po.getProduceDataItemId()
                                .equals(produceDataItemPo.getProduceDataItemId()))
                        .collect(Collectors.toList());
                List<ProduceDataItemProcessDescListBo> produceDataItemProcessDescBoList = new ArrayList<>();
                for (ProduceDataItemProcessDescPo produceDataItemProcessDescPo : produceDataItemProcessDescPoIdList) {
                    ProduceDataItemProcessDescListBo produceDataItemProcessDescListBo
                            = new ProduceDataItemProcessDescListBo();
                    produceDataItemProcessDescListBo.setName(produceDataItemProcessDescPo.getName());
                    produceDataItemProcessDescListBo.setDescValue(produceDataItemProcessDescPo.getDescValue());
                    produceDataItemProcessDescBoList.add(produceDataItemProcessDescListBo);
                }
                produceDataItemBo.setProduceDataItemProcessDescBoList(produceDataItemProcessDescBoList);

                produceDataItemBoList.add(produceDataItemBo);
            }

            produceDataDetailBo.setProduceDataItemBoList(produceDataItemBoList);

            detailBoList.add(produceDataDetailBo);
        }

        log.info("根据sku获取生产资料工序与原料bom表。返回值={}", JacksonUtil.parse2Str(detailBoList));
        return detailBoList;
    }

    /**
     * 通过SKU批量查询产品规格书信息
     *
     * @param skuList:
     * @return List<ProduceDataSpecBatchVo>
     * @author ChenWenLong
     * @date 2023/11/6 10:19
     */
    public List<ProduceDataSpecBatchVo> getBatchLoadProductFile(List<String> skuList) {
        final List<ProduceDataPo> produceDataPoList = produceDataDao.getListBySkuList(skuList);
        List<Long> produceDataIdList = produceDataPoList.stream()
                .map(ProduceDataPo::getProduceDataId)
                .collect(Collectors.toList());
        // 封样图
        Map<Long, List<String>> fileCodeMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.SEAL_IMAGE,
                produceDataIdList);

        //规格书信息列表
        final List<ProduceDataSpecPo> produceDataSpecPoList = produceDataSpecDao.getListBySkuList(skuList);
        Map<String, List<ProduceDataSpecPo>> produceDataSpecPoMap = produceDataSpecPoList.stream()
                .collect(Collectors.groupingBy(ProduceDataSpecPo::getSku));

        List<Long> produceDataSpecIdList = produceDataSpecPoList.stream()
                .map(ProduceDataSpecPo::getProduceDataSpecId)
                .collect(Collectors.toList());
        Map<Long, List<String>> productFileCodeMap = scmImageBaseService.getFileCodeMapByIdAndType(
                ImageBizType.PRODUCT_IMAGE, produceDataSpecIdList);

        // 查询关联供应商
        List<ProduceDataSpecSupplierPo> produceDataSpecSupplierPoList
                = produceDataSpecSupplierDao.getByProduceDataSpecIdList(produceDataSpecIdList);

        return ProduceDataSpecConverter.specPoToBatchVo(skuList, produceDataPoList,
                produceDataSpecPoMap, fileCodeMap, productFileCodeMap,
                produceDataSpecSupplierPoList);
    }

    /**
     * 增加封样图信息
     *
     * @param boList:
     * @return void
     * @author ChenWenLong
     * @date 2023/12/15 16:41
     */
    public void addProduceDataSkuSealImage(List<ProduceDataSkuSealImageBo> boList) {
        if (CollectionUtils.isEmpty(boList)) {
            return;
        }
        log.info("审版单的产前样增加封样图信息，入参={}", JacksonUtil.parse2Str(boList));
        List<String> skuList = boList.stream()
                .map(ProduceDataSkuSealImageBo::getSku)
                .distinct()
                .collect(Collectors.toList());

        // 查询plm获取spu
        final Map<String, String> spuMap = plmRemoteService.getSpuMapBySkuList(skuList);
        for (String sku : skuList) {
            if (!spuMap.containsKey(sku)) {
                throw new BizException("sku:{}通道plm获取不到对应spu", sku);
            }
        }

        Map<String, Long> categoriesIdMap = plmRemoteService.getCategoriesIdBySku(skuList);

        //生产信息
        List<ProduceDataPo> produceDataPoList = produceDataDao.getListBySkuList(skuList);
        List<ProduceDataPo> insertBatchPoList = new ArrayList<>();
        List<ScmImageBo> scmImageBoList = new ArrayList<>();
        List<CreateSkuSupSampInfo> skuSupSampInfoList = new ArrayList<>();
        for (ProduceDataSkuSealImageBo produceDataSkuSealImageBo : boList) {
            String sku = produceDataSkuSealImageBo.getSku();
            ProduceDataPo produceDataPo = produceDataPoList.stream()
                    .filter(po -> sku.equals(po.getSku()))
                    .findFirst()
                    .orElse(null);

            if (produceDataPo == null) {
                produceDataPo = new ProduceDataPo();
                if (spuMap.containsKey(sku)) {
                    produceDataPo.setSpu(spuMap.get(sku));
                }
                long snowflakeId = idGenerateService.getSnowflakeId();
                produceDataPo.setProduceDataId(snowflakeId);
                produceDataPo.setSku(sku);
                produceDataPo.setCategoryId(categoriesIdMap.get(sku));
                produceDataPo.setBindingProduceData(BindingProduceData.FALSE);
                produceDataPo.setRawManage(BooleanType.TRUE);
                insertBatchPoList.add(produceDataPo);
            }
            ScmImageBo scmImageBo = new ScmImageBo();
            scmImageBo.setImageBizId(produceDataPo.getProduceDataId());
            scmImageBo.setFileCodeList(produceDataSkuSealImageBo.getFileCodeList());
            scmImageBoList.add(scmImageBo);
            if (CollectionUtils.isNotEmpty(produceDataSkuSealImageBo.getFileCodeList())) {
                CreateSkuSupSampInfo createSkuSupSampInfo = new CreateSkuSupSampInfo();
                createSkuSupSampInfo.setSku(sku);
                createSkuSupSampInfo.setSupplierCode(produceDataSkuSealImageBo.getSupplierCode());
                createSkuSupSampInfo.setSourceOrderNo(produceDataSkuSealImageBo.getSourceOrderNo());
                createSkuSupSampInfo.setSamplePicFileCodeList(new HashSet<>(produceDataSkuSealImageBo.getFileCodeList()));
                skuSupSampInfoList.add(createSkuSupSampInfo);
            }
        }
        if (CollectionUtils.isNotEmpty(insertBatchPoList)) {
            produceDataDao.insertBatch(insertBatchPoList);
        }
        // todo 原生产资料创建的封样图，如果原生产资料关闭了，则这里建议不需要同步了
        scmImageBaseService.insertBatchImageBo(scmImageBoList, ImageBizType.SEAL_IMAGE);

        // 创建新商品质量的商品+供应商的封样图
        supSkuSampleRefService.createSkuSupplierSamplePic(skuSupSampInfoList);

    }

    /**
     * 列表检索条件
     *
     * @param dto:
     * @return ProduceDataSearchDto
     * @author ChenWenLong
     * @date 2024/1/2 10:51
     */
    public ProduceDataSearchDto getSearchProduceDataWhere(ProduceDataSearchDto dto) {
        if (null == skuProdRefService.getSearchPlmSkuWhere(dto)) {
            return null;
        }
        return dto;
    }

    /**
     * 导入生产信息原料sku
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/1/23 10:11
     */
    @Transactional(rollbackFor = Exception.class)
    public void importProduceDataItemRaw(ProduceDataItemRawImportationDto dto) {
        log.info("批量导入生产信息原料sku入参dto={}", dto);
        // sku的集合
        List<String> skuList = new ArrayList<>();

        // 插入原料的PO
        List<ProduceDataItemRawPo> insertProduceDataItemRawList = new ArrayList<>();

        // 校验入参
        if (StringUtils.isBlank(dto.getSku())) {
            throw new ParamIllegalException("SKU不能为空");
        }
        if (StringUtils.isBlank(dto.getRawSku1())) {
            throw new ParamIllegalException("原料SKU1不能为空");
        }
        if (StringUtils.isBlank(dto.getSkuCnt1())) {
            throw new ParamIllegalException("数量必须填写正整数数值，请调整后再导入");
        }
        if (!StringUtil.isNumeric(dto.getSkuCnt1())) {
            throw new ParamIllegalException("数量必须填写正整数数值，请调整后再导入");
        }
        if (StringUtils.isBlank(dto.getBomName())) {
            throw new ParamIllegalException("BOM名称不能为空");
        }
        if (dto.getBomName().length() > 15) {
            throw new ParamIllegalException("BOM名称长度不能超过15个字符");
        }
        String bomName = StringUtils.isNotBlank(dto.getBomName()) ? dto.getBomName().trim() : dto.getBomName();
        String dtoSku = StringUtils.isNotBlank(dto.getSku()) ? dto.getSku().trim() : dto.getSku();

        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(dtoSku);
        if (plmSkuPo == null) {
            throw new ParamIllegalException("导入失败，sku：{}不存在，请确认后再导入", dto.getSku());
        }

        String spu = plmSkuPo.getSpu();
        String sku = plmSkuPo.getSku();

        // 新增关联供应商信息
        List<ProduceDataItemSupplierPo> insertProduceDataItemSupplierPoList = new ArrayList<>();

        // 关联供应商验证是否存在
        if (StringUtils.isNotBlank(dto.getSupplierCodeList())) {
            // 按/拆分成数组
            String[] supplierCodeArray = dto.getSupplierCodeList()
                    .split("/");
            // 将数组转换为List，并使用Stream去重
            List<String> supplierCodeList = Arrays.stream(supplierCodeArray)
                    .distinct()
                    .collect(Collectors.toList());
            Map<String, String> supplierNameMap = supplierDao.getSupplierNameBySupplierCodeList(supplierCodeList);
            for (String supplierCode : supplierCodeList) {
                if (!supplierNameMap.containsKey(supplierCode)) {
                    throw new ParamIllegalException("供应商代码{}不存在，请调整后再导入", supplierCode);
                }
                ProduceDataItemSupplierPo produceDataItemSupplierPo = new ProduceDataItemSupplierPo();
                produceDataItemSupplierPo.setSupplierCode(supplierCode);
                produceDataItemSupplierPo.setSupplierName(supplierNameMap.get(supplierCode));
                insertProduceDataItemSupplierPoList.add(produceDataItemSupplierPo);
            }
        }

        skuList.add(sku);
        skuList.add(dto.getRawSku1());
        ProduceDataItemRawPo insertProduceDataItemRawPo = new ProduceDataItemRawPo();
        insertProduceDataItemRawPo.setMaterialType(MaterialType.SKU);
        insertProduceDataItemRawPo.setSku(dto.getRawSku1());
        insertProduceDataItemRawPo.setSkuCnt(Integer.valueOf(dto.getSkuCnt1()));
        insertProduceDataItemRawList.add(insertProduceDataItemRawPo);

        if (StringUtils.isNotBlank(dto.getRawSku2())
                && (StringUtils.isBlank(dto.getSkuCnt2()) || !StringUtil.isNumeric(dto.getSkuCnt2()))) {
            throw new ParamIllegalException("数量必须填写正整数数值，请调整后再导入");
        }
        if (StringUtils.isNotBlank(dto.getRawSku3())
                && (StringUtils.isBlank(dto.getSkuCnt3()) || !StringUtil.isNumeric(dto.getSkuCnt3()))) {
            throw new ParamIllegalException("数量必须填写正整数数值，请调整后再导入");
        }
        if (StringUtils.isNotBlank(dto.getRawSku4())
                && (StringUtils.isBlank(dto.getSkuCnt4()) || !StringUtil.isNumeric(dto.getSkuCnt4()))) {
            throw new ParamIllegalException("数量必须填写正整数数值，请调整后再导入");
        }
        if (StringUtils.isNotBlank(dto.getRawSku5())
                && (StringUtils.isBlank(dto.getSkuCnt5()) || !StringUtil.isNumeric(dto.getSkuCnt5()))) {
            throw new ParamIllegalException("数量必须填写正整数数值，请调整后再导入");
        }
        if (StringUtils.isNotBlank(dto.getRawSku6())
                && (StringUtils.isBlank(dto.getSkuCnt6()) || !StringUtil.isNumeric(dto.getSkuCnt6()))) {
            throw new ParamIllegalException("数量必须填写正整数数值，请调整后再导入");
        }
        if (StringUtils.isNotBlank(dto.getRawSku7())
                && (StringUtils.isBlank(dto.getSkuCnt7()) || !StringUtil.isNumeric(dto.getSkuCnt7()))) {
            throw new ParamIllegalException("数量必须填写正整数数值，请调整后再导入");
        }
        if (StringUtils.isNotBlank(dto.getRawSku2())) {
            ProduceDataItemRawPo produceDataItemRawPo2 = new ProduceDataItemRawPo();
            produceDataItemRawPo2.setMaterialType(MaterialType.SKU);
            produceDataItemRawPo2.setSku(dto.getRawSku2());
            produceDataItemRawPo2.setSkuCnt(Integer.valueOf(dto.getSkuCnt2()));
            insertProduceDataItemRawList.add(produceDataItemRawPo2);
            skuList.add(dto.getRawSku2());
        }
        if (StringUtils.isNotBlank(dto.getRawSku3())) {
            ProduceDataItemRawPo produceDataItemRawPo3 = new ProduceDataItemRawPo();
            produceDataItemRawPo3.setMaterialType(MaterialType.SKU);
            produceDataItemRawPo3.setSku(dto.getRawSku3());
            produceDataItemRawPo3.setSkuCnt(Integer.valueOf(dto.getSkuCnt3()));
            insertProduceDataItemRawList.add(produceDataItemRawPo3);
            skuList.add(dto.getRawSku3());
        }
        if (StringUtils.isNotBlank(dto.getRawSku4())) {
            ProduceDataItemRawPo produceDataItemRawPo4 = new ProduceDataItemRawPo();
            produceDataItemRawPo4.setMaterialType(MaterialType.SKU);
            produceDataItemRawPo4.setSku(dto.getRawSku4());
            produceDataItemRawPo4.setSkuCnt(Integer.valueOf(dto.getSkuCnt4()));
            insertProduceDataItemRawList.add(produceDataItemRawPo4);
            skuList.add(dto.getRawSku4());
        }
        if (StringUtils.isNotBlank(dto.getRawSku5())) {
            ProduceDataItemRawPo produceDataItemRawPo5 = new ProduceDataItemRawPo();
            produceDataItemRawPo5.setMaterialType(MaterialType.SKU);
            produceDataItemRawPo5.setSku(dto.getRawSku5());
            produceDataItemRawPo5.setSkuCnt(Integer.valueOf(dto.getSkuCnt5()));
            insertProduceDataItemRawList.add(produceDataItemRawPo5);
            skuList.add(dto.getRawSku5());
        }
        if (StringUtils.isNotBlank(dto.getRawSku6())) {
            ProduceDataItemRawPo produceDataItemRawPo6 = new ProduceDataItemRawPo();
            produceDataItemRawPo6.setMaterialType(MaterialType.SKU);
            produceDataItemRawPo6.setSku(dto.getRawSku6());
            produceDataItemRawPo6.setSkuCnt(Integer.valueOf(dto.getSkuCnt6()));
            insertProduceDataItemRawList.add(produceDataItemRawPo6);
            skuList.add(dto.getRawSku6());
        }
        if (StringUtils.isNotBlank(dto.getRawSku7())) {
            ProduceDataItemRawPo produceDataItemRawPo7 = new ProduceDataItemRawPo();
            produceDataItemRawPo7.setMaterialType(MaterialType.SKU);
            produceDataItemRawPo7.setSku(dto.getRawSku7());
            produceDataItemRawPo7.setSkuCnt(Integer.valueOf(dto.getSkuCnt7()));
            insertProduceDataItemRawList.add(produceDataItemRawPo7);
            skuList.add(dto.getRawSku7());
        }

        // 获取sku的分类信息
        final List<PlmGoodsDetailVo> skuCategoriesList = plmRemoteService.getCategoriesBySku(skuList);
        Map<String, List<PlmCategoryVo>> skuCategoriesMap = new HashMap<>();
        skuCategoriesList.forEach(skuCategories -> skuCategories.getSkuCodeList()
                .forEach(item ->
                        skuCategoriesMap.putIfAbsent(item, skuCategories.getCategoryList())));


        Map<String, Long> categoriesIdMap = plmRemoteService.getCategoriesIdBySku(skuList);

        if (!categoriesIdMap.containsKey(sku)) {
            throw new ParamIllegalException("导入失败，sku：{}查询不到分类信息，请确认后再导入", sku);
        }

        if (!skuCategoriesMap.containsKey(dto.getRawSku1())) {
            throw new ParamIllegalException("导入失败，sku：{}查询不到相关信息，请确认后再导入", dto.getRawSku1());
        }
        if (StringUtils.isNotBlank(dto.getRawSku2()) && !skuCategoriesMap.containsKey(dto.getRawSku2())) {
            throw new ParamIllegalException("导入失败，sku：{}查询不到相关信息，请确认后再导入", dto.getRawSku2());
        }
        if (StringUtils.isNotBlank(dto.getRawSku3()) && !skuCategoriesMap.containsKey(dto.getRawSku3())) {
            throw new ParamIllegalException("导入失败，sku：{}查询不到相关信息，请确认后再导入", dto.getRawSku3());
        }
        if (StringUtils.isNotBlank(dto.getRawSku4()) && !skuCategoriesMap.containsKey(dto.getRawSku4())) {
            throw new ParamIllegalException("导入失败，sku：{}查询不到相关信息，请确认后再导入", dto.getRawSku4());
        }
        if (StringUtils.isNotBlank(dto.getRawSku5()) && !skuCategoriesMap.containsKey(dto.getRawSku5())) {
            throw new ParamIllegalException("导入失败，sku：{}查询不到相关信息，请确认后再导入", dto.getRawSku5());
        }
        if (StringUtils.isNotBlank(dto.getRawSku6()) && !skuCategoriesMap.containsKey(dto.getRawSku6())) {
            throw new ParamIllegalException("导入失败，sku：{}查询不到相关信息，请确认后再导入", dto.getRawSku6());
        }
        if (StringUtils.isNotBlank(dto.getRawSku7()) && !skuCategoriesMap.containsKey(dto.getRawSku7())) {
            throw new ParamIllegalException("导入失败，sku：{}查询不到相关信息，请确认后再导入", dto.getRawSku7());
        }

        // 验证原料入参是否存在重复
        Set<String> skuRawSet = new HashSet<>();
        Optional.of(insertProduceDataItemRawList)
                .orElse(new ArrayList<>())
                .forEach(insertProduceDataItemRaw -> {
                    if (skuRawSet.contains(insertProduceDataItemRaw.getSku())) {
                        throw new ParamIllegalException("导入失败，禁止添加的重复的原料sku：{}，请确认后再导入", insertProduceDataItemRaw.getSku());
                    } else {
                        skuRawSet.add(insertProduceDataItemRaw.getSku());
                    }
                });

        //生产信息
        ProduceDataPo produceDataPo = produceDataDao.getBySku(sku);
        if (produceDataPo == null) {
            produceDataPo = new ProduceDataPo();
            produceDataPo.setBindingProduceData(BindingProduceData.FALSE);
            produceDataPo.setCategoryId(categoriesIdMap.get(sku));
            produceDataPo.setSpu(spu);
            produceDataPo.setRawManage(BooleanType.TRUE);
        }
        produceDataPo.setSku(sku);
        produceDataDao.insertOrUpdate(produceDataPo);


        // bom信息
        ProduceDataItemPo produceDataItemPo = produceDataItemDao.getOneBySku(sku);
        Integer sort = ScmConstant.PRODUCE_DATA_ITEM_MAX_SORT;
        if (produceDataItemPo != null) {
            sort = produceDataItemPo.getSort();
        }
        ProduceDataItemPo insertProduceDataItemPo = new ProduceDataItemPo();
        insertProduceDataItemPo.setSku(sku);
        insertProduceDataItemPo.setSpu(spu);
        insertProduceDataItemPo.setSort(sort);
        insertProduceDataItemPo.setBomName(bomName);
        produceDataItemDao.insert(insertProduceDataItemPo);

        // 原料sku
        for (ProduceDataItemRawPo produceDataItemRawPo : insertProduceDataItemRawList) {
            produceDataItemRawPo.setProduceDataItemId(insertProduceDataItemPo.getProduceDataItemId());
            skuCategoriesList.stream()
                    .filter(skuCategories -> skuCategories.getSkuCodeList()
                            .contains(produceDataItemRawPo.getSku()))
                    .findFirst()
                    .ifPresent(plmGoodsDetailVo -> produceDataItemRawPo.setSpu(plmGoodsDetailVo.getSpuCode()));
        }
        produceDataItemRawDao.insertBatch(insertProduceDataItemRawList);

        // 关联供应商
        if (CollectionUtils.isNotEmpty(insertProduceDataItemSupplierPoList)) {
            for (ProduceDataItemSupplierPo produceDataItemSupplierPo : insertProduceDataItemSupplierPoList) {
                produceDataItemSupplierPo.setProduceDataItemId(insertProduceDataItemPo.getProduceDataItemId());
                produceDataItemSupplierPo.setSpu(insertProduceDataItemPo.getSpu());
                produceDataItemSupplierPo.setSku(insertProduceDataItemPo.getSku());
            }
            produceDataItemSupplierDao.insertBatch(insertProduceDataItemSupplierPoList);
        }

        // 导入工序
        this.importProduceDataItemProcess(dto, insertProduceDataItemPo);


        // 同步绑定供应商对照关系
        if (CollectionUtils.isNotEmpty(insertProduceDataItemSupplierPoList)) {
            List<String> supplierCodes = insertProduceDataItemSupplierPoList.stream()
                    .map(ProduceDataItemSupplierPo::getSupplierCode)
                    .distinct()
                    .collect(Collectors.toList());
            supplierProductCompareRefService.insertSupplierProductCompareBySku(sku, supplierCodes);
        }

    }

    /**
     * 提供采购需求单创建、跟单确认获取BOM信息
     *
     * @param dto:
     * @return List<SkuBomListVo>
     * @author ChenWenLong
     * @date 2024/3/11 14:29
     */
    public List<SkuBomListVo> getBomListBySkuList(SampleSkuListDto dto) {
        final List<ProduceDataItemPo> produceDataItemPoList = produceDataItemDao.getListBySkuList(dto.getSkuList());
        if (CollectionUtils.isEmpty(produceDataItemPoList)) {
            return new ArrayList<>();
        }
        final Map<String, ProduceDataPo> produceDataPoMap = produceDataDao.getMapBySkuList(dto.getSkuList());

        List<Long> produceDataItemIdList = produceDataItemPoList.stream()
                .map(ProduceDataItemPo::getProduceDataItemId)
                .collect(Collectors.toList());
        final List<ProduceDataItemRawPo> produceDataItemRawPoList = produceDataItemRawDao.getByProduceDataItemIdList(
                        produceDataItemIdList)
                .stream()
                .filter(po -> StringUtils.isNotBlank(po.getSku()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(produceDataItemRawPoList)) {
            return new ArrayList<>();
        }
        final Map<Long, List<ProduceDataItemRawPo>> produceDataItemIdRawPoListMap = produceDataItemRawPoList.stream()
                .collect(Collectors.groupingBy(ProduceDataItemRawPo::getProduceDataItemId));

        // 获取原料sku的名称
        List<String> skuRawList = produceDataItemRawPoList.stream()
                .map(ProduceDataItemRawPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        Map<String, String> skuEncodeMapByRaw = plmRemoteService.getSkuEncodeMapBySku(skuRawList);

        List<String> skuList = produceDataItemPoList.stream()
                .map(ProduceDataItemPo::getSku)
                .distinct()
                .collect(Collectors.toList());

        // 获取关联供应商
        final List<ProduceDataItemSupplierPo> produceDataItemSupplierPoList
                = produceDataItemSupplierDao.getByProduceDataItemIdList(produceDataItemIdList);
        final Map<Long, List<ProduceDataItemSupplierPo>> produceDataItemSupplierPoMap
                = produceDataItemSupplierPoList.stream()
                .collect(Collectors.groupingBy(ProduceDataItemSupplierPo::getProduceDataItemId));

        return skuList.stream()
                .map(sku -> {
                    SkuBomListVo skuBomListVo = new SkuBomListVo();
                    skuBomListVo.setSku(sku);
                    ProduceDataPo produceDataPo = produceDataPoMap.get(sku);
                    skuBomListVo.setRawManage(BooleanType.TRUE);
                    if (null != produceDataPo) {
                        skuBomListVo.setRawManage(produceDataPo.getRawManage());
                    }
                    List<SkuBomVo> bomList = new ArrayList<>();
                    List<ProduceDataItemPo> produceDataItemPoBySkuList = produceDataItemPoList.stream()
                            .filter(produceDataItemPo -> sku.equals(produceDataItemPo.getSku()))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(produceDataItemPoBySkuList)) {
                        return null;
                    }
                    for (ProduceDataItemPo produceDataItemPo : produceDataItemPoBySkuList) {
                        // 获取原料列表
                        List<ProduceDataItemRawPo> produceDataItemRawPos = produceDataItemIdRawPoListMap.get(
                                produceDataItemPo.getProduceDataItemId());
                        // 存在原料的才返回BOM
                        if (CollectionUtils.isNotEmpty(produceDataItemRawPos)) {
                            // 返回VO
                            SkuBomVo skuBomVo = new SkuBomVo();
                            skuBomVo.setProduceDataItemId(produceDataItemPo.getProduceDataItemId());
                            skuBomVo.setBomName(produceDataItemPo.getBomName());

                            // 原料信息返回
                            List<SkuBomVo.SkuBomRawVo> skuBomRawList = new ArrayList<>();
                            for (ProduceDataItemRawPo produceDataItemRawPo : produceDataItemRawPos) {
                                SkuBomVo.SkuBomRawVo skuBomRawVo = new SkuBomVo.SkuBomRawVo();
                                skuBomRawVo.setSku(produceDataItemRawPo.getSku());
                                skuBomRawVo.setSkuEncode(skuEncodeMapByRaw.get(produceDataItemRawPo.getSku()));
                                skuBomRawVo.setSkuCnt(produceDataItemRawPo.getSkuCnt());
                                skuBomRawList.add(skuBomRawVo);
                            }
                            skuBomVo.setSkuBomRawList(skuBomRawList);

                            // 获取关联供应商
                            List<SkuBomVo.SkuBomSupplierVo> skuBomSupplierList = Optional.ofNullable(
                                            produceDataItemSupplierPoMap.get(produceDataItemPo.getProduceDataItemId()))
                                    .orElse(new ArrayList<>())
                                    .stream()
                                    .map(produceDataItemSupplierPo -> {
                                        SkuBomVo.SkuBomSupplierVo skuBomSupplierVo = new SkuBomVo.SkuBomSupplierVo();
                                        skuBomSupplierVo.setSupplierCode(produceDataItemSupplierPo.getSupplierCode());
                                        skuBomSupplierVo.setSupplierName(produceDataItemSupplierPo.getSupplierName());
                                        return skuBomSupplierVo;
                                    })
                                    .collect(Collectors.toList());
                            skuBomVo.setSkuBomSupplierList(skuBomSupplierList);

                            bomList.add(skuBomVo);

                        }

                    }
                    skuBomListVo.setBomList(bomList);
                    return skuBomListVo;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 采购通过sku更新生产资料的商品采购价格
     *
     * @param produceDataUpdatePurchasePriceBo:
     * @return void
     * @author ChenWenLong
     * @date 2024/2/29 16:11
     */
    @RedisLock(key = "#produceDataUpdatePurchasePriceBo.sku", prefix = ScmRedisConstant.SCM_PRODUCE_DATA_PURCHASE_PRICE)
    public void updateGoodsPurchasePriceBySku(ProduceDataUpdatePurchasePriceBo produceDataUpdatePurchasePriceBo) {
        log.info("更新生产资料的商品采购价格bo={}", JacksonUtil.parse2Str(produceDataUpdatePurchasePriceBo));
        if (null == produceDataUpdatePurchasePriceBo) {
            return;
        }

        Map<String, String> spuMap = plmRemoteService.getSpuMapBySkuList(List.of(produceDataUpdatePurchasePriceBo.getSku()));
        Map<String, Long> categoriesIdMap = plmRemoteService.getCategoriesIdBySku(List.of(produceDataUpdatePurchasePriceBo.getSku()));
        //生产信息
        Map<String, ProduceDataPo> produceDataPoMap = produceDataDao.getMapBySkuList(List.of(produceDataUpdatePurchasePriceBo.getSku()));

        String sku = produceDataUpdatePurchasePriceBo.getSku();
        ProduceDataPo produceDataPo = produceDataPoMap.get(sku);
        if (produceDataPo == null) {
            ProduceDataPo insertProduceDataPo = new ProduceDataPo();
            insertProduceDataPo.setSpu(spuMap.get(sku));
            insertProduceDataPo.setSku(sku);
            insertProduceDataPo.setCategoryId(categoriesIdMap.get(sku));
            insertProduceDataPo.setBindingProduceData(BindingProduceData.FALSE);
            insertProduceDataPo.setGoodsPurchasePrice(produceDataUpdatePurchasePriceBo.getGoodsPurchasePrice());
            insertProduceDataPo.setGoodsPurchasePriceTime(LocalDateTime.now());
            insertProduceDataPo.setRawManage(BooleanType.TRUE);
            produceDataDao.insert(insertProduceDataPo);
        } else {
            ProduceDataPo updateProduceDataPo = new ProduceDataPo();
            updateProduceDataPo.setProduceDataId(produceDataPo.getProduceDataId());
            updateProduceDataPo.setGoodsPurchasePrice(produceDataUpdatePurchasePriceBo.getGoodsPurchasePrice());
            updateProduceDataPo.setGoodsPurchasePriceTime(LocalDateTime.now());
            produceDataDao.updateById(updateProduceDataPo);
        }

    }

    public Map<String, List<String>> getBomImageBySkuList(List<String> skuList) {
        final List<ProduceDataItemPo> produceDataItemPoList = produceDataItemDao.getListBySkuList(skuList);

        final Map<String, ProduceDataItemPo> skuItemPoMap = produceDataItemPoList.stream()
                .collect(Collectors.toMap(ProduceDataItemPo::getSku, itemPo -> itemPo,
                        (itemPo1, itemPo2) -> {
                            if (itemPo1.getSort() > itemPo2.getSort()) {
                                return itemPo1;
                            } else if (itemPo1.getSort() < itemPo2.getSort()) {
                                return itemPo2;
                            } else {
                                return itemPo1.getProduceDataItemId() > itemPo2.getProduceDataItemId() ? itemPo1 : itemPo2;
                            }
                        }));
        final List<Long> idList = skuItemPoMap.values().stream()
                .map(ProduceDataItemPo::getProduceDataItemId)
                .distinct()
                .collect(Collectors.toList());

        final Map<Long, List<String>> idFileCodeListMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.PRODUCE_DATA_ITEM_EFFECT, idList);

        return skuItemPoMap.entrySet().stream()
                .filter(entry -> idFileCodeListMap.containsKey(entry.getValue().getProduceDataItemId()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> idFileCodeListMap.get(entry.getValue().getProduceDataItemId())
                ));

    }

    /**
     * 通过蕾丝面积的值获取SKU列表
     *
     * @param dto:
     * @return ProduceDataAttrValueVo
     * @author ChenWenLong
     * @date 2024/3/20 15:09
     */
    public ProduceDataAttrValueVo getProduceDataAttrValue(ProduceDataAttrValueDto dto) {
        log.info("通过蕾丝面积的值获取SKU列表,dto={}", JacksonUtil.parse2Str(dto));
        ProduceDataAttrValueVo produceDataAttrValueVo = new ProduceDataAttrValueVo();

        List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.getProduceDataAttrValue(dto);
        if (CollectionUtils.isEmpty(produceDataAttrPoList)) {
            return produceDataAttrValueVo;
        }
        produceDataAttrValueVo.setAttributeNameId(dto.getAttributeNameId());
        produceDataAttrValueVo.setAttrName(produceDataAttrPoList.get(0)
                .getAttrName());
        produceDataAttrValueVo.setAttrValue(produceDataAttrPoList.get(0)
                .getAttrValue());
        List<String> skuList = produceDataAttrPoList.stream()
                .map(ProduceDataAttrPo::getSku)
                .distinct()
                .collect(Collectors.toList());

        produceDataAttrValueVo.setSkuList(skuList);
        return produceDataAttrValueVo;
    }


    /**
     * 通过SKU列表获取生产属性信息
     *
     * @param dto:
     * @return ProduceDataAttrValueBySkuVo
     * @author ChenWenLong
     * @date 2024/3/20 15:15
     */
    public ProduceDataAttrValueBySkuVo produceDataAttrValueBySku(ProduceDataAttrValueBySkuDto dto) {
        log.info("通过SKU列表获取生产属性信息,dto={}", JacksonUtil.parse2Str(dto));
        ProduceDataAttrValueBySkuVo produceDataAttrValueBySkuVo = new ProduceDataAttrValueBySkuVo();

        List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.produceDataAttrValueBySku(dto);
        if (CollectionUtils.isEmpty(produceDataAttrPoList)) {
            return produceDataAttrValueBySkuVo;
        }

        produceDataAttrValueBySkuVo.setSku(dto.getSku());
        List<ProduceDataAttrValueBySkuVo.ProduceDataAttrValueBySkuItemVo> produceDataAttrValueBySkuItemList
                = new ArrayList<>();
        for (ProduceDataAttrPo produceDataAttrPo : produceDataAttrPoList) {
            ProduceDataAttrValueBySkuVo.ProduceDataAttrValueBySkuItemVo produceDataAttrValueBySkuItemVo
                    = new ProduceDataAttrValueBySkuVo.ProduceDataAttrValueBySkuItemVo();
            produceDataAttrValueBySkuItemVo.setAttributeNameId(produceDataAttrPo.getAttributeNameId());
            produceDataAttrValueBySkuItemVo.setAttrName(produceDataAttrPo.getAttrName());
            produceDataAttrValueBySkuItemVo.setAttrValue(produceDataAttrPo.getAttrValue());
            produceDataAttrValueBySkuItemList.add(produceDataAttrValueBySkuItemVo);
        }

        produceDataAttrValueBySkuVo.setProduceDataAttrValueBySkuItemList(produceDataAttrValueBySkuItemList);
        return produceDataAttrValueBySkuVo;
    }

    public Long getLaceAreaAttributeNameId() {
        String laceAreaAttributeNameIdStr = environment.getProperty("produceDataAttr.laceAreaAttributeNameId");
        if (StrUtil.isBlank(laceAreaAttributeNameIdStr)) {
            throw new ParamIllegalException("无法获取蕾丝面积属性分类id，请配置！");
        }
        return Long.valueOf(laceAreaAttributeNameIdStr);
    }

    public Long getLengthAttributeNameId() {
        String lengthAttributeNameIdStr = environment.getProperty("produceDataAttr.lengthAttributeNameId");
        if (StrUtil.isBlank(lengthAttributeNameIdStr)) {
            throw new ParamIllegalException("无法获取长度属性分类id，请配置！");
        }
        return Long.valueOf(lengthAttributeNameIdStr);
    }

    public Long getWigAttributeNameId() {
        String wigAttributeNameIdStr = environment.getProperty("produceDataAttr.wigAttributeNameId");
        if (StrUtil.isBlank(wigAttributeNameIdStr)) {
            throw new ParamIllegalException("无法获取分类id，请配置！");
        }
        return Long.valueOf(wigAttributeNameIdStr);
    }

    public Set<String> listSkuByContainAttrValues(Long attributeNameId,
                                                  List<String> attrValues) {
        return produceDataAttrDao.getSkusByContainAttrValues(attributeNameId, attrValues);
    }

    public Map<String, List<String>> getAttributesForSkus(Set<String> skuList,
                                                          Long attributeNameId) {
        Map<String, List<String>> skuAttributesMap = new HashMap<>(16);
        if (CollectionUtils.isEmpty(skuList)) {
            return skuAttributesMap;
        }

        List<ProduceDataAttrPo> produceDataAttrPos = produceDataAttrDao.listBySkus(skuList, attributeNameId);
        if (CollectionUtils.isEmpty(produceDataAttrPos)) {
            return skuAttributesMap;
        }
        return produceDataAttrPos.stream()
                .collect(Collectors.groupingBy(ProduceDataAttrPo::getSku,
                        Collectors.mapping(ProduceDataAttrPo::getAttrValue,
                                Collectors.toList())));
    }

    public String getWigAttributeName() {
        String wigAttributeNameStr = environment.getProperty("produceDataAttr.wigAttributeName");
        if (StrUtil.isBlank(wigAttributeNameStr)) {
            throw new ParamIllegalException("无法获取分类名称，请配置！");
        }
        return wigAttributeNameStr;
    }

    /**
     * 通过SKU获取生产属性值相等的对应SKU
     *
     * @param skuList:
     * @return Map<String, List < String>>
     * @author ChenWenLong
     * @date 2024/4/19 09:11
     */
    public Map<String, List<String>> getAttrMatchSkuListMap(List<String> skuList) {
        final List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.getListBySkuList(skuList);
        Map<String, List<String>> skuMap = new HashMap<>();
        if (CollectionUtils.isEmpty(produceDataAttrPoList)) {
            return skuMap;
        }
        // 蕾丝分类id
        Long laceAreaAttributeNameId = this.getLaceAreaAttributeNameId();
        // 长度
        Long lengthAttributeNameId = this.getLengthAttributeNameId();
        for (String sku : skuList) {

            Map<Long, List<ProduceDataAttrPo>> produceDataAttrPoMap = produceDataAttrPoList.stream()
                    .filter(produceDataAttrPo -> sku.equals(produceDataAttrPo.getSku()))
                    .filter(produceDataAttrPo -> laceAreaAttributeNameId.equals(produceDataAttrPo.getAttributeNameId())
                            || lengthAttributeNameId.equals(produceDataAttrPo.getAttributeNameId()))
                    .filter(produceDataAttrPo -> StringUtils.isNotBlank(produceDataAttrPo.getAttrValue()))
                    .collect(Collectors.groupingBy(ProduceDataAttrPo::getAttributeNameId));

            List<ProduceDataAttrPo> produceDataAttrLacePos = produceDataAttrPoMap.get(laceAreaAttributeNameId);
            if (CollectionUtils.isEmpty(produceDataAttrLacePos)) {
                log.info("sku:{}存在蕾丝面积生产属性必须有值情况，Map={}", sku, JacksonUtil.parse2Str(produceDataAttrPoMap));
                continue;
            }
            List<String> laceAreas = produceDataAttrLacePos.stream()
                    .map(ProduceDataAttrPo::getAttrValue)
                    .collect(Collectors.toList());

            List<ProduceDataAttrPo> produceDataAttrLengthsPos = produceDataAttrPoMap.get(lengthAttributeNameId);
            if (CollectionUtils.isEmpty(produceDataAttrLengthsPos)) {
                log.info("sku:{}存在裆长尺寸生产属性必须有值情况，Map={}", sku, JacksonUtil.parse2Str(produceDataAttrPoMap));
                continue;
            }
            List<String> lengths = produceDataAttrLengthsPos.stream()
                    .map(ProduceDataAttrPo::getAttrValue)
                    .collect(Collectors.toList());

            // 获取相同蕾丝面积长度的SKU列表
            List<String> sameLaceAreaAndLengthSkuCodes = this.getSkuListByLaceAreaAndLength(laceAreas,
                    lengths,
                    laceAreaAttributeNameId,
                    lengthAttributeNameId);
            if (CollectionUtils.isEmpty(sameLaceAreaAndLengthSkuCodes)) {
                continue;
            }
            sameLaceAreaAndLengthSkuCodes.remove(sku);
            if (CollectionUtils.isEmpty(sameLaceAreaAndLengthSkuCodes)) {
                continue;
            }
            skuMap.putIfAbsent(sku, sameLaceAreaAndLengthSkuCodes);
        }

        return skuMap;

    }

    /**
     * 根据蕾丝面积和长度获取SKU列表
     *
     * @param laceAreasAttributes 蕾丝面积列表
     * @param lengthAttributes    长度列表
     * @return 返回包含SKU的列表
     */
    public List<String> getSkuListByLaceAreaAndLength(List<String> laceAreasAttributes,
                                                      List<String> lengthAttributes,
                                                      Long laceAreaAttributeNameId,
                                                      Long lengthAttributeNameId) {

        // 获取生产资料同蕾丝面积属性的SKU列表
        Set<String> laceSkus
                = this.listSkuByContainAttrValues(laceAreaAttributeNameId, laceAreasAttributes);
        Set<String> lengthSkus
                = this.listSkuByContainAttrValues(lengthAttributeNameId, lengthAttributes);
        if (CollectionUtils.isEmpty(laceSkus) || CollectionUtils.isEmpty(lengthSkus)) {
            return Collections.emptyList();
        }

        Map<String, List<String>> laceAttributeMap
                = this.getAttributesForSkus(laceSkus, laceAreaAttributeNameId);
        Map<String, List<String>> lengthAttributeMap
                = this.getAttributesForSkus(lengthSkus, lengthAttributeNameId);

        log.info("计算加权平均价：参考SKU的蕾丝面积信息:{} ",
                JSON.toJSONString(laceAttributeMap));
        log.info("计算加权平均价：参考SKU的裆长尺寸信息:{} ",
                JSON.toJSONString(lengthAttributeMap));

        List<String> sameLaceSkus = HeteCollectionsUtil.filterIntersectingKeys(laceAreasAttributes, laceAttributeMap);
        List<String> sameLengthSkus = HeteCollectionsUtil.filterIntersectingKeys(lengthAttributes, lengthAttributeMap);
        if (CollectionUtils.isEmpty(sameLaceSkus) || CollectionUtils.isEmpty(sameLengthSkus)) {
            return Collections.emptyList();
        }

        return sameLaceSkus.stream()
                .filter(sameLengthSkus::contains)
                .collect(Collectors.toList());
    }

    /**
     * 导入工序
     *
     * @param dto:
     * @param produceDataItemPo:
     * @return void
     * @author ChenWenLong
     * @date 2024/6/27 13:48
     */
    private void importProduceDataItemProcess(ProduceDataItemRawImportationDto dto, ProduceDataItemPo produceDataItemPo) {
        // 新增工序信息
        List<ProduceDataItemProcessPo> insertProduceDataItemProcessPoList = new ArrayList<>();

        if (StringUtils.isNotBlank(dto.getProcess1())) {
            this.importAddProduceDataItemProcessPo(produceDataItemPo,
                    dto.getProcess1(),
                    dto.getProcessSecond1(),
                    insertProduceDataItemProcessPoList,
                    "1");
        } else {
            if (StringUtils.isNotBlank(dto.getProcessSecond1())) {
                throw new ParamIllegalException("二级工序名1存在情况下工序1为必填，请调整后再导入");
            }
        }

        if (StringUtils.isNotBlank(dto.getProcess2())) {
            this.importAddProduceDataItemProcessPo(produceDataItemPo,
                    dto.getProcess2(),
                    dto.getProcessSecond2(),
                    insertProduceDataItemProcessPoList,
                    "2");
        } else {
            if (StringUtils.isNotBlank(dto.getProcessSecond2())) {
                throw new ParamIllegalException("二级工序名2存在情况下工序2为必填，请调整后再导入");
            }
        }

        if (StringUtils.isNotBlank(dto.getProcess3())) {
            this.importAddProduceDataItemProcessPo(produceDataItemPo,
                    dto.getProcess3(),
                    dto.getProcessSecond3(),
                    insertProduceDataItemProcessPoList,
                    "3");
        } else {
            if (StringUtils.isNotBlank(dto.getProcessSecond3())) {
                throw new ParamIllegalException("二级工序名3存在情况下工序3为必填，请调整后再导入");
            }
        }

        if (StringUtils.isNotBlank(dto.getProcess4())) {
            this.importAddProduceDataItemProcessPo(produceDataItemPo,
                    dto.getProcess4(),
                    dto.getProcessSecond4(),
                    insertProduceDataItemProcessPoList,
                    "4");
        } else {
            if (StringUtils.isNotBlank(dto.getProcessSecond4())) {
                throw new ParamIllegalException("二级工序名4存在情况下工序4为必填，请调整后再导入");
            }
        }

        if (StringUtils.isNotBlank(dto.getProcess5())) {
            this.importAddProduceDataItemProcessPo(produceDataItemPo,
                    dto.getProcess5(),
                    dto.getProcessSecond5(),
                    insertProduceDataItemProcessPoList,
                    "5");
        } else {
            if (StringUtils.isNotBlank(dto.getProcessSecond5())) {
                throw new ParamIllegalException("二级工序名5存在情况下工序5为必填，请调整后再导入");
            }
        }

        produceDataItemProcessDao.insertBatch(insertProduceDataItemProcessPoList);

    }

    /**
     * 导入工序增加PO和验证
     *
     * @param produceDataItemPo:
     * @param process:
     * @param processSecond:
     * @param insertProduceDataItemProcessPoList:
     * @param num:
     * @return void
     * @author ChenWenLong
     * @date 2024/6/27 13:48
     */
    private void importAddProduceDataItemProcessPo(ProduceDataItemPo produceDataItemPo,
                                                   @NotBlank String process,
                                                   String processSecond,
                                                   List<ProduceDataItemProcessPo> insertProduceDataItemProcessPoList,
                                                   String num) {
        if (StringUtils.isBlank(processSecond)) {
            throw new ParamIllegalException("工序{}存在情况下二级工序名{}为必填，请调整后再导入", num, num);
        }
        process = process.trim();
        processSecond = processSecond.trim();
        final ProcessLabel processLabel = ProcessLabel.getByDesc(process);
        if (null == processLabel) {
            throw new ParamIllegalException("工序{}{}不存在！请确保您输入的工序有效值，请调整后再导入", num, process);
        }
        final ProcessPo processPo = processDao.getOneByProcessSecondNameAndLabel(processSecond, processLabel);
        if (null == processPo) {
            throw new ParamIllegalException("二级工序名{}{}不存在！请确保您输入的二级工序名有效值，请调整后再导入", num, processSecond);
        }
        insertProduceDataItemProcessPoList.add(ProduceDataConverter.importationToProcessPo(produceDataItemPo, processPo));
    }

    /**
     * 原料库存获取生产资料信息
     *
     * @param skuList:
     * @return ProduceDataGetRawInventoryBo
     * @author ChenWenLong
     * @date 2024/8/12 15:54
     */
    public ProduceDataGetRawInventoryBo getRawInventoryBomInfo(List<String> skuList) {
        // 查询生产资料信息
        Map<String, ProduceDataPo> produceDataPoMap = produceDataDao.getMapBySkuList(skuList);
        // 查询是否存在Bom信息
        List<ProduceDataItemPo> produceDataItemPoList = produceDataItemDao.getListBySkuList(skuList);
        // 查询BOM的供应商
        List<Long> produceDataItemIdList = produceDataItemPoList.stream()
                .map(ProduceDataItemPo::getProduceDataItemId)
                .collect(Collectors.toList());
        List<ProduceDataItemSupplierPo> produceDataItemSupplierPoList = produceDataItemSupplierDao.getByProduceDataItemIdList(produceDataItemIdList);
        // 获取BOM的原料
        List<ProduceDataItemRawPo> produceDataItemRawPoList = produceDataItemRawDao.getByProduceDataItemIdList(produceDataItemIdList);
        return ProduceDataGetRawInventoryBo.builder()
                .produceDataPoMap(produceDataPoMap)
                .produceDataItemPoList(produceDataItemPoList)
                .produceDataItemSupplierPoList(produceDataItemSupplierPoList)
                .produceDataItemRawPoList(produceDataItemRawPoList)
                .build();
    }

    /**
     * 更新SKU的生产属性，关联SPU已维护属性的SKU的属性
     *
     * @param skuList:
     * @author ChenWenLong
     * @date 2024/9/12 16:32
     */
    public void updateSkuProduceDataAttr(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            log.info("更新SKU生产属性结束！没有需要更新的sku。");
            return;
        }

        // 去掉重复
        skuList = skuList.stream().filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());

        // 查询plm的sku信息
        Map<String, String> spuMap = plmRemoteService.getSpuMapBySkuList(skuList);
        Map<String, Long> categoriesIdMap = plmRemoteService.getCategoriesIdBySku(skuList);

        // 获取所有值，并去重
        Set<String> uniqueSetSpu = new HashSet<>(spuMap.values());
        // 获取spu下全部sku
        List<PlmSkuVo> plmSkuVoList = plmRemoteService.getSkuBySpuCode(new ArrayList<>(uniqueSetSpu));
        Map<String, List<PlmSkuVo>> plmSkuVoSpuMap = plmSkuVoList.stream()
                .collect(Collectors.groupingBy(PlmSkuVo::getSpuCode));
        List<String> plmSkuList = plmSkuVoList.stream()
                .map(PlmSkuVo::getSkuCode)
                .distinct().collect(Collectors.toList());

        // 获取spu下全部sku信息查询创建时间最大的sku
        List<PlmSkuPo> plmSkuPoList = plmSkuDao.getList(plmSkuList);
        Map<String, PlmSkuPo> plmSkuPoMap = plmSkuPoList.stream()
                .collect(Collectors.toMap(PlmSkuPo::getSku, Function.identity()));

        // 需要排除的属性ID
        List<Long> filterAttributeNameIds = produceDataProp.getWhitelistNameIds();

        // 获取sku的生产属性
        List<String> combinedList = new ArrayList<>(skuList);
        combinedList.addAll(plmSkuList);
        Map<String, List<ProduceDataAttrPo>> produceDataAttrPoMap = produceDataAttrDao.getMapBySkuList(combinedList);

        //生产信息
        Map<String, ProduceDataPo> produceDataPoMap = produceDataDao.getMapBySkuList(skuList);
        List<ProduceDataPo> insertProduceDataPoList = new ArrayList<>();
        // 创建生产属性信息
        List<ProduceDataAttrPo> insertProduceDataAttrPoList = new ArrayList<>();
        // 删除空的生产属性信息
        List<Long> delProduceDataAttrPoIdList = new ArrayList<>();
        // 逻辑处理
        for (String sku : skuList) {
            // 判断是否已经存在生产属性
            List<ProduceDataAttrPo> produceDataAttrPos = produceDataAttrPoMap.get(sku);
            List<ProduceDataAttrPo> produceDataAttrPoFilterList = Optional.ofNullable(produceDataAttrPos)
                    .orElse(new ArrayList<>())
                    .stream()
                    .filter(produceDataAttrPo -> StringUtils.isNotBlank(produceDataAttrPo.getAttrValue()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(produceDataAttrPoFilterList)) {
                log.info("sku=>{}已存在生产属性=>{}，无需处理", sku, produceDataAttrPoFilterList);
                continue;
            }
            // 执行删除空值的生产属性
            Optional.ofNullable(produceDataAttrPos)
                    .orElse(new ArrayList<>())
                    .stream()
                    .filter(produceDataAttrPo -> StringUtils.isBlank(produceDataAttrPo.getAttrValue()))
                    .forEach(produceDataAttrPo -> delProduceDataAttrPoIdList.add(produceDataAttrPo.getProduceDataAttrId()));
            ProduceDataPo produceDataPo = produceDataPoMap.get(sku);
            if (produceDataPo == null) {
                produceDataPo = new ProduceDataPo();
                long snowflakeId = idGenerateService.getSnowflakeId();
                produceDataPo.setProduceDataId(snowflakeId);
                produceDataPo.setSpu(spuMap.get(sku));
                produceDataPo.setSku(sku);
                produceDataPo.setCategoryId(categoriesIdMap.get(sku));
                produceDataPo.setBindingProduceData(BindingProduceData.FALSE);
                produceDataPo.setRawManage(BooleanType.TRUE);
                insertProduceDataPoList.add(produceDataPo);
            }

            String spu = produceDataPo.getSpu();

            // 获取spu下的全部sku
            List<PlmSkuVo> plmSkuVos = plmSkuVoSpuMap.get(spu);
            if (CollectionUtils.isEmpty(plmSkuVos)) {
                log.info("sku=>{}对应的spu=>{}下没有sku，无法复制生产属性", sku, spu);
                continue;
            }
            // 排除掉当前循序sku
            plmSkuVos.removeIf(plmSkuVo -> plmSkuVo.getSkuCode().equals(sku));
            if (CollectionUtils.isEmpty(plmSkuVos)) {
                log.info("sku=>{}对应的spu=>{}下没有sku，无法复制生产属性", sku, spu);
                continue;
            }

            // 获取最大创建时间的sku以及属性
            ProduceDataAttrCopyBo produceDataAttrCopyBo = this.getProduceDataMaxCreateTimeSku(plmSkuVos,
                    produceDataAttrPoMap,
                    filterAttributeNameIds,
                    plmSkuPoMap);
            log.info("sku=>{}，spu=>{}，复制生产属性为：{}", sku, spu, produceDataAttrCopyBo);

            if (produceDataAttrCopyBo == null || CollectionUtils.isEmpty(produceDataAttrCopyBo.getProduceDataAttrPoList())) {
                continue;
            }
            log.info("成功:sku=>{}，spu=>{}，复制生产属性为：{}", sku, spu, produceDataAttrCopyBo);

            for (ProduceDataAttrPo produceDataAttrPo : produceDataAttrCopyBo.getProduceDataAttrPoList()) {
                ProduceDataAttrPo insertProduceDataAttrPo = new ProduceDataAttrPo();
                insertProduceDataAttrPo.setSku(sku);
                insertProduceDataAttrPo.setSpu(spu);
                insertProduceDataAttrPo.setAttrName(produceDataAttrPo.getAttrName());
                insertProduceDataAttrPo.setAttrValue(produceDataAttrPo.getAttrValue());
                insertProduceDataAttrPo.setAttributeNameId(produceDataAttrPo.getAttributeNameId());
                insertProduceDataAttrPoList.add(insertProduceDataAttrPo);
            }
        }

        if (CollectionUtils.isNotEmpty(delProduceDataAttrPoIdList)) {
            produceDataAttrDao.removeBatchByIds(delProduceDataAttrPoIdList);
        }

        log.info("成功最终:sku=>{}", insertProduceDataAttrPoList.stream()
                .map(ProduceDataAttrPo::getSku).distinct().collect(Collectors.toList()));
        produceDataDao.insertBatch(insertProduceDataPoList);
        produceDataAttrDao.insertBatch(insertProduceDataAttrPoList);

        //批量初始化SKU特定销售属性
        initSpecialProdDataAttrBatch(skuList);
    }

    /**
     * @Description 批量初始化SKU特殊销售属性
     * @author yanjiawei
     * @Date 2024/11/14 16:09
     */
    public void initSpecialProdDataAttrBatch(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            log.info("批量初始化SKU指定销售属性结束！skuList为空。");
            return;
        }
        for (String sku : skuList) {
            initProdDataAttr(sku);
        }
    }

    /**
     * @Description 初始化生产资料指定销售属性
     * @author yanjiawei
     * @Date 2024/11/14 16:09
     */
    public void initProdDataAttr(String sku) {
        if (StrUtil.isBlank(sku)) {
            log.info("初始化生产资料指定销售属性结束！sku为空。");
            return;
        }

        //初始化需更新的销售属性列表
        List<AttrVariantCompareBo> attrVariantCompareConfig
                = Optional.ofNullable(produceDataProp.getAttrVariantCompareList()).orElse(Collections.emptyList());
        List<UpdateProduceDataAttrBo> updateProduceDataAttrList = ProdBuilder.buildUpdateProduceDataAttrList(sku, attrVariantCompareConfig);

        //sku销售属性列表
        updateProduceDataAttrList = filterPlmAttr(sku, updateProduceDataAttrList);
        log.info("过滤指定配置的属性列表，sku=>{}，根据属性变体配置=>{} 过滤后更新属性列表=>{}", sku, JSON.toJSONString(attrVariantCompareConfig), JSON.toJSONString(updateProduceDataAttrList));

        //过滤已配置变体名与属性变体值列表
        updateProduceDataAttrList = filterPlmVariant(sku, updateProduceDataAttrList);
        log.info("过滤指定配置变体值列表，sku=>{}，根据属性变体配置=>{} 过滤后更新属性列表=>{}", sku, JSON.toJSONString(attrVariantCompareConfig), JSON.toJSONString(updateProduceDataAttrList));

        //保存销售属性
        Map<String, String> spuMapBySkuList = plmRemoteService.getSpuMapBySkuList(Collections.singletonList(sku));
        String spu = ParamValidUtils.requireNotBlank(spuMapBySkuList.get(sku), StrUtil.format("属性复制失败！spu为空！sku=>{}", sku));

        List<ProduceDataAttrPo> produceDataAttrPoList = ProdBuilder.buildProduceDataAttrPoList(sku, spu, updateProduceDataAttrList);
        if (CollectionUtils.isNotEmpty(produceDataAttrPoList)) {
            List<Long> attrNameIds = produceDataAttrPoList.stream()
                    .map(ProduceDataAttrPo::getAttributeNameId)
                    .collect(Collectors.toList());
            List<ProduceDataAttrPo> existProduceDataAttrPoList
                    = produceDataAttrDao.getListBySkuList(Collections.singletonList(sku), attrNameIds);
            if (CollectionUtils.isNotEmpty(existProduceDataAttrPoList)) {
                produceDataAttrDao.removeBatch(existProduceDataAttrPoList);
            }
            produceDataAttrDao.insertBatch(produceDataAttrPoList);
        }
    }

    private List<UpdateProduceDataAttrBo> filterPlmVariant(String sku,
                                                           List<UpdateProduceDataAttrBo> updateProduceDataAttrList) {
        if (CollectionUtils.isEmpty(updateProduceDataAttrList)) {
            log.info("过滤plm变体属性结束！销售属性更新列表为空。");
            return Collections.emptyList();
        }

        List<PlmVariantVo> skuVariantAttrList = plmRemoteService.getVariantAttr(List.of(sku));
        if (CollectionUtils.isEmpty(skuVariantAttrList)) {
            log.info("过滤plm变体属性结束！找不到sku=>{}变体属性列表", sku);
            return Collections.emptyList();
        }
        PlmVariantVo skuVariantVo = skuVariantAttrList.stream().findFirst().orElse(null);
        if (Objects.isNull(skuVariantVo)) {
            log.info("过滤plm变体属性结束！找不到sku=>{}变体属性列表", sku);
            return Collections.emptyList();
        }
        List<PlmAttrSkuVo> variantList = skuVariantVo.getVariantSkuList();
        if (CollectionUtils.isEmpty(variantList)) {
            log.info("过滤plm变体属性结束！找不到sku=>{}变体属性信息", sku);
            return Collections.emptyList();
        }

        updateProduceDataAttrList = updateProduceDataAttrList.stream().filter(updateBo ->
                        variantList.stream().anyMatch(variant ->
                                Objects.equals(variant.getAttrName(), updateBo.getVariantName()) && Objects.equals(variant.getAttrValue(), updateBo.getVariantValue())
                        ))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(updateProduceDataAttrList)) {
            log.info("过滤plm变体属性结束！无法匹配sku=>{}变体属性信息", sku);
            return Collections.emptyList();
        }

        log.info("过滤plm变体属性结束！sku=>{}，根据属性变体值列表=>{} 匹配到更新属性列表=>{}", sku, JSON.toJSONString(variantList), JSON.toJSONString(updateProduceDataAttrList));
        return updateProduceDataAttrList;
    }

    private List<UpdateProduceDataAttrBo> filterPlmAttr(String sku,
                                                        List<UpdateProduceDataAttrBo> updateProduceDataAttrList) {
        if (CollectionUtils.isEmpty(updateProduceDataAttrList)) {
            log.info("过滤plm生产属性结束！销售属性更新列表为空。");
            return Collections.emptyList();
        }

        if (StrUtil.isBlank(sku)) {
            log.info("过滤plm生产属性结束！sku为空");
            return Collections.emptyList();
        }
        //查询sku末级类目id
        Map<String, PlmCategoryVo> categoriesVoMap = plmRemoteService.getCategoriesInfoBySku(List.of(sku));
        if (CollectionUtils.isEmpty(categoriesVoMap)) {
            log.info("过滤plm生产属性结束！找不到sku=>{}类目信息", sku);
            return Collections.emptyList();
        }
        PlmCategoryVo plmCategoryVo = categoriesVoMap.get(sku);
        if (Objects.isNull(plmCategoryVo)) {
            log.info("过滤plm生产属性结束！找不到sku=>{}类目信息", sku);
            return Collections.emptyList();
        }
        Long categoryId = plmCategoryVo.getCategoryId();

        //查询末级类目关联属性详情列表
        List<String> attrNameList = plmRemoteService.getCategoryAttr(categoryId);
        if (CollectionUtils.isEmpty(attrNameList)) {
            log.info("过滤plm生产属性结束！找不到sku=>{}类目关联的属性信息", sku);
            return Collections.emptyList();
        }
        List<PlmAttributeVo> plmAttributeVoList = plmRemoteService.getAttrListByName(attrNameList);
        if (CollectionUtils.isEmpty(plmAttributeVoList)) {
            log.info("过滤plm生产属性结束！找不到sku=>{} 属性名=>{}属性详情信息", sku, JSON.toJSONString(attrNameList));
            return Collections.emptyList();
        }

        //剔除非启用状态的属性
        plmAttributeVoList.removeIf(attrVo -> !Objects.equals(attrVo.getState(), BooleanType.TRUE));
        if (CollectionUtils.isEmpty(plmAttributeVoList)) {
            log.info("过滤plm生产属性结束！剔除sku=>{} 非启用状态后 属性详情信息结果=>{}", sku, JSON.toJSONString(plmAttributeVoList));
            return Collections.emptyList();
        }

        //筛选存在于plm的属性与属性值列表
        List<Long> plmAttrNameIdList
                = plmAttributeVoList.stream().map(PlmAttributeVo::getAttributeNameId).collect(Collectors.toList());
        updateProduceDataAttrList = updateProduceDataAttrList.stream()
                .filter(updateProduceDataAttr -> plmAttrNameIdList.contains(updateProduceDataAttr.getAttributeNameId()))
                .collect(Collectors.toList());
        log.info("过滤只存在于plm生产属性 sku=>{}，属性更新列表=>{}", sku, JSON.toJSONString(updateProduceDataAttrList));

        List<Long> allPlmAttrValueIdList = Lists.newArrayList();
        for (PlmAttributeVo plmAttributeVo : plmAttributeVoList) {
            List<PlmAttributeVo.AttrValue> attrValueList = plmAttributeVo.getAttrValueList();
            if (CollectionUtils.isEmpty(attrValueList)) {
                continue;
            }
            List<Long> plmAttrValueIdList = attrValueList.stream()
                    .map(PlmAttributeVo.AttrValue::getAttributeValueId)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(plmAttrValueIdList)) {
                continue;
            }
            allPlmAttrValueIdList.addAll(plmAttrValueIdList);
        }
        updateProduceDataAttrList = updateProduceDataAttrList.stream()
                .filter(updateProduceDataAttr -> allPlmAttrValueIdList.contains(updateProduceDataAttr.getAttributeValueId()))
                .collect(Collectors.toList());

        for (UpdateProduceDataAttrBo updateProduceDataAttrBo : updateProduceDataAttrList) {
            Long attributeNameId = updateProduceDataAttrBo.getAttributeNameId();
            PlmAttributeVo matchAttr = plmAttributeVoList.stream()
                    .filter(plmAttributeVo -> Objects.equals(plmAttributeVo.getAttributeNameId(), attributeNameId))
                    .findFirst().orElse(null);

            Long attributeValueId = updateProduceDataAttrBo.getAttributeValueId();
            if (Objects.nonNull(matchAttr)) {
                updateProduceDataAttrBo.setAttributeName(matchAttr.getAttributeName());

                List<PlmAttributeVo.AttrValue> attrValueList = matchAttr.getAttrValueList();
                PlmAttributeVo.AttrValue matchAttrVal = attrValueList.stream()
                        .filter(attrVal -> Objects.equals(attrVal.getAttributeValueId(), attributeValueId))
                        .findFirst().orElse(null);
                if (Objects.nonNull(matchAttrVal)) {
                    updateProduceDataAttrBo.setAttributeValue(matchAttrVal.getAttrValue());
                }
            }
        }
        log.info("过滤只存在于plm生产属性值 sku=>{}，属性更新列表=>{}", sku, JSON.toJSONString(updateProduceDataAttrList));
        return updateProduceDataAttrList;
    }

    /**
     * 获取最大创建时间的sku以及属性
     *
     * @param plmSkuVos:
     * @param produceDataAttrPoMap:
     * @param filterAttributeNameIds:
     * @param plmSkuPoMap:
     * @return ProduceDataAttrCopyBo
     * @author ChenWenLong
     * @date 2024/9/13 15:06
     */
    private ProduceDataAttrCopyBo getProduceDataMaxCreateTimeSku(List<PlmSkuVo> plmSkuVos,
                                                                 Map<String, List<ProduceDataAttrPo>> produceDataAttrPoMap,
                                                                 List<Long> filterAttributeNameIds,
                                                                 Map<String, PlmSkuPo> plmSkuPoMap) {
        // 复制的属性列表
        List<ProduceDataAttrCopyBo> produceDataAttrCopyBoList = new ArrayList<>();

        // 计算存在则将属性而且创建时间最大的一个SKU
        for (PlmSkuVo plmSkuVo : plmSkuVos) {
            ProduceDataAttrCopyBo produceDataAttrCopyBo = new ProduceDataAttrCopyBo();
            String plmSku = plmSkuVo.getSkuCode();
            // 获取sku的属性
            List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrPoMap.get(plmSku);
            if (CollectionUtils.isEmpty(produceDataAttrPoList)) {
                continue;
            }
            // 过滤掉需要排除的属性ID
            produceDataAttrPoList = produceDataAttrPoList.stream()
                    .filter(produceDataAttrPo -> !filterAttributeNameIds.contains(produceDataAttrPo.getAttributeNameId()))
                    .filter(produceDataAttrPo -> StringUtils.isNotBlank(produceDataAttrPo.getAttrValue()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(produceDataAttrPoList)) {
                continue;
            }
            PlmSkuPo plmSkuPo = plmSkuPoMap.get(plmSku);
            if (null == plmSkuPo) {
                continue;
            }
            produceDataAttrCopyBo.setPlmSkuId(plmSkuPo.getPlmSkuId());
            produceDataAttrCopyBo.setSku(plmSkuPo.getSku());
            produceDataAttrCopyBo.setProduceDataAttrPoList(produceDataAttrPoList);
            produceDataAttrCopyBoList.add(produceDataAttrCopyBo);
        }

        if (CollectionUtils.isEmpty(produceDataAttrCopyBoList)) {
            return null;
        }

        // 获取plm的产品ID最大的sku
        return produceDataAttrCopyBoList.stream()
                .max(Comparator.comparing(ProduceDataAttrCopyBo::getPlmSkuId))
                .orElse(null);
    }

    /**
     * 提供PLM创建MQ时推送的创建sku时更新生产属性
     *
     * @param plmSkuPo:
     * @author ChenWenLong
     * @date 2024/9/13 17:55
     */
    @RedisLock(key = "#plmSkuPo.sku", prefix = ScmRedisConstant.SCM_DEVELOP_COMPLETE_RETURN)
    public void updateSkuProduceDataAttrBySku(PlmSkuPo plmSkuPo) {
        if (plmSkuPo == null || StringUtils.isBlank(plmSkuPo.getSku())) {
            return;
        }
        PlmSkuPo existPlmSkuPo = plmSkuDao.getBySku(plmSkuPo.getSku());
        if (existPlmSkuPo == null) {
            plmSkuDao.insert(plmSkuPo);
        } else {
            log.warn("创建plm_sku时，sku已存在，plmSkuPo:{}", plmSkuPo);
        }

        String sku = plmSkuPo.getSku();
        List<DevelopSampleOrderPo> updateSkuSampleList = developSampleOrderDao.getListBySku(List.of(sku));
        if (CollectionUtils.isNotEmpty(updateSkuSampleList)) {
            log.info("创建sku=>{}时，已经查询对应样品单信息，无需再更新生产属性", sku);
            return;
        }
        List<DevelopChildOrderPo> developChildOrderPoList = developChildOrderDao.getListBySku(sku);
        if (CollectionUtils.isNotEmpty(developChildOrderPoList)) {
            log.info("创建sku=>{}时，已经查询对应开发单信息，无需再更新生产属性", sku);
            return;
        }
        this.updateSkuProduceDataAttr(List.of(sku));
    }

    /**
     * 提供sku定价表更新生产资料的商品采购价格
     *
     * @param boList:
     * @author ChenWenLong
     * @date 2024/9/11 11:25
     */
    public void updateGoodsPurchasePriceByAttr(List<ProduceDataAttrPriceUpdateBo> boList) {
        log.info("提供sku定价表更新生产资料的商品采购价格，入参boList=>{}", boList);
        if (CollectionUtils.isEmpty(boList)) {
            return;
        }

        // 触发工作流异步任务
        ProduceDataPriceByAttrDto produceDataPriceByAttrDto = new ProduceDataPriceByAttrDto();
        produceDataPriceByAttrDto.setProduceDataAttrPriceUpdateBoList(boList);
        consistencyService.execAsyncTask(ProduceDataPriceByAttrHandler.class, produceDataPriceByAttrDto);

    }

    /**
     * 异步任务调用事务SKU定价来更新商品采购价格
     *
     * @param boList:
     * @author ChenWenLong
     * @date 2024/9/12 11:44
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateGoodsPurchasePriceByAttrTask(List<ProduceDataAttrPriceUpdateBo> boList) {
        log.info("异步任务调用事务SKU定价来更新商品采购价格，入参boList=>{}", boList);
        if (CollectionUtils.isEmpty(boList)) {
            return;
        }

        final Long laceAreaAttributeNameId = produceDataProp.getLaceAreaAttributeNameId();
        final Long crotchLengthAttributeNameId = produceDataProp.getCrotchLengthAttributeNameId();
        final Long materialSizeNameId = produceDataProp.getMaterialSizeNameId();
        List<ProduceDataPo> updateProduceDataPoList = new ArrayList<>();
        for (ProduceDataAttrPriceUpdateBo produceDataAttrPriceUpdateBo : boList) {
            final ProduceAttrIdAndValueBo laceAreaBo = new ProduceAttrIdAndValueBo();
            final ProduceAttrIdAndValueBo crotchLengthBo = new ProduceAttrIdAndValueBo();
            final ProduceAttrIdAndValueBo materialSizeBo = new ProduceAttrIdAndValueBo();
            laceAreaBo.setAttrNameId(laceAreaAttributeNameId);
            laceAreaBo.setAttrValueList(Collections.singletonList(produceDataAttrPriceUpdateBo.getLaceAttrValue()));
            crotchLengthBo.setAttrNameId(crotchLengthAttributeNameId);
            crotchLengthBo.setAttrValueList(Collections.singletonList(produceDataAttrPriceUpdateBo.getSizeAttrValue()));
            materialSizeBo.setAttrNameId(materialSizeNameId);
            materialSizeBo.setAttrValueList(Collections.singletonList(produceDataAttrPriceUpdateBo.getMaterialAttrValue()));
            List<String> skuList = this.getSkuListByProduceBoList(Arrays.asList(laceAreaBo, crotchLengthBo, materialSizeBo));

            if (CollectionUtils.isEmpty(skuList)) {
                log.info("根据蕾丝面积、裆长、材料获取不到sku，入参：蕾丝面积：{}, 裆长：{}, 材料:{}",
                        produceDataAttrPriceUpdateBo.getLaceAttrValue(),
                        produceDataAttrPriceUpdateBo.getSizeAttrValue(),
                        produceDataAttrPriceUpdateBo.getMaterialAttrValue());
                continue;
            }
            List<String> skuDistinctList = skuList.stream()
                    .distinct()
                    .collect(Collectors.toList());
            log.info("根据蕾丝面积和裆长获取到sku，入参：蕾丝面积：{}, 裆长：{}，材料:{},sku：{}",
                    produceDataAttrPriceUpdateBo.getLaceAttrValue(),
                    produceDataAttrPriceUpdateBo.getSizeAttrValue(),
                    produceDataAttrPriceUpdateBo.getMaterialAttrValue(),
                    skuDistinctList);
            Map<String, ProduceDataPo> produceDataPoMap = produceDataDao.getMapBySkuList(skuDistinctList);
            for (String sku : skuDistinctList) {
                ProduceDataPo produceDataPo = produceDataPoMap.get(sku);
                if (null == produceDataPo) {
                    log.warn("根据sku:{}获取不到对应生产资料信息", sku);
                    continue;
                }
                ProduceDataPo updateProduceDataPo = new ProduceDataPo();
                updateProduceDataPo.setProduceDataId(produceDataPo.getProduceDataId());
                updateProduceDataPo.setGoodsPurchasePrice(produceDataAttrPriceUpdateBo.getSkuPrice());
                updateProduceDataPo.setGoodsPurchasePriceTime(LocalDateTime.now());
                log.info("根据蕾丝面积和裆长获取到sku:{}，更新商品采购价：{}",
                        produceDataPo.getSku(),
                        produceDataAttrPriceUpdateBo.getSkuPrice());

                // 检查是否已经存在相同的 produceDataId
                updateProduceDataPoList.removeIf(existingPo ->
                        existingPo.getProduceDataId().equals(produceDataPo.getProduceDataId()));

                updateProduceDataPoList.add(updateProduceDataPo);
            }
        }
        produceDataDao.updateBatchById(updateProduceDataPoList);

    }

    private List<String> getSkuListByProduceBoList(List<ProduceAttrIdAndValueBo> produceAttrIdAndValueBoList) {
        if (CollectionUtils.isEmpty(produceAttrIdAndValueBoList)) {
            return Collections.emptyList();
        }
        Set<String> resultList = new HashSet<>();
        for (int i = 0; i < produceAttrIdAndValueBoList.size(); i++) {
            ProduceAttrIdAndValueBo bo = produceAttrIdAndValueBoList.get(i);
            final Set<String> skuSet = this.listSkuByContainAttrValues(bo.getAttrNameId(), bo.getAttrValueList());
            if (CollectionUtil.isEmpty(skuSet)) {
                return Collections.emptyList();
            }

            Map<String, List<String>> attributeMap
                    = this.getAttributesForSkus(skuSet, bo.getAttrNameId());

            log.info("计算加权平均价：参考SKU的属性id:{}，属性内容:{} ", bo.getAttrNameId(), attributeMap);
            List<String> sameSkuList = HeteCollectionsUtil.filterIntersectingKeys(bo.getAttrValueList(), attributeMap);
            if (CollectionUtils.isEmpty(sameSkuList)) {
                return Collections.emptyList();
            }
            if (i == 0) {
                resultList.addAll(sameSkuList);
            } else {
                resultList.retainAll(sameSkuList);
            }
        }


        return new ArrayList<>(resultList);
    }


    /**
     * 创建更新生产资料时调用SKU定价表获取价格更新价格
     *
     * @param produceDataPoList:
     * @param produceDataAttrPoList:
     * @author ChenWenLong
     * @date 2024/9/11 17:08
     */
    public void saveProduceDataUpdatePrice(List<ProduceDataPo> produceDataPoList,
                                           List<ProduceDataAttrPo> produceDataAttrPoList) {
        log.info("创建更新生产资料时价格更新价格，入参produceDataPoList=>{},入参produceDataAttrPoList=>{}",
                produceDataPoList,
                produceDataAttrPoList);
        if (CollectionUtils.isEmpty(produceDataPoList)) {
            return;
        }
        if (CollectionUtils.isEmpty(produceDataAttrPoList)) {
            return;
        }
        Long laceAreaAttributeNameId = this.getLaceAreaAttributeNameId();
        Long crotchLengthAttributeNameId = produceDataProp.getCrotchLengthAttributeNameId();
        Long materialSizeNameId = produceDataProp.getMaterialSizeNameId();

        Map<String, List<ProduceDataAttrPo>> produceDataAttrPoMap = produceDataAttrPoList.stream()
                .collect(Collectors.groupingBy(ProduceDataAttrPo::getSku));
        for (ProduceDataPo produceDataPo : produceDataPoList) {
            // 判断商品采购价格是否大于0
            if (null != produceDataPo.getGoodsPurchasePrice()
                    && BigDecimal.ZERO.compareTo(produceDataPo.getGoodsPurchasePrice()) < 0) {
                log.info("生产资料po=>{}对应商品采购价格大于0，无需进行SKU定价表获取价格更新价格", produceDataPo);
                continue;
            }
            List<ProduceDataAttrPo> produceDataAttrPos = produceDataAttrPoMap.get(produceDataPo.getSku());
            ProduceDataAttrPo laceAreaProduceDataAttrPo = produceDataAttrPos.stream()
                    .filter(produceDataAttrPo -> laceAreaAttributeNameId.equals(produceDataAttrPo.getAttributeNameId()))
                    .filter(produceDataAttrPo -> StringUtils.isNotBlank(produceDataAttrPo.getAttrValue()))
                    .findFirst()
                    .orElse(null);

            ProduceDataAttrPo crotchLengthProduceDataAttrPo = produceDataAttrPos.stream()
                    .filter(produceDataAttrPo -> crotchLengthAttributeNameId.equals(produceDataAttrPo.getAttributeNameId()))
                    .filter(produceDataAttrPo -> StringUtils.isNotBlank(produceDataAttrPo.getAttrValue()))
                    .findFirst()
                    .orElse(null);

            ProduceDataAttrPo materialSizeProduceDataAttrPo = produceDataAttrPos.stream()
                    .filter(produceDataAttrPo -> materialSizeNameId.equals(produceDataAttrPo.getAttributeNameId()))
                    .filter(produceDataAttrPo -> StringUtils.isNotBlank(produceDataAttrPo.getAttrValue()))
                    .findFirst()
                    .orElse(null);
            if (laceAreaProduceDataAttrPo == null || crotchLengthProduceDataAttrPo == null || materialSizeProduceDataAttrPo == null) {
                log.info("查询SKU定价表信息，但是没有找到蕾丝面积po=>{}和裆长属性po=>{}和材料属性po=>{}，无法计算价格，入参：商品sku=>{}",
                        laceAreaProduceDataAttrPo, crotchLengthProduceDataAttrPo, materialSizeProduceDataAttrPo, produceDataPo.getSku());
                continue;
            }

            SkuAttrItemDto skuAttrItemDto = new SkuAttrItemDto();
            skuAttrItemDto.setLaceAttrValue(laceAreaProduceDataAttrPo.getAttrValue());
            skuAttrItemDto.setSizeAttrValue(crotchLengthProduceDataAttrPo.getAttrValue());
            skuAttrItemDto.setMaterialAttrValue(materialSizeProduceDataAttrPo.getAttrValue());
            List<SkuAttrPricePo> skuAttrPricePoList = skuAttrPriceRefService.getListByAttr(List.of(skuAttrItemDto));
            if (CollectionUtils.isEmpty(skuAttrPricePoList)) {
                log.info("查询SKU定价表信息，但是没有找到sku属性定价，无法计算价格，入参：商品sku=>{},属性值=>{}", produceDataPo.getSku(), skuAttrItemDto);
                continue;
            }
            SkuAttrPricePo skuAttrPricePo = skuAttrPricePoList.get(0);
            log.info("查询SKU{}定价表信息=>{}", produceDataPo.getSku(), skuAttrPricePo);
            produceDataPo.setGoodsPurchasePrice(skuAttrPricePo.getSkuPrice());
            produceDataPo.setGoodsPurchasePriceTime(LocalDateTime.now());
        }

    }

    /**
     * 判断是否无需打样场景
     *
     * @param developChildOrderPo:
     * @param developPamphletOrderPo:
     * @param developSampleOrderPoList:
     * @return Boolean
     * @author ChenWenLong
     * @date 2024/11/1 18:27
     */
    public Boolean isNoProofingRequired(@NotNull DevelopChildOrderPo developChildOrderPo,
                                        DevelopPamphletOrderPo developPamphletOrderPo,
                                        List<DevelopSampleOrderPo> developSampleOrderPoList) {
        return BooleanType.FALSE.equals(developChildOrderPo.getIsSample())
                && CollectionUtils.isEmpty(developSampleOrderPoList)
                && developPamphletOrderPo != null;
    }

    /**
     * 判断是否非封样入库场景
     *
     * @param developChildOrderPo:
     * @param developPamphletOrderPo:
     * @param developSampleOrderPoList:
     * @return Boolean
     * @author ChenWenLong
     * @date 2024/10/31 17:38
     */
    public Boolean isUnsealedSampleStorage(@NotNull DevelopChildOrderPo developChildOrderPo,
                                           DevelopPamphletOrderPo developPamphletOrderPo,
                                           List<DevelopSampleOrderPo> developSampleOrderPoList) {
        return BooleanType.TRUE.equals(developChildOrderPo.getIsSample())
                && CollectionUtils.isEmpty(developSampleOrderPoList)
                && developPamphletOrderPo != null;
    }

    /**
     * 批量sku更新生产资料的商品采购价格
     *
     * @param boList:
     * @author ChenWenLong
     * @date 2024/10/31 18:21
     */
    public void updateGoodsPurchasePriceBySkuBatch(List<ProduceDataUpdatePurchasePriceBo> boList) {
        log.info("批量更新生产资料的商品采购价格，入参=>{}", boList);
        if (CollectionUtils.isEmpty(boList)) {
            return;
        }
        for (ProduceDataUpdatePurchasePriceBo produceDataUpdatePurchasePriceBo : boList) {
            this.updateGoodsPurchasePriceBySku(produceDataUpdatePurchasePriceBo);
        }
    }

}