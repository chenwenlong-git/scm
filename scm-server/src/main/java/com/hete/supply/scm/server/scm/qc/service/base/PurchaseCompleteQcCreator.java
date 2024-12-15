package com.hete.supply.scm.server.scm.qc.service.base;

import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.qc.dao.QcDetailDao;
import com.hete.supply.scm.server.scm.qc.dao.QcOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.bo.PurchaseCompleteQcBo;
import com.hete.supply.scm.server.scm.qc.entity.bo.PurchaseCompleteQcItemBo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 采购单完成质检创建质检单
 *
 * @author yanjiawei
 * Created on 2024/4/15.
 */
public class PurchaseCompleteQcCreator extends AbstractQcOrderCreator<PurchaseCompleteQcBo, QcOrderPo> {

    private final PlmRemoteService plmRemoteService;
    private final QcOrderLogService qcOrderLogService;

    public PurchaseCompleteQcCreator(QcOrderDao qcOrderDao,
                                     QcDetailDao qcDetailDao,
                                     IdGenerateService idGenerateService,
                                     PlmRemoteService plmRemoteService,
                                     QcOrderLogService qcOrderLogService) {
        super(qcOrderDao, qcDetailDao, idGenerateService);
        this.plmRemoteService = plmRemoteService;
        this.qcOrderLogService = qcOrderLogService;
    }

    /**
     * @Description 采购单创建质检单前置校验
     * @author yanjiawei
     * @Date 2024/4/15 15:48
     */
    @Override
    protected void createPreOperations(PurchaseCompleteQcBo input) {
        // 前置校验
        ParamValidUtils.requireNotBlank(input.getPurchaseChildOrderNo(),
                "创建质检单失败！原因：采购订单号不能为空，请提供有效的采购订单号。");
        ParamValidUtils.requireNotNull(input.getQcOriginProperty(),
                "创建质检单失败！原因：质检标识不能为空，请提供有效的质检标识");
        ParamValidUtils.requireNotBlank(input.getSupplierCode(),
                "创建质检单失败！原因：采购供应商编码不能为空，请提供有效的采购供应商编码。");
        List<PurchaseCompleteQcItemBo> purchaseCompleteQcItemBos
                = ParamValidUtils.requireNotEmpty(input.getPurchaseCompleteQcItemBos(),
                "创建质检单失败！原因：采购明细信息不能为空，请提供有效的采购明细信息。");
        purchaseCompleteQcItemBos.forEach(purchaseCompleteQcItemBo -> {
            // 验证SKU
            ParamValidUtils.requireNotBlank(purchaseCompleteQcItemBo.getSku(),
                    "创建质检单失败！原因:采购明细SKU不能为空，请提供有效的SKU");

            // 验证批次码
            ParamValidUtils.requireNotBlank(purchaseCompleteQcItemBo.getBatchCode(),
                    "创建质检单失败！原因:批次码不能为空，请提供有效的批次码");

            // 验证采购数量
            ParamValidUtils.requireGreaterThan(purchaseCompleteQcItemBo.getQcAmount(), 0,
                    "创建质检单失败！原因:采购数量必须大于0，请提供有效的采购数量");
        });
    }

    @Override
    protected QcOrderPo performCreateQcOrder(PurchaseCompleteQcBo purchaseCompleteQcBo) {
        final QcType qcType = QcType.SAMPLE_CHECK;
        final String qcOrderNo = idGenerateService.getIncrCode(ScmConstant.QC_CODE, TimeType.CN_DAY, 4);

        String purchaseChildOrderNo = purchaseCompleteQcBo.getPurchaseChildOrderNo();
        QcOriginProperty qcOriginProperty = purchaseCompleteQcBo.getQcOriginProperty();
        String supplierCode = purchaseCompleteQcBo.getSupplierCode();
        String warehouseCode = purchaseCompleteQcBo.getWarehouseCode();
        List<PurchaseCompleteQcItemBo> purchaseCompleteQcItemBos = purchaseCompleteQcBo.getPurchaseCompleteQcItemBos();
        int qcAmount = purchaseCompleteQcItemBos.stream()
                .mapToInt(PurchaseCompleteQcItemBo::getQcAmount)
                .sum();
        List<String> querySkuList = purchaseCompleteQcItemBos.stream()
                .map(PurchaseCompleteQcItemBo::getSku)
                .distinct()
                .collect(Collectors.toList());
        //查询sku的分类信息
        Map<String, String> skuSecondCategoriesMap = plmRemoteService.getSkuSecondCategoriesCnMapBySkuList(
                querySkuList);

        // 质检单信息
        QcOrderPo qcOrderPo = new QcOrderPo();
        qcOrderPo.setQcOrderNo(qcOrderNo);
        qcOrderPo.setQcType(qcType);
        qcOrderPo.setQcOrigin(QcOrigin.RESIDENT);
        qcOrderPo.setQcOriginProperty(qcOriginProperty);
        qcOrderPo.setQcAmount(qcAmount);
        qcOrderPo.setQcState(QcState.WAIT_HAND_OVER);
        qcOrderPo.setSupplierCode(supplierCode);
        qcOrderPo.setPurchaseChildOrderNo(purchaseChildOrderNo);
        qcOrderPo.setQcSourceOrderNo(purchaseChildOrderNo);
        qcOrderPo.setQcSourceOrderType(QcSourceOrderType.PURCHASE_CHILD_ORDER_NO);
        qcOrderPo.setWarehouseCode(warehouseCode);
        qcOrderDao.insert(qcOrderPo);

        // 质检明细信息
        List<QcDetailPo> qcDetailPos = purchaseCompleteQcItemBos.stream()
                .map(purchaseCompleteQcItemBo -> {
                    QcDetailPo qcDetailPo = new QcDetailPo();
                    qcDetailPo.setQcOrderNo(qcOrderNo);
                    qcDetailPo.setBatchCode(purchaseCompleteQcItemBo.getBatchCode());

                    String sku = purchaseCompleteQcItemBo.getSku();
                    qcDetailPo.setSkuCode(sku);
                    qcDetailPo.setCategoryName(skuSecondCategoriesMap.get(sku));

                    int qualityCheckQuantity = purchaseCompleteQcItemBo.getQcAmount();
                    qcDetailPo.setAmount(qualityCheckQuantity);
                    qcDetailPo.setWaitAmount(qualityCheckQuantity);

                    qcDetailPo.setPassAmount(0);
                    qcDetailPo.setNotPassAmount(0);
                    qcDetailPo.setQcResult(QcResult.PASSED);
                    qcDetailPo.setPlatform(purchaseCompleteQcBo.getPlatform());

                    return qcDetailPo;
                })
                .collect(Collectors.toList());
        qcDetailDao.insertBatch(qcDetailPos);

        return qcOrderPo;
    }

    @Override
    protected void doAfterCreation(QcOrderPo qcOrderPo) {
        qcOrderLogService.createStatusChangeLog(qcOrderPo);
    }
}
