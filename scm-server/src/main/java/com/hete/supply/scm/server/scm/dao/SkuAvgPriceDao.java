package com.hete.supply.scm.server.scm.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.entity.bo.SkuAvgUpdateBo;
import com.hete.supply.scm.server.scm.entity.po.SkuAvgPricePo;
import com.hete.supply.scm.server.scm.enums.SkuAvgPriceBizType;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * sku均价表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-31
 */
@Component
@Validated
public class SkuAvgPriceDao extends BaseDao<SkuAvgPriceMapper, SkuAvgPricePo> {

    public List<SkuAvgPricePo> getListBySkuList(List<String> skuList, SkuAvgPriceBizType skuAvgPriceBizType) {
        if (CollectionUtils.isEmpty(skuList) || null == skuAvgPriceBizType) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SkuAvgPricePo>lambdaQuery()
                .eq(SkuAvgPricePo::getSkuAvgPriceBizType, skuAvgPriceBizType)
                .in(SkuAvgPricePo::getSku, skuList));
    }

    public void updateSkuAvgPrice(List<SkuAvgUpdateBo> skuAvgUpdateBoList) {
        if (CollectionUtils.isEmpty(skuAvgUpdateBoList)) {
            return;
        }

        baseMapper.updateSkuAvgPrice(skuAvgUpdateBoList);
    }
}
