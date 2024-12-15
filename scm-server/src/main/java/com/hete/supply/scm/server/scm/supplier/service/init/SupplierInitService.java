package com.hete.supply.scm.server.scm.supplier.service.init;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.plm.api.goods.entity.vo.PlmCategoryVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmGoodsDetailVo;
import com.hete.supply.scm.api.scm.entity.enums.SupplierWarehouse;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.dao.PlmSkuDao;
import com.hete.supply.scm.server.scm.entity.po.PlmSkuPo;
import com.hete.supply.scm.server.scm.enums.BindingSupplierProduct;
import com.hete.supply.scm.server.scm.process.service.base.PageExecutor;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierInventoryDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierProductCompareDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierWarehouseDao;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierInventoryBatchUpdateBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierWarehousePo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierProductCompareBaseService;
import com.hete.support.api.enums.BooleanType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/1/16 14:07
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierInitService {

    private final SupplierProductCompareDao supplierProductCompareDao;
    private final PlmRemoteService plmRemoteService;
    private final SupplierInventoryDao supplierInventoryDao;
    private final SupplierDao supplierDao;
    private final SupplierWarehouseDao supplierWarehouseDao;
    private final PlmSkuDao plmSkuDao;
    private final SupplierProductCompareBaseService supplierProductCompareBaseService;

    private final static Integer LOOP_SIZE = 50;


    @Transactional(rollbackFor = Exception.class)
    public void initSupplierInventory() {
        final Long totalsCnt = supplierProductCompareDao.getTotalsCnt();
        for (int i = 1; i <= totalsCnt / LOOP_SIZE + 1; i++) {
            final IPage<SupplierProductComparePo> pageResult = supplierProductCompareDao.getByPage(PageDTO.of(i, LOOP_SIZE));
            final List<SupplierProductComparePo> records = pageResult.getRecords();

            final List<String> skuList = records.stream()
                    .map(SupplierProductComparePo::getSku)
                    .distinct()
                    .collect(Collectors.toList());

            final List<PlmGoodsDetailVo> plmGoodsDetailVoList = plmRemoteService.getCategoriesBySku(skuList);


            final List<SupplierInventoryPo> supplierInventoryPoList = records.stream()
                    .map(record -> {
                        final PlmGoodsDetailVo plmGoodsDetailVo = plmGoodsDetailVoList.stream()
                                .filter(vo -> vo.getSkuCodeList().contains(record.getSku()))
                                .findFirst()
                                .orElse(null);
                        if (null == plmGoodsDetailVo) {
                            log.error("scm供应商与sku对照关系中，存在plm没有的sku:{}", record.getSku());
                            return null;
                        }

                        final List<PlmCategoryVo> categoryList = plmGoodsDetailVo.getCategoryList();
                        PlmCategoryVo plmCategoryVo;
                        // 只有一级品类时，取一级品类
                        if (categoryList.size() == 1) {
                            plmCategoryVo = categoryList.get(0);
                        } else {
                            plmCategoryVo = categoryList.stream()
                                    .filter(categoryVo -> categoryVo.getLevel() == 2)
                                    .findFirst()
                                    .orElse(null);
                            if (null == plmCategoryVo) {
                                log.error("sku:{}在plm存在错误的类目关系", record.getSku());
                                return null;
                            }
                        }

                        final SupplierInventoryPo supplierInventoryPo = new SupplierInventoryPo();
                        supplierInventoryPo.setSupplierCode(record.getSupplierCode());
                        supplierInventoryPo.setSupplierName(record.getSupplierName());
                        supplierInventoryPo.setSpu(plmGoodsDetailVo.getSpuCode());
                        supplierInventoryPo.setSku(record.getSku());
                        supplierInventoryPo.setCategoryId(plmCategoryVo.getCategoryId());
                        supplierInventoryPo.setCategoryName(plmCategoryVo.getCategoryNameCn());

                        return supplierInventoryPo;
                    }).collect(Collectors.toList());

            supplierInventoryDao.insertBatch(supplierInventoryPoList);

        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void initSupplierWarehouse() {
        // 线上供应商数据数量在200以内，直接getAll不分页
        final List<SupplierPo> allSupplierList = supplierDao.getAllSupplierList();

        final List<SupplierWarehousePo> supplierWarehousePoList = new ArrayList<>();
        allSupplierList.forEach(supplierPo -> {
            final SupplierWarehousePo supplierWarehousePo1 = new SupplierWarehousePo();
            supplierWarehousePo1.setSupplierCode(supplierPo.getSupplierCode());
            supplierWarehousePo1.setSupplierName(supplierPo.getSupplierName());
            supplierWarehousePo1.setWarehouseCode(ScmConstant.SUPPLIER_WAREHOUSE_PREFIX + supplierPo.getSupplierCode()
                    + ScmConstant.DEFECTIVE_WAREHOUSE_SUFFIX);
            supplierWarehousePo1.setWarehouseName(supplierPo.getSupplierCode() + ScmConstant.DEFECTIVE_WAREHOUSE_SUFFIX_NAME);
            supplierWarehousePo1.setSupplierWarehouse(SupplierWarehouse.DEFECTIVE_WAREHOUSE);

            supplierWarehousePoList.add(supplierWarehousePo1);
        });

        supplierWarehouseDao.insertBatch(supplierWarehousePoList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void supProdCompareInit() {
        PageExecutor<SupplierProductComparePo> processOrderPoPageExecutor = new PageExecutor<>();

        processOrderPoPageExecutor.doForPage(supplierProductCompareDao::getInitData, records -> {
            Set<String> supCodeList = records.stream()
                    .map(SupplierProductComparePo::getSupplierCode)
                    .collect(Collectors.toSet());
            Set<String> skuList = records.stream()
                    .distinct()
                    .map(SupplierProductComparePo::getSku)
                    .collect(Collectors.toSet());

            List<SupplierProductComparePo> supProdComparePoList
                    = supplierProductCompareDao.listBySupCodeListAndSkuList(new ArrayList<>(supCodeList), new ArrayList<>(skuList));
            List<SupplierPo> supplierPoList = supplierDao.getBySupplierCodeList(new ArrayList<>(supCodeList));
            Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(new ArrayList<>(skuList));
            List<PlmSkuPo> plmSkuPoList = plmSkuDao.getList(new ArrayList<>(skuList));

            // 遍历订单列表
            for (SupplierProductComparePo supProdComparePo : records) {
                String supplierCode = supProdComparePo.getSupplierCode();
                String sku = supProdComparePo.getSku();

                SupplierProductComparePo existCompare = supProdComparePoList.stream()
                        .filter(po -> Objects.equals(po.getSupplierCode(), supplierCode) && Objects.equals(po.getSku(), sku))
                        .findFirst().orElse(null);
                if (Objects.isNull(existCompare)) {
                    log.info("供应商=>{} sku=>{} 对照关系不存在，开始初始化", supplierCode, sku);

                    SupplierPo matchSupplierPo = supplierPoList.stream()
                            .filter(supplierPo -> Objects.equals(supplierCode, supplierPo.getSupplierCode()))
                            .findFirst().orElse(null);
                    if (Objects.isNull(matchSupplierPo)) {
                        log.info("供应商=>{} 对应供应商信息不存在，跳过", supplierCode);
                        continue;
                    }

                    String skuEncode = skuEncodeMap.get(sku);
                    if (StrUtil.isBlank(skuEncode)) {
                        log.info("sku=>{} 对应plm系统不存在，跳过", sku);
                        continue;
                    }

                    PlmSkuPo matchPlmSku = plmSkuPoList.stream()
                            .filter(plmSkuPo -> Objects.equals(sku, plmSkuPo.getSku()))
                            .findFirst().orElse(null);
                    if (Objects.isNull(matchPlmSku)) {
                        log.info("sku=>{} 对应plm_sku不存在，跳过", sku);
                        continue;
                    }

                    supProdComparePo.setSupplierProductName(skuEncode);
                    supProdComparePo.setPlmSkuId(matchPlmSku.getPlmSkuId());
                    supProdComparePo.setSupplierName(matchSupplierPo.getSupplierName());
                    supProdComparePo.setSupplierProductCompareStatus(BooleanType.TRUE);
                    supplierProductCompareDao.insert(supProdComparePo);
                    log.info("供应商=>{} sku=>{} 对照关系初始化完成", supplierCode, sku);

                    //初始化供应商库存信息
                    SupplierInventoryBatchUpdateBo supplierInventoryBatchUpdateBo = new SupplierInventoryBatchUpdateBo();
                    supplierInventoryBatchUpdateBo.setSku(sku);
                    supplierInventoryBatchUpdateBo.setSupplierCode(supplierCode);
                    supplierInventoryBatchUpdateBo.setSupplierName(matchSupplierPo.getSupplierName());
                    supplierProductCompareBaseService.supplierInventoryUpdateBatch(Collections.singletonList(supplierInventoryBatchUpdateBo));
                    log.info("供应商=>{} sku=>{} 初始化供应商库存信息完成", supplierCode, sku);

                    matchPlmSku.setBindingSupplierProduct(BindingSupplierProduct.TRUE);
                    plmSkuDao.updateByIdVersion(matchPlmSku);
                    log.info("sku=>{} 初始化绑定状态完成", sku);
                }
            }
        });
    }
}
