package com.hete.supply.scm.server.scm.purchase.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseRawBizType;
import com.hete.supply.scm.api.scm.entity.enums.RawSupplier;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderRawPo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 采购需求子单原料 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-01
 */
@Component
@Validated
public class PurchaseChildOrderRawDao extends BaseDao<PurchaseChildOrderRawMapper, PurchaseChildOrderRawPo> {

    public List<PurchaseChildOrderRawPo> getListByChildNo(@NotBlank String purchaseChildOrderNo) {

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderRawPo>lambdaQuery()
                .eq(PurchaseChildOrderRawPo::getPurchaseChildOrderNo, purchaseChildOrderNo));
    }

    public List<PurchaseChildOrderRawPo> getListByChildNo(@NotBlank String purchaseChildOrderNo,
                                                          PurchaseRawBizType purchaseRawBizType) {

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderRawPo>lambdaQuery()
                .eq(PurchaseChildOrderRawPo::getPurchaseChildOrderNo, purchaseChildOrderNo)
                .eq(PurchaseChildOrderRawPo::getPurchaseRawBizType, purchaseRawBizType));
    }

    public List<PurchaseChildOrderRawPo> getListByChildNo(@NotBlank String purchaseChildOrderNo,
                                                          PurchaseRawBizType purchaseRawBizType,
                                                          List<RawSupplier> rawSupplierList) {

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderRawPo>lambdaQuery()
                .eq(PurchaseChildOrderRawPo::getPurchaseChildOrderNo, purchaseChildOrderNo)
                .eq(PurchaseChildOrderRawPo::getPurchaseRawBizType, purchaseRawBizType)
                .in(PurchaseChildOrderRawPo::getRawSupplier, rawSupplierList));
    }

    public List<PurchaseChildOrderRawPo> getListByChildNo(@NotBlank String purchaseChildOrderNo,
                                                          PurchaseRawBizType purchaseRawBizType,
                                                          RawSupplier rawSupplier) {

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderRawPo>lambdaQuery()
                .eq(PurchaseChildOrderRawPo::getPurchaseChildOrderNo, purchaseChildOrderNo)
                .eq(PurchaseChildOrderRawPo::getPurchaseRawBizType, purchaseRawBizType)
                .eq(PurchaseChildOrderRawPo::getRawSupplier, rawSupplier));
    }

    public List<PurchaseChildOrderRawPo> getListByChildNo(@NotBlank String purchaseChildOrderNo,
                                                          List<PurchaseRawBizType> purchaseRawBizTypeList,
                                                          RawSupplier rawSupplier) {

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderRawPo>lambdaQuery()
                .eq(PurchaseChildOrderRawPo::getPurchaseChildOrderNo, purchaseChildOrderNo)
                .in(PurchaseChildOrderRawPo::getPurchaseRawBizType, purchaseRawBizTypeList)
                .eq(PurchaseChildOrderRawPo::getRawSupplier, rawSupplier));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        return super.removeBatchByIds(list);
    }

    public List<PurchaseChildOrderRawPo> getListByIdList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderRawPo>lambdaQuery()
                .in(PurchaseChildOrderRawPo::getPurchaseChildOrderRawId, idList));
    }

    public void deleteByChildNoList(List<String> noList) {
        if (CollectionUtils.isEmpty(noList)) {
            return;
        }
        baseMapper.deleteSkipCheck(Wrappers.<PurchaseChildOrderRawPo>lambdaUpdate()
                .in(PurchaseChildOrderRawPo::getPurchaseChildOrderNo, noList));
    }

    public List<PurchaseChildOrderRawPo> getListByChildNoList(List<String> purchaseChildOrderNoList) {
        if (CollectionUtils.isEmpty(purchaseChildOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderRawPo>lambdaQuery()
                .in(PurchaseChildOrderRawPo::getPurchaseChildOrderNo, purchaseChildOrderNoList));
    }

    public List<PurchaseChildOrderRawPo> getListByChildNoList(List<String> purchaseChildOrderNoList,
                                                              PurchaseRawBizType purchaseRawBizType) {
        if (CollectionUtils.isEmpty(purchaseChildOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderRawPo>lambdaQuery()
                .in(PurchaseChildOrderRawPo::getPurchaseChildOrderNo, purchaseChildOrderNoList)
                .eq(PurchaseChildOrderRawPo::getPurchaseRawBizType, purchaseRawBizType));
    }

    public List<PurchaseChildOrderRawPo> getListByChildNoList(List<String> purchaseChildOrderNoList,
                                                              PurchaseRawBizType purchaseRawBizType,
                                                              List<RawSupplier> rawSupplierList) {
        if (CollectionUtils.isEmpty(purchaseChildOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderRawPo>lambdaQuery()
                .in(PurchaseChildOrderRawPo::getPurchaseChildOrderNo, purchaseChildOrderNoList)
                .eq(PurchaseChildOrderRawPo::getPurchaseRawBizType, purchaseRawBizType)
                .in(PurchaseChildOrderRawPo::getRawSupplier, rawSupplierList));
    }

    public List<PurchaseChildOrderRawPo> getListByChildNoList(String purchaseChildOrderNo,
                                                              PurchaseRawBizType purchaseRawBizType,
                                                              List<RawSupplier> rawSupplierList) {
        if (StringUtils.isBlank(purchaseChildOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderRawPo>lambdaQuery()
                .eq(PurchaseChildOrderRawPo::getPurchaseChildOrderNo, purchaseChildOrderNo)
                .eq(PurchaseChildOrderRawPo::getPurchaseRawBizType, purchaseRawBizType)
                .in(PurchaseChildOrderRawPo::getRawSupplier, rawSupplierList));
    }

    public void deleteByChildNo(String purchaseChildOrderNo) {
        if (StringUtils.isBlank(purchaseChildOrderNo)) {
            return;
        }
        baseMapper.deleteSkipCheck(Wrappers.<PurchaseChildOrderRawPo>lambdaUpdate()
                .eq(PurchaseChildOrderRawPo::getPurchaseChildOrderNo, purchaseChildOrderNo));
    }

    public List<PurchaseChildOrderRawPo> getListByChildNoList(List<String> purchaseChildOrderNoList,
                                                              PurchaseRawBizType purchaseRawBizType,
                                                              RawSupplier rawSupplier) {
        if (CollectionUtils.isEmpty(purchaseChildOrderNoList)) {
            return new ArrayList<>();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderRawPo>lambdaQuery()
                .in(PurchaseChildOrderRawPo::getPurchaseChildOrderNo, purchaseChildOrderNoList)
                .eq(PurchaseChildOrderRawPo::getPurchaseRawBizType, purchaseRawBizType)
                .eq(PurchaseChildOrderRawPo::getRawSupplier, rawSupplier));
    }

    public CommonPageResult.PageInfo<PurchaseChildOrderRawPo> getListByPage(Page<PurchaseChildOrderRawPo> page,
                                                                            List<PurchaseRawBizType> purchaseRawBizTypeList) {
        return PageInfoUtil.getPageInfo(baseMapper.selectPage(page, Wrappers.<PurchaseChildOrderRawPo>lambdaQuery()
                .in(PurchaseChildOrderRawPo::getPurchaseRawBizType, purchaseRawBizTypeList)));
    }
}
