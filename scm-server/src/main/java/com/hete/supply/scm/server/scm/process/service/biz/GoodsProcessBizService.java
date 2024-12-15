package com.hete.supply.scm.server.scm.process.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.plm.api.goods.entity.vo.PlmCategoryVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmGoodsDetailVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmSkuVo;
import com.hete.supply.scm.api.scm.entity.dto.GoodsProcessQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.dto.GoodsProcessQueryDto;
import com.hete.supply.scm.api.scm.entity.enums.GoodsProcessStatus;
import com.hete.supply.scm.api.scm.entity.vo.GoodsProcessExportVo;
import com.hete.supply.scm.api.scm.importation.entity.dto.GoodsProcessImportationDto;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.dao.PlmSkuDao;
import com.hete.supply.scm.server.scm.entity.po.GoodsProcessPo;
import com.hete.supply.scm.server.scm.entity.po.GoodsProcessRelationPo;
import com.hete.supply.scm.server.scm.entity.po.PlmSkuPo;
import com.hete.supply.scm.server.scm.entity.vo.GoodsProcessItemVo;
import com.hete.supply.scm.server.scm.entity.vo.GoodsProcessVo;
import com.hete.supply.scm.server.scm.enums.BindingSupplierProduct;
import com.hete.supply.scm.server.scm.process.dao.GoodsProcessDao;
import com.hete.supply.scm.server.scm.process.dao.GoodsProcessRelationDao;
import com.hete.supply.scm.server.scm.process.dao.ProcessDao;
import com.hete.supply.scm.server.scm.process.entity.dto.GoodsProcessBindDto;
import com.hete.supply.scm.server.scm.process.entity.dto.GoodsProcessMqDto;
import com.hete.supply.scm.server.scm.process.entity.dto.GoodsProcessUnBindDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessPo;
import com.hete.supply.scm.server.scm.service.ref.ProduceDataRefService;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierProductCompareDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.mybatis.plus.entity.bo.CompareResult;
import com.hete.support.mybatis.plus.util.DataCompareUtil;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author: RockyHuas
 * @date: 2022/11/7 13:31
 */
@Service
@RequiredArgsConstructor
public class GoodsProcessBizService {
    private final GoodsProcessDao goodsProcessDao;
    private final GoodsProcessRelationDao goodsProcessRelationDao;
    private final PlmRemoteService plmRemoteService;
    private final ProcessDao processDao;
    private final PlmSkuDao plmSkuDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final ProduceDataRefService produceDataRefService;
    private final SupplierProductCompareDao supplierProductCompareDao;


    public CommonPageResult.PageInfo<GoodsProcessVo> getByPage(GoodsProcessQueryDto goodsProcessQueryDto) {

        Long categoryId = goodsProcessQueryDto.getCategoryId();
        if (null != categoryId) {
            List<String> skuByCategoryId = plmRemoteService.getSkuByCategoryId(categoryId);
            if (CollectionUtils.isEmpty(skuByCategoryId)) {
                return new CommonPageResult.PageInfo<>();
            }
            goodsProcessQueryDto.setSkuByCategory(skuByCategoryId);
        }

        // 通过 code 查询
        String processCode = goodsProcessQueryDto.getProcessCode();
        if (StringUtils.isNotBlank(processCode)) {
            ProcessPo byProcessCode = processDao.getByProcessCode(processCode);
            if (null == byProcessCode) {
                return new CommonPageResult.PageInfo<>();
            }
            ArrayList<Long> processIdsByCode = new ArrayList<>();
            processIdsByCode.add(byProcessCode.getProcessId());
            List<GoodsProcessRelationPo> goodsProcessRelationPosByCode = goodsProcessRelationDao.getByProcessIds(processIdsByCode);
            if (CollectionUtils.isNotEmpty(goodsProcessRelationPosByCode)) {
                goodsProcessQueryDto.setGoodsProcessIdsByProcessCode(goodsProcessRelationPosByCode.stream().map(GoodsProcessRelationPo::getGoodsProcessId).collect(Collectors.toList()));
            }
        }

        // 通过工序名称查询
        String processName = goodsProcessQueryDto.getProcessName();
        if (StringUtils.isNotBlank(processName)) {
            ArrayList<String> processNames = new ArrayList<>();
            processNames.add(processName);
            List<ProcessPo> byProcessNames = processDao.getByProcessNames(processNames);
            if (CollectionUtils.isEmpty(byProcessNames)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<GoodsProcessRelationPo> goodsProcessRelationPosByName = goodsProcessRelationDao.getByProcessIds(byProcessNames.stream().map(ProcessPo::getProcessId).collect(Collectors.toList()));
            if (CollectionUtils.isEmpty(goodsProcessRelationPosByName)) {
                return new CommonPageResult.PageInfo<>();
            }
            goodsProcessQueryDto.setGoodsProcessIdsByProcessName(goodsProcessRelationPosByName.stream()
                    .map(GoodsProcessRelationPo::getGoodsProcessId)
                    .collect(Collectors.toList()));
        }

        CommonPageResult.PageInfo<GoodsProcessVo> resultByPage = goodsProcessDao.getByPage(PageDTO.of(goodsProcessQueryDto.getPageNo(), goodsProcessQueryDto.getPageSize()),
                goodsProcessQueryDto);
        List<GoodsProcessVo> records = resultByPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return resultByPage;
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
        resultByPage.setRecords(newRecords);


        return resultByPage;
    }

    /**
     * 同步
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmConstant.GOODS_PROCESS_SPU_LOCK_PREFIX, key = "#dto.spuCode", waitTime = 1, leaseTime = -1)
    public void sync(GoodsProcessMqDto dto, Boolean isDelete) {
        List<GoodsProcessPo> goodsProcessPoList = goodsProcessDao.getBySkuList(dto.getSkuCodeList());
        if (isDelete) {
            // 删除操作
            if (CollectionUtils.isEmpty(goodsProcessPoList)) {
                return;
            }
            final List<Long> goodsProcessIds = goodsProcessPoList.stream()
                    .map(GoodsProcessPo::getGoodsProcessId)
                    .collect(Collectors.toList());
            goodsProcessDao.removeBatchByIds(goodsProcessIds);
            List<GoodsProcessRelationPo> goodsProcessRelationPos = goodsProcessRelationDao.getByGoodsProcessIds(goodsProcessIds);
            if (CollectionUtils.isNotEmpty(goodsProcessRelationPos)) {
                goodsProcessRelationDao.removeBatchByIds(goodsProcessRelationPos);
            }
        } else {
            // 创建操作（plm那边只会把新增的skuCodeList推送过来）
            if (CollectionUtils.isNotEmpty(goodsProcessPoList)) {
                return;
            }
            List<PlmSkuPo> plmSkuPoList = new ArrayList<>();
            // 查询产品对照是否存在
            Map<String, List<SupplierProductComparePo>> supplierProductComparePoMap = supplierProductCompareDao.getBySkuList(dto.getSkuCodeList());
            final List<GoodsProcessPo> newGoodsProcessPoList = dto.getSkuCodeList().stream()
                    .map(skuCode -> {
                        PlmSkuPo plmSkuPo = new PlmSkuPo();
                        plmSkuPo.setSku(skuCode);
                        plmSkuPo.setSpu(dto.getSpuCode());
                        if (supplierProductComparePoMap.containsKey(skuCode)) {
                            plmSkuPo.setBindingSupplierProduct(BindingSupplierProduct.TRUE);
                        } else {
                            plmSkuPo.setBindingSupplierProduct(BindingSupplierProduct.FALSE);
                        }
                        plmSkuPoList.add(plmSkuPo);
                        GoodsProcessPo newGoodsProcessPo = new GoodsProcessPo();
                        newGoodsProcessPo.setSku(skuCode);
                        newGoodsProcessPo.setGoodsProcessStatus(GoodsProcessStatus.UNBINDED);
                        return newGoodsProcessPo;
                    }).collect(Collectors.toList());
            // 创建PlmSku时需要进行处理sku生产属性
            produceDataRefService.createPlmSkuUpdateProduceDataAttr(plmSkuPoList);
            goodsProcessDao.insertBatch(newGoodsProcessPoList);
        }
    }

    /**
     * 绑定工序
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean bind(GoodsProcessBindDto dto) {

        List<GoodsProcessBindDto.GoodsProcess> goodsProcesses = dto.getGoodsProcesses();
        List<GoodsProcessBindDto.ProcessRelation> processRelations = dto.getProcessRelations();

        // 更新商品工序的绑定状态
        List<GoodsProcessPo> needUpdatedProcessPos = goodsProcesses.stream().map((item) -> {
            GoodsProcessPo goodsProcessPo = new GoodsProcessPo();
            goodsProcessPo.setGoodsProcessId(item.getGoodsProcessId());
            goodsProcessPo.setVersion(item.getVersion());
            goodsProcessPo.setGoodsProcessStatus(GoodsProcessStatus.BINDED);
            return goodsProcessPo;
        }).collect(Collectors.toList());
        goodsProcessDao.updateBatchByIdVersion(needUpdatedProcessPos);

        // 查询所有绑定的工序
        List<GoodsProcessRelationPo> queriedGoodsProcessRelationPos = goodsProcessRelationDao.getByGoodsProcessIds(goodsProcesses.stream().map(GoodsProcessBindDto.GoodsProcess::getGoodsProcessId).collect(Collectors.toList()));
        Map<String, List<GoodsProcessRelationPo>> grouped = queriedGoodsProcessRelationPos.stream()
                .collect(Collectors.groupingBy(it -> it.getGoodsProcessId() + "_" + it.getProcessId()));

        ArrayList<GoodsProcessRelationPo> goodsProcessRelationPos = new ArrayList<>();
        goodsProcesses.forEach(it -> {
            processRelations.forEach((item) -> {
                if (grouped.isEmpty()) {
                    GoodsProcessRelationPo goodsProcessRelationPo = new GoodsProcessRelationPo();
                    goodsProcessRelationPo.setProcessId(item.getProcessId());
                    goodsProcessRelationPo.setSort(item.getSort());
                    goodsProcessRelationPo.setGoodsProcessId(it.getGoodsProcessId());
                    goodsProcessRelationPos.add(goodsProcessRelationPo);
                } else {
                    int size = grouped.get(it.getGoodsProcessId() + "_" + item.getProcessId()).size();
                    if (size == 0) {
                        GoodsProcessRelationPo goodsProcessRelationPo = new GoodsProcessRelationPo();
                        goodsProcessRelationPo.setProcessId(item.getProcessId());
                        goodsProcessRelationPo.setSort(item.getSort());
                        goodsProcessRelationPo.setGoodsProcessId(it.getGoodsProcessId());
                        goodsProcessRelationPos.add(goodsProcessRelationPo);
                    }
                }


            });
        });

        if (CollectionUtils.isNotEmpty(goodsProcessRelationPos)) {
            goodsProcessRelationDao.insertBatch(goodsProcessRelationPos);
        }

        return true;


    }

    /**
     * 删除工序
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean unbind(GoodsProcessUnBindDto dto) {

        List<GoodsProcessUnBindDto.GoodsProcess> goodsProcesses = dto.getGoodsProcesses();
        List<Long> processIds = dto.getProcessIds();

        List<Long> goodsProcessIds = goodsProcesses.stream().map(GoodsProcessUnBindDto.GoodsProcess::getGoodsProcessId).collect(Collectors.toList());

        // 查询所有的关联的工序(并且按照goods_process_id 分组)
        List<GoodsProcessRelationPo> queriedRelations = goodsProcessRelationDao.getByGoodsProcessIds(goodsProcessIds);

        // 需要删除的工序
        List<GoodsProcessRelationPo> needDeleteRelationPos = queriedRelations.stream().filter((it) -> processIds.contains(it.getProcessId())).collect(Collectors.toList());

        // 需要保留的商品工序
        List<Long> needRemainGoodsProcessIds = queriedRelations.stream().filter((it) -> !processIds.contains(it.getProcessId())).map(GoodsProcessRelationPo::getGoodsProcessId).collect(Collectors.toList());

        // 更新商品工序的绑定状态
        List<GoodsProcessPo> needUpdatedProcessPos = goodsProcesses.stream().map((item) -> {
            GoodsProcessPo goodsProcessPo = new GoodsProcessPo();
            goodsProcessPo.setGoodsProcessId(item.getGoodsProcessId());
            goodsProcessPo.setVersion(item.getVersion());
            if (needRemainGoodsProcessIds.contains(item.getGoodsProcessId())) {
                goodsProcessPo.setGoodsProcessStatus(GoodsProcessStatus.BINDED);
            } else {
                goodsProcessPo.setGoodsProcessStatus(GoodsProcessStatus.UNBINDED);
            }
            return goodsProcessPo;
        }).collect(Collectors.toList());
        goodsProcessDao.updateBatchByIdVersion(needUpdatedProcessPos);

        // 更新绑定关系
        if (CollectionUtils.isNotEmpty(needDeleteRelationPos)) {
            goodsProcessRelationDao.removeBatchByIds(needDeleteRelationPos);
        }
        return true;
    }

    public Integer getExportTotals(GoodsProcessQueryDto dto) {
        Long categoryId = dto.getCategoryId();
        if (null != categoryId) {
            List<String> skuByCategoryId = plmRemoteService.getSkuByCategoryId(categoryId);
            if (CollectionUtils.isEmpty(skuByCategoryId)) {
                return 0;
            }
            dto.setSkuByCategory(skuByCategoryId);
        }
        return goodsProcessDao.getExportTotals(dto);
    }

    /**
     * 查询导出列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<GoodsProcessExportVo> getExportList(GoodsProcessQueryByApiDto dto) {
        Long categoryId = dto.getCategoryId();
        if (null != categoryId) {
            List<String> skuByCategoryId = plmRemoteService.getSkuByCategoryId(categoryId);
            if (CollectionUtils.isEmpty(skuByCategoryId)) {
                return new CommonPageResult.PageInfo<>();
            }
            dto.setSkuByCategory(skuByCategoryId);
        }
        CommonPageResult.PageInfo<GoodsProcessExportVo> exportList = goodsProcessDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize(), false), dto);

        List<GoodsProcessExportVo> goodsProcessExportVos = exportList.getRecords();

        ArrayList<Object> categoryNames = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(goodsProcessExportVos)) {
            List<String> skuList = goodsProcessExportVos.stream().map(GoodsProcessExportVo::getSku).collect(Collectors.toList());
            List<PlmGoodsDetailVo> categoriesBySku = plmRemoteService.getGoodsDetail(skuList);

            goodsProcessExportVos = goodsProcessExportVos.stream().peek(item -> {
                ArrayList<String> categoryList = new ArrayList<>();

                if (!categoriesBySku.isEmpty()) {
                    List<PlmGoodsDetailVo> plmGoodsDetailVos = categoriesBySku.stream().filter(it -> it.skuCodeInside(item.getSku())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(plmGoodsDetailVos)) {
                        plmGoodsDetailVos.forEach(it -> {
                            categoryList.add(String.join(">", it.getCategoryList().stream().map(PlmCategoryVo::getCategoryName).collect(Collectors.toList())));
                        });
                    }
                }

                item.setCategoryName(String.join(";", categoryList));
            }).collect(Collectors.toList());
        }

        List<GoodsProcessExportVo> newGoodsProcessExportVos = goodsProcessExportVos.stream()
                .peek(item -> {
                    if (null != item.getProcessFirst()) {
                        item.setProcessFirstName(item.getProcessFirst().getDesc());
                    }
                })
                .collect(Collectors.toList());

        exportList.setRecords(newGoodsProcessExportVos);

        return exportList;

    }


    /**
     * 处理商品工序导入数据
     *
     * @param importationDetail
     */
    @Transactional(rollbackFor = Exception.class)
    public void importData(GoodsProcessImportationDto.ImportationDetail importationDetail) {

        String sku = importationDetail.getSku();
        if (StringUtils.isBlank(sku)) {
            throw new ParamIllegalException("sku 不能为空");
        }
        // 通过 spu 查询数据
        GoodsProcessPo goodsProcessPo = goodsProcessDao.getBySku(sku);
        if (null == goodsProcessPo) {
            throw new ParamIllegalException("系统不存在sku:{}", sku);
        }

        ArrayList<String> processNames = new ArrayList<>();
        String firstProcessName = importationDetail.getFirstProcessName();
        if (StringUtils.isBlank(firstProcessName)) {
            throw new ParamIllegalException("1 号工序不能为空");
        }
        processNames.add(firstProcessName);

        if (StringUtils.isNotBlank(importationDetail.getSecondProcessName())) {
            processNames.add(importationDetail.getSecondProcessName());
        }

        if (StringUtils.isNotBlank(importationDetail.getThirdProcessName())) {
            processNames.add(importationDetail.getThirdProcessName());
        }

        if (StringUtils.isNotBlank(importationDetail.getFourthProcessName())) {
            processNames.add(importationDetail.getFourthProcessName());
        }

        if (StringUtils.isNotBlank(importationDetail.getFifthProcessName())) {
            processNames.add(importationDetail.getFifthProcessName());
        }

        if (StringUtils.isNotBlank(importationDetail.getSixthProcessName())) {
            processNames.add(importationDetail.getSixthProcessName());
        }

        // 查询工序
        List<ProcessPo> processPos = processDao.getByProcessNames(processNames);
        if (processPos.size() != processNames.size()) {
            throw new ParamIllegalException("工序名称重复或不存在");
        }


        ArrayList<Long> goodsProcessIds = new ArrayList<>();
        goodsProcessIds.add(goodsProcessPo.getGoodsProcessId());
        List<GoodsProcessRelationPo> goodsProcessRelationPos = goodsProcessRelationDao.getByGoodsProcessIds(goodsProcessIds);
        Integer sort = 1;
        if (CollectionUtils.isNotEmpty(goodsProcessRelationPos)) {
            List<Integer> sorts = goodsProcessRelationPos.stream().map(GoodsProcessRelationPo::getSort).collect(Collectors.toList());
            sort = Collections.max(sorts);
        }
        Map<Long, List<GoodsProcessRelationPo>> groupedRelations = goodsProcessRelationPos.stream().collect(Collectors.groupingBy(GoodsProcessRelationPo::getProcessId));

        Integer finalSort = sort;
        List<GoodsProcessRelationPo> newGoodsProcessRelationPos = processPos.stream().map(item -> {
            int i = processPos.indexOf(item);
            GoodsProcessRelationPo goodsProcessRelationPo = new GoodsProcessRelationPo();
            goodsProcessRelationPo.setProcessId(item.getProcessId());
            goodsProcessRelationPo.setSort(finalSort + i + 1);
            goodsProcessRelationPo.setGoodsProcessId(goodsProcessPo.getGoodsProcessId());
            if (!groupedRelations.isEmpty()) {
                Optional<GoodsProcessRelationPo> first = groupedRelations.get(item.getProcessId()).stream().findFirst();
                if (first.isPresent()) {
                    GoodsProcessRelationPo goodsProcessRelationPo1 = first.get();
                    goodsProcessRelationPo.setGoodsProcessRelationId(goodsProcessRelationPo1.getGoodsProcessRelationId());
                    goodsProcessRelationPo.setVersion(goodsProcessRelationPo1.getVersion());
                }
            }
            return goodsProcessRelationPo;
        }).collect(Collectors.toList());

        CompareResult<GoodsProcessRelationPo> compare = DataCompareUtil.compare(newGoodsProcessRelationPos, goodsProcessRelationPos, GoodsProcessRelationPo::getGoodsProcessRelationId);
        goodsProcessRelationDao.insertBatch(compare.getNewItems());
        goodsProcessRelationDao.updateBatchByIdVersion(compare.getExistingItems());
        goodsProcessRelationDao.removeBatchByIds(compare.getDeletedItems());

        goodsProcessPo.setGoodsProcessStatus(GoodsProcessStatus.BINDED);
        goodsProcessDao.updateByIdVersion(goodsProcessPo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportGoodsProcess(GoodsProcessQueryDto dto) {
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                FileOperateBizType.SCM_GOODS_PROCESS_EXPORT.getCode(), dto));
    }
}