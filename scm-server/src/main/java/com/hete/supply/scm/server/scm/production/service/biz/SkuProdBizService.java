package com.hete.supply.scm.server.scm.production.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.google.common.collect.Lists;
import com.hete.supply.plm.api.basedata.entity.vo.PlmAttributeVo;
import com.hete.supply.plm.api.basedata.enums.AttributeEntryType;
import com.hete.supply.plm.api.developorder.enums.MaterialType;
import com.hete.supply.plm.api.goods.entity.vo.*;
import com.hete.supply.scm.api.scm.entity.dto.PlmSkuSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.ProduceDataAttrImportDto;
import com.hete.supply.scm.api.scm.entity.dto.ProduceDataSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.supply.scm.api.scm.entity.enums.SkuRisk;
import com.hete.supply.scm.api.scm.entity.enums.SupplierStatus;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataExportSkuAttrVo;
import com.hete.supply.scm.api.scm.entity.vo.SkuProdSkuCompareExportVo;
import com.hete.supply.scm.api.scm.entity.vo.SkuProdSkuExportVo;
import com.hete.supply.scm.api.scm.importation.entity.dto.ProduceDataRawProcessImportDto;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.SdaRemoteService;
import com.hete.supply.scm.server.scm.adjust.entity.po.GoodsPricePo;
import com.hete.supply.scm.server.scm.adjust.service.base.GoodsPriceBaseService;
import com.hete.supply.scm.server.scm.converter.ProduceDataConverter;
import com.hete.supply.scm.server.scm.dao.*;
import com.hete.supply.scm.server.scm.develop.dao.DevelopChildOrderDao;
import com.hete.supply.scm.server.scm.entity.dto.ProdDataAttrImportDto;
import com.hete.supply.scm.server.scm.entity.dto.SkuDto;
import com.hete.supply.scm.server.scm.entity.po.*;
import com.hete.supply.scm.server.scm.enums.BindingSupplierProduct;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.nacosconfig.ProduceDataProp;
import com.hete.supply.scm.server.scm.process.dao.ProcessDao;
import com.hete.supply.scm.server.scm.process.dao.ProduceDataItemRawCompareDao;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProduceDataItemRawComparePo;
import com.hete.supply.scm.server.scm.process.service.base.PageExecutor;
import com.hete.supply.scm.server.scm.production.builder.ProdBuilder;
import com.hete.supply.scm.server.scm.production.converter.SkuProdConverter;
import com.hete.supply.scm.server.scm.production.dao.SkuRiskDao;
import com.hete.supply.scm.server.scm.production.dao.SkuRiskLogDao;
import com.hete.supply.scm.server.scm.production.entity.bo.ColorAttrRuleBo;
import com.hete.supply.scm.server.scm.production.entity.bo.ProduceDataPoCreateBo;
import com.hete.supply.scm.server.scm.production.entity.bo.SkuRiskBo;
import com.hete.supply.scm.server.scm.production.entity.bo.SpecBookRelateBo;
import com.hete.supply.scm.server.scm.production.entity.dto.*;
import com.hete.supply.scm.server.scm.production.entity.po.SkuRiskLogPo;
import com.hete.supply.scm.server.scm.production.entity.po.SkuRiskPo;
import com.hete.supply.scm.server.scm.production.entity.vo.*;
import com.hete.supply.scm.server.scm.production.enums.AttributeScope;
import com.hete.supply.scm.server.scm.production.service.base.AttributeBaseService;
import com.hete.supply.scm.server.scm.production.service.base.SkuProdBaseService;
import com.hete.supply.scm.server.scm.production.service.ref.AttributeRefService;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseLatestPriceItemBo;
import com.hete.supply.scm.server.scm.service.base.ProduceDataBaseService;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.service.base.SkuBaseService;
import com.hete.supply.scm.server.scm.service.ref.PlmSkuRefService;
import com.hete.supply.scm.server.scm.service.ref.ProduceDataRefService;
import com.hete.supply.scm.server.scm.supplier.converter.SupplierProductCompareConverter;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierProductCompareDao;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierInventoryBatchUpdateBo;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierProductCompareDetailDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierProductCompareItemDto;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierProductCompareVo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierProductCompareBaseService;
import com.hete.supply.scm.server.scm.supplier.service.ref.SupplierProductCompareRefService;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.mybatis.plus.entity.bo.CompareResult;
import com.hete.support.mybatis.plus.util.DataCompareUtil;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 商品信息业务层
 *
 * @author ChenWenLong
 * @date 2024/9/25 10:38
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SkuProdBizService {

    private final PlmRemoteService plmRemoteService;
    private final PlmSkuDao plmSkuDao;
    private final ProduceDataDao produceDataDao;
    private final ProduceDataAttrDao produceDataAttrDao;
    private final SkuProdBaseService skuProdBaseService;
    private final SkuInfoDao skuInfoDao;
    private final SupplierProductCompareDao supplierProductCompareDao;
    private final GoodsPriceBaseService goodsPriceBaseService;
    private final SupplierDao supplierDao;
    private final SupplierProductCompareBaseService supplierProductCompareBaseService;
    private final SkuBaseService skuBaseService;
    private final ProduceDataItemDao produceDataItemDao;
    private final ProduceDataItemRawDao produceDataItemRawDao;
    private final ProduceDataItemProcessDao produceDataItemProcessDao;
    private final ScmImageBaseService scmImageBaseService;
    private final ProduceDataItemProcessDescDao produceDataItemProcessDescDao;
    private final ProduceDataItemSupplierDao produceDataItemSupplierDao;
    private final DevelopChildOrderDao developChildOrderDao;
    private final AttributeBaseService attributeBaseService;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final IdGenerateService idGenerateService;
    private final AttributeRefService attrRefService;
    private final SupplierProductCompareRefService supplierProductCompareRefService;
    private final SkuRiskLogDao skuRiskLogDao;
    private final SkuRiskDao skuRiskDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final ProduceDataRefService produceDataRefService;
    private final SdaRemoteService sdaRemoteService;
    private final PlmSkuRefService plmSkuRefService;
    private final ProduceDataProp produceDataProp;
    private final ProduceDataItemRawCompareDao prodRawCompareDao;
    private final ProduceDataBaseService produceDataBaseService;
    private final ProcessDao processDao;

    /**
     * 获取商品信息详情头部信息
     *
     * @param dto:
     * @return SkuTopDetailVo
     * @author ChenWenLong
     * @date 2024/9/25 10:47
     */
    public SkuTopDetailVo getSkuTop(SkuDto dto) {
        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(dto.getSku());
        Assert.notNull(plmSkuPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新列表页面后重试！"));

        // 查询plm信息
        List<PlmVariantVo> plmVariantVoList = plmRemoteService.getVariantAttr(List.of(plmSkuPo.getSku()));
        PlmVariantVo plmVariantVo = plmVariantVoList.stream()
                .filter(variantVo -> variantVo.getSkuCode().equals(plmSkuPo.getSku()))
                .findFirst()
                .orElse(null);
        if (null == plmVariantVo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新列表页面后重试！");
        }

        // 查询PLM的商品类目名称
        Map<String, PlmCategoryVo> categoriesVoMap = plmRemoteService.getCategoriesInfoBySku(List.of(plmSkuPo.getSku()));


        // 获取图片
        Map<String, List<String>> skuSaleFileCodeMap = skuProdBaseService.getSkuSaleFileCodeList(List.of(plmSkuPo.getSku()));

        // 获取供应商绑定关系
        List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getListByBatchSku(List.of(plmSkuPo.getSku()));

        // 转换为出参的数据
        return SkuProdConverter.skuDataToSkuTopDetail(plmSkuPo,
                plmVariantVo,
                categoriesVoMap,
                skuSaleFileCodeMap,
                supplierProductComparePoList);

    }

    /**
     * 销售属性详情
     *
     * @param dto:
     * @return ProduceDataAttrDetailVo
     * @author ChenWenLong
     * @date 2024/9/25 14:49
     */
    public ProduceDataAttrDetailVo produceDataAttrDetail(SkuDto dto) {
        ProduceDataAttrDetailVo produceDataAttrDetailVo = new ProduceDataAttrDetailVo();
        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(dto.getSku());
        Assert.notNull(plmSkuPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新列表页面后重试！"));

        produceDataAttrDetailVo.setSku(plmSkuPo.getSku());

        //生产信息
        ProduceDataPo produceDataPo = produceDataDao.getBySku(dto.getSku());
        if (produceDataPo == null) {
            return produceDataAttrDetailVo;
        }

        //获取生产属性
        List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.getBySku(plmSkuPo.getSku());

        // 转换为出参的数据
        List<ProduceDataAttrItemVo> produceDataAttrItemVos = SkuProdConverter.arrPoToArrVo(produceDataAttrPoList);
        produceDataAttrDetailVo.setProduceDataAttrList(produceDataAttrItemVos);
        return produceDataAttrDetailVo;
    }

    /**
     * 更新销售属性详情
     *
     * @param dto:
     * @author ChenWenLong
     * @date 2024/9/25 15:24
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateProduceDataAttr(UpdateProduceDataAttrDto dto) {
        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(dto.getSku());
        Assert.notNull(plmSkuPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新列表页面后重试！"));

        Map<String, String> spuMapBySku = plmRemoteService.getSpuMapBySkuList(List.of(plmSkuPo.getSku()));
        String spu = spuMapBySku.get(plmSkuPo.getSku());
        if (StringUtils.isBlank(spu)) {
            throw new BizException("SKU:{}获取不到对应的PLM的SPU，请联系系统管理员！", plmSkuPo.getSku());
        }

        // 生产信息
        ProduceDataPoCreateBo produceDataPoCreateBo = new ProduceDataPoCreateBo();
        produceDataPoCreateBo.setSpu(spu);
        produceDataPoCreateBo.setSku(plmSkuPo.getSku());
        skuProdBaseService.insertOrUpdateProduceDataPo(produceDataPoCreateBo);
        skuProdBaseService.produceDataAttrPoSave(dto.getProduceDataAttrList(), spu, plmSkuPo.getSku());
    }

    /**
     * 采购信息详情
     *
     * @param dto:
     * @return PurchaseProduceDataDetailVo
     * @author ChenWenLong
     * @date 2024/9/26 09:58
     */
    public PurchaseProduceDataDetailVo purchaseProduceDataDetail(SkuDto dto) {
        PurchaseProduceDataDetailVo purchaseProduceDataDetailVo = new PurchaseProduceDataDetailVo();
        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(dto.getSku());
        Assert.notNull(plmSkuPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新列表页面后重试！"));

        purchaseProduceDataDetailVo.setSku(plmSkuPo.getSku());
        // 查询生产周期
        purchaseProduceDataDetailVo.setCycle(plmSkuPo.getCycle());
        purchaseProduceDataDetailVo.setVersion(plmSkuPo.getVersion());

        // 查询单件产能
        SkuInfoPo skuInfoPo = skuInfoDao.getBySku(plmSkuPo.getSku());
        if (null != skuInfoPo) {
            purchaseProduceDataDetailVo.setSingleCapacity(skuInfoPo.getSingleCapacity());
        }

        // 查询采购价格
        ProduceDataPo produceDataPo = produceDataDao.getBySku(plmSkuPo.getSku());
        if (null != produceDataPo) {
            purchaseProduceDataDetailVo.setGoodsPurchasePrice(produceDataPo.getGoodsPurchasePrice());
        }

        //查询渠道价格
        List<GoodsPricePo> goodsPricePoList = goodsPriceBaseService.getEffectiveListBySkuList(List.of(plmSkuPo.getSku()));

        // 查询供应商产品对照信息
        List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getBySku(dto.getSku());
        List<SupplierProductCompareItemVo> supplierProductCompareItemVoList = SupplierProductCompareConverter.poToItemVo(supplierProductComparePoList, goodsPricePoList);
        purchaseProduceDataDetailVo.setSupplierProductCompareItemList(supplierProductCompareItemVoList);

        return purchaseProduceDataDetailVo;

    }

    /**
     * 更新采购信息
     *
     * @param dto:
     * @author ChenWenLong
     * @date 2024/9/26 11:15
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePurchaseProduceData(UpdatePurchaseProduceDataDto dto) {
        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(dto.getSku());
        Assert.notNull(plmSkuPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新列表页面后重试！"));
        Assert.isTrue(plmSkuPo.getVersion().equals(dto.getVersion()),
                () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));

        // 供应商库存信息
        List<SupplierInventoryBatchUpdateBo> supplierInventoryBatchUpdateBoList = new ArrayList<>();

        //当绑定供应商产品对照列表不为空时
        if (CollectionUtils.isNotEmpty(dto.getSupplierProductCompareItemList())) {
            List<String> supplierCodeList = dto.getSupplierProductCompareItemList().stream()
                    .map(SupplierProductCompareItemDto::getSupplierCode).distinct().collect(Collectors.toList());
            Map<String, SupplierPo> supplierMap = supplierDao.getSupplierMapBySupplierCodeList(supplierCodeList);
            List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getBySku(plmSkuPo.getSku());
            // 对产品对照关系信息进行验证
            this.verifySupplierProductCompareItem(dto.getSupplierProductCompareItemList(), supplierMap, supplierProductComparePoList);

            List<SupplierProductComparePo> newSupplierProductComparePoList = SupplierProductCompareConverter.itemDtoToPo(dto.getSupplierProductCompareItemList());

            CompareResult<SupplierProductComparePo> itemResult = DataCompareUtil.compare(newSupplierProductComparePoList,
                    supplierProductComparePoList, SupplierProductComparePo::getSupplierProductCompareId);
            List<SupplierProductComparePo> insertSupplierProductComparePos = itemResult.getNewItems().stream().peek(item -> {
                SupplierPo supplierPo = supplierMap.get(item.getSupplierCode());

                // 供应商库存信息组装
                SupplierInventoryBatchUpdateBo supplierInventoryBatchUpdateBo = new SupplierInventoryBatchUpdateBo();
                supplierInventoryBatchUpdateBo.setSku(plmSkuPo.getSku());
                supplierInventoryBatchUpdateBo.setSupplierCode(item.getSupplierCode());

                if (null != supplierPo) {
                    item.setSupplierName(supplierPo.getSupplierName());
                    supplierInventoryBatchUpdateBo.setSupplierName(supplierPo.getSupplierName());
                }
                supplierInventoryBatchUpdateBoList.add(supplierInventoryBatchUpdateBo);
                item.setSku(plmSkuPo.getSku());
                item.setSupplierProductCompareStatus(BooleanType.TRUE);
            }).collect(Collectors.toList());
            supplierProductCompareDao.insertBatch(insertSupplierProductComparePos);
            // 编辑时数据处理
            if (CollectionUtils.isNotEmpty(itemResult.getExistingItems())) {
                for (SupplierProductComparePo existingItem : itemResult.getExistingItems()) {
                    // 供应商库存信息组装
                    SupplierInventoryBatchUpdateBo supplierInventoryBatchUpdateBo = new SupplierInventoryBatchUpdateBo();
                    supplierInventoryBatchUpdateBo.setSku(plmSkuPo.getSku());
                    supplierInventoryBatchUpdateBo.setSupplierCode(existingItem.getSupplierCode());
                    SupplierPo supplierPo = supplierMap.get(existingItem.getSupplierCode());
                    if (null != supplierPo) {
                        existingItem.setSupplierName(supplierPo.getSupplierName());
                        supplierInventoryBatchUpdateBo.setSupplierName(supplierPo.getSupplierName());
                    }
                    supplierInventoryBatchUpdateBoList.add(supplierInventoryBatchUpdateBo);
                }
            }
            supplierProductCompareDao.updateBatchByIdVersion(itemResult.getExistingItems());
            plmSkuPo.setBindingSupplierProduct(BindingSupplierProduct.TRUE);
        }

        plmSkuPo.setCycle(dto.getCycle());
        plmSkuDao.updateByIdVersion(plmSkuPo);

        // 生产信息更新商品采购价格
        Map<String, String> spuMapBySku = plmRemoteService.getSpuMapBySkuList(List.of(plmSkuPo.getSku()));
        String spu = spuMapBySku.get(plmSkuPo.getSku());
        ProduceDataPoCreateBo produceDataPoCreateBo = new ProduceDataPoCreateBo();
        produceDataPoCreateBo.setSpu(spu);
        produceDataPoCreateBo.setSku(plmSkuPo.getSku());
        produceDataPoCreateBo.setGoodsPurchasePrice(dto.getGoodsPurchasePrice());
        produceDataPoCreateBo.setGoodsPurchasePriceIsGetPricing(true);
        skuProdBaseService.insertOrUpdateProduceDataPo(produceDataPoCreateBo);

        // 增加时执行初始供应商库存信息
        supplierProductCompareBaseService.supplierInventoryUpdateBatch(supplierInventoryBatchUpdateBoList);

        // 更新单价产能
        skuBaseService.saveSkuInfoSingleCapacity(plmSkuPo.getSku(), dto.getSingleCapacity());

    }

    /**
     * 供应商产品对照关系入参进行检验
     *
     * @param supplierProductCompareItemList:
     * @param supplierMap:
     * @param supplierProductComparePoList:
     * @return void
     * @author ChenWenLong
     * @date 2024/9/26 14:20
     */
    public void verifySupplierProductCompareItem(List<SupplierProductCompareItemDto> supplierProductCompareItemList,
                                                 Map<String, SupplierPo> supplierMap,
                                                 List<SupplierProductComparePo> supplierProductComparePoList) {
        if (CollectionUtils.isEmpty(supplierProductCompareItemList)) {
            return;
        }

        // 拦截入参是否存在重复供应商
        List<String> supplierCodeList = supplierProductCompareItemList.stream()
                .map(SupplierProductCompareItemDto::getSupplierCode)
                .distinct()
                .collect(Collectors.toList());

        if (supplierCodeList.size() != supplierProductCompareItemList.size()) {
            throw new BizException("商品信息详情中禁止重复添加供应商！");
        }

        // 验证创建时候供应商是否正确
        supplierProductCompareItemList.stream()
                .filter(item -> null == item.getSupplierProductCompareId())
                .forEach(item -> {
                    SupplierPo insertSupplierPo = supplierMap.get(item.getSupplierCode());
                    if (null == insertSupplierPo) {
                        throw new BizException("供应商代码：查询不到供应商信息，请联系系统管理员！", item.getSupplierCode());
                    }
                    if (null == insertSupplierPo.getSupplierStatus() || insertSupplierPo.getSupplierStatus() != SupplierStatus.ENABLED) {
                        throw new BizException("供应商代码：{}的供应商状态为：{}，请先联系管理员启用！", item.getSupplierCode(), insertSupplierPo.getSupplierStatus());
                    }
                    // 验证新建的是否已经在数据库中存在
                    if (supplierProductComparePoList.stream().anyMatch(po -> po.getSupplierCode().equals(item.getSupplierCode()))) {
                        throw new BizException("新建的关联供应商：{}已经存在，禁止重复新建，请联系系统管理员！", item.getSupplierCode());
                    }
                });

        // 验证编辑时候供应商是否正确
        supplierProductCompareItemList.stream()
                .filter(item -> null != item.getSupplierProductCompareId())
                .forEach(item -> {

                    // 验证新建的是否已经在数据库中存在
                    if (supplierProductComparePoList.stream()
                            .filter(po -> !po.getSupplierProductCompareId().equals(item.getSupplierProductCompareId()))
                            .anyMatch(po -> po.getSupplierCode().equals(item.getSupplierCode()))) {
                        throw new BizException("编辑的关联供应商：{}已经存在，禁止重复，请联系系统管理员！", item.getSupplierCode());
                    }

                    // 验证版本号是否正确
                    supplierProductComparePoList.stream()
                            .filter(po -> po.getSupplierProductCompareId().equals(item.getSupplierProductCompareId()))
                            .findFirst()
                            .ifPresent(po -> Assert.isTrue(po.getVersion().equals(item.getVersion()),
                                    () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！")));

                    if (null == item.getSupplierProductCompareStatus()) {
                        throw new BizException("编辑的关联供应商：{}入参需要传状态字段，请联系系统管理员！", item.getSupplierCode());
                    }

                });

    }

    /**
     * 原料&工序信息详情
     *
     * @param dto:
     * @return ProduceDataItemDetailVo
     * @author ChenWenLong
     * @date 2024/9/26 16:50
     */
    public ProduceDataItemDetailVo produceDataItemDetail(SkuDto dto) {
        ProduceDataItemDetailVo produceDataItemDetailVo = new ProduceDataItemDetailVo();
        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(dto.getSku());
        Assert.notNull(plmSkuPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新列表页面后重试！"));

        produceDataItemDetailVo.setSku(plmSkuPo.getSku());
        produceDataItemDetailVo.setRawManage(BooleanType.TRUE);

        //生产信息
        ProduceDataPo produceDataPo = produceDataDao.getBySku(dto.getSku());
        if (produceDataPo == null) {
            return produceDataItemDetailVo;
        }
        produceDataItemDetailVo.setVersion(produceDataPo.getVersion());
        produceDataItemDetailVo.setRawManage(produceDataPo.getRawManage());

        //获取生产信息BOM详情列表
        List<ProduceDataItemPo> produceDataItemPoList = produceDataItemDao.getListBySku(plmSkuPo.getSku());
        // 获取ID
        List<Long> produceDataItemIdList = produceDataItemPoList
                .stream()
                .map(ProduceDataItemPo::getProduceDataItemId)
                .collect(Collectors.toList());

        //原料BOM信息
        List<ProduceDataItemRawPo> produceDataItemRawPoList = produceDataItemRawDao.getByProduceDataItemIdList(produceDataItemIdList);

        //原料BOM关联关系
        List<ProduceDataItemRawComparePo> prodRawComparePoList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(produceDataItemRawPoList)) {
            List<Long> prodRawIdList = produceDataItemRawPoList.stream()
                    .map(ProduceDataItemRawPo::getProduceDataItemRawId)
                    .collect(Collectors.toList());
            prodRawComparePoList = prodRawCompareDao.listByProdRawIdList(prodRawIdList);
        }

        // 获取产品名称
        List<String> querySkuList = Lists.newArrayList();

        List<String> skuRawList = produceDataItemRawPoList.stream()
                .map(ProduceDataItemRawPo::getSku)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(skuRawList)) {
            querySkuList.addAll(skuRawList);
        }

        List<String> skuComList = prodRawComparePoList.stream()
                .map(ProduceDataItemRawComparePo::getSku).filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(skuComList)) {
            querySkuList.addAll(skuComList);
        }

        Map<String, String> skuEncodeMapByRaw = plmRemoteService.getSkuEncodeMapBySku(querySkuList);
        Map<Long, List<ProduceDataItemRawPo>> produceDataItemRawPoMap = produceDataItemRawPoList
                .stream()
                .collect(Collectors.groupingBy(ProduceDataItemRawPo::getProduceDataItemId));


        //获取工序和描述
        List<ProduceDataItemProcessPo> produceDataItemProcessPoList = produceDataItemProcessDao.getListBySku(plmSkuPo.getSku());
        Map<Long, List<ProduceDataItemProcessPo>> produceDataItemProcessPoMap = produceDataItemProcessPoList.stream()
                .collect(Collectors.groupingBy(ProduceDataItemProcessPo::getProduceDataItemId));
        List<ProduceDataItemProcessDescPo> produceDataItemProcessDescPoList = produceDataItemProcessDescDao.getListBySku(plmSkuPo.getSku());
        Map<Long, List<ProduceDataItemProcessDescPo>> produceDataItemProcessDescPoMap = produceDataItemProcessDescPoList.stream()
                .collect(Collectors.groupingBy(ProduceDataItemProcessDescPo::getProduceDataItemId));

        //获取图片
        Map<Long, List<String>> produceDataItemEffectMap = new HashMap<>();
        Map<Long, List<String>> produceDataItemDetailMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(produceDataItemIdList)) {
            produceDataItemEffectMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.PRODUCE_DATA_ITEM_EFFECT, produceDataItemIdList);
            produceDataItemDetailMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.PRODUCE_DATA_ITEM_DETAIL, produceDataItemIdList);
        }

        // 获取关联供应商
        List<ProduceDataItemSupplierPo> produceDataItemSupplierPoList = produceDataItemSupplierDao.getByProduceDataItemIdList(produceDataItemIdList);

        produceDataItemDetailVo.setProduceDataItemInfoList(SkuProdConverter.itemPoToVo(produceDataItemPoList,
                produceDataItemRawPoMap,
                produceDataItemProcessPoMap,
                produceDataItemProcessDescPoMap,
                produceDataItemEffectMap,
                produceDataItemDetailMap,
                skuEncodeMapByRaw,
                produceDataItemSupplierPoList,
                prodRawComparePoList
        ));

        return produceDataItemDetailVo;

    }

    /**
     * 更新原料&工序信息信息
     *
     * @param dto:
     * @author ChenWenLong
     * @date 2024/9/27 10:11
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateProduceDataItem(UpdateProduceDataItemDto dto) {
        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(dto.getSku());
        Assert.notNull(plmSkuPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新列表页面后重试！"));
        String sku = plmSkuPo.getSku();
        Map<String, String> spuMapBySku = plmRemoteService.getSpuMapBySkuList(List.of(sku));
        String spu = spuMapBySku.get(sku);
        if (StringUtils.isBlank(spu)) {
            throw new BizException("SKU:{}获取不到对应的PLM的SPU，请联系系统管理员！", sku);
        }

        // 创建生产信息
        BooleanType rawManage = dto.getRawManage();
        ProduceDataPoCreateBo produceDataPoCreateBo = new ProduceDataPoCreateBo();
        produceDataPoCreateBo.setSpu(spu);
        produceDataPoCreateBo.setSku(sku);
        produceDataPoCreateBo.setRawManage(rawManage);
        skuProdBaseService.insertOrUpdateProduceDataPo(produceDataPoCreateBo);

        // 验证原料入参是否存在重复
        if (CollectionUtils.isNotEmpty(dto.getProduceDataItemInfoList())) {
            for (ProduceDataItemInfoDto produceDataItemInfoDto : dto.getProduceDataItemInfoList()) {
                Set<String> skuRawSet = new HashSet<>();
                Optional.ofNullable(produceDataItemInfoDto.getProduceDataItemRawInfoList())
                        .orElse(new ArrayList<>())
                        .forEach(produceDataItemRaw -> {
                            if (skuRawSet.contains(produceDataItemRaw.getSku())) {
                                throw new BizException("原料列表禁止添加的重复的原料sku：{}", produceDataItemRaw.getSku());
                            } else {
                                skuRawSet.add(produceDataItemRaw.getSku());
                            }
                        });
            }
        }

        // 处理BOM的信息
        List<ProduceDataItemInfoDto> produceDataItemDtoList = Optional.ofNullable(dto.getProduceDataItemInfoList())
                .orElse(new ArrayList<>());
        List<Long> produceDataItemDtoIdList = produceDataItemDtoList.stream()
                .map(ProduceDataItemInfoDto::getProduceDataItemId)
                .collect(Collectors.toList());

        // 查询已存在BOM信息
        List<ProduceDataItemPo> produceDataItemPoList = produceDataItemDao.getListBySku(sku);

        // 验证BOM的版本号
        if (CollectionUtils.isNotEmpty(produceDataItemDtoList)) {
            // 判断版本号是否一致
            for (ProduceDataItemInfoDto produceDataItemInfoDto : produceDataItemDtoList) {
                produceDataItemPoList.stream()
                        .filter(produceDataItemPo -> produceDataItemPo.getProduceDataItemId().equals(produceDataItemInfoDto.getProduceDataItemId()))
                        .findFirst()
                        .ifPresent(produceDataItemPo ->
                                Assert.isTrue(produceDataItemPo.getVersion().equals(produceDataItemInfoDto.getVersion()),
                                        () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！")));

                // 新增重新赋值ID
                if (produceDataItemInfoDto.getProduceDataItemId() == null) {
                    produceDataItemInfoDto.setProduceDataItemId(idGenerateService.getSnowflakeId());
                }
            }

        }

        // BOM的信息保存
        skuProdBaseService.produceDataItemSave(produceDataItemDtoList, produceDataItemPoList, spu, sku);

        // 生产信息详情效果图、细节图的保存
        skuProdBaseService.produceDataItemEffectDetailSave(produceDataItemDtoList, produceDataItemDtoIdList);

    }

    public SkuProdDetailVo scmAttrDetail(GetSkuProductionDto dto) {
        return skuProdBaseService.attrDetail(dto);
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.UPDATE_SKU_PRODUCTION_ATTR, key = "#dto.sku",
            waitTime = 1, leaseTime = -1, exceptionDesc = "生产属性信息处理中，请稍后重试")
    public void updateScmAttr(UpdateSkuProductionDto dto) {
        dto.validate();
        skuProdBaseService.updateAttr(dto);
    }

    /**
     * 规格书详情
     *
     * @param dto:
     * @return SpecBookDetailVo
     * @author ChenWenLong
     * @date 2024/9/29 16:04
     */
    public SpecBookDetailVo getSpecBook(SkuDto dto) {
        String sku = dto.getSku();

        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(sku);
        Assert.notNull(plmSkuPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新列表页面后重试！"));

        // 获取供应链属性
        GetSkuProductionDto queryScmDetailParam = new GetSkuProductionDto();
        queryScmDetailParam.setSku(plmSkuPo.getSku());
        SkuProdDetailVo skuProdDetailVo = skuProdBaseService.attrDetail(queryScmDetailParam);
        List<SupSkuProdInfoVo> supSkuProdInfoVoList = Optional.ofNullable(skuProdDetailVo.getSupSkuProdInfoVoList())
                .orElse(Collections.emptyList());

        //商品属性
        SpecBookDetailVo specBookDetailVo = new SpecBookDetailVo();
        List<SkuAttributeInfoVo> skuAttributeInfoVoList = skuProdDetailVo.getSkuAttributeInfoVoList();
        specBookDetailVo.setSkuAttributeInfoVoList(skuAttributeInfoVoList);

        //规格书关联信息
        List<SpecBookRelateBo> specBookRelateBoList = skuProdBaseService.getSpecBookRelateBoList(sku);
        List<SpecBookDetailVo.SpecBookItemVo> specBookItemList = specBookRelateBoList.stream().map(specBookRelateBo -> {
            String supplierCode = specBookRelateBo.getSupplierCode();

            SpecBookDetailVo.SpecBookItemVo specBookItemVo = new SpecBookDetailVo.SpecBookItemVo();
            specBookItemVo.setSku(specBookRelateBo.getSku());
            specBookItemVo.setSupplierCode(supplierCode);
            specBookItemVo.setSpu(specBookRelateBo.getSpu());
            specBookItemVo.setSkuEncode(specBookRelateBo.getSkuEncode());
            specBookItemVo.setSpecPlatformVoList(specBookRelateBo.getSpecPlatformVoList());
            specBookItemVo.setLoginUsername(specBookRelateBo.getLoginUsername());
            specBookItemVo.setFileCodeList(specBookRelateBo.getFileCodeList());

            supSkuProdInfoVoList.stream()
                    .filter(supSkuProdInfo -> Objects.equals(supSkuProdInfo.getSupplierCode(), supplierCode))
                    .findFirst().ifPresent(supSkuProdInfoVo -> {
                        specBookItemVo.setSupSkuMaterialAttrDetailVoList(supSkuProdInfoVo.getSupSkuMaterialAttrDetailVoList());
                        specBookItemVo.setSupSkuCraftAttrDetailVoList(supSkuProdInfoVo.getSupSkuCraftAttrDetailVoList());
                        specBookItemVo.setSupSkuAttributeInfoVoList(supSkuProdInfoVo.getSupSkuAttributeInfoVoList());
                    });
            return specBookItemVo;
        }).collect(Collectors.toList());
        specBookDetailVo.setSpecBookItemList(specBookItemList);
        return specBookDetailVo;
    }

    /**
     * 商品信息列表
     *
     * @param dto:
     * @return PageInfo<PlmSkuSearchVo>
     * @author ChenWenLong
     * @date 2024/9/29 18:13
     */
    public CommonPageResult.PageInfo<PlmSkuSearchVo> searchPlmSku(PlmSkuSearchDto dto) {
        //条件过滤
        if (null == skuProdBaseService.getSearchPlmSkuWhere(dto)) {
            return new CommonPageResult.PageInfo<>();
        }
        CommonPageResult.PageInfo<PlmSkuSearchVo> pageResult = plmSkuDao.searchPlmSku(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<PlmSkuSearchVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new CommonPageResult.PageInfo<>();
        }

        List<String> skuList = records.stream()
                .map(PlmSkuSearchVo::getSku)
                .collect(Collectors.toList());

        // 查询PLM的商品类目名称
        final Map<String, PlmCategoryVo> categoriesVoMap = plmRemoteService.getCategoriesInfoBySku(skuList);

        // 获取图片
        Map<String, List<String>> skuSaleFileCodeMap = skuProdBaseService.getSkuSaleFileCodeList(skuList);

        //查询sku在售平台
        final List<PlmNormalSkuVo> plmNormalSkuVoList = plmRemoteService.getSkuInfoBySkuList(skuList, null);

        // 查询质量风险
        Map<String, SkuRisk> skuRiskBySkuMap = attributeBaseService.listBySkuList(skuList)
                .stream()
                .filter(Objects::nonNull)
                .filter(skuRiskBo -> Objects.nonNull(skuRiskBo.getSku()) && Objects.nonNull(skuRiskBo.getLevel()))
                .collect(Collectors.toMap(SkuRiskBo::getSku, SkuRiskBo::getLevel));

        // 获取单件产能
        Map<String, SkuInfoPo> singleCapacityMap = skuInfoDao.getMapBySkuList(skuList);

        // 获取绑定供应商产品对照关系
        List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getListByBatchSku(skuList);

        // 获取采购最新价格
        List<PurchaseLatestPriceItemBo> purchasePriceBySkuList = purchaseChildOrderDao.getPurchasePriceBySkuList(skuList);

        // 组装数据
        for (PlmSkuSearchVo record : records) {
            // 转换数据
            SkuProdConverter.searchToPlmSkuSearchVo(record,
                    categoriesVoMap,
                    skuSaleFileCodeMap,
                    plmNormalSkuVoList,
                    skuRiskBySkuMap,
                    singleCapacityMap,
                    supplierProductComparePoList,
                    purchasePriceBySkuList);
        }

        return pageResult;
    }

    public List<AttributeInfoVo> getMaintainableAttrList(GetMaintainableAttrDto dto) {
        Set<Long> maintainableAttrIds = new HashSet<>();

        String sku = dto.getSku();
        List<Long> skuAttrIds = attrRefService.getMaintainableAttrIds(sku, AttributeScope.GOODS);
        if (CollectionUtils.isNotEmpty(skuAttrIds)) {
            maintainableAttrIds.addAll(skuAttrIds);
        }
        List<Long> supAttrIds = attrRefService.getMaintainableAttrIds(sku, AttributeScope.SUPPLIER);
        if (CollectionUtils.isNotEmpty(supAttrIds)) {
            maintainableAttrIds.addAll(supAttrIds);
        }
        return attrRefService.listByAttrIds(new ArrayList<>(maintainableAttrIds));
    }

    public SkuRiskLogVo getSkuRiskLog(GetSkuRiskLogDto dto) {
        SkuRiskLogVo skuRiskLogVo = new SkuRiskLogVo();
        String sku = dto.getSku();

        List<SkuRiskPo> skuRiskPoList = skuRiskDao.listBySkuList(Collections.singletonList(sku));
        if (CollectionUtils.isEmpty(skuRiskPoList)) {
            return skuRiskLogVo;
        }
        SkuRiskPo skuRiskPo = skuRiskPoList.stream().max(Comparator.comparing(SkuRiskPo::getCreateTime)).orElse(null);
        if (Objects.isNull(skuRiskPo)) {
            return skuRiskLogVo;
        }

        SkuRisk skuRisk = skuRiskPo.getLevel();
        skuRiskLogVo.setSkuRisk(skuRisk);

        Long skuRiskId = skuRiskPo.getSkuRiskId();
        List<SkuRiskLogPo> skuRiskLogPoList = skuRiskLogDao.listBySkuRiskId(skuRiskId);
        if (CollectionUtils.isEmpty(skuRiskLogPoList)) {
            return skuRiskLogVo;
        }

        //根据attributeId+attributeOptionId分组,求和分数
        Map<String, BigDecimal> attrScoreMap = skuRiskLogPoList.stream()
                .collect(Collectors.groupingBy(item -> item.getAttributeId() + "+" + item.getAttributeOptionId(),
                        Collectors.reducing(BigDecimal.ZERO, SkuRiskLogPo::getScore, BigDecimal::add)));

        List<SkuRiskLogVo.RiskLogItemVo> riskLogItemVoList = attrScoreMap.entrySet().stream()
                .map(entry -> {
                    // 将 attributeId 和 attributeOptionId 分离
                    String[] keys = entry.getKey().split("\\+");
                    Long attributeId = Long.valueOf(keys[0]);
                    Long attributeOptionId = Long.valueOf(keys[1]);
                    BigDecimal totalScore = entry.getValue();

                    // 创建新的 SkuRiskLogPo 对象
                    SkuRiskLogVo.RiskLogItemVo riskLogItemVo = new SkuRiskLogVo.RiskLogItemVo();
                    riskLogItemVo.setAttrId(attributeId);

                    String attrName = skuRiskLogPoList.stream()
                            .filter(skuRiskLogPo -> Objects.equals(skuRiskLogPo.getAttributeId(), attributeId))
                            .map(SkuRiskLogPo::getAttributeName).findFirst().orElse("");
                    riskLogItemVo.setAttrName(attrName);

                    riskLogItemVo.setAttrOptionId(attributeOptionId);
                    String attrOptName = skuRiskLogPoList.stream()
                            .filter(skuRiskLogPo -> Objects.equals(skuRiskLogPo.getAttributeOptionId(), attributeOptionId))
                            .map(SkuRiskLogPo::getAttributeOptionValue).findFirst().orElse("");
                    riskLogItemVo.setAttributeValue(attrOptName);

                    riskLogItemVo.setScore(totalScore);

                    BigDecimal coefficient = skuRiskLogPoList.
                            stream().filter(skuRiskLogPo -> Objects.equals(skuRiskLogPo.getAttributeId(), attributeId))
                            .map(SkuRiskLogPo::getCoefficient).findFirst().orElse(BigDecimal.ZERO);
                    riskLogItemVo.setCoefficient(coefficient);
                    return riskLogItemVo;
                })
                .collect(Collectors.toList());
        skuRiskLogVo.setRiskLogItemVoList(riskLogItemVoList);

        BigDecimal totalScore = riskLogItemVoList.stream().map(SkuRiskLogVo.RiskLogItemVo::getScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        skuRiskLogVo.setTotalScore(totalScore);
        return skuRiskLogVo;
    }

    /**
     * 查询商品对照关系详情
     *
     * @param dto:
     * @return SupplierProductCompareDetailVo
     * @author ChenWenLong
     * @date 2024/10/14 10:47
     */
    public SupplierProductCompareInfoVo getSupplierProductCompareDetail(SupplierProductCompareDetailDto dto) {
        PlmSkuPo plmSkuPo = plmSkuDao.getBySku(dto.getSku());
        if (plmSkuPo == null) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }
        List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getBySku(dto.getSku());
        SupplierProductCompareInfoVo detailVo = new SupplierProductCompareInfoVo();

        List<String> skuList = new ArrayList<>();
        skuList.add(plmSkuPo.getSku());
        List<PlmGoodsDetailVo> goodsSkuList = plmRemoteService.getGoodsDetail(skuList);
        if (CollectionUtils.isNotEmpty(goodsSkuList)) {
            detailVo.setCategoryList(goodsSkuList.get(0).getCategoryList());
        }

        Map<String, String> skuMap = plmRemoteService.getSkuEncodeBySku(skuList).stream().collect(Collectors.toMap(PlmSkuVo::getSkuCode, PlmSkuVo::getSkuEncode));
        detailVo.setSkuEncode(skuMap.get(detailVo.getSku()));
        detailVo.setPlmSkuId(plmSkuPo.getPlmSkuId());
        detailVo.setSku(plmSkuPo.getSku());
        detailVo.setVersion(plmSkuPo.getVersion());
        detailVo.setCycle(plmSkuPo.getCycle());

        // 获取单件产能
        Map<String, BigDecimal> singleCapacityMap = skuInfoDao.getListBySkuList(skuList).stream()
                .collect(Collectors.toMap(SkuInfoPo::getSku, SkuInfoPo::getSingleCapacity));
        detailVo.setSingleCapacity(BigDecimal.ZERO);
        if (singleCapacityMap.containsKey(detailVo.getSku())) {
            detailVo.setSingleCapacity(singleCapacityMap.get(detailVo.getSku()));
        }

        List<SupplierProductCompareVo> list = new ArrayList<>();
        for (SupplierProductComparePo supplierProductComparePo : supplierProductComparePoList) {
            SupplierProductCompareVo supplierProductCompareVo = new SupplierProductCompareVo();
            supplierProductCompareVo.setSupplierProductCompareId(supplierProductComparePo.getSupplierProductCompareId());
            supplierProductCompareVo.setSupplierProductName(supplierProductComparePo.getSupplierProductName());
            supplierProductCompareVo.setSupplierCode(supplierProductComparePo.getSupplierCode());
            supplierProductCompareVo.setVersion(supplierProductComparePo.getVersion());
            supplierProductCompareVo.setSku(supplierProductComparePo.getSku());
            supplierProductCompareVo.setSupplierProductCompareStatus(supplierProductComparePo.getSupplierProductCompareStatus());
            list.add(supplierProductCompareVo);
        }
        detailVo.setSupplierProductCompareList(list);
        return detailVo;

    }

    /**
     * 确认提交对照关系
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/10/14 10:55
     */
    @Transactional(rollbackFor = Exception.class)
    public void editSupplierProductCompare(SupplierProductCompareUpdateDto dto) {

        PlmSkuPo plmSkuPo = plmSkuDao.getByIdVersion(dto.getPlmSkuId(), dto.getVersion());
        if (plmSkuPo == null) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }
        String user = GlobalContext.getUserKey();
        String userName = GlobalContext.getUsername();

        // 供应商库存信息
        List<SupplierInventoryBatchUpdateBo> supplierInventoryBatchUpdateBoList = new ArrayList<>();

        //当绑定供应商产品对照列表不为空时
        if (CollectionUtils.isNotEmpty(dto.getSupplierProductCompareList())) {
            List<String> supplierCodeList = dto.getSupplierProductCompareList().stream()
                    .map(SupplierProductCompareItemDto::getSupplierCode).distinct().collect(Collectors.toList());
            if (supplierCodeList.size() != dto.getSupplierProductCompareList().size()) {
                throw new ParamIllegalException("禁止重复添加供应商");
            }
            Map<String, SupplierPo> supplierMap = supplierDao.getSupplierMapBySupplierCodeList(supplierCodeList);
            List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getBySku(plmSkuPo.getSku());
            List<SupplierProductComparePo> newSupplierProductComparePoList = SupplierProductCompareConverter.itemDtoToPo(dto.getSupplierProductCompareList());

            CompareResult<SupplierProductComparePo> itemResult = DataCompareUtil.compare(newSupplierProductComparePoList, supplierProductComparePoList, SupplierProductComparePo::getSupplierProductCompareId);
            List<SupplierProductComparePo> collect = itemResult.getNewItems().stream().peek(item -> {
                SupplierPo supplierPo = supplierMap.get(item.getSupplierCode());

                // 供应商库存信息组装
                SupplierInventoryBatchUpdateBo supplierInventoryBatchUpdateBo = new SupplierInventoryBatchUpdateBo();
                supplierInventoryBatchUpdateBo.setSku(plmSkuPo.getSku());
                supplierInventoryBatchUpdateBo.setSupplierCode(item.getSupplierCode());

                if (null != supplierPo) {
                    item.setSupplierName(supplierPo.getSupplierName());
                    supplierInventoryBatchUpdateBo.setSupplierName(supplierPo.getSupplierName());
                }
                supplierInventoryBatchUpdateBoList.add(supplierInventoryBatchUpdateBo);

                item.setSku(plmSkuPo.getSku());
                item.setCreateUser(user);
                item.setCreateUsername(userName);
                item.setUpdateUser(user);
                item.setUpdateUsername(userName);
                item.setSupplierProductCompareStatus(BooleanType.TRUE);
            }).collect(Collectors.toList());
            supplierProductCompareDao.insertBatch(collect);
            // 编辑时数据处理
            if (CollectionUtils.isNotEmpty(itemResult.getExistingItems())) {
                for (SupplierProductComparePo existingItem : itemResult.getExistingItems()) {
                    // 供应商库存信息组装
                    SupplierInventoryBatchUpdateBo supplierInventoryBatchUpdateBo = new SupplierInventoryBatchUpdateBo();
                    supplierInventoryBatchUpdateBo.setSku(plmSkuPo.getSku());
                    supplierInventoryBatchUpdateBo.setSupplierCode(existingItem.getSupplierCode());
                    SupplierPo supplierPo = supplierMap.get(existingItem.getSupplierCode());
                    if (null != supplierPo) {
                        existingItem.setSupplierName(supplierPo.getSupplierName());
                        supplierInventoryBatchUpdateBo.setSupplierName(supplierPo.getSupplierName());
                    }
                    supplierInventoryBatchUpdateBoList.add(supplierInventoryBatchUpdateBo);
                    Assert.notNull(existingItem.getSupplierProductCompareStatus(), () -> new BizException("编辑的关联供应商：{}入参需要传状态字段，请联系系统管理员！", existingItem.getSupplierCode()));

                }
            }
            supplierProductCompareDao.updateBatchByIdVersion(itemResult.getExistingItems());
            supplierProductCompareDao.removeBatchByIds(itemResult.getDeletedItems());
            plmSkuPo.setBindingSupplierProduct(BindingSupplierProduct.TRUE);
        }

        plmSkuPo.setCycle(dto.getCycle());
        plmSkuDao.updateByIdVersion(plmSkuPo);

        // 增加时执行初始供应商库存信息
        supplierProductCompareBaseService.supplierInventoryUpdateBatch(supplierInventoryBatchUpdateBoList);

        // 更新单价产能
        skuBaseService.saveSkuInfoSingleCapacity(plmSkuPo.getSku(), dto.getSingleCapacity());

    }


    @Transactional(rollbackFor = Exception.class)
    public void exportPlmSku(PlmSkuSearchDto dto) {
        ParamValidUtils.requireNotNull(skuProdBaseService.getSearchPlmSkuWhere(dto), "导出数据为空");

        Integer exportTotals = plmSkuDao.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        consistencySendMqService.execSendMq(ScmExportHandler.class,
                new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(), FileOperateBizType.SCM_SKU_PROD_SKU_EXPORT.getCode(), dto));
    }

    public Integer getExportTotals(PlmSkuSearchDto dto) {
        if (null == skuProdBaseService.getSearchPlmSkuWhere(dto)) {
            return 0;
        }
        Integer exportTotals = plmSkuDao.getExportTotals(dto);
        if (Objects.isNull(exportTotals)) {
            exportTotals = 0;
        }
        return exportTotals;
    }

    public CommonResult<ExportationListResultBo<SkuProdSkuExportVo>> getExportList(PlmSkuSearchDto dto) {
        ExportationListResultBo<SkuProdSkuExportVo> resultBo = new ExportationListResultBo<>();
        if (null == skuProdBaseService.getSearchPlmSkuWhere(dto)) {
            return CommonResult.success(resultBo);
        }

        CommonPageResult.PageInfo<SkuProdSkuExportVo> pageInfo = plmSkuDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);

        final List<SkuProdSkuExportVo> records = pageInfo.getRecords();
        List<String> skuList = records.stream()
                .map(SkuProdSkuExportVo::getSku)
                .collect(Collectors.toList());
        //获取生产资料-销售属性
        List<ProduceDataExportSkuAttrVo> prodDataAttrExportVoList = skuProdBaseService.getProduceDataExportSkuAttrVoList(skuList);

        // 查询PLM的二级商品类目名称
        Map<String, String> skuCategoryNameMap = plmRemoteService.getCategoryEnNameBySkuList(skuList, 2);

        //查询sku在售平台
        final List<PlmNormalSkuVo> plmNormalSkuVoList = plmRemoteService.getSkuInfoBySkuList(skuList, null);
        // 查询质量风险
        Map<String, SkuRisk> skuRiskBySkuMap = attributeBaseService.listBySkuList(skuList)
                .stream()
                .filter(Objects::nonNull)
                .filter(skuRiskBo -> Objects.nonNull(skuRiskBo.getSku()) && Objects.nonNull(skuRiskBo.getLevel()))
                .collect(Collectors.toMap(SkuRiskBo::getSku, SkuRiskBo::getLevel));
        final Map<String, PlmNormalSkuVo> skuPlmNormalSkuVoMap = plmNormalSkuVoList.stream()
                .collect(Collectors.toMap(PlmNormalSkuVo::getSkuCode, Function.identity(), (item1, item2) -> item1));

        // 获取单件产能
        Map<String, SkuInfoPo> singleCapacityMap = skuInfoDao.getMapBySkuList(skuList);
        for (SkuProdSkuExportVo record : records) {
            final PlmNormalSkuVo plmNormalSkuVo = skuPlmNormalSkuVoMap.get(record.getSku());
            if (null != plmNormalSkuVo) {
                record.setSkuEncode(plmNormalSkuVo.getSkuEncode());
                record.setSkuDevType(plmNormalSkuVo.getSkuDevType().getRemark());
                String platName = Optional.ofNullable(plmNormalSkuVo.getGoodsPlatVoList())
                        .orElse(new ArrayList<>())
                        .stream()
                        .map(PlmGoodsPlatVo::getPlatName)
                        .distinct()
                        .collect(Collectors.joining(","));
                record.setPlatName(platName);
                if (StringUtils.isNotBlank(platName)) {
                    record.setIsSale(BooleanType.TRUE.getValue());
                } else {
                    record.setIsSale(BooleanType.FALSE.getValue());
                }
            }

            final SkuRisk skuRisk = skuRiskBySkuMap.get(record.getSku());
            if (null != skuRisk) {
                record.setSkuRisk(skuRisk.getRemark());
            }
            SkuInfoPo skuInfoPo = singleCapacityMap.get(record.getSku());
            if (null != skuInfoPo) {
                record.setSingleCapacity(skuInfoPo.getSingleCapacity());
            }

            record.setCategoryName(skuCategoryNameMap.get(record.getSku()));

            //生产资料-销售属性
            prodDataAttrExportVoList.stream()
                    .filter(prodDataExportSkuAttrVo -> Objects.equals(prodDataExportSkuAttrVo.getSku(), record.getSku()))
                    .findFirst().ifPresent(prodDataExportSkuAttrVo -> {
                        record.setWeight(prodDataExportSkuAttrVo.getWeight());
                        record.setTolerance(prodDataExportSkuAttrVo.getTolerance());

                        record.setColor(prodDataExportSkuAttrVo.getColor());
                        record.setLaceArea(prodDataExportSkuAttrVo.getLaceArea());
                        record.setFileLengthSize(prodDataExportSkuAttrVo.getFileLengthSize());
                        record.setCompleteLongSize(prodDataExportSkuAttrVo.getCompleteLongSize());
                        record.setNetCapSize(prodDataExportSkuAttrVo.getNetCapSize());
                        record.setPartedBangs(prodDataExportSkuAttrVo.getPartedBangs());
                        record.setParting(prodDataExportSkuAttrVo.getParting());
                        record.setMaterial(prodDataExportSkuAttrVo.getMaterial());
                        record.setContour(prodDataExportSkuAttrVo.getContour());
                        record.setColorSystem(prodDataExportSkuAttrVo.getColorSystem());
                        record.setColorMixPartition(prodDataExportSkuAttrVo.getColorMixPartition());
                        record.setLeftSideLength(prodDataExportSkuAttrVo.getLeftSideLength());
                        record.setLeftFinish(prodDataExportSkuAttrVo.getLeftFinish());
                        record.setRightSideLength(prodDataExportSkuAttrVo.getRightSideLength());
                        record.setRightFinish(prodDataExportSkuAttrVo.getRightFinish());
                        record.setSymmetry(prodDataExportSkuAttrVo.getSymmetry());
                        record.setPreselectionLace(prodDataExportSkuAttrVo.getPreselectionLace());
                    });
        }

        resultBo.setRowDataList(records);
        return CommonResult.success(resultBo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportPlmSkuCompare(PlmSkuSearchDto dto) {
        ParamValidUtils.requireNotNull(skuProdBaseService.getSearchPlmSkuWhere(dto), "导出数据为空");

        Integer exportTotals = plmSkuDao.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        consistencySendMqService.execSendMq(ScmExportHandler.class,
                new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                        FileOperateBizType.SCM_SKU_PROD_SKU_COMPARE_EXPORT.getCode(), dto));
    }

    public CommonResult<ExportationListResultBo<SkuProdSkuCompareExportVo>> getSkuCompareExportList(PlmSkuSearchDto dto) {
        ExportationListResultBo<SkuProdSkuCompareExportVo> resultBo = new ExportationListResultBo<>();
        if (null == skuProdBaseService.getSearchPlmSkuWhere(dto)) {
            return CommonResult.success(resultBo);
        }

        CommonPageResult.PageInfo<SkuProdSkuExportVo> pageInfo = plmSkuDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);

        final List<SkuProdSkuExportVo> records = pageInfo.getRecords();
        final Map<String, SkuProdSkuExportVo> skuExportVoMap = records.stream()
                .collect(Collectors.toMap(SkuProdSkuExportVo::getSku, Function.identity(),
                        (item1, item2) -> item1));

        List<String> skuList = records.stream()
                .map(SkuProdSkuExportVo::getSku)
                .collect(Collectors.toList());

        // 查询PLM的商品类目名称
        final Map<String, PlmCategoryVo> categoriesVoMap = plmRemoteService.getCategoriesInfoBySku(skuList);
        //查询sku在售平台
        final List<PlmNormalSkuVo> plmNormalSkuVoList = plmRemoteService.getSkuInfoBySkuList(skuList, null);
        final Map<String, PlmNormalSkuVo> skuPlmNormalSkuVoMap = plmNormalSkuVoList.stream()
                .collect(Collectors.toMap(PlmNormalSkuVo::getSkuCode, Function.identity(), (item1, item2) -> item1));

        // 获取绑定供应商产品对照关系
        List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getListByBatchSku(skuList);
        final Map<String, List<SupplierProductComparePo>> skuComparePoListMap = supplierProductComparePoList.stream()
                .collect(Collectors.groupingBy(SupplierProductComparePo::getSku));
        // 获取采购最新价格
        List<PurchaseLatestPriceItemBo> purchasePriceBySkuBoList = purchaseChildOrderDao.getPurchasePriceBySkuList(skuList);
        final Map<String, BigDecimal> skuSupplierCodePriceMap = purchasePriceBySkuBoList.stream()
                .filter(bo -> bo.getConfirmTime() != null)
                .collect(Collectors.toMap(bo -> bo.getSku() + bo.getSupplierCode(), PurchaseLatestPriceItemBo::getPurchasePrice,
                        (item1, item2) -> item2));

        final List<SkuProdSkuCompareExportVo> resultList = new ArrayList<>();
        for (SkuProdSkuExportVo record : records) {
            final List<SupplierProductComparePo> supplierProductComparePoList1 = skuComparePoListMap.get(record.getSku());
            final PlmNormalSkuVo plmNormalSkuVo = skuPlmNormalSkuVoMap.get(record.getSku());

            if (CollectionUtils.isEmpty(supplierProductComparePoList1)) {
                final SkuProdSkuCompareExportVo skuProdSkuCompareExportVo = new SkuProdSkuCompareExportVo();
                skuProdSkuCompareExportVo.setSku(record.getSku());
                if (null != plmNormalSkuVo) {
                    skuProdSkuCompareExportVo.setSkuEncode(plmNormalSkuVo.getSkuEncode());
                }
                skuProdSkuCompareExportVo.setSpu(record.getSpu());
                PlmCategoryVo plmCategoryVo = categoriesVoMap.get(record.getSku());
                if (null != plmCategoryVo) {
                    skuProdSkuCompareExportVo.setCategoryName(plmCategoryVo.getCategoryNameCn());
                }
                resultList.add(skuProdSkuCompareExportVo);
            } else {
                final List<SkuProdSkuCompareExportVo> skuProdSkuCompareExportVoList = supplierProductComparePoList1.stream()
                        .map(po -> {
                            final SkuProdSkuCompareExportVo skuProdSkuCompareExportVo = new SkuProdSkuCompareExportVo();
                            skuProdSkuCompareExportVo.setSku(record.getSku());
                            if (null != plmNormalSkuVo) {
                                skuProdSkuCompareExportVo.setSkuEncode(plmNormalSkuVo.getSkuEncode());
                            }
                            skuProdSkuCompareExportVo.setSpu(record.getSpu());
                            PlmCategoryVo plmCategoryVo = categoriesVoMap.get(record.getSku());
                            if (null != plmCategoryVo) {
                                skuProdSkuCompareExportVo.setCategoryName(plmCategoryVo.getCategoryNameCn());
                            }
                            skuProdSkuCompareExportVo.setSupplierCode(po.getSupplierCode());
                            skuProdSkuCompareExportVo.setSupplierProductName(po.getSupplierProductName());
                            skuProdSkuCompareExportVo.setGoodsPurchasePrice(skuSupplierCodePriceMap.get(po.getSku() + po.getSupplierCode()));

                            return skuProdSkuCompareExportVo;
                        }).collect(Collectors.toList());
                resultList.addAll(skuProdSkuCompareExportVoList);
            }
        }
        resultBo.setRowDataList(resultList);
        return CommonResult.success(resultBo);
    }

    /**
     * @Description 商品信息导入
     * @author yanjiawei
     * @Date 2024/11/20 17:32
     */
    @Transactional(rollbackFor = Exception.class)
    public void importProdDataAttr(ProduceDataAttrImportDto dto) {
        //字段校验
        String sku = ParamValidUtils.requireNotBlank(dto.getSku(), "sku不能为空！请校验后导入");
        sku = sku.trim();
        verifyProdDataAttrFields(dto);

        //更新生产周期
        String cycleStr = dto.getCycle();
        doImportSkuCycle(sku, cycleStr);

        //更新单件产能
        String singleCapacityStr = dto.getSingleCapacity();
        doImportSkuSingleCapacity(sku, singleCapacityStr);

        //更新克重
        String weightStr = dto.getWeight();
        doImportSkuWeight(sku, weightStr);

        //更新公差
        String toleranceStr = dto.getTolerance();
        doImportSkuTolerance(sku, toleranceStr);

        //更新原料需管理
        String rawManageStr = dto.getRawManage();
        doImportSkuRawManage(sku, rawManageStr);

        //更新商品采购价格
        String goodsPurchasePriceStr = dto.getGoodsPurchasePrice();
        doImportSkuGoodsPurchasePrice(sku, goodsPurchasePriceStr);
        doRefreshSkuGoodsPurchasePrice(sku);

        //导入的销售属性列表
        List<ProdDataAttrImportDto> importAttrDtoList = ProdBuilder.buildProdDataAttrImportDtoList(dto);
        PlmSkuPo plmSkuPo
                = ParamValidUtils.requireNotNull(plmSkuDao.getBySku(sku), "该sku不存在！请校验/同步后导入");
        String spu = plmSkuPo.getSpu();
        doImportProduceDataAttr(sku, spu, importAttrDtoList);
    }

    public void doImportSkuRawManage(String sku, String rawManageStr) {
        ParamValidUtils.requireNotBlank(sku, "sku不能为空！请校验后导入");
        if (StringUtils.isBlank(rawManageStr)) {
            log.info("sku=>{}原料需管理为空，不更新！", sku);
            return;
        }

        Map<String, BooleanType> rawManageMap = Map.of("是", BooleanType.TRUE, "否", BooleanType.FALSE);
        BooleanType rawManage
                = ParamValidUtils.requireNotNull(rawManageMap.get(rawManageStr),
                StrUtil.format("原料需管理请填写{}！请校验后导入。", String.join("/", rawManageMap.keySet())));

        ProduceDataPo produceDataPo
                = ParamValidUtils.requireNotNull(produceDataDao.getBySku(sku), "该sku不存在！请校验后导入");
        produceDataPo.setRawManage(rawManage);
        produceDataDao.updateByIdVersion(produceDataPo);
    }

    public void doImportSkuTolerance(String sku, String toleranceStr) {
        ParamValidUtils.requireNotBlank(sku, "sku不能为空！请校验后导入");
        if (StringUtils.isBlank(toleranceStr)) {
            log.info("sku=>{}公差为空，不更新！", sku);
            return;
        }

        BigDecimal tolerance;
        if (StringUtils.isNotBlank(toleranceStr)) {
            try {
                tolerance = new BigDecimal(toleranceStr);
            } catch (Exception e) {
                throw new ParamIllegalException("重量格式不正确！请校验后导入。");
            }
            ParamValidUtils.requireGreaterThanOrEqual(tolerance, BigDecimal.ZERO, "公差必须大于等于0！请校验后导入。");
            ParamValidUtils.requireLessThanOrEqual(tolerance.precision() - tolerance.scale(), 8, "公差整数最多8位！请校验后导入。");
            ParamValidUtils.requireLessThanOrEqual(tolerance.scale(), 2, "公差小数最多2位！请校验后导入。");

            ProduceDataPo produceDataPo = ParamValidUtils.requireNotNull(produceDataDao.getBySku(sku), "该sku不存在！请校验后导入");
            produceDataPo.setTolerance(tolerance);
            produceDataDao.updateByIdVersion(produceDataPo);
        }
    }

    public void doImportProduceDataAttr(String sku, String spu, List<ProdDataAttrImportDto> importAttrDtoList) {
        if (CollectionUtils.isEmpty(importAttrDtoList)) {
            log.info("没有需要导入的销售属性！导入结束。");
            return;
        }

        //plm属性列表
        List<String> attrNameDtoList = importAttrDtoList.stream()
                .filter(attrImportDto -> Objects.nonNull(attrImportDto) && StrUtil.isNotBlank(attrImportDto.getAttrName()))
                .map(ProdDataAttrImportDto::getAttrName).distinct()
                .collect(Collectors.toList());
        List<PlmAttributeVo> plmAttributeList = plmRemoteService.getAttrListByName(attrNameDtoList);

        //sku末级分类下的所有属性名
        Map<String, Long> skuCategoryIdMap = plmRemoteService.getCategoriesIdBySku(List.of(sku));
        if (!skuCategoryIdMap.containsKey(sku)) {
            throw new ParamIllegalException("当前sku无商品分类信息！请校验后导入。", sku);
        }
        Long categoriesId = skuCategoryIdMap.get(sku);
        List<String> categoryAttrList = plmRemoteService.getCategoryAttr(categoriesId);

        List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.getBySku(sku);

        //下拉录入类型
        List<AttributeEntryType> pullDownEntryTypeList
                = List.of(AttributeEntryType.PULL_DOWN_SINGLE, AttributeEntryType.PULL_DOWN);
        for (ProdDataAttrImportDto importAttrDto : importAttrDtoList) {
            //校验属性名称非空&长度
            ParamValidUtils.requireNotBlank(importAttrDto.getAttrName(), "属性名称为空！请校验后重新导入。");
            String importAttrName = importAttrDto.getAttrName().trim();
            int maxLen = 100;
            ParamValidUtils.requireLessThanOrEqual(importAttrName.length(), maxLen,
                    StrUtil.format("属性名称长度不能超过{}位！请校验后重新导入。", maxLen));

            //校验属性存在plm属性列表中
            PlmAttributeVo matchPlmAttr = plmAttributeList.stream()
                    .filter(plmAttribute -> Objects.equals(plmAttribute.getAttributeName(), importAttrName))
                    .findFirst()
                    .orElse(null);
            if (matchPlmAttr == null) {
                throw new ParamIllegalException("属性名称：{}不存在于PDC系统，请确认后再填写！", importAttrName);
            }

            //校验属性名
            verifyAttrName(matchPlmAttr, categoryAttrList, importAttrName);

            //删除已存在的属性
            Long plmAttrNameId = matchPlmAttr.getAttributeNameId();
            List<ProduceDataAttrPo> matchProdDataAttrPoList = produceDataAttrPoList.stream()
                    .filter(produceDataAttrPo -> Objects.equals(plmAttrNameId, produceDataAttrPo.getAttributeNameId()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(matchProdDataAttrPoList)) {
                produceDataAttrDao.removeBatch(matchProdDataAttrPoList);
            }

            //校验属性值长度
            String importAttrValListStr = importAttrDto.getAttrValue();
            ParamValidUtils.requireNotBlank(importAttrValListStr, "属性值不能为空！请校验后重新导入。");
            String[] importAttrValList = importAttrValListStr.split("/");

            //根据录入类型校验属性值数量
            AttributeEntryType attrInputType = matchPlmAttr.getEntryType();
            String plmAttrName = matchPlmAttr.getAttributeName();
            verifyAttrValueNums(importAttrValList, attrInputType, plmAttrName);

            //校验下拉录入类型可选值=导入值
            for (String importAttrVal : importAttrValList) {
                ParamValidUtils.requireNotBlank(importAttrVal,
                        StrUtil.format("属性名称:{} 属性值不能为空！请校验后重新导入。", importAttrName));

                int valMaxLen = 200;
                ParamValidUtils.requireLessThanOrEqual(importAttrVal.length(), valMaxLen,
                        StrUtil.format("属性值长度不能超过{}位！请校验后重新导入。", valMaxLen));

                List<String> plmAttrValList = matchPlmAttr.getAttributeValueList();
                if (CollectionUtils.isNotEmpty(plmAttrValList) && !plmAttrValList.contains(importAttrVal)
                        && pullDownEntryTypeList.contains(attrInputType)) {
                    throw new ParamIllegalException("属性值：{}属性值在属性名称：{}中不存在，请确认后再填写！", importAttrVal, plmAttrName);
                }

                ProduceDataAttrPo produceDataAttrPo = ProduceDataConverter.attrImportationDtoToPo(spu, sku, plmAttrNameId, plmAttrName, importAttrVal);
                produceDataAttrDao.insert(produceDataAttrPo);
            }
        }
    }

    public void doRefreshSkuGoodsPurchasePrice(String sku) {
        ParamValidUtils.requireNotBlank(sku, "sku不能为空！请校验后导入");

        // 采购商品价格为空或者等于0的情况,调用SKU定价表获取价格
        ProduceDataPo produceDataPo = ParamValidUtils.requireNotNull(produceDataDao.getBySku(sku), "该sku不存在！请校验后导入");
        List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.getBySku(sku);
        if (produceDataPo.getGoodsPurchasePrice() == null || produceDataPo.getGoodsPurchasePrice().compareTo(BigDecimal.ZERO) == 0) {
            produceDataBaseService.saveProduceDataUpdatePrice(List.of(produceDataPo), produceDataAttrPoList);
            produceDataDao.updateByIdVersion(produceDataPo);
        }
    }

    public void doImportSkuGoodsPurchasePrice(String sku, String goodsPurchasePriceStr) {
        ParamValidUtils.requireNotBlank(sku, "sku不能为空！请校验后导入");
        if (StrUtil.isBlank(goodsPurchasePriceStr)) {
            log.info("sku=>{}商品采购价格为空，不更新！", sku);
            return;
        }

        BigDecimal goodsPurchasePrice;
        if (StringUtils.isNotBlank(goodsPurchasePriceStr)) {
            try {
                goodsPurchasePrice = new BigDecimal(goodsPurchasePriceStr);
            } catch (Exception e) {
                throw new ParamIllegalException("商品采购价格格式不正确！请校验后导入。");
            }
            ParamValidUtils.requireLessThanOrEqual(goodsPurchasePrice.precision() - goodsPurchasePrice.scale(), 8, "商品采购价格整数最多8位！请校验后导入。");
            ParamValidUtils.requireLessThanOrEqual(goodsPurchasePrice.scale(), 2, "商品采购价格小数最多2位！请校验后导入。");

            ProduceDataPo produceDataPo = ParamValidUtils.requireNotNull(produceDataDao.getBySku(sku), "该sku不存在！请校验后导入");
            produceDataPo.setGoodsPurchasePrice(goodsPurchasePrice);
            produceDataPo.setGoodsPurchasePriceTime(LocalDateTime.now());
            produceDataDao.updateByIdVersion(produceDataPo);
        }
    }

    public void doImportSkuWeight(String sku, String weightStr) {
        ParamValidUtils.requireNotBlank(sku, "sku不能为空！请校验后导入");
        if (StringUtils.isBlank(weightStr)) {
            log.info("sku=>{}克重为空，不更新！", sku);
            return;
        }

        BigDecimal weight;
        if (StringUtils.isNotBlank(weightStr)) {
            try {
                weight = new BigDecimal(weightStr);
            } catch (Exception e) {
                throw new ParamIllegalException("重量格式不正确！请校验后导入。");
            }
            ParamValidUtils.requireGreaterThan(weight, BigDecimal.ZERO, "重量必须大于0！请校验后导入。");
            ParamValidUtils.requireLessThanOrEqual(weight.precision() - weight.scale(), 8, "重量整数最多8位！请校验后导入。");
            ParamValidUtils.requireLessThanOrEqual(weight.scale(), 2, "重量小数最多2位！请校验后导入。");

            ProduceDataPo produceDataPo = ParamValidUtils.requireNotNull(produceDataDao.getBySku(sku), "该sku不存在！请校验后导入");
            produceDataPo.setWeight(weight);
            produceDataDao.updateByIdVersion(produceDataPo);
        }
    }

    public void doImportSkuSingleCapacity(String sku, String singleCapacityStr) {
        ParamValidUtils.requireNotBlank(sku, "sku不能为空！请校验后导入");
        if (StringUtils.isBlank(singleCapacityStr)) {
            log.info("sku=>{}单件产能为空，不更新！", sku);
            return;
        }

        BigDecimal singleCapacity;
        if (StringUtils.isNotBlank(singleCapacityStr)) {
            try {
                singleCapacity = new BigDecimal(singleCapacityStr);
            } catch (Exception e) {
                throw new ParamIllegalException("单件产能格式不正确！请校验后导入。");
            }
            ParamValidUtils.requireGreaterThanOrEqual(singleCapacity, BigDecimal.ZERO, "单件产能必须大于0！请校验后导入。");
            ParamValidUtils.requireLessThanOrEqual(singleCapacity.precision() - singleCapacity.scale(), 8, "单件产能整数最多8位！请校验后导入。");
            ParamValidUtils.requireLessThanOrEqual(singleCapacity.scale(), 2, "单件产能小数最多2位！请校验后导入。");
            skuBaseService.saveSkuInfoSingleCapacity(sku, singleCapacity);
        }
    }

    public void doImportSkuCycle(String sku, String cycleStr) {
        ParamValidUtils.requireNotBlank(sku, "sku不能为空！请校验后导入");
        if (StringUtils.isBlank(cycleStr)) {
            log.info("sku=>{}生产周期为空，不更新！", sku);
            return;
        }

        BigDecimal cycle;
        if (StringUtils.isNotBlank(cycleStr)) {
            try {
                cycle = new BigDecimal(cycleStr);
            } catch (Exception e) {
                throw new ParamIllegalException("生产周期格式不正确！请校验后导入。");
            }
            ParamValidUtils.requireGreaterThanOrEqual(cycle, BigDecimal.ZERO, "生产周期必须大于0！请校验后导入。");
            ParamValidUtils.requireLessThanOrEqual(cycle.precision() - cycle.scale(), 8, "生产周期整数最多8位！请校验后导入。");
            ParamValidUtils.requireLessThanOrEqual(cycle.scale(), 2, "生产周期小数最多2位！请校验后导入。");

            PlmSkuPo plmSkuPo = ParamValidUtils.requireNotNull(plmSkuDao.getBySku(sku), "该sku不存在！请校验/同步后导入");
            plmSkuPo.setCycle(cycle);
            plmSkuDao.updateByIdVersion(plmSkuPo);
        }
    }

    private void verifyAttrName(PlmAttributeVo matchPlmAttr, List<String> categoryAttrList, String importAttrName) {
        //校验属性=非开启
        if (BooleanType.FALSE.equals(matchPlmAttr.getState())) {
            throw new ParamIllegalException("属性名称：{}在PDC系统已关闭，请在PDC系统开启后再填写！", importAttrName);
        }

        //校验末级分类下属性
        String plmAttrName = matchPlmAttr.getAttributeName();
        if (CollectionUtils.isEmpty(categoryAttrList) || !categoryAttrList.contains(plmAttrName)) {
            throw new ParamIllegalException("属性名称：{}不属于当前sku类目下的属性，请确认后再填写！", importAttrName);
        }
    }

    private void verifyAttrValueNums(String[] importAttrValList, AttributeEntryType attrInputType, String importAttrName) {
        if (AttributeEntryType.TEXT_INPUT.equals(attrInputType)) {
            if (importAttrValList.length > 1) {
                throw new ParamIllegalException("属性名称：{}的录入类型是：{} 禁止输入多个值，请确认后再填写！",
                        importAttrName, AttributeEntryType.TEXT_INPUT.getRemark());
            }
        }
        if (AttributeEntryType.PULL_INPUT_SINGLE.equals(attrInputType)) {
            if (importAttrValList.length > 1) {
                throw new ParamIllegalException("属性名称：{}的录入类型是：{} 禁止输入多个值，请确认后再填写！",
                        importAttrName, AttributeEntryType.PULL_INPUT_SINGLE.getRemark());
            }
        }
        if (AttributeEntryType.PULL_DOWN_SINGLE.equals(attrInputType)) {
            if (importAttrValList.length > 1) {
                throw new ParamIllegalException("属性名称：{}的录入类型是：{} 禁止输入多个值，请确认后再填写！",
                        importAttrName, AttributeEntryType.PULL_DOWN_SINGLE.getRemark());
            }
        }
    }

    private void verifyProdDataAttrFields(ProduceDataAttrImportDto dto) {
        if (Objects.isNull(dto)) {
            return;
        }
        boolean allFieldsEmpty = StringUtils.isBlank(dto.getCycle()) &&
                StringUtils.isBlank(dto.getSingleCapacity()) &&
                StringUtils.isBlank(dto.getWeight()) &&
                StringUtils.isBlank(dto.getTolerance()) &&
                StringUtils.isBlank(dto.getRawManage()) &&
                StringUtils.isBlank(dto.getGoodsPurchasePrice()) &&
                StringUtils.isBlank(dto.getAttrName1()) && StringUtils.isBlank(dto.getAttrValue1()) &&
                StringUtils.isBlank(dto.getAttrName2()) && StringUtils.isBlank(dto.getAttrValue2()) &&
                StringUtils.isBlank(dto.getAttrName3()) && StringUtils.isBlank(dto.getAttrValue3()) &&
                StringUtils.isBlank(dto.getAttrName4()) && StringUtils.isBlank(dto.getAttrValue4()) &&
                StringUtils.isBlank(dto.getAttrName5()) && StringUtils.isBlank(dto.getAttrValue5()) &&
                StringUtils.isBlank(dto.getAttrName6()) && StringUtils.isBlank(dto.getAttrValue6()) &&
                StringUtils.isBlank(dto.getAttrName7()) && StringUtils.isBlank(dto.getAttrValue7()) &&
                StringUtils.isBlank(dto.getAttrName8()) && StringUtils.isBlank(dto.getAttrValue8()) &&
                StringUtils.isBlank(dto.getAttrName9()) && StringUtils.isBlank(dto.getAttrValue9()) &&
                StringUtils.isBlank(dto.getAttrName10()) && StringUtils.isBlank(dto.getAttrValue10()) &&
                StringUtils.isBlank(dto.getAttrName11()) && StringUtils.isBlank(dto.getAttrValue11()) &&
                StringUtils.isBlank(dto.getAttrName12()) && StringUtils.isBlank(dto.getAttrValue12()) &&
                StringUtils.isBlank(dto.getAttrName13()) && StringUtils.isBlank(dto.getAttrValue13()) &&
                StringUtils.isBlank(dto.getAttrName14()) && StringUtils.isBlank(dto.getAttrValue14()) &&
                StringUtils.isBlank(dto.getAttrName15()) && StringUtils.isBlank(dto.getAttrValue15()) &&
                StringUtils.isBlank(dto.getAttrName16()) && StringUtils.isBlank(dto.getAttrValue16()) &&
                StringUtils.isBlank(dto.getAttrName17()) && StringUtils.isBlank(dto.getAttrValue17()) &&
                StringUtils.isBlank(dto.getAttrName18()) && StringUtils.isBlank(dto.getAttrValue18()) &&
                StringUtils.isBlank(dto.getAttrName19()) && StringUtils.isBlank(dto.getAttrValue19()) &&
                StringUtils.isBlank(dto.getAttrName20()) && StringUtils.isBlank(dto.getAttrValue20());
        if (allFieldsEmpty) {
            throw new ParamIllegalException("请填写生产周期/单件产能/重量/原料需管理/商品采购价格/属性信息");
        }

        Set<String> uniqueAttrNameSet = new HashSet<>();
        verifyAttrNameUniqueness(dto.getAttrName1(), uniqueAttrNameSet);
        verifyAttrNameUniqueness(dto.getAttrName2(), uniqueAttrNameSet);
        verifyAttrNameUniqueness(dto.getAttrName3(), uniqueAttrNameSet);
        verifyAttrNameUniqueness(dto.getAttrName4(), uniqueAttrNameSet);
        verifyAttrNameUniqueness(dto.getAttrName5(), uniqueAttrNameSet);
        verifyAttrNameUniqueness(dto.getAttrName6(), uniqueAttrNameSet);
        verifyAttrNameUniqueness(dto.getAttrName7(), uniqueAttrNameSet);
        verifyAttrNameUniqueness(dto.getAttrName8(), uniqueAttrNameSet);
        verifyAttrNameUniqueness(dto.getAttrName9(), uniqueAttrNameSet);
        verifyAttrNameUniqueness(dto.getAttrName10(), uniqueAttrNameSet);
        verifyAttrNameUniqueness(dto.getAttrName11(), uniqueAttrNameSet);
        verifyAttrNameUniqueness(dto.getAttrName12(), uniqueAttrNameSet);
        verifyAttrNameUniqueness(dto.getAttrName13(), uniqueAttrNameSet);
        verifyAttrNameUniqueness(dto.getAttrName14(), uniqueAttrNameSet);
        verifyAttrNameUniqueness(dto.getAttrName15(), uniqueAttrNameSet);
        verifyAttrNameUniqueness(dto.getAttrName16(), uniqueAttrNameSet);
        verifyAttrNameUniqueness(dto.getAttrName17(), uniqueAttrNameSet);
        verifyAttrNameUniqueness(dto.getAttrName18(), uniqueAttrNameSet);
        verifyAttrNameUniqueness(dto.getAttrName19(), uniqueAttrNameSet);
        verifyAttrNameUniqueness(dto.getAttrName20(), uniqueAttrNameSet);
    }

    //校验属性名称唯一性
    private static void verifyAttrNameUniqueness(String attributeName, Set<String> uniqueAttrNameSet) {
        if (StringUtils.isNotBlank(attributeName) && !uniqueAttrNameSet.add(attributeName)) {
            throw new ParamIllegalException("属性名称:{}重复，请调整后再导入", attributeName);
        }
    }

    /**
     * @Description 商品+供应商信息导入
     * 1. 导入供应商与sku对照关系
     * 2. 导入生产资料原料与工序
     * @author yanjiawei
     * @Date 2024/11/20 10:24
     */
    @Transactional(rollbackFor = Exception.class)
    public void importProduceDataRawProcess(ProduceDataRawProcessImportDto dto) {
        String sku = ParamValidUtils.requireNotBlank(dto.getSku(), "sku不能为空！请校验后导入");
        sku = sku.trim();
        verifyProduceDataRawProcessFields(dto);

        //导入sku与供应商对照关系
        String supplierProductName = dto.getSupplierProductName();
        boolean isImportSupCompare = StrUtil.isNotBlank(supplierProductName);
        if (isImportSupCompare) {
            String supplierCode = dto.getSupplierCode();
            supplierProductName = supplierProductName.trim();
            log.info("导入sku与供应商对照关系，sku=>{}，供应商产品名称=>{}，供应商代码=>{}", sku, supplierProductName, supplierCode);
            doImportSupplierProductCompare(sku, supplierCode, supplierProductName);
        }

        List<ProduceDataRamImpDto> produceDataRamImpDtoList = ProdBuilder.buildProduceDataRamImpDtoList(dto);
        List<ProduceDataProcessImpDto> produceDataProcessImpDtoList = ProdBuilder.buildProduceDataProcessImpDtoList(dto);

        boolean isImportProduceDataRaw = CollectionUtils.isNotEmpty(produceDataRamImpDtoList);
        boolean isImportProduceDataProcess = CollectionUtils.isNotEmpty(produceDataProcessImpDtoList);

        String bomName = dto.getBomName();
        if (StrUtil.isNotBlank(bomName) && !isImportProduceDataRaw && !isImportProduceDataProcess) {
            throw new ParamIllegalException("导入失败，bom:{}原料/工序信息未填写！请校验后导入", bomName);
        }

        if (isImportProduceDataRaw || isImportProduceDataProcess) {
            //保存生产资料bom
            log.info("更新生产资料bom，sku=>{} bomName=>{}", sku, bomName);
            ProduceDataItemPo curProduceDataItemPo = doImportProduceDataItem(sku, bomName);

            String supplierCode = dto.getSupplierCode();
            boolean isImportBomSup = StrUtil.isNotBlank(supplierCode);
            if (isImportBomSup) {
                Long produceDataItemId = curProduceDataItemPo.getProduceDataItemId();
                String bomSku = curProduceDataItemPo.getSku();
                String bomSpu = curProduceDataItemPo.getSpu();
                log.info("更新生产资料bom供应商，bomId=>{} bomSku=>{} bomSpu=>{} supplierCode=>{}",
                        produceDataItemId, bomSku, bomSpu, supplierCode);
                doImportProduceDataItemSupplier(produceDataItemId, bomSku, bomSpu, supplierCode);
            }

            //更新生产资料原料
            if (isImportProduceDataRaw) {
                Long produceDataItemId = curProduceDataItemPo.getProduceDataItemId();
                log.info("更新生产资料原料，bomId=>{} produceDataRamImpDtoList=>{}", produceDataItemId, JSON.toJSONString(produceDataRamImpDtoList));
                doImportProduceDataRaw(produceDataItemId, produceDataRamImpDtoList);
            }

            //更新生产资料工序
            if (isImportProduceDataProcess) {
                Long produceDataItemId = curProduceDataItemPo.getProduceDataItemId();
                String bomSku = curProduceDataItemPo.getSku();
                String bomSpu = curProduceDataItemPo.getSpu();
                log.info("更新生产资料工序，bomId=>{} produceDataProcessImpDtoList=>{}", produceDataItemId, JSON.toJSONString(produceDataProcessImpDtoList));
                doImportProduceDataProcess(produceDataItemId, bomSku, bomSpu, produceDataProcessImpDtoList);
            }
        }
    }

    public void doImportProduceDataItemSupplier(Long produceDataItemId, String bomSku, String bomSpu, String supplierCode) {
        //校验供应商存在
        ParamValidUtils.requireNotBlank(supplierCode, "供应商编码不能为空！请校验后导入");
        supplierCode = supplierCode.trim();
        SupplierPo supplierPo = ParamValidUtils.requireNotNull(supplierDao.getBySupplierCode(supplierCode),
                StrUtil.format("供应商：{}不存在！请校验后导入", supplierCode)
        );

        ProduceDataItemSupplierPo produceDataItemSupplierPo
                = ProdBuilder.buildProduceDataItemSupplierPo(produceDataItemId, bomSku, bomSpu, supplierPo);
        produceDataItemSupplierDao.insert(produceDataItemSupplierPo);
    }

    public ProduceDataItemPo doImportProduceDataItem(String sku, String bomName) {
        ParamValidUtils.requireNotBlank(bomName, "已填写原料信息，BOM名称不能为空！请校验后导入");
        bomName = bomName.trim();
        int maxLen = 32;
        ParamValidUtils.requireLessThanOrEqual(bomName.length(), maxLen,
                StrUtil.format("bom名称长度不能超过{}位！请校验后重新导入。", maxLen));

        PlmSkuPo plmSkuPo = ParamValidUtils.requireNotNull(plmSkuDao.getBySku(sku), "该sku不存在！请校验/同步后导入");
        String spu = plmSkuPo.getSpu();

        ProduceDataItemPo produceDataItemPo = produceDataItemDao.getOneBySku(sku);
        Integer sort = Objects.nonNull(produceDataItemPo) ? produceDataItemPo.getSort() : ScmConstant.PRODUCE_DATA_ITEM_MAX_SORT;

        //保存生产资料-bom
        ProduceDataItemPo curProduceDataItemPo = ProdBuilder.buildProduceDataItemPo(bomName, sku, spu, sort);
        produceDataItemDao.insert(curProduceDataItemPo);
        return curProduceDataItemPo;
    }

    public void doImportProduceDataProcess(Long produceDataItemId, String bomSku, String bomSpu, List<ProduceDataProcessImpDto> produceDataProcessImpDtoList) {
        AtomicInteger no = new AtomicInteger(1);
        List<ProduceDataItemProcessPo> produceDataItemProcessPoList = produceDataProcessImpDtoList.stream().map(produceDataProcessImpDto -> {
            String processLabelStr = produceDataProcessImpDto.getProcess();
            String processSecondName = produceDataProcessImpDto.getProcessSecond();
            verifyProduceProcessImport(processLabelStr, processSecondName, no.getAndIncrement());

            ProcessLabel processLabel = ParamValidUtils.requireNotNull(ProcessLabel.getByDesc(processLabelStr),
                    StrUtil.format("工序{}不正确！请校验后导入", no));
            ProcessPo processPo = ParamValidUtils.requireNotNull(processDao.getOneByProcessSecondNameAndLabel(processSecondName, processLabel),
                    StrUtil.format("二级工序名{}不正确！请校验后导入", no));

            ProduceDataItemProcessPo produceDataItemProcessPo = new ProduceDataItemProcessPo();
            produceDataItemProcessPo.setProduceDataItemId(produceDataItemId);
            produceDataItemProcessPo.setSku(bomSku);
            produceDataItemProcessPo.setSpu(bomSpu);
            produceDataItemProcessPo.setProcessCode(processPo.getProcessCode());
            produceDataItemProcessPo.setProcessName(processPo.getProcessSecondName());
            produceDataItemProcessPo.setProcessSecondCode(processPo.getProcessSecondCode());
            produceDataItemProcessPo.setProcessSecondName(processPo.getProcessSecondName());
            produceDataItemProcessPo.setProcessFirst(processPo.getProcessFirst());
            produceDataItemProcessPo.setProcessLabel(processPo.getProcessLabel());
            return produceDataItemProcessPo;
        }).collect(Collectors.toList());
        produceDataItemProcessDao.insertBatch(produceDataItemProcessPoList);
    }

    public void doImportProduceDataRaw(Long produceDataItemId, List<ProduceDataRamImpDto> produceDataRamImpDtoList) {
        //校验原料sku唯一
        if (CollectionUtils.isEmpty(produceDataRamImpDtoList)) {
            log.info("导入原料列表为空，无需更新。");
            return;
        }

        Set<String> seenSkus = new HashSet<>();
        for (ProduceDataRamImpDto item : produceDataRamImpDtoList) {
            String rawSku = item.getRawSku();
            if (StrUtil.isNotBlank(rawSku)) {
                if (!seenSkus.add(rawSku.trim())) {
                    throw new ParamIllegalException("原料{}重复!请校验后导入", rawSku);
                }
            }
        }

        AtomicInteger no = new AtomicInteger(1);
        List<ProduceDataItemRawPo> produceDataItemRawPoList = produceDataRamImpDtoList.stream().map(produceDataRamImpDto -> {
            String rawSku = produceDataRamImpDto.getRawSku();
            String skuCntStr = produceDataRamImpDto.getSkuCnt();
            verifyProduceRawImport(rawSku, skuCntStr, no.getAndIncrement());

            ProduceDataItemRawPo produceDataItemRawPo = new ProduceDataItemRawPo();
            produceDataItemRawPo.setProduceDataItemId(produceDataItemId);
            produceDataItemRawPo.setSku(rawSku);
            produceDataItemRawPo.setSkuCnt(Integer.parseInt(skuCntStr));
            produceDataItemRawPo.setMaterialType(MaterialType.MATERIAL);
            return produceDataItemRawPo;
        }).collect(Collectors.toList());
        produceDataItemRawDao.insertBatch(produceDataItemRawPoList);
    }

    public void doImportSupplierProductCompare(String sku, String supplierCode, String supplierProductName) {
        //校验供应商产品名称长度
        int maxLength = 100;
        ParamValidUtils.requireLessThanOrEqual(supplierProductName.length(), maxLength, StrUtil.format("供应商产品名称长度不能超过{}！请校验后导入", maxLength));

        //校验供应商存在
        ParamValidUtils.requireNotBlank(supplierCode, "供应商编码不能为空！请校验后导入");
        supplierCode = supplierCode.trim();
        SupplierPo supplierPo = ParamValidUtils.requireNotNull(supplierDao.getBySupplierCode(supplierCode),
                StrUtil.format("供应商：{}不存在！请校验后导入", supplierCode)
        );

        //校验供应商状态
        SupplierStatus supplierStatus = supplierPo.getSupplierStatus();
        ParamValidUtils.requireEquals(SupplierStatus.ENABLED, supplierStatus, StrUtil.format("供应商：{}状态为禁用，请校验后导入", supplierCode));

        //插入/更新供应商对照关系
        SupplierProductComparePo supplierProductComparePo = supplierProductCompareDao.getBySupplierCodeAndSku(supplierCode, sku);
        if (Objects.nonNull(supplierProductComparePo)) {
            supplierProductComparePo.setSupplierProductName(supplierProductName);
            supplierProductCompareDao.updateByIdVersion(supplierProductComparePo);
        } else {
            SupplierProductComparePo newSupplierProductCompare = new SupplierProductComparePo();
            newSupplierProductCompare.setSku(sku);
            newSupplierProductCompare.setSupplierCode(supplierPo.getSupplierCode());
            newSupplierProductCompare.setSupplierProductName(supplierProductName);
            newSupplierProductCompare.setSupplierName(supplierPo.getSupplierName());
            newSupplierProductCompare.setSupplierProductCompareStatus(BooleanType.TRUE);
            supplierProductCompareDao.insert(newSupplierProductCompare);
        }

        //更新sku绑定状态
        PlmSkuPo plmSkuPo = ParamValidUtils.requireNotNull(plmSkuDao.getBySku(sku), "该sku不存在！请校验/同步后导入");
        plmSkuPo.setBindingSupplierProduct(BindingSupplierProduct.TRUE);
        plmSkuDao.updateByIdVersion(plmSkuPo);

        //初始化供应商库存信息
        SupplierInventoryBatchUpdateBo initSupInventoryBo = new SupplierInventoryBatchUpdateBo();
        initSupInventoryBo.setSku(plmSkuPo.getSku());
        initSupInventoryBo.setSupplierCode(supplierPo.getSupplierCode());
        initSupInventoryBo.setSupplierName(supplierPo.getSupplierName());
        log.info("初始化供应商库存信息：{}", JSON.toJSONString(initSupInventoryBo));
        supplierProductCompareBaseService.supplierInventoryUpdateBatch(Collections.singletonList(initSupInventoryBo));
    }

    private void verifyProduceDataRawProcessFields(ProduceDataRawProcessImportDto dto) {
        if (Objects.isNull(dto)) {
            return;
        }

        boolean allFieldsEmpty =
                StringUtils.isBlank(dto.getSupplierProductName()) &&
                        StringUtils.isBlank(dto.getSupplierCode()) &&
                        StringUtils.isBlank(dto.getBomName()) &&
                        StringUtils.isBlank(dto.getRawSku1()) && StringUtils.isBlank(dto.getSkuCnt1()) &&
                        StringUtils.isBlank(dto.getRawSku2()) && StringUtils.isBlank(dto.getSkuCnt2()) &&
                        StringUtils.isBlank(dto.getRawSku3()) && StringUtils.isBlank(dto.getSkuCnt3()) &&
                        StringUtils.isBlank(dto.getRawSku4()) && StringUtils.isBlank(dto.getSkuCnt4()) &&
                        StringUtils.isBlank(dto.getRawSku5()) && StringUtils.isBlank(dto.getSkuCnt5()) &&
                        StringUtils.isBlank(dto.getRawSku6()) && StringUtils.isBlank(dto.getSkuCnt6()) &&
                        StringUtils.isBlank(dto.getRawSku7()) && StringUtils.isBlank(dto.getSkuCnt7()) &&
                        StringUtils.isBlank(dto.getProcess1()) && StringUtils.isBlank(dto.getProcessSecond1()) &&
                        StringUtils.isBlank(dto.getProcess2()) && StringUtils.isBlank(dto.getProcessSecond2()) &&
                        StringUtils.isBlank(dto.getProcess3()) && StringUtils.isBlank(dto.getProcessSecond3()) &&
                        StringUtils.isBlank(dto.getProcess4()) && StringUtils.isBlank(dto.getProcessSecond4()) &&
                        StringUtils.isBlank(dto.getProcess5()) && StringUtils.isBlank(dto.getProcessSecond5());
        if (allFieldsEmpty) {
            throw new ParamIllegalException("请填写供应商产品名称/供应商编码/Bom信息/原料信息/工序信息");
        }

        boolean supplierCodeOnly = StringUtils.isNotBlank(dto.getSupplierCode()) &&
                StringUtils.isBlank(dto.getSupplierProductName()) &&
                StringUtils.isBlank(dto.getBomName()) &&
                StringUtils.isBlank(dto.getRawSku1()) && StringUtils.isBlank(dto.getSkuCnt1()) &&
                StringUtils.isBlank(dto.getRawSku2()) && StringUtils.isBlank(dto.getSkuCnt2()) &&
                StringUtils.isBlank(dto.getRawSku3()) && StringUtils.isBlank(dto.getSkuCnt3()) &&
                StringUtils.isBlank(dto.getRawSku4()) && StringUtils.isBlank(dto.getSkuCnt4()) &&
                StringUtils.isBlank(dto.getRawSku5()) && StringUtils.isBlank(dto.getSkuCnt5()) &&
                StringUtils.isBlank(dto.getRawSku6()) && StringUtils.isBlank(dto.getSkuCnt6()) &&
                StringUtils.isBlank(dto.getRawSku7()) && StringUtils.isBlank(dto.getSkuCnt7()) &&
                StringUtils.isBlank(dto.getProcess1()) && StringUtils.isBlank(dto.getProcessSecond1()) &&
                StringUtils.isBlank(dto.getProcess2()) && StringUtils.isBlank(dto.getProcessSecond2()) &&
                StringUtils.isBlank(dto.getProcess3()) && StringUtils.isBlank(dto.getProcessSecond3()) &&
                StringUtils.isBlank(dto.getProcess4()) && StringUtils.isBlank(dto.getProcessSecond4()) &&
                StringUtils.isBlank(dto.getProcess5()) && StringUtils.isBlank(dto.getProcessSecond5());
        if (supplierCodeOnly) {
            throw new ParamIllegalException("请填写供应商产品名称/Bom信息/原料信息/工序信息");
        }
    }

    private void verifyProduceProcessImport(String processLabelStr, String processSecondName, int no) {
        if (StrUtil.isNotBlank(processLabelStr)) {
            ParamValidUtils.requireNotBlank(processSecondName, StrUtil.format("二级工序名{}不能为空！请校验后导入", no));

            processLabelStr = processLabelStr.trim();
            ProcessLabel processLabel = ParamValidUtils.requireNotNull(ProcessLabel.getByDesc(processLabelStr),
                    StrUtil.format("工序{}不正确！请校验后导入", no));
            ParamValidUtils.requireNotNull(processDao.getOneByProcessSecondNameAndLabel(processSecondName, processLabel),
                    StrUtil.format("二级工序名{}不正确！请校验后导入", no));
        }

        if (StrUtil.isNotBlank(processSecondName)) {
            ParamValidUtils.requireNotBlank(processLabelStr, StrUtil.format("工序{}不能为空！请校验后导入", no));

            processLabelStr = processLabelStr.trim();
            ProcessLabel processLabel = ParamValidUtils.requireNotNull(ProcessLabel.getByDesc(processLabelStr),
                    StrUtil.format("工序{}不正确！请校验后导入", no));
            ParamValidUtils.requireNotNull(processDao.getOneByProcessSecondNameAndLabel(processSecondName, processLabel),
                    StrUtil.format("二级工序名{}不正确！请校验后导入", no));
        }
    }

    private void verifyProduceRawImport(String rawSku, String skuCntStr, int no) {
        //填写原料sku
        if (StrUtil.isNotBlank(rawSku)) {
            ParamValidUtils.requireNotBlank(skuCntStr, StrUtil.format("单件用量{}不能为空！请校验后导入", no));
            int skuCnt;
            try {
                skuCnt = Integer.parseInt(skuCntStr);
            } catch (Exception e) {
                throw new ParamIllegalException("单件用量不正确！请校验后导入。");
            }
            ParamValidUtils.requireGreaterThan(skuCnt, 0, "单件用量必须大于0！请校验后导入。");
            ParamValidUtils.requireLessThanOrEqual(skuCnt, 99999999, "单件用量最多8位！请校验后导入。");
        }

        //填写原料单件用量
        if (StrUtil.isNotBlank(skuCntStr)) {
            ParamValidUtils.requireNotBlank(rawSku, StrUtil.format("原料SKU{}不能为空！请校验后导入", no));
            List<PlmGoodsSkuVo> skuInfoList = plmRemoteService.getSkuDetailListBySkuList(Collections.singletonList(rawSku));
            ParamValidUtils.requireNotEmpty(skuInfoList, StrUtil.format("原料SKU{}不存在！请校验后导入", no));
            ParamValidUtils.requireNotNull(skuInfoList.stream().findFirst().orElse(null),
                    StrUtil.format("原料SKU{}不存在！请校验后导入", no));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCopyProduceDataAttr(UpdateProduceDataAttrDto dto) {
        //更新属性信息
        updateProduceDataAttr(dto);

        //查询同spu下的所有sku(剔除当前sku)
        String sku = dto.getSku();
        List<String> sameSpuSkuList = plmSkuRefService.listSameSpuSkuList(sku);
        if (CollectionUtils.isEmpty(sameSpuSkuList)) {
            log.info("复制属性信息结束！sku=>{}，没有同spu下的sku", sku);
            return;
        }
        sameSpuSkuList.removeIf(sameSpuSku -> Objects.equals(sameSpuSku, sku));
        if (CollectionUtils.isEmpty(sameSpuSkuList)) {
            log.info("复制属性信息结束！sku=>{}，没有同spu下的sku", sku);
            return;
        }

        //当前可复制的属性
        List<ProduceDataAttrPo> curProdAttrPoList = produceDataAttrDao.getBySku(sku);

        //获取不能复制的属性id
        List<Long> unCopyAttrNameIds
                = Optional.ofNullable(produceDataProp.getWhitelistNameIds()).orElse(Collections.emptyList());
        curProdAttrPoList.removeIf(curProdAttrPo -> unCopyAttrNameIds.contains(curProdAttrPo.getAttributeNameId()));

        //属性复制
        log.info("复制属性信息开始！sku=>{} 去向属性列表=>{} 去向sku列表=>{}", sku, curProdAttrPoList, JSON.toJSONString(sameSpuSkuList));
        copy2SkuProduceDataAttr(curProdAttrPoList, sameSpuSkuList, unCopyAttrNameIds);
    }

    public void copy2SkuProduceDataAttr(List<ProduceDataAttrPo> copyProdAttrPoList, List<String> copySkuList, List<Long> unCopyAttrNameIds) {
        if (CollectionUtils.isEmpty(copySkuList)) {
            log.info("复制属性信息结束！没有可复制的sku");
            return;
        }

        //剔除不可更新的属性
        copySkuList = copySkuList.stream().distinct().collect(Collectors.toList());
        List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.getListBySkuList(copySkuList);
        produceDataAttrPoList.removeIf(curProdAttrPo -> unCopyAttrNameIds.contains(curProdAttrPo.getAttributeNameId()));
        if (CollectionUtils.isNotEmpty(produceDataAttrPoList)) {
            log.info("复制属性信息开始！剔除不可更新属性id列表=>{}，清空同spu下所有sku现有的属性值=>{}",
                    JSON.toJSONString(unCopyAttrNameIds), JSON.toJSONString(produceDataAttrPoList));
            produceDataAttrDao.removeBatch(produceDataAttrPoList);
        }

        //根据属性名id分组
        Map<Long, List<ProduceDataAttrPo>> attrIdValueMap = copyProdAttrPoList.stream()
                .collect(Collectors.groupingBy(ProduceDataAttrPo::getAttributeNameId));
        for (Map.Entry<Long, List<ProduceDataAttrPo>> attrIdValueEntry : attrIdValueMap.entrySet()) {
            List<ProduceDataAttrPo> attributeValueList = attrIdValueEntry.getValue();
            String attributeName = attributeValueList.stream().findFirst().map(ProduceDataAttrPo::getAttrName).orElse("");

            for (String copySku : copySkuList) {
                produceDataAttrDao.insertBatch(ProdBuilder.buildProduceDataAttrPoList(copySku, attributeValueList));
                log.info("复制属性信息开始！sku=>{} 不存在=>{}属性信息，赋值属性=>{}", copySku, attributeName, JSON.toJSONString(attributeValueList));
            }
        }
    }

    /**
     * @Description 导出sku信息与供应商原料工序信息
     * @author yanjiawei
     * @Date 2024/11/5 14:15
     */
    @Transactional(rollbackFor = Exception.class)
    public void exportSkuSup(PlmSkuSearchDto dto) {
        Integer skuTotal = getExportTotals(dto);
        Integer supTotal = produceDataRefService.getSkuProcessExportTotals(JSON.parseObject(JSON.toJSONString(dto), ProduceDataSearchDto.class));
        if (Objects.isNull(skuTotal) && Objects.isNull(supTotal)) {
            throw new ParamIllegalException("导出数据为空!");
        }
        if (skuTotal.equals(0) && supTotal.equals(0)) {
            throw new ParamIllegalException("导出数据为空!");
        }

        consistencySendMqService.execSendMq(ScmExportHandler.class,
                new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                        FileOperateBizType.SCM_SKU_PROD_SUPPLIER_PROCESS_EXPORT.getCode(), dto)
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void refreshColorAttr() {
        Long colorAttributeNameId = produceDataProp.getColorAttributeNameId();
        if (Objects.isNull(colorAttributeNameId)) {
            log.info("颜色属性ID配置为空，跳过刷新");
            return;
        }

        Long colorSysAttributeNameId = produceDataProp.getColorSystemAttributeNameId();
        if (Objects.isNull(colorSysAttributeNameId)) {
            log.info("颜色色系属性ID配置为空，跳过刷新");
        }

        String colorSysAttributeNameName = produceDataProp.getColorSystemAttributeNameName();
        if (StrUtil.isBlank(colorSysAttributeNameName)) {
            log.info("颜色色系属性名称配置为空，跳过刷新");
        }

        List<ColorAttrRuleBo> colorAttrRuleList = produceDataProp.getColorAttrRuleList();
        if (CollectionUtil.isEmpty(colorAttrRuleList)) {
            log.info("颜色色系规则配置为空，跳过刷新");
            return;
        }

        PageExecutor<String> produceDataAttrPoPageExecutor = new PageExecutor<>();
        produceDataAttrPoPageExecutor.doForPage((page) -> {
            return produceDataAttrDao.getSkuByPage(page, Collections.singletonList(colorAttributeNameId));
        }, skuList -> {
            for (String sku : skuList) {
                List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.getBySku(sku);
                if (CollectionUtil.isEmpty(produceDataAttrPoList)) {
                    log.info("刷新颜色色系属性：sku=>{}未找到属性信息", sku);
                    continue;
                }

                List<ProduceDataAttrPo> colorAttrList = produceDataAttrPoList.stream()
                        .filter(attr -> Objects.equals(attr.getAttributeNameId(), colorAttributeNameId))
                        .collect(Collectors.toList());
                if (CollectionUtil.isEmpty(colorAttrList)) {
                    log.info("刷新颜色色系属性：sku=>{}未找到颜色属性信息", sku);
                    continue;
                }

                Set<String> colorAttrValueList = colorAttrList.stream().map(ProduceDataAttrPo::getAttrValue)
                        .filter(StrUtil::isNotBlank)
                        .collect(Collectors.toSet());
                Set<String> colorSysAttrValueList = matchColorSystemValueSet(colorAttrValueList, colorAttrRuleList);
                log.info("根据颜色属性值匹配色系属性值：sku=>{} 颜色属性值列表=>{} 颜色-色系匹配规则=>{}无法匹配对应色系属性值",
                        sku, colorAttrValueList, JSON.toJSONString(colorAttrRuleList));

                List<ProduceDataAttrPo> existColorSysAttrList = produceDataAttrPoList.stream()
                        .filter(attr -> Objects.equals(attr.getAttributeNameId(), colorSysAttributeNameId))
                        .collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(existColorSysAttrList)) {
                    produceDataAttrDao.removeBatch(existColorSysAttrList);
                }

                String spu = colorAttrList.stream().map(ProduceDataAttrPo::getSpu).findFirst().orElse("");
                List<ProduceDataAttrPo> colorSysAttrList
                        = ProdBuilder.buildProduceDataAttrList(sku, spu, colorSysAttributeNameId, colorSysAttributeNameName, colorSysAttrValueList);
                produceDataAttrDao.insertBatch(colorSysAttrList);
                log.info("刷新颜色色系属性：sku=>{}，颜色色系属性=>{}", sku, JSON.toJSONString(colorSysAttrList));
            }
        });
    }

    private Set<String> matchColorSystemValueSet(Set<String> colorAttrValueList, List<ColorAttrRuleBo> colorAttrRuleList) {
        if (CollectionUtil.isEmpty(colorAttrValueList)) {
            log.info("根据颜色属性值匹配色系属性值：颜色属性值列表为空，匹配结束。");
            return Collections.emptySet();
        }
        if (CollectionUtil.isEmpty(colorAttrRuleList)) {
            log.info("根据颜色属性值匹配色系属性值：颜色-色系匹配规则为空，匹配结束。");
            return Collections.emptySet();
        }

        List<ColorAttrRuleBo> matchColorRuleList = colorAttrRuleList.stream()
                .filter(rule -> colorAttrValueList.contains(rule.getColorAttrValue()))
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(matchColorRuleList)) {
            return Collections.emptySet();
        }

        for (int priority = 1; priority <= 4; priority++) {
            int finalPriority = priority;
            List<ColorAttrRuleBo> matchHighPriorityValues = matchColorRuleList.stream()
                    .filter(rule -> rule.getPriority() == finalPriority)
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(matchHighPriorityValues)) {
                Set<String> colorSysAttrList = matchHighPriorityValues
                        .stream().map(ColorAttrRuleBo::getColorSysAttrValue)
                        .collect(Collectors.toSet());
                log.info("根据颜色属性值匹配到色系属性值：优先级=>{} 色系属性值=>{}匹配成功，匹配结束。",
                        priority, JSON.toJSONString(colorSysAttrList));
                return colorSysAttrList;
            }
        }
        return Collections.emptySet();
    }
}










