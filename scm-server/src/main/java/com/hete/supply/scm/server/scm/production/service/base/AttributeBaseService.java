package com.hete.supply.scm.server.scm.production.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.SkuRisk;
import com.hete.supply.scm.server.scm.production.builder.ProdBuilder;
import com.hete.supply.scm.server.scm.production.dao.SkuRiskDao;
import com.hete.supply.scm.server.scm.production.dao.SkuRiskLogDao;
import com.hete.supply.scm.server.scm.production.entity.bo.CalSkuRiskPreBo;
import com.hete.supply.scm.server.scm.production.entity.bo.SkuRiskBo;
import com.hete.supply.scm.server.scm.production.entity.po.SkuRiskLogPo;
import com.hete.supply.scm.server.scm.production.entity.po.SkuRiskPo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/9/14.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AttributeBaseService {
    private final SkuRiskDao skuRiskDao;
    private final SkuRiskLogDao skuRiskLogDao;

    @Transactional(rollbackFor = Exception.class)
    public void clearSkuRisk() {
        List<Long> skuRiskIds = skuRiskDao.listAllIds();
        if (CollectionUtils.isNotEmpty(skuRiskIds)) {
            skuRiskDao.removeBatchByIds(skuRiskIds);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveSkuRisk(CalSkuRiskPreBo calSkuRiskPreBo) {
        Map<String, SkuRisk> skuRiskMap = calSkuRiskPreBo.getSkuRiskMap();
        TreeMap<String, BigDecimal> skuScoreMap = calSkuRiskPreBo.getSkuScoreMap();
        if (CollectionUtils.isEmpty(skuRiskMap)) {
            log.info("【保存SKU风险数据】：没有可计算的SKU风险数据，跳过保存。");
            return;
        }
        List<SkuRiskPo> skuRiskPos = ProdBuilder.buildSkuRiskPoList(skuRiskMap, skuScoreMap);
        List<SkuRiskLogPo> skuRiskLogPoList = calSkuRiskPreBo.getSkuRiskLogPoList();

        for (SkuRiskPo skuRiskPo : skuRiskPos) {
            String sku = skuRiskPo.getSku();
            skuRiskDao.insert(skuRiskPo);

            List<SkuRiskLogPo> matchSkuRiskLogPoList = skuRiskLogPoList.stream()
                    .filter(skuRiskLogPo -> Objects.equals(skuRiskLogPo.getSku(), sku))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(matchSkuRiskLogPoList)) {
                log.info("【保存SKU风险日志数据】：没有可计算的SKU风险日志数据，跳过保存。");
                continue;
            }
            Long skuRiskId = skuRiskPo.getSkuRiskId();
            matchSkuRiskLogPoList.forEach(skuRiskLogPo -> skuRiskLogPo.setSkuRiskId(skuRiskId));
            skuRiskLogDao.insertBatch(matchSkuRiskLogPoList);
        }
    }

    public List<SkuRiskBo> listBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            log.info("查询SKU风险数据返回结果为空！SKU编码列表为空。");
            return Collections.emptyList();
        }
        List<SkuRiskPo> skuRiskPoList = skuRiskDao.listBySkuList(skuList);
        return skuRiskPoList.stream().map(skuRiskPo -> {
            SkuRiskBo res = new SkuRiskBo();
            res.setSku(skuRiskPo.getSku());
            res.setLevel(skuRiskPo.getLevel());
            return res;
        }).collect(Collectors.toList());
    }


}


















