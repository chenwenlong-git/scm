package com.hete.supply.scm.server.scm.production.service.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.plm.api.goods.entity.vo.*;
import com.hete.supply.scm.api.scm.entity.dto.PlmSkuSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.BindingProduceData;
import com.hete.supply.scm.api.scm.entity.enums.SupplierStatus;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataExportSkuAttrVo;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.SdaRemoteService;
import com.hete.supply.scm.server.scm.converter.ProduceDataAttrConverter;
import com.hete.supply.scm.server.scm.converter.ProduceDataItemConverter;
import com.hete.supply.scm.server.scm.dao.*;
import com.hete.supply.scm.server.scm.develop.dao.DevelopChildOrderDao;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopChildOrderPo;
import com.hete.supply.scm.server.scm.entity.dto.ProduceDataAttrDto;
import com.hete.supply.scm.server.scm.entity.dto.ProduceDataItemDto;
import com.hete.supply.scm.server.scm.entity.dto.ProduceDataItemRawListDto;
import com.hete.supply.scm.server.scm.entity.dto.SkuAttrItemDto;
import com.hete.supply.scm.server.scm.entity.po.*;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.nacosconfig.ProduceDataProp;
import com.hete.supply.scm.server.scm.process.dao.ProduceDataItemRawCompareDao;
import com.hete.supply.scm.server.scm.process.entity.po.ProduceDataItemRawComparePo;
import com.hete.supply.scm.server.scm.production.builder.ProdBuilder;
import com.hete.supply.scm.server.scm.production.converter.SkuProdConverter;
import com.hete.supply.scm.server.scm.production.dao.*;
import com.hete.supply.scm.server.scm.production.entity.bo.ProduceDataPoCreateBo;
import com.hete.supply.scm.server.scm.production.entity.bo.SpecBookRelateBo;
import com.hete.supply.scm.server.scm.production.entity.bo.VerifyAttrValueBo;
import com.hete.supply.scm.server.scm.production.entity.dto.*;
import com.hete.supply.scm.server.scm.production.entity.po.*;
import com.hete.supply.scm.server.scm.production.entity.vo.*;
import com.hete.supply.scm.server.scm.production.enums.AttributeScope;
import com.hete.supply.scm.server.scm.production.enums.AttributeStatus;
import com.hete.supply.scm.server.scm.production.service.ref.AttributeRefService;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.service.ref.SkuAttrPriceRefService;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierBindingListQueryBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.scm.supplier.service.ref.SupplierProductCompareRefService;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.mybatis.plus.entity.bo.CompareResult;
import com.hete.support.mybatis.plus.util.DataCompareUtil;
import com.hete.support.redis.lock.annotation.RedisLock;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/9/25 10:38
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SkuProdBaseService {

    private final ProduceDataAttrDao produceDataAttrDao;
    private final ProduceDataDao produceDataDao;
    private final PlmRemoteService plmRemoteService;
    private final ScmImageBaseService scmImageBaseService;
    private final ProduceDataItemDao produceDataItemDao;
    private final ProduceDataItemProcessDao produceDataItemProcessDao;
    private final ProduceDataItemRawDao produceDataItemRawDao;
    private final ProduceDataItemSupplierDao produceDataItemSupplierDao;
    private final ProduceDataItemProcessDescDao produceDataItemProcessDescDao;
    private final SupplierProductCompareRefService supplierProductCompareRefService;
    private final SupplierDao supplierDao;
    private final SupplierSkuMaterialAttributeDao supplierSkuMaterialAttributeDao;
    private final SupplierSkuCraftAttributeDao supplierSkuCraftAttributeDao;
    private final AttributeDao attrDao;
    private final SupplierSkuAttributeDao supSkuAttrDao;
    private final SupplierSkuAttributeValueDao supSkuAttrValDao;
    private final AttributeRefService attrRefService;
    private final SupplierSkuSampleDao supSkuSampleDao;
    private final ScmImageDao scmImageDao;
    private final SupplierProductCompareRefService supProdCompareRefService;
    private final SkuAttributeDao skuAttrDao;
    private final SkuAttributeValueDao skuAttrValDao;
    private final DevelopChildOrderDao developChildOrderDao;
    private final SdaRemoteService sdaRemoteService;
    private final ProduceDataItemRawCompareDao prodRawCompareDao;
    private final SkuAttrPriceRefService skuAttrPriceRefService;
    private final ProduceDataProp produceDataProp;

    /**
     * 编辑保存商品属性
     *
     * @param produceDataAttrList:
     * @param spu:
     * @param sku:
     * @return void
     * @author ChenWenLong
     * @date 2024/9/25 15:35
     */
    @RedisLock(key = "#sku", prefix = ScmRedisConstant.SCM_PRODUCE_DATA_ATTR_UPDATE, waitTime = 1, leaseTime = -1, exceptionDesc = "商品属性风险信息处理中，请稍后再试。")
    public void produceDataAttrPoSave(List<ProduceDataAttrDto> produceDataAttrList, String spu, String sku) {
        List<ProduceDataAttrPo> insertAttrPoList = ProduceDataAttrConverter.INSTANCE.insertConvert(produceDataAttrList);
        if (CollectionUtils.isNotEmpty(insertAttrPoList)) {
            for (ProduceDataAttrPo produceDataAttrPo : insertAttrPoList) {
                produceDataAttrPo.setSku(sku);
                produceDataAttrPo.setSpu(spu);
            }
        }
        List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.getListBySkuList(List.of(sku));
        if (CollectionUtils.isNotEmpty(produceDataAttrPoList)) {
            produceDataAttrDao.deleteBySkuList(List.of(sku));
        }
        if (CollectionUtils.isNotEmpty(insertAttrPoList)) {
            produceDataAttrDao.insertBatch(insertAttrPoList);
        }
    }

    /**
     * 创建生产资料主表
     *
     * @param produceDataPoCreateBo:
     * @return ProduceDataPo
     * @author ChenWenLong
     * @date 2024/12/2 17:04
     */
    public ProduceDataPo insertOrUpdateProduceDataPo(@NotNull ProduceDataPoCreateBo produceDataPoCreateBo) {

        // 生产信息
        ProduceDataPo produceDataPo = produceDataDao.getBySku(produceDataPoCreateBo.getSku());
        if (produceDataPo == null) {
            produceDataPo = new ProduceDataPo();
            Map<String, Long> categoriesIdMap = plmRemoteService.getCategoriesIdBySku(List.of(produceDataPoCreateBo.getSku()));
            if (!categoriesIdMap.containsKey(produceDataPoCreateBo.getSku())) {
                throw new BizException("sku:{}查询不到对应的商品类目信息，请联系管理员！", produceDataPoCreateBo.getSku());
            }
            produceDataPo.setCategoryId(categoriesIdMap.get(produceDataPoCreateBo.getSku()));
            produceDataPo.setBindingProduceData(BindingProduceData.FALSE);
            produceDataPo.setSku(produceDataPoCreateBo.getSku());
            // 判断采购价格是否大于0
            if (null != produceDataPoCreateBo.getGoodsPurchasePrice()
                    && BigDecimal.ZERO.compareTo(produceDataPoCreateBo.getGoodsPurchasePrice()) < 0) {
                produceDataPo.setGoodsPurchasePriceTime(LocalDateTime.now());
            }
            // 默认是需要管理
            produceDataPo.setRawManage(BooleanType.TRUE);
        } else {
            // 是否更新 商品采购价格的更新时间
            if (null != produceDataPoCreateBo.getGoodsPurchasePrice()
                    && produceDataPoCreateBo.getGoodsPurchasePrice().compareTo(produceDataPo.getGoodsPurchasePrice()) != 0) {
                produceDataPo.setGoodsPurchasePriceTime(LocalDateTime.now());
            }
        }
        produceDataPo.setSpu(produceDataPoCreateBo.getSpu());
        if (null != produceDataPoCreateBo.getRawManage()) {
            produceDataPo.setRawManage(produceDataPoCreateBo.getRawManage());
        }
        if (null != produceDataPoCreateBo.getGoodsPurchasePrice()) {
            produceDataPo.setGoodsPurchasePrice(produceDataPoCreateBo.getGoodsPurchasePrice());
        }

        // 采购商品价格为空或者等于0的情况,调用SKU定价表获取价格
        if (produceDataPoCreateBo.getGoodsPurchasePriceIsGetPricing()
                && (produceDataPo.getGoodsPurchasePrice() == null || BigDecimal.ZERO.compareTo(produceDataPo.getGoodsPurchasePrice()) == 0)) {
            List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.getBySku(produceDataPo.getSku());
            this.saveProduceDataUpdatePrice(List.of(produceDataPo), produceDataAttrPoList);
        }
        produceDataDao.insertOrUpdateByIdVersion(produceDataPo);
        return produceDataPo;
    }

    /**
     * 查询在售平台销售图/产品图片
     *
     * @param skuList:
     * @return Map<String, List < String>>
     * @author ChenWenLong
     * @date 2024/9/25 18:31
     */
    public Map<String, List<String>> getSkuSaleFileCodeList(List<String> skuList) {
        Map<String, List<String>> saleFileCodeMap = new HashMap<>();
        if (CollectionUtils.isEmpty(skuList)) {
            return saleFileCodeMap;
        }

        // 查询在售平台销售图
        // 创建一个 Map 来存储 platCode 和对应的 sku 列表
        Map<String, List<String>> platCodeSkuMap = new HashMap<>();
        final List<PlmNormalSkuVo> plmNormalSkuVoList = plmRemoteService.getSkuInfoBySkuList(skuList, null);
        // 使用流处理数据，将 platCode 作为 key，sku 列表作为 value
        for (PlmNormalSkuVo plmNormalSkuVo : plmNormalSkuVoList) {
            // 获取 goodsPlatVoList
            List<PlmGoodsPlatVo> goodsPlatVoList = plmNormalSkuVo.getGoodsPlatVoList();
            Optional.ofNullable(goodsPlatVoList)
                    .orElse(new ArrayList<>())
                    .forEach(platSkuVo -> {
                        // 获取 platCode
                        String platCode = platSkuVo.getPlatCode();
                        // 如果 platCode 不为空
                        if (StringUtils.isNotBlank(platCode)) {
                            // 如果 map 中还没有这个 platCode，则创建新的列表
                            platCodeSkuMap.putIfAbsent(platCode, new ArrayList<>());
                            // 将 sku 加入到 platCode 对应的列表中
                            platCodeSkuMap.get(platCode).add(plmNormalSkuVo.getSkuCode());
                        }
                    });
        }

        platCodeSkuMap.forEach((platCode, mapSkuList) -> {
            List<PlmSkuImage> skuImageList = plmRemoteService.getSkuImage(mapSkuList, platCode);
            Optional.ofNullable(skuImageList)
                    .orElse(Collections.emptyList())
                    .forEach(skuImageItem -> {
                        saleFileCodeMap.putIfAbsent(skuImageItem.getSkuCode(), new ArrayList<>());
                        saleFileCodeMap.get(skuImageItem.getSkuCode()).addAll(skuImageItem.getSaleFileCodeList());
                    });
        });


        // 如果没有在售平台的图片就取全平台第一个图片
        final List<PlmGoodsSkuVo> plmSkuInfoVoList = plmRemoteService.getSkuDetailListBySkuList(skuList);
        Map<String, List<String>> allPlatCodeSkuMap = new HashMap<>();
        for (PlmGoodsSkuVo plmGoodsSkuVo : plmSkuInfoVoList) {
            // 获取 goodsPlatVoList
            List<PlmGoodsPlatVo> goodsPlatVoList = plmGoodsSkuVo.getGoodsPlatVoList();
            Optional.ofNullable(goodsPlatVoList)
                    .orElse(new ArrayList<>())
                    .forEach(platSkuVo -> {
                        String platCode = platSkuVo.getPlatCode();
                        if (StringUtils.isNotBlank(platCode)) {
                            allPlatCodeSkuMap.putIfAbsent(platCode, new ArrayList<>());
                            allPlatCodeSkuMap.get(platCode).add(plmGoodsSkuVo.getSkuCode());
                        }
                    });
        }

        allPlatCodeSkuMap.forEach((platCode, mapSkuList) -> {
            List<PlmSkuImage> skuImageList = plmRemoteService.getSkuImage(mapSkuList, platCode);
            Optional.ofNullable(skuImageList)
                    .orElse(Collections.emptyList())
                    .forEach(skuImageItem -> {
                        if (CollectionUtils.isNotEmpty(skuImageItem.getSaleFileCodeList())
                                && CollectionUtils.isEmpty(saleFileCodeMap.get(skuImageItem.getSkuCode()))) {
                            saleFileCodeMap.putIfAbsent(skuImageItem.getSkuCode(), new ArrayList<>());
                            saleFileCodeMap.get(skuImageItem.getSkuCode()).addAll(skuImageItem.getSaleFileCodeList());
                        }

                    });
        });
        return saleFileCodeMap;
    }

    /**
     * 效果图、细节图的保存
     *
     * @param produceDataItemDtoList:
     * @param produceDataItemDtoIdList:
     * @return void
     * @author ChenWenLong
     * @date 2024/9/27 16:01
     */
    public void produceDataItemEffectDetailSave(List<ProduceDataItemInfoDto> produceDataItemDtoList,
                                                List<Long> produceDataItemDtoIdList) {
        //生产信息详情效果图
        Map<Long, List<String>> produceDataItemEffectMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.PRODUCE_DATA_ITEM_EFFECT, produceDataItemDtoIdList);
        //生产信息详情细节图
        Map<Long, List<String>> produceDataItemDetailMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.PRODUCE_DATA_ITEM_DETAIL, produceDataItemDtoIdList);

        // 效果图和细节图保存
        for (ProduceDataItemInfoDto produceDataItemInfoDto : produceDataItemDtoList) {
            List<String> fileEffectList = produceDataItemEffectMap.get(produceDataItemInfoDto.getProduceDataItemId());
            if (CollectionUtils.isNotEmpty(fileEffectList)) {
                scmImageBaseService.removeAllImageList(ImageBizType.PRODUCE_DATA_ITEM_EFFECT, List.of(produceDataItemInfoDto.getProduceDataItemId()));
            }
            scmImageBaseService.insertBatchImage(produceDataItemInfoDto.getEffectFileCodeList(), ImageBizType.PRODUCE_DATA_ITEM_EFFECT, produceDataItemInfoDto.getProduceDataItemId());

            List<String> fileDetailList = produceDataItemDetailMap.get(produceDataItemInfoDto.getProduceDataItemId());
            if (CollectionUtils.isNotEmpty(fileDetailList)) {
                scmImageBaseService.removeAllImageList(ImageBizType.PRODUCE_DATA_ITEM_DETAIL, List.of(produceDataItemInfoDto.getProduceDataItemId()));
            }
            scmImageBaseService.insertBatchImage(produceDataItemInfoDto.getDetailFileCodeList(), ImageBizType.PRODUCE_DATA_ITEM_DETAIL, produceDataItemInfoDto.getProduceDataItemId());

        }

    }

    public static List<ProduceDataItemRawPo> itemDtoToRawPo(List<ProduceDataItemDto> dtoList, String spu) {
        List<ProduceDataItemRawPo> produceDataItemRawPoList = new ArrayList<>();
        for (ProduceDataItemDto produceDataItemDto : dtoList) {
            if (CollectionUtils.isNotEmpty(produceDataItemDto.getProduceDataItemRawList())) {
                for (ProduceDataItemRawListDto produceDataItemRawDto : produceDataItemDto.getProduceDataItemRawList()) {
                    final ProduceDataItemRawPo produceDataItemRawPo = new ProduceDataItemRawPo();
                    produceDataItemRawPo.setProduceDataItemId(produceDataItemDto.getProduceDataItemId());
                    produceDataItemRawPo.setMaterialType(produceDataItemRawDto.getMaterialType());
                    produceDataItemRawPo.setSpu(spu);
                    produceDataItemRawPo.setSku(produceDataItemRawDto.getSku());
                    produceDataItemRawPo.setSkuCnt(produceDataItemRawDto.getSkuCnt());
                    produceDataItemRawPoList.add(produceDataItemRawPo);
                }
            }
        }
        return produceDataItemRawPoList;
    }

    /**
     * BOM的信息保存
     *
     * @param produceDataItemDtoList:
     * @param produceDataItemPoList:
     * @param spu:
     * @param sku:
     * @author ChenWenLong
     * @date 2024/9/27 16:12
     */
    public void produceDataItemSave(List<ProduceDataItemInfoDto> produceDataItemDtoList,
                                    List<ProduceDataItemPo> produceDataItemPoList,
                                    String spu, String sku) {
        List<Long> produceDataItemPoIdList = produceDataItemPoList.stream()
                .map(ProduceDataItemPo::getProduceDataItemId)
                .collect(Collectors.toList());
        // 入参是空情况，清空对应数据
        if (CollectionUtils.isEmpty(produceDataItemDtoList)) {
            produceDataItemDao.removeBatchByIds(produceDataItemPoList.stream()
                    .map(ProduceDataItemPo::getProduceDataItemId).collect(Collectors.toList()));
        }

        // 取入参的供应商
        List<String> uniqueSupplierCodes = produceDataItemDtoList.stream()
                .flatMap(itemDto -> {
                    // 使用 Optional 处理可能的 null 值
                    return Optional.ofNullable(itemDto.getProduceDataItemSupplierInfoList())
                            .orElseGet(List::of) // 如果为 null，则返回一个空列表
                            .stream()
                            .map(ProduceDataItemSupplierInfoDto::getSupplierCode);
                }).filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        Map<String, SupplierPo> supplierPoMap = supplierDao.getSupplierMapBySupplierCodeList(uniqueSupplierCodes);
        // 验证入参供应商数据是否正确
        for (ProduceDataItemInfoDto produceDataItemInfoDto : produceDataItemDtoList) {
            List<ProduceDataItemSupplierInfoDto> produceDataItemSupplierInfoList = produceDataItemInfoDto.getProduceDataItemSupplierInfoList();
            if (CollectionUtils.isEmpty(produceDataItemSupplierInfoList)) {
                continue;
            }

            //校验生产资料原料对照关系sku重复
            List<ProduceDataItemRawInfoDto> prodRawDtoList = produceDataItemInfoDto.getProduceDataItemRawInfoList();
            if (CollectionUtils.isNotEmpty(prodRawDtoList)) {
                for (ProduceDataItemRawInfoDto prodRawDto : prodRawDtoList) {
                    prodRawDto.validateSkuCompareDtoList();
                }
            }

            for (ProduceDataItemSupplierInfoDto produceDataItemSupplierInfoDto : produceDataItemSupplierInfoList) {
                SupplierPo supplierPo = supplierPoMap.get(produceDataItemSupplierInfoDto.getSupplierCode());
                if (supplierPo == null) {
                    throw new BizException("BOM：{}的供应商代码：{}查询不到供应商信息，请联系系统管理员！", produceDataItemInfoDto.getBomName(),
                            produceDataItemSupplierInfoDto.getSupplierCode());
                }
                // 检查供应商是否开启
                if (!SupplierStatus.ENABLED.equals(supplierPo.getSupplierStatus())) {
                    throw new BizException("BOM：{}的供应商代码：{}对应的供应商未{}，请到供应商管理列表先开启！", produceDataItemInfoDto.getBomName(),
                            produceDataItemSupplierInfoDto.getSupplierCode(), SupplierStatus.ENABLED.getRemark());
                }
            }
        }


        // 先删除BOM关联数据，在重新写入新关联数据
        // 删除原料
        List<ProduceDataItemRawPo> produceDataItemRawPoList = produceDataItemRawDao.getByProduceDataItemIdList(produceDataItemPoIdList);
        if (CollectionUtils.isNotEmpty(produceDataItemRawPoList)) {
            produceDataItemRawDao.deleteByProduceDataItemIdList(produceDataItemPoIdList);

            //删除生产资料原料对照关系
            List<Long> prodRawIdList = produceDataItemRawPoList.stream()
                    .map(ProduceDataItemRawPo::getProduceDataItemRawId)
                    .collect(Collectors.toList());
            List<ProduceDataItemRawComparePo> prodRawComparePoList = prodRawCompareDao.listByProdRawIdList(prodRawIdList);
            if (CollectionUtils.isNotEmpty(prodRawComparePoList)) {
                prodRawCompareDao.removeBatch(prodRawComparePoList);
            }
        }

        // 删除工序
        List<ProduceDataItemProcessPo> produceDataItemProcessPoList = produceDataItemProcessDao.getListBySku(sku);
        if (CollectionUtils.isNotEmpty(produceDataItemProcessPoList)) {
            produceDataItemProcessDao.deleteByProduceDataItemIdList(produceDataItemProcessPoList.stream()
                    .map(ProduceDataItemProcessPo::getProduceDataItemId).collect(Collectors.toList()));
        }
        // 删除工序描述
        List<ProduceDataItemProcessDescPo> produceDataItemProcessDescPoList = produceDataItemProcessDescDao.getListBySku(sku);
        if (CollectionUtils.isNotEmpty(produceDataItemProcessDescPoList)) {
            produceDataItemProcessDescDao.deleteByProduceDataItemIdList(produceDataItemProcessDescPoList.stream()
                    .map(ProduceDataItemProcessDescPo::getProduceDataItemId).collect(Collectors.toList()));
        }
        // 删除BOM关联供用商
        List<ProduceDataItemSupplierPo> produceDataItemSupplierPoList = produceDataItemSupplierDao.getByProduceDataItemIdList(produceDataItemPoIdList);
        if (CollectionUtils.isNotEmpty(produceDataItemSupplierPoList)) {
            produceDataItemSupplierDao.deleteByProduceDataItemIdList(produceDataItemPoIdList);
        }

        // 入参是有值情况，更新BOM信息详情
        if (CollectionUtils.isNotEmpty(produceDataItemDtoList)) {
            List<ProduceDataItemPo> insertProduceDataItemPoList = ProduceDataItemConverter.INSTANCE.itemInfoConvertPo(produceDataItemDtoList);
            int sort = insertProduceDataItemPoList.size();
            // 新增BOM的信息初始化新增ID,新增工序、工序描述等使用主键ID
            // 设置优先级
            for (ProduceDataItemPo produceDataItemPo : insertProduceDataItemPoList) {
                // 设置优先级
                produceDataItemPo.setSort(sort);
                sort--;
            }

            //编辑生产信息详情
            CompareResult<ProduceDataItemPo> itemResult = DataCompareUtil.compare(insertProduceDataItemPoList, produceDataItemPoList, ProduceDataItemPo::getProduceDataItemId);
            List<ProduceDataItemPo> insertPoList = itemResult.getNewItems().stream().peek(item -> {
                item.setSpu(spu);
                item.setSku(sku);
            }).collect(Collectors.toList());
            produceDataItemDao.insertBatch(insertPoList);
            produceDataItemDao.updateBatchByIdVersion(itemResult.getExistingItems());
            produceDataItemDao.removeBatchByIds(itemResult.getDeletedItems());

            // 写入新原料信息
            for (ProduceDataItemInfoDto prodBomDto : produceDataItemDtoList) {
                Long produceDataItemId = prodBomDto.getProduceDataItemId();
                List<ProduceDataItemRawInfoDto> prodRawDtoList = prodBomDto.getProduceDataItemRawInfoList();
                if (CollectionUtils.isEmpty(prodRawDtoList)) {
                    log.info("保存BOM：{}原料信息为空，跳过！", prodBomDto.getBomName());
                    continue;
                }

                for (ProduceDataItemRawInfoDto prodRawDto : prodRawDtoList) {
                    ProduceDataItemRawPo produceDataItemRawPo = SkuProdConverter.prodDtoToRawPo(produceDataItemId, prodRawDto, spu);
                    if (Objects.isNull(produceDataItemRawPo)) {
                        continue;
                    }
                    produceDataItemRawDao.insert(produceDataItemRawPo);

                    Long prodRawId = produceDataItemRawPo.getProduceDataItemRawId();
                    List<ProduceDataItemRawCompareDto> prodRawCompareDtoList = prodRawDto.getProdRawCompareDtoList();
                    List<ProduceDataItemRawComparePo> prodRawComparePoList = SkuProdConverter.toProdRawComparePoList(prodRawId, prodRawCompareDtoList);
                    prodRawCompareDao.insertBatch(prodRawComparePoList);
                }
            }

            //工序
            produceDataItemProcessDao.insertBatch(SkuProdConverter.itemDtoToProcessPo(produceDataItemDtoList, spu, sku));

            //工序描述
            produceDataItemProcessDescDao.insertBatch(SkuProdConverter.itemDtoToProcessDescPo(produceDataItemDtoList, spu, sku));

            //生产信息详情关联供应商
            produceDataItemSupplierDao.insertBatch(SkuProdConverter.itemDtoToSupplierPo(produceDataItemDtoList, spu, sku));

            // 增加供应商对照关系
            if (CollectionUtils.isNotEmpty(uniqueSupplierCodes)) {
                supplierProductCompareRefService.insertSupplierProductCompareBySku(sku, uniqueSupplierCodes);
            }
        }

    }

    public void updateAttr(UpdateSkuProductionDto dto) {
        String sku = dto.getSku();

        //更新克重&公差
        BigDecimal weight = Objects.isNull(dto.getWeight()) ? BigDecimal.ZERO : dto.getWeight();
        Integer weightVersion = dto.getWeightVersion();
        BigDecimal tolerance = Objects.isNull(dto.getTolerance()) ? BigDecimal.ZERO : dto.getTolerance();
        Integer toleranceVersion = dto.getToleranceVersion();
        updateProduceDataInfo(sku, weight, weightVersion, tolerance, toleranceVersion);

        //更新商品属性列表
        List<UpdateSkuAttributeDto> updateSkuAttrDtoList = dto.getSkuAttrInfoDtoList();
        updateSkuAttrList(sku, updateSkuAttrDtoList);

        //更新供应商商品生产信息列表
        List<UpdateSupSkuProdInfoDto> updateSupSkuProdInfoDtoList = dto.getUpdateSupSkuProdInfoDtoList();
        updateSupSkuProdInfoList(sku, updateSupSkuProdInfoDtoList);
    }

    public SkuProdDetailVo attrDetail(GetSkuProductionDto dto) {
        SkuProdDetailVo resVo = new SkuProdDetailVo();

        String sku = dto.getSku();
        if (StrUtil.isBlank(sku)) {
            return resVo;
        }
        resVo.setSku(sku);

        //克重&公差
        ProduceDataPo produceDataPo = produceDataDao.getBySku(sku);
        if (Objects.nonNull(produceDataPo)) {
            resVo.setWeight(produceDataPo.getWeight());
            resVo.setWeightVersion(produceDataPo.getVersion());
            resVo.setTolerance(produceDataPo.getTolerance());
            resVo.setToleranceVersion(produceDataPo.getVersion());
        }

        //商品属性值列表
        List<SkuAttributeInfoVo> skuAttributeInfoVoList = getSkuAttrInfoVoList(sku);
        resVo.setSkuAttributeInfoVoList(skuAttributeInfoVoList);

        //供应商商品生产信息列表
        List<SupSkuProdInfoVo> supSkuProdInfoVoList = getSupSkuProdInfoVoList(sku);
        resVo.setSupSkuProdInfoVoList(supSkuProdInfoVoList);
        return resVo;
    }

    /**
     * 列表查询条件
     *
     * @param dto:
     * @return PlmSkuSearchDto
     * @author ChenWenLong
     * @date 2024/10/10 15:52
     */
    public PlmSkuSearchDto getSearchPlmSkuWhere(PlmSkuSearchDto dto) {
        if (CollectionUtils.isEmpty(dto.getSkuList())) {
            dto.setSkuList(new ArrayList<>());
        }
        if (null != dto.getCategoryId()) {
            List<String> skuList = plmRemoteService.getSkuByCategoryId(dto.getCategoryId());
            if (CollectionUtils.isEmpty(skuList)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuList);
            } else {
                skuList.retainAll(dto.getSkuList());
                if (CollectionUtils.isEmpty(skuList)) {
                    return null;
                }
                dto.setSkuList(skuList);
            }
        }
        if (CollectionUtils.isNotEmpty(dto.getCategoryIdList())) {
            final List<String> skuList = plmRemoteService.getSkuListByCategoryIdList(dto.getCategoryIdList());
            if (CollectionUtils.isEmpty(skuList)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuList);
            } else {
                skuList.retainAll(dto.getSkuList());
                if (CollectionUtils.isEmpty(skuList)) {
                    return null;
                }
                dto.setSkuList(skuList);
            }
        }
        if (StringUtils.isNotBlank(dto.getSkuEncode())) {
            final List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(List.of(dto.getSkuEncode()));
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                dto.getSkuList().retainAll(skuListByEncode);
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                return null;
            }
        }

        List<String> skuEncodeList = dto.getSkuEncodeList();
        if (CollectionUtils.isNotEmpty(skuEncodeList)) {
            final List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(skuEncodeList);
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                dto.getSkuList().retainAll(skuListByEncode);
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                return null;
            }
        }

        if (dto.getBindingProduceDataList() != null &&
                dto.getBindingProduceDataList()
                        .size() == 1 &&
                dto.getBindingProduceDataList()
                        .get(0)
                        .equals(BindingProduceData.TRUE)) {
            List<ProduceDataPo> produceDataPoBindingList = produceDataDao.getListByBindingProduceData(
                    BindingProduceData.TRUE);
            if (CollectionUtil.isEmpty(produceDataPoBindingList)) {
                return null;
            }
            List<String> produceDataSkuList = produceDataPoBindingList.stream()
                    .map(ProduceDataPo::getSku)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(produceDataSkuList);
            } else {
                dto.getSkuList().retainAll(produceDataSkuList);
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                return null;
            }
        }

        if (dto.getBindingProduceDataList() != null &&
                dto.getBindingProduceDataList()
                        .size() == 1 &&
                dto.getBindingProduceDataList()
                        .get(0)
                        .equals(BindingProduceData.FALSE)) {
            List<ProduceDataPo> produceDataPoBindingList = produceDataDao.getListByBindingProduceData(
                    BindingProduceData.TRUE);
            if (CollectionUtil.isNotEmpty(produceDataPoBindingList)) {
                List<String> produceDataSkuList = produceDataPoBindingList.stream()
                        .map(ProduceDataPo::getSku)
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(dto.getNotSkuList())) {
                    dto.setNotSkuList(produceDataSkuList);
                } else {
                    dto.getNotSkuList().retainAll(produceDataSkuList);
                }
                if (CollectionUtils.isEmpty(dto.getNotSkuList())) {
                    return null;
                }
            }
        }
        return dto;
    }

    public void updateProduceDataInfo(String sku, BigDecimal weight, Integer weightVersion, BigDecimal tolerance, Integer toleranceVersion) {
        ProduceDataPo produceDataPo = ParamValidUtils.requireNotNull(
                produceDataDao.getBySku(sku), "更新生产属性信息失败！未找到商品克重&公差信息，请联系相关业务人员处理。"
        );

        boolean isUpdate = false;
        if (produceDataPo.getWeight().compareTo(weight) != 0) {
            ParamValidUtils.requireEquals(weightVersion, produceDataPo.getVersion(), "更新生产属性信息失败！克重信息已更新，请刷新重试。");
            produceDataPo.setWeight(weight);
            isUpdate = true;

        }
        if (produceDataPo.getTolerance().compareTo(tolerance) != 0) {
            ParamValidUtils.requireEquals(toleranceVersion, produceDataPo.getVersion(), "更新生产属性信息失败！克重信息已更新，请刷新重试。");
            produceDataPo.setTolerance(tolerance);
            isUpdate = true;
        }

        if (isUpdate) {
            produceDataDao.updateByIdVersion(produceDataPo);
        }
    }

    public void updateSkuAttrList(String sku, List<UpdateSkuAttributeDto> skuAttrDtoList) {
        if (CollectionUtils.isEmpty(skuAttrDtoList)) {
            log.info("sku=>{} 属性信息为空，不进行更新！", sku);
            return;
        }

        //更新商品属性
        List<SkuAttributePo> delSkuAttrPoList = skuAttrDao.listBySku(sku);
        if (CollectionUtils.isNotEmpty(delSkuAttrPoList)) {
            //删除商品属性
            List<Long> skuAttrIds = delSkuAttrPoList.stream().map(SkuAttributePo::getSkuAttributeId).collect(Collectors.toList());
            skuAttrDao.removeBatchByIds(delSkuAttrPoList);

            //删除商品属性值
            List<SkuAttributeValuePo> skuAttrValPoList = skuAttrValDao.listByIds(skuAttrIds);
            if (CollectionUtils.isNotEmpty(skuAttrValPoList)) {
                skuAttrValDao.removeBatchByIds(skuAttrValPoList);
            }
        }

        List<Long> attrIds = skuAttrDtoList.stream().map(UpdateSkuAttributeDto::getAttrId).distinct().collect(Collectors.toList());
        List<AttributePo> attributePos = attrDao.listByIds(attrIds);

        List<Long> maintainableAttrIds = attrRefService.getMaintainableAttrIds(sku, AttributeScope.GOODS);

        for (UpdateSkuAttributeDto skuAttrDto : skuAttrDtoList) {
            Long attrId = skuAttrDto.getAttrId();
            AttributePo matchAttrPo = attributePos.stream()
                    .filter(attrPo -> Objects.equals(attrId, attrPo.getAttributeId())).findFirst()
                    .orElse(null);

            List<UpdateSkuAttributeDto.SkuAttrValueDto> skuAttrValDtoList = skuAttrDto.getSkuAttrValueDtoList();
            List<VerifyAttrValueBo> verifyAttrValList = ProdBuilder.buildVerifySkuAttrValueBoList(skuAttrValDtoList);
            attrRefService.verifyAttrValList(matchAttrPo, maintainableAttrIds, verifyAttrValList, AttributeScope.GOODS);

            String attrName = Objects.nonNull(matchAttrPo) ? matchAttrPo.getAttributeName() : StrUtil.EMPTY;
            SkuAttributePo skuAttributePo = ProdBuilder.buildSkuAttributePo(sku, attrId, attrName);
            skuAttrDao.insert(skuAttributePo);
            Long skuAttrId = skuAttributePo.getSkuAttributeId();

            if (CollectionUtils.isNotEmpty(skuAttrValDtoList)) {
                List<SkuAttributeValuePo> skuAttributeValuePosList = ProdBuilder.buildSkuAttributeValuePoList(skuAttrId, skuAttrValDtoList);
                skuAttrValDao.insertBatch(skuAttributeValuePosList);
            }
        }
    }

    public void updateSupSkuProdInfoList(String sku, List<UpdateSupSkuProdInfoDto> updateSupSkuProdInfoDtoList) {
        if (CollectionUtils.isEmpty(updateSupSkuProdInfoDtoList)) {
            log.info("sku=>{} 供应商商品生产信息为空，不进行更新！", sku);
            return;
        }

        SupplierBindingListQueryBo queryParam = new SupplierBindingListQueryBo();
        queryParam.setSkuList(Collections.singletonList(sku));
        List<SupplierProductComparePo> bindingSupList
                = supProdCompareRefService.getBindingSupplierList(queryParam);
        Set<String> canUpdateSupCodeList
                = bindingSupList.stream().map(SupplierProductComparePo::getSupplierCode).collect(Collectors.toSet());

        boolean canUpdate = updateSupSkuProdInfoDtoList.stream()
                .allMatch(updateSupSkuProdInfoDto -> canUpdateSupCodeList.contains(updateSupSkuProdInfoDto.getSupplierCode()));
        ParamValidUtils.requireEquals(canUpdate, true, "生产属性数据变更或删除！请刷新页面。");

        for (UpdateSupSkuProdInfoDto updateSupSkuProdInfoDto : updateSupSkuProdInfoDtoList) {
            String supplierCode = updateSupSkuProdInfoDto.getSupplierCode();
            ParamValidUtils.requireEquals(true, attrRefService.isBinding(sku, supplierCode),
                    StrUtil.format("供应商{}与商品{}对照关系不存在！请校验后提交。", supplierCode, sku));

            //更新供应商固定属性
            List<UpdateSupSkuMaterialAttrDetailDto> supSkuMaterialAttrDtoList = updateSupSkuProdInfoDto.getSupSkuMaterialAttrDetailDtoList();
            List<UpdateSupSkuCraftAttrDetailDto> supSkuCraftAttrDtoList = updateSupSkuProdInfoDto.getSupSkuCraftAttrDetailDtoList();
            doUpdateSupSkuFixedAttrList(sku, supplierCode, supSkuMaterialAttrDtoList, supSkuCraftAttrDtoList);

            //更新供应商属性信息
            List<SupSkuAttributeInfoDto> supSkuAttrInfoDtoList = updateSupSkuProdInfoDto.getSupSkuAttrInfoDtoList();
            doUpdateSupSkuAttrList(sku, supplierCode, supSkuAttrInfoDtoList);

            //更新封样图信息
            List<String> samplePicList = updateSupSkuProdInfoDto.getSamplePicList();
            doUpdateSamplePicList(sku, supplierCode, samplePicList);
        }
    }

    public void doUpdateSamplePicList(String sku, String supplierCode, List<String> samplePicList) {
        List<SupplierSkuSamplePo> supplierSkuSamplePoList = supSkuSampleDao.listBySkuAndSupplierCode(sku, supplierCode);
        if (CollectionUtils.isNotEmpty(supplierSkuSamplePoList)) {
            List<Long> supplierSkuSampleIds = supplierSkuSamplePoList.stream().map(SupplierSkuSamplePo::getSupplierSkuSampleId).collect(Collectors.toList());
            supSkuSampleDao.removeBatchByIds(supplierSkuSamplePoList);

            List<ScmImagePo> scmImagePoList = scmImageDao.getListByIdAndType(ImageBizType.SUPPLIER_SKU_SAMPLE_PIC, supplierSkuSampleIds);
            if (CollectionUtils.isNotEmpty(scmImagePoList)) {
                scmImageDao.removeBatchByIds(scmImagePoList);
            }
        }

        if (CollectionUtils.isEmpty(samplePicList)) {
            log.info("sku=>{} 供应商{}的封样图为空，不进行更新！", sku, supplierCode);
            return;
        }

        SupplierSkuSamplePo supplierSkuSamplePo = ProdBuilder.buildSupplierSkuSamplePo(sku, supplierCode, "");
        supSkuSampleDao.insert(supplierSkuSamplePo);
        Long supplierSkuSampleId = supplierSkuSamplePo.getSupplierSkuSampleId();

        List<ScmImagePo> scmImagePoList = ProdBuilder.buildScmImagePoList(supplierSkuSampleId, new HashSet<>(samplePicList));
        scmImageDao.insertBatch(scmImagePoList);
    }

    public void doUpdateSupSkuFixedAttrList(String sku,
                                            String supplierCode,
                                            List<UpdateSupSkuMaterialAttrDetailDto> updateSupSkuMaterialAttrDtoList,
                                            List<UpdateSupSkuCraftAttrDetailDto> updateSupSkuCraftAttrDtoList) {
        //先删除后新增供应商商品原料属性
        List<SupplierSkuMaterialAttributePo> delMaterialAttrPoList
                = supplierSkuMaterialAttributeDao.listBySkuAndSupplierCode(sku, supplierCode);
        if (CollectionUtils.isNotEmpty(delMaterialAttrPoList)) {
            supplierSkuMaterialAttributeDao.removeBatchByIds(delMaterialAttrPoList);
        }
        List<SupplierSkuMaterialAttributePo> materialAttrPoList
                = ProdBuilder.buildSupplierSkuMaterialAttributePoList(sku, supplierCode, updateSupSkuMaterialAttrDtoList);
        supplierSkuMaterialAttributeDao.insertBatch(materialAttrPoList);

        //先删除后新增供应商商品工艺属性
        List<SupplierSkuCraftAttributePo> delCraftAttrPoList
                = supplierSkuCraftAttributeDao.listBySkuAndSupplierCode(sku, supplierCode);
        if (CollectionUtils.isNotEmpty(delCraftAttrPoList)) {
            supplierSkuCraftAttributeDao.removeBatchByIds(delCraftAttrPoList);
        }
        List<SupplierSkuCraftAttributePo> craftAttributePos
                = ProdBuilder.buildSupplierSkuCraftAttributePoList(sku, supplierCode, updateSupSkuCraftAttrDtoList);
        supplierSkuCraftAttributeDao.insertBatch(craftAttributePos);
    }

    public void doUpdateSupSkuAttrList(String sku, String supplierCode, List<SupSkuAttributeInfoDto> supSkuAttrDtoList) {
        //删除供应商商品属性
        List<SupplierSkuAttributePo> delSupSkuAttrPoList = supSkuAttrDao.listBySkuAndSupplierCode(sku, supplierCode);
        if (CollectionUtils.isNotEmpty(delSupSkuAttrPoList)) {
            //删除供应商商品属性
            List<Long> delSupSkuAttrIds = delSupSkuAttrPoList.stream().map(SupplierSkuAttributePo::getSupplierSkuAttributeId).collect(Collectors.toList());
            supSkuAttrDao.removeBatchByIds(delSupSkuAttrPoList);

            //删除供应商商品属性值
            List<SupplierSkuAttributeValuePo> supSkuAttrValuePos = supSkuAttrValDao.listByIds(delSupSkuAttrIds);
            if (CollectionUtils.isNotEmpty(supSkuAttrValuePos)) {
                List<Long> supSkuAttrValueIds = supSkuAttrValuePos.stream().map(SupplierSkuAttributeValuePo::getSupplierSkuAttributeValueId).collect(Collectors.toList());
                supSkuAttrValDao.removeBatchByIds(supSkuAttrValueIds);
            }
        }

        if (CollectionUtils.isEmpty(supSkuAttrDtoList)) {
            log.info("供应商商品属性信息更新结束，未传入供应商商品属性信息 sku=>{} supplierCode=>{}", sku, supplierCode);
            return;
        }

        //获取可维护的属性id列表
        List<Long> maintainableAttrIds = attrRefService.getMaintainableAttrIds(sku, AttributeScope.SUPPLIER);

        //校验：存在供应链属性
        List<Long> attrIds = supSkuAttrDtoList.stream().map(SupSkuAttributeInfoDto::getAttrId).distinct().collect(Collectors.toList());
        List<AttributePo> attributePos = attrDao.listByIds(attrIds);

        for (SupSkuAttributeInfoDto supSkuAttrDto : supSkuAttrDtoList) {
            // 属性值必填
            Long attrId = supSkuAttrDto.getAttrId();
            AttributePo matchAttrPo = attributePos.stream()
                    .filter(attrPo -> Objects.equals(attrId, attrPo.getAttributeId())).findFirst()
                    .orElse(null);

            //校验：属性值是否合法
            List<SupSkuAttributeInfoDto.SupSkuAttrValueDto> supSkuAttrValDtoList = supSkuAttrDto.getSupSkuAttrValueDtoList();
            List<VerifyAttrValueBo> verifyAttrValList = ProdBuilder.buildVerifySupSkuAttrValueBoList(supSkuAttrValDtoList);
            attrRefService.verifyAttrValList(matchAttrPo, maintainableAttrIds, verifyAttrValList, AttributeScope.SUPPLIER);

            String attrName = Objects.nonNull(matchAttrPo) ? matchAttrPo.getAttributeName() : StrUtil.EMPTY;
            SupplierSkuAttributePo supSkuAttrPo = ProdBuilder.buildSupplierSkuAttributePo(sku, supplierCode, attrId, attrName);
            supSkuAttrDao.insert(supSkuAttrPo);
            Long supSkuAttrId = supSkuAttrPo.getSupplierSkuAttributeId();

            if (CollectionUtils.isNotEmpty(supSkuAttrValDtoList)) {
                List<SupplierSkuAttributeValuePo> supplierSkuAttrValPoList
                        = ProdBuilder.buildSupplierSkuAttributeValuePoList(supSkuAttrId, supSkuAttrValDtoList);
                supSkuAttrValDao.insertBatch(supplierSkuAttrValPoList);
            }
        }
    }


    public List<SupSkuProdInfoVo> getSupSkuProdInfoVoList(String sku) {
        Set<String> supplierCodeSet = getRelateSupplierCodeList(sku);
        if (CollectionUtils.isEmpty(supplierCodeSet)) {
            return Collections.emptyList();
        }

        List<Long> attrIds = attrRefService.getMaintainableAttrIds(sku, AttributeScope.SUPPLIER);
        return supplierCodeSet.stream().map(supplierCode -> {
            SupSkuProdInfoVo resVo = new SupSkuProdInfoVo();
            resVo.setSupplierCode(supplierCode);

            //供应商商品原料属性
            List<SupSkuMaterialAttrDetailVo> materialAttrDetailVoList = getMaterialAttrDetailVoList(sku, supplierCode);
            resVo.setSupSkuMaterialAttrDetailVoList(materialAttrDetailVoList);

            //供应商商品工艺属性
            List<SupSkuCraftAttrDetailVo> craftAttrDetailVoList = getCraftAttrDetailVoList(sku, supplierCode);
            resVo.setSupSkuCraftAttrDetailVoList(craftAttrDetailVoList);

            //供应商商品属性
            List<SupSkuAttributeInfoVo> supSkuAttributeInfoVoList = getSupSkuAttrInfoVoList(sku, supplierCode, attrIds);
            resVo.setSupSkuAttributeInfoVoList(supSkuAttributeInfoVoList);

            //封样图
            List<String> samplePicList = getSamplePicList(sku, supplierCode);
            resVo.setSamplePicList(samplePicList);

            if (CollectionUtils.isNotEmpty(materialAttrDetailVoList) || CollectionUtils.isNotEmpty(craftAttrDetailVoList) ||
                    CollectionUtils.isNotEmpty(supSkuAttributeInfoVoList) || CollectionUtils.isNotEmpty(samplePicList)) {
                resVo.setIsMaintain(BooleanType.TRUE);
            } else {
                resVo.setIsMaintain(BooleanType.FALSE);
            }
            return resVo;
        }).collect(Collectors.toList());
    }

    public List<SupSkuProdInfoVo> getSupSkuProdInfoVoList(String sku, List<String> supplierCodeList) {
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return Collections.emptyList();
        }

        Set<String> supplierCodeSet = getRelateSupplierCodeList(sku);
        if (CollectionUtils.isEmpty(supplierCodeSet)) {
            return Collections.emptyList();
        }

        supplierCodeSet = supplierCodeSet.stream().filter(supplierCodeList::contains).collect(Collectors.toSet());
        List<Long> attrIds = attrRefService.getMaintainableAttrIds(sku, AttributeScope.SUPPLIER);
        return supplierCodeSet.stream().map(supplierCode -> {
            SupSkuProdInfoVo resVo = new SupSkuProdInfoVo();
            resVo.setSupplierCode(supplierCode);

            //供应商商品原料属性
            List<SupSkuMaterialAttrDetailVo> materialAttrDetailVoList = getMaterialAttrDetailVoList(sku, supplierCode);
            resVo.setSupSkuMaterialAttrDetailVoList(materialAttrDetailVoList);

            //供应商商品工艺属性
            List<SupSkuCraftAttrDetailVo> craftAttrDetailVoList = getCraftAttrDetailVoList(sku, supplierCode);
            resVo.setSupSkuCraftAttrDetailVoList(craftAttrDetailVoList);

            //供应商商品属性
            List<SupSkuAttributeInfoVo> supSkuAttributeInfoVoList = getSupSkuAttrInfoVoList(sku, supplierCode, attrIds);
            resVo.setSupSkuAttributeInfoVoList(supSkuAttributeInfoVoList);

            //封样图
            List<String> samplePicList = getSamplePicList(sku, supplierCode);
            resVo.setSamplePicList(samplePicList);

            if (CollectionUtils.isNotEmpty(materialAttrDetailVoList) || CollectionUtils.isNotEmpty(craftAttrDetailVoList) ||
                    CollectionUtils.isNotEmpty(supSkuAttributeInfoVoList) || CollectionUtils.isNotEmpty(samplePicList)) {
                resVo.setIsMaintain(BooleanType.TRUE);
            } else {
                resVo.setIsMaintain(BooleanType.FALSE);
            }
            return resVo;
        }).collect(Collectors.toList());
    }

    private Set<String> getRelateSupplierCodeList(String sku) {
        Set<String> supplierCodeSet = new HashSet<>();

        Set<String> valSupCodeSet = supSkuAttrDao.getSupCodeListBySku(sku);
        if (CollectionUtils.isNotEmpty(valSupCodeSet)) {
            log.info("sku=>{} 关联属性供应商编码=>{}", sku, JSON.toJSONString(valSupCodeSet));
            supplierCodeSet.addAll(valSupCodeSet);
        }

        Set<String> materialSupCodeSet = supplierSkuMaterialAttributeDao.getSupCodeListBySku(sku);
        if (CollectionUtils.isNotEmpty(materialSupCodeSet)) {
            log.info("sku=>{} 关联原料属性供应商编码=>{}", sku, JSON.toJSONString(materialSupCodeSet));
            supplierCodeSet.addAll(materialSupCodeSet);
        }

        Set<String> craftSupCodeSet = supplierSkuCraftAttributeDao.getSupCodeListBySku(sku);
        if (CollectionUtils.isNotEmpty(craftSupCodeSet)) {
            log.info("sku=>{} 关联工艺属性供应商编码=>{}", sku, JSON.toJSONString(craftSupCodeSet));
            supplierCodeSet.addAll(craftSupCodeSet);
        }

        Set<String> samSupCodeSet = supSkuSampleDao.getSupCodeListBySku(sku);
        if (CollectionUtils.isNotEmpty(samSupCodeSet)) {
            log.info("sku=>{} 关联封样图供应商编码=>{}", sku, JSON.toJSONString(samSupCodeSet));
            supplierCodeSet.addAll(samSupCodeSet);
        }

        SupplierBindingListQueryBo queryParam = new SupplierBindingListQueryBo();
        queryParam.setSkuList(Collections.singletonList(sku));
        List<SupplierProductComparePo> bindingSupList
                = supProdCompareRefService.getBindingSupplierList(queryParam);
        Set<String> bindingSupCodeList
                = bindingSupList.stream().map(SupplierProductComparePo::getSupplierCode).collect(Collectors.toSet());
        log.info("供应商产品对照绑定供应商编码=>{}", JSON.toJSONString(bindingSupCodeList));

        supplierCodeSet.removeIf(s -> !bindingSupCodeList.contains(s));
        log.info("生产属性可维护供应商编码=>{}", JSON.toJSONString(supplierCodeSet));
        return supplierCodeSet;
    }

    private List<SupSkuMaterialAttrDetailVo> getMaterialAttrDetailVoList(String sku, String supplierCode) {
        if (StrUtil.isBlank(sku)) {
            log.info("sku为空，无法获取供应商商品原料属性信息");
            return Collections.emptyList();
        }
        if (StrUtil.isBlank(supplierCode)) {
            log.info("供应商代码为空，无法获取供应商商品原料属性信息");
            return Collections.emptyList();
        }
        List<SupplierSkuMaterialAttributePo> supplierSkuMaterialAttributePos
                = supplierSkuMaterialAttributeDao.listBySkuAndSupplierCode(sku, supplierCode);
        return ProdBuilder.buildMaterialAttrDetailVoList(supplierSkuMaterialAttributePos);
    }

    private List<SupSkuCraftAttrDetailVo> getCraftAttrDetailVoList(String sku, String supplierCode) {
        if (StrUtil.isBlank(sku)) {
            log.info("sku为空，无法获取供应商商品工艺属性信息");
            return Collections.emptyList();
        }
        if (StrUtil.isBlank(supplierCode)) {
            log.info("供应商代码为空，无法获取供应商商品工艺属性信息");
            return Collections.emptyList();
        }
        List<SupplierSkuCraftAttributePo> supplierSkuCraftAttributePos
                = supplierSkuCraftAttributeDao.listBySkuAndSupplierCode(sku, supplierCode);
        return ProdBuilder.buildCraftAttrDetailVoList(supplierSkuCraftAttributePos);
    }

    private List<SupSkuAttributeInfoVo> getSupSkuAttrInfoVoList(String sku, String supplierCode, List<Long> attrIds) {
        //查询供应商商品属性值列表
        List<SupplierSkuAttributePo> supSkuAttrPoList
                = supSkuAttrDao.listBySupplierCodeAndSkuAndAttrIds(sku, supplierCode, attrIds);
        if (CollectionUtils.isEmpty(supSkuAttrPoList)) {
            return Collections.emptyList();
        }

        List<Long> supSkuAttrIds = supSkuAttrPoList.stream().map(SupplierSkuAttributePo::getSupplierSkuAttributeId).collect(Collectors.toList());
        List<SupplierSkuAttributeValuePo> supSkuAttrValPoList = supSkuAttrValDao.listBySupSkuAttrIds(supSkuAttrIds);

        List<AttributeInfoVo> attrInfoVoList = attrRefService.listByAttrIds(attrIds);

        return supSkuAttrPoList.stream().map(supSkuAttrPo -> {
            SupSkuAttributeInfoVo supSkuAttrInfoVo = new SupSkuAttributeInfoVo();

            Long attrId = supSkuAttrPo.getAttributeId();
            supSkuAttrInfoVo.setAttrId(attrId);

            Long supSkuAttrId = supSkuAttrPo.getSupplierSkuAttributeId();
            supSkuAttrInfoVo.setSupSkuAttrId(supSkuAttrId);

            attrInfoVoList.stream().filter(attrInfoVo -> Objects.equals(attrId, attrInfoVo.getAttrId()))
                    .findFirst().ifPresent(matchSupSkuAttrPo -> {
                        AttributeStatus attributeStatus = matchSupSkuAttrPo.getAttributeStatus();
                        supSkuAttrInfoVo.setAttributeStatus(attributeStatus);

                        List<SupplierSkuAttributeValuePo> matchSupSkuAttrValList = supSkuAttrValPoList.stream()
                                .filter(supSkuAttributeValuePo -> Objects.equals(supSkuAttrId, supSkuAttributeValuePo.getSupplierSkuAttributeId()))
                                .collect(Collectors.toList());
                        List<SupSkuAttrValueVo> supSkuAttrValVoList = ProdBuilder.buildSupSkuAttrValueVoList(matchSupSkuAttrValList);
                        supSkuAttrInfoVo.setSupSkuAttrValueVoList(supSkuAttrValVoList);
                    });
            return supSkuAttrInfoVo;
        }).collect(Collectors.toList());
    }

    public List<String> getSamplePicList(String sku, String supplierCode) {
        List<SupplierSkuSamplePo> supplierSkuSamplePos = supSkuSampleDao.listBySkuAndSupplierCode(sku, supplierCode);
        if (CollectionUtils.isEmpty(supplierSkuSamplePos)) {
            return Collections.emptyList();
        }
        List<Long> supplierSkuSampleIds = supplierSkuSamplePos.stream().map(SupplierSkuSamplePo::getSupplierSkuSampleId).collect(Collectors.toList());
        List<ScmImagePo> scmImagePoList = scmImageDao.getListByIdAndType(ImageBizType.SUPPLIER_SKU_SAMPLE_PIC, supplierSkuSampleIds);
        if (CollectionUtils.isEmpty(scmImagePoList)) {
            return Collections.emptyList();
        }
        return scmImagePoList.stream().map(ScmImagePo::getFileCode).collect(Collectors.toList());
    }

    public List<SkuAttributeInfoVo> getSkuAttrInfoVoList(String sku) {
        //查询商品属性值列表
        List<SkuAttributePo> skuAttrPoList = skuAttrDao.listBySku(sku);
        if (CollectionUtils.isEmpty(skuAttrPoList)) {
            return Collections.emptyList();
        }

        List<Long> skuAttrIds = skuAttrPoList.stream().map(SkuAttributePo::getSkuAttributeId).collect(Collectors.toList());
        List<SkuAttributeValuePo> skuAttrValPoList = skuAttrValDao.listBySkuAttrIds(skuAttrIds);

        List<Long> attrIds = skuAttrPoList.stream().map(SkuAttributePo::getAttributeId).collect(Collectors.toList());
        List<AttributeInfoVo> attrInfoVoList = attrRefService.listByAttrIds(attrIds);

        return skuAttrPoList.stream().map(skuAttributePo -> {
            SkuAttributeInfoVo skuAttrInfoVo = new SkuAttributeInfoVo();

            Long attrId = skuAttributePo.getAttributeId();
            skuAttrInfoVo.setAttrId(attrId);

            Long skuAttrId = skuAttributePo.getSkuAttributeId();
            skuAttrInfoVo.setSkuAttrId(skuAttrId);

            attrInfoVoList.stream().filter(attrInfoVo -> Objects.equals(attrId, attrInfoVo.getAttrId()))
                    .findFirst().ifPresent(matchSkuAttrPo -> {
                        AttributeStatus attrStatus = matchSkuAttrPo.getAttributeStatus();
                        skuAttrInfoVo.setAttributeStatus(attrStatus);

                        List<SkuAttributeValuePo> matchSkuAttrValList = skuAttrValPoList.stream()
                                .filter(skuAttrValPo -> Objects.equals(skuAttrId, skuAttrValPo.getSkuAttributeId()))
                                .collect(Collectors.toList());
                        List<SkuAttrValueVo> skuAttrValueVoList = ProdBuilder.buildSkuAttrValueVoList(matchSkuAttrValList);
                        skuAttrInfoVo.setSkuAttrValueVoList(skuAttrValueVoList);
                    });
            return skuAttrInfoVo;
        }).collect(Collectors.toList());
    }

    /**
     * @Description 通过sku列表获取原销售属性以及克重公差
     * @author yanjiawei
     * @Date 2024/11/4 16:27
     */
    public List<ProduceDataExportSkuAttrVo> getProduceDataExportSkuAttrVoList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.getListBySkuList(skuList);
        Map<String, List<ProduceDataAttrPo>> produceDataAttrPoMap = produceDataAttrPoList.stream()
                .collect(Collectors.groupingBy(ProduceDataAttrPo::getSku));

        Map<String, ProduceDataPo> produceDataPoMap = produceDataDao.getMapBySkuList(skuList);
        Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        Class<ProduceDataExportSkuAttrVo> clazz = ProduceDataExportSkuAttrVo.class;
        Map<String, String> fieldDescriptions = getFieldDescriptions(clazz);

        return skuList.stream().map(sku -> {
            ProduceDataExportSkuAttrVo record = new ProduceDataExportSkuAttrVo();
            record.setSku(sku);
            record.setSkuEncode(skuEncodeMap.get(record.getSku()));

            ProduceDataPo produceDataPo = produceDataPoMap.get(record.getSku());
            if (null != produceDataPo) {
                record.setWeight(produceDataPo.getWeight());
                record.setTolerance(produceDataPo.getTolerance());
            }

            List<ProduceDataAttrPo> produceDataAttrPos = Optional.ofNullable(produceDataAttrPoMap.get(record.getSku())).orElse(new ArrayList<>());
            Map<String, List<ProduceDataAttrPo>> produceDataAttrPoAttrNameMap = produceDataAttrPos.stream()
                    .filter(produceDataAttrPo -> StringUtils.isNotBlank(produceDataAttrPo.getAttrName()))
                    .collect(Collectors.groupingBy(ProduceDataAttrPo::getAttrName));

            fieldDescriptions.forEach((String fieldName, String fieldVal) -> {
                List<ProduceDataAttrPo> produceDataAttrPoAttrNameList = produceDataAttrPoAttrNameMap.get(fieldVal);
                if (CollectionUtils.isNotEmpty(produceDataAttrPoAttrNameList)) {
                    String attrValue = produceDataAttrPoAttrNameList.stream()
                            .map(ProduceDataAttrPo::getAttrValue)
                            .collect(Collectors.joining(","));
                    this.setFieldValue(record, fieldName, attrValue);
                }
            });
            return record;
        }).collect(Collectors.toList());
    }

    private static Map<String, String> getFieldDescriptions(Class<?> clazz) {
        Map<String, String> fieldDescriptionMap = new HashMap<>();

        // 获取类中定义的所有字段
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 判断字段是否有 ApiModelProperty 注解
            if (field.isAnnotationPresent(ApiModelProperty.class)) {
                ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
                String fieldName = field.getName();
                String description = apiModelProperty.value();
                // 将字段名和描述信息存入 Map
                fieldDescriptionMap.put(fieldName, description);
            }
        }
        return fieldDescriptionMap;
    }

    private void setFieldValue(ProduceDataExportSkuAttrVo record, String fieldName, String value) {
        switch (fieldName) {
            case "color":
                record.setColor(value);
                break;
            case "laceArea":
                record.setLaceArea(value);
                break;
            case "fileLengthSize":
                record.setFileLengthSize(value);
                break;
            case "completeLongSize":
                record.setCompleteLongSize(value);
                break;
            case "netCapSize":
                record.setNetCapSize(value);
                break;
            case "partedBangs":
                record.setPartedBangs(value);
                break;
            case "parting":
                record.setParting(value);
                break;
            case "material":
                record.setMaterial(value);
                break;
            case "contour":
                record.setContour(value);
                break;
            case "colorSystem":
                record.setColorSystem(value);
                break;
            case "colorMixPartition":
                record.setColorMixPartition(value);
                break;
            case "leftSideLength":
                record.setLeftSideLength(value);
                break;
            case "leftFinish":
                record.setLeftFinish(value);
                break;
            case "rightSideLength":
                record.setRightSideLength(value);
                break;
            case "rightFinish":
                record.setRightFinish(value);
                break;
            case "symmetry":
                record.setSymmetry(value);
                break;
            case "preselectionLace":
                record.setPreselectionLace(value);
                break;
            default:
                throw new BizException("导出VO字段{}数据错误，系统匹配不到对应表头字段" + fieldName);
        }
    }

    public List<SpecBookRelateBo> getSpecBookRelateBoList(String sku) {
        if (StringUtils.isBlank(sku)) {
            log.info("获取规格书关联信息返回为空！sku为空!");
            return Collections.emptyList();
        }

        SupplierBindingListQueryBo queryParam = new SupplierBindingListQueryBo();
        queryParam.setSkuList(Collections.singletonList(sku));
        List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareRefService.getBindingSupplierList(queryParam);
        if (CollectionUtils.isEmpty(supplierProductComparePoList)) {
            log.info("获取规格书关联信息返回为空！未找到sku对应的供应商信息！sku:{}", sku);
            return Collections.emptyList();
        }
        Set<String> supplierCodeList = supplierProductComparePoList.stream().map(SupplierProductComparePo::getSupplierCode).collect(Collectors.toSet());
        return getSpecBookRelateBoList(sku, supplierCodeList);
    }

    public List<SpecBookRelateBo> getSpecBookRelateBoList(String sku, Set<String> supplierCodeList) {
        if (StringUtils.isBlank(sku)) {
            log.info("获取规格书关联信息返回为空！sku为空!");
            return Collections.emptyList();
        }
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            log.info("获取规格书关联信息返回为空！supplierCodeList为空!");
            return Collections.emptyList();
        }

        List<PlmSkuVo> skuEncodeBySku = plmRemoteService.getSkuEncodeBySku(Collections.singletonList(sku));
        PlmSkuVo plmSkuVo = skuEncodeBySku.stream().findFirst().orElse(null);
        if (Objects.isNull(plmSkuVo)) {
            return Collections.emptyList();
        }
        String spuCode = plmSkuVo.getSpuCode();
        String skuEncode = plmSkuVo.getSkuEncode();
        String loginUsername = GlobalContext.getUsername();

        List<DevelopChildOrderPo> developChildOrderPoList = developChildOrderDao.getListBySku(sku);

        Map<String, List<String>> skuSaleFileCodeMap = getSkuSaleFileCodeList(Collections.singletonList(sku));

        return supplierCodeList.stream().map(supplierCode -> {
            SpecBookRelateBo specBookRelateBo = new SpecBookRelateBo();
            specBookRelateBo.setSupplierCode(supplierCode);

            specBookRelateBo.setSku(sku);
            specBookRelateBo.setSkuEncode(skuEncode);
            specBookRelateBo.setSpu(spuCode);

            List<String> devPlatformList = developChildOrderPoList.stream()
                    .filter(developChildOrderPo -> Objects.equals(developChildOrderPo.getSupplierCode(), supplierCode) && StrUtil.isNotBlank(developChildOrderPo.getPlatform()))
                    .map(DevelopChildOrderPo::getPlatform)
                    .distinct()
                    .collect(Collectors.toList());
            specBookRelateBo.setDevPlatformList(devPlatformList);

            if (CollectionUtils.isNotEmpty(devPlatformList)) {
                Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(devPlatformList);
                List<SpecPlatformVo> specPlatformVoList = devPlatformList.stream().map(platformCode -> {
                    SpecPlatformVo specPlatformVo = new SpecPlatformVo();
                    specPlatformVo.setPlatformCode(platformCode);
                    specPlatformVo.setPlatformName(platCodeNameMap.get(platformCode));
                    return specPlatformVo;
                }).collect(Collectors.toList());
                specBookRelateBo.setSpecPlatformVoList(specPlatformVoList);
            }

            specBookRelateBo.setFileCodeList(skuSaleFileCodeMap.get(sku));
            specBookRelateBo.setLoginUsername(loginUsername);
            return specBookRelateBo;
        }).collect(Collectors.toList());
    }

    /**
     * 创建更新生产资料时调用SKU定价表获取价格更新价格 todo: 与ProduceDataBaseService的saveProduceDataUpdatePrice方法逻辑重复，需要优化
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
        Long laceAreaAttributeNameId = produceDataProp.getLaceAreaAttributeNameId();
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
                    .filter(produceDataAttrPo -> com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(produceDataAttrPo.getAttrValue()))
                    .findFirst()
                    .orElse(null);

            ProduceDataAttrPo crotchLengthProduceDataAttrPo = produceDataAttrPos.stream()
                    .filter(produceDataAttrPo -> crotchLengthAttributeNameId.equals(produceDataAttrPo.getAttributeNameId()))
                    .filter(produceDataAttrPo -> com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(produceDataAttrPo.getAttrValue()))
                    .findFirst()
                    .orElse(null);

            ProduceDataAttrPo materialSizeProduceDataAttrPo = produceDataAttrPos.stream()
                    .filter(produceDataAttrPo -> materialSizeNameId.equals(produceDataAttrPo.getAttributeNameId()))
                    .filter(produceDataAttrPo -> com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(produceDataAttrPo.getAttrValue()))
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

}










