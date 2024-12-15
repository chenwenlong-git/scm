package com.hete.supply.scm.server.scm.ibfs.builder;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.mc.api.workflow.entity.dto.WorkflowTransferDto;
import com.hete.supply.scm.api.scm.entity.enums.FinanceSettleOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.FinanceSettleOrderExportVo;
import com.hete.supply.scm.api.scm.entity.vo.FinanceSettleOrderItemExportVo;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ScmFormatUtil;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.*;
import com.hete.supply.scm.server.scm.ibfs.entity.po.*;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.SettleOrderDetailVo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.SettleOrderPageVo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.SupplierVo;
import com.hete.supply.scm.server.scm.ibfs.enums.Currency;
import com.hete.supply.scm.server.scm.ibfs.enums.FinanceSettleOrderItemType;
import com.hete.supply.scm.server.scm.ibfs.enums.PaymentBizType;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPaymentAccountPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.supplier.ibfs.entity.dto.AddPaymentRecordDto;
import com.hete.supply.scm.server.supplier.ibfs.entity.dto.AddSettleOrderAccountDto;
import com.hete.supply.scm.server.supplier.ibfs.entity.dto.UpdateSettleOrderAccountDto;
import com.hete.supply.udb.api.entity.vo.OrgVo;
import com.hete.supply.udb.api.entity.vo.UserVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/5/23.
 */
public class FinanceSettleOrderBuilder {

    private final static String RECIPIENT_SUBJECT = "贝坤电子商务有限公司";

    public static List<FinanceSettleOrderItemPo> buildSettleOrderItems(CreateFinanceSettleItemBo createFinanceSettleItemBo) {
        if (Objects.isNull(createFinanceSettleItemBo)) {
            return Collections.emptyList();
        }

        // 初始化一个空的结算明细列表
        List<FinanceSettleOrderItemPo> settleOrderItems = new ArrayList<>();

        // 处理对账单列表，构建结算明细
        List<CreateFinanceSettleItemBo.FinanceRecoOrderBo> financeRecoOrderBos
                = createFinanceSettleItemBo.getFinanceRecoOrderBos();
        if (CollectionUtils.isNotEmpty(financeRecoOrderBos)) {
            List<FinanceSettleOrderItemPo> recoOrderItems = financeRecoOrderBos.stream()
                    .map(FinanceSettleOrderBuilder::buildSettleOrderItemFromRecoOrder)
                    .collect(Collectors.toList());
            settleOrderItems.addAll(recoOrderItems);
        }

        // 处理结转单列表，构建结算明细
        List<CreateFinanceSettleItemBo.FinanceSettleCarryoverOrderBo> financeSettleCarryoverOrderBos
                = createFinanceSettleItemBo.getFinanceSettleCarryoverOrderBos();
        if (CollectionUtils.isNotEmpty(financeSettleCarryoverOrderBos)) {
            List<FinanceSettleOrderItemPo> carryoverOrderItems = financeSettleCarryoverOrderBos.stream()
                    .map(FinanceSettleOrderBuilder::buildSettleOrderItemFromCarryoverOrder)
                    .collect(Collectors.toList());
            settleOrderItems.addAll(carryoverOrderItems);
        }

        // 返回构建的结算明细列表
        return settleOrderItems;
    }

    /**
     * 根据单个对账单构建结算明细。
     *
     * @param recoOrder 对账单
     * @return 构建的结算明细
     */
    public static FinanceSettleOrderItemPo buildSettleOrderItemFromRecoOrder(CreateFinanceSettleItemBo.FinanceRecoOrderBo recoOrder) {
        FinanceSettleOrderItemPo settleOrderItem = new FinanceSettleOrderItemPo();
        settleOrderItem.setBusinessNo(recoOrder.getFinanceRecoOrderNo());
        // 标识为对账单类型
        settleOrderItem.setFinanceSettleOrderItemType(FinanceSettleOrderItemType.RECO_ORDER);
        settleOrderItem.setPayAmount(
                Objects.nonNull(recoOrder.getPayPrice()) ? recoOrder.getPayPrice() : BigDecimal.ZERO);
        settleOrderItem.setReceiveAmount(
                Objects.nonNull(recoOrder.getReceivePrice()) ? recoOrder.getReceivePrice() : BigDecimal.ZERO);
        settleOrderItem.setSettleAmount(settleOrderItem.getPayAmount()
                .subtract(settleOrderItem.getReceiveAmount()));
        return settleOrderItem;
    }


    /**
     * 根据单个结转单构建结算明细。
     *
     * @param carryoverOrder 结转单
     * @return 构建的结算明细
     */
    public static FinanceSettleOrderItemPo buildSettleOrderItemFromCarryoverOrder(CreateFinanceSettleItemBo.FinanceSettleCarryoverOrderBo carryoverOrder) {
        FinanceSettleOrderItemPo settleOrderItem = new FinanceSettleOrderItemPo();
        settleOrderItem.setBusinessNo(carryoverOrder.getFinanceSettleCarryoverOrderNo());
        // 标识为结转单类型
        settleOrderItem.setPayAmount(BigDecimal.ZERO);
        settleOrderItem.setReceiveAmount(carryoverOrder.getCarryoverAmount());
        settleOrderItem.setFinanceSettleOrderItemType(FinanceSettleOrderItemType.CARRYOVER_ORDER);
        settleOrderItem.setSettleAmount(settleOrderItem.getPayAmount()
                .subtract(settleOrderItem.getReceiveAmount()));
        return settleOrderItem;
    }

    public static List<FinanceSettleOrderItemPo> buildSettleOrderItemsByRecoOrders(List<FinanceRecoOrderPo> recoOrders) {
        if (CollectionUtils.isEmpty(recoOrders)) {
            return Collections.emptyList();
        }

        return recoOrders.stream()
                .map(recoOrder -> {
                    FinanceSettleOrderItemPo settleOrderItem = new FinanceSettleOrderItemPo();
                    settleOrderItem.setBusinessNo(recoOrder.getFinanceRecoOrderNo());
                    // 标识为对账单类型
                    settleOrderItem.setFinanceSettleOrderItemType(FinanceSettleOrderItemType.RECO_ORDER);
                    settleOrderItem.setPayAmount(
                            Objects.nonNull(recoOrder.getPayPrice()) ? recoOrder.getPayPrice() : BigDecimal.ZERO);
                    settleOrderItem.setReceiveAmount(Objects.nonNull(
                            recoOrder.getReceivePrice()) ? recoOrder.getReceivePrice() : BigDecimal.ZERO);
                    settleOrderItem.setSettleAmount(settleOrderItem.getPayAmount()
                            .subtract(settleOrderItem.getReceiveAmount()));
                    return settleOrderItem;
                })
                .collect(Collectors.toList());
    }

    public static List<FinanceSettleOrderItemPo> buildSettleOrderItemsByCarryoverOrder(List<CarryoverCalculationBo.CarryoverOrderBo> carryoverOrders) {
        if (CollectionUtils.isEmpty(carryoverOrders)) {
            return Collections.emptyList();
        }

        return carryoverOrders.stream()
                .map(carryoverOrder -> {
                    FinanceSettleOrderItemPo settleOrderItem = new FinanceSettleOrderItemPo();
                    settleOrderItem.setBusinessNo(carryoverOrder.getCarryoverNo());
                    // 标识为结转单类型
                    settleOrderItem.setPayAmount(BigDecimal.ZERO);
                    settleOrderItem.setReceiveAmount(carryoverOrder.getCarryoverAmount());
                    settleOrderItem.setFinanceSettleOrderItemType(FinanceSettleOrderItemType.CARRYOVER_ORDER);
                    settleOrderItem.setSettleAmount(settleOrderItem.getPayAmount()
                            .subtract(settleOrderItem.getReceiveAmount()));
                    return settleOrderItem;
                })
                .collect(Collectors.toList());
    }

    public static FinanceSettleOrderReceivePo buildSettleOrderReceivePo(FinanceSettleOrderPo settleOrderPo,
                                                                        AddSettleOrderAccountDto dto,
                                                                        SupplierPaymentAccountPo supplierPaymentAccountPo) {
        FinanceSettleOrderReceivePo po = new FinanceSettleOrderReceivePo();
        po.setFinanceSettleOrderNo(settleOrderPo.getFinanceSettleOrderNo());
        po.setSupplierCode(settleOrderPo.getSupplierCode());
        po.setAccount(supplierPaymentAccountPo.getAccount());
        po.setSupplierPaymentAccountType(supplierPaymentAccountPo.getSupplierPaymentAccountType());
        po.setBankName(supplierPaymentAccountPo.getBankName());
        po.setAccountUsername(supplierPaymentAccountPo.getAccountUsername());
        po.setBankSubbranchName(supplierPaymentAccountPo.getBankSubbranchName());
        po.setAccountRemarks(supplierPaymentAccountPo.getRemarks());
        po.setBankProvince(supplierPaymentAccountPo.getBankProvince());
        po.setBankCity(supplierPaymentAccountPo.getBankCity());
        po.setBankArea(supplierPaymentAccountPo.getBankArea());
        po.setSubject(supplierPaymentAccountPo.getSubject());
        po.setExpectReceiveDate(dto.getExpectReceiveDate());

        BigDecimal expectReceiveAmount = dto.getExpectReceiveAmount();
        po.setExpectReceiveAmount(expectReceiveAmount);

        Currency currency = supplierPaymentAccountPo.getSupplierPaymentCurrencyType().toCurrency();
        po.setCurrency(currency);
        po.setExchangeRate(currency.toRmbExchangeRate());

        po.setCurrencyAmount(Currency.currencyExchange(expectReceiveAmount, Currency.RMB, currency));
        return po;
    }

    public static FinanceSettleOrderReceivePo buildSettleOrderReceivePo(FinanceSettleOrderPo settleOrderPo,
                                                                        UpdateSettleOrderAccountDto dto,
                                                                        SupplierPaymentAccountPo supplierPaymentAccountPo) {
        FinanceSettleOrderReceivePo po = new FinanceSettleOrderReceivePo();
        po.setFinanceSettleOrderReceiveId(dto.getFinanceSettleOrderReceiveId());
        po.setVersion(dto.getFinanceSettleOrderReceiveVersion());
        po.setFinanceSettleOrderNo(settleOrderPo.getFinanceSettleOrderNo());
        po.setSupplierCode(settleOrderPo.getSupplierCode());
        po.setAccount(supplierPaymentAccountPo.getAccount());
        po.setSupplierPaymentAccountType(supplierPaymentAccountPo.getSupplierPaymentAccountType());
        po.setBankName(supplierPaymentAccountPo.getBankName());
        po.setAccountUsername(supplierPaymentAccountPo.getAccountUsername());
        po.setBankSubbranchName(supplierPaymentAccountPo.getBankSubbranchName());
        po.setAccountRemarks(supplierPaymentAccountPo.getRemarks());
        po.setBankProvince(supplierPaymentAccountPo.getBankProvince());
        po.setBankCity(supplierPaymentAccountPo.getBankCity());
        po.setBankArea(supplierPaymentAccountPo.getBankArea());
        po.setSubject(supplierPaymentAccountPo.getSubject());
        Currency currency
                = supplierPaymentAccountPo.getSupplierPaymentCurrencyType()
                .toCurrency();
        po.setCurrency(currency);
        po.setExchangeRate(currency.toRmbExchangeRate());

        BigDecimal currencyAmount = dto.getCurrencyAmount();
        po.setCurrencyAmount(currencyAmount);
        po.setExpectReceiveAmount(Currency.currencyExchange(currencyAmount, currency, Currency.RMB));
        po.setExpectReceiveDate(dto.getExpectReceiveDate());
        return po;
    }

    public static FinancePaymentItemPo buildFinancePaymentItemPo(String settleOrderNo, AddPaymentRecordDto dto, SupplierPaymentAccountPo supplierPaymentAccountPo) {
        FinancePaymentItemPo financePaymentItemPo = new FinancePaymentItemPo();
        financePaymentItemPo.setPaymentBizNo(settleOrderNo);
        financePaymentItemPo.setPaymentBizType(PaymentBizType.SETTLEMENT);
        financePaymentItemPo.setSupplierCode(dto.getSupplierCode());
        financePaymentItemPo.setAccount(supplierPaymentAccountPo.getAccount());
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

        // 付款金额
        BigDecimal paymentMoney = dto.getPaymentMoney();
        financePaymentItemPo.setPaymentMoney(paymentMoney);

        // 目标付款金额
        BigDecimal targetPaymentMoney = dto.getTargetPaymentMoney();
        financePaymentItemPo.setTargetPaymentMoney(targetPaymentMoney);

        Currency currency = dto.getCurrency();
        financePaymentItemPo.setCurrency(currency);

        // 设置目标汇率，人民币汇率
        if (Objects.equals(Currency.RMB, currency)) {
            financePaymentItemPo.setExchangeRate(BigDecimal.ONE);
            financePaymentItemPo.setRmbExchangeRate(BigDecimal.ONE);
        } else if (Objects.equals(Currency.USD, currency)) {
            BigDecimal usdExchangeRate = dto.getExchangeRate();

            financePaymentItemPo.setRmbExchangeRate(usdExchangeRate);
            financePaymentItemPo.setExchangeRate(usdExchangeRate);
        } else {
            throw new ParamIllegalException("添加付款记录失败！无效的币种类型，请联系业务人员配置后添加付款记录");
        }

        // 人民币金额：
        financePaymentItemPo.setRmbPaymentMoney(targetPaymentMoney);

        financePaymentItemPo.setPaymentDate(LocalDateTime.now());
        financePaymentItemPo.setBankProvince(supplierPaymentAccountPo.getBankProvince());
        financePaymentItemPo.setBankCity(supplierPaymentAccountPo.getBankCity());
        financePaymentItemPo.setBankArea(supplierPaymentAccountPo.getBankArea());
        return financePaymentItemPo;
    }

    public static FinanceSettleOrderApproveBo buildFinanceSettleOrderApproveBo(FinanceSettleOrderPo settleOrderPo,
                                                                               List<FinanceSettleOrderItemPo> settleOrderItemPos,
                                                                               OrgVo orgVo,
                                                                               List<String> instanceCodes,
                                                                               String settlementLink,
                                                                               String reCreateStr) {
        final String supplierCode = settleOrderPo.getSupplierCode();
        final BigDecimal settleAmount = settleOrderPo.getSettleAmount();
        final LocalDateTime createTime = settleOrderPo.getCreateTime();
        final String settleOrderNo = settleOrderPo.getFinanceSettleOrderNo();
        String remarks = settleOrderPo.getRemarks();

        FinanceSettleOrderApproveBo bo = new FinanceSettleOrderApproveBo();
        bo.setRecipientSubject(RECIPIENT_SUBJECT);
        bo.setCargoCompanyName(StrUtil.format("货物供应商{}", supplierCode));
        bo.setSettleAmount(settleAmount.toString());
        bo.setSettlePriceUnit(ScmFormatUtil.convertToThousandFormat(settleAmount) + " " + ScmConstant.RMB_MONEY_UNIT);
        bo.setSettlementType("货款");

        LocalDateTime toCnCreateTime = TimeUtil.convertZone(createTime, TimeZoneId.UTC, TimeZoneId.CN);
        bo.setApplyDate(toCnCreateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        String dept = Arrays.stream(orgVo.getOrgChainName().split("-")).skip(1).findFirst().orElse("");
        bo.setDept(dept);

        bo.setReason(StrUtil.format("{}家申请支付{}货款金额结算总金额{}元，按支付当天实时汇率支付{}",
                supplierCode, reCreateStr, settleAmount.toString(), settlementLink + settleOrderNo));

        List<FinanceSettleOrderApproveBo.SettlementDetailApproveBo> detail = settleOrderItemPos.stream()
                .map(settleOrderItemPo -> {
                    FinanceSettleOrderApproveBo.SettlementDetailApproveBo info = new FinanceSettleOrderApproveBo.SettlementDetailApproveBo();
                    info.setDetailType(Objects.nonNull(settleOrderItemPo.getFinanceSettleOrderItemType()) ? settleOrderItemPo.getFinanceSettleOrderItemType().getRemark() : "");
                    info.setRmbText(ScmFormatUtil.convertToChinese(settleOrderItemPo.getSettleAmount()));
                    info.setRmb(settleOrderItemPo.getSettleAmount());
                    return info;
                })
                .collect(Collectors.toList());
        bo.setDetail(detail);

        bo.setInstanceCodes(instanceCodes);
        bo.setRemarks(remarks);
        return bo;
    }

    public static WorkflowTransferDto buildWorkflowTransferDto(String workflowNo,
                                                               String taskId,
                                                               String comment,
                                                               UserVo toUserInfo) {
        WorkflowTransferDto dto = new WorkflowTransferDto();
        dto.setWorkflowNo(workflowNo);
        dto.setTaskId(taskId);
        dto.setComment(comment);
        dto.setTransferUserCode(toUserInfo.getUserCode());
        dto.setUserCode(GlobalContext.getUserKey());
        return dto;
    }

    public static SettleOrderDetailVo buildSettleOrderDetailVo(FinanceSettleOrderPo settleOrderPo,
                                                               List<FinanceSettleOrderItemPo> settleOrderItemPos,
                                                               FinanceSettleCarryoverOrderPo carryoverOrder,
                                                               List<FinanceSettleCarryoverOrderPo> carryoverOrderPos,
                                                               List<FinanceSettleOrderReceivePo> receivePos,
                                                               List<FinancePaymentItemPo> paymentItemPos,
                                                               Map<Long, List<String>> attachmentMap,
                                                               SupplierPo supplierPo) {
        SettleOrderDetailVo detailVo = new SettleOrderDetailVo();

        // 设置 FinanceSettleOrderPo 相关属性
        detailVo.setSettleOrderNo(settleOrderPo.getFinanceSettleOrderNo());
        detailVo.setSettleOrderStatus(settleOrderPo.getFinanceSettleOrderStatus());
        detailVo.setSupplierCode(settleOrderPo.getSupplierCode());
        detailVo.setSupplierGrade(Objects.nonNull(supplierPo) ? supplierPo.getSupplierGrade() : null);
        detailVo.setCreateTime(settleOrderPo.getCreateTime());
        detailVo.setCtrlUser(settleOrderPo.getCtrlUser());
        detailVo.setCtrlUsername(settleOrderPo.getCtrlUsername());
        detailVo.setWorkflowNo(settleOrderPo.getWorkflowNo());
        detailVo.setTaskId(settleOrderPo.getTaskId());
        detailVo.setTotalSettleAmount(settleOrderPo.getSettleAmount());
        detailVo.setCurrency(Currency.RMB);
        detailVo.setTotalPayAmount(settleOrderPo.getPayAmount());
        detailVo.setTotalReceiveAmount(settleOrderPo.getReceiveAmount());
        detailVo.setRemarks(settleOrderPo.getRemarks());
        detailVo.setVersion(settleOrderPo.getVersion());

        BigDecimal totalPayedAmount = CollectionUtils.isEmpty(paymentItemPos) ? BigDecimal.ZERO :
                paymentItemPos.stream()
                        .map(FinancePaymentItemPo::getRmbPaymentMoney)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
        detailVo.setTotalPayedAmount(totalPayedAmount);

        if (Objects.nonNull(carryoverOrder)) {
            detailVo.setCarryoverOrderNo(carryoverOrder.getFinanceSettleCarryoverOrderNo());
            detailVo.setAvailableCarryoverAmount(carryoverOrder.getAvailableCarryoverAmount());
        }

        // 设置 FinanceSettleOrderItemPo 列表相关属性
        List<SettleOrderDetailVo.SettleOrderItemVo> settleOrderItemVos = settleOrderItemPos.stream()
                .map(settleOrderItemPo -> {
                    SettleOrderDetailVo.SettleOrderItemVo itemVo = new SettleOrderDetailVo.SettleOrderItemVo();

                    String businessNo = settleOrderItemPo.getBusinessNo();
                    itemVo.setBusinessNo(businessNo);

                    itemVo.setFinanceSettleOrderItemType(settleOrderItemPo.getFinanceSettleOrderItemType());
                    itemVo.setPayAmount(settleOrderItemPo.getPayAmount());
                    itemVo.setReceiveAmount(settleOrderItemPo.getReceiveAmount());
                    itemVo.setSettleAmount(settleOrderItemPo.getSettleAmount());

                    FinanceSettleCarryoverOrderPo matchCarryoverOrder = carryoverOrderPos.stream()
                            .filter(carryoverOrderPo -> Objects.equals(businessNo, carryoverOrderPo.getFinanceSettleCarryoverOrderNo()))
                            .findFirst()
                            .orElse(null);
                    itemVo.setCarryoverAmount(Objects.isNull(matchCarryoverOrder) ? BigDecimal.ZERO : matchCarryoverOrder.getCarryoverAmount());

                    // 跳转单号
                    if (Objects.equals(FinanceSettleOrderItemType.RECO_ORDER, settleOrderItemPo.getFinanceSettleOrderItemType())) {
                        itemVo.setRedirectDocumentNo(businessNo);
                    }
                    if (Objects.equals(FinanceSettleOrderItemType.CARRYOVER_ORDER, settleOrderItemPo.getFinanceSettleOrderItemType())
                            && Objects.nonNull(matchCarryoverOrder)) {
                        itemVo.setRedirectDocumentNo(matchCarryoverOrder.getFinanceSettleOrderNo());
                    }
                    return itemVo;
                })
                .collect(Collectors.toList());
        detailVo.setSettleOrderItems(settleOrderItemVos);

        // 设置 FinanceSettleOrderReceivePo 列表相关属性
        List<SettleOrderDetailVo.SettleOrderAccountVo> receiveVos = receivePos.stream()
                .map(receivePo -> {
                    SettleOrderDetailVo.SettleOrderAccountVo receiveVo = new SettleOrderDetailVo.SettleOrderAccountVo();
                    receiveVo.setFinanceSettleOrderReceiveId(receivePo.getFinanceSettleOrderReceiveId());
                    receiveVo.setSupplierPaymentAccountType(receivePo.getSupplierPaymentAccountType());
                    receiveVo.setAccountUsername(receivePo.getAccountUsername());
                    receiveVo.setAccount(receivePo.getAccount());
                    receiveVo.setBankName(receivePo.getBankName());
                    receiveVo.setBankSubbranchName(receivePo.getBankSubbranchName());
                    receiveVo.setAccountRemarks(receivePo.getAccountRemarks());
                    receiveVo.setBankProvince(receivePo.getBankProvince());
                    receiveVo.setBankCity(receivePo.getBankCity());
                    receiveVo.setBankArea(receivePo.getBankArea());
                    receiveVo.setCurrencyAmount(receivePo.getCurrencyAmount());
                    receiveVo.setExpectReceiveAmount(receivePo.getExpectReceiveAmount());
                    receiveVo.setCurrency(receivePo.getCurrency());
                    receiveVo.setExpectReceiveDate(receivePo.getExpectReceiveDate());
                    receiveVo.setFinanceSettleOrderReceiveVersion(receivePo.getVersion());
                    return receiveVo;
                })
                .collect(Collectors.toList());
        detailVo.setSettleOrderAccounts(receiveVos);


        // 设置 FinancePaymentItemPo 列表相关属性
        List<SettleOrderDetailVo.PaymentRecordVo> paymentItemVos = paymentItemPos.stream()
                .map(paymentItemPo -> {
                    SettleOrderDetailVo.PaymentRecordVo paymentItemVo = new SettleOrderDetailVo.PaymentRecordVo();

                    Long financePaymentItemId = paymentItemPo.getFinancePaymentItemId();

                    paymentItemVo.setAccount(paymentItemPo.getAccount());
                    paymentItemVo.setPaymentSubject(paymentItemPo.getPaymentSubject());
                    paymentItemVo.setBankName(paymentItemPo.getBankName());
                    paymentItemVo.setAccountUsername(paymentItemPo.getAccountUsername());
                    paymentItemVo.setBankSubbranchName(paymentItemPo.getBankSubbranchName());
                    paymentItemVo.setRecipientSubject(paymentItemPo.getSubject());
                    paymentItemVo.setPaymentReason(paymentItemPo.getPaymentReason());
                    paymentItemVo.setPaymentRemark(paymentItemPo.getPaymentRemark());
                    paymentItemVo.setFileCodeList(attachmentMap.get(financePaymentItemId));
                    paymentItemVo.setRecipientSubject(paymentItemPo.getRecipientSubject());
                    paymentItemVo.setPaymentMoney(paymentItemPo.getPaymentMoney());
                    paymentItemVo.setCurrency(paymentItemPo.getCurrency());
                    paymentItemVo.setExchangeRate(paymentItemPo.getExchangeRate());
                    paymentItemVo.setTargetPaymentMoney(paymentItemPo.getTargetPaymentMoney());
                    paymentItemVo.setRmbExchangeRate(paymentItemPo.getRmbExchangeRate());
                    paymentItemVo.setRmbPaymentMoney(paymentItemPo.getRmbPaymentMoney());
                    paymentItemVo.setPaymentDate(paymentItemPo.getPaymentDate());
                    paymentItemVo.setBankProvince(paymentItemPo.getBankProvince());
                    paymentItemVo.setBankCity(paymentItemPo.getBankCity());
                    paymentItemVo.setBankArea(paymentItemPo.getBankArea());
                    return paymentItemVo;
                })
                .collect(Collectors.toList());
        detailVo.setPaymentRecords(paymentItemVos);

        BooleanType validateAddPayment;
        if (Arrays.asList(FinanceSettleOrderStatus.INVALIDATE, FinanceSettleOrderStatus.SETTLE_COMPLETED)
                .contains(settleOrderPo.getFinanceSettleOrderStatus()) ||
                Objects.nonNull(carryoverOrder) ||
                !Objects.equals(GlobalContext.getUserKey(), settleOrderPo.getCtrlUser()) ||
                settleOrderPo.getSettleAmount()
                        .compareTo(BigDecimal.ZERO) < 0) {
            validateAddPayment = BooleanType.FALSE;
        } else {
            validateAddPayment = BooleanType.TRUE;
        }
        detailVo.setValidateAddPayment(validateAddPayment);
        return detailVo;
    }

    public static List<FinanceSettleOrderItemExportVo> buildFinanceSettleOrderExportItemVos(List<FinanceSettleOrderPo> settleOrderPos,
                                                                                            List<FinanceSettleOrderItemPo> settleOrderItemPos) {
        return settleOrderItemPos.stream()
                .map(settleOrderItemPo -> {
                    FinanceSettleOrderItemExportVo exportItemVo = new FinanceSettleOrderItemExportVo();

                    String settleOrderNo = settleOrderItemPo.getFinanceSettleOrderNo();
                    FinanceSettleOrderPo matchSettleOrderPo = settleOrderPos.stream()
                            .filter(settleOrderPo -> Objects.equals(settleOrderNo,
                                    settleOrderPo.getFinanceSettleOrderNo()))
                            .findFirst()
                            .orElse(null);
                    if (Objects.nonNull(matchSettleOrderPo)) {
                        exportItemVo.setSettleOrderNo(matchSettleOrderPo.getFinanceSettleOrderNo());
                        exportItemVo.setSettleOrderStatusRemark(matchSettleOrderPo.getFinanceSettleOrderStatus()
                                .getRemark());
                        exportItemVo.setSupplierCode(matchSettleOrderPo.getSupplierCode());
                        exportItemVo.setApplyTime(
                                ScmTimeUtil.localDateTimeToStr(matchSettleOrderPo.getApplyTime(), TimeZoneId.CN,
                                        DatePattern.NORM_DATETIME_PATTERN));
                        exportItemVo.setCtrlUserName(matchSettleOrderPo.getCtrlUsername());
                    }

                    String businessNo = settleOrderItemPo.getBusinessNo();

                    exportItemVo.setBusinessNo(businessNo);
                    exportItemVo.setReceiveAmount(settleOrderItemPo.getReceiveAmount());
                    exportItemVo.setPayAmount(settleOrderItemPo.getPayAmount());
                    exportItemVo.setSettleAmount(settleOrderItemPo.getSettleAmount());
                    return exportItemVo;
                })
                .collect(Collectors.toList());
    }

    public static List<SettleOrderPageVo> buildSettleOrderPageVos(List<FinanceSettleOrderPo> financeSettleOrderPos,
                                                                  Map<String, BigDecimal> settlerOrderAmountMap) {
        return financeSettleOrderPos.stream()
                .map(financeSettleOrderPo -> {
                    SettleOrderPageVo pageVo = new SettleOrderPageVo();
                    String financeSettleOrderNo = financeSettleOrderPo.getFinanceSettleOrderNo();

                    pageVo.setSettleOrderNo(financeSettleOrderNo);
                    pageVo.setSupplierCode(financeSettleOrderPo.getSupplierCode());
                    pageVo.setSettleOrderStatus(financeSettleOrderPo.getFinanceSettleOrderStatus());
                    pageVo.setTotalPayableAmount(financeSettleOrderPo.getPayAmount());
                    pageVo.setTotalReceivableAmount(financeSettleOrderPo.getReceiveAmount());
                    pageVo.setTotalSettleAmount(financeSettleOrderPo.getSettleAmount());
                    pageVo.setPaidAmount(settlerOrderAmountMap.getOrDefault(financeSettleOrderNo, BigDecimal.ZERO));
                    pageVo.setCreateTime(financeSettleOrderPo.getCreateTime());
                    pageVo.setCtrlUser(financeSettleOrderPo.getCtrlUser());
                    pageVo.setTaskId(financeSettleOrderPo.getTaskId());
                    return pageVo;
                })
                .collect(Collectors.toList());
    }

    public static List<SupplierVo> buildSupplierVo(List<String> supplierCodes,
                                                   List<SupplierPo> supplierCodesAndNames) {
        return supplierCodes.stream()
                .map(supplierCode -> {
                    SupplierPo matchSupplierPo = supplierCodesAndNames.stream()
                            .filter(supplierPo -> Objects.equals(supplierCode, supplierPo.getSupplierCode()))
                            .findFirst()
                            .orElse(null);
                    if (Objects.nonNull(matchSupplierPo)) {
                        SupplierVo supplierVo = new SupplierVo();
                        supplierVo.setSupplierCode(supplierCode);
                        supplierVo.setSupplierName(matchSupplierPo.getSupplierName());
                        supplierVo.setSupplierGrade(matchSupplierPo.getSupplierGrade());
                        return supplierVo;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(SupplierVo::getSupplierGrade))
                .collect(Collectors.toList());
    }


    public static FinanceSettleOrderCreateResultBo buildFinanceSettleOrderCreateResultBo(FinanceSettleOrderPo financeSettleOrderPo,
                                                                                         List<FinanceSettleOrderItemPo> settleOrderItems) {
        FinanceSettleOrderCreateResultBo bo = new FinanceSettleOrderCreateResultBo();
        bo.setFinanceSettleOrderNo(financeSettleOrderPo.getFinanceSettleOrderNo());

        List<FinanceSettleOrderCreateResultBo.FinanceSettleOrderItemBo> items = settleOrderItems.stream()
                .map(settleOrderItemPo -> {
                    FinanceSettleOrderCreateResultBo.FinanceSettleOrderItemBo itemBo
                            = new FinanceSettleOrderCreateResultBo.FinanceSettleOrderItemBo();
                    itemBo.setBusinessNo(settleOrderItemPo.getBusinessNo());
                    itemBo.setFinanceSettleOrderItemType(settleOrderItemPo.getFinanceSettleOrderItemType());
                    return itemBo;
                })
                .collect(Collectors.toList());
        bo.setSettleOrderItems(items);
        return bo;
    }

    public static RecoOrderBindingSettleOrderBo buildRecoOrderBindingSettleOrderBo(String financeSettleOrderNo,
                                                                                   List<FinanceSettleOrderCreateResultBo.FinanceSettleOrderItemBo> settleOrderItems) {
        RecoOrderBindingSettleOrderBo recoOrderBindingSettleOrderBo = new RecoOrderBindingSettleOrderBo();
        recoOrderBindingSettleOrderBo.setFinanceSettleOrderNo(financeSettleOrderNo);

        List<RecoOrderBindingSettleOrderBo.RecoOrderBindingSettleOrderItemBo> recoOrderBindingSettleOrderItemList
                = settleOrderItems.stream()
                .map(settleOrderItem -> {
                    RecoOrderBindingSettleOrderBo.RecoOrderBindingSettleOrderItemBo itemBo
                            = new RecoOrderBindingSettleOrderBo.RecoOrderBindingSettleOrderItemBo();
                    itemBo.setFinanceRecoOrderNo(settleOrderItem.getBusinessNo());
                    return itemBo;
                })
                .collect(Collectors.toList());
        recoOrderBindingSettleOrderBo.setRecoOrderBindingSettleOrderItemList(recoOrderBindingSettleOrderItemList);
        return recoOrderBindingSettleOrderBo;
    }

    public static List<FinanceSettleOrderExportVo> buildFinanceSettleOrderExportVos(List<FinanceSettleOrderPo> records,
                                                                                    Map<String, BigDecimal> paymentBizNoToSum,
                                                                                    List<FinanceSettleCarryoverOrderPo> carryoverOrderPos) {
        List<FinanceSettleOrderExportVo> vos = new ArrayList<>();

        for (FinanceSettleOrderPo record : records) {
            FinanceSettleOrderExportVo vo = new FinanceSettleOrderExportVo();
            vo.setSettleOrderNo(record.getFinanceSettleOrderNo());
            vo.setSettleOrderStatus(record.getFinanceSettleOrderStatus()
                    .getRemark());
            vo.setSupplierCode(record.getSupplierCode());
            vo.setPayAmount(record.getPayAmount());
            vo.setReceiveAmount(record.getReceiveAmount());
            vo.setSettleAmount(record.getSettleAmount());
            vo.setPaidAmount(paymentBizNoToSum.getOrDefault(record.getFinanceSettleOrderNo(), BigDecimal.ZERO));
            vo.setCtrlUsername(record.getCtrlUsername());
            vo.setCreateTime(ScmTimeUtil.localDateTimeToStr(record.getCreateTime(), TimeZoneId.CN,
                    DatePattern.NORM_DATETIME_PATTERN));
            vo.setSupplierSubmitTime(ScmTimeUtil.localDateTimeToStr(record.getSupplierSubmitTime(), TimeZoneId.CN,
                    DatePattern.NORM_DATETIME_PATTERN));
            vo.setFollowConfirmUsername(record.getFollowerConfirmUsername());
            vo.setFollowerConfirmTime(ScmTimeUtil.localDateTimeToStr(record.getFollowerConfirmTime(), TimeZoneId.CN,
                    DatePattern.NORM_DATETIME_PATTERN));
            vo.setWorkflowFinishTime(ScmTimeUtil.localDateTimeToStr(record.getWorkflowFinishTime(), TimeZoneId.CN,
                    DatePattern.NORM_DATETIME_PATTERN));
            vo.setSettleFinishTime(ScmTimeUtil.localDateTimeToStr(record.getSettleFinishTime(), TimeZoneId.CN,
                    DatePattern.NORM_DATETIME_PATTERN));

            FinanceSettleCarryoverOrderPo matchCarryOrderPo = carryoverOrderPos.stream()
                    .filter(carryoverOrderPo -> Objects.equals(record.getFinanceSettleOrderNo(),
                            carryoverOrderPo.getFinanceSettleOrderNo()))
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(matchCarryOrderPo)) {
                vo.setFinanceSettleCarryoverOrderNo(matchCarryOrderPo.getFinanceSettleCarryoverOrderNo());
                vo.setFinanceSettleCarryoverOrderStatus(matchCarryOrderPo.getFinanceSettleCarryoverOrderStatus()
                        .getRemark());
                vo.setAvailableCarryoverAmount(matchCarryOrderPo.getAvailableCarryoverAmount());
            }

            vos.add(vo);
        }
        return vos;
    }
}
