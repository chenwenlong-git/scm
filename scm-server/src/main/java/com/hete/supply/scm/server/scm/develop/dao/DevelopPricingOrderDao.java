package com.hete.supply.scm.server.scm.develop.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.DevelopPricingOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.DevelopPricingOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.DevelopPricingOrderExportVo;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopPricingGroupByStatusBo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPricingOrderPo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopPricingOrderSearchVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 核价单表列表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-02
 */
@Component
@Validated
public class DevelopPricingOrderDao extends BaseDao<DevelopPricingOrderMapper, DevelopPricingOrderPo> {


    public CommonPageResult.PageInfo<DevelopPricingOrderSearchVo> search(Page<Void> page, DevelopPricingOrderSearchDto dto) {
        IPage<DevelopPricingOrderSearchVo> pageResult = baseMapper.search(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public Integer getExportTotals(DevelopPricingOrderSearchDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    public CommonPageResult.PageInfo<DevelopPricingOrderExportVo> getExportList(Page<Void> page, DevelopPricingOrderSearchDto dto) {
        IPage<DevelopPricingOrderExportVo> pageResult = baseMapper.getExportList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public DevelopPricingOrderPo getByDevelopPricingOrderNo(String developPricingOrderNo) {
        return baseMapper.selectOne(Wrappers.<DevelopPricingOrderPo>lambdaQuery()
                .eq(DevelopPricingOrderPo::getDevelopPricingOrderNo, developPricingOrderNo));
    }

    public List<DevelopPricingOrderPo> getByDevelopPricingOrderNoList(List<String> developPricingOrderNoList) {
        if (CollectionUtils.isEmpty(developPricingOrderNoList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<DevelopPricingOrderPo>lambdaQuery()
                .in(DevelopPricingOrderPo::getDevelopPricingOrderNo, developPricingOrderNoList));
    }


    public List<DevelopPricingOrderPo> getListByDevelopChildOrderNoList(List<String> developChildOrderNoList) {
        if (CollectionUtils.isEmpty(developChildOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopPricingOrderPo>lambdaQuery()
                .in(DevelopPricingOrderPo::getDevelopChildOrderNo, developChildOrderNoList));
    }

    public List<DevelopPricingGroupByStatusBo> getListByGroupByStatus(List<String> supplierCodeList) {
        return baseMapper.getListByGroupByStatus(supplierCodeList);
    }

    public List<DevelopPricingOrderPo> getListByStatusList(List<DevelopPricingOrderStatus> statusList) {
        if (CollectionUtils.isEmpty(statusList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopPricingOrderPo>lambdaQuery()
                .in(DevelopPricingOrderPo::getDevelopPricingOrderStatus, statusList));
    }

    /**
     * 获取最新的一条
     *
     * @param developChildOrderNo:
     * @return DevelopPricingOrderPo
     * @author ChenWenLong
     * @date 2023/9/5 09:59
     */
    public DevelopPricingOrderPo getListByChildOrderNoOrderByTime(String developChildOrderNo) {
        return baseMapper.selectOne(Wrappers.<DevelopPricingOrderPo>lambdaQuery()
                .eq(DevelopPricingOrderPo::getDevelopChildOrderNo, developChildOrderNo)
                .orderByDesc(DevelopPricingOrderPo::getCreateTime));
    }

    public List<DevelopPricingOrderPo> getListByChildOrderNoListAndStatus(List<String> developChildOrderNoList,
                                                                          @NotEmpty List<DevelopPricingOrderStatus> developPricingOrderStatusList) {
        if (CollectionUtils.isEmpty(developChildOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DevelopPricingOrderPo>lambdaQuery()
                .in(DevelopPricingOrderPo::getDevelopChildOrderNo, developChildOrderNoList)
                .in(DevelopPricingOrderPo::getDevelopPricingOrderStatus, developPricingOrderStatusList));
    }
}
