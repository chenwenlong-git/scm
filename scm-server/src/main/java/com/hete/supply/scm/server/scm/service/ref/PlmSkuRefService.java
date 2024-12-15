package com.hete.supply.scm.server.scm.service.ref;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.plm.api.goods.entity.vo.PlmGoodsDetailVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmSkuVo;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.dao.PlmSkuDao;
import com.hete.supply.scm.server.scm.entity.po.PlmSkuPo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * @date 2023年06月27日 11:57
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class PlmSkuRefService {
    private final PlmSkuDao plmSkuDao;
    private final PlmRemoteService plmRemoteService;

    /**
     * 返回供应链平台存在sku
     *
     * @param skuList 入参sku
     * @return List<String> 已存在skuList
     */
    public Set<String> filterExistScmSku(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptySet();
        }
        List<PlmSkuPo> plmSkuPoList = plmSkuDao.getList(skuList);
        if (CollectionUtils.isEmpty(plmSkuPoList)) {
            return Collections.emptySet();
        }
        Set<String> existSkuSet = plmSkuPoList.stream().map(PlmSkuPo::getSku).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(existSkuSet)) {
            return Collections.emptySet();
        }
        return skuList.stream().filter(existSkuSet::contains).collect(Collectors.toSet());
    }

    /**
     * 返回plm平台存在sku
     *
     * @param skuList
     * @return List<String> 已存在skuList
     */
    public List<String> filterExistPlmSku(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        List<PlmSkuVo> skuEncodeBySku = plmRemoteService.getSkuEncodeBySku(skuList);
        if (CollectionUtils.isEmpty(skuEncodeBySku)) {
            return Collections.emptyList();
        }
        List<String> existSkuList = skuEncodeBySku.stream().map(PlmSkuVo::getSkuCode).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(existSkuList)) {
            return Collections.emptyList();
        }
        return skuList.stream().filter(existSkuList::contains).collect(Collectors.toList());
    }

    /**
     * @param sku
     * @Description 查询同一个spu下的sku列表
     * @author yanjiawei
     * @Date 2024/11/21 13:57
     */
    public List<String> listSameSpuSkuList(String sku) {
        if (StringUtils.isBlank(sku)) {
            log.info("查询同一个spu下的sku列表结束！sku为空");
            return Collections.emptyList();
        }

        List<PlmGoodsDetailVo> skuDetailList = plmRemoteService.getGoodsDetail(Collections.singletonList(sku));
        if (CollectionUtils.isEmpty(skuDetailList)) {
            log.info("查询同一个spu下的sku列表结束！sku=>{}查询plm返回为空", sku);
            return Collections.emptyList();
        }

        PlmGoodsDetailVo plmGoodsDetailVo = skuDetailList.stream().findFirst().orElse(null);
        if (plmGoodsDetailVo == null) {
            log.info("查询同一个spu下的sku列表结束！sku=>{}查询plm返回为空", sku);
            return Collections.emptyList();
        }

        List<String> skuCodeList = Optional.ofNullable(plmGoodsDetailVo.getSkuCodeList()).orElse(Collections.emptyList());
        skuCodeList.removeIf(skuCode -> Objects.equals(skuCode, sku));
        return skuCodeList;
    }
}














