package com.hete.supply.scm.server.scm.ibfs.service.biz;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.RecoOrderItemSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.RecoOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.entity.vo.RecoOrderExportItemVo;
import com.hete.supply.scm.api.scm.entity.vo.RecoOrderExportVo;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.common.util.ScmFormatUtil;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.McRemoteService;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.UdbRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.cost.dao.CostOfGoodsDao;
import com.hete.supply.scm.server.scm.cost.entity.po.CostOfGoodsPo;
import com.hete.supply.scm.server.scm.defect.dao.DefectHandlingDao;
import com.hete.supply.scm.server.scm.develop.dao.DevelopSampleOrderDao;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderPo;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.entity.po.DefectHandlingPo;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.feishu.dao.FeishuAuditOrderDao;
import com.hete.supply.scm.server.scm.feishu.service.base.AbstractApproveCreator;
import com.hete.supply.scm.server.scm.ibfs.config.ScmFinanceProp;
import com.hete.supply.scm.server.scm.ibfs.converter.FinanceRecoOrderConverter;
import com.hete.supply.scm.server.scm.ibfs.converter.FinanceRecoOrderItemConverter;
import com.hete.supply.scm.server.scm.ibfs.converter.FinanceRecoOrderItemInspectConverter;
import com.hete.supply.scm.server.scm.ibfs.converter.FinanceRecoOrderItemRelationConverter;
import com.hete.supply.scm.server.scm.ibfs.dao.*;
import com.hete.supply.scm.server.scm.ibfs.entity.bo.*;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.*;
import com.hete.supply.scm.server.scm.ibfs.entity.po.*;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.*;
import com.hete.supply.scm.server.scm.ibfs.enums.RecoOrderItemRelationType;
import com.hete.supply.scm.server.scm.ibfs.service.base.IbfsRecoOrderApproveApproveCreator;
import com.hete.supply.scm.server.scm.ibfs.service.base.PrepaymentBaseService;
import com.hete.supply.scm.server.scm.ibfs.service.base.RecoOrderBaseService;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderItemDao;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderItemPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.qc.dao.QcDetailDao;
import com.hete.supply.scm.server.scm.qc.dao.QcReceiveOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcReceiveOrderPo;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.settle.dao.DeductOrderDefectiveDao;
import com.hete.supply.scm.server.scm.settle.dao.DeductOrderPurchaseDao;
import com.hete.supply.scm.server.scm.settle.dao.DeductOrderQualityDao;
import com.hete.supply.scm.server.scm.settle.dao.SupplementOrderPurchaseDao;
import com.hete.supply.scm.server.scm.settle.entity.bo.DeductSupplementAndStatusBo;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.api.scm.entity.enums.SupplierStatus;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierBaseService;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.supply.udb.api.entity.vo.OrgVo;
import com.hete.supply.udb.api.entity.vo.UserVo;
import com.hete.supply.wms.api.entry.entity.dto.ReceiveOrderGetDto;
import com.hete.supply.wms.api.entry.entity.vo.ReceiveOrderForScmVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.consistency.core.service.ConsistencyService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.JacksonUtil;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ChenWenLong
 * @date 2024/5/13 14:22
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecoOrderBizService {

    private final FinanceRecoOrderDao financeRecoOrderDao;
    private final FinanceSettleOrderDao financeSettleOrderDao;
    private final SupplierDao supplierDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final AuthBaseService authBaseService;
    private final SupplierBaseService supplierBaseService;
    private final McRemoteService mcRemoteService;
    private final FinanceRecoOrderItemSkuDao financeRecoOrderItemSkuDao;
    private final FinanceRecoOrderItemDao financeRecoOrderItemDao;
    private final FinanceRecoOrderItemInspectDao financeRecoOrderItemInspectDao;
    private final PlmRemoteService plmRemoteService;
    private final RecoOrderBaseService recoOrderBaseService;
    private final LogBaseService logBaseService;
    private final UdbRemoteService udbRemoteService;
    private final IdGenerateService idGenerateService;
    private final FeishuAuditOrderDao feishuAuditOrderDao;
    private final PrepaymentBaseService prepaymentBaseService;
    private final PurchaseDeliverOrderDao purchaseDeliverOrderDao;
    private final PurchaseDeliverOrderItemDao purchaseDeliverOrderItemDao;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;
    private final DevelopSampleOrderDao developSampleOrderDao;
    private final PurchaseReturnOrderDao purchaseReturnOrderDao;
    private final PurchaseReturnOrderItemDao purchaseReturnOrderItemDao;
    private final CostOfGoodsDao costOfGoodsDao;
    private final DefectHandlingDao defectHandlingDao;
    private final WmsRemoteService wmsRemoteService;
    private final ConsistencyService consistencyService;
    private final FinanceRecoOrderItemRelationDao financeRecoOrderItemRelationDao;
    private final DeductOrderPurchaseDao deductOrderPurchaseDao;
    private final SupplementOrderPurchaseDao supplementOrderPurchaseDao;
    private final DeductOrderDefectiveDao deductOrderDefectiveDao;
    private final DeductOrderQualityDao deductOrderQualityDao;
    private final ScmFinanceProp scmFinanceProp;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final QcDetailDao qcDetailDao;
    private final QcReceiveOrderDao qcReceiveOrderDao;
    private final ScmImageBaseService scmImageBaseService;


    /**
     * 列表搜索
     *
     * @param dto:
     * @return PageInfo<RecoOrderSearchVo>
     * @author ChenWenLong
     * @date 2024/5/18 16:11
     */
    public CommonPageResult.PageInfo<RecoOrderSearchVo> searchRecoOrder(RecoOrderSearchDto dto) {
        return financeRecoOrderDao.searchRecoOrderPage(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
    }

    /**
     * 对账单导出
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/20 11:17
     */
    @Transactional(rollbackFor = Exception.class)
    public void exportRecoOrder(RecoOrderSearchDto dto) {
        Integer exportTotals = financeRecoOrderDao.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        consistencySendMqService.execSendMq(ScmExportHandler.class,
                new FileOperateMessageDto<>(GlobalContext.getUserKey(),
                        GlobalContext.getUsername(),
                        FileOperateBizType.SCM_FINANCE_RECO_ORDER_EXPORT.getCode(),
                        dto));
    }

    /**
     * 对账单详情导出
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/21 11:04
     */
    @Transactional(rollbackFor = Exception.class)
    public void exportRecoOrderItem(RecoOrderSearchDto dto) {
        Integer exportTotals = financeRecoOrderDao.getExportTotalsItem(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        consistencySendMqService.execSendMq(ScmExportHandler.class,
                new FileOperateMessageDto<>(GlobalContext.getUserKey(),
                        GlobalContext.getUsername(),
                        FileOperateBizType.SCM_FINANCE_RECO_ORDER_ITEM_EXPORT.getCode(),
                        dto));
    }

    /**
     * 对账单导出统计
     *
     * @param dto:
     * @return Integer
     * @author ChenWenLong
     * @date 2024/5/20 14:49
     */
    public Integer getExportTotals(RecoOrderSearchDto dto) {
        Integer exportTotals = financeRecoOrderDao.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        return exportTotals;
    }

    /**
     * 对账单导出列表
     *
     * @param dto:
     * @return CommonResult<ExportationListResultBo < RecoOrderExportVo>>
     * @author ChenWenLong
     * @date 2024/5/20 14:55
     */
    public CommonResult<ExportationListResultBo<RecoOrderExportVo>> getExportList(RecoOrderSearchDto dto) {
        ExportationListResultBo<RecoOrderExportVo> resultBo = new ExportationListResultBo<>();

        CommonPageResult.PageInfo<RecoOrderExportVo> exportList = financeRecoOrderDao.getExportList(
                PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<RecoOrderExportVo> records = exportList.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }

        List<String> financeSettleOrderNoList = records.stream()
                .map(RecoOrderExportVo::getFinanceSettleOrderNo)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());

        List<FinanceSettleOrderPo> financeSettleOrderPos
                = financeSettleOrderDao.findByOrderNos(financeSettleOrderNoList);
        final Map<String, FinanceSettleOrderPo> financeSettleOrderPoMap = financeSettleOrderPos.stream()
                .collect(Collectors.toMap(FinanceSettleOrderPo::getFinanceSettleOrderNo, order -> order,
                        (existing, replacement) -> existing));

        // 获取用户信息
        List<String> ctrlUserList = records.stream()
                .map(RecoOrderExportVo::getCtrlUser)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        Map<String, UserVo> userVoMap = udbRemoteService.getMapByUserCodeList(ctrlUserList);

        for (RecoOrderExportVo record : records) {
            record.setFinanceRecoOrderStatusName(record.getFinanceRecoOrderStatus()
                    .getRemark());
            record.setReconciliationCycleName(record.getReconciliationCycle()
                    .getRemark());

            if (userVoMap.containsKey(record.getCtrlUser())) {
                record.setHandleUsername(userVoMap.get(record.getCtrlUser())
                        .getUsername());
            }

            // 获取结算单信息
            if (financeSettleOrderPoMap.containsKey(record.getFinanceSettleOrderNo())) {
                FinanceSettleOrderPo financeSettleOrderPo = financeSettleOrderPoMap.get(
                        record.getFinanceSettleOrderNo());
                record.setFinanceSettleOrderStatusName(financeSettleOrderPo.getFinanceSettleOrderStatus()
                        .getRemark());
            }

            // 时间转换
            record.setCreateTimeStr(ScmTimeUtil.localDateTimeToStr(record.getCreateTime(), TimeZoneId.CN,
                    DatePattern.NORM_DATETIME_PATTERN));
            record.setReconciliationStartTimeStr(
                    ScmTimeUtil.localDateTimeToStr(record.getReconciliationStartTime(), TimeZoneId.CN,
                            DatePattern.NORM_DATETIME_PATTERN));
            record.setReconciliationEndTimeStr(
                    ScmTimeUtil.localDateTimeToStr(record.getReconciliationEndTime(), TimeZoneId.CN,
                            DatePattern.NORM_DATETIME_PATTERN));
            record.setCollectOrderTimeStr(ScmTimeUtil.localDateTimeToStr(record.getCollectOrderTime(), TimeZoneId.CN,
                    DatePattern.NORM_DATETIME_PATTERN));
            record.setConfirmTimeStr(ScmTimeUtil.localDateTimeToStr(record.getConfirmTime(), TimeZoneId.CN,
                    DatePattern.NORM_DATETIME_PATTERN));
            record.setSupplierConfirmTimeStr(
                    ScmTimeUtil.localDateTimeToStr(record.getSupplierConfirmTime(), TimeZoneId.CN,
                            DatePattern.NORM_DATETIME_PATTERN));
            record.setCompleteTimeStr(ScmTimeUtil.localDateTimeToStr(record.getCompleteTime(), TimeZoneId.CN,
                    DatePattern.NORM_DATETIME_PATTERN));

        }

        resultBo.setRowDataList(records);

        return CommonResult.success(resultBo);
    }

    /**
     * 对账单详情导出统计
     *
     * @param dto:
     * @return Integer
     * @author ChenWenLong
     * @date 2024/5/20 14:49
     */
    public Integer getExportTotalsItem(RecoOrderSearchDto dto) {
        Integer exportTotals = financeRecoOrderDao.getExportTotalsItem(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        return exportTotals;
    }

    /**
     * 对账单详情导出列表
     *
     * @param dto:
     * @return CommonResult<ExportationListResultBo < RecoOrderExportVo>>
     * @author ChenWenLong
     * @date 2024/5/20 14:55
     */
    public CommonResult<ExportationListResultBo<RecoOrderExportItemVo>> getExportListItem(RecoOrderSearchDto dto) {
        ExportationListResultBo<RecoOrderExportItemVo> resultBo = new ExportationListResultBo<>();

        CommonPageResult.PageInfo<RecoOrderExportItemVo> exportList = financeRecoOrderDao.getExportListItem(
                PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<RecoOrderExportItemVo> records = exportList.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }
        // 获取产品信息
        List<String> skuList = records.stream().map(RecoOrderExportItemVo::getSku)
                .distinct()
                .collect(Collectors.toList());
        Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        FinanceRecoOrderPo financeRecoOrderPo = financeRecoOrderDao.getOneByNo(records.get(0).getFinanceRecoOrderNo());


        List<Long> financeRecoOrderItemSkuIdList = records.stream().map(RecoOrderExportItemVo::getFinanceRecoOrderItemSkuId)
                .collect(Collectors.toList());

        // 查询采购单信息
        List<String> collectOrderNoList = records.stream().map(RecoOrderExportItemVo::getCollectOrderNo)
                .collect(Collectors.toList());

        List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByNoList(collectOrderNoList);
        List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList = purchaseDeliverOrderItemDao.getListByDeliverOrderNoList(collectOrderNoList);
        Map<String, PurchaseDeliverOrderPo> purchaseDeliverOrderPoMap = purchaseDeliverOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo, purchaseDeliverOrderPo -> purchaseDeliverOrderPo));

        List<String> purchaseChildOrderNoList = purchaseDeliverOrderPoList.stream()
                .map(PurchaseDeliverOrderPo::getPurchaseChildOrderNo)
                .distinct()
                .collect(Collectors.toList());
        List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList);
        Map<String, PurchaseChildOrderPo> purchaseChildOrderPoMap = purchaseChildOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, purchaseChildOrderPo -> purchaseChildOrderPo));

        List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNoList(purchaseChildOrderNoList);
        Map<String, BigDecimal> purchaseChildOrderPriceMap = purchaseChildOrderItemPoList.stream()
                .collect(Collectors.toMap(purchaseChildOrderItemPo -> purchaseChildOrderItemPo.getPurchaseChildOrderNo() + purchaseChildOrderItemPo.getSkuBatchCode(),
                        PurchaseChildOrderItemPo::getSettlePrice,
                        (existingValue, newValue) -> existingValue));

        // 查询退货数
        List<PurchaseReturnOrderPo> purchaseReturnOrderPoList = purchaseReturnOrderDao.getListByReturnBizNoList(collectOrderNoList);
        List<String> returnOrderNoList = purchaseReturnOrderPoList.stream().map(PurchaseReturnOrderPo::getReturnOrderNo)
                .collect(Collectors.toList());
        List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList = purchaseReturnOrderItemDao.getListByReturnNoList(returnOrderNoList);
        Map<String, Integer> purchaseReturnOrderCntMap = purchaseReturnOrderItemPoList.stream()
                .collect(Collectors.toMap(purchaseReturnOrderItemPo -> purchaseReturnOrderItemPo.getReturnOrderNo() + purchaseReturnOrderItemPo.getSkuBatchCode(),
                        PurchaseReturnOrderItemPo::getRealityReturnCnt,
                        Integer::sum));
        // 查询质检单
        List<QcReceiveOrderPo> qcReceiveOrderPoList = qcReceiveOrderDao.getListByScmBizNoList(collectOrderNoList);
        List<String> qcOrderNoList = qcReceiveOrderPoList.stream().map(QcReceiveOrderPo::getQcOrderNo)
                .distinct().collect(Collectors.toList());
        List<QcDetailPo> qcDetailPoList = qcDetailDao.getListByQcOrderNoList(qcOrderNoList);
        // 正品数量
        Map<String, Integer> qcDetailPoPassMap = qcDetailPoList.stream()
                .collect(Collectors.toMap(qcDetailPo -> qcDetailPo.getQcOrderNo() + qcDetailPo.getBatchCode(),
                        QcDetailPo::getPassAmount,
                        Integer::sum));
        // 次品数量
        Map<String, Integer> qcDetailPoNotPassMap = qcDetailPoList.stream()
                .collect(Collectors.toMap(qcDetailPo -> qcDetailPo.getQcOrderNo() + qcDetailPo.getBatchCode(),
                        QcDetailPo::getNotPassAmount,
                        Integer::sum));


        // 查询补扣款
        List<FinanceRecoOrderItemRelationPo> financeRecoOrderItemRelationPoList = financeRecoOrderItemRelationDao.getListByRecoOrderItemSkuIdList(financeRecoOrderItemSkuIdList);

        for (RecoOrderExportItemVo record : records) {
            record.setFinanceRecoOrderStatusName(record.getFinanceRecoOrderStatus().getRemark());
            record.setRecoOrderItemSkuStatusName(record.getRecoOrderItemSkuStatus().getRemark());
            record.setReconciliationCycleName(record.getReconciliationCycle().getRemark());
            if (null != record.getFinanceRecoFundType()) {
                record.setFinanceRecoFundTypeName(record.getFinanceRecoFundType().getRemark());
            }
            if (null != record.getCollectOrderType()) {
                record.setCollectOrderTypeName(record.getCollectOrderType().getRemark());
            }
            if (null != record.getFinanceRecoPayType()) {
                record.setFinanceRecoPayTypeName(record.getFinanceRecoPayType().getRemark());
            }

            if (FinanceRecoPayType.RECEIVABLE.equals(record.getFinanceRecoPayType())) {
                record.setPrice(record.getPrice().negate());
                record.setTotalPrice(record.getTotalPrice().negate());
            }

            record.setSkuEncode(skuEncodeMap.get(record.getSku()));

            // 时间转换
            record.setReconciliationStartTimeStr(
                    ScmTimeUtil.localDateTimeToStr(record.getReconciliationStartTime(), TimeZoneId.CN,
                            DatePattern.NORM_DATETIME_PATTERN));
            record.setReconciliationEndTimeStr(
                    ScmTimeUtil.localDateTimeToStr(record.getReconciliationEndTime(), TimeZoneId.CN,
                            DatePattern.NORM_DATETIME_PATTERN));
            record.setAssociationTimeStr(ScmTimeUtil.localDateTimeToStr(record.getAssociationTime(), TimeZoneId.CN,
                    DatePattern.NORM_DATETIME_PATTERN));

            // 补扣单
            List<FinanceRecoOrderItemRelationPo> relationPoList = financeRecoOrderItemRelationPoList.stream()
                    .filter(po -> RecoOrderItemRelationType.DEDUCT.equals(po.getRecoOrderItemRelationType())
                            || RecoOrderItemRelationType.SUPPLEMENT.equals(po.getRecoOrderItemRelationType()))
                    .filter(po -> po.getFinanceRecoOrderItemSkuId().equals(record.getFinanceRecoOrderItemSkuId()))
                    .collect(Collectors.toList());
            List<FinanceRecoOrderItemRelationPo> deductRelationPoList = relationPoList.stream()
                    .filter(relationPo -> RecoOrderItemRelationType.DEDUCT.equals(relationPo.getRecoOrderItemRelationType()))
                    .collect(Collectors.toList());
            List<FinanceRecoOrderItemRelationPo> supplementRelationPoList = relationPoList.stream()
                    .filter(relationPo -> RecoOrderItemRelationType.SUPPLEMENT.equals(relationPo.getRecoOrderItemRelationType()))
                    .collect(Collectors.toList());


            String businessNoJoining = relationPoList.stream()
                    .map(FinanceRecoOrderItemRelationPo::getBusinessNo)
                    .distinct()
                    .collect(Collectors.joining("、"));

            record.setDeductSupplementOrder(businessNoJoining);

            if (CollectionUtils.isNotEmpty(deductRelationPoList)) {
                record.setDeductSupplementType("扣款");
            }

            if (CollectionUtils.isNotEmpty(supplementRelationPoList)) {
                record.setDeductSupplementType("补款");
            }

            if (CollectionUtils.isNotEmpty(deductRelationPoList) && CollectionUtils.isNotEmpty(supplementRelationPoList)) {
                record.setDeductSupplementType("补扣款");
            }

            BigDecimal deductTotal = deductRelationPoList.stream()
                    .map(FinanceRecoOrderItemRelationPo::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal supplementTotal = supplementRelationPoList.stream()
                    .map(FinanceRecoOrderItemRelationPo::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            record.setDeductSupplementPrice(supplementTotal.subtract(deductTotal));

            // 采购单信息
            PurchaseDeliverOrderItemPo purchaseDeliverOrderItemPo = purchaseDeliverOrderItemPoList.stream()
                    .filter(itemPo -> itemPo.getPurchaseDeliverOrderNo().equals(record.getCollectOrderNo())
                            && itemPo.getSkuBatchCode().equals(record.getSkuBatchCode())
                            && itemPo.getPurchaseDeliverOrderItemId().equals(record.getCollectOrderItemId()))
                    .findFirst()
                    .orElse(null);
            if (null != purchaseDeliverOrderItemPo) {
                String purchaseDeliverOrderNo = purchaseDeliverOrderItemPo.getPurchaseDeliverOrderNo();
                PurchaseDeliverOrderPo purchaseDeliverOrderPo = purchaseDeliverOrderPoMap.get(purchaseDeliverOrderNo);
                if (null != purchaseDeliverOrderPo) {
                    record.setPurchaseChildOrderNo(purchaseDeliverOrderPo.getPurchaseChildOrderNo());
                    record.setWarehouseName(purchaseDeliverOrderPo.getWarehouseName());
                    record.setPurchaseDeliverTime(ScmTimeUtil.localDateTimeToStr(purchaseDeliverOrderPo.getDeliverTime(), TimeZoneId.CN,
                            DatePattern.NORM_DATETIME_PATTERN));
                    record.setOnShelvesTime(ScmTimeUtil.localDateTimeToStr(purchaseDeliverOrderPo.getWarehousingTime(), TimeZoneId.CN,
                            DatePattern.NORM_DATETIME_PATTERN));

                    BigDecimal bigDecimal = purchaseChildOrderPriceMap.get(purchaseDeliverOrderPo.getPurchaseChildOrderNo() + record.getSkuBatchCode());
                    if (null == bigDecimal) {
                        bigDecimal = BigDecimal.ZERO;
                    }
                    record.setPurchaseSettlePrice(bigDecimal);
                }
                if (null != purchaseDeliverOrderPo && purchaseChildOrderPoMap.containsKey(purchaseDeliverOrderPo.getPurchaseChildOrderNo())) {
                    PurchaseBizType purchaseBizType = purchaseChildOrderPoMap.get(purchaseDeliverOrderPo.getPurchaseChildOrderNo()).getPurchaseBizType();
                    record.setPurchaseBizType(purchaseBizType != null ? purchaseBizType.getRemark() : null);

                }

                record.setDeliveryAmount(purchaseDeliverOrderItemPo.getDeliverCnt());
                record.setReceiveAmount(purchaseDeliverOrderItemPo.getReceiptCnt());
                record.setDefectiveGoodsCnt(purchaseDeliverOrderItemPo.getDefectiveGoodsCnt());
                record.setOnShelvesAmount(purchaseDeliverOrderItemPo.getQualityGoodsCnt());
                record.setSettleTotalPrice(record.getPurchaseSettlePrice().multiply(new BigDecimal(record.getOnShelvesAmount())));

                // 退货数
                List<PurchaseReturnOrderPo> purchaseReturnOrderPos = purchaseReturnOrderPoList.stream()
                        .filter(po -> po.getReturnBizNo().equals(record.getCollectOrderNo()))
                        .collect(Collectors.toList());
                int realityReturnCnt = 0;
                for (PurchaseReturnOrderPo purchaseReturnOrderPo : purchaseReturnOrderPos) {
                    Integer realityReturnCntSingle = purchaseReturnOrderCntMap.get(purchaseReturnOrderPo.getReturnOrderNo() + record.getSkuBatchCode());
                    if (null != realityReturnCntSingle) {
                        realityReturnCnt += realityReturnCntSingle;
                    }
                }
                record.setRealityReturnCnt(realityReturnCnt);

                // 正品数
                List<QcReceiveOrderPo> qcReceiveOrderPos = qcReceiveOrderPoList.stream()
                        .filter(po -> po.getScmBizNo().equals(record.getCollectOrderNo()))
                        .collect(Collectors.toList());
                int qualityGoodsCnt = 0;
                for (QcReceiveOrderPo qcReceiveOrderPo : qcReceiveOrderPos) {
                    Integer qualityGoodsCntSingle = qcDetailPoPassMap.get(qcReceiveOrderPo.getQcOrderNo() + record.getSkuBatchCode());
                    if (null != qualityGoodsCntSingle) {
                        qualityGoodsCnt += qualityGoodsCntSingle;
                    }
                }
                record.setQualityGoodsCnt(qualityGoodsCnt);

                // 次品数
                int defectiveGoodsCnt = 0;
                for (QcReceiveOrderPo qcReceiveOrderPo : qcReceiveOrderPos) {
                    Integer defectiveGoodsCntSingle = qcDetailPoNotPassMap.get(qcReceiveOrderPo.getQcOrderNo() + record.getSkuBatchCode());
                    if (null != defectiveGoodsCntSingle) {
                        defectiveGoodsCnt += defectiveGoodsCntSingle;
                    }
                }
                record.setDefectiveGoodsCnt(defectiveGoodsCnt);

            }

        }

        // 最后一页追加数据
        if (exportList.getPageNo() == exportList.getTotalPage()) {
            RecoOrderExportItemVo recoOrderExportItemVo = new RecoOrderExportItemVo();
            recoOrderExportItemVo.setFinanceRecoOrderNo("对账总金额");
            recoOrderExportItemVo.setTotalPrice(financeRecoOrderPo.getSettlePrice());
            records.add(recoOrderExportItemVo);
        }
        resultBo.setRowDataList(records);

        return CommonResult.success(resultBo);
    }

    /**
     * 对账单列表获取供应商
     *
     * @param :
     * @return List<SupplierVo>
     * @author ChenWenLong
     * @date 2024/5/21 15:48
     */
    public List<SupplierVo> getRecoOrderSupplier(RecoOrderSearchDto dto) {
        // 对账单对应的供应商
        final List<String> financeRecoOrderSupplierCodeList = financeRecoOrderDao.getSupplierList(dto);
        final List<SupplierPo> supplierPoList = supplierBaseService.getBySupplierCodeList(
                financeRecoOrderSupplierCodeList);
        return Optional.ofNullable(supplierPoList)
                .orElse(new ArrayList<>())
                .stream()
                .map(po -> {
                    final SupplierVo supplierVo = new SupplierVo();
                    supplierVo.setSupplierCode(po.getSupplierCode());
                    supplierVo.setSupplierName(po.getSupplierName());
                    supplierVo.setSupplierGrade(po.getSupplierGrade());
                    return supplierVo;
                })
                .sorted(Comparator.comparing(SupplierVo::getSupplierGrade))
                .collect(Collectors.toList());

    }

    /**
     * 批量转交
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/21 16:40
     */
    public void batchTransferRecoOrder(RecoOrderTransferDto dto) {
        final List<RecoOrderTransferItemDto> recoOrderTransferItemList = dto.getRecoOrderTransferItemList();
        final List<String> financeRecoOrderNoList = recoOrderTransferItemList.stream()
                .map(RecoOrderTransferItemDto::getFinanceRecoOrderNo)
                .distinct()
                .collect(Collectors.toList());
        List<FinanceRecoOrderPo> financeRecoOrderPoList = financeRecoOrderDao.getListByNoList(financeRecoOrderNoList);
        if (financeRecoOrderPoList.size() != financeRecoOrderNoList.size()) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }


        // 验证转交人
        final UserVo userVo = udbRemoteService.getByUserCode(dto.getTransferUser());
        if (null == userVo) {
            throw new BizException("转交人数据不存在，请重新选择其他转交人", dto.getTransferUser());
        }

        final Map<String, RecoOrderTransferItemDto> financeRecoOrderDtoMap = recoOrderTransferItemList.stream()
                .collect(Collectors.toMap(RecoOrderTransferItemDto::getFinanceRecoOrderNo,
                        Function.identity()));


        // 验证版本号
        for (RecoOrderTransferItemDto recoOrderTransferItemDto : recoOrderTransferItemList) {
            financeRecoOrderPoList.stream()
                    .filter(po -> po.getFinanceRecoOrderNo()
                            .equals(recoOrderTransferItemDto.getFinanceRecoOrderNo()))
                    .findFirst()
                    .ifPresent(po -> {
                        Assert.isTrue(po.getVersion()
                                        .equals(recoOrderTransferItemDto.getVersion()),
                                () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
                        // 校验操作人是否为当前用户
                        if (!GlobalContext.getUserKey()
                                .equals(po.getCtrlUser())) {
                            throw new ParamIllegalException("当前登录用户无法操作该对账单:{}，请刷新后重试",
                                    po.getFinanceRecoOrderNo());
                        }
                        // 状态
                        if (FinanceRecoOrderStatus.verifyTransfer(po.getFinanceRecoOrderStatus())) {
                            throw new ParamIllegalException("当前对账单:{}状态:{}无法进行转交操作，请刷新后重试！",
                                    po.getFinanceRecoOrderNo(),
                                    po.getFinanceRecoOrderStatus()
                                            .getRemark());
                        }

                        final RecoOrderTransferItemDto itemDto = financeRecoOrderDtoMap.get(po.getFinanceRecoOrderNo());
                        if (null == itemDto) {
                            throw new BizException("查找不到对账单:{}入参数据，数据异常，请联系系统管理员！",
                                    po.getFinanceRecoOrderNo());
                        }

                        // 推送飞书转交判断
                        if (FinanceRecoOrderStatus.UNDER_REVIEW.equals(po.getFinanceRecoOrderStatus())) {
                            Assert.isTrue(StringUtils.isNotBlank(po.getWorkflowNo()), () -> new ParamIllegalException(
                                    "对账单:{}获取不到工作流单号，数据异常，请联系系统管理员！",
                                    po.getFinanceRecoOrderNo()));
                            Assert.isTrue(StringUtils.isNotBlank(itemDto.getTaskId()), () -> new ParamIllegalException(
                                    "对账单:{}的入参任务id不能为空，数据异常，请联系系统管理员！",
                                    po.getFinanceRecoOrderNo()));
                        }

                    });
        }

        // 先处理简单的数据变更类型
        final List<FinanceRecoOrderPo> simpleTransferList = financeRecoOrderPoList.stream()
                .filter(po -> !FinanceRecoOrderStatus.UNDER_REVIEW.equals(po.getFinanceRecoOrderStatus()))
                .collect(Collectors.toList());
        recoOrderBaseService.simpleTransfer(simpleTransferList, dto, userVo);

        // 再处理飞书审批相关
        List<String> failRecoOrderNoList = new ArrayList<>();
        financeRecoOrderPoList.stream()
                .filter(po -> FinanceRecoOrderStatus.UNDER_REVIEW.equals(po.getFinanceRecoOrderStatus()))
                .forEach(po -> recoOrderBaseService.feiShuTransfer(financeRecoOrderDtoMap, po, userVo, dto,
                        failRecoOrderNoList));

        if (CollectionUtils.isNotEmpty(failRecoOrderNoList)) {
            throw new ParamIllegalException(
                    "转交条件：当前登录账号为单据处理人，且转交对象非审批节点前置处理人。对账单转交失败单号：{}",
                    failRecoOrderNoList);
        }

    }

    /**
     * 对账单详情
     *
     * @param dto:
     * @return String
     * @author ChenWenLong
     * @date 2024/5/21 19:24
     */
    public RecoOrderDetailVo recoOrderDetail(RecoOrderNoDto dto) {
        final FinanceRecoOrderPo financeRecoOrderPo = financeRecoOrderDao.getOneByNo(dto.getFinanceRecoOrderNo());
        if (null == financeRecoOrderPo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }

        String userKey = GlobalContext.getUserKey();

        // 获取供应商信息
        SupplierPo supplierPo = supplierDao.getBySupplierCode(financeRecoOrderPo.getSupplierCode());

        // 获取明细sku详情
        List<FinanceRecoOrderItemSkuPo> financeRecoOrderItemSkuPoList
                = financeRecoOrderItemSkuDao.getListByFinanceRecoOrderNo(financeRecoOrderPo.getFinanceRecoOrderNo());


        // 处理人信息
        final UserVo ctrlUserVo = udbRemoteService.getByUserCode(financeRecoOrderPo.getCtrlUser());


        // 判断按钮是否显示作废按钮
        FinanceSettleOrderPo financeSettleOrderPo = financeSettleOrderDao.findByOrderNo(
                financeRecoOrderPo.getFinanceSettleOrderNo());
        RecoOrderButtonVo recoOrderButtonVo = new RecoOrderButtonVo();
        recoOrderButtonVo.setInvalidButton(BooleanType.FALSE);
        recoOrderButtonVo.setSubmitButton(BooleanType.FALSE);
        recoOrderButtonVo.setConfirmAllButton(BooleanType.FALSE);
        if (FinanceRecoOrderStatus.WAIT_SUBMIT.equals(financeRecoOrderPo.getFinanceRecoOrderStatus())
                && userKey.equals(financeRecoOrderPo.getCtrlUser())) {
            if (null == financeSettleOrderPo) {
                recoOrderButtonVo.setInvalidButton(BooleanType.TRUE);
            }
            if (null != financeSettleOrderPo
                    && FinanceSettleOrderStatus.INVALIDATE.equals(financeSettleOrderPo.getFinanceSettleOrderStatus())) {
                recoOrderButtonVo.setInvalidButton(BooleanType.TRUE);
            }
        }

        // 跟单提交按钮、确认所有条目按钮
        if (FinanceRecoOrderStatus.WAIT_SUBMIT.equals(financeRecoOrderPo.getFinanceRecoOrderStatus())
                && userKey.equals(financeRecoOrderPo.getCtrlUser())) {
            List<FinanceRecoOrderItemSkuPo> financeRecoOrderItemSkuPoFilter = financeRecoOrderItemSkuPoList.stream()
                    .filter(financeRecoOrderItemSkuPo -> !RecoOrderItemSkuStatus.CONFIRMED.equals(
                            financeRecoOrderItemSkuPo.getRecoOrderItemSkuStatus()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(financeRecoOrderItemSkuPoFilter)) {
                recoOrderButtonVo.setSubmitButton(BooleanType.TRUE);
            }
            if (CollectionUtils.isNotEmpty(financeRecoOrderItemSkuPoFilter)) {
                recoOrderButtonVo.setConfirmAllButton(BooleanType.TRUE);
            }
        }


        return FinanceRecoOrderConverter.poToVo(financeRecoOrderPo,
                supplierPo,
                recoOrderButtonVo,
                financeRecoOrderItemSkuPoList,
                financeSettleOrderPo,
                ctrlUserVo);
    }

    /**
     * 新建款项条目
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/22 17:12
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.RECO_ORDER_CREATE_ITEM, key = "#dto.financeRecoOrderNo", waitTime = 1,
            leaseTime = -1, exceptionDesc = "对账单新建款项条目正在处理中，请稍后再试！")
    public void createRecoOrderItem(RecoOrderItemCreateDto dto) {
        FinanceRecoOrderPo financeRecoOrderPo = financeRecoOrderDao.getOneByNoAndVersion(dto.getFinanceRecoOrderNo(),
                dto.getVersion());
        Assert.notNull(financeRecoOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        String userKey = GlobalContext.getUserKey();
        if (!FinanceRecoOrderStatus.PENDING_ORDER.equals(financeRecoOrderPo.getFinanceRecoOrderStatus())) {
            throw new ParamIllegalException("对账单状态:{}中，无法进行此操作！",
                    financeRecoOrderPo.getFinanceRecoOrderStatus()
                            .getRemark());
        }
        if (!userKey.equals(financeRecoOrderPo.getCtrlUser())) {
            throw new ParamIllegalException("对账单:{}的处理人不是当前登录账号，无法进行此操作！",
                    financeRecoOrderPo.getFinanceRecoOrderNo());
        }
        BigDecimal price = dto.getPrice();
        BigDecimal totalPrice = price.multiply(new BigDecimal(dto.getNum()));
        if (totalPrice.compareTo(dto.getTotalPrice()) != 0) {
            throw new BizException("款项的总金额计算错误，请联系系统管理员！");
        }
        String collectOrderNo = idGenerateService.getConfuseCode(ScmConstant.FINANCE_RECO_ORDER_ITEM_NO_PREFIX,
                TimeType.CN_DAY, ConfuseLength.L_4);
        FinanceRecoOrderItemPo financeRecoOrderItemPo = FinanceRecoOrderItemConverter.dtoToPo(financeRecoOrderPo, dto,
                collectOrderNo);
        financeRecoOrderItemDao.insert(financeRecoOrderItemPo);
        FinanceRecoOrderItemSkuPo financeRecoOrderItemSkuPo = FinanceRecoOrderItemConverter.itemPoToItemSkuPo(
                financeRecoOrderItemPo, dto, collectOrderNo);
        financeRecoOrderItemSkuDao.insert(financeRecoOrderItemSkuPo);

        // 增加附件
        scmImageBaseService.insertBatchImage(dto.getFileCodeList(), ImageBizType.RECO_ORDER_ITEM_SKU_FILE, financeRecoOrderItemSkuPo.getFinanceRecoOrderItemSkuId());

        // 更新对账单价格和PO
        recoOrderBaseService.updateRecoOrderPrice(financeRecoOrderPo);
    }

    /**
     * 对账单详情获取详情单据列表
     *
     * @param dto:
     * @return PageInfo<RecoOrderItemSearchVo>
     * @author ChenWenLong
     * @date 2024/5/22 19:33
     */
    public CommonPageResult.PageInfo<RecoOrderItemSearchVo> searchRecoOrderItem(RecoOrderItemSearchDto dto) {
        CommonPageResult.PageInfo<RecoOrderItemSearchVo> pageResult = financeRecoOrderItemDao.searchRecoOrderItemPage(
                PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<RecoOrderItemSearchVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageResult;
        }

        // sku品类信息
        List<String> skuList = records.stream()
                .map(RecoOrderItemSearchVo::getSku)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        Map<String, String> skuSecondCategoriesMap = plmRemoteService.getSkuSecondCategoriesCnMapBySkuList(skuList);

        // 获取产品名称
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        List<Long> financeRecoOrderItemSkuIdList = records.stream()
                .map(RecoOrderItemSearchVo::getFinanceRecoOrderItemSkuId)
                .collect(Collectors.toList());

        // 获取检验信息
        List<FinanceRecoOrderItemInspectPo> financeRecoOrderItemInspectPoList
                = financeRecoOrderItemInspectDao.getListByRecoOrderItemSkuIdList(financeRecoOrderItemSkuIdList);

        records.forEach(record -> {
            record.setCategoryName(skuSecondCategoriesMap.get(record.getSku()));
            record.setSkuEncode(skuEncodeMap.get(record.getSku()));

            List<RecoOrderItemInspectVo> recoOrderItemInspectList = financeRecoOrderItemInspectPoList.stream()
                    .filter(po -> po.getFinanceRecoOrderItemSkuId()
                            .equals(record.getFinanceRecoOrderItemSkuId()))
                    .map(po -> {
                        RecoOrderItemInspectVo recoOrderItemInspectVo = new RecoOrderItemInspectVo();
                        recoOrderItemInspectVo.setRecoOrderInspectType(po.getRecoOrderInspectType());
                        recoOrderItemInspectVo.setOriginalValue(po.getOriginalValue());
                        recoOrderItemInspectVo.setInspectValue(po.getInspectValue());
                        recoOrderItemInspectVo.setInspectResult(po.getInspectResult());
                        if (null != record.getCollectOrderType()) {
                            recoOrderItemInspectVo.setInspectResultRule(record.getCollectOrderType().getInspectRule(po.getRecoOrderInspectType()));
                        }
                        return recoOrderItemInspectVo;
                    }).collect(Collectors.toList());

            Integer inspectTotalNum = (int) financeRecoOrderItemInspectPoList.stream()
                    .filter(po -> po.getFinanceRecoOrderItemSkuId()
                            .equals(record.getFinanceRecoOrderItemSkuId()))
                    .filter(financeRecoOrderItemInspectPo -> BooleanType.FALSE.equals(
                            financeRecoOrderItemInspectPo.getInspectResult()))
                    .count();
            record.setInspectTotalNum(inspectTotalNum);
            record.setRecoOrderItemInspectList(recoOrderItemInspectList);
        });
        return pageResult;
    }

    /**
     * 获取款项类型关联收单类型
     *
     * @param dto:
     * @return List<FinanceRecoFundTypeVo>
     * @author ChenWenLong
     * @date 2024/5/23 10:25
     */
    public List<FinanceRecoFundTypeVo> getCollectOrderType(FinanceRecoFundTypeDto dto) {
        List<CollectOrderType> collectOrderTypeList = dto.getFinanceRecoFundType()
                .getCollectOrderTypeList();
        return Optional.ofNullable(collectOrderTypeList)
                .orElse(new ArrayList<>())
                .stream()
                .map(collectOrderType -> {
                    FinanceRecoFundTypeVo financeRecoFundTypeVo = new FinanceRecoFundTypeVo();
                    financeRecoFundTypeVo.setCollectOrderType(collectOrderType);
                    return financeRecoFundTypeVo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 对账单详情获取详情单据汇总数据
     *
     * @param dto:
     * @return Integer
     * @author ChenWenLong
     * @date 2024/5/23 10:43
     */
    public RecoOrderItemTotalVo getRecoOrderItemTotal(RecoOrderItemSearchDto dto) {

        RecoOrderItemTotalVo recoOrderItemTotalVo = financeRecoOrderItemDao.getRecoOrderItemTotal(dto);

        Integer receiptTotalCount = financeRecoOrderItemDao.getRecoOrderItemTotalGroup(dto);
        recoOrderItemTotalVo.setReceiptTotalCount(receiptTotalCount);

        Integer skuTotalCount = financeRecoOrderItemDao.getRecoOrderItemTotalGroupSku(dto);
        recoOrderItemTotalVo.setSkuTotalCount(skuTotalCount);

        List<String> skuList = financeRecoOrderItemDao.getRecoOrderItemTotalGroupSkuList(dto);
        Map<String, String> skuSecondCategoriesMap = plmRemoteService.getSkuSecondCategoriesCnMapBySkuList(skuList);
        // 获取SKU的类名
        Set<String> uniqueValues = new HashSet<>(skuSecondCategoriesMap.values());
        recoOrderItemTotalVo.setTypeTotalCount(uniqueValues.size());

        return recoOrderItemTotalVo;
    }

    /**
     * 确认所有条目
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/23 11:21
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmAllRecoOrderItem(RecoOrderItemSkuIdListDto dto) {
        List<RecoOrderItemSkuIdAndVersionDto> recoOrderItemSkuIdAndVersionList = dto.getRecoOrderItemSkuIdAndVersionList();

        List<Long> financeRecoOrderItemSkuIdList = recoOrderItemSkuIdAndVersionList.stream()
                .map(RecoOrderItemSkuIdAndVersionDto::getFinanceRecoOrderItemSkuId)
                .collect(Collectors.toList());
        List<FinanceRecoOrderItemSkuPo> financeRecoOrderItemSkuPoList = financeRecoOrderItemSkuDao.getListByIdList(financeRecoOrderItemSkuIdList);
        Assert.notEmpty(financeRecoOrderItemSkuPoList, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        // 验证版本号
        for (RecoOrderItemSkuIdAndVersionDto recoOrderItemSkuIdAndVersionDto : recoOrderItemSkuIdAndVersionList) {
            financeRecoOrderItemSkuPoList.stream()
                    .filter(po -> po.getFinanceRecoOrderItemSkuId().equals(recoOrderItemSkuIdAndVersionDto.getFinanceRecoOrderItemSkuId()))
                    .findFirst()
                    .ifPresent(po -> Assert.isTrue(po.getVersion().equals(recoOrderItemSkuIdAndVersionDto.getVersion()),
                            () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！")));
        }

        this.batchConfirmRecoOrderItem(financeRecoOrderItemSkuPoList);

    }

    /**
     * 确认条目
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/23 11:40
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmRecoOrderItem(RecoOrderItemSkuIdAndVersionDto dto) {
        FinanceRecoOrderItemSkuPo financeRecoOrderItemSkuPo = financeRecoOrderItemSkuDao.getByIdVersion(
                dto.getFinanceRecoOrderItemSkuId(), dto.getVersion());
        Assert.notNull(financeRecoOrderItemSkuPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));

        this.batchConfirmRecoOrderItem(List.of(financeRecoOrderItemSkuPo));


    }

    /**
     * 删除条目
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/23 13:42
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.RECO_ORDER_DEL_ITEM, key = "#dto.financeRecoOrderNo", waitTime = 1,
            leaseTime = -1, exceptionDesc = "对账单删除款项条目正在处理中，请稍后再试！")
    public void delRecoOrderItem(RecoOrderDelItemSkuDto dto) {
        FinanceRecoOrderItemSkuPo financeRecoOrderItemSkuPo = financeRecoOrderItemSkuDao.getByIdVersion(
                dto.getRecoOrderItemSkuIdAndVersion()
                        .getFinanceRecoOrderItemSkuId(), dto.getRecoOrderItemSkuIdAndVersion()
                        .getVersion());
        Assert.notNull(financeRecoOrderItemSkuPo,
                () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        FinanceRecoOrderPo financeRecoOrderPo = financeRecoOrderDao.getOneByNoAndVersion(dto.getFinanceRecoOrderNo(),
                dto.getVersion());
        Assert.notNull(financeRecoOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        String userKey = GlobalContext.getUserKey();
        if (!FinanceRecoOrderStatus.PENDING_ORDER.equals(financeRecoOrderPo.getFinanceRecoOrderStatus())) {
            throw new ParamIllegalException("对账单状态非{}，无法进行此操作，请刷新页面后重试！",
                    FinanceRecoOrderStatus.PENDING_ORDER.getRemark());
        }
        if (!userKey.equals(financeRecoOrderPo.getCtrlUser())) {
            throw new ParamIllegalException("对账单:{}的处理人不是当前登录账号，无法进行此操作！",
                    financeRecoOrderPo.getFinanceRecoOrderNo());
        }
        if (!(FinanceRecoFundType.CUSTOM_PAYMENT.equals(financeRecoOrderItemSkuPo.getFinanceRecoFundType())
                || FinanceRecoFundType.OTHER_PAYMENT.equals(financeRecoOrderItemSkuPo.getFinanceRecoFundType()))) {
            throw new ParamIllegalException("该数据非{}/{}类型，无法进行此操作！",
                    FinanceRecoFundType.CUSTOM_PAYMENT.getRemark(),
                    FinanceRecoFundType.OTHER_PAYMENT.getRemark());
        }

        financeRecoOrderItemSkuDao.removeById(financeRecoOrderItemSkuPo.getFinanceRecoOrderItemSkuId());

        FinanceRecoOrderItemPo financeRecoOrderItemPo = financeRecoOrderItemDao.getById(
                financeRecoOrderItemSkuPo.getFinanceRecoOrderItemId());
        if (null != financeRecoOrderItemPo) {
            financeRecoOrderItemDao.removeById(financeRecoOrderItemPo.getFinanceRecoOrderItemId());
        }

        // 更新对账单价格和PO
        recoOrderBaseService.updateRecoOrderPrice(financeRecoOrderPo);

    }

    /**
     * 批量备注
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/23 15:23
     */
    @Transactional(rollbackFor = Exception.class)
    public void noteRecoOrderItem(RecoOrderItemSkuIdBatchDto dto) {
        List<RecoOrderItemSkuIdAndVersionDto> recoOrderItemSkuDtoList = Optional.ofNullable(
                        dto.getRecoOrderItemSkuIdAndVersionList())
                .orElse(new ArrayList<>());
        List<Long> itemSkuIdList = recoOrderItemSkuDtoList.stream()
                .map(RecoOrderItemSkuIdAndVersionDto::getFinanceRecoOrderItemSkuId)
                .collect(Collectors.toList());
        String userKey = GlobalContext.getUserKey();

        List<FinanceRecoOrderItemSkuPo> financeRecoOrderItemSkuPoList = financeRecoOrderItemSkuDao.getListByIdList(
                itemSkuIdList);
        Assert.notEmpty(financeRecoOrderItemSkuPoList,
                () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        // 验证版本号
        for (RecoOrderItemSkuIdAndVersionDto recoOrderItemSkuIdAndVersionDto : recoOrderItemSkuDtoList) {
            financeRecoOrderItemSkuPoList.stream()
                    .filter(po -> po.getFinanceRecoOrderItemSkuId()
                            .equals(recoOrderItemSkuIdAndVersionDto.getFinanceRecoOrderItemSkuId()))
                    .findFirst()
                    .ifPresent(po -> Assert.isTrue(po.getVersion()
                                    .equals(recoOrderItemSkuIdAndVersionDto.getVersion()),
                            () -> new ParamIllegalException(
                                    "数据已被修改或删除，请刷新页面后重试！")));
        }

        // 获取对账单
        List<String> financeRecoOrderNoList = financeRecoOrderItemSkuPoList.stream()
                .map(FinanceRecoOrderItemSkuPo::getFinanceRecoOrderNo)
                .distinct()
                .collect(Collectors.toList());
        List<FinanceRecoOrderPo> financeRecoOrderPoList = financeRecoOrderDao.getListByNoList(financeRecoOrderNoList);
        Assert.notEmpty(financeRecoOrderPoList,
                () -> new BizException("查询不到对应对账单信息数据异常，请联系系统管理员！"));
        for (FinanceRecoOrderPo financeRecoOrderPo : financeRecoOrderPoList) {
            if (!userKey.equals(financeRecoOrderPo.getCtrlUser())) {
                throw new ParamIllegalException("对账单:{}的处理人不是当前登录账号，无法进行此操作！",
                        financeRecoOrderPo.getFinanceRecoOrderNo());
            }
        }

        // 更新备注
        for (FinanceRecoOrderItemSkuPo financeRecoOrderItemSkuPo : financeRecoOrderItemSkuPoList) {
            financeRecoOrderItemSkuPo.setRemarks(dto.getRemarks());
        }
        financeRecoOrderItemSkuDao.updateBatchByIdVersion(financeRecoOrderItemSkuPoList);

    }

    /**
     * 作废
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/23 15:30
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelRecoOrder(RecoOrderNoAndVersionDto dto) {
        FinanceRecoOrderPo financeRecoOrderPo = financeRecoOrderDao.getOneByNoAndVersion(dto.getFinanceRecoOrderNo(),
                dto.getVersion());
        Assert.notNull(financeRecoOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        String userKey = GlobalContext.getUserKey();
        if (!FinanceRecoOrderStatus.WAIT_SUBMIT.equals(financeRecoOrderPo.getFinanceRecoOrderStatus())) {
            throw new ParamIllegalException("对账单状态非{}，无法进行此操作，请刷新页面后重试！",
                    FinanceRecoOrderStatus.WAIT_SUBMIT.getRemark());
        }
        if (!userKey.equals(financeRecoOrderPo.getCtrlUser())) {
            throw new ParamIllegalException("对账单:{}的处理人不是当前登录账号，无法进行此操作！",
                    financeRecoOrderPo.getFinanceRecoOrderNo());
        }

        // 获取结算单信息
        if (StringUtils.isNotBlank(financeRecoOrderPo.getFinanceSettleOrderNo())) {
            FinanceSettleOrderPo financeSettleOrderPo = financeSettleOrderDao.findByOrderNo(
                    financeRecoOrderPo.getFinanceSettleOrderNo());
            Assert.notNull(financeSettleOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
            if (!FinanceSettleOrderStatus.INVALIDATE.equals(financeSettleOrderPo.getFinanceSettleOrderStatus())) {
                throw new ParamIllegalException("当前对账单关联结算单:{}，无法作废！",
                        financeSettleOrderPo.getFinanceSettleOrderNo());
            }
        }

        // 更新数据
        FinanceRecoOrderStatus financeRecoOrderStatus = financeRecoOrderPo.getFinanceRecoOrderStatus()
                .toDelete();
        financeRecoOrderPo.setFinanceRecoOrderStatus(financeRecoOrderStatus);
        financeRecoOrderDao.updateByIdVersion(financeRecoOrderPo);

        // 日志
        final LogVersionBo logVersionBo = new LogVersionBo();
        logVersionBo.setKey("操作");
        logVersionBo.setValue("作废对账单");
        logBaseService.simpleLog(LogBizModule.FINANCE_RECO_ORDER_STATUS, ScmConstant.RECO_ORDER_LOG_VERSION,
                financeRecoOrderPo.getFinanceRecoOrderNo(), financeRecoOrderStatus.getRemark(),
                Collections.singletonList(logVersionBo));

        // 如果是存在关联预付款单时逻辑处理
        List<FinanceRecoOrderItemSkuPo> financeRecoOrderItemSkuPoList
                = financeRecoOrderItemSkuDao.getListByFinanceRecoOrderNo(financeRecoOrderPo.getFinanceRecoOrderNo());
        List<String> collectOrderNoList = financeRecoOrderItemSkuPoList.stream()
                .filter(financeRecoOrderItemSkuPo -> FinanceRecoFundType.PREPAYMENTS.equals(
                        financeRecoOrderItemSkuPo.getFinanceRecoFundType()))
                .map(FinanceRecoOrderItemSkuPo::getCollectOrderNo)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collectOrderNoList)) {
            PrepaymentNoListDto prepaymentNoListDto = new PrepaymentNoListDto();
            prepaymentNoListDto.setPrepaymentOrderNoList(collectOrderNoList);
            prepaymentBaseService.resetPrepayment(prepaymentNoListDto);
        }


        // 重新生成新对账单(重新生成对账单放在最后处理)
        this.recreateFinanceRecoOrder(financeRecoOrderPo);

    }

    /**
     * 跟单提交
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/23 16:22
     */
    @Transactional(rollbackFor = Exception.class)
    public void submitRecoOrder(RecoOrderSubmitDto dto) {
        FinanceRecoOrderPo financeRecoOrderPo = financeRecoOrderDao.getOneByNoAndVersion(dto.getFinanceRecoOrderNo(),
                dto.getVersion());
        Assert.notNull(financeRecoOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        String userKey = GlobalContext.getUserKey();
        String username = GlobalContext.getUsername();
        if (!FinanceRecoOrderStatus.WAIT_SUBMIT.equals(financeRecoOrderPo.getFinanceRecoOrderStatus())) {
            throw new ParamIllegalException("对账单状态非{}，无法进行此操作，请刷新页面后重试！",
                    FinanceRecoOrderStatus.WAIT_SUBMIT.getRemark());
        }
        if (!userKey.equals(financeRecoOrderPo.getCtrlUser())) {
            throw new ParamIllegalException("对账单:{}的处理人不是当前登录账号，无法进行此操作！",
                    financeRecoOrderPo.getFinanceRecoOrderNo());
        }

        // 获取明细数据
        List<FinanceRecoOrderItemSkuPo> financeRecoOrderItemSkuPoList
                = financeRecoOrderItemSkuDao.getListByFinanceRecoOrderNo(financeRecoOrderPo.getFinanceRecoOrderNo());
        List<String> collectOrderNoErrorList = new ArrayList<>();
        for (FinanceRecoOrderItemSkuPo financeRecoOrderItemSkuPo : financeRecoOrderItemSkuPoList) {
            if (!RecoOrderItemSkuStatus.CONFIRMED.equals(financeRecoOrderItemSkuPo.getRecoOrderItemSkuStatus())
                    && StringUtils.isNotBlank(financeRecoOrderItemSkuPo.getCollectOrderNo())) {
                collectOrderNoErrorList.add(financeRecoOrderItemSkuPo.getCollectOrderNo());
            }
        }
        if (CollectionUtils.isNotEmpty(collectOrderNoErrorList)) {
            String collectOrderNoErrorJoining = String.join(",", collectOrderNoErrorList);
            throw new ParamIllegalException("收单单据:{}未确认，请全部确认后再进行此操作！", collectOrderNoErrorJoining);
        }
        // 解决新建款项条目没有单号情况
        List<FinanceRecoOrderItemSkuPo> financeRecoOrderItemSkuPoErrorList = financeRecoOrderItemSkuPoList.stream()
                .filter(po -> !RecoOrderItemSkuStatus.CONFIRMED.equals(po.getRecoOrderItemSkuStatus()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(financeRecoOrderItemSkuPoErrorList)) {
            throw new ParamIllegalException("收单单据存在未确认，请全部确认后再进行此操作！");
        }


        // 更新数据
        FinanceRecoOrderStatus financeRecoOrderStatus = financeRecoOrderPo.getFinanceRecoOrderStatus()
                .toWaitSupplierConfirm();
        financeRecoOrderPo.setFinanceRecoOrderStatus(financeRecoOrderStatus);
        financeRecoOrderPo.setSubmitUser(userKey);
        financeRecoOrderPo.setSubmitUsername(username);
        financeRecoOrderPo.setSubmitTime(LocalDateTime.now());
        financeRecoOrderPo.setFollowUser(userKey);
        financeRecoOrderPo.setFollowUsername(username);
        financeRecoOrderPo.setCtrlUser("");
        financeRecoOrderPo.setRemarks(dto.getRemarks());
        financeRecoOrderDao.updateByIdVersion(financeRecoOrderPo);

        // 日志
        final LogVersionBo logVersionBo = new LogVersionBo();
        logVersionBo.setKey("操作");
        logVersionBo.setValue("跟单提交");
        logBaseService.simpleLog(LogBizModule.FINANCE_RECO_ORDER_STATUS, ScmConstant.RECO_ORDER_LOG_VERSION,
                financeRecoOrderPo.getFinanceRecoOrderNo(), financeRecoOrderStatus.getRemark(),
                Collections.singletonList(logVersionBo));

    }

    /**
     * 跟单确认
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/24 10:01
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmRecoOrder(RecoOrderConfirmDto dto) {
        FinanceRecoOrderPo financeRecoOrderPo = financeRecoOrderDao.getOneByNoAndVersion(dto.getFinanceRecoOrderNo(),
                dto.getVersion());
        Assert.notNull(financeRecoOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        String userKey = GlobalContext.getUserKey();
        String username = GlobalContext.getUsername();
        if (!userKey.equals(financeRecoOrderPo.getCtrlUser())) {
            throw new ParamIllegalException("对账单:{}的处理人不是当前登录账号，无法进行此操作！",
                    financeRecoOrderPo.getFinanceRecoOrderNo());
        }

        // 更新数据
        FinanceRecoOrderStatus financeRecoOrderStatus;
        String logRemark = "拒绝";
        if (BooleanType.TRUE.equals(dto.getExamine())) {
            financeRecoOrderStatus = financeRecoOrderPo.getFinanceRecoOrderStatus()
                    .toLaunchApprove();
            logRemark = "通过";
        } else {
            financeRecoOrderStatus = financeRecoOrderPo.getFinanceRecoOrderStatus()
                    .toRejectUnderReview();
        }

        // 更新数据
        financeRecoOrderPo.setFinanceRecoOrderStatus(financeRecoOrderStatus);
        financeRecoOrderPo.setConfirmUser(userKey);
        financeRecoOrderPo.setConfirmUsername(username);
        financeRecoOrderPo.setConfirmTime(LocalDateTime.now());
        financeRecoOrderPo.setFollowUser(userKey);
        financeRecoOrderPo.setFollowUsername(username);
        financeRecoOrderPo.setRemarks(dto.getRemarks());
        financeRecoOrderDao.updateByIdVersion(financeRecoOrderPo);

        // 日志
        final LogVersionBo logVersionBo = new LogVersionBo();
        logVersionBo.setKey("操作");
        logVersionBo.setValue("跟单确认-" + logRemark);
        logBaseService.simpleLog(LogBizModule.FINANCE_RECO_ORDER_STATUS, ScmConstant.RECO_ORDER_LOG_VERSION,
                financeRecoOrderPo.getFinanceRecoOrderNo(), financeRecoOrderStatus.getRemark(),
                Collections.singletonList(logVersionBo));

        // 如果同意的话，就走下面飞书流程了
        if (BooleanType.TRUE.equals(dto.getExamine())) {
            // 获取明细sku详情
            List<FinanceRecoOrderItemSkuPo> financeRecoOrderItemSkuPoList
                    = financeRecoOrderItemSkuDao.getListByFinanceRecoOrderNo(
                    financeRecoOrderPo.getFinanceRecoOrderNo());
            Map<FinanceRecoFundType, List<FinanceRecoOrderItemSkuPo>> financeRecoOrderItemSkuPoMap
                    = financeRecoOrderItemSkuPoList.stream()
                    .collect(Collectors.groupingBy(FinanceRecoOrderItemSkuPo::getFinanceRecoFundType));

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

            // 创建飞书审批流
            final RecoOrderApproveBo recoOrderApproveBo = new RecoOrderApproveBo();
            recoOrderApproveBo.setSupplierCode(financeRecoOrderPo.getSupplierCode());
            recoOrderApproveBo.setSettlePrice(financeRecoOrderPo.getSettlePrice()
                    .toString());
            LocalDateTime reconciliationStartTime = TimeUtil.convertZone(
                    financeRecoOrderPo.getReconciliationStartTime(), TimeZoneId.UTC, TimeZoneId.CN);
            LocalDateTime reconciliationEndTime = TimeUtil.convertZone(financeRecoOrderPo.getReconciliationEndTime(),
                    TimeZoneId.UTC, TimeZoneId.CN);
            recoOrderApproveBo.setReconciliationCycleTime(
                    reconciliationStartTime.toLocalDate() + " 至 " + reconciliationEndTime.toLocalDate());
            String reconciliationCycleValue = ScmTimeUtil.getTimeChangeName(reconciliationStartTime,
                    reconciliationEndTime);
            recoOrderApproveBo.setReconciliationCycleValue(reconciliationCycleValue);
            recoOrderApproveBo.setReconciliationCycleDay(
                    ScmTimeUtil.calculateDaysBetween(reconciliationStartTime, reconciliationEndTime)
                            .toString());

            recoOrderApproveBo.setReason(
                    financeRecoOrderPo.getSupplierCode() + "家" + reconciliationCycleValue + "货款对账申请" + scmFinanceProp.getRecoOrderLink() + financeRecoOrderPo.getFinanceRecoOrderNo());
            recoOrderApproveBo.setDept(dept);
            recoOrderApproveBo.setRemarks(financeRecoOrderPo.getRemarks());

            final List<RecoOrderDetailApproveBo> recoOrderDetailApproveList = new ArrayList<>();
            // 详情
            // 按顺序对分组结果进行排序
            List<FinanceRecoFundType> sortedTypeList = FinanceRecoFundType.getSortedTypeList();
            Map<FinanceRecoFundType, List<FinanceRecoOrderItemSkuPo>> sortedFinanceRecoOrderItemSkuPoMap = new LinkedHashMap<>();
            for (FinanceRecoFundType financeRecoFundType : sortedTypeList) {
                if (financeRecoOrderItemSkuPoMap.containsKey(financeRecoFundType)) {
                    sortedFinanceRecoOrderItemSkuPoMap.put(financeRecoFundType, financeRecoOrderItemSkuPoMap.get(financeRecoFundType));
                }
            }
            sortedFinanceRecoOrderItemSkuPoMap.forEach(
                    (FinanceRecoFundType financeRecoFundType, List<FinanceRecoOrderItemSkuPo> poList) -> {

                        // 应付
                        BigDecimal payPrice = poList.stream()
                                .filter(po -> FinanceRecoPayType.HANDLE.equals(po.getFinanceRecoPayType()))
                                .map(FinanceRecoOrderItemSkuPo::getTotalPrice)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        // 应收
                        BigDecimal receivePrice = poList.stream()
                                .filter(po -> FinanceRecoPayType.RECEIVABLE.equals(po.getFinanceRecoPayType()))
                                .map(FinanceRecoOrderItemSkuPo::getTotalPrice)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        // 对账
                        BigDecimal settlePrice = payPrice.subtract(receivePrice);

                        RecoOrderDetailApproveBo recoOrderDetailApproveBo = new RecoOrderDetailApproveBo();
                        recoOrderDetailApproveBo.setDetailType(financeRecoFundType.getRemark());
                        recoOrderDetailApproveBo.setRmbText(ScmFormatUtil.convertToChinese(settlePrice));
                        recoOrderDetailApproveBo.setRmb(settlePrice);
                        recoOrderDetailApproveList.add(recoOrderDetailApproveBo);
                    });

            recoOrderApproveBo.setRecoOrderDetailApproveList(recoOrderDetailApproveList);

            AbstractApproveCreator<RecoOrderApproveBo> abstractApproveCreator = new IbfsRecoOrderApproveApproveCreator(
                    idGenerateService,
                    mcRemoteService, feishuAuditOrderDao);
            abstractApproveCreator.createFeiShuInstance(financeRecoOrderPo.getFinanceRecoOrderId(), recoOrderApproveBo);
        }

    }

    /**
     * 重新生成新对账单
     *
     * @param financeRecoOrderPo:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/23 16:08
     */
    public void recreateFinanceRecoOrder(FinanceRecoOrderPo financeRecoOrderPo) {
        RecoOrderCreateBo recoOrderCreateBo = new RecoOrderCreateBo();
        recoOrderCreateBo.setSupplierCode(financeRecoOrderPo.getSupplierCode());
        SupplierPo supplierPo = supplierDao.getBySupplierCode(financeRecoOrderPo.getSupplierCode());
        if (supplierPo == null) {
            throw new BizException("查找不到对应的供应商库存数据，数据异常，请联系系统管理员!");
        }
        if (StringUtils.isBlank(supplierPo.getSupplierAlias())) {
            throw new BizException("当前供应商:{}的别称为空，请联系系统管理员维护数据！", supplierPo.getSupplierCode());
        }
        recoOrderCreateBo.setSupplierAlias(supplierPo.getSupplierAlias());
        recoOrderCreateBo.setReconciliationCycle(supplierPo.getReconciliationCycle());
        recoOrderCreateBo.setReconciliationStartTime(financeRecoOrderPo.getReconciliationStartTime());
        recoOrderCreateBo.setReconciliationEndTime(financeRecoOrderPo.getReconciliationEndTime());
        recoOrderCreateBo.setFollowUser(financeRecoOrderPo.getFollowUser());
        recoOrderCreateBo.setFollowUsername(financeRecoOrderPo.getFollowUsername());

        this.createRecoOrder(recoOrderCreateBo);

    }

    /**
     * 定时任务创建对账单
     *
     * @param param:1手动触发收单
     * @return void
     * @author ChenWenLong
     * @date 2024/5/29 13:39
     */
    public void createFinanceRecoOrderTask(String param) {
        // 获取供应商信息
        List<SupplierPo> supplierPoList = supplierDao.getListByStatusAndReconciliationCycle(SupplierStatus.ENABLED,
                ReconciliationCycle.getAllValues());

        // 获取当前CN的时间
        LocalDateTime localDateTimeNow = TimeUtil.now(TimeZoneId.CN);
        // 如果存在入参就以入参时间作为标准
        if (StringUtils.isNotBlank(param)) {
            localDateTimeNow = ScmTimeUtil.strToLocalDateTime(param);
            if (null == localDateTimeNow) {
                throw new BizException("定时任务创建对账单的入参错误，请联系系统管理员！");
            }
            localDateTimeNow = localDateTimeNow.plusHours(1);
        }

        for (SupplierPo supplierPo : supplierPoList) {
            ReconciliationCycle reconciliationCycle = supplierPo.getReconciliationCycle();
            // 创建对账单
            if (ReconciliationCycle.whetherFirstDay(reconciliationCycle, localDateTimeNow) || StringUtils.isNotBlank(
                    param)) {
                RecoOrderCreateBo recoOrderCreateBo = new RecoOrderCreateBo();
                recoOrderCreateBo.setSupplierCode(supplierPo.getSupplierCode());
                if (StringUtils.isBlank(supplierPo.getSupplierAlias())) {
                    throw new BizException("当前供应商:{}的别称为空，请联系系统管理员维护数据！",
                            supplierPo.getSupplierCode());
                }
                if (StringUtils.isBlank(supplierPo.getFollowUser())) {
                    throw new BizException("当前供应商:{}的跟单采购员为空，创建对账单失败，请联系系统管理员维护数据！",
                            supplierPo.getSupplierCode());
                }
                recoOrderCreateBo.setSupplierAlias(supplierPo.getSupplierAlias());
                recoOrderCreateBo.setReconciliationCycle(supplierPo.getReconciliationCycle());
                // CN时间
                LocalDateTime reconciliationStartTime = ReconciliationCycle.getReconciliationStartTime(
                        reconciliationCycle, localDateTimeNow);
                LocalDateTime reconciliationEndTime = ReconciliationCycle.getReconciliationEndTime(reconciliationCycle,
                        localDateTimeNow);
                LocalDateTime localDateStartTime = TimeUtil.convertZone(reconciliationStartTime, TimeZoneId.CN,
                        TimeZoneId.UTC);
                LocalDateTime localDateEndTime = TimeUtil.convertZone(reconciliationEndTime, TimeZoneId.CN,
                        TimeZoneId.UTC);
                recoOrderCreateBo.setReconciliationStartTime(localDateStartTime);
                recoOrderCreateBo.setReconciliationEndTime(localDateEndTime);
                recoOrderCreateBo.setFollowUser(supplierPo.getFollowUser());
                recoOrderCreateBo.setFollowUsername(supplierPo.getFollowUsername());
                List<FinanceRecoOrderPo> financeRecoOrderPoList = financeRecoOrderDao.getListBySupplierCodeAndNotStatus(
                        supplierPo.getSupplierCode(),
                        FinanceRecoOrderStatus.DELETE,
                        localDateStartTime);
                if (CollectionUtils.isNotEmpty(financeRecoOrderPoList)) {
                    log.warn("供应商代码：{}，已存在非作废对账单了，禁止在重复生成！", supplierPo.getSupplierCode());
                } else {
                    // 执行异步任务处理每一个供应商
                    recoOrderBaseService.createRecoOrderHandler(recoOrderCreateBo);
                }

            }

        }

    }

    /**
     * 定时任务收单对账单
     *
     * @param param:1手动触发收单
     * @return void
     * @author ChenWenLong
     * @date 2024/5/29 13:39
     */
    public void collectFinanceRecoOrderTask(String param) {

        // 获取当前CN的时间
        LocalDateTime localDateTimeNow = TimeUtil.now(TimeZoneId.CN);

        // 当输入是对账单号时，只收指定的对账单
        if (StringUtils.isNotBlank(param)) {
            FinanceRecoOrderPo financeRecoOrderCollectPo = financeRecoOrderDao.getOneByNo(param);
            if (null != financeRecoOrderCollectPo
                    && FinanceRecoOrderStatus.PENDING_ORDER.equals(
                    financeRecoOrderCollectPo.getFinanceRecoOrderStatus())) {
                // 执行异步任务处理一个对账单
                recoOrderBaseService.collectRecoOrderHandler(financeRecoOrderCollectPo);
                return;
            }
            if (null != financeRecoOrderCollectPo) {
                throw new BizException("定时任务收单对账单的入参错误，对账单单号:{}的状态是{}处于非{}，禁止操作！", param,
                        financeRecoOrderCollectPo.getFinanceRecoOrderStatus()
                                .getRemark(),
                        FinanceRecoOrderStatus.PENDING_ORDER.getRemark());
            }
        }

        // 如果存在入参就以入参时间作为标准
        if (StringUtils.isNotBlank(param)) {
            localDateTimeNow = ScmTimeUtil.strToLocalDateTime(param);
            if (null == localDateTimeNow) {
                throw new BizException("定时任务创建对账单的入参错误，请联系系统管理员！");
            }
            localDateTimeNow = localDateTimeNow.plusHours(1);
        }

        if (!ReconciliationCycle.whetherCurrentFirstDay(localDateTimeNow)) {
            log.info("时间:{}不是周第一天或者月第一天", localDateTimeNow);
            return;
        }

        // 获取昨天
        LocalDateTime reconciliationLastEndTime = localDateTimeNow.minusDays(1);

        // 获取已经生成对账单的，进行收单操作
        List<FinanceRecoOrderPo> financeRecoOrderPoList = financeRecoOrderDao.getListByStatus(FinanceRecoOrderStatus.PENDING_ORDER);

        List<FinanceRecoOrderPo> filteredList = financeRecoOrderPoList.stream()
                .filter(po -> TimeUtil.convertZone(po.getReconciliationEndTime(), TimeZoneId.UTC, TimeZoneId.CN).toLocalDate()
                        .equals(reconciliationLastEndTime.toLocalDate()))
                .collect(Collectors.toList());

        for (FinanceRecoOrderPo financeRecoOrderPo : filteredList) {
            // 执行异步任务处理每一个对账单
            recoOrderBaseService.collectRecoOrderHandler(financeRecoOrderPo);
        }

    }


    /**
     * 异步任务调用事务创建
     *
     * @param recoOrderCreateBo:
     * @return void
     * @author ChenWenLong
     * @date 2024/6/6 15:57
     */
    @Transactional(rollbackFor = Exception.class)
    public void createRecoOrderTask(RecoOrderCreateBo recoOrderCreateBo) {
        this.createRecoOrder(recoOrderCreateBo);
    }

    /**
     * 生成对账单
     *
     * @param :
     * @return void
     * @author ChenWenLong
     * @date 2024/5/27 18:09
     */
    @RedisLock(prefix = ScmRedisConstant.CREATE_RECO_ORDER, key = "#recoOrderCreateBo.supplierCode", waitTime = 1,
            leaseTime = -1, exceptionDesc = "供应商创建对账单正在处理中，本次中断执行")
    public FinanceRecoOrderPo createRecoOrder(RecoOrderCreateBo recoOrderCreateBo) {
        List<FinanceRecoOrderPo> financeRecoOrderPoList = financeRecoOrderDao.getListBySupplierCodeAndNotStatus(
                recoOrderCreateBo.getSupplierCode(),
                FinanceRecoOrderStatus.DELETE,
                recoOrderCreateBo.getReconciliationStartTime());
        if (CollectionUtils.isNotEmpty(financeRecoOrderPoList)) {
            throw new BizException("供应商代码：{}，已存在非作废对账单了，禁止在重复生成！", recoOrderCreateBo.getSupplierCode());
        }
        final String financeRecoOrderNo = this.createFinanceRecoOrderNo(recoOrderCreateBo.getSupplierAlias());
        FinanceRecoOrderPo insertPo = new FinanceRecoOrderPo();
        insertPo.setFinanceRecoOrderNo(financeRecoOrderNo);
        insertPo.setFinanceRecoOrderStatus(FinanceRecoOrderStatus.PENDING_ORDER);
        insertPo.setSupplierCode(recoOrderCreateBo.getSupplierCode());
        insertPo.setReconciliationCycle(recoOrderCreateBo.getReconciliationCycle());
        insertPo.setReconciliationStartTime(recoOrderCreateBo.getReconciliationStartTime());
        insertPo.setReconciliationEndTime(recoOrderCreateBo.getReconciliationEndTime());
        insertPo.setCtrlUser(recoOrderCreateBo.getFollowUser());
        insertPo.setFollowUser(recoOrderCreateBo.getFollowUser());
        insertPo.setFollowUsername(recoOrderCreateBo.getFollowUsername());
        financeRecoOrderDao.insert(insertPo);
        // 日志
        final LogVersionBo logVersionBo = new LogVersionBo();
        logVersionBo.setKey("操作");
        logVersionBo.setValue("创建对账单");
        logBaseService.simpleLog(LogBizModule.FINANCE_RECO_ORDER_STATUS, ScmConstant.RECO_ORDER_LOG_VERSION,
                insertPo.getFinanceRecoOrderNo(), insertPo.getFinanceRecoOrderStatus()
                        .getRemark(),
                Collections.singletonList(logVersionBo));
        return insertPo;
    }

    /**
     * 异步任务调用事务收单
     *
     * @param financeRecoOrderPo:
     * @return void
     * @author ChenWenLong
     * @date 2024/6/6 15:57
     */
    @Transactional(rollbackFor = Exception.class)
    public void collectRecoOrderTask(FinanceRecoOrderPo financeRecoOrderPo) {
        this.collectRecoOrder(financeRecoOrderPo);
    }

    /**
     * 对账单收单逻辑
     *
     * @param :
     * @return void
     * @author ChenWenLong
     * @date 2024/5/27 18:09
     */
    @RedisLock(prefix = ScmRedisConstant.COLLECT_RECO_ORDER, key = "#financeRecoOrderPo.financeRecoOrderNo",
            waitTime = 1,
            leaseTime = -1, exceptionDesc = "对账单在收单正在处理中，本次中断执行")
    public void collectRecoOrder(FinanceRecoOrderPo financeRecoOrderPo) {
        if (!FinanceRecoOrderStatus.PENDING_ORDER.equals(financeRecoOrderPo.getFinanceRecoOrderStatus())) {
            throw new BizException("定时任务收单对账单错误，对账单单号:{}的状态是{}处于非{}，禁止操作！",
                    financeRecoOrderPo.getFinanceRecoOrderNo(),
                    financeRecoOrderPo.getFinanceRecoOrderStatus().getRemark(),
                    FinanceRecoOrderStatus.PENDING_ORDER.getRemark());
        }
        log.info("对账单进行收单的对账单:{}", JacksonUtil.parse2Str(financeRecoOrderPo));
        // 对账应付金额
        BigDecimal payPrice
                = financeRecoOrderPo.getPayPrice() == null ? BigDecimal.ZERO : financeRecoOrderPo.getPayPrice();
        // 对账应收金额
        BigDecimal receivePrice
                = financeRecoOrderPo.getReceivePrice() == null ? BigDecimal.ZERO : financeRecoOrderPo.getReceivePrice();

        // 创建单据
        List<FinanceRecoOrderItemPo> insertRecoOrderItemPoList = new ArrayList<>();
        // 创建单据详情SKU
        List<FinanceRecoOrderItemSkuPo> insertRecoOrderItemSkuPoList = new ArrayList<>();
        // 创建校验单据详情SKU
        List<FinanceRecoOrderItemInspectPo> insertRecoOrderItemInspectPoList = new ArrayList<>();
        // 创建校验单据详情SKU
        List<FinanceRecoOrderItemRelationPo> insertRecoOrderItemRelationPoList = new ArrayList<>();
        // 金额信息
        RecoOrderCollectBo recoOrderCollectBo = new RecoOrderCollectBo();
        recoOrderCollectBo.setReceivePrice(receivePrice);
        recoOrderCollectBo.setPayPrice(payPrice);
        // 收集采购发货单数据
        this.collectPurchaseDeliverOrder(financeRecoOrderPo,
                recoOrderCollectBo,
                insertRecoOrderItemPoList,
                insertRecoOrderItemSkuPoList,
                insertRecoOrderItemInspectPoList,
                insertRecoOrderItemRelationPoList);

        // 收集样品单数据
        this.collectSampleOrder(financeRecoOrderPo,
                recoOrderCollectBo,
                insertRecoOrderItemPoList,
                insertRecoOrderItemSkuPoList,
                insertRecoOrderItemInspectPoList,
                insertRecoOrderItemRelationPoList);

        // 收集预付款单
        this.collectPrepayment(financeRecoOrderPo,
                recoOrderCollectBo,
                insertRecoOrderItemPoList,
                insertRecoOrderItemSkuPoList,
                insertRecoOrderItemInspectPoList);

        // 收集库内退供单
        this.collectPurchaseReturnOrder(financeRecoOrderPo,
                recoOrderCollectBo,
                insertRecoOrderItemPoList,
                insertRecoOrderItemSkuPoList,
                insertRecoOrderItemInspectPoList,
                insertRecoOrderItemRelationPoList);

        BigDecimal updateSettlePrice = recoOrderCollectBo.getPayPrice()
                .subtract(recoOrderCollectBo.getReceivePrice());
        financeRecoOrderPo.setSettlePrice(updateSettlePrice);
        financeRecoOrderPo.setPayPrice(recoOrderCollectBo.getPayPrice());
        financeRecoOrderPo.setReceivePrice(recoOrderCollectBo.getReceivePrice());
        financeRecoOrderPo.setCollectOrderTime(LocalDateTime.now());
        financeRecoOrderPo.setFinanceRecoOrderStatus(FinanceRecoOrderStatus.WAIT_SUBMIT);
        financeRecoOrderDao.updateByIdVersion(financeRecoOrderPo);
        financeRecoOrderItemDao.insertBatch(insertRecoOrderItemPoList);
        financeRecoOrderItemSkuDao.insertBatch(insertRecoOrderItemSkuPoList);
        financeRecoOrderItemInspectDao.insertBatch(insertRecoOrderItemInspectPoList);
        financeRecoOrderItemRelationDao.insertBatch(insertRecoOrderItemRelationPoList);

        // 日志
        final LogVersionBo logVersionBo = new LogVersionBo();
        logVersionBo.setKey("操作");
        logVersionBo.setValue("收单");
        logBaseService.simpleLog(LogBizModule.FINANCE_RECO_ORDER_STATUS, ScmConstant.RECO_ORDER_LOG_VERSION,
                financeRecoOrderPo.getFinanceRecoOrderNo(),
                financeRecoOrderPo.getFinanceRecoOrderStatus().getRemark(),
                Collections.singletonList(logVersionBo));

    }

    /**
     * 收集库内退供单
     *
     * @param financeRecoOrderPo:
     * @param recoOrderCollectBo:
     * @param insertRecoOrderItemPoList:
     * @param insertRecoOrderItemSkuPoList:
     * @param insertRecoOrderItemRelationPoList:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/29 10:12
     */
    private void collectPurchaseReturnOrder(FinanceRecoOrderPo financeRecoOrderPo,
                                            RecoOrderCollectBo recoOrderCollectBo,
                                            List<FinanceRecoOrderItemPo> insertRecoOrderItemPoList,
                                            List<FinanceRecoOrderItemSkuPo> insertRecoOrderItemSkuPoList,
                                            List<FinanceRecoOrderItemInspectPo> insertRecoOrderItemInspectPoList,
                                            List<FinanceRecoOrderItemRelationPo> insertRecoOrderItemRelationPoList) {
        BigDecimal receivePrice = recoOrderCollectBo.getReceivePrice();
        // 收集库内退供单
        List<PurchaseReturnOrderPo> purchaseReturnOrderPoList = purchaseReturnOrderDao.getListByStatusAndSupplierCode(
                ReturnOrderStatus.RECEIPTED,
                financeRecoOrderPo.getSupplierCode(),
                financeRecoOrderPo.getReconciliationStartTime(),
                financeRecoOrderPo.getReconciliationEndTime(),
                List.of(ReturnType.PROCESS_DEFECT, ReturnType.INSIDE_CHECK, ReturnType.MATERIAL_DEFECT));

        List<String> returnOrderNoList = purchaseReturnOrderPoList.stream()
                .map(PurchaseReturnOrderPo::getReturnOrderNo)
                .collect(Collectors.toList());

        // 查询退货单详情
        List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList
                = purchaseReturnOrderItemDao.getListByReturnNoList(returnOrderNoList);
        Map<String, List<PurchaseReturnOrderItemPo>> purchaseReturnOrderItemPoMap
                = purchaseReturnOrderItemPoList.stream()
                .collect(Collectors.groupingBy(PurchaseReturnOrderItemPo::getReturnOrderNo));

        // 获取次品记录信息，可以获得对应退货仓库以及结算价
        List<DefectHandlingPo> defectHandlingPoList = defectHandlingDao.getListByRelatedOrderNoList(returnOrderNoList);
        Map<String, DefectHandlingPo> defectHandlingPoRelatedMap = defectHandlingPoList.stream()
                .collect(Collectors.toMap(DefectHandlingPo::getRelatedOrderNo, Function.identity(),
                        (existing, replacement) -> existing));


        // 获取批次码的价格
        List<String> skuBatchCodeList = purchaseReturnOrderItemPoList.stream()
                .map(PurchaseReturnOrderItemPo::getSkuBatchCode)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        Map<String, BigDecimal> skuBatchPriceMap = wmsRemoteService.getSkuBatchPriceMapBySkuBatchList(skuBatchCodeList);

        // 查询是否存在收集
        Map<String, List<FinanceRecoOrderItemSkuPo>> recoOrderItemSkuPoPurchaseMap
                = financeRecoOrderItemSkuDao.getListByCollectOrderNoAndNotStatus(returnOrderNoList,
                        List.of(FinanceRecoOrderStatus.DELETE))
                .stream()
                .collect(Collectors.groupingBy(FinanceRecoOrderItemSkuPo::getCollectOrderNo));


        // 查询扣款单信息
        List<DeductSupplementAndStatusBo> deductAndStatusBoDefectiveList = deductOrderDefectiveDao.getListByBusinessNoAndStatus(returnOrderNoList, List.of(DeductStatus.AUDITED));
        List<DeductSupplementAndStatusBo> deductAndStatusBoPurchaseList = deductOrderPurchaseDao.getListByBusinessNoAndStatus(returnOrderNoList, List.of(DeductStatus.AUDITED));
        List<DeductSupplementAndStatusBo> deductAndStatusBoQualityList = deductOrderQualityDao.getListByBusinessNoAndStatus(returnOrderNoList, List.of(DeductStatus.AUDITED));
        List<DeductSupplementAndStatusBo> deductAndStatusBoList = new ArrayList<>();
        deductAndStatusBoList.addAll(deductAndStatusBoDefectiveList);
        deductAndStatusBoList.addAll(deductAndStatusBoPurchaseList);
        deductAndStatusBoList.addAll(deductAndStatusBoQualityList);

        // 查询补款单信息
        List<DeductSupplementAndStatusBo> supplementAndStatusBoList = supplementOrderPurchaseDao.getListByBusinessNoAndStatus(returnOrderNoList, List.of(SupplementStatus.AUDITED));

        // 查询是否存在扣款使用
        // 使用 Stream API 合并并去重
        List<Long> businessIdList = Stream.concat(
                        deductAndStatusBoList.stream().map(DeductSupplementAndStatusBo::getBusinessId),
                        supplementAndStatusBoList.stream().map(DeductSupplementAndStatusBo::getBusinessId))
                .distinct()
                .collect(Collectors.toList());
        Map<Long, List<RecoOrderItemRelationCheckVo>> financeRecoOrderItemRelationMap
                = financeRecoOrderItemRelationDao.getListByBusinessIdAndNotStatus(businessIdList,
                        List.of(FinanceRecoOrderStatus.DELETE))
                .stream()
                .collect(Collectors.groupingBy(RecoOrderItemRelationCheckVo::getBusinessId));

        for (PurchaseReturnOrderPo purchaseReturnOrderPo : purchaseReturnOrderPoList) {
            if (recoOrderItemSkuPoPurchaseMap.containsKey(purchaseReturnOrderPo.getReturnOrderNo())) {
                log.warn("对账单:{}收集退货单:{}时，已被其他对账单:{}收集了",
                        financeRecoOrderPo.getFinanceRecoOrderNo(),
                        purchaseReturnOrderPo.getReturnOrderNo(),
                        recoOrderItemSkuPoPurchaseMap.get(purchaseReturnOrderPo.getReturnOrderNo())
                                .stream()
                                .map(FinanceRecoOrderItemSkuPo::getFinanceRecoOrderNo)
                                .findFirst()
                                .orElse(null));
                continue;
            }
            // 收集单据
            BigDecimal purchaseReturnOrderPrice = BigDecimal.ZERO;
            int skuNum = 0;

            List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPos = Optional
                    .ofNullable(purchaseReturnOrderItemPoMap.get(purchaseReturnOrderPo.getReturnOrderNo()))
                    .orElse(Collections.emptyList());

            // 雪花id
            long financeRecoOrderItemId = idGenerateService.getSnowflakeId();
            FinanceRecoOrderItemPo insertRecoOrderItemPo = FinanceRecoOrderItemConverter.collectToItemPo(
                    financeRecoOrderPo,
                    FinanceRecoFundType.PURCHASE_PAYMENT,
                    CollectOrderType.RETURN_ORDER,
                    purchaseReturnOrderPo.getReturnOrderNo(),
                    financeRecoOrderItemId);

            // 收集单据SKU详情
            for (PurchaseReturnOrderItemPo purchaseReturnOrderItemPo : purchaseReturnOrderItemPos) {
                log.info("对账单号:{},收入的单据信息PO=>{}", financeRecoOrderPo.getFinanceRecoOrderNo(), JacksonUtil.parse2Str(purchaseReturnOrderItemPo));
                Integer receiptCnt = purchaseReturnOrderItemPo.getReceiptCnt();
                BigDecimal returnOrderSettleItemPrice = purchaseReturnOrderItemPo.getSettlePrice();
                // 如果是价格为0的情况，获取月初加权单价
                if (null == returnOrderSettleItemPrice || BigDecimal.ZERO.compareTo(returnOrderSettleItemPrice) == 0) {
                    DefectHandlingPo defectHandlingPo = defectHandlingPoRelatedMap.get(
                            purchaseReturnOrderPo.getReturnOrderNo());
                    if (null == defectHandlingPo) {
                        throw new BizException(
                                "退货单:{}关联不是次品记录信息，导致对账单收单时获取不到对应退货仓库，请联系系统管理员！",
                                purchaseReturnOrderPo.getReturnOrderNo());
                    }
                    if (StringUtils.isBlank(defectHandlingPo.getWarehouseCode())) {
                        throw new BizException(
                                "次品记录:{}的仓库为空，导致对账单收单时获取不到对应退货仓库，请联系系统管理员！",
                                defectHandlingPo.getDefectHandlingNo());
                    }

                    // 查询商品成本的月初加权单价
                    final String currentMonthString = ScmTimeUtil.getSpecifyMonthString(
                            financeRecoOrderPo.getReconciliationEndTime());
                    CostOfGoodsPo costOfGoodsPo = costOfGoodsDao.getMoDataBySkuAndWarehouse(
                            purchaseReturnOrderItemPo.getSku(), defectHandlingPo.getWarehouseCode(),
                            PolymerizeType.SINGLE_WAREHOUSE, null, currentMonthString);
                    if (Objects.nonNull(costOfGoodsPo)) {
                        returnOrderSettleItemPrice = Objects.isNull(
                                costOfGoodsPo.getWeightingPrice()) || costOfGoodsPo.getWeightingPrice()
                                .compareTo(BigDecimal.ZERO) == 0 ?
                                BigDecimal.ZERO :
                                costOfGoodsPo.getWeightingPrice();
                    }
                }

                BigDecimal totalPrice = returnOrderSettleItemPrice.multiply(BigDecimal.valueOf(receiptCnt));
                long financeRecoOrderItemSkuId = idGenerateService.getSnowflakeId();
                FinanceRecoOrderItemSkuPo financeRecoOrderItemSkuPo = FinanceRecoOrderItemConverter.collectToItemSkuPo(
                        insertRecoOrderItemPo,
                        receiptCnt,
                        returnOrderSettleItemPrice,
                        totalPrice,
                        purchaseReturnOrderItemPo.getPurchaseReturnOrderItemId(),
                        financeRecoOrderItemSkuId,
                        purchaseReturnOrderItemPo.getSku(),
                        purchaseReturnOrderItemPo.getSkuBatchCode());

                // 扣款单收单计算逻辑
                CollectDeductSupplementBo collectDeductBo = this.collectDeductOrder(deductAndStatusBoList,
                        financeRecoOrderItemRelationMap,
                        financeRecoOrderItemSkuPo,
                        purchaseReturnOrderPo.getReturnOrderNo(),
                        purchaseReturnOrderItemPo.getSku());

                // 补款单收单计算逻辑
                CollectDeductSupplementBo collectSupplementBo = this.collectSupplementOrder(supplementAndStatusBoList,
                        financeRecoOrderItemRelationMap,
                        financeRecoOrderItemSkuPo,
                        purchaseReturnOrderPo.getReturnOrderNo(),
                        purchaseReturnOrderItemPo.getSku());


                // 计算扣款金额
                financeRecoOrderItemSkuPo.setTotalPrice(this.deductCount(financeRecoOrderItemSkuPo.getTotalPrice(),
                        collectDeductBo.getTotalPrice(),
                        financeRecoOrderItemSkuPo.getFinanceRecoPayType()));
                insertRecoOrderItemRelationPoList.addAll(collectDeductBo.getInsertRecoOrderItemRelationPoList());

                // 计算补款金额
                financeRecoOrderItemSkuPo.setTotalPrice(this.supplementCount(financeRecoOrderItemSkuPo.getTotalPrice(),
                        collectSupplementBo.getTotalPrice(),
                        financeRecoOrderItemSkuPo.getFinanceRecoPayType()));
                insertRecoOrderItemRelationPoList.addAll(collectSupplementBo.getInsertRecoOrderItemRelationPoList());

                // 创建详情sku数据
                // 重新计算单价
                if (0 == financeRecoOrderItemSkuPo.getNum()) {
                    financeRecoOrderItemSkuPo.setPrice(BigDecimal.ZERO);
                    log.warn("退货单详情PO={}的收货数量为0", JacksonUtil.parse2Str(purchaseReturnOrderItemPo));
                } else {
                    BigDecimal price = financeRecoOrderItemSkuPo.getTotalPrice().divide(new BigDecimal(financeRecoOrderItemSkuPo.getNum()), 2, RoundingMode.HALF_UP);
                    financeRecoOrderItemSkuPo.setPrice(price);
                }
                insertRecoOrderItemSkuPoList.add(financeRecoOrderItemSkuPo);

                // 创建对应验证数据
                // 数量验证
                Integer inspectNum = purchaseReturnOrderItemPo.getRealityReturnCnt();
                insertRecoOrderItemInspectPoList.add(
                        FinanceRecoOrderItemInspectConverter.create(financeRecoOrderItemSkuPo,
                                RecoOrderInspectType.QUANTITY,
                                new BigDecimal(financeRecoOrderItemSkuPo.getNum()),
                                new BigDecimal(inspectNum)));


                // 价格验证
                BigDecimal inspectPrice = BigDecimal.ZERO;
                if (skuBatchPriceMap.containsKey(purchaseReturnOrderItemPo.getSkuBatchCode())) {
                    inspectPrice = skuBatchPriceMap.get(purchaseReturnOrderItemPo.getSkuBatchCode());
                }

                // 计算扣款金额
                BigDecimal inspectPriceTotal = inspectPrice.multiply(new BigDecimal(inspectNum));
                inspectPriceTotal = this.deductCount(inspectPriceTotal,
                        collectDeductBo.getTotalPrice(),
                        financeRecoOrderItemSkuPo.getFinanceRecoPayType());

                // 计算补款金额
                inspectPriceTotal = this.supplementCount(inspectPriceTotal,
                        collectSupplementBo.getTotalPrice(),
                        financeRecoOrderItemSkuPo.getFinanceRecoPayType());
                // 重新计算检验单价
                if (0 == inspectNum) {
                    throw new BizException("对账单收单异常，退货单的实际退货数存在为0情况，请系统管理员处理数据po={}", JacksonUtil.parse2Str(purchaseReturnOrderItemPo));
                } else {
                    inspectPrice = inspectPriceTotal.divide(new BigDecimal(inspectNum), 2, RoundingMode.HALF_UP);
                }

                insertRecoOrderItemInspectPoList.add(
                        FinanceRecoOrderItemInspectConverter.create(financeRecoOrderItemSkuPo,
                                RecoOrderInspectType.PRICE,
                                financeRecoOrderItemSkuPo.getPrice(),
                                inspectPrice));


                // 累加金额和数量
                purchaseReturnOrderPrice = purchaseReturnOrderPrice.add(financeRecoOrderItemSkuPo.getTotalPrice());
                skuNum += receiptCnt;
            }
            insertRecoOrderItemPo.setNum(skuNum);
            insertRecoOrderItemPo.setTotalPrice(purchaseReturnOrderPrice);
            insertRecoOrderItemPoList.add(insertRecoOrderItemPo);

            receivePrice = receivePrice.add(purchaseReturnOrderPrice);
            recoOrderCollectBo.setReceivePrice(receivePrice);
        }
    }

    /**
     * 收集预付款单
     *
     * @param financeRecoOrderPo:
     * @param recoOrderCollectBo:
     * @param insertRecoOrderItemPoList:
     * @param insertRecoOrderItemSkuPoList:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/29 10:11
     */
    private void collectPrepayment(FinanceRecoOrderPo financeRecoOrderPo,
                                   RecoOrderCollectBo recoOrderCollectBo,
                                   List<FinanceRecoOrderItemPo> insertRecoOrderItemPoList,
                                   List<FinanceRecoOrderItemSkuPo> insertRecoOrderItemSkuPoList,
                                   List<FinanceRecoOrderItemInspectPo> insertRecoOrderItemInspectPoList) {
        BigDecimal receivePrice = recoOrderCollectBo.getReceivePrice();
        // 收集预付款单
        PrepaymentSupplierDto prepaymentSupplierDto = new PrepaymentSupplierDto();
        prepaymentSupplierDto.setFinanceRecoOrderNo(financeRecoOrderPo.getFinanceRecoOrderNo());
        prepaymentSupplierDto.setSupplierCode(financeRecoOrderPo.getSupplierCode());
        List<PrepaymentBo> prepaymentBoList = prepaymentBaseService.getPrepaymentBoBySupplier(prepaymentSupplierDto);

        List<String> prepaymentOrderNoList = prepaymentBoList.stream()
                .map(PrepaymentBo::getPrepaymentOrderNo)
                .collect(Collectors.toList());
        // 查询是否存在收集
        Map<String, List<FinanceRecoOrderItemSkuPo>> recoOrderItemSkuPoPrepaymentMap
                = financeRecoOrderItemSkuDao.getListByCollectOrderNoAndNotStatus(prepaymentOrderNoList,
                        List.of(FinanceRecoOrderStatus.DELETE))
                .stream()
                .collect(Collectors.groupingBy(FinanceRecoOrderItemSkuPo::getCollectOrderNo));

        for (PrepaymentBo prepaymentBo : prepaymentBoList) {
            if (recoOrderItemSkuPoPrepaymentMap.containsKey(prepaymentBo.getPrepaymentOrderNo())) {
                log.warn("对账单:{}收集采购发货单:{}时，已被其他对账单:{}收集了",
                        financeRecoOrderPo.getFinanceRecoOrderNo(),
                        prepaymentBo.getPrepaymentOrderNo(),
                        recoOrderItemSkuPoPrepaymentMap.get(prepaymentBo.getPrepaymentOrderNo())
                                .stream()
                                .map(FinanceRecoOrderItemSkuPo::getFinanceRecoOrderNo)
                                .findFirst()
                                .orElse(null));
                continue;
            }
            log.info("对账单号:{},收入的单据信息PO=>{}", financeRecoOrderPo.getFinanceRecoOrderNo(), JacksonUtil.parse2Str(prepaymentBo));
            // 收集单据
            // 雪花id
            long financeRecoOrderItemId = idGenerateService.getSnowflakeId();
            FinanceRecoOrderItemPo insertRecoOrderItemPo = FinanceRecoOrderItemConverter.collectToItemPo(
                    financeRecoOrderPo,
                    FinanceRecoFundType.PREPAYMENTS,
                    CollectOrderType.PREPAYMENT,
                    prepaymentBo.getPrepaymentOrderNo(),
                    financeRecoOrderItemId);

            // 可抵扣金额
            BigDecimal canDeductionMoney = prepaymentBo.getCanDeductionMoney();
            // 样品的数量
            int num = 1;

            // 收集单据SKU详情
            long financeRecoOrderItemSkuId = idGenerateService.getSnowflakeId();
            FinanceRecoOrderItemSkuPo financeRecoOrderItemSkuPo = FinanceRecoOrderItemConverter.collectToItemSkuPo(
                    insertRecoOrderItemPo,
                    num,
                    canDeductionMoney,
                    canDeductionMoney,
                    prepaymentBo.getPrepaymentOrderId(),
                    financeRecoOrderItemSkuId,
                    null, null);

            insertRecoOrderItemSkuPoList.add(financeRecoOrderItemSkuPo);

            insertRecoOrderItemPo.setNum(financeRecoOrderItemSkuPo.getNum());
            insertRecoOrderItemPo.setTotalPrice(financeRecoOrderItemSkuPo.getTotalPrice());
            insertRecoOrderItemPoList.add(insertRecoOrderItemPo);

            // 数量验证
            insertRecoOrderItemInspectPoList.add(FinanceRecoOrderItemInspectConverter.create(financeRecoOrderItemSkuPo,
                    RecoOrderInspectType.QUANTITY,
                    new BigDecimal(financeRecoOrderItemSkuPo.getNum()),
                    new BigDecimal(num)));

            // 价格验证
            insertRecoOrderItemInspectPoList.add(FinanceRecoOrderItemInspectConverter.create(financeRecoOrderItemSkuPo,
                    RecoOrderInspectType.PRICE,
                    financeRecoOrderItemSkuPo.getPrice(),
                    financeRecoOrderItemSkuPo.getPrice()));

            receivePrice = receivePrice.add(financeRecoOrderItemSkuPo.getTotalPrice());
            recoOrderCollectBo.setReceivePrice(receivePrice);
        }
    }

    /**
     * 收集样品单数据
     *
     * @param financeRecoOrderPo:
     * @param recoOrderCollectBo:
     * @param insertRecoOrderItemPoList:
     * @param insertRecoOrderItemSkuPoList:
     * @param insertRecoOrderItemRelationPoList:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/29 10:09
     */
    private void collectSampleOrder(FinanceRecoOrderPo financeRecoOrderPo,
                                    RecoOrderCollectBo recoOrderCollectBo,
                                    List<FinanceRecoOrderItemPo> insertRecoOrderItemPoList,
                                    List<FinanceRecoOrderItemSkuPo> insertRecoOrderItemSkuPoList,
                                    List<FinanceRecoOrderItemInspectPo> insertRecoOrderItemInspectPoList,
                                    List<FinanceRecoOrderItemRelationPo> insertRecoOrderItemRelationPoList) {
        BigDecimal payPrice = recoOrderCollectBo.getPayPrice();

        List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByStatusList(
                List.of(DevelopSampleStatus.ON_SHELVES),
                List.of(financeRecoOrderPo.getSupplierCode()),
                financeRecoOrderPo.getReconciliationStartTime(),
                financeRecoOrderPo.getReconciliationEndTime(),
                List.of(DevelopSampleMethod.SEAL_SAMPLE, DevelopSampleMethod.SALE),
                List.of(ScmConstant.PROCESS_SUPPLIER_CODE)
        );

        List<String> developSampleOrderNoList = developSampleOrderPoList.stream()
                .map(DevelopSampleOrderPo::getDevelopSampleOrderNo)
                .collect(Collectors.toList());
        // 查询是否存在收集
        Map<String, List<FinanceRecoOrderItemSkuPo>> recoOrderItemSkuPoSampleMap
                = financeRecoOrderItemSkuDao.getListByCollectOrderNoAndNotStatus(developSampleOrderNoList,
                        List.of(FinanceRecoOrderStatus.DELETE))
                .stream()
                .collect(Collectors.groupingBy(FinanceRecoOrderItemSkuPo::getCollectOrderNo));

        // 查询WMS查询收货单信息
        List<ReceiveOrderForScmVo> receiveOrderForScmVos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(developSampleOrderNoList)) {
            ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
            receiveOrderGetDto.setScmBizNoList(developSampleOrderNoList);
            receiveOrderForScmVos = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto);
        }

        // 查询扣款单信息
        List<DeductSupplementAndStatusBo> deductAndStatusBoDefectiveList = deductOrderDefectiveDao.getListByBusinessNoAndStatus(developSampleOrderNoList, List.of(DeductStatus.AUDITED));
        List<DeductSupplementAndStatusBo> deductAndStatusBoPurchaseList = deductOrderPurchaseDao.getListByBusinessNoAndStatus(developSampleOrderNoList, List.of(DeductStatus.AUDITED));
        List<DeductSupplementAndStatusBo> deductAndStatusBoQualityList = deductOrderQualityDao.getListByBusinessNoAndStatus(developSampleOrderNoList, List.of(DeductStatus.AUDITED));
        List<DeductSupplementAndStatusBo> deductAndStatusBoList = new ArrayList<>();
        deductAndStatusBoList.addAll(deductAndStatusBoDefectiveList);
        deductAndStatusBoList.addAll(deductAndStatusBoPurchaseList);
        deductAndStatusBoList.addAll(deductAndStatusBoQualityList);

        // 查询补款单信息
        List<DeductSupplementAndStatusBo> supplementAndStatusBoList = supplementOrderPurchaseDao.getListByBusinessNoAndStatus(developSampleOrderNoList, List.of(SupplementStatus.AUDITED));

        // 查询是否存在补扣款使用
        // 使用 Stream API 合并并去重
        List<Long> businessIdList = Stream.concat(
                        deductAndStatusBoList.stream().map(DeductSupplementAndStatusBo::getBusinessId),
                        supplementAndStatusBoList.stream().map(DeductSupplementAndStatusBo::getBusinessId))
                .distinct()
                .collect(Collectors.toList());
        Map<Long, List<RecoOrderItemRelationCheckVo>> financeRecoOrderItemRelationMap
                = financeRecoOrderItemRelationDao.getListByBusinessIdAndNotStatus(businessIdList,
                        List.of(FinanceRecoOrderStatus.DELETE))
                .stream()
                .collect(Collectors.groupingBy(RecoOrderItemRelationCheckVo::getBusinessId));


        // 获取批次码的价格
        List<String> skuBatchCodeList = developSampleOrderPoList.stream()
                .map(DevelopSampleOrderPo::getSkuBatchCode)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        Map<String, BigDecimal> skuBatchPriceMap = wmsRemoteService.getSkuBatchPriceMapBySkuBatchList(skuBatchCodeList);

        for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoList) {
            if (recoOrderItemSkuPoSampleMap.containsKey(developSampleOrderPo.getDevelopSampleOrderNo())) {
                log.warn("对账单:{}收集采购发货单:{}时，已被其他对账单:{}收集了",
                        financeRecoOrderPo.getFinanceRecoOrderNo(),
                        developSampleOrderPo.getDevelopSampleOrderNo(),
                        recoOrderItemSkuPoSampleMap.get(developSampleOrderPo.getDevelopSampleOrderNo())
                                .stream()
                                .map(FinanceRecoOrderItemSkuPo::getFinanceRecoOrderNo)
                                .findFirst()
                                .orElse(null));
                continue;
            }
            log.info("对账单号:{},收入的单据信息PO=>{}", financeRecoOrderPo.getFinanceRecoOrderNo(), JacksonUtil.parse2Str(developSampleOrderPo));
            // 收集单据
            // 雪花id
            long financeRecoOrderItemId = idGenerateService.getSnowflakeId();
            FinanceRecoOrderItemPo insertRecoOrderItemPo = FinanceRecoOrderItemConverter.collectToItemPo(
                    financeRecoOrderPo,
                    FinanceRecoFundType.PURCHASE_PAYMENT,
                    CollectOrderType.PURCHASE_SAMPLE,
                    developSampleOrderPo.getDevelopSampleOrderNo(),
                    financeRecoOrderItemId);

            // 批次码大货价
            BigDecimal skuBatchSamplePrice = developSampleOrderPo.getSkuBatchSamplePrice();
            // 样品的数量
            int num = 1;

            // 收集单据SKU详情
            long financeRecoOrderItemSkuId = idGenerateService.getSnowflakeId();
            FinanceRecoOrderItemSkuPo financeRecoOrderItemSkuPo = FinanceRecoOrderItemConverter.collectToItemSkuPo(
                    insertRecoOrderItemPo,
                    num,
                    skuBatchSamplePrice,
                    skuBatchSamplePrice,
                    developSampleOrderPo.getDevelopSampleOrderId(),
                    financeRecoOrderItemSkuId,
                    developSampleOrderPo.getSku(),
                    developSampleOrderPo.getSkuBatchCode());


            // 扣款单收单计算逻辑
            CollectDeductSupplementBo collectDeductBo = this.collectDeductOrder(deductAndStatusBoList,
                    financeRecoOrderItemRelationMap,
                    financeRecoOrderItemSkuPo,
                    developSampleOrderPo.getDevelopSampleOrderNo(),
                    developSampleOrderPo.getSku());

            // 补款单收单计算逻辑
            CollectDeductSupplementBo collectSupplementBo = this.collectSupplementOrder(supplementAndStatusBoList,
                    financeRecoOrderItemRelationMap,
                    financeRecoOrderItemSkuPo,
                    developSampleOrderPo.getDevelopSampleOrderNo(),
                    developSampleOrderPo.getSku());

            // 计算扣款金额
            financeRecoOrderItemSkuPo.setTotalPrice(this.deductCount(financeRecoOrderItemSkuPo.getTotalPrice(),
                    collectDeductBo.getTotalPrice(),
                    financeRecoOrderItemSkuPo.getFinanceRecoPayType()));
            insertRecoOrderItemRelationPoList.addAll(collectDeductBo.getInsertRecoOrderItemRelationPoList());

            // 计算补款金额
            financeRecoOrderItemSkuPo.setTotalPrice(this.supplementCount(financeRecoOrderItemSkuPo.getTotalPrice(),
                    collectSupplementBo.getTotalPrice(),
                    financeRecoOrderItemSkuPo.getFinanceRecoPayType()));
            insertRecoOrderItemRelationPoList.addAll(collectSupplementBo.getInsertRecoOrderItemRelationPoList());

            // 重新计算单价
            if (0 == financeRecoOrderItemSkuPo.getNum()) {
                financeRecoOrderItemSkuPo.setPrice(BigDecimal.ZERO);
            } else {
                BigDecimal price = financeRecoOrderItemSkuPo.getTotalPrice().divide(new BigDecimal(financeRecoOrderItemSkuPo.getNum()), 2, RoundingMode.HALF_UP);
                financeRecoOrderItemSkuPo.setPrice(price);
            }
            insertRecoOrderItemSkuPoList.add(financeRecoOrderItemSkuPo);

            insertRecoOrderItemPo.setNum(num);
            insertRecoOrderItemPo.setTotalPrice(financeRecoOrderItemSkuPo.getTotalPrice());
            insertRecoOrderItemPoList.add(insertRecoOrderItemPo);

            payPrice = payPrice.add(financeRecoOrderItemSkuPo.getTotalPrice());
            // 更新对账应付金额
            recoOrderCollectBo.setPayPrice(payPrice);

            // 创建对应验证数据
            // 获取收货单上架数量
            List<ReceiveOrderForScmVo> receiveOrderForScmVoList = receiveOrderForScmVos.stream()
                    .filter(receiveOrderForScmVo -> receiveOrderForScmVo.getScmBizNo()
                            .equals(developSampleOrderPo.getDevelopSampleOrderNo()))
                    .collect(Collectors.toList());
            Integer inspectNum = 0;
            for (ReceiveOrderForScmVo receiveOrderForScmVo : receiveOrderForScmVoList) {
                List<ReceiveOrderForScmVo.ReceiveDeliver> receiveDeliverList
                        = receiveOrderForScmVo.getReceiveDeliverList();
                ReceiveOrderForScmVo.ReceiveDeliver receiveDeliverInfo = receiveDeliverList.stream()
                        .filter(receiveDeliver -> receiveDeliver.getBatchCode()
                                .equals(developSampleOrderPo.getSkuBatchCode()))
                        .filter(receiveDeliver -> receiveDeliver.getSkuCode()
                                .equals(developSampleOrderPo.getSku()))
                        .findFirst()
                        .orElse(null);
                if (null != receiveDeliverInfo) {
                    inspectNum = receiveDeliverInfo.getOnShelvesAmount();
                }
            }
            // 数量验证
            insertRecoOrderItemInspectPoList.add(FinanceRecoOrderItemInspectConverter.create(financeRecoOrderItemSkuPo,
                    RecoOrderInspectType.QUANTITY,
                    new BigDecimal(financeRecoOrderItemSkuPo.getNum()),
                    new BigDecimal(inspectNum)));

            // 价格验证
            BigDecimal inspectPrice = BigDecimal.ZERO;
            if (skuBatchPriceMap.containsKey(developSampleOrderPo.getSkuBatchCode())) {
                inspectPrice = skuBatchPriceMap.get(developSampleOrderPo.getSkuBatchCode());
            }

            // 计算扣款金额
            BigDecimal inspectPriceTotal = inspectPrice.multiply(new BigDecimal(inspectNum));
            inspectPriceTotal = this.deductCount(inspectPriceTotal,
                    collectDeductBo.getTotalPrice(),
                    financeRecoOrderItemSkuPo.getFinanceRecoPayType());

            // 计算补款金额
            inspectPriceTotal = this.supplementCount(inspectPriceTotal,
                    collectSupplementBo.getTotalPrice(),
                    financeRecoOrderItemSkuPo.getFinanceRecoPayType());
            // 重新计算检验单价
            if (0 == inspectNum) {
                throw new BizException("对账单收单异常，样品单的已上架数量存在为0情况，请系统管理员处理数据po={}", JacksonUtil.parse2Str(developSampleOrderPo));
            } else {
                inspectPrice = inspectPriceTotal.divide(new BigDecimal(inspectNum), 2, RoundingMode.HALF_UP);
            }

            insertRecoOrderItemInspectPoList.add(FinanceRecoOrderItemInspectConverter.create(financeRecoOrderItemSkuPo,
                    RecoOrderInspectType.PRICE,
                    financeRecoOrderItemSkuPo.getPrice(),
                    inspectPrice));

        }
    }

    /**
     * 收集采购发货单
     *
     * @param financeRecoOrderPo:
     * @param recoOrderCollectBo:
     * @param insertRecoOrderItemPoList:
     * @param insertRecoOrderItemSkuPoList:
     * @param insertRecoOrderItemRelationPoList:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/29 10:08
     */
    private void collectPurchaseDeliverOrder(FinanceRecoOrderPo financeRecoOrderPo,
                                             RecoOrderCollectBo recoOrderCollectBo,
                                             List<FinanceRecoOrderItemPo> insertRecoOrderItemPoList,
                                             List<FinanceRecoOrderItemSkuPo> insertRecoOrderItemSkuPoList,
                                             List<FinanceRecoOrderItemInspectPo> insertRecoOrderItemInspectPoList,
                                             List<FinanceRecoOrderItemRelationPo> insertRecoOrderItemRelationPoList) {
        BigDecimal payPrice = recoOrderCollectBo.getPayPrice();
        // 获取采购发货列表信息
        List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByStatus(
                List.of(DeliverOrderStatus.WAREHOUSED),
                List.of(financeRecoOrderPo.getSupplierCode()),
                financeRecoOrderPo.getReconciliationStartTime(),
                financeRecoOrderPo.getReconciliationEndTime(),
                null);
        List<String> purchaseDeliverOrderNoList = purchaseDeliverOrderPoList.stream()
                .map(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo)
                .collect(Collectors.toList());
        Map<String, List<PurchaseDeliverOrderItemPo>> purchaseDeliverOrderItemPoMap
                = purchaseDeliverOrderItemDao.getListByDeliverOrderNoList(purchaseDeliverOrderNoList)
                .stream()
                .collect(Collectors.groupingBy(PurchaseDeliverOrderItemPo::getPurchaseDeliverOrderNo));

        // 查询采购单信息
        List<String> purchaseChildOrderNoList = purchaseDeliverOrderPoList.stream()
                .map(PurchaseDeliverOrderPo::getPurchaseChildOrderNo)
                .distinct()
                .collect(Collectors.toList());
        List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNoList(purchaseChildOrderNoList);
        Map<String, BigDecimal> purchaseChildOrderPriceMap = purchaseChildOrderItemPoList.stream()
                .collect(Collectors.toMap(purchaseChildOrderItemPo -> purchaseChildOrderItemPo.getPurchaseChildOrderNo() + purchaseChildOrderItemPo.getSkuBatchCode(),
                        PurchaseChildOrderItemPo::getSettlePrice,
                        (existingValue, newValue) -> existingValue));

        // 查询是否存在收集
        Map<String, List<FinanceRecoOrderItemSkuPo>> recoOrderItemSkuPoPurchaseMap
                = financeRecoOrderItemSkuDao.getListByCollectOrderNoAndNotStatus(purchaseDeliverOrderNoList,
                        List.of(FinanceRecoOrderStatus.DELETE))
                .stream()
                .collect(Collectors.groupingBy(FinanceRecoOrderItemSkuPo::getCollectOrderNo));

        // 查询WMS查询收货单信息
        List<ReceiveOrderForScmVo> receiveOrderForScmVos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(purchaseDeliverOrderNoList)) {
            ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
            receiveOrderGetDto.setScmBizNoList(purchaseDeliverOrderNoList);
            receiveOrderForScmVos = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto);
        }

        // 查询扣款单信息
        List<DeductSupplementAndStatusBo> deductAndStatusBoDefectiveList = deductOrderDefectiveDao.getListByBusinessNoAndStatus(purchaseDeliverOrderNoList, List.of(DeductStatus.AUDITED));
        List<DeductSupplementAndStatusBo> deductAndStatusBoPurchaseList = deductOrderPurchaseDao.getListByBusinessNoAndStatus(purchaseDeliverOrderNoList, List.of(DeductStatus.AUDITED));
        List<DeductSupplementAndStatusBo> deductAndStatusBoQualityList = deductOrderQualityDao.getListByBusinessNoAndStatus(purchaseDeliverOrderNoList, List.of(DeductStatus.AUDITED));
        List<DeductSupplementAndStatusBo> deductAndStatusBoList = new ArrayList<>();
        deductAndStatusBoList.addAll(deductAndStatusBoDefectiveList);
        deductAndStatusBoList.addAll(deductAndStatusBoPurchaseList);
        deductAndStatusBoList.addAll(deductAndStatusBoQualityList);

        // 查询补款单信息
        List<DeductSupplementAndStatusBo> supplementAndStatusBoList = supplementOrderPurchaseDao.getListByBusinessNoAndStatus(purchaseDeliverOrderNoList, List.of(SupplementStatus.AUDITED));


        // 查询是否存在补扣款使用
        // 使用 Stream API 合并并去重
        List<Long> businessIdList = Stream.concat(
                        deductAndStatusBoList.stream().map(DeductSupplementAndStatusBo::getBusinessId),
                        supplementAndStatusBoList.stream().map(DeductSupplementAndStatusBo::getBusinessId))
                .distinct()
                .collect(Collectors.toList());
        Map<Long, List<RecoOrderItemRelationCheckVo>> financeRecoOrderItemRelationMap
                = financeRecoOrderItemRelationDao.getListByBusinessIdAndNotStatus(businessIdList,
                        List.of(FinanceRecoOrderStatus.DELETE))
                .stream()
                .collect(Collectors.groupingBy(RecoOrderItemRelationCheckVo::getBusinessId));

        for (PurchaseDeliverOrderPo purchaseDeliverOrderPo : purchaseDeliverOrderPoList) {
            if (recoOrderItemSkuPoPurchaseMap.containsKey(purchaseDeliverOrderPo.getPurchaseDeliverOrderNo())) {
                log.warn("对账单:{}收集采购发货单:{}时，已被其他对账单:{}收集了",
                        financeRecoOrderPo.getFinanceRecoOrderNo(),
                        purchaseDeliverOrderPo.getPurchaseDeliverOrderNo(),
                        recoOrderItemSkuPoPurchaseMap.get(purchaseDeliverOrderPo.getPurchaseDeliverOrderNo())
                                .stream()
                                .map(FinanceRecoOrderItemSkuPo::getFinanceRecoOrderNo)
                                .findFirst()
                                .orElse(null));
                continue;
            }
            // 收集单据
            BigDecimal purchaseSettlePrice = BigDecimal.ZERO;
            int skuNum = 0;

            List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPos = Optional
                    .ofNullable(purchaseDeliverOrderItemPoMap.get(purchaseDeliverOrderPo.getPurchaseDeliverOrderNo()))
                    .orElse(Collections.emptyList());

            // 雪花id
            long financeRecoOrderItemId = idGenerateService.getSnowflakeId();
            FinanceRecoOrderItemPo insertRecoOrderItemPo = FinanceRecoOrderItemConverter.collectToItemPo(
                    financeRecoOrderPo,
                    FinanceRecoFundType.PURCHASE_PAYMENT,
                    CollectOrderType.PURCHASE_DELIVER,
                    purchaseDeliverOrderPo.getPurchaseDeliverOrderNo(),
                    financeRecoOrderItemId);

            // 收集单据SKU详情
            for (PurchaseDeliverOrderItemPo purchaseDeliverOrderItemPo : purchaseDeliverOrderItemPos) {
                log.info("对账单号:{},收入的单据信息PO=>{}", financeRecoOrderPo.getFinanceRecoOrderNo(), JacksonUtil.parse2Str(purchaseDeliverOrderItemPo));
                Integer qualityGoodsCnt = purchaseDeliverOrderItemPo.getQualityGoodsCnt();

                BigDecimal skuBatchCodePrice = BigDecimal.ZERO;
                String purchaseChildOrderNo = purchaseDeliverOrderPo.getPurchaseChildOrderNo();
                String skuBatchCode = purchaseDeliverOrderItemPo.getSkuBatchCode();

                if (StringUtils.isNotBlank(purchaseChildOrderNo) && StringUtils.isNotBlank(skuBatchCode)) {
                    skuBatchCodePrice = Optional.ofNullable(purchaseChildOrderPriceMap.get(purchaseChildOrderNo + skuBatchCode))
                            .orElse(BigDecimal.ZERO);
                }
                BigDecimal totalPrice = skuBatchCodePrice.multiply(BigDecimal.valueOf(qualityGoodsCnt));
                long financeRecoOrderItemSkuId = idGenerateService.getSnowflakeId();
                FinanceRecoOrderItemSkuPo financeRecoOrderItemSkuPo = FinanceRecoOrderItemConverter.collectToItemSkuPo(
                        insertRecoOrderItemPo,
                        qualityGoodsCnt,
                        skuBatchCodePrice,
                        totalPrice,
                        purchaseDeliverOrderItemPo.getPurchaseDeliverOrderItemId(),
                        financeRecoOrderItemSkuId,
                        purchaseDeliverOrderItemPo.getSku(),
                        purchaseDeliverOrderItemPo.getSkuBatchCode()
                );

                // 获取收货单上架数量
                List<ReceiveOrderForScmVo> receiveOrderForScmVoList = receiveOrderForScmVos.stream()
                        .filter(receiveOrderForScmVo -> receiveOrderForScmVo.getScmBizNo()
                                .equals(purchaseDeliverOrderPo.getPurchaseDeliverOrderNo()))
                        .collect(Collectors.toList());
                Integer inspectNum = 0;
                for (ReceiveOrderForScmVo receiveOrderForScmVo : receiveOrderForScmVoList) {
                    List<ReceiveOrderForScmVo.ReceiveDeliver> receiveDeliverList
                            = receiveOrderForScmVo.getReceiveDeliverList();
                    ReceiveOrderForScmVo.ReceiveDeliver receiveDeliverInfo = receiveDeliverList.stream()
                            .filter(receiveDeliver -> receiveDeliver.getBatchCode()
                                    .equals(purchaseDeliverOrderItemPo.getSkuBatchCode()))
                            .filter(receiveDeliver -> receiveDeliver.getSkuCode()
                                    .equals(purchaseDeliverOrderItemPo.getSku()))
                            .findFirst()
                            .orElse(null);
                    if (null != receiveDeliverInfo) {
                        inspectNum = receiveDeliverInfo.getOnShelvesAmount();
                    }
                }


                // 扣款单收单计算逻辑
                CollectDeductSupplementBo collectDeductBo = this.collectDeductOrder(deductAndStatusBoList,
                        financeRecoOrderItemRelationMap,
                        financeRecoOrderItemSkuPo,
                        purchaseDeliverOrderPo.getPurchaseDeliverOrderNo(),
                        purchaseDeliverOrderItemPo.getSku());

                // 补款单收单计算逻辑
                CollectDeductSupplementBo collectSupplementBo = this.collectSupplementOrder(supplementAndStatusBoList,
                        financeRecoOrderItemRelationMap,
                        financeRecoOrderItemSkuPo,
                        purchaseDeliverOrderPo.getPurchaseDeliverOrderNo(),
                        purchaseDeliverOrderItemPo.getSku());

                // 计算扣款金额
                financeRecoOrderItemSkuPo.setTotalPrice(this.deductCount(financeRecoOrderItemSkuPo.getTotalPrice(),
                        collectDeductBo.getTotalPrice(),
                        financeRecoOrderItemSkuPo.getFinanceRecoPayType()));
                insertRecoOrderItemRelationPoList.addAll(collectDeductBo.getInsertRecoOrderItemRelationPoList());

                // 计算补款金额
                financeRecoOrderItemSkuPo.setTotalPrice(this.supplementCount(financeRecoOrderItemSkuPo.getTotalPrice(),
                        collectSupplementBo.getTotalPrice(),
                        financeRecoOrderItemSkuPo.getFinanceRecoPayType()));
                insertRecoOrderItemRelationPoList.addAll(collectSupplementBo.getInsertRecoOrderItemRelationPoList());

                // 创建详情sku数据
                // 重新计算单价
                if (0 == financeRecoOrderItemSkuPo.getNum()) {
                    financeRecoOrderItemSkuPo.setPrice(BigDecimal.ZERO);
                    log.warn("发货单详情PO={}的正品数为0", JacksonUtil.parse2Str(purchaseDeliverOrderItemPo));
                } else {
                    BigDecimal price = financeRecoOrderItemSkuPo.getTotalPrice().divide(new BigDecimal(financeRecoOrderItemSkuPo.getNum()), 2, RoundingMode.HALF_UP);
                    financeRecoOrderItemSkuPo.setPrice(price);
                }
                insertRecoOrderItemSkuPoList.add(financeRecoOrderItemSkuPo);

                // 累加金额和数量
                purchaseSettlePrice = purchaseSettlePrice.add(financeRecoOrderItemSkuPo.getTotalPrice());
                skuNum += qualityGoodsCnt;


                // 创建对应验证数据
                // 数量验证
                insertRecoOrderItemInspectPoList.add(
                        FinanceRecoOrderItemInspectConverter.create(financeRecoOrderItemSkuPo,
                                RecoOrderInspectType.QUANTITY,
                                new BigDecimal(financeRecoOrderItemSkuPo.getNum()),
                                new BigDecimal(inspectNum)));

                // 价格验证
                PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildOrderItemPoList.stream()
                        .filter(itemPo -> itemPo.getPurchaseChildOrderNo()
                                .equals(purchaseDeliverOrderPo.getPurchaseChildOrderNo()))
                        .filter(itemPo -> itemPo.getSkuBatchCode()
                                .equals(purchaseDeliverOrderItemPo.getSkuBatchCode()))
                        .filter(itemPo -> itemPo.getSku()
                                .equals(purchaseDeliverOrderItemPo.getSku()))
                        .findFirst()
                        .orElse(null);
                BigDecimal inspectPrice = BigDecimal.ZERO;
                if (null != purchaseChildOrderItemPo) {
                    inspectPrice = purchaseChildOrderItemPo.getPurchasePrice()
                            .subtract(purchaseChildOrderItemPo.getSubstractPrice());
                }

                // 计算扣款金额
                BigDecimal inspectPriceTotal = inspectPrice.multiply(new BigDecimal(inspectNum));
                inspectPriceTotal = this.deductCount(inspectPriceTotal,
                        collectDeductBo.getTotalPrice(),
                        financeRecoOrderItemSkuPo.getFinanceRecoPayType());

                // 计算补款金额
                inspectPriceTotal = this.supplementCount(inspectPriceTotal,
                        collectSupplementBo.getTotalPrice(),
                        financeRecoOrderItemSkuPo.getFinanceRecoPayType());
                // 重新计算检验单价
                if (0 == inspectNum) {
                    throw new BizException("对账单收单异常，发货单关联采购单已上架数量存在为0情况，请系统管理员处理数据po={}", purchaseDeliverOrderItemPo);
                } else {
                    inspectPrice = inspectPriceTotal.divide(new BigDecimal(inspectNum), 2, RoundingMode.HALF_UP);
                }

                insertRecoOrderItemInspectPoList.add(
                        FinanceRecoOrderItemInspectConverter.create(financeRecoOrderItemSkuPo,
                                RecoOrderInspectType.PRICE,
                                financeRecoOrderItemSkuPo.getPrice(),
                                inspectPrice));

            }
            insertRecoOrderItemPo.setNum(skuNum);
            insertRecoOrderItemPo.setTotalPrice(purchaseSettlePrice);
            insertRecoOrderItemPoList.add(insertRecoOrderItemPo);

            payPrice = payPrice.add(purchaseSettlePrice);
            recoOrderCollectBo.setPayPrice(payPrice);
        }

    }

    /**
     * 扣款单计算规则：减去扣款金额
     *
     * @param totalPrice:单据金额
     * @param deductPrice:扣款金额
     * @param financeRecoPayType:应付应收
     * @return BigDecimal
     * @author ChenWenLong
     * @date 2024/7/15 17:49
     */
    private BigDecimal deductCount(BigDecimal totalPrice,
                                   BigDecimal deductPrice,
                                   FinanceRecoPayType financeRecoPayType) {
        if (null == totalPrice) {
            totalPrice = BigDecimal.ZERO;
        }
        if (null == deductPrice) {
            deductPrice = BigDecimal.ZERO;
        }
        if (FinanceRecoPayType.HANDLE.equals(financeRecoPayType)) {
            return totalPrice.subtract(deductPrice);
        }
        if (FinanceRecoPayType.RECEIVABLE.equals(financeRecoPayType)) {
            return totalPrice.add(deductPrice);
        }

        throw new BizException("应付应收匹配不到对应枚举，请核对对账单计算逻辑是否正确，联系管理员进行处理！");

    }

    /**
     * 补款单计算规则：加补款金额
     *
     * @param totalPrice:单据金额
     * @param supplementPrice:补款金额
     * @param financeRecoPayType:应付应收
     * @return BigDecimal
     * @author ChenWenLong
     * @date 2024/7/15 17:49
     */
    private BigDecimal supplementCount(BigDecimal totalPrice,
                                       BigDecimal supplementPrice,
                                       FinanceRecoPayType financeRecoPayType) {
        if (null == totalPrice) {
            totalPrice = BigDecimal.ZERO;
        }
        if (null == supplementPrice) {
            supplementPrice = BigDecimal.ZERO;
        }
        if (FinanceRecoPayType.HANDLE.equals(financeRecoPayType)) {
            return totalPrice.add(supplementPrice);
        }
        if (FinanceRecoPayType.RECEIVABLE.equals(financeRecoPayType)) {
            return totalPrice.subtract(supplementPrice);
        }

        throw new BizException("应付应收匹配不到对应枚举，请核对对账单计算逻辑是否正确，联系管理员进行处理！");
    }

    /**
     * 获取对账单单号规则
     *
     * @param supplierAlias:
     * @return String
     * @author ChenWenLong
     * @date 2024/5/28 18:18
     */
    private String createFinanceRecoOrderNo(String supplierAlias) {
        return idGenerateService.getConfuseCode(ScmConstant.FINANCE_RECO_ORDER_NO_PREFIX
                        + ScmFormatUtil.subStringLastThree(supplierAlias),
                TimeType.CN_DAY, ConfuseLength.L_4);
    }


    /**
     * 获取跟单采购员的供应商
     *
     * @param followUser:
     * @return List<String>
     * @author ChenWenLong
     * @date 2024/5/30 18:31
     */
    public List<String> getSupplierCodeListByFollower(String followUser) {
        if (StringUtils.isBlank(followUser)) {
            return Collections.emptyList();
        }
        final List<SupplierPo> supplierPoList = supplierDao.getSupplierPoListByFollowUser(followUser);
        if (CollectionUtils.isEmpty(supplierPoList)) {
            return Collections.emptyList();
        }
        return supplierPoList.stream()
                .map(SupplierPo::getSupplierCode)
                .collect(Collectors.toList());
    }

    /**
     * 审批同意
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/31 09:38
     */
    public void approveWorkFlow(RecoOrderApproveDto dto) {
        final FinanceRecoOrderPo financeRecoOrderPo = financeRecoOrderDao.getOneByNoAndVersion(
                dto.getFinanceRecoOrderNo(), dto.getVersion());
        Assert.notNull(financeRecoOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        if (!FinanceRecoOrderStatus.UNDER_REVIEW.equals(financeRecoOrderPo.getFinanceRecoOrderStatus())) {
            throw new ParamIllegalException("对账单状态非{}，无法进行此操作，请刷新页面后重试！",
                    FinanceRecoOrderStatus.UNDER_REVIEW.getRemark());
        }
        if (!GlobalContext.getUserKey()
                .equals(financeRecoOrderPo.getCtrlUser())) {
            throw new ParamIllegalException("当前用户无法对对账单:{}进行审批操作，请刷新后重试！",
                    financeRecoOrderPo.getFinanceRecoOrderNo());
        }
        mcRemoteService.approveWorkFlow(dto);
    }

    /**
     * 审批拒绝
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/31 09:38
     */
    public void rejectWorkFlow(RecoOrderApproveDto dto) {
        final FinanceRecoOrderPo financeRecoOrderPo = financeRecoOrderDao.getOneByNoAndVersion(
                dto.getFinanceRecoOrderNo(), dto.getVersion());
        Assert.notNull(financeRecoOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        if (!FinanceRecoOrderStatus.UNDER_REVIEW.equals(financeRecoOrderPo.getFinanceRecoOrderStatus())) {
            throw new ParamIllegalException("对账单状态非{}，无法进行此操作，请刷新页面后重试！",
                    FinanceRecoOrderStatus.UNDER_REVIEW.getRemark());
        }
        if (!GlobalContext.getUserKey()
                .equals(financeRecoOrderPo.getCtrlUser())) {
            throw new ParamIllegalException("当前用户无法对对账单:{}进行审批操作，请刷新后重试！",
                    financeRecoOrderPo.getFinanceRecoOrderNo());
        }
        mcRemoteService.rejectWorkFlow(dto);
    }

    /**
     * 收单时计算扣款的数据
     *
     * @param deductAndStatusBoList:
     * @param financeRecoOrderItemRelationMap:
     * @param financeRecoOrderItemSkuPo:
     * @param businessNo:
     * @param sku:
     * @return CollectDeductSupplementBo
     * @author ChenWenLong
     * @date 2024/6/14 17:20
     */
    private CollectDeductSupplementBo collectDeductOrder(List<DeductSupplementAndStatusBo> deductAndStatusBoList,
                                                         Map<Long, List<RecoOrderItemRelationCheckVo>> financeRecoOrderItemRelationMap,
                                                         FinanceRecoOrderItemSkuPo financeRecoOrderItemSkuPo,
                                                         String businessNo,
                                                         String sku) {
        // 查询扣款单数据
        BigDecimal deductPrice = BigDecimal.ZERO;
        // 创建关联使用单据数据
        List<FinanceRecoOrderItemRelationPo> insertRecoOrderItemRelationPoList = new ArrayList<>();

        // 通过单据号+sku匹配数据
        List<DeductSupplementAndStatusBo> deductBoList = deductAndStatusBoList.stream()
                .filter(deductAndStatusBo -> deductAndStatusBo.getBusinessNo().equals(businessNo))
                .filter(deductAndStatusBo -> StringUtils.isNotBlank(deductAndStatusBo.getSku()) && deductAndStatusBo.getSku().equals(sku))
                .collect(Collectors.toList());
        // 如果数据为空就通过单据号匹配数据
        if (deductBoList.isEmpty()) {
            deductBoList = deductAndStatusBoList.stream()
                    .filter(deductAndStatusBo -> deductAndStatusBo.getBusinessNo().equals(businessNo))
                    .collect(Collectors.toList());
        }
        for (DeductSupplementAndStatusBo deductSupplementAndStatusBo : deductBoList) {
            if (financeRecoOrderItemRelationMap.containsKey(deductSupplementAndStatusBo.getBusinessId())) {
                log.warn("扣款单:{}的详情ID:{}已经被收集了，对应收集的对账单信息List={}", deductSupplementAndStatusBo.getDeductSupplementNo(),
                        deductSupplementAndStatusBo.getBusinessId(),
                        JacksonUtil.parse2Str(financeRecoOrderItemRelationMap.get(deductSupplementAndStatusBo.getBusinessId())));
                continue;
            }
            insertRecoOrderItemRelationPoList.add(FinanceRecoOrderItemRelationConverter.create(financeRecoOrderItemSkuPo,
                    RecoOrderItemRelationType.DEDUCT,
                    deductSupplementAndStatusBo.getBusinessId(),
                    deductSupplementAndStatusBo.getDeductSupplementNo(),
                    deductSupplementAndStatusBo.getSku(),
                    deductSupplementAndStatusBo.getNum(),
                    deductSupplementAndStatusBo.getTotalPrice()
            ));
            if (null != deductSupplementAndStatusBo.getTotalPrice()) {
                deductPrice = deductPrice.add(deductSupplementAndStatusBo.getTotalPrice());
            }

            // 删除已经使用的BO
            Iterator<DeductSupplementAndStatusBo> iterator = deductAndStatusBoList.iterator();
            while (iterator.hasNext()) {
                DeductSupplementAndStatusBo item = iterator.next();
                if (item.equals(deductSupplementAndStatusBo)) {
                    iterator.remove();
                    break; // 一旦找到并删除就可以退出当前循环
                }
            }
        }
        CollectDeductSupplementBo collectDeductSupplementBo = new CollectDeductSupplementBo();
        collectDeductSupplementBo.setTotalPrice(deductPrice);
        collectDeductSupplementBo.setInsertRecoOrderItemRelationPoList(insertRecoOrderItemRelationPoList);
        return collectDeductSupplementBo;
    }

    /**
     * 收单时计算补款的数据
     *
     * @param supplementAndStatusBoList:
     * @param financeRecoOrderItemRelationMap:
     * @param financeRecoOrderItemSkuPo:
     * @param businessNo:
     * @param sku:
     * @return CollectDeductSupplementBo
     * @author ChenWenLong
     * @date 2024/6/14 17:28
     */
    private CollectDeductSupplementBo collectSupplementOrder(List<DeductSupplementAndStatusBo> supplementAndStatusBoList,
                                                             Map<Long, List<RecoOrderItemRelationCheckVo>> financeRecoOrderItemRelationMap,
                                                             FinanceRecoOrderItemSkuPo financeRecoOrderItemSkuPo,
                                                             String businessNo,
                                                             String sku) {
        // 查询补款单数据
        BigDecimal supplementPrice = BigDecimal.ZERO;
        // 创建关联使用单据数据
        List<FinanceRecoOrderItemRelationPo> insertRecoOrderItemRelationPoList = new ArrayList<>();

        List<DeductSupplementAndStatusBo> supplementBoList = supplementAndStatusBoList.stream()
                .filter(supplementAndStatusBo -> supplementAndStatusBo.getBusinessNo().equals(businessNo))
                .filter(supplementAndStatusBo -> StringUtils.isNotBlank(supplementAndStatusBo.getSku()) && supplementAndStatusBo.getSku().equals(sku))
                .collect(Collectors.toList());
        // 如果数据为空就通过单据号匹配数据
        if (supplementBoList.isEmpty()) {
            supplementBoList = supplementAndStatusBoList.stream()
                    .filter(supplementAndStatusBo -> supplementAndStatusBo.getBusinessNo().equals(businessNo))
                    .collect(Collectors.toList());
        }
        for (DeductSupplementAndStatusBo deductSupplementAndStatusBo : supplementBoList) {
            if (financeRecoOrderItemRelationMap.containsKey(deductSupplementAndStatusBo.getBusinessId())) {
                log.warn("补款单:{}的详情ID:{}已经被收集了，对应收集的对账单信息List={}", deductSupplementAndStatusBo.getDeductSupplementNo(),
                        deductSupplementAndStatusBo.getBusinessId(),
                        JacksonUtil.parse2Str(financeRecoOrderItemRelationMap.get(deductSupplementAndStatusBo.getBusinessId())));
                continue;
            }
            insertRecoOrderItemRelationPoList.add(FinanceRecoOrderItemRelationConverter.create(financeRecoOrderItemSkuPo,
                    RecoOrderItemRelationType.SUPPLEMENT,
                    deductSupplementAndStatusBo.getBusinessId(),
                    deductSupplementAndStatusBo.getDeductSupplementNo(),
                    deductSupplementAndStatusBo.getSku(),
                    deductSupplementAndStatusBo.getNum(),
                    deductSupplementAndStatusBo.getTotalPrice()
            ));
            if (null != deductSupplementAndStatusBo.getTotalPrice()) {
                supplementPrice = supplementPrice.add(deductSupplementAndStatusBo.getTotalPrice());
            }

            // 删除已经使用的BO
            Iterator<DeductSupplementAndStatusBo> iterator = supplementAndStatusBoList.iterator();
            while (iterator.hasNext()) {
                DeductSupplementAndStatusBo item = iterator.next();
                if (item.equals(deductSupplementAndStatusBo)) {
                    iterator.remove();
                    break; // 一旦找到并删除就可以退出当前循环
                }
            }
        }
        CollectDeductSupplementBo collectDeductSupplementBo = new CollectDeductSupplementBo();
        collectDeductSupplementBo.setTotalPrice(supplementPrice);
        collectDeductSupplementBo.setInsertRecoOrderItemRelationPoList(insertRecoOrderItemRelationPoList);
        return collectDeductSupplementBo;
    }

    /**
     * 批量确认单据
     *
     * @param financeRecoOrderItemSkuPoList:
     * @return void
     * @author ChenWenLong
     * @date 2024/7/2 16:47
     */
    public void batchConfirmRecoOrderItem(List<FinanceRecoOrderItemSkuPo> financeRecoOrderItemSkuPoList) {

        if (CollectionUtils.isEmpty(financeRecoOrderItemSkuPoList)) {
            return;
        }

        // 查询对账单信息
        List<String> financeRecoOrderNoList = financeRecoOrderItemSkuPoList.stream()
                .map(FinanceRecoOrderItemSkuPo::getFinanceRecoOrderNo).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(financeRecoOrderNoList)) {
            throw new BizException("查询不到对账单信息，数据已被修改或删除，请刷新页面后重试！");
        }

        List<FinanceRecoOrderPo> financeRecoOrderPoList = financeRecoOrderDao.getListByNoList(financeRecoOrderNoList);
        Assert.notEmpty(financeRecoOrderPoList, () -> new BizException("查询不到对账单信息，数据已被修改或删除，请刷新页面后重试！"));
        String userKey = GlobalContext.getUserKey();
        // 验证对账单信息
        for (FinanceRecoOrderPo financeRecoOrderPo : financeRecoOrderPoList) {
            if (!FinanceRecoOrderStatus.WAIT_SUBMIT.equals(financeRecoOrderPo.getFinanceRecoOrderStatus())) {
                throw new ParamIllegalException("对账单:{}状态非{}，无法进行此操作，请刷新页面后重试！",
                        financeRecoOrderPo.getFinanceRecoOrderNo(),
                        FinanceRecoOrderStatus.WAIT_SUBMIT.getRemark());
            }
            if (!userKey.equals(financeRecoOrderPo.getCtrlUser())) {
                throw new ParamIllegalException("对账单:{}的处理人不是当前登录账号，无法进行此操作！",
                        financeRecoOrderPo.getFinanceRecoOrderNo());
            }
        }

        // 验证收单和收单状态
        for (FinanceRecoOrderItemSkuPo recoOrderItemSkuPo : financeRecoOrderItemSkuPoList) {
            if (RecoOrderItemSkuStatus.CONFIRMED.equals(recoOrderItemSkuPo.getRecoOrderItemSkuStatus())) {
                throw new ParamIllegalException("收单单据:{}状态{}，无法进行此操作，请刷新页面后重试！",
                        recoOrderItemSkuPo.getCollectOrderNo(),
                        recoOrderItemSkuPo.getRecoOrderItemSkuStatus()
                                .getRemark());
            }
            recoOrderItemSkuPo.setRecoOrderItemSkuStatus(RecoOrderItemSkuStatus.CONFIRMED);
        }

        financeRecoOrderItemSkuDao.updateBatchByIdVersion(financeRecoOrderItemSkuPoList);
    }


    /**
     * 删除已收单的对账单金额为0的任务
     *
     * @param param: 多个供应商按逗号分割
     * @return void
     * @author ChenWenLong
     * @date 2024/7/4 16:33
     */
    @Transactional(rollbackFor = Exception.class)
    public void delRecoOrderAmountEqZeroTask(String param) {
        List<String> supplierCodeList = new ArrayList<>();
        if (StringUtils.isNotBlank(param)) {
            supplierCodeList.addAll(Arrays.asList(param.split(",")));
        }
        List<FinanceRecoOrderPo> financeRecoOrderPoList = financeRecoOrderDao.getListByPriceAndStatus(FinanceRecoOrderStatus.WAIT_SUBMIT,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                supplierCodeList);
        recoOrderBaseService.delRecoOrderAmountEqZero(financeRecoOrderPoList);
    }

    /**
     * 获取对账单的单据详情信息
     *
     * @param dto:
     * @return RecoOrderItemSkuDetailVo
     * @author ChenWenLong
     * @date 2024/7/15 17:01
     */
    public RecoOrderItemSkuDetailVo getRecoOrderItemSkuDetail(RecoOrderItemSkuIdDto dto) {
        FinanceRecoOrderItemSkuPo financeRecoOrderItemSkuPo = financeRecoOrderItemSkuDao.getById(dto.getFinanceRecoOrderItemSkuId());
        if (null == financeRecoOrderItemSkuPo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }

        // 获取检验信息
        final List<FinanceRecoOrderItemInspectPo> financeRecoOrderItemInspectPoList = financeRecoOrderItemInspectDao.getListByRecoOrderItemSkuId(financeRecoOrderItemSkuPo.getFinanceRecoOrderItemSkuId());

        // 获取关联使用单据
        List<FinanceRecoOrderItemRelationPo> financeRecoOrderItemRelationPoList = financeRecoOrderItemRelationDao.getListByRecoOrderItemSkuId(financeRecoOrderItemSkuPo.getFinanceRecoOrderItemSkuId());

        //获取文件附件
        List<String> scmFileList = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.RECO_ORDER_ITEM_SKU_FILE, List.of(financeRecoOrderItemSkuPo.getFinanceRecoOrderItemSkuId()));

        return FinanceRecoOrderItemConverter.poToVo(financeRecoOrderItemSkuPo,
                financeRecoOrderItemInspectPoList,
                financeRecoOrderItemRelationPoList,
                scmFileList);

    }
}
