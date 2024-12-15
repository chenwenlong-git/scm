package com.hete.supply.scm.server.scm.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.plm.api.goods.entity.vo.PlmSkuVo;
import com.hete.supply.scm.api.scm.entity.dto.SkuCodeListDto;
import com.hete.supply.scm.api.scm.entity.enums.GoodsProcessStatus;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseSkuCntVo;
import com.hete.supply.scm.api.scm.entity.vo.SkuProduceDataVo;
import com.hete.supply.scm.api.scm.entity.vo.SkuRelatedDataVo;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.dao.PlmSkuDao;
import com.hete.supply.scm.server.scm.dao.ProduceDataDao;
import com.hete.supply.scm.server.scm.entity.bo.SyncSkuBo;
import com.hete.supply.scm.server.scm.entity.dto.PlmCreateDto;
import com.hete.supply.scm.server.scm.entity.po.GoodsProcessPo;
import com.hete.supply.scm.server.scm.entity.po.PlmSkuPo;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataPo;
import com.hete.supply.scm.server.scm.enums.BindingSupplierProduct;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseBaseService;
import com.hete.supply.scm.server.scm.qc.entity.bo.QcWaitDetailBo;
import com.hete.supply.scm.server.scm.qc.service.base.QcOrderBaseService;
import com.hete.supply.scm.server.scm.service.base.SkuBaseService;
import com.hete.supply.scm.server.scm.service.ref.GoodsProcessRefService;
import com.hete.supply.scm.server.scm.service.ref.PlmSkuRefService;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierProductCompareDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.wms.api.basic.entity.vo.WarehouseVo;
import com.hete.support.api.entity.dto.ComPageDto;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.core.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * @date 2023年06月27日 11:55
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SkuBizService {
    private final GoodsProcessRefService goodsProcessRefService;
    private final PlmSkuRefService plmSkuRefService;
    private final PlmRemoteService plmRemoteService;
    private final SkuBaseService skuBaseService;
    private final PlmSkuDao plmSkuDao;
    private final ProduceDataDao produceDataDao;
    private final PurchaseBaseService purchaseBaseService;
    private final QcOrderBaseService qcOrderBaseService;
    private final WmsRemoteService wmsRemoteService;
    private final SupplierProductCompareDao supplierProductCompareDao;
    private final static Integer INIT_GOODS_PROCESS_SIZE = 500;

    public void batchInsertGoodsProcess() {
        for (int i = 0; ; i++) {
            final ComPageDto comPageDto = new ComPageDto();
            comPageDto.setPageNo(i + 1);
            comPageDto.setPageSize(INIT_GOODS_PROCESS_SIZE);
            final List<String> skuList = plmRemoteService.getSkuList(comPageDto);
            if (CollectionUtils.isNotEmpty(skuList)) {
                SyncSkuBo syncSkuBo = this.convertToBoBySkus(skuList);
                skuBaseService.saveSkuBatch(syncSkuBo);
            }
            if (CollectionUtils.isEmpty(skuList) || skuList.size() < INIT_GOODS_PROCESS_SIZE) {
                break;
            }
        }
    }

    public void syncSku(PlmCreateDto plmCreateDto) {
        // 检查sku是否存在供应链系统
        final String skuParam = plmCreateDto.getSku();
        Set<String> existScmSku = plmSkuRefService.filterExistScmSku(Collections.singletonList(skuParam));
        if (CollectionUtils.isNotEmpty(existScmSku) && existScmSku.contains(skuParam)) {
            throw new ParamIllegalException("您添加的商品sku已经存在该列表，请勿重复添加！");
        }

        // 检查sku是否存在PLM系统
        List<String> existPlmSku = plmSkuRefService.filterExistPlmSku(Collections.singletonList(skuParam));
        if (CollectionUtils.isEmpty(existPlmSku) || !existPlmSku.contains(skuParam)) {
            throw new ParamIllegalException("您添加的商品sku不存在商品系统中，请确认后再添加！");
        }
        // 转换Bo并保存
        SyncSkuBo syncSkuBo = this.convertToBoBySkus(Collections.singletonList(skuParam));
        skuBaseService.saveSkuBatch(syncSkuBo);
    }

    public SyncSkuBo convertToBoBySkus(List<String> skuList) {
        SyncSkuBo syncSkuBo = new SyncSkuBo();
        if (CollectionUtils.isEmpty(skuList)) {
            return null;
        }

        final List<PlmSkuVo> plmSkuVoList = plmRemoteService.getSkuEncodeBySku(skuList);

        Set<String> insertPlmSkus;
        Set<String> insertGoodsProcessSkus;

        // 获取不存在供应链系统的sku && 获取不存在产品加工工序的sku
        Set<String> existScmSku = plmSkuRefService.filterExistScmSku(skuList);
        if (CollectionUtils.isEmpty(existScmSku)) {
            insertPlmSkus = new HashSet<>(skuList);
        } else {
            insertPlmSkus = skuList.stream().filter(skuParam -> !existScmSku.contains(skuParam)).collect(Collectors.toSet());
        }
        Set<String> existGoodsProcessSkus = goodsProcessRefService.filterExistSkus(skuList);
        if (CollectionUtils.isEmpty(existGoodsProcessSkus)) {
            insertGoodsProcessSkus = new HashSet<>(skuList);
        } else {
            insertGoodsProcessSkus = skuList.stream().filter(skuParam -> !existGoodsProcessSkus.contains(skuParam)).collect(Collectors.toSet());
        }

        // 转换对应实体
        if (CollectionUtils.isNotEmpty(insertPlmSkus)) {
            // 查询产品对照是否存在
            Map<String, List<SupplierProductComparePo>> supplierProductComparePoMap = supplierProductCompareDao.getBySkuList(new ArrayList<>(insertPlmSkus));
            List<PlmSkuPo> plmSkuPoList = insertPlmSkus.stream().map(insertSku -> {
                PlmSkuPo plmSkuPo = new PlmSkuPo();
                plmSkuVoList.stream().filter(w -> w.getSkuCode()
                                .equals(insertSku)).findFirst()
                        .ifPresent(plmSkuVo -> plmSkuPo.setSpu(plmSkuVo.getSpuCode()));
                plmSkuPo.setSku(insertSku);
                if (supplierProductComparePoMap.containsKey(insertSku)) {
                    plmSkuPo.setBindingSupplierProduct(BindingSupplierProduct.TRUE);
                } else {
                    plmSkuPo.setBindingSupplierProduct(BindingSupplierProduct.FALSE);
                }
                return plmSkuPo;
            }).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(plmSkuPoList)) {
                syncSkuBo.setPlmSkuPoList(plmSkuPoList);
            }
        }
        if (CollectionUtils.isNotEmpty(insertGoodsProcessSkus)) {
            List<GoodsProcessPo> goodsProcessPoList = insertGoodsProcessSkus.stream().map(insertSku -> {
                GoodsProcessPo goodsProcessPo = new GoodsProcessPo();
                goodsProcessPo.setSku(insertSku);
                goodsProcessPo.setGoodsProcessStatus(GoodsProcessStatus.UNBINDED);
                return goodsProcessPo;
            }).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(goodsProcessPoList)) {
                syncSkuBo.setGoodsProcessPoList(goodsProcessPoList);
            }
        }
        return syncSkuBo;
    }


    /**
     * 通过sku获取重量、生产周期信息
     *
     * @param dto:
     * @return List<SkuProduceDataVo>
     * @author ChenWenLong
     * @date 2023/10/12 15:24
     */
    public List<SkuProduceDataVo> getSkuProduceDataBySkuList(SkuCodeListDto dto) {
        List<SkuProduceDataVo> skuProduceDataVoList = new ArrayList<>();
        List<PlmSkuPo> plmSkuPoList = plmSkuDao.getListBySpuList(dto.getSkuCodeList());
        //获取重量信息
        List<ProduceDataPo> produceDataPoList = produceDataDao.getListBySkuList(dto.getSkuCodeList());
        for (PlmSkuPo plmSkuPo : plmSkuPoList) {
            SkuProduceDataVo vo = new SkuProduceDataVo();
            vo.setSku(plmSkuPo.getSku());
            vo.setSpu(plmSkuPo.getSpu());
            vo.setCycle(plmSkuPo.getCycle());
            produceDataPoList.stream()
                    .filter(po -> po.getSku().equals(plmSkuPo.getSku()))
                    .findFirst()
                    .ifPresent(produceDataPo -> vo.setWeight(produceDataPo.getWeight()));
            skuProduceDataVoList.add(vo);
        }
        return skuProduceDataVoList;
    }

    /**
     * plm通过sku获取采购或质检的信息
     *
     * @param dto:
     * @return List<SkuRelatedDataVo>
     * @author ChenWenLong
     * @date 2023/12/26 14:02
     */
    public List<SkuRelatedDataVo> getSkuRelatedDataBySkuList(SkuCodeListDto dto) {
        log.info("plm通过sku获取采购或质检的信息,dto={}", JacksonUtil.parse2Str(dto));
        List<String> skuCodeList = dto.getSkuCodeList();
        List<SkuRelatedDataVo> voList = new ArrayList<>();

        //采购未交数量
        List<PurchaseSkuCntVo> purchaseUndeliveredCntList = purchaseBaseService.getPurchaseUndeliveredCnt(dto);

        //获取采购在途数量
        List<PurchaseSkuCntVo> purchaseInTransitCntList = purchaseBaseService.getPurchaseInTransitCnt(dto);

        // 获取待质检数据
        List<QcWaitDetailBo> qcWaitDetailBoList = qcOrderBaseService.getQcWaitDetailBySkuCodeList(skuCodeList);

        //待质检sku数量（sku+仓库维度）
        Map<String, Integer> waitQcSkuWarehouseAmountMap = qcWaitDetailBoList.stream().collect(Collectors.groupingBy(it -> it.getSkuCode() + "_" + it.getWarehouseCode(),
                Collectors.collectingAndThen(Collectors.toList(), pos -> pos.stream().mapToInt(QcWaitDetailBo::getAmount).sum())));

        // 获取仓库名称
        List<String> warehouseCodeList = qcWaitDetailBoList.stream()
                .map(QcWaitDetailBo::getWarehouseCode)
                .distinct()
                .collect(Collectors.toList());
        List<WarehouseVo> warehouseVoList = wmsRemoteService.getWarehouseByCode(warehouseCodeList);

        for (String skuCode : skuCodeList) {
            SkuRelatedDataVo skuRelatedDataVo = new SkuRelatedDataVo();
            skuRelatedDataVo.setSkuCode(skuCode);

            purchaseUndeliveredCntList.stream()
                    .filter(vo -> skuCode.equals(vo.getSkuCode()))
                    .findFirst()
                    .ifPresent(vo -> skuRelatedDataVo.setPurchaseUndeliveredCnt(vo.getSkuCnt()));

            purchaseInTransitCntList.stream()
                    .filter(vo -> skuCode.equals(vo.getSkuCode()))
                    .findFirst()
                    .ifPresent(vo -> skuRelatedDataVo.setPurchaseInTransitCnt(vo.getSkuCnt()));

            List<SkuRelatedDataVo.WarehouseInventory> inventoryList = qcWaitDetailBoList.stream()
                    .filter(vo -> skuCode.equals(vo.getSkuCode()))
                    .map(bo -> {
                        SkuRelatedDataVo.WarehouseInventory warehouseInventory = new SkuRelatedDataVo.WarehouseInventory();
                        warehouseInventory.setWarehouseCode(bo.getWarehouseCode());

                        warehouseVoList.stream()
                                .filter(warehouseVo -> warehouseVo.getWarehouseCode().equals(bo.getWarehouseCode()))
                                .findFirst()
                                .ifPresent(warehouseVoItem -> warehouseInventory.setWarehouseName(warehouseVoItem.getWarehouseName()));

                        warehouseInventory.setToCheckAmount(waitQcSkuWarehouseAmountMap.getOrDefault(skuCode + "_" + warehouseInventory.getWarehouseCode(), 0));

                        return warehouseInventory;
                    }).collect(Collectors.toList());

            skuRelatedDataVo.setInventoryList(inventoryList);

            Integer totalToCheckAmount = inventoryList.stream().mapToInt(SkuRelatedDataVo.WarehouseInventory::getToCheckAmount).sum();
            skuRelatedDataVo.setTotalToCheckAmount(totalToCheckAmount);

            voList.add(skuRelatedDataVo);
        }

        log.info("plm通过sku获取采购或质检的信息,vo={}", JacksonUtil.parse2Str(voList));
        return voList;

    }
}
