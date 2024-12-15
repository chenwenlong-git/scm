package com.hete.supply.scm.server.scm.qc.service.biz;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.plm.api.goods.entity.vo.PlmSkuImage;
import com.hete.supply.plm.api.goods.entity.vo.PlmSkuVo;
import com.hete.supply.scm.api.scm.entity.dto.QcDefectHandlingQueryDto;
import com.hete.supply.scm.api.scm.entity.dto.QcDto;
import com.hete.supply.scm.api.scm.entity.dto.QcOrderQueryDto;
import com.hete.supply.scm.api.scm.entity.dto.QcSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.entity.vo.QcDefectHandlingVo;
import com.hete.supply.scm.api.scm.entity.vo.QcDetailSkuVo;
import com.hete.supply.scm.api.scm.entity.vo.QcExportVo;
import com.hete.supply.scm.api.scm.entity.vo.QcVo;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.common.service.base.IContainer;
import com.hete.supply.scm.server.scm.defect.converter.DefectHandlingConverter;
import com.hete.supply.scm.server.scm.defect.dao.DefectHandlingDao;
import com.hete.supply.scm.server.scm.defect.entity.bo.DefectHandlingCreateBo;
import com.hete.supply.scm.server.scm.defect.service.ref.DefectRefService;
import com.hete.supply.scm.server.scm.entity.po.DefectHandlingPo;
import com.hete.supply.scm.server.scm.nacosconfig.SpuConfig;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProduceDataSpecBatchVo;
import com.hete.supply.scm.server.scm.process.service.ref.ProcessOrderRefService;
import com.hete.supply.scm.server.scm.production.entity.bo.SpecBookRelateBo;
import com.hete.supply.scm.server.scm.production.entity.vo.SkuAttributeInfoVo;
import com.hete.supply.scm.server.scm.production.entity.vo.SupSkuProdInfoVo;
import com.hete.supply.scm.server.scm.production.service.base.SkuProdBaseService;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderItemDao;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderItemPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.purchase.service.ref.PurchaseRefService;
import com.hete.supply.scm.server.scm.qc.builder.QcOrderBuilder;
import com.hete.supply.scm.server.scm.qc.config.QcConfig;
import com.hete.supply.scm.server.scm.qc.converter.QcOrderConverter;
import com.hete.supply.scm.server.scm.qc.dao.QcDetailDao;
import com.hete.supply.scm.server.scm.qc.dao.QcOnShelvesOrderDao;
import com.hete.supply.scm.server.scm.qc.dao.QcOrderDao;
import com.hete.supply.scm.server.scm.qc.dao.QcReceiveOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.bo.DeliveryDetailKeyBo;
import com.hete.supply.scm.server.scm.qc.entity.bo.PurchaseQcCreateRequestBo;
import com.hete.supply.scm.server.scm.qc.entity.bo.QcOriginUpdateBo;
import com.hete.supply.scm.server.scm.qc.entity.bo.ReceiveOrderQcOrderBo;
import com.hete.supply.scm.server.scm.qc.entity.dto.*;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOnShelvesOrderPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcReceiveOrderPo;
import com.hete.supply.scm.server.scm.qc.entity.vo.*;
import com.hete.supply.scm.server.scm.qc.enums.DefectType;
import com.hete.supply.scm.server.scm.qc.enums.QcApproveOperate;
import com.hete.supply.scm.server.scm.qc.enums.QcBizOperate;
import com.hete.supply.scm.server.scm.qc.enums.QcOperate;
import com.hete.supply.scm.server.scm.qc.handler.QcOriginUpdateHandler;
import com.hete.supply.scm.server.scm.qc.service.base.*;
import com.hete.supply.scm.server.scm.qc.service.ref.QcOrderRefService;
import com.hete.supply.scm.server.scm.service.base.ProduceDataBaseService;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierBaseService;
import com.hete.supply.scm.server.supplier.entity.bo.ReturnOrderBo;
import com.hete.supply.scm.server.supplier.enums.DefectRate;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.service.ref.PurchaseDeliverRefService;
import com.hete.supply.scm.server.supplier.service.ref.ReturnRefService;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.basic.entity.vo.SimpleSkuBatchVo;
import com.hete.supply.wms.api.entry.entity.dto.ReceiveOrderGetDto;
import com.hete.supply.wms.api.entry.entity.vo.ReceiveOrderForScmVo;
import com.hete.supply.wms.api.leave.entity.dto.DeliveryQueryDto;
import com.hete.supply.wms.api.leave.entity.vo.DeliveryOrderVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.consistency.core.service.ConsistencyService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.JacksonUtil;
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author weiwenxin
 * @date 2023/10/10 09:28
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class QcOrderBizService {
    private final QcOrderDao qcOrderDao;
    private final QcDetailDao qcDetailDao;
    private final QcReceiveOrderDao qcReceiveOrderDao;
    private final QcOnShelvesOrderDao qcOnShelvesOrderDao;
    private final PlmRemoteService plmRemoteService;
    private final ProcessOrderRefService processOrderRefService;
    private final PurchaseRefService purchaseRefService;
    private final IdGenerateService idGenerateService;
    private final QcCheckParamService qcCheckParamService;
    private final ConsistencySendMqService consistencySendMqService;
    private final DefectRefService defectRefService;
    private final PurchaseReturnOrderItemDao purchaseReturnOrderItemDao;
    private final PurchaseDeliverRefService purchaseDeliverRefService;
    private final ReturnRefService returnRefService;
    private final QcOrderBaseService qcOrderBaseService;
    private final DefectHandlingDao defectHandlingDao;
    private final WmsRemoteService wmsRemoteService;
    private final SupplierBaseService supplierBaseService;
    private final QcExportStrategyFactory qcExportStrategyFactory;
    private final ProduceDataBaseService produceDataBaseService;
    private final SupplierDao supplierDao;
    private final ConsistencyService consistencyService;
    private final SpuConfig spuConfig;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;
    private final QcOrderRefService qcOrderRefService;
    private final QcConfig qcConfig;
    private final SkuProdBaseService skuProdBaseService;

    /**
     * 质检判断低不良率的标准数
     */
    private final static Integer QC_STANDER_NUM = 100;
    /**
     * 不良率判断标准
     */
    private final static BigDecimal LOW_DEFECT_RATE = new BigDecimal("0.1");

    private final static int PAGE_SIZE = 500;
    private final static int MAX_ITERATIONS = 200;

    private final static String DEFAULT_EMPTY_KEY = "emptyKey";

    public CommonPageResult.PageInfo<QcSearchVo> searchQc(QcSearchDto dto) {
        if (null == this.searchQcWhere(dto)) {
            return new CommonPageResult.PageInfo<>();
        }

        IPage<QcSearchVo> pageResult = qcOrderDao.searchQcOrderPage(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<QcSearchVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new CommonPageResult.PageInfo<>();
        }

        final List<String> qcOrderNoList = records.stream().map(QcSearchVo::getQcOrderNo).collect(Collectors.toList());
        final List<QcDetailPo> qcDetailPoList1 = qcDetailDao.getListByQcOrderNoList(qcOrderNoList);
        final List<QcReceiveOrderPo> qcReceiveOrderPoList1 = qcReceiveOrderDao.getListByQcOrderNoList(qcOrderNoList);
        final List<QcOnShelvesOrderPo> qcOnShelvesOrderPoList
                = qcOnShelvesOrderDao.getListByQcOrderNoList(qcOrderNoList);
        final List<DefectHandlingPo> defectHandlingPoList = defectHandlingDao.listByQcOrderNoList(qcOrderNoList);
        final List<QcDetailVo> qcDetailVoList = dto.isPrint() ? getQcDetailVoList(qcOrderNoList) : Collections.emptyList();

        // 根据质检单号找退货单
        final List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList = purchaseReturnOrderItemDao.getListByReturnBizNos(qcOrderNoList);
        final List<String> receiveOrderNoList = qcReceiveOrderPoList1.stream().map(QcReceiveOrderPo::getReceiveOrderNo).collect(Collectors.toList());
        Map<String, ReceiveOrderForScmVo> receiveOrderNoVoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(receiveOrderNoList)) {
            final ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
            receiveOrderGetDto.setReceiveOrderNoList(receiveOrderNoList);
            final List<ReceiveOrderForScmVo> receiveOrderList = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto);
            receiveOrderNoVoMap = receiveOrderList.stream().collect(Collectors.toMap(ReceiveOrderForScmVo::getReceiveOrderNo, Function.identity()));
        }

        final List<String> skuList = qcDetailPoList1.stream()
                .map(QcDetailPo::getSkuCode)
                .collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        // 驻场质检判断标准
        List<QcOrigin> residentQcOrigins = qcOrderBaseService.getResidentQcOrigins();
        return PageInfoUtil.getPageInfo(pageResult, QcOrderConverter.qcOrderPoToSearchVo(records, qcDetailPoList1,
                qcReceiveOrderPoList1,
                qcOnShelvesOrderPoList,
                skuEncodeMap,
                defectHandlingPoList,
                receiveOrderNoVoMap,
                purchaseReturnOrderItemPoList,
                qcDetailVoList,
                residentQcOrigins));
    }

    public QcDetailVo qcDetail(QcOrderNoDto dto) {
        QcOrderPo qcOrderPo = qcOrderDao.getByQcOrderNo(dto.getQcOrderNo());
        // 查不到质检单号，用容器码查询
        if (null == qcOrderPo) {
            final List<QcDetailPo> qcDetailPoList = qcDetailDao.getListByContainerCode(dto.getQcOrderNo());
            if (CollectionUtils.isNotEmpty(qcDetailPoList)) {
                qcOrderPo = qcOrderDao.getByQcOrderNo(qcDetailPoList.get(0)
                        .getQcOrderNo());
            }
            // 查不到质检单号，用收货单查询
            if (null == qcOrderPo) {
                final List<QcReceiveOrderPo> qcReceiveOrderPoList = qcReceiveOrderDao.getListByReceiveNo(
                        dto.getQcOrderNo());
                if (CollectionUtils.isNotEmpty(qcReceiveOrderPoList)) {
                    qcOrderPo = qcOrderDao.getByQcOrderNo(qcReceiveOrderPoList.get(0)
                            .getQcOrderNo());
                }
            }
            if (null == qcOrderPo) {
                throw new ParamIllegalException("无法找到质检单{}，请联系系统管理员！", dto.getQcOrderNo());
            }
        }
        final List<QcDetailPo> qcDetailPoList = qcDetailDao.getListByQcOrderNo(qcOrderPo.getQcOrderNo());
        if (CollectionUtils.isEmpty(qcDetailPoList)) {
            throw new BizException("无法找到质检单{}，请联系系统管理员！", dto.getQcOrderNo());
        }
        final QcReceiveOrderPo qcReceiveOrderPo = qcReceiveOrderDao.getOneByReceiveNo(qcOrderPo.getReceiveOrderNo());
        final List<String> skuList = qcDetailPoList.stream()
                .map(QcDetailPo::getSkuCode)
                .distinct()
                .collect(Collectors.toList());
        List<PlmSkuVo> skuEncodeBySku = plmRemoteService.getSkuEncodeBySku(skuList);
        final Map<String, String> skuEncodeMap = skuEncodeBySku.stream()
                .collect(Collectors.toMap(PlmSkuVo::getSkuCode, PlmSkuVo::getSkuEncode));


        // 获取配置spu关联的提示信息信息
        String spuTips = "";
        Map<String, String> spuTipMap = spuConfig.getSpuTipMap();
        if (CollectionUtils.isNotEmpty(spuTipMap)) {
            Set<String> curSpuList = skuEncodeBySku.stream()
                    .map(PlmSkuVo::getSpuCode)
                    .collect(Collectors.toSet());
            for (String spu : curSpuList) {
                if (StringUtils.isNotBlank(spuTipMap.getOrDefault(spu, ""))) {
                    spuTips = spuTipMap.get(spu);
                    break;
                }
            }
        }

        // 根据成品的加工单或采购单，获取平台
        ProcessOrderPo processOrderPo = processOrderRefService.getProcessOrderPoByNo(qcOrderPo.getProcessOrderNo());
        String platform = null != processOrderPo ? processOrderPo.getPlatform() : "";
        if (null != qcReceiveOrderPo && WmsEnum.ReceiveType.BULK.equals(qcReceiveOrderPo.getReceiveType())) {
            final PurchaseChildOrderPo purchaseChildOrderPo = purchaseRefService.getPurchaseChildPoByDeliverNo(
                    qcReceiveOrderPo.getScmBizNo());
            platform = null != purchaseChildOrderPo ? purchaseChildOrderPo.getPlatform() : "";
        }
        final List<PlmSkuImage> plmSkuImageList = plmRemoteService.getSkuImage(skuList, platform);


        //获取生产资料的信息
        List<ProduceDataSpecBatchVo> produceDataSpecBatchVoList = produceDataBaseService.getBatchLoadProductFile(
                skuList);
        Map<String, ProduceDataSpecBatchVo> produceDataSpecBatchVoMap = produceDataSpecBatchVoList.stream()
                .collect(Collectors.toMap(ProduceDataSpecBatchVo::getSku, Function.identity(),
                        (existing, replacement) -> existing));

        return QcOrderConverter.qcOrderPoToDetailVo(qcOrderPo, qcDetailPoList, qcReceiveOrderPo, skuEncodeMap,
                plmSkuImageList, skuList, produceDataSpecBatchVoMap, spuTips);
    }

    public List<QcDetailVo> qcDetails(List<String> qcOrderNos) {
        final List<QcDetailPo> qcDetailPoList = qcDetailDao.getListByQcOrderNoList(qcOrderNos);
        if (CollectionUtils.isEmpty(qcDetailPoList)) {
            return null;
        }

        final List<String> skuList = qcDetailPoList.stream()
                .map(QcDetailPo::getSkuCode)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        return QcOrderConverter.qcOrderPoToDetailVos(qcDetailPoList, skuEncodeMap);
    }

    @Transactional(rollbackFor = Exception.class)
    public void completeHandover(QcOrderNoListDto dto) {
        final List<QcOrderPo> qcOrderPoList = qcOrderDao.getByQcOrderNos(dto.getQcOrderNoList());
        if (CollectionUtils.isEmpty(qcOrderPoList) || qcOrderPoList.size() != dto.getQcOrderNoList()
                .size()) {
            throw new BizException("质检数据有误，请联系系统管理员");
        }
        qcOrderPoList.forEach(qcOrderPo -> {
            final QcState resultQcState = qcOrderPo.getQcState()
                    .toBeQc();
            qcOrderPo.setQcState(resultQcState);
            qcOrderPo.setHandOverTime(LocalDateTime.now());
            qcOrderPo.setHandOverUser(GlobalContext.getUsername());
            // 质检变更状态时推送mq
            qcOrderBaseService.handleStatusChange(qcOrderPo.getQcOrderNo(), resultQcState, QcBizOperate.HANDOVER);
        });
        qcOrderDao.updateBatchByIdVersion(qcOrderPoList);

        List<QcDetailPo> qcDetailPoList = ParamValidUtils.requireNotEmpty(
                qcDetailDao.getListByQcOrderNoList(dto.getQcOrderNoList()),
                "质检明细数据有误，请联系相关业务人员处理。"
        );
        qcDetailPoList.forEach(qcDetailPo -> qcDetailPo.setHandOverAmount(qcDetailPo.getAmount()));
        qcDetailDao.updateBatchByIdVersion(qcDetailPoList);

        // 日志
        qcOrderPoList.forEach(qcOrderBaseService::createStatusChangeLog);
    }

    @Transactional(rollbackFor = Exception.class)
    public void startQc(QcOrderNoListDto dto) {
        final List<QcOrderPo> qcOrderPoList = qcOrderDao.getByQcOrderNos(dto.getQcOrderNoList());
        if (CollectionUtils.isEmpty(qcOrderPoList) || qcOrderPoList.size() != dto.getQcOrderNoList()
                .size()) {
            throw new BizException("质检数据有误，请联系系统管理员");
        }

        qcOrderPoList.forEach(qcOrderPo -> {
            final QcState resultQcState = qcOrderPo.getQcState()
                    .toQcing();
            qcOrderPo.setQcState(resultQcState);
            // 质检变更状态时推送mq
            qcOrderBaseService.handleStatusChange(qcOrderPo.getQcOrderNo(), resultQcState, QcBizOperate.START);
        });

        qcOrderDao.updateBatchByIdVersion(qcOrderPoList);
        // 日志
        qcOrderPoList.forEach(qcOrderBaseService::createStatusChangeLog);
    }

    @Transactional(rollbackFor = Exception.class)
    public void submitQc(QcSubmitDto dto) {
        final QcOrderPo qcOrderPo = qcOrderDao.getByIdVersion(dto.getQcOrderId(), dto.getVersion());
        if (null == qcOrderPo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        if (!QcState.TO_BE_QC.equals(qcOrderPo.getQcState())
                && !QcState.QCING.equals(qcOrderPo.getQcState())) {
            throw new ParamIllegalException("当前质检单状态不处于{}或{},请刷新后重试",
                    QcState.TO_BE_QC.getRemark(), QcState.QCING.getRemark());
        }
        String qcOrderNo = qcOrderPo.getQcOrderNo();
        boolean residentQc = qcOrderBaseService.isResidentQc(qcOrderNo);
        if (!residentQc) {
            qcCheckParamService.validateContainerCodeNotEmpty(dto);
        }

        // 若不良数量不为空，按批次码聚合不合格数量
        Map<Long, Integer> qcDetailIdUnPassedAmountMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(dto.getQcUnPassDetailItemList())) {
            // 按照批次码聚合
            qcDetailIdUnPassedAmountMap = dto.getQcUnPassDetailItemList()
                    .stream()
                    .collect(Collectors.groupingBy(QcUnPassDetailItemDto::getQcDetailId,
                            Collectors.summingInt(QcUnPassDetailItemDto::getNotPassAmount)));
        }

        final List<QcDetailPo> qcDetailPoList = qcDetailDao.getListByQcOrderNo(qcOrderPo.getQcOrderNo());
        if (CollectionUtils.isEmpty(qcDetailPoList)) {
            throw new BizException("质检单:{},质检详情数据出现错误，请联系系统管理员", qcOrderPo.getQcOrderNo());
        }
        qcCheckParamService.checkSubmitQcParam(dto, qcDetailIdUnPassedAmountMap, qcDetailPoList, residentQc);

        // 更新质检单状态
        boolean eqToBeQc = QcState.TO_BE_QC.equals(qcOrderPo.getQcState());
        if (eqToBeQc) {
            final QcState resultQcState = qcOrderPo.getQcState()
                    .toQcing();
            qcOrderPo.setQcState(qcOrderPo.getQcState()
                    .toQcing());
            // 质检变更状态时推送mq
            qcOrderBaseService.handleStatusChange(qcOrderPo.getQcOrderNo(), resultQcState, QcBizOperate.SUBMIT);
        }
        qcOrderDao.updateByIdVersion(qcOrderPo);
        if (eqToBeQc) {
            // 日志
            qcOrderBaseService.createStatusChangeLog(qcOrderPo);
        }


        // 处理质检详情
        this.handleQcDetail(qcOrderPo, qcDetailPoList, qcDetailIdUnPassedAmountMap, dto.getQcDetailHandItemList(),
                dto.getQcUnPassDetailItemList());
    }

    /**
     * 处理加工单详情
     *
     * @param qcOrderPo
     * @param qcDetailPoList
     * @param qcDetailIdUnPassedAmountMap
     * @param qcDetailHandItemList
     * @param qcUnPassDetailItemList
     */
    private void handleQcDetail(QcOrderPo qcOrderPo,
                                List<QcDetailPo> qcDetailPoList,
                                Map<Long, Integer> qcDetailIdUnPassedAmountMap,
                                List<QcDetailHandItemDto> qcDetailHandItemList,
                                List<QcUnPassDetailItemDto> qcUnPassDetailItemList) {
        // 加工质检
        boolean isProcessQc = StrUtil.isNotBlank(qcOrderPo.getProcessOrderNo());
        // 驻场质检
        boolean residentQc = qcOrderBaseService.isResidentQc(qcOrderPo.getQcOrderNo());


        // 获取正品的质检详情信息
        final Map<Long, QcDetailPo> qcDetailIdPoMap = qcDetailPoList.stream()
                .filter(qcDetailPo -> QcResult.PASSED.equals(qcDetailPo.getQcResult()))
                .collect(Collectors.toMap(QcDetailPo::getQcDetailId, Function.identity()));
        // 更新正品的质检详情信息
        for (QcDetailHandItemDto handItemDto : qcDetailHandItemList) {
            final QcDetailPo qcDetailPo = qcDetailIdPoMap.get(handItemDto.getQcDetailId());
            if (null == qcDetailPo) {
                throw new BizException("质检详情id:{}找不到对应的数据，数据错误，请联系系统管理员!",
                        handItemDto.getQcDetailId());
            }
            final Integer notPassAmountSum = qcDetailIdUnPassedAmountMap.getOrDefault(handItemDto.getQcDetailId(), 0);
            qcDetailPo.setPassAmount(handItemDto.getPassAmount());
            qcDetailPo.setWaitAmount(handItemDto.getAmount() - (handItemDto.getPassAmount() + notPassAmountSum));
            qcDetailPo.setRemark(handItemDto.getRemark());
        }
        qcDetailDao.updateBatchByIdVersion(qcDetailIdPoMap.values());

        // 删除现有的次品信息
        final List<QcDetailPo> unPassedQcDetailPoList = qcDetailPoList.stream()
                .filter(qcDetailPo -> QcResult.NOT_PASSED.equals(qcDetailPo.getQcResult()))
                .collect(Collectors.toList());
        String releaseContainerCode = "";
        if (CollectionUtils.isNotEmpty(unPassedQcDetailPoList)) {
            releaseContainerCode = unPassedQcDetailPoList.get(0)
                    .getContainerCode();
            // 删除记录
            qcDetailDao.removeUnPassedByQcOrderNo(qcOrderPo.getQcOrderNo());
        }


        // 次品信息不为空时重新添加
        if (CollectionUtils.isNotEmpty(qcUnPassDetailItemList)) {
            // 保存新的次品信息
            final List<QcDetailPo> newUnPassedQcDetailPoList = QcOrderConverter.convertUnPassedDtoToPo(
                    qcOrderPo.getQcOrderNo(),
                    qcUnPassDetailItemList, qcDetailIdPoMap);
            qcDetailDao.insertBatch(newUnPassedQcDetailPoList);

            if (isProcessQc || residentQc) {
                return;
            }
            final String occupyContainerCode = newUnPassedQcDetailPoList.get(0).getContainerCode();
            // 如果容器码发生变更,释放旧的容器，重新占用容器
            if (!occupyContainerCode.equals(releaseContainerCode)) {
                if (StringUtils.isNotBlank(releaseContainerCode)) {
                    IContainer releaseContainer = new QcContainer(releaseContainerCode, qcOrderPo.getWarehouseCode());
                    releaseContainer.tryReleaseContainer();
                }

                IContainer occupyContainer = new QcContainer(occupyContainerCode, qcOrderPo.getWarehouseCode());
                occupyContainer.tryOccupyContainer();
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void approve(QcApproveDto dto) {
        final List<QcOrderPo> qcOrderPoList = qcOrderDao.getByQcOrderNos(dto.getQcOrderNoList());
        if (CollectionUtils.isEmpty(qcOrderPoList) || qcOrderPoList.size() != dto.getQcOrderNoList()
                .size()) {
            throw new BizException("质检数据有误，请联系系统管理员");
        }
        // 赋值审核人与审核时间
        qcOrderPoList.forEach(qcOrderPo -> {
            qcOrderPo.setAuditor(GlobalContext.getUsername());
            qcOrderPo.setAuditTime(LocalDateTime.now());
        });

        if (QcApproveOperate.PASSED.equals(dto.getQcApproveOperate())) {
            this.approvePassed(qcOrderPoList);
        } else if (QcApproveOperate.NOT_PASSED.equals(dto.getQcApproveOperate())) {
            this.approveUnPassed(qcOrderPoList);
        }
    }

    /**
     * 质检审核通过操作
     *
     * @param qcOrderPoList
     */
    private void approvePassed(List<QcOrderPo> qcOrderPoList) {
        final List<String> qcOrderNoList = qcOrderPoList.stream().map(QcOrderPo::getQcOrderNo).collect(Collectors.toList());

        // 处理次品信息
        final List<QcDetailPo> qcDetailPoList = qcDetailDao.getListByQcOrderNoList(qcOrderNoList);
        if (CollectionUtils.isEmpty(qcDetailPoList)) {
            throw new BizException("质检单号:{}质检数据异常，请联系系统管理员！", qcOrderNoList);
        }
        final Map<String, List<QcDetailPo>> qcOrderNoUnPassedDetailListMap = qcDetailPoList.stream()
                .filter(qcDetailPo -> qcDetailPo.getNotPassAmount() > 0)
                .collect(Collectors.groupingBy(QcDetailPo::getQcOrderNo));
        final List<QcReceiveOrderPo> qcReceiveOrderPoList = qcReceiveOrderDao.getListByQcOrderNoList(qcOrderNoList);
        final Map<String, QcReceiveOrderPo> qcOrderNoReceivePoMap = qcReceiveOrderPoList.stream()
                .collect(Collectors.toMap(QcReceiveOrderPo::getQcOrderNo, Function.identity()));
        final Map<String, List<QcDetailPo>> qcOrderNoDetailListMap = qcDetailPoList.stream()
                .collect(Collectors.groupingBy(QcDetailPo::getQcOrderNo));

        // 记录质检单对应的次品处理情况
        Map<String, List<DefectType>> qcOrderNoDefectTypeListMap = new HashMap<>();
        qcOrderPoList.forEach(qcOrderPo -> {
            final DefectType defectType = this.handleCreateDefect(qcOrderPo,
                    qcOrderNoReceivePoMap.get(qcOrderPo.getQcOrderNo()),
                    qcOrderNoUnPassedDetailListMap.get(qcOrderPo.getQcOrderNo()),
                    qcOrderNoDetailListMap.get(qcOrderPo.getQcOrderNo()));
            if (qcOrderNoDefectTypeListMap.containsKey(qcOrderPo.getQcOrderNo())) {
                qcOrderNoDefectTypeListMap.get(qcOrderPo.getQcOrderNo()).add(defectType);
            } else {
                qcOrderNoDefectTypeListMap.put(qcOrderPo.getQcOrderNo(), Lists.newArrayList(defectType));
            }
        });

        // 存在次品信息，则进入次品处理中。不存在次品信息，进入完结状态
        qcOrderPoList.forEach(qcOrderPo -> {
            QcState resultQcState = null;
            final List<DefectType> defectTypeList = qcOrderNoDefectTypeListMap.get(qcOrderPo.getQcOrderNo());
            // 生成次品处理记录的数量
            final long defectCnt = defectTypeList.stream().filter(DefectType.DEFECT::equals).count();
            // 次品处理列表为空或者不生成次品处理,则质检单状态为已完结
            if (CollectionUtils.isEmpty(defectTypeList) || defectCnt == 0) {
                resultQcState = qcOrderPo.getQcState().toFinished();
            } else {
                resultQcState = qcOrderPo.getQcState().toDefectHandling();
            }
            qcOrderPo.setQcState(resultQcState);
            // 质检变更状态时推送mq
            qcOrderBaseService.handleStatusChange(qcOrderPo.getQcOrderNo(), resultQcState, QcBizOperate.APPROVE_PASS);
        });

        qcOrderDao.updateBatchByIdVersion(qcOrderPoList);
        // 日志
        qcOrderPoList.forEach(qcOrderBaseService::createStatusChangeLog);

        // 若质检单变更为已完结，则释放不合格容器
        qcOrderPoList.stream().filter(qcOrderPo -> QcState.FINISHED.equals(qcOrderPo.getQcState()))
                .forEach(qcOrderPo -> {
                    this.releaseContainerAfterApprove(qcDetailPoList, qcOrderPo, qcOrderNoUnPassedDetailListMap);
                });
    }

    /**
     * @Description 审核通过后容器操作
     * @author yanjiawei
     * @Date 2024/7/11 10:49
     */
    private void releaseContainerAfterApprove(List<QcDetailPo> qcDetailPoList,
                                              QcOrderPo qcOrderPo,
                                              Map<String, List<QcDetailPo>> qcOrderNoUnPassedDetailListMap) {
        // 加工质检
        boolean isProcessQc = StrUtil.isNotBlank(qcOrderPo.getProcessOrderNo());
        // 驻场质检
        boolean residentQc = qcOrderBaseService.isResidentQc(qcOrderPo.getQcOrderNo());
        // 出库质检
        boolean outBoundQc = qcOrderBaseService.isOutBoundQc(qcOrderPo.getQcOrderNo());

        if (isProcessQc) {
            log.info("加工质检不释放容器");
        } else if (residentQc) {
            log.info("驻场质检不释放容器");
        } else if (outBoundQc) {
            Set<String> relCotList
                    = qcDetailPoList.stream().map(QcDetailPo::getContainerCode).collect(Collectors.toSet());
            relCotList.forEach(containerCode -> {
                IContainer container = new QcContainer(containerCode, qcOrderPo.getWarehouseCode());
                if (!container.tryReleaseContainer()) {
                    throw new ParamIllegalException("释放容器{}失败！", containerCode);
                }
            });
        } else {
            List<QcDetailPo> unPassQcDetailList = qcOrderNoUnPassedDetailListMap.get(qcOrderPo.getQcOrderNo());
            if (CollectionUtils.isEmpty(unPassQcDetailList)) {
                return;
            }
            Set<String> relCotList
                    = unPassQcDetailList.stream().map(QcDetailPo::getContainerCode).collect(Collectors.toSet());
            relCotList.forEach(containerCode -> {
                IContainer container = new QcContainer(containerCode, qcOrderPo.getWarehouseCode());
                if (!container.tryReleaseContainer()) {
                    throw new ParamIllegalException("释放容器{}失败！", containerCode);
                }
            });
        }
    }

    /**
     * 质检审核不通过
     *
     * @param qcOrderPoList
     */
    public void approveUnPassed(List<QcOrderPo> qcOrderPoList) {
        qcOrderPoList.forEach(qcOrderPo -> {
            final QcState resultQcState = qcOrderPo.getQcState().unPassedToBeQc();
            qcOrderPo.setQcState(resultQcState);
            // 质检变更状态时推送mq
            qcOrderBaseService.handleStatusChange(qcOrderPo.getQcOrderNo(), resultQcState, QcBizOperate.APPROVE_UN_PASSED);
        });

        qcOrderDao.updateBatchByIdVersion(qcOrderPoList);

        // 日志
        qcOrderPoList.forEach(qcOrderBaseService::createStatusChangeLog);

        // 重置质检详情
        this.resetQcDetail(qcOrderPoList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void resetQc(QcOrderNoListDto dto) {
        final List<QcOrderPo> qcOrderPoList = qcOrderDao.getByQcOrderNos(dto.getQcOrderNoList());
        if (CollectionUtils.isEmpty(qcOrderPoList) || qcOrderPoList.size() != dto.getQcOrderNoList()
                .size()) {
            throw new BizException("质检数据有误，请联系系统管理员");
        }
        qcOrderPoList.forEach(qcOrderPo -> {
            final QcState resultQcState = qcOrderPo.getQcState()
                    .resetQc();
            qcOrderPo.setQcState(resultQcState);
            // 质检变更状态时推送mq
            qcOrderBaseService.handleStatusChange(qcOrderPo.getQcOrderNo(), resultQcState, QcBizOperate.RESET);
        });
        qcOrderDao.updateBatchByIdVersion(qcOrderPoList);
        // 日志
        qcOrderPoList.forEach(qcOrderBaseService::createStatusChangeLog);


        // 重置质检详情
        this.resetQcDetail(qcOrderPoList);
    }

    /**
     * 重置质检详情信息
     *
     * @param qcOrderPoList
     */
    private void resetQcDetail(List<QcOrderPo> qcOrderPoList) {
        if (CollectionUtils.isEmpty(qcOrderPoList)) {
            return;
        }

        // 恢复合格明细
        final List<String> qcOrderNoList = qcOrderPoList.stream()
                .map(QcOrderPo::getQcOrderNo)
                .collect(Collectors.toList());
        final List<QcDetailPo> qcDetailPoList = qcDetailDao.getListByQcOrderNoList(qcOrderNoList);
        final List<QcDetailPo> passedQcDetailPoList = qcDetailPoList.stream()
                .filter(qcDetailPo -> QcResult.PASSED.equals(qcDetailPo.getQcResult()))
                .collect(Collectors.toList());
        passedQcDetailPoList.forEach(passedQcDetailPo -> {
            passedQcDetailPo.setWaitAmount(passedQcDetailPo.getAmount());
            passedQcDetailPo.setPassAmount(0);
        });
        qcDetailDao.updateBatchByIdVersion(passedQcDetailPoList);

        // 删除不良信息，释放容器
        final Map<String, QcOrderPo> qcOrderNoPoMap = qcOrderPoList.stream()
                .collect(Collectors.toMap(QcOrderPo::getQcOrderNo, Function.identity()));
        final List<QcDetailPo> unPassedQcDetailPoList = qcDetailPoList.stream()
                .filter(qcDetailPo -> QcResult.NOT_PASSED.equals(qcDetailPo.getQcResult()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(unPassedQcDetailPoList)) {
            qcDetailDao.removeUnPassedByQcOrderNoList(qcOrderNoList);
            final Map<String, List<QcDetailPo>> qcOrderNoUnPassedDetailPoMap = unPassedQcDetailPoList.stream()
                    .collect(Collectors.groupingBy(QcDetailPo::getQcOrderNo));
            qcOrderNoUnPassedDetailPoMap.forEach((key, value) -> {
                final QcDetailPo qcDetailPo = value.get(0);
                final QcOrderPo qcOrderPo = qcOrderNoPoMap.get(qcDetailPo.getQcOrderNo());
                if (null == qcOrderPo) {
                    throw new BizException("质检次品信息数据错误，请联系系统管理员！");
                }
                // 加工质检
                boolean isProcessQc = StrUtil.isNotBlank(qcOrderPo.getProcessOrderNo());
                // 驻场质检
                boolean residentQc = qcOrderBaseService.isResidentQc(qcOrderPo.getQcOrderNo());
                if (!isProcessQc && !residentQc) {
                    IContainer container = new QcContainer(qcDetailPo.getContainerCode(), qcOrderPo.getWarehouseCode());
                    container.tryReleaseContainer();
                }
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void completedQc(QcCompletedQcDto dto) {
        final QcOrderPo qcOrderPo = qcOrderDao.getByIdVersion(dto.getQcOrderId(), dto.getVersion());
        if (null == qcOrderPo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        if (!QcState.QCING.equals(qcOrderPo.getQcState()) && !QcState.TO_BE_QC.equals(qcOrderPo.getQcState())) {
            throw new ParamIllegalException("当前质检单状态不处于{}或{},请刷新后重试", QcState.TO_BE_QC.getRemark(), QcState.QCING.getRemark());
        }
        final List<QcDetailPo> qcDetailPoList = qcDetailDao.getListByQcOrderNo(qcOrderPo.getQcOrderNo());
        if (CollectionUtils.isEmpty(qcDetailPoList)) {
            throw new BizException("质检单:{}找不到对应的详情数据，数据错误，请联系系统管理员！", qcOrderPo.getQcOrderNo());
        }
        // 是否驻场质检
        boolean residentQc = qcOrderBaseService.isResidentQc(qcOrderPo.getQcOrderNo());
        if (!residentQc) {
            qcCheckParamService.validateContainerCodeNotEmpty(dto);
        }

        // 获取批次码与sku的关系
        final Map<String, String> skuBatchCodeSkuMap = qcDetailPoList.stream()
                .collect(Collectors.toMap(QcDetailPo::getBatchCode, QcDetailPo::getSkuCode, (item1, item2) -> item1));

        // 若不良数量不为空，按批次码聚合不合格数量
        Map<Long, Integer> qcDetailIdUnPassedAmountMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(dto.getQcUnPassDetailItemList())) {
            // 按照质检id聚合
            qcDetailIdUnPassedAmountMap = dto.getQcUnPassDetailItemList()
                    .stream()
                    .collect(Collectors.groupingBy(QcUnPassDetailItemDto::getQcDetailId,
                            Collectors.summingInt(QcUnPassDetailItemDto::getNotPassAmount)));
        }

        // 整单合格/整单不合格，重置参数并打印。同时获取质检单状态
        QcResult qcResult = null;
        if (QcOperate.COMPLETED.equals(dto.getQcOperate())) {
            if (CollectionUtils.isEmpty(dto.getQcUnPassDetailItemList())) {
                qcResult = QcResult.PASSED;
            } else {
                final int totalPassAmount = dto.getQcDetailHandItemList()
                        .stream()
                        .mapToInt(QcDetailHandItemDto::getPassAmount)
                        .sum();
                qcResult = totalPassAmount == 0 ? QcResult.NOT_PASSED : QcResult.FEW_NOT_PASSED;
            }
        } else if (QcOperate.PASSED.equals(dto.getQcOperate())) {
            // 先校验每个合格明细的合格数是大于0的
            dto.getQcDetailHandItemList().forEach(itemDto -> {
                if (itemDto.getPassAmount() <= 0 && StrUtil.isNotBlank(itemDto.getContainerCode())) {
                    throw new ParamIllegalException(
                            "整单合格的质检正品数应该大于0，容器:{}的正品数不满足该要求，请重新填写！",
                            itemDto.getContainerCode());
                } else if (itemDto.getPassAmount() <= 0) {
                    throw new ParamIllegalException("整单合格的质检正品数应该大于0，正品数不满足该要求，请重新填写！");
                }
            });

            // 整单合格，质检合格数等于质检交接数
            dto.getQcDetailHandItemList()
                    .forEach(itemDto -> itemDto.setPassAmount(itemDto.getAmount()));
            log.info("质检单:{}，操作整单合格之后，入参变更为:{}", qcOrderPo.getQcOrderNo(),
                    JSONUtil.toJsonStr(dto.getQcDetailHandItemList()));
            qcResult = QcResult.PASSED;
        } else if (QcOperate.NOT_PASSED.equals(dto.getQcOperate())) {
            // 整单不合格，校验不合格数之和是否等于交接数，
            // 若不等于，补充一行新的次品信息与最后一行次品信息内容一致，数量为交接数与次品数的差值
            this.updateDtoForQcNotPassed(dto, qcDetailIdUnPassedAmountMap, skuBatchCodeSkuMap);
            // 重新聚合。按照质检id聚合
            qcDetailIdUnPassedAmountMap = dto.getQcUnPassDetailItemList()
                    .stream()
                    .collect(Collectors.groupingBy(QcUnPassDetailItemDto::getQcDetailId,
                            Collectors.summingInt(QcUnPassDetailItemDto::getNotPassAmount)));
            qcResult = QcResult.NOT_PASSED;
        }

        // 校验入参
        qcCheckParamService.checkCompletedQc(qcOrderPo, qcDetailPoList, dto, qcDetailIdUnPassedAmountMap, residentQc);

        // 质检单标识=驻场质检，状态流转到已完结
        QcState resultQcState;
        if (residentQc) {
            resultQcState = qcOrderPo.getQcState().toCompletedQc(QcResult.PASSED);
        } else {
            resultQcState = qcOrderPo.getQcState().toCompletedQc(qcResult);
        }

        qcOrderPo.setQcResult(qcResult);
        qcOrderPo.setQcState(resultQcState);
        qcOrderPo.setTaskFinishTime(LocalDateTime.now());
        qcOrderPo.setOperator(GlobalContext.getUserKey());
        qcOrderPo.setOperatorName(GlobalContext.getUsername());
        qcOrderDao.updateByIdVersion(qcOrderPo);
        // 日志
        qcOrderBaseService.createStatusChangeLog(qcOrderPo);

        // 处理质检详情
        this.handleQcDetail(qcOrderPo, qcDetailPoList, qcDetailIdUnPassedAmountMap, dto.getQcDetailHandItemList(),
                dto.getQcUnPassDetailItemList());
        qcOrderBaseService.handleStatusChange(qcOrderPo.getQcOrderNo(), resultQcState, QcBizOperate.COMPLETE);

        // 整单合格完成质检，质检单会进入已完结状态，释放容器
        if (QcState.FINISHED.equals(qcOrderPo.getQcState())) {
            // 加工前置质检
            boolean isProcessQc = StrUtil.isNotBlank(qcOrderPo.getProcessOrderNo());
            // 出库质检
            boolean outBoundQc = qcOrderBaseService.isOutBoundQc(qcOrderPo.getQcOrderNo());

            if (outBoundQc) {
                Set<String> reCotCodes
                        = qcDetailPoList.stream().map(QcDetailPo::getContainerCode).filter(StrUtil::isNotBlank).collect(Collectors.toSet());
                reCotCodes.forEach(containerCode -> {
                    IContainer releaseContainer = new QcContainer(containerCode, qcOrderPo.getWarehouseCode());
                    boolean release = releaseContainer.tryReleaseContainer();
                    if (!release) {
                        throw new ParamIllegalException("释放容器:{}失败！", containerCode);
                    }
                });
            } else if (isProcessQc) {
                log.info("加工质检不释放容器");
            } else if (residentQc) {
                log.info("驻场质检不释放容器");
            } else {
                Set<String> containerCodes = qcDetailPoList.stream()
                        .filter(qcDetailPo -> QcResult.NOT_PASSED.equals(qcDetailPo.getQcResult()))
                        .map(QcDetailPo::getContainerCode)
                        .collect(Collectors.toSet());

                containerCodes.forEach(containerCode -> {
                    IContainer releaseContainer = new QcContainer(containerCode, qcOrderPo.getWarehouseCode());
                    boolean release = releaseContainer.tryReleaseContainer();
                    if (!release) {
                        throw new ParamIllegalException("释放容器:{}失败！", containerCode);
                    }
                });
            }
        }
    }

    /**
     * 重置整单不合格入参并打印
     *
     * @param dto
     * @param qcDetailIdUnPassedAmountMap
     * @param skuBatchCodeSkuMap
     */
    private void updateDtoForQcNotPassed(QcCompletedQcDto dto,
                                         Map<Long, Integer> qcDetailIdUnPassedAmountMap,
                                         Map<String, String> skuBatchCodeSkuMap) {
        final List<QcUnPassDetailItemDto> qcUnPassDetailItemList = dto.getQcUnPassDetailItemList();
        if (CollectionUtils.isEmpty(qcUnPassDetailItemList)) {
            throw new ParamIllegalException("操作整单不合格时，次品信息不能为空，至少填写一行次品信息");
        }
        // 模版dto
        final QcUnPassDetailItemDto stencilDto = qcUnPassDetailItemList.get(qcUnPassDetailItemList.size() - 1);

        dto.getQcDetailHandItemList()
                .forEach(qcDetailHandItemDto -> {
                    final Integer unPassedAmount = qcDetailIdUnPassedAmountMap.getOrDefault(
                            qcDetailHandItemDto.getQcDetailId(), 0);

                    // 若质检数与不合格总数相等，则无需处理
                    if (qcDetailHandItemDto.getAmount()
                            .equals(unPassedAmount)) {
                        return;
                    }
                    final QcUnPassDetailItemDto newDto = QcUnPassDetailItemDto.copyObj(stencilDto,
                            qcDetailHandItemDto.getQcDetailId());
                    newDto.setNotPassAmount(qcDetailHandItemDto.getAmount() - unPassedAmount);
                    newDto.setSku(skuBatchCodeSkuMap.get(qcDetailHandItemDto.getSkuBatchCode()));
                    newDto.setSkuBatchCode(qcDetailHandItemDto.getSkuBatchCode());
                    dto.getQcUnPassDetailItemList()
                            .add(newDto);
                    newDto.setPlatform(qcDetailHandItemDto.getPlatform());
                });

        log.info("质检单id:{}，操作整单不合格之后，入参变更为:{}", dto.getQcOrderId(),
                JSONUtil.toJsonStr(dto.getQcUnPassDetailItemList()));
    }

    /**
     * 处理次品
     *
     * @param qcOrderPo
     * @param qcReceiveOrderPo
     * @param unPassedQcDetailPoList
     * @param qcDetailPoList
     */
    private DefectType handleCreateDefect(QcOrderPo qcOrderPo,
                                          QcReceiveOrderPo qcReceiveOrderPo,
                                          List<QcDetailPo> unPassedQcDetailPoList,
                                          List<QcDetailPo> qcDetailPoList) {
        // 查找不到收货单数据直接返回
        if (null == qcReceiveOrderPo) {
            log.info("质检单：{}，找不到对应的收货单，不需要进入次品处理逻辑", qcOrderPo.getQcOrderNo());
            return DefectType.EXCEPTION;
        }

        if (CollectionUtils.isEmpty(unPassedQcDetailPoList)) {
            log.info("质检单：{}，不存在次品信息，无需生成次品处理记录", qcOrderPo.getQcOrderNo());
            return DefectType.EXCEPTION;
        }

        boolean outBoundQc = qcOrderBaseService.isOutBoundQc(qcOrderPo.getQcOrderNo());
        if (outBoundQc) {
            log.info("出库质检单{}不创建次品处理。", qcOrderPo.getQcOrderNo());
            return DefectType.EXCEPTION;
        }

        if (WmsEnum.ReceiveType.DEFECTIVE_PROCESS_PRODUCT.equals(qcReceiveOrderPo.getReceiveType())
                || WmsEnum.ReceiveType.OTHER.equals(qcReceiveOrderPo.getReceiveType())
                || WmsEnum.ReceiveType.TRANSFER.equals(qcReceiveOrderPo.getReceiveType())
                || WmsEnum.ReceiveType.SALE_RETURN.equals(qcReceiveOrderPo.getReceiveType())
                || WmsEnum.ReceiveType.RETURN.equals(qcReceiveOrderPo.getReceiveType())) {
            log.info("质检单对应的收货单类型为:{},不创建次品处理,{}", qcReceiveOrderPo.getReceiveType().getRemark(), qcReceiveOrderPo);
            return DefectType.EXCEPTION;
        }

        // 校验质检单号是否生成
        final String qcOrderNo = qcOrderPo.getQcOrderNo();
        final List<DefectHandlingPo> defectHandlingPoList = defectRefService.getListByQcNo(qcOrderNo);
        if (CollectionUtils.isNotEmpty(defectHandlingPoList)) {
            log.error("质检单号:{}已经生成次品记录!", qcOrderNo);
            return DefectType.EXCEPTION;
        }

        final List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList
                = purchaseReturnOrderItemDao.getListByReturnBizNo(qcOrderNo);
        if (CollectionUtils.isNotEmpty(purchaseReturnOrderItemPoList)) {
            log.error("质检单号:{}已经生成退货单!", qcOrderNo);
            return DefectType.EXCEPTION;
        }

        // 计算合格数
        final int totalPassAmount = qcDetailPoList.stream()
                .mapToInt(QcDetailPo::getPassAmount)
                .sum();
        // 计算不合格数
        final int totalNotPassAmount = unPassedQcDetailPoList.stream()
                .mapToInt(QcDetailPo::getNotPassAmount)
                .sum();

        // 获取批次码的价格
        List<String> skuBatchCodeList = qcDetailPoList.stream()
                .map(QcDetailPo::getBatchCode)
                .filter(StringUtils::isNotBlank)
                .distinct().collect(Collectors.toList());
        Map<String, BigDecimal> skuBatchCodePriceMap = wmsRemoteService.getSkuBatchPriceMapBySkuBatchList(skuBatchCodeList);
        log.info("生成次品记录时获取批次码价格Map={}", JacksonUtil.parse2Str(skuBatchCodePriceMap));
        // 大货类型
        if (WmsEnum.ReceiveType.BULK.equals(qcReceiveOrderPo.getReceiveType())) {
            final PurchaseDeliverOrderPo purchaseDeliverOrderPo = purchaseDeliverRefService.getPurchaseDeliverOrderByNo(
                    qcReceiveOrderPo.getScmBizNo());
            if (null == purchaseDeliverOrderPo) {
                throw new BizException("发货单:{}不存在，创建次品处理记录失败！", qcReceiveOrderPo.getScmBizNo());
            }

            final DefectRate defectRate = this.getDefectRate(qcOrderPo.getQcAmount(), totalNotPassAmount);
            // 不为返修类型的才生成退货单
            if (!QcOriginProperty.REPAIR.equals(qcOrderPo.getQcOriginProperty())) {
                // 不良率低或整单不合格,生成退货单
                if (DefectRate.LOW_DEFECT_RATE.equals(defectRate) || DefectRate.ALL_DEFECTS.equals(defectRate)) {
                    final SupplierPo supplierPo = supplierBaseService.getSupplierByCode(qcReceiveOrderPo.getSupplierCode());
                    final QcDetailPo qcDetailPo = qcDetailPoList.get(0);
                    final ReturnOrderBo returnOrderBo = QcOrderConverter.qcDefectPoToBo(qcReceiveOrderPo,
                            DefectRate.LOW_DEFECT_RATE.equals(defectRate) ? ReturnType.QC_NOT_PASSED_PART : ReturnType.QC_NOT_PASSED_ALL,
                            skuBatchCodePriceMap, purchaseDeliverOrderPo, unPassedQcDetailPoList, totalNotPassAmount, supplierPo, qcDetailPo.getPlatform());

                    // 释放可发货数
                    purchaseRefService.addPurchaseShippableCntByNo(purchaseDeliverOrderPo.getPurchaseChildOrderNo(),
                            returnOrderBo.getExpectedReturnCnt());
                    returnRefService.createReturnOrder(returnOrderBo);
                    return DefectType.RETURN;
                }
            }
        }

        final List<SimpleSkuBatchVo> simpleSkuBatchVoList = wmsRemoteService.getSkuBatchByBatchCodes(skuBatchCodeList);
        final Map<String, String> batchCodeSupplierCodeMap = simpleSkuBatchVoList.stream()
                .collect(Collectors.toMap(SimpleSkuBatchVo::getBatchCode, SimpleSkuBatchVo::getSupplierCode));

        final List<DefectHandlingCreateBo> defectHandlingCreateBoList = DefectHandlingConverter.qcDefectMqDtoToBo(
                qcOrderPo, skuBatchCodePriceMap, unPassedQcDetailPoList, qcReceiveOrderPo,
                totalPassAmount, batchCodeSupplierCodeMap, qcOrderPo.getQcOriginProperty());

        defectRefService.createDefectHandling(defectHandlingCreateBoList);
        return DefectType.DEFECT;
    }

    /**
     * 计算不良率
     *
     * @param qcAmount
     * @param totalReturnAmount
     */
    private DefectRate getDefectRate(Integer qcAmount,
                                     Integer totalReturnAmount) {
        // 整单不合格
        if (qcAmount.equals(totalReturnAmount)) {
            return DefectRate.ALL_DEFECTS;
        }

        // 质检数小于等于100
        if (qcAmount <= QC_STANDER_NUM) {
            return DefectRate.LOW_DEFECT_RATE;
        }

        if ((NumberUtil.div(totalReturnAmount, qcAmount)
                .compareTo(LOW_DEFECT_RATE) <= 0)) {
            return DefectRate.LOW_DEFECT_RATE;
        }

        return DefectRate.HIGH_DEFECT_RATE;
    }

    /**
     * 根据查询条件获取质检单和详情信息，并将它们合并到一个 QcVo 对象中。
     *
     * @param qcOrderQueryDto 包含查询条件的数据传输对象。
     * @return 合并后的 QcVo 对象，包含质检单信息和相关详情。
     */
    public QcVo findQcOrderAndDetails(QcOrderQueryDto qcOrderQueryDto) {
        // 从查询条件中获取质检单号和收货单号
        final Collection<String> queryQcOrderNos = qcOrderQueryDto.getQcOrderNos();
        final Collection<String> queryReceiveNos = qcOrderQueryDto.getReceiveNos();

        // 如果查询条件中没有质检单号和收货单号，返回空
        if (CollectionUtils.isEmpty(queryQcOrderNos) && CollectionUtils.isEmpty(queryReceiveNos)) {
            return null;
        }

        // 根据质检单号查询质检单信息
        final List<QcOrderPo> qcOrderPosByQcOrderNos = qcOrderDao.getByQcOrderNos(queryQcOrderNos);

        // 根据收货单号查询质检单信息
        final List<QcOrderPo> qcOrderPosByReceiveNos = qcOrderDao.getByReceiveNos(queryReceiveNos);

        // 合并查询结果，按质检单主键 id 去重
        List<QcOrderPo> mergedQcOrderList = new ArrayList<>(Stream.of(qcOrderPosByQcOrderNos, qcOrderPosByReceiveNos)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toMap(QcOrderPo::getQcOrderId,
                        Function.identity(),
                        (existing, replacement) -> existing))
                .values());

        // 获取合并后的质检单号
        final Set<String> qcOrderNos = mergedQcOrderList.stream()
                .map(QcOrderPo::getQcOrderNo)
                .collect(Collectors.toSet());

        // 根据质检单号获取质检单详情信息
        final List<QcDetailPo> qcDetailPos = qcDetailDao.getListByQcOrderNoList(qcOrderNos);

        // 将质检单信息和质检单详情信息转换成 QcVo 对象
        return QcOrderConverter.toQcVo(mergedQcOrderList, qcDetailPos);
    }

    /**
     * 处理接收订单质检单消息，创建相应的质检单。
     *
     * @param message 包含接收订单质检单信息的数据传输对象。
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleCreateQcMessage(ReceiveOrderQcOrderDto message) {
        // 将接收订单质检单消息转换为业务对象
        ReceiveOrderQcOrderBo receiveOrderQcOrderBo = QcOrderConverter.toReceiveOrderQcOrderBo(message);

        // 创建接收订单质检单的具体操作类
        AbstractQcOrderCreator<ReceiveOrderQcOrderBo, QcOrderChangeDto> qcOrderCreator
                = new ReceiveOrderQcOrderCreator(qcOrderDao,
                qcDetailDao,
                qcReceiveOrderDao,
                idGenerateService,
                consistencySendMqService,
                qcOrderBaseService,
                supplierDao,
                plmRemoteService,
                wmsRemoteService);

        // 调用创建质检单的方法
        qcOrderCreator.createQcOrder(receiveOrderQcOrderBo);
    }

    /**
     * 获取质检明细导出的统计总数。
     * <p>
     * 根据不同的导出策略（按质检单或不良原因），选择相应的策略进行统计，返回符合条件的记录数量。
     *
     * @param qcSearchDto 质检明细导出筛选的输入参数，包含导出策略和其他筛选条件。
     * @param qcSearchDto 输入参数对象，包含筛选条件和导出策略。
     * @return 质检明细导出的统计总数，根据指定策略进行统计。如果未指定有效的导出策略，将抛出 ParamIllegalException 异常。
     * @return 符合条件的记录数量，根据选择的导出策略统计。
     * @throws ParamIllegalException 如果未指定有效的导出策略，将抛出此异常。
     */
    public CommonResult<Integer> getExportTotals(QcSearchDto qcSearchDto) {
        final QcExportType type = qcSearchDto.getQcExportType();

        QcExportFilterStrategy qcExportFilterStrategy
                = qcExportStrategyFactory.getQcExportFilterStrategy(type);
        return qcExportFilterStrategy.filterCount(qcSearchDto);
    }


    /**
     * 获取质检明细导出列表的分页数据。
     * <p>
     * 根据不同的导出策略（按质检单或不良原因），选择相应的策略进行导出，返回分页数据结果。
     *
     * @param qcSearchDto 质检明细导出搜索条件的 DTO，包含筛选条件和导出策略。
     * @param qcSearchDto 输入参数对象，包含筛选条件和导出策略。
     * @return 质检明细导出列表的分页数据结果，根据选择的导出策略进行导出。如果未指定有效的导出策略，将抛出 ParamIllegalException 异常。
     * @return 符合条件的质检明细导出列表的分页数据。
     * @throws ParamIllegalException 如果未指定有效的导出策略，将抛出此异常。
     */
    public CommonResult<ExportationListResultBo<QcExportVo>> getQcDetailExportList(QcSearchDto qcSearchDto) {
        final QcExportType type = qcSearchDto.getQcExportType();

        QcExportFilterStrategy qcExportFilterStrategy
                = qcExportStrategyFactory.getQcExportFilterStrategy(type);
        return qcExportFilterStrategy.filterList(qcSearchDto);
    }


    /**
     * 导出质检明细数据。
     * <p>
     * 根据不同的导出策略（按质检单或不良原因），选择相应的策略进行导出。首先，检查是否有可导出的数据，如果数据为空，则抛出异常。
     * 然后，根据导出策略执行导出操作，将导出请求发送到消息队列，以执行异步导出。
     *
     * @param dto 质检明细导出搜索条件的 DTO，包含筛选条件和导出策略。
     * @throws ParamIllegalException 如果导出数据为空或未指定有效的导出策略，将抛出此异常。
     */
    @Transactional(rollbackFor = Exception.class)
    public void exportQc(QcSearchDto dto) {
        if (null == this.searchQcWhere(dto)) {
            throw new ParamIllegalException("导出数据为空！");
        }
        CommonResult<Integer> qcDetailExportTotals = this.getExportTotals(dto);

        if (Objects.isNull(qcDetailExportTotals) || qcDetailExportTotals.getData() == 0) {
            throw new ParamIllegalException("导出数据为空！");
        }

        final QcExportType qcExportType = dto.getQcExportType();

        if (Objects.equals(QcExportType.BY_QC_ORDER, qcExportType)) {
            consistencySendMqService.execSendMq(ScmExportHandler.class,
                    new FileOperateMessageDto<>(GlobalContext.getUserKey(),
                            GlobalContext.getUsername(),
                            FileOperateBizType.SCM_QC_ORDER_EXPORT.getCode(),
                            dto));
        } else if (Objects.equals(QcExportType.BY_QC_ORDER_DETAIL, qcExportType)) {
            consistencySendMqService.execSendMq(ScmExportHandler.class,
                    new FileOperateMessageDto<>(GlobalContext.getUserKey(),
                            GlobalContext.getUsername(),
                            FileOperateBizType.SCM_QC_DETAIL_EXPORT.getCode(),
                            dto));
        } else {
            throw new ParamIllegalException("导出质检信息异常！原因：未指定导出业务类型。");
        }
    }


    /**
     * 处理Sku编码相关逻辑，包括从远程服务获取Sku信息，处理Sku编码，以及更新相关属性。
     *
     * @param qcSearchDto 包含Sku编码和其他搜索参数的数据传输对象
     * @return 更新后的数据传输对象
     */
    public QcSearchDto processSkuCode(QcSearchDto qcSearchDto) {
        if (StringUtils.isNotBlank(qcSearchDto.getSkuEncode())) {
            // 从远程服务获取Sku信息
            List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(
                    List.of(qcSearchDto.getSkuEncode()));

            if (CollectionUtils.isEmpty(skuListByEncode)) {
                // 如果Sku信息为空，返回null
                return null;
            }

            if (CollectionUtils.isEmpty(qcSearchDto.getSkuList())) {
                // 如果Sku列表为空，直接设置为获取的Sku信息
                qcSearchDto.setSkuList(skuListByEncode);
            } else {
                // 否则，保留与获取的Sku信息匹配的部分
                qcSearchDto.getSkuList()
                        .retainAll(skuListByEncode);
            }
        }

        return qcSearchDto;
    }

    /**
     * 根据质检订单号列表获取对应的 QcSearchVo 列表。
     *
     * @param qcOrderNos 质检订单号列表
     * @return 包含 QcDetailVo 列表的结果对象
     */
    public List<QcDetailVo> getQcDetailVoList(List<String> qcOrderNos) {
        if (CollectionUtils.isEmpty(qcOrderNos)) {
            return Collections.emptyList();
        }

        return this.qcDetails(qcOrderNos);
    }


    /**
     * 根据质检订单号获取对应的 QcSearchVo 对象。
     *
     * @param qcOrderNos 质检订单号
     * @return 包含 QcSearchVo 对象的结果，如果未找到匹配订单号的信息，返回null
     */
    /**
     * 根据质检订单号获取对应的 QcSearchVo 对象。
     *
     * @param qcOrderNoList 质检订单号
     * @return 包含 QcSearchVo 对象的结果，如果未找到匹配订单号的信息，返回null
     */
    public List<QcSearchVo> getQcSearchVos(List<String> qcOrderNoList) {
        // 检查输入的质检订单号是否为空
        if (CollectionUtils.isEmpty(qcOrderNoList)) {
            return null;
        }

        // 创建质检搜索参数对象，设置页码、每页数量和质检订单号
        QcSearchDto qcSearchDto = new QcSearchDto();
        qcSearchDto.setPageNo(1);
        qcSearchDto.setPageSize(qcOrderNoList.size());
        qcSearchDto.setQcOrderNoList(qcOrderNoList);

        // 调用搜索方法以获取包含 QcSearchVo 对象的结果
        CommonPageResult.PageInfo<QcSearchVo> qcSearchVoPageInfo = this.searchQc(qcSearchDto);

        // 如果结果为null或者记录为空，返回null；否则返回第一个匹配的QcSearchVo对象
        return qcSearchVoPageInfo.getRecords();
    }

    public CommonPageResult.PageInfo<QcSearchVo> printQc(QcSearchDto dto) {
        dto.setPrint(true);
        return searchQc(dto);
    }

    public CommonResult<QcDefectHandlingVo> findQcDefectHandlingByQcOrderNos(QcDefectHandlingQueryDto qcDefectHandlingQueryDto) {
        // 从查询条件中获取质检单号
        String qcOrderNo = qcDefectHandlingQueryDto.getQcOrderNo();

        // 调用 DAO 层获取质检次品处理信息列表
        List<DefectHandlingPo> defectHandlingPos = defectHandlingDao.listByQcOrderNo(qcOrderNo);

        // 使用 Lambda 表达式筛选出“让步上架”方案的记录，并求和“不合格数量”
        int concessionShelfTotal = defectHandlingPos.stream()
                .filter(po -> Objects.equals(DefectHandlingProgramme.COMPROMISE, po.getDefectHandlingProgramme()))
                .mapToInt(DefectHandlingPo::getNotPassCnt)
                .sum();

        // 构建结果对象
        QcDefectHandlingVo vo = new QcDefectHandlingVo();
        vo.setQcOrderNo(qcOrderNo);
        vo.setConcessionShelfTotal(concessionShelfTotal);

        // 返回成功的 CommonResult
        return CommonResult.success(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void initQcOrigin() {
        // 当前页数
        int currentPage = 1;

        // 循环迭代直到达到最大迭代次数
        while (currentPage <= MAX_ITERATIONS) {
            IPage<QcOrderPo> procAndRepairByPage = qcOrderDao.getProcAndRepairQcNoByPage(currentPage, PAGE_SIZE);
            List<QcOrderPo> records = procAndRepairByPage.getRecords();
            if (CollectionUtils.isEmpty(records)) {
                return;
            }
            QcOriginUpdateBo qcOriginUpdateBo = new QcOriginUpdateBo();
            List<String> qcOrderNos = records.stream()
                    .map(QcOrderPo::getQcOrderNo)
                    .collect(Collectors.toList());
            qcOriginUpdateBo.setQcOrderNos(qcOrderNos);
            consistencyService.execAsyncTask(QcOriginUpdateHandler.class, qcOriginUpdateBo);
            // 增加当前页数
            currentPage++;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void urgentChangeQcMessage(ReceiveUrgentMqDto message) {
        final List<QcReceiveOrderPo> qcReceiveOrderPoList = qcReceiveOrderDao.getByReceiveNos(message.getReceiveOrderNoList());
        if (CollectionUtils.isEmpty(qcReceiveOrderPoList)) {
            return;
        }
        final List<String> qcOrderNoList = qcReceiveOrderPoList.stream()
                .map(QcReceiveOrderPo::getQcOrderNo)
                .distinct()
                .collect(Collectors.toList());

        final List<QcOrderPo> qcOrderPoList = qcOrderDao.getByQcOrderNos(qcOrderNoList);
        final List<String> updateQcOrderNoList = qcOrderPoList.stream()
                .filter(qcOrderPo -> !QcState.FINISHED.equals(qcOrderPo.getQcState()))
                .map(QcOrderPo::getQcOrderNo)
                .collect(Collectors.toList());

        final QcOrderPo qcOrderPo = new QcOrderPo();
        qcOrderPo.setIsUrgentOrder(message.getIsUrgent());

        qcOrderDao.updateByQcOrderNoList(qcOrderPo, updateQcOrderNoList);
    }


    /**
     * 质检列表检索条件
     *
     * @param dto:
     * @return QcSearchDto
     * @author ChenWenLong
     * @date 2024/5/9 16:51
     */
    public QcSearchDto searchQcWhere(QcSearchDto dto) {
        //产品名称
        if (StringUtils.isNotBlank(dto.getSkuEncode())) {
            List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(List.of(dto.getSkuEncode()));
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                dto.getSkuList().retainAll(skuListByEncode);
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                return null;
            }
        }
        //产品名称
        if (CollectionUtils.isNotEmpty(dto.getSkuEncodeList())) {
            List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(dto.getSkuEncodeList());
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                dto.getSkuList().retainAll(skuListByEncode);
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                return null;
            }
        }
        //供应商等级
        if (dto.getSupplierGrade() != null) {
            List<SupplierPo> supplierPoList = supplierDao.getListBySupplierGrade(dto.getSupplierGrade());
            if (CollectionUtils.isEmpty(supplierPoList)) {
                return null;
            }
            List<String> supplierCodeList = supplierPoList.stream()
                    .map(SupplierPo::getSupplierCode)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(dto.getSupplierCodeList())) {
                dto.setSupplierCodeList(supplierCodeList);
            } else {
                dto.getSupplierCodeList()
                        .retainAll(supplierCodeList);
            }
            if (CollectionUtils.isEmpty(dto.getSupplierCodeList())) {
                return null;
            }
        }
        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createPurchaseQc(PurchaseQcCreateRequestDto purchaseQcCreateRequestDto) {
        // 获取采购子订单号
        String purchaseChildOrderNo = purchaseQcCreateRequestDto.getPurchaseChildOrderNo();

        // 验证采购子订单信息是否存在，不存在则抛出异常
        PurchaseChildOrderPo purchaseChildOrderPo = ParamValidUtils.requireNotNull(
                purchaseChildOrderDao.getOneByChildOrderNo(purchaseChildOrderNo),
                "创建质检单失败！采购订单信息不存在，请选择有效的采购订单号"
        );

        // 定义不可创建质检单的采购订单状态
        List<PurchaseOrderStatus> unCreateStatus = List.of(
                PurchaseOrderStatus.WAIT_CONFIRM,
                PurchaseOrderStatus.WAIT_FOLLOWER_CONFIRM,
                PurchaseOrderStatus.WAIT_RECEIVE_ORDER,
                PurchaseOrderStatus.FINISH,
                PurchaseOrderStatus.DELETE
        );

        // 验证采购订单状态是否允许创建质检单，不允许则抛出异常
        ParamValidUtils.requireNotContains(
                purchaseChildOrderPo.getPurchaseOrderStatus(),
                unCreateStatus,
                StrUtil.format("创建质检单失败！采购订单处于{}状态，请确认后提交。",
                        unCreateStatus.stream()
                                .map(PurchaseOrderStatus::getRemark)
                                .collect(Collectors.joining("/")))
        );

        // 验证采购订单明细信息是否存在，不存在则抛出异常
        List<PurchaseChildOrderItemPo> purchaseChildOrderItemPos = ParamValidUtils.requireNotEmpty(
                purchaseChildOrderItemDao.getListByChildNo(purchaseChildOrderNo),
                "创建质检单失败！采购订单明细信息不存在，请选择有效的采购订单号"
        );

        // 获取采购订单类型
        PurchaseOrderType purchaseOrderType = purchaseChildOrderPo.getPurchaseOrderType();
        // 根据采购订单类型映射质检来源属性
        QcOriginProperty qcOriginProperty = qcOrderRefService.mapPurchaseOrderTypeToQcOriginProperty(purchaseOrderType);

        // 创建质检单创建器实例
        AbstractQcOrderCreator<PurchaseQcCreateRequestBo, QcOrderPo> qcOrderCreator
                = new PurchaseQcCreator(qcOrderDao, qcDetailDao, idGenerateService, qcOrderBaseService, plmRemoteService);

        // 构建采购质检单创建请求业务对象
        PurchaseQcCreateRequestBo purchaseQcCreateRequestBo
                = QcOrderBuilder.buildPurchaseQcCreateRequestBo(purchaseChildOrderPo, qcOriginProperty, purchaseChildOrderItemPos);

        // 使用质检单创建器创建质检单
        qcOrderCreator.createQcOrder(purchaseQcCreateRequestBo);
    }

    @RedisLock(prefix = ScmRedisConstant.BIZ_ORDER_CREATE_QC, key = "#dto.outBoundNo",
            waitTime = 1, leaseTime = -1, exceptionDesc = "出库质检单正在处理中，请稍后再试。")
    public void createOutBoundQc(BizNoQcCreateRequestDto dto) {
        final String bizOrderNo = dto.getOutBoundNo();
        final QcOrigin origin = dto.getOrigin();

        // 质检标识校验
        if (!Objects.equals(QcOrigin.OUTBOUND, origin)) {
            throw new ParamIllegalException("创建质检单失败!仅支持{}单据创建!", QcOrigin.OUTBOUND.getRemark());
        }

        // 出库单信息校验
        DeliveryQueryDto queryParam = new DeliveryQueryDto();
        queryParam.setDeliveryOrderNoList(List.of(bizOrderNo));
        DeliveryOrderVo outBoundOrder = ParamValidUtils.requireNotNull(getOutBoundOrder(queryParam),
                StrUtil.format("创建{}质检单失败，出库单{}信息不存在", QcOrigin.OUTBOUND.getRemark(), bizOrderNo));
        List<DeliveryOrderVo.DeliveryDetail> orderDetailList = ParamValidUtils.requireNotEmpty(outBoundOrder.getDeliveryDetailList(),
                StrUtil.format("创建{}质检单失败，出库单{}商品信息不存在", QcOrigin.OUTBOUND.getRemark(), bizOrderNo));

        String platCode = ParamValidUtils.requireNotBlank(outBoundOrder.getPlatCode(),
                StrUtil.format("创建{}质检单失败，出库单{}平台不存在", QcOrigin.OUTBOUND.getRemark(), bizOrderNo));
        dto.setPlatCode(platCode);

        //出库单明细聚合
        Map<String, Integer> sbsQuantityMap = orderDetailList.stream().collect(
                Collectors.groupingBy(detail -> genDeliveryDetailKey(detail.getSkuCode(), detail.getBatchCode(), detail.getSupplierCode()),
                        Collectors.summingInt(DeliveryOrderVo.DeliveryDetail::getAmount))
        );

        //提交出库单明细聚合
        List<BizNoQcCreateRequestDto.CreateBizQcDetailDto> orderDetailDtoList = dto.getCreateBizQcDetailDtoList();
        Map<String, Integer> sbsQuantityDtoMap = orderDetailDtoList.stream().collect(
                Collectors.groupingBy(detail -> genDeliveryDetailKey(detail.getSku(), detail.getBatchCode(), detail.getSupplierCode()),
                        Collectors.summingInt(BizNoQcCreateRequestDto.CreateBizQcDetailDto::getQuantity))
        );

        // 质检数量校验
        sbsQuantityMap.forEach((sbs, quantity) -> {
            DeliveryDetailKeyBo deliveryDetailKeyBo
                    = ParamValidUtils.requireNotNull(parseDeliveryDetailKey(sbs), "出库质检单数据异常！请联系相关业务人员处理");
            String sku = deliveryDetailKeyBo.getSku();
            String batchCode = deliveryDetailKeyBo.getBatchCode();
            String supplierCode = deliveryDetailKeyBo.getSupplierCode();

            String errorMsg = "";
            if (StrUtil.isBlank(supplierCode)) {
                errorMsg = StrUtil.format("创建质检单失败：sku:{} 批次码:{} 质检数量与出库单明细不匹配，请重新确认。", sku, batchCode);
            } else {
                errorMsg = StrUtil.format("创建质检单失败：sku:{} 批次码:{} 供应商编码:{} 质检数量与出库单明细不匹配，请重新确认。", sku, batchCode, supplierCode);
            }
            ParamValidUtils.requireEqual(quantity, sbsQuantityDtoMap.getOrDefault(sbs, 0), errorMsg);
        });

        // 创建出库质检单
        qcOrderBaseService.createObOrderQc(dto);
    }

    private DeliveryOrderVo getOutBoundOrder(DeliveryQueryDto queryParam) {
        if (null == queryParam) {
            return null;
        }

        List<DeliveryOrderVo> deliveryOrderList
                = wmsRemoteService.getListDetailByDeliveryOrderNo(queryParam);
        if (CollectionUtils.isEmpty(deliveryOrderList)) {
            return null;
        }

        return deliveryOrderList.stream()
                .findFirst().orElse(null);
    }

    /**
     * 根据出库单查询需要生成质检单信息
     *
     * @param dto 出库质检请求信息DTO
     * @return 质检单信息VO
     */
    public List<BizOrderCreateQcVo> outBoundQcInfo(BizOrderCreateQcReqDto dto) {

        DeliveryQueryDto queryParam = new DeliveryQueryDto();
        if (StringUtils.isNotBlank(dto.getOutBoundNo())) {
            queryParam.setDeliveryOrderNoList(Collections.singletonList(dto.getOutBoundNo()));
        }
        if (StringUtils.isNotBlank(dto.getExpressOrderNo())) {
            queryParam.setExpressOrderNoList(Collections.singletonList(dto.getExpressOrderNo()));
        }

        // 获取质检来源与仓库映射关系
        Map<QcOriginProperty, List<String>> qcOriginWarehouseMapping = qcConfig.getQcOriginWarehouseMapping();
        if (CollectionUtils.isEmpty(qcOriginWarehouseMapping)) {
            throw new BizException("查不到质检类型与仓库编码关系配置！请配置。");
        }
        List<DeliveryOrderVo> deliveryOrderVoList = wmsRemoteService.getListDetailByDeliveryOrderNo(queryParam);

        return Optional.ofNullable(deliveryOrderVoList).orElse(new ArrayList<>())
                .stream()
                .map(deliveryOrderVo -> {
                    // 创建返回对象
                    BizOrderCreateQcVo vo = new BizOrderCreateQcVo();
                    // 设置调拨单号
                    vo.setOutBoundNo(deliveryOrderVo.getDeliveryOrderNo());
                    vo.setExpressOrderNo(deliveryOrderVo.getExpressOrderNo());
                    vo.setPlatCode(deliveryOrderVo.getPlatCode());

                    // 设置仓库编码 & 根据仓库编码设置质检来源属性
                    String targetWarehouseCode = deliveryOrderVo.getTargetWarehouseCode();
                    String warehouseCode = deliveryOrderVo.getWarehouseCode();
                    QcOriginProperty qcOriginProperty
                            = qcOrderBaseService.getQcOriginPropertyByWarehouseCodes(warehouseCode, targetWarehouseCode);
                    vo.setQcOriginProperty(qcOriginProperty);
                    vo.setWarehouseCode(warehouseCode);

                    // 设置订单明细列表
                    List<DeliveryOrderVo.DeliveryDetail> deliveryDetailList
                            = Optional.ofNullable(deliveryOrderVo.getDeliveryDetailList()).orElse(Collections.emptyList());

                    // 根据skuCode、batchCode、supplierCode分组, 求和数量
                    Map<String, Integer> sbsAmountMap = deliveryDetailList.stream().collect(
                            Collectors.groupingBy(detail -> genDeliveryDetailKey(detail.getSkuCode(), detail.getBatchCode(), detail.getSupplierCode()),
                                    Collectors.summingInt(DeliveryOrderVo.DeliveryDetail::getAmount))
                    );

                    List<BizOrderCreateQcVo.BizOrderDetailVo> deliveryDetailVoList = Lists.newArrayList();
                    sbsAmountMap.forEach((key, amount) -> {
                        DeliveryDetailKeyBo deliveryDetailKeyBo
                                = ParamValidUtils.requireNotNull(parseDeliveryDetailKey(key), "出库质检单数据异常！请联系相关业务人员处理");
                        String sku = deliveryDetailKeyBo.getSku();
                        String batchCode = deliveryDetailKeyBo.getBatchCode();
                        String supplierCode = deliveryDetailKeyBo.getSupplierCode();

                        DeliveryOrderVo.DeliveryDetail matchSbsDeliveryDetail = deliveryDetailList.stream().filter(detail ->
                                        Objects.equals(sku, detail.getSkuCode()) &&
                                                Objects.equals(batchCode, detail.getBatchCode()) &&
                                                Objects.equals(supplierCode, StrUtil.isBlank(detail.getSupplierCode()) ? Strings.EMPTY : detail.getSupplierCode()))
                                .findFirst().orElse(null);
                        if (Objects.nonNull(matchSbsDeliveryDetail)) {
                            BizOrderCreateQcVo.BizOrderDetailVo detailVo = new BizOrderCreateQcVo.BizOrderDetailVo();
                            detailVo.setSku(sku);
                            detailVo.setBatchCode(batchCode);
                            detailVo.setSupplierCode(supplierCode);
                            detailVo.setQuantity(amount);
                            deliveryDetailVoList.add(detailVo);
                        }
                    });
                    vo.setDetailList(deliveryDetailVoList);

                    return vo;
                }).sorted((a1, a2) -> a2.getOutBoundNo().compareToIgnoreCase(a1.getOutBoundNo()))
                .collect(Collectors.toList());
    }

    public List<QcSpecInfoVo> getQcSpecBookList(GetQcSpecBookDto dto) {
        String supplierCode = dto.getSupplierCode();
        List<String> skuList = dto.getSkuList();

        return skuList.stream().map(sku -> {
            QcSpecInfoVo qcSpecInfoVo = new QcSpecInfoVo();
            qcSpecInfoVo.setSku(sku);

            List<SpecBookRelateBo> specBookRelateBoList = skuProdBaseService.getSpecBookRelateBoList(sku, Set.of(supplierCode));
            if (CollectionUtils.isNotEmpty(specBookRelateBoList)) {
                SpecBookRelateBo specBookRelateBo = specBookRelateBoList.stream().findFirst().orElse(null);
                if (Objects.nonNull(specBookRelateBo)) {
                    qcSpecInfoVo.setSkuEncode(specBookRelateBo.getSkuEncode());
                    qcSpecInfoVo.setSpu(specBookRelateBo.getSpu());
                    qcSpecInfoVo.setFileCodeList(specBookRelateBo.getFileCodeList());
                    qcSpecInfoVo.setLoginUsername(specBookRelateBo.getLoginUsername());
                    qcSpecInfoVo.setSpecPlatformVoList(specBookRelateBo.getSpecPlatformVoList());
                    qcSpecInfoVo.setSupplierCode(specBookRelateBo.getSupplierCode());
                }
            }

            //商品属性值列表
            List<SkuAttributeInfoVo> skuAttributeInfoVoList = skuProdBaseService.getSkuAttrInfoVoList(sku);
            qcSpecInfoVo.setSkuAttributeInfoVoList(skuAttributeInfoVoList);

            //供应商商品生产信息列表
            List<SupSkuProdInfoVo> supSkuProdInfoVoList = skuProdBaseService.getSupSkuProdInfoVoList(sku, Collections.singletonList(supplierCode));
            if (CollectionUtils.isNotEmpty(supSkuProdInfoVoList)) {
                SupSkuProdInfoVo matchSupSkuProdInfoVo = supSkuProdInfoVoList.stream()
                        .filter(supSkuProdInfoVo -> Objects.equals(supplierCode, supSkuProdInfoVo.getSupplierCode()))
                        .findFirst().orElse(null);
                if (Objects.nonNull(matchSupSkuProdInfoVo)) {
                    qcSpecInfoVo.setSupSkuProdInfoVoList(Collections.singletonList(matchSupSkuProdInfoVo));
                    matchSupSkuProdInfoVo.setSpecPlatformVoList(qcSpecInfoVo.getSpecPlatformVoList());
                }
            }
            return qcSpecInfoVo;
        }).collect(Collectors.toList());
    }

    public List<QcSampleVo> getQcSample(GetQcSampleDto dto) {
        String qcOrderNo = dto.getQcOrderNo();

        QcOrderPo qcOrderPo = qcOrderDao.getByQcOrderNo(qcOrderNo);
        if (Objects.isNull(qcOrderPo)) {
            log.info("获取样品信息返回为空！质检单信息为空=>{}", qcOrderNo);
            return Collections.emptyList();
        }
        List<QcDetailPo> qcDetailPoList = qcDetailDao.getListByQcOrderNo(qcOrderNo);
        if (CollectionUtils.isEmpty(qcDetailPoList)) {
            log.info("获取样品信息返回为空！质检明细信息为空=>{}", qcOrderNo);
            return Collections.emptyList();
        }

        String supplierCode = "";
        QcReceiveOrderPo qcReceiveOrderPo = qcReceiveOrderDao.getOneByQcOrderNo(qcOrderNo);
        if (Objects.nonNull(qcReceiveOrderPo)) {
            supplierCode = qcReceiveOrderPo.getSupplierCode();
        } else {
            supplierCode = qcOrderPo.getSupplierCode();
        }
        if (StringUtils.isBlank(supplierCode)) {
            log.info("获取样品信息返回为空！找不到质检单关联供应商编码=>{}", qcOrderNo);
            return Collections.emptyList();
        }

        Set<String> skuSet = qcDetailPoList.stream().map(QcDetailPo::getSkuCode).collect(Collectors.toSet());
        String finalSupplierCode = supplierCode;
        return skuSet.stream().map(sku -> {
            QcSampleVo qcSampleVo = new QcSampleVo();
            qcSampleVo.setSku(sku);
            qcSampleVo.setSupplierCode(finalSupplierCode);
            List<String> samplePicList = skuProdBaseService.getSamplePicList(sku, finalSupplierCode);
            qcSampleVo.setSamplePicList(samplePicList);
            return qcSampleVo;
        }).collect(Collectors.toList());
    }

    private String genDeliveryDetailKey(String sku, String batchCode, String supplierCode) {
        return String.format("%s:%s:%s",
                StrUtil.isBlank(sku) ? DEFAULT_EMPTY_KEY : sku,
                StrUtil.isBlank(batchCode) ? DEFAULT_EMPTY_KEY : batchCode,
                StrUtil.isBlank(supplierCode) ? DEFAULT_EMPTY_KEY : supplierCode);
    }

    public static DeliveryDetailKeyBo parseDeliveryDetailKey(String key) {
        if (StrUtil.isBlank(key) || !key.contains(":")) {
            throw new ParamIllegalException("分组键码格式不正确。");
        }
        String[] parts = key.split(":");
        if (parts.length != 3) {
            log.error("出库质检单数据分组异常！分组key未包含 sku、批次码、供应商。");
            throw new ParamIllegalException("出库质检单数据异常！请联系相关业务人员处理");
        }
        return new DeliveryDetailKeyBo(
                Objects.equals(parts[0], DEFAULT_EMPTY_KEY) ? "" : parts[0],
                Objects.equals(parts[1], DEFAULT_EMPTY_KEY) ? "" : parts[1],
                Objects.equals(parts[2], DEFAULT_EMPTY_KEY) ? "" : parts[2]
        );
    }

    public List<QcDetailSkuVo> listBySkuAndQcState(QcDto dto) {
        return qcOrderDao.listBySkuAndQcState(dto);
    }
}



