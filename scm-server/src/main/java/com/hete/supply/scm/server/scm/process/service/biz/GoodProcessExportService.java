package com.hete.supply.scm.server.scm.process.service.biz;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.plm.api.goods.entity.vo.PlmCategoryVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmGoodsDetailVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmSkuVo;
import com.hete.supply.scm.api.scm.entity.dto.GoodsProcessQueryDto;
import com.hete.supply.scm.api.scm.entity.vo.GoodsProcessNewExportVo;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.entity.po.GoodsProcessRelationPo;
import com.hete.supply.scm.server.scm.entity.vo.GoodsProcessItemVo;
import com.hete.supply.scm.server.scm.entity.vo.GoodsProcessVo;
import com.hete.supply.scm.server.scm.process.dao.GoodsProcessDao;
import com.hete.supply.scm.server.scm.process.dao.GoodsProcessRelationDao;
import com.hete.supply.scm.server.scm.process.dao.ProcessDao;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessPo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.core.util.TimeZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/8/7 09:42
 */
@Service
@RequiredArgsConstructor
public class GoodProcessExportService {
    private final PlmRemoteService plmRemoteService;
    private final ProcessDao processDao;
    private final GoodsProcessRelationDao goodsProcessRelationDao;
    private final GoodsProcessDao goodsProcessDao;


    public CommonResult<ExportationListResultBo<GoodsProcessNewExportVo>> getNewExportList(GoodsProcessQueryDto dto) {
        ExportationListResultBo<GoodsProcessNewExportVo> resultBo = new ExportationListResultBo<>();


        Long categoryId = dto.getCategoryId();
        if (null != categoryId) {
            List<String> skuByCategoryId = plmRemoteService.getSkuByCategoryId(categoryId);
            if (CollectionUtils.isEmpty(skuByCategoryId)) {
                return CommonResult.success(resultBo);
            }
            dto.setSkuByCategory(skuByCategoryId);
        }

        // 通过 code 查询
        String processCode = dto.getProcessCode();
        if (StringUtils.isNotBlank(processCode)) {
            ProcessPo byProcessCode = processDao.getByProcessCode(processCode);
            if (null == byProcessCode) {
                return CommonResult.success(resultBo);
            }
            ArrayList<Long> processIdsByCode = new ArrayList<>();
            processIdsByCode.add(byProcessCode.getProcessId());
            List<GoodsProcessRelationPo> goodsProcessRelationPosByCode = goodsProcessRelationDao.getByProcessIds(processIdsByCode);
            if (CollectionUtils.isNotEmpty(goodsProcessRelationPosByCode)) {
                dto.setGoodsProcessIdsByProcessCode(goodsProcessRelationPosByCode.stream().map(GoodsProcessRelationPo::getGoodsProcessId).collect(Collectors.toList()));
            }
        }

        // 通过工序名称查询
        String processName = dto.getProcessName();
        if (StringUtils.isNotBlank(processName)) {
            ArrayList<String> processNames = new ArrayList<>();
            processNames.add(processName);
            List<ProcessPo> byProcessNames = processDao.getByProcessNames(processNames);
            if (CollectionUtils.isEmpty(byProcessNames)) {
                return CommonResult.success(resultBo);
            }
            List<GoodsProcessRelationPo> goodsProcessRelationPosByName = goodsProcessRelationDao.getByProcessIds(byProcessNames.stream().map(ProcessPo::getProcessId).collect(Collectors.toList()));
            if (CollectionUtils.isEmpty(goodsProcessRelationPosByName)) {
                return CommonResult.success(resultBo);
            }
            dto.setGoodsProcessIdsByProcessName(goodsProcessRelationPosByName.stream()
                    .map(GoodsProcessRelationPo::getGoodsProcessId)
                    .collect(Collectors.toList()));
        }

        CommonPageResult.PageInfo<GoodsProcessVo> resultByPage = goodsProcessDao.getByPage(PageDTO.of(dto.getPageNo(), dto.getPageSize()),
                dto);
        List<GoodsProcessVo> records = resultByPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }
        List<Long> goodsProcessIds = records.stream().map(GoodsProcessVo::getGoodsProcessId).collect(Collectors.toList());
        List<GoodsProcessRelationPo> goodsProcessRelationPos = goodsProcessRelationDao.getByGoodsProcessIds(goodsProcessIds);
        List<ProcessPo> processPos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(goodsProcessRelationPos)) {
            List<Long> processIds = goodsProcessRelationPos.stream().map(GoodsProcessRelationPo::getProcessId).collect(Collectors.toList());
            processPos = processDao.getByProcessIds(processIds);
        }

        Map<Long, List<GoodsProcessRelationPo>> groupedGoodsProcessRelationPos = goodsProcessRelationPos.stream()
                .collect(Collectors.groupingBy(GoodsProcessRelationPo::getGoodsProcessId));

        List<String> skuList = records.stream()
                .map(GoodsProcessVo::getSku)
                .distinct()
                .collect(Collectors.toList());

        List<PlmSkuVo> skuEncodeBySku = plmRemoteService.getSkuEncodeBySku(skuList);
        Map<String, List<PlmSkuVo>> groupedPlmSkuVo = skuEncodeBySku.stream()
                .collect(Collectors.groupingBy(PlmSkuVo::getSkuCode));
        final List<PlmGoodsDetailVo> skuCategoriesList = plmRemoteService.getCategoriesBySku(skuList);

        Map<String, List<PlmCategoryVo>> skuToCategoryMap = new HashMap<>();
        skuCategoriesList.forEach(skuCategories -> {
            skuCategories.getSkuCodeList().forEach(sku -> {
                skuToCategoryMap.putIfAbsent(sku, skuCategories.getCategoryList());
            });
        });

        List<ProcessPo> finalProcessPos = processPos;
        List<GoodsProcessVo> newRecords = records.stream().peek(item -> {

            // 组装详情
            if (!groupedGoodsProcessRelationPos.isEmpty()) {
                List<GoodsProcessRelationPo> goodsProcessRelationPos1 = groupedGoodsProcessRelationPos.get(item.getGoodsProcessId());
                if (CollectionUtils.isNotEmpty(goodsProcessRelationPos1)) {
                    goodsProcessRelationPos1.sort(Comparator.comparing(GoodsProcessRelationPo::getSort));
                    AtomicInteger sort = new AtomicInteger();
                    List<GoodsProcessItemVo> goodsProcessItemVos = goodsProcessRelationPos1.stream().map(item2 -> {
                        GoodsProcessItemVo goodsProcessItemVo = new GoodsProcessItemVo();
                        goodsProcessItemVo.setProcessId(item2.getProcessId());
                        goodsProcessItemVo.setGoodsProcessRelationId(item2.getGoodsProcessRelationId());

                        Optional<ProcessPo> first = finalProcessPos.stream().filter(it -> it.getProcessId().equals(item2.getProcessId())).findFirst();
                        if (first.isPresent()) {
                            ProcessPo processPo = first.get();
                            goodsProcessItemVo.setProcessCode(processPo.getProcessCode());
                            goodsProcessItemVo.setProcessName(processPo.getProcessName());
                            goodsProcessItemVo.setProcessSecondName(processPo.getProcessSecondName());
                            goodsProcessItemVo.setProcessLabel(processPo.getProcessLabel());
                        }

                        goodsProcessItemVo.setVersion(item2.getVersion());
                        goodsProcessItemVo.setSort(sort.get());
                        sort.getAndIncrement();
                        return goodsProcessItemVo;
                    }).collect(Collectors.toList());
                    item.setProcesses(goodsProcessItemVos);
                }
            }

            List<PlmSkuVo> plmSkuVos = groupedPlmSkuVo.get(item.getSku());
            if (CollectionUtils.isNotEmpty(plmSkuVos)) {
                Optional<PlmSkuVo> firstPlmSkuVoOptional = plmSkuVos.stream().findFirst();
                firstPlmSkuVoOptional.ifPresent(plmSkuVo -> item.setSkuEncode(plmSkuVo.getSkuEncode()));
            }
            final List<PlmCategoryVo> plmCategoryVoList = skuToCategoryMap.get(item.getSku());
            item.setPlmCategoryVoList(plmCategoryVoList);

        }).collect(Collectors.toList());

        final List<GoodsProcessNewExportVo> goodsProcessNewExportVoList = newRecords.stream().map(record -> {
            final GoodsProcessNewExportVo goodsProcessNewExportVo = new GoodsProcessNewExportVo();
            goodsProcessNewExportVo.setSku(record.getSku());
            final String categoryName = Optional.ofNullable(record.getPlmCategoryVoList())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(PlmCategoryVo::getCategoryName)
                    .collect(Collectors.joining(","));
            goodsProcessNewExportVo.setCategoryName(categoryName);

            goodsProcessNewExportVo.setSkuEncode(record.getSkuEncode());
            goodsProcessNewExportVo.setUpdateUsername(record.getUpdateUsername());
            goodsProcessNewExportVo.setUpdateTime(ScmTimeUtil.localDateTimeToStr(record.getUpdateTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));

            final String processSecondName = Optional.ofNullable(record.getProcesses())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(vo -> vo.getProcessLabel().getRemark() + vo.getProcessSecondName())
                    .collect(Collectors.joining(","));

            goodsProcessNewExportVo.setProcessSecondName(processSecondName);

            return goodsProcessNewExportVo;
        }).collect(Collectors.toList());

        resultBo.setRowDataList(goodsProcessNewExportVoList);

        return CommonResult.success(resultBo);
    }
}
