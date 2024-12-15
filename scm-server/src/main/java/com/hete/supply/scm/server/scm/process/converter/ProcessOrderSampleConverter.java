package com.hete.supply.scm.server.scm.process.converter;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.hete.supply.scm.api.scm.entity.vo.SampleChildOrderInfoVo;
import com.hete.supply.scm.server.scm.entity.bo.ProduceDataAttrBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessOrderSampleBo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderSamplePo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2023/10/24.
 */
public class ProcessOrderSampleConverter {

    public static List<SampleChildOrderInfoVo> toSampleChildOrderInfoVo(List<ProcessOrderSamplePo> processOrderSamplePoList) {
        if (CollectionUtils.isEmpty(processOrderSamplePoList)) {
            return Collections.emptyList();
        }

        final List<SampleChildOrderInfoVo> voList = Lists.newArrayList();
        for (ProcessOrderSamplePo processOrderSamplePo : processOrderSamplePoList) {
            final String sampleInfoValuesStr = processOrderSamplePo.getSampleInfoValue();
            final List<String> sampleInfoValues = Arrays.stream(sampleInfoValuesStr.split(",")).map(String::trim)
                    .filter(StrUtil::isNotBlank).collect(Collectors.toList());

            // 无属性信息
            if (CollectionUtils.isEmpty(sampleInfoValues)) {
                SampleChildOrderInfoVo sampleChildOrderInfoVo = new SampleChildOrderInfoVo();
                sampleChildOrderInfoVo.setSampleChildOrderNo(processOrderSamplePo.getSampleChildOrderNo());
                sampleChildOrderInfoVo.setSampleInfoKey(processOrderSamplePo.getSampleInfoKey());
                voList.add(sampleChildOrderInfoVo);
                continue;
            }

            // 存在一条或者多条属性信息
            for (String sampleInfoValue : sampleInfoValues) {
                SampleChildOrderInfoVo sampleChildOrderInfoVo = new SampleChildOrderInfoVo();
                sampleChildOrderInfoVo.setSampleChildOrderNo(processOrderSamplePo.getSampleChildOrderNo());
                sampleChildOrderInfoVo.setSampleInfoKey(processOrderSamplePo.getSampleInfoKey());
                sampleChildOrderInfoVo.setSampleInfoValue(sampleInfoValue);
                voList.add(sampleChildOrderInfoVo);
            }
        }
        return voList;
    }

    public static List<ProcessOrderSampleBo> toProcessOrderSampleBos(List<ProduceDataAttrBo> produceDataAttrBoList) {
        if (CollectionUtils.isEmpty(produceDataAttrBoList)) {
            return Collections.emptyList();
        }

        final Map<Long, List<ProduceDataAttrBo>> groupByAttributeNameId = produceDataAttrBoList.stream()
                .collect(Collectors.groupingBy(ProduceDataAttrBo::getAttributeNameId));
        final List<ProcessOrderSampleBo> mergedList = Lists.newArrayList();

        for (Map.Entry<Long, List<ProduceDataAttrBo>> entry : groupByAttributeNameId.entrySet()) {
            final Long attributeNameId = entry.getKey();
            final List<ProduceDataAttrBo> attributeList = entry.getValue();

            // 属性名称 = 分组后任意一条（分组后属性名称均相同）
            final String sampleInfoKey = Objects.nonNull(attributeList.stream().findFirst().orElse(null)) ?
                    attributeList.stream().findFirst().get().getAttrName() : "";
            // 属性值 = 分组后将属性值通过逗号分隔
            final String sampleInfoValue = attributeList.stream().map(ProduceDataAttrBo::getAttrValue)
                    .filter(StrUtil::isNotBlank).collect(Collectors.joining(","));

            ProcessOrderSampleBo mergedBo = new ProcessOrderSampleBo();
            mergedBo.setSourceDocumentNumber(Objects.nonNull(attributeNameId) ? String.valueOf(attributeNameId) : "0");
            mergedBo.setSampleInfoKey(sampleInfoKey);
            mergedBo.setSampleInfoValue(sampleInfoValue);
            mergedList.add(mergedBo);
        }
        return mergedList;
    }


}
