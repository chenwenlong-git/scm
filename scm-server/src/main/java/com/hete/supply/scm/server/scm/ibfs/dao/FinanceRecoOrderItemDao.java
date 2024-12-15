package com.hete.supply.scm.server.scm.ibfs.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.RecoOrderItemSearchDto;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderItemPo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.RecoOrderItemSearchVo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.RecoOrderItemTotalVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * <p>
 * 财务对账单明细表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-13
 */
@Component
@Validated
public class FinanceRecoOrderItemDao extends BaseDao<FinanceRecoOrderItemMapper, FinanceRecoOrderItemPo> {

    public CommonPageResult.PageInfo<RecoOrderItemSearchVo> searchRecoOrderItemPage(Page<Void> page, RecoOrderItemSearchDto dto) {
        IPage<RecoOrderItemSearchVo> pageResult = baseMapper.searchRecoOrderItemPage(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public Integer getRecoOrderItemTotalGroup(RecoOrderItemSearchDto dto) {
        return baseMapper.getRecoOrderItemTotalGroup(dto);
    }

    public Integer getRecoOrderItemTotalGroupSku(RecoOrderItemSearchDto dto) {
        return baseMapper.getRecoOrderItemTotalGroupSku(dto);
    }

    public List<String> getRecoOrderItemTotalGroupSkuList(RecoOrderItemSearchDto dto) {
        return baseMapper.getRecoOrderItemTotalGroupSkuList(dto);
    }

    public RecoOrderItemTotalVo getRecoOrderItemTotal(RecoOrderItemSearchDto dto) {
        return baseMapper.getRecoOrderItemTotal(dto);
    }
}
