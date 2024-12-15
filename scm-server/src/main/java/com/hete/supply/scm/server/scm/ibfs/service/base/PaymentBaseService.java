package com.hete.supply.scm.server.scm.ibfs.service.base;

import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.ibfs.converter.PaymentConverter;
import com.hete.supply.scm.server.scm.ibfs.dao.FinancePaymentItemDao;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinancePaymentItemPo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.FinancePaymentOrderItemVo;
import com.hete.supply.scm.server.scm.ibfs.enums.PaymentBizType;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/5/17 14:34
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class PaymentBaseService {
    private final FinancePaymentItemDao financePaymentItemDao;
    private final ScmImageBaseService scmImageBaseService;

    /**
     * 获取付款记录
     *
     * @param paymentBizNo
     * @param paymentBizType
     * @return
     */
    public List<FinancePaymentOrderItemVo> getListByBizNoAndType(String paymentBizNo, PaymentBizType paymentBizType) {
        if (StringUtils.isBlank(paymentBizNo) || null == paymentBizType) {
            return Collections.emptyList();
        }
        final List<FinancePaymentItemPo> financePaymentItemPoList = financePaymentItemDao.getListByBizNoAndType(paymentBizNo, paymentBizType);

        if (CollectionUtils.isEmpty(financePaymentItemPoList)) {
            return Collections.emptyList();
        }

        final List<Long> financePaymentItemIdList = financePaymentItemPoList.stream()
                .map(FinancePaymentItemPo::getFinancePaymentItemId)
                .collect(Collectors.toList());
        // 附件
        final Map<Long, List<String>> bizIdFileCodeListMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.PAYMENT_FILE, financePaymentItemIdList);

        return PaymentConverter.paymentItemPoListToVoList(financePaymentItemPoList, bizIdFileCodeListMap);
    }
}
