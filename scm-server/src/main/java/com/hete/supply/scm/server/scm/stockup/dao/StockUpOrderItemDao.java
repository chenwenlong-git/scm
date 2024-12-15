package com.hete.supply.scm.server.scm.stockup.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.stockup.entity.bo.StockUpCntBo;
import com.hete.supply.scm.server.scm.stockup.entity.po.StockUpOrderItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 备货单项目表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-08
 */
@Component
@Validated
public class StockUpOrderItemDao extends BaseDao<StockUpOrderItemMapper, StockUpOrderItemPo> {

    public List<StockUpOrderItemPo> getListByStockUpOrderNo(String stockUpOrderNo) {
        if (StringUtils.isBlank(stockUpOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<StockUpOrderItemPo>lambdaQuery()
                .eq(StockUpOrderItemPo::getStockUpOrderNo, stockUpOrderNo));
    }

    public List<StockUpOrderItemPo> getListByStockUpOrderNoList(List<String> stockUpOrderNoList) {
        if (CollectionUtils.isEmpty(stockUpOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<StockUpOrderItemPo>lambdaQuery()
                .in(StockUpOrderItemPo::getStockUpOrderNo, stockUpOrderNoList));
    }

    public List<StockUpCntBo> getSumCntByNoList(List<String> stockUpOrderNoList) {
        return baseMapper.getSumCntByNoList(stockUpOrderNoList);
    }
}
