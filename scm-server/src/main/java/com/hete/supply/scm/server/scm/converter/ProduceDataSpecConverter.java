package com.hete.supply.scm.server.scm.converter;

import com.hete.supply.scm.server.scm.entity.dto.ProduceDataSpecDto;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataPo;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataSpecPo;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataSpecSupplierPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProduceDataSpecBatchVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProduceDataSpecSupplierVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProduceDataSpecVo;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2023/11/1 11:25
 */
@Slf4j
public class ProduceDataSpecConverter {

    public static List<ProduceDataSpecVo> specPoToVo(List<ProduceDataSpecPo> poList,
                                                     Map<Long, List<String>> fileCodeMap,
                                                     List<ProduceDataSpecSupplierPo> produceDataSpecSupplierPoList) {
        return Optional.ofNullable(poList)
                .orElse(Collections.emptyList())
                .stream()
                .map(po -> {
                    final ProduceDataSpecVo produceDataSpecVo = new ProduceDataSpecVo();
                    produceDataSpecVo.setProduceDataSpecId(po.getProduceDataSpecId());
                    produceDataSpecVo.setVersion(po.getVersion());
                    produceDataSpecVo.setSku(po.getSku());
                    produceDataSpecVo.setProductLink(po.getProductLink());
                    produceDataSpecVo.setProductFileCode(fileCodeMap.get(po.getProduceDataSpecId()));
                    produceDataSpecVo.setCreateUsername(po.getCreateUsername());
                    produceDataSpecVo.setCreateTime(po.getCreateTime());
                    produceDataSpecVo.setUpdateUsername(po.getUpdateUsername());
                    produceDataSpecVo.setUpdateTime(po.getUpdateTime());

                    List<ProduceDataSpecSupplierVo> produceDataSpecSupplierVoList = produceDataSpecSupplierPoList.stream()
                            .filter(produceDataSpecSupplierPo -> po.getProduceDataSpecId().equals(produceDataSpecSupplierPo.getProduceDataSpecId()))
                            .map(produceDataSpecSupplierPo -> {
                                ProduceDataSpecSupplierVo produceDataSpecSupplierVo = new ProduceDataSpecSupplierVo();
                                produceDataSpecSupplierVo.setSupplierCode(produceDataSpecSupplierPo.getSupplierCode());
                                produceDataSpecSupplierVo.setSupplierName(produceDataSpecSupplierPo.getSupplierName());
                                return produceDataSpecSupplierVo;
                            }).collect(Collectors.toList());
                    produceDataSpecVo.setProduceDataSpecSupplierList(produceDataSpecSupplierVoList);

                    return produceDataSpecVo;
                }).collect(Collectors.toList());
    }

    public static List<ProduceDataSpecBatchVo> specPoToBatchVo(List<String> skuList,
                                                               List<ProduceDataPo> produceDataPoList,
                                                               Map<String, List<ProduceDataSpecPo>> produceDataSpecPoMap,
                                                               Map<Long, List<String>> fileCodeMap,
                                                               Map<Long, List<String>> productFileCodeMap,
                                                               List<ProduceDataSpecSupplierPo> produceDataSpecSupplierPoList) {
        return Optional.ofNullable(skuList)
                .orElse(Collections.emptyList())
                .stream()
                .map(sku -> {
                    final ProduceDataSpecBatchVo produceDataSpecBatchVo = new ProduceDataSpecBatchVo();
                    ProduceDataPo produceDataPo = produceDataPoList.stream()
                            .filter(po -> sku.equals(po.getSku()))
                            .findFirst()
                            .orElse(null);
                    produceDataSpecBatchVo.setSku(sku);
                    if (null != produceDataPo) {
                        produceDataSpecBatchVo.setSealImageFileCodeList(fileCodeMap.get(produceDataPo.getProduceDataId()));
                    }
                    //规格书信息详情列表
                    List<ProduceDataSpecPo> produceDataSpecPoList = produceDataSpecPoMap.get(sku);
                    List<ProduceDataSpecVo> produceDataSpecList = new ArrayList<>();
                    Optional.ofNullable(produceDataSpecPoList)
                            .orElse(Collections.emptyList())
                            .forEach(produceDataSpecPo -> {
                                final ProduceDataSpecVo produceDataSpecVo = new ProduceDataSpecVo();
                                produceDataSpecVo.setProduceDataSpecId(produceDataSpecPo.getProduceDataSpecId());
                                produceDataSpecVo.setVersion(produceDataSpecPo.getVersion());
                                produceDataSpecVo.setSku(produceDataSpecPo.getSku());
                                produceDataSpecVo.setProductLink(produceDataSpecPo.getProductLink());
                                produceDataSpecVo.setProductFileCode(productFileCodeMap.get(produceDataSpecPo.getProduceDataSpecId()));
                                produceDataSpecVo.setCreateUsername(produceDataSpecPo.getCreateUsername());
                                produceDataSpecVo.setCreateTime(produceDataSpecPo.getCreateTime());
                                produceDataSpecVo.setUpdateUsername(produceDataSpecPo.getUpdateUsername());
                                produceDataSpecVo.setUpdateTime(produceDataSpecPo.getUpdateTime());

                                List<ProduceDataSpecSupplierVo> produceDataSpecSupplierList = produceDataSpecSupplierPoList.stream()
                                        .filter(produceDataSpecSupplierPo -> produceDataSpecPo.getProduceDataSpecId().equals(produceDataSpecSupplierPo.getProduceDataSpecId()))
                                        .map(produceDataSpecSupplierPo -> {
                                            ProduceDataSpecSupplierVo produceDataSpecSupplierVo = new ProduceDataSpecSupplierVo();
                                            produceDataSpecSupplierVo.setSupplierCode(produceDataSpecSupplierPo.getSupplierCode());
                                            produceDataSpecSupplierVo.setSupplierName(produceDataSpecSupplierPo.getSupplierName());
                                            return produceDataSpecSupplierVo;
                                        }).collect(Collectors.toList());
                                produceDataSpecVo.setProduceDataSpecSupplierList(produceDataSpecSupplierList);

                                produceDataSpecList.add(produceDataSpecVo);
                            });
                    produceDataSpecBatchVo.setProduceDataSpecList(produceDataSpecList);

                    return produceDataSpecBatchVo;
                }).collect(Collectors.toList());
    }

    public static List<ProduceDataSpecPo> editDtoToPo(List<ProduceDataSpecDto> dtoList) {
        return Optional.ofNullable(dtoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(dto -> {
                    final ProduceDataSpecPo produceDataSpecPo = new ProduceDataSpecPo();
                    produceDataSpecPo.setProduceDataSpecId(dto.getProduceDataSpecId());
                    produceDataSpecPo.setVersion(dto.getVersion());
                    produceDataSpecPo.setProductLink(dto.getProductLink());
                    return produceDataSpecPo;
                }).collect(Collectors.toList());
    }

}
