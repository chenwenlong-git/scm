package com.hete.supply.scm.server.scm.qc.service.base;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.enums.DefaultDatabaseTime;
import com.hete.supply.scm.server.scm.qc.converter.QcOrderConverter;
import com.hete.supply.scm.server.scm.qc.dao.QcDetailDao;
import com.hete.supply.scm.server.scm.qc.dao.QcOrderDao;
import com.hete.supply.scm.server.scm.qc.dao.QcReceiveOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.bo.ReceiveOrderQcOrderBo;
import com.hete.supply.scm.server.scm.qc.entity.bo.ReceiveOrderQcOrderDetailBo;
import com.hete.supply.scm.server.scm.qc.entity.dto.QcOrderChangeDto;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcReceiveOrderPo;
import com.hete.supply.scm.server.scm.qc.handler.AbstractQcOrderStatusHandler;
import com.hete.supply.scm.server.scm.qc.handler.ReceiveOrderQcOrderCreateStatusHandler;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.entry.entity.dto.ReceiveOrderGetDto;
import com.hete.supply.wms.api.entry.entity.vo.ReceiveOrderForScmVo;
import com.hete.support.api.exception.BizException;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ReceiveOrderQcOrderCreator 是用于创建收货单质检单的服务类，继承自 AbstractQcOrderCreator。
 * 该类标记为 Spring 服务，使用 Lombok 的 Log 注解实现日志记录。
 *
 * @author yanjiawei
 * Created on 2023/10/18.
 */
@Slf4j
public class ReceiveOrderQcOrderCreator extends AbstractQcOrderCreator<ReceiveOrderQcOrderBo, QcOrderChangeDto> {

    private static final String SYSTEM_USER_NAME = "SYSTEM";
    private static final String SYSTEM_USER_KEY = "System";

    private final QcReceiveOrderDao qcReceiveOrderDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final QcOrderBaseService qcOrderBaseService;
    private final SupplierDao supplierDao;
    private final PlmRemoteService plmRemoteService;
    private final WmsRemoteService wmsRemoteService;

    public ReceiveOrderQcOrderCreator(QcOrderDao qcOrderDao,
                                      QcDetailDao qcDetailDao,
                                      QcReceiveOrderDao qcReceiveOrderDao,
                                      IdGenerateService idGenerateService,
                                      ConsistencySendMqService consistencySendMqService,
                                      QcOrderBaseService qcOrderBaseService,
                                      SupplierDao supplierDao,
                                      PlmRemoteService plmRemoteService,
                                      WmsRemoteService wmsRemoteService) {
        super(qcOrderDao, qcDetailDao, idGenerateService);
        this.qcReceiveOrderDao = qcReceiveOrderDao;
        this.consistencySendMqService = consistencySendMqService;
        this.qcOrderBaseService = qcOrderBaseService;
        this.supplierDao = supplierDao;
        this.plmRemoteService = plmRemoteService;
        this.wmsRemoteService = wmsRemoteService;
    }

    /**
     * 创建质检单前的预操作，包括检查质检类型和质检明细是否为空。
     *
     * @param input 收货单质检单创建的输入数据
     */
    @Override
    protected void createPreOperations(ReceiveOrderQcOrderBo input) {
        final QcType qcType = input.getQcType();
        final QcResult qcResult = input.getQcResult();
        boolean noInspectionRequired = Objects.equals(QcType.NOT_CHECK, qcType);
        boolean isInvalidValue = noInspectionRequired && Objects.isNull(qcResult);
        if (isInvalidValue) {
            log.error("收货单创建质检单异常，原因：质检单=免检，质检结果为空！{}", JSON.toJSONString(input));
            throw new BizException("收货单创建质检单异常，原因：质检单=免检，质检结果为空！");
        }

        final List<ReceiveOrderQcOrderDetailBo> createQcOrderDetails = input.getQcOrderDetails();
        if (CollectionUtils.isEmpty(createQcOrderDetails)) {
            throw new BizException("wms收货单创建质检单异常，原因：质检明细为空");
        }

        // 质检类型 质检标识
        QcOrigin qcOrigin = qcOrderBaseService.getQcOrigin(input);
        if (null == qcOrigin) {
            throw new BizException("wms收货单创建质检单异常，原因：获取不到对应质检标识");
        }

    }

    /**
     * 执行质检单的创建操作，包括创建质检单信息和质检明细。
     *
     * @param createQcOrderBo 收货单质检单的创建数据
     * @return 返回质检单的状态变化数据
     */
    @Override
    protected QcOrderChangeDto performCreateQcOrder(ReceiveOrderQcOrderBo createQcOrderBo) {
        final QcType qcType = createQcOrderBo.getQcType();
        final QcResult qcResult = createQcOrderBo.getQcResult();
        final WmsEnum.ReceiveType receiveType = createQcOrderBo.getReceiveType();
        final String supplierCode = createQcOrderBo.getSupplierCode();
        final String deliveryOrderNo = createQcOrderBo.getDeliveryOrderNo();
        final String goodsCategory = createQcOrderBo.getGoodsCategory();
        int qcAmount = createQcOrderBo.getQcAmount();
        final String createUser = createQcOrderBo.getCreateUser();
        final String createUsername = createQcOrderBo.getCreateUsername();
        final String onShelvesOrderNo = createQcOrderBo.getOnShelvesOrderNo();
        final String warehouseCode = createQcOrderBo.getWarehouseCode();
        final String deliverOrderNo = createQcOrderBo.getDeliverOrderNo();
        // 免检
        boolean noInspectionRequired = Objects.equals(QcType.NOT_CHECK, qcType);
        final QcState qcState = noInspectionRequired ? QcState.FINISHED : QcState.WAIT_HAND_OVER;
        final String qcOrderNo = idGenerateService.getIncrCode(ScmConstant.QC_CODE, TimeType.CN_DAY, 4);
        final String receiveOrderNo = createQcOrderBo.getReceiveOrderNo();

        // 是否加急
        final ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
        receiveOrderGetDto.setReceiveOrderNoList(List.of(receiveOrderNo));
        final List<ReceiveOrderForScmVo> receiveOrderList = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto);

        // 质检类型 质检标识
        QcOrigin qcOrigin = qcOrderBaseService.getQcOrigin(createQcOrderBo);
        QcOriginProperty qcOriginProperty = qcOrderBaseService.getQcOriginProperty(createQcOrderBo);

        // 质检单信息
        QcOrderPo qcOrderPo = new QcOrderPo();
        qcOrderPo.setQcOrderNo(qcOrderNo);
        qcOrderPo.setReceiveOrderNo(receiveOrderNo);
        qcOrderPo.setQcSourceOrderNo(receiveOrderNo);
        qcOrderPo.setQcSourceOrderType(QcSourceOrderType.RECEIVE_ORDER_NO);
        qcOrderPo.setQcType(qcType);
        qcOrderPo.setQcState(qcState);
        qcOrderPo.setQcResult(noInspectionRequired ? qcResult : null);
        qcOrderPo.setHandOverTime(
                noInspectionRequired ? LocalDateTime.now() : DefaultDatabaseTime.DEFAULT_TIME.getDateTime());
        qcOrderPo.setTaskFinishTime(
                noInspectionRequired ? LocalDateTime.now() : DefaultDatabaseTime.DEFAULT_TIME.getDateTime());
        qcOrderPo.setAuditTime(
                noInspectionRequired ? LocalDateTime.now() : DefaultDatabaseTime.DEFAULT_TIME.getDateTime());
        qcOrderPo.setAuditor(noInspectionRequired ? SYSTEM_USER_KEY : "");
        qcOrderPo.setWarehouseCode(warehouseCode);
        qcOrderPo.setQcAmount(qcAmount);
        GlobalContext.setUserKey(StrUtil.isNotBlank(createUser) ? createUser : SYSTEM_USER_KEY);
        GlobalContext.setUsername(StrUtil.isNotBlank(createUsername) ? createUsername : SYSTEM_USER_NAME);
        SupplierPo supplierPo = supplierDao.getBySupplierCode(supplierCode);
        qcOrderPo.setSupplierCode(supplierCode);
        if (supplierPo != null) {
            qcOrderPo.setSupplierName(supplierPo.getSupplierName());
        }

        // 是否加急
        receiveOrderList.stream()
                .filter(receiveOrder -> receiveOrder.getReceiveOrderNo().equals(receiveOrderNo))
                .findFirst()
                .ifPresent(receiveOrderForScmVo -> qcOrderPo.setIsUrgentOrder(receiveOrderForScmVo.getIsUrgentOrder()));


        // 增加质检标识
        qcOrderPo.setQcOrigin(qcOrigin);
        qcOrderPo.setQcOriginProperty(qcOriginProperty);

        qcOrderDao.insert(qcOrderPo);

        // 日志
        qcOrderBaseService.createStatusChangeLog(qcOrderPo);

        // 质检明细
        final List<ReceiveOrderQcOrderDetailBo> createQcOrderDetails = createQcOrderBo.getQcOrderDetails();
        List<String> skuList = createQcOrderDetails.stream()
                .map(ReceiveOrderQcOrderDetailBo::getSkuCode)
                .distinct()
                .collect(Collectors.toList());
        //查询sku的分类信息
        Map<String, String> skuSecondCategoriesMap = plmRemoteService.getSkuSecondCategoriesCnMapBySkuList(skuList);

        List<QcDetailPo> qcDetailPos = createQcOrderDetails.stream()
                .map(createQcOrderDetail -> {
                    // 待质检数量
                    int waitAmount = createQcOrderDetail.getWaitAmount();
                    // 合格数量 = 当免检且合格，合格数量=待质检数量,否则=0
                    int passAmount = noInspectionRequired && Objects.equals(QcResult.PASSED, qcResult) ? waitAmount : 0;
                    // 不合格数量 = 当免检且不合格，不合格数量=待质检数量,否则=0
                    int notPassAmount
                            = noInspectionRequired && Objects.equals(QcResult.NOT_PASSED, qcResult) ? waitAmount : 0;

                    QcDetailPo qcDetailPo = new QcDetailPo();
                    qcDetailPo.setQcOrderNo(qcOrderNo);
                    qcDetailPo.setQcResult(noInspectionRequired ? qcResult : QcResult.PASSED);
                    qcDetailPo.setBatchCode(createQcOrderDetail.getBatchCode());
                    qcDetailPo.setSkuCode(createQcOrderDetail.getSkuCode());
                    qcDetailPo.setContainerCode(createQcOrderDetail.getContainerCode());
                    qcDetailPo.setAmount(waitAmount);
                    qcDetailPo.setHandOverAmount(noInspectionRequired ? waitAmount : 0);
                    qcDetailPo.setWaitAmount(noInspectionRequired ? 0 : waitAmount);
                    qcDetailPo.setPassAmount(passAmount);
                    qcDetailPo.setNotPassAmount(notPassAmount);
                    qcDetailPo.setCategoryName(skuSecondCategoriesMap.get(createQcOrderDetail.getSkuCode()));
                    qcDetailPo.setPlatform(createQcOrderDetail.getPlatform());
                    return qcDetailPo;
                })
                .collect(Collectors.toList());
        qcDetailDao.insertBatch(qcDetailPos);
        return QcOrderConverter.toQcOrderChangeDto(qcOrderPo, qcDetailPos, receiveType, supplierCode, deliveryOrderNo,
                goodsCategory, onShelvesOrderNo, deliverOrderNo);
    }

    /**
     * 质检单创建后的后续操作，包括保存质检单关联的收货单信息并推送质检单信息到WMS。
     *
     * @param qcOrderChangeDto 质检单的状态变化数据
     */
    @Override
    protected void doAfterCreation(QcOrderChangeDto qcOrderChangeDto) {
        final String qcOrderNo = qcOrderChangeDto.getQcOrderNo();
        final String receiveOrderNo = qcOrderChangeDto.getReceiveOrderNo();
        final WmsEnum.ReceiveType receiveType = qcOrderChangeDto.getReceiveType();
        final String supplierCode = qcOrderChangeDto.getSupplierCode();
        final String deliveryOrderNo = qcOrderChangeDto.getDeliveryOrderNo();
        final String goodsCategory = qcOrderChangeDto.getGoodsCategory();
        final String deliverOrderNo = qcOrderChangeDto.getDeliverOrderNo();

        QcReceiveOrderPo qcReceiveOrderPo = new QcReceiveOrderPo();
        qcReceiveOrderPo.setQcOrderNo(qcOrderNo);
        qcReceiveOrderPo.setReceiveOrderNo(receiveOrderNo);
        qcReceiveOrderPo.setReceiveType(receiveType);
        qcReceiveOrderPo.setSupplierCode(supplierCode);
        qcReceiveOrderPo.setDeliveryOrderNo(deliveryOrderNo);
        qcReceiveOrderPo.setScmBizNo(deliverOrderNo);
        qcReceiveOrderPo.setGoodsCategory(goodsCategory);
        qcReceiveOrderDao.insert(qcReceiveOrderPo);

        // 推送质检单信息到WMS
        AbstractQcOrderStatusHandler<QcOrderChangeDto> qcOrderStatusHandler = new ReceiveOrderQcOrderCreateStatusHandler(
                consistencySendMqService, idGenerateService);
        qcOrderStatusHandler.handlePostStatusChange(qcOrderChangeDto);
    }
}

