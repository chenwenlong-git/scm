package com.hete.supply.scm.server.scm.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.entity.dto.SkuAttrItemDto;
import com.hete.supply.scm.server.scm.entity.dto.SkuAttrPricePageDto;
import com.hete.supply.scm.server.scm.entity.po.SkuAttrPricePo;
import com.hete.supply.scm.server.scm.entity.vo.SkuAttrPricePageVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * sku属性定价表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-09
 */
@Component
@Validated
public class SkuAttrPriceDao extends BaseDao<SkuAttrPriceMapper, SkuAttrPricePo> {

    public CommonPageResult.PageInfo<SkuAttrPricePageVo> skuAttrPriceList(Page<Void> page, SkuAttrPricePageDto dto) {
        return PageInfoUtil.getPageInfo(baseMapper.searchSkuAttrPricePage(page, dto));
    }

    public List<SkuAttrPricePo> getListBySkuAndAttr(List<? extends SkuAttrItemDto> itemDtoList) {
        if (CollectionUtils.isEmpty(itemDtoList)) {
            return Collections.emptyList();
        }
        return baseMapper.getListBySkuAndAttr(itemDtoList);
    }

    public void delByIdList(List<Long> skuAttrPriceIdList) {
        baseMapper.deleteBatchIds(skuAttrPriceIdList);
    }

    public List<SkuAttrPricePo> getByIdList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectBatchIds(idList);
    }
}
