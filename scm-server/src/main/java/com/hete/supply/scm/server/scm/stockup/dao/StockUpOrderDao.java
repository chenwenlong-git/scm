package com.hete.supply.scm.server.scm.stockup.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.StockUpSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.StockUpOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.StockUpExportVo;
import com.hete.supply.scm.server.scm.stockup.entity.po.StockUpOrderPo;
import com.hete.supply.scm.server.scm.stockup.entity.vo.StockUpSearchVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 备货单表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-01-08
 */
@Component
@Validated
public class StockUpOrderDao extends BaseDao<StockUpOrderMapper, StockUpOrderPo> {

    public CommonPageResult.PageInfo<StockUpSearchVo> searchStockUp(Page<StockUpSearchVo> page, StockUpSearchDto dto) {
        final IPage<StockUpSearchVo> pageResult = baseMapper.searchStockUp(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public List<StockUpOrderPo> getListByNoList(List<String> stockUpOrderNoList) {
        if (CollectionUtils.isEmpty(stockUpOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<StockUpOrderPo>lambdaQuery()
                .in(StockUpOrderPo::getStockUpOrderNo, stockUpOrderNoList));

    }

    public CommonPageResult.PageInfo<StockUpExportVo> getExportList(Page<StockUpExportVo> page, StockUpSearchDto dto) {
        final IPage<StockUpExportVo> pageResult = baseMapper.getExportList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }


    public Integer getExportTotals(StockUpSearchDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    public List<StockUpOrderPo> getBySkuListAndStatus(List<String> skuList,
                                                      List<StockUpOrderStatus> stockUpOrderStatusList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<StockUpOrderPo>lambdaQuery()
                .in(StockUpOrderPo::getSku, skuList)
                .in(StockUpOrderPo::getStockUpOrderStatus, stockUpOrderStatusList));
    }
}
