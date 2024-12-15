package com.hete.supply.scm.server.scm.process.service.base;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hete.supply.plm.api.goods.entity.dto.PlmPlatSkuDto;
import com.hete.supply.plm.api.goods.entity.dto.PlmPlatSkuListDto;
import com.hete.supply.plm.api.goods.entity.vo.PlmPlatSkuVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmSkuImage;
import com.hete.supply.plm.api.goods.entity.vo.PlmSkuVo;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.converter.SkuMaterialConverter;
import com.hete.supply.scm.server.scm.converter.SkuProcedureConverter;
import com.hete.supply.scm.server.scm.entity.bo.*;
import com.hete.supply.scm.server.scm.entity.dto.BizLogCreateMqDto;
import com.hete.supply.scm.server.scm.entity.dto.GetSkuMaterialDto;
import com.hete.supply.scm.server.scm.entity.dto.GetSkuProcedureDto;
import com.hete.supply.scm.server.scm.entity.dto.ProduceDataBySkuListDto;
import com.hete.supply.scm.server.scm.entity.po.GoodsProcessPo;
import com.hete.supply.scm.server.scm.entity.po.GoodsProcessRelationPo;
import com.hete.supply.scm.server.scm.entity.vo.SkuMaterialVo;
import com.hete.supply.scm.server.scm.entity.vo.SkuProcedureVo;
import com.hete.supply.scm.server.scm.enums.DefaultDatabaseTime;
import com.hete.supply.scm.server.scm.enums.LogVersionValueType;
import com.hete.supply.scm.server.scm.handler.LogVersionHandler;
import com.hete.supply.scm.server.scm.process.builder.ProcessOrderBuilder;
import com.hete.supply.scm.server.scm.process.config.ScmProcessProp;
import com.hete.supply.scm.server.scm.process.converter.MissInformationConverter;
import com.hete.supply.scm.server.scm.process.converter.ProcessOrderProductionConverter;
import com.hete.supply.scm.server.scm.process.dao.*;
import com.hete.supply.scm.server.scm.process.entity.bo.*;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderCreateDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessWaveCreateResultMqDto;
import com.hete.supply.scm.server.scm.process.entity.po.*;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderProcedureByH5Vo;
import com.hete.supply.scm.server.scm.process.enums.*;
import com.hete.supply.scm.server.scm.process.handler.ProcessOrderHandler;
import com.hete.supply.scm.server.scm.process.handler.WmsWaveCreateResultHandler;
import com.hete.supply.scm.server.scm.sample.service.base.SampleBaseService;
import com.hete.supply.scm.server.scm.sample.service.ref.SampleRefService;
import com.hete.supply.scm.server.scm.service.base.ProduceDataBaseService;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.basic.entity.dto.SkuBatchCreateDto;
import com.hete.supply.wms.api.basic.entity.vo.WarehouseVo;
import com.hete.supply.wms.api.interna.entity.dto.BatchCodeInventoryQueryDto;
import com.hete.supply.wms.api.interna.entity.vo.BatchCodeInventoryVo;
import com.hete.supply.wms.api.leave.entity.vo.DeliveryOrderCreateVo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.consistency.core.service.ConsistencyService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.redis.lock.annotation.RedisLock;
import com.hete.trace.util.TraceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: RockyHuas
 * @date: 2022/11/17 15:54
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class ProcessOrderBaseService {
    private final ProcessOrderDao processOrderDao;
    private final ProcessOrderItemDao processOrderItemDao;
    private final ProcessOrderMaterialDao processOrderMaterialDao;
    private final ProcessMaterialReceiptDao processMaterialReceiptDao;
    private final ProcessMaterialReceiptItemDao processMaterialReceiptItemDao;
    private final ProcessOrderExtraDao processOrderExtraDao;
    private final ProcessOrderScanDao processOrderScanDao;
    private final ProcessOrderProcedureDao processOrderProcedureDao;
    private final PlmRemoteService plmRemoteService;
    private final SampleRefService sampleRefService;
    private final WmsRemoteService wmsRemoteService;
    private final IdGenerateService idGenerateService;
    private final ConsistencySendMqService consistencySendMqService;
    private final ProcessDao processDao;
    private final ProcessDefectiveRecordDao processDefectiveRecordDao;
    private final ProcessMaterialDetailDao processMaterialDetailDao;
    private final ProcessMaterialDetailItemDao processMaterialDetailItemDao;
    private final ConsistencyService consistencyService;
    private final ProcessTemplateDao processTemplateDao;
    private final ProcessTemplateMaterialDao processTemplateMaterialDao;
    private final ProcessTemplateRelationDao processTemplateRelationDao;
    private final GoodsProcessDao goodsProcessDao;
    private final GoodsProcessRelationDao goodsProcessRelationDao;
    private final SampleBaseService sampleBaseService;
    private final ProcessMaterialBackDao processMaterialBackDao;
    private final ProcessMaterialBackItemDao processMaterialBackItemDao;
    private final ProduceDataBaseService produceDataBaseService;
    private final Environment environment;
    private final ScmProcessProp scmProcessProp;
    private final SupplierDao supplierDao;
    private final ProcessOrderMaterialCompareDao prodMaterialCompareDao;

    /**
     * 次品数判断标准
     */
    private final static Integer DEFECTIVE_GOODS_CNT = 1;
    private final static Integer SINGLE_PROCESS_NUM = 1;

    @Transactional(rollbackFor = Exception.class)
    public Boolean changeStatus(ProcessOrderPo queriedProcessOrderPo, ProcessOrderStatus newStatus, OperatorUserBo operatorUserBo) {
        ProcessOrderStatus oldStatus = queriedProcessOrderPo.getProcessOrderStatus();
        if (oldStatus.getSort() > ProcessOrderStatus.WAIT_DELIVERY.getSort() && oldStatus.getSort() > newStatus.getSort()) {
            throw new ParamIllegalException("待发货状态之后禁止回退");
        }
        if (oldStatus.equals(newStatus)) {
            return true;
        }
        ProcessOrderExtraPo processOrderExtraPo = processOrderExtraDao.getByProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
        if (processOrderExtraPo == null) {
            processOrderExtraPo = new ProcessOrderExtraPo();
            processOrderExtraPo.setProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
            processOrderExtraDao.insert(processOrderExtraPo);
        }

        if (ProcessOrderStatus.PRODUCED.equals(newStatus)) {
            processOrderExtraPo.setProducedUser(GlobalContext.getUserKey());
            processOrderExtraPo.setProducedUsername(GlobalContext.getUsername());
            queriedProcessOrderPo.setProducedTime(new DateTime().toLocalDateTime());
        }

        if (ProcessOrderStatus.PROCESSING.equals(newStatus)) {
            processOrderExtraPo.setProcessingUser(GlobalContext.getUserKey());
            processOrderExtraPo.setProcessingUsername(GlobalContext.getUsername());
            processOrderExtraPo.setProcessingTime(new DateTime().toLocalDateTime());
        }

        if (ProcessOrderStatus.WAIT_MOVING.equals(newStatus)) {
            processOrderExtraPo.setCompleteScanUser(GlobalContext.getUserKey());
            processOrderExtraPo.setCompleteScanUsername(GlobalContext.getUsername());
            processOrderExtraPo.setCompleteScanTime(new DateTime().toLocalDateTime());
        }

        if (ProcessOrderStatus.CHECKING.equals(newStatus)) {
            throw new BizException("不能更新为后整质检中");
        }

        if (ProcessOrderStatus.WAIT_RECEIPT.equals(newStatus)) {
            queriedProcessOrderPo.setReceiptTime(new DateTime().toLocalDateTime());
            processOrderExtraPo.setReceiptUser(GlobalContext.getUserKey());
            processOrderExtraPo.setReceiptUsername(GlobalContext.getUsername());

        }
        queriedProcessOrderPo.setProcessOrderStatus(newStatus);

        processOrderDao.updateByIdVersion(queriedProcessOrderPo);
        processOrderExtraDao.updateByIdVersion(processOrderExtraPo);


        // 写入状态变更日志
        this.createStatusChangeLog(queriedProcessOrderPo, processOrderExtraPo, operatorUserBo);

        // 完成后处理的加工单，并且为补单类型的，直接变成已入库
        if (ProcessOrderStatus.WAIT_MOVING.equals(newStatus) && ProcessOrderType.EXTRA.equals(
                queriedProcessOrderPo.getProcessOrderType())) {
            this.changeStatus(queriedProcessOrderPo, ProcessOrderStatus.STORED, operatorUserBo);
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public void changeStatusToWaitProcedure(ProcessOrderPo queriedProcessOrderPo,
                                            List<ProcessOrderItemPo> processOrderItemPoList,
                                            List<ProcessOrderMaterialPo> processOrderMaterialPoList,
                                            ProcessMaterialDetailPo processMaterialDetailPo,
                                            OperatorUserBo operatorUserBo) {

        ProcessOrderStatus oldStatus = queriedProcessOrderPo.getProcessOrderStatus();
        ProcessOrderStatus newStatus = ProcessOrderStatus.WAIT_PRODUCE;

        if (oldStatus.equals(newStatus)) {
            return;
        }

        ProcessOrderExtraPo processOrderExtraPo = processOrderExtraDao.getByProcessOrderNo(
                queriedProcessOrderPo.getProcessOrderNo());
        if (processOrderExtraPo == null) {
            processOrderExtraPo = new ProcessOrderExtraPo();
            processOrderExtraPo.setProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
            processOrderExtraDao.insert(processOrderExtraPo);
        }

        queriedProcessOrderPo.setProcessOrderStatus(newStatus);
        processOrderDao.updateByIdVersion(queriedProcessOrderPo);

        // 写入状态变更日志
        this.createStatusChangeLog(queriedProcessOrderPo, processOrderExtraPo, operatorUserBo);

        // 如果没有原料，则直接进入已投产
        List<ProcessOrderMaterialPo> processOrderMaterialPos = processOrderMaterialDao.getByProcessOrderNo(
                queriedProcessOrderPo.getProcessOrderNo());
        if (CollectionUtils.isEmpty(processOrderMaterialPos)) {
            processOrderExtraPo.setProducedUser(GlobalContext.getUserKey());
            processOrderExtraPo.setProducedUsername(GlobalContext.getUsername());
            queriedProcessOrderPo.setProducedTime(new DateTime().toLocalDateTime());

            queriedProcessOrderPo.setProcessOrderStatus(ProcessOrderStatus.PRODUCED);
            processOrderDao.updateByIdVersion(queriedProcessOrderPo);
            processOrderExtraDao.updateByIdVersion(processOrderExtraPo);
            // 写入状态变更日志
            this.createStatusChangeLog(queriedProcessOrderPo, processOrderExtraPo, operatorUserBo);
        }

        // 如果成品信息有更新
        if (CollectionUtils.isNotEmpty(processOrderItemPoList)) {
            processOrderItemDao.updateBatchByIdVersion(processOrderItemPoList);
        }

        // 如果原料信息有更新
        if (CollectionUtils.isNotEmpty(processOrderMaterialPoList)) {
            processOrderMaterialDao.updateBatchByIdVersion(processOrderMaterialPoList);
        }

        // 如果需要插入插入原料明细信息
        if (null != processMaterialDetailPo) {
            processMaterialDetailDao.insert(processMaterialDetailPo);
        }

    }

    public List<ProcessOrderItemPo> batchCreatePoiBatchCode(String processOrderNo,
                                                            List<ProcessOrderItemPo> processOrderItemPos) {
        SkuBatchCreateDto createBachCodeParam = new SkuBatchCreateDto();
        createBachCodeParam.setPurchaseChildOrderNo(processOrderNo);

        List<String> skuList = processOrderItemPos.stream()
                .filter(item -> StringUtils.isBlank(item.getSkuBatchCode()))
                .map(ProcessOrderItemPo::getSku)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(skuList)) {
            log.info("加工单创建批次码失败！sku列表为空。");
            return new ArrayList<>();
        }
        createBachCodeParam.setSkuCodeList(skuList);

        Map<String, String> skuBatchCodeMap = wmsRemoteService.batchCreateBatchCode(createBachCodeParam);
        return processOrderItemPos.stream()
                .peek(item -> {
                    String skuBatchCode = skuBatchCodeMap.get(item.getSku());
                    if (StringUtils.isNotBlank(skuBatchCode)) {
                        item.setSkuBatchCode(skuBatchCode);
                    }
                }).collect(Collectors.toList());
    }

    /**
     * @Description 创建加工明细批次码
     * @author yanjiawei
     * @Date 2024/8/27 16:20
     */
    public List<ProcessOrderItemPo> generateBatchCodes(List<ProcessOrderItemPo> processOrderItemPoList) {
        String createBatchCodeSupplierCode = scmProcessProp.getCreateBatchCodeSupplierCode();
        ParamValidUtils.requireNotEmpty(processOrderItemPoList, "创建批次码失败！加工单明细信息缺失。");

        //根据加工单号分组
        Map<String, List<ProcessOrderItemPo>> processOrderItemPoMap = processOrderItemPoList.stream()
                .collect(Collectors.groupingBy(ProcessOrderItemPo::getProcessOrderNo));
        for (Map.Entry<String, List<ProcessOrderItemPo>> entry : processOrderItemPoMap.entrySet()) {
            String processOrderNo = ParamValidUtils.requireNotBlank(entry.getKey(), "创建批次码失败！加工单号为空");
            List<ProcessOrderItemPo> poiList
                    = ParamValidUtils.requireNotEmpty(entry.getValue(), StrUtil.format("创建批次码失败！加工单{}明细信息缺失。", processOrderNo));
            List<String> skuCodeList = poiList.stream().map(ProcessOrderItemPo::getSku).collect(Collectors.toList());
            ParamValidUtils.requireNotEmpty(skuCodeList, StrUtil.format("创建批次码失败！加工单{} sku列表为空。", processOrderNo));

            //创建批次码
            SkuBatchCreateDto createBachCodeParam = new SkuBatchCreateDto();
            createBachCodeParam.setPurchaseChildOrderNo(processOrderNo);
            createBachCodeParam.setSkuCodeList(skuCodeList);
            if (StrUtil.isNotBlank(createBatchCodeSupplierCode)) {
                createBachCodeParam.setSupplierCode(createBatchCodeSupplierCode);
                SupplierPo supplierPo = supplierDao.getBySupplierCode(createBatchCodeSupplierCode);
                if (Objects.nonNull(supplierPo)) {
                    createBachCodeParam.setSupplierName(supplierPo.getSupplierName());
                }
            }
            Map<String, String> batchCodeResMap = wmsRemoteService.batchCreateBatchCode(createBachCodeParam);
            poiList.forEach(item -> item.setSkuBatchCode(batchCodeResMap.getOrDefault(item.getSku(), "")));
        }
        log.info("创建批次码结果！processOrderItemPoList:{}", JSON.toJSONString(processOrderItemPoList));
        return processOrderItemPoList;
    }

    /**
     * 状态变更日志
     *
     * @param processOrderPo
     * @param processOrderExtraPo
     */
    @Transactional(rollbackFor = Exception.class)
    public void createStatusChangeLog(ProcessOrderPo processOrderPo,
                                      ProcessOrderExtraPo processOrderExtraPo,
                                      OperatorUserBo operatorUserBo) {
        BizLogCreateMqDto bizLogCreateMqDto = new BizLogCreateMqDto();
        bizLogCreateMqDto.setBizLogCode(idGenerateService.getSnowflakeCode(LogBizModule.PROSTATUS.name()));
        bizLogCreateMqDto.setBizSystemCode(ScmConstant.SYSTEM_CODE);
        bizLogCreateMqDto.setLogVersion(ScmConstant.PROCESS_ORDER_LOG_VERSION);
        bizLogCreateMqDto.setBizModule(LogBizModule.PROSTATUS.name());
        bizLogCreateMqDto.setBizCode(processOrderPo.getProcessOrderNo());
        bizLogCreateMqDto.setOperateTime(DateUtil.current());

        String operatorUser = GlobalContext.getUserKey();
        String operatorUsername = GlobalContext.getUsername();
        if (StringUtils.isNotBlank(operatorUserBo.getOperator())) {
            operatorUser = operatorUserBo.getOperator();
        }
        if (StringUtils.isNotBlank(operatorUserBo.getOperatorUsername())) {
            operatorUsername = operatorUserBo.getOperatorUsername();
        }

        if (StringUtils.isBlank(operatorUser)) {
            operatorUser = ScmConstant.SYSTEM_USER;
        }
        if (StringUtils.isBlank(operatorUsername)) {
            operatorUsername = ScmConstant.SYSTEM_USER;
        }

        bizLogCreateMqDto.setOperateUser(operatorUser);
        bizLogCreateMqDto.setOperateUsername(operatorUsername);

        ArrayList<LogVersionBo> logVersionBos = new ArrayList<>();
        bizLogCreateMqDto.setContent(processOrderPo.getProcessOrderStatus()
                .getDesc());

        // 待收货
        if (ProcessOrderStatus.WAIT_RECEIPT.equals(processOrderPo.getProcessOrderStatus())) {
            LogVersionBo logVersionBo = new LogVersionBo();
            logVersionBo.setKey("收货单号");
            logVersionBo.setValueType(LogVersionValueType.STRING);
            logVersionBo.setValue(processOrderExtraPo.getReceiptOrderNo());
            logVersionBos.add(logVersionBo);
        }
        // 待入库
        if (ProcessOrderStatus.WAIT_STORE.equals(processOrderPo.getProcessOrderStatus())) {
            LogVersionBo logVersionBo = new LogVersionBo();
            logVersionBo.setKey("入库单号");
            logVersionBo.setValueType(LogVersionValueType.STRING);
            logVersionBo.setValue(processOrderExtraPo.getStoreOrderNo());
            logVersionBos.add(logVersionBo);
        }
        // 已入库
        if (ProcessOrderStatus.STORED.equals(processOrderPo.getProcessOrderStatus())) {
            LogVersionBo logVersionBo = new LogVersionBo();
            logVersionBo.setKey("入库单号");
            logVersionBo.setValueType(LogVersionValueType.STRING);
            logVersionBo.setValue(processOrderExtraPo.getStoreOrderNo());
            logVersionBos.add(logVersionBo);
        }
        bizLogCreateMqDto.setDetail(logVersionBos);

        consistencySendMqService.execSendMq(LogVersionHandler.class, bizLogCreateMqDto);
    }


    /**
     * 检查缺失信息
     *
     * @param processOrderNo
     */
    @Transactional(rollbackFor = Exception.class)
    public void checkMissingInfo(String processOrderNo) {
        ProcessOrderBo processOrderBo = new ProcessOrderBo();
        processOrderBo.setProcessOrderNo(processOrderNo);
        processOrderBo.setOperator(GlobalContext.getUserKey());
        processOrderBo.setOperatorUsername(GlobalContext.getUsername());
        processOrderBo.setTraceId(TraceUtil.getUberTraceId());
        consistencyService.execAsyncTask(ProcessOrderHandler.class, processOrderBo);
    }

    /**
     * 新增单据变更日志
     *
     * @param processOrderId
     * @param productInfo
     */
    @Transactional(rollbackFor = Exception.class)
    public void createOrderChangeLog(Long processOrderId,
                                     List<LogVersionBo> productInfo) {

        String operatorUser = GlobalContext.getUserKey();
        String operatorUsername = GlobalContext.getUsername();
        if (StringUtils.isBlank(operatorUser)) {
            operatorUser = ScmConstant.SYSTEM_USER;
        }
        if (StringUtils.isBlank(operatorUsername)) {
            operatorUsername = ScmConstant.SYSTEM_USER;
        }

        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderId(processOrderId);
        BizLogCreateMqDto bizLogCreateMqDto = new BizLogCreateMqDto();
        bizLogCreateMqDto.setBizLogCode(idGenerateService.getSnowflakeCode(LogBizModule.PRO.name()));
        bizLogCreateMqDto.setBizSystemCode(ScmConstant.SYSTEM_CODE);
        bizLogCreateMqDto.setLogVersion(ScmConstant.PROCESS_ORDER_LOG_VERSION);
        bizLogCreateMqDto.setBizModule(LogBizModule.PRO.name());
        bizLogCreateMqDto.setBizCode(processOrderPo.getProcessOrderNo());
        bizLogCreateMqDto.setOperateTime(DateUtil.current());
        bizLogCreateMqDto.setOperateUser(operatorUser);
        bizLogCreateMqDto.setOperateUsername(operatorUsername);

        ArrayList<LogVersionBo> logVersionBos = new ArrayList<>();
        // 修改人
        if (StringUtils.isNotBlank(GlobalContext.getUsername())) {
            LogVersionBo updateNameBo = new LogVersionBo();
            updateNameBo.setKey(ProcessOrderVersion.UPDATE_NAME.getDesc());
            updateNameBo.setValueType(ProcessOrderVersion.UPDATE_NAME.getLogVersionValueType());
            updateNameBo.setValue(GlobalContext.getUsername());
            logVersionBos.add(updateNameBo);
        }

        // 修改时间
        LogVersionBo updateTimeBo = new LogVersionBo();
        updateTimeBo.setKey(ProcessOrderVersion.UPDATE_TIME.getDesc());
        updateTimeBo.setValueType(ProcessOrderVersion.UPDATE_TIME.getLogVersionValueType());
        updateTimeBo.setValue(new DateTime().toLocalDateTime());
        logVersionBos.add(updateTimeBo);

        // 赫特SPU
        LogVersionBo spuBo = new LogVersionBo();
        spuBo.setKey(ProcessOrderVersion.SPU.getDesc());
        spuBo.setValueType(ProcessOrderVersion.SPU.getLogVersionValueType());
        spuBo.setValue(processOrderPo.getSpu());
        logVersionBos.add(spuBo);

        // 仓库名称
        LogVersionBo warehouseBo = new LogVersionBo();
        warehouseBo.setKey(ProcessOrderVersion.WAREHOUSE_NAME.getDesc());
        warehouseBo.setValueType(ProcessOrderVersion.WAREHOUSE_NAME.getLogVersionValueType());
        warehouseBo.setValue(processOrderPo.getWarehouseName());
        logVersionBos.add(warehouseBo);

        // 业务约定日期
        LogVersionBo deliverDateBo = new LogVersionBo();
        deliverDateBo.setKey(ProcessOrderVersion.DELIVER_DATE.getDesc());
        deliverDateBo.setValueType(ProcessOrderVersion.DELIVER_DATE.getLogVersionValueType());
        deliverDateBo.setValue(processOrderPo.getDeliverDate());
        logVersionBos.add(deliverDateBo);

        // 平台
        LogVersionBo platformBo = new LogVersionBo();
        platformBo.setKey(ProcessOrderVersion.PLAT_FORM.getDesc());
        platformBo.setValueType(ProcessOrderVersion.PLAT_FORM.getLogVersionValueType());
        platformBo.setValue(processOrderPo.getPlatform());
        logVersionBos.add(platformBo);

        // 加工单备注
        if (StringUtils.isNotBlank(processOrderPo.getProcessOrderNote())) {
            LogVersionBo processOrderNoteBo = new LogVersionBo();
            processOrderNoteBo.setKey(ProcessOrderVersion.ORDER_NOTE.getDesc());
            processOrderNoteBo.setValueType(ProcessOrderVersion.ORDER_NOTE.getLogVersionValueType());
            processOrderNoteBo.setValue(processOrderPo.getProcessOrderNote());
            logVersionBos.add(processOrderNoteBo);
        }

        // 生产信息
        if (CollectionUtils.isNotEmpty(productInfo)) {
            LogVersionBo productBo = new LogVersionBo();
            productBo.setKey(ProcessOrderVersion.PRODUCT_INFO.getDesc());
            productBo.setValueType(ProcessOrderVersion.PRODUCT_INFO.getLogVersionValueType());
            productBo.setValue(productInfo);
            logVersionBos.add(productBo);
        }

        bizLogCreateMqDto.setDetail(logVersionBos);

        consistencySendMqService.execSendMq(LogVersionHandler.class, bizLogCreateMqDto);
    }


    /**
     * 组装查询条件
     *
     * @param skus
     * @param categoryIdList
     * @param skuAttributes
     * @return
     */
    public ProcessOrderQueryBo getProcessOrderQueryCondition(List<String> skus,
                                                             List<Long> categoryIdList,
                                                             List<String> skuAttributes,
                                                             List<String> skuEncodeList,
                                                             List<String> materialSkuList,
                                                             DefectiveRecordStatus defectiveRecordStatus) {
        ProcessOrderQueryBo processOrderQueryBo = new ProcessOrderQueryBo();

        List<String> processOrderNosBySku = this.getProcessOrderNosBySkus(skus);
        processOrderQueryBo.setProcessOrderNosBySku(processOrderNosBySku);
        List<String> processOrderNosByCategoryId = this.getProcessOrderNosByCategoryId(categoryIdList);
        processOrderQueryBo.setProcessOrderNosByCategoryId(processOrderNosByCategoryId);
        List<String> processOrderNosBySkuAttribute = this.getProcessOrderNosBySkuAttribute(skuAttributes);
        processOrderQueryBo.setProcessOrderNosBySkuAttribute(processOrderNosBySkuAttribute);

        List<String> processOrderNosBySkuEncode = this.getProcessOrderNosBySkuEncodeList(skuEncodeList);
        processOrderQueryBo.setProcessOrderNosBySkuEncode(processOrderNosBySkuEncode);

        List<String> processOrderNosByMaterialSku = this.getProcessOrderNosByMaterialSkus(materialSkuList);
        processOrderQueryBo.setProcessOrderNosByMaterialSku(processOrderNosByMaterialSku);

        List<String> processOrderNosByDefective = this.getProcessOrderNosByDefective(defectiveRecordStatus);
        processOrderQueryBo.setProcessOrderNosByDefective(processOrderNosByDefective);

        boolean isEmpty = CollectionUtils.isNotEmpty(skus) && CollectionUtils.isEmpty(processOrderNosBySku);

        if (CollectionUtils.isNotEmpty(categoryIdList) && CollectionUtils.isEmpty(processOrderNosByCategoryId)) {
            isEmpty = true;
        }
        if (CollectionUtils.isNotEmpty(skuAttributes) && CollectionUtils.isEmpty(processOrderNosBySkuAttribute)) {
            isEmpty = true;
        }
        if (CollectionUtils.isNotEmpty(skuEncodeList) && CollectionUtils.isEmpty(processOrderNosBySkuEncode)) {
            isEmpty = true;
        }
        if (null != defectiveRecordStatus && CollectionUtils.isEmpty(processOrderNosByDefective)) {
            isEmpty = true;
        }
        if (CollectionUtils.isNotEmpty(materialSkuList) && CollectionUtils.isEmpty(processOrderNosByMaterialSku)) {
            isEmpty = true;
        }
        processOrderQueryBo.setIsEmpty(isEmpty);

        return processOrderQueryBo;
    }

    /**
     * 通过 sku 产品名称查询加工单编号
     *
     * @param skuEncodeList
     * @return
     */
    public List<String> getProcessOrderNosBySkuEncodeList(List<String> skuEncodeList) {
        List<String> processOrderNos = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(skuEncodeList)) {
            // 查询 sku
            List<PlmSkuVo> skuBySkuEncode = plmRemoteService.getSkuBySkuEncode(skuEncodeList);
            if (CollectionUtils.isNotEmpty(skuBySkuEncode)) {
                List<String> skus = skuBySkuEncode.stream()
                        .map(PlmSkuVo::getSkuCode)
                        .collect(Collectors.toList());
                List<ProcessOrderItemPo> processOrderItemPos = processOrderItemDao.getBySkus(skus);
                processOrderNos = processOrderItemPos.stream()
                        .map(ProcessOrderItemPo::getProcessOrderNo)
                        .collect(Collectors.toList());
            }
        }
        return processOrderNos;
    }

    public List<String> getProcessOrderNosBySkus(List<String> skus) {
        List<String> processOrderNos = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(skus)) {
            // 查询 sku
            List<ProcessOrderItemPo> processOrderItemPos = processOrderItemDao.getBySkus(skus);
            processOrderNos = processOrderItemPos.stream()
                    .map(ProcessOrderItemPo::getProcessOrderNo)
                    .collect(Collectors.toList());
        }
        return processOrderNos;
    }

    /**
     * 通过原料 sku 查询加工单号
     *
     * @param materialSkuList
     * @return
     */
    public List<String> getProcessOrderNosByMaterialSkus(List<String> materialSkuList) {
        List<String> processOrderNos = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(materialSkuList)) {
            // 查询 sku
            List<ProcessOrderMaterialPo> processOrderMaterialPoList = processOrderMaterialDao.getBySkuList(
                    materialSkuList);
            processOrderNos = processOrderMaterialPoList.stream()
                    .map(ProcessOrderMaterialPo::getProcessOrderNo)
                    .collect(Collectors.toList());
        }
        return processOrderNos;
    }

    /**
     * 通过分类查询 spu
     *
     * @param categoryIdList
     * @return
     */
    public List<String> getProcessOrderNosByCategoryId(List<Long> categoryIdList) {
        List<String> skuByCategoryId = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(categoryIdList)) {
            skuByCategoryId = plmRemoteService.getSkuListByCategoryIdList(categoryIdList);
        }
        return this.getProcessOrderNosBySkus(skuByCategoryId);
    }

    /**
     * 通过属性查询 spu
     *
     * @param skuAttributes
     * @return
     */
    public List<String> getProcessOrderNosBySkuAttribute(List<String> skuAttributes) {
        List<String> skuByAttribute = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(skuAttributes)) {
            skuByAttribute = sampleRefService.getSkuListByProperties(skuAttributes);
        }
        return this.getProcessOrderNosBySkus(skuByAttribute);
    }

    /**
     * 次品记录获取加工单号
     *
     * @param defectiveRecordStatus
     * @return
     */
    public List<String> getProcessOrderNosByDefective(DefectiveRecordStatus defectiveRecordStatus) {
        List<String> processOrderNos = new ArrayList<>();

        // 查询存在次品的加工单
        List<ProcessOrderItemPo> processOrderItemPos = processOrderItemDao.getByHasDefective(DEFECTIVE_GOODS_CNT);
        List<String> queryProcessOrderNos = processOrderItemPos.stream()
                .map(ProcessOrderItemPo::getProcessOrderNo)
                .collect(Collectors.toList());

        // 查询关联的次品记录
        List<ProcessDefectiveRecordPo> processDefectiveRecordPoList = processDefectiveRecordDao.getByProcessOrderNos(
                queryProcessOrderNos);
        Map<String, List<ProcessDefectiveRecordPo>> groupedDefectiveRecord = processDefectiveRecordPoList.stream()
                .collect(Collectors.groupingBy(ProcessDefectiveRecordPo::getProcessOrderNo));

        // 次品待处理
        if (DefectiveRecordStatus.DEFECTIVE_WAIT_HANDLE.equals(defectiveRecordStatus)) {
            processOrderNos = queryProcessOrderNos.stream()
                    .filter(item -> {
                        List<ProcessDefectiveRecordPo> processDefectiveRecordPoListByItem = groupedDefectiveRecord.get(
                                item);
                        return CollectionUtils.isEmpty(processDefectiveRecordPoListByItem);
                    })
                    .collect(Collectors.toList());
        }

        // 次品返工中
        if (DefectiveRecordStatus.DEFECTIVE_HANDLED.equals(defectiveRecordStatus)) {
            processOrderNos = queryProcessOrderNos.stream()
                    .filter(item -> {
                        List<ProcessDefectiveRecordPo> processDefectiveRecordPoListByItem = groupedDefectiveRecord.get(
                                item);
                        return CollectionUtils.isNotEmpty(processDefectiveRecordPoListByItem);
                    })
                    .collect(Collectors.toList());
        }

        return processOrderNos;
    }

    /**
     * 获取生产图片
     *
     * @param sku
     * @param platform
     * @return
     */
    public List<String> getSkuImage(String sku,
                                    String platform) {

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
        return this.formatPlmSkuImage(plmSkuImage);
    }

    /**
     * 格式化图片
     *
     * @param plmSkuImage
     * @return
     */
    public List<String> formatPlmSkuImage(PlmSkuImage plmSkuImage) {
        List<String> fileCodeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(plmSkuImage.getFileCodeList())) {
            fileCodeList = plmSkuImage.getFileCodeList();
        } else {
            String spuSaleMainImageCode = plmSkuImage.getSpuSaleMainImageCode();
            if (StringUtils.isNotBlank(spuSaleMainImageCode)) {
                fileCodeList.add(spuSaleMainImageCode);
            }
        }
        return fileCodeList;
    }


    /**
     * 查询加工进度
     *
     * @param processOrderNo
     * @return
     */
    public List<ProcessOrderProcedureByH5Vo> getProcessOrderScanByNo(String processOrderNo) {
        // 获取加工单所有的扫码记录
        ArrayList<ProcessOrderProcedureByH5Vo> processOrderProcedureByH5Vos = new ArrayList<>();
        Map<Long, List<ProcessOrderScanPo>> groupedProcessOrderScanPos = processOrderScanDao.getByProcessOrderNo(
                        processOrderNo)
                .stream()
                .collect(Collectors.groupingBy(ProcessOrderScanPo::getProcessOrderProcedureId));

        List<ProcessOrderProcedurePo> processOrderProcedurePos = processOrderProcedureDao.getByProcessOrderNo(
                processOrderNo);
        if (CollectionUtils.isEmpty(processOrderProcedurePos)) {
            return processOrderProcedureByH5Vos;
        }

        List<Long> processIds = processOrderProcedurePos.stream()
                .map(ProcessOrderProcedurePo::getProcessId)
                .collect(Collectors.toList());
        List<ProcessPo> processPoList = processDao.getByProcessIds(processIds);
        processOrderProcedurePos.forEach(it -> {
            Optional<ProcessPo> first1 = processPoList.stream()
                    .filter(it2 -> it2.getProcessId()
                            .equals(it.getProcessId()))
                    .findFirst();
            ProcessOrderProcedureByH5Vo processOrderProcedureByH5Vo = new ProcessOrderProcedureByH5Vo();
            processOrderProcedureByH5Vo.setProcessOrderProcedureId(it.getProcessOrderProcedureId());
            processOrderProcedureByH5Vo.setProcessCode(it.getProcessCode());
            processOrderProcedureByH5Vo.setProcessName(it.getProcessName());
            first1.ifPresent(
                    processPo -> processOrderProcedureByH5Vo.setProcessSecondName(processPo.getProcessSecondName()));
            processOrderProcedureByH5Vo.setProcessLabel(it.getProcessLabel());
            processOrderProcedureByH5Vo.setProcessId(it.getProcessId());

            List<ProcessOrderScanPo> processOrderScanPos = groupedProcessOrderScanPos.get(
                    it.getProcessOrderProcedureId());

            // 存在扫码记录
            if (CollectionUtils.isNotEmpty(processOrderScanPos)) {
                Optional<ProcessOrderScanPo> first = processOrderScanPos.stream()
                        .findFirst();
                if (first.isPresent()) {
                    ProcessOrderScanPo processOrderScanPo = first.get();
                    processOrderProcedureByH5Vo.setReceiptNum(processOrderScanPo.getReceiptNum());
                    processOrderProcedureByH5Vo.setReceiptUser(processOrderScanPo.getReceiptUser());
                    processOrderProcedureByH5Vo.setReceiptUsername(processOrderScanPo.getReceiptUsername());
                    processOrderProcedureByH5Vo.setReceiptTime(processOrderScanPo.getReceiptTime());
                    processOrderProcedureByH5Vo.setDefectiveGoodsCnt(processOrderScanPo.getDefectiveGoodsCnt());
                    processOrderProcedureByH5Vo.setQualityGoodsCnt(processOrderScanPo.getQualityGoodsCnt());
                    processOrderProcedureByH5Vo.setCompleteTime(processOrderScanPo.getCompleteTime());
                    processOrderProcedureByH5Vo.setCompleteUsername(processOrderScanPo.getCompleteUsername());
                    processOrderProcedureByH5Vos.add(processOrderProcedureByH5Vo);
                }
            } else {
                processOrderProcedureByH5Vo.setReceiptNum(0);
                processOrderProcedureByH5Vo.setReceiptUser("");
                processOrderProcedureByH5Vo.setReceiptUsername("");
                processOrderProcedureByH5Vo.setReceiptTime(null);
                processOrderProcedureByH5Vo.setDefectiveGoodsCnt(0);
                processOrderProcedureByH5Vo.setQualityGoodsCnt(0);
                processOrderProcedureByH5Vo.setCompleteTime(null);
                processOrderProcedureByH5Vo.setCompleteUsername("");
                processOrderProcedureByH5Vos.add(processOrderProcedureByH5Vo);
            }
        });

        return processOrderProcedureByH5Vos;
    }


    /**
     * 判断原料归还状态
     *
     * @return
     */
    public MaterialBackBo getBackStatus(String processOrderNo) {
        // 原料收货信息
        final List<ProcessMaterialReceiptPo> processMaterialReceiptPos = processMaterialReceiptDao.getByProcessOrderNo(
                processOrderNo);
        final List<Long> processMaterialReceiptIds = CollectionUtils.isEmpty(processMaterialReceiptPos) ?
                Collections.emptyList() : processMaterialReceiptPos.stream()
                .map(ProcessMaterialReceiptPo::getProcessMaterialReceiptId)
                .collect(Collectors.toList());
        final List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPos
                = processMaterialReceiptItemDao.getByMaterialReceiptIds(processMaterialReceiptIds);

        // 原料归还信息
        final List<ProcessMaterialBackPo> processMaterialBackPos = processMaterialBackDao.getByProcessOrderNo(
                processOrderNo);
        final List<Long> processMaterialBackIds = CollectionUtils.isEmpty(processMaterialBackPos) ?
                Collections.emptyList() : processMaterialBackPos.stream()
                .map(ProcessMaterialBackPo::getProcessMaterialBackId)
                .collect(Collectors.toList());
        final List<ProcessMaterialBackItemPo> processMaterialBackItemPos
                = processMaterialBackItemDao.listByProcessMaterialBackIds(processMaterialBackIds);

        Map<String, Integer> skuBatchCodeReceiptMap = processMaterialReceiptItemPos.stream()
                .collect(Collectors.groupingBy(ProcessMaterialReceiptItemPo::getSkuBatchCode,
                        Collectors.summingInt(ProcessMaterialReceiptItemPo::getReceiptNum)));

        Map<String, Integer> skuBatchCodeBackMap = processMaterialBackItemPos.stream()
                .collect(Collectors.groupingBy(ProcessMaterialBackItemPo::getSkuBatchCode,
                        Collectors.summingInt(ProcessMaterialBackItemPo::getDeliveryNum)));

        List<MaterialBackBo.MaterialBackSku> materialBackSkus = skuBatchCodeReceiptMap.keySet()
                .stream()
                .map(skuBatchCode -> {
                    MaterialBackBo.MaterialBackSku materialBackSku = new MaterialBackBo.MaterialBackSku();
                    materialBackSku.setSkuBatchCode(skuBatchCode);

                    int skuBatchCodeReceiptNum = skuBatchCodeReceiptMap.get(skuBatchCode);
                    int skuBatchCodeBackNum = skuBatchCodeBackMap.getOrDefault(skuBatchCode, 0);
                    int availableNum = skuBatchCodeReceiptNum - skuBatchCodeBackNum;
                    materialBackSku.setAvailableBackNum(availableNum);
                    return materialBackSku;
                })
                .collect(Collectors.toList());
        MaterialBackBo materialBackBo = new MaterialBackBo();
        materialBackBo.setMaterialBackSkus(materialBackSkus);

        // 原料可归还数量信息
        int totalDeliveryNum = CollectionUtils.isEmpty(
                processMaterialReceiptItemPos) ? 0 : processMaterialReceiptItemPos.stream()
                .mapToInt(ProcessMaterialReceiptItemPo::getReceiptNum)
                .sum();
        int totalBackNum = processMaterialBackItemPos.stream()
                .mapToInt(ProcessMaterialBackItemPo::getDeliveryNum)
                .sum();
        int availableNum = totalDeliveryNum - totalBackNum;
        materialBackBo.setAvailableBackNum(availableNum);

        // 根据加工单原料总出库数和总归还数更新原料归还状态
        if (totalDeliveryNum > 0 && totalBackNum == 0) {
            materialBackBo.setMaterialBackStatus(MaterialBackStatus.UN_BACK);
        } else if (totalDeliveryNum > 0 && totalBackNum != totalDeliveryNum) {
            materialBackBo.setMaterialBackStatus(MaterialBackStatus.PARTIAL_BACK);
        } else {
            materialBackBo.setMaterialBackStatus(MaterialBackStatus.NO_BACK);
        }

        return materialBackBo;
    }


    /**
     * 更新数量
     *
     * @param processMaterialBackItemPoList
     * @param processOrderNo
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmConstant.PROCESS_MATERIAL_UPDATE_LOCK_PREFIX, key = "#processOrderNo", waitTime = 1,
            leaseTime = -1)
    public void updateProcessOrderMaterialBackNum(List<ProcessMaterialBackItemPo> processMaterialBackItemPoList,
                                                  String processOrderNo) {

        ProcessOrderPo queriedProcessOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (null == queriedProcessOrderPo) {
            throw new BizException("加工单不存在");
        }

        List<ProcessOrderMaterialPo> processOrderMaterialPoList = processOrderMaterialDao.getByProcessOrderNo(
                processOrderNo);
        if (CollectionUtils.isEmpty(processOrderMaterialPoList)) {
            throw new BizException("加工单不存在原料");
        }
        ArrayList<ProcessOrderMaterialPo> lastMaterialPos = new ArrayList<>();
        processMaterialBackItemPoList.forEach(item -> {
            Optional<ProcessOrderMaterialPo> first = processOrderMaterialPoList.stream()
                    .filter(it -> it.getSkuBatchCode()
                            .equals(item.getSkuBatchCode()))
                    .findFirst();
            if (first.isPresent()) {

                ProcessOrderMaterialPo processOrderMaterialPo = first.get();
                int backNum = processOrderMaterialPo.getBackNum() + item.getDeliveryNum();
                processOrderMaterialPo.setBackNum(backNum);
                if (backNum <= processOrderMaterialPo.getDeliveryNum()) {
                    lastMaterialPos.add(processOrderMaterialPo);
                }
            }
        });

        if (CollectionUtils.isNotEmpty(lastMaterialPos)) {
            processOrderMaterialDao.updateBatchByIdVersion(lastMaterialPos);
        }


        MaterialBackBo materialBackBo = this.getBackStatus(processOrderNo);
        MaterialBackStatus materialBackStatus = materialBackBo.getMaterialBackStatus();
        queriedProcessOrderPo.setMaterialBackStatus(materialBackStatus);

        processOrderDao.updateByIdVersion(queriedProcessOrderPo);
    }


    public void canCreateProcessOrder(ProcessOrderCreateDto dto) {
        ProcessOrderType processOrderType = dto.getProcessOrderType();
        ProcessOrderOriginal processOrderOriginal = dto.getProcessOrderOriginal();

        boolean isReworking = ProcessOrderType.REWORKING.equals(
                processOrderType) || ProcessOrderType.LIMITED_REWORKING.equals(processOrderType);
        if (isReworking && !ProcessOrderOriginal.REWORKING.equals(processOrderOriginal)) {
            throw new ParamIllegalException("返工类型加工单只能通过次品返工创建");
        }

        if (isReworking && StringUtils.isBlank(dto.getParentProcessOrderNo())) {
            throw new BizException("返工类型加工单必须存在关联的加工单");
        }

        // 补单类型的加工单不能选原料
        if (ProcessOrderType.EXTRA.equals(processOrderType) && CollectionUtils.isNotEmpty(
                dto.getProcessOrderMaterials())) {
            throw new ParamIllegalException("补单类型的加工单不能选择原料");
        }

    }

    /**
     * 加工单初始状态
     *
     * @param processOrderType
     * @return
     */
    public ProcessOrderStatus parseProcessOrderStatusByProcessOrderType(ProcessOrderType processOrderType) {
        List<ProcessOrderType> waitReadyProcTypes = Arrays.asList(
                ProcessOrderType.NORMAL,
                ProcessOrderType.LIMITED,
                ProcessOrderType.OVERSEAS_REPAIR,
                ProcessOrderType.WH,
                ProcessOrderType.REPAIR);
        if (waitReadyProcTypes.contains(processOrderType)) {
            return ProcessOrderStatus.WAIT_READY;
        } else if (ProcessOrderType.EXTRA.equals(processOrderType)) {
            return ProcessOrderStatus.WAIT_PLAN;
        } else if (Arrays.asList(ProcessOrderType.REWORKING, ProcessOrderType.LIMITED_REWORKING).contains(processOrderType)) {
            return ProcessOrderStatus.WAIT_PRODUCE;
        } else {
            throw new ParamIllegalException("无法根据类型解析状态:{}", processOrderType);
        }
    }

    /**
     * 推送加工单创建结果给 wms
     *
     * @param processOrderPo
     */
    @Transactional(rollbackFor = Exception.class)
    public void createProcessWaveResultMq(ProcessOrderPo processOrderPo) {
        ProcessWaveCreateResultMqDto processWaveCreateResultMqDto = new ProcessWaveCreateResultMqDto();
        processWaveCreateResultMqDto.setProcessWaveId(processOrderPo.getProcessWaveId());
        processWaveCreateResultMqDto.setProcessOrderNo(processOrderPo.getProcessOrderNo());

        processWaveCreateResultMqDto.setKey(
                idGenerateService.getSnowflakeCode(processOrderPo.getProcessOrderNo() + "-"));

        log.info("limited 加工单{}，完成创建，回推结果给 wms", processOrderPo.getProcessOrderNo());
        consistencySendMqService.execSendMq(WmsWaveCreateResultHandler.class, processWaveCreateResultMqDto);
    }


    /**
     * 向特定加工订单中添加缺失信息，并更新缺失信息列表。
     * 该方法用于将一组缺失信息添加到指定加工订单中，并更新缺失信息列表。
     *
     * @param processOrderNo               待更新缺失信息的加工订单号。
     * @param updateMissingInformationList 包含要添加的缺失信息的集合。
     */
    public void addMissInformation(String processOrderNo,
                                   Set<MissingInformation> updateMissingInformationList) {
        if (StrUtil.isBlank(processOrderNo)) {
            throw new ParamIllegalException("更新加工单缺失信息失败,加工单号为空");
        }
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (Objects.isNull(processOrderPo)) {
            throw new ParamIllegalException("更新加工单缺失信息失败,加工单信息为空");
        }
        if (CollectionUtils.isEmpty(updateMissingInformationList)) {
            throw new ParamIllegalException("更新加工单缺失信息失败,缺失信息为空");
        }
        final String existMissingInformation = processOrderPo.getMissingInformation();
        Set<MissingInformation> existMissingInformationList = MissInformationConverter.convertToMissingInformationEnums(
                existMissingInformation);
        existMissingInformationList.addAll(updateMissingInformationList);
        String updateMissInformation = MissInformationConverter.convertToMissInformation(existMissingInformationList);

        processOrderPo.setMissingInformation(updateMissInformation);
        processOrderPo.setProcessOrderStatus(ProcessOrderStatus.WAIT_READY);
        processOrderDao.updateById(processOrderPo);
    }

    /**
     * 从特定加工订单中移除缺失信息，并更新缺失信息列表。
     * 该方法用于从指定的加工订单中移除一组缺失信息，并更新缺失信息列表以反映变更。
     *
     * @param processOrderNo               待更新缺失信息的加工订单号。
     * @param updateMissingInformationList 包含要移除的缺失信息的集合。
     */
    public void removeMissInformation(String processOrderNo,
                                      Set<MissingInformation> updateMissingInformationList) {
        if (StrUtil.isBlank(processOrderNo)) {
            throw new ParamIllegalException("更新加工单缺失信息失败,加工单号为空");
        }
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (Objects.isNull(processOrderPo)) {
            throw new ParamIllegalException("更新加工单缺失信息失败,加工单信息为空");
        }
        if (CollectionUtils.isEmpty(updateMissingInformationList)) {
            throw new ParamIllegalException("更新加工单缺失信息失败,缺失信息为空");
        }
        final String existMissingInformation = processOrderPo.getMissingInformation();
        Set<MissingInformation> existMissingInformationList = MissInformationConverter.convertToMissingInformationEnums(
                existMissingInformation);
        existMissingInformationList.removeIf(updateMissingInformationList::contains);
        String updateMissInformation = MissInformationConverter.convertToMissInformation(existMissingInformationList);

        processOrderPo.setMissingInformation(updateMissInformation);
        processOrderDao.updateById(processOrderPo);
    }

    /**
     * 更新加工订单时间信息并返回更新后的加工订单对象。
     * 该方法用于根据传入的加工订单对象和员工计划列表，更新加工订单的时间信息，并返回更新后的加工订单对象。
     *
     * @param processOrderPo   待更新时间信息的加工订单对象。应符合验证注解和非空要求。
     * @param employeePlanList 员工计划列表，包含了与加工订单相关的员工计划信息。
     * @return 更新后的加工订单对象，包含了更新后的时间信息。
     */
    public ProcessOrderPo refreshProcessOrderTime(@Valid @NotNull ProcessOrderPo processOrderPo,
                                                  List<ProcessProcedureEmployeePlanBo> employeePlanList) {
        if (CollectionUtils.isNotEmpty(employeePlanList)) {
            // 获取 expectBeginDateTime 的最小值
            LocalDateTime minExpectBeginDateTime = employeePlanList.stream()
                    .map(ProcessProcedureEmployeePlanBo::getExpectBeginDateTime)
                    .min(LocalDateTime::compareTo)
                    .orElse(null);

            LocalDateTime maxExpectEndDateTime = employeePlanList.stream()
                    .map(ProcessProcedureEmployeePlanBo::getExpectEndDateTime)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
            if (minExpectBeginDateTime == null) {
                throw new ParamIllegalException("刷新加工单最早开始时间失败，最早开始时间为空");
            }
            if (maxExpectEndDateTime == null) {
                throw new ParamIllegalException("刷新加工单最晚结束时间失败，最晚结束时间为空");
            }
            processOrderPo.setProcessPlanTime(LocalDateTime.now());
            processOrderPo.setProcessPlanEarliestExpectBeginTime(minExpectBeginDateTime);
            processOrderPo.setProcessPlanLatestExpectEndTime(maxExpectEndDateTime);
        } else {
            processOrderPo.setProcessPlanEarliestExpectBeginTime(DefaultDatabaseTime.DEFAULT_TIME.getDateTime());
            processOrderPo.setProcessPlanLatestExpectEndTime(DefaultDatabaseTime.DEFAULT_TIME.getDateTime());
            processOrderPo.setProcessPlanTime(DefaultDatabaseTime.DEFAULT_TIME.getDateTime());
        }
        return processOrderPo;
    }

    public void updateOverPlan(String processOrderNo,
                               OverPlan overPlan) {
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (Objects.nonNull(processOrderNo)) {
            processOrderPo.setOverPlan(overPlan);
            processOrderDao.updateById(processOrderPo);
        }
    }

    /**
     * 刷新缺失信息，并根据指定条件进行操作。
     * 该方法用于根据传入的加工订单号、操作用户信息、刷新类型和条件标志，刷新缺失信息并根据条件进行操作。
     *
     * @param processOrderNo           待刷新缺失信息的加工订单号。应符合验证注解和非空要求。
     * @param operatorUserBo           执行刷新操作的用户信息对象。
     * @param missingInfoOperationType 缺失信息刷新类型，指定刷新的具体方式。
     * @param filterBizException       是否过滤业务异常。如果为true，当出现业务异常时不进行操作。
     */
    @Transactional(rollbackFor = Exception.class)
    public void refreshMissingInfo(@Valid @NotBlank String processOrderNo,
                                   OperatorUserBo operatorUserBo,
                                   MissingInfoOperationType missingInfoOperationType,
                                   boolean filterBizException) {
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (Objects.isNull(processOrderPo)) {
            log.info("刷新加工单缺失信息结束！加工单信息不存在，加工单号=>{}", processOrderNo);
            return;
        }

        // 非待齐备状态不需要刷新
        final ProcessOrderStatus processOrderStatus = processOrderPo.getProcessOrderStatus();
        if (!ProcessOrderStatus.WAIT_READY.equals(processOrderStatus)) {
            log.info("刷新加工单缺失信息结束！加工单状态不是待齐备状态，加工单号=>{}", processOrderNo);
            return;
        }

        // 返单、补单类型不刷新状态
        final ProcessOrderType processOrderType = processOrderPo.getProcessOrderType();
        List<ProcessOrderType> notNeedRefreshType = Arrays.asList(
                ProcessOrderType.EXTRA,
                ProcessOrderType.LIMITED_REWORKING,
                ProcessOrderType.REWORKING);
        if (notNeedRefreshType.contains(processOrderType)) {
            log.info("刷新加工单缺失信息结束！加工单类型=>{}不支持刷新，加工单号=>{}", processOrderType, processOrderNo);
            return;
        }

        /*
          根据指定的缺失信息操作类型=原料，执行相应的操作，包括检查原料信息是否存在并更新、刷新库存信息、创建原料出库单、
          以及在操作完成后检查原料、工序、库存是否齐全，并进行相应的流转状态操作。
         */
        if (MissingInfoOperationType.PERFORM_MATERIAL_OPERATIONS.equals(missingInfoOperationType)) {
            SpringUtil.getBean(this.getClass()).refreshProcessOrderMaterials(processOrderNo, filterBizException);
            SpringUtil.getBean(this.getClass()).refreshInventory(processOrderNo);
            SpringUtil.getBean(this.getClass()).createProcessMaterialDetail(processOrderNo);
            SpringUtil.getBean(this.getClass()).doRefreshMissingInfoAfter(processOrderNo, operatorUserBo);
        }

        /*
          根据指定的缺失信息操作类型=原料覆盖更新，执行相应的操作，包括覆盖更新工序信息以及在操作完成后检查原料、工序、库存是否齐全，
          并进行相应的流转状态操作。
         */
        if (MissingInfoOperationType.PERFORM_MATERIAL_UPDATE.equals(missingInfoOperationType)) {
            SpringUtil.getBean(this.getClass()).updateProcessOrderMaterials(processOrderNo);
            SpringUtil.getBean(this.getClass()).refreshInventory(processOrderNo);
            SpringUtil.getBean(this.getClass()).createProcessMaterialDetail(processOrderNo);
            SpringUtil.getBean(this.getClass()).doRefreshMissingInfoAfter(processOrderNo, operatorUserBo);
        }

        /*
          根据指定的缺失信息操作类型=工序，执行相应的操作，包括刷新工序信息以及在操作完成后检查原料、工序、库存是否齐全，
          并进行相应的流转状态操作。
         */
        if (MissingInfoOperationType.PERFORM_PROCEDURE_OPERATIONS.equals(missingInfoOperationType)) {
            SpringUtil.getBean(this.getClass()).refreshProcessOrderProcedures(processOrderNo, filterBizException);
            SpringUtil.getBean(this.getClass()).doRefreshMissingInfoAfter(processOrderNo, operatorUserBo);
        }

        /*
          根据指定的缺失信息操作类型=原料且工序，执行相应的操作，包括刷新原料信息、库存信息、生成原料出库单、刷新工序信息，
          以及在操作完成后检查原料、工序、库存是否齐全，并进行相应的流转状态操作。
         */
        if (MissingInfoOperationType.PERFORM_MATERIAL_PROCEDURE_OPERATIONS.equals(missingInfoOperationType)) {
            SpringUtil.getBean(this.getClass()).refreshProcessOrderMaterials(processOrderNo, filterBizException);
            SpringUtil.getBean(this.getClass()).refreshInventory(processOrderNo);
            SpringUtil.getBean(this.getClass()).createProcessMaterialDetail(processOrderNo);
            SpringUtil.getBean(this.getClass()).refreshProcessOrderProcedures(processOrderNo, filterBizException);
            SpringUtil.getBean(this.getClass()).doRefreshMissingInfoAfter(processOrderNo, operatorUserBo);
        }

        /*
          根据指定的缺失信息操作类型=工序检查，执行相应的操作，包括检查工序是否存在、记录缺失信息、以及在操作完成后检查原料、工序、库存是否齐全，
          并进行相应的流转状态操作。
         */
        if (MissingInfoOperationType.CHECK_PROCEDURE.equals(missingInfoOperationType)) {
            boolean existProcedures = checkProcessOrderProceduresExist(processOrderNo);
            log.info("检查工序！加工单号：{} 是否存在工序：{}", processOrderNo, existProcedures);
            if (!existProcedures) {
                SpringUtil.getBean(this.getClass()).addMissInformation(processOrderNo, Set.of(MissingInformation.NOT_EXIST_PROCESS));
            } else {
                SpringUtil.getBean(this.getClass()).removeMissInformation(processOrderNo, Set.of(MissingInformation.NOT_EXIST_PROCESS));
            }
            SpringUtil.getBean(this.getClass()).doRefreshMissingInfoAfter(processOrderNo, operatorUserBo);
        }

        /*
          根据指定的缺失信息操作类型=全部检查，执行相应的操作，包括检查原料信息、库存信息、工序信息是否齐全，记录缺失信息以及在操作完成后检查原料、工序、库存是否齐全，
          并进行相应的流转状态操作。
         */
        if (MissingInfoOperationType.CHECK_MATERIAL_PROCEDURE.equals(missingInfoOperationType)) {
            // 检查原料信息
            boolean existMaterial = checkMaterialExistence(processOrderNo);
            log.info("检查原料！加工单号：{} 是否存在原料：{}", processOrderNo, existMaterial);

            // 检查是否都存在原料出库单
            boolean existenceMaterialDeliveryNo = checkMaterialDeliveryNoExistence(processOrderNo);
            log.info("检查原料是否生成过出库单！加工单号：{} 结果：{}", processOrderNo, existenceMaterialDeliveryNo);

            if (!existMaterial) {
                SpringUtil.getBean(this.getClass()).addMissInformation(processOrderNo, Set.of(MissingInformation.NOT_EXIST_MATERIAL));
            } else {
                SpringUtil.getBean(this.getClass()).removeMissInformation(processOrderNo, Set.of(MissingInformation.NOT_EXIST_MATERIAL));

                boolean existMaterialSkuInStock = SpringUtil.getBean(this.getClass()).checkMaterialSkuInStock(processOrderNo);
                log.info("检查原料库存！加工单号：{} 是否存在库存：{}", processOrderNo, existMaterialSkuInStock);

                // 存在原料 & 无库存 & 未生成出库单
                if (!existMaterialSkuInStock && !existenceMaterialDeliveryNo) {
                    SpringUtil.getBean(this.getClass()).addMissInformation(processOrderNo, Set.of(MissingInformation.OUT_OF_STOCK));
                    existMaterial = false;
                } else {
                    SpringUtil.getBean(this.getClass()).removeMissInformation(processOrderNo, Set.of(MissingInformation.OUT_OF_STOCK));
                }
            }

            if (existMaterial && !existenceMaterialDeliveryNo) {
                log.info("存在原料和库存，且没有生成过原料出库单，加工单号：{} 生成出库单", processOrderNo);
                SpringUtil.getBean(this.getClass()).createProcessMaterialDetail(processOrderNo);
            }

            boolean existProcedures = checkProcessOrderProceduresExist(processOrderNo);
            log.info("检查工序！加工单号：{} 是否存在工序：{}", processOrderNo, existProcedures);
            if (!existProcedures) {
                SpringUtil.getBean(this.getClass()).addMissInformation(processOrderNo, Set.of(MissingInformation.NOT_EXIST_PROCESS));
            } else {
                SpringUtil.getBean(this.getClass()).removeMissInformation(processOrderNo, Set.of(MissingInformation.NOT_EXIST_PROCESS));
            }
            if (existMaterial && existProcedures) {
                SpringUtil.getBean(this.getClass()).doRefreshMissingInfoAfter(processOrderNo, operatorUserBo);
            }
        }
    }

    /**
     * 检查特定加工订单是否存在工序。
     * 该方法用于检查给定加工订单号是否存在相关的处理流程信息。
     *
     * @param processOrderNo 要检查的加工订单号。
     * @return 如果存在与加工订单关联的处理流程，则返回true；否则返回false。
     */
    public boolean checkProcessOrderProceduresExist(String processOrderNo) {
        if (StrUtil.isBlank(processOrderNo)) {
            throw new IllegalArgumentException("校验工序异常！加工单号不能为空");
        }

        List<ProcessOrderProcedurePo> processOrderProcedurePos = processOrderProcedureDao.getByProcessOrderNo(
                processOrderNo);
        return !CollectionUtils.isEmpty(processOrderProcedurePos);
    }


    /**
     * 检查特定加工单的原料是否存在。
     * 该方法用于检查给定加工订单号是否存在相关的物料信息。
     *
     * @param processOrderNo 要检查的加工订单号。
     * @return 如果存在与加工订单关联的原料信息，则返回true；否则返回false。
     */
    public boolean checkMaterialExistence(String processOrderNo) {
        if (StrUtil.isBlank(processOrderNo)) {
            throw new IllegalArgumentException("校验原料异常！加工单号不能为空");
        }

        List<ProcessOrderMaterialPo> processOrderMaterialPos = processOrderMaterialDao.getByProcessOrderNo(
                processOrderNo);
        return !CollectionUtils.isEmpty(processOrderMaterialPos);
    }


    /**
     * 根据加工单号校验原料的出库单单号是否存在且不为空。
     *
     * @param processOrderNo 加工单号，不能为空
     * @return 如果所有原料的交货单号存在且不为空，返回 true；否则返回 false
     * @throws IllegalArgumentException 如果加工单号为空，抛出 IllegalArgumentException 异常
     */
    public boolean checkMaterialDeliveryNoExistence(String processOrderNo) {
        if (StrUtil.isBlank(processOrderNo)) {
            throw new IllegalArgumentException("校验原料异常！加工单号不能为空");
        }

        List<ProcessOrderMaterialPo> processOrderMaterialPos = processOrderMaterialDao.getByProcessOrderNo(
                processOrderNo);
        return CollectionUtils.isNotEmpty(processOrderMaterialPos) &&
                processOrderMaterialPos.stream()
                        .allMatch(processOrderMaterialPo -> StringUtils.isNotBlank(
                                processOrderMaterialPo.getDeliveryNo()));
    }

    /**
     * @Description 校验加工单原料库存是否满足
     * @author yanjiawei
     * @Date 2024/11/13 14:46
     */
    public boolean checkMaterialSkuInStock(String processOrderNo) {
        ParamValidUtils.requireNotBlank(processOrderNo, "校验原料库存失败！加工单号不能为空");
        ProcessOrderPo processOrderPo = ParamValidUtils.requireNotNull(processOrderDao.getByProcessOrderNo(processOrderNo),
                StrUtil.format("校验原料库存失败！加工单{}不存在", processOrderNo)
        );

        List<ProcessOrderMaterialPo> processOrderMaterialPos = processOrderMaterialDao.getByProcessOrderNo(processOrderNo);
        if (CollectionUtils.isEmpty(processOrderMaterialPos)) {
            log.info("校验库存结果=>{}，无原料信息！加工单号=>{}", false, processOrderNo);
            return false;
        }
        List<String> materialSkus = processOrderMaterialPos.stream()
                .map(ProcessOrderMaterialPo::getSku)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(materialSkus)) {
            log.info("校验库存结果=>{}，原料sku为空！加工单号=>{}", false, processOrderNo);
            return false;
        }

        String warehouseCode = processOrderPo.getWarehouseCode();
        if (StringUtils.isNotBlank(processOrderPo.getDeliveryWarehouseCode())) {
            warehouseCode = processOrderPo.getDeliveryWarehouseCode();
        }
        WmsEnum.ProductQuality productQuality = Objects.isNull(processOrderPo.getProductQuality()) ?
                WmsEnum.ProductQuality.GOOD :
                processOrderPo.getProductQuality();
        List<MaterialInventoryCheckBo> checkBoList = ProcessOrderBuilder.buildMaterialInventoryCheckBoList(productQuality, processOrderMaterialPos);
        String platform = ParamValidUtils.requireNotBlank(processOrderPo.getPlatform(), "校验原料库存失败！平台编码为空");
        return SpringUtil.getBean(this.getClass()).validateMaterialInventory(warehouseCode, platform, checkBoList);
    }


    /**
     * 生成原料出库单
     */
    public void createProcessMaterialDetail(@Valid @NotBlank String processOrderNo) {
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (Objects.isNull(processOrderPo)) {
            throw new ParamIllegalException("加工单信息不存在，加工单号：{}", processOrderNo);
        }
        WmsEnum.ProductQuality productQuality = Objects.isNull(processOrderPo.getProductQuality()) ?
                WmsEnum.ProductQuality.GOOD : processOrderPo.getProductQuality();

        boolean outOfStock
                = checkProcessOrderStatusAndMissInformation(processOrderPo, ProcessOrderStatus.WAIT_READY, MissingInformation.OUT_OF_STOCK);
        boolean notExistMaterial = checkProcessOrderStatusAndMissInformation(processOrderPo, ProcessOrderStatus.WAIT_READY, MissingInformation.NOT_EXIST_MATERIAL);
        if (!outOfStock && !notExistMaterial) {
            String warehouseCode = processOrderPo.getWarehouseCode();
            if (StringUtils.isNotBlank(processOrderPo.getDeliveryWarehouseCode())) {
                warehouseCode = processOrderPo.getDeliveryWarehouseCode();
            }

            String warehouseName = processOrderPo.getWarehouseName();
            if (StringUtils.isNotBlank(processOrderPo.getDeliveryWarehouseName())) {
                warehouseName = processOrderPo.getDeliveryWarehouseName();
            }

            // 生成原料出库单
            ProcessMaterialDetailPo processMaterialDetailPo = null;

            List<ProcessOrderMaterialPo> processOrderMaterialPos = processOrderMaterialDao.getByProcessOrderNo(processOrderNo);
            if (CollectionUtils.isNotEmpty(processOrderMaterialPos)) {
                List<ProcessOrderMaterialSkuBo> skuBos = processOrderMaterialPos.stream().map(item -> {
                    ProcessOrderMaterialSkuBo processOrderMaterialSkuBo = new ProcessOrderMaterialSkuBo();
                    processOrderMaterialSkuBo.setSku(item.getSku());
                    processOrderMaterialSkuBo.setDeliveryNum(item.getDeliveryNum());
                    processOrderMaterialSkuBo.setSkuBatchCode(item.getSkuBatchCode());
                    return processOrderMaterialSkuBo;
                }).collect(Collectors.toList());

                //平台编码
                String platformCode = processOrderPo.getPlatform();
                processMaterialDetailPo = updateProcMaterialDeliveryInfo(processOrderPo.getProcessOrderNo(), skuBos, warehouseCode, warehouseName, productQuality, platformCode);

                ProcessMaterialDetailPo finalProcessMaterialDetailPo = processMaterialDetailPo;
                processOrderMaterialPos = processOrderMaterialPos.stream()
                        .peek(item -> item.setDeliveryNo(finalProcessMaterialDetailPo.getDeliveryNo()))
                        .collect(Collectors.toList());
            }

            // 生成批次码
            List<ProcessOrderItemPo> processOrderItemPoList = processOrderItemDao.getByProcessOrderNo(processOrderNo);
            List<ProcessOrderItemPo> newProcessOrderItemPoList = generateBatchCodes(processOrderItemPoList);

            // 如果成品信息有更新
            if (CollectionUtils.isNotEmpty(newProcessOrderItemPoList)) {
                processOrderItemDao.updateBatchByIdVersion(newProcessOrderItemPoList);
            }

            // 如果原料信息有更新
            if (CollectionUtils.isNotEmpty(processOrderMaterialPos)) {
                processOrderMaterialDao.updateBatchByIdVersion(processOrderMaterialPos);
            }

            // 保存原料明细信息
            if (null != processMaterialDetailPo) {
                processMaterialDetailDao.insert(processMaterialDetailPo);
            }
        }
    }

    /**
     * 刷新工序、原料、库存后置方法
     *
     * @param processOrderNo
     */
    public void doRefreshMissingInfoAfter(String processOrderNo,
                                          OperatorUserBo operatorUserBo) {
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (Objects.isNull(processOrderPo)) {
            return;
        }

        final String missingInformation = processOrderPo.getMissingInformation();
        Set<MissingInformation> existMissingInformationList = MissInformationConverter.convertToMissingInformationEnums(
                missingInformation);
        if (CollectionUtils.isEmpty(existMissingInformationList)) {
            changeStatus(processOrderPo, ProcessOrderStatus.WAIT_PLAN, operatorUserBo);
        } else {
            changeStatus(processOrderPo, ProcessOrderStatus.WAIT_READY, operatorUserBo);
        }
    }

    /**
     * 刷新特定加工单工序信息。
     * 该方法用于刷新给定加工订单号对应的处理流程信息。
     *
     * @param processOrderNo     待刷新处理流程信息的加工订单号。
     * @param filterBizException 是否过滤业务异常。如果为true，当出现业务异常时不进行刷新操作。
     */
    public void refreshProcessOrderProcedures(String processOrderNo,
                                              boolean filterBizException) {
        // 校验单据状态是待齐备 且缺货信息是无工序
        if (StringUtils.isBlank(processOrderNo)) {
            if (filterBizException) {
                log.error("更新加工单原料信息异常，加工单号为空");
                return;
            } else {
                throw new ParamIllegalException("更新加工单工序信息异常，加工单号为空");
            }
        }
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        List<ProcessOrderItemPo> processOrderItemPos = processOrderItemDao.getByProcessOrderNo(processOrderNo);
        List<ProcessOrderProcedurePo> processOrderProcedurePos = processOrderProcedureDao.getByProcessOrderNo(
                processOrderNo);

        if (CollectionUtils.isNotEmpty(processOrderProcedurePos)) {
            if (filterBizException) {
                removeMissInformation(processOrderNo, Set.of(MissingInformation.NOT_EXIST_PROCESS));
                return;
            } else {
                throw new BizException("加工单工序已存在，请刷新页面重试");
            }
        }
        if (Objects.isNull(processOrderPo)) {
            if (filterBizException) {
                log.info("刷新加工单工序信息失败 加工单信息不存在");
                return;
            } else {
                throw new BizException("加工单信息不存在，请刷新页面重试");
            }
        }
        if (CollectionUtils.isEmpty(processOrderItemPos)) {
            if (filterBizException) {
                log.info("刷新加工单工序信息失败 加工单明细信息不存在");
                return;
            } else {
                throw new BizException("加工单明细信息不存在，请刷新页面重试");
            }
        }
        ProcessOrderItemPo processOrderItemPo = processOrderItemPos.stream()
                .filter(item -> StrUtil.isNotBlank(item.getSku()))
                .findFirst()
                .orElse(null);
        if (Objects.isNull(processOrderItemPo)) {
            if (filterBizException) {
                return;
            } else {
                throw new ParamIllegalException("加工单明细sku不存在");
            }
        }
        final String processSku = processOrderItemPo.getSku();
        boolean checkStatus = this.checkProcessOrderStatusAndMissInformation(processOrderPo,
                ProcessOrderStatus.WAIT_READY,
                MissingInformation.NOT_EXIST_PROCESS);
        if (!checkStatus && !filterBizException) {
            throw new BizException("加工单状态必须是待齐备且缺失信息存在无工序信息");
        }

        // 获取工序信息
        List<SkuProcedureBo> skuProcedureBos = this.getSkuProcedureBos(processSku,
                processOrderPo.getDeliveryWarehouseCode(),
                processOrderPo.getProductQuality());
        if (CollectionUtils.isEmpty(skuProcedureBos)) {
            if (filterBizException) {
                addMissInformation(processOrderNo, Set.of(MissingInformation.NOT_EXIST_PROCESS));
                return;
            } else {
                if (null == skuProcedureBos) {
                    throw new ParamIllegalException(
                            "因商品sku({})未同步到SCM，导致无法获取到原料信息，请先去同步sku到SCM，再维护生产信息！",
                            processSku);
                }
                if (CollectionUtils.isEmpty(skuProcedureBos)) {
                    throw new ParamIllegalException(
                            "该加工sku({})的生产信息缺失，无法获取到工序信息，请前去生产信息进行维护！", processSku);
                }
            }
        }
        List<ProcessOrderProcedurePo> insertProcessOrderProcedurePos = SkuProcedureConverter.boToPo(skuProcedureBos,
                processOrderNo);
        processOrderProcedureDao.insertBatch(insertProcessOrderProcedurePos);

        // 更新加工单缺失信息和状态
        removeMissInformation(processOrderNo, Set.of(MissingInformation.NOT_EXIST_PROCESS));
    }

    /**
     * 刷新特定加工订单的原料信息。
     * 该方法用于刷新给定加工订单号对应的物料信息。
     *
     * @param processOrderNo     待刷新物料信息的加工订单号。应符合验证注解和非空要求。
     * @param filterBizException 是否过滤业务异常。如果为true，当出现业务异常时不进行刷新操作。
     */
    public void refreshProcessOrderMaterials(@Valid @NotBlank String processOrderNo,
                                             boolean filterBizException) {
        // 校验单据状态是待齐备 且缺货信息是无原料
        if (StringUtils.isBlank(processOrderNo)) {
            if (filterBizException) {
                log.error("更新加工单原料信息异常，加工单号为空");
                return;
            } else {
                throw new ParamIllegalException("更新加工单原料信息异常，加工单号为空");
            }
        }

        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        List<ProcessOrderItemPo> processOrderItemPos = processOrderItemDao.getByProcessOrderNo(processOrderNo);
        List<ProcessOrderMaterialPo> processOrderMaterialPos = processOrderMaterialDao.getByProcessOrderNo(
                processOrderNo);
        if (CollectionUtils.isNotEmpty(processOrderMaterialPos)) {
            if (filterBizException) {
                removeMissInformation(processOrderNo, Set.of(MissingInformation.NOT_EXIST_MATERIAL));
                return;
            } else {
                throw new BizException("加工单原料信息已存在，请刷新页面重试");
            }
        }
        if (Objects.isNull(processOrderPo)) {
            if (filterBizException) {
                log.info("刷新原料失败，加工单信息不存在");
                return;
            } else {
                throw new BizException("加工单信息不存在，请刷新页面重试");
            }
        }
        if (CollectionUtils.isEmpty(processOrderItemPos)) {
            if (filterBizException) {
                log.info("刷新原料失败，加工单信息不存在");
                return;
            } else {
                throw new BizException("加工单明细信息不存在，请刷新页面重试");
            }
        }

        final Integer totalProcessNum = processOrderPo.getTotalProcessNum();
        ProcessOrderItemPo processOrderItemPo = processOrderItemPos.stream()
                .filter(item -> StrUtil.isNotBlank(item.getSku()))
                .findFirst()
                .orElse(null);
        if (Objects.isNull(processOrderItemPo)) {
            if (filterBizException) {
                return;
            } else {
                throw new ParamIllegalException("加工单明细sku不存在");
            }
        }

        final String processSku = processOrderItemPo.getSku();
        boolean checkStatus = this.checkProcessOrderStatusAndMissInformation(processOrderPo,
                ProcessOrderStatus.WAIT_READY,
                MissingInformation.NOT_EXIST_MATERIAL);
        if (!checkStatus && !filterBizException) {
            throw new BizException("加工单状态必须是待齐备且缺失信息存在无原料信息");
        }

        // 获取原料信息
        String platform = ParamValidUtils.requireNotBlank(processOrderPo.getPlatform(), "刷新原料失败！平台编码为空");
        List<SkuMaterialBo> skuMaterialBos
                = getSkuMaterialBos(processSku, processOrderPo.getDeliveryWarehouseCode(), platform, processOrderPo.getProductQuality(), processOrderPo.getTotalProcessNum());
        if (CollectionUtils.isEmpty(skuMaterialBos)) {
            if (filterBizException) {
                addMissInformation(processOrderNo, Set.of(MissingInformation.NOT_EXIST_MATERIAL));
                return;
            } else {
                if (null == skuMaterialBos) {
                    throw new ParamIllegalException(
                            "因商品sku:{}未同步到SCM，导致无法获取到原料信息，请先去同步sku到SCM，再维护生产信息！",
                            processSku);
                } else {
                    throw new ParamIllegalException(
                            "该加工sku:{}的生产信息缺失，无法获取到原料信息，请前去生产信息进行维护！", processSku);
                }
            }
        }

        List<ProcessOrderMaterialRefBo> procMaterialRefBoList
                = SkuMaterialConverter.toProcMaterialRefBoList(skuMaterialBos, processOrderNo, totalProcessNum);
        for (ProcessOrderMaterialRefBo procMaterialRefBo : procMaterialRefBoList) {
            ProcessOrderMaterialPo procMaterialPo = procMaterialRefBo.getProcMaterialPo();
            processOrderMaterialDao.insert(procMaterialPo);

            List<ProcessOrderMaterialComparePo> procMaterialComparePoList = procMaterialRefBo.getProcMaterialComparePoList();
            if (CollectionUtils.isNotEmpty(procMaterialComparePoList)) {
                Long procMaterialId = procMaterialPo.getProcessOrderMaterialId();
                procMaterialComparePoList.forEach(comparePo -> comparePo.setProcessOrderMaterialId(procMaterialId));
                prodMaterialCompareDao.insertBatch(procMaterialComparePoList);
            }
        }

        // 更新加工单缺失信息和状态
        removeMissInformation(processOrderNo, Set.of(MissingInformation.NOT_EXIST_MATERIAL));
    }


    /**
     * 更新特定加工订单的原料信息。
     * 该方法用于刷新给定加工订单号对应的物料信息。
     *
     * @param processOrderNo 待刷新物料信息的加工订单号。应符合验证注解和非空要求。
     */
    public void updateProcessOrderMaterials(@Valid @NotBlank String processOrderNo) {
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (Objects.isNull(processOrderPo)) {
            throw new BizException("加工单信息不存在，请刷新页面重试");
        }
        List<ProcessOrderItemPo> processOrderItemPos = processOrderItemDao.getByProcessOrderNo(processOrderNo);
        if (CollectionUtils.isEmpty(processOrderItemPos)) {
            throw new BizException("加工单明细信息不存在，请刷新页面重试");
        }
        ProcessOrderItemPo processOrderItemPo = processOrderItemPos.stream()
                .filter(item -> StrUtil.isNotBlank(item.getSku()))
                .findFirst()
                .orElse(null);
        if (Objects.isNull(processOrderItemPo)) {
            throw new ParamIllegalException("加工单明细sku不存在");
        }

        // 获取原料信息
        List<ProcessOrderMaterialPo> processOrderMaterialPos = processOrderMaterialDao.getByProcessOrderNo(processOrderNo);
        final Integer totalProcessNum = processOrderPo.getTotalProcessNum();
        final String processSku = processOrderItemPo.getSku();
        String platform = ParamValidUtils.requireNotBlank(processOrderPo.getPlatform(), "更新原料失败！平台编码为空");
        List<SkuMaterialBo> skuMaterialBos
                = getSkuMaterialBos(processSku, processOrderPo.getDeliveryWarehouseCode(), platform, processOrderPo.getProductQuality(), processOrderPo.getTotalProcessNum());
        if (CollectionUtils.isEmpty(skuMaterialBos)) {
            if (null == skuMaterialBos) {
                throw new ParamIllegalException("因商品sku:{}未同步到SCM，导致无法获取到原料信息，请先去同步sku到SCM，再维护生产信息！", processSku);
            } else {
                throw new ParamIllegalException("该加工sku:{}的生产信息缺失，无法获取到原料信息，请前去生产信息进行维护！", processSku);
            }
        }

        //删除已存在加工单原料&原料对照关系
        if (CollectionUtils.isNotEmpty(processOrderMaterialPos)) {
            List<Long> delMaterialIdList = processOrderMaterialPos.stream()
                    .map(ProcessOrderMaterialPo::getProcessOrderMaterialId)
                    .collect(Collectors.toList());
            processOrderMaterialDao.removeBatchByIds(delMaterialIdList);

            List<ProcessOrderMaterialComparePo> procMaterialComparePoList = prodMaterialCompareDao.listByMaterialIds(delMaterialIdList);
            if (CollectionUtils.isNotEmpty(procMaterialComparePoList)) {
                prodMaterialCompareDao.removeBatch(procMaterialComparePoList);
            }
        }

        List<ProcessOrderMaterialRefBo> procMaterialRefBoList
                = SkuMaterialConverter.toProcMaterialRefBoList(skuMaterialBos, processOrderNo, totalProcessNum);

        //保存原料&原料对照关系
        for (ProcessOrderMaterialRefBo procMaterialRefBo : procMaterialRefBoList) {
            ProcessOrderMaterialPo procMaterialPo = procMaterialRefBo.getProcMaterialPo();
            processOrderMaterialDao.insert(procMaterialPo);

            List<ProcessOrderMaterialComparePo> procMaterialComparePoList = procMaterialRefBo.getProcMaterialComparePoList();
            if (CollectionUtils.isNotEmpty(procMaterialComparePoList)) {
                Long procMaterialId = procMaterialPo.getProcessOrderMaterialId();
                procMaterialComparePoList.forEach(comparePo -> comparePo.setProcessOrderMaterialId(procMaterialId));
                prodMaterialCompareDao.insertBatch(procMaterialComparePoList);
            }
        }

        // 更新加工单缺失信息和状态
        List<ProcessOrderMaterialPo> newestProcessOrderMaterialPos = processOrderMaterialDao.getByProcessOrderNo(processOrderNo);
        if (CollectionUtils.isNotEmpty(newestProcessOrderMaterialPos)) {
            removeMissInformation(processOrderNo, Set.of(MissingInformation.NOT_EXIST_MATERIAL));
        }
    }

    /**
     * 检查加工订单的状态和缺失信息是否符合指定条件。
     * 该方法用于检查给定加工订单的状态和缺失信息是否满足特定条件。
     *
     * @param processOrderPo     待检查的加工订单对象。
     * @param processOrderStatus 加工订单的待排产状态。
     * @param missingInformation 缺失信息对象，指定需要检查的缺失信息。
     * @return 如果加工订单的状态与待检查状态一致，并且缺失信息匹配指定条件，则返回true；否则返回false。
     */
    public boolean checkProcessOrderStatusAndMissInformation(ProcessOrderPo processOrderPo,
                                                             ProcessOrderStatus processOrderStatus,
                                                             MissingInformation missingInformation) {
        return Objects.equals(processOrderPo.getProcessOrderStatus(), processOrderStatus) &&
                StrUtil.isNotBlank(processOrderPo.getMissingInformation()) &&
                MissInformationConverter.convertToMissingInformationEnums(processOrderPo.getMissingInformation())
                        .contains(missingInformation);
    }

    /**
     * 检查加工订单的状态和缺失信息是否符合指定条件。
     * 该方法用于检查给定加工订单的状态和缺失信息是否满足特定条件。
     *
     * @param processOrderPo         待检查的加工订单对象。
     * @param processOrderStatus     加工订单的待排产状态。
     * @param missingInformationList 缺失信息列表，指定需要检查的缺失信息。
     * @return 如果加工订单的状态与待检查状态一致，并且缺失信息匹配指定条件，则返回true；否则返回false。
     */
    public boolean checkProcessOrderStatusAndMissInformation(ProcessOrderPo processOrderPo,
                                                             ProcessOrderStatus processOrderStatus,
                                                             Set<MissingInformation> missingInformationList) {
        for (MissingInformation missingInformation : missingInformationList) {
            if (checkProcessOrderStatusAndMissInformation(processOrderPo, processOrderStatus, missingInformation)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据提供的 GetSkuProcedureDto 对象，获取相应的 SkuProcedureVo sku工序对象列表。
     * 该方法用于根据传入的数据传输对象（GetSkuProcedureDto），获取与其相关联的处理流程信息。
     *
     * @param dto 包含获取信息所需参数的数据传输对象。
     * @return 包含 SkuProcedureVo 对象的列表，表示与给定 GetSkuProcedureDto 相匹配的处理流程信息。
     */
    public List<SkuProcedureVo> getSkuProcedures(GetSkuProcedureDto dto) {
        final String processSku = dto.getProcessSku();
        final ProcessOrderType processOrderType = dto.getProcessOrderType();

        if (Objects.equals(ProcessOrderType.OVERSEAS_REPAIR, processOrderType)) {
            return Collections.emptyList();
        }

        List<SkuProcedureBo> skuProcedureBos = this.getSkuProcedureBos(processSku, null, null);
        return SkuProcedureConverter.boToVo(skuProcedureBos);
    }

    /**
     * 根据提供的 GetSkuMaterialDto 对象，获取相应的 SkuMaterialVo 原料对象列表。
     * 该方法用于根据传入的数据传输对象（GetSkuMaterialDto），获取与其相关联的物料信息。
     *
     * @param dto 包含获取信息所需参数的数据传输对象。
     * @return 包含 SkuMaterialVo 对象的列表，表示与给定 GetSkuMaterialDto 相匹配的物料信息。
     */
    public List<SkuMaterialVo> getSkuMaterials(GetSkuMaterialDto dto) {
        final String processSku = dto.getProcessSku();
        final ProcessOrderType processOrderType = dto.getProcessOrderType();
        String platform = dto.getPlatform();

        if (Objects.equals(ProcessOrderType.OVERSEAS_REPAIR, processOrderType)) {
            return Collections.emptyList();
        }
        List<SkuMaterialBo> skuMaterialBos
                = this.getSkuMaterialBos(processSku, null, platform, null, 1);
        return SkuMaterialConverter.boToVo(skuMaterialBos);
    }

    /**
     * 获取特定加工模板SKU的SkuProcedureBo对象列表。
     * 该方法用于根据特定的加工SKU，获取与该模板相关的处理流程信息列表。
     *
     * @param processSku 加工的SKU信息。
     * @return 包含SkuProcedureBo对象的列表，表示与给定加工模板SKU相匹配的处理流程信息。
     */
    public List<SkuProcedureBo> getProcessTemplateSkuProcedureBos(String processSku) {
        if (StringUtils.isBlank(processSku)) {
            throw new ParamIllegalException("获取工序模板中的原料信息异常，sku为空！");
        }
        ProcessTemplatePo newestProcessTemplate = processTemplateDao.getNewestProcessTemplate(processSku,
                ProcessTemplateType.SKU);
        if (Objects.isNull(newestProcessTemplate)) {
            return Collections.emptyList();
        }
        final Long processTemplateId = newestProcessTemplate.getProcessTemplateId();
        List<ProcessTemplateRelationPo> processTemplateRelationPos = processTemplateRelationDao.getByProcessTemplateId(
                processTemplateId);
        if (CollectionUtils.isEmpty(processTemplateRelationPos)) {
            return Collections.emptyList();
        }
        List<Long> processIds = processTemplateRelationPos.stream()
                .filter(processTemplateRelationPo -> Objects.nonNull(processTemplateRelationPo.getProcessId()))
                .map(ProcessTemplateRelationPo::getProcessId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(processIds)) {
            return Collections.emptyList();
        }
        List<ProcessPo> processPos = processDao.getByProcessIds(processIds);
        if (CollectionUtils.isEmpty(processPos)) {
            return Collections.emptyList();
        }
        return processTemplateRelationPos.stream()
                .map(processTemplateRelationPo -> {
                    Long processId = processTemplateRelationPo.getProcessId();
                    ProcessPo matchProcessBo = processPos.stream()
                            .filter(processPo -> Objects.equals(processId, processPo.getProcessId()))
                            .findFirst()
                            .orElse(null);
                    if (Objects.isNull(matchProcessBo)) {
                        throw new ParamIllegalException("获取工序模板工序信息异常，无法匹配到工序信息!processId:{}",
                                processId);
                    }
                    SkuProcedureBo skuProcedureBo = new SkuProcedureBo();
                    skuProcedureBo.setSku(processSku);
                    skuProcedureBo.setProcessId(processId);
                    skuProcedureBo.setProcessCode(matchProcessBo.getProcessCode());
                    skuProcedureBo.setProcessName(matchProcessBo.getProcessName());
                    skuProcedureBo.setProcessLabel(matchProcessBo.getProcessLabel());
                    skuProcedureBo.setCommission(matchProcessBo.getCommission());
                    skuProcedureBo.setSort(processTemplateRelationPo.getSort());
                    return skuProcedureBo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 通过sku获取商品工序中的工序信息
     *
     * @param processSku
     * @return
     */
    public List<SkuProcedureBo> getGoodsProcessSkuProcedureBos(String processSku) {
        if (StringUtils.isBlank(processSku)) {
            throw new ParamIllegalException("获取商品工序中的原料信息异常，sku为空！");
        }
        GoodsProcessPo goodsProcessPos = goodsProcessDao.getNewestGoodsProcess(processSku);
        if (Objects.isNull(goodsProcessPos)) {
            return Collections.emptyList();
        }
        final Long goodsProcessId = goodsProcessPos.getGoodsProcessId();
        List<GoodsProcessRelationPo> goodsProcessRelationPos = goodsProcessRelationDao.getByGoodsProcessId(
                goodsProcessId);
        if (CollectionUtils.isEmpty(goodsProcessRelationPos)) {
            return Collections.emptyList();
        }
        List<Long> processIds = goodsProcessRelationPos.stream()
                .filter(goodsProcessRelationPo -> Objects.nonNull(goodsProcessRelationPo.getProcessId()))
                .map(GoodsProcessRelationPo::getProcessId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(processIds)) {
            return Collections.emptyList();
        }
        List<ProcessPo> processPos = processDao.getByProcessIds(processIds);
        if (CollectionUtils.isEmpty(processPos)) {
            return Collections.emptyList();
        }
        return goodsProcessRelationPos.stream()
                .map(goodsProcessRelationPo -> {
                    Long processId = goodsProcessRelationPo.getProcessId();
                    ProcessPo matchProcessBo = processPos.stream()
                            .filter(processPo -> Objects.equals(processId, processPo.getProcessId()))
                            .findFirst()
                            .orElse(null);
                    if (Objects.isNull(matchProcessBo)) {
                        throw new ParamIllegalException("获取商品工序模板工序信息异常，无法匹配到工序信息!processId:{}",
                                processId);
                    }
                    SkuProcedureBo skuProcedureBo = new SkuProcedureBo();
                    skuProcedureBo.setSku(processSku);
                    skuProcedureBo.setProcessId(processId);
                    skuProcedureBo.setProcessCode(matchProcessBo.getProcessCode());
                    skuProcedureBo.setProcessName(matchProcessBo.getProcessName());
                    skuProcedureBo.setProcessLabel(matchProcessBo.getProcessLabel());
                    skuProcedureBo.setCommission(matchProcessBo.getCommission());
                    skuProcedureBo.setSort(goodsProcessRelationPo.getSort());
                    return skuProcedureBo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 根据给定的加工单SKU和加工单类型，获取样品子订单的SkuProcedureBo对象列表。
     * 该方法用于根据特定的加工单SKU和加工单类型，获取与样品子订单相关的处理流程信息。
     *
     * @param processSku     加工单的SKU（库存单位）信息。
     * @param warehouseCode
     * @param productQuality
     * @return 包含SkuProcedureBo对象的列表，表示与给定加工单SKU和加工单类型相匹配的样品子订单的处理流程信息。
     * null 代表 sku不存在 空集合代表bom对应的工序信息不存在
     */
    public List<SkuProcedureBo> getSampleChildOrderSkuProcedureBos(String processSku,
                                                                   String warehouseCode,
                                                                   WmsEnum.ProductQuality productQuality) {
        if (StringUtils.isBlank(processSku)) {
            throw new ParamIllegalException("获取样品子单中的工序异常，sku为空！");
        }
        List<ProcessOrderProductionInfoBo> processOrderProductionInfoBos = this.getProcessOrderProductionInfoBos(
                processSku);
        if (null == processOrderProductionInfoBos) {
            // sku不存在
            return null;
        }

        ProcessOrderProductionInfoBo processOrderGenerateInfoBo = processOrderProductionInfoBos.stream()
                .filter(infoBo -> CollectionUtils.isNotEmpty(infoBo.getProcessOrderProcedureBoList()))
                .findFirst()
                .stream()
                .findFirst()
                .orElse(null);
        if (null == processOrderGenerateInfoBo) {
            // sku存在，但无工序信息
            return Collections.emptyList();
        }

        final List<ProcessOrderProductionInfoBo.ProcessOrderProcedureBo> processOrderProcedureBoList
                = processOrderGenerateInfoBo.getProcessOrderProcedureBoList();
        if (CollectionUtils.isEmpty(processOrderProcedureBoList)) {
            return Collections.emptyList();
        }

        List<Long> processIds = processOrderProcedureBoList.stream()
                .map(ProcessOrderProductionInfoBo.ProcessOrderProcedureBo::getProcessId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(processIds)) {
            return Collections.emptyList();
        }
        List<ProcessPo> processPos = processDao.getByProcessIds(processIds);
        if (CollectionUtils.isEmpty(processPos)) {
            return Collections.emptyList();
        }
        AtomicInteger sort = new AtomicInteger(1);

        return processOrderProcedureBoList.stream()
                .map(processOrderProcedureBo -> {
                    Long processId = processOrderProcedureBo.getProcessId();
                    ProcessPo matchProcessBo = processPos.stream()
                            .filter(processPo -> Objects.equals(processId, processPo.getProcessId()))
                            .findFirst()
                            .orElse(null);
                    if (Objects.isNull(matchProcessBo)) {
                        throw new ParamIllegalException("获取样品子单工序信息异常，无法匹配到工序信息!processId:{}",
                                processId);
                    }
                    SkuProcedureBo skuProcedureBo = new SkuProcedureBo();
                    skuProcedureBo.setSku(processSku);
                    skuProcedureBo.setProcessId(processId);
                    skuProcedureBo.setProcessCode(matchProcessBo.getProcessCode());
                    skuProcedureBo.setProcessName(matchProcessBo.getProcessName());
                    skuProcedureBo.setProcessLabel(matchProcessBo.getProcessLabel());
                    skuProcedureBo.setCommission(matchProcessBo.getCommission());
                    skuProcedureBo.setSort(sort.getAndIncrement());
                    return skuProcedureBo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 根据给定的处理SKU和处理加工单类型，获取对应的SkuProcedureBo对象列表。
     *
     * @param processSku     待处理的SKU。
     * @param warehouseCode
     * @param productQuality
     * @return 包含SkuProcedureBo对象的列表，表示与给定处理SKU和订单类型相匹配的处理流程信息。
     */
    public List<SkuProcedureBo> getSkuProcedureBos(String processSku,
                                                   String warehouseCode,
                                                   WmsEnum.ProductQuality productQuality) {
        if (StringUtils.isBlank(processSku)) {
            throw new ParamIllegalException("获取sku工序信息异常，sku为空！");
        }
        return this.getSampleChildOrderSkuProcedureBos(processSku, warehouseCode, productQuality);
    }

    /**
     * 根据给定的处理SKU和处理订单类型，获取相应的SkuMaterialBo对象列表。
     *
     * @param processSku     待处理的SKU（库存单位）信息。
     * @param warehouseCode  仓库编码
     * @param productQuality 良品/不良品信息
     * @return 包含SkuMaterialBo对象的列表，表示与给定处理SKU和订单类型相匹配的物料信息。
     */
    public List<SkuMaterialBo> getSkuMaterialBos(String processSku,
                                                 String warehouseCode,
                                                 String platform,
                                                 WmsEnum.ProductQuality productQuality,
                                                 Integer processNum) {
        if (StringUtils.isBlank(processSku)) {
            throw new ParamIllegalException("获取sku原料信息异常，sku为空！");
        }

        ProcessOrderProductionInfoBo processOrderGenerateInfoBo
                = this.getProcProdInfoBo(processSku, warehouseCode, platform, productQuality, processNum);
        if (null == processOrderGenerateInfoBo) {
            return null;
        }

        final List<ProcessOrderProductionInfoBo.ProcessOrderMaterialBo> processOrderMaterialBoList
                = processOrderGenerateInfoBo.getProcessOrderMaterialBoList();
        if (CollectionUtils.isEmpty(processOrderMaterialBoList)) {
            return Collections.emptyList();
        }
        List<SkuMaterialBo> sampleChildOrderSkuMaterialBos = processOrderMaterialBoList.stream()
                .map(processOrderMaterialBo -> {
                    SkuMaterialBo skuMaterialBo = new SkuMaterialBo();
                    skuMaterialBo.setSku(processOrderMaterialBo.getSku());
                    skuMaterialBo.setSingleNum(processOrderMaterialBo.getDeliveryNum());
                    skuMaterialBo.setProcessOrderMaterialCompareBoList(processOrderMaterialBo.getProcMaterialCompareBoList());
                    return skuMaterialBo;
                })
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(sampleChildOrderSkuMaterialBos)) {
            log.info(String.format("通过：%s，成品sku：%s 获取到加工单原料信息：%s", "BOM", processSku,
                    JSON.toJSONString(sampleChildOrderSkuMaterialBos)));
            return sampleChildOrderSkuMaterialBos;
        }
        return Collections.emptyList();
    }

    /**
     * 刷新库存信息，根据给定的处理加工单号。
     *
     * @param processOrderNo 处理订单的唯一标识号，用于定位要刷新库存的特定订单。
     */
    public void refreshInventory(String processOrderNo) {
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (null == processOrderPo) {
            throw new BizException("加工单不存在");
        }

        List<ProcessOrderMaterialPo> processOrderMaterialPos = processOrderMaterialDao.getByProcessOrderNo(processOrderNo);
        if (CollectionUtils.isEmpty(processOrderMaterialPos)) {
            log.info("校验库存结束，无原料信息！加工单号=>{}", processOrderNo);
            return;
        }
        List<String> materialSkus = processOrderMaterialPos.stream()
                .map(ProcessOrderMaterialPo::getSku)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(materialSkus)) {
            log.warn("校验库存结束，原料信息无sku！加工单号=>{}", processOrderNo);
            return;
        }

        //查询库存
        String warehouseCode = processOrderPo.getWarehouseCode();
        if (StringUtils.isNotBlank(processOrderPo.getDeliveryWarehouseCode())) {
            warehouseCode = processOrderPo.getDeliveryWarehouseCode();
        }
        WmsEnum.ProductQuality productQuality = Objects.isNull(processOrderPo.getProductQuality()) ?
                WmsEnum.ProductQuality.GOOD : processOrderPo.getProductQuality();
        List<MaterialInventoryCheckBo> checkBoList = ProcessOrderBuilder.buildMaterialInventoryCheckBoList(productQuality, processOrderMaterialPos);
        String platform = ParamValidUtils.requireNotBlank(processOrderPo.getPlatform(), "校验原料库存失败！平台编码为空");
        boolean validateMaterialInventory = SpringUtil.getBean(this.getClass()).validateMaterialInventory(warehouseCode, platform, checkBoList);
        if (!validateMaterialInventory) {
            SpringUtil.getBean(this.getClass()).addMissInformation(processOrderNo, Set.of(MissingInformation.OUT_OF_STOCK));
        } else {
            SpringUtil.getBean(this.getClass()).removeMissInformation(processOrderNo, Set.of(MissingInformation.OUT_OF_STOCK));
        }
        log.info("检查原料库存！加工单号=>{} 库存结果=>：{}", processOrderNo, validateMaterialInventory);
    }

    /**
     * @param processOrderNo 加工单号
     * @return ProcessMaterialDetailPo
     * @Description 更新加工单原料出库信息
     * @author yanjiawei
     * @Date 2024/9/9 10:51
     */
    @RedisLock(prefix = ScmConstant.PROCESS_MATERIAL_UPDATE_LOCK_PREFIX, key = "#processOrderNo", waitTime = 1, leaseTime = -1)
    public ProcessMaterialDetailPo updateProcMaterialDeliveryInfo(String processOrderNo, List<ProcessOrderMaterialSkuBo> skus,
                                                                  String warehouseCode, String warehouseName,
                                                                  WmsEnum.ProductQuality productQuality, String platCode) {
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (null == processOrderPo) {
            throw new BizException("更新加工单出库信息失败！加工单{}信息不存在。", processOrderNo);
        }

        if (CollectionUtils.isEmpty(skus)) {
            throw new BizException("更新加工单出库信息失败！sku数据为空。");
        }
        List<ProcessOrderMaterialSkuBo> deSkuList = skus.stream()
                .map(item -> {
                    ProcessOrderMaterialSkuBo processOrderMaterialSkuBo = new ProcessOrderMaterialSkuBo();
                    processOrderMaterialSkuBo.setSku(item.getSku());
                    processOrderMaterialSkuBo.setDeliveryNum(item.getDeliveryNum());
                    processOrderMaterialSkuBo.setSkuBatchCode(item.getSkuBatchCode());
                    return processOrderMaterialSkuBo;
                }).collect(Collectors.toList());

        //调用WMS接口创建原料出库单
        String deliveryNote = processOrderPo.getDeliveryNote();
        DeliveryOrderCreateVo deliveryRes
                = wmsRemoteService.createProcDelivery(processOrderNo, deSkuList, warehouseCode, deliveryNote, productQuality, platCode);

        //更新原料出库信息
        ProcessMaterialDetailPo processMaterialDetailPo = new ProcessMaterialDetailPo();
        processMaterialDetailPo.setProcessOrderNo(processOrderNo);
        processMaterialDetailPo.setDeliveryNo(deliveryRes.getDeliveryOrderNo());
        processMaterialDetailPo.setDeliveryWarehouseCode(warehouseCode);
        processMaterialDetailPo.setDeliveryWarehouseName(warehouseName);
        processMaterialDetailPo.setProductQuality(productQuality);
        return processMaterialDetailPo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void clearContainerCode(String processOrderNo) throws BizException {
        if (StrUtil.isBlank(processOrderNo)) {
            throw new BizException("加工单信息不存在");
        }

        // 根据加工单号查询 ProcessOrderPo
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        if (Objects.isNull(processOrderPo)) {
            throw new BizException("加工单信息不存在");
        }

        // 清空容器编码
        processOrderPo.setContainerCode("");
        // 执行更新操作
        processOrderDao.updateByIdVersion(processOrderPo);
    }

    /**
     * 校验两个集合是否完全相同。
     *
     * @param existingProcedurePos 第一个集合
     * @param modifiedProcedurePos 第二个集合
     * @return 如果两个集合完全相同，返回true；否则返回false。
     */
    public boolean areListsEqual(List<ProcessOrderProcedurePo> existingProcedurePos,
                                 List<ProcessOrderProcedurePo> modifiedProcedurePos) {
        // 第一步：按sort升序排序
        existingProcedurePos.sort(Comparator.comparingLong(ProcessOrderProcedurePo::getSort));
        modifiedProcedurePos.sort(Comparator.comparingLong(ProcessOrderProcedurePo::getSort));

        // 第二步：判断元素数量是否相同
        if (existingProcedurePos.size() != modifiedProcedurePos.size()) {
            return false;
        }

        // 第三步：逐个比较元素的 processOrderProcedureId 和 processId
        for (int i = 0; i < existingProcedurePos.size(); i++) {
            ProcessOrderProcedurePo existingItem = existingProcedurePos.get(i);
            ProcessOrderProcedurePo modifiedItem = modifiedProcedurePos.get(i);

            if (!Objects.equals(existingItem.getProcessOrderProcedureId(), modifiedItem.getProcessOrderProcedureId()) ||
                    !Objects.equals(existingItem.getProcessId(), modifiedItem.getProcessId())) {
                // 存在不匹配的 processOrderProcedureId 或 processId，返回false
                return false;
            }
        }

        // 如果三个步骤都通过了，说明集合相同
        return true;
    }

    /**
     * 通过平台编码和SKU编码列表获取商品信息。
     *
     * @param platCode 平台编码。
     * @param skuList  SKU编码列表。
     * @return 包含商品信息的 PlmPlatSkuVo 列表。
     */
    public List<PlmPlatSkuVo> getPlatSkuByPlatCodeAndSkuList(String platCode,
                                                             List<String> skuList) {
        // 创建 PlmPlatSkuListDto 对象并设置参数
        PlmPlatSkuDto plmPlatSkuDto = new PlmPlatSkuDto();
        plmPlatSkuDto.setPlatCode(platCode);
        plmPlatSkuDto.setSkuCodeList(skuList);

        List<PlmPlatSkuDto> platSkuDtoList = Lists.newArrayList();
        platSkuDtoList.add(plmPlatSkuDto);

        PlmPlatSkuListDto param = new PlmPlatSkuListDto();
        param.setPlatSkuDtoList(platSkuDtoList);

        // 调用PLM远程服务获取商品信息
        return plmRemoteService.getPlatSkuByPlatAndCode(param);
    }

    /**
     * 校验原料sku库存是否充足
     *
     * @param warehouseCode 仓库编码
     * @param checkBoList   原料库存校验对象列表
     * @return 如果所有校验通过，返回true；否则返回false
     * @throws ParamIllegalException 当校验库存sku信息为空时抛出参数异常
     */
    public boolean validateMaterialInventory(String warehouseCode, String platform, List<MaterialInventoryCheckBo> checkBoList) {
        ParamValidUtils.requireNotBlank(platform, "校验库存失败！平台为空。");

        if (CollectionUtils.isEmpty(checkBoList)) {
            throw new ParamIllegalException("校验库存sku信息不能为空！");
        }

        // 初始化为true
        boolean availableInventory = true;

        // 使用Stream对List按productQuality分组
        Map<WmsEnum.ProductQuality, List<MaterialInventoryCheckBo>> groupedByQuality = checkBoList.stream()
                .collect(Collectors.groupingBy(MaterialInventoryCheckBo::getProductQuality));
        for (Map.Entry<WmsEnum.ProductQuality, List<MaterialInventoryCheckBo>> entry : groupedByQuality.entrySet()) {
            BatchCodeInventoryQueryDto batchCodeInventoryQueryDto = new BatchCodeInventoryQueryDto();
            batchCodeInventoryQueryDto.setPlatCode(platform);
            batchCodeInventoryQueryDto.setWarehouseCode(warehouseCode);

            WmsEnum.ProductQuality productQuality = entry.getKey();
            if (Objects.isNull(productQuality)) {
                throw new ParamIllegalException("库存校验失败！产品质量不能为空。");
            }
            batchCodeInventoryQueryDto.setProductQuality(productQuality);

            List<MaterialInventoryCheckBo> materialInventoryList = entry.getValue();
            List<String> materialSkus = materialInventoryList.stream()
                    .map(MaterialInventoryCheckBo::getMaterialSku)
                    .distinct()
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(materialSkus)) {
                throw new ParamIllegalException("库存校验失败！原料sku不能为空。");
            }
            batchCodeInventoryQueryDto.setSkuCodeList(materialSkus);

            // 不存在批次码，按照skuCode分组，否则不分组
            List<String> batchCodeList = materialInventoryList.stream()
                    .map(MaterialInventoryCheckBo::getBatchCode)
                    .filter(StrUtil::isNotBlank)
                    .collect(Collectors.toList());
            boolean existBatchCode = CollectionUtils.isNotEmpty(batchCodeList);
            Function<BatchCodeInventoryVo, String> groupingFunction = existBatchCode ? null : BatchCodeInventoryVo::getSkuCode;
            List<BatchCodeInventoryVo> skuInventoryVos = wmsRemoteService.listBatchCodeAvailableInventory(batchCodeInventoryQueryDto, groupingFunction);

            // 校验所有的原料sku是否都存在wms
            boolean allSkusExistInWms = CollectionUtils.isNotEmpty(skuInventoryVos) &&
                    skuInventoryVos.stream().allMatch(item -> materialSkus.contains(item.getSkuCode()));
            if (!allSkusExistInWms) {
                // 退出循环,表示校验未通过
                availableInventory = false;
                log.info("校验原料库存结果=>{} 原因：存在无库存的sku 查询库存sku=>{} 结果=>{}",
                        false, JSON.toJSONString(materialSkus), JSON.toJSONString(skuInventoryVos));
                break;
            }

            // 构建sku+批次码组合键与SkuBatchCodeBo的关系
            Map<String, SkuBatchCodeBo> skuBatchCodeMap = materialInventoryList.stream().collect(Collectors.toMap(
                    item -> item.getMaterialSku() + item.getBatchCode(),
                    item -> new SkuBatchCodeBo(item.getMaterialSku(), item.getBatchCode()),
                    // 合并函数，忽略重复键
                    (existing, replacement) -> existing
            ));

            // 按[原料sku-所需库存数】进行分组求和 & 校验每个sku的库存是否足够/校验每个sku对应的批次码的库存是否足够
            Map<String, Integer> requiredInventoryBySku = materialInventoryList.stream().collect(
                    Collectors.groupingBy(item -> item.getMaterialSku() + item.getBatchCode(),
                            Collectors.summingInt(MaterialInventoryCheckBo::getRequiredInventory))
            );

            for (Map.Entry<String, Integer> requiredInventoryEntry : requiredInventoryBySku.entrySet()) {
                String skuAndBatchCode = requiredInventoryEntry.getKey();
                SkuBatchCodeBo skuBatchCodeBo = skuBatchCodeMap.get(skuAndBatchCode);
                if (Objects.isNull(skuBatchCodeBo)) {
                    log.error("通过组合键无法找到SKU和批次码信息,组合键：{}", skuAndBatchCode);
                    break;
                }
                String materialSku = skuBatchCodeBo.getMaterialSku();
                String batchCode = skuBatchCodeBo.getBatchCode();
                Integer materialSkuRequiredInventory = requiredInventoryEntry.getValue();

                // 匹配到sku库存信息
                BatchCodeInventoryVo matchSkuInventoryInfo;
                if (existBatchCode) {
                    matchSkuInventoryInfo = skuInventoryVos.stream()
                            .filter(skuInventoryVo ->
                                    Objects.equals(materialSku, skuInventoryVo.getSkuCode()) && Objects.equals(batchCode, skuInventoryVo.getBatchCode()))
                            .findFirst()
                            .orElse(null);
                } else {
                    matchSkuInventoryInfo = skuInventoryVos.stream()
                            .filter(skuInventoryVo -> Objects.equals(materialSku, skuInventoryVo.getSkuCode()))
                            .findFirst()
                            .orElse(null);
                }
                if (Objects.isNull(matchSkuInventoryInfo)) {
                    // 退出循环,表示校验未通过
                    log.info("校验原料库存结果=>{} 原因：无法根据sku=>{} 批次码=>{} 匹配库存信息", false, materialSku, batchCode);
                    availableInventory = false;
                    break;
                }

                Integer skuAvailableInventory = matchSkuInventoryInfo.getInStockAmount();
                if (materialSkuRequiredInventory > skuAvailableInventory) {
                    // 退出循环,表示校验未通过
                    log.info("校验原料库存结果=>{} 原因：sku=>{} 需出库数=>{} 大于库存数=>{}",
                            false, materialSku, materialSkuRequiredInventory, skuAvailableInventory);
                    availableInventory = false;
                    break;
                }
            }

            if (!availableInventory) {
                // 退出循环,表示校验未通过
                log.info("校验原料库存结果=>{} 原因：存在库存不足的sku", false);
                break;
            }
        }

        // 返回校验结果
        return availableInventory;
    }


    /**
     * 验证加工单是否存在，如果不存在则抛出业务异常。
     *
     * @param processOrderNo 加工单号
     * @throws BizException 如果加工单不存在，则抛出业务异常
     */
    public void validateProcessOrderExists(@NotBlank String processOrderNo) {
        // 根据加工单号查询加工单信息
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);

        // 如果加工单信息不存在，则抛出业务异常
        if (Objects.isNull(processOrderPo)) {
            throw new BizException(StrUtil.format("加工单号：{} {}！", processOrderNo,
                    ProcessOrderValidationErrorMessage.PROCESS_ORDER_NOT_FOUND_PLEASE_REFRESH.getMessageTemplate()));
        }
    }

    /**
     * 验证加工单是否存在，如果不存在则抛出业务异常。
     *
     * @param processOrderNo 加工单号
     * @param errorMsg       自定义错误消息，用于构建异常信息
     * @throws BizException 如果加工单不存在，则抛出业务异常
     */
    public void validateProcessOrderExists(@NotBlank String processOrderNo,
                                           @NotBlank String errorMsg) {
        // 根据加工单号查询加工单信息
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);

        // 如果加工单信息不存在，则抛出业务异常
        if (Objects.isNull(processOrderPo)) {
            throw new BizException(StrUtil.format("加工单号：{} {}！", processOrderNo, errorMsg));
        }
    }

    /**
     * 验证加工单是否存在，并根据异常消息模板抛出异常。
     *
     * @param processOrderNo 加工单号
     * @param errorMsg       异常消息模板枚举
     * @throws BizException 如果加工单不存在，将抛出业务异常
     */
    public void validateProcessOrderExists(@NotBlank String processOrderNo,
                                           @NotNull ProcessOrderValidationErrorMessage errorMsg) {
        // 根据加工单号查询加工单信息
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);

        // 如果加工单信息不存在，则抛出业务异常
        if (Objects.isNull(processOrderPo)) {
            throw new ParamIllegalException(
                    StrUtil.format("加工单号：{} {}！", processOrderNo, errorMsg.getMessageTemplate()));
        }
    }

    /**
     * 获取加工单生产信息
     *
     * @param processSku 处理后的SKU，用于查询特定的生产信息
     * @param platform   平台信息，用于区分不同平台的生产信息
     * @return 返回包含加工生产信息的ProcessOrderProductionInfoBo对象
     */
    public ProcessOrderProductionInfoBo getProcProdInfoBo(String processSku, String platform) {
        return this.getProcProdInfoBo(processSku, null, platform);
    }

    /**
     * 获取加工单生产信息。
     *
     * @param processSku    加工SKU，用于查询生成信息。
     * @param warehouseCode 仓库编码，加工单的原料仓库。
     * @return 返回包含生成加工单信息的 {@link ProcessOrderProductionInfoBo} 对象。
     * ProcessOrderProductionInfoBo返回为空，说明processSku不存在；
     * ProcessOrderProductionInfoBo不为空，说明processSku存在，此时工序、原料、生产信息、可能部分存在。
     */
    public ProcessOrderProductionInfoBo getProcProdInfoBo(String processSku, String warehouseCode, String platform) {
        return this.getProcProdInfoBo(processSku, warehouseCode, platform, WmsEnum.ProductQuality.GOOD);
    }

    /**
     * 获取加工单生产信息。
     *
     * @param processSku     加工SKU，用于查询生成信息。
     * @param warehouseCode  仓库编码，加工单的原料仓库。
     * @param productQuality 产品质量，指定生成加工单的产品质量要求。
     * @return 返回包含生成加工单信息的 {@link ProcessOrderProductionInfoBo} 对象。
     * ProcessOrderProductionInfoBo返回为空，说明processSku不存在；
     * ProcessOrderProductionInfoBo不为空，说明processSku存在，此时工序、原料、生产信息、可能部分存在。
     */
    public ProcessOrderProductionInfoBo getProcProdInfoBo(String processSku, String warehouseCode, String platform, WmsEnum.ProductQuality productQuality) {
        return this.getProcProdInfoBo(processSku, warehouseCode, platform, productQuality, SINGLE_PROCESS_NUM);
    }


    /**
     * 获取加工单生产信息。
     *
     * @param processSku     加工SKU，用于查询生成信息。
     * @param warehouseCode  仓库编码，加工单的原料仓库。
     * @param productQuality 产品质量，指定生成加工单的产品质量要求。
     * @param processNum     加工数量
     * @return 返回包含生成加工单信息的 {@link ProcessOrderProductionInfoBo} 对象。
     * ProcessOrderProductionInfoBo返回为空，说明processSku不存在；
     * ProcessOrderProductionInfoBo不为空，说明processSku存在，此时工序、原料、生产信息、可能部分存在。
     */
    public ProcessOrderProductionInfoBo getProcProdInfoBo(String processSku, String warehouseCode, String platform, WmsEnum.ProductQuality productQuality, Integer processNum) {
        if (Objects.isNull(processNum)) {
            throw new ParamIllegalException("获取加工单生产信息失败，加工数为空");
        }

        // 获取加工单BOM信息
        List<ProcessOrderProductionInfoBo> bomList = this.getProcessOrderProductionInfoBos(processSku);
        if (CollectionUtils.isEmpty(bomList)) {
            log.info("获取加工单bom返回为空！processSku=>{} ", processSku);
            return null;
        }
        if (StrUtil.isBlank(warehouseCode)) {
            ProcessOrderProductionInfoBo bomInfo = bomList.stream().findFirst().orElse(null);
            log.info("获取加工单bom返回第一条！仓库编码为空 processSku=>{} productQuality=>{} result=>{}", processSku, productQuality, JSON.toJSONString(bomInfo));
            return bomInfo;
        }

        //筛选库存满足的bom
        WmsEnum.ProductQuality productQualityParam = Objects.isNull(productQuality) ? WmsEnum.ProductQuality.GOOD : productQuality;
        for (ProcessOrderProductionInfoBo bomInfo : bomList) {
            Long bomId = bomInfo.getBomId();
            List<ProcessOrderProductionInfoBo.ProcessOrderMaterialBo> processOrderMaterialBoList = bomInfo.getProcessOrderMaterialBoList();
            if (CollectionUtils.isEmpty(processOrderMaterialBoList)) {
                log.info("筛选满足库存的bom结束!bomId=>{}不存在原料", bomId);
                continue;
            }

            List<MaterialInventoryCheckBo> checkBoList = processOrderMaterialBoList.stream().map(processOrderMaterial -> {
                MaterialInventoryCheckBo materialInventoryCheckBo = new MaterialInventoryCheckBo();
                materialInventoryCheckBo.setProductQuality(productQualityParam);
                materialInventoryCheckBo.setMaterialSku(processOrderMaterial.getSku());
                materialInventoryCheckBo.setRequiredInventory(processOrderMaterial.getDeliveryNum() * processNum);
                return materialInventoryCheckBo;
            }).collect(Collectors.toList());
            ParamValidUtils.requireNotBlank(platform, "校验原料库存失败！平台编码为空");
            boolean validateMaterialInventory = validateMaterialInventory(warehouseCode, platform, checkBoList);
            log.info("筛选满足库存的bom结束!库存结果=>{} warehouseCode=>{} platform=>{} checkBoList=>{} ", validateMaterialInventory, warehouseCode, platform, JSON.toJSONString(checkBoList));

            if (validateMaterialInventory) {
                log.info("筛选满足库存的bom结束!库存满足 warehouseCode=>{} platform=>{} bomInfo=>{} ", warehouseCode, platform, JSON.toJSONString(bomInfo));
                return bomInfo;
            }
        }

        //默认返回
        ProcessOrderProductionInfoBo defaultRes = new ProcessOrderProductionInfoBo();
        ProcessOrderProductionInfoBo matchSkuProdInfo = bomList.stream()
                .filter(info -> Objects.nonNull(info) && StrUtil.isNotBlank(info.getProcessSku()))
                .findFirst()
                .orElse(null);
        String existProcessSku = Objects.isNull(matchSkuProdInfo) ? "" : matchSkuProdInfo.getProcessSku();
        defaultRes.setProcessSku(existProcessSku);

        //库存不满足/bom原料不存在
        ProcessOrderProductionInfoBo res = bomList.stream()
                .filter(infoBo -> Objects.nonNull(infoBo) && CollectionUtils.isNotEmpty(infoBo.getProcessOrderMaterialBoList()))
                .findFirst()
                .orElse(defaultRes);
        log.info("获取加工单bom返回存在原料的bom！库存不满足 processSku=>{} warehouseCode=>{} platform=>{} productQuality=>{} bomInfo=>{}",
                processSku, warehouseCode, platform, productQuality, JSON.toJSONString(res));
        return res;
    }

    /**
     * @Description 通过sku获取生产资料信息
     * @author yanjiawei
     * @Date 2024/11/12 10:33
     */
    public List<ProcessOrderProductionInfoBo> getProcessOrderProductionInfoBos(String processSku) {
        ProduceDataBySkuListDto queryParam = new ProduceDataBySkuListDto();
        queryParam.setSku(processSku);
        List<ProduceDataDetailBo> produceDataBoList
                = produceDataBaseService.getProduceDataBySkuList(Collections.singletonList(queryParam));

        boolean existSku = produceDataBoList.stream().anyMatch(detail -> Objects.nonNull(detail) && StrUtil.isNotBlank(detail.getSku()));
        if (!existSku) {
            log.info("获取加工单生产资料-bom信息返回为空！processSku=>{}不存在", processSku);
            return null;
        }

        // 获取工序信息
        List<String> processCodes = this.getProcessCodes(produceDataBoList);
        List<ProcessPo> processPos = processDao.getByProcessCodes(processCodes);
        return ProcessOrderProductionConverter.toProcessOrderProductionInfoBos(produceDataBoList, processPos);
    }

    public List<String> getProcessCodes(List<ProduceDataDetailBo> skuProduceDataList) {
        List<String> processCodes = skuProduceDataList.stream()
                .map(ProduceDataDetailBo::getProduceDataItemBoList)
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(produceDataItemBoList -> produceDataItemBoList.stream()
                        .map(ProduceDataItemBo::getProduceDataItemProcessBoList)
                        .filter(CollectionUtils::isNotEmpty)
                        .flatMap(produceDataItemProcessBoList -> produceDataItemProcessBoList.stream()
                                .map(ProduceDataItemProcessListBo::getProcessCode)))
                .collect(Collectors.toList());

        return CollectionUtils.isEmpty(processCodes) ? Collections.emptyList() : processCodes;
    }

    /**
     * 根据加工单类型列表获取限制仓库编码的方法。
     *
     * @param processOrderTypes 加工单类型列表
     * @return 包含限制仓库编码的 Map，key 为加工单类型，value 为仓库编码
     */
    public Map<ProcessOrderType, Set<String>> getRestrictedWarehouseCodes(Set<ProcessOrderType> processOrderTypes) {
        if (CollectionUtils.isEmpty(processOrderTypes)) {
            throw new ParamIllegalException("获取加工单可选仓库异常！加工单类型为空。");
        }

        Map<ProcessOrderType, Set<String>> restrictedWarehouseCodeMap = new HashMap<>(16);
        for (ProcessOrderType processOrderType : processOrderTypes) {
            Set<String> restrictedWarehouseCodes = getConfigRestrictedWarehouseCodes(processOrderType);
            restrictedWarehouseCodeMap.put(processOrderType, restrictedWarehouseCodes);
        }

        return restrictedWarehouseCodeMap;
    }

    /**
     * 获取所有加工单类型的可选仓库编码的方法。
     *
     * @return 包含可选仓库编码的 Map，key 为加工单类型，value 为仓库编码
     */
    public Map<ProcessOrderType, Set<String>> getSelectableWarehouseCodeMap(Set<ProcessOrderType> processOrderTypes) {
        Map<ProcessOrderType, Set<String>> selectableWarehouseCodeMap = new HashMap<>(16);
        Map<ProcessOrderType, Set<String>> allRestrictedWarehouseCodes = getRestrictedWarehouseCodes(Set.of(ProcessOrderType.values()));

        for (ProcessOrderType processOrderType : processOrderTypes) {
            Set<String> selectableWarehouseCodes = allRestrictedWarehouseCodes.get(processOrderType);
            if (CollectionUtils.isNotEmpty(selectableWarehouseCodes)) {
                selectableWarehouseCodeMap.put(processOrderType, selectableWarehouseCodes);
            } else {
                Set<String> canNotSelectWarehouseCodes = allRestrictedWarehouseCodes.values()
                        .stream()
                        .flatMap(Set::stream)
                        .collect(Collectors.toSet());
                List<WarehouseVo> warehouseVos = wmsRemoteService.getAllWarehouse();
                if (CollectionUtils.isNotEmpty(canNotSelectWarehouseCodes) && CollectionUtils.isNotEmpty(warehouseVos)) {
                    warehouseVos.removeIf(
                            warehouseVo -> canNotSelectWarehouseCodes.contains(warehouseVo.getWarehouseCode()));
                    selectableWarehouseCodeMap.put(processOrderType, warehouseVos.stream().map(WarehouseVo::getWarehouseCode).collect(
                            Collectors.toSet()));
                }
            }
        }
        return selectableWarehouseCodeMap;
    }


    private Set<String> getConfigRestrictedWarehouseCodes(ProcessOrderType processOrderType) {
        final String RESTRICTED_WAREHOUSE_PREFIX_KEY = "procOrderTypeWarehouseRestricted";
        Set<String> orderTypes = Sets.newHashSet();
        int index = 0;
        String orderType = "";
        while ((orderType = environment.getProperty(
                StrUtil.format("{}.{}[{}]", RESTRICTED_WAREHOUSE_PREFIX_KEY, processOrderType.name(),
                        index))) != null) {
            orderTypes.add(orderType);
            index++;
        }
        return orderTypes;
    }

    /**
     * 更新订单的交期延期状态。
     *
     * @param processOrder 订单实体对象。
     */
    public void updatePromiseDateDelayed(ProcessOrderPo processOrder) {
        if (Objects.isNull(processOrder)) {
            log.error("更新订单的交期延期状态失败！加工单信息不存在。");
            return;
        }

        // 获取订单的答交时间
        LocalDateTime promiseDate = processOrder.getPromiseDate();

        // 判断答交时间不为空
        if (Objects.nonNull(promiseDate)) {
            // 获取当前时间
            LocalDateTime currentTime = LocalDateTime.now();

            // 计算答交时间减1后的时间
            LocalDateTime promiseDateMinusOne = promiseDate.minusDays(1);

            // 判断是否延期
            if (promiseDateMinusOne.isBefore(currentTime)) {
                // 更新订单的交期延期状态为延期
                processOrder.setPromiseDateDelayed(PromiseDateDelayed.DELAYED);
            } else {
                // 更新订单的交期延期状态为正常
                processOrder.setPromiseDateDelayed(PromiseDateDelayed.NOT_DELAYED);
            }

            // 更新订单
            processOrderDao.updateByIdVersion(processOrder);
        }
    }
}
