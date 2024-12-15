package com.hete.supply.scm.server.scm.develop.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopPricingOrderInfoByPriceTimeBo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPricingOrderInfoPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 核价单表详情信息 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-02
 */
@Component
@Validated
public class DevelopPricingOrderInfoDao extends BaseDao<DevelopPricingOrderInfoMapper, DevelopPricingOrderInfoPo> {

    public List<DevelopPricingOrderInfoPo> getListByDevelopSampleOrderNoList(List<String> developSampleOrderNoList) {
        if (CollectionUtils.isEmpty(developSampleOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopPricingOrderInfoPo>lambdaQuery()
                .in(DevelopPricingOrderInfoPo::getDevelopSampleOrderNo, developSampleOrderNoList));
    }

    public List<DevelopPricingOrderInfoPo> getListByLikeDevelopSampleOrderNo(String developSampleOrderNo) {
        if (StringUtils.isBlank(developSampleOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopPricingOrderInfoPo>lambdaQuery()
                .like(DevelopPricingOrderInfoPo::getDevelopSampleOrderNo, developSampleOrderNo));
    }

    public List<DevelopPricingOrderInfoPo> getListByDevelopPricingOrderNoList(List<String> developPricingOrderNoList) {
        if (CollectionUtils.isEmpty(developPricingOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopPricingOrderInfoPo>lambdaQuery()
                .in(DevelopPricingOrderInfoPo::getDevelopPricingOrderNo, developPricingOrderNoList));
    }

    public List<DevelopPricingOrderInfoPo> getListByDevelopPricingOrderNo(String developPricingOrderNo) {
        if (StringUtils.isBlank(developPricingOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopPricingOrderInfoPo>lambdaQuery()
                .eq(DevelopPricingOrderInfoPo::getDevelopPricingOrderNo, developPricingOrderNo));
    }

    public List<DevelopPricingOrderInfoPo> getListByDevelopChildOrderNoList(List<String> developChildOrderNoList) {
        if (CollectionUtils.isEmpty(developChildOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopPricingOrderInfoPo>lambdaQuery()
                .in(DevelopPricingOrderInfoPo::getDevelopChildOrderNo, developChildOrderNoList));
    }

    public List<DevelopPricingOrderInfoPo> getListByDevelopPamphletOrderNo(String developPamphletOrderNo) {
        if (StringUtils.isBlank(developPamphletOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopPricingOrderInfoPo>lambdaQuery()
                .eq(DevelopPricingOrderInfoPo::getDevelopPamphletOrderNo, developPamphletOrderNo));
    }

    public List<DevelopPricingOrderInfoByPriceTimeBo> getListBySampleOrderNoAndPriceTime(List<String> developSampleOrderNoList) {
        if (CollectionUtils.isEmpty(developSampleOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.getListBySampleOrderNoAndPriceTime(developSampleOrderNoList);
    }

    public List<DevelopPricingOrderInfoPo> getAll() {
        return list(Wrappers.lambdaQuery());
    }
}
