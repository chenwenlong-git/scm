package com.hete.supply.scm.server.scm.develop.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.api.scm.entity.enums.SampleRawBizType;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPamphletOrderRawPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 开发版单原料表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-01
 */
@Component
@Validated
public class DevelopPamphletOrderRawDao extends BaseDao<DevelopPamphletOrderRawMapper, DevelopPamphletOrderRawPo> {

    public List<DevelopPamphletOrderRawPo> getListByDevelopChildOrderNoAndType(String developChildOrderNo, SampleRawBizType type) {
        if (StringUtils.isBlank(developChildOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopPamphletOrderRawPo>lambdaQuery()
                .eq(DevelopPamphletOrderRawPo::getDevelopChildOrderNo, developChildOrderNo)
                .eq(DevelopPamphletOrderRawPo::getSampleRawBizType, type));
    }

    public List<DevelopPamphletOrderRawPo> getListByDevelopPamphletOrderNoAndType(String developPamphletOrderNo, SampleRawBizType type) {
        if (StringUtils.isBlank(developPamphletOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopPamphletOrderRawPo>lambdaQuery()
                .eq(DevelopPamphletOrderRawPo::getDevelopPamphletOrderNo, developPamphletOrderNo)
                .eq(DevelopPamphletOrderRawPo::getSampleRawBizType, type));
    }

    public List<DevelopPamphletOrderRawPo> getListByIdList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<DevelopPamphletOrderRawPo>lambdaQuery()
                .in(DevelopPamphletOrderRawPo::getDevelopPamphletOrderRawId, idList));
    }

    public List<DevelopPamphletOrderRawPo> getListByNoAndTypeAndSku(String developPamphletOrderNo, SampleRawBizType type, String sku) {
        if (StringUtils.isBlank(developPamphletOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopPamphletOrderRawPo>lambdaQuery()
                .eq(DevelopPamphletOrderRawPo::getDevelopPamphletOrderNo, developPamphletOrderNo)
                .eq(DevelopPamphletOrderRawPo::getSampleRawBizType, type)
                .eq(DevelopPamphletOrderRawPo::getSku, sku));
    }

    public List<DevelopPamphletOrderRawPo> getListByDevelopChildOrderNoListAndType(List<String> developChildOrderNoList, SampleRawBizType type) {
        if (CollectionUtils.isEmpty(developChildOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopPamphletOrderRawPo>lambdaQuery()
                .in(DevelopPamphletOrderRawPo::getDevelopChildOrderNo, developChildOrderNoList)
                .eq(DevelopPamphletOrderRawPo::getSampleRawBizType, type));
    }
}
