package com.hete.supply.scm.server.scm.process.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.api.scm.entity.enums.CreateType;
import com.hete.supply.scm.server.scm.process.builder.ProcessOrderBuilder;
import com.hete.supply.scm.server.scm.process.dao.*;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessOrderSampleBo;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderCreateDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderMaterialCompareDto;
import com.hete.supply.scm.server.scm.process.entity.po.*;
import com.hete.supply.scm.server.scm.process.enums.ProcessStatus;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: RockyHuas
 * @date: 2022/11/17 15:54
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class ProcessOrderCreateBaseService {

    private final ProcessDao processDao;
    private final ProcessOrderDao processOrderDao;
    private final ProcessOrderItemDao processOrderItemDao;
    private final ProcessOrderMaterialDao processOrderMaterialDao;
    private final ProcessOrderProcedureDao processOrderProcedureDao;
    private final ProcessOrderDescDao processOrderDescDao;
    private final ProcessOrderExtraDao processOrderExtraDao;
    private final ProcessOrderSampleDao processOrderSampleDao;
    private final ProcessMaterialDetailDao processMaterialDetailDao;
    private final ProcessOrderMaterialCompareDao prodMaterialCompareDao;

    /**
     * 创建加工单
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ProcessOrderPo create(ProcessOrderPo processOrderPo, ProcessOrderCreateDto dto,
                                 List<ProcessOrderItemPo> updateProcessOrderItemPos,
                                 List<ProcessOrderSampleBo> processOrderSampleBoList) {

        // 创建加工单
        processOrderDao.insert(processOrderPo);

        // 创建加工单详情
        this.createProcessOrderDetails(processOrderPo.getProcessOrderNo(), dto, BooleanType.TRUE,
                BooleanType.FALSE, processOrderSampleBoList);

        // 创建加工单额外信息
        ProcessOrderExtraPo processOrderExtraPo = new ProcessOrderExtraPo();
        processOrderExtraPo.setProcessOrderNo(processOrderPo.getProcessOrderNo());
        processOrderExtraDao.insert(processOrderExtraPo);

        if (CollectionUtils.isNotEmpty(updateProcessOrderItemPos)) {
            Map<String, ProcessOrderItemPo> groupedProcessOrderItemPos = updateProcessOrderItemPos.stream().collect(Collectors.toMap(ProcessOrderItemPo::getSku, it -> it));
            List<ProcessOrderItemPo> needUpdateProcessOrderItemPos = processOrderItemDao.getByProcessOrderNo(processOrderPo.getProcessOrderNo());
            needUpdateProcessOrderItemPos = needUpdateProcessOrderItemPos.stream().peek(item -> {
                ProcessOrderItemPo processOrderItemPo = groupedProcessOrderItemPos.get(item.getSku());
                if (null != processOrderItemPo) {
                    item.setSkuBatchCode(processOrderItemPo.getSkuBatchCode());
                }
            }).collect(Collectors.toList());

            this.updateProcessOrderSkuBatchCode(needUpdateProcessOrderItemPos);
        }

        return processOrderPo;
    }


    /**
     * 创建加工单详细信息
     *
     * @param processOrderNo
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    public void createProcessOrderDetails(String processOrderNo, ProcessOrderCreateDto dto, BooleanType isCreateItem
            , BooleanType isUpdateFileCode, List<ProcessOrderSampleBo> processOrderSampleBoList) {
        // 创建加工单成品
        if (BooleanType.TRUE.equals(isCreateItem)) {
            List<ProcessOrderItemPo> processOrderItemPoList = processOrderItemDao.getByProcessOrderNo(processOrderNo);
            if (CollectionUtils.isNotEmpty(processOrderItemPoList)) {
                processOrderItemDao.removeBatchByIds(processOrderItemPoList);
            }
            this.createProcessOrderItems(processOrderNo, dto);
        }

        // 是否更新生产图片
        if (BooleanType.TRUE.equals(isUpdateFileCode) && CollectionUtils.isNotEmpty(dto.getFileCodeList())) {
            ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
            processOrderPo.setFileCode(String.join(",", dto.getFileCodeList()));
            processOrderDao.updateByIdVersion(processOrderPo);
        }

        // 删除历史信息
        List<ProcessOrderMaterialPo> processOrderMaterialPoList = processOrderMaterialDao.getByProcessOrderNo(processOrderNo);
        if (CollectionUtils.isNotEmpty(processOrderMaterialPoList)) {
            processOrderMaterialDao.removeBatchByIds(processOrderMaterialPoList);
        }

        List<ProcessOrderProcedurePo> processOrderProcedurePoList = processOrderProcedureDao.getByProcessOrderNo(processOrderNo);
        if (CollectionUtils.isNotEmpty(processOrderProcedurePoList)) {
            processOrderProcedureDao.removeBatchByIds(processOrderProcedurePoList);
        }

        List<ProcessOrderDescPo> processOrderDescPoList = processOrderDescDao.getByProcessOrderNo(processOrderNo);
        if (CollectionUtils.isNotEmpty(processOrderDescPoList)) {
            processOrderDescDao.removeBatchByIds(processOrderDescPoList);
        }

        List<ProcessOrderSamplePo> processOrderSamplePoList = processOrderSampleDao.getByProcessOrderNo(processOrderNo);
        if (CollectionUtils.isNotEmpty(processOrderSamplePoList)) {
            processOrderSampleDao.removeBatchByIds(processOrderSamplePoList);
        }

        // 创建加工单原料
        this.createProcessOrderMaterials(processOrderNo, dto);

        // 创建加工工序
        this.createProcessOrderProcedures(processOrderNo, dto);

        // 创建加工描述
        this.createProcessOrderDesc(processOrderNo, dto);

        // 创建生产信息
        this.createProcessOrderSamples(processOrderNo, processOrderSampleBoList);

    }

    /**
     * 创建加工单成品
     *
     * @param processOrderNo
     * @param dto
     */
    public void createProcessOrderItems(String processOrderNo, ProcessOrderCreateDto dto) {
        List<ProcessOrderCreateDto.ProcessOrderItem> processOrderItems = dto.getProcessOrderItems();
        // 处理加工明细
        List<ProcessOrderItemPo> processOrderItemPos = processOrderItems.stream().map((item) -> {
            ProcessOrderItemPo processOrderItemPo = new ProcessOrderItemPo();
            processOrderItemPo.setProcessOrderNo(processOrderNo);
            processOrderItemPo.setProcessNum(item.getProcessNum());
            processOrderItemPo.setSku(item.getSku());
            processOrderItemPo.setVariantProperties(item.getVariantProperties());
            processOrderItemPo.setPurchasePrice(item.getPurchasePrice());
            processOrderItemPo.setIsFirst(BooleanType.TRUE);
            return processOrderItemPo;
        }).collect(Collectors.toList());
        processOrderItemDao.insertBatch(processOrderItemPos);
    }


    /**
     * 创建加工单原料
     *
     * @param processOrderNo
     * @param dto
     */
    public void createProcessOrderMaterials(String processOrderNo, ProcessOrderCreateDto dto) {
        List<ProcessOrderCreateDto.ProcessOrderMaterial> processOrderMaterials = dto.getProcessOrderMaterials();
        if (CollectionUtils.isNotEmpty(processOrderMaterials)) {
            if (StringUtils.isBlank(dto.getDeliveryWarehouseCode()) || StringUtils.isBlank(dto.getDeliveryWarehouseName())) {
                throw new ParamIllegalException("原料发货仓库必填");
            }

            List<String> skus = processOrderMaterials.stream().map(ProcessOrderCreateDto.ProcessOrderMaterial::getSku)
                    .distinct()
                    .collect(Collectors.toList());
            Map<String, Integer> groupedMaterials = processOrderMaterials.stream()
                    .collect(Collectors.groupingBy(ProcessOrderCreateDto.ProcessOrderMaterial::getSku,
                            Collectors.summingInt(ProcessOrderCreateDto.ProcessOrderMaterial::getDeliveryNum)));

            Map<String, List<ProcessOrderMaterialCompareDto>> materialSkuCompareMap = new HashMap<>();
            for (ProcessOrderCreateDto.ProcessOrderMaterial processOrderMaterial : processOrderMaterials) {
                String sku = processOrderMaterial.getSku();
                List<ProcessOrderMaterialCompareDto> materialCompareDtoList = processOrderMaterial.getMaterialCompareDtoList();
                if (CollectionUtils.isEmpty(materialCompareDtoList)) {
                    continue;
                }

                // 检查 map 中是否已有该 sku，如果有则叠加 list，没有则直接添加
                if (materialSkuCompareMap.containsKey(sku)) {
                    materialSkuCompareMap.get(sku).addAll(materialCompareDtoList);
                } else {
                    materialSkuCompareMap.put(sku, materialCompareDtoList);
                }
            }

            for (String sku : skus) {
                ProcessOrderMaterialPo processOrderMaterialPo = new ProcessOrderMaterialPo();
                processOrderMaterialPo.setProcessOrderNo(processOrderNo);
                processOrderMaterialPo.setSku(sku);
                int totalDeliveryNum = groupedMaterials.get(sku);
                processOrderMaterialPo.setDeliveryNum(totalDeliveryNum);
                processOrderMaterialPo.setCreateType(CreateType.CREATE);
                processOrderMaterialDao.insert(processOrderMaterialPo);

                List<ProcessOrderMaterialCompareDto> procMaterialCompareDtoList = materialSkuCompareMap.get(sku);
                if (CollectionUtils.isNotEmpty(procMaterialCompareDtoList)) {
                    Long procMaterialId = processOrderMaterialPo.getProcessOrderMaterialId();
                    List<ProcessOrderMaterialComparePo> procMaterialComparePoList
                            = ProcessOrderBuilder.buildProcessOrderMaterialComparePoList(procMaterialId, procMaterialCompareDtoList);
                    prodMaterialCompareDao.insertBatch(procMaterialComparePoList);
                }
            }
        }
    }

    /**
     * 创建加工工序
     *
     * @param processOrderNo
     * @param dto
     */
    public void createProcessOrderProcedures(String processOrderNo, ProcessOrderCreateDto dto) {
        List<ProcessOrderCreateDto.ProcessOrderProcedure> processOrderProcedures = dto.getProcessOrderProcedures();
        if (CollectionUtils.isNotEmpty(processOrderProcedures)) {
            List<Long> processIds = processOrderProcedures.stream().map(ProcessOrderCreateDto.ProcessOrderProcedure::getProcessId).collect(Collectors.toList());
            Map<Long, List<ProcessPo>> groupedProcessPos = processDao.getByProcessIds(processIds).stream().collect(Collectors.groupingBy(ProcessPo::getProcessId));
            if (null == groupedProcessPos) {
                throw new BizException("工序选择错误");
            }
            processOrderProcedures.forEach((item) -> {
                List<ProcessPo> processPos = groupedProcessPos.get(item.getProcessId());
                if (CollectionUtils.isEmpty(processPos)) {
                    throw new BizException("工序选择错误");
                }
            });

            List<ProcessOrderProcedurePo> processOrderProcedurePos = processOrderProcedures.stream().map((item) -> {
                List<ProcessPo> processPos = groupedProcessPos.get(item.getProcessId());

                ProcessOrderProcedurePo processOrderProcedurePo = new ProcessOrderProcedurePo();
                ProcessPo processPo = processPos.get(0);
                if (ProcessStatus.DISABLED.equals(processPo.getProcessStatus())) {
                    throw new ParamIllegalException("工序：{},已被禁用", processPo.getProcessName());
                }
                processOrderProcedurePo.setProcessOrderNo(processOrderNo);
                processOrderProcedurePo.setProcessId(processPo.getProcessId());
                processOrderProcedurePo.setProcessCode(processPo.getProcessCode());
                processOrderProcedurePo.setProcessName(processPo.getProcessName());
                processOrderProcedurePo.setSort(item.getSort());
                processOrderProcedurePo.setProcessLabel(processPo.getProcessLabel());
                processOrderProcedurePo.setCommission(item.getCommission());

                return processOrderProcedurePo;
            }).collect(Collectors.toList());
            processOrderProcedureDao.insertBatch(processOrderProcedurePos);
        }
    }

    /**
     * 创建加工描述
     *
     * @param processOrderNo
     * @param dto
     */
    public void createProcessOrderDesc(String processOrderNo, ProcessOrderCreateDto dto) {
        List<ProcessOrderCreateDto.ProcessOrderDesc> processOrderDescs = dto.getProcessOrderDescs();
        if (CollectionUtils.isNotEmpty(processOrderDescs)) {
            List<ProcessOrderDescPo> processOrderDescPos = processOrderDescs.stream().map((item) -> {
                ProcessOrderDescPo processOrderDescPo = new ProcessOrderDescPo();
                processOrderDescPo.setProcessOrderNo(processOrderNo);
                processOrderDescPo.setProcessDescName(item.getProcessDescName());
                processOrderDescPo.setProcessDescValue(item.getProcessDescValue());
                return processOrderDescPo;
            }).collect(Collectors.toList());
            processOrderDescDao.insertBatch(processOrderDescPos);
        }
    }

    /**
     * 创建加工生产信息
     *
     * @param processOrderNo
     * @param processOrderSampleBoList
     */
    public void createProcessOrderSamples(String processOrderNo, List<ProcessOrderSampleBo> processOrderSampleBoList) {
        if (CollectionUtils.isEmpty(processOrderSampleBoList)) {
            return;
        }
        // 处理加工生产信息
        List<ProcessOrderSamplePo> processOrderSamplePoList = processOrderSampleBoList.stream().map((item) -> {
            ProcessOrderSamplePo processOrderSamplePo = new ProcessOrderSamplePo();
            processOrderSamplePo.setProcessOrderNo(processOrderNo);
            processOrderSamplePo.setSampleChildOrderNo(item.getSampleChildOrderNo());
            processOrderSamplePo.setSourceDocumentNumber(item.getSourceDocumentNumber());
            processOrderSamplePo.setSampleInfoKey(item.getSampleInfoKey());
            processOrderSamplePo.setSampleInfoValue(item.getSampleInfoValue());
            return processOrderSamplePo;
        }).collect(Collectors.toList());
        processOrderSampleDao.insertBatch(processOrderSamplePoList);
    }


    /**
     * 更新批次码信息
     *
     * @param processOrderItemPoList
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateProcessOrderSkuBatchCode(List<ProcessOrderItemPo> processOrderItemPoList) {
        if (CollectionUtils.isNotEmpty(processOrderItemPoList)) {
            processOrderItemDao.updateBatchByIdVersion(processOrderItemPoList);
        }
    }

    /**
     * 补充原料
     *
     * @param processOrderMaterialPoList
     */
    @Transactional(rollbackFor = Exception.class)
    public void addProcessOrderMaterials(List<ProcessOrderMaterialPo> processOrderMaterialPoList,
                                         ProcessMaterialDetailPo processMaterialDetailPo) {
        Map<String, Integer> groupedMaterials = processOrderMaterialPoList.stream()
                .collect(Collectors.groupingBy(processOrderMaterialPo -> processOrderMaterialPo.getSku() + processOrderMaterialPo.getSkuBatchCode(),
                        Collectors.summingInt(ProcessOrderMaterialPo::getDeliveryNum)));

        List<ProcessOrderMaterialPo> processOrderMaterialPos = processOrderMaterialPoList.stream()
                .map(item -> {
                    ProcessOrderMaterialPo processOrderMaterialPo = new ProcessOrderMaterialPo();
                    processOrderMaterialPo.setProcessOrderNo(item.getProcessOrderNo());
                    processOrderMaterialPo.setDeliveryNo(item.getDeliveryNo());
                    processOrderMaterialPo.setSku(item.getSku());
                    processOrderMaterialPo.setSkuBatchCode(item.getSkuBatchCode());
                    int totalDeliveryNum = groupedMaterials.get(item.getSku() + item.getSkuBatchCode());
                    processOrderMaterialPo.setDeliveryNum(totalDeliveryNum);
                    processOrderMaterialPo.setCreateType(CreateType.CREATE);
                    return processOrderMaterialPo;
                }).collect(Collectors.toList());

        processOrderMaterialDao.insertBatch(processOrderMaterialPos);

        processMaterialDetailDao.insert(processMaterialDetailPo);
    }

}
