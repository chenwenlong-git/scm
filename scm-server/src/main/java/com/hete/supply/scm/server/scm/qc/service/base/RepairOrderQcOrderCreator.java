package com.hete.supply.scm.server.scm.qc.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.process.entity.bo.RepairQcDetailCreateBo;
import com.hete.supply.scm.server.scm.qc.builder.QcOrderBuilder;
import com.hete.supply.scm.server.scm.qc.dao.QcDetailDao;
import com.hete.supply.scm.server.scm.qc.dao.QcOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.bo.RepairOrderCreateQcBo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ProcessOrderQcOrderCreator 是质检单创建的具体实现类，用于创建质检单相关的信息，包括质检单主体和质检明细。
 * 这个类继承自 AbstractQcOrderCreator，并实现了其中的抽象方法来执行质检单的创建逻辑。
 * 它将校验输入数据，创建质检单和质检明细，然后绑定加工单与质检单关系。
 *
 * @author yanjiawei
 * @date 2023/10/25 09:33
 */
public class RepairOrderQcOrderCreator extends AbstractQcOrderCreator<RepairOrderCreateQcBo, String> {

    private final PlmRemoteService plmRemoteService;

    /**
     * 构造一个 AbstractQcOrderCreator 实例。
     *
     * @param qcOrderDao        质检单数据访问对象，用于操作质检单数据。
     * @param qcDetailDao       质检明细数据访问对象，用于操作质检明细数据。
     * @param idGenerateService ID生成服务，用于生成唯一标识符。
     */
    public RepairOrderQcOrderCreator(QcOrderDao qcOrderDao,
                                     QcDetailDao qcDetailDao,
                                     IdGenerateService idGenerateService,
                                     PlmRemoteService plmRemoteService) {
        super(qcOrderDao, qcDetailDao, idGenerateService);
        this.plmRemoteService = plmRemoteService;
    }

    @Override
    protected void createPreOperations(RepairOrderCreateQcBo input) {

    }

    @Override
    protected String performCreateQcOrder(RepairOrderCreateQcBo repairOrderCreateQcBo) {
        String repairOrderNo = repairOrderCreateQcBo.getRepairOrderNo();
        String warehouseCode = repairOrderCreateQcBo.getWarehouseCode();
        List<RepairQcDetailCreateBo> repairQcDetailCreateBos = repairOrderCreateQcBo.getRepairQcDetailCreateBos();

        // 待质检总数 & 已质检总数
        int totalPendingQcQuantity = repairQcDetailCreateBos.stream()
                .mapToInt(RepairQcDetailCreateBo::getPendingQcQuantity)
                .sum();
        int totalQcPassQuantity = repairQcDetailCreateBos.stream()
                .mapToInt(RepairQcDetailCreateBo::getQcPassQuantity)
                .sum();
        List<String> qcDetailSkus = repairQcDetailCreateBos.stream()
                .map(RepairQcDetailCreateBo::getSku)
                .collect(Collectors.toList());
        //查询sku的分类信息
        Map<String, String> skuSecondCategoriesMap
                = plmRemoteService.getSkuSecondCategoriesCnMapBySkuList(qcDetailSkus);

        QcResult qcResult = totalPendingQcQuantity == totalQcPassQuantity ? QcResult.PASSED : QcResult.FEW_NOT_PASSED;

        // 质检单信息
        String qcOrderNo = idGenerateService.getIncrCode(ScmConstant.QC_CODE, TimeType.CN_DAY, 4);
        QcOrderPo qcOrderPo = new QcOrderPo();
        qcOrderPo.setQcOrderNo(qcOrderNo);
        qcOrderPo.setRepairOrderNo(repairOrderNo);
        qcOrderPo.setQcSourceOrderNo(repairOrderNo);
        qcOrderPo.setQcSourceOrderType(QcSourceOrderType.REPAIR_ORDER_NO);
        qcOrderPo.setQcType(QcType.ALL_CHECK);
        qcOrderPo.setQcAmount(totalPendingQcQuantity);
        qcOrderPo.setQcState(QcState.FINISHED);
        qcOrderPo.setQcResult(qcResult);
        qcOrderPo.setHandOverTime(LocalDateTime.now());
        qcOrderPo.setTaskFinishTime(LocalDateTime.now());
        qcOrderPo.setAuditTime(LocalDateTime.now());
        qcOrderPo.setHandOverUser(GlobalContext.getUsername());
        qcOrderPo.setOperator(GlobalContext.getUserKey());
        qcOrderPo.setOperatorName(GlobalContext.getUsername());
        qcOrderPo.setAuditor(GlobalContext.getUsername());
        qcOrderPo.setAuditTime(LocalDateTime.now());
        qcOrderPo.setWarehouseCode(warehouseCode);
        qcOrderPo.setQcOrigin(QcOrigin.REPAIR_ORDER);
        qcOrderPo.setQcOriginProperty(QcOriginProperty.REPAIR);
        qcOrderDao.insert(qcOrderPo);

        List<QcDetailPo> qcDetailPos = repairQcDetailCreateBos.stream()
                .map(repairQcDetailCreateBo -> {
                    String batchCode = repairQcDetailCreateBo.getBatchCode();
                    String sku = repairQcDetailCreateBo.getSku();
                    String spu = repairQcDetailCreateBo.getSpu();

                    Integer pendingQcQuantity = repairQcDetailCreateBo.getPendingQcQuantity();
                    Integer qcPassQuantity = repairQcDetailCreateBo.getQcPassQuantity();
                    Integer qcFailQuantity = repairQcDetailCreateBo.getQcFailQuantity();

                    QcDetailPo qcDetailPo = new QcDetailPo();
                    qcDetailPo.setQcOrderNo(qcOrderNo);
                    qcDetailPo.setBatchCode(batchCode);
                    qcDetailPo.setSpu(spu);
                    qcDetailPo.setSkuCode(sku);
                    qcDetailPo.setAmount(pendingQcQuantity);
                    qcDetailPo.setHandOverAmount(pendingQcQuantity);
                    qcDetailPo.setWaitAmount(0);
                    qcDetailPo.setPassAmount(qcPassQuantity);
                    qcDetailPo.setNotPassAmount(qcFailQuantity);
                    qcDetailPo.setQcResult(qcFailQuantity > 0 ? QcResult.NOT_PASSED : QcResult.PASSED);
                    qcDetailPo.setCategoryName(skuSecondCategoriesMap.get(qcDetailPo.getSkuCode()));
                    qcDetailPo.setPlatform(repairOrderCreateQcBo.getPlatform());
                    return qcDetailPo;
                })
                .collect(Collectors.toList());
        qcDetailDao.insertBatch(qcDetailPos);

        // 创建质检不合格SKU的正品为0的质检明细
        List<QcDetailPo> notPassedQcDetails = qcDetailPos.stream()
                .filter(qcDetailPo -> Objects.equals(QcResult.NOT_PASSED, qcDetailPo.getQcResult()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(notPassedQcDetails)) {
            for (QcDetailPo notPassedQcDetail : notPassedQcDetails) {
                QcDetailPo matchPassDetail = qcDetailPos.stream()
                        .filter(qcDetailPo -> Objects.equals(qcDetailPo.getSkuCode(), notPassedQcDetail.getSkuCode()) &&
                                Objects.equals(QcResult.PASSED, qcDetailPo.getQcResult()))
                        .findFirst()
                        .orElse(null);
                if (Objects.nonNull(matchPassDetail)) {
                    notPassedQcDetail.setRelationQcDetailId(matchPassDetail.getQcDetailId());
                } else {
                    QcDetailPo passQcDetail = QcOrderBuilder.buildPassQcDetail(notPassedQcDetail);
                    qcDetailDao.insert(passQcDetail);

                    notPassedQcDetail.setAmount(0);
                    notPassedQcDetail.setRelationQcDetailId(passQcDetail.getQcDetailId());
                }
                qcDetailDao.updateByIdVersion(notPassedQcDetail);
            }
        }

        return qcOrderNo;
    }

    @Override
    protected void doAfterCreation(String result) {

    }
}
