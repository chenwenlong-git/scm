package com.hete.supply.scm.server.scm.process.converter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.plm.api.developorder.enums.MaterialType;
import com.hete.supply.scm.server.scm.entity.bo.*;
import com.hete.supply.scm.server.scm.process.builder.ProcessOrderBuilder;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessOrderMaterialCompareBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessOrderProductionInfoBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessOrderSampleBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProduceDataItemRawCompareBo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessPo;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderMaterialCompareVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderProductionInfoVo;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2023/10/16.
 */
public class ProcessOrderProductionConverter {


    public static ProcessOrderProductionInfoVo toVo(ProcessOrderProductionInfoBo processOrderGenerateInfoBo) {
        if (Objects.isNull(processOrderGenerateInfoBo)) {
            return null;
        }

        final List<ProcessOrderProductionInfoBo.ProcessOrderMaterialBo> processOrderMaterialBoList = processOrderGenerateInfoBo.getProcessOrderMaterialBoList();
        final List<ProcessOrderProductionInfoBo.ProcessOrderProcedureBo> processOrderProcedureBoList = processOrderGenerateInfoBo.getProcessOrderProcedureBoList();
        final List<ProcessOrderProductionInfoBo.ProcessOrderDescBo> processOrderDescBoList = processOrderGenerateInfoBo.getProcessOrderDescBoList();
        final List<String> fileCodeList = processOrderGenerateInfoBo.getFileCodeList();

        ProcessOrderProductionInfoVo processOrderProductionInfoVo = new ProcessOrderProductionInfoVo();
        processOrderProductionInfoVo.setFileCodeList(fileCodeList);

        List<ProcessOrderProductionInfoVo.ProcessOrderMaterialVo> processOrderMaterialVoList = processOrderMaterialBoList.stream().map(material -> {
            ProcessOrderProductionInfoVo.ProcessOrderMaterialVo processOrderMaterialVo = new ProcessOrderProductionInfoVo.ProcessOrderMaterialVo();
            processOrderMaterialVo.setSku(material.getSku());
            processOrderMaterialVo.setDeliveryNum(material.getDeliveryNum());
            processOrderMaterialVo.setMaterialSkuType(material.getMaterialSkuType());

            List<ProcessOrderMaterialCompareBo> procMaterialCompareBoList = material.getProcMaterialCompareBoList();
            List<ProcessOrderMaterialCompareVo> procMaterialCompareVos = ProcessOrderBuilder.buildProcessOrderMaterialCompareVoList(procMaterialCompareBoList);
            processOrderMaterialVo.setProcessOrderMaterialCompareVoList(procMaterialCompareVos);
            return processOrderMaterialVo;
        }).collect(Collectors.toList());
        processOrderProductionInfoVo.setProcessOrderMaterialVoList(processOrderMaterialVoList);

        List<ProcessOrderProductionInfoVo.ProcessOrderProcedureVo> processOrderProcedureVoList = processOrderProcedureBoList.stream().map(procedure -> {
            ProcessOrderProductionInfoVo.ProcessOrderProcedureVo processOrderProcedureVo = new ProcessOrderProductionInfoVo.ProcessOrderProcedureVo();
            processOrderProcedureVo.setProcessId(procedure.getProcessId());
            processOrderProcedureVo.setSort(procedure.getSort());
            processOrderProcedureVo.setCommission(procedure.getCommission());
            processOrderProcedureVo.setProcessSecondCode(procedure.getProcessSecondCode());
            processOrderProcedureVo.setProcessLabel(procedure.getProcessLabel());
            return processOrderProcedureVo;
        }).collect(Collectors.toList());
        processOrderProductionInfoVo.setProcessOrderProcedureVoList(processOrderProcedureVoList);

        List<ProcessOrderProductionInfoVo.ProcessOrderDescVo> processOrderDescVoList = processOrderDescBoList.stream().map(desc -> {
            ProcessOrderProductionInfoVo.ProcessOrderDescVo processOrderDescVo = new ProcessOrderProductionInfoVo.ProcessOrderDescVo();
            processOrderDescVo.setProcessDescName(desc.getProcessDescName());
            processOrderDescVo.setProcessDescValue(desc.getProcessDescValue());
            return processOrderDescVo;
        }).collect(Collectors.toList());
        processOrderProductionInfoVo.setProcessOrderDescVoList(processOrderDescVoList);

        return processOrderProductionInfoVo;
    }

    public static List<ProcessOrderProductionInfoBo> toProcessOrderProductionInfoBos(List<ProduceDataDetailBo> skuProduceDataList, List<ProcessPo> processPos) {
        if (CollectionUtils.isEmpty(skuProduceDataList)) {
            return Collections.emptyList();
        }

        final ProduceDataDetailBo skuProduceData = skuProduceDataList.stream().findFirst().orElse(null);
        if (Objects.isNull(skuProduceData)) {
            return Collections.emptyList();
        }

        final String processSku = skuProduceData.getSku();
        final List<ProduceDataItemBo> bomList = skuProduceData.getProduceDataItemBoList();
        // 生产属性
        final List<ProduceDataAttrBo> produceDataAttrBoList = skuProduceData.getProduceDataAttrBoList();
        if (CollectionUtils.isEmpty(bomList)) {
            return Collections.singletonList(new ProcessOrderProductionInfoBo() {{
                setProcessSku(processSku);
            }});
        }

        // spu生产主图
        final List<String> spuFileCodeList = skuProduceData.getSpuFileCodeList();

        return bomList.stream().map(bomInfo -> {
            final Long produceDataItemId = bomInfo.getProduceDataItemId();
            // 参考图
            final List<String> effectFileCodeList = bomInfo.getEffectFileCodeList();
            ProcessOrderProductionInfoBo processOrderProductionInfoBo = new ProcessOrderProductionInfoBo();
            processOrderProductionInfoBo.setBomId(produceDataItemId);

            // 加工单主图 = 参考图 -> spu主图
            processOrderProductionInfoBo.setFileCodeList(CollectionUtils.isNotEmpty(effectFileCodeList) ? effectFileCodeList : spuFileCodeList);

            // 加工原料
            List<ProduceDataItemRawListBo> produceDataItemRawBoList = bomInfo.getProduceDataItemRawBoList();
            List<ProcessOrderProductionInfoBo.ProcessOrderMaterialBo> processOrderMaterialBoList = produceDataItemRawBoList.stream().map(material -> {
                ProcessOrderProductionInfoBo.ProcessOrderMaterialBo processOrderMaterialBo = new ProcessOrderProductionInfoBo.ProcessOrderMaterialBo();

                String materialSku = material.getSku();
                processOrderMaterialBo.setSku(materialSku);

                MaterialType materialType = material.getMaterialType();
                processOrderMaterialBo.setMaterialSkuType(materialType);

                Integer skuCnt = material.getSkuCnt();
                processOrderMaterialBo.setDeliveryNum(skuCnt);

                //加工单原料对照关系
                List<ProduceDataItemRawCompareBo> prodRawCompareBoList = material.getProduceDataItemRawCompareBoList();
                List<ProcessOrderMaterialCompareBo> procMaterialCompareBoList
                        = ProcessOrderBuilder.buildProduceDataItemRawCompareBoList(prodRawCompareBoList);
                processOrderMaterialBo.setProcMaterialCompareBoList(procMaterialCompareBoList);
                return processOrderMaterialBo;
            }).collect(Collectors.toList());
            processOrderProductionInfoBo.setProcessOrderMaterialBoList(processOrderMaterialBoList);

            // 加工工序
            final List<ProduceDataItemProcessListBo> produceDataItemProcessBoList = bomInfo.getProduceDataItemProcessBoList();
            AtomicInteger sort = new AtomicInteger(0);
            List<ProcessOrderProductionInfoBo.ProcessOrderProcedureBo> processOrderProcedureBoList = produceDataItemProcessBoList.stream().map(procedure -> {
                final String processCode = procedure.getProcessCode();
                ProcessPo matchProcess
                        = processPos.stream().filter(processPo -> Objects.equals(processCode, processPo.getProcessCode())).findFirst().orElse(null);
                if (Objects.isNull(matchProcess)) {
                    return null;
                }

                ProcessOrderProductionInfoBo.ProcessOrderProcedureBo processOrderProcedureBo = new ProcessOrderProductionInfoBo.ProcessOrderProcedureBo();
                // 设置工序ID
                processOrderProcedureBo.setProcessId(matchProcess.getProcessId());
                // 设置工序排序
                processOrderProcedureBo.setSort(sort.getAndIncrement());
                // 设置人工提成
                processOrderProcedureBo.setCommission(matchProcess.getCommission());
                // 设置工序代码
                processOrderProcedureBo.setProcessCode(processCode);
                processOrderProcedureBo.setProcessSecondCode(matchProcess.getProcessSecondCode());
                processOrderProcedureBo.setProcessLabel(matchProcess.getProcessLabel());
                return processOrderProcedureBo;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            processOrderProductionInfoBo.setProcessOrderProcedureBoList(processOrderProcedureBoList);

            // 加工描述
            final List<ProduceDataItemProcessDescListBo> produceDataItemProcessDescBoList = bomInfo.getProduceDataItemProcessDescBoList();
            List<ProcessOrderProductionInfoBo.ProcessOrderDescBo> processOrderDescBoList = produceDataItemProcessDescBoList.stream().map(desc -> {
                final String name = desc.getName();
                final String descValue = desc.getDescValue();

                ProcessOrderProductionInfoBo.ProcessOrderDescBo processOrderDescBo = new ProcessOrderProductionInfoBo.ProcessOrderDescBo();
                processOrderDescBo.setProcessDescName(name);
                processOrderDescBo.setProcessDescValue(descValue);
                return processOrderDescBo;
            }).collect(Collectors.toList());
            processOrderProductionInfoBo.setProcessOrderDescBoList(processOrderDescBoList);

            // 生产信息
            List<ProcessOrderSampleBo> processOrderSampleBoList = ProcessOrderSampleConverter.toProcessOrderSampleBos(produceDataAttrBoList);
            processOrderProductionInfoBo.setProcessOrderSampleBoList(processOrderSampleBoList);
            return processOrderProductionInfoBo;
        }).collect(Collectors.toList());
    }
}
