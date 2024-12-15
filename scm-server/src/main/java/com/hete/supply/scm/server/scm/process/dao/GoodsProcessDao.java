package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.GoodsProcessQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.dto.GoodsProcessQueryDto;
import com.hete.supply.scm.api.scm.entity.vo.GoodsProcessExportVo;
import com.hete.supply.scm.server.scm.entity.po.GoodsProcessPo;
import com.hete.supply.scm.server.scm.entity.vo.GoodsProcessVo;
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
 * 商品工序管理表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Component
@Validated
public class GoodsProcessDao extends BaseDao<GoodsProcessMapper, GoodsProcessPo> {


    /**
     * 通过分页查询
     *
     * @param page
     * @param goodsProcessQueryDto
     * @return
     */
    public CommonPageResult.PageInfo<GoodsProcessVo> getByPage(Page<Void> page, GoodsProcessQueryDto goodsProcessQueryDto) {
        IPage<GoodsProcessVo> pageResult = baseMapper.getByPage(page, goodsProcessQueryDto);

        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 统计导出的总数
     *
     * @param dto
     * @return
     */
    public Integer getExportTotals(GoodsProcessQueryDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    /**
     * 查询导出的列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<GoodsProcessExportVo> getExportList(Page<Void> page, GoodsProcessQueryByApiDto dto) {
        IPage<GoodsProcessExportVo> pageResult = baseMapper.getExportList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 通过多个主键查询
     *
     * @param goodsProcessIds
     * @return
     */
    public List<GoodsProcessPo> getByGoodsProcessIds(List<Long> goodsProcessIds) {
        return list(Wrappers.<GoodsProcessPo>lambdaQuery()
                .in(GoodsProcessPo::getGoodsProcessId, goodsProcessIds));
    }

    /**
     * 通过 spu 查询数据
     *
     * @param sku
     * @return
     */
    public GoodsProcessPo getBySku(String sku) {
        return getOne(Wrappers.<GoodsProcessPo>lambdaQuery()
                .eq(GoodsProcessPo::getSku, sku));
    }

    /**
     * 根据sku列表查询
     *
     * @param skuCodeList
     * @return
     */
    public List<GoodsProcessPo> getBySkuList(List<String> skuCodeList) {
        if (CollectionUtils.isEmpty(skuCodeList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<GoodsProcessPo>lambdaQuery()
                .in(GoodsProcessPo::getSku, skuCodeList));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public GoodsProcessPo getNewestGoodsProcess(String processSku) {
        List<GoodsProcessPo> goodsProcessPos = list(Wrappers.<GoodsProcessPo>lambdaQuery()
                .eq(GoodsProcessPo::getSku, processSku).orderByDesc(GoodsProcessPo::getCreateTime));
        if (CollectionUtils.isEmpty(goodsProcessPos)) {
            return null;
        }
        return goodsProcessPos.stream().findFirst().orElse(null);
    }
}
