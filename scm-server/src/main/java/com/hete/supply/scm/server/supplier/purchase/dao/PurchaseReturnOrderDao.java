package com.hete.supply.scm.server.supplier.purchase.dao;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseReturnDto;
import com.hete.supply.scm.api.scm.entity.enums.ReceiptOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ReturnOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ReturnType;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseReturnExportVo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseReturnSearchVo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 采购退货单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Component
@Validated
public class PurchaseReturnOrderDao extends BaseDao<PurchaseReturnOrderMapper, PurchaseReturnOrderPo> {

    /**
     * 通过模糊单号查询
     *
     * @author ChenWenLong
     * @date 2022/11/9 10:11
     */
    public List<PurchaseReturnOrderPo> getListLikeByPurchaseReturnOrderNo(String purchaseReturnOrderNo,
                                                                          String supplierCode,
                                                                          List<ReceiptOrderStatus> receiptOrderStatusNotList) {
        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderPo>lambdaQuery()
                .like(PurchaseReturnOrderPo::getReturnOrderNo, purchaseReturnOrderNo)
                .eq(PurchaseReturnOrderPo::getSupplierCode, supplierCode)
                .notIn(PurchaseReturnOrderPo::getReturnOrderStatus, receiptOrderStatusNotList));
    }

    /**
     * 查询采购单
     *
     * @param page
     * @param dto
     * @return
     */
    public IPage<PurchaseReturnOrderPo> searchProductPurchase(Page<PurchaseReturnOrderPo> page, PurchaseReturnDto dto) {
        return baseMapper.selectPage(page, Wrappers.<PurchaseReturnOrderPo>lambdaQuery()
                .eq(StringUtils.isNotBlank(dto.getReceiptUser()),
                        PurchaseReturnOrderPo::getReceiptUser, dto.getReceiptUser())
                .like(StringUtils.isNotBlank(dto.getReceiptUsername()),
                        PurchaseReturnOrderPo::getReceiptUsername, dto.getReceiptUsername())
                .ge(null != dto.getReceiptTimeStart(),
                        PurchaseReturnOrderPo::getReceiptTime, dto.getReceiptTimeStart())
                .le(null != dto.getReceiptTimeEnd(),
                        PurchaseReturnOrderPo::getReceiptTime, dto.getReceiptTimeEnd())
                .ge(null != dto.getReturnTimeStart(),
                        PurchaseReturnOrderPo::getReturnTime, dto.getReturnTimeStart())
                .le(null != dto.getReturnTimeEnd(),
                        PurchaseReturnOrderPo::getReturnTime, dto.getReturnTimeEnd())
                .ge(null != dto.getCreateTimeStart(),
                        PurchaseReturnOrderPo::getCreateTime, dto.getCreateTimeStart())
                .le(null != dto.getCreateTimeEnd(),
                        PurchaseReturnOrderPo::getCreateTime, dto.getCreateTimeEnd())
                .in(CollectionUtils.isNotEmpty(dto.getReturnTypeList()),
                        PurchaseReturnOrderPo::getReturnType, dto.getReturnTypeList())
                .in(CollectionUtils.isNotEmpty(dto.getReturnOrderStatusList()),
                        PurchaseReturnOrderPo::getReturnOrderStatus, dto.getReturnOrderStatusList())
                .eq(StringUtils.isNotBlank(dto.getTrackingNo()),
                        PurchaseReturnOrderPo::getTrackingNo, dto.getTrackingNo())
                .in(CollectionUtils.isNotEmpty(dto.getReturnOrderNoList()),
                        PurchaseReturnOrderPo::getReturnOrderNo, dto.getReturnOrderNoList())
                .in(CollectionUtils.isNotEmpty(dto.getReturnOrderNoBySkuList()),
                        PurchaseReturnOrderPo::getReturnOrderNo, dto.getReturnOrderNoBySkuList())
                .in(CollectionUtils.isNotEmpty(dto.getAuthSupplierCode()),
                        PurchaseReturnOrderPo::getSupplierCode, dto.getAuthSupplierCode())
                .in(CollectionUtils.isNotEmpty(dto.getSupplierCodeList()),
                        PurchaseReturnOrderPo::getSupplierCode, dto.getSupplierCodeList())
                .like(StringUtils.isNotBlank(dto.getReturnOrderNo()),
                        PurchaseReturnOrderPo::getReturnOrderNo, dto.getReturnOrderNo())
                .eq(StringUtils.isNotBlank(dto.getPurchaseChildOrderNo()),
                        PurchaseReturnOrderPo::getPurchaseChildOrderNo, dto.getPurchaseChildOrderNo())
                .orderByDesc(PurchaseReturnOrderPo::getPurchaseReturnOrderId));
    }

    public PurchaseReturnOrderPo getOneByNo(String purchaseReturnOrderNo) {
        if (StringUtils.isBlank(purchaseReturnOrderNo)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<PurchaseReturnOrderPo>lambdaQuery()
                .eq(PurchaseReturnOrderPo::getReturnOrderNo, purchaseReturnOrderNo));
    }

    public Map<String, PurchaseReturnOrderPo> getMapByReturnOrderNoList(List<String> returnOrderNoList) {
        if (CollectionUtils.isEmpty(returnOrderNoList)) {
            return Collections.emptyMap();
        }

        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderPo>lambdaQuery()
                        .in(PurchaseReturnOrderPo::getReturnOrderNo, returnOrderNoList))
                .stream()
                .collect(Collectors.toMap(PurchaseReturnOrderPo::getReturnOrderNo, Function.identity()));
    }

    public Integer getExportTotals(PurchaseReturnDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    public CommonPageResult.PageInfo<PurchaseReturnExportVo> getExportList(Page<Void> page, PurchaseReturnDto dto) {
        return PageInfoUtil.getPageInfo(baseMapper.getExportList(page, dto));
    }

    public List<PurchaseReturnOrderPo> getListByTrackingNo(String trackingNo) {
        if (StringUtils.isBlank(trackingNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderPo>lambdaQuery()
                .eq(PurchaseReturnOrderPo::getTrackingNo, trackingNo));
    }

    /**
     * 获取指定日期之前的待发货退货单
     *
     * @param days
     * @return
     */
    public List<PurchaseReturnOrderPo> getWaitReceiveListWithOverDays(Integer days, List<ReturnType> returnTypeList) {
        LocalDateTime time = new DateTime().toLocalDateTime().minusDays(days);
        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderPo>lambdaQuery()
                .eq(PurchaseReturnOrderPo::getReturnOrderStatus, ReturnOrderStatus.WAIT_RECEIVE)
                .in(PurchaseReturnOrderPo::getReturnType, returnTypeList)
                .le(PurchaseReturnOrderPo::getReturnTime, time)
        );
    }

    /**
     * 通过多个单号获取退货单
     *
     * @param returnOrderNoList
     * @return
     */
    public List<PurchaseReturnOrderPo> getListByReturnOrderNoList(List<String> returnOrderNoList) {
        if (CollectionUtils.isEmpty(returnOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderPo>lambdaQuery()
                .in(PurchaseReturnOrderPo::getReturnOrderNo, returnOrderNoList)
                .orderByDesc(PurchaseReturnOrderPo::getCreateTime));
    }

    public List<PurchaseReturnOrderPo> getListByReturnBizNo(String returnBizNo) {
        if (StringUtils.isBlank(returnBizNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderPo>lambdaQuery()
                .eq(PurchaseReturnOrderPo::getReturnBizNo, returnBizNo));
    }

    /**
     * 通过批量退货单来源单据号查询
     *
     * @param returnBizNoList:
     * @return List<PurchaseReturnOrderPo>
     * @author ChenWenLong
     * @date 2023/7/4 11:47
     */
    public Map<String, List<PurchaseReturnOrderPo>> getMapByBatchReturnBizNo(List<String> returnBizNoList) {
        if (CollectionUtils.isEmpty(returnBizNoList)) {
            return Collections.emptyMap();
        }
        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderPo>lambdaQuery()
                        .in(PurchaseReturnOrderPo::getReturnBizNo, returnBizNoList))
                .stream().collect(Collectors.groupingBy(PurchaseReturnOrderPo::getReturnBizNo));
    }

    public List<PurchaseReturnOrderPo> getListByReturnBizNoList(List<String> returnBizNoList) {
        if (CollectionUtils.isEmpty(returnBizNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderPo>lambdaQuery()
                .in(PurchaseReturnOrderPo::getReturnBizNo, returnBizNoList));
    }

    public List<PurchaseReturnOrderPo> getListByPurchaseChildNoList(List<String> childOrderNoList) {
        if (CollectionUtils.isEmpty(childOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderPo>lambdaQuery()
                .in(PurchaseReturnOrderPo::getPurchaseChildOrderNo, childOrderNoList));

    }


    public IPage<PurchaseReturnSearchVo> searchPurchaseReturnPage(Page<Void> page, PurchaseReturnDto dto) {
        return baseMapper.searchPurchaseReturnPage(page, dto);
    }

    public List<PurchaseReturnOrderPo> getListByStatusAndSupplierCode(ReturnOrderStatus returnOrderStatus,
                                                                      String supplierCode,
                                                                      LocalDateTime receiptTimeStart,
                                                                      LocalDateTime receiptTimeEnd,
                                                                      List<ReturnType> returnTypeList) {
        if (null == returnOrderStatus || StringUtils.isBlank(supplierCode)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderPo>lambdaQuery()
                .eq(PurchaseReturnOrderPo::getReturnOrderStatus, returnOrderStatus)
                .eq(PurchaseReturnOrderPo::getSupplierCode, supplierCode)
                .in(CollectionUtils.isNotEmpty(returnTypeList), PurchaseReturnOrderPo::getReturnType, returnTypeList)
                .ge(null != receiptTimeStart, PurchaseReturnOrderPo::getReceiptTime, receiptTimeStart)
                .le(null != receiptTimeEnd, PurchaseReturnOrderPo::getReceiptTime, receiptTimeEnd));
    }

    public List<PurchaseReturnOrderPo> getListByNoListAndSupplierCode(List<String> returnOrderNoList,
                                                                      String supplierCode,
                                                                      ReceiptOrderStatus receiptOrderStatusNot) {
        if (CollectionUtils.isEmpty(returnOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderPo>lambdaQuery()
                .in(PurchaseReturnOrderPo::getReturnOrderNo, returnOrderNoList)
                .eq(StringUtils.isNotBlank(supplierCode), PurchaseReturnOrderPo::getSupplierCode, supplierCode)
                .ne(null != receiptOrderStatusNot, PurchaseReturnOrderPo::getReturnOrderStatus, receiptOrderStatusNot));
    }
}
