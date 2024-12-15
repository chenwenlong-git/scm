package com.hete.supply.scm.server.scm.qc.service.base;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.defect.dao.DefectHandlingDao;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.entity.dto.BizLogCreateMqDto;
import com.hete.supply.scm.server.scm.handler.LogVersionHandler;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderBizService;
import com.hete.supply.scm.server.scm.process.service.biz.RepairOrderBizService;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.qc.config.QcConfig;
import com.hete.supply.scm.server.scm.qc.converter.QcOrderConverter;
import com.hete.supply.scm.server.scm.qc.dao.QcDetailDao;
import com.hete.supply.scm.server.scm.qc.dao.QcOrderDao;
import com.hete.supply.scm.server.scm.qc.dao.QcReceiveOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.bo.*;
import com.hete.supply.scm.server.scm.qc.entity.dto.BizNoQcCreateRequestDto;
import com.hete.supply.scm.server.scm.qc.entity.dto.QcOrderChangeDto;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcReceiveOrderPo;
import com.hete.supply.scm.server.scm.qc.enums.QcBizOperate;
import com.hete.supply.scm.server.scm.qc.handler.AbstractQcOrderStatusHandler;
import com.hete.supply.scm.server.scm.qc.handler.ProcessOrderQcOrderStatusHandler;
import com.hete.supply.scm.server.scm.qc.handler.ReceiveOrderQcOrderUpdateStatusHandler;
import com.hete.supply.scm.server.scm.qc.service.biz.OnShelvesOrderBizService;
import com.hete.supply.scm.server.scm.qc.service.ref.QcOrderRefService;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.service.base.PurchaseDeliverBaseService;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class QcOrderBaseService {
    private final QcOrderDao qcOrderDao;
    private final QcDetailDao qcDetailDao;
    private final ProcessOrderBizService processOrderBizService;
    private final ConsistencySendMqService consistencySendMqService;
    private final IdGenerateService idGenerateService;
    private final OnShelvesOrderBizService onShelvesOrderBizService;
    private final DefectHandlingDao defectHandlingDao;
    private final PurchaseReturnOrderItemDao purchaseReturnOrderItemDao;
    private final WmsRemoteService wmsRemoteService;
    private final PurchaseDeliverBaseService purchaseDeliverBaseService;
    private final RepairOrderBizService repairOrderBizService;
    private final QcReceiveOrderDao qcReceiveOrderDao;
    private final QcOrderRefService qcOrderRefService;
    private final PlmRemoteService plmRemoteService;
    private final QcConfig qcConfig;

    /**
     * 处理质检单状态变更。
     *
     * @param qcOrderNo 质检单号
     * @param qcState   质检单状态
     */
    public void handleStatusChange(@Valid @NotBlank String qcOrderNo,
                                   @NotNull QcState qcState,
                                   QcBizOperate qcBizOperate) {
        final QcOrderPo qcOrderPo = qcOrderDao.getByQcOrderNo(qcOrderNo);
        if (Objects.isNull(qcOrderPo)) {
            log.error("handlerStatusChange error qcOrderPo is null qcOrderNo:{}", qcOrderNo);
            return;
        }
        final List<QcDetailPo> qcDetailPos = qcDetailDao.getListByQcOrderNo(qcOrderNo);

        // 加工质检
        boolean isProcessQc = StrUtil.isNotBlank(qcOrderPo.getProcessOrderNo());
        // 驻场质检
        boolean residentQc = this.isResidentQc(qcOrderNo);
        // 出库质检
        boolean outBoundQc = this.isOutBoundQc(qcOrderNo);

        if (isProcessQc) {
            AbstractQcOrderStatusHandler<ProcessOrderQcOrderFinishedBo> qcStatusHandler
                    = new ProcessOrderQcOrderStatusHandler(processOrderBizService, qcState);
            qcStatusHandler.handlePostStatusChange(QcOrderConverter.toProcessOrderQcOrderBo(qcOrderPo, qcDetailPos));
        } else if (residentQc) {
            // 处理驻场质检状态变更
            log.info("驻场质检单{}状态变更:{}", qcOrderNo, qcState.getRemark());
        } else if (outBoundQc) {
            log.info("出库质检单{}状态变更:{}", qcOrderNo, qcState.getRemark());
        } else {
            qcOrderPo.setQcState(qcState);
            AbstractQcOrderStatusHandler<QcOrderChangeDto> qcStatusHandler
                    = new ReceiveOrderQcOrderUpdateStatusHandler(consistencySendMqService, idGenerateService, qcBizOperate);
            qcStatusHandler.handlePostStatusChange(QcOrderConverter.toQcOrderChangeDto(qcOrderPo, qcDetailPos));
        }
    }

    /**
     * 执行质检单巡检操作，根据指定的时间范围和质检单状态进行巡检。
     *
     * @param beforeDay 巡检的时间范围，表示前几天的质检单
     */
    public void performInspection(int beforeDay) {
        AbstractQcOrderInspection defectHandlingInspection
                = new DefectHandlingQcOrderInspection(qcOrderDao, defectHandlingDao);
        defectHandlingInspection.doInspection(beforeDay, QcState.DEFECT_HANDLING);

        AbstractQcOrderInspection finishedInspection
                = new FinishedQcOrderInspection(qcOrderDao, defectHandlingDao, onShelvesOrderBizService,
                purchaseReturnOrderItemDao, this);
        finishedInspection.doInspection(beforeDay, QcState.FINISHED, true);

        AbstractQcOrderInspection wmsSyncQcOrderInspection
                = new WmsSyncQcOrderInspection(qcOrderDao, wmsRemoteService, this);
        wmsSyncQcOrderInspection.doInspection(beforeDay, QcState.WAIT_HAND_OVER, true);
        wmsSyncQcOrderInspection.doInspection(beforeDay, QcState.FINISHED, true);
    }

    /**
     * 统计待质检数据
     *
     * @param skuCodeList:
     * @return List<QcWaitDetailBo>
     * @author ChenWenLong
     * @date 2023/12/26 15:06
     */
    public List<QcWaitDetailBo> getQcWaitDetailBySkuCodeList(List<String> skuCodeList) {
        List<QcState> qcStateList = Lists.newArrayList(QcState.WAIT_HAND_OVER,
                QcState.TO_BE_QC, QcState.QCING, QcState.TO_BE_AUDIT,
                QcState.DEFECT_HANDLING);
        return qcDetailDao.getQcWaitDetail(skuCodeList, qcStateList);
    }


    /**
     * 日志
     *
     * @param qcOrderPo:
     * @return void
     * @author ChenWenLong
     * @date 2023/12/27 11:16
     */
    public void createStatusChangeLog(QcOrderPo qcOrderPo) {
        BizLogCreateMqDto bizLogCreateMqDto = new BizLogCreateMqDto();
        bizLogCreateMqDto.setBizLogCode(idGenerateService.getSnowflakeCode(LogBizModule.QC_ORDER_STATUS.name()));
        bizLogCreateMqDto.setBizSystemCode(ScmConstant.SYSTEM_CODE);
        bizLogCreateMqDto.setLogVersion(ScmConstant.QC_ORDER_LOG_VERSION);
        bizLogCreateMqDto.setBizModule(LogBizModule.QC_ORDER_STATUS.name());
        bizLogCreateMqDto.setBizCode(qcOrderPo.getQcOrderNo());
        bizLogCreateMqDto.setOperateTime(DateUtil.current());
        String userKey = StringUtils.isNotBlank(
                GlobalContext.getUserKey()) ? GlobalContext.getUserKey() : ScmConstant.SYSTEM_USER;
        String username = StringUtils.isNotBlank(
                GlobalContext.getUsername()) ? GlobalContext.getUsername() : ScmConstant.MQ_DEFAULT_USER;
        bizLogCreateMqDto.setOperateUser(userKey);
        bizLogCreateMqDto.setOperateUsername(username);

        ArrayList<LogVersionBo> logVersionBos = new ArrayList<>();

        bizLogCreateMqDto.setContent(qcOrderPo.getQcState()
                .getRemark());

        bizLogCreateMqDto.setDetail(logVersionBos);

        consistencySendMqService.execSendMq(LogVersionHandler.class, bizLogCreateMqDto);
    }

    public QcOrigin getQcOrigin(ReceiveOrderQcOrderBo createQcOrderBo) {
        // 加工&返修
        WmsEnum.ReceiveType receiveType = createQcOrderBo.getReceiveType();
        if (Objects.equals(WmsEnum.ReceiveType.PROCESS_PRODUCT, receiveType)) {
            String scmBizNo = createQcOrderBo.getScmBizNo();
            QcOrigin qcOriginByScmBizNo = getQcOriginByScmBizNo(scmBizNo);
            if (Objects.nonNull(qcOriginByScmBizNo)) {
                return qcOriginByScmBizNo;
            }
        }
        final String deliverOrderNo = createQcOrderBo.getDeliverOrderNo();
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseDeliverBaseService.getPurchaseChildPoByNo(
                deliverOrderNo);
        if (null != purchaseChildOrderPo) {
            final PurchaseBizType purchaseBizType = purchaseChildOrderPo.getPurchaseBizType();
            if (PurchaseBizType.PRODUCT.equals(purchaseBizType)) {
                return QcOrigin.PRODUCT;
            } else if (PurchaseBizType.PROCESS.equals(purchaseBizType)) {
                return QcOrigin.PROCESS;
            }
        }
        // 其他入库类型获取质检来源
        return this.getQcOrderProperty(createQcOrderBo.getReceiveType());
    }

    public QcOriginProperty getQcOriginProperty(ReceiveOrderQcOrderBo createQcOrderBo) {
        // 加工&返修
        WmsEnum.ReceiveType receiveType = createQcOrderBo.getReceiveType();
        if (Objects.equals(WmsEnum.ReceiveType.PROCESS_PRODUCT, receiveType)) {
            String scmBizNo = createQcOrderBo.getScmBizNo();
            QcOriginProperty qcOriginPropertyByScmBizNo = getQcOriginPropertyByScmBizNo(scmBizNo);
            if (Objects.nonNull(qcOriginPropertyByScmBizNo)) {
                return qcOriginPropertyByScmBizNo;
            }
        }

        final String deliverOrderNo = createQcOrderBo.getDeliverOrderNo();
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseDeliverBaseService.getPurchaseChildPoByNo(
                deliverOrderNo);
        if (null != purchaseChildOrderPo) {
            final PurchaseOrderType purchaseOrderType = purchaseChildOrderPo.getPurchaseOrderType();
            return qcOrderRefService.mapPurchaseOrderTypeToQcOriginProperty(purchaseOrderType);
        }

        // 其他入库类型获取属性
        if (null != this.getQcOrderProperty(createQcOrderBo.getReceiveType())) {
            return QcOriginProperty.NORMAL;
        }
        return null;
    }

    /**
     * 获取生存质检单质检单对应质检类型=入库类型、质检标识=常规的枚举
     *
     * @param wmsReceiveType:
     * @return ReceiveType
     * @author ChenWenLong
     * @date 2024/3/6 13:46
     */
    public QcOrigin getQcOrderProperty(WmsEnum.ReceiveType wmsReceiveType) {

        if (WmsEnum.ReceiveType.TRANSFER.equals(wmsReceiveType)) {
            return QcOrigin.TRANSFER;
        }
        if (WmsEnum.ReceiveType.PROCESS_MATERIAL.equals(wmsReceiveType)) {
            return QcOrigin.PROCESS_MATERIAL;
        }
        if (WmsEnum.ReceiveType.SAMPLE.equals(wmsReceiveType)) {
            return QcOrigin.SAMPLE;
        }
        if (WmsEnum.ReceiveType.SALE_RETURN.equals(wmsReceiveType)) {
            return QcOrigin.SALE_RETURN;
        }
        if (WmsEnum.ReceiveType.PROFIT.equals(wmsReceiveType)) {
            return QcOrigin.PROFIT;
        }
        if (WmsEnum.ReceiveType.RETURN.equals(wmsReceiveType)) {
            return QcOrigin.RETURN;
        }
        if (WmsEnum.ReceiveType.DEFECTIVE_PROCESS_PRODUCT.equals(wmsReceiveType)) {
            return QcOrigin.DEFECTIVE_PROCESS_PRODUCT;
        }
        if (WmsEnum.ReceiveType.OTHER.equals(wmsReceiveType)) {
            return QcOrigin.OTHER;
        }
        if (WmsEnum.ReceiveType.CHANGE_GOODS.equals(wmsReceiveType)) {
            return QcOrigin.CHANGE_GOODS;
        }
        if (WmsEnum.ReceiveType.INSIDE_CHECK.equals(wmsReceiveType)) {
            return QcOrigin.INSIDE_CHECK;
        }
        if (WmsEnum.ReceiveType.FAST_SALE.equals(wmsReceiveType)) {
            return QcOrigin.FAST_SALE;
        }
        if (WmsEnum.ReceiveType.PREPARE_ORDER.equals(wmsReceiveType)) {
            return QcOrigin.PREPARE_ORDER;
        }

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean initProcAndRepairQcOrigin(String qcOrderNo) {
        QcOrderPo qcOrderPo = qcOrderDao.getByQcOrderNo(qcOrderNo);
        if (Objects.isNull(qcOrderPo)) {
            return false;
        }

        String processOrderNo = qcOrderPo.getProcessOrderNo();
        if (StrUtil.isNotBlank(processOrderNo)) {
            QcOriginBo procQcOriginBo = processOrderBizService.getQcOriginBo(processOrderNo);
            if (Objects.nonNull(procQcOriginBo)) {
                qcOrderPo.setQcOrigin(procQcOriginBo.getQcOrigin());
                qcOrderPo.setQcOriginProperty(procQcOriginBo.getQcOriginProperty());
                qcOrderDao.updateByIdVersion(qcOrderPo);
                return true;
            }
        }

        String repairOrderNo = qcOrderPo.getRepairOrderNo();
        if (StrUtil.isNotBlank(repairOrderNo)) {
            QcOriginBo repairQcOriginBo = repairOrderBizService.getQcOriginBo(repairOrderNo);
            if (Objects.nonNull(repairQcOriginBo)) {
                qcOrderPo.setQcOrigin(repairQcOriginBo.getQcOrigin());
                qcOrderPo.setQcOriginProperty(repairQcOriginBo.getQcOriginProperty());
                qcOrderDao.updateByIdVersion(qcOrderPo);
                return true;
            }
        }

        String receiveOrderNo = qcOrderPo.getReceiveOrderNo();
        if (StrUtil.isNotBlank(receiveOrderNo)) {
            QcReceiveOrderPo receiveOrder = qcReceiveOrderDao.getOneByReceiveNo(receiveOrderNo);
            if (Objects.nonNull(receiveOrder)) {
                WmsEnum.ReceiveType receiveType = receiveOrder.getReceiveType();
                if (Objects.equals(WmsEnum.ReceiveType.PROCESS_PRODUCT, receiveType)) {
                    String scmBizNo = receiveOrder.getScmBizNo();
                    if (StrUtil.isNotBlank(scmBizNo)) {
                        QcOrigin qcOriginByScmBizNo = getQcOriginByScmBizNo(scmBizNo);
                        QcOriginProperty qcOriginPropertyByScmBizNo = getQcOriginPropertyByScmBizNo(scmBizNo);
                        if (Objects.nonNull(qcOriginByScmBizNo) && Objects.nonNull(qcOriginPropertyByScmBizNo)) {
                            qcOrderPo.setQcOrigin(qcOriginByScmBizNo);
                            qcOrderPo.setQcOriginProperty(qcOriginPropertyByScmBizNo);
                            qcOrderDao.updateByIdVersion(qcOrderPo);
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }


    public QcOrigin getQcOriginByScmBizNo(String scmBizNo) {
        QcOriginBo processOrderQcOriginBo = processOrderBizService.getQcOriginBo(scmBizNo);
        if (Objects.nonNull(processOrderQcOriginBo)) {
            return processOrderQcOriginBo.getQcOrigin();
        }

        QcOriginBo repairQcOriginBo = repairOrderBizService.getQcOriginBo(scmBizNo);
        if (Objects.nonNull(repairQcOriginBo)) {
            return repairQcOriginBo.getQcOrigin();
        }
        return null;
    }

    public QcOriginProperty getQcOriginPropertyByScmBizNo(String scmBizNo) {
        QcOriginBo processOrderQcOriginBo = processOrderBizService.getQcOriginBo(scmBizNo);
        if (Objects.nonNull(processOrderQcOriginBo)) {
            return processOrderQcOriginBo.getQcOriginProperty();
        }

        QcOriginBo repairQcOriginBo = repairOrderBizService.getQcOriginBo(scmBizNo);
        if (Objects.nonNull(repairQcOriginBo)) {
            return repairQcOriginBo.getQcOriginProperty();
        }
        return null;
    }

    /**
     * 判断给定的 qcOrderNo 对象是否是驻场质检。
     *
     * @param qcOrderNo 质检单号
     * @return 如果是驻场质检则返回 true，否则返回 false
     */
    public boolean isResidentQc(String qcOrderNo) {
        QcOrderPo qcOrderPo = ParamValidUtils.requireNotNull(
                qcOrderDao.getByQcOrderNo(qcOrderNo), "质检单信息删除或者被更新，请刷新页面！"
        );
        List<QcOrigin> residentQcOrigins = getResidentQcOrigins();
        QcOrigin qcOrigin = qcOrderPo.getQcOrigin();
        return residentQcOrigins.contains(qcOrigin);
    }

    public boolean isOutBoundQc(String qcOrderNo) {
        QcOrderPo qcOrderPo = ParamValidUtils.requireNotNull(
                qcOrderDao.getByQcOrderNo(qcOrderNo), "质检单信息删除或者被更新，请刷新页面！"
        );

        List<QcOrigin> residentQcOrigins = getOutBoundQcOrigins();
        QcOrigin qcOrigin = qcOrderPo.getQcOrigin();
        return residentQcOrigins.contains(qcOrigin);
    }

    /**
     * 获取驻场质检来源列表。
     *
     * @return 包含驻场质检属性列表。
     */
    public List<QcOrigin> getResidentQcOrigins() {
        return List.of(QcOrigin.RESIDENT, QcOrigin.REWORK_RESIDENT);
    }

    public List<QcOrigin> getOutBoundQcOrigins() {
        return List.of(QcOrigin.OUTBOUND);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createObOrderQc(BizNoQcCreateRequestDto bizNoQcCreateRequestDto) {
        String outBoundNo = bizNoQcCreateRequestDto.getOutBoundNo();
        QcOrigin origin = bizNoQcCreateRequestDto.getOrigin();
        QcOriginProperty qcOriginProperty = bizNoQcCreateRequestDto.getQcOriginProperty();
        String warehouseCode = bizNoQcCreateRequestDto.getWarehouseCode();
        String platCode = bizNoQcCreateRequestDto.getPlatCode();

        // 已使用容器编码
        Set<String> usedCtCodes = Sets.newHashSet();

        AbstractQcOrderCreator<BizNoQcCreateRequestDto, OutBoundQcOrderResultBo> creator
                = new OutBoundOrderQcCreator(qcOrderDao, qcDetailDao, idGenerateService, plmRemoteService, this);

        List<BizNoQcCreateRequestDto.CreateBizQcDetailDto> qcDetailList = bizNoQcCreateRequestDto.getCreateBizQcDetailDtoList();
        Map<String, List<BizNoQcCreateRequestDto.CreateBizQcDetailDto>> supQcDetail
                = qcDetailList.stream().collect(Collectors.groupingBy(detail -> StrUtil.isNotBlank(detail.getSupplierCode()) ? detail.getSupplierCode() : ""));
        supQcDetail.forEach((supplierCode, detailList) -> {
            Set<String> ctCodes
                    = detailList.stream().map(BizNoQcCreateRequestDto.CreateBizQcDetailDto::getContainerCode).collect(Collectors.toSet());

            Set<String> filterSet
                    = ctCodes.stream().filter(usedCtCodes::contains).collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(filterSet)) {
                throw new ParamIllegalException("容器{}已被重复占用!请校验后提交。",
                        filterSet.stream().map(String::valueOf).collect(Collectors.joining(",")));
            } else {
                usedCtCodes.addAll(ctCodes);
            }

            BizNoQcCreateRequestDto groupDto = new BizNoQcCreateRequestDto();
            groupDto.setOutBoundNo(outBoundNo);
            groupDto.setPlatCode(platCode);
            groupDto.setOrigin(origin);
            groupDto.setQcOriginProperty(qcOriginProperty);
            groupDto.setWarehouseCode(warehouseCode);
            groupDto.setCreateBizQcDetailDtoList(detailList);
            creator.createQcOrder(groupDto);
        });
    }

    public QcOriginProperty getQcOriginPropertyByWarehouseCodes(String warehouseCode, String targetWarehouseCode) {
        if (StrUtil.isBlank(warehouseCode) && StrUtil.isBlank(targetWarehouseCode)) {
            log.info("入库编码&出库编码为空");
            return QcOriginProperty.NORMAL;
        }

        Map<QcOriginProperty, List<String>> qoWhCodes = qcConfig.getQcOriginWarehouseMapping();
        if (CollectionUtils.isEmpty(qoWhCodes)) {
            throw new BizException("查不到质检类型与仓库编码关系配置！请配置。");
        }

        if (StrUtil.isNotBlank(targetWarehouseCode)) {
            QcOriginProperty matchOriginProperty = qoWhCodes.entrySet().stream()
                    .filter(entry -> entry.getValue().contains(targetWarehouseCode))
                    .map(Map.Entry::getKey)
                    .findFirst().orElse(null);

            QcOriginProperty originProperty = Objects.nonNull(matchOriginProperty) ? matchOriginProperty : QcOriginProperty.NORMAL;
            log.info("通过入库编码质检来源属性:{} 结果:{}", targetWarehouseCode, originProperty.getRemark());
            return originProperty;
        }

        if (StrUtil.isNotBlank(warehouseCode)) {
            QcOriginProperty matchOriginProperty = qoWhCodes.entrySet().stream()
                    .filter(entry -> entry.getValue().contains(warehouseCode))
                    .map(Map.Entry::getKey)
                    .findFirst().orElse(null);

            QcOriginProperty originProperty = Objects.nonNull(matchOriginProperty) ? matchOriginProperty : QcOriginProperty.NORMAL;
            log.info("通过出库编码质检来源属性:{} 结果:{}", warehouseCode, originProperty.getRemark());
            return originProperty;
        }

        return null;
    }
}
