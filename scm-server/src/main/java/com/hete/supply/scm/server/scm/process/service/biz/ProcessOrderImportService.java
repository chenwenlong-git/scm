package com.hete.supply.scm.server.scm.process.service.biz;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.importation.entity.dto.ProcessOrderCreateImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.ProcessOrderPromiseDateImportationDto;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.SdaRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.entity.bo.OperatorUserBo;
import com.hete.supply.scm.server.scm.enums.LogVersionValueType;
import com.hete.supply.scm.server.scm.process.converter.ProcessOrderClassConverter;
import com.hete.supply.scm.server.scm.process.dao.*;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessOrderProductionInfoBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessOrderSampleBo;
import com.hete.supply.scm.server.scm.process.entity.po.*;
import com.hete.supply.scm.server.scm.process.service.base.ProcessOrderBaseService;
import com.hete.supply.scm.server.scm.sample.service.base.SampleBaseService;
import com.hete.supply.wms.api.basic.entity.vo.WarehouseVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/9/20 19:02
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessOrderImportService {
    private final PlmRemoteService plmRemoteService;
    private final WmsRemoteService wmsRemoteService;
    private final IdGenerateService idGenerateService;
    private final ProcessOrderBaseService processOrderBaseService;
    private final SampleBaseService sampleBaseService;
    private final ProcessDao processDao;
    private final ProcessOrderProcedureDao processOrderProcedureDao;
    private final ProcessOrderSampleDao processOrderSampleDao;
    private final ProcessOrderMaterialDao processOrderMaterialDao;
    private final ProcessOrderDao processOrderDao;
    private final ProcessOrderItemDao processOrderItemDao;
    private final ProcessOrderExtraDao processOrderExtraDao;
    private final ProcessOrderDescDao processOrderDescDao;
    private final SdaRemoteService sdaRemoteService;
    private final ProcessOrderBizService processOrderBizService;

    private final static Integer FIRST_PROCESS_SORT = 0;
    private final static Integer SECOND_PROCESS_SORT = 1;
    private final static Integer THIRD_PROCESS_SORT = 2;
    private final static Integer FORTH_PROCESS_SORT = 3;
    private final static Integer FIFTH_PROCESS_SORT = 4;
    private final static Integer PROCESS_NUM_MIN = 0;
    private final static int COMPLEX_PROCESS_NUM_LIMIT = 5;
    private final static Integer PROCESS_NUM_LIMIT = 10;
    private final static Integer CHAR_LENGTH_LIMIT = 255;


    @Transactional(rollbackFor = Exception.class)
    public void importationProcess(ProcessOrderCreateImportationDto req) {
        log.info("批量创建加工单入参：{}", JSON.toJSONString(req));

        // 备注校验
        if (StringUtils.isNotBlank(req.getProcessOrderNote()) && req.getProcessOrderNote().length() > CHAR_LENGTH_LIMIT) {
            throw new ParamIllegalException("加工单备注不能超过{}个字符! 请核对后重新导入。", CHAR_LENGTH_LIMIT);
        }

        //加工单类型校验
        String processOrderTypeStr = ParamValidUtils.requireNotBlank(req.getProcessOrderType(), "加工单类型不能为空! 请核对后重新导入。");
        ProcessOrderType processOrderType = ProcessOrderType.getByDesc(processOrderTypeStr);
        List<ProcessOrderType> importableProcessOrderTypes = Arrays.asList(
                ProcessOrderType.NORMAL,
                ProcessOrderType.OVERSEAS_REPAIR,
                ProcessOrderType.WH,
                ProcessOrderType.REPAIR
        );
        ParamValidUtils.requireContains(processOrderType, importableProcessOrderTypes,
                StrUtil.format("加工单类型填写错误! 仅允许填写{} 请核对后重新导入。",
                        importableProcessOrderTypes.stream().map(ProcessOrderType::getRemark).collect(Collectors.joining("/")))
        );

        //平台编码校验
        String platformName = ParamValidUtils.requireNotBlank(req.getPlatform(), "平台为空! 请核对后重新导入。");
        String platform = ParamValidUtils.requireNotBlank(sdaRemoteService.getCodeByName(platformName), "平台错误！请核对后重新导入。");

        //加工仓库校验
        String reqWarehouseCode = ParamValidUtils.requireNotBlank(req.getWarehouseCode(), "收货仓库编码不能为空! 请核对后重新导入。");
        List<WarehouseVo> warehouseVoList = wmsRemoteService.getWarehouseByCode(Collections.singletonList(reqWarehouseCode));
        ParamValidUtils.requireNotEmpty(warehouseVoList, StrUtil.format("仓库：{}信息不存在! 请核对后重新导入。", req.getWarehouseCode()));
        WarehouseVo warehouseVo = warehouseVoList.get(0);
        String warehouseCode = warehouseVo.getWarehouseCode();
        Map<ProcessOrderType, Set<String>> selectableWarehouseCodeMap = processOrderBaseService.getSelectableWarehouseCodeMap(Collections.singleton(processOrderType));
        Set<String> selectableWarehouseCodes = selectableWarehouseCodeMap.get(processOrderType);
        if (CollectionUtils.isNotEmpty(selectableWarehouseCodes) && !selectableWarehouseCodes.contains(warehouseCode)) {
            throw new ParamIllegalException("填写的仓库编码：{} 不符合当前加工单类型:{} 的可选范围。请核对后重新导入。", warehouseCode, processOrderType.getDesc());
        }

        //原料发货仓库校验
        String deliveryWarehouseCode = ParamValidUtils.requireNotBlank(req.getDeliveryWarehouseCode(), "原料发货仓库编码不能为空! 请核对后重新导入。");
        List<WarehouseVo> deliveryWarehouseVoList = wmsRemoteService.getWarehouseByCode(Collections.singletonList(deliveryWarehouseCode));
        ParamValidUtils.requireNotEmpty(deliveryWarehouseVoList, StrUtil.format("原料仓库：{}信息不存在! 请核对后重新导入。", req.getDeliveryWarehouseCode()));

        //加工数量校验
        Integer processNum = ParamValidUtils.requireNotNull(req.getProcessNum(), "加工数量不能为空! 请核对后重新导入。");
        List<ProcessPo> processPoList = getAllImportProcessPoList(req);
        List<Long> processIds = processPoList.stream().map(ProcessPo::getProcessId).collect(Collectors.toList());
        boolean complexProcessOrder = processOrderBizService.isComplexProcessOrder(processIds);
        if (complexProcessOrder) {
            if (processNum <= PROCESS_NUM_MIN || processNum > COMPLEX_PROCESS_NUM_LIMIT) {
                throw new ParamIllegalException("工序复杂，加工数量不能小于{}或大于{} 请核对后重新导入。", PROCESS_NUM_MIN, COMPLEX_PROCESS_NUM_LIMIT);
            }
        } else {
            if (processNum <= PROCESS_NUM_MIN || processNum > PROCESS_NUM_LIMIT) {
                throw new ParamIllegalException("非复杂工序，加工数量不能小于{}或大于{} 请核对后重新导入。", PROCESS_NUM_MIN, PROCESS_NUM_LIMIT);
            }
        }

        // 校验交货日期
        String deliverDateStr = ParamValidUtils.requireNotBlank(req.getDeliverDate(), "业务约定交期不能为空! 请核对后重新导入。");
        String normDatetimePattern = DatePattern.NORM_DATETIME_PATTERN;
        if (!ScmTimeUtil.isValidDateFormat(deliverDateStr, normDatetimePattern)) {
            throw new ParamIllegalException("时间格式不正确，请确保您输入的时间符合以下格式：{}！请核对后重新导入。", normDatetimePattern);
        }
        LocalDateTime deliverDate = ScmTimeUtil.getLastSecondTimeOfDayForTime(ScmTimeUtil.dateStrToLocalDateTime(deliverDateStr, normDatetimePattern));
        LocalDate curDate = TimeUtil.convertZone(LocalDateTime.now(), TimeZoneId.UTC, TimeZoneId.CN).toLocalDate();
        if (null != deliverDate && (deliverDate.toLocalDate().isBefore(curDate) || deliverDate.toLocalDate().equals(curDate))) {
            throw new ParamIllegalException("期望上架时间限制不可小于等于今天！请核对后重新导入。");
        }

        // 校验成品sku
        String processSku = ParamValidUtils.requireNotBlank(req.getSku(), "加工单商品sku不能为空! 请核对后重新导入。");
        ParamValidUtils.requireNotEmpty(plmRemoteService.getSkuEncodeBySku(Collections.singletonList(processSku)),
                StrUtil.format("sku：{}信息不存在! 请核对后重新导入。", req.getSku()));

        // 校验原料sku
        ParamValidUtils.requireNotBlank(req.getMaterialSku1(), "原料sku1不能为空! 请核对后重新导入。");
        // 校验单个加工需求数
        ParamValidUtils.requireNotNull(req.getDeliveryNum1(), "单个加工需求数1不能为空! 请核对后重新导入。");
        // 校验工序名称
        ParamValidUtils.requireNotBlank(req.getProcessSecondName1(), "工序名称1不能为空! 请核对后重新导入。");
        // 校验工序类别
        ParamValidUtils.requireNotBlank(req.getProcessLabel1(), "工序类别1不能为空! 请核对后重新导入。");

        final String processOrderNo = idGenerateService.getConfuseCode(ScmConstant.PROCESS_ORDER_NO_PREFIX, TimeType.CN_DAY, ConfuseLength.L_4);

        //加工单原料
        List<ProcessOrderMaterialPo> processOrderMaterialPoList = new ArrayList<>();
        final ProcessOrderMaterialPo processOrderMaterialPo1
                = this.collectProcessMaterial(req.getMaterialSku1(), req.getDeliveryNum1(), req.getProcessNum(), processOrderNo);
        if (null != processOrderMaterialPo1) {
            ParamValidUtils.requireNotEmpty(plmRemoteService.getSkuEncodeBySku(Collections.singletonList(req.getMaterialSku1())),
                    StrUtil.format("原料sku1：{}信息不存在! 请核对后重新导入。", req.getMaterialSku1()));
            processOrderMaterialPoList.add(processOrderMaterialPo1);
        }
        final ProcessOrderMaterialPo processOrderMaterialPo2
                = this.collectProcessMaterial(req.getMaterialSku2(), req.getDeliveryNum2(), req.getProcessNum(), processOrderNo);
        if (null != processOrderMaterialPo2) {
            ParamValidUtils.requireNotEmpty(plmRemoteService.getSkuEncodeBySku(Collections.singletonList(req.getMaterialSku2())),
                    StrUtil.format("原料sku2：{}信息不存在! 请核对后重新导入。", req.getMaterialSku2()));
            processOrderMaterialPoList.add(processOrderMaterialPo2);
        }
        final ProcessOrderMaterialPo processOrderMaterialPo3
                = this.collectProcessMaterial(req.getMaterialSku3(), req.getDeliveryNum3(), req.getProcessNum(), processOrderNo);
        if (null != processOrderMaterialPo3) {
            ParamValidUtils.requireNotEmpty(plmRemoteService.getSkuEncodeBySku(Collections.singletonList(req.getMaterialSku3())),
                    StrUtil.format("原料sku3：{}信息不存在! 请核对后重新导入。", req.getMaterialSku3()));
            processOrderMaterialPoList.add(processOrderMaterialPo3);
        }
        final ProcessOrderMaterialPo processOrderMaterialPo4
                = this.collectProcessMaterial(req.getMaterialSku4(), req.getDeliveryNum4(), req.getProcessNum(), processOrderNo);
        if (null != processOrderMaterialPo4) {
            ParamValidUtils.requireNotEmpty(plmRemoteService.getSkuEncodeBySku(Collections.singletonList(req.getMaterialSku4())),
                    StrUtil.format("原料sku4：{}信息不存在! 请核对后重新导入。", req.getMaterialSku4()));
            processOrderMaterialPoList.add(processOrderMaterialPo4);
        }
        final ProcessOrderMaterialPo processOrderMaterialPo5
                = this.collectProcessMaterial(req.getMaterialSku5(), req.getDeliveryNum5(), req.getProcessNum(), processOrderNo);
        if (null != processOrderMaterialPo5) {
            ParamValidUtils.requireNotEmpty(plmRemoteService.getSkuEncodeBySku(Collections.singletonList(req.getMaterialSku5())),
                    StrUtil.format("原料sku5：{}信息不存在! 请核对后重新导入。", req.getMaterialSku5()));
            processOrderMaterialPoList.add(processOrderMaterialPo5);
        }
        processOrderMaterialDao.insertBatch(processOrderMaterialPoList);

        // 加工描述
        List<ProcessOrderDescPo> processOrderDescPoList = new ArrayList<>();
        ProcessOrderDescPo processOrderDescPo1
                = this.collectProcessDesc(req.getProcessDescName1(), req.getProcessDescValue1(), processOrderNo);
        if (null != processOrderDescPo1) {
            processOrderDescPoList.add(processOrderDescPo1);
        }
        ProcessOrderDescPo processOrderDescPo2
                = this.collectProcessDesc(req.getProcessDescName2(), req.getProcessDescValue2(), processOrderNo);
        if (null != processOrderDescPo2) {
            processOrderDescPoList.add(processOrderDescPo2);
        }
        ProcessOrderDescPo processOrderDescPo3
                = this.collectProcessDesc(req.getProcessDescName3(), req.getProcessDescValue3(), processOrderNo);
        if (null != processOrderDescPo3) {
            processOrderDescPoList.add(processOrderDescPo3);
        }
        ProcessOrderDescPo processOrderDescPo4
                = this.collectProcessDesc(req.getProcessDescName4(), req.getProcessDescValue4(), processOrderNo);
        if (null != processOrderDescPo4) {
            processOrderDescPoList.add(processOrderDescPo4);
        }
        ProcessOrderDescPo processOrderDescPo5
                = this.collectProcessDesc(req.getProcessDescName5(), req.getProcessDescValue5(), processOrderNo);
        if (null != processOrderDescPo5) {
            processOrderDescPoList.add(processOrderDescPo5);
        }
        if (CollectionUtils.isNotEmpty(processOrderDescPoList)) {
            processOrderDescDao.insertBatch(processOrderDescPoList);
        }

        // 加工单工序
        List<ProcessOrderProcedurePo> processOrderProcedurePoList = new ArrayList<>();
        final ProcessOrderProcedurePo processOrderProcedurePo1
                = this.collectProcess(req.getProcessSecondName1(), req.getProcessLabel1(), processOrderNo, FIRST_PROCESS_SORT);
        if (null != processOrderProcedurePo1) {
            processOrderProcedurePoList.add(processOrderProcedurePo1);
        }
        final ProcessOrderProcedurePo processOrderProcedurePo2
                = this.collectProcess(req.getProcessSecondName2(), req.getProcessLabel2(), processOrderNo, SECOND_PROCESS_SORT);
        if (null != processOrderProcedurePo2) {
            processOrderProcedurePoList.add(processOrderProcedurePo2);
        }
        final ProcessOrderProcedurePo processOrderProcedurePo3
                = this.collectProcess(req.getProcessSecondName3(), req.getProcessLabel3(), processOrderNo, THIRD_PROCESS_SORT);
        if (null != processOrderProcedurePo3) {
            processOrderProcedurePoList.add(processOrderProcedurePo3);
        }
        final ProcessOrderProcedurePo processOrderProcedurePo4
                = this.collectProcess(req.getProcessSecondName4(), req.getProcessLabel4(), processOrderNo, FORTH_PROCESS_SORT);
        if (null != processOrderProcedurePo4) {
            processOrderProcedurePoList.add(processOrderProcedurePo4);
        }
        final ProcessOrderProcedurePo processOrderProcedurePo5
                = this.collectProcess(req.getProcessSecondName5(), req.getProcessLabel5(), processOrderNo, FIFTH_PROCESS_SORT);
        if (null != processOrderProcedurePo5) {
            processOrderProcedurePoList.add(processOrderProcedurePo5);
        }
        if (CollectionUtils.isNotEmpty(processOrderProcedurePoList)) {
            processOrderProcedureDao.insertBatch(processOrderProcedurePoList);
        }

        // 获取加工单bom的图片信息
        ProcessOrderProductionInfoBo processOrderGenerateInfoBo = ParamValidUtils.requireNotNull(
                processOrderBaseService.getProcProdInfoBo(processSku, platform),
                StrUtil.format("{}SKU信息未同步，请到基础设置-商品对照关系 同步商品sku 后重新导入！", processSku)
        );
        List<String> imageList = processOrderGenerateInfoBo.getFileCodeList();

        // 创建加工单
        ProcessOrderPo processOrderPo = new ProcessOrderPo();
        if (CollectionUtils.isNotEmpty(imageList)) {
            processOrderPo.setFileCode(String.join(",", imageList));
        }
        processOrderPo.setProcessOrderNo(processOrderNo);
        processOrderPo.setProcessOrderType(processOrderType);
        processOrderPo.setPlatform(platform);
        processOrderPo.setProcessOrderNote(req.getProcessOrderNote());
        processOrderPo.setWarehouseCode(warehouseCode);
        processOrderPo.setWarehouseName(warehouseVo.getWarehouseName());
        processOrderPo.setWarehouseTypes(warehouseVo.getWarehouseTypeName());
        if (CollectionUtils.isNotEmpty(deliveryWarehouseVoList)) {
            final WarehouseVo deliveryWarehouseVo = deliveryWarehouseVoList.get(0);
            processOrderPo.setDeliveryWarehouseCode(deliveryWarehouseVo.getWarehouseCode());
            processOrderPo.setDeliveryWarehouseName(deliveryWarehouseVo.getWarehouseName());
        }
        processOrderPo.setSpu(plmRemoteService.getSpuBySku(req.getSku()));
        processOrderPo.setDeliverDate(ScmTimeUtil.setToEndOfDay(deliverDate));
        processOrderPo.setPromiseDate(processOrderPo.getDeliverDate());
        processOrderPo.setProcessOrderStatus(processOrderBaseService.parseProcessOrderStatusByProcessOrderType(processOrderType));
        processOrderPo.setOverPlan(OverPlan.FALSE);
        processOrderPo.setNeedProcessPlan(NeedProcessPlan.FALSE);
        processOrderPo.setProcessPlanDelay(ProcessPlanDelay.FALSE);
        processOrderPo.setTotalSkuNum(1);
        processOrderPo.setTotalProcessNum(req.getProcessNum());

        // 设置原料归还状态为"无需归还"
        if (CollectionUtils.isNotEmpty(processOrderMaterialPoList)) {
            processOrderPo.setMaterialBackStatus(MaterialBackStatus.UN_BACK);
            processOrderPo.setIsReceiveMaterial(IsReceiveMaterial.FALSE);
        } else {
            processOrderPo.setMaterialBackStatus(MaterialBackStatus.NO_BACK);
            processOrderPo.setIsReceiveMaterial(IsReceiveMaterial.NO_RETURN_REQUIRED);
        }
        processOrderDao.insert(processOrderPo);

        // 加工单明细&创建批次码
        ProcessOrderItemPo processOrderItemPo = new ProcessOrderItemPo();
        processOrderItemPo.setProcessOrderNo(processOrderNo);
        processOrderItemPo.setProcessNum(req.getProcessNum());
        processOrderItemPo.setSku(req.getSku());
        processOrderItemPo.setIsFirst(BooleanType.TRUE);
        processOrderBaseService.generateBatchCodes(Collections.singletonList(processOrderItemPo));
        processOrderItemDao.insert(processOrderItemPo);

        // 加工单额外信息
        ProcessOrderExtraPo processOrderExtraPo = new ProcessOrderExtraPo();
        processOrderExtraPo.setProcessOrderNo(processOrderNo);
        processOrderExtraDao.insert(processOrderExtraPo);

        //加工单bom生产信息&日志
        List<ProcessOrderSamplePo> processOrderSamplePoList = new ArrayList<>();
        List<ProcessOrderSampleBo> processOrderProductAttrList = processOrderGenerateInfoBo.getProcessOrderSampleBoList();
        if (CollectionUtils.isNotEmpty(processOrderProductAttrList)) {
            processOrderSamplePoList.addAll(processOrderProductAttrList.stream()
                    .map(processOrderSampleBo -> {
                        ProcessOrderSamplePo processOrderSamplePo = new ProcessOrderSamplePo();
                        processOrderSamplePo.setProcessOrderNo(processOrderNo);
                        processOrderSamplePo.setSampleChildOrderNo(processOrderSampleBo.getSampleChildOrderNo());
                        processOrderSamplePo.setSourceDocumentNumber(processOrderSampleBo.getSourceDocumentNumber());
                        processOrderSamplePo.setSampleInfoKey(processOrderSampleBo.getSampleInfoKey());
                        processOrderSamplePo.setSampleInfoValue(processOrderSampleBo.getSampleInfoValue());
                        return processOrderSamplePo;
                    }).collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(processOrderSamplePoList)) {
            processOrderSampleDao.insertBatch(processOrderSamplePoList);
        }
        List<LogVersionBo> logVersionBos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(processOrderProductAttrList)) {
            logVersionBos = processOrderProductAttrList.stream().map(item -> {
                LogVersionBo logVersionBo = new LogVersionBo();
                logVersionBo.setKey(item.getSampleInfoKey());
                logVersionBo.setValueType(LogVersionValueType.STRING);
                logVersionBo.setValue(item.getSampleInfoValue());
                return logVersionBo;
            }).collect(Collectors.toList());
        }
        processOrderBaseService.createOrderChangeLog(processOrderPo.getProcessOrderId(), logVersionBos);

        // 生成日志
        processOrderBaseService.createStatusChangeLog(processOrderPo, processOrderExtraPo, new OperatorUserBo());
        // 刷新缺失信息
        processOrderBaseService.checkMissingInfo(processOrderPo.getProcessOrderNo());
    }

    private List<ProcessPo> getAllImportProcessPoList(ProcessOrderCreateImportationDto req) {
        List<ProcessPo> res = Lists.newArrayList();

        String processSecondName1 = req.getProcessSecondName1();
        String processLabelStr1 = req.getProcessLabel1();
        log.info("processSecondName1=>{},processLabelStr1=>{}", processSecondName1, processLabelStr1);
        if (StringUtils.isNotBlank(processSecondName1) && StringUtils.isNotBlank(processLabelStr1)) {
            ProcessLabel processLabel = ProcessLabel.getByDesc(processLabelStr1);
            log.info("processLabelStr1=>{} 查询结果=>{}", processLabelStr1, JSON.toJSONString(processLabel));
            if (Objects.nonNull(processLabel)) {
                ProcessPo processPo = processDao.getOneByProcessSecondNameAndLabel(processSecondName1, processLabel);
                if (Objects.nonNull(processPo)) {
                    res.add(processPo);
                }
            }
        }

        String processSecondName2 = req.getProcessSecondName2();
        String processLabelStr2 = req.getProcessLabel2();
        log.info("processSecondName2=>{},processLabelStr2=>{}", processSecondName2, processLabelStr2);
        if (StringUtils.isNotBlank(processSecondName2) && StringUtils.isNotBlank(processLabelStr2)) {
            ProcessLabel processLabel = ProcessLabel.getByDesc(processLabelStr2);
            log.info("processLabelStr2=>{} 查询结果=>{}", processLabelStr2, JSON.toJSONString(processLabel));
            if (Objects.nonNull(processLabel)) {
                ProcessPo processPo = processDao.getOneByProcessSecondNameAndLabel(processSecondName2, processLabel);
                if (Objects.nonNull(processPo)) {
                    res.add(processPo);
                }
            }
        }
        String processSecondName3 = req.getProcessSecondName3();
        String processLabelStr3 = req.getProcessLabel3();
        log.info("processSecondName3=>{},processLabelStr3=>{}", processSecondName3, processLabelStr3);
        if (StringUtils.isNotBlank(processSecondName3) && StringUtils.isNotBlank(processLabelStr3)) {
            ProcessLabel processLabel = ProcessLabel.getByDesc(processLabelStr3);
            log.info("processLabelStr3=>{} 查询结果=>{}", processLabelStr3, JSON.toJSONString(processLabel));
            if (Objects.nonNull(processLabel)) {
                ProcessPo processPo = processDao.getOneByProcessSecondNameAndLabel(processSecondName3, processLabel);
                if (Objects.nonNull(processPo)) {
                    res.add(processPo);
                }
            }
        }
        String processSecondName4 = req.getProcessSecondName4();
        String processLabelStr4 = req.getProcessLabel4();
        log.info("processSecondName4=>{},processLabelStr4=>{}", processSecondName4, processLabelStr4);
        if (StringUtils.isNotBlank(processSecondName4) && StringUtils.isNotBlank(processLabelStr4)) {
            ProcessLabel processLabel = ProcessLabel.getByDesc(processLabelStr4);
            log.info("processLabelStr4=>{} 查询结果=>{}", processLabelStr4, JSON.toJSONString(processLabel));
            if (Objects.nonNull(processLabel)) {
                ProcessPo processPo = processDao.getOneByProcessSecondNameAndLabel(processSecondName4, processLabel);
                if (Objects.nonNull(processPo)) {
                    res.add(processPo);
                }
            }
        }
        String processSecondName5 = req.getProcessSecondName5();
        String processLabelStr5 = req.getProcessLabel5();
        log.info("processSecondName5=>{},processLabelStr5=>{}", processSecondName5, processLabelStr5);
        if (StringUtils.isNotBlank(processSecondName5) && StringUtils.isNotBlank(processLabelStr5)) {
            ProcessLabel processLabel = ProcessLabel.getByDesc(processLabelStr5);
            log.info("processLabelStr5=>{} 查询结果=>{}", processLabelStr5, JSON.toJSONString(processLabel));
            if (Objects.nonNull(processLabel)) {
                ProcessPo processPo = processDao.getOneByProcessSecondNameAndLabel(processSecondName5, processLabel);
                if (Objects.nonNull(processPo)) {
                    res.add(processPo);
                }
            }
        }
        return res;
    }

    private ProcessOrderDescPo collectProcessDesc(String processDescName, String processDescValue, String processOrderNo) {
        if (StringUtils.isBlank(processDescName) || StringUtils.isBlank(processDescValue)) {
            return null;
        }
        final ProcessOrderDescPo processOrderDescPo = new ProcessOrderDescPo();
        processOrderDescPo.setProcessOrderNo(processOrderNo);
        processOrderDescPo.setProcessDescName(processDescName);
        processOrderDescPo.setProcessDescValue(processDescValue);

        return processOrderDescPo;
    }

    private ProcessOrderMaterialPo collectProcessMaterial(String materialSku, Integer deliveryNum,
                                                          Integer processNum, String processOrderNo) {
        if (StringUtils.isBlank(materialSku) || null == deliveryNum || null == processNum) {
            return null;
        }
        ProcessOrderMaterialPo processOrderMaterialPo = new ProcessOrderMaterialPo();
        processOrderMaterialPo.setSku(materialSku.trim());
        processOrderMaterialPo.setDeliveryNum(deliveryNum * processNum);
        processOrderMaterialPo.setCreateType(CreateType.CREATE);
        processOrderMaterialPo.setProcessOrderNo(processOrderNo);

        return processOrderMaterialPo;
    }

    private ProcessOrderProcedurePo collectProcess(String processSecondName, String processLabelStr,
                                                   String processOrderNo, Integer sort) {
        if (StringUtils.isBlank(processSecondName) || StringUtils.isBlank(processLabelStr)) {
            return null;
        }
        final ProcessLabel processLabel = ProcessLabel.getByDesc(processLabelStr);
        if (null == processLabel) {
            throw new ParamIllegalException("工序类别{}不正确！请确保您输入的工序类别{}是有效值。", processLabelStr, sort);
        }
        final ProcessPo processPo = processDao.getOneByProcessSecondNameAndLabel(processSecondName, processLabel);

        return ProcessOrderClassConverter.convertProcessPoToProcedurePo(processOrderNo, processPo, sort);

    }

    @Transactional(rollbackFor = Exception.class)
    public void importPromiseDate(ProcessOrderPromiseDateImportationDto dto) {
        // 加工单校验
        String processOrderNo = ParamValidUtils.requireNotBlank(dto.getProcessOrderNo(),
                "加工单答交时间的导入失败！请确保填写了加工单号。");
        ParamValidUtils.requireEquals(true, processOrderNo.startsWith("JG"),
                "加工单答交时间的导入失败！请确保填写的加工单号是JG开头。");
        ProcessOrderPo processOrderPo
                = ParamValidUtils.requireNotNull(processOrderDao.getByProcessOrderNo(processOrderNo),
                "加工单答交时间的导入失败！请确保填写的加工单信息存在。");

        List<ProcessOrderStatus> canNotImportStatus
                = Arrays.asList(ProcessOrderStatus.CHECKING, ProcessOrderStatus.WAIT_RECEIPT,
                ProcessOrderStatus.WAIT_STORE, ProcessOrderStatus.STORED, ProcessOrderStatus.DELETED);
        ProcessOrderStatus processOrderStatus = processOrderPo.getProcessOrderStatus();
        ParamValidUtils.requireNotContains(processOrderStatus,
                canNotImportStatus,
                StrUtil.format("加工单答交时间的导入失败！请确保加工单状态不处于{}状态",
                        canNotImportStatus.stream()
                                .map(ProcessOrderStatus::getRemark)
                                .map(String::valueOf)
                                .collect(Collectors.joining("/"))));

        // 答交时间校验
        String promiseDateStr
                = ParamValidUtils.requireNotBlank(dto.getPromiseDate(),
                "加工单答交时间的导入失败！请确保填写了答交时间。");

        LocalDateTime promiseDate;
        String pattern = DatePattern.NORM_DATETIME_PATTERN;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        try {
            promiseDate = LocalDateTime.parse(promiseDateStr, dateTimeFormatter);
        } catch (Exception e) {
            log.warn("importPromiseDate error ", e);
            throw new ParamIllegalException("加工单答交时间的导入失败! 请确保答交时间格式：{}", "yyyy/MM/dd");
        }

        // 更新加工单答交时间
        processOrderPo.setPromiseDate(ScmTimeUtil.getLastSecondTimeOfDayForTime(promiseDate));
        processOrderDao.updateByIdVersion(processOrderPo);
    }
}
