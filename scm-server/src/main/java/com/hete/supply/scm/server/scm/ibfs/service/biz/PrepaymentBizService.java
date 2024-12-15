package com.hete.supply.scm.server.scm.ibfs.service.biz;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.PrepaymentSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.PrepaymentOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.PrepaymentExportVo;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.common.util.ScmFormatUtil;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.McRemoteService;
import com.hete.supply.scm.remote.dubbo.UdbRemoteService;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.enums.FeishuAuditOrderType;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.feishu.dao.FeishuAuditOrderDao;
import com.hete.supply.scm.server.scm.feishu.service.base.AbstractApproveCreator;
import com.hete.supply.scm.server.scm.ibfs.config.ScmFinanceProp;
import com.hete.supply.scm.server.scm.ibfs.converter.PrepaymentConverter;
import com.hete.supply.scm.server.scm.ibfs.dao.*;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.FinancePrepaymentApproveBo;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.PrepaymentDetailApproveBo;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.*;
import com.hete.supply.scm.server.scm.ibfs.entity.po.*;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.*;
import com.hete.supply.scm.server.scm.ibfs.enums.Currency;
import com.hete.supply.scm.server.scm.ibfs.enums.DeductionStatus;
import com.hete.supply.scm.server.scm.ibfs.enums.PaymentBizType;
import com.hete.supply.scm.server.scm.ibfs.service.base.IbfsPrepaymentApproveCreator;
import com.hete.supply.scm.server.scm.ibfs.service.base.PaymentBaseService;
import com.hete.supply.scm.server.scm.ibfs.service.base.PrepaymentBaseService;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseBaseService;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPaymentAccountPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierBaseService;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierPaymentAccountBaseService;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/5/15 14:43
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrepaymentBizService {
    private final IdGenerateService idGenerateService;
    private final McRemoteService mcRemoteService;
    private final FeishuAuditOrderDao feishuAuditOrderDao;
    private final FinancePrepaymentOrderDao financePrepaymentOrderDao;
    private final SupplierBaseService supplierBaseService;
    private final SupplierPaymentAccountBaseService supplierPaymentAccountBaseService;
    private final FinancePrepaymentOrderItemDao financePrepaymentOrderItemDao;
    private final LogBaseService logBaseService;
    private final ScmImageBaseService scmImageBaseService;
    private final UdbRemoteService udbRemoteService;
    private final FinancePaymentItemDao financePaymentItemDao;
    private final PaymentBaseService paymentBaseService;
    private final PurchaseBaseService purchaseBaseService;
    private final PrepaymentBaseService prepaymentBaseService;
    private final ConsistencySendMqService consistencySendMqService;
    private final FinanceRecoPrepaymentDao financeRecoPrepaymentDao;
    private final FinanceRecoOrderDao financeRecoOrderDao;
    private final ScmFinanceProp scmFinanceProp;


    private final static String RECIPIENT_SUBJECT = "贝坤电子商务有限公司";
    private final static BigDecimal USD_TO_CNY_RATE = new BigDecimal("7.3");


    public CommonPageResult.PageInfo<PrepaymentSearchVo> searchPrepayment(PrepaymentSearchDto dto) {
        final CommonPageResult.PageInfo<PrepaymentSearchVo> pageInfo = financePrepaymentOrderDao.searchPrepayment(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);

        final List<PrepaymentSearchVo> records = pageInfo.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new CommonPageResult.PageInfo<>();
        }
        final List<String> supplierCodeList = records.stream()
                .map(PrepaymentSearchVo::getSupplierCode)
                .distinct()
                .collect(Collectors.toList());
        final List<SupplierPo> supplierPoList = supplierBaseService.getBySupplierCodeList(supplierCodeList);
        final Map<String, String> supplierCodeNameMap = supplierPoList.stream()
                .collect(Collectors.toMap(SupplierPo::getSupplierCode, SupplierPo::getSupplierName));
        records.forEach(record -> {
            record.setSupplierName(supplierCodeNameMap.get(record.getSupplierCode()));
        });

        return pageInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportPrepaymentOrder(PrepaymentSearchDto dto) {
        Integer exportTotals = financePrepaymentOrderDao.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        consistencySendMqService.execSendMq(ScmExportHandler.class,
                new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(), FileOperateBizType.SCM_FINANCE_PREPAYMENT_ORDER_EXPORT.getCode(), dto));
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportSupplierPrepaymentOrder(PrepaymentSearchDto dto) {
        Integer exportTotals = financePrepaymentOrderDao.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        consistencySendMqService.execSendMq(ScmExportHandler.class,
                new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(), FileOperateBizType.SPM_FINANCE_PREPAYMENT_ORDER_EXPORT.getCode(), dto));
    }

    public Integer getExportTotals(PrepaymentSearchDto dto) {
        Integer exportTotals = financePrepaymentOrderDao.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        return exportTotals;
    }


    public CommonResult<ExportationListResultBo<PrepaymentExportVo>> getExportList(PrepaymentSearchDto dto) {
        ExportationListResultBo<PrepaymentExportVo> resultBo = new ExportationListResultBo<>();

        final CommonPageResult.PageInfo<PrepaymentExportVo> pageInfo = financePrepaymentOrderDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<PrepaymentExportVo> records = pageInfo.getRecords();
        final List<String> prepaymentOrderNoList = records.stream()
                .map(PrepaymentExportVo::getPrepaymentOrderNo)
                .distinct()
                .collect(Collectors.toList());
        final List<FinanceRecoPrepaymentPo> financeRecoPrepaymentPoList = financeRecoPrepaymentDao.getListByPrepaymentNoList(prepaymentOrderNoList);
        final Map<String, String> prepaymentNoRecoNoMap = financeRecoPrepaymentPoList.stream()
                .collect(Collectors.toMap(FinanceRecoPrepaymentPo::getPrepaymentOrderNo,
                        FinanceRecoPrepaymentPo::getFinanceRecoOrderNo));


        records.forEach(record -> {
            record.setPrepaymentOrderStatusStr(record.getPrepaymentOrderStatus().getRemark());
            record.setCreateTimeStr(ScmTimeUtil.localDateTimeToStr(record.getCreateTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            record.setPrepaymentTypeStr(record.getPrepaymentType().getRemark());
            record.setFinanceRecoOrderNo(prepaymentNoRecoNoMap.get(record.getPrepaymentOrderNo()));
        });

        resultBo.setRowDataList(records);

        return CommonResult.success(resultBo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addPrepayment(PrepaymentAddDto dto) {
        final SupplierPo supplierPo = supplierBaseService.getSupplierByCode(dto.getSupplierCode());
        if (null == supplierPo) {
            throw new BizException("供应商:{}不存在，请联系系统管理员!", dto.getSupplierCode());
        }
        if (StringUtils.isBlank(supplierPo.getSupplierAlias())) {
            throw new BizException("当前供应商:{}的别称为空，请联系系统管理员维护数据！", supplierPo.getSupplierCode());
        }
        final List<PrepaymentAddItemDto> prepaymentAddItemList = dto.getPrepaymentAddItemList();

        // 收款金额求和
        final BigDecimal targetPrepaymentTotalMoney = prepaymentAddItemList.stream()
                .map(PrepaymentAddItemDto::getTargetPrepaymentMoney)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (targetPrepaymentTotalMoney.compareTo(dto.getPrepaymentMoney()) > 0) {
            throw new ParamIllegalException("收款总金额:{}大于预付款金额:{}，请重新填写后再提交",
                    targetPrepaymentTotalMoney, dto.getPrepaymentMoney());
        }

        String prepaymentOrderNo = idGenerateService.getConfuseCode(ScmConstant.PREPAYMENT_ORDER_NO_PREFIX
                + ScmFormatUtil.subStringLastThree(supplierPo.getSupplierAlias()), TimeType.CN_DAY, ConfuseLength.L_4);
        final FinancePrepaymentOrderPo financePrepaymentOrderPo = PrepaymentConverter.addDtoToPo(prepaymentOrderNo, dto);

        // 查找账号
        final List<String> supplierAccountList = prepaymentAddItemList.stream()
                .map(PrepaymentAddItemDto::getAccount)
                .distinct()
                .collect(Collectors.toList());
        final List<SupplierPaymentAccountPo> supplierPaymentAccountPoList = supplierPaymentAccountBaseService.getListByAccount(supplierAccountList);
        final Map<String, SupplierPaymentAccountPo> accountPoMap = supplierPaymentAccountPoList.stream()
                .collect(Collectors.toMap(SupplierPaymentAccountPo::getAccount, Function.identity()));
        final List<FinancePrepaymentOrderItemPo> financePrepaymentOrderItemPoList = this.getPrepaymentItemPoList(prepaymentAddItemList,
                prepaymentOrderNo, dto, accountPoMap, financePrepaymentOrderPo);

        financePrepaymentOrderDao.insert(financePrepaymentOrderPo);
        financePrepaymentOrderItemDao.insertBatch(financePrepaymentOrderItemPoList);

        // 保存附件
        scmImageBaseService.insertBatchImage(dto.getFileCodeList(), ImageBizType.PREPAYMENT_FILE, financePrepaymentOrderPo.getFinancePrepaymentOrderId());
        // 日志
        final LogVersionBo logVersionBo = new LogVersionBo();
        logVersionBo.setKey("操作");
        logVersionBo.setValue("新建预付款单");
        logBaseService.simpleLog(LogBizModule.PREPAYMENT_LIST_SIMPLE, ScmConstant.PREPAYMENT_LOG_VERSION,
                prepaymentOrderNo, PrepaymentOrderStatus.TO_BE_FOLLOW_SUBMIT.getRemark(),
                Collections.singletonList(logVersionBo));
    }

    /**
     * 根据入参创建预付款明细
     *
     * @param prepaymentAddItemList
     * @param prepaymentOrderNo
     * @param dto
     * @param accountPoMap
     * @param financePrepaymentOrderPo
     * @return
     */
    private List<FinancePrepaymentOrderItemPo> getPrepaymentItemPoList(List<PrepaymentAddItemDto> prepaymentAddItemList,
                                                                       String prepaymentOrderNo, PrepaymentAddDto dto,
                                                                       Map<String, SupplierPaymentAccountPo> accountPoMap,
                                                                       FinancePrepaymentOrderPo financePrepaymentOrderPo) {

        return prepaymentAddItemList.stream().map(addItemDto -> {
            final FinancePrepaymentOrderItemPo financePrepaymentOrderItemPo = new FinancePrepaymentOrderItemPo();
            financePrepaymentOrderItemPo.setPrepaymentOrderNo(prepaymentOrderNo);
            financePrepaymentOrderItemPo.setSupplierCode(dto.getSupplierCode());
            financePrepaymentOrderItemPo.setAccount(addItemDto.getAccount());
            final SupplierPaymentAccountPo supplierPaymentAccountPo = accountPoMap.get(addItemDto.getAccount());
            if (null == supplierPaymentAccountPo) {
                throw new BizException("收款账户:{}不存在，请联系系统管理员！", addItemDto.getAccount());
            }
            financePrepaymentOrderItemPo.setSupplierPaymentAccountType(supplierPaymentAccountPo.getSupplierPaymentAccountType());
            financePrepaymentOrderItemPo.setBankName(supplierPaymentAccountPo.getBankName());
            financePrepaymentOrderItemPo.setAccountUsername(supplierPaymentAccountPo.getAccountUsername());
            financePrepaymentOrderItemPo.setBankSubbranchName(supplierPaymentAccountPo.getBankSubbranchName());
            financePrepaymentOrderItemPo.setAccountRemarks(supplierPaymentAccountPo.getRemarks());
            financePrepaymentOrderItemPo.setBankProvince(supplierPaymentAccountPo.getBankProvince());
            financePrepaymentOrderItemPo.setBankCity(supplierPaymentAccountPo.getBankCity());
            financePrepaymentOrderItemPo.setBankArea(supplierPaymentAccountPo.getBankArea());
            financePrepaymentOrderItemPo.setSubject(supplierPaymentAccountPo.getSubject());
            financePrepaymentOrderItemPo.setPrepaymentMoney(addItemDto.getPrepaymentMoney());
            final Currency currency = supplierPaymentAccountPo.getSupplierPaymentCurrencyType().toCurrency();
            final BigDecimal prepaymentMoney = Currency.currencyExchange(addItemDto.getTargetPrepaymentMoney(), financePrepaymentOrderPo.getCurrency(), currency);
            if (prepaymentMoney.compareTo(addItemDto.getPrepaymentMoney()) != 0) {
                throw new BizException("入参收款金额:{}计算有误，与实际计算金额:{}不相等", addItemDto.getPrepaymentMoney(), prepaymentMoney);
            }
            financePrepaymentOrderItemPo.setCurrency(currency);
            financePrepaymentOrderItemPo.setTargetPrepaymentMoney(addItemDto.getTargetPrepaymentMoney());
            financePrepaymentOrderItemPo.setExpectedPrepaymentDate(addItemDto.getExpectedPrepaymentDate());

            return financePrepaymentOrderItemPo;
        }).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void submitForApproval(PrepaymentIdAndVersionDto dto) {
        final FinancePrepaymentOrderPo financePrepaymentOrderPo = financePrepaymentOrderDao.getByIdVersion(dto.getFinancePrepaymentOrderId(), dto.getVersion());
        if (null == financePrepaymentOrderPo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }

        if (!GlobalContext.getUserKey().equals(financePrepaymentOrderPo.getCtrlUser())) {
            throw new ParamIllegalException("当前用户无法对预付款单:{}进行提交审批操作，请刷新后重试！", financePrepaymentOrderPo.getPrepaymentOrderNo());
        }

        final PrepaymentOrderStatus targetStatus = financePrepaymentOrderPo.getPrepaymentOrderStatus().toLaunchApprove();
        financePrepaymentOrderPo.setPrepaymentOrderStatus(targetStatus);
        financePrepaymentOrderPo.setFollowUser(GlobalContext.getUserKey());
        financePrepaymentOrderDao.updateByIdVersion(financePrepaymentOrderPo);
        final LogVersionBo logVersionBo = new LogVersionBo();
        logVersionBo.setKey("操作");
        logVersionBo.setValue("提交审批");
        logBaseService.simpleLog(LogBizModule.PREPAYMENT_LIST_SIMPLE, ScmConstant.PREPAYMENT_LOG_VERSION,
                financePrepaymentOrderPo.getPrepaymentOrderNo(), financePrepaymentOrderPo.getPrepaymentOrderStatus().getRemark(),
                Collections.singletonList(logVersionBo));

        final UserVo userVo = udbRemoteService.getByUserCode(GlobalContext.getUserKey());
        if (null == userVo) {
            throw new BizException("当前登录用户不存在，无法发起审批");
        }
        final OrgVo orgVo = udbRemoteService.getOrgByCode(userVo.getOrgCode());
        if (null == orgVo) {
            throw new BizException("当前登录用户部门不存在，无法发起审批");
        }
        String dept = Arrays.stream(orgVo.getOrgChainName().split("-"))
                .skip(1)
                .findFirst()
                .orElse("");
        final FinancePrepaymentApproveBo financePrepaymentApproveBo = new FinancePrepaymentApproveBo();
        financePrepaymentApproveBo.setRecipientSubject(RECIPIENT_SUBJECT);
        financePrepaymentApproveBo.setSupplierCode(financePrepaymentOrderPo.getSupplierCode());
        financePrepaymentApproveBo.setPrepaymentMoney(financePrepaymentOrderPo.getPrepaymentMoney().toString());
        financePrepaymentApproveBo.setPrepaymentType(financePrepaymentOrderPo.getPrepaymentType().getRemark());

        LocalDateTime cnTimeApplyDate = TimeUtil.convertZone(financePrepaymentOrderPo.getApplyDate(), TimeZoneId.UTC, TimeZoneId.CN);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        financePrepaymentApproveBo.setApplyDate(cnTimeApplyDate.format(formatter));
        financePrepaymentApproveBo.setDept(dept);
        financePrepaymentApproveBo.setReason(financePrepaymentOrderPo.getPrepaymentReason() +
                "。预付款申请:" + scmFinanceProp.getPrepaymentLink() + financePrepaymentOrderPo.getPrepaymentOrderNo());

        final List<PrepaymentDetailApproveBo> prepaymentDetailApproveList = new ArrayList<>();
        final PrepaymentDetailApproveBo prepaymentDetailApproveBo = new PrepaymentDetailApproveBo();
        prepaymentDetailApproveBo.setDetailType("预付款");
        prepaymentDetailApproveBo.setPrepaymentDetailMoney(financePrepaymentOrderPo.getPrepaymentMoney());
        prepaymentDetailApproveBo.setCurrency(financePrepaymentOrderPo.getCurrency());
        prepaymentDetailApproveBo.setRmbText(ScmFormatUtil.convertToChinese(financePrepaymentOrderPo.getPrepaymentMoney()));
        prepaymentDetailApproveList.add(prepaymentDetailApproveBo);
        financePrepaymentApproveBo.setPrepaymentDetailApproveList(prepaymentDetailApproveList);

        // 预付款附件
        List<String> fileCodeList
                = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.PREPAYMENT_FILE, Collections.singletonList(financePrepaymentOrderPo.getFinancePrepaymentOrderId()));
        if (CollectionUtils.isNotEmpty(fileCodeList)) {
            financePrepaymentApproveBo.setFileCodeList(fileCodeList);
        }

        AbstractApproveCreator<FinancePrepaymentApproveBo> abstractApproveCreator = new IbfsPrepaymentApproveCreator(idGenerateService,
                mcRemoteService, feishuAuditOrderDao);
        abstractApproveCreator.createFeiShuInstance(financePrepaymentOrderPo.getFinancePrepaymentOrderId(), financePrepaymentApproveBo);
    }

    public PrepaymentDetailVo prepaymentDetail(PrepaymentNoDto dto) {
        final FinancePrepaymentOrderPo financePrepaymentOrderPo = financePrepaymentOrderDao.getOneByNo(dto.getPrepaymentOrderNo());
        if (null == financePrepaymentOrderPo) {
            throw new BizException("预付款单:{}不存在，请联系系统管理员！", dto.getPrepaymentOrderNo());
        }
        final List<FinancePrepaymentOrderItemPo> financePrepaymentOrderItemPoList = financePrepaymentOrderItemDao.getListByNo(financePrepaymentOrderPo.getPrepaymentOrderNo());
        if (CollectionUtils.isEmpty(financePrepaymentOrderItemPoList)) {
            throw new BizException("预付款单收款明细:{}不存在，请联系系统管理员！", dto.getPrepaymentOrderNo());
        }

        // 付款记录
        final List<FinancePaymentOrderItemVo> financePaymentOrderItemList = paymentBaseService.getListByBizNoAndType(financePrepaymentOrderPo.getPrepaymentOrderNo(), PaymentBizType.PREPAYMENT);
        // 收款记录
        final List<FinancePrepaymentOrderItemVo> financePrepaymentOrderItemList = PrepaymentConverter.prepaymentItemPoListToVoList(financePrepaymentOrderItemPoList);
        final SupplierPo supplierPo = supplierBaseService.getSupplierByCode(financePrepaymentOrderPo.getSupplierCode());

        final UserVo userVo = udbRemoteService.getByUserCode(financePrepaymentOrderPo.getCtrlUser());
        final PrepaymentDetailVo prepaymentDetailVo = PrepaymentConverter.prepaymentPoToVo(financePrepaymentOrderPo,
                financePaymentOrderItemList, financePrepaymentOrderItemList, supplierPo, userVo);
        // 附件
        final List<String> fileCodeList = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.PREPAYMENT_FILE,
                Collections.singletonList(financePrepaymentOrderPo.getFinancePrepaymentOrderId()));
        prepaymentDetailVo.setFileCodeList(fileCodeList);

        // 本月货款
        final BigDecimal monthGoodsPayment = purchaseBaseService.getMonthGoodsPayment(prepaymentDetailVo.getSupplierCode());
        prepaymentDetailVo.setMonthGoodsPayment(monthGoodsPayment);

        // 计算已经付款的目标付款金额
        final BigDecimal targetPaymentMoney = financePaymentOrderItemList.stream()
                .map(FinancePaymentOrderItemVo::getTargetPaymentMoney)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        prepaymentDetailVo.setTargetPaymentMoney(targetPaymentMoney);

        // 赋值对账单
        if (DeductionStatus.ASSOCIATED.equals(financePrepaymentOrderPo.getDeductionStatus())) {
            final FinanceRecoPrepaymentPo financeRecoPrepaymentPo = financeRecoPrepaymentDao.getOneByPrepaymentNo(financePrepaymentOrderPo.getPrepaymentOrderNo());
            if (null != financeRecoPrepaymentPo) {
                final FinanceRecoOrderPo financeRecoOrderPo = financeRecoOrderDao.getOneByNo(financeRecoPrepaymentPo.getFinanceRecoOrderNo());
                if (null != financeRecoOrderPo) {
                    prepaymentDetailVo.setFinanceRecoOrderNo(financeRecoOrderPo.getFinanceRecoOrderNo());
                    prepaymentDetailVo.setFinanceRecoOrderStatus(financeRecoOrderPo.getFinanceRecoOrderStatus());
                }
            }
        }

        // 新增收款账户操作权限
        final String userKey = GlobalContext.getUserKey();
        if (financePrepaymentOrderPo.getFollowUser().equals(GlobalContext.getUserKey())
                && !PrepaymentOrderStatus.FULL_PAYMENT.equals(financePrepaymentOrderPo.getPrepaymentOrderStatus())
                && !PrepaymentOrderStatus.DELETED.equals(financePrepaymentOrderPo.getPrepaymentOrderStatus())) {
            prepaymentDetailVo.setReceiveAdd(BooleanType.TRUE);
        } else {
            prepaymentDetailVo.setReceiveAdd(BooleanType.FALSE);
        }

        return prepaymentDetailVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addPayment(PaymentAddDto dto) {

        final FinancePrepaymentOrderPo financePrepaymentOrderPo = financePrepaymentOrderDao.getOneByNo(dto.getPrepaymentOrderNo());
        if (null == financePrepaymentOrderPo) {
            throw new BizException("预付款单:{}不存在，请联系系统管理员！", dto.getPrepaymentOrderNo());
        }
        if (!GlobalContext.getUserKey().equals(financePrepaymentOrderPo.getCtrlUser())) {
            throw new ParamIllegalException("当前用户无法对预付款单:{}进行新增付款记录操作，请刷新后重试！", dto.getPrepaymentOrderNo());
        }
        final PrepaymentOrderStatus prepaymentOrderStatus = financePrepaymentOrderPo.getPrepaymentOrderStatus();
        if (!PrepaymentOrderStatus.TO_BE_PAYMENT.equals(prepaymentOrderStatus)
                && !PrepaymentOrderStatus.PARTIAL_PAYMENT.equals(prepaymentOrderStatus)) {
            throw new ParamIllegalException("当前状态不处于{}、{}，无法添加付款信息", PrepaymentOrderStatus.TO_BE_PAYMENT.getRemark(),
                    PrepaymentOrderStatus.PARTIAL_PAYMENT.getRemark());
        }

        final List<FinancePaymentItemPo> financePaymentItemPoList = financePaymentItemDao.getListByBizNoAndType(dto.getPrepaymentOrderNo(), PaymentBizType.PREPAYMENT);
        // 付款金额大于预付款金额
        BigDecimal totalTargetPaymentMoney = Optional.ofNullable(financePaymentItemPoList).orElse(Collections.emptyList()).stream()
                .map(FinancePaymentItemPo::getTargetPaymentMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        totalTargetPaymentMoney = dto.getTargetPaymentMoney().add(totalTargetPaymentMoney);
        if (totalTargetPaymentMoney.compareTo(financePrepaymentOrderPo.getPrepaymentMoney()) > 0) {
            throw new ParamIllegalException("付款总金额兑预付金额为:{}，超过了预付款金额:{}", totalTargetPaymentMoney,
                    financePrepaymentOrderPo.getPrepaymentMoney());
        }

        // 新增付款数据
        final SupplierPaymentAccountPo supplierPaymentAccountPo = supplierPaymentAccountBaseService.getByAccount(dto.getAccount());
        if (null == supplierPaymentAccountPo) {
            throw new BizException("供应商收款账户:{}不存在，请刷新后重试！", dto.getAccount());
        }
        final FinancePaymentItemPo financePaymentItemPo = PrepaymentConverter.addDtoToPaymentItemPo(financePrepaymentOrderPo.getPrepaymentOrderNo(),
                dto, supplierPaymentAccountPo);
        financePaymentItemDao.insert(financePaymentItemPo);

        // 更新预付款信息
        financePrepaymentOrderPo.setCanDeductionMoney(financePrepaymentOrderPo.getCanDeductionMoney()
                .add(dto.getRmbPaymentMoney()));
        financePrepaymentOrderPo.setPaymentMoney(financePrepaymentOrderPo.getPaymentMoney()
                .add(dto.getRmbPaymentMoney()));
        financePrepaymentOrderPo.setTargetPaymentMoney(financePrepaymentOrderPo.getTargetPaymentMoney()
                .add(dto.getTargetPaymentMoney()));
        // 付款金额大于等于预付款金额
        PrepaymentOrderStatus targetStatus;

        final LogVersionBo logVersionBo = new LogVersionBo();
        logVersionBo.setKey("操作");
        logVersionBo.setValue("新增付款记录");
        if (totalTargetPaymentMoney.compareTo(financePrepaymentOrderPo.getPrepaymentMoney()) == 0) {
            targetStatus = financePrepaymentOrderPo.getPrepaymentOrderStatus().toFullPayment();
            // 日志
            logBaseService.simpleLog(LogBizModule.PREPAYMENT_LIST_SIMPLE, ScmConstant.PREPAYMENT_LOG_VERSION,
                    financePrepaymentOrderPo.getPrepaymentOrderNo(), targetStatus.getRemark(),
                    Collections.singletonList(logVersionBo));
        } else {
            targetStatus = financePrepaymentOrderPo.getPrepaymentOrderStatus().toPartialPayment();
            // 日志
            logBaseService.simpleLog(LogBizModule.PREPAYMENT_LIST_SIMPLE, ScmConstant.PREPAYMENT_LOG_VERSION,
                    financePrepaymentOrderPo.getPrepaymentOrderNo(), targetStatus.getRemark(),
                    Collections.singletonList(logVersionBo));
        }
        financePrepaymentOrderPo.setPrepaymentOrderStatus(targetStatus);
        financePrepaymentOrderDao.updateByIdVersion(financePrepaymentOrderPo);

        // 附件
        scmImageBaseService.insertBatchImage(dto.getFileCodeList(), ImageBizType.PAYMENT_FILE,
                financePaymentItemPo.getFinancePaymentItemId());

    }

    public void batchTransferPrepayment(PrepaymentTransferDto dto) {
        final List<PrepaymentTransferItemDto> prepaymentTransferItemList = dto.getPrepaymentTransferItemList();
        final List<String> prepaymentOrderNoList = prepaymentTransferItemList.stream()
                .map(PrepaymentTransferItemDto::getPrepaymentOrderNo)
                .distinct()
                .collect(Collectors.toList());
        final List<FinancePrepaymentOrderPo> financePrepaymentOrderPoList = financePrepaymentOrderDao.getListByNoList(prepaymentOrderNoList);
        if (financePrepaymentOrderPoList.size() != prepaymentOrderNoList.size()) {
            throw new BizException("预付款数据异常，请联系系统管理员");
        }

        // 单据状态校验
        financePrepaymentOrderPoList.forEach(po -> {
            if (!PrepaymentOrderStatus.canTransferStatus().contains(po.getPrepaymentOrderStatus())) {
                throw new ParamIllegalException("当前单据:{}状态:{}无法进行转交操作，请刷新后重试",
                        po.getPrepaymentOrderNo(), po.getPrepaymentOrderStatus().getRemark());
            }
        });

        final Map<String, PrepaymentTransferItemDto> prepaymentOrderNoDtoMap = prepaymentTransferItemList.stream()
                .collect(Collectors.toMap(PrepaymentTransferItemDto::getPrepaymentOrderNo,
                        Function.identity()));
        final UserVo userVo = udbRemoteService.getByUserCode(dto.getTransferUser());
        if (null == userVo) {
            throw new BizException("转交人数据不存在，请重新选择其他转交人", dto.getTransferUser());
        }

        // 校验操作人是否为当前用户
        financePrepaymentOrderPoList.forEach(po -> {
            // 校验操作人是否为当前用户
            if (!GlobalContext.getUserKey().equals(po.getCtrlUser())) {
                throw new ParamIllegalException("当前登录用户无法操作该预付款单:{}，请刷新后重试", po.getPrepaymentOrderNo());
            }
        });

        // 先处理简单的数据变更类型
        final List<FinancePrepaymentOrderPo> simpleTransferList = financePrepaymentOrderPoList.stream()
                .filter(po -> PrepaymentOrderStatus.transferStatus().contains(po.getPrepaymentOrderStatus()))
                .collect(Collectors.toList());
        prepaymentBaseService.simpleTransfer(simpleTransferList, dto, userVo);

        // 再处理飞书审批相关
        List<String> failPrepaymentCodeList = new ArrayList<>();
        financePrepaymentOrderPoList.stream()
                .filter(po -> PrepaymentOrderStatus.APPROVING.equals(po.getPrepaymentOrderStatus()))
                .forEach(po -> prepaymentBaseService.feiShuTransfer(prepaymentOrderNoDtoMap, po, userVo, dto, failPrepaymentCodeList));

        if (CollectionUtils.isNotEmpty(failPrepaymentCodeList)) {
            throw new ParamIllegalException("转交条件：当前登录账号为单据处理人，且转交对象非审批节点前置处理人。预付款转交失败单号：{}", failPrepaymentCodeList);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void cancelPrepayment(PrepaymentIdAndVersionDto dto) {
        final FinancePrepaymentOrderPo financePrepaymentOrderPo = financePrepaymentOrderDao.getByIdVersion(dto.getFinancePrepaymentOrderId(), dto.getVersion());
        if (null == financePrepaymentOrderPo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }

        if (!GlobalContext.getUserKey().equals(financePrepaymentOrderPo.getCtrlUser())) {
            throw new ParamIllegalException("当前用户无法对预付款单:{}进行作废操作，请刷新后重试！", financePrepaymentOrderPo.getPrepaymentOrderNo());
        }


        final PrepaymentOrderStatus prepaymentOrderStatus = financePrepaymentOrderPo.getPrepaymentOrderStatus().toDelete();
        financePrepaymentOrderPo.setPrepaymentOrderStatus(prepaymentOrderStatus);
        financePrepaymentOrderDao.updateByIdVersion(financePrepaymentOrderPo);

        // 日志
        final LogVersionBo logVersionBo = new LogVersionBo();
        logVersionBo.setKey("操作");
        logVersionBo.setValue("作废预付款单");
        logBaseService.simpleLog(LogBizModule.PREPAYMENT_LIST_SIMPLE, ScmConstant.PREPAYMENT_LOG_VERSION,
                financePrepaymentOrderPo.getPrepaymentOrderNo(), prepaymentOrderStatus.getRemark(),
                Collections.singletonList(logVersionBo));
    }

    public MonthGoodsPaymentVo getMonthGoodsPayment(SupplierCodeDto dto) {
        final BigDecimal monthGoodsPayment = purchaseBaseService.getMonthGoodsPayment(dto.getSupplierCode());
        final MonthGoodsPaymentVo monthGoodsPaymentVo = new MonthGoodsPaymentVo();
        monthGoodsPaymentVo.setMonthGoodsPayment(monthGoodsPayment);
        return monthGoodsPaymentVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addPrepaymentReceive(PrepaymentReceiveDto dto) {
        final FinancePrepaymentOrderPo financePrepaymentOrderPo = financePrepaymentOrderDao.getOneByNo(dto.getPrepaymentOrderNo());
        if (null == financePrepaymentOrderPo) {
            throw new BizException("预付款单:{}不存在，请联系系统管理员！", dto.getPrepaymentOrderNo());
        }
        final SupplierPaymentAccountPo supplierPaymentAccountPo = supplierPaymentAccountBaseService.getByAccount(dto.getAccount());
        if (null == supplierPaymentAccountPo) {
            throw new BizException("供应商账号:{}不存在，请联系系统管理员！", dto.getAccount());
        }
        if (!GlobalContext.getUserKey().equals(financePrepaymentOrderPo.getFollowUser())) {
            throw new ParamIllegalException("当前用户无法对预付款单:{}进行新增收款账户操作，请刷新后重试！", dto.getPrepaymentOrderNo());
        }
        final List<FinancePrepaymentOrderItemPo> financePrepaymentOrderItemPoList = financePrepaymentOrderItemDao.getListByNo(dto.getPrepaymentOrderNo());

        BigDecimal targetPrepaymentTotalMoney = Optional.ofNullable(financePrepaymentOrderItemPoList).orElse(new ArrayList<>())
                .stream()
                .map(FinancePrepaymentOrderItemPo::getTargetPrepaymentMoney)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        targetPrepaymentTotalMoney = targetPrepaymentTotalMoney.add(dto.getTargetPrepaymentMoney());
        // 校验收款明细总和不能大于预付款总额
        if (targetPrepaymentTotalMoney.compareTo(financePrepaymentOrderPo.getPrepaymentMoney()) > 0) {
            throw new ParamIllegalException("收款总金额:{}大于预付款金额:{}，请重新填写后再提交", targetPrepaymentTotalMoney,
                    financePrepaymentOrderPo.getPrepaymentMoney());
        }

        // 构建预付款收款信息
        final Currency currency = supplierPaymentAccountPo.getSupplierPaymentCurrencyType().toCurrency();
        final BigDecimal prepaymentMoney = Currency.currencyExchange(dto.getTargetPrepaymentMoney(), financePrepaymentOrderPo.getCurrency(), currency);
        if (prepaymentMoney.compareTo(dto.getPrepaymentMoney()) != 0) {
            throw new BizException("入参收款金额:{}计算有误，与实际计算金额:{}不相等", dto.getPrepaymentMoney(), prepaymentMoney);
        }

        final FinancePrepaymentOrderItemPo financePrepaymentOrderItemPo = new FinancePrepaymentOrderItemPo();
        financePrepaymentOrderItemPo.setPrepaymentOrderNo(dto.getPrepaymentOrderNo());
        financePrepaymentOrderItemPo.setSupplierCode(financePrepaymentOrderPo.getSupplierCode());
        financePrepaymentOrderItemPo.setAccount(dto.getAccount());
        financePrepaymentOrderItemPo.setSupplierPaymentAccountType(supplierPaymentAccountPo.getSupplierPaymentAccountType());
        financePrepaymentOrderItemPo.setBankName(supplierPaymentAccountPo.getBankName());
        financePrepaymentOrderItemPo.setAccountUsername(supplierPaymentAccountPo.getAccountUsername());
        financePrepaymentOrderItemPo.setBankSubbranchName(supplierPaymentAccountPo.getBankSubbranchName());
        financePrepaymentOrderItemPo.setAccountRemarks(supplierPaymentAccountPo.getRemarks());
        financePrepaymentOrderItemPo.setBankProvince(supplierPaymentAccountPo.getBankProvince());
        financePrepaymentOrderItemPo.setBankCity(supplierPaymentAccountPo.getBankCity());
        financePrepaymentOrderItemPo.setBankArea(supplierPaymentAccountPo.getBankArea());
        financePrepaymentOrderItemPo.setSubject(supplierPaymentAccountPo.getSubject());
        financePrepaymentOrderItemPo.setPrepaymentMoney(dto.getPrepaymentMoney());
        financePrepaymentOrderItemPo.setCurrency(currency);
        financePrepaymentOrderItemPo.setTargetPrepaymentMoney(dto.getTargetPrepaymentMoney());
        financePrepaymentOrderItemPo.setExpectedPrepaymentDate(dto.getExpectedPrepaymentDate());

        financePrepaymentOrderItemDao.insert(financePrepaymentOrderItemPo);

        // 日志
        final LogVersionBo logVersionBo = new LogVersionBo();
        logVersionBo.setKey("操作");
        logVersionBo.setValue("新增收款账户");
        logBaseService.simpleLog(LogBizModule.PREPAYMENT_LIST_SIMPLE, ScmConstant.PREPAYMENT_LOG_VERSION,
                financePrepaymentOrderPo.getPrepaymentOrderNo(), financePrepaymentOrderPo.getPrepaymentOrderStatus().getRemark(),
                Collections.singletonList(logVersionBo));
    }

    public List<SupplierVo> getSupplierList(PrepaymentSearchDto dto) {
        // 预付款对应的供应商
        final List<String> prePaymentSupplierCodeList = financePrepaymentOrderDao.getSupplierList(dto);
        final List<SupplierPo> supplierPoList = supplierBaseService.getBySupplierCodeList(prePaymentSupplierCodeList);
        return Optional.ofNullable(supplierPoList).orElse(new ArrayList<>())
                .stream().map(po -> {
                    final SupplierVo supplierVo = new SupplierVo();
                    supplierVo.setSupplierCode(po.getSupplierCode());
                    supplierVo.setSupplierName(po.getSupplierName());
                    supplierVo.setSupplierGrade(po.getSupplierGrade());
                    return supplierVo;
                }).sorted(Comparator.comparing(SupplierVo::getSupplierGrade))
                .collect(Collectors.toList());
    }

    public PrepaymentRiskMsgVo prepaymentRiskMsg(PrepaymentNoDto dto) {
        final FinancePrepaymentOrderPo financePrepaymentOrderPo = financePrepaymentOrderDao.getOneByNo(dto.getPrepaymentOrderNo());
        if (null == financePrepaymentOrderPo) {
            throw new BizException("预付款单:{}不存在，请联系系统管理员！", dto.getPrepaymentOrderNo());
        }

        final PrepaymentRiskMsgVo prepaymentRiskMsgVo = new PrepaymentRiskMsgVo();
        final BigDecimal supplierWarehousingMoney = purchaseBaseService.purchaseLastMonWarehousingMoney(financePrepaymentOrderPo.getSupplierCode());

        final BigDecimal supplierInTransitMoney = purchaseBaseService.purchaseLastMonInTransitMoney(financePrepaymentOrderPo.getSupplierCode());

        final BigDecimal waitPrePayMoney = prepaymentBaseService.getAllCanDeductionMoney(financePrepaymentOrderPo.getSupplierCode());

        final BigDecimal lastThreeMonPrePayMoney = prepaymentBaseService.getRecentlyPrepaymentMoney(financePrepaymentOrderPo.getSupplierCode());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneYearAgo = now.minusYears(1);
        final Long lastYearPrepayFailUserCnt = prepaymentBaseService.getApproveFailTimesByFollowUser(FeishuAuditOrderType.IBFS_PREPAYMENT_APPROVE,
                financePrepaymentOrderPo.getFollowUser(), oneYearAgo, now);
        final Long lastYearPrepayFailSupplierCnt = prepaymentBaseService.getApproveFailTimesBySupplier(FeishuAuditOrderType.IBFS_PREPAYMENT_APPROVE,
                financePrepaymentOrderPo.getSupplierCode(), oneYearAgo, now);

        prepaymentRiskMsgVo.setSupplierWarehousingMoney(supplierWarehousingMoney);
        prepaymentRiskMsgVo.setSupplierInTransitMoney(supplierInTransitMoney);
        prepaymentRiskMsgVo.setWaitPrePayMoney(waitPrePayMoney);
        prepaymentRiskMsgVo.setLastThreeMonPrePayMoney(lastThreeMonPrePayMoney);
        prepaymentRiskMsgVo.setLastYearRecoFailApproveCnt(lastYearPrepayFailUserCnt);
        prepaymentRiskMsgVo.setLastYearPrePayFailApproveCnt(lastYearPrepayFailSupplierCnt);
        prepaymentRiskMsgVo.setIsRisk(BooleanType.FALSE);
        prepaymentRiskMsgVo.setSupplierWarehousingMoneyRisk(BooleanType.FALSE);
        prepaymentRiskMsgVo.setSupplierInTransitMoneyRisk(BooleanType.FALSE);
        prepaymentRiskMsgVo.setWaitPrePayMoneyRisk(BooleanType.FALSE);
        prepaymentRiskMsgVo.setLastThreeMonPrePayMoneyRisk(BooleanType.FALSE);
        prepaymentRiskMsgVo.setLastYearRecoFailApproveCntRisk(BooleanType.FALSE);
        prepaymentRiskMsgVo.setLastYearPrePayFailApproveCntRisk(BooleanType.FALSE);
        // 判断风险
        if (prepaymentRiskMsgVo.getSupplierWarehousingMoney().compareTo(financePrepaymentOrderPo.getPrepaymentMoney()) > 0) {
            log.info("供应商上月入库金额:{}大于预付款总金额:{}", prepaymentRiskMsgVo.getSupplierWarehousingMoney(),
                    financePrepaymentOrderPo.getPrepaymentMoney());
            prepaymentRiskMsgVo.setIsRisk(BooleanType.TRUE);
            prepaymentRiskMsgVo.setSupplierWarehousingMoneyRisk(BooleanType.TRUE);
        }
        if (prepaymentRiskMsgVo.getSupplierInTransitMoney().compareTo(financePrepaymentOrderPo.getPrepaymentMoney()) > 0) {
            log.info("供应商在途生产金额:{}大于预付款总金额:{}", prepaymentRiskMsgVo.getSupplierInTransitMoney(),
                    financePrepaymentOrderPo.getPrepaymentMoney());
            prepaymentRiskMsgVo.setIsRisk(BooleanType.TRUE);
            prepaymentRiskMsgVo.setSupplierInTransitMoneyRisk(BooleanType.TRUE);
        }
        if (prepaymentRiskMsgVo.getWaitPrePayMoney().compareTo(prepaymentRiskMsgVo.getSupplierWarehousingMoney()) > 0) {
            log.info("待预付预付款金额:{}大于上月入库金额:{}", prepaymentRiskMsgVo.getWaitPrePayMoney(),
                    prepaymentRiskMsgVo.getSupplierWarehousingMoney());
            prepaymentRiskMsgVo.setIsRisk(BooleanType.TRUE);
            prepaymentRiskMsgVo.setWaitPrePayMoneyRisk(BooleanType.TRUE);
            prepaymentRiskMsgVo.setSupplierWarehousingMoneyRisk(BooleanType.TRUE);
        }
        if (prepaymentRiskMsgVo.getLastYearRecoFailApproveCnt() > 3) {
            log.info("近1年对账审批失败次数:{}，大于3次", prepaymentRiskMsgVo.getLastYearRecoFailApproveCnt());
            prepaymentRiskMsgVo.setIsRisk(BooleanType.TRUE);
            prepaymentRiskMsgVo.setLastYearRecoFailApproveCntRisk(BooleanType.TRUE);
        }
        if (prepaymentRiskMsgVo.getLastYearPrePayFailApproveCnt() > 3) {
            log.info("近1年预付款审批失败次数:{}，大于3次", prepaymentRiskMsgVo.getLastYearPrePayFailApproveCnt());
            prepaymentRiskMsgVo.setIsRisk(BooleanType.TRUE);
            prepaymentRiskMsgVo.setLastYearPrePayFailApproveCntRisk(BooleanType.TRUE);
        }


        return prepaymentRiskMsgVo;
    }

    public void approveWorkFlow(PrepaymentApproveDto dto) {
        final FinancePrepaymentOrderPo financePrepaymentOrderPo = financePrepaymentOrderDao.getOneByNo(dto.getPrepaymentOrderNo());
        if (null == financePrepaymentOrderPo) {
            throw new BizException("预付款单:{}不存在，请联系系统管理员！", dto.getPrepaymentOrderNo());
        }

        if (!PrepaymentOrderStatus.APPROVING.equals(financePrepaymentOrderPo.getPrepaymentOrderStatus())) {
            throw new ParamIllegalException("当前预付款单不处于{}状态，无法进行审批操作", PrepaymentOrderStatus.APPROVING.getRemark());
        }

        if (!GlobalContext.getUserKey().equals(financePrepaymentOrderPo.getCtrlUser())) {
            throw new ParamIllegalException("当前用户无法对预付款单:{}进行审批操作，请刷新后重试！", dto.getPrepaymentOrderNo());
        }
        mcRemoteService.approveWorkFlow(dto);
    }

    public void rejectWorkFlow(PrepaymentApproveDto dto) {
        final FinancePrepaymentOrderPo financePrepaymentOrderPo = financePrepaymentOrderDao.getOneByNo(dto.getPrepaymentOrderNo());
        if (null == financePrepaymentOrderPo) {
            throw new BizException("预付款单:{}不存在，请联系系统管理员！", dto.getPrepaymentOrderNo());
        }

        if (!PrepaymentOrderStatus.APPROVING.equals(financePrepaymentOrderPo.getPrepaymentOrderStatus())) {
            throw new ParamIllegalException("当前预付款单不处于{}状态，无法进行审批操作", PrepaymentOrderStatus.APPROVING.getRemark());
        }

        if (!GlobalContext.getUserKey().equals(financePrepaymentOrderPo.getCtrlUser())) {
            throw new ParamIllegalException("当前用户无法对预付款单:{}进行审批操作，请刷新后重试！", dto.getPrepaymentOrderNo());
        }
        mcRemoteService.rejectWorkFlow(dto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void fullPayment(PrepaymentFullPaymentDto dto) {
        String preOrderNo = dto.getPrepaymentOrderNo();
        FinancePrepaymentOrderPo preOrderPo = ParamValidUtils.requireNotNull(
                financePrepaymentOrderDao.getOneByNo(dto.getPrepaymentOrderNo()),
                StrUtil.format("预付款单{}不存在!请刷新页面后重试。", preOrderNo)
        );

        // 登录信息校验 & 处理人校验
        validateCtrlUser(preOrderPo);

        PrepaymentOrderStatus preOrderStatus = preOrderPo.getPrepaymentOrderStatus().toManualFullPayment();
        preOrderPo.setPrepaymentOrderStatus(preOrderStatus);
        financePrepaymentOrderDao.updateByIdVersion(preOrderPo);


        LogVersionBo logVersionBo = new LogVersionBo();
        logVersionBo.setKey("操作");
        logVersionBo.setValue("完结预付款单");
        logBaseService.simpleLog(
                LogBizModule.PREPAYMENT_LIST_SIMPLE, ScmConstant.PREPAYMENT_LOG_VERSION,
                preOrderPo.getPrepaymentOrderNo(), preOrderStatus.getRemark(), Collections.singletonList(logVersionBo)
        );
    }

    private void validateCtrlUser(FinancePrepaymentOrderPo financePrepaymentOrderPo) {
        String curUserKey = ParamValidUtils.requireNotBlank(GlobalContext.getUserKey(), "用户信息不存在，请先登录");
        String ctrlUser = financePrepaymentOrderPo.getCtrlUser();
        ParamValidUtils.requireEquals(curUserKey, ctrlUser,
                StrUtil.format("预付款单{}处理人不是当前登录账号，无法进行此操作",
                        financePrepaymentOrderPo.getPrepaymentOrderNo())
        );
    }
}














