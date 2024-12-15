package com.hete.supply.scm.server.scm.supplier.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.plm.api.goods.entity.vo.PlmCategoryVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmGoodsDetailVo;
import com.hete.supply.scm.api.scm.entity.enums.SupplierStatus;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.dao.PlmSkuDao;
import com.hete.supply.scm.server.scm.entity.po.PlmSkuPo;
import com.hete.supply.scm.server.scm.enums.BindingSupplierProduct;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierInventoryDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierProductCompareDao;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierBindingListQueryBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierCodeAndSkuBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierInventoryBatchUpdateBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.core.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/3/7 17:25
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierProductCompareBaseService {
    private final SupplierProductCompareDao supplierProductCompareDao;
    private final SupplierDao supplierDao;
    private final PlmSkuDao plmSkuDao;
    private final PlmRemoteService plmRemoteService;
    private final SupplierInventoryDao supplierInventoryDao;


    /**
     * 通过sku获取已绑定供应商且启动供应商的sku
     *
     * @param skuList:
     * @return List<SupplierProductComparePo>
     * @author ChenWenLong
     * @date 2024/3/7 17:44
     */
    public List<SupplierProductComparePo> getPoListBySkuList(List<String> skuList) {
        List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getListByBatchSku(skuList);
        List<String> supplierCodeList = supplierProductComparePoList.stream()
                .map(SupplierProductComparePo::getSupplierCode)
                .distinct()
                .collect(Collectors.toList());

        List<SupplierPo> supplierPoList = supplierDao.getBySupplierCodeList(supplierCodeList);
        List<String> supplierCodeEnabledList = supplierPoList.stream()
                .filter(supplierPo -> SupplierStatus.ENABLED.equals(supplierPo.getSupplierStatus()))
                .map(SupplierPo::getSupplierCode)
                .collect(Collectors.toList());

        return supplierProductComparePoList.stream()
                .filter(supplierProductComparePo -> supplierCodeEnabledList.contains(supplierProductComparePo.getSupplierCode()))
                .collect(Collectors.toList());

    }

    /**
     * 通过sku获取已绑定供应商的sku(包括启动和禁止)
     *
     * @param skuList:
     * @return List<SupplierProductComparePo>
     * @author ChenWenLong
     * @date 2024/3/7 17:44
     */
    public List<SupplierProductComparePo> getAllPoListBySkuList(List<String> skuList) {
        List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getListByBatchSku(skuList);
        List<String> supplierCodeList = supplierProductComparePoList.stream()
                .map(SupplierProductComparePo::getSupplierCode)
                .distinct()
                .collect(Collectors.toList());

        List<SupplierPo> supplierPoList = supplierDao.getBySupplierCodeList(supplierCodeList);
        List<String> supplierCodeEnabledList = supplierPoList.stream()
                .map(SupplierPo::getSupplierCode)
                .collect(Collectors.toList());

        return supplierProductComparePoList.stream()
                .filter(supplierProductComparePo -> supplierCodeEnabledList.contains(supplierProductComparePo.getSupplierCode()))
                .collect(Collectors.toList());

    }

    /**
     * 创建供应商产品对照以及创建供应商库存信息
     *
     * @param supplierCode:
     * @param skuList:
     * @return void
     * @author ChenWenLong
     * @date 2024/4/24 15:20
     */
    public void insertSupplierProductCompareAndInventory(@NotBlank String supplierCode,
                                                         List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return;
        }
        // 查询供应商产品对照
        Map<String, String> supplierProductCompareMap = supplierProductCompareDao.getBatchSkuMapAndSupplierCode(skuList, supplierCode);

        // 查询供应商信息
        SupplierPo supplierPo = supplierDao.getBySupplierCode(supplierCode);
        if (null == supplierPo) {
            throw new BizException("供应商代码:{}查询不到对应供应商的信息，请联系管理员！", supplierCode);
        }

        // 查询PLM的sku信息
        Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        List<SupplierProductComparePo> insertProductComparePoList = new ArrayList<>();
        // 供应商库存信息
        List<SupplierInventoryBatchUpdateBo> supplierInventoryBatchUpdateBoList = new ArrayList<>();
        for (String sku : skuList) {
            if (!supplierProductCompareMap.containsKey(sku)) {
                if (!skuEncodeMap.containsKey(sku)) {
                    log.warn("获取不到对应的sku的信息,skuEncodeMap={}", JacksonUtil.parse2Str(skuEncodeMap));
                    continue;
                }
                SupplierProductComparePo supplierProductComparePo = new SupplierProductComparePo();
                supplierProductComparePo.setSupplierProductName(skuEncodeMap.get(sku));
                supplierProductComparePo.setSupplierCode(supplierPo.getSupplierCode());
                supplierProductComparePo.setSupplierName(supplierPo.getSupplierName());
                supplierProductComparePo.setSku(sku);
                supplierProductComparePo.setSupplierProductCompareStatus(BooleanType.TRUE);
                insertProductComparePoList.add(supplierProductComparePo);
                // 供应商库存信息组装
                SupplierInventoryBatchUpdateBo supplierInventoryBatchUpdateBo = new SupplierInventoryBatchUpdateBo();
                supplierInventoryBatchUpdateBo.setSku(sku);
                supplierInventoryBatchUpdateBo.setSupplierName(supplierPo.getSupplierName());
                supplierInventoryBatchUpdateBo.setSupplierCode(supplierPo.getSupplierCode());
                supplierInventoryBatchUpdateBoList.add(supplierInventoryBatchUpdateBo);
            }
        }

        if (CollectionUtils.isNotEmpty(insertProductComparePoList)) {
            supplierProductCompareDao.insertBatch(insertProductComparePoList);
            List<String> updateSkuBindingList = insertProductComparePoList.stream()
                    .map(SupplierProductComparePo::getSku)
                    .distinct()
                    .collect(Collectors.toList());
            this.updatePlmSkuBindingSupplierProduct(updateSkuBindingList);
        }
        // 增加时执行初始供应商库存信息
        this.supplierInventoryUpdateBatch(supplierInventoryBatchUpdateBoList);

    }

    /**
     * 批量创建供应商库存信息
     *
     * @param boList:
     * @return void
     * @author ChenWenLong
     * @date 2024/4/24 14:43
     */
    public void supplierInventoryUpdateBatch(List<SupplierInventoryBatchUpdateBo> boList) {
        if (CollectionUtils.isEmpty(boList)) {
            return;
        }

        final List<String> skuList = boList.stream()
                .map(SupplierInventoryBatchUpdateBo::getSku)
                .distinct()
                .collect(Collectors.toList());

        // 验证SKU是否已存在
        final List<SupplierCodeAndSkuBo> supplierCodeAndSkuBoList = boList.stream()
                .map(item -> {
                    final SupplierCodeAndSkuBo supplierCodeAndSkuBo = new SupplierCodeAndSkuBo();
                    supplierCodeAndSkuBo.setSku(item.getSku());
                    supplierCodeAndSkuBo.setSupplierCode(item.getSupplierCode());
                    return supplierCodeAndSkuBo;
                }).collect(Collectors.toList());

        final List<SupplierInventoryPo> supplierInventoryPoList = supplierInventoryDao.getInventoryBySkuAndSupplier(supplierCodeAndSkuBoList);
        List<SupplierInventoryBatchUpdateBo> filteredBoList = boList;
        if (CollectionUtils.isNotEmpty(supplierInventoryPoList)) {
            filteredBoList = boList.stream()
                    .filter(bo -> supplierInventoryPoList.stream()
                            .noneMatch(po -> po.getSupplierCode().equals(bo.getSupplierCode()) && po.getSku().equals(bo.getSku())))
                    .collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(filteredBoList)) {
            return;
        }

        // 获取分类信息
        final List<PlmGoodsDetailVo> plmGoodsDetailVoList = plmRemoteService.getCategoriesBySku(skuList);

        // 组装插入数据PO
        final List<SupplierInventoryPo> supplierInventoryPoInsertList = filteredBoList.stream()
                .map(bo -> {
                    final PlmGoodsDetailVo plmGoodsDetailVo = plmGoodsDetailVoList.stream()
                            .filter(vo -> vo.getSkuCodeList().contains(bo.getSku()))
                            .findFirst()
                            .orElse(null);
                    if (null == plmGoodsDetailVo) {
                        throw new BizException("scm供应商与sku对照关系中，存在plm没有的sku:{}", bo.getSku());
                    }

                    final List<PlmCategoryVo> categoryList = plmGoodsDetailVo.getCategoryList();
                    PlmCategoryVo plmCategoryVo;
                    // 只有一级品类时，取一级品类
                    if (categoryList.size() == 1) {
                        plmCategoryVo = categoryList.stream().findFirst().get();
                    } else {
                        plmCategoryVo = categoryList.stream()
                                .filter(categoryVo -> categoryVo.getLevel() == 2)
                                .findFirst()
                                .orElse(null);
                        if (null == plmCategoryVo) {
                            throw new BizException("sku:{}在plm存在错误的类目关系", bo.getSku());
                        }
                    }

                    final SupplierInventoryPo supplierInventoryPo = new SupplierInventoryPo();
                    supplierInventoryPo.setSupplierCode(bo.getSupplierCode());
                    supplierInventoryPo.setSupplierName(bo.getSupplierName());
                    supplierInventoryPo.setSpu(plmGoodsDetailVo.getSpuCode());
                    supplierInventoryPo.setSku(bo.getSku());
                    supplierInventoryPo.setCategoryId(plmCategoryVo.getCategoryId());
                    supplierInventoryPo.setCategoryName(plmCategoryVo.getCategoryNameCn());

                    return supplierInventoryPo;
                }).collect(Collectors.toList());

        supplierInventoryDao.insertBatch(supplierInventoryPoInsertList);

    }

    /**
     * 通过sku和供应商列表增加对照关系
     *
     * @param sku:
     * @param supplierCodeList:
     * @author ChenWenLong
     * @date 2024/9/29 10:36
     */
    public void insertSupplierProductCompareBySku(@NotBlank String sku, List<String> supplierCodeList) {
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return;
        }
        log.info("sku：{}进行新增供应商产品对照的供应商：{}", sku, supplierCodeList);
        // 查询供应商产品对照
        Map<String, String> supplierProductCompareMap = supplierProductCompareDao.getBatchSupplierMapAndSku(supplierCodeList, sku);

        // 查询供应商信息
        Map<String, SupplierPo> supplierMap = supplierDao.getSupplierMapBySupplierCodeList(supplierCodeList);

        // 查询PLM的sku信息
        Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(List.of(sku));

        List<SupplierProductComparePo> insertProductComparePoList = new ArrayList<>();
        // 供应商库存信息
        List<SupplierInventoryBatchUpdateBo> supplierInventoryBatchUpdateBoList = new ArrayList<>();
        for (String supplierCode : supplierCodeList) {
            SupplierPo supplierPo = supplierMap.get(supplierCode);
            if (null == supplierPo) {
                throw new BizException("供应商代码:{}查询不到对应供应商的信息，请联系管理员！", supplierCode);
            }
            if (!supplierProductCompareMap.containsKey(supplierCode)) {
                if (!skuEncodeMap.containsKey(sku)) {
                    throw new BizException("通过PLM获取不到对应的sku的信息,skuEncodeMap={}", JacksonUtil.parse2Str(skuEncodeMap));
                }
                SupplierProductComparePo supplierProductComparePo = new SupplierProductComparePo();
                supplierProductComparePo.setSupplierProductName(skuEncodeMap.get(sku));
                supplierProductComparePo.setSupplierCode(supplierPo.getSupplierCode());
                supplierProductComparePo.setSupplierName(supplierPo.getSupplierName());
                supplierProductComparePo.setSku(sku);
                supplierProductComparePo.setSupplierProductCompareStatus(BooleanType.TRUE);
                insertProductComparePoList.add(supplierProductComparePo);
                // 供应商库存信息组装
                SupplierInventoryBatchUpdateBo supplierInventoryBatchUpdateBo = new SupplierInventoryBatchUpdateBo();
                supplierInventoryBatchUpdateBo.setSku(sku);
                supplierInventoryBatchUpdateBo.setSupplierName(supplierPo.getSupplierName());
                supplierInventoryBatchUpdateBo.setSupplierCode(supplierPo.getSupplierCode());
                supplierInventoryBatchUpdateBoList.add(supplierInventoryBatchUpdateBo);
            }
        }

        if (CollectionUtils.isNotEmpty(insertProductComparePoList)) {
            log.info("最终成功sku：{}进行新增供应商产品对照的供应商PO：{}", sku, JacksonUtil.parse2Str(insertProductComparePoList));
            supplierProductCompareDao.insertBatch(insertProductComparePoList);
            List<String> updateSkuBindingList = insertProductComparePoList.stream()
                    .map(SupplierProductComparePo::getSku)
                    .distinct()
                    .collect(Collectors.toList());
            this.updatePlmSkuBindingSupplierProduct(updateSkuBindingList);
        }
        // 增加时执行初始供应商库存信息
        this.supplierInventoryUpdateBatch(supplierInventoryBatchUpdateBoList);

    }

    /**
     * 提供公用通过条件获取可用供应商产品对照列表
     *
     * @param supplierBindingListQueryBo:
     * @return List<SupplierProductComparePo>
     * @author ChenWenLong
     * @date 2024/10/10 16:35
     */
    public List<SupplierProductComparePo> getBindingSupplierList(@NotNull SupplierBindingListQueryBo supplierBindingListQueryBo) {
        // 获取供应商绑定关系逻辑处理
        return supplierProductCompareDao.getBySkuListAndStatus(supplierBindingListQueryBo.getSkuList(), BooleanType.TRUE);
    }

    /**
     * 更新plm_sku表中的绑定供应商产品字段
     *
     * @param skuList:
     * @return void
     * @author ChenWenLong
     * @date 2024/10/15 11:52
     */
    public void updatePlmSkuBindingSupplierProduct(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return;
        }
        List<PlmSkuPo> plmSkuPoList = plmSkuDao.getList(skuList);
        if (CollectionUtils.isEmpty(plmSkuPoList)) {
            return;
        }
        for (PlmSkuPo plmSkuPo : plmSkuPoList) {
            plmSkuPo.setBindingSupplierProduct(BindingSupplierProduct.TRUE);
        }
        plmSkuDao.updateBatchById(plmSkuPoList);
    }

}
