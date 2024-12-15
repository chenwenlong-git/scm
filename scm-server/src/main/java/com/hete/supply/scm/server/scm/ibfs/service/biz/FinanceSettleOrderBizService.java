package com.hete.supply.scm.server.scm.ibfs.service.biz;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.hete.supply.mc.api.workflow.entity.dto.WorkflowNoListDto;
import com.hete.supply.mc.api.workflow.entity.vo.WorkflowInstanceCodeVo;
import com.hete.supply.mc.api.workflow.entity.vo.WorkflowTaskVo;
import com.hete.supply.scm.api.scm.entity.dto.GetSettleOrderDetailDto;
import com.hete.supply.scm.api.scm.entity.dto.SearchSettleOrderDto;
import com.hete.supply.scm.api.scm.entity.enums.FinanceSettleOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.FinanceSettleOrderExportVo;
import com.hete.supply.scm.api.scm.entity.vo.FinanceSettleOrderItemExportVo;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.common.util.BizValidUtils;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.common.util.ScmFormatUtil;
import com.hete.supply.scm.remote.dubbo.McRemoteService;
import com.hete.supply.scm.remote.dubbo.UdbRemoteService;
import com.hete.supply.scm.server.scm.dao.ScmImageDao;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.entity.po.ScmImagePo;
import com.hete.supply.scm.server.scm.enums.DefaultDatabaseTime;
import com.hete.supply.scm.server.scm.enums.FeishuAuditOrderType;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.feishu.dao.FeishuAuditOrderDao;
import com.hete.supply.scm.server.scm.feishu.service.base.AbstractApproveCreator;
import com.hete.supply.scm.server.scm.ibfs.builder.FinanceSettleOrderBuilder;
import com.hete.supply.scm.server.scm.ibfs.config.ScmFinanceProp;
import com.hete.supply.scm.server.scm.ibfs.dao.*;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.CarryoverCalculationBo;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.CreateFinanceSettleOrderBo;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.FinanceSettleOrderApproveBo;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.FinanceSettleOrderCreateResultBo;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.*;
import com.hete.supply.scm.server.scm.ibfs.entity.po.*;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.*;
import com.hete.supply.scm.server.scm.ibfs.enums.FinanceSettleCarryoverOrderStatus;
import com.hete.supply.scm.server.scm.ibfs.enums.FinanceSettleOrderItemType;
import com.hete.supply.scm.server.scm.ibfs.enums.SupplierConfirmResult;
import com.hete.supply.scm.server.scm.ibfs.service.base.*;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseBaseService;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPaymentAccountPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.api.scm.entity.enums.SupplierStatus;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierBaseService;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierPaymentAccountBaseService;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.scm.server.supplier.ibfs.entity.dto.*;
import com.hete.supply.scm.server.supplier.purchase.service.base.PurchaseReturnBaseService;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.supply.udb.api.entity.vo.OrgVo;
import com.hete.supply.udb.api.entity.vo.UserVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/5/23.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FinanceSettleOrderBizService {
    private final AuthBaseService authBaseService;
    private final FinanceSettleOrderDao settleOrderDao;
    private final FinanceSettleOrderItemDao settleOrderItemDao;
    private final FinanceSettleCarryoverOrderDao carryoverOrderDao;
    private final FinanceSettleOrderBaseService financeSettleOrderBaseService;
    private final IdGenerateService idGenerateService;
    private final SupplierBaseService supplierBaseService;
    private final FinanceSettleOrderReceiveDao settleOrderReceiveDao;
    private final LogBaseService logBaseService;
    private final FinancePaymentItemDao paymentItemDao;
    private final McRemoteService mcRemoteService;
    private final FeishuAuditOrderDao feishuAuditOrderDao;
    private final UdbRemoteService udbRemoteService;
    private final SupplierDao supplierDao;
    private final ScmImageDao scmImageDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final RecoOrderBaseService recoOrderBaseService;
    private final PurchaseBaseService purchaseBaseService;
    private final PrepaymentBaseService prepaymentBaseService;
    private final PurchaseReturnBaseService purchaseReturnBaseService;
    private final FinanceRecoOrderDao recoOrderDao;
    private final ScmImageBaseService scmImageBaseService;
    private final SupplierPaymentAccountBaseService supplierPaymentAccountBaseService;
    private final ScmFinanceProp scmFinanceProp;

    @Transactional(rollbackFor = Exception.class)
    public void createFinanceSettleOrder(GenerateSettleOrderDto dto) {
        final String supplierCode = dto.getSupplierCode();
        SupplierPo supplierPo
                = ParamValidUtils.requireNotNull(supplierBaseService.getSupplierByCode(supplierCode),
                "供应商信息不存在，请联系管理员维护");
        createFinanceSettleOrder(supplierPo);
    }

    @RedisLock(prefix = ScmRedisConstant.CREATE_SETTLE_ORDER, key = "#supplierPo.supplierCode", waitTime = 1,
            leaseTime = -1, exceptionDesc = "供应商结算单正在处理中，请稍后再试。")
    public void createFinanceSettleOrder(SupplierPo supplierPo) {
        // 获取供应商代码和别名
        final String supplierCode = supplierPo.getSupplierCode();
        final String supplierAlias = supplierPo.getSupplierAlias();

        // 校验并获取该供应商的财务对账单信息
        List<FinanceRecoOrderPo> financeRecoOrderPos = ParamValidUtils.requireNotEmpty(
                financeSettleOrderBaseService.getRecoOrdersBySupplierCode(supplierCode), "没有可生成的结算单的信息!");

        // 校验供应商别名是否为空
        ParamValidUtils.requireNotBlank(supplierAlias, "未配置供应商别名，请联系维护供应商别名");

        buildAndCreateFinanceSettleOrder(supplierPo, financeRecoOrderPos);
    }

    public void createFinanceSettleOrderJob() {
        Set<String> todaySettleSuppliers = financeSettleOrderBaseService.getTodaySettleSuppliers();
        if (CollectionUtils.isEmpty(todaySettleSuppliers)) {
            log.info("没有可生成的结算单的供应商信息!");
            return;
        }

        for (String supplierCode : todaySettleSuppliers) {
            financeSettleOrderBaseService.createFinanceSettleOrderJob(supplierCode);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.CREATE_SETTLE_ORDER, key = "#supplierPo.supplierCode", waitTime = 1,
            leaseTime = -1, exceptionDesc = "供应商结算单正在处理中，本次中断执行")
    public void createFinanceSettleOrderJob(SupplierPo supplierPo) {
        // 获取供应商代码和别名
        final String supplierCode = supplierPo.getSupplierCode();
        final String supplierAlias = supplierPo.getSupplierAlias();

        // 校验并获取该供应商的财务对账单信息
        List<FinanceRecoOrderPo> financeRecoOrderPos = financeSettleOrderBaseService.getRecoOrdersBySupplierCode(
                supplierCode);
        if (CollectionUtils.isEmpty(financeRecoOrderPos)) {
            log.info("{}没有可生成的结算单的信息!", supplierCode);
            return;
        }

        // 校验供应商别名是否为空
        if (StrUtil.isBlank(supplierAlias)) {
            log.error("定时任务创建结算单失败！未配置供应商{}别名，请相关同事注意! ", supplierCode);
            return;
        }

        buildAndCreateFinanceSettleOrder(supplierPo, financeRecoOrderPos);
    }

    /**
     * 构建并创建结算单。
     *
     * @param supplierPo          供应商代码。
     * @param financeRecoOrderPos 财务对账单列表。
     * @return 结算单创建结果对象。
     * @throws IllegalArgumentException 如果结算单信息不完整或无法生成结算单。
     */
    public void buildAndCreateFinanceSettleOrder(SupplierPo supplierPo,
                                                 List<FinanceRecoOrderPo> financeRecoOrderPos) {

        // 构建创建结算单的业务对象
        CreateFinanceSettleOrderBo createFinanceSettleOrderBo = new CreateFinanceSettleOrderBo();
        createFinanceSettleOrderBo.setSupplierCode(supplierPo.getSupplierCode());
        createFinanceSettleOrderBo.setSupplierAlias(supplierPo.getSupplierAlias());
        createFinanceSettleOrderBo.setFollowUser(supplierPo.getFollowUser());

        // 构建结算单明细项
        List<FinanceSettleOrderItemPo> settleOrderItemPos = FinanceSettleOrderBuilder.buildSettleOrderItemsByRecoOrders(
                financeRecoOrderPos);

        // 计算总结算金额
        BigDecimal totalSettleAmount = financeRecoOrderPos.stream()
                .map(FinanceRecoOrderPo::getSettlePrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 如果总结算金额大于0，计算并构建结转单明细项
        if (totalSettleAmount.compareTo(BigDecimal.ZERO) > 0) {
            CarryoverCalculationBo carryoverCalculationBo = financeSettleOrderBaseService.calculateCarryoverDetails(
                    totalSettleAmount, supplierPo.getSupplierCode());
            List<CarryoverCalculationBo.CarryoverOrderBo> carryoverOrders = carryoverCalculationBo.getCarryoverOrders();
            if (CollectionUtils.isNotEmpty(carryoverOrders)) {
                List<FinanceSettleOrderItemPo> carryOverSettleOrderItemPos
                        = FinanceSettleOrderBuilder.buildSettleOrderItemsByCarryoverOrder(carryoverOrders);
                settleOrderItemPos.addAll(carryOverSettleOrderItemPos);
            }

            List<FinanceSettleCarryoverOrderPo> updateCarryovers
                    = carryoverCalculationBo.getUpdateFinanceSettleCarryoverOrderPos();
            if (CollectionUtils.isNotEmpty(updateCarryovers)) {
                carryoverOrderDao.updateBatchByIdVersion(updateCarryovers);
            }

        }

        // 设置结算单明细项到业务对象中
        createFinanceSettleOrderBo.setSettleOrderItems(settleOrderItemPos);

        // 使用结算单创建器创建并保存结算单
        AbstractFinanceSettleOrderCreator<CreateFinanceSettleOrderBo, FinanceSettleOrderCreateResultBo> creator
                = new NormalFinanceSettleOrderCreator(settleOrderDao, settleOrderItemDao, idGenerateService,
                recoOrderBaseService, logBaseService);
        creator.createFinanceSettleOrder(createFinanceSettleOrderBo);
    }


    @RedisLock(prefix = ScmRedisConstant.BATCH_OPERATION_SETTLE_ORDER, key = "#dto.curUserKey", waitTime = 1,
            leaseTime = -1, exceptionDesc = "结算单批量转交处理中，请稍后重试")
    public void batchTransfer(FinanceSettleOrderTransferDto dto) {
        List<FinanceSettleOrderTransferDto.FianceSettleOrderTransferItemDto> transferItemDtoList
                = dto.getSettleOrderTransferItemDtoList();

        // 数据校验
        Set<String> financeSettleOrderNos = transferItemDtoList.stream()
                .map(FinanceSettleOrderTransferDto.FianceSettleOrderTransferItemDto::getSettleOrderNo)
                .collect(Collectors.toSet());
        List<FinanceSettleOrderPo> settleOrderPos
                = ParamValidUtils.requireNotEmpty(settleOrderDao.findByOrderNos(financeSettleOrderNos),
                "结算单不存在，请刷新页面后重试。");
        ParamValidUtils.requireEquals(true, Objects.equals(settleOrderPos.size(), financeSettleOrderNos.size()),
                "部分结算单不存在，请刷新页面后重试。");

        // 结算单状态校验
        List<FinanceSettleOrderStatus> illegalStatus = List.of(FinanceSettleOrderStatus.SETTLE_COMPLETED,
                FinanceSettleOrderStatus.INVALIDATE);
        List<FinanceSettleOrderPo> illegalSettleOrderList = settleOrderPos.stream()
                .filter(settleOrderPo -> illegalStatus.contains(settleOrderPo.getFinanceSettleOrderStatus()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(illegalSettleOrderList)) {
            throw new ParamIllegalException(StrUtil.format("{}状态结算完成/已作废，请重新选择",
                    illegalSettleOrderList.stream()
                            .map(FinanceSettleOrderPo::getFinanceSettleOrderNo)
                            .collect(Collectors.joining(","))));
        }

        // 登录信息校验 & 处理人校验
        String curUserKey = ParamValidUtils.requireNotBlank(GlobalContext.getUserKey(), "用户信息不存在，请先登录");
        List<FinanceSettleOrderPo> unBelongToCurUserList = settleOrderPos.stream()
                .filter(settleOrderPo -> !Objects.equals(curUserKey, settleOrderPo.getCtrlUser()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(unBelongToCurUserList)) {
            throw new ParamIllegalException(StrUtil.format("{}处理人不是当前登录账号，无法进行此操作",
                    unBelongToCurUserList.stream()
                            .map(FinanceSettleOrderPo::getFinanceSettleOrderNo)
                            .collect(Collectors.joining(","))));
        }
        // 转交人用户信息校验：
        UserVo toUserInfo = ParamValidUtils.requireNotNull(udbRemoteService.getByUserCode(dto.getTransferUser()),
                "被转交用户信息不存在，请联系业务人员处理");

        // 仅需变更结算单处理人
        List<FinanceSettleOrderStatus> onlyChangeCurUser = List.of(FinanceSettleOrderStatus.WAIT_SUPPLIER_SUBMIT,
                FinanceSettleOrderStatus.WAIT_FOLLOWUP_CONFIRM,
                FinanceSettleOrderStatus.WAIT_SETTLEMENT,
                FinanceSettleOrderStatus.PARTIAL_SETTLEMENT);
        List<FinanceSettleOrderPo> onlyChangeCurUserOrderList = settleOrderPos.stream()
                .filter(settleOrderPo -> onlyChangeCurUser.contains(settleOrderPo.getFinanceSettleOrderStatus()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(onlyChangeCurUserOrderList)) {
            financeSettleOrderBaseService.batchTransfer(onlyChangeCurUserOrderList, toUserInfo);
        }

        // 需要变更飞书结算单处理人
        List<FinanceSettleOrderStatus> changeFeishuCurUser = List.of(FinanceSettleOrderStatus.IN_APPROVAL);
        List<FinanceSettleOrderPo> changeFeishuCurUserList = settleOrderPos.stream()
                .filter(settleOrderPo -> changeFeishuCurUser.contains(settleOrderPo.getFinanceSettleOrderStatus()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(changeFeishuCurUserList)) {
            financeSettleOrderBaseService.batchTransferWithFeishu(changeFeishuCurUserList,
                    transferItemDtoList,
                    toUserInfo);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.SETTLE_ORDER_FLOWING, key = "#submitSettleOrderDto.settleOrderNo", waitTime
            = 1, leaseTime = -1, exceptionDesc = "结算单正在处理中，请稍后再试。")
    public void submitSettleOrder(SupplierSubmitSettleOrderDto submitSettleOrderDto) {
        final String financeSettleOrderNo = submitSettleOrderDto.getSettleOrderNo();
        final Integer version = submitSettleOrderDto.getVersion();

        // 数据校验：通过结算单ID列表查询结算单信息列表
        FinanceSettleOrderPo settleOrderPo = ParamValidUtils.requireNotNull(
                settleOrderDao.findByOrderNoAndVersion(financeSettleOrderNo, version),
                "结算单不存在或数据发生变更，请刷新页面后重试。");

        // 状态校验: 判断结算单状态=待供应商提交？
        validateOrderStatus(settleOrderPo, FinanceSettleOrderStatus.WAIT_SUPPLIER_SUBMIT);

        // 收款信息校验
        validateReceiveInfo(settleOrderPo);

        // 跟单人员校验： 通过当前供应商编码查询供应商跟单
        String supplierCode = settleOrderPo.getSupplierCode();
        SupplierPo supplierPo = ParamValidUtils.requireNotNull(supplierBaseService.getSupplierByCode(supplierCode),
                "供应商信息不存在，请联系管理员处理。");
        String followUserKey = ParamValidUtils.requireNotBlank(supplierPo.getFollowUser(),
                "供应商未绑定跟单，请联系管理员进行处理。");

        // 变更结算单信息：结算单处理人：跟单人，结算单状态：待跟单确认
        if (StrUtil.isNotBlank(settleOrderPo.getFollowUser())) {
            String followUser = settleOrderPo.getFollowUser();
            settleOrderPo.setCtrlUser(followUser);
            UserVo followUserInfo = ParamValidUtils.requireNotNull(udbRemoteService.getByUserCode(followUser),
                    "当前跟单用户信息不存在，请联系业务人员处理");
            settleOrderPo.setCtrlUsername(followUserInfo.getUsername());
        } else {
            settleOrderPo.setCtrlUser(followUserKey);
            settleOrderPo.setCtrlUsername(supplierPo.getFollowUsername());
        }

        settleOrderPo.setSupplierSubmitTime(LocalDateTime.now());
        settleOrderPo.setFinanceSettleOrderStatus(FinanceSettleOrderStatus.WAIT_FOLLOWUP_CONFIRM);
        settleOrderDao.updateByIdVersion(settleOrderPo);

        // 记录操作记录
        logBaseService.simpleLog(
                LogBizModule.FINANCE_SETTLE_ORDER_STATUS, ScmConstant.SETTLE_ORDER_LOG_VERSION,
                financeSettleOrderNo, settleOrderPo.getFinanceSettleOrderStatus().getRemark(),
                Collections.singletonList(new LogVersionBo("操作", null, "工厂提交"))
        );
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.SETTLE_ORDER_FLOWING, key = "#confirmOrderDto.settleOrderNo", waitTime = 1,
            leaseTime = -1, exceptionDesc = "结算单正在处理中，请稍后再试。")
    public void followerConfirm(SupplierConfirmOrderDto confirmOrderDto) {
        final String financeSettleOrderNo = confirmOrderDto.getSettleOrderNo();
        final Integer version = confirmOrderDto.getVersion();

        // 数据校验：结算单&结算明细
        FinanceSettleOrderPo settleOrderPo = ParamValidUtils.requireNotNull(
                settleOrderDao.findByOrderNoAndVersion(financeSettleOrderNo, version),
                "结算单数据已被修改或删除，请刷新页面后重试！");
        List<FinanceSettleOrderItemPo> settleOrderItemPos = ParamValidUtils.requireNotEmpty(
                settleOrderItemDao.findBySettleOrderNo(financeSettleOrderNo),
                "结算单明细信息已不存在，请刷新页面后重试");

        // 状态校验: 判断结算单状态=待跟单确认
        validateOrderStatus(settleOrderPo, FinanceSettleOrderStatus.WAIT_FOLLOWUP_CONFIRM);

        // 登录信息校验 & 处理人校验
        validateCtrlUser(settleOrderPo);

        String remarks = confirmOrderDto.getRemarks();

        final SupplierConfirmResult supplierConfirmResult = confirmOrderDto.getSupplierConfirmResult();
        if (Objects.equals(SupplierConfirmResult.REJECT, supplierConfirmResult)) {
            settleOrderPo.setFinanceSettleOrderStatus(FinanceSettleOrderStatus.WAIT_SUPPLIER_SUBMIT);
            settleOrderPo.setCtrlUser("");
            settleOrderPo.setCtrlUsername("");
            settleOrderPo.setFollowUser(GlobalContext.getUserKey());
            settleOrderPo.setFollowerConfirmTime(LocalDateTime.now());
            settleOrderPo.setFollowerConfirmUser(GlobalContext.getUserKey());
            settleOrderPo.setFollowerConfirmUsername(GlobalContext.getUsername());
            settleOrderPo.setRemarks(remarks);
            settleOrderDao.updateByIdVersion(settleOrderPo);

            logBaseService.simpleLog(
                    LogBizModule.FINANCE_SETTLE_ORDER_STATUS, ScmConstant.SETTLE_ORDER_LOG_VERSION,
                    financeSettleOrderNo, settleOrderPo.getFinanceSettleOrderStatus().getRemark(),
                    Collections.singletonList(new LogVersionBo("操作", null, "跟单确认-拒绝")));
        } else if (Objects.equals(SupplierConfirmResult.AGREE, supplierConfirmResult)) {
            // 收款信息校验
            validateReceiveInfo(settleOrderPo);

            UserVo userVo = ParamValidUtils.requireNotNull(udbRemoteService.getByUserCode(GlobalContext.getUserKey()),
                    "当前登录用户信息不存在，请联系业务人员处理");
            OrgVo orgVo = ParamValidUtils.requireNotNull(udbRemoteService.getOrgByCode(userVo.getOrgCode()),
                    "当前登录用户部门信息不存在，请联系业务人员处理");

            // 查询结算单关联工作流编号
            List<String> instanceCodes = Lists.newArrayList();
            String reCreateStr = "";
            List<String> recoOrderNos = settleOrderItemPos.stream()
                    .filter(settleOrderItemPo -> Objects.equals(FinanceSettleOrderItemType.RECO_ORDER, settleOrderItemPo.getFinanceSettleOrderItemType()))
                    .map(FinanceSettleOrderItemPo::getBusinessNo)
                    .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(recoOrderNos)) {
                List<FinanceRecoOrderPo> recoOrderPos = ParamValidUtils.requireNotEmpty(
                        recoOrderDao.getListByNoList(recoOrderNos),
                        "提交失败！结算单关联对账单信息不存在，请联系业务人员处理"
                );
                List<String> workflowNos = ParamValidUtils.requireNotEmpty(
                        recoOrderPos.stream().map(FinanceRecoOrderPo::getWorkflowNo).collect(Collectors.toList()),
                        "提交失败！结算单关联对账单审批信息缺失，请联系业务人员处理"
                );

                // 校验工作流是否存在
                WorkflowNoListDto queryWfParam = new WorkflowNoListDto();
                queryWfParam.setWorkflowNoList(workflowNos);
                List<WorkflowInstanceCodeVo> workflowInstanceCodeVos = ParamValidUtils.requireNotEmpty(
                        mcRemoteService.getInstanceCodeByWorkflowNoList(queryWfParam),
                        "提交失败！结算单关联对账单飞书审批信息缺失，请联系业务人员处理"
                );
                instanceCodes = workflowInstanceCodeVos.stream().map(WorkflowInstanceCodeVo::getInstanceCode).collect(Collectors.toList());

                // 拼接对账单对账开始周期
                List<String> resTimeStrList = recoOrderPos.stream()
                        .sorted(Comparator.comparing(FinanceRecoOrderPo::getReconciliationStartTime))
                        .map(repo -> TimeUtil.convertZone(repo.getReconciliationStartTime(), TimeZoneId.UTC, TimeZoneId.CN).format(DateTimeFormatter.ofPattern("yyyy-MM")))
                        .collect(Collectors.toList());
                reCreateStr = resTimeStrList.stream().distinct().map(String::valueOf).collect(Collectors.joining("、"));
            }

            // 赋值备注字段
            settleOrderPo.setRemarks(remarks);

            String settlementLink = scmFinanceProp.getSettlementLink();
            FinanceSettleOrderApproveBo approveBo
                    = FinanceSettleOrderBuilder.buildFinanceSettleOrderApproveBo(settleOrderPo, settleOrderItemPos, orgVo, instanceCodes, settlementLink, reCreateStr);
            AbstractApproveCreator<FinanceSettleOrderApproveBo> abstractApproveCreator
                    = new IbfsSettlementApproveCreator(idGenerateService, mcRemoteService, feishuAuditOrderDao);
            abstractApproveCreator.createFeiShuInstance(settleOrderPo.getFinanceSettleOrderId(), approveBo);

            settleOrderPo.setApplyTime(LocalDateTime.now());
            settleOrderPo.setFollowerConfirmTime(LocalDateTime.now());
            settleOrderPo.setFollowerConfirmUser(GlobalContext.getUserKey());
            settleOrderPo.setFollowerConfirmUsername(GlobalContext.getUsername());
            settleOrderPo.setFollowUser(GlobalContext.getUserKey());
            settleOrderPo.setFinanceSettleOrderStatus(FinanceSettleOrderStatus.FEISHU_APPROVAL_PROCESSING);
            settleOrderDao.updateByIdVersion(settleOrderPo);

            logBaseService.simpleLog(
                    LogBizModule.FINANCE_SETTLE_ORDER_STATUS, ScmConstant.SETTLE_ORDER_LOG_VERSION,
                    financeSettleOrderNo, "发起审批",
                    Collections.singletonList(new LogVersionBo("操作", null, "跟单确认-同意")));
        } else {
            throw new BizException("结算单跟单确认异常，处理结果只能是同意/拒绝");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.SETTLE_ORDER_FLOWING, key = "#invalidateOrderDto.settleOrderNo", waitTime =
            1, leaseTime = -1, exceptionDesc = "结算单正在处理中，请稍后再试。")
    public void invalidateOrder(SupplierInvalidateSettleOrderDto invalidateOrderDto) {
        final String financeSettleOrderNo = invalidateOrderDto.getSettleOrderNo();
        final Integer version = invalidateOrderDto.getVersion();

        // 数据校验：通过结算单ID列表查询结算单信息列表
        FinanceSettleOrderPo settleOrderPo = ParamValidUtils.requireNotNull(
                settleOrderDao.findByOrderNoAndVersion(financeSettleOrderNo, version),
                "结算单不存在或数据发生变更，请刷新页面后重试。");

        // 状态校验: 判断结算单状态 = 待供应商提交？
        validateOrderStatus(settleOrderPo, FinanceSettleOrderStatus.WAIT_SUPPLIER_SUBMIT);

        // 判断是否有结转单，增加可结转金额，
        List<FinanceSettleOrderItemPo> settleOrderItemPos = settleOrderItemDao.findBySettleOrderNo(
                financeSettleOrderNo);
        List<FinanceSettleOrderItemPo> carryoverOrders = settleOrderItemPos.stream()
                .filter(settleOrderItemPo -> Objects.equals(FinanceSettleOrderItemType.CARRYOVER_ORDER,
                        settleOrderItemPo.getFinanceSettleOrderItemType()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(carryoverOrders)) {
            for (FinanceSettleOrderItemPo carryoverOrder : carryoverOrders) {
                String carryoverOrderNo = carryoverOrder.getBusinessNo();
                BigDecimal receiveAmount = carryoverOrder.getReceiveAmount();

                FinanceSettleCarryoverOrderPo updateCarryoverOrderPo = carryoverOrderDao.findByCarryoverOrderNo(
                        carryoverOrderNo);
                updateCarryoverOrderPo.setAvailableCarryoverAmount(updateCarryoverOrderPo.getAvailableCarryoverAmount()
                        .add(receiveAmount));
                if (updateCarryoverOrderPo.getAvailableCarryoverAmount()
                        .compareTo(BigDecimal.ZERO) < 0) {
                    log.error("结算单作废异常，增加结转单{}可结转金额后结果小于0，请相关同事注意！", carryoverOrderNo);
                    throw new BizException("作废失败！增加结转单{} 可结转金额异常。", carryoverOrderNo);
                }
                if (updateCarryoverOrderPo.getAvailableCarryoverAmount()
                        .compareTo(updateCarryoverOrderPo.getCarryoverAmount()) > 0) {
                    log.error("结算单作废异常，增加结转单{}可结转金额后大于初始结转金额，请相关同事注意！",
                            carryoverOrderNo);
                    throw new BizException("作废失败！增加结转单{} 可结转金额异常。", carryoverOrderNo);
                }

                carryoverOrderDao.updateByIdVersion(updateCarryoverOrderPo);
            }
        }

        // 变更结算单状态
        settleOrderPo.setFinanceSettleOrderStatus(FinanceSettleOrderStatus.INVALIDATE);
        settleOrderDao.updateByIdVersion(settleOrderPo);

        // 记录操作记录
        logBaseService.simpleLog(
                LogBizModule.FINANCE_SETTLE_ORDER_STATUS, ScmConstant.SETTLE_ORDER_LOG_VERSION,
                financeSettleOrderNo, settleOrderPo.getFinanceSettleOrderStatus().getRemark(),
                Collections.singletonList(new LogVersionBo("操作", null, "作废结算单"))
        );
    }

    /**
     * @Description spm待办列表
     * @author yanjiawei
     * @Date 2024/5/26 10:52
     */
    public CommonPageResult.PageInfo<SettleOrderPageVo> getSpmPendingSettleOrders(SearchSettleOrderDto searchDto) {
        List<FinanceSettleOrderStatus> queryStatus = List.of(
                FinanceSettleOrderStatus.WAIT_SUPPLIER_SUBMIT);
        searchDto.setSettleOrderStatusList(queryStatus);

        return searchSpmSupplierOrders(searchDto);
    }

    public CommonPageResult.PageInfo<SettleOrderPageVo> searchSpmSupplierOrders(SearchSettleOrderDto searchDto) {
        searchDto.setAuthSupplierCodes(authBaseService.getSupplierCodeList());

        if (StrUtil.isNotBlank(searchDto.getRecoOrderNo())) {
            List<String> settleOrderNosParam = settleOrderItemDao.findSettleOrderNosByRecoOrderNo(
                    searchDto.getRecoOrderNo());
            if (CollectionUtils.isNotEmpty(settleOrderNosParam)) {
                searchDto.setSettleOrderNos(settleOrderNosParam);
            } else {
                return new CommonPageResult.PageInfo<>();
            }
        }

        Page<FinanceSettleOrderPo> page = settleOrderDao.findPageOptAuthSupCode(searchDto);
        List<FinanceSettleOrderPo> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new CommonPageResult.PageInfo<>();
        }

        List<String> settleOrderNos = records.stream()
                .map(FinanceSettleOrderPo::getFinanceSettleOrderNo)
                .collect(Collectors.toList());
        List<FinancePaymentItemPo> paymentItemPos = paymentItemDao.findByPayAmountBySettleOrderNos(settleOrderNos);
        Map<String, BigDecimal> settlerOrderAmountMap = paymentItemPos.stream()
                .collect(Collectors.groupingBy(FinancePaymentItemPo::getPaymentBizNo,
                        Collectors.mapping(FinancePaymentItemPo::getRmbPaymentMoney,
                                Collectors.reducing(BigDecimal.ZERO,
                                        BigDecimal::add))));

        List<SettleOrderPageVo> settleOrderPageVos = FinanceSettleOrderBuilder.buildSettleOrderPageVos(records,
                settlerOrderAmountMap);

        return PageInfoUtil.getPageInfo(page, settleOrderPageVos);
    }

    public CommonPageResult.PageInfo<SettleOrderPageVo> getScmPendingSettleOrders(SearchSettleOrderDto searchDto) {
        List<FinanceSettleOrderStatus> queryStatus = Arrays.asList(FinanceSettleOrderStatus.WAIT_SUPPLIER_SUBMIT,
                FinanceSettleOrderStatus.WAIT_FOLLOWUP_CONFIRM,
                FinanceSettleOrderStatus.IN_APPROVAL,
                FinanceSettleOrderStatus.WAIT_SETTLEMENT,
                FinanceSettleOrderStatus.PARTIAL_SETTLEMENT,
                FinanceSettleOrderStatus.FEISHU_APPROVAL_PROCESSING);
        searchDto.setSettleOrderStatusList(queryStatus);
        searchDto.setExistCarryoverOrder(false);

        if (StrUtil.isNotBlank(searchDto.getRecoOrderNo())) {
            List<String> settleOrderNosParam = settleOrderItemDao.findSettleOrderNosByRecoOrderNo(searchDto.getRecoOrderNo());
            if (CollectionUtils.isNotEmpty(settleOrderNosParam)) {
                searchDto.setSettleOrderNos(settleOrderNosParam);
            } else {
                return new CommonPageResult.PageInfo<>();
            }
        }

        String curUserKey = ParamValidUtils.requireNotBlank(GlobalContext.getUserKey(), "用户信息不存在，请先登录");
        searchDto.setCtrlUser(curUserKey);
        Page<FinanceSettleOrderPo> page = settleOrderDao.findPageOptAuthSupCode(searchDto);

        List<FinanceSettleOrderPo> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new CommonPageResult.PageInfo<>();
        }

        List<String> settleOrderNos = records.stream()
                .map(FinanceSettleOrderPo::getFinanceSettleOrderNo)
                .collect(Collectors.toList());
        List<FinancePaymentItemPo> paymentItemPos = paymentItemDao.findByPayAmountBySettleOrderNos(settleOrderNos);
        Map<String, BigDecimal> paymentBizNoToSum = paymentItemPos.stream()
                .collect(Collectors.groupingBy(FinancePaymentItemPo::getPaymentBizNo, Collectors.mapping(FinancePaymentItemPo::getRmbPaymentMoney,
                        Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
        List<SettleOrderPageVo> settleOrderPageVos = FinanceSettleOrderBuilder.buildSettleOrderPageVos(records, paymentBizNoToSum);
        return PageInfoUtil.getPageInfo(page, settleOrderPageVos);
    }

    public CommonPageResult.PageInfo<SettleOrderPageVo> searchScmSupplierOrders(SearchSettleOrderDto searchDto) {
        String curUserKey = ParamValidUtils.requireNotBlank(GlobalContext.getUserKey(), "用户信息不存在，请先登录");

        if (StrUtil.isNotBlank(searchDto.getRecoOrderNo())) {
            List<String> settleOrderNosParam = settleOrderItemDao.findSettleOrderNosByRecoOrderNo(
                    searchDto.getRecoOrderNo());
            if (CollectionUtils.isNotEmpty(settleOrderNosParam)) {
                searchDto.setSettleOrderNos(settleOrderNosParam);
            } else {
                return new CommonPageResult.PageInfo<>();
            }
        }

        Page<FinanceSettleOrderPo> page;
        if (scmFinanceProp.getWhitelist().contains(curUserKey)) {
            page = settleOrderDao.findPageOptAuthSupCode(searchDto);
        } else {
            List<String> authSupplierCodes = supplierDao.findSupplierCodesByFollower(curUserKey);
            if (CollectionUtils.isNotEmpty(authSupplierCodes)) {
                searchDto.setAuthSupplierCodes(authSupplierCodes);
            }
            searchDto.setCtrlUser(curUserKey);
            page = settleOrderDao.findPageWithAuthSupCode(searchDto);
        }


        List<FinanceSettleOrderPo> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new CommonPageResult.PageInfo<>();
        }

        List<String> settleOrderNos = records.stream()
                .map(FinanceSettleOrderPo::getFinanceSettleOrderNo)
                .collect(Collectors.toList());
        List<FinancePaymentItemPo> paymentItemPos = paymentItemDao.findByPayAmountBySettleOrderNos(settleOrderNos);
        Map<String, BigDecimal> paymentBizNoToSum = paymentItemPos.stream()
                .collect(Collectors.groupingBy(FinancePaymentItemPo::getPaymentBizNo,
                        Collectors.mapping(FinancePaymentItemPo::getRmbPaymentMoney,
                                Collectors.reducing(BigDecimal.ZERO,
                                        BigDecimal::add))));

        List<SettleOrderPageVo> settleOrderPageVos = FinanceSettleOrderBuilder.buildSettleOrderPageVos(records,
                paymentBizNoToSum);

        return PageInfoUtil.getPageInfo(page, settleOrderPageVos);
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportScmSettleOrders(SearchSettleOrderDto exportSettleOrderDto) {
        List<SupplierVo> scmSupplierList = ParamValidUtils.requireNotEmpty(getScmSupplierList(exportSettleOrderDto),
                "导出数据为空！");
        List<String> supplierCodes = scmSupplierList.stream()
                .map(SupplierVo::getSupplierCode)
                .collect(Collectors.toList());
        exportSettleOrderDto.setSupplierCodes(supplierCodes);
        consistencySendMqService.execSendMq(ScmExportHandler.class,
                new FileOperateMessageDto<>(GlobalContext.getUserKey(),
                        GlobalContext.getUsername(),
                        FileOperateBizType.SCM_FINANCE_SETTLE_ORDER_EXPORT.getCode(),
                        exportSettleOrderDto));
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportSpmSettleOrders(SearchSettleOrderDto exportSettleOrderDto) {
        List<SupplierVo> scmSupplierList = ParamValidUtils.requireNotEmpty(getSpmSupplierList(exportSettleOrderDto),
                "导出数据为空！");
        List<String> supplierCodes = scmSupplierList.stream()
                .map(SupplierVo::getSupplierCode)
                .collect(Collectors.toList());
        exportSettleOrderDto.setSupplierCodes(supplierCodes);
        consistencySendMqService.execSendMq(ScmExportHandler.class,
                new FileOperateMessageDto<>(GlobalContext.getUserKey(),
                        GlobalContext.getUsername(),
                        FileOperateBizType.SPM_FINANCE_SETTLE_ORDER_EXPORT.getCode(),
                        exportSettleOrderDto));
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportScmSettleOrderDetails(SearchSettleOrderDto dto) {
        List<String> settleOrderNos = getScmSettleOrderNos(dto);
        if (CollectionUtils.isEmpty(settleOrderNos)) {
            throw new ParamIllegalException("导出结算单明细数据为空！请刷新后重试");
        }

        dto.setSettleOrderNos(settleOrderNos);
        consistencySendMqService.execSendMq(ScmExportHandler.class,
                new FileOperateMessageDto<>(GlobalContext.getUserKey(),
                        GlobalContext.getUsername(),
                        FileOperateBizType.SCM_FINANCE_SETTLE_ORDER_ITEM_EXPORT.getCode(),
                        dto));
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportSpmSettleOrderDetails(SearchSettleOrderDto dto) {
        List<String> settleOrderNos = getSpmSettleOrderNos(dto);
        if (CollectionUtils.isEmpty(settleOrderNos)) {
            throw new ParamIllegalException("导出结算单明细数据为空！请刷新后重试");
        }

        dto.setSettleOrderNos(settleOrderNos);
        consistencySendMqService.execSendMq(ScmExportHandler.class,
                new FileOperateMessageDto<>(GlobalContext.getUserKey(),
                        GlobalContext.getUsername(),
                        FileOperateBizType.SPM_FINANCE_SETTLE_ORDER_ITEM_EXPORT.getCode(),
                        dto));
    }

    public SettleOrderDetailVo getSettleOrderDetail(GetSettleOrderDetailDto requestDto) {
        final String settleOrderNo = requestDto.getSettleOrderNo();

        // 查询结算单
        FinanceSettleOrderPo settleOrderPo = settleOrderDao.findByOrderNo(settleOrderNo);
        if (Objects.isNull(settleOrderPo)) {
            return null;
        }

        // 查询结算明细
        List<FinanceSettleOrderItemPo> settleOrderItemPos = settleOrderItemDao.findBySettleOrderNo(settleOrderNo);

        // 查询结转单
        Set<String> carryoverNos = settleOrderItemPos.stream()
                .filter(settleItem -> Objects.equals(FinanceSettleOrderItemType.CARRYOVER_ORDER,
                        settleItem.getFinanceSettleOrderItemType()))
                .map(FinanceSettleOrderItemPo::getBusinessNo)
                .collect(Collectors.toSet());
        List<FinanceSettleCarryoverOrderPo> carryoverOrderPos = carryoverOrderDao.findByCarryoverOrderNos(carryoverNos);
        FinanceSettleCarryoverOrderPo carryoverOrderPo = carryoverOrderDao.findBySettleOrderNo(settleOrderNo);

        // 查询收款信息
        List<FinanceSettleOrderReceivePo> settleOrderReceivePos = settleOrderReceiveDao.findByFinanceSettleOrderNo(
                settleOrderNo);

        // 查询支付记录
        Map<Long, List<String>> paymentImageMap = new HashMap<>(16);
        List<FinancePaymentItemPo> paymentItemPos = paymentItemDao.findBySettleOrderNo(settleOrderNo);
        if (CollectionUtils.isNotEmpty(paymentItemPos)) {
            // 支付凭证文件信息
            List<Long> paymentItemIds = paymentItemPos.stream()
                    .map(FinancePaymentItemPo::getFinancePaymentItemId)
                    .collect(Collectors.toList());
            List<ScmImagePo> paymentImages = scmImageDao.getListByIdAndType(ImageBizType.PAYMENT_FILE,
                    paymentItemIds);
            paymentImageMap = paymentImages.stream()
                    .collect(Collectors.groupingBy(ScmImagePo::getImageBizId,
                            Collectors.mapping(ScmImagePo::getFileCode, Collectors.toList())));
        }

        // 查询供应商信息
        String supplierCode = settleOrderPo.getSupplierCode();
        SupplierPo supplierPo = supplierDao.getBySupplierCode(supplierCode);

        return FinanceSettleOrderBuilder.buildSettleOrderDetailVo(settleOrderPo, settleOrderItemPos, carryoverOrderPo,
                carryoverOrderPos, settleOrderReceivePos,
                paymentItemPos, paymentImageMap, supplierPo);
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.SETTLE_ORDER_ACCOUNT, key = "#addSettleOrderAccountDto.settleOrderNo",
            waitTime = 1, leaseTime = -1, exceptionDesc = "结算单收款信息正在处理中，请稍后再试。")
    public void addSettleOrderAccount(AddSettleOrderAccountDto addSettleOrderAccountDto) {
        final String financeSettleOrderNo = addSettleOrderAccountDto.getSettleOrderNo();
        final Integer version = addSettleOrderAccountDto.getVersion();

        // 数据校验：通过结算单ID列表查询结算单信息列表
        FinanceSettleOrderPo settleOrderPo = ParamValidUtils.requireNotNull(
                settleOrderDao.findByOrderNoAndVersion(financeSettleOrderNo, version),
                "结算单不存在或数据发生变更，请刷新页面后重试。");

        // 状态校验：非已作废/已结算
        validateOrderStatus(settleOrderPo, List.of(FinanceSettleOrderStatus.INVALIDATE, FinanceSettleOrderStatus.SETTLE_COMPLETED));

        // 保存结算单收款信息列表
        final String account = addSettleOrderAccountDto.getAccount();
        SupplierPaymentAccountPo supplierPaymentAccountPo = ParamValidUtils.requireNotNull(
                supplierPaymentAccountBaseService.getByAccount(account),
                StrUtil.format("供应商收款账户:{}不存在，请刷新后重试！", account));
        FinanceSettleOrderReceivePo settleOrderReceivePo
                = FinanceSettleOrderBuilder.buildSettleOrderReceivePo(settleOrderPo,
                addSettleOrderAccountDto,
                supplierPaymentAccountPo);
        settleOrderReceiveDao.insert(settleOrderReceivePo);

        validateReceiveInfo(settleOrderPo);

        logBaseService.simpleLog(
                LogBizModule.FINANCE_SETTLE_ORDER_STATUS, ScmConstant.SETTLE_ORDER_LOG_VERSION,
                settleOrderPo.getFinanceSettleOrderNo(), settleOrderPo.getFinanceSettleOrderStatus().getRemark(),
                Collections.singletonList(new LogVersionBo("操作", null, "新增收款账户"))
        );
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.SETTLE_ORDER_ACCOUNT, key = "#updateSettleOrderAccountDto" + ".settleOrderNo"
            , waitTime = 1, leaseTime = -1, exceptionDesc = "结算单收款信息正在处理中，请稍后再试。")
    public void updateSettleOrderAccount(UpdateSettleOrderAccountDto updateSettleOrderAccountDto) {
        final String financeSettleOrderNo = updateSettleOrderAccountDto.getSettleOrderNo();
        final Integer version = updateSettleOrderAccountDto.getVersion();

        // 数据校验：通过结算单ID列表查询结算单信息列表
        FinanceSettleOrderPo settleOrderPo = ParamValidUtils.requireNotNull(
                settleOrderDao.findByOrderNoAndVersion(financeSettleOrderNo, version),
                "结算单已不存在，请刷新页面后重试。");

        // 状态校验：非已作废/已结算
        validateOrderStatus(settleOrderPo,
                List.of(FinanceSettleOrderStatus.INVALIDATE, FinanceSettleOrderStatus.SETTLE_COMPLETED));

        // 更新结算单收款信息列表
        final String account = updateSettleOrderAccountDto.getAccount();
        SupplierPaymentAccountPo supplierPaymentAccountPo = ParamValidUtils.requireNotNull(
                supplierPaymentAccountBaseService.getByAccount(account),
                StrUtil.format("供应商收款账户:{}不存在，请刷新后重试！", account));
        FinanceSettleOrderReceivePo settleOrderReceivePo
                = FinanceSettleOrderBuilder.buildSettleOrderReceivePo(settleOrderPo, updateSettleOrderAccountDto,
                supplierPaymentAccountPo);
        settleOrderReceiveDao.updateByIdVersion(settleOrderReceivePo);

        validateReceiveInfo(settleOrderPo);
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.SETTLE_ORDER_PAYMENT_RECORD, key = "#addPaymentRecordDto" + ".settleOrderNo"
            , waitTime = 1, leaseTime = -1, exceptionDesc = "结算单付款信息正在处理中，请稍后再试。")
    public void addPaymentRecord(AddPaymentRecordDto addPaymentRecordDto) {
        final String financeSettleOrderNo = addPaymentRecordDto.getSettleOrderNo();
        final Integer version = addPaymentRecordDto.getVersion();

        // 数据校验：通过结算单ID列表查询结算单信息列表
        FinanceSettleOrderPo settleOrderPo = ParamValidUtils.requireNotNull(
                settleOrderDao.findByOrderNoAndVersion(financeSettleOrderNo, version),
                "数据已被修改或删除，请刷新页面后重试！");
        List<FinanceSettleOrderItemPo> settleOrderItemPos = ParamValidUtils.requireNotEmpty(
                settleOrderItemDao.findBySettleOrderNo(financeSettleOrderNo),
                "结算明细数据已被修改或删除，请刷新页面后重试！");

        // 状态校验：非已作废/已结算
        validateOrderStatus(settleOrderPo, List.of(FinanceSettleOrderStatus.INVALIDATE, FinanceSettleOrderStatus.SETTLE_COMPLETED));

        // 登录信息校验 & 处理人校验
        validateCtrlUser(settleOrderPo);

        // 付款信息校验：付款金额，银行账号，币种
        BigDecimal totalSettleAmount = settleOrderPo.getSettleAmount();
        ParamValidUtils.requireEquals(true, totalSettleAmount.compareTo(BigDecimal.ZERO) > 0,
                "当前结算单没有结算金额，请先确认结算金额再提交付款记录。");

        final String account = addPaymentRecordDto.getAccount();
        SupplierPaymentAccountPo supplierPaymentAccountPo = ParamValidUtils.requireNotNull(
                supplierPaymentAccountBaseService.getByAccount(account),
                StrUtil.format("供应商收款账户:{}不存在，请刷新后重试！", account));

        FinancePaymentItemPo financePaymentItemPo
                = FinanceSettleOrderBuilder.buildFinancePaymentItemPo(financeSettleOrderNo, addPaymentRecordDto, supplierPaymentAccountPo);
        paymentItemDao.insert(financePaymentItemPo);

        // 付款文件
        List<String> fileCodeList = addPaymentRecordDto.getFileCodeList();
        scmImageBaseService.insertBatchImage(fileCodeList, ImageBizType.PAYMENT_FILE, financePaymentItemPo.getFinancePaymentItemId());

        // 付款总金额校验：小于等于总结算金额
        List<FinancePaymentItemPo> paymentItemAgain = paymentItemDao.findBySettleOrderNo(financeSettleOrderNo);
        BigDecimal payedAmount = paymentItemAgain.stream()
                .map(FinancePaymentItemPo::getRmbPaymentMoney)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        ParamValidUtils.requireEquals(true, payedAmount.compareTo(totalSettleAmount) <= 0,
                "付款金额超出结算金额，请确认后重试");

        // 变更结算单状态
        if (payedAmount.compareTo(totalSettleAmount) == 0) {
            settleOrderPo.setFinanceSettleOrderStatus(FinanceSettleOrderStatus.SETTLE_COMPLETED);
            settleOrderPo.setSettleFinishTime(LocalDateTime.now());
        } else {
            settleOrderPo.setFinanceSettleOrderStatus(FinanceSettleOrderStatus.PARTIAL_SETTLEMENT);
        }
        settleOrderDao.updateByIdVersion(settleOrderPo);

        // 记录变更日志
        logBaseService.simpleLog(
                LogBizModule.FINANCE_SETTLE_ORDER_STATUS, ScmConstant.SETTLE_ORDER_LOG_VERSION,
                settleOrderPo.getFinanceSettleOrderNo(), settleOrderPo.getFinanceSettleOrderStatus().getRemark(),
                Collections.singletonList(new LogVersionBo("操作", null, "新增付款记录"))
        );

        // 状态=结算完成，如果存在结转单：刷新结算单状态
        if (Objects.equals(FinanceSettleOrderStatus.SETTLE_COMPLETED, settleOrderPo.getFinanceSettleOrderStatus())) {
            List<FinanceSettleOrderItemPo> carryoverOrders = settleOrderItemPos.stream()
                    .filter(settleOrderItemPo -> Objects.equals(FinanceSettleOrderItemType.CARRYOVER_ORDER,
                            settleOrderItemPo.getFinanceSettleOrderItemType()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(carryoverOrders)) {
                return;
            }
            for (FinanceSettleOrderItemPo carryoverOrder : carryoverOrders) {
                refreshCarryoverOrderStatus(carryoverOrder.getBusinessNo());
            }
        }
    }

    @RedisLock(prefix = ScmRedisConstant.SETTLE_CARRYOVER_ORDER_UPDATE_STATUS, key = "#carryoverOrderNo", waitTime =
            1, leaseTime = -1, exceptionDesc = "结转单信息正在处理中，请稍后再试。")
    public void refreshCarryoverOrderStatus(String carryoverOrderNo) {
        // 通过结转单号查询结转单信息
        FinanceSettleCarryoverOrderPo carryoverOrder
                = ParamValidUtils.requireNotNull(carryoverOrderDao.findByCarryoverOrderNo(carryoverOrderNo),
                "结转单信息处理异常！结转单信息不存在，请联系业务人员处理");

        if (carryoverOrder.getAvailableCarryoverAmount()
                .compareTo(BigDecimal.ZERO) != 0) {
            log.info("当前结转单可结转金额!=0 结转单号:{}", carryoverOrderNo);
            return;
        }

        List<FinanceSettleOrderItemPo> settleOrderItemPos
                = settleOrderItemDao.findByCarryoverOrderNo(carryoverOrderNo);
        if (CollectionUtils.isEmpty(settleOrderItemPos)) {
            log.info("当前结转单关联结算明细不存在 结转单号:{}", carryoverOrderNo);
            return;
        }

        Set<String> settleOrderNos = settleOrderItemPos.stream()
                .map(FinanceSettleOrderItemPo::getFinanceSettleOrderNo)
                .collect(Collectors.toSet());
        List<FinanceSettleOrderPo> settleOrderPos = settleOrderDao.findByOrderNos(settleOrderNos);
        if (CollectionUtils.isEmpty(settleOrderPos)) {
            log.info("当前结转单关联结算单不存在 结转单号:{}", carryoverOrderNo);
            return;
        }

        List<FinanceSettleOrderStatus> completeStatus = Arrays.asList(
                FinanceSettleOrderStatus.SETTLE_COMPLETED, FinanceSettleOrderStatus.INVALIDATE);

        boolean allSettleComplete = settleOrderPos.stream()
                .allMatch(settleOrderPo -> completeStatus.contains(settleOrderPo.getFinanceSettleOrderStatus()));
        if (allSettleComplete) {
            carryoverOrder.setFinanceSettleCarryoverOrderStatus(FinanceSettleCarryoverOrderStatus.CARRIED_OVER);
            carryoverOrderDao.updateByIdVersion(carryoverOrder);
            log.info("更新结转单到结转完成 结转单号:{}", carryoverOrderNo);

            String settleOrderNo = carryoverOrder.getFinanceSettleOrderNo();
            FinanceSettleOrderPo settleOrderPo = settleOrderDao.findByOrderNo(settleOrderNo);
            settleOrderPo.setFinanceSettleOrderStatus(FinanceSettleOrderStatus.SETTLE_COMPLETED);
            settleOrderPo.setSettleFinishTime(LocalDateTime.now());
            settleOrderDao.updateByIdVersion(settleOrderPo);

            logBaseService.simpleLog(LogBizModule.FINANCE_SETTLE_ORDER_STATUS, ScmConstant.SETTLE_ORDER_LOG_VERSION,
                    settleOrderPo.getFinanceSettleOrderNo(),
                    settleOrderPo.getFinanceSettleOrderStatus()
                            .getRemark(), Collections.emptyList());
        }
    }

    public Integer getExportSettleItemTotalCount(SearchSettleOrderDto searchDto) {
        if (CollectionUtils.isEmpty(searchDto.getSettleOrderNos())) {
            return 0;
        }
        return settleOrderItemDao.findExportSettleItemTotalCount(searchDto);
    }

    public CommonResult<ExportationListResultBo<FinanceSettleOrderItemExportVo>> getExportSettleItemList(SearchSettleOrderDto dto) {
        ExportationListResultBo<FinanceSettleOrderItemExportVo> resultBo = new ExportationListResultBo<>();

        IPage<FinanceSettleOrderItemPo> settleOrderItems = settleOrderItemDao.findSettleOrderItems(dto);
        List<FinanceSettleOrderItemPo> settleOrderItemPos = settleOrderItems.getRecords();
        if (CollectionUtils.isEmpty(settleOrderItemPos)) {
            return CommonResult.success(resultBo);
        }

        List<FinanceSettleOrderPo> settleOrderPos = settleOrderDao.findByOrderNos(dto.getSettleOrderNos());

        List<FinanceSettleOrderItemExportVo> financeSettleOrderItemExportVos
                = FinanceSettleOrderBuilder.buildFinanceSettleOrderExportItemVos(settleOrderPos, settleOrderItemPos);
        resultBo.setRowDataList(financeSettleOrderItemExportVos);
        return CommonResult.success(resultBo);
    }

    /**
     * 获取 SPM 供应商列表。
     *
     * @param searchDto 包含查询条件的 DTO 对象
     * @return 供应商 VO 列表
     */
    public List<SupplierVo> getSpmSupplierList(SearchSettleOrderDto searchDto) {
        String curUserKey = ParamValidUtils.requireNotBlank(GlobalContext.getUserKey(), "用户信息不存在，请先登录");
        searchDto.setCtrlUser(curUserKey);
        searchDto.setAuthSupplierCodes(authBaseService.getSupplierCodeList());

        if (StrUtil.isNotBlank(searchDto.getRecoOrderNo())) {
            List<String> settleOrderNosParam = settleOrderItemDao.findSettleOrderNosByRecoOrderNo(
                    searchDto.getRecoOrderNo());
            if (CollectionUtils.isNotEmpty(settleOrderNosParam)) {
                searchDto.setSettleOrderNos(settleOrderNosParam);
            } else {
                return Collections.emptyList();
            }
        }

        List<String> supplierCodes = settleOrderDao.findSupCodesWithAuthSupCode(searchDto);
        List<SupplierPo> supplierCodesAndNames = supplierDao.getBySupplierCodeList(supplierCodes);
        return FinanceSettleOrderBuilder.buildSupplierVo(supplierCodes, supplierCodesAndNames);
    }

    public List<String> getSpmSettleOrderNos(SearchSettleOrderDto searchDto) {
        if (StrUtil.isNotBlank(searchDto.getRecoOrderNo())) {
            List<String> settleOrderNosParam = settleOrderItemDao.findSettleOrderNosByRecoOrderNo(
                    searchDto.getRecoOrderNo());
            if (CollectionUtils.isNotEmpty(settleOrderNosParam)) {
                searchDto.setSettleOrderNos(settleOrderNosParam);
            } else {
                return Collections.emptyList();
            }
        }

        searchDto.setAuthSupplierCodes(authBaseService.getSupplierCodeList());
        return settleOrderDao.findSoCodesOptAuthSupCode(searchDto);
    }

    /**
     * 获取 SCM 供应商列表。
     *
     * @param searchDto 包含查询条件的 DTO 对象
     * @return 供应商 VO 列表
     */
    public List<SupplierVo> getScmSupplierList(SearchSettleOrderDto searchDto) {
        String curUserKey = ParamValidUtils.requireNotBlank(GlobalContext.getUserKey(), "用户信息不存在，请先登录");

        if (StrUtil.isNotBlank(searchDto.getRecoOrderNo())) {
            List<String> settleOrderNosParam = settleOrderItemDao.findSettleOrderNosByRecoOrderNo(
                    searchDto.getRecoOrderNo());
            if (CollectionUtils.isNotEmpty(settleOrderNosParam)) {
                searchDto.setSettleOrderNos(settleOrderNosParam);
            } else {
                return Collections.emptyList();
            }
        }

        List<String> supplierCodes;
        if (scmFinanceProp.getWhitelist()
                .contains(curUserKey)) {
            supplierCodes = settleOrderDao.findSupCodesOptAuthSupCode(searchDto);
        } else {
            List<String> authSupplierCodes = supplierDao.findSupplierCodesByFollower(curUserKey);
            if (CollectionUtils.isNotEmpty(authSupplierCodes)) {
                searchDto.setAuthSupplierCodes(authSupplierCodes);
            }
            searchDto.setCtrlUser(curUserKey);
            supplierCodes = settleOrderDao.findSupCodesWithAuthSupCode(searchDto);
        }

        List<SupplierPo> supplierPos = supplierDao.getBySupplierCodeList(supplierCodes);
        return FinanceSettleOrderBuilder.buildSupplierVo(supplierCodes, supplierPos);
    }

    private List<String> getScmSettleOrderNos(SearchSettleOrderDto searchDto) {
        String curUserKey = ParamValidUtils.requireNotBlank(GlobalContext.getUserKey(), "用户信息不存在，请先登录");

        if (StrUtil.isNotBlank(searchDto.getRecoOrderNo())) {
            List<String> settleOrderNosParam = settleOrderItemDao.findSettleOrderNosByRecoOrderNo(
                    searchDto.getRecoOrderNo());
            if (CollectionUtils.isNotEmpty(settleOrderNosParam)) {
                searchDto.setSettleOrderNos(settleOrderNosParam);
            } else {
                return Collections.emptyList();
            }
        }

        List<String> settleOrderNos;
        if (scmFinanceProp.getWhitelist()
                .contains(curUserKey)) {
            settleOrderNos = settleOrderDao.findSoCodesOptAuthSupCode(searchDto);
        } else {
            List<String> authSupplierCodes = supplierDao.findSupplierCodesByFollower(curUserKey);
            if (CollectionUtils.isNotEmpty(authSupplierCodes)) {
                searchDto.setAuthSupplierCodes(authSupplierCodes);
            }
            searchDto.setCtrlUser(curUserKey);
            settleOrderNos = settleOrderDao.findSoCodesWithAuthSupCode(searchDto);
        }
        return settleOrderNos;
    }

    private void validateOrderStatus(FinanceSettleOrderPo settleOrderPo,
                                     FinanceSettleOrderStatus expectedStatus) {
        final FinanceSettleOrderStatus curStatus = settleOrderPo.getFinanceSettleOrderStatus();
        ParamValidUtils.requireEquals(expectedStatus, curStatus,
                StrUtil.format("结算单状态非{}，无法进行此操作", expectedStatus.getRemark()));
    }

    private void validateCtrlUser(FinanceSettleOrderPo settleOrderPo) {
        String curUserKey = ParamValidUtils.requireNotBlank(GlobalContext.getUserKey(), "用户信息不存在，请先登录");
        String ctrlUser = settleOrderPo.getCtrlUser();
        ParamValidUtils.requireEquals(curUserKey, ctrlUser,
                StrUtil.format("结算单{}处理人不是当前登录账号，无法进行此操作",
                        settleOrderPo.getFinanceSettleOrderNo()));
    }

    private void validateReceiveInfo(FinanceSettleOrderPo settleOrderPo) {
        BigDecimal totalSettleAmount = settleOrderPo.getSettleAmount();
        if (totalSettleAmount.compareTo(BigDecimal.ZERO) > 0) {
            List<FinanceSettleOrderReceivePo> settleOrderReceivePos = ParamValidUtils.requireNotEmpty(
                    settleOrderReceiveDao.findByFinanceSettleOrderNo(settleOrderPo.getFinanceSettleOrderNo()),
                    "结算金额大于0，但未填写收款信息，请先填写收款信息再提交");
            BigDecimal totalExpectReceiveAmount = settleOrderReceivePos.stream()
                    .map(FinanceSettleOrderReceivePo::getExpectReceiveAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            ParamValidUtils.requireEquals(true, totalExpectReceiveAmount.compareTo(totalSettleAmount) <= 0,
                    "收款总金额大于结算总金额，请确认后重试");
        } else {
            ParamValidUtils.requireEmpty(
                    settleOrderReceiveDao.findByFinanceSettleOrderNo(settleOrderPo.getFinanceSettleOrderNo()),
                    "结算金额小于等于0，无需添加收款账户信息");
        }
    }

    private void validateOrderStatus(FinanceSettleOrderPo settleOrderPo,
                                     List<FinanceSettleOrderStatus> invalidStatuses) {
        ParamValidUtils.requireEquals(false, invalidStatuses.contains(settleOrderPo.getFinanceSettleOrderStatus()),
                StrUtil.format("结算单状态{}，无法进行此操作", invalidStatuses.stream()
                        .map(FinanceSettleOrderStatus::getRemark)
                        .collect(Collectors.joining("/"))));
    }


    public SettlementRiskMsgVo settlementRiskMsg(GetSettlementRiskMsgDto dto) {
        final String settleOrderNo = dto.getSettleOrderNo();
        FinanceSettleOrderPo settleOrderPo = ParamValidUtils.requireNotNull(settleOrderDao.findByOrderNo(settleOrderNo),
                "结算单已不存在，请刷新页面后重试。");

        SettlementRiskMsgVo riskMsgVo = new SettlementRiskMsgVo();
        final String supplierCode = settleOrderPo.getSupplierCode();
        if (StrUtil.isBlank(supplierCode)) {
            return riskMsgVo;
        }

        // 供应商上月入库金额
        BigDecimal supplierWarehousingMoney = purchaseBaseService.purchaseLastMonWarehousingMoney(supplierCode);
        riskMsgVo.setSupplierWarehousingMoney(supplierWarehousingMoney);

        // 供应商在途生产金额
        BigDecimal supplierInTransitMoney = purchaseBaseService.purchaseLastMonInTransitMoney(supplierCode);
        riskMsgVo.setSupplierInTransitMoney(supplierInTransitMoney);

        // 待抵扣预付款金额
        BigDecimal waitPrePayMoney = prepaymentBaseService.getAllCanDeductionMoney(supplierCode);
        riskMsgVo.setWaitPrePayMoney(waitPrePayMoney);

        // 月结算平均金额
        BigDecimal avgMonthSettleAmount = settleOrderDao.getAvgMonthSettleAmount(supplierCode);
        riskMsgVo.setAvgMonthSettleAmount(avgMonthSettleAmount);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneYearAgo = now.minusYears(1);

        // 跟单近1年审批失败次数
        long lastYearRecoFailApproveCnt = 0L;
        String follower = settleOrderPo.getFollowUser();
        if (StrUtil.isBlank(follower)) {
            riskMsgVo.setLastYearRecoFailApproveCnt(lastYearRecoFailApproveCnt);
        } else {
            lastYearRecoFailApproveCnt = settleOrderDao.getApproveFailTimesByFollowUser(
                    FeishuAuditOrderType.IFBS_SETTLEMENT_APPROVE, follower, oneYearAgo, now);
            riskMsgVo.setLastYearRecoFailApproveCnt(lastYearRecoFailApproveCnt);
        }

        // 供应商近1年审批失败次数
        long lastYearSettleFailApproveCnt = settleOrderDao.getApproveFailTimesBySupplier(
                FeishuAuditOrderType.IFBS_SETTLEMENT_APPROVE, supplierCode, oneYearAgo, now);
        riskMsgVo.setLastYearSettleFailApproveCnt(lastYearSettleFailApproveCnt);

        // 供应商上月入库金额风险
        if (riskMsgVo.getSupplierWarehousingMoney()
                .compareTo(settleOrderPo.getSettleAmount()) > 0) {
            log.info("供应商上月入库金额:{}大于结算总金额:{}", riskMsgVo.getSupplierWarehousingMoney(),
                    settleOrderPo.getSettleAmount());

            riskMsgVo.setIsRisk(BooleanType.TRUE);
            riskMsgVo.setSupplierWarehousingMoneyRisk(BooleanType.TRUE);
        }

        // 供应商在途生产金额风险
        if (riskMsgVo.getSupplierInTransitMoney()
                .compareTo(settleOrderPo.getSettleAmount()) > 0) {
            log.info("供应商在途生产金额:{}大于结算总金额:{}", riskMsgVo.getSupplierInTransitMoney(),
                    settleOrderPo.getSettleAmount());

            riskMsgVo.setIsRisk(BooleanType.TRUE);
            riskMsgVo.setSupplierInTransitMoneyRisk(BooleanType.TRUE);
        }

        // 待抵扣预付款金额风险,供应商上月入库金额风险
        if (waitPrePayMoney.compareTo(settleOrderPo.getSettleAmount()) > 0) {

            riskMsgVo.setIsRisk(BooleanType.TRUE);
            riskMsgVo.setWaitSettleMoneyRisk(BooleanType.TRUE);
        }

        // 近1年对账审批失败次数风险
        if (lastYearRecoFailApproveCnt > 3) {
            log.info("近1年结算审批失败次数:{}，大于3次", riskMsgVo.getLastYearRecoFailApproveCnt());
            riskMsgVo.setIsRisk(BooleanType.TRUE);
            riskMsgVo.setLastYearRecoFailApproveCntRisk(BooleanType.TRUE);
        }

        // 近1年结算审批失败次数风险
        if (lastYearSettleFailApproveCnt > 3) {
            log.info("近1年供应商结算付款审批失败次数:{}，大于3次",
                    riskMsgVo.getLastYearSettleFailApproveCnt());
            riskMsgVo.setIsRisk(BooleanType.TRUE);
            riskMsgVo.setLastYearSettleFailApproveCntRisk(BooleanType.TRUE);
        }

        // 月均结算金额风险
        if (settleOrderPo.getSettleAmount()
                .compareTo(avgMonthSettleAmount.multiply(new BigDecimal("1.50"))) > 0) {
            riskMsgVo.setIsRisk(BooleanType.TRUE);
            riskMsgVo.setAvgMonthSettleAmountRisk(BooleanType.TRUE);
        }

        return riskMsgVo;
    }

    public List<GenerateSettleOrderVo> generateRecoOrderList(GenerateSettleOrderDto dto) {
        String curSupplierCode = dto.getSupplierCode();
        List<FinanceRecoOrderPo> recoOrderPos
                = financeSettleOrderBaseService.getRecoOrdersBySupplierCode(curSupplierCode);
        if (CollectionUtils.isEmpty(recoOrderPos)) {
            return Collections.emptyList();
        }
        return recoOrderPos.stream()
                .map(recoOrderPo -> {
                    GenerateSettleOrderVo vo = new GenerateSettleOrderVo();
                    vo.setRecoOrderNo(recoOrderPo.getFinanceRecoOrderNo());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    public Integer getExportSettleOrderTotalCount(SearchSettleOrderDto dto) {
        return settleOrderDao.getExportSettleOrderTotalCount(dto);
    }

    public CommonResult<ExportationListResultBo<FinanceSettleOrderExportVo>> getExportSettleList(SearchSettleOrderDto searchDto) {
        ExportationListResultBo<FinanceSettleOrderExportVo> resultBo = new ExportationListResultBo<>();
        Page<FinanceSettleOrderPo> pageResult = settleOrderDao.findPageOptAuthSupCode(searchDto);
        List<FinanceSettleOrderPo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }

        List<String> settleOrderNos = records.stream()
                .map(FinanceSettleOrderPo::getFinanceSettleOrderNo)
                .collect(Collectors.toList());
        List<FinancePaymentItemPo> paymentItemPos = paymentItemDao.findByPayAmountBySettleOrderNos(settleOrderNos);
        Map<String, BigDecimal> paymentBizNoToSum = paymentItemPos.stream()
                .collect(Collectors.groupingBy(FinancePaymentItemPo::getPaymentBizNo,
                        Collectors.mapping(FinancePaymentItemPo::getRmbPaymentMoney,
                                Collectors.reducing(BigDecimal.ZERO,
                                        BigDecimal::add))));

        List<FinanceSettleCarryoverOrderPo> carryoverOrderPos = carryoverOrderDao.findBySettleOrderNos(settleOrderNos);

        List<FinanceSettleOrderExportVo> vos = FinanceSettleOrderBuilder.buildFinanceSettleOrderExportVos(records,
                paymentBizNoToSum,
                carryoverOrderPos);
        resultBo.setRowDataList(vos);
        return CommonResult.success(resultBo);
    }


    public void approveWorkFlow(SettleOrderApproveDto dto) {
        final String settleOrderNo = dto.getSettleOrderNo();
        final Integer version = dto.getVersion();

        FinanceSettleOrderPo settleOrderPo = ParamValidUtils.requireNotNull(
                settleOrderDao.findByOrderNoAndVersion(settleOrderNo, version),
                "数据已被修改或删除，请刷新页面后重试！");
        validateOrderStatus(settleOrderPo, FinanceSettleOrderStatus.IN_APPROVAL);
        validateCtrlUser(settleOrderPo);

        settleOrderPo.setFinanceSettleOrderStatus(FinanceSettleOrderStatus.FEISHU_APPROVAL_PROCESSING);
        financeSettleOrderBaseService.updateSettleOrder(settleOrderPo);
        mcRemoteService.approveWorkFlow(dto);
    }

    public void rejectWorkFlow(SettleOrderApproveDto dto) {
        final String settleOrderNo = dto.getSettleOrderNo();
        final Integer version = dto.getVersion();

        FinanceSettleOrderPo settleOrderPo = ParamValidUtils.requireNotNull(
                settleOrderDao.findByOrderNoAndVersion(settleOrderNo, version),
                "数据已被修改或删除，请刷新页面后重试！");
        validateOrderStatus(settleOrderPo, FinanceSettleOrderStatus.IN_APPROVAL);
        validateCtrlUser(settleOrderPo);

        settleOrderPo.setFinanceSettleOrderStatus(FinanceSettleOrderStatus.FEISHU_APPROVAL_PROCESSING);
        financeSettleOrderBaseService.updateSettleOrder(settleOrderPo);
        mcRemoteService.rejectWorkFlow(dto);
    }

    public List<GenerateSettleOrderSupplierVo> generateRecoOrderSupplierCodeList() {
        List<String> supplierCodeList
                = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return Collections.emptyList();
        }
        List<SupplierPo> supplierPos = supplierDao.getBySupplierCodeList(supplierCodeList);
        if (CollectionUtils.isEmpty(supplierPos)) {
            return Collections.emptyList();
        }

        supplierPos.removeIf(supplierPo -> Objects.equals(SupplierStatus.DISABLED, supplierPo.getSupplierStatus()));
        if (CollectionUtils.isEmpty(supplierPos)) {
            return Collections.emptyList();
        }

        return supplierPos.stream()
                .map(supplierPo -> {
                    GenerateSettleOrderSupplierVo vo = new GenerateSettleOrderSupplierVo();
                    vo.setSupplierCode(supplierPo.getSupplierCode());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 处理结算单发起/审批中的逻辑
     *
     * @param businessId       结算单业务ID
     * @param workflowNo       工作流编号
     * @param nextWorkflowTask 下一个审批节点任务信息
     */
    public void handleSettlementInApproval(Long businessId,
                                           String workflowNo,
                                           WorkflowTaskVo nextWorkflowTask) {
        FinanceSettleOrderPo financeSettleOrderPo = settleOrderDao.getById(businessId);
        if (Objects.isNull(financeSettleOrderPo)) {
            log.error("飞书结算单：发起审批/审批中回调异常!结算单信息不存在，请相关同事注意！businessId:{}", businessId);
            return;
        }

        financeSettleOrderPo.setWorkflowNo(workflowNo);
        financeSettleOrderPo.setCtrlUser(nextWorkflowTask.getTaskUserCode());
        financeSettleOrderPo.setCtrlUsername(nextWorkflowTask.getTaskUsername());
        financeSettleOrderPo.setTaskId(nextWorkflowTask.getId());
        financeSettleOrderPo.setFinanceSettleOrderStatus(FinanceSettleOrderStatus.IN_APPROVAL);
        settleOrderDao.updateByIdVersion(financeSettleOrderPo);

        logBaseService.simpleLog(LogBizModule.FINANCE_SETTLE_ORDER_STATUS, ScmConstant.PREPAYMENT_LOG_VERSION,
                financeSettleOrderPo.getFinanceSettleOrderNo(),
                financeSettleOrderPo.getFinanceSettleOrderStatus().getRemark(),
                Collections.singletonList(new LogVersionBo("操作", null, "审批"))
        );
    }

    /**
     * 处理结算单撤回逻辑
     *
     * @param businessId 结算单业务ID
     */
    public void handleSettlementReversal(Long businessId) {
        FinanceSettleOrderPo settleOrderPo = settleOrderDao.getById(businessId);
        if (Objects.isNull(settleOrderPo)) {
            log.error("飞书结算单：发起人撤回回调异常!结算单信息不存在，请相关同事注意！businessId:{}", businessId);
            return;
        }

        if (Objects.equals(FinanceSettleOrderStatus.WAIT_SUPPLIER_SUBMIT,
                settleOrderPo.getFinanceSettleOrderStatus())) {
            log.info("结算单已流转{}，跳过处理结算单撤回逻辑",
                    FinanceSettleOrderStatus.WAIT_SUPPLIER_SUBMIT.getRemark());
            return;
        }

        settleOrderPo.setFinanceSettleOrderStatus(FinanceSettleOrderStatus.WAIT_SUPPLIER_SUBMIT);
        settleOrderPo.setSupplierSubmitTime(DefaultDatabaseTime.DEFAULT_TIME.getDateTime());

        settleOrderPo.setFollowerConfirmUser("");
        settleOrderPo.setFollowerConfirmUsername("");
        settleOrderPo.setFollowerConfirmTime(DefaultDatabaseTime.DEFAULT_TIME.getDateTime());

        settleOrderPo.setApplyTime(DefaultDatabaseTime.DEFAULT_TIME.getDateTime());

        settleOrderPo.setCtrlUser("");
        settleOrderPo.setCtrlUsername("");
        settleOrderDao.updateByIdVersion(settleOrderPo);

        logBaseService.simpleLog(
                LogBizModule.FINANCE_SETTLE_ORDER_STATUS, ScmConstant.SETTLE_ORDER_LOG_VERSION,
                settleOrderPo.getFinanceSettleOrderNo(), settleOrderPo.getFinanceSettleOrderStatus().getRemark(),
                Collections.singletonList(new LogVersionBo("操作", null, "审批-撤回"))
        );
    }

    /**
     * 处理结算单拒绝逻辑
     *
     * @param businessId   结算单业务ID
     * @param workflowTask 最新工作流任务信息
     */
    public void handleSettlementRejection(Long businessId,
                                          WorkflowTaskVo workflowTask) {
        // 根据业务ID获取结算单信息
        FinanceSettleOrderPo settleOrderPo = settleOrderDao.getById(businessId);

        // 如果结算单信息不存在，记录错误日志并返回
        if (Objects.isNull(settleOrderPo)) {
            log.error("飞书结算单完结：拒绝审批回调异常!结算单信息不存在，请相关同事注意！businessId:{}", businessId);
            return;
        }

        if (Objects.equals(FinanceSettleOrderStatus.WAIT_SUPPLIER_SUBMIT,
                settleOrderPo.getFinanceSettleOrderStatus())) {
            log.info("结算单已流转{}，跳过处理结算单拒绝逻辑",
                    FinanceSettleOrderStatus.WAIT_SUPPLIER_SUBMIT.getRemark());
            return;
        }

        // 更新结算单状态为"等待供应商提交"
        settleOrderPo.setFinanceSettleOrderStatus(FinanceSettleOrderStatus.WAIT_SUPPLIER_SUBMIT);
        settleOrderPo.setSupplierSubmitTime(DefaultDatabaseTime.DEFAULT_TIME.getDateTime());

        // 清空跟单确认人信息
        settleOrderPo.setFollowerConfirmUser("");
        settleOrderPo.setFollowerConfirmUsername("");
        settleOrderPo.setFollowerConfirmTime(DefaultDatabaseTime.DEFAULT_TIME.getDateTime());

        // 重置申请时间、工作流编号和任务ID
        settleOrderPo.setApplyTime(DefaultDatabaseTime.DEFAULT_TIME.getDateTime());

        // 清空处理人信息
        settleOrderPo.setCtrlUser("");
        settleOrderPo.setCtrlUsername("");

        // 更新结算单信息
        settleOrderDao.updateByIdVersion(settleOrderPo);

        GlobalContext.setUserKey(workflowTask.getTaskUserCode());
        GlobalContext.setUsername(workflowTask.getTaskUsername());
        logBaseService.simpleLog(LogBizModule.FINANCE_SETTLE_ORDER_STATUS, ScmConstant.PREPAYMENT_LOG_VERSION,
                settleOrderPo.getFinanceSettleOrderNo(),
                settleOrderPo.getFinanceSettleOrderStatus().getRemark(),
                Collections.singletonList(new LogVersionBo("操作", null, "审批-拒绝"))
        );
    }

    /**
     * 处理结算单同意逻辑
     *
     * @param businessId     结算单业务ID
     * @param workflowTaskVo 最新工作流任务信息
     */
    public void handleSettlementAgreement(Long businessId,
                                          WorkflowTaskVo workflowTaskVo) {
        FinanceSettleOrderPo settleOrderPo = settleOrderDao.getById(businessId);
        if (Objects.isNull(settleOrderPo)) {
            log.error("飞书结算单完结：同意审批回调异常!结算单信息不存在，请相关同事注意！businessId:{}", businessId);
            return;
        }

        final BigDecimal settleAmount = settleOrderPo.getSettleAmount();
        if (settleAmount.compareTo(BigDecimal.ZERO) < 0) {
            log.info("飞书结算单完结（同意）：结算金额{}小于0!", settleAmount);

            // 变更结算单信息&记录日志
            if (Objects.equals(FinanceSettleOrderStatus.WAIT_SETTLEMENT, settleOrderPo.getFinanceSettleOrderStatus())) {
                log.info("结算单已流转{}，跳过处理结算单同意逻辑",
                        FinanceSettleOrderStatus.WAIT_SETTLEMENT.getRemark());
                return;
            }
            settleOrderPo.setFinanceSettleOrderStatus(FinanceSettleOrderStatus.WAIT_SETTLEMENT);
            settleOrderPo.setCtrlUser(workflowTaskVo.getTaskUserCode());
            settleOrderPo.setCtrlUsername(workflowTaskVo.getTaskUsername());
            settleOrderPo.setWorkflowFinishTime(LocalDateTime.now());
            settleOrderDao.updateByIdVersion(settleOrderPo);

            GlobalContext.setUserKey(workflowTaskVo.getTaskUserCode());
            GlobalContext.setUsername(workflowTaskVo.getTaskUsername());
            logBaseService.simpleLog(LogBizModule.FINANCE_SETTLE_ORDER_STATUS, ScmConstant.PREPAYMENT_LOG_VERSION,
                    settleOrderPo.getFinanceSettleOrderNo(),
                    settleOrderPo.getFinanceSettleOrderStatus().getRemark(),
                    Collections.singletonList(new LogVersionBo("操作", null, "审批-通过"))
            );

            // 创建结转单
            String supplierCode = settleOrderPo.getSupplierCode();
            SupplierPo supplierPo = BizValidUtils.requireNotNull(supplierBaseService.getSupplierByCode(supplierCode),
                    "结算单审核完成处理失败！创建结转单供应商信息为空");
            String supplierAlias = BizValidUtils.requireNotBlank(supplierPo.getSupplierAlias(),
                    "结算单审核完成处理失败！创建结转单供应商别称为空");

            FinanceSettleCarryoverOrderPo carryoverOrderPo = new FinanceSettleCarryoverOrderPo();
            String carryoverNo = idGenerateService.getConfuseCode(
                    ScmConstant.CARRY_OVER_ORDER_NO_PREFIX + ScmFormatUtil.subStringLastThree(supplierAlias),
                    TimeType.CN_DAY, ConfuseLength.L_4);
            carryoverOrderPo.setFinanceSettleCarryoverOrderNo(carryoverNo);
            carryoverOrderPo.setSupplierCode(supplierCode);
            carryoverOrderPo.setFinanceSettleOrderNo(settleOrderPo.getFinanceSettleOrderNo());
            carryoverOrderPo.setFinanceSettleCarryoverOrderStatus(
                    FinanceSettleCarryoverOrderStatus.PENDING_CARRYOVER);

            carryoverOrderPo.setCarryoverAmount(settleOrderPo.getSettleAmount()
                    .abs());
            carryoverOrderPo.setAvailableCarryoverAmount(settleOrderPo.getSettleAmount()
                    .abs());
            carryoverOrderDao.insert(carryoverOrderPo);

        } else if (settleAmount.compareTo(BigDecimal.ZERO) > 0) {
            log.info("飞书结算单完结（同意）：结算金额{}大于0!", settleAmount);

            if (Objects.equals(FinanceSettleOrderStatus.WAIT_SETTLEMENT, settleOrderPo.getFinanceSettleOrderStatus())) {
                log.info("结算单已流转{}，跳过处理结算单同意逻辑",
                        FinanceSettleOrderStatus.WAIT_SETTLEMENT.getRemark());
                return;
            }

            // 变更结算单信息&记录日志
            settleOrderPo.setFinanceSettleOrderStatus(FinanceSettleOrderStatus.WAIT_SETTLEMENT);
            settleOrderPo.setCtrlUser(workflowTaskVo.getTaskUserCode());
            settleOrderPo.setCtrlUsername(workflowTaskVo.getTaskUsername());
            settleOrderPo.setWorkflowFinishTime(LocalDateTime.now());
            settleOrderDao.updateByIdVersion(settleOrderPo);

            GlobalContext.setUserKey(workflowTaskVo.getTaskUserCode());
            GlobalContext.setUsername(workflowTaskVo.getTaskUsername());
            logBaseService.simpleLog(
                    LogBizModule.FINANCE_SETTLE_ORDER_STATUS, ScmConstant.SETTLE_ORDER_LOG_VERSION,
                    settleOrderPo.getFinanceSettleOrderNo(), settleOrderPo.getFinanceSettleOrderStatus().getRemark(),
                    Collections.singletonList(new LogVersionBo("操作", null, "审批-通过"))
            );
        } else {
            log.info("飞书结算单完结（同意）：结算金额{}等于0!", settleAmount);

            if (Objects.equals(FinanceSettleOrderStatus.SETTLE_COMPLETED,
                    settleOrderPo.getFinanceSettleOrderStatus())) {
                log.info("结算单已流转{}，跳过处理结算单同意逻辑",
                        FinanceSettleOrderStatus.SETTLE_COMPLETED.getRemark());
                return;
            }

            // 变更结算单信息&记录日志
            settleOrderPo.setFinanceSettleOrderStatus(FinanceSettleOrderStatus.SETTLE_COMPLETED);
            settleOrderPo.setSettleFinishTime(LocalDateTime.now());
            settleOrderPo.setCtrlUser(workflowTaskVo.getTaskUserCode());
            settleOrderPo.setCtrlUsername(workflowTaskVo.getTaskUsername());
            settleOrderPo.setWorkflowFinishTime(LocalDateTime.now());
            settleOrderDao.updateByIdVersion(settleOrderPo);

            GlobalContext.setUserKey(workflowTaskVo.getTaskUserCode());
            GlobalContext.setUsername(workflowTaskVo.getTaskUsername());
            logBaseService.simpleLog(
                    LogBizModule.FINANCE_SETTLE_ORDER_STATUS, ScmConstant.SETTLE_ORDER_LOG_VERSION,
                    settleOrderPo.getFinanceSettleOrderNo(), settleOrderPo.getFinanceSettleOrderStatus().getRemark(),
                    Collections.singletonList(new LogVersionBo("操作", null, "审批-通过"))
            );

            // 刷新结算单关联结转单状态
            List<FinanceSettleOrderItemPo> settleOrderItemPos
                    = settleOrderItemDao.findBySettleOrderNo(settleOrderPo.getFinanceSettleOrderNo());
            if (CollectionUtils.isEmpty(settleOrderItemPos)) {
                log.error("飞书结算单完结：同意审批回调异常!结算单{}明细信息缺失，请相关同事注意！",
                        settleOrderPo.getFinanceSettleOrderNo());
                return;
            }

            List<FinanceSettleOrderItemPo> carryoverOrderList = settleOrderItemPos.stream()
                    .filter(settleOrderItemPo -> Objects.equals(FinanceSettleOrderItemType.CARRYOVER_ORDER,
                            settleOrderItemPo.getFinanceSettleOrderItemType()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(carryoverOrderList)) {
                log.info("飞书结算单完结刷新结算单关联结转单结束！无结转单信息。");
                return;
            }
            for (FinanceSettleOrderItemPo carryoverOrder : carryoverOrderList) {
                refreshCarryoverOrderStatus(carryoverOrder.getBusinessNo());
            }
        }
    }
}
