package com.hete.supply.scm.server.scm.ibfs.converter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoFundType;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoPayType;
import com.hete.supply.scm.api.scm.entity.enums.RecoOrderItemSkuStatus;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderItemSkuPo;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderPo;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceSettleOrderPo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.RecoOrderButtonVo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.RecoOrderDetailFundTypeVo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.RecoOrderDetailVo;
import com.hete.supply.scm.server.scm.ibfs.enums.Currency;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.udb.api.entity.vo.UserVo;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/5/21 19:29
 */
@Slf4j
public class FinanceRecoOrderConverter {


    public static RecoOrderDetailVo poToVo(FinanceRecoOrderPo financeRecoOrderPo,
                                           SupplierPo supplierPo,
                                           RecoOrderButtonVo recoOrderButtonVo,
                                           List<FinanceRecoOrderItemSkuPo> financeRecoOrderItemSkuPoList,
                                           FinanceSettleOrderPo financeSettleOrderPo,
                                           UserVo ctrlUserVo) {

        RecoOrderDetailVo recoOrderDetailVo = new RecoOrderDetailVo();
        recoOrderDetailVo.setFinanceRecoOrderNo(financeRecoOrderPo.getFinanceRecoOrderNo());
        recoOrderDetailVo.setVersion(financeRecoOrderPo.getVersion());
        recoOrderDetailVo.setFinanceRecoOrderStatus(financeRecoOrderPo.getFinanceRecoOrderStatus());
        recoOrderDetailVo.setSupplierCode(financeRecoOrderPo.getSupplierCode());
        if (null != supplierPo) {
            recoOrderDetailVo.setSupplierName(supplierPo.getSupplierName());
            recoOrderDetailVo.setSupplierGrade(supplierPo.getSupplierGrade());
        }
        recoOrderDetailVo.setReconciliationCycle(financeRecoOrderPo.getReconciliationCycle());
        recoOrderDetailVo.setReconciliationStartTime(financeRecoOrderPo.getReconciliationStartTime());
        recoOrderDetailVo.setReconciliationEndTime(financeRecoOrderPo.getReconciliationEndTime());
        recoOrderDetailVo.setFinanceSettleOrderNo(financeRecoOrderPo.getFinanceSettleOrderNo());
        recoOrderDetailVo.setWorkflowNo(financeRecoOrderPo.getWorkflowNo());
        recoOrderDetailVo.setSettlePrice(financeRecoOrderPo.getSettlePrice());
        recoOrderDetailVo.setReceivePrice(financeRecoOrderPo.getReceivePrice());
        recoOrderDetailVo.setPayPrice(financeRecoOrderPo.getPayPrice());
        recoOrderDetailVo.setCtrlUser(financeRecoOrderPo.getCtrlUser());
        if (StringUtils.isNotBlank(financeRecoOrderPo.getCtrlUser()) && null != ctrlUserVo) {
            recoOrderDetailVo.setCtrlUsername(ctrlUserVo.getUsername());
        }
        recoOrderDetailVo.setTaskId(financeRecoOrderPo.getTaskId());
        recoOrderDetailVo.setComment(financeRecoOrderPo.getComment());
        recoOrderDetailVo.setRemarks(financeRecoOrderPo.getRemarks());
        recoOrderDetailVo.setRecoOrderButtonVo(recoOrderButtonVo);
        if (null != financeSettleOrderPo) {
            recoOrderDetailVo.setFinanceSettleOrderStatus(financeSettleOrderPo.getFinanceSettleOrderStatus());
        }

        Integer inspectTotalNum = (int) financeRecoOrderItemSkuPoList.stream()
                .filter(financeRecoOrderItemSkuPo ->
                        RecoOrderItemSkuStatus.EXCEPTION.equals(financeRecoOrderItemSkuPo.getRecoOrderItemSkuStatus()))
                .count();
        recoOrderDetailVo.setInspectTotalNum(inspectTotalNum);
        recoOrderDetailVo.setCurrency(Currency.RMB);


        Map<FinanceRecoFundType, List<FinanceRecoOrderItemSkuPo>> financeRecoOrderItemSkuPoMap = financeRecoOrderItemSkuPoList.stream()
                .collect(Collectors.groupingBy(FinanceRecoOrderItemSkuPo::getFinanceRecoFundType));
        // 按顺序对分组结果进行排序
        List<FinanceRecoFundType> sortedTypeList = FinanceRecoFundType.getSortedTypeList();
        Map<FinanceRecoFundType, List<FinanceRecoOrderItemSkuPo>> sortedFinanceRecoOrderItemSkuPoMap = new LinkedHashMap<>();
        for (FinanceRecoFundType financeRecoFundType : sortedTypeList) {
            if (financeRecoOrderItemSkuPoMap.containsKey(financeRecoFundType)) {
                sortedFinanceRecoOrderItemSkuPoMap.put(financeRecoFundType, financeRecoOrderItemSkuPoMap.get(financeRecoFundType));
            }
        }
        // 单据类型汇总列表
        List<RecoOrderDetailFundTypeVo> recoOrderDetailFundTypeList = new ArrayList<>();
        sortedFinanceRecoOrderItemSkuPoMap.forEach((FinanceRecoFundType financeRecoFundType, List<FinanceRecoOrderItemSkuPo> poList) -> {
            RecoOrderDetailFundTypeVo recoOrderDetailFundTypeVo = new RecoOrderDetailFundTypeVo();
            recoOrderDetailFundTypeVo.setFinanceRecoFundType(financeRecoFundType);

            // 应付
            BigDecimal payPrice = poList.stream().filter(po -> FinanceRecoPayType.HANDLE.equals(po.getFinanceRecoPayType()))
                    .map(FinanceRecoOrderItemSkuPo::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            recoOrderDetailFundTypeVo.setPayPrice(payPrice);

            // 应收
            BigDecimal receivePrice = poList.stream().filter(po -> FinanceRecoPayType.RECEIVABLE.equals(po.getFinanceRecoPayType()))
                    .map(FinanceRecoOrderItemSkuPo::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            recoOrderDetailFundTypeVo.setReceivePrice(receivePrice);

            // 对账
            BigDecimal settlePrice = payPrice.subtract(receivePrice);
            recoOrderDetailFundTypeVo.setSettlePrice(settlePrice);


            // 异常
            BigDecimal abnormalPrice = poList.stream().filter(po -> RecoOrderItemSkuStatus.EXCEPTION.equals(po.getRecoOrderItemSkuStatus()))
                    .map(FinanceRecoOrderItemSkuPo::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            recoOrderDetailFundTypeVo.setAbnormalPrice(abnormalPrice);

            // 汇总的状态
            List<FinanceRecoOrderItemSkuPo> financeRecoOrderItemSkuPoCustom = poList.stream()
                    .filter(financeRecoOrderItemSkuPo -> !RecoOrderItemSkuStatus.CONFIRMED.equals(financeRecoOrderItemSkuPo.getRecoOrderItemSkuStatus()))
                    .collect(Collectors.toList());
            List<FinanceRecoOrderItemSkuPo> financeRecoOrderItemSkuPoOther = poList.stream()
                    .filter(financeRecoOrderItemSkuPo -> RecoOrderItemSkuStatus.EXCEPTION.equals(financeRecoOrderItemSkuPo.getRecoOrderItemSkuStatus()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(financeRecoOrderItemSkuPoCustom)) {
                recoOrderDetailFundTypeVo.setRecoOrderItemSkuStatus(RecoOrderItemSkuStatus.CONFIRMED);
            } else if (CollectionUtils.isNotEmpty(financeRecoOrderItemSkuPoOther)) {
                recoOrderDetailFundTypeVo.setRecoOrderItemSkuStatus(RecoOrderItemSkuStatus.EXCEPTION);
            } else {
                recoOrderDetailFundTypeVo.setRecoOrderItemSkuStatus(RecoOrderItemSkuStatus.WAIT_CONFIRM);
            }

            recoOrderDetailFundTypeList.add(recoOrderDetailFundTypeVo);
        });
        recoOrderDetailVo.setRecoOrderDetailFundTypeList(recoOrderDetailFundTypeList);


        return recoOrderDetailVo;
    }


}
