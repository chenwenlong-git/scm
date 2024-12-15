package com.hete.supply.scm.server.supplier.settle.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseSettleOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseSettleStatus;
import com.hete.supply.scm.server.scm.settle.dao.PurchaseSettleOrderDao;
import com.hete.supply.scm.server.scm.settle.entity.dto.PurchaseSettleOrderDetailDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.PurchaseSettleOrderExamineDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.PurchaseSettleOrderProductDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.PurchaseSettleOrderUpdateDto;
import com.hete.supply.scm.server.scm.settle.entity.po.PurchaseSettleOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleOrderDetailVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleOrderProductVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleOrderVo;
import com.hete.supply.scm.server.scm.settle.service.base.PurchaseSettleOrderBaseService;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2022/11/1 13:44
 */
@Service
@RequiredArgsConstructor
public class SupplierPurchaseSettleOrderBizService {
    private final PurchaseSettleOrderBaseService purchaseSettleOrderBaseService;
    private final PurchaseSettleOrderDao purchaseSettleOrderDao;
    private final AuthBaseService authBaseService;

    public CommonPageResult.PageInfo<PurchaseSettleOrderVo> searchPurchaseSettleOrder(PurchaseSettleOrderSearchDto dto) {

        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        List<PurchaseSettleStatus> notPurchaseSettleStatusList = new ArrayList<>();
        notPurchaseSettleStatusList.add(PurchaseSettleStatus.WAIT_CONFIRM);
        dto.setNotPurchaseSettleStatusList(notPurchaseSettleStatusList);
        dto.setAuthSupplierCode(supplierCodeList);
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonPageResult.PageInfo<>();
        }

        return purchaseSettleOrderBaseService.searchPurchaseSettleOrder(dto);
    }

    public PurchaseSettleOrderDetailVo getPurchaseSettleOrderDetail(PurchaseSettleOrderDetailDto dto) {
        verifyAuth(dto.getPurchaseSettleOrderId());
        return purchaseSettleOrderBaseService.getPurchaseSettleOrderDetail(dto);
    }

    public PurchaseSettleOrderProductVo searchPurchaseSettleOrderProduct(PurchaseSettleOrderProductDto dto) {
        verifyAuth(dto.getPurchaseSettleOrderId());
        return purchaseSettleOrderBaseService.searchPurchaseSettleOrderProduct(dto);
    }

    public Boolean update(PurchaseSettleOrderUpdateDto dto) {
        verifyAuth(dto.getPurchaseSettleOrderId());
        return purchaseSettleOrderBaseService.update(dto);
    }

    public Boolean examine(PurchaseSettleOrderExamineDto dto) {
        verifyAuth(dto.getPurchaseSettleOrderId());
        return purchaseSettleOrderBaseService.examine(dto);
    }

    /**
     * 验证供应商
     *
     * @author ChenWenLong
     * @date 2022/11/17 16:21
     */
    public void verifyAuth(Long purchaseSettleOrderId) {
        PurchaseSettleOrderPo purchaseSettleOrderPo = purchaseSettleOrderDao.getById(purchaseSettleOrderId);
        if (purchaseSettleOrderPo == null) {
            throw new ParamIllegalException("查询不到供应商信息");
        }
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            throw new BizException("禁止非法请求");
        }

        if (CollectionUtil.isNotEmpty(supplierCodeList) && !supplierCodeList.contains(purchaseSettleOrderPo.getSupplierCode())) {
            throw new BizException("禁止非法请求");
        }

    }

}
