package com.hete.supply.scm.server.scm.sample.service.base;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.plm.api.goods.entity.vo.PlmSkuVo;
import com.hete.supply.scm.api.scm.entity.dto.*;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.entity.vo.SampleInfoVo;
import com.hete.supply.scm.api.scm.entity.vo.SampleNoAndStatusVo;
import com.hete.supply.scm.api.scm.entity.vo.SampleProductVo;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.entity.bo.RawDeliverBo;
import com.hete.supply.scm.server.scm.entity.dto.SkuListDto;
import com.hete.supply.scm.server.scm.entity.vo.SkuDetailVo;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.entity.bo.WmsDeliverBo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.purchase.service.ref.PurchaseRawRefService;
import com.hete.supply.scm.server.scm.sample.converter.SampleConverter;
import com.hete.supply.scm.server.scm.sample.dao.*;
import com.hete.supply.scm.server.scm.sample.entity.dto.*;
import com.hete.supply.scm.server.scm.sample.entity.po.*;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleChildOrderChangeSearchVo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleChildOrderNoListVo;
import com.hete.supply.scm.server.scm.sample.handler.SampleStatusHandler;
import com.hete.supply.scm.server.scm.sample.service.biz.SampleDingService;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.scm.service.base.ProduceDataBaseService;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.supplier.sample.dao.SampleReturnOrderDao;
import com.hete.supply.scm.server.supplier.sample.dao.SampleReturnOrderItemDao;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleReceiveDto;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderPo;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/8 10:54
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class SampleBaseService {
    private final SampleChildOrderDao sampleChildOrderDao;
    private final ScmImageBaseService scmImageBaseService;
    private final SampleParentOrderDao sampleParentOrderDao;
    private final SampleChildOrderInfoDao sampleChildOrderInfoDao;
    private final SampleReturnOrderDao sampleReturnOrderDao;
    private final SampleReturnOrderItemDao sampleReturnOrderItemDao;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final SampleChildOrderResultDao sampleChildOrderResultDao;
    private final SampleChildOrderRawDao sampleChildOrderRawDao;
    private final SampleChildOrderProcessDao sampleChildOrderProcessDao;
    private final SampleChildOrderProcessDescDao sampleChildOrderProcessDescDao;
    private final IdGenerateService idGenerateService;
    private final PlmRemoteService plmRemoteService;
    private final SampleChildOrderChangeDao sampleChildOrderChangeDao;
    private final SampleParentOrderChangeDao sampleParentOrderChangeDao;
    private final LogBaseService logBaseService;
    private final ConsistencySendMqService consistencySendMqService;
    private final SampleDingService sampleDingService;
    private final PurchaseRawRefService purchaseRawRefService;
    private final ProduceDataBaseService produceDataBaseService;

    private final static int INIT_CHILD_ORDER_NO_INDEX = 0;

    /**
     * 根据母单号查询最新子单号
     *
     * @param sampleParentOrderNo
     * @return
     */
    public int getLatestChildOrderNo(String sampleParentOrderNo) {
        SampleChildOrderPo sampleChildOrderPo = sampleChildOrderDao.getLatestChildOrderPo(sampleParentOrderNo);

        if (null == sampleChildOrderPo) {
            return INIT_CHILD_ORDER_NO_INDEX;
        }

        String sampleChildOrderNo = sampleChildOrderPo.getSampleChildOrderNo();
        String childOrderNoIndex = sampleChildOrderNo.split("-")[1];
        return Integer.parseInt(childOrderNoIndex);
    }

    public SampleChildOrderNoListVo searchSampleNo(SampleChildOrderNoDto dto) {

        List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getListByChildNoAndStatus(dto.getSampleChildOrderNo(),
                SampleOrderStatus.SELECTED);

        final List<String> sampleChildOrderNoList = sampleChildOrderPoList.stream()
                .map(SampleChildOrderPo::getSampleChildOrderNo)
                .collect(Collectors.toList());

        final SampleChildOrderNoListVo sampleChildOrderNoListVo = new SampleChildOrderNoListVo();
        sampleChildOrderNoListVo.setSampleChildOrderNoList(sampleChildOrderNoList);

        return sampleChildOrderNoListVo;
    }

    /**
     * 通过模糊样品采购子单号查询
     *
     * @author ChenWenLong
     * @date 2022/11/10 11:12
     */
    public List<SampleChildOrderPo> getListLikeBySampleChildOrderNo(String sampleChildOrderNo, String supplierCode) {
        return sampleChildOrderDao.getListLikeBySampleChildOrderNo(sampleChildOrderNo, supplierCode, null);
    }

    /**
     * 通过模糊样品采购子单号查询增加剔除状态
     *
     * @author ChenWenLong
     * @date 2022/11/10 11:12
     */
    public List<SampleChildOrderPo> getListLikeBySampleChildOrderNo(String sampleChildOrderNo, String supplierCode, List<SampleOrderStatus> sampleOrderStatusNotList) {
        return sampleChildOrderDao.getListLikeBySampleChildOrderNo(sampleChildOrderNo, supplierCode, sampleOrderStatusNotList);
    }


    /**
     * 获取子单最前状态，更新母单
     *
     * @param sampleParentOrderNo
     * @param newOrderStatusList
     * @return
     */
    public SampleOrderStatus getEarliestStatus(String sampleParentOrderNo, List<SampleOrderStatus> newOrderStatusList) {
        final List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getChildOrderByParentNo(sampleParentOrderNo);
        final List<SampleOrderStatus> statusList = sampleChildOrderPoList.stream()
                .map(SampleChildOrderPo::getSampleOrderStatus)
                .collect(Collectors.toList());
        statusList.addAll(newOrderStatusList);

        return SampleOrderStatus.getEarliestStatus(statusList);
    }


    public Map<String, SampleOrderStatus> getEarliestStatusMapByParentNoList(List<String> sampleParentOrderNoList,
                                                                             List<SampleOrderStatus> sampleOrderStatusList) {
        final Map<String, List<SampleChildOrderPo>> parentNoChildOrderMap = sampleChildOrderDao.getMapByParentNoList(sampleParentOrderNoList);

        Map<String, SampleOrderStatus> earliestStatusMap = new HashMap<>();
        parentNoChildOrderMap.forEach((key, sampleChildOrderPoList) -> {
            List<SampleOrderStatus> statusList = sampleChildOrderPoList.stream()
                    .map(SampleChildOrderPo::getSampleOrderStatus)
                    .collect(Collectors.toList());
            statusList.addAll(sampleOrderStatusList);
            earliestStatusMap.put(key, SampleOrderStatus.getEarliestStatus(statusList));
        });

        return earliestStatusMap;
    }

    /**
     * 通过供应商和时间查询样品单列表
     *
     * @author ChenWenLong
     * @date 2022/11/22 17:50
     */
    public List<SampleChildOrderChangeSearchVo> sampleChildOrderChangeSearch(String supplierCode, LocalDateTime sampleTime, LocalDateTime sampleTimeStart, LocalDateTime sampleTimeEnd, SampleOrderStatus sampleOrderStatus) {
        return sampleChildOrderDao.sampleChildOrderChangeSearch(supplierCode, sampleTime, sampleTimeStart, sampleTimeEnd, sampleOrderStatus);
    }


    /**
     * 根据生产属性值查询sku列表
     *
     * @param properties
     * @return
     */
    public List<String> getSkuListByProperties(List<String> properties) {
        final List<SampleChildOrderInfoPo> sampleChildOrderInfoPoList = sampleChildOrderInfoDao.getBySampleInfoValueList(properties);
        final List<String> sampleChildOrderNoList = sampleChildOrderInfoPoList.stream()
                .map(SampleChildOrderInfoPo::getSampleChildOrderNo)
                .collect(Collectors.toList());

        final List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getChildOrderListByChildNoList(sampleChildOrderNoList);
        return sampleChildOrderPoList.stream()
                .map(SampleChildOrderPo::getSku)
                .distinct()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
    }

    /**
     * 根据子单号查询生产信息
     *
     * @param sampleChildOrderNo
     * @return
     */
    public SampleProductVo getSampleProductVoByChildNo(String sampleChildOrderNo) {
        final SampleProductVo sampleProductVo = new SampleProductVo();
        final SampleChildOrderPo sampleChildOrderPo = sampleChildOrderDao.getOneByChildOrderNo(sampleChildOrderNo);
        if (null == sampleChildOrderPo) {
            return sampleProductVo;
        }
        final SampleParentOrderPo sampleParentOrderPo = sampleParentOrderDao.getByParentNo(sampleChildOrderPo.getSampleParentOrderNo());
        if (null == sampleParentOrderPo) {
            return sampleProductVo;
        }
        sampleProductVo.setSampleChildOrderNo(sampleChildOrderNo);
        final List<SampleChildOrderInfoPo> sampleChildOrderInfoPoList = sampleChildOrderInfoDao.getInfoListByChildNo(sampleChildOrderPo.getSampleChildOrderNo());
        sampleProductVo.setSampleChildOrderInfoList(SampleConverter.childInfoPoListToVoList(sampleChildOrderInfoPoList));

        final List<String> fileCodeList = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.SAMPLE_PARENT_ORDER,
                Collections.singletonList(sampleParentOrderPo.getSampleParentOrderId()));
        sampleProductVo.setFileCodeList(fileCodeList);

        return sampleProductVo;
    }

    public List<String> getSkuParentOrderNoList(SampleSearchDto dto) {
        List<String> skuParentOrderNoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dto.getSkuList())) {
            List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getListBySku(dto.getSkuList());
            if (CollectionUtils.isEmpty(sampleChildOrderPoList)) {
                return skuParentOrderNoList;
            }

            skuParentOrderNoList.addAll(sampleChildOrderPoList.stream()
                    .map(SampleChildOrderPo::getSampleParentOrderNo)
                    .distinct()
                    .collect(Collectors.toList()));
        }

        return skuParentOrderNoList;
    }

    /**
     * 通过模糊样品退货单号查询
     *
     * @author ChenWenLong
     * @date 2022/11/10 11:12
     */
    public List<SampleReturnOrderPo> getBySampleReturnOrderNo(String sampleReturnOrderNo, String supplierCode, List<ReceiptOrderStatus> receiptOrderStatusNotList) {
        return sampleReturnOrderDao.getBySampleReturnOrderNo(sampleReturnOrderNo, supplierCode, receiptOrderStatusNotList);
    }

    /**
     * 通过模糊样品退货单号查询详情
     *
     * @author ChenWenLong
     * @date 2022/11/10 11:12
     */
    public List<SampleReturnOrderItemPo> getSampleReturnOrderItemByNo(String sampleReturnOrderNo) {
        return sampleReturnOrderItemDao.getListByReturnOrderNo(sampleReturnOrderNo);
    }

    /**
     * 根据skuList查询生产信息
     *
     * @param dto
     * @return
     */
    public List<SampleInfoVo> getSampleInfoBySkuList(@NotNull @Valid SampleSkuListDto dto) {
        final Set<String> sampleChildOrderNoSet = sampleChildOrderDao.getDistinctNoListBySkuList(dto.getSkuList());
        if (CollectionUtils.isEmpty(sampleChildOrderNoSet)) {
            return Collections.emptyList();
        }
        final List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getChildOrderListByChildNoList(new ArrayList<>(sampleChildOrderNoSet));

        List<String> sampleParentNoList = sampleChildOrderPoList.stream()
                .map(SampleChildOrderPo::getSampleParentOrderNo)
                .distinct()
                .collect(Collectors.toList());

        final List<SampleParentOrderPo> sampleParentOrderPoList = sampleParentOrderDao.getByParentNoList(sampleParentNoList);
        final List<Long> idList = sampleParentOrderPoList.stream()
                .map(SampleParentOrderPo::getSampleParentOrderId)
                .distinct()
                .collect(Collectors.toList());

        final Map<String, List<String>> skuFileCodeListMap = produceDataBaseService.getBomImageBySkuList(dto.getSkuList());

        List<SampleChildOrderInfoPo> sampleChildOrderInfoPoList = sampleChildOrderInfoDao.getListByChildNoList(sampleChildOrderNoSet);
        if (CollectionUtils.isEmpty(sampleChildOrderInfoPoList)) {
            return Collections.emptyList();
        }

        return SampleConverter.childInfoFileCodeToVoList(sampleParentOrderPoList, sampleChildOrderInfoPoList,
                sampleChildOrderPoList, skuFileCodeListMap);
    }

    /**
     * key-》采购子单号
     * value-》生产信息
     *
     * @param purchaseChildOrderNoList
     * @return
     */
    public Map<String, List<SampleChildOrderInfoPo>> getSampleInfoByPurchaseChildNoList(List<String> purchaseChildOrderNoList) {
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList);
        final List<String> sampleChildOrderNoList = purchaseChildOrderPoList.stream()
                .map(PurchaseChildOrderPo::getSampleChildOrderNo)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());

        Map<String, List<SampleChildOrderInfoPo>> sampleChildInfoMap = sampleChildOrderInfoDao.getMapByChildNoList(sampleChildOrderNoList);

        if (CollectionUtils.isEmpty(sampleChildInfoMap)) {
            return sampleChildInfoMap;
        }

        return purchaseChildOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo,
                        po -> sampleChildInfoMap.get(po.getSampleChildOrderNo())));
    }

    public List<SampleNoAndStatusVo> getSampleNoAndStatusBySpu(SpuDto dto) {
        final List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getSampleNoAndStatusBySpu(dto.getSpuCode());
        final List<SampleParentOrderPo> sampleParentOrderPoList = sampleParentOrderDao.getSampleNoAndStatusBySpu(dto.getSpuCode());
        return SampleConverter.poToStatusVo(sampleChildOrderPoList, sampleParentOrderPoList);
    }

    /**
     * 通过编号批量更新状态
     *
     * @author ChenWenLong
     * @date 2023/1/14 15:38
     */
    public void updateBatchSampleChildOrderNo(List<String> sampleChildOrderNos, SampleOrderStatus sampleOrderStatus) {
        sampleChildOrderDao.updateBatchSampleChildOrderNo(sampleChildOrderNos, sampleOrderStatus);
    }

    /**
     * 通过供应商代码查询样品需求子单列表
     *
     * @author ChenWenLong
     * @date 2023/1/30 14:33
     */
    public List<SampleChildOrderPo> getSampleChildOrderBySupplierCode(String supplierCode) {
        return sampleChildOrderDao.getBySupplierCode(supplierCode);
    }

    /**
     * 通过spu批量查询
     *
     * @author ChenWenLong
     * @date 2023/2/1 11:00
     */
    public List<SampleChildOrderPo> getListBySpuList(List<String> spuList) {
        return sampleChildOrderDao.getListBySpuList(spuList);
    }

    /**
     * 通过编号批量查询
     *
     * @author ChenWenLong
     * @date 2023/3/7 14:39
     */
    public List<SampleChildOrderPo> getBatchSampleChildOrderNo(List<String> sampleChildOrderNoList) {
        return sampleChildOrderDao.getBatchSampleChildOrderNo(sampleChildOrderNoList);
    }

    /**
     * 通过样品采购子单号批量选样成功的选样数量
     *
     * @author ChenWenLong
     * @date 2023/3/7 14:39
     */
    public Map<String, List<SampleChildOrderResultPo>> getSampleCntBatchSampleChildOrderNo(List<String> sampleChildOrderNoList, SampleResult sampleResult) {
        return sampleChildOrderResultDao.getMapByChildOrderNoList(sampleChildOrderNoList, sampleResult);
    }

    public List<SkuDetailVo> getSkuDetailBySku(SkuListDto dto) {
        final List<PlmSkuVo> plmSkuVoList = plmRemoteService.getSkuEncodeBySku(dto.getSkuList());

        return Optional.ofNullable(plmSkuVoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(plmSkuVo -> SkuDetailVo.builder()
                        .sku(plmSkuVo.getSkuCode())
                        .skuEncode(plmSkuVo.getSkuEncode())
                        .spu(plmSkuVo.getSpuCode())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 打样成功、失败的数据处理
     *
     * @author ChenWenLong
     * @date 2023/5/9 13:43
     */
    @Transactional(rollbackFor = Exception.class)
    public void createSuccessSample(SampleSelectionDto dto,
                                    SampleChildOrderPo sampleChildOrderPo,
                                    SampleParentOrderPo sampleParentOrderPo,
                                    SampleChildOrderChangePo sampleChildOrderChangePo,
                                    SampleParentOrderChangePo sampleParentOrderChangePo,
                                    SampleOrderStatus targetStatus,
                                    Map<String, String> skuBatchCodeMap) {

        //失败退样
        if (dto.getReturnNum() > 0) {
            //创建样品子单结果
            final SampleChildOrderResultPo sampleChildOrderResultPo = new SampleChildOrderResultPo();
            sampleChildOrderResultPo.setSampleParentOrderNo(sampleChildOrderPo.getSampleParentOrderNo());
            sampleChildOrderResultPo.setSampleChildOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
            sampleChildOrderResultPo.setSampleResult(SampleResult.SAMPLE_RETURN);
            sampleChildOrderResultPo.setSampleResultStatus(SampleResultStatus.WAIT_HANDLE);
            sampleChildOrderResultPo.setSampleCnt(dto.getReturnNum());
            String sampleResultNo = idGenerateService.getConfuseCode(sampleChildOrderPo.getSampleChildOrderNo(), ConfuseLength.L_4);
            sampleChildOrderResultPo.setSampleResultNo(sampleResultNo);
            sampleChildOrderResultPo.setRemark(dto.getReturnSampleReason());
            sampleChildOrderResultDao.insert(sampleChildOrderResultPo);
        }

        //失败闪售
        if (dto.getSaleNum() > 0) {
            final SampleChildOrderResultPo sampleChildOrderResultPo = new SampleChildOrderResultPo();
            sampleChildOrderResultPo.setSampleParentOrderNo(sampleChildOrderPo.getSampleParentOrderNo());
            sampleChildOrderResultPo.setSampleChildOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
            sampleChildOrderResultPo.setSampleResult(SampleResult.FAIL_SALE);
            sampleChildOrderResultPo.setSampleResultStatus(SampleResultStatus.WAIT_HANDLE);
            sampleChildOrderResultPo.setSampleCnt(dto.getSaleNum());
            String sampleResultNo = idGenerateService.getConfuseCode(sampleChildOrderPo.getSampleChildOrderNo(), ConfuseLength.L_4);
            sampleChildOrderResultPo.setSampleResultNo(sampleResultNo);
            sampleChildOrderResultPo.setRemark(dto.getSaleSampleReason());
            sampleChildOrderResultDao.insert(sampleChildOrderResultPo);
        }

        //打样成功
        if (dto.getSuccessNum() > 0) {
            sampleChildOrderPo.setSku(dto.getSku());
            sampleChildOrderPo.setSkuBatchCode(skuBatchCodeMap.get(dto.getSku()));
            //创建样品子单结果
            final SampleChildOrderResultPo sampleChildOrderResultPo = new SampleChildOrderResultPo();
            sampleChildOrderResultPo.setSampleParentOrderNo(sampleChildOrderPo.getSampleParentOrderNo());
            sampleChildOrderResultPo.setSampleChildOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
            sampleChildOrderResultPo.setSampleResult(SampleResult.SAMPLE_SUCCESS);
            sampleChildOrderResultPo.setSampleResultStatus(SampleResultStatus.WAIT_HANDLE);
            sampleChildOrderResultPo.setSampleCnt(dto.getSuccessNum());
            String sampleResultNo = idGenerateService.getConfuseCode(sampleChildOrderPo.getSampleChildOrderNo(), ConfuseLength.L_4);
            sampleChildOrderResultPo.setSampleResultNo(sampleResultNo);
            sampleChildOrderResultPo.setRemark(dto.getRemark());
            sampleChildOrderResultDao.insert(sampleChildOrderResultPo);

            // 更新图片
            scmImageBaseService.insertBatchImage(dto.getFileCodeList(), ImageBizType.SAMPLE_CTRL_SUCCESS,
                    sampleChildOrderPo.getSampleChildOrderId());

            // 更新生产信息
            final List<SampleChildOrderInfoPo> sampleChildOrderInfoPoList = SampleConverter.childOrderInfoDtoListToPo(dto.getSampleChildOrderInfoList(),
                    sampleChildOrderPo.getSampleChildOrderNo());
            if (CollectionUtils.isNotEmpty(sampleChildOrderInfoPoList)) {
                sampleChildOrderInfoDao.insertOrUpdateBatch(sampleChildOrderInfoPoList);
            }

            // limited类型更新bom信息和工序信息
            if (SampleDevType.confirmSubmitAudited(sampleChildOrderPo.getSampleDevType())) {
                final List<SampleChildOrderProcessPo> sampleChildOrderProcessPoList = SampleConverter.processDtoToPo(sampleChildOrderPo, dto.getSampleProcessList());
                final List<SampleChildOrderRawPo> sampleChildOrderRawPoList = SampleConverter.rawDtoToPo(sampleChildOrderPo, dto.getSampleRawList());

                sampleChildOrderProcessDao.removeByNo(sampleChildOrderPo.getSampleChildOrderNo());
                sampleChildOrderRawDao.removeByNoAndType(sampleChildOrderPo.getSampleChildOrderNo(), SampleRawBizType.FORMULA);

                sampleChildOrderProcessDao.insertBatch(sampleChildOrderProcessPoList);
                sampleChildOrderRawDao.insertBatch(sampleChildOrderRawPoList);
            }

            sampleChildOrderPo.setSettlePrice(sampleChildOrderPo.getProofingPrice().multiply(new BigDecimal(dto.getSuccessNum())));
            sampleChildOrderPo.setCostPrice(dto.getCostPrice());

            //打上生效标签
            sampleChildOrderPo.setSampleProduceLabel(SampleProduceLabel.EFFECTIVE);

        }

        // 更新子单状态
        sampleChildOrderPo.setSampleOrderStatus(targetStatus);
        sampleChildOrderDao.updateByIdVersion(sampleChildOrderPo);

        // 更新母单状态
        SampleOrderStatus earliestStatus = this.getEarliestStatus(sampleChildOrderPo.getSampleParentOrderNo(),
                Collections.singletonList(targetStatus));
        if (!sampleParentOrderPo.getSampleOrderStatus().equals(earliestStatus)) {
            sampleParentOrderPo.setSampleOrderStatus(earliestStatus);
            sampleParentOrderDao.updateByIdVersion(sampleParentOrderPo);
        }
        // 更新母单change
        sampleParentOrderChangePo.setSampleTime(new DateTime().toLocalDateTime());
        sampleParentOrderChangeDao.updateByIdVersion(sampleParentOrderChangePo);

        // 更新change
        sampleChildOrderChangePo.setSampleTime(new DateTime().toLocalDateTime());
        sampleChildOrderChangePo.setSampleUser(GlobalContext.getUserKey());
        sampleChildOrderChangePo.setSampleUsername(GlobalContext.getUsername());
        sampleChildOrderChangeDao.updateByIdVersion(sampleChildOrderChangePo);


        //写日志
        logBaseService.simpleLog(LogBizModule.SAMPLE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                sampleChildOrderPo.getSampleChildOrderNo(), targetStatus.getRemark(), Collections.emptyList());

        //plm推送状态
        final SampleStatusDto sampleStatusDto = new SampleStatusDto();
        sampleStatusDto.setSpuCode(sampleParentOrderPo.getSpu());
        sampleStatusDto.setSampleOrderStatus(targetStatus);
        sampleStatusDto.setKey(sampleChildOrderPo.getSampleChildOrderNo());
        sampleStatusDto.setUserKey(GlobalContext.getUserKey());
        sampleStatusDto.setUsername(GlobalContext.getUsername());
        consistencySendMqService.execSendMq(SampleStatusHandler.class, sampleStatusDto);

        // 推送钉钉消息
        sampleDingService.sendChildDingTalkMsg(sampleChildOrderPo, dto.getSku());

    }

    /**
     * 下发打版事务提交
     *
     * @author ChenWenLong
     * @date 2023/5/9 14:02
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateTypeset(SampleChildTypesetDto dto,
                              SampleChildOrderPo sampleChildOrderPo,
                              SampleOrderStatus targetStatus,
                              SampleChildOrderChangePo sampleChildOrderChangePo,
                              SampleParentOrderPo sampleParentOrderPo) {

        sampleChildOrderPo.setSampleOrderStatus(targetStatus);
        // 更新change
        sampleChildOrderChangePo.setTypesetTime(new DateTime().toLocalDateTime());
        sampleChildOrderChangeDao.updateByIdVersion(sampleChildOrderChangePo);

        // 更新母单状态
        SampleOrderStatus earliestStatus = this.getEarliestStatus(sampleChildOrderPo.getSampleParentOrderNo(),
                Collections.singletonList(targetStatus));
        if (!sampleParentOrderPo.getSampleOrderStatus().equals(earliestStatus)) {
            sampleParentOrderPo.setSampleOrderStatus(earliestStatus);
        }

        //创建原料信息
        if (SampleDevType.confirmSubmitAudited(sampleChildOrderPo.getSampleDevType())
                && CollectionUtils.isNotEmpty(dto.getSampleRawList())) {
            sampleChildOrderPo.setRawWarehouseCode(dto.getRawWarehouseCode());
            sampleChildOrderPo.setRawWarehouseName(dto.getRawWarehouseName());
            //先清除原来旧数据
            List<SampleRawBizType> sampleRawBizTypeList = new ArrayList<>();
            sampleRawBizTypeList.add(SampleRawBizType.FORMULA);
            sampleRawBizTypeList.add(SampleRawBizType.DEMAND);
            sampleChildOrderRawDao.removeBySampleChildOrderNo(sampleChildOrderPo.getSampleChildOrderNo(), sampleRawBizTypeList);
            final List<SampleChildOrderRawPo> sampleChildOrderRawPoList = SampleConverter.sampleChildTypesetToChildRaw(dto.getSampleRawList(),
                    sampleChildOrderPo, SampleRawBizType.FORMULA);
            sampleChildOrderRawPoList.addAll(SampleConverter.sampleChildTypesetToMultiplyChildRaw(dto.getSampleRawList(),
                    sampleChildOrderPo, SampleRawBizType.DEMAND));
            sampleChildOrderRawDao.insertBatch(sampleChildOrderRawPoList);
        }
        sampleChildOrderDao.updateByIdVersion(sampleChildOrderPo);

    }

    /**
     * 确认接单事务提交
     *
     * @author ChenWenLong
     * @date 2023/5/9 14:24
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateReceiveOrder(SampleReceiveDto dto,
                                   SampleChildOrderPo sampleChildOrderPo,
                                   SampleOrderStatus targetStatus,
                                   SampleChildOrderChangePo sampleChildOrderChangePo,
                                   SampleParentOrderPo sampleParentOrderPo,
                                   SampleParentOrderChangePo sampleParentOrderChangePo) {

        sampleChildOrderPo.setSampleOrderStatus(targetStatus);
        sampleChildOrderDao.updateByIdVersion(sampleChildOrderPo);

        // 更新change
        sampleChildOrderChangePo.setReceiveOrderTime(new DateTime().toLocalDateTime());
        sampleChildOrderChangePo.setReceiveUser(GlobalContext.getUserKey());
        sampleChildOrderChangePo.setReceiptUsername(GlobalContext.getUsername());
        sampleChildOrderChangeDao.updateByIdVersion(sampleChildOrderChangePo);
        // 更新母单
        SampleOrderStatus earliestStatus = this.getEarliestStatus(sampleChildOrderPo.getSampleParentOrderNo(),
                Collections.singletonList(targetStatus));
        if (!sampleParentOrderPo.getSampleOrderStatus().equals(earliestStatus)) {
            sampleParentOrderPo.setSampleOrderStatus(earliestStatus);
            sampleParentOrderDao.updateByIdVersion(sampleParentOrderPo);
        }

        // 更新母单change
        sampleParentOrderChangePo.setReceiveOrderTime(new DateTime().toLocalDateTime());
        sampleParentOrderChangeDao.updateByIdVersion(sampleParentOrderChangePo);

        // limited、微创新、委外打样类型保存加工列表和加工工序
        if (BooleanType.TRUE.equals(dto.getIsReceived()) && SampleDevType.confirmSubmitAudited(sampleParentOrderPo.getSampleDevType())) {
            final List<SampleChildOrderProcessPo> sampleChildOrderProcessPoList = dto.getSampleProcessList()
                    .stream()
                    .map(processDto -> {
                        final SampleChildOrderProcessPo sampleChildOrderProcessPo = new SampleChildOrderProcessPo();
                        sampleChildOrderProcessPo.setSampleChildOrderId(sampleChildOrderPo.getSampleChildOrderId());
                        sampleChildOrderProcessPo.setSampleParentOrderNo(sampleParentOrderPo.getSampleParentOrderNo());
                        sampleChildOrderProcessPo.setSampleChildOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
                        sampleChildOrderProcessPo.setProcessCode(processDto.getProcessCode());
                        sampleChildOrderProcessPo.setProcessName(processDto.getProcessName());
                        sampleChildOrderProcessPo.setProcessSecondCode(processDto.getProcessSecondCode());
                        sampleChildOrderProcessPo.setProcessSecondName(processDto.getProcessSecondName());
                        sampleChildOrderProcessPo.setProcessLabel(processDto.getProcessLabel());

                        return sampleChildOrderProcessPo;
                    }).collect(Collectors.toList());
            sampleChildOrderProcessDao.insertBatch(sampleChildOrderProcessPoList);


            final List<SampleChildOrderProcessDescPo> sampleChildOrderProcessDescPoList = dto.getSampleProcessDescList()
                    .stream()
                    .map(processDescDto -> {
                        final SampleChildOrderProcessDescPo sampleChildOrderProcessDescPo = new SampleChildOrderProcessDescPo();
                        sampleChildOrderProcessDescPo.setSampleChildOrderId(sampleChildOrderPo.getSampleChildOrderId());
                        sampleChildOrderProcessDescPo.setSampleParentOrderNo(sampleParentOrderPo.getSampleParentOrderNo());
                        sampleChildOrderProcessDescPo.setSampleChildOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
                        sampleChildOrderProcessDescPo.setName(processDescDto.getName());
                        sampleChildOrderProcessDescPo.setDescValue(processDescDto.getDescValue());

                        return sampleChildOrderProcessDescPo;
                    }).collect(Collectors.toList());

            sampleChildOrderProcessDescDao.insertBatch(sampleChildOrderProcessDescPoList);

            // 原料发货
            final List<SampleChildOrderRawPo> sampleChildOrderRawPoList = sampleChildOrderRawDao.getListByChildNoAndType(sampleChildOrderPo.getSampleChildOrderNo(),
                    SampleRawBizType.DEMAND);
            final List<RawDeliverBo> purchaseRawDeliverBoList = sampleChildOrderRawPoList.stream()
                    .map(po -> {
                        final RawDeliverBo purchaseRawDeliverBo = new RawDeliverBo();
                        purchaseRawDeliverBo.setDeliveryCnt(po.getDeliveryCnt());
                        purchaseRawDeliverBo.setSku(po.getSku());
                        return purchaseRawDeliverBo;
                    }).collect(Collectors.toList());

            final WmsDeliverBo wmsDeliverBo = new WmsDeliverBo();
            wmsDeliverBo.setRelatedOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
            wmsDeliverBo.setRawWarehouseCode(sampleChildOrderPo.getRawWarehouseCode());
            wmsDeliverBo.setDeliveryType(WmsEnum.DeliveryType.SAMPLE_RAW_MATERIAL);
            wmsDeliverBo.setRawDeliverMode(null);
            wmsDeliverBo.setDispatchNow(BooleanType.TRUE);
            wmsDeliverBo.setPlatform(sampleChildOrderPo.getPlatform());
            purchaseRawRefService.wmsRawDeliver(wmsDeliverBo, purchaseRawDeliverBoList);
        }

        //日志
        logBaseService.simpleLog(LogBizModule.SAMPLE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                sampleChildOrderPo.getSampleChildOrderNo(), targetStatus.getRemark(), Collections.emptyList());
        final SampleStatusDto sampleStatusDto = new SampleStatusDto();
        sampleStatusDto.setSpuCode(sampleParentOrderPo.getSpu());
        sampleStatusDto.setSampleOrderStatus(targetStatus);
        sampleStatusDto.setKey(sampleChildOrderPo.getSampleChildOrderNo());
        sampleStatusDto.setUserKey(GlobalContext.getUserKey());
        sampleStatusDto.setUsername(GlobalContext.getUsername());
        consistencySendMqService.execSendMq(SampleStatusHandler.class, sampleStatusDto);

        // 推送钉钉消息
        sampleDingService.sendChildDingTalkMsg(sampleChildOrderPo, dto.getRefuseReason());

    }

    /**
     * 供应商补充原料
     *
     * @author ChenWenLong
     * @date 2023/5/11 11:56
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateSupplyRaw(SampleSupplyRawDto dto, SampleChildOrderPo sampleChildOrderPo) {
        final List<SampleChildOrderRawPo> sampleChildOrderRawPoList = sampleChildOrderRawDao.getListByChildNoAndType(dto.getSampleChildOrderNo(),
                SampleRawBizType.DEMAND);
        final Map<String, SampleChildOrderRawPo> skuRawPoMap = sampleChildOrderRawPoList.stream()
                .collect(Collectors.toMap(SampleChildOrderRawPo::getSku, Function.identity()));
        List<SampleChildOrderRawPo> updateSampleChildOrderRawList = new ArrayList<>();
        dto.getSampleRawList()
                .forEach(item -> {
                    SampleChildOrderRawPo sampleChildOrderRawPo = skuRawPoMap.get(item.getSku());
                    if (null == sampleChildOrderRawPo) {
                        sampleChildOrderRawPo = new SampleChildOrderRawPo();
                        sampleChildOrderRawPo.setSku(item.getSku());
                        sampleChildOrderRawPo.setDeliveryCnt(item.getDeliveryCnt());
                        sampleChildOrderRawPo.setSampleRawBizType(SampleRawBizType.DEMAND);
                        sampleChildOrderRawPo.setSampleParentOrderNo(sampleChildOrderPo.getSampleParentOrderNo());
                        sampleChildOrderRawPo.setSampleChildOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
                        sampleChildOrderRawPo.setSampleChildOrderId(sampleChildOrderPo.getSampleChildOrderId());
                    } else {
                        sampleChildOrderRawPo.setDeliveryCnt(sampleChildOrderRawPo.getDeliveryCnt() + item.getDeliveryCnt());
                    }
                    updateSampleChildOrderRawList.add(sampleChildOrderRawPo);
                });
        sampleChildOrderRawDao.insertOrUpdateBatch(updateSampleChildOrderRawList);


        // wms 生成原料发货单
        final List<RawDeliverBo> sampleRawDeliverBoList = dto.getSampleRawList().stream()
                .map(item -> {
                    final RawDeliverBo rawDeliverBo = new RawDeliverBo();
                    rawDeliverBo.setDeliveryCnt(item.getDeliveryCnt());
                    rawDeliverBo.setSku(item.getSku());
                    return rawDeliverBo;
                }).collect(Collectors.toList());

        final WmsDeliverBo wmsDeliverBo = new WmsDeliverBo();
        wmsDeliverBo.setRelatedOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
        wmsDeliverBo.setRawWarehouseCode(sampleChildOrderPo.getRawWarehouseCode());
        wmsDeliverBo.setDeliveryType(WmsEnum.DeliveryType.SAMPLE_RAW_MATERIAL);
        wmsDeliverBo.setRawDeliverMode(null);
        wmsDeliverBo.setDispatchNow(BooleanType.TRUE);
        wmsDeliverBo.setPlatform(sampleChildOrderPo.getPlatform());

        purchaseRawRefService.wmsRawDeliver(wmsDeliverBo, sampleRawDeliverBoList);
    }

    /**
     * 通过SKU查询处理是否第一次打样的SKU
     *
     * @author ChenWenLong
     * @date 2023/5/15 11:21
     */
    public Map<String, SampleProduceLabel> getSampleChildOrderMapBatchSku(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyMap();
        }

        Map<String, SampleProduceLabel> sampleProduceLabelMap = new HashMap<>();
        Map<String, List<SampleChildOrderPo>> sampleChildOrderPoMap = sampleChildOrderDao.getListBySkuAndStatusAndNotNo(skuList, List.of(SampleOrderStatus.SELECTED, SampleOrderStatus.SETTLE), Collections.emptyList())
                .stream().collect(Collectors.groupingBy(SampleChildOrderPo::getSku));

        for (String sku : skuList) {
            if (!sampleChildOrderPoMap.containsKey(sku)) {
                sampleProduceLabelMap.put(sku, SampleProduceLabel.EFFECTIVE);
            } else {
                sampleProduceLabelMap.put(sku, null);
            }
        }

        return sampleProduceLabelMap;
    }

    /**
     * 样品选样结果管理列表查询条件
     *
     * @author ChenWenLong
     * @date 2023/5/15 17:27
     */
    public SampleChildOrderResultSearchDto getSampleResultSearchWhere(SampleChildOrderResultSearchDto dto) {
        if (dto.getSampleTimeStart() != null || dto.getSampleTimeEnd() != null) {
            List<SampleChildOrderChangePo> sampleChildOrderChangePoList = sampleChildOrderChangeDao.getBySampleTime(dto.getSampleTimeStart(), dto.getSampleTimeEnd());
            if (CollectionUtils.isEmpty(sampleChildOrderChangePoList)) {
                return null;
            }
            dto.setSampleChildOrderIdList(sampleChildOrderChangePoList.stream().map(SampleChildOrderChangePo::getSampleChildOrderId).distinct().collect(Collectors.toList()));
        }
        return dto;
    }

    /**
     * 样品采购列表查询条件
     *
     * @author ChenWenLong
     * @date 2023/5/23 15:11
     */
    public SamplePurchaseSearchDto getSearchSampleChildWhere(SamplePurchaseSearchDto dto) {
        if (CollectionUtils.isNotEmpty(dto.getSkuEncodeList())) {
            final List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(dto.getSkuEncodeList());
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                skuListByEncode.addAll(dto.getSkuList());
                dto.setSkuList(skuListByEncode);
            }
        }
        return dto;
    }
}
