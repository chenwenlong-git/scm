package com.hete.supply.scm.server.scm.purchase.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseSearchNewDto;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseParentOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseParentExportVo;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseParentSkuExportVo;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseSearchBo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseParentOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseSearchNewVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 采购需求母单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-01
 */
@Component
@Validated
public class PurchaseParentOrderDao extends BaseDao<PurchaseParentOrderMapper, PurchaseParentOrderPo> {

    public CommonPageResult.PageInfo<PurchaseSearchNewVo> searchPurchaseNew(Page<Void> page, PurchaseSearchNewDto dto, PurchaseSearchBo purchaseSearchBo) {
        return PageInfoUtil.getPageInfo(baseMapper.searchPurchaseNew(page, dto, purchaseSearchBo));

    }

    public Integer exportPurchaseParentTotals(PurchaseSearchNewDto dto, PurchaseSearchBo purchaseSearchBo) {
        return baseMapper.exportPurchaseParentTotals(dto, purchaseSearchBo);
    }

    public CommonPageResult.PageInfo<PurchaseParentExportVo> exportPurchaseParent(Page<Void> page, PurchaseSearchNewDto dto, PurchaseSearchBo purchaseSearchBo) {
        return PageInfoUtil.getPageInfo(baseMapper.exportPurchaseParent(page, dto, purchaseSearchBo));

    }

    public Integer exportPurchaseParentBySkuTotals(PurchaseSearchNewDto dto, PurchaseSearchBo purchaseSearchBo) {
        return baseMapper.exportPurchaseParentBySkuTotals(dto, purchaseSearchBo);
    }


    public CommonPageResult.PageInfo<PurchaseParentSkuExportVo> exportPurchaseParentBySku(Page<Void> page,
                                                                                          PurchaseSearchNewDto dto,
                                                                                          PurchaseSearchBo purchaseSearchBo) {
        return PageInfoUtil.getPageInfo(baseMapper.exportPurchaseParentBySku(page, dto, purchaseSearchBo));

    }


    public PurchaseParentOrderPo getOneByNo(@NotBlank String purchaseParentOrderNo) {
        return baseMapper.selectOne(Wrappers.<PurchaseParentOrderPo>lambdaQuery()
                .eq(PurchaseParentOrderPo::getPurchaseParentOrderNo, purchaseParentOrderNo));

    }

    public PurchaseParentOrderPo getOneByIdAndVersion(@NotNull Long id, @NotNull Integer version) {
        return baseMapper.selectByIdVersion(id, version);
    }

    public List<PurchaseParentOrderPo> getByNoList(List<String> purchaseParentOrderNoList) {
        if (CollectionUtils.isEmpty(purchaseParentOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseParentOrderPo>lambdaQuery()
                .in(PurchaseParentOrderPo::getPurchaseParentOrderNo, purchaseParentOrderNoList));
    }

    public List<PurchaseParentOrderPo> getFirstOrderListBySpuList(List<String> spuList) {
        if (CollectionUtils.isEmpty(spuList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseParentOrderPo>lambdaQuery()
                .in(PurchaseParentOrderPo::getSpu, spuList));
    }


    /**
     * 根据单号与状态查找采购母单
     *
     * @param purchaseParentNoList 查询的单号
     * @param ridStatusList        排除的状态
     * @return
     */
    public List<PurchaseParentOrderPo> getListByNoListAndRidStatus(List<String> purchaseParentNoList,
                                                                   List<PurchaseParentOrderStatus> ridStatusList) {
        if (CollectionUtils.isEmpty(purchaseParentNoList)) {
            return new ArrayList<>();
        }
        return baseMapper.selectList(Wrappers.<PurchaseParentOrderPo>lambdaQuery()
                .in(PurchaseParentOrderPo::getPurchaseParentOrderNo, purchaseParentNoList)
                .notIn(PurchaseParentOrderPo::getPurchaseParentOrderStatus, ridStatusList));


    }

    public List<PurchaseParentOrderPo> getListByNoList(List<String> purchaseParentNoList) {
        if (CollectionUtils.isEmpty(purchaseParentNoList)) {
            return new ArrayList<>();
        }
        return baseMapper.selectList(Wrappers.<PurchaseParentOrderPo>lambdaQuery()
                .in(PurchaseParentOrderPo::getPurchaseParentOrderNo, purchaseParentNoList));
    }

    public List<PurchaseParentOrderPo> getCanCompletePurchase() {
        return baseMapper.getCanCompletePurchase();
    }
}
