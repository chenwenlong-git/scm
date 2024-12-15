package com.hete.supply.scm.server.scm.ibfs.converter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.PrepaymentOrderStatus;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.PaymentAddDto;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.PrepaymentAddDto;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinancePaymentItemPo;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinancePrepaymentOrderItemPo;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinancePrepaymentOrderPo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.FinancePaymentOrderItemVo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.FinancePrepaymentOrderItemVo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.PrepaymentDetailVo;
import com.hete.supply.scm.server.scm.ibfs.enums.DeductionStatus;
import com.hete.supply.scm.server.scm.ibfs.enums.PaymentBizType;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPaymentAccountPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.udb.api.entity.vo.UserVo;
import com.hete.support.core.holder.GlobalContext;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/5/16 14:50
 */
public class PrepaymentConverter {

    public static FinancePrepaymentOrderPo addDtoToPo(String prepaymentOrderNo, PrepaymentAddDto dto) {
        final FinancePrepaymentOrderPo financePrepaymentOrderPo = new FinancePrepaymentOrderPo();
        financePrepaymentOrderPo.setPrepaymentOrderNo(prepaymentOrderNo);
        financePrepaymentOrderPo.setPrepaymentOrderStatus(PrepaymentOrderStatus.TO_BE_FOLLOW_SUBMIT);
        financePrepaymentOrderPo.setSupplierCode(dto.getSupplierCode());
        financePrepaymentOrderPo.setPrepaymentType(dto.getPrepaymentType());
        financePrepaymentOrderPo.setPrepaymentReason(dto.getPrepaymentReason());
        financePrepaymentOrderPo.setPrepaymentMoney(dto.getPrepaymentMoney());
        financePrepaymentOrderPo.setCurrency(dto.getCurrency());
        financePrepaymentOrderPo.setDeductionStatus(DeductionStatus.UNASSOCIATED);
        financePrepaymentOrderPo.setApplyDate(LocalDateTime.now());
        financePrepaymentOrderPo.setPrepaymentRemark(dto.getPrepaymentRemark());
        financePrepaymentOrderPo.setCtrlUser(GlobalContext.getUserKey());
        financePrepaymentOrderPo.setFollowUser(GlobalContext.getUserKey());

        return financePrepaymentOrderPo;
    }

    public static FinancePaymentItemPo addDtoToPaymentItemPo(String prepaymentOrderNo, PaymentAddDto dto,
                                                             SupplierPaymentAccountPo supplierPaymentAccountPo) {
        final FinancePaymentItemPo financePaymentItemPo = new FinancePaymentItemPo();
        financePaymentItemPo.setPaymentBizNo(prepaymentOrderNo);
        financePaymentItemPo.setPaymentBizType(PaymentBizType.PREPAYMENT);
        financePaymentItemPo.setSupplierCode(dto.getSupplierCode());
        financePaymentItemPo.setAccount(dto.getAccount());
        financePaymentItemPo.setSubject(supplierPaymentAccountPo.getSubject());
        financePaymentItemPo.setSupplierPaymentAccountType(supplierPaymentAccountPo.getSupplierPaymentAccountType());
        financePaymentItemPo.setBankName(supplierPaymentAccountPo.getBankName());
        financePaymentItemPo.setAccountUsername(supplierPaymentAccountPo.getAccountUsername());
        financePaymentItemPo.setBankSubbranchName(supplierPaymentAccountPo.getBankSubbranchName());
        financePaymentItemPo.setAccountRemarks(supplierPaymentAccountPo.getRemarks());
        financePaymentItemPo.setPaymentSubject(dto.getPaymentSubject());
        financePaymentItemPo.setPaymentReason(dto.getPaymentReason());
        financePaymentItemPo.setPaymentRemark(dto.getPaymentRemark());
        financePaymentItemPo.setRecipientSubject(supplierPaymentAccountPo.getSubject());
        financePaymentItemPo.setPaymentMoney(dto.getPaymentMoney());
        financePaymentItemPo.setCurrency(dto.getCurrency());
        financePaymentItemPo.setExchangeRate(dto.getExchangeRate());
        financePaymentItemPo.setTargetPaymentMoney(dto.getTargetPaymentMoney());
        financePaymentItemPo.setRmbExchangeRate(dto.getRmbExchangeRate());
        financePaymentItemPo.setRmbPaymentMoney(dto.getRmbPaymentMoney());
        financePaymentItemPo.setPaymentDate(LocalDateTime.now());
        financePaymentItemPo.setBankProvince(supplierPaymentAccountPo.getBankProvince());
        financePaymentItemPo.setBankCity(supplierPaymentAccountPo.getBankCity());
        financePaymentItemPo.setBankArea(supplierPaymentAccountPo.getBankArea());

        return financePaymentItemPo;
    }

    public static List<FinancePrepaymentOrderItemVo> prepaymentItemPoListToVoList(List<FinancePrepaymentOrderItemPo> financePrepaymentOrderItemPoList) {
        if (CollectionUtils.isEmpty(financePrepaymentOrderItemPoList)) {
            return Collections.emptyList();
        }

        return financePrepaymentOrderItemPoList.stream().map(itemPo -> {
            final FinancePrepaymentOrderItemVo financePrepaymentOrderItemVo = new FinancePrepaymentOrderItemVo();
            financePrepaymentOrderItemVo.setAccount(itemPo.getAccount());
            financePrepaymentOrderItemVo.setSupplierPaymentAccountType(itemPo.getSupplierPaymentAccountType());
            financePrepaymentOrderItemVo.setAccountUsername(itemPo.getAccountUsername());
            financePrepaymentOrderItemVo.setBankName(itemPo.getBankName());
            financePrepaymentOrderItemVo.setBankSubbranchName(itemPo.getBankSubbranchName());
            financePrepaymentOrderItemVo.setAccountRemarks(itemPo.getAccountRemarks());
            financePrepaymentOrderItemVo.setBankProvince(itemPo.getBankProvince());
            financePrepaymentOrderItemVo.setBankCity(itemPo.getBankCity());
            financePrepaymentOrderItemVo.setBankArea(itemPo.getBankArea());
            financePrepaymentOrderItemVo.setPrepaymentMoney(itemPo.getPrepaymentMoney());
            financePrepaymentOrderItemVo.setCurrency(itemPo.getCurrency());
            financePrepaymentOrderItemVo.setExpectedPrepaymentDate(itemPo.getExpectedPrepaymentDate());
            financePrepaymentOrderItemVo.setTargetPrepaymentMoney(itemPo.getTargetPrepaymentMoney());
            return financePrepaymentOrderItemVo;
        }).collect(Collectors.toList());
    }

    public static PrepaymentDetailVo prepaymentPoToVo(FinancePrepaymentOrderPo financePrepaymentOrderPo,
                                                      List<FinancePaymentOrderItemVo> financePaymentOrderItemList,
                                                      List<FinancePrepaymentOrderItemVo> financePrepaymentOrderItemList,
                                                      SupplierPo supplierPo, UserVo userVo) {
        final PrepaymentDetailVo prepaymentDetailVo = new PrepaymentDetailVo();
        prepaymentDetailVo.setFinancePrepaymentOrderId(financePrepaymentOrderPo.getFinancePrepaymentOrderId());
        prepaymentDetailVo.setVersion(financePrepaymentOrderPo.getVersion());
        prepaymentDetailVo.setPrepaymentOrderNo(financePrepaymentOrderPo.getPrepaymentOrderNo());
        prepaymentDetailVo.setPrepaymentOrderStatus(financePrepaymentOrderPo.getPrepaymentOrderStatus());
        prepaymentDetailVo.setSupplierCode(financePrepaymentOrderPo.getSupplierCode());
        prepaymentDetailVo.setCreateTime(financePrepaymentOrderPo.getCreateTime());
        prepaymentDetailVo.setDeductionStatus(financePrepaymentOrderPo.getDeductionStatus());
        prepaymentDetailVo.setPrepaymentType(financePrepaymentOrderPo.getPrepaymentType());
        prepaymentDetailVo.setPrepaymentReason(financePrepaymentOrderPo.getPrepaymentReason());
        prepaymentDetailVo.setPrepaymentMoney(financePrepaymentOrderPo.getPrepaymentMoney());
        prepaymentDetailVo.setCurrency(financePrepaymentOrderPo.getCurrency());
        prepaymentDetailVo.setPaymentMoney(financePrepaymentOrderPo.getPaymentMoney());
        prepaymentDetailVo.setPrepaymentRemark(financePrepaymentOrderPo.getPrepaymentRemark());
        prepaymentDetailVo.setFinancePaymentOrderItemList(financePaymentOrderItemList);
        prepaymentDetailVo.setFinancePrepaymentOrderItemList(financePrepaymentOrderItemList);
        prepaymentDetailVo.setWorkflowNo(financePrepaymentOrderPo.getWorkflowNo());
        prepaymentDetailVo.setCtrlUser(financePrepaymentOrderPo.getCtrlUser());
        prepaymentDetailVo.setCtrlUsername(userVo.getUsername());
        prepaymentDetailVo.setCreateUser(financePrepaymentOrderPo.getCreateUser());
        prepaymentDetailVo.setTaskId(financePrepaymentOrderPo.getTaskId());
        prepaymentDetailVo.setSupplierGrade(supplierPo.getSupplierGrade());

        return prepaymentDetailVo;
    }
}
