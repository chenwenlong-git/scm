package com.hete.supply.scm.server.scm.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 生产信息详情表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-21
 */
@Component
@Validated
public class ProduceDataItemDao extends BaseDao<ProduceDataItemMapper, ProduceDataItemPo> {

    public List<ProduceDataItemPo> getListBySku(String sku) {
        if (StringUtils.isBlank(sku)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataItemPo>lambdaQuery()
                .eq(ProduceDataItemPo::getSku, sku)
                .orderByDesc(ProduceDataItemPo::getSort)
                .orderByDesc(ProduceDataItemPo::getProduceDataItemId));
    }

    public List<ProduceDataItemPo> getListBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataItemPo>lambdaQuery()
                .in(ProduceDataItemPo::getSku, skuList)
                .orderByDesc(ProduceDataItemPo::getSort)
                .orderByDesc(ProduceDataItemPo::getProduceDataItemId));
    }

    /**
     * 批量删除
     *
     * @param list:
     * @return boolean
     * @author ChenWenLong
     * @date 2023/8/23 17:02
     */
    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        return super.removeBatchByIds(list);
    }

    public List<ProduceDataItemPo> getListByBusinessNoList(List<String> businessNoList) {
        if (CollectionUtils.isEmpty(businessNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataItemPo>lambdaQuery()
                .in(ProduceDataItemPo::getBusinessNo, businessNoList));
    }

    /**
     * 只获取优先级最高一条
     *
     * @param sku:
     * @return ProduceDataItemPo
     * @author ChenWenLong
     * @date 2024/1/23 11:17
     */
    public ProduceDataItemPo getOneBySku(String sku) {
        if (StringUtils.isBlank(sku)) {
            return null;
        }
        return getOne(Wrappers.<ProduceDataItemPo>lambdaQuery()
                .eq(ProduceDataItemPo::getSku, sku)
                .orderByDesc(ProduceDataItemPo::getSort)
                .orderByDesc(ProduceDataItemPo::getProduceDataItemId)
                .last("limit 1"));
    }


}
