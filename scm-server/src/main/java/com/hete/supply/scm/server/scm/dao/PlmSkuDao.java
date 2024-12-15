package com.hete.supply.scm.server.scm.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.GoodsPriceDto;
import com.hete.supply.scm.api.scm.entity.dto.PlmSkuSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.ProduceDataSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.GoodsPriceExportVo;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataExportSkuAttrVo;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataExportSkuProcessVo;
import com.hete.supply.scm.api.scm.entity.vo.SkuProdSkuExportVo;
import com.hete.supply.scm.server.scm.adjust.entity.vo.GoodsPriceSearchVo;
import com.hete.supply.scm.server.scm.entity.po.PlmSkuPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProduceDataSearchVo;
import com.hete.supply.scm.server.scm.production.entity.vo.PlmSkuSearchVo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SkuCycleBo;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierProductCompareDto;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierProductComparePageVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * plm的产品信息表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-03-28
 */
@Component
@Validated
public class PlmSkuDao extends BaseDao<PlmSkuMapper, PlmSkuPo> {

    /**
     * 供应商产品分页
     *
     * @author ChenWenLong
     * @date 2023/3/29 10:20
     */
    public CommonPageResult.PageInfo<SupplierProductComparePageVo> selectSupplierProductPage(Page<Void> page, SupplierProductCompareDto dto) {
        IPage<SupplierProductComparePageVo> pageResult = baseMapper.selectSupplierProductPage(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 通过SKU查询单记录
     *
     * @author ChenWenLong
     * @date 2023/3/29 10:24
     */
    public PlmSkuPo getBySku(String sku) {
        if (StringUtils.isBlank(sku)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<PlmSkuPo>lambdaQuery()
                .eq(PlmSkuPo::getSku, sku));
    }

    public List<PlmSkuPo> getList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PlmSkuPo>lambdaQuery().in(PlmSkuPo::getSku, skuList));
    }

    public CommonPageResult.PageInfo<ProduceDataSearchVo> selectProduceDataPage(Page<Void> page, ProduceDataSearchDto dto) {
        IPage<ProduceDataSearchVo> pageResult = baseMapper.selectProduceDataPage(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public List<PlmSkuPo> getListBySpuList(List<String> spuList) {
        if (CollectionUtils.isEmpty(spuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PlmSkuPo>lambdaQuery().in(PlmSkuPo::getSpu, spuList));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public List<PlmSkuPo> getAll() {
        return list(Wrappers.lambdaQuery());
    }

    public Integer getExportSkuProcessTotals(ProduceDataSearchDto dto) {
        return baseMapper.getExportSkuProcessTotals(dto);
    }

    public CommonPageResult.PageInfo<ProduceDataExportSkuProcessVo> getSkuProcessExportList(Page<Void> page, ProduceDataSearchDto dto) {
        IPage<ProduceDataExportSkuProcessVo> pageResult = baseMapper.getSkuProcessExportList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public Long getTotalsCnt() {
        return baseMapper.selectCount(null);
    }

    public IPage<PlmSkuPo> getByPage(Page<PlmSkuPo> page) {
        return baseMapper.selectPage(page, null);
    }

    public Integer getExportSkuAttrTotals(ProduceDataSearchDto dto) {
        return baseMapper.getExportSkuAttrTotals(dto);
    }

    public CommonPageResult.PageInfo<ProduceDataExportSkuAttrVo> getSkuAttrExportList(Page<Void> page, ProduceDataSearchDto dto) {
        IPage<ProduceDataExportSkuAttrVo> pageResult = baseMapper.getSkuAttrExportList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public CommonPageResult.PageInfo<GoodsPriceSearchVo> searchGoodsPricePage(Page<Void> page, GoodsPriceDto dto) {
        IPage<GoodsPriceSearchVo> pageResult = baseMapper.searchGoodsPricePage(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public Integer getGoodsPriceExportTotals(GoodsPriceDto dto) {
        return baseMapper.getGoodsPriceExportTotals(dto);
    }

    public CommonPageResult.PageInfo<GoodsPriceExportVo> getGoodsPriceExportList(Page<Void> page, GoodsPriceDto dto) {
        IPage<GoodsPriceExportVo> pageResult = baseMapper.getGoodsPriceExportList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public List<SkuCycleBo> listByProduceCycleBo(Collection<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.listByProduceCycleBo(skuList);
    }

    public CommonPageResult.PageInfo<PlmSkuSearchVo> searchPlmSku(Page<Void> page, PlmSkuSearchDto dto) {
        IPage<PlmSkuSearchVo> pageResult = baseMapper.searchPlmSku(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public Integer getExportTotals(PlmSkuSearchDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    public CommonPageResult.PageInfo<SkuProdSkuExportVo> getExportList(Page<Void> page, PlmSkuSearchDto dto) {
        IPage<SkuProdSkuExportVo> pageResult = baseMapper.getExportList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);

    }
}
