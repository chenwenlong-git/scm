package com.hete.supply.scm.server.scm.qc.service.base;

import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.qc.dao.QcDetailDao;
import com.hete.supply.scm.server.scm.qc.dao.QcOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.bo.PurchaseQcCreateRequestBo;
import com.hete.supply.scm.server.scm.qc.entity.bo.PurchaseQcCreateRequestItemBo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 创建采购质检单
 *
 * @author yanjiawei
 * Created on 2024/4/16.
 */
public class PurchaseQcCreator extends AbstractQcOrderCreator<PurchaseQcCreateRequestBo, QcOrderPo> {

    private final QcOrderBaseService qcOrderBaseService;
    private final PlmRemoteService plmRemoteService;

    /**
     * 构造一个 AbstractQcOrderCreator 实例。
     *
     * @param qcOrderDao        质检单数据访问对象，用于操作质检单数据。
     * @param qcDetailDao       质检明细数据访问对象，用于操作质检明细数据。
     * @param idGenerateService ID生成服务，用于生成唯一标识符。
     */
    public PurchaseQcCreator(QcOrderDao qcOrderDao,
                             QcDetailDao qcDetailDao,
                             IdGenerateService idGenerateService,
                             QcOrderBaseService qcOrderBaseService,
                             PlmRemoteService plmRemoteService) {
        super(qcOrderDao, qcDetailDao, idGenerateService);
        this.qcOrderBaseService = qcOrderBaseService;
        this.plmRemoteService = plmRemoteService;
    }

    @Override
    protected void createPreOperations(PurchaseQcCreateRequestBo input) {
        // 前置校验
        ParamValidUtils.requireNotBlank(input.getPurchaseChildOrderNo(),
                "创建质检单失败！原因：采购订单号不能为空，请提供有效的采购订单号。");
        ParamValidUtils.requireNotNull(input.getQcOriginProperty(),
                "创建质检单失败！原因：质检标识不能为空，请提供有效的质检标识");
        ParamValidUtils.requireNotBlank(input.getSupplierCode(),
                "创建质检单失败！原因：采购供应商编码不能为空，请提供有效的采购供应商编码。");
        List<PurchaseQcCreateRequestItemBo> purchaseQcCreateRequestItemBos
                = ParamValidUtils.requireNotEmpty(input.getPurchaseQcCreateRequestItemBos(),
                "创建质检单失败！原因：采购明细信息不能为空，请提供有效的采购明细信息。");
        purchaseQcCreateRequestItemBos.forEach(purchaseQcCreateRequestItemBo -> {
            // 验证SKU
            ParamValidUtils.requireNotBlank(purchaseQcCreateRequestItemBo.getSku(),
                    "创建质检单失败！原因:采购明细SKU不能为空，请提供有效的SKU");

            // 验证批次码
            ParamValidUtils.requireNotBlank(purchaseQcCreateRequestItemBo.getBatchCode(),
                    "创建质检单失败！原因:批次码不能为空，请提供有效的批次码");

            // 验证采购数量
            ParamValidUtils.requireGreaterThan(purchaseQcCreateRequestItemBo.getQcAmount(), 0,
                    "创建质检单失败！原因:采购数量必须大于0，请提供有效的采购数量");
        });
    }

    @Override
    protected QcOrderPo performCreateQcOrder(PurchaseQcCreateRequestBo input) {
        final QcType qcType = QcType.SAMPLE_CHECK;
        final String qcOrderNo = idGenerateService.getIncrCode(ScmConstant.QC_CODE, TimeType.CN_DAY, 4);

        String purchaseChildOrderNo = input.getPurchaseChildOrderNo();
        QcOriginProperty qcOriginProperty = input.getQcOriginProperty();
        String supplierCode = input.getSupplierCode();
        String warehouseCode = input.getWarehouseCode();

        List<PurchaseQcCreateRequestItemBo> purchaseQcCreateRequestItemBos = input.getPurchaseQcCreateRequestItemBos();
        int qcAmount = purchaseQcCreateRequestItemBos.stream()
                .mapToInt(PurchaseQcCreateRequestItemBo::getQcAmount)
                .sum();
        List<String> querySkuList = purchaseQcCreateRequestItemBos.stream()
                .map(PurchaseQcCreateRequestItemBo::getSku)
                .distinct()
                .collect(Collectors.toList());
        //查询sku的分类信息
        Map<String, String> skuSecondCategoriesMap = plmRemoteService.getSkuSecondCategoriesCnMapBySkuList(
                querySkuList);

        // 质检单信息
        QcOrderPo qcOrderPo = new QcOrderPo();
        qcOrderPo.setQcOrderNo(qcOrderNo);
        qcOrderPo.setQcType(qcType);
        qcOrderPo.setQcOrigin(QcOrigin.REWORK_RESIDENT);
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
        List<QcDetailPo> qcDetailPos = purchaseQcCreateRequestItemBos.stream()
                .map(purchaseQcCreateRequestItemBo -> {
                    QcDetailPo qcDetailPo = new QcDetailPo();
                    qcDetailPo.setQcOrderNo(qcOrderNo);
                    qcDetailPo.setBatchCode(purchaseQcCreateRequestItemBo.getBatchCode());

                    String sku = purchaseQcCreateRequestItemBo.getSku();
                    qcDetailPo.setSkuCode(sku);
                    qcDetailPo.setCategoryName(skuSecondCategoriesMap.get(sku));

                    int qualityCheckQuantity = purchaseQcCreateRequestItemBo.getQcAmount();
                    qcDetailPo.setAmount(qualityCheckQuantity);
                    qcDetailPo.setWaitAmount(qualityCheckQuantity);

                    qcDetailPo.setPassAmount(0);
                    qcDetailPo.setNotPassAmount(0);
                    qcDetailPo.setQcResult(QcResult.PASSED);
                    qcDetailPo.setPlatform(input.getPlatform());

                    return qcDetailPo;
                })
                .collect(Collectors.toList());
        qcDetailDao.insertBatch(qcDetailPos);

        return qcOrderPo;
    }

    @Override
    protected void doAfterCreation(QcOrderPo qcOrderPo) {
        qcOrderBaseService.createStatusChangeLog(qcOrderPo);
    }
}
