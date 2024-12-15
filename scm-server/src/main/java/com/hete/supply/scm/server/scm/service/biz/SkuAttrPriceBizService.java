package com.hete.supply.scm.server.scm.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.plm.api.basedata.entity.vo.PlmAttributeVo;
import com.hete.supply.scm.api.scm.importation.entity.dto.SkuAttrImportationDto;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.dao.SkuAttrPriceDao;
import com.hete.supply.scm.server.scm.entity.bo.ProduceDataAttrPriceUpdateBo;
import com.hete.supply.scm.server.scm.entity.dto.*;
import com.hete.supply.scm.server.scm.entity.po.SkuAttrPricePo;
import com.hete.supply.scm.server.scm.entity.vo.SkuAttrPricePageVo;
import com.hete.supply.scm.server.scm.handler.ProduceDataPriceByAttrHandler;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/9/10 09:38
 */
@RequiredArgsConstructor
@Service
@Slf4j
@Validated
public class SkuAttrPriceBizService {

    private final SkuAttrPriceDao skuAttrPriceDao;
    private final ConsistencyService consistencyService;
    private final PlmRemoteService plmRemoteService;

    public CommonPageResult.PageInfo<SkuAttrPricePageVo> skuAttrPriceList(SkuAttrPricePageDto dto) {
        return skuAttrPriceDao.skuAttrPriceList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delSkuAttrPrice(SkuAttrPriceDelDto dto) {
        skuAttrPriceDao.delByIdList(dto.getSkuAttrPriceIdList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void editSkuAttrPrice(SkuAttrPriceEditDto dto) {
        final List<SkuAttrPriceEditItemDto> skuAttrPriceEditItemList = dto.getSkuAttrPriceEditItemList();

        final List<SkuAttrPriceEditItemDto> addItemDtoList = skuAttrPriceEditItemList.stream()
                .filter(itemDto -> itemDto.getSkuAttrPriceId() == null)
                .peek(itemDto -> {
                    if (StringUtils.isBlank(itemDto.getLaceAttrValue())) {
                        throw new ParamIllegalException("蕾丝面积不能为空，请检查后再提交!");
                    }
                    if (StringUtils.isBlank(itemDto.getSizeAttrValue())) {
                        throw new ParamIllegalException("档长尺寸不能为空，请检查后再提交!");
                    }
                    if (StringUtils.isBlank(itemDto.getMaterialAttrValue())) {
                        throw new ParamIllegalException("材料不能为空，请检查后再提交!");
                    }
                    if (null == itemDto.getSkuPrice()) {
                        throw new ParamIllegalException("档长尺寸不能为空，请检查后再提交!");
                    }
                }).collect(Collectors.toList());

        final Map<String, List<SkuAttrPriceEditItemDto>> uniqueCheckMap = addItemDtoList.stream()
                .collect(Collectors.groupingBy(itemDto -> itemDto.getSizeAttrValue() + itemDto.getLaceAttrValue() + itemDto.getMaterialAttrValue()));
        uniqueCheckMap.forEach((key, valueList) -> {
            if (valueList.size() > 1) {
                final SkuAttrPriceEditItemDto skuAttrPriceEditItemDto = valueList.get(0);
                throw new ParamIllegalException("不允许添加重复的蕾丝面积:{}&档长尺寸:{}&材料:{}配置!",
                        skuAttrPriceEditItemDto.getLaceAttrValue(), skuAttrPriceEditItemDto.getSizeAttrValue(),
                        skuAttrPriceEditItemDto.getMaterialAttrValue());
            }
        });

        final List<SkuAttrPricePo> dbSkuAttrPricePoList = skuAttrPriceDao.getListBySkuAndAttr(addItemDtoList);
        if (CollectionUtils.isNotEmpty(dbSkuAttrPricePoList)) {
            throw new ParamIllegalException("新增的数据已经存在，不允许重复添加！");
        }

        final List<SkuAttrPricePo> skuAttrPricePoList = addItemDtoList.stream()
                .map(itemDto -> {
                    final SkuAttrPricePo skuAttrPricePo = new SkuAttrPricePo();
                    skuAttrPricePo.setSkuPrice(itemDto.getSkuPrice());
                    skuAttrPricePo.setLaceAttrValue(itemDto.getLaceAttrValue());
                    skuAttrPricePo.setSizeAttrValue(itemDto.getSizeAttrValue());
                    skuAttrPricePo.setMaterialAttrValue(itemDto.getMaterialAttrValue());
                    return skuAttrPricePo;
                }).collect(Collectors.toList());
        skuAttrPriceDao.insertBatch(skuAttrPricePoList);

        final List<SkuAttrPricePo> updateSkuAttrPricePoList = skuAttrPriceEditItemList.stream()
                .filter(itemDto -> itemDto.getSkuAttrPriceId() != null)
                .peek(itemDto -> {
                    if (null == itemDto.getSkuAttrPriceId()) {
                        throw new BizException("id不能为空");
                    }
                    if (null == itemDto.getVersion()) {
                        throw new BizException("version不能为空");
                    }
                    if (null == itemDto.getSkuPrice()) {
                        throw new ParamIllegalException("变更价格不能为空");
                    }
                })
                .map(itemDto -> {
                    final SkuAttrPricePo skuAttrPricePo = new SkuAttrPricePo();
                    skuAttrPricePo.setSkuAttrPriceId(itemDto.getSkuAttrPriceId());
                    skuAttrPricePo.setVersion(itemDto.getVersion());
                    skuAttrPricePo.setSkuPrice(itemDto.getSkuPrice());
                    return skuAttrPricePo;
                })
                .collect(Collectors.toList());
        skuAttrPriceDao.updateBatchByIdVersion(updateSkuAttrPricePoList);

        final List<Long> idList = updateSkuAttrPricePoList.stream()
                .map(SkuAttrPricePo::getSkuAttrPriceId)
                .distinct()
                .collect(Collectors.toList());
        final List<SkuAttrPricePo> updatePoList = skuAttrPriceDao.getByIdList(idList);
        skuAttrPricePoList.addAll(updatePoList);
        final List<ProduceDataAttrPriceUpdateBo> boList = skuAttrPricePoList.stream().map(po -> {
            final ProduceDataAttrPriceUpdateBo bo = new ProduceDataAttrPriceUpdateBo();
            bo.setSkuPrice(po.getSkuPrice());
            bo.setLaceAttrValue(po.getLaceAttrValue());
            bo.setSizeAttrValue(po.getSizeAttrValue());
            bo.setMaterialAttrValue(po.getMaterialAttrValue());
            return bo;
        }).collect(Collectors.toList());
        final ProduceDataPriceByAttrDto produceDataPriceByAttrDto = new ProduceDataPriceByAttrDto();
        produceDataPriceByAttrDto.setProduceDataAttrPriceUpdateBoList(boList);

        consistencyService.execAsyncTask(ProduceDataPriceByAttrHandler.class, produceDataPriceByAttrDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void importSkuAttr(SkuAttrImportationDto dto) {
        log.info("批量导入定价入参dto={}", dto);
        if (StringUtils.isBlank(dto.getLaceAttrValue())) {
            throw new ParamIllegalException("蕾丝面积不能为空，请检查后再提交!");
        }
        if (StringUtils.isBlank(dto.getSizeAttrValue())) {
            throw new ParamIllegalException("档长尺寸不能为空，请检查后再提交!");
        }
        if (StringUtils.isBlank(dto.getMaterialAttrValue())) {
            throw new ParamIllegalException("材料不能为空，请检查后再提交!");
        }
        if (null == dto.getSkuPrice() || dto.getSkuPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ParamIllegalException("sku价格输入有误，请检查后再提交!");
        }
        final int valueLength = 100;
        if (StringUtils.length(dto.getLaceAttrValue()) > valueLength) {
            throw new ParamIllegalException("蕾丝面积字段长度超过100个字符，请检查后再提交！");
        }
        if (StringUtils.length(dto.getSizeAttrValue()) > valueLength) {
            throw new ParamIllegalException("档长尺寸字段长度超过100个字符，请检查后再提交！");
        }
        if (StringUtils.length(dto.getMaterialAttrValue()) > valueLength) {
            throw new ParamIllegalException("材料字段长度超过100个字符，请检查后再提交！");
        }
        String lace = "蕾丝面积";
        String size = "档长尺寸";
        String material = "材料";
        final List<PlmAttributeVo> plmAttributeVoList = plmRemoteService.getAttrListByName(Arrays.asList("蕾丝面积", "档长尺寸", "材料"));
        for (PlmAttributeVo plmAttributeVo : plmAttributeVoList) {
            if (lace.equals(plmAttributeVo.getAttributeName())
                    && !plmAttributeVo.getAttributeValueList().contains(dto.getLaceAttrValue())) {
                throw new ParamIllegalException("蕾丝面积的值不存在，请检查后再提交！");
            }
            if (size.equals(plmAttributeVo.getAttributeName())
                    && !plmAttributeVo.getAttributeValueList().contains(dto.getSizeAttrValue())) {
                throw new ParamIllegalException("档长尺寸的值不存在，请检查后再提交！");
            }
            if (material.equals(plmAttributeVo.getAttributeName())
                    && !plmAttributeVo.getAttributeValueList().contains(dto.getMaterialAttrValue())) {
                throw new ParamIllegalException("材料的值不存在，请检查后再提交！");
            }
        }

        final SkuAttrItemDto skuAttrItemDto = new SkuAttrItemDto();
        skuAttrItemDto.setLaceAttrValue(dto.getLaceAttrValue());
        skuAttrItemDto.setSizeAttrValue(dto.getSizeAttrValue());
        skuAttrItemDto.setMaterialAttrValue(dto.getMaterialAttrValue());

        final List<SkuAttrPricePo> dbSkuAttrPricePoList = skuAttrPriceDao.getListBySkuAndAttr(Collections.singletonList(skuAttrItemDto));
        final ProduceDataAttrPriceUpdateBo bo = new ProduceDataAttrPriceUpdateBo();
        if (CollectionUtils.isNotEmpty(dbSkuAttrPricePoList)) {
            final SkuAttrPricePo skuAttrPricePo = dbSkuAttrPricePoList.get(0);
            skuAttrPricePo.setSkuPrice(dto.getSkuPrice());
            skuAttrPriceDao.updateByIdVersion(skuAttrPricePo);

            bo.setSkuPrice(skuAttrPricePo.getSkuPrice());
            bo.setLaceAttrValue(skuAttrPricePo.getLaceAttrValue());
            bo.setSizeAttrValue(skuAttrPricePo.getSizeAttrValue());
            bo.setMaterialAttrValue(skuAttrPricePo.getMaterialAttrValue());
        } else {
            final SkuAttrPricePo skuAttrPricePo = new SkuAttrPricePo();
            skuAttrPricePo.setSkuPrice(dto.getSkuPrice());
            skuAttrPricePo.setLaceAttrValue(dto.getLaceAttrValue());
            skuAttrPricePo.setSizeAttrValue(dto.getSizeAttrValue());
            skuAttrPricePo.setMaterialAttrValue(dto.getMaterialAttrValue());
            skuAttrPriceDao.insert(skuAttrPricePo);

            bo.setSkuPrice(skuAttrPricePo.getSkuPrice());
            bo.setLaceAttrValue(skuAttrPricePo.getLaceAttrValue());
            bo.setSizeAttrValue(skuAttrPricePo.getSizeAttrValue());
            bo.setMaterialAttrValue(skuAttrPricePo.getMaterialAttrValue());
        }

        final ProduceDataPriceByAttrDto produceDataPriceByAttrDto = new ProduceDataPriceByAttrDto();
        produceDataPriceByAttrDto.setProduceDataAttrPriceUpdateBoList(Collections.singletonList(bo));

        consistencyService.execAsyncTask(ProduceDataPriceByAttrHandler.class, produceDataPriceByAttrDto);
    }
}
