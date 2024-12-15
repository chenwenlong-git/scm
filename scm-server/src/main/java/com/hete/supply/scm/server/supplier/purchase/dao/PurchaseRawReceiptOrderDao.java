package com.hete.supply.scm.server.supplier.purchase.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseRawReceiptSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.RawReceiptBizType;
import com.hete.supply.scm.api.scm.entity.enums.ReceiptOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.RawReceiptExportVo;
import com.hete.supply.scm.server.supplier.purchase.converter.SupplierPurchaseConverter;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseRawReceiptOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseRawReceiptVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 采购原料收货单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-28
 */
@Component
@Validated
public class PurchaseRawReceiptOrderDao extends BaseDao<PurchaseRawReceiptOrderMapper, PurchaseRawReceiptOrderPo> {

    public CommonPageResult.PageInfo<PurchaseRawReceiptVo> search(Page<PurchaseRawReceiptOrderPo> page,
                                                                  PurchaseRawReceiptSearchDto dto) {

        final LambdaQueryWrapper<PurchaseRawReceiptOrderPo> wrapper = this.getWrapper(dto);
        final Page<PurchaseRawReceiptOrderPo> pageResult = baseMapper.selectPage(page, wrapper);
        final List<PurchaseRawReceiptOrderPo> records = pageResult.getRecords();
        List<PurchaseRawReceiptVo> purchaseRawReceiptVoList = SupplierPurchaseConverter.rawReceiptPoListToVoList(records);

        return PageInfoUtil.getPageInfo(pageResult, purchaseRawReceiptVoList);
    }

    private LambdaQueryWrapper<PurchaseRawReceiptOrderPo> getWrapper(PurchaseRawReceiptSearchDto dto) {
        return Wrappers.<PurchaseRawReceiptOrderPo>lambdaQuery()
                .eq(StringUtils.isNotBlank(dto.getPurchaseRawReceiptOrderNo()),
                        PurchaseRawReceiptOrderPo::getPurchaseRawReceiptOrderNo, dto.getPurchaseRawReceiptOrderNo())
                .like(StringUtils.isNotBlank(dto.getPurchaseChildOrderNo()),
                        PurchaseRawReceiptOrderPo::getPurchaseChildOrderNo, dto.getPurchaseChildOrderNo())
                .in(CollectionUtils.isNotEmpty(dto.getPurchaseChildOrderNoList()),
                        PurchaseRawReceiptOrderPo::getPurchaseChildOrderNo, dto.getPurchaseChildOrderNoList())
                .like(StringUtils.isNotBlank(dto.getDevelopPamphletOrderNo()),
                        PurchaseRawReceiptOrderPo::getDevelopPamphletOrderNo, dto.getDevelopPamphletOrderNo())
                .ge(dto.getReceiptTimeStart() != null,
                        PurchaseRawReceiptOrderPo::getReceiptTime, dto.getReceiptTimeStart())
                .le(dto.getReceiptTimeEnd() != null,
                        PurchaseRawReceiptOrderPo::getReceiptTime, dto.getReceiptTimeEnd())
                .ge(dto.getDeliverTimeStart() != null,
                        PurchaseRawReceiptOrderPo::getDeliverTime, dto.getDeliverTimeStart())
                .le(dto.getDeliverTimeEnd() != null,
                        PurchaseRawReceiptOrderPo::getDeliverTime, dto.getDeliverTimeEnd())
                .like(StringUtils.isNotBlank(dto.getLogistics()),
                        PurchaseRawReceiptOrderPo::getLogistics, dto.getLogistics())
                .like(StringUtils.isNotBlank(dto.getTrackingNo()),
                        PurchaseRawReceiptOrderPo::getTrackingNo, dto.getTrackingNo())
                .in(CollectionUtils.isNotEmpty(dto.getReceiptOrderStatusList()),
                        PurchaseRawReceiptOrderPo::getReceiptOrderStatus, dto.getReceiptOrderStatusList())
                .in(CollectionUtils.isNotEmpty(dto.getAuthSupplierCode()),
                        PurchaseRawReceiptOrderPo::getSupplierCode, dto.getAuthSupplierCode())
                .in(CollectionUtils.isNotEmpty(dto.getSupplierCodeList()),
                        PurchaseRawReceiptOrderPo::getSupplierCode, dto.getSupplierCodeList())
                .like(StringUtils.isNotBlank(dto.getPurchaseRawDeliverOrderNo()),
                        PurchaseRawReceiptOrderPo::getPurchaseRawDeliverOrderNo, dto.getPurchaseRawDeliverOrderNo())
                .eq(null != dto.getRawReceiptBizType(),
                        PurchaseRawReceiptOrderPo::getRawReceiptBizType, dto.getRawReceiptBizType())
                .like(StringUtils.isNotBlank(dto.getSampleChildOrderNo()),
                        PurchaseRawReceiptOrderPo::getSampleChildOrderNo, dto.getSampleChildOrderNo())
                .in(CollectionUtils.isNotEmpty(dto.getRawReceiptBizTypeList()),
                        PurchaseRawReceiptOrderPo::getRawReceiptBizType, dto.getRawReceiptBizTypeList())
                .in(CollectionUtils.isNotEmpty(dto.getPurchaseRawReceiptOrderNoList()),
                        PurchaseRawReceiptOrderPo::getPurchaseRawReceiptOrderNo, dto.getPurchaseRawReceiptOrderNoList())
                .orderByDesc(PurchaseRawReceiptOrderPo::getCreateTime)
                .orderByDesc(PurchaseRawReceiptOrderPo::getPurchaseRawReceiptOrderId);
    }

    public PurchaseRawReceiptOrderPo getOneByNo(String purchaseRawReceiptOrderNo) {
        if (StringUtils.isBlank(purchaseRawReceiptOrderNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<PurchaseRawReceiptOrderPo>lambdaQuery()
                .eq(PurchaseRawReceiptOrderPo::getPurchaseRawReceiptOrderNo, purchaseRawReceiptOrderNo));
    }

    public PurchaseRawReceiptOrderPo getOneByReceiptNoOrTrackingNo(String purchaseRawReceiptOrderNo) {
        if (StringUtils.isBlank(purchaseRawReceiptOrderNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<PurchaseRawReceiptOrderPo>lambdaQuery()
                .eq(PurchaseRawReceiptOrderPo::getPurchaseRawReceiptOrderNo, purchaseRawReceiptOrderNo)
                .or().eq(PurchaseRawReceiptOrderPo::getTrackingNo, purchaseRawReceiptOrderNo)
                .or().eq(PurchaseRawReceiptOrderPo::getPurchaseRawDeliverOrderNo, purchaseRawReceiptOrderNo));
    }

    public Integer getExportTotals(PurchaseRawReceiptSearchDto dto) {
        return baseMapper.selectCount(this.getWrapper(dto)).intValue();
    }


    public CommonPageResult.PageInfo<RawReceiptExportVo> getExportList(Page<PurchaseRawReceiptOrderPo> page, PurchaseRawReceiptSearchDto dto) {
        final LambdaQueryWrapper<PurchaseRawReceiptOrderPo> wrapper = this.getWrapper(dto);
        final Page<PurchaseRawReceiptOrderPo> pageResult = baseMapper.selectPage(page, wrapper);
        final List<PurchaseRawReceiptOrderPo> records = pageResult.getRecords();
        List<RawReceiptExportVo> purchaseRawReceiptVoList = SupplierPurchaseConverter.rawReceiptPoListToExportVoList(records);

        return PageInfoUtil.getPageInfo(pageResult, purchaseRawReceiptVoList);
    }

    public PurchaseRawReceiptOrderPo getOneByDeliverNo(String purchaseRawDeliverOrderNo) {
        if (StringUtils.isBlank(purchaseRawDeliverOrderNo)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<PurchaseRawReceiptOrderPo>lambdaQuery()
                .eq(PurchaseRawReceiptOrderPo::getPurchaseRawDeliverOrderNo, purchaseRawDeliverOrderNo));
    }

    public List<PurchaseRawReceiptOrderPo> getListByPurchaseChildNo(String purchaseChildOrderNo) {
        if (StringUtils.isBlank(purchaseChildOrderNo)) {
            return new ArrayList<>();
        }

        return baseMapper.selectList(Wrappers.<PurchaseRawReceiptOrderPo>lambdaQuery()
                .eq(PurchaseRawReceiptOrderPo::getPurchaseChildOrderNo, purchaseChildOrderNo));

    }

    public List<PurchaseRawReceiptOrderPo> getListBySampleChildNo(String sampleChildOrderNo) {
        if (StringUtils.isBlank(sampleChildOrderNo)) {
            return null;
        }

        return baseMapper.selectList(Wrappers.<PurchaseRawReceiptOrderPo>lambdaQuery()
                .eq(PurchaseRawReceiptOrderPo::getSampleChildOrderNo, sampleChildOrderNo)
                .eq(PurchaseRawReceiptOrderPo::getRawReceiptBizType, RawReceiptBizType.SAMPLE));

    }

    public List<PurchaseRawReceiptOrderPo> getListByPurchaseChildNoListAndStatus(List<String> purchaseChildOrderNoList,
                                                                                 ReceiptOrderStatus receiptOrderStatus) {
        if (CollectionUtils.isEmpty(purchaseChildOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseRawReceiptOrderPo>lambdaQuery()
                .in(PurchaseRawReceiptOrderPo::getPurchaseChildOrderNo, purchaseChildOrderNoList)
                .eq(PurchaseRawReceiptOrderPo::getRawReceiptBizType, RawReceiptBizType.PURCHASE)
                .eq(PurchaseRawReceiptOrderPo::getReceiptOrderStatus, receiptOrderStatus));
    }

    public List<PurchaseRawReceiptOrderPo> getListByDeliver(List<String> purchaseRawDeliverOrderNoList) {
        if (CollectionUtils.isEmpty(purchaseRawDeliverOrderNoList)) {
            return new ArrayList<>();
        }

        return baseMapper.selectList(Wrappers.<PurchaseRawReceiptOrderPo>lambdaQuery()
                .in(PurchaseRawReceiptOrderPo::getPurchaseRawDeliverOrderNo, purchaseRawDeliverOrderNoList)
                .eq(PurchaseRawReceiptOrderPo::getRawReceiptBizType, RawReceiptBizType.PURCHASE));
    }

    public List<PurchaseRawReceiptOrderPo> getListByDeliverAndStatus(List<String> purchaseRawDeliverOrderNoList,
                                                                     ReceiptOrderStatus receiptOrderStatus) {
        if (CollectionUtils.isEmpty(purchaseRawDeliverOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseRawReceiptOrderPo>lambdaQuery()
                .in(PurchaseRawReceiptOrderPo::getPurchaseRawDeliverOrderNo, purchaseRawDeliverOrderNoList)
                .eq(PurchaseRawReceiptOrderPo::getRawReceiptBizType, RawReceiptBizType.PURCHASE)
                .eq(PurchaseRawReceiptOrderPo::getReceiptOrderStatus, receiptOrderStatus));
    }

    public List<PurchaseRawReceiptOrderPo> getListByPurchaseChildNoList(List<String> purchaseChildOrderNoList) {
        if (CollectionUtils.isEmpty(purchaseChildOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseRawReceiptOrderPo>lambdaQuery()
                .in(PurchaseRawReceiptOrderPo::getPurchaseChildOrderNo, purchaseChildOrderNoList));
    }

    public Page<PurchaseRawReceiptOrderPo> getNewRawReceiptExportList(Page<PurchaseRawReceiptOrderPo> page,
                                                                      PurchaseRawReceiptSearchDto dto) {

        final LambdaQueryWrapper<PurchaseRawReceiptOrderPo> wrapper = this.getWrapper(dto);
        return baseMapper.selectPage(page, wrapper);

    }

    public List<PurchaseRawReceiptOrderPo> getListByDeliverNo(String purchaseRawDeliverOrderNo) {
        if (StringUtils.isBlank(purchaseRawDeliverOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseRawReceiptOrderPo>lambdaQuery()
                .eq(PurchaseRawReceiptOrderPo::getPurchaseRawDeliverOrderNo, purchaseRawDeliverOrderNo));
    }

    public List<PurchaseRawReceiptOrderPo> getListByDevelopPamphletOrderNo(String developPamphletOrderNo) {
        if (StringUtils.isBlank(developPamphletOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseRawReceiptOrderPo>lambdaQuery()
                .eq(PurchaseRawReceiptOrderPo::getDevelopPamphletOrderNo, developPamphletOrderNo));
    }

    public List<PurchaseRawReceiptOrderPo> getListByDevelopPamphletOrderNoListAndStatus(List<String> developPamphletOrderNoList, ReceiptOrderStatus status) {
        if (CollectionUtils.isEmpty(developPamphletOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseRawReceiptOrderPo>lambdaQuery()
                .in(PurchaseRawReceiptOrderPo::getDevelopPamphletOrderNo, developPamphletOrderNoList)
                .eq(status != null, PurchaseRawReceiptOrderPo::getReceiptOrderStatus, status));
    }
}
