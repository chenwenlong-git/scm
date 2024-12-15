package com.hete.supply.scm.server.scm.supplier.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.api.scm.entity.dto.SkuGetSuggestSupplierDto;
import com.hete.supply.scm.api.scm.entity.enums.SupplierStatus;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierProductCompareDao;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SkuGetSuggestSupplierBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.support.api.enums.BooleanType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/8/5 17:17
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SkuGetSuggestSupplierStepOneHandler extends AbstractSkuGetSuggestSupplierHandler {

    private final SupplierProductCompareDao supplierProductCompareDao;
    private final SupplierDao supplierDao;

    @Override
    protected int sort() {
        return 1;
    }

    @Override
    protected SkuGetSuggestSupplierBo handleData(List<SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto> dtoList,
                                                 SkuGetSuggestSupplierBo resultBo) {
        log.info("推荐供应商第一步开始:入参的Dto={},入参结果Bo={}", JSON.toJSONString(dtoList), JSON.toJSONString(resultBo));
        if (CollectionUtils.isEmpty(dtoList)) {
            resultBo.setIsResult(BooleanType.TRUE);
            return resultBo;
        }
        // 实现第1步批量处理逻辑
        // 批量查询supplier_product_compare表，返回匹配的供应商列表
        List<String> skuList = dtoList.stream()
                .map(SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto::getSku)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());

        // 查询商品对照关系
        List<SupplierProductComparePo> supplierProductComparePoList = supplierProductCompareDao.getBySkuListAndStatus(skuList, BooleanType.TRUE);
        if (CollectionUtils.isEmpty(supplierProductComparePoList)) {
            resultBo.setIsResult(BooleanType.TRUE);
            return resultBo;
        }
        List<String> supplierCodeList = supplierProductComparePoList.stream()
                .map(SupplierProductComparePo::getSupplierCode)
                .distinct()
                .collect(Collectors.toList());
        Map<String, List<SupplierProductComparePo>> supplierProductComparePoMap = supplierProductComparePoList.stream()
                .collect(Collectors.groupingBy(SupplierProductComparePo::getSku));

        // 获取供应商信息
        List<SupplierPo> supplierPoList = supplierDao.getListBySupplierCodeListAndStatus(supplierCodeList, SupplierStatus.ENABLED);
        if (CollectionUtils.isEmpty(supplierPoList)) {
            resultBo.setIsResult(BooleanType.TRUE);
            return resultBo;
        }
        Map<String, SupplierPo> supplierPoMap = supplierPoList.stream()
                .collect(Collectors.toMap(SupplierPo::getSupplierCode, Function.identity()));

        // 组装返回数据
        for (SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto skuAndBusinessIdBatchDto : dtoList) {

            // 获取更新的BO
            SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo skuGetSuggestSupplierListBo = resultBo.getSkuGetSuggestSupplierBoList().stream()
                    .filter(bo -> bo.getBusinessId().equals(skuAndBusinessIdBatchDto.getBusinessId()))
                    .findFirst()
                    .orElse(null);

            String sku = skuAndBusinessIdBatchDto.getSku();
            List<SupplierProductComparePo> comparePoList = Optional.ofNullable(supplierProductComparePoMap.get(sku)).orElse(new ArrayList<>());
            if (CollectionUtils.isEmpty(comparePoList) && skuGetSuggestSupplierListBo != null) {
                skuGetSuggestSupplierListBo.setIsIdResult(BooleanType.TRUE);
                continue;
            }

            List<SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo.SkuGetSuggestSupplierItemBo> skuGetSuggestSupplierItemBoList = new ArrayList<>();
            List<SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo.SkuGetSuggestSupplierItemAllBo> skuGetSuggestSupplierItemAllBoList = new ArrayList<>();
            for (SupplierProductComparePo supplierProductComparePo : comparePoList) {
                String supplierCode = supplierProductComparePo.getSupplierCode();
                SupplierPo supplierPo = supplierPoMap.get(supplierCode);
                if (supplierPo == null) {
                    continue;
                }
                // 推荐供应商信息
                SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo.SkuGetSuggestSupplierItemBo skuGetSuggestSupplierItemBo = new SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo.SkuGetSuggestSupplierItemBo();
                skuGetSuggestSupplierItemBo.setSupplierCode(supplierPo.getSupplierCode());
                skuGetSuggestSupplierItemBo.setSupplierAlias(supplierPo.getSupplierAlias());
                skuGetSuggestSupplierItemBo.setIsDefault(BooleanType.FALSE);
                skuGetSuggestSupplierItemBo.setSupplierPo(supplierPo);
                skuGetSuggestSupplierItemBoList.add(skuGetSuggestSupplierItemBo);

                // 全部绑定供应商信息
                SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo.SkuGetSuggestSupplierItemAllBo skuGetSuggestSupplierItemAllBo = new SkuGetSuggestSupplierBo.SkuGetSuggestSupplierListBo.SkuGetSuggestSupplierItemAllBo();
                skuGetSuggestSupplierItemAllBo.setSupplierCode(supplierPo.getSupplierCode());
                skuGetSuggestSupplierItemAllBo.setSupplierAlias(supplierPo.getSupplierAlias());
                skuGetSuggestSupplierItemAllBoList.add(skuGetSuggestSupplierItemAllBo);

            }
            // 如果skuGetSuggestSupplierItemBoList的数量为1则设置isDefault为true
            if (skuGetSuggestSupplierItemBoList.size() == 1) {
                skuGetSuggestSupplierItemBoList.get(0).setIsDefault(BooleanType.TRUE);
            }

            if (skuGetSuggestSupplierListBo != null) {
                skuGetSuggestSupplierListBo.setSkuGetSuggestSupplierItemBoList(skuGetSuggestSupplierItemBoList);
                skuGetSuggestSupplierListBo.setSkuGetSuggestSupplierItemAllBoList(skuGetSuggestSupplierItemAllBoList);
            }
        }


        log.info("推荐供应商第一步结束:入参结果Bo={}", JSON.toJSONString(resultBo));

        return resultBo;
    }
}