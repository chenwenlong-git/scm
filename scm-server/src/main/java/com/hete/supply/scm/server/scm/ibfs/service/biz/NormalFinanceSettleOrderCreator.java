package com.hete.supply.scm.server.scm.ibfs.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.FinanceSettleOrderStatus;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.common.util.ScmFormatUtil;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.ibfs.builder.FinanceSettleOrderBuilder;
import com.hete.supply.scm.server.scm.ibfs.dao.FinanceSettleOrderDao;
import com.hete.supply.scm.server.scm.ibfs.dao.FinanceSettleOrderItemDao;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.CreateFinanceSettleOrderBo;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.FinanceSettleOrderCreateResultBo;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.RecoOrderBindingSettleOrderBo;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceSettleOrderItemPo;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceSettleOrderPo;
import com.hete.supply.scm.server.scm.ibfs.enums.FinanceSettleOrderItemType;
import com.hete.supply.scm.server.scm.ibfs.service.base.AbstractFinanceSettleOrderCreator;
import com.hete.supply.scm.server.scm.ibfs.service.base.RecoOrderBaseService;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/5/23.
 */
public class NormalFinanceSettleOrderCreator extends AbstractFinanceSettleOrderCreator<CreateFinanceSettleOrderBo,
        FinanceSettleOrderCreateResultBo> {

    private final RecoOrderBaseService recoOrderBaseService;
    private final LogBaseService logBaseService;

    public NormalFinanceSettleOrderCreator(FinanceSettleOrderDao financeSettleOrderDao,
                                           FinanceSettleOrderItemDao financeSettleOrderItemDao,
                                           IdGenerateService idGenerateService,
                                           RecoOrderBaseService recoOrderBaseService,
                                           LogBaseService logBaseService) {
        super(financeSettleOrderDao, financeSettleOrderItemDao, idGenerateService);
        this.recoOrderBaseService = recoOrderBaseService;
        this.logBaseService = logBaseService;
    }

    @Override
    protected void createPreOperations(CreateFinanceSettleOrderBo createFinanceSettleOrderBo) {
        ParamValidUtils.requireNotNull(createFinanceSettleOrderBo, "创建结算单失败！信息为空。");
        ParamValidUtils.requireNotBlank(createFinanceSettleOrderBo.getSupplierCode(), "创建结算单失败！供应商编码为空");
        ParamValidUtils.requireNotBlank(createFinanceSettleOrderBo.getSupplierAlias(), "创建结算单失败！供应商别名为空");
        ParamValidUtils.requireNotEmpty(createFinanceSettleOrderBo.getSettleOrderItems(),
                "创建结算单失败！对账单/结转单信息为空");
    }

    @Override
    protected FinanceSettleOrderCreateResultBo performCreateQcOrder(CreateFinanceSettleOrderBo createFinanceSettleOrderBo) {
        // 获取供应商信息 & 结算单号 & 计算明细
        final String supplierCode = createFinanceSettleOrderBo.getSupplierCode();
        final String supplierAlias = createFinanceSettleOrderBo.getSupplierAlias();
        String followUser = createFinanceSettleOrderBo.getFollowUser();
        final String financeSettleOrderNo = createFinanceSettleOrderNo(supplierAlias);
        final List<FinanceSettleOrderItemPo> settleOrderItems = createFinanceSettleOrderBo.getSettleOrderItems();

        // 计算总收款金额和总付款金额
        BigDecimal totalReceiveAmount = settleOrderItems.stream()
                .map(FinanceSettleOrderItemPo::getReceiveAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPayAmount = settleOrderItems.stream()
                .map(FinanceSettleOrderItemPo::getPayAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 计算结算金额
        BigDecimal settleAmount = totalPayAmount.subtract(totalReceiveAmount);

        // 创建结算单
        FinanceSettleOrderPo financeSettleOrderPo = new FinanceSettleOrderPo();
        financeSettleOrderPo.setFinanceSettleOrderNo(financeSettleOrderNo);
        financeSettleOrderPo.setFinanceSettleOrderStatus(FinanceSettleOrderStatus.WAIT_SUPPLIER_SUBMIT);
        financeSettleOrderPo.setSupplierCode(supplierCode);
        financeSettleOrderPo.setReceiveAmount(totalReceiveAmount);
        financeSettleOrderPo.setPayAmount(totalPayAmount);
        financeSettleOrderPo.setSettleAmount(settleAmount);
        financeSettleOrderPo.setFollowUser(followUser);
        financeSettleOrderDao.insert(financeSettleOrderPo);

        settleOrderItems.forEach(settleOrderItemPo -> settleOrderItemPo.setFinanceSettleOrderNo(financeSettleOrderNo));
        financeSettleOrderItemDao.insertBatch(settleOrderItems);

        return FinanceSettleOrderBuilder.buildFinanceSettleOrderCreateResultBo(financeSettleOrderPo, settleOrderItems);
    }

    private String createFinanceSettleOrderNo(String supplierAlias) {
        return idGenerateService.getConfuseCode(
                ScmConstant.SETTLEMENT_ORDER_NO_PREFIX + ScmFormatUtil.subStringLastThree(supplierAlias),
                TimeType.CN_DAY, ConfuseLength.L_4);
    }

    @Override
    protected void doAfterCreation(FinanceSettleOrderCreateResultBo result) {
        final String financeSettleOrderNo = result.getFinanceSettleOrderNo();
        final List<FinanceSettleOrderCreateResultBo.FinanceSettleOrderItemBo> settleOrderItems
                = result.getSettleOrderItems();

        // 记录操作日志
        logBaseService.simpleLog(LogBizModule.FINANCE_SETTLE_ORDER_STATUS, ScmConstant.SETTLE_ORDER_LOG_VERSION,
                financeSettleOrderNo, FinanceSettleOrderStatus.WAIT_SUPPLIER_SUBMIT.getRemark(),
                Collections.singletonList(new LogVersionBo("操作", null, "新建结算单")));

        // 绑定对账单与结算单关系
        List<FinanceSettleOrderCreateResultBo.FinanceSettleOrderItemBo> recoList = settleOrderItems.stream()
                .filter(settleOrderItem -> Objects.equals(FinanceSettleOrderItemType.RECO_ORDER,
                        settleOrderItem.getFinanceSettleOrderItemType()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(recoList)) {
            RecoOrderBindingSettleOrderBo recoOrderBindingSettleOrderBo
                    = FinanceSettleOrderBuilder.buildRecoOrderBindingSettleOrderBo(financeSettleOrderNo,
                    settleOrderItems);
            recoOrderBaseService.recoOrderBindingSettleOrder(recoOrderBindingSettleOrderBo);
        }
    }
}
