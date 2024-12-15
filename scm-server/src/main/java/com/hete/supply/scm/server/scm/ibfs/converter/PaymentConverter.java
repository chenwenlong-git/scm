package com.hete.supply.scm.server.scm.ibfs.converter;

import com.hete.supply.scm.server.scm.ibfs.entity.po.FinancePaymentItemPo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.FinancePaymentOrderItemVo;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/5/17 15:49
 */
public class PaymentConverter {
    public static List<FinancePaymentOrderItemVo> paymentItemPoListToVoList(List<FinancePaymentItemPo> financePaymentItemPoList, Map<Long, List<String>> bizIdFileCodeListMap) {
        if (CollectionUtils.isEmpty(financePaymentItemPoList)) {
            return Collections.emptyList();
        }
        return financePaymentItemPoList.stream().map(itemPo -> {
            final FinancePaymentOrderItemVo financePaymentOrderItemVo = new FinancePaymentOrderItemVo();
            financePaymentOrderItemVo.setPaymentSubject(itemPo.getPaymentSubject());
            financePaymentOrderItemVo.setPaymentReason(itemPo.getPaymentReason());
            financePaymentOrderItemVo.setPaymentRemark(itemPo.getPaymentRemark());
            financePaymentOrderItemVo.setSubject(itemPo.getRecipientSubject());
            financePaymentOrderItemVo.setAccount(itemPo.getAccount());
            financePaymentOrderItemVo.setBankName(itemPo.getBankName());
            financePaymentOrderItemVo.setBankSubbranchName(itemPo.getBankSubbranchName());
            financePaymentOrderItemVo.setBankProvince(itemPo.getBankProvince());
            financePaymentOrderItemVo.setBankCity(itemPo.getBankCity());
            financePaymentOrderItemVo.setBankArea(itemPo.getBankArea());
            financePaymentOrderItemVo.setPaymentMoney(itemPo.getPaymentMoney());
            financePaymentOrderItemVo.setTargetPaymentMoney(itemPo.getTargetPaymentMoney());
            financePaymentOrderItemVo.setRmbPaymentMoney(itemPo.getRmbPaymentMoney());
            financePaymentOrderItemVo.setCurrency(itemPo.getCurrency());
            financePaymentOrderItemVo.setPaymentDate(itemPo.getPaymentDate());
            financePaymentOrderItemVo.setFileCodeList(bizIdFileCodeListMap.get(itemPo.getFinancePaymentItemId()));
            financePaymentOrderItemVo.setAccountUsername(itemPo.getAccountUsername());
            return financePaymentOrderItemVo;
        }).collect(Collectors.toList());
    }
}
