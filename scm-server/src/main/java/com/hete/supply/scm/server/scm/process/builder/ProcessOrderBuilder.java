package com.hete.supply.scm.server.scm.process.builder;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.plm.api.goods.entity.vo.PlmSkuVo;
import com.hete.supply.scm.api.scm.entity.dto.SkuExistenceResponseDto;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderMaterialCompareVo;
import com.hete.supply.scm.server.scm.entity.bo.MaterialInventoryCheckBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessOrderMaterialCompareBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProduceDataItemRawCompareBo;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderDefectiveHandleDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderMaterialCompareDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderReWorkingDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderMaterialComparePo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderMaterialPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderPo;
import com.hete.supply.wms.api.WmsEnum;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/4/23.
 */
public class ProcessOrderBuilder {

    /**
     * 根据SKU列表构建SkuExistenceResponseDto列表
     *
     * @param skuList SKU列表
     * @return 包含SKU的SkuExistenceResponseDto列表
     */
    public static List<SkuExistenceResponseDto> buildSkuExistenceResponseDto(Set<String> skuList) {
        return skuList.stream()
                .map(sku -> {
                    SkuExistenceResponseDto dto = new SkuExistenceResponseDto();
                    dto.setSku(sku);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public static ProcessOrderReWorkingDto buildProcessOrderReWorkingDto(ProcessOrderDefectiveHandleDto dto, ProcessOrderPo processOrderPo) {
        ProcessOrderReWorkingDto processOrderReWorkingDto = new ProcessOrderReWorkingDto();

        processOrderReWorkingDto.setParentProcessOrderNo(processOrderPo.getProcessOrderNo());
        processOrderReWorkingDto.setWarehouseCode(dto.getWarehouseCode());
        processOrderReWorkingDto.setWarehouseName(dto.getWarehouseName());
        processOrderReWorkingDto.setWarehouseTypeList(dto.getWarehouseTypeList());
        processOrderReWorkingDto.setDeliveryWarehouseCode(dto.getDeliveryWarehouseCode());
        processOrderReWorkingDto.setDeliveryWarehouseName(dto.getDeliveryWarehouseName());
        processOrderReWorkingDto.setProductQuality(dto.getProductQuality());
        processOrderReWorkingDto.setDeliverDate(dto.getDeliverDate());
        processOrderReWorkingDto.setProcessOrderNote(dto.getProcessOrderNote());
        processOrderReWorkingDto.setProcessNum(dto.getDefectiveGoodsCnt());

        // 原料信息
        List<ProcessOrderDefectiveHandleDto.ProcessOrderMaterial> pomDtoList = dto.getProcessOrderMaterials();
        if (CollectionUtils.isNotEmpty(pomDtoList)) {
            List<ProcessOrderReWorkingDto.ProcessOrderMaterial> processOrderMaterials = pomDtoList.stream().map(pom -> {
                ProcessOrderReWorkingDto.ProcessOrderMaterial material = new ProcessOrderReWorkingDto.ProcessOrderMaterial();
                material.setSku(pom.getSku());
                material.setDeliveryNum(pom.getDeliveryNum());
                return material;
            }).collect(Collectors.toList());
            processOrderReWorkingDto.setProcessOrderMaterials(processOrderMaterials);
        }

        // 工序信息
        List<ProcessOrderDefectiveHandleDto.ProcessOrderProcedure> popDtoList = dto.getProcessOrderProcedures();
        if (CollectionUtils.isNotEmpty(popDtoList)) {
            List<ProcessOrderReWorkingDto.ProcessOrderProcedure> processOrderProcedures = popDtoList.stream().map(popDto -> {
                ProcessOrderReWorkingDto.ProcessOrderProcedure pop = new ProcessOrderReWorkingDto.ProcessOrderProcedure();
                pop.setProcessId(popDto.getProcessId());
                pop.setCommission(popDto.getCommission());
                pop.setSort(popDto.getSort());
                return pop;
            }).collect(Collectors.toList());
            processOrderReWorkingDto.setProcessOrderProcedures(processOrderProcedures);
        }

        return processOrderReWorkingDto;
    }

    public static List<ProcessOrderMaterialCompareBo> buildProduceDataItemRawCompareBoList(List<ProduceDataItemRawCompareBo> produceDataItemRawCompareBoList) {
        if (CollectionUtils.isEmpty(produceDataItemRawCompareBoList)) {
            return Collections.emptyList();
        }
        return produceDataItemRawCompareBoList.stream().map(produceDataItemRawCompareBo -> {
            ProcessOrderMaterialCompareBo processOrderMaterialCompareBo = new ProcessOrderMaterialCompareBo();
            processOrderMaterialCompareBo.setSku(produceDataItemRawCompareBo.getSku());
            processOrderMaterialCompareBo.setSkuEncode(produceDataItemRawCompareBo.getSkuEncode());
            processOrderMaterialCompareBo.setQuantity(produceDataItemRawCompareBo.getQuantity());
            return processOrderMaterialCompareBo;
        }).collect(Collectors.toList());
    }

    public static List<ProcessOrderMaterialCompareVo> buildProcessOrderMaterialCompareVoList(List<ProcessOrderMaterialCompareBo> procMaterialCompareBoList) {
        if (CollectionUtils.isEmpty(procMaterialCompareBoList)) {
            return Collections.emptyList();
        }
        return procMaterialCompareBoList.stream().map(procMaterialCompareBo -> {
            ProcessOrderMaterialCompareVo processOrderMaterialCompareVo = new ProcessOrderMaterialCompareVo();
            processOrderMaterialCompareVo.setSku(procMaterialCompareBo.getSku());
            processOrderMaterialCompareVo.setSkuEncode(procMaterialCompareBo.getSkuEncode());
            processOrderMaterialCompareVo.setQuantity(procMaterialCompareBo.getQuantity());
            return processOrderMaterialCompareVo;
        }).collect(Collectors.toList());
    }

    public static List<ProcessOrderMaterialCompareVo> buildProcessOrderMaterialCompareVoList(List<ProcessOrderMaterialComparePo> matchSkuComparePoList, Map<String, List<PlmSkuVo>> groupedPlmSkuMap) {
        if (CollectionUtils.isEmpty(matchSkuComparePoList)) {
            return Collections.emptyList();
        }
        return matchSkuComparePoList.stream().map(matchSkuComparePo -> {
            ProcessOrderMaterialCompareVo processOrderMaterialCompareVo = new ProcessOrderMaterialCompareVo();

            String sku = matchSkuComparePo.getSku();
            processOrderMaterialCompareVo.setSku(sku);
            List<PlmSkuVo> plmSkuVos = groupedPlmSkuMap.get(sku);
            if (CollectionUtils.isNotEmpty(plmSkuVos)) {
                Optional<PlmSkuVo> firstPlmSkuVoOptional = plmSkuVos.stream().findFirst();
                firstPlmSkuVoOptional.ifPresent(plmSkuVo -> processOrderMaterialCompareVo.setSkuEncode(plmSkuVo.getSkuEncode()));
            }
            processOrderMaterialCompareVo.setQuantity(matchSkuComparePo.getQuantity());
            return processOrderMaterialCompareVo;
        }).collect(Collectors.toList());
    }

    public static List<ProcessOrderMaterialCompareDto> buildProcessOrderMaterialCompareDtoList(List<ProcessOrderMaterialCompareBo> procMaterialCompareBoList) {
        if (CollectionUtils.isEmpty(procMaterialCompareBoList)) {
            return Collections.emptyList();
        }
        return procMaterialCompareBoList.stream().map(procMaterialCompareBo -> {
            ProcessOrderMaterialCompareDto processOrderMaterialCompareDto = new ProcessOrderMaterialCompareDto();
            processOrderMaterialCompareDto.setSku(procMaterialCompareBo.getSku());
            processOrderMaterialCompareDto.setQuantity(procMaterialCompareBo.getQuantity());
            return processOrderMaterialCompareDto;
        }).collect(Collectors.toList());
    }

    public static List<ProcessOrderMaterialComparePo> buildProcessOrderMaterialComparePoList(Long procMaterialId,
                                                                                             List<ProcessOrderMaterialCompareDto> procMaterialCompareDtoList) {
        if (CollectionUtils.isEmpty(procMaterialCompareDtoList)) {
            return Collections.emptyList();
        }

        return procMaterialCompareDtoList.stream().map(procMaterialCompareDto -> {
            ProcessOrderMaterialComparePo processOrderMaterialComparePo = new ProcessOrderMaterialComparePo();
            processOrderMaterialComparePo.setProcessOrderMaterialId(procMaterialId);
            processOrderMaterialComparePo.setSku(procMaterialCompareDto.getSku());
            processOrderMaterialComparePo.setQuantity(procMaterialCompareDto.getQuantity());
            return processOrderMaterialComparePo;
        }).collect(Collectors.toList());
    }

    public static List<MaterialInventoryCheckBo> buildMaterialInventoryCheckBoList(WmsEnum.ProductQuality productQuality,
                                                                                   List<ProcessOrderMaterialPo> processOrderMaterialPos) {
        if (CollectionUtils.isEmpty(processOrderMaterialPos)) {
            return Collections.emptyList();
        }
        return processOrderMaterialPos.stream().map(processOrderMaterial -> {
            MaterialInventoryCheckBo materialInventoryCheckBo = new MaterialInventoryCheckBo();
            materialInventoryCheckBo.setProductQuality(productQuality);
            materialInventoryCheckBo.setMaterialSku(processOrderMaterial.getSku());
            materialInventoryCheckBo.setRequiredInventory(processOrderMaterial.getDeliveryNum());
            if (StrUtil.isNotBlank(processOrderMaterial.getSkuBatchCode())) {
                materialInventoryCheckBo.setBatchCode(processOrderMaterial.getSkuBatchCode());
            }
            return materialInventoryCheckBo;
        }).collect(Collectors.toList());
    }
}
