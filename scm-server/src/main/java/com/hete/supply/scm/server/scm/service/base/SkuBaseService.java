package com.hete.supply.scm.server.scm.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.plm.api.goods.entity.vo.PlmSkuVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmVariantVo;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.dao.PlmSkuDao;
import com.hete.supply.scm.server.scm.dao.SkuInfoDao;
import com.hete.supply.scm.server.scm.entity.bo.SyncSkuBo;
import com.hete.supply.scm.server.scm.entity.dto.SkuListDto;
import com.hete.supply.scm.server.scm.entity.po.GoodsProcessPo;
import com.hete.supply.scm.server.scm.entity.po.PlmSkuPo;
import com.hete.supply.scm.server.scm.entity.po.SkuInfoPo;
import com.hete.supply.scm.server.scm.entity.vo.SkuVariantAttrVo;
import com.hete.supply.scm.server.scm.process.dao.GoodsProcessDao;
import com.hete.supply.scm.server.scm.service.ref.ProduceDataRefService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * @date 2023年06月27日 00:13
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class SkuBaseService {
    private final PlmSkuDao plmSkuDao;
    private final GoodsProcessDao goodsProcessDao;
    private final PlmRemoteService plmRemoteService;
    private final SkuInfoDao skuInfoDao;
    private final ProduceDataRefService produceDataRefService;

    @Transactional(rollbackFor = Exception.class)
    public void saveSkuBatch(SyncSkuBo syncSkuBo) {
        List<PlmSkuPo> plmSkuPoList = syncSkuBo.getPlmSkuPoList();
        List<GoodsProcessPo> goodsProcessPoList = syncSkuBo.getGoodsProcessPoList();

        if (CollectionUtils.isNotEmpty(plmSkuPoList)) {
            // 创建PlmSku时需要进行处理sku生产属性
            produceDataRefService.createPlmSkuUpdateProduceDataAttr(plmSkuPoList);
        }
        if (CollectionUtils.isNotEmpty(goodsProcessPoList)) {
            goodsProcessDao.insertBatch(goodsProcessPoList);
        }
    }

    public List<SkuVariantAttrVo> getSkuDetailBySkuList(SkuListDto dto) {
        List<PlmVariantVo> plmVariantVoList = plmRemoteService.getVariantAttr(dto.getSkuList());

        return Optional.ofNullable(plmVariantVoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(plmVo -> {
                    final SkuVariantAttrVo skuVariantAttrVo = new SkuVariantAttrVo();
                    skuVariantAttrVo.setSku(plmVo.getSkuCode());
                    skuVariantAttrVo.setVariantSkuList(plmVo.getVariantSkuList());
                    return skuVariantAttrVo;
                }).collect(Collectors.toList());
    }

    /**
     * 一次性检验PLM两边sku数据
     *
     * @param plmSkuPoList:
     * @return void
     * @author ChenWenLong
     * @date 2023/10/16 17:37
     */
    public void delSpuBatch(List<PlmSkuPo> plmSkuPoList, List<String> updateSkuList, List<String> delSkuList) {
        if (CollectionUtils.isEmpty(plmSkuPoList)) {
            return;
        }
        List<String> skuList = plmSkuPoList.stream().map(PlmSkuPo::getSku).distinct().collect(Collectors.toList());
        final List<PlmSkuVo> plmSkuVoList = plmRemoteService.getSkuEncodeBySku(skuList);

        List<Long> delPlmSkuPoIdList = new ArrayList<>();
        List<PlmSkuPo> updatePlmSkuPoList = new ArrayList<>();
        for (PlmSkuPo plmSkuPo : plmSkuPoList) {
            PlmSkuVo plmSkuVo = plmSkuVoList.stream().filter(vo -> vo.getSkuCode().equals(plmSkuPo.getSku())).findFirst().orElse(null);
            if (plmSkuVo == null || StringUtils.isBlank(plmSkuVo.getSpuCode())) {
                delPlmSkuPoIdList.add(plmSkuPo.getPlmSkuId());
                delSkuList.add(plmSkuPo.getSku());
            }
            if (plmSkuVo != null && StringUtils.isNotBlank(plmSkuVo.getSpuCode())) {
                plmSkuPo.setSpu(plmSkuVo.getSpuCode());
                updatePlmSkuPoList.add(plmSkuPo);
                updateSkuList.add(plmSkuPo.getSku());
            }
        }
        if (CollectionUtils.isNotEmpty(delPlmSkuPoIdList)) {
            plmSkuDao.removeBatchByIds(delPlmSkuPoIdList);
        }

        if (CollectionUtils.isNotEmpty(updatePlmSkuPoList)) {
            plmSkuDao.updateBatchByIdVersion(updatePlmSkuPoList);
        }
    }

    /**
     * 更新sku单件产能
     *
     * @param sku:
     * @param singleCapacity:
     * @return void
     * @author ChenWenLong
     * @date 2024/8/1 16:05
     */
    public void saveSkuInfoSingleCapacity(@NotEmpty String sku, @NotNull BigDecimal singleCapacity) {
        SkuInfoPo skuInfoPo = skuInfoDao.getBySku(sku);
        if (skuInfoPo == null) {
            SkuInfoPo insertSkuInfoPo = new SkuInfoPo();
            insertSkuInfoPo.setSku(sku);
            insertSkuInfoPo.setSingleCapacity(singleCapacity);
            skuInfoDao.insert(insertSkuInfoPo);
        } else {
            skuInfoPo.setSingleCapacity(singleCapacity);
            skuInfoDao.updateByIdVersion(skuInfoPo);
        }
    }


}
