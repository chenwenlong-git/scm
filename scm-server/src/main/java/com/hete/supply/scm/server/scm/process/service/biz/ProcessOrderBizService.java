package com.hete.supply.scm.server.scm.process.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hete.supply.mc.api.msg.entity.dto.DingTalkOtoMsgDto;
import com.hete.supply.mc.api.msg.util.DingTalkMsgUtil;
import com.hete.supply.plm.api.goods.entity.vo.*;
import com.hete.supply.scm.api.scm.entity.dto.*;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.entity.vo.*;
import com.hete.supply.scm.api.scm.importation.entity.dto.ProcessMaterialImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.ProcessOrderDescImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.ProcessProcedureImportationDto;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.SdaRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.common.service.base.IContainer;
import com.hete.supply.scm.server.scm.defect.dao.DefectHandlingDao;
import com.hete.supply.scm.server.scm.defect.entity.bo.DefectHandlingCreateBo;
import com.hete.supply.scm.server.scm.defect.service.ref.DefectRefService;
import com.hete.supply.scm.server.scm.entity.bo.*;
import com.hete.supply.scm.server.scm.entity.dto.*;
import com.hete.supply.scm.server.scm.entity.po.DefectHandlingPo;
import com.hete.supply.scm.server.scm.entity.vo.MaterialNumInformationVo;
import com.hete.supply.scm.server.scm.entity.vo.WarehouseListVo;
import com.hete.supply.scm.server.scm.enums.BackStatus;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.enums.LogVersionValueType;
import com.hete.supply.scm.server.scm.enums.MaterialProductQuality;
import com.hete.supply.scm.server.scm.enums.wms.ScmBizReceiveOrderType;
import com.hete.supply.scm.server.scm.enums.wms.SkuDevType;
import com.hete.supply.scm.server.scm.handler.DingTalkHandler;
import com.hete.supply.scm.server.scm.handler.WmsReceiptHandler;
import com.hete.supply.scm.server.scm.nacosconfig.SpuConfig;
import com.hete.supply.scm.server.scm.process.builder.ProcessOrderBuilder;
import com.hete.supply.scm.server.scm.process.config.ScmProcessProp;
import com.hete.supply.scm.server.scm.process.converter.*;
import com.hete.supply.scm.server.scm.process.dao.*;
import com.hete.supply.scm.server.scm.process.entity.bo.*;
import com.hete.supply.scm.server.scm.process.entity.dto.*;
import com.hete.supply.scm.server.scm.process.entity.po.*;
import com.hete.supply.scm.server.scm.process.entity.vo.*;
import com.hete.supply.scm.server.scm.process.enums.MissingInfoOperationType;
import com.hete.supply.scm.server.scm.process.enums.ProcessOrderOriginal;
import com.hete.supply.scm.api.scm.entity.enums.ProcessProgressStatus;
import com.hete.supply.scm.server.scm.process.enums.ProcessStatus;
import com.hete.supply.scm.server.scm.process.handler.ProcessCostHandler;
import com.hete.supply.scm.server.scm.process.handler.ProcessOrderHandler;
import com.hete.supply.scm.server.scm.process.handler.RefreshProDateDelayHandler;
import com.hete.supply.scm.server.scm.process.handler.WmsProcessCancelHandler;
import com.hete.supply.scm.server.scm.process.service.base.*;
import com.hete.supply.scm.server.scm.qc.converter.QcOrderConverter;
import com.hete.supply.scm.server.scm.qc.dao.QcDetailDao;
import com.hete.supply.scm.server.scm.qc.dao.QcOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.bo.*;
import com.hete.supply.scm.server.scm.qc.service.base.AbstractQcOrderCreator;
import com.hete.supply.scm.server.scm.qc.service.base.ProcessOrderQcOrderCreator;
import com.hete.supply.scm.server.scm.sample.service.base.SampleBaseService;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.basic.entity.dto.ContainerUpdateStateDto;
import com.hete.supply.wms.api.basic.entity.vo.SimpleSkuBatchVo;
import com.hete.supply.wms.api.basic.entity.vo.WarehouseVo;
import com.hete.supply.wms.api.entry.entity.dto.ReceiveOrderGetDto;
import com.hete.supply.wms.api.entry.entity.vo.ReceiveOrderForScmVo;
import com.hete.supply.wms.api.interna.entity.dto.SkuInstockInventoryQueryDto;
import com.hete.supply.wms.api.interna.entity.vo.SkuInventoryVo;
import com.hete.supply.wms.api.leave.entity.vo.ProcessDeliveryOrderVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.consistency.core.service.ConsistencyService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.FormatStringUtil;
import com.hete.support.core.util.FreemarkerUtil;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.mybatis.plus.entity.bo.CompareResult;
import com.hete.support.mybatis.plus.util.DataCompareUtil;
import com.hete.support.redis.lock.annotation.RedisLock;
import com.hete.trace.util.TraceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: RockyHuas
 * @date: 2022/11/9 16:22
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessOrderBizService {
    private final ProcessOrderDao processOrderDao;
    private final IdGenerateService idGenerateService;
    private final ProcessOrderItemDao processOrderItemDao;
    private final ProcessOrderMaterialDao processOrderMaterialDao;
    private final ProcessOrderProcedureDao processOrderProcedureDao;
    private final ProcessOrderDescDao processOrderDescDao;
    private final ProcessDao processDao;
    private final ProcessOrderExtraDao processOrderExtraDao;
    private final ProcessOrderBaseService processOrderBaseService;
    private final ConsistencyService consistencyService;
    private final ConsistencySendMqService consistencySendMqService;
    private final WmsRemoteService wmsRemoteService;
    private final SampleBaseService sampleBaseService;
    private final PlmRemoteService plmRemoteService;
    private final ProcessMaterialReceiptDao processMaterialReceiptDao;
    private final ProcessMaterialReceiptItemDao processMaterialReceiptItemDao;
    private final ProcessMaterialBackDao processMaterialBackDao;
    private final ProcessMaterialBackItemDao processMaterialBackItemDao;
    private final ProcessOrderScanDao processOrderScanDao;
    private final ProcessDefectiveRecordDao processDefectiveRecordDao;
    private final ProcessDefectiveRecordItemDao processDefectiveRecordItemDao;
    private final ProcessOrderScanBaseService processOrderScanBaseService;
    private final ProcessOrderCreateBaseService processOrderCreateBaseService;
    private final ProcessOrderSampleDao processOrderSampleDao;
    private final ProcessMaterialDetailDao processMaterialDetailDao;
    private final ProcessMaterialDetailItemDao processMaterialDetailItemDao;
    private final ScmImageBaseService scmImageBaseService;
    private final DefectRefService defectRefService;
    private final DefectHandlingDao defectHandlingDao;
    private final ProcessPlanBaseService processPlanBaseService;
    private final ProcessOrderMaterialBaseService processOrderMaterialBaseService;
    private final ProcessProcedureEmployeePlanDao processProcedureEmployeePlanDao;
    private final QcOrderDao qcOrderDao;
    private final QcDetailDao qcDetailDao;
    private final Environment environment;
    private final SdaRemoteService sdaRemoteService;
    private final SpuConfig spuConfig;
    private final ScmProcessProp scmProcessProp;
    private final ProcessOrderMaterialCompareDao procMaterialCompareDao;

    private final static int MAX_ITERATIONS = 200;

    /**
     * 原料配比小数点
     */
    private final static Integer RATE_SCALE = 1;

    /**
     * 加工单数量限制
     */
    private final static Integer PROCESS_NUM_LIMIT = 10;

    //复杂工序加工数量限制产量
    private final static int COMPLEX_PROCESS_NUM_LIMIT = 5;

    @Value("${scm.processOrder.deliveryWarehouseCode}")
    private String deliveryWarehouseCode;

    @Value("${scm.processOrder.deliveryWarehouseName}")
    private String deliveryWarehouseName;

    @Value("${scm.processOrder.defectiveWarehouseCode}")
    private String defectiveWarehouseCode;

    /**
     * 分页查询
     *
     * @param processOrderQueryDto
     * @return
     */
    public CommonPageResult.PageInfo<ProcessOrderVo> getByPage(ProcessOrderQueryDto processOrderQueryDto) {
        List<String> skus = processOrderQueryDto.getSkus();
        // spu 属性，以及商品品类
        List<Long> categoryIdList = processOrderQueryDto.getCategoryIdList();
        List<String> skuAttributes = processOrderQueryDto.getSkuAttributes();
        List<String> skuEncodeList = processOrderQueryDto.getSkuEncodeList();
        DefectiveRecordStatus defectiveRecordStatus = processOrderQueryDto.getDefectiveRecordStatus();
        List<String> materialSkuList = processOrderQueryDto.getMaterialSkuList();
        ProcessOrderQueryBo processOrderQueryCondition
                = processOrderBaseService.getProcessOrderQueryCondition(skus, categoryIdList, skuAttributes,
                skuEncodeList, materialSkuList,
                defectiveRecordStatus);
        if (processOrderQueryCondition.getIsEmpty()) {
            return new CommonPageResult.PageInfo<>();
        }

        CommonPageResult.PageInfo<ProcessOrderVo> result
                = processOrderDao.getByPage(
                PageDTO.of(processOrderQueryDto.getPageNo(), processOrderQueryDto.getPageSize()), processOrderQueryDto,
                processOrderQueryCondition, ProcessOrderStatus.PROCESSING);
        List<ProcessOrderVo> records = result.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return result;
        }

        for (ProcessOrderVo record : records) {
            final String processOrderNo = record.getProcessOrderNo();

            // 图片信息
            if (StringUtils.isNotBlank(record.getFileCodeStr())) {
                record.setFileCodeList(Arrays.asList(record.getFileCodeStr()
                        .split(",")));
            }

            // 缺失信息
            Set<MissingInformation> missingInformationList
                    = MissInformationConverter.convertToMissingInformationEnums(record.getMissingInformationEnums());
            if (CollectionUtils.isNotEmpty(missingInformationList)) {
                Set<String> missingInformationRemarks = missingInformationList.stream()
                        .map(missingInformation -> MissingInformation.valueOf(missingInformation.name())
                                .getRemark())
                        .collect(Collectors.toSet());
                record.setMissingInformationRemarks(missingInformationRemarks);
            }

            // 原料数量信息
            List<MaterialNumInformationVo> materialNumInformationVos
                    = this.getMaterialNumInformationVos(processOrderNo);
            record.setMaterialNumInformationVos(materialNumInformationVos);

            // 加工进度
            List<ProcessProgressVo> processProgressVos = this.getProcessProgressVo(processOrderNo);
            record.setProcessProgressVos(processProgressVos);
        }

        List<String> processOrderNos = records.stream()
                .map(ProcessOrderVo::getProcessOrderNo)
                .collect(Collectors.toList());
        List<ProcessOrderItemPo> allProcessOrderItemPos = processOrderItemDao.getByProcessOrderNos(processOrderNos);
        if (CollectionUtils.isEmpty(allProcessOrderItemPos)) {
            return result;
        }
        List<ProcessOrderMaterialPo> processOrderMaterialPoList
                = processOrderMaterialDao.getByProcessOrderNos(processOrderNos);
        List<String> skuList = allProcessOrderItemPos.stream()
                .map(ProcessOrderItemPo::getSku)
                .collect(Collectors.toList());
        processOrderMaterialPoList.forEach(item -> {
            skuList.add(item.getSku());
        });


        List<PlmSkuVo> skuEncodeBySku = plmRemoteService.getSkuEncodeBySku(skuList);
        Map<String, List<PlmSkuVo>> groupedPlmSkuVo = skuEncodeBySku.stream()
                .collect(Collectors.groupingBy(PlmSkuVo::getSkuCode));

        // 通过 spu 获取商品类目展示
        List<String> spuList = records.stream()
                .map(ProcessOrderVo::getSpu)
                .distinct()
                .collect(Collectors.toList());
        Map<String, String> spuCategoriesMapBySpuList = plmRemoteService.getSpuCategoriesMapBySpuList(spuList);

        Map<String, List<ProcessOrderItemPo>> grouped = allProcessOrderItemPos.stream()
                .collect(Collectors.groupingBy(ProcessOrderItemPo::getProcessOrderNo));

        // 获取原料信息
        Map<String, List<ProcessOrderMaterialPo>> groupedMaterialPos = processOrderMaterialPoList.stream()
                .collect(Collectors.groupingBy(ProcessOrderMaterialPo::getProcessOrderNo));

        List<ProcessOrderVo> newRecords = records.stream()
                .peek(it -> {
                    List<ProcessOrderItemPo> processOrderItemPos = grouped.get(it.getProcessOrderNo());
                    if (CollectionUtils.isNotEmpty(processOrderItemPos)) {
                        List<ProcessOrderItemVo> processOrderItemVoList = processOrderItemPos.stream()
                                .map(it2 -> {
                                    ProcessOrderItemVo processOrderItemVo = ProcessOrderConverter.INSTANCE.convert(it2);
                                    List<PlmSkuVo> plmSkuVos = groupedPlmSkuVo.get(it2.getSku());
                                    if (CollectionUtils.isNotEmpty(plmSkuVos)) {
                                        Optional<PlmSkuVo> firstPlmSkuVoOptional = plmSkuVos.stream()
                                                .findFirst();
                                        firstPlmSkuVoOptional.ifPresent(
                                                plmSkuVo -> processOrderItemVo.setSkuEncode(plmSkuVo.getSkuEncode()));
                                    }
                                    return processOrderItemVo;
                                })
                                .collect(Collectors.toList());
                        it.setProcessOrderItemVoList(processOrderItemVoList);
                    }
                    List<ProcessOrderMaterialPo> needProcessOrderMaterialPoList
                            = groupedMaterialPos.get(it.getProcessOrderNo());
                    if (CollectionUtils.isNotEmpty(needProcessOrderMaterialPoList)) {
                        List<ProcessOrderMaterialVo> materialVoList = needProcessOrderMaterialPoList.stream()
                                .map(it2 -> {
                                    ProcessOrderMaterialVo processOrderMaterialVo
                                            = ProcessOrderConverter.INSTANCE.convert(it2);
                                    List<PlmSkuVo> plmSkuVos = groupedPlmSkuVo.get(it2.getSku());
                                    if (CollectionUtils.isNotEmpty(plmSkuVos)) {
                                        Optional<PlmSkuVo> firstPlmSkuVoOptional = plmSkuVos.stream()
                                                .findFirst();
                                        firstPlmSkuVoOptional.ifPresent(plmSkuVo -> processOrderMaterialVo.setSkuEncode(
                                                plmSkuVo.getSkuEncode()));
                                    }
                                    return processOrderMaterialVo;
                                })
                                .collect(Collectors.toList());
                        it.setProcessOrderMaterialVoList(materialVoList);
                    }
                    it.setCategoryName(spuCategoriesMapBySpuList.get(it.getSpu()));

                })
                .collect(Collectors.toList());

        result.setRecords(newRecords);

        return result;

    }

    /**
     * 通过加工单号获取工序进度信息
     *
     * @param processOrderNo
     * @return
     */
    private List<ProcessProgressVo> getProcessProgressVo(String processOrderNo) {
        if (StringUtils.isBlank(processOrderNo)) {
            return null;
        }

        List<ProcessOrderProcedureByH5Vo> processOrderScanByNo
                = processOrderBaseService.getProcessOrderScanByNo(processOrderNo);
        if (CollectionUtils.isNotEmpty(processOrderScanByNo)) {
            return processOrderScanByNo.stream()
                    .map(processOrderScan -> {
                        ProcessProgressVo processProgressVo = new ProcessProgressVo();
                        processProgressVo.setProcessName(StrUtil.format("{}({})", Objects.nonNull(
                                processOrderScan.getProcessLabel()) ? processOrderScan.getProcessLabel()
                                .getRemark() : "", processOrderScan.getProcessSecondName()));
                        if (StrUtil.isBlank(processOrderScan.getReceiptUsername())) {
                            processProgressVo.setProcessProgressStatus(ProcessProgressStatus.UN_START);
                        }
                        if (StrUtil.isNotBlank(processOrderScan.getReceiptUsername()) && StrUtil.isBlank(
                                processOrderScan.getCompleteUsername())) {
                            processProgressVo.setProcessProgressStatus(ProcessProgressStatus.PROCESSING);
                        }
                        if (StrUtil.isNotBlank(processOrderScan.getCompleteUsername())) {
                            processProgressVo.setProcessProgressStatus(ProcessProgressStatus.COMPLETED);
                        }
                        return processProgressVo;
                    })
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 通过加工单号获取原料数量信息
     *
     * @param processOrderNo
     * @return
     */
    private List<MaterialNumInformationVo> getMaterialNumInformationVos(String processOrderNo) {
        if (StringUtils.isBlank(processOrderNo)) {
            return Collections.emptyList();
        }

        // 获取加工单原料、收货、收货明细信息
        final List<ProcessMaterialDetailPo> processMaterialDetailPoList
                = processMaterialDetailDao.getListByProcessOrderNo(processOrderNo);
        final List<Long> processMaterialDetailIdList = processMaterialDetailPoList.stream()
                .map(ProcessMaterialDetailPo::getProcessMaterialDetailId)
                .collect(Collectors.toList());
        final List<ProcessMaterialDetailItemPo> processMaterialDetailItemPoList
                = processMaterialDetailItemDao.getListByProcessMaterialDetailIdList(processMaterialDetailIdList);
        List<ProcessOrderMaterialPo> processOrderMaterialPos
                = processOrderMaterialDao.getByProcessOrderNo(processOrderNo);
        List<ProcessMaterialReceiptPo> processMaterialReceiptPos
                = processMaterialReceiptDao.getByProcessOrderNo(processOrderNo);
        List<ProcessMaterialReceiptItemPo> materialReceiptItems = Collections.emptyList();
        if (CollectionUtils.isNotEmpty(processMaterialReceiptPos)) {
            List<Long> materialReceiptIds = processMaterialReceiptPos.stream()
                    .map(ProcessMaterialReceiptPo::getProcessMaterialReceiptId)
                    .collect(Collectors.toList());
            materialReceiptItems = processMaterialReceiptItemDao.getByMaterialReceiptIds(materialReceiptIds);
        }

        // 获取加工单相关的所有sku
        Set<String> allSkus = Sets.newHashSet();
        Set<String> materialSkus = processOrderMaterialPos.stream()
                .map(ProcessOrderMaterialPo::getSku)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(materialSkus)) {
            allSkus.addAll(materialSkus);
        }
        if (CollectionUtils.isNotEmpty(materialReceiptItems)) {
            Set<String> receiptItemSkus = materialReceiptItems.stream()
                    .map(ProcessMaterialReceiptItemPo::getSku)
                    .filter(StrUtil::isNotBlank)
                    .collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(receiptItemSkus)) {
                allSkus.addAll(receiptItemSkus);
            }
        }

        if (CollectionUtils.isEmpty(allSkus)) {
            return Collections.emptyList();
        }

        List<ProcessMaterialReceiptItemPo> finalMaterialReceiptItems = materialReceiptItems;
        return allSkus.stream()
                .map(sku -> {
                    MaterialNumInformationVo materialNumInformationVo = new MaterialNumInformationVo();
                    materialNumInformationVo.setSku(sku);
                    // 下单数
                    if (CollectionUtils.isNotEmpty(processMaterialDetailItemPoList)) {
                        int orderNum = processMaterialDetailItemPoList.stream()
                                .filter(processMaterialDetailItemPo -> Objects.equals(sku,
                                        processMaterialDetailItemPo.getSku()) && Objects.nonNull(
                                        processMaterialDetailItemPo.getDeliveryNum()))
                                .mapToInt(ProcessMaterialDetailItemPo::getDeliveryNum)
                                .sum();
                        materialNumInformationVo.setOrderNum(orderNum);
                    } else {
                        int orderNum = processOrderMaterialPos.stream()
                                .filter(processOrderMaterialPo -> Objects.equals(sku,
                                        processOrderMaterialPo.getSku()) && Objects.nonNull(
                                        processOrderMaterialPo.getDeliveryNum()))
                                .mapToInt(ProcessOrderMaterialPo::getDeliveryNum)
                                .sum();
                        materialNumInformationVo.setOrderNum(orderNum);
                    }

                    // 发货数
                    int deliveryNum = finalMaterialReceiptItems.stream()
                            .filter(materialReceipt -> Objects.equals(sku, materialReceipt.getSku()) && Objects.nonNull(
                                    materialReceipt.getDeliveryNum()))
                            .mapToInt(ProcessMaterialReceiptItemPo::getDeliveryNum)
                            .sum();
                    materialNumInformationVo.setDeliveryNum(deliveryNum);

                    // 收货数
                    int receiptNum = finalMaterialReceiptItems.stream()
                            .filter(materialReceipt -> Objects.equals(sku, materialReceipt.getSku()) && Objects.nonNull(
                                    materialReceipt.getReceiptNum()))
                            .mapToInt(ProcessMaterialReceiptItemPo::getReceiptNum)
                            .sum();
                    materialNumInformationVo.setReceiptNum(receiptNum);
                    return materialNumInformationVo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 查询总数（按单查询）
     *
     * @param dto
     * @return
     */
    public Integer getExportTotalsByOrder(ProcessOrderQueryByApiDto dto) {

        List<String> skus = dto.getSkus();
        // spu 属性，以及商品品类
        List<Long> categoryIdList = dto.getCategoryIdList();
        List<String> skuAttributes = dto.getSkuAttributes();
        List<String> skuEncodeList = dto.getSkuEncodeList();
        DefectiveRecordStatus defectiveRecordStatus = dto.getDefectiveRecordStatus();
        List<String> materialSkuList = dto.getMaterialSkuList();
        ProcessOrderQueryBo processOrderQueryCondition
                = processOrderBaseService.getProcessOrderQueryCondition(skus, categoryIdList, skuAttributes,
                skuEncodeList, materialSkuList,
                defectiveRecordStatus);
        if (processOrderQueryCondition.getIsEmpty()) {
            return 0;
        }
        return processOrderDao.getExportTotalsByOrder(dto, processOrderQueryCondition, ProcessOrderStatus.PROCESSING);
    }

    /**
     * 查询列表（按单查询）
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<ProcessOrderExportByOrderVo> getExportListByOrder(ProcessOrderQueryByApiDto dto) {
        List<String> skus = dto.getSkus();
        // spu 属性，以及商品品类
        List<Long> categoryIdList = dto.getCategoryIdList();
        List<String> skuAttributes = dto.getSkuAttributes();
        List<String> skuEncodeList = dto.getSkuEncodeList();
        DefectiveRecordStatus defectiveRecordStatus = dto.getDefectiveRecordStatus();
        List<String> materialSkuList = dto.getMaterialSkuList();
        ProcessOrderQueryBo processOrderQueryCondition
                = processOrderBaseService.getProcessOrderQueryCondition(skus, categoryIdList, skuAttributes,
                skuEncodeList, materialSkuList,
                defectiveRecordStatus);
        if (processOrderQueryCondition.getIsEmpty()) {
            return new CommonPageResult.PageInfo<>();
        }
        CommonPageResult.PageInfo<ProcessOrderExportByOrderVo> exportListByOrder
                = processOrderDao.getExportListByOrder(PageDTO.of(dto.getPageNo(), dto.getPageSize(), false), dto,
                processOrderQueryCondition, ProcessOrderStatus.PROCESSING);
        List<ProcessOrderExportByOrderVo> processOrderExportByOrderVos = exportListByOrder.getRecords();

        // 获取详情
        if (CollectionUtils.isNotEmpty(processOrderExportByOrderVos)) {
            List<String> queriedProcessOrderNos = processOrderExportByOrderVos.stream()
                    .map(ProcessOrderExportByOrderVo::getProcessOrderNo)
                    .collect(Collectors.toList());
            List<ProcessOrderItemPo> processOrderItemPos
                    = processOrderItemDao.getByProcessOrderNos(queriedProcessOrderNos);
            Map<String, List<ProcessOrderItemPo>> grouped = processOrderItemPos.stream()
                    .collect(Collectors.groupingBy(ProcessOrderItemPo::getProcessOrderNo));

            List<String> needProcessPlanProcessOrderNos = processOrderExportByOrderVos.stream()
                    .filter(vo -> Objects.equals(NeedProcessPlan.TRUE, vo.getNeedProcessPlan()))
                    .map(ProcessOrderExportByOrderVo::getProcessOrderNo)
                    .collect(Collectors.toList());
            List<ProcessProcedureEmployeePlanPo> processProcedureEmployeePlanPos
                    = processProcedureEmployeePlanDao.getDistinctByProcessOrderNos(needProcessPlanProcessOrderNos);
            Map<String, ProcessProcedureEmployeePlanPo> processProcedureEmployeePlanPoMap =
                    processProcedureEmployeePlanPos.stream()
                            .collect(Collectors.toMap(
                                    ProcessProcedureEmployeePlanPo::getProcessOrderNo,
                                    p -> p,
                                    (existing, replacement) -> existing
                            ));

            // 获取平台名称
            final List<String> platCodeList = processOrderExportByOrderVos.stream()
                    .map(ProcessOrderExportByOrderVo::getPlatform)
                    .distinct()
                    .collect(Collectors.toList());
            final Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);

            processOrderExportByOrderVos = processOrderExportByOrderVos.stream()
                    .peek(item -> {
                        // 转化缺失信息备注
                        String missInformationRemarks
                                = MissInformationConverter.convertToRemarks(item.getMissingInformationEnums());
                        item.setMissingInformationRemarks(missInformationRemarks);
                        // 转化是否回料备注
                        IsReceiveMaterial isReceiveMaterialEnum = item.getIsReceiveMaterialEnum();
                        if (Objects.nonNull(isReceiveMaterialEnum)) {
                            item.setIsReceiveMaterialRemark(isReceiveMaterialEnum.getRemark());
                        } else {
                            item.setIsReceiveMaterialRemark("");
                        }
                        // 转化是否超额备注
                        if (Objects.nonNull(item.getOverPlanEnum())) {
                            item.setOverPlanRemark(item.getOverPlanEnum()
                                    .getRemark());
                        } else {
                            item.setOverPlanRemark("");
                        }
                        // 转化是否需排产
                        if (Objects.nonNull(item.getNeedProcessPlan())) {
                            item.setNeedProcessPlanRemark(item.getNeedProcessPlan()
                                    .getRemark());
                        } else {
                            item.setNeedProcessPlanRemark("");
                        }

                        List<ProcessOrderItemPo> processOrderItemPosByOrder = grouped.get(item.getProcessOrderNo());
                        if (CollectionUtils.isNotEmpty(processOrderItemPosByOrder)) {
                            ProcessOrderItemPo matchOneOfItem = processOrderItemPosByOrder.stream()
                                    .filter(Objects::nonNull)
                                    .findFirst()
                                    .orElse(null);

                            BigDecimal totalPurchasePrice = processOrderItemPosByOrder.stream()
                                    .map(it -> it.getPurchasePrice()
                                            .multiply(new BigDecimal(it.getQualityGoodsCnt())))
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                            item.setTotalPurchasePrice(totalPurchasePrice);
                            int totalQualityGoodsCnt = processOrderItemPosByOrder.stream()
                                    .mapToInt(ProcessOrderItemPo::getQualityGoodsCnt)
                                    .sum();
                            item.setTotalQualityGoodsCnt(totalQualityGoodsCnt);
                            int totalDefectiveGoodsCnt = processOrderItemPosByOrder.stream()
                                    .mapToInt(ProcessOrderItemPo::getDefectiveGoodsCnt)
                                    .sum();
                            item.setTotalDefectiveGoodsCnt(totalDefectiveGoodsCnt);
                            int processNum = processOrderItemPosByOrder.stream()
                                    .mapToInt(ProcessOrderItemPo::getProcessNum)
                                    .sum();
                            item.setProcessNum(processNum);
                            item.setSku(Objects.nonNull(matchOneOfItem) ? matchOneOfItem.getSku() : "");
                        } else {
                            item.setTotalPurchasePrice(new BigDecimal(0));
                            item.setTotalQualityGoodsCnt(0);
                            item.setTotalDefectiveGoodsCnt(0);
                            item.setProcessNum(0);
                        }

                        // 排产人
                        if (Objects.equals(NeedProcessPlan.TRUE, item.getNeedProcessPlan())) {
                            ProcessProcedureEmployeePlanPo processProcedureEmployeePlanPo
                                    = processProcedureEmployeePlanPoMap.get(item.getProcessOrderNo());
                            if (Objects.nonNull(processProcedureEmployeePlanPo)) {
                                item.setProcessPlanUserName(processProcedureEmployeePlanPo.getCreateUsername());
                            }
                        }
                        item.setPlatform(platCodeNameMap.get(item.getPlatform()));
                    })
                    .collect(Collectors.toList());

        }
        exportListByOrder.setRecords(processOrderExportByOrderVos);
        return exportListByOrder;
    }

    /**
     * 查询总数（按sku查询）
     *
     * @param dto
     * @return
     */
    public Integer getExportTotalsByItem(ProcessOrderQueryByApiDto dto) {
        List<String> skus = dto.getSkus();
        // spu 属性，以及商品品类
        List<Long> categoryIdList = dto.getCategoryIdList();
        List<String> skuAttributes = dto.getSkuAttributes();
        List<String> skuEncodeList = dto.getSkuEncodeList();
        DefectiveRecordStatus defectiveRecordStatus = dto.getDefectiveRecordStatus();
        List<String> materialSkuList = dto.getMaterialSkuList();
        ProcessOrderQueryBo processOrderQueryCondition
                = processOrderBaseService.getProcessOrderQueryCondition(skus, categoryIdList, skuAttributes,
                skuEncodeList, materialSkuList,
                defectiveRecordStatus);
        if (processOrderQueryCondition.getIsEmpty()) {
            return 0;
        }
        return processOrderDao.getExportTotalsByItem(dto, processOrderQueryCondition, ProcessOrderStatus.PROCESSING);

    }

    /**
     * 查询列表（按sku查询）
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<ProcessOrderExportByItemVo> getExportListByItem(ProcessOrderQueryByApiDto dto) {
        List<String> skus = dto.getSkus();
        // spu 属性，以及商品品类
        List<Long> categoryIdList = dto.getCategoryIdList();
        List<String> skuAttributes = dto.getSkuAttributes();
        List<String> skuEncodeList = dto.getSkuEncodeList();
        DefectiveRecordStatus defectiveRecordStatus = dto.getDefectiveRecordStatus();
        List<String> materialSkuList = dto.getMaterialSkuList();
        ProcessOrderQueryBo processOrderQueryCondition
                = processOrderBaseService.getProcessOrderQueryCondition(skus, categoryIdList, skuAttributes,
                skuEncodeList, materialSkuList,
                defectiveRecordStatus);
        if (processOrderQueryCondition.getIsEmpty()) {
            return new CommonPageResult.PageInfo<>();
        }
        CommonPageResult.PageInfo<ProcessOrderExportByItemVo> exportListByItem
                = processOrderDao.getExportListByItem(PageDTO.of(dto.getPageNo(), dto.getPageSize(), false), dto,
                processOrderQueryCondition, ProcessOrderStatus.PROCESSING);
        List<ProcessOrderExportByItemVo> records = exportListByItem.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return exportListByItem;
        }

        List<String> skuList = records.stream()
                .map(ProcessOrderExportByItemVo::getSku)
                .collect(Collectors.toList());
        List<PlmSkuVo> skuEncodeBySku = plmRemoteService.getSkuEncodeBySku(skuList);
        Map<String, String> groupedSkuEncode = skuEncodeBySku.stream()
                .collect(Collectors.toMap(PlmSkuVo::getSkuCode, PlmSkuVo::getSkuEncode));

        // 通过 spu 获取商品类目展示
        List<String> spuList = records.stream()
                .map(ProcessOrderExportByItemVo::getSpu)
                .distinct()
                .collect(Collectors.toList());
        Map<String, String> spuCategoriesMapBySpuList = plmRemoteService.getSpuCategoriesMapBySpuList(spuList);

        // 需排产加工单号
        List<String> needProcessPlanProcessOrderNos = records.stream()
                .filter(r -> Objects.equals(NeedProcessPlan.TRUE, r.getNeedProcessPlan()))
                .map(ProcessOrderExportByItemVo::getProcessOrderNo)
                .collect(Collectors.toList());
        List<ProcessProcedureEmployeePlanPo> processProcedureEmployeePlanPos
                = processProcedureEmployeePlanDao.getDistinctByProcessOrderNos(needProcessPlanProcessOrderNos);
        Map<String, ProcessProcedureEmployeePlanPo> processProcedureEmployeePlanPoMap =
                processProcedureEmployeePlanPos.stream()
                        .collect(Collectors.toMap(
                                ProcessProcedureEmployeePlanPo::getProcessOrderNo,
                                p -> p,
                                (existing, replacement) -> existing
                        ));

        // 获取平台名称
        final List<String> platCodeList = records.stream()
                .map(ProcessOrderExportByItemVo::getPlatform)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);

        exportListByItem.setRecords(records.stream()
                .peek(item -> {
                    // 转化缺失信息备注
                    String missInformationRemarks
                            = MissInformationConverter.convertToRemarks(
                            item.getMissingInformationEnums());
                    item.setMissingInformationRemarks(missInformationRemarks);
                    // 转化是否回料备注
                    IsReceiveMaterial isReceiveMaterialEnum
                            = item.getIsReceiveMaterialEnum();
                    if (Objects.nonNull(isReceiveMaterialEnum)) {
                        item.setIsReceiveMaterialRemark(isReceiveMaterialEnum.getRemark());
                    } else {
                        item.setIsReceiveMaterialRemark("");
                    }
                    // 转化是否超额备注
                    if (Objects.nonNull(item.getOverPlanEnum())) {
                        item.setOverPlanRemark(item.getOverPlanEnum()
                                .getRemark());
                    } else {
                        item.setOverPlanRemark("");
                    }
                    item.setPlatform(platCodeNameMap.get(item.getPlatform()));
                    item.setSkuEncode(groupedSkuEncode.get(item.getSku()));
                    item.setCategoryName(spuCategoriesMapBySpuList.get(item.getSpu()));

                    // 设置排产人名称
                    if (Objects.equals(NeedProcessPlan.TRUE, item.getNeedProcessPlan())) {
                        ProcessProcedureEmployeePlanPo processProcedureEmployeePlanPo
                                = processProcedureEmployeePlanPoMap.get(
                                item.getProcessOrderNo());
                        if (Objects.nonNull(processProcedureEmployeePlanPo)) {
                            item.setProcessPlanUserName(
                                    processProcedureEmployeePlanPo.getCreateUsername());
                        }
                    }
                })
                .collect(Collectors.toList()));
        return exportListByItem;
    }

    /**
     * 查询总数（按原料查询）
     *
     * @param dto
     * @return
     */
    public Integer getExportTotalsByMaterial(ProcessOrderQueryByApiDto dto) {
        List<String> skus = dto.getSkus();
        // spu 属性，以及商品品类
        List<Long> categoryIdList = dto.getCategoryIdList();
        List<String> skuAttributes = dto.getSkuAttributes();
        List<String> skuEncodeList = dto.getSkuEncodeList();
        DefectiveRecordStatus defectiveRecordStatus = dto.getDefectiveRecordStatus();
        List<String> materialSkuList = dto.getMaterialSkuList();
        ProcessOrderQueryBo processOrderQueryCondition
                = processOrderBaseService.getProcessOrderQueryCondition(skus, categoryIdList, skuAttributes,
                skuEncodeList, materialSkuList,
                defectiveRecordStatus);
        if (processOrderQueryCondition.getIsEmpty()) {
            return 0;
        }
        return processOrderDao.getExportTotalsByMaterial(dto, processOrderQueryCondition,
                ProcessOrderStatus.PROCESSING);

    }

    /**
     * @Description 按原料导出
     * @author yanjiawei
     * @Date 2024/12/3 上午10:17
     */
    public CommonPageResult.PageInfo<ProcessOrderExportByMaterialVo> getExportListByMaterial(ProcessOrderQueryByApiDto dto) {
        List<String> skus = dto.getSkus();
        List<Long> categoryIdList = dto.getCategoryIdList();
        List<String> skuAttributes = dto.getSkuAttributes();
        List<String> skuEncodeList = dto.getSkuEncodeList();
        DefectiveRecordStatus defectiveRecordStatus = dto.getDefectiveRecordStatus();
        List<String> materialSkuList = dto.getMaterialSkuList();

        ProcessOrderQueryBo processOrderQueryCondition
                = processOrderBaseService.getProcessOrderQueryCondition(skus, categoryIdList, skuAttributes, skuEncodeList, materialSkuList, defectiveRecordStatus);
        if (processOrderQueryCondition.getIsEmpty()) {
            return new CommonPageResult.PageInfo<>();
        }

        CommonPageResult.PageInfo<ProcessOrderExportByMaterialVo> pageResult
                = processOrderDao.getExportListByMaterial(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto, processOrderQueryCondition);
        List<ProcessOrderExportByMaterialVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageResult;
        }

        List<String> processOrderNoList = records.stream()
                .map(ProcessOrderExportByMaterialVo::getProcessOrderNo).distinct()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());

        //加工明细散列表
        List<ProcessOrderItemPo> processOrderItemPoList = processOrderItemDao.getByProcessOrderNos(processOrderNoList);
        Map<String, ProcessOrderItemPo> procNoItemMap = processOrderItemPoList.stream().collect(
                Collectors.toMap(ProcessOrderItemPo::getProcessOrderNo, item -> item,
                        (exitingItem, newItem) -> exitingItem)
        );

        //收货明细散列表
        Map<String, ReceiveOrderForScmVo> procNoReceiveMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(processOrderNoList)) {
            ReceiveOrderGetDto queryReceiveOrderParam = new ReceiveOrderGetDto();
            queryReceiveOrderParam.setScmBizNoList(processOrderNoList);
            queryReceiveOrderParam.setReceiveType(WmsEnum.ReceiveType.PROCESS_PRODUCT);

            List<ReceiveOrderForScmVo> receiveOrderForScmVoList = wmsRemoteService.getReceiveOrderList(queryReceiveOrderParam);
            if (CollectionUtils.isNotEmpty(receiveOrderForScmVoList)) {
                procNoReceiveMap = receiveOrderForScmVoList.stream().collect(
                        Collectors.toMap(ReceiveOrderForScmVo::getScmBizNo, item -> item,
                                (exitingItem, newItem) -> exitingItem)
                );
            }
        }

        //平台名称散列表
        List<String> platCodeList = records.stream()
                .map(ProcessOrderExportByMaterialVo::getPlatform).distinct()
                .collect(Collectors.toList());
        Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);

        //sku产品名称散列表
        Set<String> processSkus = processOrderItemPoList.stream()
                .map(ProcessOrderItemPo::getSku)
                .collect(Collectors.toSet());
        Set<String> materialSkus = records.stream()
                .map(ProcessOrderExportByMaterialVo::getSku)
                .collect(Collectors.toSet());
        Set<String> querySkuList = Stream.concat(materialSkus.stream(), processSkus.stream()).collect(Collectors.toSet());
        Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(new ArrayList<>(querySkuList));

        for (ProcessOrderExportByMaterialVo record : records) {
            String processOrderNo = record.getProcessOrderNo();
            ProcessOrderItemPo processOrderItemPo = procNoItemMap.get(processOrderNo);
            if (Objects.nonNull(processOrderItemPo)) {
                String processSku = processOrderItemPo.getSku();
                record.setProcessSku(processSku);
                record.setProcessSkuName(skuEncodeMap.getOrDefault(processSku, ""));
                record.setProcessNum(processOrderItemPo.getProcessNum());
                record.setCheckingNum(processOrderItemPo.getQualityGoodsCnt() + processOrderItemPo.getDefectiveGoodsCnt());
            }

            String missInformationRemarks = MissInformationConverter.convertToRemarks(record.getMissingInformationEnums());
            record.setMissingInformationRemarks(missInformationRemarks);

            IsReceiveMaterial isReceiveMaterialEnum = record.getIsReceiveMaterialEnum();
            record.setIsReceiveMaterialRemark(Objects.nonNull(isReceiveMaterialEnum) ? isReceiveMaterialEnum.getRemark() : "");

            OverPlan overPlanEnum = record.getOverPlanEnum();
            record.setOverPlanRemark(Objects.nonNull(overPlanEnum) ? overPlanEnum.getRemark() : "");

            record.setPlatform(platCodeNameMap.get(record.getPlatform()));
            record.setMaterialSkuName(skuEncodeMap.getOrDefault(record.getSku(), ""));

            ReceiveOrderForScmVo receiveOrder = procNoReceiveMap.get(processOrderNo);
            if (Objects.nonNull(receiveOrder)) {
                record.setReceiveAmount(receiveOrder.getReceiveAmount());
                record.setFinishReceiveTime(receiveOrder.getFinishReceiveTime());
                record.setFinishOnShelfTime(receiveOrder.getFinishOnShelfTime());
                record.setReceiptOrderNo(receiveOrder.getReceiveOrderNo());

                List<ReceiveOrderForScmVo.ReceiveDeliver> receiveDeliverList = receiveOrder.getReceiveDeliverList();
                int onShelvesAmount = receiveDeliverList.stream().mapToInt(ReceiveOrderForScmVo.ReceiveDeliver::getOnShelvesAmount).sum();
                record.setOnShelvesAmount(onShelvesAmount);
            }
        }
        return pageResult;
    }

    /**
     * 处理加工次品的业务逻辑。
     * 通过分布式锁保证同一加工单号的处理操作的互斥性，避免并发问题。
     * 对加工单的状态和次品处理方式进行校验，确保业务规则的正确性。
     * 根据不同的次品处理方法，执行相应的处理流程，包括报废、返工和退回供应商。
     *
     * @param dto 包含次品处理相关信息的DTO，包括加工单号、次品数量、次品处理方法等。
     * @throws Exception 如果业务规则校验失败或处理过程中发生错误，则抛出异常。
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.PROC_DEFECTIVE_HANDLE, key = "#dto.processOrderNo",
            waitTime = 1, leaseTime = -1, exceptionDesc = "加工单正在处理中，请稍后再试。")
    public void defectiveHandle(ProcessOrderDefectiveHandleDto dto) {
        // 校验加工单是否存在
        final String processOrderNo = dto.getProcessOrderNo();
        // 加工单信息 & 状态校验
        ProcessOrderPo processOrderPo = ParamValidUtils.requireNotNull(
                processOrderDao.getByProcessOrderNo(processOrderNo),
                StrUtil.format("加工单{}不存在", processOrderNo)
        );
        // 校验加工单状态是否允许处理次品
        Set<ProcessOrderStatus> validStatuses = EnumSet.of(
                ProcessOrderStatus.WAIT_DELIVERY,
                ProcessOrderStatus.WAIT_RECEIPT,
                ProcessOrderStatus.WAIT_STORE,
                ProcessOrderStatus.STORED
        );
        if (!validStatuses.contains(processOrderPo.getProcessOrderStatus())) {
            throw new ParamIllegalException("{}才能处理次品",
                    validStatuses.stream().map(ProcessOrderStatus::getRemark).collect(Collectors.joining("/")));
        }

        // 获取总次品数量
        List<ProcessOrderItemPo> processOrderItemPoList = processOrderItemDao.getByProcessOrderNo(processOrderNo);
        ProcessOrderItemPo processOrderItemPo = processOrderItemPoList.stream()
                .filter(item -> BooleanType.TRUE.equals(item.getIsFirst()))
                .findFirst().orElse(null);
        if (Objects.isNull(processOrderItemPo)) {
            throw new ParamIllegalException("加工成品不存在");
        }
        int totalDefectiveGoodsCnt = processOrderItemPo.getDefectiveGoodsCnt();

        // 计算已处理的次品数量
        int createdDefectiveGoodsCnt = 0;
        List<ProcessDefectiveRecordPo> processDefectiveRecordPoList = processDefectiveRecordDao.getByProcessOrderNo(processOrderNo);
        if (CollectionUtils.isNotEmpty(processDefectiveRecordPoList)) {
            List<String> pdrNoList = processDefectiveRecordPoList.stream().map(ProcessDefectiveRecordPo::getProcessDefectiveRecordNo).collect(Collectors.toList());
            List<ProcessDefectiveRecordItemPo> recordItemPoList
                    = processDefectiveRecordItemDao.getByProcessDefectiveRecordNos(pdrNoList);
            createdDefectiveGoodsCnt = recordItemPoList.stream().mapToInt(ProcessDefectiveRecordItemPo::getDefectiveGoodsCnt).sum();
        }

        // 校验本次处理的次品数量是否超过剩余可处理的次品数量
        // 可处理次品数校验：本次处理次品数<=（总次品数-已处理次品数）
        int availableDefectiveGoodsCnt = totalDefectiveGoodsCnt - createdDefectiveGoodsCnt;
        Integer curDefectiveGoodsCnt = dto.getDefectiveGoodsCnt();
        if (curDefectiveGoodsCnt > availableDefectiveGoodsCnt) {
            throw new ParamIllegalException("最大可处理的次品数:{}", availableDefectiveGoodsCnt);
        }

        // 根据次品处理方法执行相应的处理逻辑
        // 处理次品报废流程
        DefectiveHandleMethod defectiveHandleMethod = dto.getDefectiveHandleMethod();
        List<ProcessOrderMaterialPo> processOrderMaterialPoList = processOrderMaterialDao.getByProcessOrderNo(processOrderNo);
        if (DefectiveHandleMethod.DEFECTIVE_SCRAP.equals(defectiveHandleMethod)) {
            // 次品报废
            this.defectiveHandleByScrap(dto, processOrderPo, processOrderItemPo, processOrderMaterialPoList);
        } else if (DefectiveHandleMethod.DEFECTIVE_REWORK.equals(defectiveHandleMethod)) {
            // 次品返工
            defectiveHandleByRework(dto, processOrderPo, processOrderItemPo);
        } else if (DefectiveHandleMethod.DEFECTIVE_RETURN_SUPPLIER.equals(defectiveHandleMethod)) {
            // 加工次品
            this.defectiveHandleBySupplier(dto, processOrderPo, processOrderItemPo, processOrderMaterialPoList);
        } else {
            throw new ParamIllegalException("次品处理选择错误");
        }
    }

    /**
     * 处理次品报废流程
     *
     * @param dto
     * @return
     */
    public Boolean defectiveHandleByScrap(ProcessOrderDefectiveHandleDto dto,
                                          ProcessOrderPo processOrderPo,
                                          ProcessOrderItemPo processOrderItemPo,
                                          List<ProcessOrderMaterialPo> processOrderMaterialPoList) {
        DefectiveHandleType defectiveHandleType = DefectiveHandleType.PROCESS_PRODUCT;
        if (null == defectiveHandleType) {
            throw new ParamIllegalException("必须选择处理类型");
        }
        if (StringUtils.isBlank(dto.getBadReason())) {
            throw new ParamIllegalException("请输入不良原因");
        }
        if (StringUtils.isBlank(dto.getPrincipalUser())) {
            throw new ParamIllegalException("请选择负责人");
        }
        if (StringUtils.isBlank(dto.getPrincipalUsername())) {
            throw new ParamIllegalException("请选择负责人");
        }

        ProcessDefectiveRecordPo processDefectiveRecordPo = new ProcessDefectiveRecordPo();
        ProcessDefectiveRecordItemPo processDefectiveRecordItemPo = new ProcessDefectiveRecordItemPo();
        final String processDefectiveRecordNo = idGenerateService.getConfuseCode(ScmConstant.PROCESS_DEFECTIVE_RECORD_NO_PREFIX, TimeType.CN_DAY, ConfuseLength.L_4);
        processDefectiveRecordPo.setProcessDefectiveRecordNo(processDefectiveRecordNo);
        processDefectiveRecordPo.setProcessOrderNo(processOrderPo.getProcessOrderNo());
        processDefectiveRecordPo.setDefectiveHandleMethod(DefectiveHandleMethod.DEFECTIVE_SCRAP);

        String skuBatchCode = dto.getSkuBatchCode();
        String sku = "";
        if (DefectiveHandleType.PROCESS_PRODUCT.equals(defectiveHandleType)) {
            Assert.isTrue(skuBatchCode.equals(processOrderItemPo.getSkuBatchCode()), () -> new BizException("批次码错误"));
            sku = processOrderItemPo.getSku();
        } else if (DefectiveHandleType.PROCESS_MATERIAL.equals(defectiveHandleType)) {
            Optional<ProcessOrderMaterialPo> firstMaterialOptional = processOrderMaterialPoList.stream()
                    .filter(item -> skuBatchCode.equals(item.getSkuBatchCode()))
                    .findFirst();
            Assert.isTrue(firstMaterialOptional.isPresent(), () -> new BizException("批次码错误"));
            sku = firstMaterialOptional.get().getSku();
        } else {
            throw new ParamIllegalException("次品处理类型错误");
        }
        processDefectiveRecordPo.setDefectiveHandleType(dto.getDefectiveHandleType());
        processDefectiveRecordPo.setProcessDefectiveRecordStatus(ProcessDefectiveRecordStatus.WAIT_HANDLE);
        processDefectiveRecordPo.setPrincipalUser(dto.getPrincipalUser());
        processDefectiveRecordPo.setPrincipalUsername(dto.getPrincipalUsername());
        processDefectiveRecordDao.insert(processDefectiveRecordPo);

        // 详情
        processDefectiveRecordItemPo.setProcessDefectiveRecordNo(processDefectiveRecordNo);
        processDefectiveRecordItemPo.setSku(sku);
        processDefectiveRecordItemPo.setSkuBatchCode(skuBatchCode);
        processDefectiveRecordItemPo.setDefectiveGoodsCnt(dto.getDefectiveGoodsCnt());
        processDefectiveRecordItemPo.setBadReason(dto.getBadReason());
        processDefectiveRecordItemDao.insert(processDefectiveRecordItemPo);

        // 有上传图片
        List<String> fileCodeList = dto.getFileCodeList();
        if (CollectionUtils.isNotEmpty(fileCodeList)) {
            scmImageBaseService.insertBatchImage(fileCodeList, ImageBizType.PROCESS_DEFECTIVE_RECORD, processDefectiveRecordItemPo.getProcessDefectiveRecordItemId());
        }

        // 创建次品入库单
        String operatorUser = GlobalContext.getUserKey();
        String operatorUsername = GlobalContext.getUsername();

        if (StringUtils.isBlank(operatorUser)) {
            operatorUser = ScmConstant.SYSTEM_USER;
        }
        if (StringUtils.isBlank(operatorUsername)) {
            operatorUsername = ScmConstant.SYSTEM_USER;
        }

        String spu = plmRemoteService.getSpuBySku(sku);

        ReceiveOrderCreateMqDto receiveOrderCreateMqDto = new ReceiveOrderCreateMqDto();
        ArrayList<ReceiveOrderCreateMqDto.ReceiveOrderCreateItem> receiveOrderCreateItems = new ArrayList<>();
        ArrayList<String> receiveOrderList = new ArrayList<>();
        receiveOrderList.add(ScmConstant.DEFECTIVE_GOODS);

        String finalOperatorUser = operatorUser;
        String finalOperatorUsername = operatorUsername;
        String processOrderNo = processOrderPo.getProcessOrderNo();
        String platform = ParamValidUtils.requireNotBlank(processOrderPo.getPlatform(),
                StrUtil.format("次品报废失败！加工单{}平台编码为空。", processOrderNo)
        );
        receiveOrderList.forEach(item -> {
            ReceiveOrderCreateMqDto.ReceiveOrderCreateItem receiveOrderCreateItem = new ReceiveOrderCreateMqDto.ReceiveOrderCreateItem();
            receiveOrderCreateItem.setReceiveType(ReceiveType.DEFECTIVE_PROCESS_PRODUCT);
            receiveOrderCreateItem.setSkuDevType(SkuDevType.NORMAL);
            // limited 类型的成品入库需要标记
            if (ProcessOrderType.LIMITED.equals(processOrderPo.getProcessOrderType()) ||
                    ProcessOrderType.LIMITED_REWORKING.equals(processOrderPo.getProcessOrderType())) {
                receiveOrderCreateItem.setSkuDevType(SkuDevType.LIMITED);
            }
            receiveOrderCreateItem.setQcType(WmsEnum.QcType.NOT_CHECK);
            receiveOrderCreateItem.setScmBizReceiveOrderType(ScmBizReceiveOrderType.DEFECTIVE_PROCESS);
            receiveOrderCreateItem.setScmBizNo(processOrderPo.getProcessOrderNo());
            receiveOrderCreateItem.setWarehouseCode(this.defectiveWarehouseCode);
            receiveOrderCreateItem.setPurchaseOrderType(PurchaseOrderType.NORMAL);
            receiveOrderCreateItem.setIsUrgentOrder(BooleanType.FALSE);
            receiveOrderCreateItem.setIsDirectSend(BooleanType.FALSE);
            receiveOrderCreateItem.setIsNormalOrder(BooleanType.FALSE);
            receiveOrderCreateItem.setPlaceOrderTime(new DateTime().toLocalDateTime());
            receiveOrderCreateItem.setSendTime(new DateTime().toLocalDateTime());
            receiveOrderCreateItem.setOperator(finalOperatorUser);
            receiveOrderCreateItem.setOperatorName(finalOperatorUsername);
            receiveOrderCreateItem.setUnionKey(processDefectiveRecordNo + ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK + ScmBizReceiveOrderType.DEFECTIVE_PROCESS.name());

            List<ReceiveOrderCreateMqDto.ReceiveOrderDetail> receiveOrderDetails = new ArrayList<>();

            ReceiveOrderCreateMqDto.ReceiveOrderDetail receiveOrderDetail = new ReceiveOrderCreateMqDto.ReceiveOrderDetail();
            receiveOrderDetail.setPlatCode(platform);
            receiveOrderDetail.setBatchCode(skuBatchCode);
            receiveOrderDetail.setSpu(spu);
            receiveOrderDetail.setSkuCode(processDefectiveRecordItemPo.getSku());
            receiveOrderDetail.setPurchaseAmount(processDefectiveRecordItemPo.getDefectiveGoodsCnt());
            receiveOrderDetail.setDeliveryAmount(processDefectiveRecordItemPo.getDefectiveGoodsCnt());
            receiveOrderDetails.add(receiveOrderDetail);
            receiveOrderCreateItem.setDetailList(receiveOrderDetails);
            receiveOrderCreateItems.add(receiveOrderCreateItem);
        });

        receiveOrderCreateMqDto.setReceiveOrderCreateItemList(receiveOrderCreateItems);
        receiveOrderCreateMqDto.setKey(idGenerateService.getSnowflakeCode(processOrderPo.getProcessOrderNo() + "-"));
        consistencySendMqService.execSendMq(WmsReceiptHandler.class, receiveOrderCreateMqDto);
        return true;
    }


    public void defectiveHandleByRework(ProcessOrderDefectiveHandleDto dto, ProcessOrderPo processOrderPo, ProcessOrderItemPo processOrderItemPo) {
        //收货仓库编码
        ParamValidUtils.requireNotBlank(dto.getWarehouseCode(), "请选择收货仓库");
        //收货仓库名称
        ParamValidUtils.requireNotBlank(dto.getWarehouseName(), "请选择收货仓库");
        //加工备注
        ParamValidUtils.requireNotBlank(dto.getProcessOrderNote(), "请输入加工备注");
        //产品质量
        ParamValidUtils.requireNotNull(dto.getProductQuality(), "请选择产品质量");
        //请选择业务约定交期
        ParamValidUtils.requireNotNull(dto.getDeliverDate(), "请选择业务约定交期");

        ParamValidUtils.requireNotEmpty(dto.getProcessOrderProcedures(), "请填写加工工序");

        if (dto.getDeliverDate().isBefore(DateUtil.beginOfDay(new DateTime()).toLocalDateTime())) {
            throw new ParamIllegalException("业务约定交接日期不允许输入过去日期");
        }
        if (!Objects.equals(dto.getSkuBatchCode(), processOrderItemPo.getSkuBatchCode())) {
            throw new ParamIllegalException("次品返工处理失败！批次码与原加工单成品批次码不一致。");
        }

        // 创建返工类型加工单
        ProcessOrderReWorkingDto processOrderReWorkingDto
                = ProcessOrderBuilder.buildProcessOrderReWorkingDto(dto, processOrderPo);
        ProcessOrderPo reWorkingProcessOrder = createReWorkingProcessOrder(processOrderReWorkingDto);

        final String processDefectiveRecordNo
                = idGenerateService.getConfuseCode(ScmConstant.PROCESS_DEFECTIVE_RECORD_NO_PREFIX, TimeType.CN_DAY, ConfuseLength.L_4);

        // 创建次品记录
        ProcessDefectiveRecordPo processDefectiveRecordPo = new ProcessDefectiveRecordPo();
        processDefectiveRecordPo.setProcessDefectiveRecordNo(processDefectiveRecordNo);
        processDefectiveRecordPo.setProcessOrderNo(processOrderPo.getProcessOrderNo());
        processDefectiveRecordPo.setDefectiveHandleMethod(DefectiveHandleMethod.DEFECTIVE_REWORK);
        processDefectiveRecordPo.setDefectiveHandleType(DefectiveHandleType.PROCESS_PRODUCT);
        processDefectiveRecordPo.setProcessDefectiveRecordStatus(ProcessDefectiveRecordStatus.WAIT_HANDLE);
        processDefectiveRecordPo.setRelatedOrderNo(reWorkingProcessOrder.getProcessOrderNo());
        processDefectiveRecordDao.insert(processDefectiveRecordPo);

        // 创建次品记录明细
        ProcessDefectiveRecordItemPo processDefectiveRecordItemPo = new ProcessDefectiveRecordItemPo();
        processDefectiveRecordItemPo.setProcessDefectiveRecordNo(processDefectiveRecordNo);

        String skuBatchCode = dto.getSkuBatchCode();
        processDefectiveRecordItemPo.setSkuBatchCode(skuBatchCode);

        String sku = processOrderItemPo.getSku();
        processDefectiveRecordItemPo.setSku(sku);

        processDefectiveRecordItemPo.setDefectiveGoodsCnt(dto.getDefectiveGoodsCnt());
        processDefectiveRecordItemDao.insert(processDefectiveRecordItemPo);
    }

    /**
     * 处理次品退供流程
     *
     * @param dto
     * @return
     */
    public Boolean defectiveHandleBySupplier(ProcessOrderDefectiveHandleDto dto,
                                             ProcessOrderPo processOrderPo,
                                             ProcessOrderItemPo processOrderItemPo,
                                             List<ProcessOrderMaterialPo> processOrderMaterialPoList) {
        // 只有加工成品
        DefectiveHandleType defectiveHandleType = DefectiveHandleType.PROCESS_PRODUCT;


        if (StringUtils.isBlank(dto.getBadReason())) {
            throw new ParamIllegalException("请输入不良原因");
        }

        if (StringUtils.isBlank(dto.getSupplierUser())) {
            throw new ParamIllegalException("供应商必传");
        }
        if (StringUtils.isBlank(dto.getSupplierUsername())) {
            throw new ParamIllegalException("供应商必传");
        }


        ProcessDefectiveRecordPo processDefectiveRecordPo = new ProcessDefectiveRecordPo();
        ProcessDefectiveRecordItemPo processDefectiveRecordItemPo = new ProcessDefectiveRecordItemPo();
        final String processDefectiveRecordNo
                = idGenerateService.getConfuseCode(ScmConstant.PROCESS_DEFECTIVE_RECORD_NO_PREFIX, TimeType.CN_DAY,
                ConfuseLength.L_4);
        processDefectiveRecordPo.setProcessDefectiveRecordNo(processDefectiveRecordNo);
        processDefectiveRecordPo.setProcessOrderNo(processOrderPo.getProcessOrderNo());
        processDefectiveRecordPo.setDefectiveHandleMethod(DefectiveHandleMethod.DEFECTIVE_RETURN_SUPPLIER);

        String skuBatchCode = dto.getSkuBatchCode();
        String sku = "";
        if (DefectiveHandleType.PROCESS_PRODUCT.equals(defectiveHandleType)) {
            Assert.isTrue(skuBatchCode.equals(processOrderItemPo.getSkuBatchCode()),
                    () -> new BizException("批次码错误"));
            sku = processOrderItemPo.getSku();
        } else if (DefectiveHandleType.PROCESS_MATERIAL.equals(defectiveHandleType)) {
            Optional<ProcessOrderMaterialPo> firstMaterialOptional = processOrderMaterialPoList.stream()
                    .filter(item -> skuBatchCode.equals(item.getSkuBatchCode()))
                    .findFirst();
            Assert.isTrue(firstMaterialOptional.isPresent(), () -> new BizException("批次码错误"));
            sku = firstMaterialOptional.get()
                    .getSku();
        } else {
            throw new ParamIllegalException("次品处理类型错误");
        }
        ProcessOrderExtraPo processOrderExtraPo
                = processOrderExtraDao.getByProcessOrderNo(processOrderPo.getProcessOrderNo());
        String receiptOrderNo = processOrderExtraPo.getReceiptOrderNo();
        String storeOrderNo = processOrderExtraPo.getStoreOrderNo();
        receiptOrderNo = StringUtils.isNotBlank(receiptOrderNo) ? receiptOrderNo : storeOrderNo;

        List<String> fileCodeList = dto.getFileCodeList();

        // 生成加工次品记录
        ArrayList<DefectHandlingCreateBo> defectHandlingCreateBoList = new ArrayList<>();
        DefectHandlingCreateBo defectHandlingCreateBo = new DefectHandlingCreateBo();
        defectHandlingCreateBo.setDefectHandlingType(DefectHandlingType.PROCESS_DEFECT);
        defectHandlingCreateBo.setDefectBizNo(processOrderPo.getProcessOrderNo());
        defectHandlingCreateBo.setSupplierCode(dto.getSupplierUser());
        defectHandlingCreateBo.setSupplierName(dto.getSupplierUsername());
        defectHandlingCreateBo.setSku(sku);
        defectHandlingCreateBo.setSkuBatchCode(skuBatchCode);
        defectHandlingCreateBo.setQcCnt(dto.getDefectiveGoodsCnt());
        defectHandlingCreateBo.setPassCnt(0);
        defectHandlingCreateBo.setNotPassCnt(dto.getDefectiveGoodsCnt());
        defectHandlingCreateBo.setAdverseReason(dto.getBadReason());

        defectHandlingCreateBo.setReceiveOrderNo(receiptOrderNo);
        defectHandlingCreateBo.setQcOrderNo(processOrderExtraPo.getCheckOrderNo());
        defectHandlingCreateBo.setFileCodeList(fileCodeList);
        defectHandlingCreateBo.setDefectCreateUser(GlobalContext.getUserKey());
        defectHandlingCreateBo.setDefectCreateUsername(GlobalContext.getUsername());
        defectHandlingCreateBo.setFileCodeList(fileCodeList);
        defectHandlingCreateBo.setWarehouseCode(processOrderPo.getWarehouseCode());
        defectHandlingCreateBo.setPlatform(processOrderPo.getPlatform());

        // 获取批次码的价格
        Map<String, BigDecimal> skuBatchCodePriceMap = wmsRemoteService.getSkuBatchPriceMapBySkuBatchList(List.of(skuBatchCode));
        if (!skuBatchCodePriceMap.containsKey(skuBatchCode)) {
            throw new BizException("批次码{}获取不到对应价格，请联系管理员!", skuBatchCode);
        }
        if (skuBatchCodePriceMap.get(skuBatchCode).compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException("批次码:{}的价格小于等于0，请联系管理员进行调整批次码价格", skuBatchCode);
        }
        defectHandlingCreateBo.setSettlePrice(skuBatchCodePriceMap.get(skuBatchCode));

        defectHandlingCreateBoList.add(defectHandlingCreateBo);

        List<DefectHandlingPo> defectHandling = defectRefService.createDefectHandling(defectHandlingCreateBoList);
        if (CollectionUtils.isNotEmpty(defectHandling)) {
            Optional<DefectHandlingPo> firstDefectHandlingPoOptional = defectHandling.stream()
                    .findFirst();
            DefectHandlingPo defectHandlingPo = firstDefectHandlingPoOptional.get();
            processDefectiveRecordPo.setRelatedOrderNo(defectHandlingPo.getDefectHandlingNo());
        }


        processDefectiveRecordPo.setDefectiveHandleType(dto.getDefectiveHandleType());
        processDefectiveRecordPo.setProcessDefectiveRecordStatus(ProcessDefectiveRecordStatus.WAIT_HANDLE);
        processDefectiveRecordPo.setPrincipalUser(dto.getPrincipalUser());
        processDefectiveRecordPo.setPrincipalUsername(dto.getPrincipalUsername());
        processDefectiveRecordDao.insert(processDefectiveRecordPo);

        // 详情
        processDefectiveRecordItemPo.setProcessDefectiveRecordNo(processDefectiveRecordNo);
        processDefectiveRecordItemPo.setSku(sku);
        processDefectiveRecordItemPo.setSkuBatchCode(skuBatchCode);
        processDefectiveRecordItemPo.setDefectiveGoodsCnt(dto.getDefectiveGoodsCnt());
        processDefectiveRecordItemPo.setBadReason(dto.getBadReason());
        processDefectiveRecordItemDao.insert(processDefectiveRecordItemPo);

        // 有上传图片

        if (CollectionUtils.isNotEmpty(fileCodeList)) {
            scmImageBaseService.insertBatchImage(fileCodeList, ImageBizType.PROCESS_DEFECTIVE_RECORD,
                    processDefectiveRecordItemPo.getProcessDefectiveRecordItemId());
        }

        return true;
    }

    /**
     * 查询返工记录
     *
     * @param dto
     * @return
     */
    public ProcessDefectiveRecordByNoVo defectiveRecord(ProcessOrderNoDto dto) {
        String processOrderNo = dto.getProcessOrderNo();
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (null == processOrderPo) {
            throw new BizException("加工单不存在");
        }

        List<ProcessOrderItemPo> processOrderItemPoList = processOrderItemDao.getByProcessOrderNo(processOrderNo);

        Optional<ProcessOrderItemPo> firstItemPoOptional = processOrderItemPoList.stream()
                .filter(item -> BooleanType.TRUE.equals(item.getIsFirst()))
                .findFirst();
        if (firstItemPoOptional.isEmpty()) {
            throw new BizException("加工 sku 不存在");
        }
        ProcessOrderItemPo processOrderItemPo = firstItemPoOptional.get();
        // 次品数
        Integer defectiveGoodsCnt = processOrderItemPo.getDefectiveGoodsCnt();
        Integer defectiveGoodsCntByRecord = 0;

        // 查询所有的次品处理记录
        List<ProcessDefectiveRecordVo> processDefectiveRecordVoList = new ArrayList<>();
        List<ProcessDefectiveRecordPo> processDefectiveRecordPoList
                = processDefectiveRecordDao.getByProcessOrderNo(processOrderNo);
        if (CollectionUtils.isNotEmpty(processDefectiveRecordPoList)) {
            List<String> processDefectiveRecordNoList = processDefectiveRecordPoList.stream()
                    .map(ProcessDefectiveRecordPo::getProcessDefectiveRecordNo)
                    .collect(Collectors.toList());

            // 查询 sku 信息
            List<ProcessDefectiveRecordItemPo> recordItemPoList
                    = processDefectiveRecordItemDao.getByProcessDefectiveRecordNos(processDefectiveRecordNoList);
            defectiveGoodsCntByRecord = recordItemPoList.stream()
                    .mapToInt(ProcessDefectiveRecordItemPo::getDefectiveGoodsCnt)
                    .sum();

            List<Long> recordItemIdList = recordItemPoList.stream()
                    .map(ProcessDefectiveRecordItemPo::getProcessDefectiveRecordItemId)
                    .collect(Collectors.toList());
            // 查询图片信息
            Map<Long, List<String>> groupedImages
                    = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.PROCESS_DEFECTIVE_RECORD,
                    recordItemIdList);

            // 获取变体属性
            List<String> skuList = recordItemPoList.stream()
                    .map(ProcessDefectiveRecordItemPo::getSku)
                    .distinct()
                    .collect(Collectors.toList());
            List<PlmVariantVo> variantAttr = plmRemoteService.getVariantAttr(skuList);
            Map<String, PlmVariantVo> groupedPlmVariant = variantAttr.stream()
                    .collect(Collectors.toMap(PlmVariantVo::getSkuCode, item -> item,
                            (existingItem, newItem) -> existingItem));

            Map<String, ProcessDefectiveRecordItemPo> groupedRecordItems = recordItemPoList.stream()
                    .collect(Collectors.toMap(ProcessDefectiveRecordItemPo::getProcessDefectiveRecordNo, item -> item,
                            (existingItem, newItem) -> existingItem));


            // 查询关联单据状态
            List<String> relatedOrderNoList = processDefectiveRecordPoList.stream()
                    .map(ProcessDefectiveRecordPo::getRelatedOrderNo)
                    .collect(Collectors.toList());

            List<ReceiveOrderForScmVo> relatedReceiveOrderList = new ArrayList<>();
            List<ProcessOrderPo> relatedProcessOrderList = new ArrayList<>();
            List<DefectHandlingPo> relatedDefectHandlingList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(relatedOrderNoList)) {
                // 关联的收货单
                ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
                receiveOrderGetDto.setReceiveOrderNoList(relatedOrderNoList);
                relatedReceiveOrderList = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto);

                // 关联的返工单
                relatedProcessOrderList = processOrderDao.getByProcessOrderNos(relatedOrderNoList);

                // 关联的次品记录
                relatedDefectHandlingList = defectHandlingDao.getByDefectHandlingNos(relatedOrderNoList);


            }
            Map<String, ReceiveOrderForScmVo> groupedReceiveOrder = relatedReceiveOrderList.stream()
                    .collect(Collectors.toMap(ReceiveOrderForScmVo::getReceiveOrderNo, item -> item,
                            (existingItem, newItem) -> existingItem));

            Map<String, ProcessOrderPo> groupedProcessOrder = relatedProcessOrderList.stream()
                    .collect(Collectors.toMap(ProcessOrderPo::getProcessOrderNo, item -> item,
                            (existingItem, newItem) -> existingItem));

            Map<String, DefectHandlingPo> groupedDefectHandling = relatedDefectHandlingList.stream()
                    .collect(Collectors.toMap(DefectHandlingPo::getDefectHandlingNo, item -> item,
                            (existingItem, newItem) -> existingItem));


            processDefectiveRecordVoList = processDefectiveRecordPoList.stream()
                    .map(item -> {
                        ProcessDefectiveRecordVo processDefectiveRecordVo = new ProcessDefectiveRecordVo();
                        processDefectiveRecordVo.setProcessDefectiveRecordNo(item.getProcessDefectiveRecordNo());
                        processDefectiveRecordVo.setDefectiveHandleMethod(item.getDefectiveHandleMethod());
                        processDefectiveRecordVo.setDefectiveHandleType(item.getDefectiveHandleType());
                        processDefectiveRecordVo.setProcessDefectiveRecordStatus(
                                item.getProcessDefectiveRecordStatus());
                        processDefectiveRecordVo.setPrincipalUser(item.getPrincipalUser());
                        processDefectiveRecordVo.setPrincipalUsername(item.getPrincipalUsername());
                        processDefectiveRecordVo.setSupplierUser(item.getSupplierUser());
                        processDefectiveRecordVo.setSupplierUsername(item.getSupplierUsername());
                        processDefectiveRecordVo.setCreateTime(item.getCreateTime());

                        // 通过详情拿数据
                        ProcessDefectiveRecordItemPo processDefectiveRecordItemPo
                                = groupedRecordItems.get(item.getProcessDefectiveRecordNo());
                        processDefectiveRecordVo.setSku(processDefectiveRecordItemPo.getSku());
                        processDefectiveRecordVo.setSkuBatchCode(processDefectiveRecordItemPo.getSkuBatchCode());
                        processDefectiveRecordVo.setDefectiveGoodsCnt(
                                processDefectiveRecordItemPo.getDefectiveGoodsCnt());
                        processDefectiveRecordVo.setBadReason(processDefectiveRecordItemPo.getBadReason());
                        PlmVariantVo plmVariantVo = groupedPlmVariant.get(processDefectiveRecordItemPo.getSku());
                        if (null != plmVariantVo) {
                            processDefectiveRecordVo.setVariantSkuList(plmVariantVo.getVariantSkuList());
                        }


                        // 图片
                        List<String> fileCodeList
                                = groupedImages.get(processDefectiveRecordItemPo.getProcessDefectiveRecordItemId());
                        processDefectiveRecordVo.setFileCodeList(fileCodeList);

                        // 设置单据以及单据状态信息
                        processDefectiveRecordVo.setRelatedOrderNo(item.getRelatedOrderNo());
                        ReceiveOrderForScmVo receiveOrderForScmVo = groupedReceiveOrder.get(item.getRelatedOrderNo());
                        ProcessOrderPo reworkingProcessOrderPo = groupedProcessOrder.get(item.getRelatedOrderNo());
                        DefectHandlingPo defectHandlingPo = groupedDefectHandling.get(item.getRelatedOrderNo());
                        if (null != reworkingProcessOrderPo) {
                            processDefectiveRecordVo.setProcessDefectiveRecordOrderType(
                                    ProcessDefectiveRecordOrderType.PROCESS_ORDER);
                            processDefectiveRecordVo.setRelatedOrderStatus(
                                    reworkingProcessOrderPo.getProcessOrderStatus()
                                            .getDesc());
                            processDefectiveRecordVo.setPrintOrderNo(item.getRelatedOrderNo());
                        }
                        if (null != receiveOrderForScmVo) {
                            processDefectiveRecordVo.setProcessDefectiveRecordOrderType(
                                    ProcessDefectiveRecordOrderType.RECEIVE_ORDER);
                            processDefectiveRecordVo.setRelatedOrderStatus(receiveOrderForScmVo.getReceiveOrderState()
                                    .getRemark());
                            processDefectiveRecordVo.setPrintOrderNo(item.getRelatedOrderNo());
                        }
                        if (null != defectHandlingPo) {
                            if (DefectHandlingProgramme.RETURN_SUPPLY.equals(
                                    defectHandlingPo.getDefectHandlingProgramme())) {
                                processDefectiveRecordVo.setProcessDefectiveRecordOrderType(
                                        ProcessDefectiveRecordOrderType.RETURN_ORDER);
                            } else {
                                processDefectiveRecordVo.setProcessDefectiveRecordOrderType(
                                        ProcessDefectiveRecordOrderType.RECEIVE_ORDER);
                            }
                            processDefectiveRecordVo.setRelatedOrderStatus(defectHandlingPo.getDefectHandlingStatus()
                                    .getRemark());
                            processDefectiveRecordVo.setPrintOrderNo(defectHandlingPo.getRelatedOrderNo());
                        }
                        return processDefectiveRecordVo;
                    })
                    .collect(Collectors.toList());

        }

        int availableDefectiveGoodsCnt = defectiveGoodsCnt - defectiveGoodsCntByRecord;
        ProcessDefectiveRecordByNoVo processDefectiveRecordByNoVo = new ProcessDefectiveRecordByNoVo();
        processDefectiveRecordByNoVo.setProcessOrderNo(processOrderNo);
        processDefectiveRecordByNoVo.setDefectiveGoodsCnt(defectiveGoodsCnt);
        processDefectiveRecordByNoVo.setAvailableDefectiveGoodsCnt(availableDefectiveGoodsCnt);
        processDefectiveRecordByNoVo.setProcessDefectiveRecordVoList(processDefectiveRecordVoList);

        return processDefectiveRecordByNoVo;
    }

    /**
     * 获取加工详情
     *
     * @param dto
     * @return
     */
    public ProcessOrderDetailVo detail(ProcessOrderDetailDto dto) {
        Long processOrderId = dto.getProcessOrderId();
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderId(processOrderId);
        if (processOrderPo == null) {
            throw new BizException("加工单不存在");
        }
        return this.poToDetailVo(processOrderPo);
    }

    /**
     * 获取加工详情（通过编号）
     *
     * @param dto
     * @return
     */
    public ProcessOrderDetailVo detailByNo(ProcessOrderDetailByNoDto dto) {
        String processOrderNo = dto.getProcessOrderNo();
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (processOrderPo == null) {
            throw new ParamIllegalException("加工单不存在，当前单号：{}，请确认正确的加工单号：JG********", processOrderNo);
        }
        ProcessOrderDetailVo processOrderDetailVo = this.poToDetailVo(processOrderPo);
        if (BooleanType.TRUE.equals(dto.getNeedSpuCategoryList()) && StringUtils.isNotBlank(processOrderPo.getSpu())) {
            ArrayList<String> spuList = new ArrayList<>();
            spuList.add(processOrderPo.getSpu());
            List<PlmGoodsDetailVo> categoriesBySpu = plmRemoteService.getCategoriesBySpu(spuList);
            Optional<PlmGoodsDetailVo> firstPlmGoodsDetailVoOptional = categoriesBySpu.stream()
                    .findFirst();
            firstPlmGoodsDetailVoOptional.ifPresent(
                    plmGoodsDetailVo -> processOrderDetailVo.setSpuCategoryList(plmGoodsDetailVo.getCategoryList()));
        }
        return processOrderDetailVo;
    }

    /**
     * po 转 detaiLVo
     *
     * @param processOrderPo
     * @return
     */
    public ProcessOrderDetailVo poToDetailVo(ProcessOrderPo processOrderPo) {
        ProcessOrderDetailVo processOrderDetailVo = ProcessOrderConverter.INSTANCE.convert(processOrderPo);
        processOrderDetailVo.setWarehouseTypeList(
                FormatStringUtil.string2List(processOrderPo.getWarehouseTypes(), ","));

        Set<MissingInformation> missingInformationList
                = MissInformationConverter.convertToMissingInformationEnums(processOrderPo.getMissingInformation());
        if (CollectionUtils.isNotEmpty(missingInformationList)) {
            Set<String> missingInformationRemarks = missingInformationList.stream()
                    .map(missingInformation -> MissingInformation.valueOf(missingInformation.name())
                            .getRemark())
                    .collect(Collectors.toSet());
            processOrderDetailVo.setMissingInformationRemarks(missingInformationRemarks);
        }
        if (StringUtils.isNotBlank(processOrderPo.getFileCode())) {
            processOrderDetailVo.setFileCodeList(Arrays.asList(processOrderPo.getFileCode()
                    .split(",")));
        }
        // 获取加工单附加信息
        ProcessOrderExtraPo processOrderExtraPo
                = processOrderExtraDao.getByProcessOrderNo(processOrderPo.getProcessOrderNo());
        if (processOrderExtraPo != null) {
            processOrderDetailVo.setReceiptOrderNo(processOrderExtraPo.getReceiptOrderNo());
            processOrderDetailVo.setReceiptUsername(processOrderExtraPo.getReceiptUsername());
            processOrderDetailVo.setStoreOrderNo(processOrderExtraPo.getStoreOrderNo());
            processOrderDetailVo.setStoreUsername(processOrderExtraPo.getStoreUsername());
            processOrderDetailVo.setSettleOrderNo(processOrderExtraPo.getSettleOrderNo());
            processOrderDetailVo.setSettleUsername(processOrderExtraPo.getSettleUsername());
            processOrderDetailVo.setSettleTime(processOrderExtraPo.getSettleTime());
            processOrderDetailVo.setProcessingTime(processOrderExtraPo.getProcessingTime());
            processOrderDetailVo.setProcessingUsername(processOrderExtraPo.getProcessingUsername());
            processOrderDetailVo.setCheckOrderNo(processOrderExtraPo.getCheckOrderNo());
            processOrderDetailVo.setCheckingTime(processOrderExtraPo.getCheckingTime());
            processOrderDetailVo.setCheckingUsername(processOrderExtraPo.getCheckingUsername());

        }


        // 获取加工明细
        List<ProcessOrderItemPo> processOrderItemPos
                = processOrderItemDao.getByProcessOrderNo(processOrderPo.getProcessOrderNo());
        if (CollectionUtils.isNotEmpty(processOrderItemPos)) {
            List<String> skuList = processOrderItemPos.stream()
                    .map(ProcessOrderItemPo::getSku)
                    .collect(Collectors.toList());
            List<PlmSkuVo> skuEncodeBySku = plmRemoteService.getSkuEncodeBySku(skuList);
            Map<String, List<PlmSkuVo>> groupedPlmSkuVo = skuEncodeBySku.stream()
                    .collect(Collectors.groupingBy(PlmSkuVo::getSkuCode));

            List<PlmGoodsDetailVo> skuCategoriesList
                    = Optional.ofNullable(plmRemoteService.getCategoriesBySku(skuList)).orElse(Collections.emptyList());

            List<ProcessOrderItemVo> processOrderItemVos = processOrderItemPos.stream()
                    .map(it -> {
                        ProcessOrderItemVo processOrderItemVo = ProcessOrderConverter.INSTANCE.convert(it);
                        List<PlmSkuVo> plmSkuVos = groupedPlmSkuVo.get(it.getSku());
                        if (CollectionUtils.isNotEmpty(plmSkuVos)) {
                            Optional<PlmSkuVo> firstPlmSkuVoOptional = plmSkuVos.stream().findFirst();
                            firstPlmSkuVoOptional.ifPresent(plmSkuVo -> processOrderItemVo.setSkuEncode(plmSkuVo.getSkuEncode()));
                        }

                        //匹配类目信息
                        skuCategoriesList.stream()
                                .filter(skuCategories -> CollectionUtils.isNotEmpty(skuCategories.getSkuCodeList()) && skuCategories.getSkuCodeList().contains(it.getSku()))
                                .findFirst().ifPresent(matchSku -> {
                                    //匹配sku类目列表信息
                                    List<PlmCategoryVo> categoryList = matchSku.getCategoryList();
                                    if (CollectionUtils.isNotEmpty(categoryList)) {
                                        //匹配最大类目id
                                        categoryList.stream().max(Comparator.comparing(PlmCategoryVo::getLevel)).ifPresent(plmCategoryVo -> {
                                            processOrderItemVo.setCategoryId(plmCategoryVo.getCategoryId());
                                            processOrderItemVo.setCategoryName(plmCategoryVo.getCategoryName());
                                        });
                                    }
                                });
                        return processOrderItemVo;
                    })
                    .collect(Collectors.toList());
            processOrderDetailVo.setProcessOrderItems(processOrderItemVos);
        }


        // 获取加工原料
        List<ProcessOrderMaterialPo> processOrderMaterialPos
                = processOrderMaterialDao.getByProcessOrderNo(processOrderPo.getProcessOrderNo());
        if (CollectionUtils.isNotEmpty(processOrderMaterialPos)) {
            List<ProcessOrderMaterialVo> processOrderMaterialVos = processOrderMaterialPos.stream()
                    .map(ProcessOrderConverter.INSTANCE::convert)
                    .collect(Collectors.toList());

            // 获取原料模板信息
            processOrderMaterialBaseService.updateMaterialSkuType(processOrderMaterialVos, processOrderMaterialPos);
            processOrderDetailVo.setProcessOrderMaterials(processOrderMaterialVos);
        }


        // 获取加工工序

        List<ProcessOrderProcedurePo> processOrderProcedurePos
                = processOrderProcedureDao.getByProcessOrderNo(processOrderPo.getProcessOrderNo());
        if (CollectionUtils.isNotEmpty(processOrderProcedurePos)) {
            List<Long> processIds = processOrderProcedurePos.stream()
                    .map(ProcessOrderProcedurePo::getProcessId)
                    .collect(Collectors.toList());
            List<ProcessPo> processPos = processDao.getByProcessIds(processIds);
            List<ProcessOrderProcedureVo> processOrderProcedureVos = processOrderProcedurePos.stream()
                    .map(it -> {
                        ProcessOrderProcedureVo processOrderProcedureVo = ProcessOrderConverter.INSTANCE.convert(it);
                        Optional<ProcessPo> first = processPos.stream()
                                .filter(item -> item.getProcessId()
                                        .equals(it.getProcessId()))
                                .findFirst();
                        first.ifPresent(
                                processPo -> processOrderProcedureVo.setProcessFirst(processPo.getProcessFirst()));
                        return processOrderProcedureVo;
                    })
                    .collect(Collectors.toList());
            processOrderDetailVo.setProcessOrderProcedures(processOrderProcedureVos);
        }


        // 处理加工工序进度
        // 获取加工单所有的扫码记录
        List<ProcessOrderProcedureByH5Vo> processOrderProcedureByH5Vos
                = processOrderBaseService.getProcessOrderScanByNo(processOrderPo.getProcessOrderNo());
        processOrderDetailVo.setProcessOrderProcess(processOrderProcedureByH5Vos);

        // 获取加工描述
        List<ProcessOrderDescPo> processOrderDescPos
                = processOrderDescDao.getByProcessOrderNo(processOrderPo.getProcessOrderNo());
        if (CollectionUtils.isNotEmpty(processOrderDescPos)) {
            List<ProcessOrderDescVo> processOrderDescVos = processOrderDescPos.stream()
                    .map(ProcessOrderConverter.INSTANCE::convert)
                    .collect(Collectors.toList());
            processOrderDetailVo.setProcessOrderDescs(processOrderDescVos);
        }


        return processOrderDetailVo;
    }


    /**
     * 组装打印数据
     *
     * @param processOrderPo
     * @param processOrderItemPos
     * @param processOrderMaterialPos
     * @param processOrderProcedurePos
     * @param processOrderDescPos
     * @return
     */
    public ProcessOrderPrintVo poToPrintVo(ProcessOrderPo processOrderPo,
                                           List<ProcessOrderItemPo> processOrderItemPos,
                                           List<ProcessOrderMaterialPo> processOrderMaterialPos,
                                           List<ProcessOrderProcedurePo> processOrderProcedurePos,
                                           List<ProcessOrderDescPo> processOrderDescPos,
                                           List<SampleInfoVo> sampleInfoBySkuList,
                                           List<PlmSkuImage> skuImages,
                                           List<ProcessPo> processPos,
                                           Map<String, List<PlmSkuVo>> groupedPlmSkuMap,
                                           ProcessDeliveryOrderVo processDeliveryOrderVo,
                                           List<ProcessOrderMaterialPo> processOrderMaterialPoDeliveryList,
                                           List<ProcessOrderMaterialComparePo> processOrderMaterialComparePoList) {

        processOrderItemPos = Optional.ofNullable(processOrderItemPos).orElse(Collections.emptyList());
        processOrderMaterialPos = Optional.ofNullable(processOrderMaterialPos).orElse(Collections.emptyList());
        processOrderProcedurePos = Optional.ofNullable(processOrderProcedurePos).orElse(Collections.emptyList());
        processOrderDescPos = Optional.ofNullable(processOrderDescPos).orElse(Collections.emptyList());
        sampleInfoBySkuList = Optional.ofNullable(sampleInfoBySkuList).orElse(Collections.emptyList());
        skuImages = Optional.ofNullable(skuImages).orElse(Collections.emptyList());
        processOrderMaterialComparePoList = Optional.ofNullable(processOrderMaterialComparePoList).orElse(Collections.emptyList());

        // 组装返回的数据
        ProcessOrderPrintVo processOrderPrintVo = new ProcessOrderPrintVo();
        processOrderPrintVo.setProcessOrderId(processOrderPo.getProcessOrderId());
        processOrderPrintVo.setProcessOrderNo(processOrderPo.getProcessOrderNo());
        processOrderPrintVo.setWarehouseCode(processOrderPo.getWarehouseCode());
        processOrderPrintVo.setWarehouseName(processOrderPo.getWarehouseName());
        processOrderPrintVo.setDeliveryWarehouseCode(processOrderPo.getDeliveryWarehouseCode());
        processOrderPrintVo.setDeliveryWarehouseName(processOrderPo.getDeliveryWarehouseName());
        processOrderPrintVo.setProcessOrderStatus(processOrderPo.getProcessOrderStatus());
        processOrderPrintVo.setProcessOrderNote(processOrderPo.getProcessOrderNote());
        processOrderPrintVo.setCreateUsername(processOrderPo.getCreateUsername());
        processOrderPrintVo.setCreateTime(processOrderPo.getCreateTime());
        processOrderPrintVo.setProcessOrderType(processOrderPo.getProcessOrderType());
        processOrderPrintVo.setDeliverDate(processOrderPo.getDeliverDate());
        processOrderPrintVo.setPrintTime(processOrderPo.getPrintTime());
        String images = processOrderPo.getFileCode();
        if (StringUtils.isNotBlank(images)) {
            processOrderPrintVo.setFileCodeList(Arrays.asList(images.split(",")));
        }

        // 获取WMS出库明细
        if (null != processDeliveryOrderVo) {
            processOrderPrintVo.setDeliveryNo(processDeliveryOrderVo.getDeliveryOrderNo());
            processOrderPrintVo.setPickOrderNo(processDeliveryOrderVo.getPickOrderNo());
            processOrderPrintVo.setRawWarehouseCode(processDeliveryOrderVo.getWarehouseCode());
            processOrderPrintVo.setRawWarehouseName(processDeliveryOrderVo.getWarehouseName());
            processOrderPrintVo.setPickingCartStackCodeList(processDeliveryOrderVo.getPickingCartStackCodeList());
        }


        // 获取加工明细
        List<SampleInfoVo> finalSampleInfoBySkuList = sampleInfoBySkuList;
        List<PlmSkuImage> finalSkuImages = skuImages;
        List<ProcessOrderItemPrintVo> processOrderItemVos = processOrderItemPos.stream()
                .map(it -> {
                    ProcessOrderItemPrintVo processOrderItemPrintVo = new ProcessOrderItemPrintVo();
                    processOrderItemPrintVo.setSampleChildOrderInfoList(new ArrayList<>());
                    if (CollectionUtils.isNotEmpty(finalSampleInfoBySkuList)) {
                        Optional<SampleInfoVo> first = finalSampleInfoBySkuList.stream()
                                .filter(item -> item.getSku()
                                        .equals(it.getSku()))
                                .findFirst();
                        if (first.isPresent()) {
                            SampleInfoVo sampleInfoVo = first.get();
                            processOrderItemPrintVo.setSampleChildOrderInfoList(
                                    sampleInfoVo.getSampleChildOrderInfoList());
                        }
                    }
                    if (CollectionUtils.isNotEmpty(finalSkuImages)) {
                        Optional<PlmSkuImage> first2 = finalSkuImages.stream()
                                .filter(item -> item.getSkuCode()
                                        .equals(it.getSku()))
                                .findFirst();
                        if (first2.isPresent()) {
                            PlmSkuImage plmSkuImage = first2.get();
                            processOrderItemPrintVo.setFileCodeList(plmSkuImage.getSaleFileCodeList());
                        }
                    }

                    List<PlmSkuVo> plmSkuVos = groupedPlmSkuMap.get(it.getSku());
                    if (CollectionUtils.isNotEmpty(plmSkuVos)) {
                        Optional<PlmSkuVo> firstPlmSkuVoOptional = plmSkuVos.stream()
                                .findFirst();
                        firstPlmSkuVoOptional.ifPresent(
                                plmSkuVo -> {
                                    processOrderItemPrintVo.setSkuEncode(plmSkuVo.getSkuEncode());

                                    String spu = plmSkuVo.getSpuCode();
                                    Map<String, String> skuTipMap = spuConfig.getSpuTipMap();
                                    if (CollectionUtils.isNotEmpty(skuTipMap)) {
                                        String spuTips = skuTipMap.getOrDefault(spu, "");
                                        processOrderItemPrintVo.setSpuTips(spuTips);
                                    }
                                });
                    }

                    processOrderItemPrintVo.setProcessOrderItemId(it.getProcessOrderItemId());
                    processOrderItemPrintVo.setSku(it.getSku());
                    processOrderItemPrintVo.setSkuBatchCode(it.getSkuBatchCode());
                    processOrderItemPrintVo.setVariantProperties(it.getVariantProperties());
                    processOrderItemPrintVo.setProcessNum(it.getProcessNum());
                    processOrderItemPrintVo.setPurchasePrice(it.getPurchasePrice());
                    processOrderItemPrintVo.setQualityGoodsCnt(it.getQualityGoodsCnt());
                    processOrderItemPrintVo.setDefectiveGoodsCnt(it.getDefectiveGoodsCnt());
                    processOrderItemPrintVo.setIsFirst(it.getIsFirst());
                    processOrderItemPrintVo.setVersion(it.getVersion());
                    return processOrderItemPrintVo;
                })
                .collect(Collectors.toList());
        processOrderPrintVo.setProcessOrderItems(processOrderItemVos);


        // 单位原料配比
        Optional<ProcessOrderItemPo> firstOrderItemOption = processOrderItemPos.stream()
                .filter(it -> BooleanType.TRUE.equals(it.getIsFirst()))
                .findFirst();
        Integer processNum = 1;
        if (firstOrderItemOption.isPresent()) {
            ProcessOrderItemPo processOrderItemPo = firstOrderItemOption.get();
            processNum = processOrderItemPo.getProcessNum();
        }
        Integer finalProcessNum = processNum;

        List<String> materialSkus = processOrderMaterialPos.stream()
                .map(ProcessOrderMaterialPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        Map<String, Integer> groupedMaterialPos = processOrderMaterialPos.stream()
                .collect(Collectors.groupingBy(ProcessOrderMaterialPo::getSku, Collectors.summingInt(ProcessOrderMaterialPo::getDeliveryNum)));
        if (CollectionUtils.isNotEmpty(materialSkus)) {
            List<ProcessOrderMaterialPo> finalProcMaterialPoList = processOrderMaterialPos;
            List<ProcessOrderMaterialComparePo> finalProcMaterialComparePoList = processOrderMaterialComparePoList;

            List<ProcessOrderMaterialPrintVo> processOrderMaterialPrintVos = materialSkus.stream().map(item -> {
                ProcessOrderMaterialPrintVo processOrderMaterialPrintVo = new ProcessOrderMaterialPrintVo();
                processOrderMaterialPrintVo.setSampleChildOrderInfoList(new ArrayList<>());
                if (CollectionUtils.isNotEmpty(finalSampleInfoBySkuList)) {
                    Optional<SampleInfoVo> first = finalSampleInfoBySkuList.stream()
                            .filter(it -> it.getSku().equals(item))
                            .findFirst();
                    if (first.isPresent()) {
                        SampleInfoVo sampleInfoVo = first.get();
                        processOrderMaterialPrintVo.setSampleChildOrderInfoList(sampleInfoVo.getSampleChildOrderInfoList());
                    }
                }

                processOrderMaterialPrintVo.setSku(item);
                Integer totalDeliveryNum = groupedMaterialPos.get(item);
                processOrderMaterialPrintVo.setRate(NumberUtil.div((Number) totalDeliveryNum, finalProcessNum, RATE_SCALE));

                List<PlmSkuVo> plmSkuVos = groupedPlmSkuMap.get(item);
                if (CollectionUtils.isNotEmpty(plmSkuVos)) {
                    Optional<PlmSkuVo> firstPlmSkuVoOptional = plmSkuVos.stream().findFirst();
                    firstPlmSkuVoOptional.ifPresent(plmSkuVo -> processOrderMaterialPrintVo.setSkuEncode(plmSkuVo.getSkuEncode()));
                }

                //通过sku匹配加工单原料列表
                List<ProcessOrderMaterialPo> matchSkuMaterialList = finalProcMaterialPoList.stream()
                        .filter(material -> Objects.equals(material.getSku(), item)).collect(Collectors.toList());
                List<Long> matchSkuMaterialIdList = matchSkuMaterialList.stream()
                        .map(ProcessOrderMaterialPo::getProcessOrderMaterialId)
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(matchSkuMaterialIdList)) {
                    //匹配当前sku加工单原料对照关系列表
                    List<ProcessOrderMaterialComparePo> matchSkuComparePoList = finalProcMaterialComparePoList.stream()
                            .filter(prodMaterialComparePo -> matchSkuMaterialIdList.contains(prodMaterialComparePo.getProcessOrderMaterialId()))
                            .collect(Collectors.toList());
                    List<ProcessOrderMaterialCompareVo> procMaterialCompareVos
                            = ProcessOrderBuilder.buildProcessOrderMaterialCompareVoList(matchSkuComparePoList, groupedPlmSkuMap);
                    processOrderMaterialPrintVo.setProcessOrderMaterialCompareVoList(procMaterialCompareVos);
                }
                return processOrderMaterialPrintVo;
            }).collect(Collectors.toList());
            processOrderPrintVo.setProcessOrderMaterials(processOrderMaterialPrintVos);
        }


        Map<Long, List<ProcessPo>> groupedProcessPos = processPos.stream()
                .collect(Collectors.groupingBy(ProcessPo::getProcessId));
        // 获取加工工序
        List<ProcessOrderProcedureVo> processOrderProcedureVos = processOrderProcedurePos.stream()
                .map(it -> {
                    Optional<ProcessPo> firstProcessPoOptional = groupedProcessPos.get(it.getProcessId())
                            .stream()
                            .findFirst();
                    ProcessOrderProcedureVo processOrderProcedureVo = ProcessOrderConverter.INSTANCE.convert(it);
                    if (firstProcessPoOptional.isPresent()) {
                        ProcessPo processPo = firstProcessPoOptional.get();
                        processOrderProcedureVo.setProcessName(processPo.getProcessName());
                        processOrderProcedureVo.setProcessLabel(processPo.getProcessLabel());
                        processOrderProcedureVo.setProcessSecondName(processPo.getProcessSecondName());
                    }
                    return processOrderProcedureVo;
                })
                .collect(Collectors.toList());
        processOrderPrintVo.setProcessOrderProcedures(processOrderProcedureVos);

        // 获取加工描述
        List<ProcessOrderDescVo> processOrderDescVos = processOrderDescPos.stream()
                .map(ProcessOrderConverter.INSTANCE::convert)
                .collect(Collectors.toList());
        processOrderPrintVo.setProcessOrderDescs(processOrderDescVos);

        // 获取出库明细
        List<ProcessOrderPrintDeliveryOrderVo> processOrderPrintDeliveryOrderList = new ArrayList<>();
        Optional.ofNullable(processOrderMaterialPoDeliveryList)
                .orElse(new ArrayList<>())
                .forEach(materialPo -> {
                    ProcessOrderPrintDeliveryOrderVo processOrderPrintDeliveryOrderVo
                            = new ProcessOrderPrintDeliveryOrderVo();
                    processOrderPrintDeliveryOrderVo.setSku(materialPo.getSku());

                    List<PlmSkuVo> plmSkuVos = groupedPlmSkuMap.get(materialPo.getSku());
                    if (CollectionUtils.isNotEmpty(plmSkuVos)) {
                        Optional<PlmSkuVo> firstPlmSkuVoOptional = plmSkuVos.stream()
                                .findFirst();
                        firstPlmSkuVoOptional.ifPresent(
                                plmSkuVo -> processOrderPrintDeliveryOrderVo.setSkuEncode(plmSkuVo.getSkuEncode()));
                    }
                    processOrderPrintDeliveryOrderVo.setDeliverCnt(materialPo.getDeliveryNum());
                    ProcessOrderPrintDeliveryOrderVo deliveryOrderSkuGrouping
                            = processOrderPrintDeliveryOrderList.stream()
                            .filter(order -> order.getSku()
                                    .equals(materialPo.getSku()))
                            .findFirst()
                            .orElse(null);
                    if (null != deliveryOrderSkuGrouping
                            && deliveryOrderSkuGrouping.getDeliverCnt() != null
                            && materialPo.getDeliveryNum() != null) {
                        deliveryOrderSkuGrouping.setDeliverCnt(
                                deliveryOrderSkuGrouping.getDeliverCnt() + materialPo.getDeliveryNum());
                    }
                    if (null == deliveryOrderSkuGrouping) {
                        processOrderPrintDeliveryOrderList.add(processOrderPrintDeliveryOrderVo);
                    }
                });
        processOrderPrintVo.setProcessOrderPrintDeliveryOrderList(processOrderPrintDeliveryOrderList);


        return processOrderPrintVo;

    }

    /**
     * 批量打印加工单（仅提供给 WMS 使用）
     *
     * @param dto
     * @return
     */
    public List<ProcessOrderPrintByWmsVo> batchPrintByDeliveryNo(ProcessOrderBatchPrintByDeliveryNoDto dto) {
        List<ProcessOrderBatchPrintByDeliveryNoDto.RelatedNos> relatedNosList = dto.getRelatedNosList();
        if (CollectionUtils.isEmpty(relatedNosList)) {
            throw new BizException("加工单号必传");
        }

        List<String> processOrderNos = relatedNosList.stream()
                .map(ProcessOrderBatchPrintByDeliveryNoDto.RelatedNos::getProcessOrderNo)
                .collect(Collectors.toList());
        List<ProcessOrderPrintVo> processOrderPrintVos = this.batchPrint(processOrderNos);
        return relatedNosList.stream()
                .map(item -> {
                    ProcessOrderPrintByWmsVo processOrderPrintVoByWms = new ProcessOrderPrintByWmsVo();
                    String processOrderNo = item.getProcessOrderNo();
                    Optional<ProcessOrderPrintVo> printOptional = processOrderPrintVos.stream()
                            .filter(it -> processOrderNo.equals(it.getProcessOrderNo()))
                            .findFirst();
                    if (printOptional.isPresent()) {
                        ProcessOrderPrintVo processOrderPrintVo = printOptional.get();
                        processOrderPrintVoByWms = ProcessOrderConverter.INSTANCE.convert(processOrderPrintVo);
                        processOrderPrintVoByWms.setDeliveryNo(item.getDeliveryNo());
                    }
                    return processOrderPrintVoByWms;
                })
                .collect(Collectors.toList());
    }

    /**
     * http批量打印加工单（仅提供给 WMS 使用）
     *
     * @param dto
     * @return
     */
    public List<ProcessOrderPrintByWmsVo> batchPrintHttpByDeliveryNo(ProcessOrderBatchPrintByDeliveryNoDto dto) {
        List<ProcessOrderBatchPrintByDeliveryNoDto.RelatedNos> relatedNosList = dto.getRelatedNosList();
        if (CollectionUtils.isEmpty(relatedNosList)) {
            throw new BizException("加工单号必传");
        }

        List<ProcessOrderPrintBo> boList = new ArrayList<>();
        for (ProcessOrderBatchPrintByDeliveryNoDto.RelatedNos relatedNos : relatedNosList) {
            ProcessOrderPrintBo processOrderPrintBo = new ProcessOrderPrintBo();
            processOrderPrintBo.setDeliveryNo(relatedNos.getDeliveryNo());
            processOrderPrintBo.setProcessOrderNo(relatedNos.getProcessOrderNo());
            boList.add(processOrderPrintBo);
        }
        List<ProcessOrderPrintVo> processOrderPrintVos = this.batchHttpPrint(boList);
        return relatedNosList.stream()
                .map(item -> {
                    ProcessOrderPrintByWmsVo processOrderPrintVoByWms = new ProcessOrderPrintByWmsVo();
                    String processOrderNo = item.getProcessOrderNo();
                    Optional<ProcessOrderPrintVo> printOptional;
                    if (StringUtils.isNotBlank(item.getDeliveryNo())) {
                        printOptional = processOrderPrintVos.stream()
                                .filter(processOrderPrint -> processOrderNo.equals(processOrderPrint.getProcessOrderNo()))
                                .filter(processOrderPrint -> item.getDeliveryNo().equals(processOrderPrint.getDeliveryNo()))
                                .findFirst();
                    } else {
                        printOptional = processOrderPrintVos.stream()
                                .filter(processOrderPrint -> processOrderNo.equals(processOrderPrint.getProcessOrderNo()))
                                .findFirst();
                    }
                    if (printOptional.isPresent()) {
                        ProcessOrderPrintVo processOrderPrintVo = printOptional.get();
                        processOrderPrintVoByWms = ProcessOrderConverter.INSTANCE.convert(processOrderPrintVo);
                    }
                    return processOrderPrintVoByWms;
                })
                .collect(Collectors.toList());
    }


    public List<ProcessOrderPrintVo> batchPrintByProcessOrderIds(ProcessOrderBatchPrintDto dto) {
        List<ProcessOrderBatchPrintDto.ProcessItem> processItems = dto.getProcessItems();
        List<Long> processOrderIds = processItems.stream()
                .map(ProcessOrderBatchPrintDto.ProcessItem::getProcessOrderId)
                .collect(Collectors.toList());

        // 查询所有的加工单
        List<ProcessOrderPo> processOrderPos = processOrderDao.getByProcessOrderIds(processOrderIds);
        if (CollectionUtils.isEmpty(processOrderPos)) {
            throw new BizException("加工单不存在");
        }
        List<ProcessOrderPrintBo> boList = new ArrayList<>();
        for (ProcessOrderPo processOrderPo : processOrderPos) {
            ProcessOrderPrintBo processOrderPrintBo = new ProcessOrderPrintBo();
            processOrderPrintBo.setProcessOrderNo(processOrderPo.getProcessOrderNo());
            boList.add(processOrderPrintBo);
        }
        return this.batchHttpPrint(boList);
    }


    /**
     * 批量打印加工单
     *
     * @param processOrderNos
     * @return
     */
    public List<ProcessOrderPrintVo> batchPrint(List<String> processOrderNos) {
        List<ProcessOrderPo> processOrderPos = processOrderDao.getByProcessOrderNos(processOrderNos);
        // 查询所有的加工详情
        List<ProcessOrderItemPo> processOrderItemPos = processOrderItemDao.getByProcessOrderNos(processOrderNos);
        Map<String, List<ProcessOrderItemPo>> groupProcessOrderItemPos = processOrderItemPos.stream()
                .collect(Collectors.groupingBy(ProcessOrderItemPo::getProcessOrderNo));

        // 查询所有的加工原料
        List<ProcessOrderMaterialPo> processOrderMaterialPos
                = processOrderMaterialDao.getByProcessOrderNos(processOrderNos);
        Map<String, List<ProcessOrderMaterialPo>> groupProcessOrderMaterialPos = processOrderMaterialPos.stream()
                .collect(Collectors.groupingBy(ProcessOrderMaterialPo::getProcessOrderNo));

        //加工原料对照关系
        List<Long> materialIdList = processOrderMaterialPos.stream()
                .map(ProcessOrderMaterialPo::getProcessOrderMaterialId)
                .collect(Collectors.toList());
        List<ProcessOrderMaterialComparePo> procMaterialComparePoList = procMaterialCompareDao.listByMaterialIds(materialIdList);

        // 查询所有的加工工序
        List<ProcessOrderProcedurePo> processOrderProcedurePos
                = processOrderProcedureDao.getByProcessOrderNos(processOrderNos);
        Map<String, List<ProcessOrderProcedurePo>> groupProcessOrderProcedurePos = processOrderProcedurePos.stream()
                .collect(Collectors.groupingBy(ProcessOrderProcedurePo::getProcessOrderNo));
        // 查询所有的加工描述
        List<ProcessOrderDescPo> processOrderDescPos = processOrderDescDao.getByProcessOrderNos(processOrderNos);
        Map<String, List<ProcessOrderDescPo>> groupProcessOrderDescPos = processOrderDescPos.stream()
                .collect(Collectors.groupingBy(ProcessOrderDescPo::getProcessOrderNo));

        // 获取生产属性
        SampleSkuListDto sampleSkuListDto = new SampleSkuListDto();
        ArrayList<String> skuList = new ArrayList<>();
        processOrderItemPos.forEach(it -> {
            skuList.add(it.getSku());
        });
        processOrderMaterialPos.forEach(it -> {
            skuList.add(it.getSku());
        });
        sampleSkuListDto.setSkuList(skuList);
        List<SampleInfoVo> sampleInfoBySkuList = sampleBaseService.getSampleInfoBySkuList(sampleSkuListDto);

        List<Long> processIds = processOrderProcedurePos.stream()
                .map(ProcessOrderProcedurePo::getProcessId)
                .distinct()
                .collect(Collectors.toList());
        List<ProcessPo> processPos = processDao.getByProcessIds(processIds);

        List<String> compareSkuList = procMaterialComparePoList.stream()
                .map(ProcessOrderMaterialComparePo::getSku)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(compareSkuList)) {
            skuList.addAll(compareSkuList);
        }

        List<PlmSkuVo> skuEncodeBySku = plmRemoteService.getSkuEncodeBySku(skuList);
        Map<String, List<PlmSkuVo>> groupedPlmSkuVo = skuEncodeBySku.stream()
                .collect(Collectors.groupingBy(PlmSkuVo::getSkuCode));


        return processOrderPos.stream()
                .map(item -> {
                    List<PlmSkuImage> skuImages = plmRemoteService.getSkuImage(skuList, item.getPlatform());
                    List<ProcessOrderItemPo> needProcessOrderItemPos = groupProcessOrderItemPos.get(item.getProcessOrderNo());
                    List<ProcessOrderMaterialPo> needProcessOrderMaterialPos = groupProcessOrderMaterialPos.get(item.getProcessOrderNo());

                    //匹配加工单原料商品对照关系
                    List<ProcessOrderMaterialComparePo> matchProcMaterialComparePoList = Lists.newArrayList();
                    if (CollectionUtils.isNotEmpty(needProcessOrderMaterialPos)) {
                        List<Long> matchMaterialIdList = needProcessOrderMaterialPos.stream()
                                .map(ProcessOrderMaterialPo::getProcessOrderMaterialId)
                                .collect(Collectors.toList());
                        matchProcMaterialComparePoList = procMaterialComparePoList.stream()
                                .filter(it -> matchMaterialIdList.contains(it.getProcessOrderMaterialId()))
                                .collect(Collectors.toList());
                    }

                    List<ProcessOrderProcedurePo> needProcessOrderProcedurePos = groupProcessOrderProcedurePos.get(item.getProcessOrderNo());
                    List<ProcessOrderDescPo> needProcessOrderDescPos = groupProcessOrderDescPos.get(item.getProcessOrderNo());

                    List<SampleInfoVo> sampleInfoBySkuListItem = new ArrayList<>();
                    List<PlmSkuImage> skuImagesItem = new ArrayList<>();

                    List<String> skuListByItem = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(needProcessOrderItemPos)) {
                        skuListByItem = needProcessOrderItemPos.stream()
                                .map(ProcessOrderItemPo::getSku)
                                .collect(Collectors.toList());
                    }
                    List<String> skuListByMaterial = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(needProcessOrderMaterialPos)) {
                        skuListByMaterial = needProcessOrderMaterialPos.stream()
                                .map(ProcessOrderMaterialPo::getSku)
                                .collect(Collectors.toList());
                    }
                    List<String> allSkuList = Stream.of(skuListByItem, skuListByMaterial)
                            .flatMap(Collection::stream)
                            .distinct()
                            .collect(Collectors.toList());

                    if (CollectionUtils.isNotEmpty(sampleInfoBySkuList)) {
                        sampleInfoBySkuListItem = sampleInfoBySkuList.stream()
                                .filter(it -> allSkuList.contains(it.getSku()))
                                .collect(Collectors.toList());
                    }
                    if (CollectionUtils.isNotEmpty(skuImages)) {
                        skuImagesItem = skuImages.stream()
                                .filter(it -> allSkuList.contains(it.getSkuCode()))
                                .collect(Collectors.toList());
                    }

                    return this.poToPrintVo(item,
                            needProcessOrderItemPos,
                            needProcessOrderMaterialPos,
                            needProcessOrderProcedurePos,
                            needProcessOrderDescPos,
                            sampleInfoBySkuListItem,
                            skuImagesItem,
                            processPos,
                            groupedPlmSkuVo,
                            null,
                            Collections.emptyList(),
                            matchProcMaterialComparePoList);
                })
                .collect(Collectors.toList());
    }

    /**
     * http接口批量打印加工单
     *
     * @param boList:
     * @return List<ProcessOrderPrintVo>
     * @author ChenWenLong
     * @date 2024/1/4 14:56
     */
    public List<ProcessOrderPrintVo> batchHttpPrint(@NotEmpty List<ProcessOrderPrintBo> boList) {
        List<String> processOrderNos = boList.stream()
                .map(ProcessOrderPrintBo::getProcessOrderNo)
                .distinct()
                .collect(Collectors.toList());
        List<ProcessOrderPo> processOrderPos = processOrderDao.getByProcessOrderNos(processOrderNos);
        // 查询所有的加工详情
        List<ProcessOrderItemPo> processOrderItemPos = processOrderItemDao.getByProcessOrderNos(processOrderNos);
        Map<String, List<ProcessOrderItemPo>> groupProcessOrderItemPos = processOrderItemPos.stream()
                .collect(Collectors.groupingBy(ProcessOrderItemPo::getProcessOrderNo));

        // 查询所有的加工原料
        List<ProcessOrderMaterialPo> processOrderMaterialPos
                = processOrderMaterialDao.getByProcessOrderNos(processOrderNos);
        Map<String, List<ProcessOrderMaterialPo>> groupProcessOrderMaterialPos = processOrderMaterialPos.stream()
                .collect(Collectors.groupingBy(ProcessOrderMaterialPo::getProcessOrderNo));

        //加工原料对照关系
        List<Long> materialIdList = processOrderMaterialPos.stream()
                .map(ProcessOrderMaterialPo::getProcessOrderMaterialId)
                .collect(Collectors.toList());
        List<ProcessOrderMaterialComparePo> procMaterialComparePoList = procMaterialCompareDao.listByMaterialIds(materialIdList);

        // 查询所有的加工工序
        List<ProcessOrderProcedurePo> processOrderProcedurePos
                = processOrderProcedureDao.getByProcessOrderNos(processOrderNos);
        Map<String, List<ProcessOrderProcedurePo>> groupProcessOrderProcedurePos = processOrderProcedurePos.stream()
                .collect(Collectors.groupingBy(ProcessOrderProcedurePo::getProcessOrderNo));
        // 查询所有的加工描述
        List<ProcessOrderDescPo> processOrderDescPos = processOrderDescDao.getByProcessOrderNos(processOrderNos);
        Map<String, List<ProcessOrderDescPo>> groupProcessOrderDescPos = processOrderDescPos.stream()
                .collect(Collectors.groupingBy(ProcessOrderDescPo::getProcessOrderNo));

        // 获取生产属性
        SampleSkuListDto sampleSkuListDto = new SampleSkuListDto();
        ArrayList<String> skuList = new ArrayList<>();
        processOrderItemPos.forEach(it -> skuList.add(it.getSku()));
        processOrderMaterialPos.forEach(it -> skuList.add(it.getSku()));
        sampleSkuListDto.setSkuList(skuList);
        List<SampleInfoVo> sampleInfoBySkuList = sampleBaseService.getSampleInfoBySkuList(sampleSkuListDto);

        List<Long> processIds = processOrderProcedurePos.stream()
                .map(ProcessOrderProcedurePo::getProcessId)
                .distinct()
                .collect(Collectors.toList());
        List<ProcessPo> processPos = processDao.getByProcessIds(processIds);


        // 获取WMS出库明细
        List<ProcessDeliveryOrderVo> processDeliveryOrderVoList
                = wmsRemoteService.getProcessDeliveryOrderBatch(processOrderNos, WmsEnum.DeliveryType.PROCESS);
        Map<String, List<ProcessDeliveryOrderVo>> processDeliveryOrderVoMap = processDeliveryOrderVoList.stream()
                .collect(Collectors.groupingBy(ProcessDeliveryOrderVo::getRelatedOrderNo));

        List<String> deliverySkuCodeList = processDeliveryOrderVoList.stream()
                .flatMap(orderVo -> orderVo.getProducts()
                        .stream())
                .map(ProcessDeliveryOrderVo.DeliveryProduct::getSkuCode)
                .collect(Collectors.toList());
        skuList.addAll(deliverySkuCodeList);

        List<String> compareSkuList = procMaterialComparePoList.stream()
                .map(ProcessOrderMaterialComparePo::getSku)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(compareSkuList)) {
            skuList.addAll(compareSkuList);
        }

        List<PlmSkuVo> skuEncodeBySku = plmRemoteService.getSkuEncodeBySku(skuList);
        Map<String, List<PlmSkuVo>> groupedPlmSkuVo = skuEncodeBySku.stream()
                .collect(Collectors.groupingBy(PlmSkuVo::getSkuCode));


        return boList.stream()
                .map(item -> {
                    ProcessOrderPo processOrderPo = processOrderPos.stream()
                            .filter(po -> po.getProcessOrderNo().equals(item.getProcessOrderNo()))
                            .findFirst()
                            .orElse(null);
                    if (processOrderPo == null) {
                        throw new BizException("加工单号：{}不存在", item.getProcessOrderNo());
                    }
                    List<PlmSkuImage> skuImages = plmRemoteService.getSkuImage(skuList, processOrderPo.getPlatform());
                    List<ProcessOrderItemPo> needProcessOrderItemPos = groupProcessOrderItemPos.get(processOrderPo.getProcessOrderNo());
                    List<ProcessOrderMaterialPo> needProcessOrderMaterialPos = groupProcessOrderMaterialPos.get(processOrderPo.getProcessOrderNo());
                    List<ProcessOrderProcedurePo> needProcessOrderProcedurePos = groupProcessOrderProcedurePos.get(processOrderPo.getProcessOrderNo());

                    //匹配加工单原料商品对照关系
                    List<ProcessOrderMaterialComparePo> matchProcMaterialComparePoList = Lists.newArrayList();
                    if (CollectionUtils.isNotEmpty(needProcessOrderMaterialPos)) {
                        List<Long> matchMaterialIdList = needProcessOrderMaterialPos.stream()
                                .map(ProcessOrderMaterialPo::getProcessOrderMaterialId)
                                .collect(Collectors.toList());
                        matchProcMaterialComparePoList = procMaterialComparePoList.stream()
                                .filter(it -> matchMaterialIdList.contains(it.getProcessOrderMaterialId()))
                                .collect(Collectors.toList());
                    }

                    List<ProcessOrderDescPo> needProcessOrderDescPos = groupProcessOrderDescPos.get(processOrderPo.getProcessOrderNo());

                    List<SampleInfoVo> sampleInfoBySkuListItem = new ArrayList<>();
                    List<PlmSkuImage> skuImagesItem = new ArrayList<>();

                    List<String> skuListByItem = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(needProcessOrderItemPos)) {
                        skuListByItem = needProcessOrderItemPos.stream()
                                .map(ProcessOrderItemPo::getSku)
                                .collect(Collectors.toList());
                    }
                    List<String> skuListByMaterial = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(needProcessOrderMaterialPos)) {
                        skuListByMaterial = needProcessOrderMaterialPos.stream()
                                .map(ProcessOrderMaterialPo::getSku)
                                .collect(Collectors.toList());
                    }
                    List<String> allSkuList = Stream.of(skuListByItem, skuListByMaterial)
                            .flatMap(Collection::stream)
                            .distinct()
                            .collect(Collectors.toList());

                    if (CollectionUtils.isNotEmpty(sampleInfoBySkuList)) {
                        sampleInfoBySkuListItem = sampleInfoBySkuList.stream()
                                .filter(it -> allSkuList.contains(it.getSku()))
                                .collect(Collectors.toList());
                    }
                    if (CollectionUtils.isNotEmpty(skuImages)) {
                        skuImagesItem = skuImages.stream()
                                .filter(it -> allSkuList.contains(it.getSkuCode()))
                                .collect(Collectors.toList());
                    }

                    // 获取WMS出库明细
                    List<ProcessDeliveryOrderVo> processDeliveryOrderVos
                            = processDeliveryOrderVoMap.get(processOrderPo.getProcessOrderNo());
                    ProcessDeliveryOrderVo processDeliveryOrderVo;
                    if (StringUtils.isNotBlank(item.getDeliveryNo())) {
                        processDeliveryOrderVo = Optional.ofNullable(processDeliveryOrderVos)
                                .orElse(new ArrayList<>())
                                .stream()
                                .filter(orderVo -> item.getDeliveryNo().equals(orderVo.getDeliveryOrderNo()))
                                .findFirst()
                                .orElse(null);
                    } else {
                        processDeliveryOrderVo = Optional.ofNullable(processDeliveryOrderVos)
                                .orElse(new ArrayList<>())
                                .stream()
                                .filter(deliveryOrderVo -> !WmsEnum.DeliveryState.CANCELING.equals(deliveryOrderVo.getDeliveryState()) &&
                                        !WmsEnum.DeliveryState.CANCELED.equals(deliveryOrderVo.getDeliveryState()))
                                .max(Comparator.comparing(ProcessDeliveryOrderVo::getCreateTime))
                                .orElse(null);
                    }
                    List<ProcessOrderMaterialPo> processOrderMaterialPoDeliveryList = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(needProcessOrderMaterialPos) && processDeliveryOrderVo != null) {
                        processOrderMaterialPoDeliveryList = needProcessOrderMaterialPos.stream()
                                .filter(materialPo -> processDeliveryOrderVo.getDeliveryOrderNo().equals(materialPo.getDeliveryNo()))
                                .collect(Collectors.toList());
                    }

                    return this.poToPrintVo(processOrderPo,
                            needProcessOrderItemPos,
                            needProcessOrderMaterialPos,
                            needProcessOrderProcedurePos,
                            needProcessOrderDescPos,
                            sampleInfoBySkuListItem,
                            skuImagesItem,
                            processPos,
                            groupedPlmSkuVo,
                            processDeliveryOrderVo,
                            processOrderMaterialPoDeliveryList,
                            matchProcMaterialComparePoList);
                })
                .collect(Collectors.toList());
    }


    public List<ProcessOrderPrintSkuCodeVo> batchPrintSkuCode(ProcessOrderBatchPrintDto dto) {
        List<ProcessOrderBatchPrintDto.ProcessItem> processItems = dto.getProcessItems();
        List<Long> processOrderIds = processItems.stream()
                .map(ProcessOrderBatchPrintDto.ProcessItem::getProcessOrderId)
                .collect(Collectors.toList());

        // 查询所有的加工单
        List<ProcessOrderPo> processOrderPos = processOrderDao.getByProcessOrderIds(processOrderIds);
        if (CollectionUtils.isEmpty(processOrderPos)) {
            throw new BizException("加工单不存在");
        }
        processOrderPos = processOrderPos.stream()
                .sorted(Comparator.comparing(ProcessOrderPo::getProcessOrderId)
                        .reversed())
                .collect(Collectors.toList());


        List<String> processOrderNos = processOrderPos.stream()
                .map(ProcessOrderPo::getProcessOrderNo)
                .collect(Collectors.toList());

        // 查询所有的加工详情
        List<ProcessOrderItemPo> processOrderItemPos = processOrderItemDao.getByProcessOrderNos(processOrderNos);
        Map<String, List<ProcessOrderItemPo>> groupProcessOrderItemPos = processOrderItemPos.stream()
                .collect(Collectors.groupingBy(ProcessOrderItemPo::getProcessOrderNo));

        // 获取变体属性
        List<String> skuList = processOrderItemPos.stream()
                .map(ProcessOrderItemPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        List<PlmVariantVo> variantAttr = plmRemoteService.getVariantAttr(skuList);

        ArrayList<ProcessOrderPrintSkuCodeVo> processOrderPrintSkuCodeVos = new ArrayList<>();
        processOrderPos.forEach(item -> {
            List<ProcessOrderItemPo> needProcessOrderItemPos = groupProcessOrderItemPos.get(item.getProcessOrderNo());
            if (CollectionUtils.isNotEmpty(needProcessOrderItemPos)) {
                needProcessOrderItemPos.forEach(item2 -> {
                    ProcessOrderPrintSkuCodeVo processOrderPrintSkuCodeVo = new ProcessOrderPrintSkuCodeVo();
                    processOrderPrintSkuCodeVo.setProcessOrderId(item.getProcessOrderId());
                    processOrderPrintSkuCodeVo.setProcessOrderNo(item.getProcessOrderNo());
                    processOrderPrintSkuCodeVo.setSku(item2.getSku());
                    processOrderPrintSkuCodeVo.setSkuBatchCode(item2.getSkuBatchCode());
                    processOrderPrintSkuCodeVo.setProcessNum(item2.getProcessNum());
                    processOrderPrintSkuCodeVo.setQualityGoodsCnt(item2.getQualityGoodsCnt());
                    processOrderPrintSkuCodeVo.setVersion(item.getVersion());
                    // 变体属性
                    Optional<PlmVariantVo> first = variantAttr.stream()
                            .filter(it -> it.getSkuCode()
                                    .equals(item2.getSku()))
                            .findFirst();
                    first.ifPresent(plmVariantVo -> processOrderPrintSkuCodeVo.setVariantSkuList(
                            plmVariantVo.getVariantSkuList()));
                    processOrderPrintSkuCodeVos.add(processOrderPrintSkuCodeVo);
                });
            }
        });

        return processOrderPrintSkuCodeVos;
    }

    /**
     * 创建加工单
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createByNormal(ProcessOrderCreateNewDto dto) {
        // 入参校验
        this.checkCreateNormalProcessOrder(dto);

        //是否复杂工序
        List<ProcessOrderCreateDto.ProcessOrderProcedure> processDtoList
                = Optional.ofNullable(dto.getProcessOrderProcedures()).orElse(Collections.emptyList());
        List<Long> processIds = processDtoList.stream().map(ProcessOrderCreateDto.ProcessOrderProcedure::getProcessId).collect(Collectors.toList());
        boolean complexProcessOrder = isComplexProcessOrder(processIds);

        //获取生产信息
        String platform = ParamValidUtils.requireNotBlank(dto.getPlatform(), "创建加工单失败！平台编码为空");
        ProcessOrderProductionInfoBo processOrderGenerateInfoBo
                = processOrderBaseService.getProcProdInfoBo(dto.getSku(), dto.getDeliveryWarehouseCode(), platform, dto.getProductQuality());
        final List<ProcessOrderSampleBo> processOrderSampleBoList = processOrderGenerateInfoBo.getProcessOrderSampleBoList();

        // 根据是否复杂工序拆分加工单数量
        ProcessOrderAndItemListBo processOrderAndItemListBo = complexProcessOrder ?
                this.splitComplexProcessNum(dto, processOrderSampleBoList) :
                this.splitProcessNum(dto, processOrderSampleBoList);

        final List<ProcessOrderPo> processOrderPoList = processOrderAndItemListBo.getProcessOrderPoList();
        final List<ProcessOrderItemPo> processOrderItemPoList = processOrderAndItemListBo.getProcessOrderItemPoList();
        final List<ProcessOrderExtraPo> processOrderExtraPoList = processOrderAndItemListBo.getProcessOrderExtraPoList();
        final List<ProcessOrderProcedurePo> processOrderProcedurePoList = processOrderAndItemListBo.getProcessOrderProcedurePoList();
        final List<ProcessOrderDescPo> processOrderDescPoList = processOrderAndItemListBo.getProcessOrderDescPoList();
        final List<ProcessOrderSamplePo> processOrderSamplePoList = processOrderAndItemListBo.getProcessOrderSamplePoList();
        processOrderDao.insertBatch(processOrderPoList);
        processOrderItemDao.insertBatch(processOrderItemPoList);
        processOrderExtraDao.insertBatch(processOrderExtraPoList);

        //保存加工单原料&原料与商品对照关系
        List<ProcessOrderMaterialRefBo> processOrderMaterialRefBoList = processOrderAndItemListBo.getProcessOrderMaterialRefBoList();
        if (CollectionUtils.isNotEmpty(processOrderMaterialRefBoList)) {
            for (ProcessOrderMaterialRefBo processOrderMaterialRefBo : processOrderMaterialRefBoList) {
                ProcessOrderMaterialPo procMaterialPo = processOrderMaterialRefBo.getProcMaterialPo();
                processOrderMaterialDao.insert(procMaterialPo);

                Long procMaterialId = procMaterialPo.getProcessOrderMaterialId();
                List<ProcessOrderMaterialComparePo> procMaterialComparePoList
                        = processOrderMaterialRefBo.getProcMaterialComparePoList();
                if (CollectionUtils.isNotEmpty(procMaterialComparePoList)) {
                    procMaterialComparePoList.forEach(comparePo -> comparePo.setProcessOrderMaterialId(procMaterialId));
                    procMaterialCompareDao.insertBatch(procMaterialComparePoList);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(processOrderProcedurePoList)) {
            processOrderProcedureDao.insertBatch(processOrderProcedurePoList);
        }
        if (CollectionUtils.isNotEmpty(processOrderDescPoList)) {
            processOrderDescDao.insertBatch(processOrderDescPoList);
        }
        if (CollectionUtils.isNotEmpty(processOrderSamplePoList)) {
            processOrderSampleDao.insertBatch(processOrderSamplePoList);
        }

        // 记录单据变化日志
        List<LogVersionBo> logVersionBos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(processOrderSampleBoList)) {
            logVersionBos = processOrderSampleBoList.stream()
                    .map(item -> {
                        LogVersionBo logVersionBo = new LogVersionBo();
                        logVersionBo.setKey(item.getSampleInfoKey());
                        logVersionBo.setValueType(LogVersionValueType.STRING);
                        logVersionBo.setValue(item.getSampleInfoValue());
                        return logVersionBo;
                    })
                    .collect(Collectors.toList());
        }


        final Map<String, ProcessOrderExtraPo> processOrderExtraNoPoMap = processOrderExtraPoList.stream()
                .collect(Collectors.toMap(ProcessOrderExtraPo::getProcessOrderNo, Function.identity()));
        for (ProcessOrderPo processOrderPo : processOrderPoList) {
            // 生成日志
            ProcessOrderExtraPo processOrderExtraPo = processOrderExtraNoPoMap.get(processOrderPo.getProcessOrderNo());
            processOrderBaseService.createStatusChangeLog(processOrderPo, processOrderExtraPo, new OperatorUserBo());
            processOrderBaseService.createOrderChangeLog(processOrderPo.getProcessOrderId(), logVersionBos);
            // 刷新缺失信息
            processOrderBaseService.checkMissingInfo(processOrderPo.getProcessOrderNo());
        }


    }

    private void checkCreateNormalProcessOrder(ProcessOrderCreateNewDto dto) {
        if (dto.getProcessNum() > 10 && dto.getProcessNum() % 10 != 0) {
            throw new ParamIllegalException("加工数必须小于10或者为整10倍数，当前加工数为：{}，请重新填写后提交",
                    dto.getProcessNum());
        }

        // 校验创建加工单的类型
        ProcessOrderType curPoType = dto.getProcessOrderType();
        List<ProcessOrderType> requirePoTypes = Arrays.asList(
                ProcessOrderType.NORMAL,
                ProcessOrderType.OVERSEAS_REPAIR,
                ProcessOrderType.WH,
                ProcessOrderType.REPAIR
        );
        ParamValidUtils.requireContains(curPoType, requirePoTypes,
                StrUtil.format("创建加工单失败，加工单类型必须是以下选项之一 {}",
                        requirePoTypes.stream().map(ProcessOrderType::getRemark).collect(Collectors.joining(","))));
        // 校验仓库原料库存是否充足 & 校验所有商品类型的SKU原料信息是否同一平台
        final String platCode = dto.getPlatCode();
        final List<ProcessOrderMaterialDto> processOrderMaterials = dto.getProcessOrderMaterials();
        if (CollectionUtils.isNotEmpty(processOrderMaterials)) {
            if (StringUtils.isBlank(dto.getDeliveryWarehouseCode()) || StringUtils.isBlank(
                    dto.getDeliveryWarehouseName())) {
                throw new ParamIllegalException("原料发货仓库必填");
            }
            if (null == dto.getProductQuality()) {
                throw new ParamIllegalException("产品质量为必填项");
            }
        }


        // 校验工序信息
        List<ProcessOrderCreateDto.ProcessOrderProcedure> processOrderProcedures = dto.getProcessOrderProcedures();
        if (CollectionUtils.isNotEmpty(processOrderProcedures)) {
            List<Long> processIds = processOrderProcedures.stream()
                    .map(ProcessOrderCreateDto.ProcessOrderProcedure::getProcessId)
                    .collect(Collectors.toList());
            Map<Long, List<ProcessPo>> groupedProcessPos = processDao.getByProcessIds(processIds)
                    .stream()
                    .collect(Collectors.groupingBy(ProcessPo::getProcessId));
            if (null == groupedProcessPos) {
                throw new BizException("工序选择错误");
            }
            processOrderProcedures.forEach((item) -> {
                List<ProcessPo> processPos = groupedProcessPos.get(item.getProcessId());
                if (CollectionUtils.isEmpty(processPos)) {
                    throw new BizException("工序选择错误");
                }
                ProcessPo processPo = processPos.get(0);
                if (ProcessStatus.DISABLED.equals(processPo.getProcessStatus())) {
                    throw new ParamIllegalException("工序：{},已被禁用", processPo.getProcessName());
                }
            });
        }

        LocalDate curDate = TimeUtil.convertZone(LocalDateTime.now(), TimeZoneId.UTC, TimeZoneId.CN).toLocalDate();
        if (dto.getDeliverDate().toLocalDate().isBefore(curDate)
                || dto.getDeliverDate().toLocalDate().equals(curDate)) {
            throw new ParamIllegalException("期望上架时间限制不可小于等于当天");
        }
    }

    /**
     * 拆分加工数，按每10个一单拆分加工单
     *
     * @param dto
     * @param processOrderSampleBoList
     */
    private ProcessOrderAndItemListBo splitProcessNum(ProcessOrderCreateNewDto dto,
                                                      List<ProcessOrderSampleBo> processOrderSampleBoList) {
        Integer processNum = dto.getProcessNum();

        List<Integer> splitList = new ArrayList<>();
        while (processNum > 0) {
            if (processNum >= PROCESS_NUM_LIMIT) {
                splitList.add(PROCESS_NUM_LIMIT);
                processNum -= PROCESS_NUM_LIMIT;
            } else {
                splitList.add(processNum);
                processNum = 0;
            }
        }

        final List<ProcessOrderMaterialDto> processOrderMaterialDtoList = dto.getProcessOrderMaterials();

        List<ProcessOrderPo> processOrderPoList = new ArrayList<>();
        List<ProcessOrderItemPo> processOrderItemPoList = new ArrayList<>();
        List<ProcessOrderExtraPo> processOrderExtraPoList = new ArrayList<>();
        List<ProcessOrderMaterialRefBo> procMaterialRefBoList = new ArrayList<>();
        List<ProcessOrderProcedurePo> processOrderProcedurePoList = new ArrayList<>();
        List<ProcessOrderDescPo> processOrderDescPoList = new ArrayList<>();
        List<ProcessOrderSamplePo> processOrderSamplePoList = new ArrayList<>();

        List<Long> processIds = Optional.ofNullable(dto.getProcessOrderProcedures())
                .orElse(Collections.emptyList())
                .stream()
                .map(ProcessOrderCreateDto.ProcessOrderProcedure::getProcessId)
                .collect(Collectors.toList());
        Map<Long, List<ProcessPo>> groupedProcessPos = processDao.getByProcessIds(processIds)
                .stream()
                .collect(Collectors.groupingBy(ProcessPo::getProcessId));


        for (Integer splitItem : splitList) {
            ProcessOrderPo processOrderPo = new ProcessOrderPo();

            final String processOrderNo
                    = idGenerateService.getConfuseCode(ScmConstant.PROCESS_ORDER_NO_PREFIX, TimeType.CN_DAY,
                    ConfuseLength.L_4);
            processOrderPo.setProcessOrderNo(processOrderNo);
            processOrderPo.setProcessOrderType(dto.getProcessOrderType());
            processOrderPo.setPlatform(dto.getPlatCode());
            processOrderPo.setProcessOrderNote(dto.getProcessOrderNote());
            processOrderPo.setDeliveryNote(dto.getDeliveryNote());
            processOrderPo.setWarehouseCode(dto.getWarehouseCode());
            processOrderPo.setWarehouseName(dto.getWarehouseName());
            processOrderPo.setDeliveryWarehouseCode(dto.getDeliveryWarehouseCode());
            processOrderPo.setDeliveryWarehouseName(dto.getDeliveryWarehouseName());
            processOrderPo.setSpu(dto.getSpu());
            processOrderPo.setDeliverDate(ScmTimeUtil.setToEndOfDay(dto.getDeliverDate()));
            processOrderPo.setPromiseDate(processOrderPo.getDeliverDate());
            processOrderPo.setWarehouseTypes(String.join(",", Optional.ofNullable(dto.getWarehouseTypeList())
                    .orElse(new ArrayList<>())));
            processOrderPo.setProcessOrderStatus(
                    processOrderBaseService.parseProcessOrderStatusByProcessOrderType(dto.getProcessOrderType()));
            processOrderPo.setOverPlan(OverPlan.FALSE);
            processOrderPo.setNeedProcessPlan(NeedProcessPlan.FALSE);
            processOrderPo.setProcessPlanDelay(ProcessPlanDelay.FALSE);
            processOrderPo.setTotalSkuNum(1);
            processOrderPo.setTotalProcessNum(splitItem);
            processOrderPo.setProductQuality(dto.getProductQuality());
            processOrderPo.setFileCode(String.join(",", dto.getFileCodeList()));

            // 设置原料归还状态为"无需归还"
            if (CollectionUtils.isNotEmpty(processOrderMaterialDtoList)) {
                processOrderPo.setMaterialBackStatus(MaterialBackStatus.UN_BACK);
                processOrderPo.setIsReceiveMaterial(IsReceiveMaterial.FALSE);
            } else {
                processOrderPo.setMaterialBackStatus(MaterialBackStatus.NO_BACK);
                processOrderPo.setIsReceiveMaterial(IsReceiveMaterial.NO_RETURN_REQUIRED);
            }
            processOrderPoList.add(processOrderPo);

            // 生成批次码
            ProcessOrderItemPo processOrderItemPo = new ProcessOrderItemPo();
            processOrderItemPo.setProcessOrderNo(processOrderNo);
            processOrderItemPo.setProcessNum(splitItem);
            processOrderItemPo.setSku(dto.getSku());
            processOrderItemPo.setIsFirst(BooleanType.TRUE);

            processOrderBaseService.generateBatchCodes(Collections.singletonList(processOrderItemPo));
            processOrderItemPoList.add(processOrderItemPo);

            // 加工单额外信息
            ProcessOrderExtraPo processOrderExtraPo = new ProcessOrderExtraPo();
            processOrderExtraPo.setProcessOrderNo(processOrderNo);
            processOrderExtraPoList.add(processOrderExtraPo);

            // 加工单原料
            List<ProcessOrderMaterialRefBo> procMaterialRefList = toProcessOrderMaterialRefBo(processOrderNo, processOrderMaterialDtoList);
            procMaterialRefBoList.addAll(procMaterialRefList);

            // 加工工序
            List<ProcessOrderCreateDto.ProcessOrderProcedure> processOrderProcedureDtoList
                    = dto.getProcessOrderProcedures();
            if (CollectionUtils.isNotEmpty(processOrderProcedureDtoList)) {
                processOrderProcedurePoList.addAll(processOrderProcedureDtoList.stream()
                        .map(item -> {
                            List<ProcessPo> processPos
                                    = groupedProcessPos.get(item.getProcessId());
                            if (CollectionUtils.isEmpty(processPos)) {
                                throw new BizException(
                                        "工序信息:{}缺失配置，请联系系统管理员！",
                                        item.getProcessId());
                            }
                            final ProcessPo processPo = processPos.get(0);
                            ProcessOrderProcedurePo processOrderProcedurePo
                                    = new ProcessOrderProcedurePo();
                            processOrderProcedurePo.setProcessOrderNo(
                                    processOrderNo);
                            processOrderProcedurePo.setProcessId(
                                    processPo.getProcessId());
                            processOrderProcedurePo.setProcessCode(
                                    processPo.getProcessCode());
                            processOrderProcedurePo.setProcessName(
                                    processPo.getProcessName());
                            processOrderProcedurePo.setSort(item.getSort());
                            processOrderProcedurePo.setProcessLabel(
                                    processPo.getProcessLabel());
                            processOrderProcedurePo.setCommission(
                                    item.getCommission());

                            return processOrderProcedurePo;
                        })
                        .collect(Collectors.toList()));
            }

            // 加工工序描述
            final List<ProcessOrderCreateDto.ProcessOrderDesc> processOrderDescDtoList = dto.getProcessOrderDescs();
            if (CollectionUtils.isNotEmpty(processOrderDescDtoList)) {
                processOrderDescPoList.addAll(processOrderDescDtoList.stream()
                        .map(descDto -> {
                            ProcessOrderDescPo processOrderDescPo
                                    = new ProcessOrderDescPo();
                            processOrderDescPo.setProcessOrderNo(processOrderNo);
                            processOrderDescPo.setProcessDescName(
                                    descDto.getProcessDescName());
                            processOrderDescPo.setProcessDescValue(
                                    descDto.getProcessDescValue());
                            return processOrderDescPo;
                        })
                        .collect(Collectors.toList()));
            }

            // 加工生产信息
            if (CollectionUtils.isNotEmpty(processOrderSampleBoList)) {
                processOrderSamplePoList.addAll(processOrderSampleBoList.stream()
                        .map(sampleChildOrderInfo -> {
                            ProcessOrderSamplePo processOrderSamplePo
                                    = new ProcessOrderSamplePo();
                            processOrderSamplePo.setProcessOrderNo(processOrderNo);
                            processOrderSamplePo.setSampleChildOrderNo(
                                    sampleChildOrderInfo.getSampleChildOrderNo());
                            processOrderSamplePo.setSourceDocumentNumber(
                                    sampleChildOrderInfo.getSourceDocumentNumber());
                            processOrderSamplePo.setSampleInfoKey(
                                    sampleChildOrderInfo.getSampleInfoKey());
                            processOrderSamplePo.setSampleInfoValue(
                                    sampleChildOrderInfo.getSampleInfoValue());
                            return processOrderSamplePo;
                        })
                        .collect(Collectors.toList()));
            }
        }


        return ProcessOrderAndItemListBo.builder()
                .processOrderPoList(processOrderPoList)
                .processOrderItemPoList(processOrderItemPoList)
                .processOrderExtraPoList(processOrderExtraPoList)
                .processOrderMaterialRefBoList(procMaterialRefBoList)
                .processOrderProcedurePoList(processOrderProcedurePoList)
                .processOrderDescPoList(processOrderDescPoList)
                .processOrderSamplePoList(processOrderSamplePoList)
                .build();
    }

    /**
     * 创建加工单
     *
     * @param dto
     * @return
     */
    public ProcessOrderPo create(ProcessOrderCreateDto dto,
                                 List<ProcessOrderSampleBo> processOrderSampleBoList) {
        if (null == dto.getProcessOrderOriginal()) {
            dto.setProcessOrderOriginal(ProcessOrderOriginal.NORMAL);
        }
        ProcessOrderPo processOrderPo = ProcessOrderConverter.INSTANCE.convert(dto);

        final String processOrderNo
                = idGenerateService.getConfuseCode(ScmConstant.PROCESS_ORDER_NO_PREFIX, TimeType.CN_DAY,
                ConfuseLength.L_4);
        processOrderPo.setProcessOrderNo(processOrderNo);
        processOrderPo.setWarehouseTypes(String.join(",", Optional.ofNullable(dto.getWarehouseTypeList())
                .orElse(new ArrayList<>())));
        processOrderPo.setDeliverDate(ScmTimeUtil.setToEndOfDay(dto.getDeliverDate()));
        processOrderPo.setPromiseDate(processOrderPo.getDeliverDate());

        // 解析加工单状态
        processOrderPo.setProcessOrderStatus(
                processOrderBaseService.parseProcessOrderStatusByProcessOrderType(dto.getProcessOrderType()));
        processOrderPo.setOverPlan(OverPlan.FALSE);
        processOrderPo.setNeedProcessPlan(NeedProcessPlan.FALSE);
        processOrderPo.setProcessPlanDelay(ProcessPlanDelay.FALSE);

        // 通过类型和来源判断加工单是否可创建
        processOrderBaseService.canCreateProcessOrder(dto);

        List<ProcessOrderCreateDto.ProcessOrderMaterial> processOrderMaterials = dto.getProcessOrderMaterials();


        List<ProcessOrderCreateDto.ProcessOrderItem> processOrderItems = dto.getProcessOrderItems();
        // 加工 sku 只能有一个
        if (processOrderItems.size() != 1) {
            throw new ParamIllegalException("加工单sku有且只能有一个");
        }

        // 总 sku 数量
        Integer totalSkuNum = processOrderItems.size();

        // 总加工数量
        int totalProcessNum = processOrderItems.stream()
                .mapToInt(ProcessOrderCreateDto.ProcessOrderItem::getProcessNum)
                .sum();

        // 返工类型的加工单，加工数不能大于次品数
        if (ProcessOrderType.REWORKING.equals(dto.getProcessOrderType()) || ProcessOrderType.LIMITED_REWORKING.equals(
                dto.getProcessOrderType())) {
            List<ProcessOrderItemPo> itemsByProcessOrderNo
                    = processOrderItemDao.getByProcessOrderNo(dto.getParentProcessOrderNo());
            int totalDefectiveGoodsCnt = itemsByProcessOrderNo.stream()
                    .mapToInt(ProcessOrderItemPo::getDefectiveGoodsCnt)
                    .sum();
            Assert.isTrue(0 != totalDefectiveGoodsCnt, () -> new ParamIllegalException("不存在次品，无需返工"));
            Assert.isTrue(totalProcessNum <= totalDefectiveGoodsCnt,
                    () -> new ParamIllegalException("返工数量不能超过次品数量"));
        }

        processOrderPo.setTotalSkuNum(totalSkuNum);
        processOrderPo.setTotalProcessNum(totalProcessNum);

        List<String> fileCodeList = dto.getFileCodeList();
        if (CollectionUtils.isNotEmpty(fileCodeList)) {
            processOrderPo.setFileCode(String.join(",", fileCodeList));
        }

        // 设置原料归还状态为"无需归还"
        if (CollectionUtils.isNotEmpty(processOrderMaterials)) {
            processOrderPo.setMaterialBackStatus(MaterialBackStatus.UN_BACK);
            processOrderPo.setIsReceiveMaterial(IsReceiveMaterial.FALSE);
        } else {
            processOrderPo.setMaterialBackStatus(MaterialBackStatus.NO_BACK);
            processOrderPo.setIsReceiveMaterial(IsReceiveMaterial.NO_RETURN_REQUIRED);
        }

        List<ProcessOrderItemPo> updateProcessOrderItemPos = new ArrayList<>();
        if (!Arrays.asList(ProcessOrderType.LIMITED_REWORKING, ProcessOrderType.REWORKING)
                .contains(processOrderPo.getProcessOrderType())) {
            // 生成批次码
            List<ProcessOrderItemPo> processOrderItemPos = processOrderItems.stream()
                    .map((item) -> {
                        ProcessOrderItemPo processOrderItemPo = new ProcessOrderItemPo();
                        processOrderItemPo.setProcessOrderNo(processOrderNo);
                        processOrderItemPo.setProcessNum(item.getProcessNum());
                        processOrderItemPo.setSku(item.getSku());
                        processOrderItemPo.setVariantProperties(item.getVariantProperties());
                        processOrderItemPo.setPurchasePrice(item.getPurchasePrice());
                        processOrderItemPo.setIsFirst(BooleanType.TRUE);
                        return processOrderItemPo;
                    })
                    .collect(Collectors.toList());
            updateProcessOrderItemPos = processOrderBaseService.generateBatchCodes(processOrderItemPos);
        }
        processOrderCreateBaseService.create(processOrderPo, dto, updateProcessOrderItemPos, processOrderSampleBoList);

        // 生成日志
        ProcessOrderExtraPo processOrderExtraPo = processOrderExtraDao.getByProcessOrderNo(processOrderNo);
        processOrderBaseService.createStatusChangeLog(processOrderPo, processOrderExtraPo, new OperatorUserBo());

        // 记录单据变化日志
        List<LogVersionBo> logVersionBos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(processOrderSampleBoList)) {
            logVersionBos = processOrderSampleBoList.stream()
                    .map(item -> {
                        LogVersionBo logVersionBo = new LogVersionBo();
                        logVersionBo.setKey(item.getSampleInfoKey());
                        logVersionBo.setValueType(LogVersionValueType.STRING);
                        logVersionBo.setValue(item.getSampleInfoValue());
                        return logVersionBo;
                    })
                    .collect(Collectors.toList());
        }

        processOrderBaseService.createOrderChangeLog(processOrderPo.getProcessOrderId(), logVersionBos);
        // 刷新缺失信息
        processOrderBaseService.checkMissingInfo(processOrderNo);
        // 如果是limit 类型的并且来自仓库的加工单，需要通过 MQ 通知 wms
        if (ProcessOrderType.LIMITED.equals(processOrderPo.getProcessOrderType()) && ProcessOrderOriginal.WMS.equals(
                dto.getProcessOrderOriginal())) {
            processOrderBaseService.createProcessWaveResultMq(processOrderPo);
        }
        return processOrderPo;
    }


    /**
     * 通过 wms 创建加工单
     *
     * @param message
     */
    @RedisLock(prefix = ScmRedisConstant.PROCESS_ORDER_CREATE_LOCK_PREFIX, key = "#message.processWaveId", waitTime =
            1, leaseTime = -1)
    public void createLimitedProcessOrder(ProcessWaveCreateMqDto message) {
        String skuCode = message.getSkuCode();
        Assert.isTrue(StringUtils.isNotBlank(this.deliveryWarehouseCode), () -> new BizException("未配置加工单原料发货仓库"));

        //通过sku&原料发货仓库获取生产信息
        ProcessOrderProductionInfoBo processOrderGenerateInfoBo
                = processOrderBaseService.getProcProdInfoBo(skuCode, this.deliveryWarehouseCode, message.getPlatCode());

        List<String> fileCodeList = new ArrayList<>();
        List<ProcessOrderProductionInfoBo.ProcessOrderMaterialBo> processOrderMaterialBoList = new ArrayList<>();
        List<ProcessOrderSampleBo> processOrderSampleBoList = new ArrayList<>();

        if (null != processOrderGenerateInfoBo) {
            fileCodeList = processOrderGenerateInfoBo.getFileCodeList();
            processOrderMaterialBoList = processOrderGenerateInfoBo.getProcessOrderMaterialBoList();
            processOrderSampleBoList = processOrderGenerateInfoBo.getProcessOrderSampleBoList();
        }

        ProcessOrderCreateDto.ProcessOrderItem processOrderItem = new ProcessOrderCreateDto.ProcessOrderItem();
        processOrderItem.setSku(message.getSkuCode());
        processOrderItem.setPurchasePrice(BigDecimal.valueOf(0));
        processOrderItem.setProcessNum(message.getAmount());
        List<ProcessOrderCreateDto.ProcessOrderItem> processOrderItems = List.of(processOrderItem);

        // 判断是否已经创建
        ProcessOrderPo poByProcessWaveId = processOrderDao.getByProcessWaveId(message.getProcessWaveId());
        Assert.isNull(poByProcessWaveId, () -> new BizException("该波次{},已经创建加工单", message.getProcessWaveId()));


        String spuBySku = plmRemoteService.getSpuBySku(skuCode);
        ProcessOrderCreateDto processOrderCreateDto = new ProcessOrderCreateDto();
        processOrderCreateDto.setSpu(spuBySku);
        processOrderCreateDto.setPlatform(message.getPlatCode());
        processOrderCreateDto.setWarehouseCode(message.getWarehouseCode());
        processOrderCreateDto.setWarehouseName(message.getWarehouseName());
        processOrderCreateDto.setWarehouseTypeList(List.of(message.getWarehouseType()));
        processOrderCreateDto.setDeliveryWarehouseCode(this.deliveryWarehouseCode);
        processOrderCreateDto.setDeliveryWarehouseName(this.deliveryWarehouseName);
        log.info("加工波次约定交期转CN时区前:{}", message.getDeliveryTime());
        processOrderCreateDto.setDeliverDate(
                TimeUtil.convertZone(message.getDeliveryTime(), TimeZoneId.UTC, TimeZoneId.CN)
                        .minusDays(1));
        log.info("加工波次约定交期转CN时区后:{}", processOrderCreateDto.getDeliverDate());
        processOrderCreateDto.setProcessOrderType(ProcessOrderType.LIMITED);
        processOrderCreateDto.setProcessOrderOriginal(ProcessOrderOriginal.WMS);
        processOrderCreateDto.setProcessWaveId(message.getProcessWaveId());
        processOrderCreateDto.setFileCodeList(fileCodeList);
        processOrderCreateDto.setProcessOrderItems(processOrderItems);

        //获取原料策略1：通过sku&原料发货仓库获取原料信息
        if (CollectionUtils.isNotEmpty(processOrderMaterialBoList)) {
            // 加工材料
            List<ProcessOrderCreateDto.ProcessOrderMaterial> processOrderMaterialList = processOrderMaterialBoList.stream().map(item -> {
                ProcessOrderCreateDto.ProcessOrderMaterial processOrderMaterial = new ProcessOrderCreateDto.ProcessOrderMaterial();
                processOrderMaterial.setSku(item.getSku());
                processOrderMaterial.setDeliveryNum(item.getDeliveryNum() * message.getAmount());

                //原料对照关系列表
                List<ProcessOrderMaterialCompareBo> procMaterialCompareBoList = item.getProcMaterialCompareBoList();
                List<ProcessOrderMaterialCompareDto> procMaterialCompareDtoList
                        = ProcessOrderBuilder.buildProcessOrderMaterialCompareDtoList(procMaterialCompareBoList);
                processOrderMaterial.setMaterialCompareDtoList(procMaterialCompareDtoList);
                return processOrderMaterial;
            }).collect(Collectors.toList());

            // 加工工序
            List<ProcessOrderCreateDto.ProcessOrderProcedure> processOrderProcedureList = new ArrayList<>();
            List<ProcessOrderCreateDto.ProcessOrderDesc> processOrderDescList = new ArrayList<>();
            if (processOrderGenerateInfoBo != null) {
                List<ProcessOrderProductionInfoBo.ProcessOrderProcedureBo> processOrderProcedureBoList
                        = Optional.ofNullable(processOrderGenerateInfoBo.getProcessOrderProcedureBoList())
                        .orElse(new ArrayList<>());
                if (CollectionUtils.isNotEmpty(processOrderProcedureBoList)) {
                    List<String> processCodes = processOrderProcedureBoList.stream()
                            .map(ProcessOrderProductionInfoBo.ProcessOrderProcedureBo::getProcessCode)
                            .collect(Collectors.toList());
                    List<ProcessPo> processPoList = processDao.getByProcessCodes(processCodes);
                    Map<String, List<ProcessPo>> groupedProcessPo = processPoList.stream()
                            .collect(Collectors.groupingBy(ProcessPo::getProcessCode));
                    for (int i = 0; i < processOrderProcedureBoList.size(); i++) {
                        ProcessOrderProductionInfoBo.ProcessOrderProcedureBo processOrderProcedureBo
                                = processOrderProcedureBoList.get(i);
                        ProcessOrderCreateDto.ProcessOrderProcedure processOrderProcedure
                                = new ProcessOrderCreateDto.ProcessOrderProcedure();
                        List<ProcessPo> processPos = groupedProcessPo.get(processOrderProcedureBo.getProcessCode());
                        Assert.isTrue(CollectionUtils.isNotEmpty(processPos), () -> new BizException("加工工序不存在"));
                        Optional<ProcessPo> firstProcessPo = processPos.stream()
                                .findFirst();
                        processOrderProcedure.setProcessId(firstProcessPo.get()
                                .getProcessId());
                        processOrderProcedure.setCommission(firstProcessPo.get()
                                .getCommission());
                        processOrderProcedure.setSort(i + 1);
                        processOrderProcedureList.add(processOrderProcedure);
                    }
                }

                // 加工描述
                List<ProcessOrderProductionInfoBo.ProcessOrderDescBo> processOrderDescBoList
                        = Optional.ofNullable(processOrderGenerateInfoBo.getProcessOrderDescBoList())
                        .orElse(new ArrayList<>());
                if (CollectionUtils.isNotEmpty(processOrderDescBoList)) {
                    for (ProcessOrderProductionInfoBo.ProcessOrderDescBo processOrderDescBo : processOrderDescBoList) {
                        ProcessOrderCreateDto.ProcessOrderDesc processOrderDesc
                                = new ProcessOrderCreateDto.ProcessOrderDesc();
                        processOrderDesc.setProcessDescName(processOrderDescBo.getProcessDescName());
                        processOrderDesc.setProcessDescValue(processOrderDescBo.getProcessDescValue());
                        processOrderDescList.add(processOrderDesc);
                    }
                }
            }

            processOrderCreateDto.setProcessOrderMaterials(processOrderMaterialList);
            processOrderCreateDto.setProcessOrderProcedures(processOrderProcedureList);
            processOrderCreateDto.setProcessOrderDescs(processOrderDescList);
        }

        //获取原料策略2：通过sku&原料发货仓库&加工数量获取原料信息
        List<ProcessOrderCreateDto.ProcessOrderMaterial> processOrderMaterials = processOrderCreateDto.getProcessOrderMaterials();
        if (CollectionUtils.isEmpty(processOrderMaterials)) {
            ParamValidUtils.requireNotBlank(processOrderCreateDto.getPlatform(), "查询生产资料信息失败！平台编码为空");
            String platform = processOrderCreateDto.getPlatform();
            List<SkuMaterialBo> skuMaterialBos
                    = processOrderBaseService.getSkuMaterialBos(skuCode, this.deliveryWarehouseCode, platform, null, message.getAmount());
            if (CollectionUtils.isNotEmpty(skuMaterialBos)) {
                List<ProcessOrderCreateDto.ProcessOrderMaterial> newProcessOrderMaterials = skuMaterialBos.stream().map(skuMaterialBo -> {
                    ProcessOrderCreateDto.ProcessOrderMaterial processOrderMaterial = new ProcessOrderCreateDto.ProcessOrderMaterial();
                    processOrderMaterial.setSku(skuMaterialBo.getSku());
                    processOrderMaterial.setDeliveryNum(skuMaterialBo.getSingleNum() * message.getAmount());

                    //原料对照关系列表
                    List<ProcessOrderMaterialCompareBo> procMaterialCompareBoList = skuMaterialBo.getProcessOrderMaterialCompareBoList();
                    List<ProcessOrderMaterialCompareDto> procMaterialCompareDtoList
                            = ProcessOrderBuilder.buildProcessOrderMaterialCompareDtoList(procMaterialCompareBoList);
                    processOrderMaterial.setMaterialCompareDtoList(procMaterialCompareDtoList);
                    return processOrderMaterial;
                }).collect(Collectors.toList());
                processOrderCreateDto.setProcessOrderMaterials(newProcessOrderMaterials);
            }
        }

        // 补充工序信息
        List<ProcessOrderCreateDto.ProcessOrderProcedure> processOrderProcedures
                = processOrderCreateDto.getProcessOrderProcedures();
        if (CollectionUtils.isEmpty(processOrderProcedures)) {
            List<SkuProcedureBo> skuProcedureBos = processOrderBaseService.getSkuProcedureBos(skuCode,
                    processOrderCreateDto.getDeliveryWarehouseCode(),
                    null);
            if (CollectionUtils.isNotEmpty(skuProcedureBos)) {
                List<ProcessOrderCreateDto.ProcessOrderProcedure> newProcessOrderProcedures = skuProcedureBos.stream()
                        .map(skuProcedureBo -> {
                            ProcessOrderCreateDto.ProcessOrderProcedure processOrderProcedure
                                    = new ProcessOrderCreateDto.ProcessOrderProcedure();
                            processOrderProcedure.setProcessId(skuProcedureBo.getProcessId());
                            processOrderProcedure.setCommission(skuProcedureBo.getCommission());
                            processOrderProcedure.setSort(skuProcedureBo.getSort());
                            return processOrderProcedure;
                        })
                        .collect(Collectors.toList());
                processOrderCreateDto.setProcessOrderProcedures(newProcessOrderProcedures);
            }
        }

        this.create(processOrderCreateDto, processOrderSampleBoList);
    }

    /**
     * 通过次品返工创建加工单
     *
     * @param dto
     */
    public ProcessOrderPo createReWorkingProcessOrder(ProcessOrderReWorkingDto dto) {
        String parentProcessOrderNo = dto.getParentProcessOrderNo();
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(parentProcessOrderNo);
        Assert.notNull(processOrderPo, () -> new BizException("加工单：{}不存在", parentProcessOrderNo));

        ProcessOrderCreateDto processOrderCreateDto = new ProcessOrderCreateDto();
        processOrderCreateDto.setSpu(processOrderPo.getSpu());
        processOrderCreateDto.setPlatform(processOrderPo.getPlatform());
        processOrderCreateDto.setWarehouseCode(dto.getWarehouseCode());
        processOrderCreateDto.setWarehouseName(dto.getWarehouseName());
        processOrderCreateDto.setWarehouseTypeList(dto.getWarehouseTypeList());
        processOrderCreateDto.setDeliveryWarehouseCode(dto.getDeliveryWarehouseCode());
        processOrderCreateDto.setDeliveryWarehouseName(dto.getDeliveryWarehouseName());
        processOrderCreateDto.setDeliverDate(dto.getDeliverDate());
        processOrderCreateDto.setOrderNo(processOrderPo.getOrderNo());
        processOrderCreateDto.setCustomerName(processOrderPo.getCustomerName());
        if (ProcessOrderType.LIMITED.equals(processOrderPo.getProcessOrderType()) || ProcessOrderType.LIMITED_REWORKING.equals(processOrderPo.getProcessOrderType())) {
            processOrderCreateDto.setProcessOrderType(ProcessOrderType.LIMITED_REWORKING);
        } else {
            processOrderCreateDto.setProcessOrderType(ProcessOrderType.REWORKING);
        }
        processOrderCreateDto.setProcessOrderOriginal(ProcessOrderOriginal.REWORKING);
        processOrderCreateDto.setProcessOrderNote(dto.getProcessOrderNote());
        processOrderCreateDto.setDeliveryNote(processOrderPo.getDeliveryNote());
        processOrderCreateDto.setParentProcessOrderNo(processOrderPo.getProcessOrderNo());
        if (StringUtils.isNotBlank(processOrderPo.getFileCode())) {
            String fileCode = processOrderPo.getFileCode();
            processOrderCreateDto.setFileCodeList(Arrays.asList(fileCode.split(",")));
        }

        // 成品
        List<ProcessOrderItemPo> processOrderItemPos = processOrderItemDao.getByProcessOrderNo(parentProcessOrderNo);
        Optional<ProcessOrderItemPo> firstProcessOrderItemOptional = processOrderItemPos.stream()
                .filter(item -> BooleanType.TRUE.equals(item.getIsFirst()))
                .findFirst();
        if (firstProcessOrderItemOptional.isEmpty()) {
            throw new BizException("加工产品不存在");
        }
        ProcessOrderItemPo processOrderItemPo = firstProcessOrderItemOptional.get();
        final String parentSkuBatchCode = processOrderItemPo.getSkuBatchCode();
        ProcessOrderCreateDto.ProcessOrderItem processOrderItem = new ProcessOrderCreateDto.ProcessOrderItem();
        processOrderItem.setSku(processOrderItemPo.getSku());
        processOrderItem.setPurchasePrice(processOrderItemPo.getPurchasePrice());
        processOrderItem.setProcessNum(dto.getProcessNum());
        List<ProcessOrderCreateDto.ProcessOrderItem> processOrderItems = List.of(processOrderItem);
        processOrderCreateDto.setProcessOrderItems(processOrderItems);

        List<ProcessOrderReWorkingDto.ProcessOrderProcedure> processOrderProcedures = dto.getProcessOrderProcedures();
        if (CollectionUtils.isNotEmpty(processOrderProcedures)) {
            List<ProcessOrderCreateDto.ProcessOrderProcedure> processOrderProcedureList
                    = processOrderProcedures.stream().map(item -> {
                ProcessOrderCreateDto.ProcessOrderProcedure processOrderProcedure = new ProcessOrderCreateDto.ProcessOrderProcedure();
                processOrderProcedure.setProcessId(item.getProcessId());
                processOrderProcedure.setCommission(item.getCommission());
                processOrderProcedure.setSort(item.getSort());
                return processOrderProcedure;
            }).collect(Collectors.toList());
            processOrderCreateDto.setProcessOrderProcedures(processOrderProcedureList);
        }

        List<ProcessOrderReWorkingDto.ProcessOrderMaterial> processOrderMaterials = dto.getProcessOrderMaterials();
        if (CollectionUtils.isNotEmpty(processOrderMaterials)) {
            List<ProcessOrderCreateDto.ProcessOrderMaterial> processOrderMaterialList = processOrderMaterials.stream().map(item -> {
                ProcessOrderCreateDto.ProcessOrderMaterial processOrderMaterial = new ProcessOrderCreateDto.ProcessOrderMaterial();
                processOrderMaterial.setDeliveryNum(item.getDeliveryNum());
                processOrderMaterial.setSku(item.getSku());
                return processOrderMaterial;
            }).collect(Collectors.toList());
            processOrderCreateDto.setProcessOrderMaterials(processOrderMaterialList);
        }

        // 获取生产信息
        List<ProcessOrderSamplePo> processOrderSamplePoList = processOrderSampleDao.getByProcessOrderNo(parentProcessOrderNo);
        List<ProcessOrderSampleBo> sampleChildOrderInfoVoList = processOrderSamplePoList.stream().map(item -> {
            ProcessOrderSampleBo sampleChildOrderInfoVo = new ProcessOrderSampleBo();
            sampleChildOrderInfoVo.setSampleChildOrderNo(item.getSampleChildOrderNo());
            sampleChildOrderInfoVo.setSampleInfoKey(item.getSampleInfoKey());
            sampleChildOrderInfoVo.setSampleInfoValue(item.getSampleInfoValue());
            return sampleChildOrderInfoVo;
        }).collect(Collectors.toList());

        //创建返工单
        ProcessOrderPo newProcessOrderPo = this.create(processOrderCreateDto, sampleChildOrderInfoVoList);

        // 返工订单复用原加工单明细的批次码，不再创建新批次码。
        List<ProcessOrderItemPo> processOrderItemPoList = processOrderItemDao.getByProcessOrderNo(newProcessOrderPo.getProcessOrderNo());
        processOrderItemPoList.forEach(item -> item.setSkuBatchCode(parentSkuBatchCode));
        if (CollectionUtils.isNotEmpty(processOrderItemPoList)) {
            processOrderItemDao.updateBatchByIdVersion(processOrderItemPoList);
        }

        // 存在原料，则需要创建原料出库单
        if (CollectionUtils.isNotEmpty(processOrderMaterials)) {
            List<ProcessOrderMaterialSkuBo> skuBos = processOrderMaterials.stream().map(item -> {
                ProcessOrderMaterialSkuBo processOrderMaterialSkuBo = new ProcessOrderMaterialSkuBo();
                processOrderMaterialSkuBo.setSku(item.getSku());
                processOrderMaterialSkuBo.setDeliveryNum(item.getDeliveryNum());
                return processOrderMaterialSkuBo;
            }).collect(Collectors.toList());

            // 创建原料出库单
            String warehouseCode = newProcessOrderPo.getWarehouseCode();
            String warehouseName = newProcessOrderPo.getWarehouseName();
            if (StringUtils.isNotBlank(newProcessOrderPo.getDeliveryWarehouseCode())) {
                warehouseCode = newProcessOrderPo.getDeliveryWarehouseCode();
            }
            if (StringUtils.isNotBlank(newProcessOrderPo.getDeliveryWarehouseName())) {
                warehouseName = newProcessOrderPo.getDeliveryWarehouseName();
            }

            //平台编码
            String platformCode = newProcessOrderPo.getPlatform();
            ProcessMaterialDetailPo processMaterialDetailPo
                    = processOrderBaseService.updateProcMaterialDeliveryInfo(newProcessOrderPo.getProcessOrderNo(), skuBos, warehouseCode, warehouseName, dto.getProductQuality(), platformCode);
            processMaterialDetailDao.insert(processMaterialDetailPo);

            // 更新加工原料出库单信息
            List<ProcessOrderMaterialPo> newProcessOrderMaterialList
                    = processOrderMaterialDao.getByProcessOrderNo(newProcessOrderPo.getProcessOrderNo());
            List<ProcessOrderMaterialPo> updateProcessOrderMaterialList = newProcessOrderMaterialList.stream()
                    .peek(item -> item.setDeliveryNo(processMaterialDetailPo.getDeliveryNo()))
                    .collect(Collectors.toList());
            processOrderMaterialDao.updateBatchByIdVersion(updateProcessOrderMaterialList);
        }
        return newProcessOrderPo;
    }

    /**
     * 检查加工单原料（已废弃）
     *
     * @param dto
     * @return
     */
    public ProcessOrderPo checkMaterialByWeb(ProcessOrderCheckMaterialDto dto) {
        String processOrderNo = dto.getProcessOrderNo();
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        Assert.notNull(processOrderPo, () -> new BizException("加工单{}不存在", processOrderNo));
        if (!ProcessOrderStatus.WAIT_ORDER.equals(processOrderPo.getProcessOrderStatus())) {
            throw new ParamIllegalException("加工单必须处于待下单状态");
        }

        if (!ProcessOrderType.LIMITED.equals(processOrderPo.getProcessOrderType())) {
            throw new ParamIllegalException("必须是limited类型的加工单");
        }

        this.checkMaterial(processOrderPo);

        return processOrderPo;
    }

    /**
     * 刷新加工原料 bom 信息
     *
     * @param dto
     * @return
     */
    public ProcessOrderPo refreshMaterialByWeb(ProcessOrderCheckMaterialDto dto) {
        String processOrderNo = dto.getProcessOrderNo();
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        Assert.notNull(processOrderPo, () -> new BizException("加工单{}不存在", processOrderNo));
        if (!ProcessOrderStatus.LACK.equals(processOrderPo.getProcessOrderStatus())) {
            throw new ParamIllegalException("加工单必须处于缺货状态");
        }

        if (!ProcessOrderType.LIMITED.equals(processOrderPo.getProcessOrderType())) {
            throw new ParamIllegalException("必须是limited类型的加工单");
        }

        this.checkMaterial(processOrderPo);

        return processOrderPo;
    }

    /**
     * 检查 limited 类型的加工单是否存在原料
     *
     * @param processOrderPo
     */
    public void checkMaterial(ProcessOrderPo processOrderPo) {
        List<ProcessOrderItemPo> processOrderItemPos
                = processOrderItemDao.getByProcessOrderNo(processOrderPo.getProcessOrderNo());
        if (CollectionUtils.isEmpty(processOrderItemPos)) {
            return;
        }
        // 过滤中首次创建的
        Optional<ProcessOrderItemPo> firstOrderItemOption = processOrderItemPos.stream()
                .filter(it -> BooleanType.TRUE.equals(it.getIsFirst()))
                .findFirst();
        Assert.isTrue(firstOrderItemOption.isPresent(), () -> new BizException("首次创建的 sku 不存在"));
        ProcessOrderItemPo processOrderItemPo = firstOrderItemOption.get();
        String sku = processOrderItemPo.getSku();
        String platform = ParamValidUtils.requireNotBlank(processOrderPo.getPlatform(), "刷新原料！平台编码为空");
        final ProcessOrderProductionInfoBo processOrderGenerateInfoBo
                = processOrderBaseService.getProcProdInfoBo(sku, processOrderPo.getDeliveryWarehouseCode(), platform, processOrderPo.getProductQuality());

        if (null == processOrderGenerateInfoBo) {
            return;
        }

        List<String> fileCodeList = processOrderGenerateInfoBo.getFileCodeList();

        final List<ProcessOrderProductionInfoBo.ProcessOrderMaterialBo> processOrderMaterialBoList
                = processOrderGenerateInfoBo.getProcessOrderMaterialBoList();
        if (CollectionUtils.isEmpty(processOrderMaterialBoList)) {
            return;
        }

        ProcessOrderCreateDto processOrderCreateDto = new ProcessOrderCreateDto();
        processOrderCreateDto.setFileCodeList(fileCodeList);

        // 加工材料
        List<ProcessOrderCreateDto.ProcessOrderMaterial> processOrderMaterialList = processOrderMaterialBoList.stream().map(item -> {
            ProcessOrderCreateDto.ProcessOrderMaterial processOrderMaterial = new ProcessOrderCreateDto.ProcessOrderMaterial();
            processOrderMaterial.setSku(item.getSku());
            int totalDeliveryNum = item.getDeliveryNum() * processOrderPo.getTotalProcessNum();
            processOrderMaterial.setDeliveryNum(totalDeliveryNum);

            List<ProcessOrderMaterialCompareBo> procMaterialCompareBoList = item.getProcMaterialCompareBoList();
            List<ProcessOrderMaterialCompareDto> procMaterialCompareDtoList
                    = ProcessOrderBuilder.buildProcessOrderMaterialCompareDtoList(procMaterialCompareBoList);
            processOrderMaterial.setMaterialCompareDtoList(procMaterialCompareDtoList);
            log.info("检查原料 原料sku=>{} 商品对照关系列表=>{}", item.getSku(), procMaterialCompareDtoList);
            return processOrderMaterial;
        }).collect(Collectors.toList());

        // 加工工序
        List<ProcessOrderProductionInfoBo.ProcessOrderProcedureBo> processOrderProcedureBoList
                = Optional.ofNullable(processOrderGenerateInfoBo.getProcessOrderProcedureBoList())
                .orElse(new ArrayList<>());
        List<ProcessOrderCreateDto.ProcessOrderProcedure> processOrderProcedureList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(processOrderProcedureBoList)) {
            List<String> processCodes = processOrderProcedureBoList.stream()
                    .map(ProcessOrderProductionInfoBo.ProcessOrderProcedureBo::getProcessCode)
                    .collect(Collectors.toList());
            List<ProcessPo> processPoList = processDao.getByProcessCodes(processCodes);
            Map<String, List<ProcessPo>> groupedProcessPo = processPoList.stream()
                    .collect(Collectors.groupingBy(ProcessPo::getProcessCode));
            for (int i = 0; i < processOrderProcedureBoList.size(); i++) {
                ProcessOrderProductionInfoBo.ProcessOrderProcedureBo processOrderProcedureBo
                        = processOrderProcedureBoList.get(i);
                ProcessOrderCreateDto.ProcessOrderProcedure processOrderProcedure
                        = new ProcessOrderCreateDto.ProcessOrderProcedure();
                List<ProcessPo> processPos = groupedProcessPo.get(processOrderProcedureBo.getProcessCode());
                Assert.isTrue(CollectionUtils.isNotEmpty(processPos), () -> new BizException("加工工序不存在"));
                Optional<ProcessPo> firstProcessPo = processPos.stream()
                        .findFirst();
                processOrderProcedure.setProcessId(firstProcessPo.get()
                        .getProcessId());
                processOrderProcedure.setCommission(firstProcessPo.get()
                        .getCommission());
                processOrderProcedure.setSort(i + 1);
                processOrderProcedureList.add(processOrderProcedure);
            }
        }

        // 加工描述
        List<ProcessOrderProductionInfoBo.ProcessOrderDescBo> processOrderDescBoList
                = Optional.ofNullable(processOrderGenerateInfoBo.getProcessOrderDescBoList())
                .orElse(new ArrayList<>());
        List<ProcessOrderCreateDto.ProcessOrderDesc> processOrderDescList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(processOrderDescBoList)) {
            processOrderDescBoList.forEach(processOrderDescBo -> {
                ProcessOrderCreateDto.ProcessOrderDesc processOrderDesc = new ProcessOrderCreateDto.ProcessOrderDesc();
                processOrderDesc.setProcessDescName(processOrderDescBo.getProcessDescName());
                processOrderDesc.setProcessDescValue(processOrderDescBo.getProcessDescValue());
                processOrderDescList.add(processOrderDesc);
            });
        }

        processOrderCreateDto.setProcessOrderMaterials(processOrderMaterialList);
        processOrderCreateDto.setProcessOrderProcedures(processOrderProcedureList);
        processOrderCreateDto.setProcessOrderDescs(processOrderDescList);
        processOrderCreateDto.setDeliveryWarehouseCode(this.deliveryWarehouseCode);
        processOrderCreateDto.setDeliveryWarehouseName(this.deliveryWarehouseName);

        //加工单生产信息
        final List<ProcessOrderSampleBo> processOrderSampleBoList
                = processOrderGenerateInfoBo.getProcessOrderSampleBoList();

        // 创建加工单详细信息
        processOrderCreateBaseService.createProcessOrderDetails(processOrderPo.getProcessOrderNo(),
                processOrderCreateDto
                , BooleanType.FALSE, BooleanType.TRUE, processOrderSampleBoList);


        // 再次检查库存状态
        this.checkStock(processOrderPo.getProcessOrderNo(), new OperatorUserBo());

    }

    /**
     * 检查加工单库存
     *
     * @param processOrderNo
     * @return
     */
    public ProcessOrderPo checkStock(String processOrderNo,
                                     OperatorUserBo operatorUserBo) {
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (null == processOrderPo) {
            throw new BizException("加工单不存在");
        }
        List<ProcessOrderMaterialPo> processOrderMaterialPos
                = processOrderMaterialDao.getByProcessOrderNo(processOrderNo);
        if (ProcessOrderType.EXTRA.equals(processOrderPo.getProcessOrderType())) {
            // 补单类型的加工单，无须检查库存
            return processOrderPo;
        }
        // 没有原料，则直接进入待投产
        if (CollectionUtils.isEmpty(processOrderMaterialPos)) {
            if (ProcessOrderType.NORMAL.equals(processOrderPo.getProcessOrderType()) ||
                    ProcessOrderType.OVERSEAS_REPAIR.equals(processOrderPo.getProcessOrderType()) ||
                    ProcessOrderType.WH.equals(processOrderPo.getProcessOrderType()) ||
                    ProcessOrderType.REPAIR.equals(processOrderPo.getProcessOrderType())) {
                this.handleProcessOrderWithWaitProcedure(processOrderPo, operatorUserBo);
            }
            return processOrderPo;
        }
        if (ProcessOrderStatus.WAIT_PRODUCE.equals(processOrderPo.getProcessOrderStatus())) {
            // 待投产，生成批次码
            List<ProcessOrderItemPo> processOrderItemPoList = processOrderItemDao.getByProcessOrderNo(processOrderNo);
            List<ProcessOrderItemPo> newProcessOrderItemPoList
                    = processOrderBaseService.batchCreatePoiBatchCode(processOrderPo.getProcessOrderNo(),
                    processOrderItemPoList);
            processOrderCreateBaseService.updateProcessOrderSkuBatchCode(newProcessOrderItemPoList);
            return processOrderPo;

        }

        // 只有待下单以及缺货状态的加工单才需要检查库存
        String warehouseCode = processOrderPo.getWarehouseCode();
        if (StringUtils.isNotBlank(processOrderPo.getDeliveryWarehouseCode())) {
            warehouseCode = processOrderPo.getDeliveryWarehouseCode();
        }
        boolean checkStatus
                = processOrderBaseService.checkProcessOrderStatusAndMissInformation(processOrderPo,
                ProcessOrderStatus.WAIT_READY,
                MissingInformation.OUT_OF_STOCK);
        if (checkStatus || ProcessOrderStatus.LACK.equals(processOrderPo.getProcessOrderStatus())) {
            AtomicReference<Boolean> result = new AtomicReference<>(true);
            List<String> skus = processOrderMaterialPos.stream()
                    .map(ProcessOrderMaterialPo::getSku)
                    .collect(Collectors.toList());
            SkuInstockInventoryQueryDto skuInstockInventoryQueryDto = new SkuInstockInventoryQueryDto();
            skuInstockInventoryQueryDto.setWarehouseCode(warehouseCode);
            skuInstockInventoryQueryDto.setSkuCodes(skus);
            skuInstockInventoryQueryDto.setDeliveryType(WmsEnum.DeliveryType.PROCESS);
            skuInstockInventoryQueryDto.setProductQuality(WmsEnum.ProductQuality.GOOD);
            List<SkuInventoryVo> skuInventoryList = wmsRemoteService.getSkuInventoryList(skuInstockInventoryQueryDto);
            if (CollectionUtils.isEmpty(skuInventoryList)) {
                result.set(false);
            } else {
                // 判断是否有缺货的
                List<String> skuList = skuInventoryList.stream()
                        .map(SkuInventoryVo::getSkuCode)
                        .collect(Collectors.toList());
                processOrderMaterialPos.forEach(item -> {
                    if (!skuList.contains(item.getSku())) {
                        result.set(false);
                    }
                });

                skuInventoryList.forEach(item -> {
                    Integer inStockAmount = item.getInStockAmount();
                    int sum = processOrderMaterialPos.stream()
                            .filter(it -> it.getSku()
                                    .equals(item.getSkuCode()))
                            .mapToInt(ProcessOrderMaterialPo::getDeliveryNum)
                            .sum();
                    if (inStockAmount < sum) {
                        result.set(false);
                    }
                });
            }


            if (result.get()) {
                this.handleProcessOrderWithWaitProcedure(processOrderPo, operatorUserBo);
            } else {
                // 库存不足
                processOrderBaseService.changeStatus(processOrderPo, ProcessOrderStatus.WAIT_READY, operatorUserBo);
                processOrderBaseService.addMissInformation(processOrderPo.getProcessOrderNo(),
                        Set.of(MissingInformation.OUT_OF_STOCK));
            }


            return processOrderPo;
        }
        return processOrderPo;
    }

    /**
     * 处理待投产状态的加工单
     *
     * @param processOrderPo
     * @param operatorUserBo
     * @return
     */
    public ProcessOrderPo handleProcessOrderWithWaitProcedure(ProcessOrderPo processOrderPo,
                                                              OperatorUserBo operatorUserBo) {
        ProcessOrderStatus oldStatus = processOrderPo.getProcessOrderStatus();
        ProcessOrderStatus newStatus = ProcessOrderStatus.WAIT_PRODUCE;
        String processOrderNo = processOrderPo.getProcessOrderNo();

        String warehouseCode = processOrderPo.getWarehouseCode();
        if (StringUtils.isNotBlank(processOrderPo.getDeliveryWarehouseCode())) {
            warehouseCode = processOrderPo.getDeliveryWarehouseCode();
        }

        String warehouseName = processOrderPo.getWarehouseName();
        if (StringUtils.isNotBlank(processOrderPo.getDeliveryWarehouseName())) {
            warehouseName = processOrderPo.getDeliveryWarehouseName();
        }

        List<ProcessOrderMaterialPo> processOrderMaterialPos
                = processOrderMaterialDao.getByProcessOrderNo(processOrderNo);
        // 生成原料出库单，生成批次码
        ProcessMaterialDetailPo processMaterialDetailPo = null;
        if (CollectionUtils.isNotEmpty(processOrderMaterialPos) && newStatus.getSort() > oldStatus.getSort()) {
            List<ProcessOrderMaterialSkuBo> skuBos = processOrderMaterialPos.stream()
                    .map(item -> {
                        ProcessOrderMaterialSkuBo processOrderMaterialSkuBo = new ProcessOrderMaterialSkuBo();
                        processOrderMaterialSkuBo.setSku(item.getSku());
                        processOrderMaterialSkuBo.setDeliveryNum(item.getDeliveryNum());
                        return processOrderMaterialSkuBo;
                    })
                    .collect(Collectors.toList());
            // 创建原料出库单
            processMaterialDetailPo
                    = processOrderBaseService.updateProcMaterialDeliveryInfo(processOrderPo.getProcessOrderNo(),
                    skuBos, warehouseCode, warehouseName,
                    WmsEnum.ProductQuality.GOOD, processOrderPo.getPlatform());

            ProcessMaterialDetailPo finalProcessMaterialDetailPo = processMaterialDetailPo;
            processOrderMaterialPos = processOrderMaterialPos.stream()
                    .peek(item -> item.setDeliveryNo(finalProcessMaterialDetailPo.getDeliveryNo()))
                    .collect(Collectors.toList());
        }

        // 待投产，生成批次码
        List<ProcessOrderItemPo> processOrderItemPoList = processOrderItemDao.getByProcessOrderNo(processOrderNo);
        List<ProcessOrderItemPo> newProcessOrderItemPoList
                = processOrderBaseService.batchCreatePoiBatchCode(processOrderPo.getProcessOrderNo(),
                processOrderItemPoList);

        processOrderBaseService.changeStatusToWaitProcedure(processOrderPo, newProcessOrderItemPoList,
                processOrderMaterialPos, processMaterialDetailPo,
                operatorUserBo);

        return processOrderPo;
    }


    /**
     * 编辑加工单
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ProcessOrderPo edit(ProcessOrderEditDto dto) {
        final Long processOrderId = dto.getProcessOrderId();
        final Integer version = dto.getVersion();

        LocalDate curDate = TimeUtil.convertZone(LocalDateTime.now(), TimeZoneId.UTC, TimeZoneId.CN).toLocalDate();
        if (dto.getDeliverDate().toLocalDate().isBefore(curDate)
                || dto.getDeliverDate().toLocalDate().equals(curDate)) {
            throw new ParamIllegalException("期望上架时间限制不可小于等于当天");
        }

        ProcessOrderPo queriedProcessOrderPo = processOrderDao.getByProcessOrderIdAndVersion(processOrderId, version);
        if (Objects.isNull(queriedProcessOrderPo)) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }

        if (!ProcessOrderStatus.WAIT_READY.equals(queriedProcessOrderPo.getProcessOrderStatus())) {
            throw new ParamIllegalException("只有待齐备状态的加工单才能编辑");
        }

        // 待投产的 limited 类型订单不能编辑
        if (ProcessOrderStatus.WAIT_PLAN.equals(
                queriedProcessOrderPo.getProcessOrderStatus()) && ProcessOrderType.LIMITED.equals(
                queriedProcessOrderPo.getProcessOrderType())) {
            throw new ParamIllegalException("待投产状态的 limited 加工单不能编辑");
        }

        // 补单类型的加工单不能选原料
        if (ProcessOrderType.EXTRA.equals(dto.getProcessOrderType()) && CollectionUtils.isNotEmpty(
                dto.getProcessOrderMaterials())) {
            throw new ParamIllegalException("补单类型的加工单不能选择原料");
        }


        // limit 类型只允许编辑收货仓库，发货仓库，  业务约定交期
        if (ProcessOrderType.LIMITED.equals(queriedProcessOrderPo.getProcessOrderType())) {
            queriedProcessOrderPo.setWarehouseCode(dto.getWarehouseCode());
            queriedProcessOrderPo.setWarehouseName(dto.getWarehouseName());
            queriedProcessOrderPo.setWarehouseTypes(String.join(",", Optional.ofNullable(dto.getWarehouseTypeList())
                    .orElse(new ArrayList<>())));
            queriedProcessOrderPo.setDeliveryWarehouseCode(dto.getDeliveryWarehouseCode());
            queriedProcessOrderPo.setDeliveryWarehouseName(dto.getDeliveryWarehouseName());
            queriedProcessOrderPo.setDeliverDate(ScmTimeUtil.setToEndOfDay(dto.getDeliverDate()));
            processOrderDao.updateByIdVersion(queriedProcessOrderPo);
            return queriedProcessOrderPo;
        }

        List<ProcessOrderEditDto.ProcessOrderMaterial> processOrderMaterials = dto.getProcessOrderMaterials();

        ProcessOrderPo processOrderPo = ProcessOrderConverter.INSTANCE.convert(dto);
        processOrderPo.setDeliverDate(ScmTimeUtil.setToEndOfDay(dto.getDeliverDate()));
        processOrderPo.setWarehouseTypes(String.join(",", Optional.ofNullable(dto.getWarehouseTypeList())
                .orElse(new ArrayList<>())));

        List<ProcessOrderEditDto.ProcessOrderItem> processOrderItems = dto.getProcessOrderItems();
        int isFirstOrderItemSize = (int) processOrderItems.stream()
                .filter((it) -> BooleanType.TRUE.equals(it.getIsFirst()))
                .count();
        // 加工 sku 只能有一个
        if (isFirstOrderItemSize > 1) {
            throw new ParamIllegalException("加工单sku只能有一个");
        }


        List<String> fileCodeList = dto.getFileCodeList();
        if (CollectionUtils.isNotEmpty(fileCodeList)) {
            processOrderPo.setFileCode(String.join(",", fileCodeList));
        }

        // 总 sku 数量
        Integer totalSkuNum = processOrderItems.size();

        // 总加工数量
        int totalProcessNum = processOrderItems.stream()
                .mapToInt(ProcessOrderEditDto.ProcessOrderItem::getProcessNum)
                .sum();
        if (totalProcessNum > 10) {
            throw new ParamIllegalException("加工数量不能超过 10 个");
        }
        processOrderPo.setTotalSkuNum(totalSkuNum);
        processOrderPo.setTotalProcessNum(totalProcessNum);

        if (!ProcessOrderStatus.WAIT_PRODUCE.equals(processOrderPo.getProcessOrderStatus())) {
            if (CollectionUtils.isNotEmpty(processOrderMaterials)) {
                processOrderPo.setMaterialBackStatus(MaterialBackStatus.UN_BACK);
            } else {
                processOrderPo.setMaterialBackStatus(MaterialBackStatus.NO_BACK);
            }
        }


        processOrderDao.updateByIdVersion(processOrderPo);

        // 处理加工明细
        List<ProcessOrderItemPo> queriedProcessOrderItemPos
                = processOrderItemDao.getByProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
        Map<Long, ProcessOrderItemPo> groupedItemPos = queriedProcessOrderItemPos.stream()
                .collect(Collectors.toMap(ProcessOrderItemPo::getProcessOrderItemId, item -> item));
        List<ProcessOrderItemPo> processOrderItemPos = processOrderItems.stream()
                .map((item) -> {
                    ProcessOrderItemPo processOrderItemPo = new ProcessOrderItemPo();
                    processOrderItemPo.setProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
                    processOrderItemPo.setProcessNum(item.getProcessNum());
                    processOrderItemPo.setSku(item.getSku());
                    processOrderItemPo.setVariantProperties(item.getVariantProperties());
                    processOrderItemPo.setPurchasePrice(item.getPurchasePrice());
                    processOrderItemPo.setProcessOrderItemId(item.getProcessOrderItemId());
                    processOrderItemPo.setVersion(item.getVersion());
                    ProcessOrderItemPo newProcessOrderItemPo = groupedItemPos.get(item.getProcessOrderItemId());
                    if (null != newProcessOrderItemPo && !item.getSku()
                            .equals(newProcessOrderItemPo.getSku())) {
                        processOrderItemPo.setSkuBatchCode("");
                    }
                    BooleanType isFirst = item.getIsFirst();
                    if (null == isFirst) {
                        isFirst = BooleanType.TRUE;
                    }
                    processOrderItemPo.setIsFirst(isFirst);

                    return processOrderItemPo;
                })
                .collect(Collectors.toList());


        CompareResult<ProcessOrderItemPo> orderItemResult
                = DataCompareUtil.compare(processOrderItemPos, queriedProcessOrderItemPos,
                ProcessOrderItemPo::getProcessOrderItemId);
        processOrderItemDao.insertBatch(orderItemResult.getNewItems());
        processOrderItemDao.updateBatchByIdVersion(orderItemResult.getExistingItems());
        processOrderItemDao.removeBatchByIds(orderItemResult.getDeletedItems());

        // 待投产的订单不允许修改原料信息
        List<ProcessOrderMaterialPo> queriedProcessOrderMaterialPos
                = processOrderMaterialDao.getByProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
        if (!ProcessOrderStatus.WAIT_PRODUCE.equals(queriedProcessOrderPo.getProcessOrderStatus())) {
            // 处理原料产品明细
            List<ProcessOrderMaterialPo> processOrderMaterialPos = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(processOrderMaterials)) {
                if (StringUtils.isBlank(dto.getDeliveryWarehouseCode()) || StringUtils.isBlank(
                        dto.getDeliveryWarehouseName())) {
                    throw new ParamIllegalException("原料发货仓库必填");
                }
                processOrderMaterialPos = processOrderMaterials.stream()
                        .map((item) -> {
                            ProcessOrderMaterialPo processOrderMaterialPo = new ProcessOrderMaterialPo();
                            processOrderMaterialPo.setProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
                            processOrderMaterialPo.setSku(item.getSku());
                            processOrderMaterialPo.setDeliveryNum(item.getDeliveryNum());
                            processOrderMaterialPo.setProcessOrderMaterialId(item.getProcessOrderMaterialId());
                            processOrderMaterialPo.setVersion(item.getVersion());
                            processOrderMaterialPo.setCreateType(CreateType.CREATE);
                            return processOrderMaterialPo;
                        })
                        .collect(Collectors.toList());
            }
            CompareResult<ProcessOrderMaterialPo> orderMaterialCompare
                    = DataCompareUtil.compare(processOrderMaterialPos, queriedProcessOrderMaterialPos,
                    ProcessOrderMaterialPo::getProcessOrderMaterialId);
            processOrderMaterialDao.insertBatch(orderMaterialCompare.getNewItems());
            processOrderMaterialDao.updateBatchByIdVersion(orderMaterialCompare.getExistingItems());
            processOrderMaterialDao.removeBatchByIds(orderMaterialCompare.getDeletedItems());
        }


        // 处理加工工序
        List<ProcessOrderEditDto.ProcessOrderProcedure> processOrderProcedures = dto.getProcessOrderProcedures();
        if (CollectionUtils.isEmpty(processOrderProcedures)) {
            throw new ParamIllegalException("加工工序不能为空");
        }
        List<Long> processIds = processOrderProcedures.stream()
                .map(ProcessOrderEditDto.ProcessOrderProcedure::getProcessId)
                .collect(Collectors.toList());
        Map<Long, List<ProcessPo>> groupedProcessPos = processDao.getByProcessIds(processIds)
                .stream()
                .collect(Collectors.groupingBy(ProcessPo::getProcessId));
        if (null == groupedProcessPos) {
            throw new BizException("工序选择错误");
        }
        processOrderProcedures.forEach((item) -> {
            List<ProcessPo> processPos = groupedProcessPos.get(item.getProcessId());
            if (CollectionUtils.isEmpty(processPos)) {
                throw new BizException("工序选择错误");
            }
        });
        List<ProcessOrderProcedurePo> processOrderProcedurePos = processOrderProcedures.stream()
                .map((item) -> {
                    List<ProcessPo> processPos = groupedProcessPos.get(item.getProcessId());

                    ProcessOrderProcedurePo processOrderProcedurePo = new ProcessOrderProcedurePo();
                    ProcessPo processPo = processPos.get(0);
                    if (ProcessStatus.DISABLED.equals(processPo.getProcessStatus())) {
                        throw new ParamIllegalException("工序：{},已被禁用", processPo.getProcessName());
                    }
                    processOrderProcedurePo.setProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
                    processOrderProcedurePo.setProcessId(processPo.getProcessId());
                    processOrderProcedurePo.setProcessCode(processPo.getProcessCode());
                    processOrderProcedurePo.setProcessName(processPo.getProcessName());
                    processOrderProcedurePo.setSort(item.getSort());
                    processOrderProcedurePo.setCommission(item.getCommission());
                    processOrderProcedurePo.setProcessOrderProcedureId(item.getProcessOrderProcedureId());
                    processOrderProcedurePo.setProcessLabel(processPo.getProcessLabel());
                    processOrderProcedurePo.setVersion(item.getVersion());

                    return processOrderProcedurePo;
                })
                .collect(Collectors.toList());
        List<ProcessOrderProcedurePo> queriedProcessOrderProcedurePos
                = processOrderProcedureDao.getByProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
        CompareResult<ProcessOrderProcedurePo> orderProcedureCompare
                = DataCompareUtil.compare(processOrderProcedurePos, queriedProcessOrderProcedurePos,
                ProcessOrderProcedurePo::getProcessOrderProcedureId);
        processOrderProcedureDao.insertBatch(orderProcedureCompare.getNewItems());
        processOrderProcedureDao.updateBatchByIdVersion(orderProcedureCompare.getExistingItems());
        processOrderProcedureDao.removeBatchByIds(orderProcedureCompare.getDeletedItems());

        // 处理加工描述
        List<ProcessOrderEditDto.ProcessOrderDesc> processOrderDescs = dto.getProcessOrderDescs();
        if (CollectionUtils.isNotEmpty(processOrderDescs)) {
            List<ProcessOrderDescPo> processOrderDescPos = processOrderDescs.stream()
                    .map((item) -> {
                        ProcessOrderDescPo processOrderDescPo = new ProcessOrderDescPo();
                        processOrderDescPo.setProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
                        processOrderDescPo.setProcessDescName(item.getProcessDescName());
                        processOrderDescPo.setProcessDescValue(item.getProcessDescValue());
                        processOrderDescPo.setProcessOrderDescId(item.getProcessOrderDescId());
                        processOrderDescPo.setVersion(item.getVersion());
                        return processOrderDescPo;
                    })
                    .collect(Collectors.toList());
            List<ProcessOrderDescPo> queriedProcessOrderDescPos
                    = processOrderDescDao.getByProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
            CompareResult<ProcessOrderDescPo> orderDescCompare
                    = DataCompareUtil.compare(processOrderDescPos, queriedProcessOrderDescPos,
                    ProcessOrderDescPo::getProcessOrderDescId);
            processOrderDescDao.insertBatch(orderDescCompare.getNewItems());
            processOrderDescDao.updateBatchByIdVersion(orderDescCompare.getExistingItems());
            processOrderDescDao.removeBatchByIds(orderDescCompare.getDeletedItems());
        }

        Optional<ProcessOrderEditDto.ProcessOrderItem> firstOrderItemOptional = processOrderItems.stream()
                .findFirst();
        if (firstOrderItemOptional.isEmpty()) {
            throw new ParamIllegalException("请选择sku");
        }
        ProcessOrderEditDto.ProcessOrderItem processOrderItem = firstOrderItemOptional.get();

        String sku = processOrderItem.getSku();
        String platform = ParamValidUtils.requireNotBlank(processOrderPo.getPlatform(), "编辑失败！平台编码为空");
        ProcessOrderProductionInfoBo processOrderGenerateInfoBo
                = processOrderBaseService.getProcProdInfoBo(sku, processOrderPo.getDeliveryWarehouseCode(), platform, processOrderPo.getProductQuality());
        final List<ProcessOrderSampleBo> processOrderSampleBoList = processOrderGenerateInfoBo.getProcessOrderSampleBoList();

        List<ProcessOrderSamplePo> processOrderSamplePoList
                = processOrderSampleDao.getByProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
        if (CollectionUtils.isNotEmpty(processOrderSamplePoList)) {
            processOrderSampleDao.removeBatchByIds(processOrderSamplePoList);
        }
        // 处理加工生产信息
        List<ProcessOrderSamplePo> newProcessOrderSamplePoList = Optional.ofNullable(processOrderSampleBoList)
                .orElse(new ArrayList<>())
                .stream()
                .map((item) -> {
                    ProcessOrderSamplePo processOrderSamplePo = new ProcessOrderSamplePo();
                    processOrderSamplePo.setProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
                    processOrderSamplePo.setSampleChildOrderNo(item.getSampleChildOrderNo());
                    processOrderSamplePo.setSourceDocumentNumber(item.getSourceDocumentNumber());
                    processOrderSamplePo.setSampleInfoKey(item.getSampleInfoKey());
                    processOrderSamplePo.setSampleInfoValue(item.getSampleInfoValue());
                    return processOrderSamplePo;
                })
                .collect(Collectors.toList());
        processOrderSampleDao.insertBatch(newProcessOrderSamplePoList);


        ProcessOrderBo processOrderBo = new ProcessOrderBo();
        processOrderBo.setProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
        processOrderBo.setTraceId(TraceUtil.getUberTraceId());
        consistencyService.execAsyncTask(ProcessOrderHandler.class, processOrderBo);

        // 记录单据变化日志
        List<LogVersionBo> logVersionBos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(processOrderSampleBoList)) {
            logVersionBos = processOrderSampleBoList.stream()
                    .map(item -> {
                        LogVersionBo logVersionBo = new LogVersionBo();
                        logVersionBo.setKey(item.getSampleInfoKey());
                        logVersionBo.setValueType(LogVersionValueType.STRING);
                        logVersionBo.setValue(item.getSampleInfoValue());
                        return logVersionBo;
                    })
                    .collect(Collectors.toList());
        }
        processOrderBaseService.createOrderChangeLog(processOrderId, logVersionBos);


        return processOrderPo;
    }

    /**
     * 取消加工单
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean remove(ProcessOrderRemoveDto dto) {
        Long processOrderId = dto.getProcessOrderId();
        ProcessOrderPo queriedProcessOrderPo = processOrderDao.getByProcessOrderId(processOrderId);
        if (queriedProcessOrderPo == null) {
            throw new BizException("加工单不存在");
        }
        if (!ProcessOrderStatus.WAIT_ORDER.equals(queriedProcessOrderPo.getProcessOrderStatus())
                && !ProcessOrderStatus.LACK.equals(queriedProcessOrderPo.getProcessOrderStatus())) {
            throw new ParamIllegalException("只能待下单以及缺货状态的加工单才能取消");
        }

        if (ProcessOrderType.LIMITED.equals(queriedProcessOrderPo.getProcessOrderType())
                || ProcessOrderType.LIMITED_REWORKING.equals(queriedProcessOrderPo.getProcessOrderType())) {
            throw new ParamIllegalException("limited 状态的加工单不能作废");
        }

        boolean removeResult = processOrderDao.removeByIdVersion(processOrderId, dto.getVersion());

        if (removeResult) {
            ProcessOrderExtraPo processOrderExtraPo
                    = processOrderExtraDao.getByProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
            if (processOrderExtraPo != null) {
                processOrderExtraDao.removeByIdVersion(processOrderExtraPo);
            }

            List<ProcessOrderItemPo> processOrderItemPos
                    = processOrderItemDao.getByProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
            if (CollectionUtils.isNotEmpty(processOrderItemPos)) {
                processOrderItemDao.removeBatchByIds(processOrderItemPos);
            }
            List<ProcessOrderMaterialPo> processOrderMaterialPos
                    = processOrderMaterialDao.getByProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
            if (CollectionUtils.isNotEmpty(processOrderMaterialPos)) {
                processOrderMaterialDao.removeBatchByIds(processOrderMaterialPos);
            }
            List<ProcessOrderProcedurePo> processOrderProcedurePos
                    = processOrderProcedureDao.getByProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
            if (CollectionUtils.isNotEmpty(processOrderProcedurePos)) {
                processOrderProcedureDao.removeBatchByIds(processOrderProcedurePos);
            }
            List<ProcessOrderDescPo> processOrderDescPos
                    = processOrderDescDao.getByProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
            if (CollectionUtils.isNotEmpty(processOrderDescPos)) {
                processOrderDescDao.removeBatchByIds(processOrderDescPos);
            }

        }

        return true;
    }

    /**
     * 编辑工序
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean editProcedure(ProcessOrderAddProcedureDto dto) {
        final Long processOrderId = dto.getProcessOrderId();
        ProcessOrderPo queriedProcessOrderPo = processOrderDao.getByProcessOrderId(processOrderId);
        if (null == queriedProcessOrderPo) {
            throw new BizException("加工单不存在");
        }
        final String processOrderNo = queriedProcessOrderPo.getProcessOrderNo();

        // 校验加工单状态
        final ProcessOrderStatus processOrderStatus = queriedProcessOrderPo.getProcessOrderStatus();
        List<ProcessOrderStatus> canEditStatus
                = Arrays.asList(ProcessOrderStatus.WAIT_PLAN, ProcessOrderStatus.WAIT_PRODUCE,
                ProcessOrderStatus.PROCESSING, ProcessOrderStatus.WAIT_MOVING);
        if (!canEditStatus.contains(processOrderStatus)) {
            throw new ParamIllegalException("只有 {} 状态方可编辑", canEditStatus.stream()
                    .map(ProcessOrderStatus::getRemark)
                    .collect(Collectors.joining(",")));
        }

        // 校验加工数量是否更改
        final Integer processNum = dto.getProcessNum();
        if (!processNum.equals(queriedProcessOrderPo.getTotalProcessNum())) {
            Assert.isFalse(ProcessOrderType.LIMITED.equals(
                            queriedProcessOrderPo.getProcessOrderType()) || ProcessOrderType.LIMITED_REWORKING.equals(
                            queriedProcessOrderPo.getProcessOrderType()),
                    () -> new ParamIllegalException("limited 类型的不允许编辑加工数量"));
            queriedProcessOrderPo.setTotalProcessNum(processNum);

            Optional<ProcessOrderItemPo> orderItemFirstOptional
                    = processOrderItemDao.getByProcessOrderNo(processOrderNo)
                    .stream()
                    .findFirst();
            if (orderItemFirstOptional.isPresent()) {
                ProcessOrderItemPo processOrderItemPo = orderItemFirstOptional.get();
                processOrderItemPo.setProcessNum(processNum);
                processOrderItemDao.updateByIdVersion(processOrderItemPo);

                queriedProcessOrderPo.setTotalProcessNum(processNum);
                processOrderDao.updateByIdVersion(queriedProcessOrderPo);
            }
        }

        Integer sort = 0;
        List<ProcessOrderProcedurePo> existingProcedurePos
                = processOrderProcedureDao.getByProcessOrderNo(processOrderNo);
        if (CollectionUtils.isNotEmpty(existingProcedurePos)) {
            List<Integer> orderProcedures = existingProcedurePos.stream()
                    .map(ProcessOrderProcedurePo::getSort)
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(orderProcedures)) {
                sort = Collections.max(orderProcedures);
            }
        }


        // 处理加工工序
        List<ProcessOrderAddProcedureDto.ProcessOrderAddProcedure> processOrderProcedures
                = dto.getProcessOrderProcedures();
        List<Long> processIds = processOrderProcedures.stream()
                .map(ProcessOrderAddProcedureDto.ProcessOrderAddProcedure::getProcessId)
                .collect(Collectors.toList());
        List<ProcessPo> processPos = processDao.getByProcessIds(processIds);

        Integer finalSort = sort;
        List<ProcessOrderProcedurePo> modifiedProcedurePos = processOrderProcedures.stream()
                .map((item) -> {
                    ProcessOrderProcedurePo processOrderProcedurePo = new ProcessOrderProcedurePo();
                    Optional<ProcessPo> optionalProcessPo = processPos.stream()
                            .filter((it) -> it.getProcessId()
                                    .equals(item.getProcessId()))
                            .findFirst();

                    if (optionalProcessPo.isPresent()) {
                        int i = processOrderProcedures.indexOf(item);
                        ProcessPo processPo = optionalProcessPo.get();
                        if (ProcessStatus.DISABLED.equals(processPo.getProcessStatus())) {
                            throw new ParamIllegalException("工序：{},已被禁用", processPo.getProcessName());
                        }
                        processOrderProcedurePo.setProcessOrderProcedureId(item.getProcessOrderProcedureId());
                        processOrderProcedurePo.setVersion(item.getVersion());
                        processOrderProcedurePo.setProcessOrderNo(processOrderNo);
                        processOrderProcedurePo.setProcessCode(processPo.getProcessCode());
                        processOrderProcedurePo.setProcessId(processPo.getProcessId());
                        processOrderProcedurePo.setProcessName(processPo.getProcessName());
                        processOrderProcedurePo.setSort(finalSort + 1 + i);
                        processOrderProcedurePo.setProcessLabel(processPo.getProcessLabel());
                        processOrderProcedurePo.setCommission(processPo.getCommission());
                    } else {
                        throw new BizException("工序选择错误");
                    }
                    return processOrderProcedurePo;
                })
                .collect(Collectors.toList());


        CompareResult<ProcessOrderProcedurePo> orderProcedureCompare
                = DataCompareUtil.compare(modifiedProcedurePos, existingProcedurePos,
                ProcessOrderProcedurePo::getProcessOrderProcedureId);

        // 如果本次工序编辑存在变更，取消排产
        if (!processOrderBaseService.areListsEqual(existingProcedurePos, modifiedProcedurePos)) {
            processPlanBaseService.cancelProcessPlan(new CancelProcessPlanBo(processOrderNo));
        }

        // 需要删除的
        List<ProcessOrderProcedurePo> deletedItems = orderProcedureCompare.getDeletedItems();
        if (CollectionUtils.isNotEmpty(deletedItems)) {
            List<Long> processOrderProcedureIds = deletedItems.stream()
                    .map(ProcessOrderProcedurePo::getProcessOrderProcedureId)
                    .collect(Collectors.toList());

            List<ProcessOrderScanPo> byProcessOrderProcedureIds
                    = processOrderScanDao.getByProcessOrderProcedureIds(processOrderProcedureIds);

            if (CollectionUtils.isNotEmpty(byProcessOrderProcedureIds)) {
                throw new ParamIllegalException("已存在扫码记录，禁止删除");
            }
        }
        processOrderProcedureDao.insertBatch(orderProcedureCompare.getNewItems());
        processOrderProcedureDao.updateBatchByIdVersion(orderProcedureCompare.getExistingItems());
        processOrderProcedureDao.removeBatchByIds(orderProcedureCompare.getDeletedItems());

        // 完工待交接之后，需要判断是否需要回到加工中状态
        if (queriedProcessOrderPo.getProcessOrderStatus()
                .getSort()
                .equals(ProcessOrderStatus.WAIT_MOVING.getSort()) && CollectionUtils.isNotEmpty(
                orderProcedureCompare.getNewItems())) {
            queriedProcessOrderPo = processOrderDao.getByProcessOrderId(processOrderId);
            processOrderBaseService.changeStatus(queriedProcessOrderPo, ProcessOrderStatus.PROCESSING,
                    new OperatorUserBo());
        }


        // 需要更新的
        List<ProcessOrderProcedurePo> existingItems = orderProcedureCompare.getExistingItems();
        if (CollectionUtils.isEmpty(existingItems)) {
            return true;
        }
        Map<Long, List<ProcessPo>> groupedProcessPos = processPos.stream()
                .collect(Collectors.groupingBy(ProcessPo::getProcessId));
        List<Long> updateProcessOrderProcedureIds = existingItems.stream()
                .map(ProcessOrderProcedurePo::getProcessOrderProcedureId)
                .collect(Collectors.toList());

        List<ProcessOrderScanPo> processOrderScanPoList
                = processOrderScanDao.getByProcessOrderProcedureIds(updateProcessOrderProcedureIds);
        if (CollectionUtils.isEmpty(processOrderScanPoList)) {
            return true;
        }
        Map<Long, List<ProcessOrderProcedurePo>> groupedProcedurePoList = existingItems.stream()
                .collect(Collectors.groupingBy(ProcessOrderProcedurePo::getProcessOrderProcedureId));
        List<ProcessOrderScanPo> updateProcessOrderScanPoList = processOrderScanPoList.stream()
                .peek(it -> {
                    List<ProcessOrderProcedurePo> processOrderProcedurePosByProcedure
                            = groupedProcedurePoList.get(it.getProcessOrderProcedureId());
                    Optional<ProcessOrderProcedurePo> procedureFirstOptional
                            = processOrderProcedurePosByProcedure.stream()
                            .findFirst();
                    if (procedureFirstOptional.isPresent()) {
                        ProcessOrderProcedurePo processOrderProcedurePo = procedureFirstOptional.get();
                        List<ProcessPo> needProcessPos = groupedProcessPos.get(processOrderProcedurePo.getProcessId());
                        Optional<ProcessPo> processPoFirstOptional = needProcessPos.stream()
                                .findFirst();
                        if (processPoFirstOptional.isPresent()) {
                            ProcessPo processPo = processPoFirstOptional.get();
                            it.setProcessFirst(processPo.getProcessFirst());
                            it.setProcessLabel(processPo.getProcessLabel());
                            it.setProcessCode(processPo.getProcessCode());
                            it.setProcessName(processPo.getProcessName());
                            it.setProcessCommission(processPo.getCommission());
                            it.setExtraCommission(processPo.getExtraCommission());
                        }

                    }
                })
                .collect(Collectors.toList());

        processOrderScanDao.updateBatchByIdVersion(updateProcessOrderScanPoList);


        // 判断是否需要进入完工待交接
        List<ProcessOrderProcedurePo> allProcessOrderProcedurePos
                = processOrderProcedureDao.getByProcessOrderNo(processOrderNo);
        // 获取加工单所有的扫码记录
        List<ProcessOrderScanPo> allProcessOrderScanPos
                = Optional.ofNullable(processOrderScanDao.getByProcessOrderNo(processOrderNo))
                .orElse(new ArrayList<>());

        ArrayList<Long> completedProcedureIds = new ArrayList<>();
        allProcessOrderScanPos.forEach(item -> {
            if (null != item.getCompleteTime()) {
                completedProcedureIds.add(item.getProcessOrderProcedureId());
            }
        });
        // 过滤需要进行扫码的工序
        List<ProcessOrderProcedurePo> needScanProcedurePos = allProcessOrderProcedurePos.stream()
                .filter(it -> !completedProcedureIds.contains(it.getProcessOrderProcedureId()))
                .collect(Collectors.toList());
        OperatorUserBo operatorUserBo = new OperatorUserBo();
        if (CollectionUtils.isEmpty(needScanProcedurePos)) {
            // 没有需要进行处理的工序了，需要将加工单变成完工待交接
            queriedProcessOrderPo = processOrderDao.getByProcessOrderId(processOrderId);
            processOrderBaseService.changeStatus(queriedProcessOrderPo, ProcessOrderStatus.WAIT_MOVING, operatorUserBo);
        }
        return true;
    }

    /**
     * 进行次品赋码
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean addSku(ProcessOrderAddSkuDto dto) {
        Long processOrderId = dto.getProcessOrderId();
        ProcessOrderPo queriedProcessOrderPo = processOrderDao.getByProcessOrderId(processOrderId);
        if (null == queriedProcessOrderPo) {
            throw new BizException("加工单不存在");
        }
        if (!ProcessOrderStatus.WAIT_DELIVERY.equals(queriedProcessOrderPo.getProcessOrderStatus())) {
            throw new BizException("待发货状态订单才能进行次品赋码");
        }

        // 查询加工产品详情
        List<ProcessOrderItemPo> processOrderItemPos
                = processOrderItemDao.getByProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());

        // 过滤中首次创建的
        Optional<ProcessOrderItemPo> firstOrderItemOption = processOrderItemPos.stream()
                .filter(it -> BooleanType.TRUE.equals(it.getIsFirst()))
                .findFirst();
        if (firstOrderItemOption.isEmpty()) {
            throw new BizException("首次创建的 sku 不存在");
        }
        ProcessOrderItemPo processOrderItemPo = firstOrderItemOption.get();
        if (processOrderItemPo.getDefectiveGoodsCnt() < dto.getProcessNum()) {
            throw new ParamIllegalException("次品赋码数量不能大于系统次品数");
        }
        processOrderItemPo.setDefectiveGoodsCnt(processOrderItemPo.getDefectiveGoodsCnt() - dto.getProcessNum());
        processOrderItemDao.updateByIdVersion(processOrderItemPo);

        ProcessOrderItemPo newProcessOrderItemPo = new ProcessOrderItemPo();
        newProcessOrderItemPo.setSku(dto.getSku());
        newProcessOrderItemPo.setProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
        newProcessOrderItemPo.setProcessNum(dto.getProcessNum());
        newProcessOrderItemPo.setSkuBatchCode(dto.getSkuBatchCode());
        newProcessOrderItemPo.setVariantProperties(dto.getVariantProperties());
        newProcessOrderItemPo.setPurchasePrice(dto.getPurchasePrice());
        newProcessOrderItemPo.setQualityGoodsCnt(dto.getProcessNum());
        newProcessOrderItemPo.setIsFirst(BooleanType.FALSE);
        processOrderItemDao.insert(newProcessOrderItemPo);

        // 更新加工单数量
        queriedProcessOrderPo.setTotalProcessNum(queriedProcessOrderPo.getTotalProcessNum() + dto.getProcessNum());
        queriedProcessOrderPo.setTotalSkuNum(queriedProcessOrderPo.getTotalSkuNum() + 1);
        processOrderDao.updateByIdVersion(queriedProcessOrderPo);

        return true;
    }

    /**
     * 更新加工单状态
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean changeUp(ProcessOrderChangeStatusDto dto) {
        Long processOrderId = dto.getProcessOrderId();
        ProcessOrderPo queriedProcessOrderPo = processOrderDao.getByProcessOrderId(processOrderId);
        if (null == queriedProcessOrderPo) {
            throw new BizException("加工单不存在");
        }
        Integer sort = queriedProcessOrderPo.getProcessOrderStatus()
                .getSort();
//        ProcessOrderIsForward isForward = dto.getIsForward();
        sort++;

        // 限制最大以及最小的加工单状态
        if (sort >= ProcessOrderStatus.WAIT_DELIVERY.getSort()) {
            sort = ProcessOrderStatus.WAIT_DELIVERY.getSort();
        }
        if (sort <= ProcessOrderStatus.WAIT_ORDER.getSort()) {
            sort = ProcessOrderStatus.WAIT_ORDER.getSort();
        }

        // 如果是 limited 类型，同时不存在加工原料，则禁止改变状态
        if (ProcessOrderType.LIMITED.equals(
                queriedProcessOrderPo.getProcessOrderType()) && ProcessOrderStatus.WAIT_ORDER.equals(
                queriedProcessOrderPo.getProcessOrderStatus())) {
            throw new BizException("limited 类型待下单状态加工单不允许提交");
        }

        ProcessOrderStatus[] values = ProcessOrderStatus.values();

        Map<Integer, List<ProcessOrderStatus>> groupedStatusesBySort = Arrays.stream(values)
                .collect(Collectors.groupingBy(ProcessOrderStatus::getSort));
        Optional<ProcessOrderStatus> firstStatusOptional = groupedStatusesBySort.get(sort)
                .stream()
                .findFirst();
        if (firstStatusOptional.isEmpty()) {
            throw new BizException("加工单枚举值异常");
        }
        ProcessOrderStatus processOrderStatus = firstStatusOptional.get();

        if (sort.equals(ProcessOrderStatus.PROCESSING.getSort())) {
            // 获取加工工序
            List<ProcessOrderProcedurePo> processOrderProcedurePos
                    = processOrderProcedureDao.getByProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
            Optional<ProcessOrderProcedurePo> firstProcessOrderProcedurePoOptional = processOrderProcedurePos.stream()
                    .findFirst();
            // 获取加工产品详情
            List<ProcessOrderItemPo> processOrderItemPos
                    = processOrderItemDao.getByProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
            Optional<ProcessOrderItemPo> firstProcessOrderItemPoOptional = processOrderItemPos.stream()
                    .filter(item -> BooleanType.TRUE.equals(item.getIsFirst()))
                    .findFirst();
            if (firstProcessOrderItemPoOptional.isEmpty()) {
                throw new BizException("加工单产品详情不存在");
            }
            if (firstProcessOrderProcedurePoOptional.isPresent()) {
                ProcessOrderItemPo processOrderItemPo = firstProcessOrderItemPoOptional.get();
                ProcessOrderProcedurePo processOrderProcedurePo = firstProcessOrderProcedurePoOptional.get();
                queriedProcessOrderPo
                        = processOrderScanBaseService.confirmReceive(
                        processOrderProcedurePo.getProcessOrderProcedureId(), processOrderItemPo.getProcessNum());
            }
        } else if (sort.equals(ProcessOrderStatus.WAIT_MOVING.getSort())) {
            processOrderScanBaseService.completeScan(queriedProcessOrderPo.getProcessOrderNo());
        }
        OperatorUserBo operatorUserBo = new OperatorUserBo();
        processOrderBaseService.changeStatus(queriedProcessOrderPo, processOrderStatus, operatorUserBo);

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean quit(ProcessOrderRemoveDto dto) {
        final Long processOrderId = dto.getProcessOrderId();
        ProcessOrderPo quitProcessOrder = processOrderDao.getByProcessOrderId(processOrderId);
        if (Objects.isNull(quitProcessOrder)) {
            throw new ParamIllegalException("加工单信息不存在，请刷新页面！");
        }

        Set<ProcessOrderType> nonVoidableTypes
                = new HashSet<>(Arrays.asList(ProcessOrderType.LIMITED, ProcessOrderType.LIMITED_REWORKING));
        if (nonVoidableTypes.contains(quitProcessOrder.getProcessOrderType())) {
            throw new ParamIllegalException("limited 状态的加工单不能作废");
        }

        final NeedProcessPlan needProcessPlan = quitProcessOrder.getNeedProcessPlan();
        Set<ProcessOrderStatus> validStatuses;
        if (Objects.equals(NeedProcessPlan.TRUE, needProcessPlan)) {
            // 如果needProcessPlan为true，校验状态需要是待齐备或待排产
            validStatuses = new HashSet<>(Arrays.asList(ProcessOrderStatus.WAIT_READY, ProcessOrderStatus.WAIT_PLAN));
        } else {
            // 如果needProcessPlan为false，校验状态需要是待齐备、待排产或待投产
            validStatuses
                    = new HashSet<>(Arrays.asList(ProcessOrderStatus.WAIT_READY, ProcessOrderStatus.WAIT_PLAN,
                    ProcessOrderStatus.WAIT_PRODUCE));
        }
        if (!validStatuses.contains(quitProcessOrder.getProcessOrderStatus())) {
            throw new ParamIllegalException("{}的加工单 状态是：{} 方可作废", Objects.equals(NeedProcessPlan.TRUE,
                    needProcessPlan) ?
                    "需要排产" : "无需排产",
                    validStatuses.stream()
                            .map(ProcessOrderStatus::name)
                            .collect(Collectors.joining("/")));
        }

        Boolean result
                = processOrderBaseService.changeStatus(quitProcessOrder, ProcessOrderStatus.DELETED,
                new OperatorUserBo());
        // 通知wms取消出库单接口
        if (result) {
            final ProcessOrderCancelEventDto processOrderCancelEventDto = new ProcessOrderCancelEventDto();
            processOrderCancelEventDto.setProcessOrderNo(quitProcessOrder.getProcessOrderNo());
            processOrderCancelEventDto.setDeliveryType(WmsEnum.DeliveryType.PROCESS);
            processOrderCancelEventDto.setKey(quitProcessOrder.getProcessOrderNo());
            processOrderCancelEventDto.setOperator(GlobalContext.getUserKey());
            processOrderCancelEventDto.setOperatorName(GlobalContext.getUsername());
            consistencySendMqService.execSendMq(WmsProcessCancelHandler.class, processOrderCancelEventDto);
        }
        return true;
    }

    /**
     * 同步加工成品收货状态
     *
     * @param message
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.PROCESS_RECEIVE_STATUS_LOCK_PREFIX, key = "#message.scmBizNo", waitTime = 1,
            leaseTime = -1)
    public void syncReceiptStatus(ReceiveOrderChangeMqDto message) {
        WmsEnum.ReceiveOrderState receiveOrderState = message.getReceiveOrderState();
        // 加工单类型的
        ProcessOrderSyncStatusDto processOrderSyncStatusDto = new ProcessOrderSyncStatusDto();
        processOrderSyncStatusDto.setProcessOrderNo(message.getScmBizNo());
        processOrderSyncStatusDto.setNo(message.getReceiveOrderNo());
        processOrderSyncStatusDto.setOperator(message.getOperator());
        processOrderSyncStatusDto.setOperatorName(message.getOperatorName());
        // 待收货
        if (WmsEnum.ReceiveOrderState.WAIT_RECEIVE.equals(receiveOrderState)) {
            processOrderSyncStatusDto.setProcessOrderStatus(ProcessOrderStatus.WAIT_RECEIPT);
            this.syncProcessOrderStatus(processOrderSyncStatusDto);
        }
        if (WmsEnum.ReceiveOrderState.WAIT_ONSHELVES.equals(receiveOrderState)) {
            processOrderSyncStatusDto.setProcessOrderStatus(ProcessOrderStatus.WAIT_STORE);
            this.syncProcessOrderStatus(processOrderSyncStatusDto);
        }
        if (WmsEnum.ReceiveOrderState.ONSHELVESED.equals(receiveOrderState)) {
            processOrderSyncStatusDto.setProcessOrderStatus(ProcessOrderStatus.STORED);
            this.syncProcessOrderStatus(processOrderSyncStatusDto);
        }
    }

    /**
     * 同步次品收货
     *
     * @param message
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.PROCESS_RECEIVE_STATUS_LOCK_PREFIX, key = "#message.scmBizNo", waitTime = 1,
            leaseTime = -1)
    public void syncDefectiveReceiptMsg(ReceiveOrderChangeMqDto message) {
        String unionKey = message.getUnionKey();
        String key = null;
        if (StringUtils.isNotBlank(unionKey)) {
            String[] keyArray = unionKey.split(ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK);
            if (keyArray.length > 0) {
                key = keyArray[0];
            }
        }

        ProcessDefectiveRecordPo processDefectiveRecordPo = processDefectiveRecordDao.getByRecordNo(key);
        if (null != processDefectiveRecordPo) {
            processDefectiveRecordPo.setRelatedOrderNo(message.getReceiveOrderNo());
            processDefectiveRecordDao.updateByIdVersion(processDefectiveRecordPo);
        }

        // 更新原料归还收货单号
        ProcessMaterialBackPo processMaterialBackPo = processMaterialBackDao.getByMessageKey(key);
        if (null != processMaterialBackPo) {
            processMaterialBackPo.setReceiptNo(message.getReceiveOrderNo());
            processMaterialBackDao.updateByIdVersion(processMaterialBackPo);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.PROCESS_RECEIVE_STATUS_LOCK_PREFIX, key = "#message.receiveOrderNo",
            waitTime = 1, leaseTime = -1)
    public void syncMaterialReceiptMsg(ReceiveOrderChangeMqDto message) {
        String receiveOrderNo = message.getReceiveOrderNo();
        List<ReceiveOrderChangeMqDto.PurchaseReceiptSyncItemDto> purchaseReceiptSyncItemList
                = message.getPurchaseReceiptSyncItemList();
        ProcessMaterialBackPo processMaterialBackPo = processMaterialBackDao.getByReceiptNo(receiveOrderNo);
        String unionKey = message.getUnionKey();

        String key = null;
        if (StringUtils.isNotBlank(unionKey)) {
            String[] keyArray = unionKey.split(ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK);
            if (keyArray.length > 0) {
                key = keyArray[0];
            }
        }
        if (null == processMaterialBackPo && CollectionUtils.isEmpty(purchaseReceiptSyncItemList)) {
            if (StringUtils.isBlank(key)) {
                throw new BizException("唯一单据号不存在");
            }
            ProcessMaterialBackPo newProcessMaterialBackPo = processMaterialBackDao.getByMessageKey(key);
            newProcessMaterialBackPo.setReceiptNo(receiveOrderNo);
            processMaterialBackDao.updateByIdVersion(newProcessMaterialBackPo);
        }

        if (null == processMaterialBackPo && CollectionUtils.isNotEmpty(purchaseReceiptSyncItemList)) {
            if (StringUtils.isBlank(key)) {
                throw new BizException("唯一单据号不存在");
            }
            ProcessMaterialBackPo newProcessMaterialBackPo = processMaterialBackDao.getByMessageKey(key);
            newProcessMaterialBackPo.setReceiptNo(receiveOrderNo);
            newProcessMaterialBackPo.setReceiptUser(message.getOperator());
            newProcessMaterialBackPo.setReceiptUsername(message.getOperatorName());
            newProcessMaterialBackPo.setReceiptTime(new DateTime().toLocalDateTime());
            newProcessMaterialBackPo.setBackStatus(BackStatus.COMPLETED_BACK);

            processMaterialBackDao.updateByIdVersion(newProcessMaterialBackPo);

            List<ProcessMaterialBackItemPo> processMaterialBackItemPoList
                    = processMaterialBackItemDao.getByMaterialBackId(
                    newProcessMaterialBackPo.getProcessMaterialBackId());
            List<ProcessMaterialBackItemPo> newProcessMaterialBackItemPoList = processMaterialBackItemPoList.stream()
                    .map(item -> {
                        Optional<ReceiveOrderChangeMqDto.PurchaseReceiptSyncItemDto> first
                                = purchaseReceiptSyncItemList.stream()
                                .filter(it -> it.getBatchCode()
                                        .equals(item.getSkuBatchCode()))
                                .findFirst();
                        first.ifPresent(purchaseReceiptSyncItemDto -> item.setReceiptNum(
                                purchaseReceiptSyncItemDto.getReceiveAmount()));
                        return item;
                    })
                    .collect(Collectors.toList());
            processMaterialBackItemDao.updateBatchByIdVersion(newProcessMaterialBackItemPoList);

        }

        ProcessDefectiveRecordPo processDefectiveRecordPo = processDefectiveRecordDao.getByRecordNo(key);
        if (null != processDefectiveRecordPo) {
            processDefectiveRecordPo.setRelatedOrderNo(message.getReceiveOrderNo());
            processDefectiveRecordDao.updateByIdVersion(processDefectiveRecordPo);
        }

    }

    /**
     * 同步状态
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean syncProcessOrderStatus(ProcessOrderSyncStatusDto dto) {
        String processOrderNo = dto.getProcessOrderNo();
        ProcessOrderStatus processOrderStatus = dto.getProcessOrderStatus();
        ProcessOrderPo queriedProcessOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (null == queriedProcessOrderPo) {
            throw new BizException("加工单不存在");
        }
        if (processOrderStatus.getSort() < ProcessOrderStatus.WAIT_MOVING.getSort()) {
            throw new BizException("禁止同步");
        }

        OperatorUserBo operatorUserBo = new OperatorUserBo();
        // 后整质检中
        ProcessOrderExtraPo processOrderExtraPo
                = processOrderExtraDao.getByProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
        if (ProcessOrderStatus.CHECKING.equals(processOrderStatus)) {
            if (!ProcessOrderStatus.WAIT_MOVING.equals(queriedProcessOrderPo.getProcessOrderStatus())) {
                throw new BizException("加工单必须处于完工待交接状态");
            }
            if (StringUtils.isBlank(dto.getNo())) {
                throw new BizException("质检单号不存在");
            }
            processOrderExtraPo.setCheckOrderNo(dto.getNo());
            processOrderExtraPo.setCheckingUser(GlobalContext.getUserKey());
            processOrderExtraPo.setCheckingUsername(GlobalContext.getUsername());
            processOrderExtraPo.setCheckingTime(new DateTime().toLocalDateTime());
            operatorUserBo.setOperator(GlobalContext.getUserKey());
            operatorUserBo.setOperatorUsername(GlobalContext.getUsername());
        }

        // 待收货
        if (ProcessOrderStatus.WAIT_RECEIPT.equals(processOrderStatus)) {
            if (!ProcessOrderStatus.WAIT_DELIVERY.equals(queriedProcessOrderPo.getProcessOrderStatus())) {
                throw new BizException("加工单必须处于待发货状态");
            }
            if (StringUtils.isBlank(dto.getNo())) {
                throw new BizException("收货单号不存在");
            }

            String operatorUser = processOrderExtraPo.getDeliverUser();
            String operatorUsername = processOrderExtraPo.getDeliverUsername();

            processOrderExtraPo.setReceiptOrderNo(dto.getNo());
            operatorUserBo.setOperator(operatorUser);
            operatorUserBo.setOperatorUsername(operatorUsername);
        }

        // 待入库
        if (ProcessOrderStatus.WAIT_STORE.equals(processOrderStatus)) {
            if (!ProcessOrderStatus.WAIT_RECEIPT.equals(queriedProcessOrderPo.getProcessOrderStatus())) {
                throw new BizException("加工单必须处于待收货状态");
            }
            if (StringUtils.isBlank(dto.getNo())) {
                throw new BizException("入库单号不存在");
            }
            queriedProcessOrderPo.setReceiptTime(new DateTime().toLocalDateTime());
            processOrderExtraPo.setStoreOrderNo(dto.getNo());
            processOrderExtraPo.setReceiptUser(dto.getOperator());
            processOrderExtraPo.setReceiptUsername(dto.getOperatorName());
            operatorUserBo.setOperator(dto.getOperator());
            operatorUserBo.setOperatorUsername(dto.getOperatorName());
        }


        // 已入库
        if (ProcessOrderStatus.STORED.equals(processOrderStatus)) {
            if (ProcessOrderStatus.STORED.equals(queriedProcessOrderPo.getProcessOrderStatus())) {
                throw new BizException("加工单{}", ProcessOrderStatus.STORED.getDesc());
            }

            queriedProcessOrderPo.setStoredTime(new DateTime().toLocalDateTime());
            processOrderExtraPo.setStoreOrderNo(dto.getNo());
            processOrderExtraPo.setStoreUser(dto.getOperator());
            processOrderExtraPo.setStoreUsername(dto.getOperatorName());
            operatorUserBo.setOperator(dto.getOperator());
            operatorUserBo.setOperatorUsername(dto.getOperatorName());
        }
        queriedProcessOrderPo.setProcessOrderStatus(processOrderStatus);

        processOrderDao.updateByIdVersion(queriedProcessOrderPo);
        processOrderExtraDao.updateByIdVersion(processOrderExtraPo);

        // 记录状态变更日志
        processOrderBaseService.createStatusChangeLog(queriedProcessOrderPo, processOrderExtraPo, operatorUserBo);

        // 入库的时候需要发送钉钉通知
        if (ProcessOrderStatus.STORED.equals(processOrderStatus)) {
            this.sendStatusDingTalkMessage(queriedProcessOrderPo.getProcessOrderNo());
        }


        return true;
    }

    /**
     * 库存匹配
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ProcessOrderVo stockMatch(ProcessOrderStockMatchDto dto) {
        ProcessOrderPo processOrderPo = ParamValidUtils.requireNotNull(
                processOrderDao.getByProcessOrderIdAndVersion(dto.getProcessOrderId(), dto.getVersion()),
                "当前加工单信息已发生变更！请刷新页面后重试。"
        );
        final String processOrderNo = processOrderPo.getProcessOrderNo();
        processOrderBaseService.refreshMissingInfo(processOrderNo, new OperatorUserBo(),
                MissingInfoOperationType.PERFORM_MATERIAL_OPERATIONS, true);

        ProcessOrderPo newProcessOrderPo = processOrderDao.getByProcessOrderId(dto.getProcessOrderId());
        ProcessOrderVo processOrderVo = new ProcessOrderVo();
        processOrderVo.setProcessOrderId(newProcessOrderPo.getProcessOrderId());
        processOrderVo.setProcessOrderNo(newProcessOrderPo.getProcessOrderNo());
        processOrderVo.setProcessOrderStatus(newProcessOrderPo.getProcessOrderStatus());
        processOrderVo.setProcessOrderType(newProcessOrderPo.getProcessOrderType());
        processOrderVo.setVersion(newProcessOrderPo.getVersion());

        return processOrderVo;
    }

    /**
     * @Description 加工单完成质检创建收货单
     * @author yanjiawei
     * @Date 2024/11/13 17:09
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean confirmDeliver(ProcessOrderConfirmDeliverDto dto, OperatorUserBo operatorUserBo) {
        Long processOrderId = dto.getProcessOrderId();
        ProcessOrderPo queriedProcessOrderPo = processOrderDao.getByProcessOrderId(processOrderId);
        if (queriedProcessOrderPo == null) {
            throw new BizException("加工单不存在");
        }
        if (!ProcessOrderStatus.WAIT_DELIVERY.equals(queriedProcessOrderPo.getProcessOrderStatus())) {
            throw new BizException("加工单必须处于待发货状态");
        }
        List<ProcessOrderItemPo> processOrderItemPos
                = processOrderItemDao.getByProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());

        String operatorUser = GlobalContext.getUserKey();
        if (StringUtils.isNotBlank(operatorUserBo.getOperator())) {
            operatorUser = operatorUserBo.getOperator();
        }
        if (StringUtils.isBlank(operatorUser)) {
            operatorUser = ScmConstant.SYSTEM_USER;
        }

        String operatorUsername = GlobalContext.getUsername();
        if (StringUtils.isNotBlank(operatorUserBo.getOperatorUsername())) {
            operatorUsername = operatorUserBo.getOperatorUsername();
        }
        if (StringUtils.isBlank(operatorUsername)) {
            operatorUsername = ScmConstant.SYSTEM_USER;
        }

        ReceiveOrderCreateMqDto receiveOrderCreateMqDto = new ReceiveOrderCreateMqDto();
        ArrayList<ReceiveOrderCreateMqDto.ReceiveOrderCreateItem> receiveOrderCreateItems = new ArrayList<>();
        ArrayList<String> receiveOrderList = new ArrayList<>();
        receiveOrderList.add(ScmConstant.QUALITY_GOODS);

        String finalOperatorUser = operatorUser;
        String finalOperatorUsername = operatorUsername;
        String processOrderNo = queriedProcessOrderPo.getProcessOrderNo();
        String platform = ParamValidUtils.requireNotBlank(queriedProcessOrderPo.getPlatform(),
                StrUtil.format("创建收货单失败！加工单{}平台编码为空。", processOrderNo)
        );
        receiveOrderList.forEach(item -> {
            String key = idGenerateService.getSnowflakeCode(item);
            ReceiveOrderCreateMqDto.ReceiveOrderCreateItem receiveOrderCreateItem = new ReceiveOrderCreateMqDto.ReceiveOrderCreateItem();
            receiveOrderCreateItem.setReceiveType(ReceiveType.PROCESS_PRODUCT);
            receiveOrderCreateItem.setUnionKey(key + ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK + ScmBizReceiveOrderType.PROCESS_FINISHED_PRODUCT.name());
            if (ScmConstant.DEFECTIVE_GOODS.equals(item)) {
                receiveOrderCreateItem.setReceiveType(ReceiveType.DEFECTIVE_PROCESS_PRODUCT);
                receiveOrderCreateItem.setUnionKey(key + ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK + ScmBizReceiveOrderType.DEFECTIVE_PROCESS.name());
            }
            receiveOrderCreateItem.setSkuDevType(SkuDevType.NORMAL);
            // limited 类型的成品入库需要标记
            if (ProcessOrderType.LIMITED.equals(queriedProcessOrderPo.getProcessOrderType()) ||
                    ProcessOrderType.LIMITED_REWORKING.equals(queriedProcessOrderPo.getProcessOrderType())) {
                receiveOrderCreateItem.setSkuDevType(SkuDevType.LIMITED);
            }
            receiveOrderCreateItem.setQcType(WmsEnum.QcType.NOT_CHECK);
            receiveOrderCreateItem.setScmBizNo(queriedProcessOrderPo.getProcessOrderNo());
            receiveOrderCreateItem.setWarehouseCode(queriedProcessOrderPo.getWarehouseCode());
            if (ScmConstant.DEFECTIVE_GOODS.equals(item)) {
                receiveOrderCreateItem.setWarehouseCode(this.defectiveWarehouseCode);
            }
            receiveOrderCreateItem.setPurchaseOrderType(PurchaseOrderType.NORMAL);
            receiveOrderCreateItem.setIsUrgentOrder(BooleanType.FALSE);
            receiveOrderCreateItem.setIsDirectSend(BooleanType.FALSE);
            receiveOrderCreateItem.setIsNormalOrder(BooleanType.FALSE);
            receiveOrderCreateItem.setPlaceOrderTime(new DateTime().toLocalDateTime());
            receiveOrderCreateItem.setSendTime(new DateTime().toLocalDateTime());
            receiveOrderCreateItem.setOperator(finalOperatorUser);
            receiveOrderCreateItem.setOperatorName(finalOperatorUsername);

            List<ReceiveOrderCreateMqDto.ReceiveOrderDetail> receiveOrderDetails = processOrderItemPos.stream().map(it -> {
                ReceiveOrderCreateMqDto.ReceiveOrderDetail receiveOrderDetail = new ReceiveOrderCreateMqDto.ReceiveOrderDetail();
                receiveOrderDetail.setPlatCode(platform);
                receiveOrderDetail.setBatchCode(it.getSkuBatchCode());
                receiveOrderDetail.setSpu(queriedProcessOrderPo.getSpu());
                receiveOrderDetail.setSkuCode(it.getSku());
                receiveOrderDetail.setPurchaseAmount(it.getProcessNum());
                receiveOrderDetail.setDeliveryAmount(it.getQualityGoodsCnt());
                if (ScmConstant.DEFECTIVE_GOODS.equals(item)) {
                    receiveOrderDetail.setDeliveryAmount(it.getDefectiveGoodsCnt());
                }
                return receiveOrderDetail;
            }).collect(Collectors.toList());

            receiveOrderCreateItem.setDetailList(receiveOrderDetails);
            receiveOrderCreateItems.add(receiveOrderCreateItem);
        });

        receiveOrderCreateMqDto.setReceiveOrderCreateItemList(receiveOrderCreateItems);
        receiveOrderCreateMqDto.setKey(idGenerateService.getSnowflakeCode(queriedProcessOrderPo.getProcessOrderNo() + "-"));
        consistencySendMqService.execSendMq(WmsReceiptHandler.class, receiveOrderCreateMqDto);

        // 更新发货时间
        ProcessOrderExtraPo processOrderExtraPo = processOrderExtraDao.getByProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
        processOrderExtraPo.setDeliverTime(new DateTime().toLocalDateTime());
        processOrderExtraPo.setDeliverUser(operatorUser);
        processOrderExtraPo.setDeliverUsername(operatorUsername);
        processOrderExtraDao.updateByIdVersion(processOrderExtraPo);
        return true;
    }

    /**
     * 加工单完成交接并处理相关操作。
     *
     * @param dto 交接手续的数据传输对象，包含了完成交接所需的信息。
     *            包括加工单号、容器码、版本号等必要信息。
     * @throws BizException 如果加工单完成交接完成时发生问题，将抛出业务异常。
     */
    @Transactional(rollbackFor = Exception.class)
    public void completeHandover(ProcessOrderCompleteHandoverDto dto) {
        ProcessOrderCompleteHandoverListDto processOrderCompleteHandoverListDto
                = new ProcessOrderCompleteHandoverListDto();
        processOrderCompleteHandoverListDto.setProcessOrderList(Collections.singletonList(dto));
        this.batchCompleteHandover(processOrderCompleteHandoverListDto);
    }

    /**
     * 处理创建质检单的结果。
     *
     * @param processOrderCreateQcResult 质检单创建结果信息，包括质检单信息、加工单信息和其他相关数据。
     * @throws BizException 如果质检单创建失败或存在问题，将抛出业务异常。
     */
    public void handleQcOrderCreation(ProcessOrderCreateQcResultBo processOrderCreateQcResult) {
        final List<ProcessOrderCreateQcOrderResultBo> qcOrderResults = processOrderCreateQcResult.getQcOrderResultBos();

        // 参数校验
        if (qcOrderResults.stream()
                .anyMatch(result -> StringUtils.isBlank(result.getQcOrderNo()))) {
            log.error("processOrderCreateQcResult:[{}]", JSON.toJSONString(processOrderCreateQcResult));
            throw new BizException("加工质检单创建异常，存在空的质检单号");
        }
        final List<String> qcProcessOrderNos = qcOrderResults.stream()
                .map(ProcessOrderCreateQcOrderResultBo::getProcessOrderNo)
                .collect(Collectors.toList());

        // 加工单数据一致性校验
        final List<ProcessOrderPo> existProcessOrderPos = processOrderDao.getByProcessOrderNos(qcProcessOrderNos);
        if (CollectionUtils.isEmpty(existProcessOrderPos)) {
            throw new BizException("加工单不存在");
        }
        final List<String> existProcessOrderNos = existProcessOrderPos.stream()
                .map(ProcessOrderPo::getProcessOrderNo)
                .collect(Collectors.toList());
        boolean allExistInProcessOrder = new HashSet<>(qcProcessOrderNos).containsAll(existProcessOrderNos);
        if (!allExistInProcessOrder) {
            throw new BizException("创建质检单失败！加工单信息不存在，请刷新页面后重新处理。");
        }

        // 加工单拓展数据一致性校验
        final List<ProcessOrderExtraPo> processOrderExtraPoList
                = processOrderExtraDao.getByProcessOrderNos(qcProcessOrderNos);
        if (CollectionUtils.isEmpty(processOrderExtraPoList)) {
            throw new BizException("创建质检单失败！加工单拓展信息不存在，请刷新页面后重新处理。");
        }
        List<String> extraPoProcessOrderNos = processOrderExtraPoList.stream()
                .map(ProcessOrderExtraPo::getProcessOrderNo)
                .collect(Collectors.toList());
        boolean allExistInProcessOrderExtra = new HashSet<>(qcProcessOrderNos).containsAll(extraPoProcessOrderNos);
        if (!allExistInProcessOrderExtra) {
            throw new BizException("创建质检单失败！加工单拓展信息不存在，请刷新页面后或联系业务人员重新处理。");
        }

        // 加工单状态校验
        final List<ProcessOrderStatus> validStatus
                = Arrays.asList(ProcessOrderStatus.WAIT_MOVING, ProcessOrderStatus.REWORKING);
        boolean allValid = existProcessOrderPos.stream()
                .allMatch(order -> validStatus.contains(order.getProcessOrderStatus()));
        if (!allValid) {
            throw new BizException("创建质检单失败！加工单必须处于完工待交接或返工中状态");
        }

        // 更新加工单状态
        existProcessOrderPos.forEach(
                processOrderPo -> processOrderPo.setProcessOrderStatus(ProcessOrderStatus.CHECKING));
        processOrderDao.updateBatchByIdVersion(existProcessOrderPos);

        // 更新加工单质检信息
        for (ProcessOrderExtraPo processOrderExtraPo : processOrderExtraPoList) {
            final String processOrderNo = processOrderExtraPo.getProcessOrderNo();
            ProcessOrderCreateQcOrderResultBo matchQcResult = qcOrderResults.stream()
                    .filter(qcOrderResult -> Objects.equals(processOrderNo, qcOrderResult.getProcessOrderNo()))
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(matchQcResult)) {
                processOrderExtraPo.setCheckOrderNo(matchQcResult.getQcOrderNo());
                processOrderExtraPo.setCheckingUser(matchQcResult.getOperator());
                processOrderExtraPo.setCheckingUsername(matchQcResult.getOperatorName());
                processOrderExtraPo.setCheckingTime(LocalDateTime.now());
            }
        }
        processOrderExtraDao.updateBatchByIdVersion(processOrderExtraPoList);

        // 根据操作人保存加工单状态变更日志
        final String operator = qcOrderResults.stream()
                .map(ProcessOrderCreateQcOrderResultBo::getOperator)
                .filter(StrUtil::isNotBlank)
                .findFirst()
                .orElse("");
        final String operatorName = qcOrderResults.stream()
                .map(ProcessOrderCreateQcOrderResultBo::getOperatorName)
                .filter(StrUtil::isNotBlank)
                .findFirst()
                .orElse("");
        final OperatorUserBo operatorUserBo = new OperatorUserBo();
        operatorUserBo.setOperator(operator);
        operatorUserBo.setOperatorUsername(operatorName);
        for (ProcessOrderPo updateProcessOrderPo : existProcessOrderPos) {
            final String updateProcessOrderNo = updateProcessOrderPo.getProcessOrderNo();
            ProcessOrderExtraPo matchUpdateExtraPo = processOrderExtraPoList.stream()
                    .filter(processOrderExtraPo -> Objects.equals(updateProcessOrderNo,
                            processOrderExtraPo.getProcessOrderNo()))
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(matchUpdateExtraPo)) {
                processOrderBaseService.createStatusChangeLog(updateProcessOrderPo, matchUpdateExtraPo, operatorUserBo);
            }
        }
    }


    /**
     * 批量执行完工交接操作，包括校验、创建质检单、释放容器等步骤。
     *
     * @param dto 批量完工交接请求对象
     * @throws BizException 如果存在加工单不存在或不满足操作条件等情况，抛出业务异常
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchCompleteHandover(ProcessOrderCompleteHandoverListDto dto) {
        // 校验加工单信息是否存在
        final List<ProcessOrderCompleteHandoverDto> processOrderDtoList = dto.getProcessOrderList();
        final List<Long> processOrderIds = processOrderDtoList.stream()
                .map(ProcessOrderCompleteHandoverDto::getProcessOrderId)
                .collect(Collectors.toList());
        final List<ProcessOrderPo> existProcessOrderPoList = processOrderDao.getByProcessOrderIds(processOrderIds);
        if (CollectionUtils.isEmpty(existProcessOrderPoList)) {
            throw new BizException("加工单不存在");
        }
        final List<Long> existProcessOrderIds = existProcessOrderPoList.stream()
                .map(ProcessOrderPo::getProcessOrderId)
                .collect(Collectors.toList());
        if (processOrderDtoList.stream()
                .anyMatch(item -> !existProcessOrderIds.contains(item.getProcessOrderId()))) {
            throw new ParamIllegalException("数据被更新或者删除，请刷新页面！");
        }

        // 校验加工单状态
        StringBuilder errMsg = new StringBuilder();
        List<ProcessOrderStatus> completeStatusList = List.of(ProcessOrderStatus.CHECKING,
                ProcessOrderStatus.WAIT_DELIVERY, ProcessOrderStatus.WAIT_STORE, ProcessOrderStatus.STORED);
        List<ProcessOrderPo> filterCompleteProc = existProcessOrderPoList.stream()
                .filter(processOrderPo -> completeStatusList.contains(processOrderPo.getProcessOrderStatus()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(filterCompleteProc)) {
            String completeProcOrder = filterCompleteProc.stream()
                    .map(ProcessOrderPo::getProcessOrderNo)
                    .map(String::valueOf).collect(Collectors.joining(","));
            errMsg.append(StrUtil.format("加工单{}已完工交接，无法再次进行此操作", completeProcOrder));
        }

        List<ProcessOrderPo> processingProcList = existProcessOrderPoList.stream()
                .filter(processOrderPo -> Objects.equals(ProcessOrderStatus.PROCESSING, processOrderPo.getProcessOrderStatus()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(processingProcList)) {
            String processingProcOrder = processingProcList.stream()
                    .map(ProcessOrderPo::getProcessOrderNo)
                    .map(String::valueOf).collect(Collectors.joining(","));
            errMsg.append(StrUtil.format("加工单{}未加工完成，无法再次进行此操作", processingProcOrder));
        }
        if (errMsg.length() > 0) {
            throw new ParamIllegalException(errMsg.toString());
        }

        final List<ProcessOrderStatus> canOperaStatus
                = Arrays.asList(ProcessOrderStatus.WAIT_MOVING, ProcessOrderStatus.REWORKING);
        if (!existProcessOrderPoList.stream()
                .allMatch(item -> canOperaStatus.contains(item.getProcessOrderStatus()))) {
            throw new ParamIllegalException("加工单状态必须是加工中方可操作，数据被更新，请刷新页面后操作！");
        }

        // 校验返工加工单出库数量与待收货数量是否>0
        final List<ProcessOrderPo> reworkingProcessOrderPos = existProcessOrderPoList.stream()
                .filter(existProcessOrderPo -> Objects.equals(ProcessOrderStatus.REWORKING,
                        existProcessOrderPo.getProcessOrderStatus()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(reworkingProcessOrderPos)) {
            for (ProcessOrderPo reworkingProcessOrderPo : reworkingProcessOrderPos) {
                final String processOrderNo = reworkingProcessOrderPo.getProcessOrderNo();
                // 仓库未签出的数量
                List<ProcessDeliveryOrderVo> processDeliveryOrderVoList
                        = wmsRemoteService.getProcessDeliveryOrder(processOrderNo, WmsEnum.DeliveryType.PROCESS);
                int deliverySize = (int) processDeliveryOrderVoList.stream()
                        .filter(it -> !it.getDeliveryState()
                                .equals(WmsEnum.DeliveryState.SIGNED_OFF) &&
                                !it.getDeliveryState()
                                        .equals(WmsEnum.DeliveryState.FINISHED) &&
                                !it.getDeliveryState()
                                        .equals(WmsEnum.DeliveryState.CANCELING) &&
                                !it.getDeliveryState()
                                        .equals(WmsEnum.DeliveryState.CANCELED))
                        .count();
                if (deliverySize > 0) {
                    throw new ParamIllegalException("加工单关联出库单未签出，请联系仓库人员操作！");
                }

                // 校验是否存在未收货的原料信息
                List<ProcessMaterialReceiptPo> materialReceiptList
                        = processMaterialReceiptDao.getByProcessOrderNo(processOrderNo);
                if (materialReceiptList.stream()
                        .anyMatch(materialReceipt -> Objects.equals(ProcessMaterialReceiptStatus.WAIT_RECEIVE,
                                materialReceipt.getProcessMaterialReceiptStatus()))) {
                    throw new ParamIllegalException("存在未收货的原料出库单，请完成收货操作！");
                }
            }
        }

        // 创建质检单
        SpringUtil.getBean(this.getClass())
                .createQcOrderByProcessOrder(existProcessOrderPoList, dto);

        // 释放加工单原料容器
        existProcessOrderPoList.forEach(po -> {
            if (StringUtils.isBlank(po.getContainerCode())) {
                return;
            }
            IContainer container = new ProcessOrderContainer(po.getContainerCode());
            container.tryReleaseContainer();

            processOrderBaseService.clearContainerCode(po.getProcessOrderNo());
        });

        Set<String> completeHandoverProcessOrderNos = existProcessOrderPoList.stream()
                .map(ProcessOrderPo::getProcessOrderNo)
                .collect(Collectors.toSet());

        // 计算加工单成本
        CalculateCostBo calculateCostBo = new CalculateCostBo();
        calculateCostBo.setProcessOrderNos(completeHandoverProcessOrderNos);
        consistencyService.execAsyncTask(ProcessCostHandler.class, calculateCostBo);

        // 更新答交时间是否延期
        RefreshPromiseDateDelayedBo refreshPromiseDateDelayedBo
                = new RefreshPromiseDateDelayedBo();
        refreshPromiseDateDelayedBo.setProcessOrderNos(completeHandoverProcessOrderNos);
        consistencyService.execAsyncTask(RefreshProDateDelayHandler.class, refreshPromiseDateDelayedBo);
    }

    /**
     * 补充原料
     *
     * @param dto
     * @return
     */
    public void addMaterial(ProcessOrderMaterialAddDto dto) {
        Long processOrderId = dto.getProcessOrderId();
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderId(processOrderId);
        if (null == processOrderPo) {
            throw new ParamIllegalException("补充原料失败！加工单不存在，请刷新页面后重试");
        }

        ProcessOrderStatus processOrderStatus = processOrderPo.getProcessOrderStatus();
        List<ProcessOrderStatus> canAddMaterialStatus = Arrays.asList(
                ProcessOrderStatus.WAIT_READY,
                ProcessOrderStatus.WAIT_PLAN,
                ProcessOrderStatus.WAIT_PRODUCE,
                ProcessOrderStatus.PROCESSING,
                ProcessOrderStatus.WAIT_MOVING,
                ProcessOrderStatus.CHECKING,
                ProcessOrderStatus.REWORKING);
        if (!canAddMaterialStatus.contains(processOrderStatus)) {
            throw new ParamIllegalException("补充原料失败！只有 {} 状态方可补充原料。",
                    canAddMaterialStatus.stream().map(ProcessOrderStatus::getRemark).collect(Collectors.joining(",")));
        }

        List<ProcessOrderMaterialSkuBo> skuBos = dto.getMaterialSkus().stream().map(item -> {
            ProcessOrderMaterialSkuBo processOrderMaterialSkuBo = new ProcessOrderMaterialSkuBo();
            processOrderMaterialSkuBo.setSku(item.getSku());
            processOrderMaterialSkuBo.setDeliveryNum(item.getDeliveryNum());
            processOrderMaterialSkuBo.setSkuBatchCode(item.getSkuBatchCode());
            return processOrderMaterialSkuBo;
        }).collect(Collectors.toList());

        // 创建原料出库单
        String warehouseCode = processOrderPo.getWarehouseCode();
        if (StringUtils.isNotBlank(processOrderPo.getDeliveryWarehouseCode())) {
            warehouseCode = processOrderPo.getDeliveryWarehouseCode();
        }
        if (StringUtils.isNotBlank(dto.getWarehouseCode())) {
            warehouseCode = dto.getWarehouseCode();
        }
        String warehouseName = processOrderPo.getWarehouseCode();
        if (StringUtils.isNotBlank(processOrderPo.getDeliveryWarehouseCode())) {
            warehouseName = processOrderPo.getDeliveryWarehouseCode();
        }
        if (StringUtils.isNotBlank(dto.getWarehouseName())) {
            warehouseName = dto.getWarehouseName();
        }

        String processOrderNo = processOrderPo.getProcessOrderNo();
        String platform = processOrderPo.getPlatform();
        ProcessMaterialDetailPo processMaterialDetailPo
                = processOrderBaseService.updateProcMaterialDeliveryInfo(processOrderNo, skuBos, warehouseCode, warehouseName, dto.getProductQuality(), platform);

        List<ProcessOrderMaterialPo> processOrderMaterialPoList = skuBos.stream().map(item -> {
            ProcessOrderMaterialPo processOrderMaterialPo = new ProcessOrderMaterialPo();
            processOrderMaterialPo.setProcessOrderNo(processOrderNo);
            processOrderMaterialPo.setDeliveryNo(processMaterialDetailPo.getDeliveryNo());
            processOrderMaterialPo.setSku(item.getSku());
            processOrderMaterialPo.setDeliveryNum(item.getDeliveryNum());
            processOrderMaterialPo.setSkuBatchCode(item.getSkuBatchCode());
            processOrderMaterialPo.setCreateType(CreateType.CREATE);
            return processOrderMaterialPo;
        }).collect(Collectors.toList());
        processOrderCreateBaseService.addProcessOrderMaterials(processOrderMaterialPoList, processMaterialDetailPo);
    }

    /**
     * 归还原料
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean backMaterial(ProcessOrderMaterialBackDto dto) {
        Long processOrderId = dto.getProcessOrderId();
        ProcessOrderPo queriedProcessOrderPo = ParamValidUtils.requireNotNull(
                processOrderDao.getByProcessOrderId(processOrderId), "加工单不存在!请校验后提交。"
        );

        ParamValidUtils.requireNotEmpty(processOrderMaterialDao.getByProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo()), "该加工单没有原料!请校验后提交");
        List<ProcessMaterialDetailPo> processMaterialDetailPoList = processMaterialDetailDao.getListByProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
        List<Long> processMaterialDetailIds = processMaterialDetailPoList.stream()
                .map(ProcessMaterialDetailPo::getProcessMaterialDetailId)
                .collect(Collectors.toList());
        List<ProcessMaterialDetailItemPo> processMaterialDetailItemPoList
                = processMaterialDetailItemDao.getListByProcessMaterialDetailIdList(processMaterialDetailIds);
        List<String> skuBatchCodes = processMaterialDetailItemPoList.stream()
                .map(ProcessMaterialDetailItemPo::getSkuBatchCode)
                .collect(Collectors.toList());

        List<ProcessOrderMaterialBackDto.MaterialBackSku> backMaterialSkus = dto.getMaterialSkus();
        backMaterialSkus.forEach(item -> {
            if (!skuBatchCodes.contains(item.getSkuBatchCode())) {
                throw new BizException("该加工单没有批次码{}", item.getSkuBatchCode());
            }
        });

        MaterialBackBo materialBackBo = processOrderBaseService.getBackStatus(queriedProcessOrderPo.getProcessOrderNo());
        List<MaterialBackBo.MaterialBackSku> materialBackSkus = materialBackBo.getMaterialBackSkus();
        if (CollectionUtils.isEmpty(materialBackSkus)) {
            throw new BizException("该加工单没有入库数，禁止归还");
        }

        backMaterialSkus.forEach(item -> {
            Optional<MaterialBackBo.MaterialBackSku> first = materialBackSkus.stream()
                    .filter(it -> it.getSkuBatchCode().equals(item.getSkuBatchCode()))
                    .findFirst();
            if (first.isEmpty()) {
                throw new ParamIllegalException("批次码{}没有入库记录，禁止归还", item.getSkuBatchCode());
            }
            MaterialBackBo.MaterialBackSku materialBackSku = first.get();
            if (materialBackSku.getAvailableBackNum() < item.getDeliveryNum()) {
                throw new ParamIllegalException("批次码{}最多只能归还{}个", item.getSkuBatchCode(), materialBackSku.getAvailableBackNum());
            }
        });

        ReceiveOrderCreateMqDto receiveOrderCreateMqDto = new ReceiveOrderCreateMqDto();
        String key = idGenerateService.getSnowflakeCode(queriedProcessOrderPo.getProcessOrderNo() + "-");
        String warehouseCode = queriedProcessOrderPo.getWarehouseCode();
        if (StringUtils.isNotBlank(queriedProcessOrderPo.getDeliveryWarehouseCode())) {
            warehouseCode = queriedProcessOrderPo.getDeliveryWarehouseCode();
        }
        if (StringUtils.isNotBlank(dto.getWarehouseCode())) {
            warehouseCode = dto.getWarehouseCode();
        }
        // 产品质量
        MaterialProductQuality materialProductQuality = dto.getMaterialProductQuality();
        WmsEnum.ReceiveType verificationReceiptType;

        // 是否虚拟仓
        boolean isVirtualWarehouse = wmsRemoteService.isMatchWarehouseTypes(warehouseCode, Set.of(WmsEnum.WarehouseType.VIRTUAL_WAREHOUSE));

        ReceiveOrderCreateMqDto.ReceiveOrderCreateItem receiveOrderCreateItem = new ReceiveOrderCreateMqDto.ReceiveOrderCreateItem();
        if (materialProductQuality.equals(MaterialProductQuality.GOOD)) {
            receiveOrderCreateItem.setReceiveType(ReceiveType.PROCESS_MATERIAL);
            verificationReceiptType = WmsEnum.ReceiveType.PROCESS_MATERIAL;
            receiveOrderCreateItem.setUnionKey(key + ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK + ScmBizReceiveOrderType.PROCESS_RAW.name());
            receiveOrderCreateItem.setQcType(isVirtualWarehouse ? WmsEnum.QcType.NOT_CHECK : WmsEnum.QcType.SAMPLE_CHECK);
        } else if (materialProductQuality.equals(MaterialProductQuality.DEFECTIVE)) {
            receiveOrderCreateItem.setReceiveType(ReceiveType.DEFECTIVE_PROCESS_PRODUCT);
            verificationReceiptType = WmsEnum.ReceiveType.DEFECTIVE_PROCESS_PRODUCT;
            receiveOrderCreateItem.setUnionKey(key + ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK + ScmBizReceiveOrderType.DEFECTIVE_PROCESS.name());
            receiveOrderCreateItem.setQcType(WmsEnum.QcType.NOT_CHECK);
        } else {
            throw new ParamIllegalException("请选择合理的产品质量类型：良品/不良品");
        }

        // 校验收货单仓库是否符合
        BooleanType booleanType = wmsRemoteService.mateWarehouseCodeAndReceiveType(verificationReceiptType, warehouseCode);
        if (!Objects.equals(BooleanType.TRUE, booleanType)) {
            throw new ParamIllegalException(ScmConstant.VERIFY_WAREHOUSE_PROMPTS);
        }

        receiveOrderCreateItem.setWarehouseCode(warehouseCode);
        receiveOrderCreateItem.setSkuDevType(SkuDevType.NORMAL);
        receiveOrderCreateItem.setScmBizNo(queriedProcessOrderPo.getProcessOrderNo());
        receiveOrderCreateItem.setPurchaseOrderType(PurchaseOrderType.NORMAL);
        receiveOrderCreateItem.setIsUrgentOrder(BooleanType.FALSE);
        receiveOrderCreateItem.setIsDirectSend(BooleanType.FALSE);
        receiveOrderCreateItem.setIsNormalOrder(BooleanType.FALSE);
        receiveOrderCreateItem.setPlaceOrderTime(LocalDateTime.now());
        receiveOrderCreateItem.setOperator(GlobalContext.getUserKey());
        receiveOrderCreateItem.setOperatorName(GlobalContext.getUsername());
        receiveOrderCreateItem.setSendTime(new DateTime().toLocalDateTime());

        String platform = ParamValidUtils.requireNotBlank(queriedProcessOrderPo.getPlatform(),
                StrUtil.format("归还原料失败！加工单{}平台编码为空。", queriedProcessOrderPo.getProcessOrderNo())
        );
        List<ReceiveOrderCreateMqDto.ReceiveOrderDetail> receiveOrderDetails = backMaterialSkus.stream().map(item -> {
            ReceiveOrderCreateMqDto.ReceiveOrderDetail receiveOrderDetail = new ReceiveOrderCreateMqDto.ReceiveOrderDetail();
            receiveOrderDetail.setPlatCode(platform);
            receiveOrderDetail.setBatchCode(item.getSkuBatchCode());
            receiveOrderDetail.setSpu(queriedProcessOrderPo.getSpu());
            receiveOrderDetail.setSkuCode(item.getSku());
            receiveOrderDetail.setPurchaseAmount(item.getDeliveryNum());
            receiveOrderDetail.setDeliveryAmount(item.getDeliveryNum());
            return receiveOrderDetail;
        }).collect(Collectors.toList());

        receiveOrderCreateItem.setDetailList(receiveOrderDetails);
        ArrayList<ReceiveOrderCreateMqDto.ReceiveOrderCreateItem> receiveOrderCreateItems = new ArrayList<>();
        receiveOrderCreateItems.add(receiveOrderCreateItem);
        receiveOrderCreateMqDto.setReceiveOrderCreateItemList(receiveOrderCreateItems);
        receiveOrderCreateMqDto.setKey(key);
        consistencySendMqService.execSendMq(WmsReceiptHandler.class, receiveOrderCreateMqDto);

        // 创建原料归还记录
        String processOrderNo = queriedProcessOrderPo.getProcessOrderNo();
        ProcessMaterialBackPo newProcessMaterialBackPo = new ProcessMaterialBackPo();
        newProcessMaterialBackPo.setProcessOrderNo(processOrderNo);
        newProcessMaterialBackPo.setMessageKey(key);
        newProcessMaterialBackPo.setBackStatus(BackStatus.WAIT_BACK);
        processMaterialBackDao.insert(newProcessMaterialBackPo);

        // 明细
        List<ProcessMaterialBackItemPo> processMaterialBackItemPoList = backMaterialSkus.stream().map(item -> {
            ProcessMaterialBackItemPo processMaterialBackItemPo = new ProcessMaterialBackItemPo();
            processMaterialBackItemPo.setSkuBatchCode(item.getSkuBatchCode());
            processMaterialBackItemPo.setDeliveryNum(item.getDeliveryNum());
            processMaterialBackItemPo.setProcessMaterialBackId(newProcessMaterialBackPo.getProcessMaterialBackId());
            return processMaterialBackItemPo;
        }).collect(Collectors.toList());
        processMaterialBackItemDao.insertBatch(processMaterialBackItemPoList);

        // 更新归还数量
        processOrderBaseService.updateProcessOrderMaterialBackNum(processMaterialBackItemPoList, processOrderNo);
        return true;
    }

    /**
     * 获取指定加工单号的生产信息列表。
     *
     * @param dto 包含加工单号的数据传输对象。
     * @return 包含产品信息的 SampleChildOrderInfoVo 列表。
     */
    public List<SampleChildOrderInfoVo> getProcessOrderSampleList(ProcessOrderNoDto dto) {
        final List<ProcessOrderSamplePo> processOrderSamplePos
                = processOrderSampleDao.getByProcessOrderNo(dto.getProcessOrderNo());
        return ProcessOrderSampleConverter.toSampleChildOrderInfoVo(processOrderSamplePos);
    }

    /**
     * 通过 sku 获取图片信息
     *
     * @param dto
     * @return
     */
    public List<String> getFileCodeListBySku(SkuPlatformDto dto) {
        String sku = dto.getSku();
        String platform = dto.getPlatform();
        ArrayList<String> skuList = new ArrayList<>();
        skuList.add(sku);

        List<PlmSkuImage> skuImage = plmRemoteService.getSkuImage(skuList, platform);

        if (CollectionUtils.isEmpty(skuImage)) {
            return new ArrayList<>();
        }
        Optional<PlmSkuImage> first = skuImage.stream()
                .findFirst();
        if (first.isEmpty()) {
            return new ArrayList<>();
        }
        PlmSkuImage plmSkuImage = first.get();
        return processOrderBaseService.formatPlmSkuImage(plmSkuImage);

    }

    /**
     * 发送钉钉消息
     */
    @Transactional(rollbackFor = Exception.class)
    public void sendStatusDingTalkMessage(String processOrderNo) {
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (null == processOrderPo) {
            throw new ParamIllegalException("加工单不存在");
        }

        List<ProcessOrderStatus> allowedStatuses = Arrays.asList(ProcessOrderStatus.WAIT_DELIVERY, ProcessOrderStatus.STORED);
        if (!allowedStatuses.contains(processOrderPo.getProcessOrderStatus())) {
            String allowedStatusNames = allowedStatuses.stream().map(ProcessOrderStatus::getDesc).collect(Collectors.joining("/"));
            throw new ParamIllegalException("只有{}状态的单才能推送消息", allowedStatusNames);
        }
        ProcessOrderExtraPo processOrderExtraPo
                = processOrderExtraDao.getByProcessOrderNo(processOrderPo.getProcessOrderNo());

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderNo", processOrderPo.getProcessOrderNo());
        hashMap.put("status", processOrderPo.getProcessOrderStatus()
                .getDesc());
        List<ProcessOrderItemPo> processOrderItemPos
                = processOrderItemDao.getByProcessOrderNo(processOrderPo.getProcessOrderNo());
        if (ProcessOrderStatus.WAIT_DELIVERY.equals(processOrderPo.getProcessOrderStatus())) {
            hashMap.put("operatorUsername", processOrderExtraPo.getCheckUsername());
            String formatTime = LocalDateTimeUtil.format(TimeUtil.zoned(processOrderPo.getCheckedTime(), TimeZoneId.CN)
                    .toLocalDateTime(), DatePattern.NORM_DATETIME_PATTERN);
            hashMap.put("operatorTime", formatTime);
            long totalQualityNum = processOrderItemPos.stream()
                    .mapToLong(ProcessOrderItemPo::getQualityGoodsCnt)
                    .sum();
            long totalDefectiveNum = processOrderItemPos.stream()
                    .mapToLong(ProcessOrderItemPo::getDefectiveGoodsCnt)
                    .sum();
            hashMap.put("desc",
                    "加工单已经加工并质检完成，进入待发货状态，正品数为" + totalQualityNum + "，次品数为" + totalDefectiveNum +
                            "，准备交付仓库，请及时跟进，核准真实情况！");
        }
        if (ProcessOrderStatus.STORED.equals(processOrderPo.getProcessOrderStatus())) {
            hashMap.put("operatorUsername", processOrderExtraPo.getStoreUsername());
            String formatTime = LocalDateTimeUtil.format(TimeUtil.zoned(processOrderPo.getStoredTime(), TimeZoneId.CN)
                    .toLocalDateTime(), DatePattern.NORM_DATETIME_PATTERN);
            hashMap.put("operatorTime", formatTime);
            long totalQualityNum = processOrderItemPos.stream()
                    .mapToLong(ProcessOrderItemPo::getQualityGoodsCnt)
                    .sum();
            hashMap.put("desc",
                    "加工单已经由" + processOrderPo.getWarehouseName() + "收货入库，上架数量为" + totalQualityNum + ",可以进行核实！");
        }

        String file = FreemarkerUtil.processByFile("ding_talk_process_order_status.ftl", hashMap);
        DingTalkOtoMsgDto msgDto
                = DingTalkMsgUtil.toO2oMdMsg(Lists.newArrayList(processOrderPo.getCreateUser()), "processOrderStatus",
                file);
        consistencySendMqService.execSendMq(DingTalkHandler.class, msgDto);

    }

    @Transactional(rollbackFor = Exception.class)
    public void importMaterialData(ProcessMaterialImportationDto.ImportationDetail req) {
        String sku = req.getSku();
        ArrayList<String> batchCodes = new ArrayList<>();
        batchCodes.add(sku);
        List<SimpleSkuBatchVo> skuBatchByBatchCodes = wmsRemoteService.getSkuBatchByBatchCodes(batchCodes);
        String finalSku = sku;
        Optional<SimpleSkuBatchVo> skuBatchVoOptional = skuBatchByBatchCodes.stream()
                .filter(it -> finalSku.equals(it.getBatchCode()))
                .findFirst();
        if (skuBatchVoOptional.isPresent()) {
            sku = skuBatchVoOptional.get()
                    .getSkuCode();
        } else {
            throw new ParamIllegalException("旧 sku 对照关系不存在");
        }

        final ProcessOrderMaterialPo processOrderMaterialPo = new ProcessOrderMaterialPo();
        processOrderMaterialPo.setProcessOrderNo(req.getProcessOrderNo());
        processOrderMaterialPo.setSku(sku);
        processOrderMaterialPo.setDeliveryNum(req.getDeliveryNum());
        processOrderMaterialPo.setCreateType(CreateType.CREATE);

        ProcessOrderPo byProcessOrderNo = processOrderDao.getByProcessOrderNo(req.getProcessOrderNo());
        if (null != byProcessOrderNo) {
            GlobalContext.setUserKey(byProcessOrderNo.getCreateUser());
            GlobalContext.setUsername(byProcessOrderNo.getCreateUsername());
        }

        processOrderMaterialDao.insert(processOrderMaterialPo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void importProcedureData(ProcessProcedureImportationDto.ImportationDetail req) {
        final ProcessOrderProcedurePo processOrderProcedurePo = new ProcessOrderProcedurePo();
        String processName = req.getProcessName();
        List<ProcessPo> processPos = processDao.getAll();
        if (CollectionUtils.isNotEmpty(processPos)) {
            Optional<ProcessPo> first = processPos.stream()
                    .filter(it -> it.getProcessSecondName()
                            .equals(processName))
                    .findFirst();
            if (first.isPresent()) {
                ProcessPo processPo = first.get();
                processOrderProcedurePo.setProcessId(processPo.getProcessId());
                processOrderProcedurePo.setProcessCode(processPo.getProcessCode());
            }
        }
        if (null == processOrderProcedurePo.getProcessId()) {
            throw new ParamIllegalException("请先导入工序");
        }
        processOrderProcedurePo.setProcessOrderNo(req.getProcessOrderNo());
        processOrderProcedurePo.setProcessName(req.getProcessName());
        processOrderProcedurePo.setCommission(req.getCommission());
        List<ProcessOrderProcedurePo> byProcessOrderNo
                = processOrderProcedureDao.getByProcessOrderNo(req.getProcessOrderNo());
        Integer max = 0;
        if (CollectionUtils.isNotEmpty(byProcessOrderNo)) {
            max = Collections.max(byProcessOrderNo.stream()
                    .map(ProcessOrderProcedurePo::getSort)
                    .collect(Collectors.toList()));
        }
        max++;
        processOrderProcedurePo.setSort(max);
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(req.getProcessOrderNo());
        if (null != processOrderPo) {
            GlobalContext.setUserKey(processOrderPo.getCreateUser());
            GlobalContext.setUsername(processOrderPo.getCreateUsername());
        }


        processOrderProcedureDao.insert(processOrderProcedurePo);
    }

    /**
     * 初始化加工描述值
     *
     * @param req
     */
    @Transactional(rollbackFor = Exception.class)
    public void importDescData(ProcessOrderDescImportationDto.ImportationDetail req) {
        final ProcessOrderDescPo processOrderDescPo = new ProcessOrderDescPo();
        processOrderDescPo.setProcessOrderNo(req.getProcessOrderNo());
        processOrderDescPo.setProcessDescName(req.getProcessDescName());
        processOrderDescPo.setProcessDescValue(req.getProcessDescValue());

        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(req.getProcessOrderNo());
        if (null != processOrderPo) {
            GlobalContext.setUserKey(processOrderPo.getCreateUser());
            GlobalContext.setUsername(processOrderPo.getCreateUsername());
        }

        processOrderDescDao.insert(processOrderDescPo);
    }

    /**
     * 原料出库信息
     *
     * @author ChenWenLong
     * @date 2023/3/17 18:25
     */
    public List<ProcessOrderDeliveryOrderVo> getDeliveryOrder(ProcessOrderDeliveryOrderDto dto) {
        List<ProcessDeliveryOrderVo> processDeliveryOrderVoList
                = wmsRemoteService.getProcessDeliveryOrder(dto.getProcessOrderNo(), WmsEnum.DeliveryType.PROCESS);
        final Map<String, ProcessDeliveryOrderVo> deliveryOrderVoMap = processDeliveryOrderVoList.stream()
                .collect(Collectors.toMap(ProcessDeliveryOrderVo::getDeliveryOrderNo, Function.identity()));

        Map<String, ProcessOrderDeliveryOrderVo> processOrderDeliveryOrderVoMap = new HashMap<>();

        List<ProcessOrderMaterialPo> processOrderMaterialPoList
                = processOrderMaterialDao.getByProcessOrderNo(dto.getProcessOrderNo());
        List<ProcessMaterialReceiptPo> processMaterialReceiptPoList
                = processMaterialReceiptDao.getByProcessOrderNo(dto.getProcessOrderNo());

        List<Long> processMaterialReceiptPoIdList = processMaterialReceiptPoList.stream()
                .map(ProcessMaterialReceiptPo::getProcessMaterialReceiptId)
                .collect(Collectors.toList());
        List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPoList
                = processMaterialReceiptItemDao.getByMaterialReceiptIds(processMaterialReceiptPoIdList);
        Map<String, Integer> receiptItemPoReceiptSumMap = processMaterialReceiptItemPoList.stream()
                .collect(Collectors.groupingBy(ProcessMaterialReceiptItemPo::getSkuBatchCode,
                        Collectors.summingInt(ProcessMaterialReceiptItemPo::getReceiptNum)));

        // 对detailItem按照加工单号分组
        final List<ProcessMaterialDetailPo> processMaterialDetailPoList
                = processMaterialDetailDao.getListByProcessOrderNo(dto.getProcessOrderNo());
        final List<Long> processMaterialDetailIdList = processMaterialDetailPoList.stream()
                .map(ProcessMaterialDetailPo::getProcessMaterialDetailId)
                .collect(Collectors.toList());
        final List<ProcessMaterialDetailItemPo> processMaterialDetailItemPoList
                = processMaterialDetailItemDao.getListByProcessMaterialDetailIdList(processMaterialDetailIdList);
        final Map<Long, List<ProcessMaterialDetailItemPo>> processMaterialDetailIdItemMap
                = processMaterialDetailItemPoList.stream()
                .collect(Collectors.groupingBy(ProcessMaterialDetailItemPo::getProcessMaterialDetailId));
        Map<String, List<ProcessMaterialDetailItemPo>> processOrderNoDetailItemListMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(processMaterialDetailPoList)) {
            processOrderNoDetailItemListMap = processMaterialDetailPoList.stream()
                    .collect(Collectors.groupingBy(ProcessMaterialDetailPo::getDeliveryNo, Collectors.mapping(
                            processMaterialDetailPo -> processMaterialDetailIdItemMap.getOrDefault(
                                    processMaterialDetailPo.getProcessMaterialDetailId(), Collections.emptyList()),
                            Collectors.flatMapping(List::stream, Collectors.toList()))));
        }
        // 记录已经组装的发货单，避免按发货单维度组装数据时重复
        List<String> deliveryNoList = new ArrayList<>();
        for (ProcessOrderMaterialPo po : processOrderMaterialPoList) {
            final List<ProcessMaterialDetailItemPo> processMaterialDetailItemPoList1
                    = processOrderNoDetailItemListMap.get(po.getDeliveryNo());

            // 当发货单号不为空并且已经组装过，跳过这次组装过程
            if (StringUtils.isNotBlank(po.getDeliveryNo()) && deliveryNoList.contains(po.getDeliveryNo())) {
                continue;
            }

            // 如果detail（出库单信息）为空，则以processOrderMaterialPo为准
            if (CollectionUtils.isEmpty(processMaterialDetailItemPoList1)) {
                final ProcessOrderDeliveryOrderVo processOrderDeliveryOrderVo
                        = processOrderDeliveryOrderVoMap.get(po.getSku() + po.getSkuBatchCode());
                if (null == processOrderDeliveryOrderVo) {
                    final ProcessOrderDeliveryOrderVo processOrderDeliveryOrderVo1 = new ProcessOrderDeliveryOrderVo();
                    processOrderDeliveryOrderVo1.setSku(po.getSku());
                    processOrderDeliveryOrderVo1.setSkuBatchCode(po.getSkuBatchCode());
                    processOrderDeliveryOrderVo1.setAmount(po.getDeliveryNum());
                    processOrderDeliveryOrderVo1.setDeliveryOrderVoList(
                            this.getDeliveryOrderVoListByDeliveryNo(deliveryOrderVoMap, po.getDeliveryNo()));
                    processOrderDeliveryOrderVoMap.put(po.getSku() + po.getSkuBatchCode(),
                            processOrderDeliveryOrderVo1);
                } else {
                    processOrderDeliveryOrderVo.setAmount(
                            processOrderDeliveryOrderVo.getAmount() + po.getDeliveryNum());
                    final List<ProcessOrderDeliveryOrderVo.DeliveryOrderVo> deliveryOrderVoList
                            = processOrderDeliveryOrderVo.getDeliveryOrderVoList();
                    final List<ProcessOrderDeliveryOrderVo.DeliveryOrderVo> newDeliveryOrderVoList
                            = this.getDeliveryOrderVoListByDeliveryNo(deliveryOrderVoMap, po.getDeliveryNo());
                    if (CollectionUtils.isEmpty(deliveryOrderVoList)) {
                        processOrderDeliveryOrderVo.setDeliveryOrderVoList(newDeliveryOrderVoList);
                    } else {
                        deliveryOrderVoList.addAll(newDeliveryOrderVoList);
                        processOrderDeliveryOrderVo.setDeliveryOrderVoList(deliveryOrderVoList);
                    }
                }
            } else {
                for (ProcessMaterialDetailItemPo processMaterialDetailItemPo : processMaterialDetailItemPoList1) {
                    final ProcessOrderDeliveryOrderVo processOrderDeliveryOrderVo
                            = processOrderDeliveryOrderVoMap.get(
                            processMaterialDetailItemPo.getSku() + processMaterialDetailItemPo.getSkuBatchCode());
                    if (null == processOrderDeliveryOrderVo) {
                        final ProcessOrderDeliveryOrderVo processOrderDeliveryOrderVo1
                                = new ProcessOrderDeliveryOrderVo();
                        processOrderDeliveryOrderVo1.setSku(processMaterialDetailItemPo.getSku());
                        processOrderDeliveryOrderVo1.setSkuBatchCode(processMaterialDetailItemPo.getSkuBatchCode());
                        processOrderDeliveryOrderVo1.setAmount(processMaterialDetailItemPo.getDeliveryNum());
                        processOrderDeliveryOrderVo1.setReceiptNum(
                                receiptItemPoReceiptSumMap.getOrDefault(processMaterialDetailItemPo.getSkuBatchCode(),
                                        0));
                        processOrderDeliveryOrderVo1.setDeliveryOrderVoList(
                                this.getDeliveryOrderVoListByDeliveryNo(deliveryOrderVoMap, po.getDeliveryNo()));
                        processOrderDeliveryOrderVoMap.put(
                                processMaterialDetailItemPo.getSku() + processMaterialDetailItemPo.getSkuBatchCode(),
                                processOrderDeliveryOrderVo1);
                    } else {
                        processOrderDeliveryOrderVo.setAmount(
                                processOrderDeliveryOrderVo.getAmount() + processMaterialDetailItemPo.getDeliveryNum());
                        // 收货数
                        processOrderDeliveryOrderVo.setReceiptNum(
                                receiptItemPoReceiptSumMap.getOrDefault(processMaterialDetailItemPo.getSkuBatchCode(),
                                        0));
                        final List<ProcessOrderDeliveryOrderVo.DeliveryOrderVo> deliveryOrderVoList
                                = processOrderDeliveryOrderVo.getDeliveryOrderVoList();
                        final List<ProcessOrderDeliveryOrderVo.DeliveryOrderVo> newDeliveryOrderVoList
                                = this.getDeliveryOrderVoListByDeliveryNo(deliveryOrderVoMap, po.getDeliveryNo());
                        if (CollectionUtils.isEmpty(deliveryOrderVoList)) {
                            processOrderDeliveryOrderVo.setDeliveryOrderVoList(newDeliveryOrderVoList);
                        } else {
                            deliveryOrderVoList.addAll(newDeliveryOrderVoList);
                            processOrderDeliveryOrderVo.setDeliveryOrderVoList(deliveryOrderVoList);
                        }
                    }
                }
                deliveryNoList.add(po.getDeliveryNo());
            }
        }

        return new ArrayList<>(processOrderDeliveryOrderVoMap.values());
    }

    private List<ProcessOrderDeliveryOrderVo.DeliveryOrderVo> getDeliveryOrderVoListByDeliveryNo(Map<String,
            ProcessDeliveryOrderVo> deliveryOrderVoMap,
                                                                                                 String deliveryNo) {
        final ProcessDeliveryOrderVo processDeliveryOrderVo = deliveryOrderVoMap.get(deliveryNo);
        if (null == processDeliveryOrderVo || StringUtils.isBlank(deliveryNo)) {
            return Collections.emptyList();
        }
        final ProcessOrderDeliveryOrderVo.DeliveryOrderVo deliveryOrderVo
                = new ProcessOrderDeliveryOrderVo.DeliveryOrderVo();
        deliveryOrderVo.setDeliveryOrderNo(processDeliveryOrderVo.getDeliveryOrderNo());
        deliveryOrderVo.setDeliveryAmount(processDeliveryOrderVo.getDeliveryAmount());
        deliveryOrderVo.setDeliveryState(processDeliveryOrderVo.getDeliveryState());
        deliveryOrderVo.setWarehouseName(processDeliveryOrderVo.getWarehouseName());

        List<ProcessOrderDeliveryOrderVo.DeliveryOrderVo> deliveryOrderVoList = new ArrayList<>();
        deliveryOrderVoList.add(deliveryOrderVo);
        return deliveryOrderVoList;
    }

    /**
     * 原料入库信息
     *
     * @author ChenWenLong
     * @date 2023/3/17 18:25
     */
    public List<ProcessOrderReceiveOrderVo> getReceiveOrder(ProcessOrderDeliveryOrderDto dto) {
        final List<ProcessMaterialDetailPo> processMaterialDetailPoList
                = processMaterialDetailDao.getListByProcessOrderNo(dto.getProcessOrderNo());
        final List<Long> processMaterialDetailIdList = processMaterialDetailPoList.stream()
                .map(ProcessMaterialDetailPo::getProcessMaterialDetailId)
                .collect(Collectors.toList());
        final List<ProcessMaterialDetailItemPo> processMaterialDetailItemPoList
                = processMaterialDetailItemDao.getListByProcessMaterialDetailIdList(processMaterialDetailIdList);
        List<ProcessMaterialBackPo> processMaterialBackPoList
                = processMaterialBackDao.getByProcessOrderNo(dto.getProcessOrderNo());
        if (CollectionUtil.isEmpty(processMaterialDetailPoList) || CollectionUtil.isEmpty(processMaterialBackPoList)) {
            return Collections.emptyList();
        }

        final Map<String, String> skuBatchSkuMap = processMaterialDetailItemPoList.stream()
                .collect(Collectors.toMap(ProcessMaterialDetailItemPo::getSkuBatchCode,
                        ProcessMaterialDetailItemPo::getSku, (item1, item2) -> item1));
        // 获取wms原料&次品入库信息
        ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
        receiveOrderGetDto.setScmBizNoList(Collections.singletonList(dto.getProcessOrderNo()));
        receiveOrderGetDto.setReceiveType(WmsEnum.ReceiveType.PROCESS_MATERIAL);
        List<ReceiveOrderForScmVo> processReceiveList = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto);
        receiveOrderGetDto.setReceiveType(WmsEnum.ReceiveType.DEFECTIVE_PROCESS_PRODUCT);
        List<ReceiveOrderForScmVo> defectiveReceiveList = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto);
        Map<String, ReceiveOrderForScmVo> receiveNoWmsVoMap = Stream.of(processReceiveList, defectiveReceiveList)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(ReceiveOrderForScmVo::getReceiveOrderNo, Function.identity(),
                        (item1, item2) -> item1));

        List<Long> idList = processMaterialBackPoList.stream()
                .map(ProcessMaterialBackPo::getProcessMaterialBackId)
                .collect(Collectors.toList());
        List<ProcessMaterialBackItemPo> processMaterialBackItemPoList
                = processMaterialBackItemDao.listByProcessMaterialBackIds(idList);
        final Map<Long, ProcessMaterialBackPo> processMaterialBackIdPoMap = processMaterialBackPoList.stream()
                .collect(Collectors.toMap(ProcessMaterialBackPo::getProcessMaterialBackId, Function.identity()));


        final List<ProcessOrderReceiveOrderVo> processOrderReceiveOrderVoList = processMaterialBackItemPoList.stream()
                .map(processMaterialBackItemPo -> {
                    ProcessOrderReceiveOrderVo processOrderReceiveOrderVo = new ProcessOrderReceiveOrderVo();
                    processOrderReceiveOrderVo.setSku(skuBatchSkuMap.get(processMaterialBackItemPo.getSkuBatchCode()));
                    processOrderReceiveOrderVo.setSkuBatchCode(processMaterialBackItemPo.getSkuBatchCode());
                    processOrderReceiveOrderVo.setAmount(processMaterialBackItemPo.getDeliveryNum());
                    final ProcessMaterialBackPo processMaterialBackPo
                            = processMaterialBackIdPoMap.get(processMaterialBackItemPo.getProcessMaterialBackId());
                    if (null == processMaterialBackPo) {
                        throw new BizException("加工单:{},原料入库信息有误，请联系系统管理员！", dto.getProcessOrderNo());
                    }
                    final ReceiveOrderForScmVo receiveOrderForScmVo
                            = receiveNoWmsVoMap.get(processMaterialBackPo.getReceiptNo());
                    if (null != receiveOrderForScmVo) {
                        final ProcessOrderReceiveOrderVo.ReceiveOrderVo receiveOrderVo
                                = new ProcessOrderReceiveOrderVo.ReceiveOrderVo();
                        receiveOrderVo.setReceiveOrderNo(receiveOrderForScmVo.getReceiveOrderNo());
                        receiveOrderVo.setReceiveOrderState(receiveOrderForScmVo.getReceiveOrderState());
                        receiveOrderVo.setWarehouseName(receiveOrderForScmVo.getWarehouseName());
                        receiveOrderVo.setWarehouseCode(receiveOrderForScmVo.getWarehouseCode());
                        receiveOrderVo.setReceiveAmount(receiveOrderForScmVo.getDeliveryAmount());
                        processOrderReceiveOrderVo.setReceiveOrderVoList(Collections.singletonList(receiveOrderVo));
                    }
                    return processOrderReceiveOrderVo;
                })
                .collect(Collectors.toList());

        // 重新按照sku批次码维度合并
        return processOrderReceiveOrderVoList.stream()
                .collect(Collectors.groupingBy(ProcessOrderReceiveOrderVo::getSkuBatchCode))
                .values()
                .stream()
                .map(group -> {
                    final ProcessOrderReceiveOrderVo processOrderReceiveOrderVo = group.get(0);
                    Integer totalAmount = group.stream()
                            .mapToInt(ProcessOrderReceiveOrderVo::getAmount)
                            .sum();
                    List<ProcessOrderReceiveOrderVo.ReceiveOrderVo> receiveOrderVoList = group.stream()
                            .filter(receiveOrderVo -> CollectionUtils.isNotEmpty(
                                    receiveOrderVo.getReceiveOrderVoList()))
                            .flatMap(receiveOrderVo -> receiveOrderVo.getReceiveOrderVoList()
                                    .stream())
                            .collect(Collectors.toList());
                    final ProcessOrderReceiveOrderVo processOrderReceiveOrderVo1 = new ProcessOrderReceiveOrderVo();
                    processOrderReceiveOrderVo1.setSku(
                            skuBatchSkuMap.get(processOrderReceiveOrderVo.getSkuBatchCode()));
                    processOrderReceiveOrderVo1.setSkuBatchCode(processOrderReceiveOrderVo.getSkuBatchCode());
                    processOrderReceiveOrderVo1.setAmount(totalAmount);
                    processOrderReceiveOrderVo1.setReceiveOrderVoList(receiveOrderVoList);
                    return processOrderReceiveOrderVo1;
                })
                .collect(Collectors.toList());
    }

    /**
     * 成品入库信息
     *
     * @author ChenWenLong
     * @date 2023/3/20 18:25
     */
    public List<ProcessOrderCompleteReceiveOrderVo> getCompleteReceiveOrder(ProcessOrderDeliveryOrderDto dto) {
        final String processOrderNo = dto.getProcessOrderNo();
        List<ProcessOrderCompleteReceiveOrderVo> list = new ArrayList<>();
        List<ProcessOrderItemPo> processOrderItemPoList
                = processOrderItemDao.getByProcessOrderNoAndIsFirst(processOrderNo, BooleanType.TRUE);

        ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
        receiveOrderGetDto.setScmBizNoList(Collections.singletonList(processOrderNo));
        receiveOrderGetDto.setReceiveType(WmsEnum.ReceiveType.PROCESS_PRODUCT);
        List<ReceiveOrderForScmVo> receiveOrderForScmVoList = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto);

        for (ReceiveOrderForScmVo receiveOrderForScmVo : receiveOrderForScmVoList) {
            for (ReceiveOrderForScmVo.ReceiveDeliver receiveDeliver : receiveOrderForScmVo.getReceiveDeliverList()) {
                ProcessOrderCompleteReceiveOrderVo vo = new ProcessOrderCompleteReceiveOrderVo();
                vo.setReceiveOrderNo(receiveOrderForScmVo.getReceiveOrderNo());
                vo.setReceiveOrderState(receiveOrderForScmVo.getReceiveOrderState());
                vo.setCreateTime(receiveOrderForScmVo.getCreateTime());
                vo.setWarehouseCode(receiveOrderForScmVo.getWarehouseCode());
                vo.setWarehouseName(receiveOrderForScmVo.getWarehouseName());
                vo.setReceiveType(receiveOrderForScmVo.getReceiveType());
                vo.setSku(receiveDeliver.getSkuCode());
                vo.setSkuBatchCode(receiveDeliver.getBatchCode());

                //数量取加工单详情的是首次创建 正品数量
                if (WmsEnum.ReceiveType.PROCESS_PRODUCT.equals(receiveOrderForScmVo.getReceiveType())) {
                    vo.setAmount(processOrderItemPoList.get(0)
                            .getQualityGoodsCnt());
                } else {
                    vo.setAmount(processOrderItemPoList.get(0)
                            .getDefectiveGoodsCnt());
                }
                list.add(vo);
            }

        }
        return list;
    }


    @Transactional(rollbackFor = Exception.class)
    public void updateProcessOrderNeedProcessPlan(UpdateNeedProcessPlanDto dto) {
        List<ProcessOrderPo> processOrderPos = processOrderDao.getByProcessOrderNos(dto.getProcessOrderNos());
        if (CollectionUtils.isEmpty(processOrderPos)) {
            throw new BizException("数据已被删除，请刷新页面");
        }

        List<String> invalidProcessOrderNos = processOrderPos.stream()
                .filter(po -> po.getProcessOrderStatus() != ProcessOrderStatus.WAIT_READY && po.getProcessOrderStatus() != ProcessOrderStatus.WAIT_PLAN)
                .map(ProcessOrderPo::getProcessOrderNo)
                .collect(Collectors.toList());
        if (!invalidProcessOrderNos.isEmpty()) {
            String invalidProcessOrderNosStr = String.join(",", invalidProcessOrderNos);
            throw new ParamIllegalException("只有待齐备/待排产才能操作无需排产，以下加工单工单不满足条件: " + invalidProcessOrderNosStr);
        }

        for (ProcessOrderPo processOrderPo : processOrderPos) {
            SpringUtil.getBean(this.getClass())
                    .updateProcessOrderNeedProcessPlan(processOrderPo);
        }
    }

    public void updateProcessOrderNeedProcessPlan(ProcessOrderPo processOrderPo) {
        final String processOrderNo = processOrderPo.getProcessOrderNo();

        processOrderPo.setNeedProcessPlan(NeedProcessPlan.FALSE);
        OperatorUserBo operatorUserBo = new OperatorUserBo();
        operatorUserBo.setOperator(GlobalContext.getUserKey());
        operatorUserBo.setOperator(GlobalContext.getUsername());
        processOrderBaseService.changeStatus(processOrderPo, ProcessOrderStatus.WAIT_PRODUCE, operatorUserBo);

        // 如果加工单回料状态=已回料，需要释放原料容器
        if (IsReceiveMaterial.TRUE.equals(processOrderPo.getIsReceiveMaterial())) {
            if (StrUtil.isNotBlank(processOrderPo.getContainerCode())) {
                final ContainerUpdateStateDto containerUpdateStateDto = new ContainerUpdateStateDto();
                containerUpdateStateDto.setContainerCode(processOrderPo.getContainerCode());
                containerUpdateStateDto.setWarehouseCode(ScmConstant.PROCESS_WAREHOUSE_CODE);
                containerUpdateStateDto.setState(WmsEnum.ContainerState.IDLE);
                wmsRemoteService.updateContainerState(containerUpdateStateDto);
                processOrderBaseService.clearContainerCode(processOrderNo);
            }
        }
    }

    /**
     * 刷新特定加工订单的工序信息。
     * 该方法用于刷新给定加工订单号对应的工序信息。
     *
     * @param processOrderNo 待刷新处理流程信息的加工订单号。
     */
    public void refreshProcessOrderProcedures(String processOrderNo) {
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (Objects.isNull(processOrderPo)) {
            return;
        }
        final ProcessOrderType processOrderType = processOrderPo.getProcessOrderType();
        if (Objects.equals(ProcessOrderType.OVERSEAS_REPAIR, processOrderType)) {
            return;
        }

        processOrderBaseService.refreshMissingInfo(processOrderNo, new OperatorUserBo(),
                MissingInfoOperationType.PERFORM_PROCEDURE_OPERATIONS, false);
    }

    /**
     * 刷新特定加工订单的原料信息。
     * 该方法用于刷新给定加工订单号对应的原料信息。
     *
     * @param processOrderNo 待刷新物料信息的加工订单号。
     */
    public void refreshProcessOrderMaterials(String processOrderNo) {
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (Objects.isNull(processOrderPo)) {
            throw new BizException("加工单信息不存在，请刷新页面后重试");
        }
        final ProcessOrderType processOrderType = processOrderPo.getProcessOrderType();
        if (Objects.equals(ProcessOrderType.OVERSEAS_REPAIR, processOrderType)) {
            return;
        }
        boolean isProcessOrderValid
                = processOrderBaseService.checkProcessOrderStatusAndMissInformation(processOrderPo,
                ProcessOrderStatus.WAIT_READY,
                Set.of(MissingInformation.NOT_EXIST_MATERIAL,
                        MissingInformation.OUT_OF_STOCK));
        if (!isProcessOrderValid) {
            throw new ParamIllegalException("加工单状态必须是待齐备且缺失信息=无原料信息/无库存信息");
        }

        processOrderBaseService.refreshMissingInfo(processOrderNo, new OperatorUserBo(),
                MissingInfoOperationType.PERFORM_MATERIAL_UPDATE, false);
    }

    /**
     * 缺失信息检查任务。
     * 该方法用于执行缺失信息的定期检查任务。
     */
    public void missingInfoCheckTask() {
        List<ProcessOrderPo> processOrderPos = processOrderDao.getByStatusList(List.of(ProcessOrderStatus.WAIT_READY));
        log.info("开始刷新缺失信息：本次筛选待齐备的订单数有 {} 条", processOrderPos.size());

        if (CollectionUtils.isNotEmpty(processOrderPos)) {
            for (ProcessOrderPo processOrderPo : processOrderPos) {
                final String processOrderNo = processOrderPo.getProcessOrderNo();
                refreshMissingInfo(processOrderNo, new OperatorUserBo());
            }
        }
    }

    /**
     * 刷新缺失信息。
     * 该方法用于根据给定的加工订单号和操作用户信息，刷新缺失信息。
     *
     * @param processOrderNo 待刷新缺失信息的加工订单号。
     * @param operatorUserBo 执行刷新操作的操作用户信息对象。
     */
    public void refreshMissingInfo(String processOrderNo,
                                   OperatorUserBo operatorUserBo) {
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (Objects.isNull(processOrderPo)) {
            return;
        }
        log.info("开始刷新缺失信息：本次需要刷新的加工单号是：{}，加工单状态是：{}，缺失信息是：{}", processOrderNo,
                processOrderPo.getProcessOrderStatus(), processOrderPo.getMissingInformation());
        final String missingInformation = processOrderPo.getMissingInformation();
        Set<MissingInformation> missingInformationEnums
                = MissInformationConverter.convertToMissingInformationEnums(missingInformation);
        boolean onlyMissingProcess
                = CollectionUtils.isNotEmpty(
                missingInformationEnums) && missingInformationEnums.size() == 1 && missingInformationEnums.contains(
                MissingInformation.NOT_EXIST_PROCESS);

        final ProcessOrderType processOrderType = processOrderPo.getProcessOrderType();
        MissingInfoOperationType refreshType;

        if (onlyMissingProcess) {
            refreshType
                    = (Objects.equals(ProcessOrderType.OVERSEAS_REPAIR,
                    processOrderType)) ? MissingInfoOperationType.CHECK_PROCEDURE :
                    MissingInfoOperationType.PERFORM_PROCEDURE_OPERATIONS;
        } else {
            refreshType
                    = (Objects.equals(ProcessOrderType.OVERSEAS_REPAIR,
                    processOrderType)) ? MissingInfoOperationType.CHECK_MATERIAL_PROCEDURE :
                    MissingInfoOperationType.PERFORM_MATERIAL_PROCEDURE_OPERATIONS;
        }
        processOrderBaseService.refreshMissingInfo(processOrderNo, operatorUserBo, refreshType, true);

        ProcessOrderPo again = processOrderDao.getByProcessOrderNo(processOrderNo);
        log.info("完成刷新缺失信息：本次完成刷新的加工单号是：{}，加工单状态是：{}，缺失信息是：{}", processOrderNo,
                again.getProcessOrderStatus(), again.getMissingInformation());
    }

    /**
     * @Description 获取加工单生产资料
     * @author yanjiawei
     * @Date 2024/11/15 09:58
     */
    public ProcessOrderProductionInfoVo getProcProdInfoVo(ProcessOrderGenerateInfoDto dto) {
        String processSku = dto.getProcessSku();
        String platform = dto.getPlatform();

        ProcessOrderProductionInfoBo procProdInfoBo = processOrderBaseService.getProcProdInfoBo(processSku, platform);
        return ProcessOrderProductionConverter.toVo(procProdInfoBo);
    }


    /**
     * @Description 完成加工质检
     * @author yanjiawei
     * @Date 2023/10/11 19:54
     */
    @RedisLock(prefix = ScmRedisConstant.PROCESS_COMPLETE_CHECK_LOCK_PREFIX, key = "#processOrderQcOrderFinishedBo" +
            ".qcOrderNo", waitTime = 1, leaseTime = -1)
    public void completeQc(ProcessOrderQcOrderFinishedBo processOrderQcOrderFinishedBo) {
        final String qcOrderNo = processOrderQcOrderFinishedBo.getQcOrderNo();
        final ProcessOrderExtraPo processOrderExtraPo = processOrderExtraDao.getByCheckOrderNo(qcOrderNo);
        if (Objects.isNull(processOrderExtraPo)) {
            throw new BizException("质检单关联的加工单不存在");
        }

        final ProcessOrderPo processOrderPo
                = processOrderDao.getByProcessOrderNo(processOrderExtraPo.getProcessOrderNo());
        if (!ProcessOrderStatus.CHECKING.equals(processOrderPo.getProcessOrderStatus())) {
            throw new BizException("加工单需处于质检状态才能操作");
        }
        if (StringUtils.isNotBlank(processOrderExtraPo.getCheckUser())) {
            throw new BizException("重复处理质检结果");
        }

        // 校验正品数+次品数是否等于发货数
        final String processOrderNo = processOrderExtraPo.getProcessOrderNo();
        final List<ProcessOrderItemPo> processOrderItemPos = processOrderItemDao.getByProcessOrderNo(processOrderNo);
        final List<ProcessOrderProcedurePo> processOrderProcedurePoList
                = processOrderProcedureDao.getByProcessOrderNo(processOrderNo);
        final List<ProcessOrderScanPo> processOrderScanPoList = processOrderScanDao.getByProcessOrderNo(processOrderNo);

        int qualityGoodsCnt = 0;
        if ((!ProcessOrderType.REWORKING.equals(
                processOrderPo.getProcessOrderType()) && !ProcessOrderType.LIMITED_REWORKING.equals(
                processOrderPo.getProcessOrderType())) && CollectionUtils.isNotEmpty(processOrderProcedurePoList)) {
            List<ProcessOrderScanPo> needReverseProcessOrderScanPoList = Optional.ofNullable(processOrderScanPoList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .sorted(Comparator.comparing(ProcessOrderScanPo::getCreateTime)
                            .reversed())
                    .collect(Collectors.toList());
            Optional<ProcessOrderScanPo> latestProcessOrderScanPoFirst = needReverseProcessOrderScanPoList.stream()
                    .findFirst();
            if (latestProcessOrderScanPoFirst.isEmpty()) {
                throw new BizException("不存在扫码记录");
            }
            ProcessOrderScanPo processOrderScanPo = latestProcessOrderScanPoFirst.get();
            qualityGoodsCnt = processOrderScanPo.getQualityGoodsCnt() + processOrderScanPo.getDefectiveGoodsCnt();
        }

        Integer allCheckCount;
        Optional<ProcessOrderItemPo> firstItemOptional = processOrderItemPos.stream()
                .findFirst();
        if (firstItemOptional.isEmpty()) {
            throw new BizException("加工单不存在成品");
        }
        ProcessOrderItemPo processOrderItemPo = firstItemOptional.get();
        if (ProcessOrderType.REWORKING.equals(processOrderPo.getProcessOrderType()) ||
                ProcessOrderType.LIMITED_REWORKING.equals(processOrderPo.getProcessOrderType()) ||
                CollectionUtils.isEmpty(processOrderProcedurePoList)) {
            allCheckCount = processOrderItemPo.getProcessNum();
        } else {
            allCheckCount = qualityGoodsCnt;
        }

        // 更改质检产品详情
        List<ProcessOrderQcOrderDetailFinishedBo> checkedSkus = processOrderQcOrderFinishedBo.getQcOrderDetails();
        Integer finalAllCheckCount = allCheckCount;
        checkedSkus.forEach(item -> {
            Integer all = item.getPassAmount() + item.getNotPassAmount();
            if (!all.equals(finalAllCheckCount)) {
                throw new BizException("正品数和次品数之和必须等于发货数");
            }
        });

        int sum = checkedSkus.stream()
                .mapToInt(ProcessOrderQcOrderDetailFinishedBo::getPassAmount)
                .sum();
        // 记录状态变更日志
        OperatorUserBo operatorUserBo = new OperatorUserBo();
        operatorUserBo.setOperator(processOrderQcOrderFinishedBo.getOperator());
        operatorUserBo.setOperatorUsername(processOrderQcOrderFinishedBo.getOperatorName());

        if (sum == 0) {
            // 需要将状态回退到完工待交接
            processOrderPo.setProcessOrderStatus(ProcessOrderStatus.STORED);
            processOrderDao.updateByIdVersion(processOrderPo);

            List<ProcessOrderItemPo> needProcessOrderItemPos = processOrderItemPos.stream()
                    .peek(item -> {
                        Optional<ProcessOrderQcOrderDetailFinishedBo> optionalSkuItem = checkedSkus.stream()
                                .filter(it -> it.getBatchCode()
                                        .equals(item.getSkuBatchCode()))
                                .findFirst();
                        if (optionalSkuItem.isPresent()) {
                            ProcessOrderQcOrderDetailFinishedBo skuItem = optionalSkuItem.get();
                            item.setQualityGoodsCnt(item.getQualityGoodsCnt() + skuItem.getPassAmount());
                            item.setDefectiveGoodsCnt(item.getDefectiveGoodsCnt() + skuItem.getNotPassAmount());
                        }
                    })
                    .collect(Collectors.toList());
            processOrderItemDao.updateBatchByIdVersion(needProcessOrderItemPos);
            processOrderBaseService.createStatusChangeLog(processOrderPo, processOrderExtraPo, operatorUserBo);

        } else {
            processOrderPo.setCheckedTime(processOrderQcOrderFinishedBo.getBizTime());
            processOrderPo.setProcessOrderStatus(ProcessOrderStatus.WAIT_DELIVERY);

            processOrderExtraPo.setCheckUser(processOrderQcOrderFinishedBo.getOperator());
            processOrderExtraPo.setCheckUsername(processOrderQcOrderFinishedBo.getOperatorName());

            processOrderDao.updateByIdVersion(processOrderPo);
            processOrderExtraDao.updateByIdVersion(processOrderExtraPo);

            List<ProcessOrderItemPo> needProcessOrderItemPos = processOrderItemPos.stream()
                    .peek(item -> {
                        Optional<ProcessOrderQcOrderDetailFinishedBo> optionalSkuItem = checkedSkus.stream()
                                .filter(it -> it.getBatchCode()
                                        .equals(item.getSkuBatchCode()))
                                .findFirst();
                        if (optionalSkuItem.isPresent()) {
                            ProcessOrderQcOrderDetailFinishedBo skuItem = optionalSkuItem.get();
                            item.setQualityGoodsCnt(item.getQualityGoodsCnt() + skuItem.getPassAmount());
                            item.setDefectiveGoodsCnt(item.getDefectiveGoodsCnt() + skuItem.getNotPassAmount());
                        }
                    })
                    .collect(Collectors.toList());
            processOrderItemDao.updateBatchByIdVersion(needProcessOrderItemPos);
            processOrderBaseService.createStatusChangeLog(processOrderPo, processOrderExtraPo, operatorUserBo);
            this.sendStatusDingTalkMessage(processOrderPo.getProcessOrderNo());

            // 自动发货
            ProcessOrderConfirmDeliverDto processOrderConfirmDeliverDto = new ProcessOrderConfirmDeliverDto();
            processOrderConfirmDeliverDto.setProcessOrderId(processOrderPo.getProcessOrderId());
            this.confirmDeliver(processOrderConfirmDeliverDto, operatorUserBo);
        }
    }

    /**
     * @Description 创建加工质检单
     * @author yanjiawei
     * @Date 2023/10/12 13:48
     */
    public void createQcOrderByProcessOrder(List<ProcessOrderPo> processOrderPoList,
                                            ProcessOrderCompleteHandoverListDto dto) {
        // 完工交接容器信息
        final List<ProcessOrderCompleteHandoverDto> processOrderCompleteHandoverDtoList = dto.getProcessOrderList();

        final List<String> processOrderNos = processOrderPoList.stream()
                .map(ProcessOrderPo::getProcessOrderNo)
                .collect(Collectors.toList());

        // 加工单明细
        final List<ProcessOrderItemPo> processOrderItemPos = processOrderItemDao.getByProcessOrderNos(processOrderNos);
        // 加工单工序
        final List<ProcessOrderProcedurePo> processOrderProcedurePoList
                = processOrderProcedureDao.getByProcessOrderNos(processOrderNos);
        // 加工单扫码记录
        final List<ProcessOrderScanPo> processOrderScanPoList
                = processOrderScanDao.getByProcessOrderNos(processOrderNos);

        // 构建创建加工质检单数据模型
        List<QcOrderCreateMqDto.QcOrderCreateDto> qcOrderCreateDtoList = processOrderPoList.stream()
                .map(item -> {
                    // 校验加工单明细是否存在
                    final String processOrderNo = item.getProcessOrderNo();
                    final List<ProcessOrderItemPo> matchProcessOrderItem = processOrderItemPos.stream().
                            filter(processOrderItemPo -> Objects.equals(processOrderNo,
                                    processOrderItemPo.getProcessOrderNo()))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(matchProcessOrderItem)) {
                        throw new BizException("创建质检单失败！原因：加工单明细信息为空:{} 质检单号：", processOrderNo);
                    }

                    // 校验完工交接容器信息
                    final Long processOrderId = item.getProcessOrderId();
                    ProcessOrderCompleteHandoverDto matchProcessOrderHandover
                            = processOrderCompleteHandoverDtoList.stream()
                            .filter(handoverProcessOrder -> Objects.equals(processOrderId,
                                    handoverProcessOrder.getProcessOrderId()))
                            .findFirst()
                            .orElse(null);
                    if (Objects.isNull(matchProcessOrderHandover) || StrUtil.isBlank(
                            matchProcessOrderHandover.getContainerCode())) {
                        throw new BizException("加工单完工交接信息为空或者容器码不存在");
                    }

                    // 校验非返工单工序信息
                    final ProcessOrderStatus processOrderStatus = item.getProcessOrderStatus();
                    final List<ProcessOrderProcedurePo> matchProcessOrderProcedure
                            = processOrderProcedurePoList.stream()
                            .filter(procedure -> Objects.equals(processOrderNo, procedure.getProcessOrderNo()))
                            .collect(Collectors.toList());

                    int qualityGoodsCnt = 0;
                    // 质检数量 = 非返工订单且存在工序，最后一道工序扫码记录正品数+次品数
                    if (!Objects.equals(ProcessOrderStatus.REWORKING, processOrderStatus) && CollectionUtils.isNotEmpty(
                            matchProcessOrderProcedure)) {
                        final List<ProcessOrderScanPo> matchProcessOrderScanList = processOrderScanPoList.stream()
                                .filter(scan -> Objects.equals(processOrderNo, scan.getProcessOrderNo()))
                                .collect(Collectors.toList());
                        if (CollectionUtils.isEmpty(matchProcessOrderScanList)) {
                            throw new BizException("创建质检单异常！原因：非返工类型加工单存在工序但不存在扫码记录！");
                        }
                        ProcessOrderScanPo latestProcessOrderScanPo = matchProcessOrderScanList
                                .stream()
                                .sorted(Comparator.comparing(ProcessOrderScanPo::getCreateTime)
                                        .reversed())
                                .collect(Collectors.toList())
                                .stream()
                                .findFirst()
                                .orElse(null);
                        if (Objects.isNull(latestProcessOrderScanPo)) {
                            throw new BizException(
                                    "创建质检单异常！原因：非返工类型加工单存在工序但不存在最后一道扫码记录！");
                        }
                        qualityGoodsCnt
                                =
                                latestProcessOrderScanPo.getQualityGoodsCnt() + latestProcessOrderScanPo.getDefectiveGoodsCnt();
                    }

                    // 加工单容器码
                    final String processOrderContainerCode = matchProcessOrderHandover.getContainerCode();
                    // 返工订单或者不存在工序信息
                    boolean isReworkOrNoProcedure
                            = ProcessOrderStatus.REWORKING.equals(
                            item.getProcessOrderStatus()) || CollectionUtils.isEmpty(matchProcessOrderProcedure);

                    // 质检明细
                    int finalQualityGoodsCnt = qualityGoodsCnt;
                    List<QcOrderCreateMqDto.GoodDetail> goodDetails = matchProcessOrderItem.stream()
                            .map(it -> {
                                QcOrderCreateMqDto.GoodDetail goodDetail = new QcOrderCreateMqDto.GoodDetail();
                                int amount = isReworkOrNoProcedure ? it.getProcessNum() : finalQualityGoodsCnt;
                                goodDetail.setContainerCode(processOrderContainerCode);
                                goodDetail.setSkuCode(it.getSku());
                                goodDetail.setBatchCode(it.getSkuBatchCode());
                                goodDetail.setAmount(amount);
                                return goodDetail;
                            })
                            .collect(Collectors.toList());
                    QcOrderCreateMqDto.QcOrderCreateDto qcOrderCreateDto = new QcOrderCreateMqDto.QcOrderCreateDto();
                    qcOrderCreateDto.setGoodDetailList(goodDetails);

                    qcOrderCreateDto.setProcessOrderNo(item.getProcessOrderNo());
                    qcOrderCreateDto.setQcType(QcType.ALL_CHECK);
                    qcOrderCreateDto.setWarehouseCode(ScmConstant.PROCESS_WAREHOUSE_CODE);
                    qcOrderCreateDto.setOperator(GlobalContext.getUserKey());
                    qcOrderCreateDto.setOperatorName(GlobalContext.getUsername());
                    qcOrderCreateDto.setPlatform(item.getPlatform());

                    ProcessOrderType processOrderType = processOrderPoList.stream()
                            .filter(processOrderPo -> Objects.equals(processOrderNo,
                                    processOrderPo.getProcessOrderNo()))
                            .findFirst()
                            .map(ProcessOrderPo::getProcessOrderType)
                            .orElse(null);
                    qcOrderCreateDto.setProcessOrderType(processOrderType);
                    return qcOrderCreateDto;
                })
                .collect(Collectors.toList());

        QcOrderCreateMqDto qcOrderCreateMqDto = new QcOrderCreateMqDto();
        qcOrderCreateMqDto.setQcOrderCreateDtoList(qcOrderCreateDtoList);
        ProcessOrderCreateQcBo processOrderCreateQcBo = QcOrderConverter.toProcessOrderCreateQcBo(qcOrderCreateMqDto);

        AbstractQcOrderCreator<ProcessOrderCreateQcBo, ProcessOrderCreateQcResultBo> qcOrderCreator
                = new ProcessOrderQcOrderCreator(qcOrderDao, qcDetailDao, idGenerateService,
                SpringUtil.getBean(this.getClass()), consistencySendMqService,
                plmRemoteService);
        qcOrderCreator.createQcOrder(processOrderCreateQcBo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void quitProcessOrderBatchJob() {
        log.info("开始读取待作废加工单");

        Set<String> processOrderNos = Sets.newHashSet();
        int index = 0;
        String employeeNo = "";
        while ((employeeNo = environment.getProperty(StrUtil.format("quitProcessOrders[{}]", index))) != null) {
            processOrderNos.add(employeeNo);
            index++;
        }

        Set<String> successProcessOrderNos = Sets.newHashSet();
        Set<String> failProcessOrderNos = Sets.newHashSet();

        log.info("读取完成待作废加工单，条数:{}", processOrderNos.size());

        for (String processOrderNo : processOrderNos) {
            log.info("执行作废加工单，加工单号:{}", processOrderNo);
            ProcessOrderPo quitProcessOrder = processOrderDao.getByProcessOrderNo(processOrderNo);
            if (Objects.isNull(quitProcessOrder)) {
                failProcessOrderNos.add(processOrderNo);
                log.error("执行作废加工单 {} 失败，加工单信息不存在，本次跳过！", processOrderNo);
                continue;
            }

            Set<ProcessOrderType> nonVoidableTypes
                    = new HashSet<>(Arrays.asList(ProcessOrderType.LIMITED, ProcessOrderType.LIMITED_REWORKING));
            if (nonVoidableTypes.contains(quitProcessOrder.getProcessOrderType())) {
                failProcessOrderNos.add(processOrderNo);
                log.error("执行作废加工单 {} 失败，limited 状态的加工单不能作废，本次跳过！", processOrderNo);
                continue;
            }

            final NeedProcessPlan needProcessPlan = quitProcessOrder.getNeedProcessPlan();
            Set<ProcessOrderStatus> validStatuses;
            if (Objects.equals(NeedProcessPlan.TRUE, needProcessPlan)) {
                // 如果needProcessPlan为true，校验状态需要是待齐备或待排产
                validStatuses
                        = new HashSet<>(Arrays.asList(ProcessOrderStatus.WAIT_READY, ProcessOrderStatus.WAIT_PLAN));
            } else {
                // 如果needProcessPlan为false，校验状态需要是待齐备、待排产或待投产
                validStatuses
                        = new HashSet<>(Arrays.asList(ProcessOrderStatus.WAIT_READY, ProcessOrderStatus.WAIT_PLAN,
                        ProcessOrderStatus.WAIT_PRODUCE));
            }
            if (!validStatuses.contains(quitProcessOrder.getProcessOrderStatus())) {
                failProcessOrderNos.add(processOrderNo);
                log.error("执行作废加工单 {} 失败，needProcessPlan:{}的加工单 当前状态：{} 不可作废，本次跳过！",
                        processOrderNo, needProcessPlan, quitProcessOrder.getProcessOrderStatus());
                continue;
            }

            Boolean result
                    = processOrderBaseService.changeStatus(quitProcessOrder, ProcessOrderStatus.DELETED,
                    new OperatorUserBo());
            // 通知wms取消出库单接口
            if (result) {
                final ProcessOrderCancelEventDto processOrderCancelEventDto = new ProcessOrderCancelEventDto();
                processOrderCancelEventDto.setProcessOrderNo(quitProcessOrder.getProcessOrderNo());
                processOrderCancelEventDto.setDeliveryType(WmsEnum.DeliveryType.PROCESS);
                processOrderCancelEventDto.setKey(quitProcessOrder.getProcessOrderNo());
                processOrderCancelEventDto.setOperator(GlobalContext.getUserKey());
                processOrderCancelEventDto.setOperatorName(GlobalContext.getUsername());
                consistencySendMqService.execSendMq(WmsProcessCancelHandler.class, processOrderCancelEventDto);
            }

            successProcessOrderNos.add(processOrderNo);
        }

        log.info("执行作废加工单结束，总条数：{},加工单号:{}",
                processOrderNos.size(), processOrderNos.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")));
        log.info("执行作废加工单结束，成功条数：{},加工单号:{}",
                successProcessOrderNos.size(), successProcessOrderNos.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")));
        log.info("执行作废加工单结束，失败条数：{},加工单号:{}",
                failProcessOrderNos.size(), failProcessOrderNos.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")));
    }

    public QcOriginProperty getQcOriginProperty(ProcessOrderType processOrderType) {
        QcOriginProperty originProperty;
        if (Objects.equals(ProcessOrderType.NORMAL, processOrderType)) {
            originProperty = QcOriginProperty.NORMAL;
        } else if (Objects.equals(ProcessOrderType.EXTRA, processOrderType)) {
            originProperty = QcOriginProperty.EXTRA;
        } else if (Objects.equals(ProcessOrderType.LIMITED, processOrderType)) {
            originProperty = QcOriginProperty.LIMITED;
        } else if (Objects.equals(ProcessOrderType.REWORKING, processOrderType)) {
            originProperty = QcOriginProperty.REWORKING;
        } else if (Objects.equals(ProcessOrderType.LIMITED_REWORKING, processOrderType)) {
            originProperty = QcOriginProperty.LIMITED_REWORKING;
        } else if (Objects.equals(ProcessOrderType.OVERSEAS_REPAIR, processOrderType)) {
            originProperty = QcOriginProperty.OVERSEAS_REPAIR;
        } else if (Objects.equals(ProcessOrderType.WH, processOrderType)) {
            originProperty = QcOriginProperty.WH;
        } else if (Objects.equals(ProcessOrderType.REPAIR, processOrderType)) {
            originProperty = QcOriginProperty.PROC_REPAIR;
        } else {
            throw new BizException("创建加工质检单失败！无法通过加工单类型获取对应标识");
        }

        return originProperty;
    }

    public QcOriginBo getQcOriginBo(String processOrderNo) {
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (Objects.isNull(processOrderPo)) {
            return null;
        }

        QcOriginBo qcOriginBo = new QcOriginBo();
        qcOriginBo.setQcOrigin(QcOrigin.PROCESS_ORDER);

        ProcessOrderType processOrderType = processOrderPo.getProcessOrderType();
        QcOriginProperty qcOriginProperty = getQcOriginProperty(processOrderType);
        qcOriginBo.setQcOriginProperty(qcOriginProperty);

        return qcOriginBo;
    }

    /**
     * 根据查询条件获取仓库列表信息。
     *
     * @param queryProcOrderWarehouseInfoDto 查询条件
     * @return 仓库列表信息
     */
    public List<WarehouseListVo> getWarehouseList(QueryProcOrderWarehouseInfoDto queryProcOrderWarehouseInfoDto) {
        // 获取加工单类型
        ProcessOrderType curProcessOrderType = queryProcOrderWarehouseInfoDto.getProcessOrderType();

        // 获取限制仓库编码的映射
        Map<ProcessOrderType, Set<String>> selectableWarehouseCodeMap
                = processOrderBaseService.getSelectableWarehouseCodeMap(
                Collections.singleton(curProcessOrderType));

        // 根据加工单类型获取对应的限制仓库编码列表
        Set<String> restrictedWarehouseCodes = selectableWarehouseCodeMap.get(curProcessOrderType);

        // 根据限制仓库编码列表获取仓库信息
        if (CollectionUtils.isNotEmpty(restrictedWarehouseCodes)) {
            List<WarehouseVo> warehouseVos = wmsRemoteService.listByWarehouseCodes(restrictedWarehouseCodes);
            return ProcWarehouseConverter.convertWarehouseListVos(warehouseVos);
        } else {
            List<WarehouseVo> allWarehouseVos = wmsRemoteService.getAllWarehouse();
            return ProcWarehouseConverter.convertWarehouseListVos(allWarehouseVos);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void refreshPromiseDateDelayed() {
        PageExecutor<ProcessOrderPo> processOrderPoPageExecutor = new PageExecutor<>();

        processOrderPoPageExecutor.doForPage(MAX_ITERATIONS, (page) -> {
            List<ProcessOrderStatus> queryStatus = Arrays.asList(ProcessOrderStatus.WAIT_READY,
                    ProcessOrderStatus.WAIT_PLAN,
                    ProcessOrderStatus.WAIT_PRODUCE,
                    ProcessOrderStatus.PRODUCED,
                    ProcessOrderStatus.PROCESSING,
                    ProcessOrderStatus.WAIT_MOVING,
                    ProcessOrderStatus.REWORKING);
            return processOrderDao.selectPage(page, queryStatus);
        }, records -> {
            // 遍历订单列表
            for (ProcessOrderPo processOrderPo : records) {
                processOrderBaseService.updatePromiseDateDelayed(processOrderPo);
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void refreshPromiseDateDelayed(RefreshPromiseDateDelayedBo refreshPromiseDateDelayedBo) {
        Set<String> processOrderNos = refreshPromiseDateDelayedBo.getProcessOrderNos();
        List<ProcessOrderPo> processOrderPos = processOrderDao.getByProcessOrderNos(processOrderNos);

        for (ProcessOrderPo processOrderPo : processOrderPos) {
            processOrderBaseService.updatePromiseDateDelayed(processOrderPo);
        }
    }

    /**
     * 检查SKU关联加工单是否存在的方法
     *
     * @param dto SKU存在性检查的请求DTO对象，包含SKU列表、创建时间范围等信息
     * @return 包含每个SKU存在性信息的DTO列表
     */
    public List<SkuExistenceResponseDto> checkSkuExistence(SkuExistenceRequestDto dto) {
        // 前置校验
        Set<String> skuList
                = ParamValidUtils.requireNotEmpty(dto.getSkuList(), "sku列表不能为空");
        ParamValidUtils.requireLessThanOrEqual(skuList.size(), 200, "sku列表最大200条");

        LocalDateTime createTimeBegin
                = ParamValidUtils.requireNotNull(dto.getCreateTimeBegin(), "创建时间开始范围不能为空");
        LocalDateTime createTimeEnd
                = ParamValidUtils.requireNotNull(dto.getCreateTimeEnd(), "创建时间结束范围不能为空");

        // 构建结果列表
        List<SkuExistenceResponseDto> resultDtoList = ProcessOrderBuilder.buildSkuExistenceResponseDto(skuList);

        // 根据sku列表查询对应的加工明细
        List<ProcessOrderItemPo> processOrderItemPos = processOrderItemDao.getBySkus(skuList);
        // 如果查询结果为空，直接返回默认结果列表
        if (CollectionUtils.isEmpty(processOrderItemPos)) {
            return resultDtoList;
        }

        // 获取查询到的加工单号列表
        List<String> processOrderNos = processOrderItemPos.stream()
                .map(ProcessOrderItemPo::getProcessOrderNo)
                .collect(Collectors.toList());
        // 如果加工单号为空，直接返回默认结果列表
        if (CollectionUtils.isEmpty(processOrderNos)) {
            return resultDtoList;
        }
        // 查询创建时间属于时间范围，且状态不等于已作废的加工单号
        List<ProcessOrderPo> processOrderPos
                = processOrderDao.listByTimeRangeAndExcludeStatusAndOrderNos(createTimeBegin, createTimeEnd,
                ProcessOrderStatus.DELETED,
                processOrderNos);
        // 如果查询结果为空，直接返回默认结果列表
        if (CollectionUtils.isEmpty(processOrderPos)) {
            return resultDtoList;
        }

        // 更新SKU结果DTO列表中的exists属性
        for (SkuExistenceResponseDto skuExistenceResponseDto : resultDtoList) {
            String sku = skuExistenceResponseDto.getSku();
            // 根据 SKU 进行过滤，得到匹配的加工单明细列表
            List<ProcessOrderItemPo> matchItem = processOrderItemPos.stream()
                    .filter(processOrderItemPo -> Objects.equals(sku, processOrderItemPo.getSku()))
                    .collect(Collectors.toList());

            // 如果匹配的加工单条目列表为空，则跳过当前循环
            if (CollectionUtils.isEmpty(matchItem)) {
                continue;
            }

            // 提取匹配的加工单条目列表中的加工单号
            List<String> matchProcessOrderNos = matchItem.stream()
                    .map(ProcessOrderItemPo::getProcessOrderNo)
                    .collect(Collectors.toList());
            // 检查加工单号是否存在于满足条件的加工单列表中
            boolean exist = processOrderPos.stream()
                    .anyMatch(processOrderPo -> matchProcessOrderNos.contains(processOrderPo.getProcessOrderNo()));
            skuExistenceResponseDto.setExists(exist);
        }
        return resultDtoList;
    }


    public List<MaterialInfoVo> getMaterialInfo(MaterialInfoReqDto dto) {
        //获取加工单号
        String processOrderNo = dto.getProcessOrderNo();

        List<ProcessOrderMaterialPo> processOrderMaterialPos = processOrderMaterialDao.getByProcessOrderNo(processOrderNo);
        if (CollectionUtils.isEmpty(processOrderMaterialPos)) {
            return Lists.newArrayList();
        }
        //获取sku列表
        List<String> skuList = processOrderMaterialPos.stream().map(ProcessOrderMaterialPo::getSku).collect(Collectors.toList());
        List<PlmSkuVo> skuEncodeBySku = plmRemoteService.getSkuEncodeBySku(skuList);
        //构建SKU产品名称关系
        Map<String, String> skuNameMap = skuEncodeBySku.stream().collect(Collectors.toMap(PlmSkuVo::getSkuCode, PlmSkuVo::getSkuEncode));

        Set<String> deliveryNoList = Sets.newHashSet();

        //获取出库单列表
        List<String> materialDeliveryNoList = processOrderMaterialPos
                .stream().distinct().map(ProcessOrderMaterialPo::getDeliveryNo).filter(StrUtil::isNotEmpty).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(materialDeliveryNoList)) {
            deliveryNoList.addAll(materialDeliveryNoList);
        }

        //获取原料收货信息 & 原料收货明细
        List<ProcessMaterialReceiptPo> materialReceivePos
                = processMaterialReceiptDao.getByProcessOrderNo(processOrderNo);
        List<Long> materialReceiveIds
                = materialReceivePos.stream().map(ProcessMaterialReceiptPo::getProcessMaterialReceiptId).collect(Collectors.toList());
        List<ProcessMaterialReceiptItemPo> materialReceiveItemPos
                = processMaterialReceiptItemDao.getByMaterialReceiptIds(materialReceiveIds);

        //获取出库单号列表
        List<String> materialReDeliveryNoList = materialReceivePos.stream().map(ProcessMaterialReceiptPo::getDeliveryNo).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(materialReDeliveryNoList)) {
            deliveryNoList.addAll(materialReDeliveryNoList);
        }

        List<ProcessDeliveryOrderVo> deliveryOrderVoList
                = Optional.ofNullable(wmsRemoteService.getDeliveryOrderByDeliverNo(new ArrayList<>(deliveryNoList))).orElse(Lists.newArrayList());

        //获取原料归还信息 & 归还明细
        List<ProcessMaterialBackPo> materialBackPos
                = processMaterialBackDao.getByProcessOrderNo(processOrderNo);

        //按批次码分组原料归还明细
        List<Long> materialBackIds
                = materialBackPos.stream().map(ProcessMaterialBackPo::getProcessMaterialBackId).collect(Collectors.toList());
        List<ProcessMaterialBackItemPo> materialBackItemPos
                = processMaterialBackItemDao.listByProcessMaterialBackIds(materialBackIds);
        Map<String, List<ProcessMaterialBackItemPo>> materialBackItemPosMap
                = materialBackItemPos.stream().collect(Collectors.groupingBy(ProcessMaterialBackItemPo::getSkuBatchCode));

        //获取收货单号列表
        List<String> receiveNoList = materialBackPos.stream().map(ProcessMaterialBackPo::getReceiptNo).collect(Collectors.toList());
        ReceiveOrderGetDto queryParam = new ReceiveOrderGetDto();
        queryParam.setReceiveOrderNoList(receiveNoList);
        List<ReceiveOrderForScmVo> receiveOrderForScmVos = Optional.ofNullable(wmsRemoteService.getReceiveOrderList(queryParam)).orElse(Lists.newArrayList());

        //按SKU分组原料信息
        Map<String, List<ProcessOrderMaterialPo>> processOrderMaterialPosMap
                = processOrderMaterialPos.stream().collect(Collectors.groupingBy(ProcessOrderMaterialPo::getSku));

        return processOrderMaterialPosMap.entrySet().stream().map(entry -> {
            MaterialInfoVo materialInfoVo = new MaterialInfoVo();

            materialInfoVo.setSku(entry.getKey());
            materialInfoVo.setSkuEncode(skuNameMap.get(entry.getKey()));

            List<ProcessOrderMaterialPo> value = entry.getValue();
            materialInfoVo.setPlanDeliveryNum(value.stream().mapToInt(ProcessOrderMaterialPo::getDeliveryNum).sum());

            List<String> matchDeliveryNoList = value.stream().map(ProcessOrderMaterialPo::getDeliveryNo).filter(StrUtil::isNotEmpty).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(matchDeliveryNoList)) {
                return materialInfoVo;
            }

            List<MaterialInfoVo.MaterialReceiveInfoVo> totalMaterialReceiveInfoVoList = Lists.newArrayList();

            List<ProcessDeliveryOrderVo> nonSingedOffList = deliveryOrderVoList.stream()
                    .filter(deliveryOrderVo -> matchDeliveryNoList.contains(deliveryOrderVo.getDeliveryOrderNo()) && !Objects.equals(WmsEnum.DeliveryState.SIGNED_OFF, deliveryOrderVo.getDeliveryState()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(nonSingedOffList)) {
                MaterialInfoVo.MaterialReceiveInfoVo materialReceiveInfoVo = new MaterialInfoVo.MaterialReceiveInfoVo();

                List<MaterialInfoVo.MaterialDeliveryInfoVo> materialDeliveryInfoVoList = nonSingedOffList.stream().map(nonSingedOff -> {
                    MaterialInfoVo.MaterialDeliveryInfoVo materialDeliveryInfoVo = new MaterialInfoVo.MaterialDeliveryInfoVo();
                    materialDeliveryInfoVo.setDeliveryNo(nonSingedOff.getDeliveryOrderNo());
                    materialDeliveryInfoVo.setWarehouseCode(nonSingedOff.getWarehouseCode());
                    materialDeliveryInfoVo.setWarehouseName(nonSingedOff.getWarehouseName());
                    materialDeliveryInfoVo.setDeliveryState(nonSingedOff.getDeliveryState());
                    return materialDeliveryInfoVo;
                }).collect(Collectors.toList());
                materialReceiveInfoVo.setMaterialDeliveryInfoVoList(materialDeliveryInfoVoList);

                totalMaterialReceiveInfoVoList.add(materialReceiveInfoVo);
            }

            List<ProcessDeliveryOrderVo> singedOffList = deliveryOrderVoList.stream()
                    .filter(deliveryOrderVo -> matchDeliveryNoList.contains(deliveryOrderVo.getDeliveryOrderNo()) && Objects.equals(WmsEnum.DeliveryState.SIGNED_OFF, deliveryOrderVo.getDeliveryState()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(singedOffList)) {
                //获取出库单号
                List<String> singedOffDeliveryOrderNoList = singedOffList.stream().map(ProcessDeliveryOrderVo::getDeliveryOrderNo).collect(Collectors.toList());
                //匹配原料收货单
                List<ProcessMaterialReceiptPo> matchMaterialRePos
                        = materialReceivePos.stream().filter(materialReceiptPo -> singedOffDeliveryOrderNoList.contains(materialReceiptPo.getDeliveryNo())).collect(Collectors.toList());
                List<Long> matchReIds
                        = matchMaterialRePos.stream().map(ProcessMaterialReceiptPo::getProcessMaterialReceiptId).collect(Collectors.toList());
                List<ProcessMaterialReceiptItemPo> matchReItemPos = materialReceiveItemPos.stream()
                        .filter(materialReceiptItemPo -> matchReIds.contains(materialReceiptItemPo.getProcessMaterialReceiptId()) && Objects.equals(entry.getKey(), materialReceiptItemPo.getSku()))
                        .collect(Collectors.toList());

                //按批次码分组原料收货明细
                Map<String, List<ProcessMaterialReceiptItemPo>> materialReceiveItemPosMap
                        = matchReItemPos.stream().collect(Collectors.groupingBy(ProcessMaterialReceiptItemPo::getSkuBatchCode));

                //遍历分组后的原料收货明细组成MaterialReceiveInfoVo列表
                List<MaterialInfoVo.MaterialReceiveInfoVo> materialReceiveInfoVoList = materialReceiveItemPosMap.entrySet().stream().map(entry1 -> {
                    MaterialInfoVo.MaterialReceiveInfoVo materialReceiveInfoVo = new MaterialInfoVo.MaterialReceiveInfoVo();

                    String skuBatchCode = entry1.getKey();
                    materialReceiveInfoVo.setSkuBatchCode(skuBatchCode);

                    //按批次码分组后的原料收货明细
                    List<ProcessMaterialReceiptItemPo> value1 = entry1.getValue();
                    //根据processMaterialReceiptId分组构建map
                    Map<Long, List<ProcessMaterialReceiptItemPo>> materialReceiveItemPosMapByReceiptId
                            = value1.stream().collect(Collectors.groupingBy(ProcessMaterialReceiptItemPo::getProcessMaterialReceiptId));

                    //遍历map组成list
                    List<MaterialInfoVo.MaterialDeliveryInfoVo> materialDeliveryInfoVoList = materialReceiveItemPosMapByReceiptId.entrySet().stream().map(entry2 -> {
                        MaterialInfoVo.MaterialDeliveryInfoVo materialDeliveryInfoVo = new MaterialInfoVo.MaterialDeliveryInfoVo();

                        //匹配收货单
                        ProcessMaterialReceiptPo matchMaterialReceivePos = materialReceivePos.stream()
                                .filter(processMaterialReceiptPo -> Objects.equals(processMaterialReceiptPo.getProcessMaterialReceiptId(), entry2.getKey()))
                                .findFirst().orElse(null);
                        if (Objects.nonNull(matchMaterialReceivePos)) {
                            materialDeliveryInfoVo.setDeliveryNo(matchMaterialReceivePos.getDeliveryNo());
                            materialDeliveryInfoVo.setWarehouseCode(matchMaterialReceivePos.getDeliveryWarehouseCode());
                            materialDeliveryInfoVo.setWarehouseName(matchMaterialReceivePos.getDeliveryWarehouseName());

                            //匹配出库单状态
                            deliveryOrderVoList.stream()
                                    .filter(processDeliveryOrderVo -> Objects.equals(processDeliveryOrderVo.getDeliveryOrderNo(), matchMaterialReceivePos.getDeliveryNo()))
                                    .findFirst().ifPresent(processDeliveryOrderVo -> materialDeliveryInfoVo.setDeliveryState(processDeliveryOrderVo.getDeliveryState()));
                        }

                        materialDeliveryInfoVo.setDeliveryNum(entry2.getValue().stream().mapToInt(ProcessMaterialReceiptItemPo::getDeliveryNum).sum());
                        materialDeliveryInfoVo.setReceiveNum(entry2.getValue().stream().mapToInt(ProcessMaterialReceiptItemPo::getReceiptNum).sum());
                        return materialDeliveryInfoVo;
                    }).collect(Collectors.toList());
                    materialReceiveInfoVo.setMaterialDeliveryInfoVoList(materialDeliveryInfoVoList);

                    //按批次码分组后的原料归还明细
                    List<ProcessMaterialBackItemPo> value2 = materialBackItemPosMap.getOrDefault(skuBatchCode, Lists.newArrayList());
                    //根据processMaterialBackId分组构建map
                    Map<Long, List<ProcessMaterialBackItemPo>> materialBackItemPosMapByBackId
                            = value2.stream().collect(Collectors.groupingBy(ProcessMaterialBackItemPo::getProcessMaterialBackId));

                    //遍历map组成list
                    List<MaterialInfoVo.MaterialBackInfoVo> materialBackInfoVoList = materialBackItemPosMapByBackId.entrySet().stream().map(entry2 -> {
                        MaterialInfoVo.MaterialBackInfoVo materialBackInfoVo = new MaterialInfoVo.MaterialBackInfoVo();

                        //匹配收货单号
                        materialBackPos.stream()
                                .filter(processMaterialBackPo -> Objects.equals(processMaterialBackPo.getProcessMaterialBackId(), entry2.getKey()))
                                .findFirst().ifPresent(materialBackPo -> materialBackInfoVo.setReceiveNo(materialBackPo.getReceiptNo()));

                        //匹配收货单状态
                        receiveOrderForScmVos.stream()
                                .filter(receiveOrderForScmVo -> Objects.equals(receiveOrderForScmVo.getReceiveOrderNo(), materialBackInfoVo.getReceiveNo()))
                                .findFirst().ifPresent(receiveOrderForScmVo -> {
                                    materialBackInfoVo.setWarehouseCode(receiveOrderForScmVo.getWarehouseCode());
                                    materialBackInfoVo.setWarehouseName(receiveOrderForScmVo.getWarehouseName());
                                    materialBackInfoVo.setReceiveOrderState(receiveOrderForScmVo.getReceiveOrderState());

                                });

                        materialBackInfoVo.setReceiveNum(entry2.getValue().stream().mapToInt(ProcessMaterialBackItemPo::getDeliveryNum).sum());
                        return materialBackInfoVo;
                    }).collect(Collectors.toList());
                    materialReceiveInfoVo.setMaterialBackInfoVoList(materialBackInfoVoList);

                    return materialReceiveInfoVo;
                }).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(materialReceiveInfoVoList)) {
                    totalMaterialReceiveInfoVoList.addAll(materialReceiveInfoVoList);
                }

                //求和SKU总收货数
                int processReceiveNum = materialReceiveInfoVoList.stream()
                        .flatMap(receiveInfo -> receiveInfo.getMaterialDeliveryInfoVoList().stream())
                        .mapToInt(MaterialInfoVo.MaterialDeliveryInfoVo::getReceiveNum)
                        .sum();
                materialInfoVo.setProcessReceiveNum(processReceiveNum);

                int backReceiveNum = materialReceiveInfoVoList.stream()
                        .flatMap(backInfo -> backInfo.getMaterialBackInfoVoList().stream())
                        .mapToInt(MaterialInfoVo.MaterialBackInfoVo::getReceiveNum)
                        .sum();
                materialInfoVo.setBackReceiveNum(backReceiveNum);
            }

            materialInfoVo.setMaterialReceiveInfoVoList(totalMaterialReceiveInfoVoList);
            return materialInfoVo;
        }).collect(Collectors.toList());
    }


    public ProcRecOrderInfoVo getProcRecOrderInfo(ProcRecOrderInfoDto dto) {
        ProcessOrderExtraPo processOrderExtraPo = processOrderExtraDao.getByProcessOrderNo(dto.getProcessOrderNo());
        if (Objects.isNull(processOrderExtraPo)) {
            log.info("未找到加工单{}的额外信息", dto.getProcessOrderNo());
            return null;
        }

        String receiptOrderNo = processOrderExtraPo.getReceiptOrderNo();
        if (StringUtils.isBlank(receiptOrderNo)) {
            log.info("加工单{}的额外信息中未找到收货单号", dto.getProcessOrderNo());
            return null;
        }

        ReceiveOrderGetDto queryRecParam = new ReceiveOrderGetDto();
        queryRecParam.setReceiveOrderNoList(Collections.singletonList(receiptOrderNo));
        List<ReceiveOrderForScmVo> receiveOrderList = wmsRemoteService.getReceiveOrderList(queryRecParam);
        if (CollectionUtils.isEmpty(receiveOrderList)) {
            log.info("未找到收货单{}信息", receiptOrderNo);
            return null;
        }

        ProcRecOrderInfoVo procRecOrderInfoVo = new ProcRecOrderInfoVo();
        receiveOrderList.stream()
                .filter(receiveOrderForScmVo -> Objects.equals(receiveOrderForScmVo.getReceiveOrderNo(), receiptOrderNo))
                .findFirst().ifPresent(receiveOrderForScmVo -> {
                    procRecOrderInfoVo.setReceiveOrderNo(receiveOrderForScmVo.getReceiveOrderNo());
                    if (Objects.nonNull(receiveOrderForScmVo.getFinishOnShelfTime())) {
                        procRecOrderInfoVo.setFinishOnShelfTime(receiveOrderForScmVo.getFinishOnShelfTime());
                    }

                    List<ReceiveOrderForScmVo.OnShelfOrder> onShelfList = receiveOrderForScmVo.getOnShelfList();
                    if (CollectionUtils.isNotEmpty(onShelfList)) {
                        //求和实际上架数量
                        int actualShelfNum = onShelfList.stream().mapToInt(ReceiveOrderForScmVo.OnShelfOrder::getOnShelvesAmount).sum();
                        procRecOrderInfoVo.setActualShelfNum(actualShelfNum);
                    }
                });
        return procRecOrderInfoVo;
    }

    /**
     * @Description 拆分复杂工序加工单，按满5进行拆分
     * @author yanjiawei
     * @Date 2024/9/2 11:47
     */
    private ProcessOrderAndItemListBo splitComplexProcessNum(ProcessOrderCreateNewDto dto, List<ProcessOrderSampleBo> processOrderSampleBoList) {
        //复杂工序前置校验
        Integer processNum = dto.getProcessNum();
        if (processNum > COMPLEX_PROCESS_NUM_LIMIT && processNum < PROCESS_NUM_LIMIT) {
            throw new ParamIllegalException("该SKU工序复杂，最大加工数{},请核对后提交", COMPLEX_PROCESS_NUM_LIMIT);
        }

        //按满5加工数量拆分加工单
        List<Integer> splitList = new ArrayList<>();
        while (processNum > 0) {
            if (processNum >= COMPLEX_PROCESS_NUM_LIMIT) {
                splitList.add(COMPLEX_PROCESS_NUM_LIMIT);
                processNum -= COMPLEX_PROCESS_NUM_LIMIT;
            } else {
                splitList.add(processNum);
                processNum = 0;
            }
        }

        final List<ProcessOrderMaterialDto> processOrderMaterialDtoList = dto.getProcessOrderMaterials();

        List<ProcessOrderPo> processOrderPoList = new ArrayList<>();
        List<ProcessOrderItemPo> processOrderItemPoList = new ArrayList<>();
        List<ProcessOrderExtraPo> processOrderExtraPoList = new ArrayList<>();
        List<ProcessOrderMaterialRefBo> procMaterialRefBoList = new ArrayList<>();
        List<ProcessOrderProcedurePo> processOrderProcedurePoList = new ArrayList<>();
        List<ProcessOrderDescPo> processOrderDescPoList = new ArrayList<>();
        List<ProcessOrderSamplePo> processOrderSamplePoList = new ArrayList<>();

        List<Long> processIds = Optional.ofNullable(dto.getProcessOrderProcedures())
                .orElse(Collections.emptyList())
                .stream()
                .map(ProcessOrderCreateDto.ProcessOrderProcedure::getProcessId)
                .collect(Collectors.toList());
        Map<Long, List<ProcessPo>> groupedProcessPos = processDao.getByProcessIds(processIds)
                .stream()
                .collect(Collectors.groupingBy(ProcessPo::getProcessId));

        for (Integer splitItem : splitList) {
            ProcessOrderPo processOrderPo = new ProcessOrderPo();
            final String processOrderNo = idGenerateService.getConfuseCode(ScmConstant.PROCESS_ORDER_NO_PREFIX, TimeType.CN_DAY, ConfuseLength.L_4);
            processOrderPo.setProcessOrderNo(processOrderNo);
            processOrderPo.setProcessOrderType(dto.getProcessOrderType());
            processOrderPo.setPlatform(dto.getPlatCode());
            processOrderPo.setProcessOrderNote(dto.getProcessOrderNote());
            processOrderPo.setDeliveryNote(dto.getDeliveryNote());
            processOrderPo.setWarehouseCode(dto.getWarehouseCode());
            processOrderPo.setWarehouseName(dto.getWarehouseName());
            processOrderPo.setDeliveryWarehouseCode(dto.getDeliveryWarehouseCode());
            processOrderPo.setDeliveryWarehouseName(dto.getDeliveryWarehouseName());
            processOrderPo.setSpu(dto.getSpu());
            processOrderPo.setDeliverDate(ScmTimeUtil.setToEndOfDay(dto.getDeliverDate()));
            processOrderPo.setPromiseDate(processOrderPo.getDeliverDate());
            processOrderPo.setWarehouseTypes(String.join(",", Optional.ofNullable(dto.getWarehouseTypeList()).orElse(new ArrayList<>())));
            processOrderPo.setProcessOrderStatus(processOrderBaseService.parseProcessOrderStatusByProcessOrderType(dto.getProcessOrderType()));
            processOrderPo.setOverPlan(OverPlan.FALSE);
            processOrderPo.setNeedProcessPlan(NeedProcessPlan.FALSE);
            processOrderPo.setProcessPlanDelay(ProcessPlanDelay.FALSE);
            processOrderPo.setTotalSkuNum(1);
            processOrderPo.setTotalProcessNum(splitItem);
            processOrderPo.setProductQuality(dto.getProductQuality());
            processOrderPo.setFileCode(String.join(",", dto.getFileCodeList()));

            // 设置原料归还状态为"无需归还"
            if (CollectionUtils.isNotEmpty(processOrderMaterialDtoList)) {
                processOrderPo.setMaterialBackStatus(MaterialBackStatus.UN_BACK);
                processOrderPo.setIsReceiveMaterial(IsReceiveMaterial.FALSE);
            } else {
                processOrderPo.setMaterialBackStatus(MaterialBackStatus.NO_BACK);
                processOrderPo.setIsReceiveMaterial(IsReceiveMaterial.NO_RETURN_REQUIRED);
            }
            processOrderPoList.add(processOrderPo);

            // 加工明细
            ProcessOrderItemPo processOrderItemPo = new ProcessOrderItemPo();
            processOrderItemPo.setProcessOrderNo(processOrderNo);
            processOrderItemPo.setProcessNum(splitItem);
            processOrderItemPo.setSku(dto.getSku());
            processOrderItemPo.setIsFirst(BooleanType.TRUE);
            processOrderBaseService.generateBatchCodes(Collections.singletonList(processOrderItemPo));
            processOrderItemPoList.add(processOrderItemPo);

            // 加工单额外信息
            ProcessOrderExtraPo processOrderExtraPo = new ProcessOrderExtraPo();
            processOrderExtraPo.setProcessOrderNo(processOrderNo);
            processOrderExtraPoList.add(processOrderExtraPo);

            // 加工单原料
            List<ProcessOrderMaterialRefBo> procMaterialRefList = toProcessOrderMaterialRefBo(processOrderNo, processOrderMaterialDtoList);
            procMaterialRefBoList.addAll(procMaterialRefList);

            // 加工工序
            final List<ProcessOrderCreateDto.ProcessOrderProcedure> processOrderProcedureDtoList = dto.getProcessOrderProcedures();
            if (CollectionUtils.isNotEmpty(processOrderProcedureDtoList)) {
                processOrderProcedurePoList.addAll(processOrderProcedureDtoList.stream().map(item -> {
                            List<ProcessPo> processPos = groupedProcessPos.get(item.getProcessId());
                            if (CollectionUtils.isEmpty(processPos)) {
                                throw new BizException("工序信息:{}缺失配置，请联系系统管理员！", item.getProcessId());
                            }
                            final ProcessPo processPo = processPos.get(0);
                            ProcessOrderProcedurePo processOrderProcedurePo = new ProcessOrderProcedurePo();
                            processOrderProcedurePo.setProcessOrderNo(processOrderNo);
                            processOrderProcedurePo.setProcessId(processPo.getProcessId());
                            processOrderProcedurePo.setProcessCode(processPo.getProcessCode());
                            processOrderProcedurePo.setProcessName(processPo.getProcessName());
                            processOrderProcedurePo.setSort(item.getSort());
                            processOrderProcedurePo.setProcessLabel(processPo.getProcessLabel());
                            processOrderProcedurePo.setCommission(item.getCommission());
                            return processOrderProcedurePo;
                        })
                        .collect(Collectors.toList()));
            }

            // 加工工序描述
            final List<ProcessOrderCreateDto.ProcessOrderDesc> processOrderDescDtoList = dto.getProcessOrderDescs();
            if (CollectionUtils.isNotEmpty(processOrderDescDtoList)) {
                processOrderDescPoList.addAll(processOrderDescDtoList.stream()
                        .map(descDto -> {
                            ProcessOrderDescPo processOrderDescPo = new ProcessOrderDescPo();
                            processOrderDescPo.setProcessOrderNo(processOrderNo);
                            processOrderDescPo.setProcessDescName(descDto.getProcessDescName());
                            processOrderDescPo.setProcessDescValue(descDto.getProcessDescValue());
                            return processOrderDescPo;
                        })
                        .collect(Collectors.toList()));
            }

            // 加工生产信息
            if (CollectionUtils.isNotEmpty(processOrderSampleBoList)) {
                processOrderSamplePoList.addAll(processOrderSampleBoList.stream()
                        .map(sampleChildOrderInfo -> {
                            ProcessOrderSamplePo processOrderSamplePo = new ProcessOrderSamplePo();
                            processOrderSamplePo.setProcessOrderNo(processOrderNo);
                            processOrderSamplePo.setSampleChildOrderNo(sampleChildOrderInfo.getSampleChildOrderNo());
                            processOrderSamplePo.setSourceDocumentNumber(sampleChildOrderInfo.getSourceDocumentNumber());
                            processOrderSamplePo.setSampleInfoKey(sampleChildOrderInfo.getSampleInfoKey());
                            processOrderSamplePo.setSampleInfoValue(sampleChildOrderInfo.getSampleInfoValue());
                            return processOrderSamplePo;
                        })
                        .collect(Collectors.toList()));
            }
        }

        return ProcessOrderAndItemListBo.builder()
                .processOrderPoList(processOrderPoList)
                .processOrderItemPoList(processOrderItemPoList)
                .processOrderExtraPoList(processOrderExtraPoList)
                .processOrderMaterialRefBoList(procMaterialRefBoList)
                .processOrderProcedurePoList(processOrderProcedurePoList)
                .processOrderDescPoList(processOrderDescPoList)
                .processOrderSamplePoList(processOrderSamplePoList)
                .build();
    }

    /**
     * @Description 通过原料入参dto构建原料与商品1:N对照关系
     * @Return List<ProcessOrderMaterialRefBo> 原料与商品1:N对照关系列表
     * @author yanjiawei
     * @Date 2024/11/10 15:57
     */
    private List<ProcessOrderMaterialRefBo> toProcessOrderMaterialRefBo(String processOrderNo,
                                                                        List<ProcessOrderMaterialDto> processOrderMaterialDtoList) {
        if (CollectionUtils.isEmpty(processOrderMaterialDtoList)) {
            return Collections.emptyList();
        }

        return processOrderMaterialDtoList.stream().map(procMaterialDto -> {
            ProcessOrderMaterialRefBo procMaterialRefBo = new ProcessOrderMaterialRefBo();

            ProcessOrderMaterialPo processOrderMaterialPo = new ProcessOrderMaterialPo();
            processOrderMaterialPo.setProcessOrderNo(processOrderNo);
            processOrderMaterialPo.setSku(procMaterialDto.getSku());
            processOrderMaterialPo.setSkuBatchCode(procMaterialDto.getSkuBatchCode());
            processOrderMaterialPo.setDeliveryNum(procMaterialDto.getDeliveryNum());
            processOrderMaterialPo.setCreateType(CreateType.CREATE);
            procMaterialRefBo.setProcMaterialPo(processOrderMaterialPo);

            List<ProcessOrderMaterialCompareDto> procMaterialCompareDtoList = procMaterialDto.getProcessOrderMaterialCompareDtoList();
            if (CollectionUtils.isNotEmpty(procMaterialCompareDtoList)) {
                List<ProcessOrderMaterialComparePo> procMaterialComparePoList = procMaterialCompareDtoList.stream().map(procMaterialCompare -> {
                    ProcessOrderMaterialComparePo comparePo = new ProcessOrderMaterialComparePo();
                    comparePo.setSku(procMaterialCompare.getSku());
                    comparePo.setQuantity(procMaterialCompare.getQuantity());
                    return comparePo;
                }).collect(Collectors.toList());
                procMaterialRefBo.setProcMaterialComparePoList(procMaterialComparePoList);
            }
            return procMaterialRefBo;
        }).collect(Collectors.toList());
    }

    /**
     * @Description 判断是否为复杂工序加工单
     * @author yanjiawei
     * @Date 2024/9/2 14:03
     */
    public boolean isComplexProcessOrder(List<Long> processIds) {
        if (CollectionUtils.isEmpty(processIds)) {
            log.info("判断是否复杂工序结果=>{} 工序id为空", false);
            return false;
        }

        List<Long> complexProcessIds = scmProcessProp.getComplexProcessIds();
        if (CollectionUtils.isEmpty(complexProcessIds)) {
            log.info("判断是否复杂工序结果=>{} 未配置复杂工序", false);
            return false;
        }

        //判断是否包含
        boolean isComplex = processIds.stream().anyMatch(complexProcessIds::contains);
        log.info("判断是否复杂工序结果=>{}", isComplex);
        return isComplex;
    }

    public ComplexProcConfigVo getComplexProcConfig() {
        ComplexProcConfigVo complexProcConfigVo = new ComplexProcConfigVo();
        complexProcConfigVo.setMaxNum(COMPLEX_PROCESS_NUM_LIMIT);

        List<Long> complexProcessIds = scmProcessProp.getComplexProcessIds();
        if (CollectionUtils.isNotEmpty(complexProcessIds)) {
            complexProcConfigVo.setComplexProcessIds(complexProcessIds);
        }
        return complexProcConfigVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateProcDeliveryWarehouseJob() {
        List<DeliveryWarehouseSkuBo> deliveryWarehouseSku = scmProcessProp.getDeliveryWarehouseSku();
        if (CollectionUtils.isEmpty(deliveryWarehouseSku)) {
            log.info("未配置发货仓库与加工成品SKU关系，不执行发货仓库更新操作");
            return;
        }

        for (DeliveryWarehouseSkuBo deliveryWarehouseSkuBo : deliveryWarehouseSku) {
            String deliveryWarehouseCode = deliveryWarehouseSkuBo.getDeliveryWarehouseCode();
            if (StringUtils.isBlank(deliveryWarehouseCode)) {
                log.info("发货仓库编码为空，不执行发货仓库更新操作");
                continue;
            }
            List<WarehouseVo> warehouseVos
                    = wmsRemoteService.getWarehouseByCode(Collections.singletonList(deliveryWarehouseCode));
            if (CollectionUtils.isEmpty(warehouseVos)) {
                log.info("发货仓库{}不存在，不执行发货仓库更新操作", deliveryWarehouseCode);
                continue;
            }
            WarehouseVo warehouseVo = warehouseVos.stream().findFirst().orElse(null);
            if (Objects.isNull(warehouseVo)) {
                log.info("发货仓库{}不存在，不执行发货仓库更新操作", deliveryWarehouseCode);
                continue;
            }

            List<String> skuList = deliveryWarehouseSkuBo.getSkuList();
            if (CollectionUtils.isEmpty(skuList)) {
                log.info("发货仓库{}未配置加工成品SKU，不执行发货仓库更新操作", deliveryWarehouseCode);
                continue;
            }

            PageExecutor<ProcessOrderPo> processOrderPoPageExecutor = new PageExecutor<>();
            processOrderPoPageExecutor.doForPage((page) -> {
                ProcessOrderType limited = ProcessOrderType.LIMITED;
                ProcessOrderStatus waitReady = ProcessOrderStatus.WAIT_READY;
                List<MissingInformation> missInfoList
                        = Arrays.asList(MissingInformation.NOT_EXIST_MATERIAL, MissingInformation.OUT_OF_STOCK);
                return processOrderDao.getDeliveryWarehouseInitData(page, limited, waitReady, missInfoList, deliveryWarehouseCode, skuList);
            }, records -> {
                for (ProcessOrderPo processOrderPo : records) {
                    processOrderPo.setDeliveryWarehouseCode(deliveryWarehouseCode);
                    processOrderPo.setDeliveryWarehouseName(warehouseVo.getWarehouseName());
                    processOrderDao.updateByIdVersion(processOrderPo);
                    log.info("更新加工单=>{} 发货仓库=>{}", processOrderPo.getProcessOrderNo(), deliveryWarehouseCode);
                }
            });
        }
    }
}















