package com.hete.supply.scm.server.supplier.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.supplier.entity.po.ShippingMarkItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 箱唛分箱表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-02-13
 */
@Component
@Validated
public class ShippingMarkItemDao extends BaseDao<ShippingMarkItemMapper, ShippingMarkItemPo> {

    public List<ShippingMarkItemPo> getListByDeliverOrderNo(String deliverOrderNo) {
        if (StringUtils.isBlank(deliverOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<ShippingMarkItemPo>lambdaQuery()
                .eq(ShippingMarkItemPo::getDeliverOrderNo, deliverOrderNo));
    }

    public List<ShippingMarkItemPo> getListByNo(String shippingMarkNo) {
        if (StringUtils.isBlank(shippingMarkNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<ShippingMarkItemPo>lambdaQuery()
                .eq(ShippingMarkItemPo::getShippingMarkNo, shippingMarkNo));
    }

    public Map<String, List<ShippingMarkItemPo>> getShippingMarkNoItemMapByNoList(List<String> shippingMarkNoList) {
        if (CollectionUtils.isEmpty(shippingMarkNoList)) {
            return Collections.emptyMap();
        }

        return baseMapper.selectList(Wrappers.<ShippingMarkItemPo>lambdaQuery()
                        .in(ShippingMarkItemPo::getShippingMarkNo, shippingMarkNoList))
                .stream()
                .collect(Collectors.groupingBy(ShippingMarkItemPo::getShippingMarkNo));
    }

    public int removeBatchByIds(List<Long> shippingMarkItemIdList) {
        return baseMapper.deleteBatchIds(shippingMarkItemIdList);
    }

    public ShippingMarkItemPo getOneByShippingMarkNum(String shippingMarkNo, String shippingMarkNum) {
        return baseMapper.selectOne(Wrappers.<ShippingMarkItemPo>lambdaQuery()
                .eq(ShippingMarkItemPo::getShippingMarkNo, shippingMarkNo)
                .eq(ShippingMarkItemPo::getShippingMarkNum, shippingMarkNum)
                .last("limit 1"));
    }

    public List<ShippingMarkItemPo> getListByBizChildOrderNo(List<String> bizChildOrderNoList) {
        if (CollectionUtils.isEmpty(bizChildOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<ShippingMarkItemPo>lambdaQuery()
                .in(ShippingMarkItemPo::getBizChildOrderNo, bizChildOrderNoList));
    }

    /**
     * 通过发货单号模糊查询
     *
     * @param deliverOrderNo:
     * @return List<ShippingMarkItemPo>
     * @author ChenWenLong
     * @date 2023/7/13 16:43
     */
    public List<ShippingMarkItemPo> getListByLikeDeliverOrderNo(String deliverOrderNo) {
        if (StringUtils.isBlank(deliverOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<ShippingMarkItemPo>lambdaQuery()
                .like(ShippingMarkItemPo::getDeliverOrderNo, deliverOrderNo));
    }
}
