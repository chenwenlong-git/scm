package com.hete.supply.scm.server.scm.qc.service.base;

import cn.hutool.core.util.StrUtil;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.common.service.base.IContainer;
import com.hete.supply.scm.server.scm.qc.dao.QcDetailDao;
import com.hete.supply.scm.server.scm.qc.dao.QcOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.bo.OutBoundQcOrderResultBo;
import com.hete.supply.scm.server.scm.qc.entity.dto.BizNoQcCreateRequestDto;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 出库单创建器
 *
 * @author yanjiawei
 * Created on 2024/6/28.
 */
public class OutBoundOrderQcCreator extends AbstractQcOrderCreator<BizNoQcCreateRequestDto, OutBoundQcOrderResultBo> {

    private final PlmRemoteService plmRemoteService;
    private final QcOrderBaseService qcOrderBaseService;

    public OutBoundOrderQcCreator(QcOrderDao qcOrderDao,
                                  QcDetailDao qcDetailDao,
                                  IdGenerateService idGenerateService,
                                  PlmRemoteService plmRemoteService,
                                  QcOrderBaseService qcOrderBaseService) {
        super(qcOrderDao, qcDetailDao, idGenerateService);
        this.plmRemoteService = plmRemoteService;
        this.qcOrderBaseService = qcOrderBaseService;
    }

    @Override
    protected void createPreOperations(BizNoQcCreateRequestDto input) {
        // 前置校验
        ParamValidUtils.requireNotBlank(input.getOutBoundNo(),
                "创建质检单失败！原因：出库单号不能为空，请提供有效的出库单号。");

        ParamValidUtils.requireNotNull(input.getQcOriginProperty(),
                "创建质检单失败！原因：质检标识不能为空，请提供有效的质检标识");

        List<BizNoQcCreateRequestDto.CreateBizQcDetailDto> qcDetailList = ParamValidUtils.requireNotEmpty(
                input.getCreateBizQcDetailDtoList(),
                "创建质检单失败！原因：出库单明细信息不能为空，请提供有效的出库单明细信息。"
        );

        qcDetailList.forEach(detail -> {
            // 验证SKU
            ParamValidUtils.requireNotBlank(detail.getSku(),
                    "创建质检单失败！原因:SKU不能为空，请提供有效的SKU");

            // 验证批次码
            ParamValidUtils.requireNotBlank(detail.getBatchCode(),
                    "创建质检单失败！原因:批次码不能为空，请提供有效的批次码");

            // 验证采购数量
            ParamValidUtils.requireGreaterThan(detail.getQuantity(), 0,
                    "创建质检单失败！原因:质检数量必须大于0，请提供有效的采购数量");
        });
    }

    @Override
    protected OutBoundQcOrderResultBo performCreateQcOrder(BizNoQcCreateRequestDto input) {
        final QcType qcType = QcType.ALL_CHECK;
        final String qcOrderNo = idGenerateService.getIncrCode(ScmConstant.QC_CODE, TimeType.CN_DAY, 4);

        String bizOrderNo = input.getOutBoundNo();
        QcOriginProperty qcOriginProperty = input.getQcOriginProperty();
        String warehouseCode = input.getWarehouseCode();
        String platCode = input.getPlatCode();

        List<BizNoQcCreateRequestDto.CreateBizQcDetailDto> qcDetailList = input.getCreateBizQcDetailDtoList();
        String supplierCode = qcDetailList.stream()
                .map(BizNoQcCreateRequestDto.CreateBizQcDetailDto::getSupplierCode)
                .filter(StrUtil::isNotBlank).findFirst().orElse(null);
        int qcAmount = qcDetailList.stream().mapToInt(BizNoQcCreateRequestDto.CreateBizQcDetailDto::getQuantity).sum();

        // 查询sku的分类信息
        List<String> querySkuList = qcDetailList.stream().map(BizNoQcCreateRequestDto.CreateBizQcDetailDto::getSku)
                .distinct().collect(Collectors.toList());
        Map<String, String> skuSecondCategoriesMap = plmRemoteService.getSkuSecondCategoriesCnMapBySkuList(querySkuList);

        // 质检单信息
        QcOrderPo qcOrderPo = new QcOrderPo();
        qcOrderPo.setQcOrderNo(qcOrderNo);
        qcOrderPo.setQcType(qcType);
        qcOrderPo.setQcOrigin(QcOrigin.OUTBOUND);
        qcOrderPo.setQcOriginProperty(qcOriginProperty);
        qcOrderPo.setQcAmount(qcAmount);
        qcOrderPo.setQcState(QcState.WAIT_HAND_OVER);
        qcOrderPo.setSupplierCode(supplierCode);
        qcOrderPo.setQcSourceOrderNo(bizOrderNo);
        qcOrderPo.setQcSourceOrderType(QcSourceOrderType.OUTBOUND_ORDER_NO);
        qcOrderPo.setWarehouseCode(warehouseCode);
        qcOrderDao.insert(qcOrderPo);

        // 质检明细信息
        List<QcDetailPo> qcDetailPos = qcDetailList.stream()
                .map(qcDetail -> {
                    QcDetailPo qcDetailPo = new QcDetailPo();
                    qcDetailPo.setQcOrderNo(qcOrderNo);
                    qcDetailPo.setBatchCode(qcDetail.getBatchCode());

                    String sku = qcDetail.getSku();
                    qcDetailPo.setSkuCode(sku);
                    qcDetailPo.setBatchCode(qcDetail.getBatchCode());
                    qcDetailPo.setCategoryName(skuSecondCategoriesMap.get(sku));
                    qcDetailPo.setContainerCode(qcDetail.getContainerCode());

                    int qualityCheckQuantity = qcDetail.getQuantity();
                    qcDetailPo.setAmount(qualityCheckQuantity);
                    qcDetailPo.setWaitAmount(qualityCheckQuantity);

                    qcDetailPo.setPassAmount(0);
                    qcDetailPo.setNotPassAmount(0);
                    qcDetailPo.setQcResult(QcResult.PASSED);

                    qcDetailPo.setPlatform(platCode);
                    return qcDetailPo;
                })
                .collect(Collectors.toList());
        qcDetailDao.insertBatch(qcDetailPos);

        return new OutBoundQcOrderResultBo(qcOrderPo, qcDetailPos);
    }

    @Override
    protected void doAfterCreation(OutBoundQcOrderResultBo outBoundQcOrderResultBo) {
        QcOrderPo qcOrderPo = outBoundQcOrderResultBo.getQcOrderPo();
        List<QcDetailPo> qcDetailPoList = outBoundQcOrderResultBo.getQcDetailPoList();

        // 占用容器
        String warehouseCode = qcOrderPo.getWarehouseCode();
        Set<String> containerCodeList
                = qcDetailPoList.stream().distinct().map(QcDetailPo::getContainerCode).collect(Collectors.toSet());
        containerCodeList.forEach(containerCode -> {
            IContainer occupyContainer = new QcContainer(containerCode, warehouseCode);
            occupyContainer.tryOccupyContainer();
        });

        // 记录日志
        qcOrderBaseService.createStatusChangeLog(qcOrderPo);
    }
}
