package com.hete.supply.scm.server.scm.process.service.biz;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.google.common.collect.Lists;
import com.hete.supply.plm.api.basedata.entity.vo.ObtainCategoryVo;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.supply.scm.api.scm.importation.entity.dto.ProcessTemplateImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.ProcessTemplateMaterialImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.ProcessTemplateProcedureImportationDto;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.enums.SortOrder;
import com.hete.supply.scm.server.scm.process.converter.ProcessTemplateConverter;
import com.hete.supply.scm.server.scm.process.dao.*;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessContrastTmpBo;
import com.hete.supply.scm.server.scm.process.entity.dto.*;
import com.hete.supply.scm.server.scm.process.entity.po.*;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessDescriptionInfoVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessTemplateDetailByTemplateVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessTemplateDetailVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessTemplateVo;
import com.hete.supply.scm.server.scm.process.enums.ProcessTemplateType;
import com.hete.supply.scm.server.scm.process.service.base.ProcessTemplateBaseService;
import com.hete.supply.wms.api.basic.entity.vo.SimpleSkuBatchVo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.entity.bo.CompareResult;
import com.hete.support.mybatis.plus.util.DataCompareUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: RockyHuas
 * @date: 2022/11/5 09:38
 */
@Service
@RequiredArgsConstructor
public class ProcessTemplateBizService {
    private final ProcessTemplateDao processTemplateDao;
    private final ProcessTemplateRelationDao processTemplateRelationDao;
    private final ProcessTemplateMaterialDao processTemplateMaterialDao;
    private final ProcessDao processDao;
    private final PlmRemoteService plmRemoteService;
    private final WmsRemoteService wmsRemoteService;
    private final ProcessTemplateProcessOrderDescriptionDao processTemplateProcessOrderDescriptionDao;
    private final ProcessTemplateBaseService processTemplateBaseService;

    /**
     * 分页查询
     *
     * @param processTemplateQueryDto
     * @return
     */
    public CommonPageResult.PageInfo<ProcessTemplateVo> getByPage(ProcessTemplateQueryDto processTemplateQueryDto) {
        final Long categoryId = processTemplateQueryDto.getCategoryId();
        final String sortField = processTemplateQueryDto.getSortField();
        final SortOrder sortOrder = processTemplateQueryDto.getSortOrder();

        processTemplateQueryDto.setSortField(StrUtil.isNotBlank(sortField) ? StrUtil.toUnderlineCase(sortField) : null);
        processTemplateQueryDto.setSortOrder(Objects.isNull(sortOrder) ? SortOrder.DESC : sortOrder);

        if (null != categoryId) {
            List<Long> childByCategoryId = plmRemoteService.getChildByCategoryId(categoryId);
            if (CollectionUtils.isEmpty(childByCategoryId)) {
                return new CommonPageResult.PageInfo<>();
            }
            processTemplateQueryDto.setCategoryIds(childByCategoryId);
        }
        return processTemplateDao.getByPage(PageDTO.of(processTemplateQueryDto.getPageNo(), processTemplateQueryDto.getPageSize()), processTemplateQueryDto);
    }

    /**
     * 通过关键字查询模版
     *
     * @param dto
     * @return
     */
    public List<ProcessTemplateDetailByTemplateVo> getByTemplate(ProcessTemplateQueryByTemplateDto dto) {
        final String sku = dto.getSku();
        final String processTemplateName = dto.getProcessTemplateName();

        if (StringUtils.isBlank(sku) && StringUtils.isBlank(processTemplateName)) {
            throw new ParamIllegalException("请输入查询的sku或者模版名称");
        }

        List<ProcessTemplatePo> processTemplatePos;
        if (StringUtils.isNotBlank(processTemplateName)) {
            // 通过模版名称
            processTemplatePos = processTemplateDao.getListByName(processTemplateName);
        } else {
            // 通过 sku
            processTemplatePos = processTemplateDao.getListBySku(sku);
            if (CollectionUtils.isEmpty(processTemplatePos)) {
                List<Long> categoriesBySku = plmRemoteService.getCategoriesBySku(sku);
                if (CollectionUtils.isNotEmpty(categoriesBySku)) {
                    processTemplatePos = processTemplateDao.getListByCategoryIds(categoriesBySku);
                }
            }
        }
        if (CollectionUtils.isEmpty(processTemplatePos)) {
            return new ArrayList<>();
        }

        ArrayList<ProcessTemplateDetailByTemplateVo> processTemplateDetailByTemplateVos = new ArrayList<>();

        // 查询工序
        List<ProcessTemplateRelationPo> processTemplateRelationPos = processTemplateRelationDao.getByProcessTemplateIds(processTemplatePos.stream().map(ProcessTemplatePo::getProcessTemplateId).collect(Collectors.toList()));
        List<ProcessPo> processPos = processDao.getByProcessIds(processTemplateRelationPos.stream().map(ProcessTemplateRelationPo::getProcessId).collect(Collectors.toList()));
        Map<Long, List<ProcessPo>> groupedProcessPos = processPos.stream().collect(Collectors.groupingBy(ProcessPo::getProcessId));

        Map<Long, List<ProcessTemplateRelationPo>> groupedRelations = processTemplateRelationPos.stream().collect(Collectors.groupingBy(ProcessTemplateRelationPo::getProcessTemplateId));

        // 查询原料
        List<ProcessTemplateMaterialPo> processTemplateMaterialPos = processTemplateMaterialDao.getByProcessTemplateIds(processTemplatePos.stream().map(ProcessTemplatePo::getProcessTemplateId).collect(Collectors.toList()));

        processTemplateMaterialPos = Optional.ofNullable(processTemplateMaterialPos).orElse(new ArrayList<>());
        Map<Long, List<ProcessTemplateMaterialPo>> groupedMaterials = processTemplateMaterialPos.stream().collect(Collectors.groupingBy(ProcessTemplateMaterialPo::getProcessTemplateId));

        processTemplatePos.forEach(item -> {
            ProcessTemplateDetailByTemplateVo processTemplateDetailByTemplateVo = new ProcessTemplateDetailByTemplateVo();
            processTemplateDetailByTemplateVo.setProcessTemplateId(item.getProcessTemplateId());
            processTemplateDetailByTemplateVo.setName(item.getName());
            processTemplateDetailByTemplateVo.setTypeValueName(item.getTypeValueName());

            List<ProcessTemplateRelationPo> sortedProcessTemplateRelationPos = groupedRelations.get(item.getProcessTemplateId()).stream().sorted(Comparator.comparing(ProcessTemplateRelationPo::getSort)).collect(Collectors.toList());

            List<ProcessTemplateDetailByTemplateVo.Process> processes = sortedProcessTemplateRelationPos.stream().map(it -> {
                Optional<ProcessPo> firstProcessPoOptional = groupedProcessPos.get(it.getProcessId()).stream().findFirst();
                ProcessTemplateDetailByTemplateVo.Process process = new ProcessTemplateDetailByTemplateVo.Process();
                process.setProcessId(it.getProcessId());
                process.setSort(it.getSort());
                if (firstProcessPoOptional.isPresent()) {
                    ProcessPo processPo = firstProcessPoOptional.get();
                    process.setProcessFirst(processPo.getProcessFirst());
                    process.setProcessCode(processPo.getProcessCode());
                    process.setProcessName(processPo.getProcessName());
                    process.setCommission(processPo.getCommission());
                    process.setProcessLabel(processPo.getProcessLabel());
                    process.setProcessSecondName(processPo.getProcessSecondName());
                }
                return process;
            }).collect(Collectors.toList());

            processTemplateDetailByTemplateVo.setProcesses(processes);

            List<ProcessTemplateMaterialPo> processTemplateMaterialPoList = groupedMaterials.get(item.getProcessTemplateId());
            if (CollectionUtils.isNotEmpty(processTemplateMaterialPoList)) {
                List<ProcessTemplateDetailByTemplateVo.Material> materialList = processTemplateMaterialPoList.stream().map(it -> {
                    ProcessTemplateDetailByTemplateVo.Material material = new ProcessTemplateDetailByTemplateVo.Material();
                    material.setName(it.getName());
                    material.setSku(it.getSku());
                    material.setNum(it.getNum());
                    material.setProcessTemplateMaterialId(it.getProcessTemplateMaterialId());
                    material.setVersion(it.getVersion());
                    return material;
                }).collect(Collectors.toList());

                processTemplateDetailByTemplateVo.setMaterials(materialList);
            }

            processTemplateDetailByTemplateVos.add(processTemplateDetailByTemplateVo);

        });


        return processTemplateDetailByTemplateVos;
    }


    /**
     * 创建工序模版
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long create(ProcessTemplateCreateDto dto) {
        // 判断名称是否存在
        ProcessTemplatePo nameExist = processTemplateDao.getByName(dto.getName());
        if (Objects.nonNull(nameExist)) {
            throw new ParamIllegalException("{}已存在", dto.getName());
        }

        ProcessTemplatePo processTemplatePo = ProcessTemplateConverter.INSTANCE.convert(dto);
        processTemplatePo.setDetailsLastUpdatedTime(LocalDateTime.now());
        processTemplateDao.insert(processTemplatePo);
        final Long processTemplateId = processTemplatePo.getProcessTemplateId();

        // 组装工序明细
        List<ProcessTemplateCreateDto.Process> processes = dto.getProcesses();
        List<ProcessTemplateRelationPo> processTemplateRelationPos = processes.stream().map(item -> {
            ProcessTemplateRelationPo processTemplateRelationPo = new ProcessTemplateRelationPo();
            processTemplateRelationPo.setProcessId(item.getProcessId());
            processTemplateRelationPo.setSort(item.getSort());
            processTemplateRelationPo.setProcessTemplateId(processTemplateId);

            return processTemplateRelationPo;

        }).collect(Collectors.toList());
        processTemplateRelationDao.insertBatch(processTemplateRelationPos);

        // 组装原料明细
        List<ProcessTemplateCreateDto.Material> materials = dto.getMaterials();
        if (CollectionUtils.isNotEmpty(materials)) {
            List<ProcessTemplateMaterialPo> processTemplateMaterialPoList = materials.stream().map(item -> {
                ProcessTemplateMaterialPo processTemplateMaterialPo = new ProcessTemplateMaterialPo();
                processTemplateMaterialPo.setName(item.getName());
                processTemplateMaterialPo.setSku(item.getSku());
                processTemplateMaterialPo.setNum(item.getNum());
                processTemplateMaterialPo.setProcessTemplateId(processTemplateId);
                processTemplateMaterialPo.setMaterialSkuType(item.getMaterialSkuType());
                return processTemplateMaterialPo;

            }).collect(Collectors.toList());
            processTemplateMaterialDao.insertBatch(processTemplateMaterialPoList);
        }

        List<ProcessDescriptionInfoDto> processDescriptionInfoDtoList = dto.getProcessDescriptionInfoDtoList();
        if (CollectionUtils.isNotEmpty(processDescriptionInfoDtoList)) {
            List<ProcessTemplateProcessOrderDescriptionPo> descPos = processDescriptionInfoDtoList.stream().map(item -> {
                ProcessTemplateProcessOrderDescriptionPo descPo = new ProcessTemplateProcessOrderDescriptionPo();
                descPo.setProcessTemplateId(processTemplateId);
                descPo.setProcessDescName(item.getProcessDescName());
                descPo.setProcessDescValue(item.getProcessDescValue());
                return descPo;
            }).collect(Collectors.toList());
            processTemplateProcessOrderDescriptionDao.insertBatch(descPos);
        }

        return processTemplateId;
    }

    /**
     * 更新工序模版
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean edit(ProcessTemplateEditDto dto) {
        // 检查模板名字是否唯一
        processTemplateBaseService.checkTemplateNameUniqueness(dto.getProcessTemplateId(), dto.getName());

        ProcessTemplatePo processTemplatePo = ProcessTemplateConverter.INSTANCE.convert(dto);
        processTemplatePo.setDetailsLastUpdatedTime(LocalDateTime.now());
        processTemplateDao.updateByIdVersion(processTemplatePo);
        final Long processTemplateId = processTemplatePo.getProcessTemplateId();

        // 组装工序明细
        List<ProcessTemplateEditDto.Process> processes = dto.getProcesses();
        List<ProcessTemplateRelationPo> processTemplateRelationPos;
        processTemplateRelationPos = processes.stream().map(item -> {
            ProcessTemplateRelationPo processTemplateRelationPo = new ProcessTemplateRelationPo();
            processTemplateRelationPo.setProcessTemplateRelationId(item.getProcessTemplateRelationId());
            processTemplateRelationPo.setProcessId(item.getProcessId());
            processTemplateRelationPo.setSort(item.getSort());
            processTemplateRelationPo.setProcessTemplateId(processTemplateId);
            return processTemplateRelationPo;
        }).collect(Collectors.toList());

        // 查询已经存在的工序
        List<ProcessTemplateRelationPo> relationPosByProcessTemplateId = processTemplateRelationDao.getByProcessTemplateId(processTemplateId);

        // 比较数据
        CompareResult<ProcessTemplateRelationPo> compareResult = DataCompareUtil.compare(processTemplateRelationPos, relationPosByProcessTemplateId, ProcessTemplateRelationPo::getProcessTemplateRelationId);
        processTemplateRelationDao.insertBatch(compareResult.getNewItems());
        processTemplateRelationDao.updateBatchByIdVersion(compareResult.getExistingItems());
        processTemplateRelationDao.removeBatchByIds(compareResult.getDeletedItems());

        // 组装原料明细
        List<ProcessTemplateEditDto.Material> materials = dto.getMaterials();
        List<ProcessTemplateMaterialPo> processTemplateMaterialPoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(materials)) {
            processTemplateMaterialPoList = materials.stream().map(item -> {
                ProcessTemplateMaterialPo processTemplateMaterialPo = new ProcessTemplateMaterialPo();
                processTemplateMaterialPo.setName(item.getName());
                processTemplateMaterialPo.setSku(item.getSku());
                processTemplateMaterialPo.setNum(item.getNum());
                processTemplateMaterialPo.setProcessTemplateId(processTemplateId);
                if (Objects.nonNull(item.getMaterialSkuType())) {
                    processTemplateMaterialPo.setMaterialSkuType(item.getMaterialSkuType());
                }
                return processTemplateMaterialPo;

            }).collect(Collectors.toList());
        }
        // 查询已经存在的原料
        List<ProcessTemplateMaterialPo> materialPosByProcessTemplateId = processTemplateMaterialDao.getByProcessTemplateId(processTemplateId);
        // 比较数据
        CompareResult<ProcessTemplateMaterialPo> result = DataCompareUtil.compare(processTemplateMaterialPoList, materialPosByProcessTemplateId, ProcessTemplateMaterialPo::getProcessTemplateMaterialId);
        processTemplateMaterialDao.insertBatch(result.getNewItems());
        processTemplateMaterialDao.updateBatchByIdVersion(result.getExistingItems());
        processTemplateMaterialDao.removeBatchByIds(result.getDeletedItems());

        // 工序模板描述信息
        List<ProcessDescriptionInfoDto> modifyDescDtoList = dto.getProcessDescriptionInfoDtoList();
        List<ProcessTemplateProcessOrderDescriptionPo> modifyDescPos = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(modifyDescDtoList)) {
            modifyDescPos = modifyDescDtoList.stream().map(item -> {
                ProcessTemplateProcessOrderDescriptionPo descPo = new ProcessTemplateProcessOrderDescriptionPo();
                descPo.setProcessTemplateId(processTemplateId);
                descPo.setProcessDescName(item.getProcessDescName());
                descPo.setProcessDescValue(item.getProcessDescValue());
                return descPo;
            }).collect(Collectors.toList());
        }
        List<ProcessTemplateProcessOrderDescriptionPo> existDescPos = processTemplateProcessOrderDescriptionDao.getByProcessTemplateId(processTemplateId);
        CompareResult<ProcessTemplateProcessOrderDescriptionPo> compareDescPos
                = DataCompareUtil.compare(modifyDescPos, existDescPos, ProcessTemplateProcessOrderDescriptionPo::getProcessTemplateProcessOrderDescriptionId);
        processTemplateProcessOrderDescriptionDao.insertBatch(compareDescPos.getNewItems());
        processTemplateProcessOrderDescriptionDao.updateBatchByIdVersion(compareDescPos.getExistingItems());
        processTemplateProcessOrderDescriptionDao.removeBatchByIds(compareDescPos.getDeletedItems());

        return true;
    }

    /**
     * 查询工序模版详情
     *
     * @param dto
     * @return
     */
    public ProcessTemplateDetailVo detail(ProcessTemplateDetailDto dto) {
        final Long processTemplateId = dto.getProcessTemplateId();

        ProcessTemplatePo processTemplatePo = processTemplateDao.getById(processTemplateId);
        if (processTemplatePo == null) {
            throw new BizException("工序模版不存在，参数错误");
        }
        ProcessTemplateDetailVo processTemplateDetailVo = ProcessTemplateConverter.INSTANCE.convert(processTemplatePo);

        // 查询工序详情
        List<ProcessTemplateRelationPo> relationPosByProcessTemplateId = processTemplateRelationDao.getByProcessTemplateId(processTemplateId);
        List<Long> idList = relationPosByProcessTemplateId.stream().map(ProcessTemplateRelationPo::getProcessId).collect(Collectors.toList());
        List<ProcessPo> processPoList = processDao.getByProcessIds(idList);
        List<ProcessTemplateDetailVo.Process> processes = relationPosByProcessTemplateId.stream().map((item) -> {
            ProcessTemplateDetailVo.Process process = new ProcessTemplateDetailVo.Process();
            final Long processId = item.getProcessId();
            process.setProcessTemplateRelationId(item.getProcessTemplateRelationId());
            process.setProcessId(processId);
            process.setSort(item.getSort());
            process.setVersion(item.getVersion());

            ProcessPo matchProcess
                    = processPoList.stream().filter(processPo -> Objects.equals(processId, processPo.getProcessId())).findFirst().orElse(null);
            if (Objects.nonNull(matchProcess)) {
                process.setProcessLabel(matchProcess.getProcessLabel());
                process.setProcessSecondCode(matchProcess.getProcessSecondCode());
            }
            return process;
        }).collect(Collectors.toList());
        processTemplateDetailVo.setProcesses(processes);

        // 查询原料详情
        List<ProcessTemplateMaterialPo> materialsByProcessTemplateId = processTemplateMaterialDao.getByProcessTemplateId(processTemplateId);
        List<ProcessTemplateDetailVo.Material> materials = materialsByProcessTemplateId.stream().map((item) -> {
            ProcessTemplateDetailVo.Material material = new ProcessTemplateDetailVo.Material();
            material.setProcessTemplateMaterialId(item.getProcessTemplateMaterialId());
            material.setName(item.getName());
            material.setNum(item.getNum());
            material.setSku(item.getSku());
            material.setMaterialSkuType(item.getMaterialSkuType());
            material.setVersion(item.getVersion());
            return material;
        }).collect(Collectors.toList());
        processTemplateDetailVo.setMaterials(materials);

        // 工序描述信息
        List<ProcessTemplateProcessOrderDescriptionPo> processTemplateProcessOrderDescriptionPos = processTemplateProcessOrderDescriptionDao.getByProcessTemplateId(processTemplateId);
        List<ProcessDescriptionInfoVo> processDescriptionInfoVos = processTemplateProcessOrderDescriptionPos.stream().map(item -> {
            ProcessDescriptionInfoVo vo = new ProcessDescriptionInfoVo();
            vo.setProcessDescName(item.getProcessDescName());
            vo.setProcessDescValue(item.getProcessDescValue());
            return vo;
        }).collect(Collectors.toList());
        processTemplateDetailVo.setProcessDescriptionInfoVos(processDescriptionInfoVos);

        return processTemplateDetailVo;
    }

    /**
     * 删除工序模版
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean remove(ProcessTemplateRemoveDto dto) {
        final Long processTemplateId = dto.getProcessTemplateId();
        final Integer version = dto.getVersion();
        boolean removeResult = processTemplateDao.removeByIdVersion(processTemplateId, version);

        // 删除关联的数据
        if (removeResult) {
            List<ProcessTemplateRelationPo> existProcess
                    = processTemplateRelationDao.getByProcessTemplateId(processTemplateId);
            if (CollectionUtils.isNotEmpty(existProcess)) {
                processTemplateRelationDao.removeBatchByIds(existProcess);
            }

            List<ProcessTemplateMaterialPo> existProcessMaterials
                    = processTemplateMaterialDao.getByProcessTemplateId(processTemplateId);
            if (CollectionUtils.isNotEmpty(existProcessMaterials)) {
                processTemplateMaterialDao.removeBatchByIds(existProcessMaterials);
            }

            List<ProcessTemplateProcessOrderDescriptionPo> existDescriptions
                    = processTemplateProcessOrderDescriptionDao.getByProcessTemplateId(processTemplateId);
            if (CollectionUtils.isNotEmpty(existDescriptions)) {
                processTemplateProcessOrderDescriptionDao.removeBatchByIds(existDescriptions);
            }
        }

        return true;
    }

    /**
     * 导入工序模版
     *
     * @param importationDetail
     */
    @Transactional(rollbackFor = Exception.class)
    public void importData(ProcessTemplateImportationDto.ImportationDetail importationDetail) {
        String name = importationDetail.getName();
        if (StringUtils.isBlank(name)) {
            throw new ParamIllegalException("工序模版名称不能为空");
        }
        String type = importationDetail.getType();
        if (StringUtils.isBlank(type)) {
            throw new ParamIllegalException("类型不能为空");
        }
        type = type.toUpperCase();
        if (!ProcessTemplateType.CATEGORY.name().equals(type) && !ProcessTemplateType.SKU.name().equals(type)) {
            throw new ParamIllegalException("类型错误");
        }
        String typeValue = importationDetail.getTypeValue();
        if (StringUtils.isBlank(typeValue)) {
            throw new ParamIllegalException("值不能为空");
        }


        ProcessTemplatePo processTemplatePo = new ProcessTemplatePo();
        processTemplatePo.setName(name);
        if (ProcessTemplateType.CATEGORY.name().equals(type)) {
            processTemplatePo.setProcessTemplateType(ProcessTemplateType.CATEGORY);
            ObtainCategoryVo categoryByName = plmRemoteService.getCategoryByName(typeValue);
            processTemplatePo.setTypeValue(categoryByName.getCategoryId().toString());
            processTemplatePo.setTypeValueName(typeValue);

        }
        if (ProcessTemplateType.SKU.name().equals(type)) {
            processTemplatePo.setProcessTemplateType(ProcessTemplateType.SKU);
            processTemplatePo.setTypeValue(importationDetail.getTypeValue());
            processTemplatePo.setTypeValueName(importationDetail.getTypeValue());
        }
        ProcessTemplatePo queriedProcessTemplatePo = processTemplateDao.getByName(name);
        if (null == queriedProcessTemplatePo) {
            processTemplateDao.insert(processTemplatePo);
        } else {
            processTemplatePo.setProcessTemplateId(queriedProcessTemplatePo.getProcessTemplateId());
            processTemplatePo.setVersion(queriedProcessTemplatePo.getVersion());
            processTemplateDao.updateByIdVersion(processTemplatePo);
        }


        ArrayList<String> processNames = new ArrayList<>();
        String firstProcessName = importationDetail.getFirstProcessName();
        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank(firstProcessName)) {
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

        if (StringUtils.isNotBlank(importationDetail.getSeventhProcessName())) {
            processNames.add(importationDetail.getSeventhProcessName());
        }

        if (StringUtils.isNotBlank(importationDetail.getEighthProcessName())) {
            processNames.add(importationDetail.getEighthProcessName());
        }

        if (StringUtils.isNotBlank(importationDetail.getNinthProcessName())) {
            processNames.add(importationDetail.getNinthProcessName());
        }

        if (StringUtils.isNotBlank(importationDetail.getTenthProcessName())) {
            processNames.add(importationDetail.getTenthProcessName());
        }

        // 查询工序
        List<ProcessPo> processPos = processDao.getByProcessNames(processNames);
        if (processPos.size() != processNames.size()) {
            throw new ParamIllegalException("工序名称重复或不存在");
        }


        List<ProcessTemplateRelationPo> processTemplateRelationPos = processTemplateRelationDao.getByProcessTemplateId(processTemplatePo.getProcessTemplateId());
        Integer sort = 1;
        if (CollectionUtils.isNotEmpty(processTemplateRelationPos)) {
            List<Integer> sorts = processTemplateRelationPos.stream().map(ProcessTemplateRelationPo::getSort).collect(Collectors.toList());
            sort = Collections.max(sorts);
        }
        Map<Long, List<ProcessTemplateRelationPo>> groupedRelations = processTemplateRelationPos.stream().collect(Collectors.groupingBy(ProcessTemplateRelationPo::getProcessId));

        Integer finalSort = sort;
        List<ProcessTemplateRelationPo> newProcessTemplateRelationPos = processPos.stream().map(item -> {
            int i = processPos.indexOf(item);
            ProcessTemplateRelationPo processTemplateRelationPo = new ProcessTemplateRelationPo();
            processTemplateRelationPo.setProcessId(item.getProcessId());
            processTemplateRelationPo.setSort(finalSort + i + 1);
            processTemplateRelationPo.setProcessTemplateId(processTemplatePo.getProcessTemplateId());
            if (!groupedRelations.isEmpty()) {
                Optional<ProcessTemplateRelationPo> first = groupedRelations.get(item.getProcessId()).stream().findFirst();
                if (first.isPresent()) {
                    ProcessTemplateRelationPo processTemplateRelationPo1 = first.get();
                    processTemplateRelationPo.setProcessTemplateRelationId(processTemplateRelationPo1.getProcessTemplateRelationId());
                    processTemplateRelationPo.setVersion(processTemplateRelationPo1.getVersion());
                }
            }
            return processTemplateRelationPo;
        }).collect(Collectors.toList());

        CompareResult<ProcessTemplateRelationPo> compare = DataCompareUtil.compare(newProcessTemplateRelationPos, processTemplateRelationPos, ProcessTemplateRelationPo::getProcessTemplateRelationId);
        processTemplateRelationDao.insertBatch(compare.getNewItems());
        processTemplateRelationDao.updateBatchByIdVersion(compare.getExistingItems());
        processTemplateRelationDao.removeBatchByIds(compare.getDeletedItems());


    }

    /**
     * 导入工序模版-工序数据
     *
     * @param req
     */
    @Transactional(rollbackFor = Exception.class)
    public void importProcedureData(ProcessTemplateProcedureImportationDto.ImportationDetail req) {
        // 旧 sku
        String sku = req.getSku();

        // 名称
        String name = req.getName();

        // 旧二级工序名称
        List<String> processSecondNameList = req.getProcessSecondNameList();

        if (StringUtils.isBlank(sku) || CollectionUtils.isEmpty(processSecondNameList) || CollectionUtils.isEmpty(processSecondNameList)) {
            throw new BizException("模版名称，sku，工序必须存在");
        }
        processSecondNameList = processSecondNameList.stream().filter(item -> !"货到加工".equals(item) && !"加工完成".equals(item) && !"货到质检".equals(item) && !"货回仓库".equals(item)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(processSecondNameList)) {
            throw new BizException("工序必须存在");
        }

        List<String> batchCodes = List.of(sku);
        List<SimpleSkuBatchVo> skuBatchByBatchCodes = wmsRemoteService.getSkuBatchByBatchCodes(batchCodes);
        String finalSku = sku;
        Optional<SimpleSkuBatchVo> skuBatchVoOptional = skuBatchByBatchCodes.stream().filter(it -> finalSku.equals(it.getBatchCode())).findFirst();
        if (skuBatchVoOptional.isPresent()) {
            sku = skuBatchVoOptional.get().getSkuCode();
        } else {
            throw new BizException("旧 sku：{} 对照关系不存在", sku);
        }

        List<ProcessTemplatePo> processTemplatePoList = processTemplateDao.getListBySku(sku);
        if (CollectionUtils.isNotEmpty(processTemplatePoList)) {
            throw new BizException("已经存在工序模版");
        }
        ProcessTemplatePo processTemplatePo = new ProcessTemplatePo();
        processTemplatePo.setProcessTemplateType(ProcessTemplateType.SKU);
        processTemplatePo.setName(name);
        processTemplatePo.setTypeValue(sku);
        processTemplatePo.setTypeValueName(sku);
        processTemplateDao.insert(processTemplatePo);

        List<ProcessContrastTmpBo> processContrastTmpBos = this.initProcessContrast();
        Map<String, List<ProcessContrastTmpBo>> groupedProcessContrastTmpBos = processContrastTmpBos.stream().collect(Collectors.groupingBy(ProcessContrastTmpBo::getOldProcessSecondName));

        List<ProcessPo> processPos = processDao.getAll();

        int sort = 0;
        List<String> finalProcessSecondNameList = processSecondNameList;
        List<ProcessTemplateRelationPo> processTemplateRelationPoList = processSecondNameList.stream().map(item -> {
            List<ProcessContrastTmpBo> needProcessContrastTmpBoList = groupedProcessContrastTmpBos.get(item);
            Assert.notEmpty(needProcessContrastTmpBoList, () -> new BizException("工序：{},不存在对应关系", item));
            Assert.isTrue(needProcessContrastTmpBoList.size() == 1, () -> new BizException("工序：{},存在多个对应关系", item));
            Optional<ProcessContrastTmpBo> firstContrastTmpBoOptional = needProcessContrastTmpBoList.stream().findFirst();

            ProcessContrastTmpBo processContrastTmpBo = firstContrastTmpBoOptional.get();
            String newProcessSecondName = processContrastTmpBo.getNewProcessSecondName();
            List<ProcessPo> newProcessPos = processPos.stream().filter(it -> processContrastTmpBo.getProcessFirstName().equals(it.getProcessFirst().getDesc()) && newProcessSecondName.equals(it.getProcessSecondName()) && processContrastTmpBo.getProcessLabel().equals(it.getProcessLabel())).collect(Collectors.toList());

            Assert.notEmpty(newProcessPos, () -> new BizException("新工序：{},不存在对应关系", newProcessSecondName));
            Assert.isTrue(newProcessPos.size() == 1, () -> new BizException("新工序：{},存在多个对应关系", newProcessSecondName));

            int i = finalProcessSecondNameList.indexOf(item);
            Optional<ProcessPo> firstProcessPoOptional = newProcessPos.stream().findFirst();
            ProcessPo processPo = firstProcessPoOptional.get();

            ProcessTemplateRelationPo processTemplateRelationPo = new ProcessTemplateRelationPo();
            processTemplateRelationPo.setProcessId(processPo.getProcessId());
            processTemplateRelationPo.setSort(sort + 1 + i);
            processTemplateRelationPo.setProcessTemplateId(processTemplatePo.getProcessTemplateId());

            return processTemplateRelationPo;
        }).collect(Collectors.toList());

        processTemplateRelationDao.insertBatch(processTemplateRelationPoList);
    }

    /**
     * 导入工序模版-原料数据
     *
     * @param req
     */
    @Transactional(rollbackFor = Exception.class)
    public void importMaterialData(ProcessTemplateMaterialImportationDto.ImportationDetail req) {

        // 名称
        String name = req.getName();

        // 原料名称
        String materialName = req.getMaterialName();

        String materialSku = req.getMaterialSku();

        Integer num = req.getNum();

        if (StringUtils.isBlank(name) || StringUtils.isBlank(materialName) || StringUtils.isBlank(materialSku)) {
            throw new BizException("模版名称，原料名称，原料sku必须存在");
        }
        List<String> batchCodes = List.of(materialSku);
        List<SimpleSkuBatchVo> skuBatchByBatchCodes = wmsRemoteService.getSkuBatchByBatchCodes(batchCodes);
        String finalSku = materialSku;
        Optional<SimpleSkuBatchVo> skuBatchVoOptional = skuBatchByBatchCodes.stream().filter(it -> finalSku.equals(it.getBatchCode())).findFirst();
        if (skuBatchVoOptional.isPresent()) {
            materialSku = skuBatchVoOptional.get().getSkuCode();
        } else {
            throw new BizException("原料 sku：{} 对照关系不存在", materialSku);
        }

        ProcessTemplatePo processTemplatePo = processTemplateDao.getByName(name);
        if (null == processTemplatePo) {
            throw new BizException("工序模版不存在");
        }
        List<ProcessTemplateMaterialPo> processTemplateMaterialPoList = processTemplateMaterialDao.getByProcessTemplateId(processTemplatePo.getProcessTemplateId());
        processTemplateMaterialPoList = Optional.ofNullable(processTemplateMaterialPoList).orElse(new ArrayList<>());
        Map<String, List<ProcessTemplateMaterialPo>> groupedMaterialPos = processTemplateMaterialPoList.stream().collect(Collectors.groupingBy(ProcessTemplateMaterialPo::getSku));

        // 没有插入过，才执行插入操作
        if (groupedMaterialPos.isEmpty() || null == groupedMaterialPos.get(materialSku)) {
            ProcessTemplateMaterialPo newProcessTemplateMaterialPo = new ProcessTemplateMaterialPo();
            newProcessTemplateMaterialPo.setProcessTemplateId(processTemplatePo.getProcessTemplateId());
            newProcessTemplateMaterialPo.setName(materialName);
            newProcessTemplateMaterialPo.setSku(materialSku);
            newProcessTemplateMaterialPo.setNum(num);
            processTemplateMaterialDao.insert(newProcessTemplateMaterialPo);
        }


    }

    /**
     * 初始化对照数据
     *
     * @return
     */
    public List<ProcessContrastTmpBo> initProcessContrast() {
        List<ProcessContrastTmpBo> processContrastTmpBos = new ArrayList<>();
        processContrastTmpBos.add(this.getFormatContrastTmp("4*13 拔毛", "4*13 拔毛", "后处理", ProcessLabel.PLUCK_HAIR));
        processContrastTmpBos.add(this.getFormatContrastTmp("360镂空拔毛", "360镂空拔毛", "后处理", ProcessLabel.PLUCK_HAIR));
        processContrastTmpBos.add(this.getFormatContrastTmp("4*13 娃娃发", "4*13 娃娃发", "后处理", ProcessLabel.DOLL_HAIR));
        processContrastTmpBos.add(this.getFormatContrastTmp("360镂空娃娃发", "360镂空娃娃发", "后处理", ProcessLabel.DOLL_HAIR));
        processContrastTmpBos.add(this.getFormatContrastTmp("超级复杂", "造型-超级复杂", "后处理", ProcessLabel.MODELING));
        processContrastTmpBos.add(this.getFormatContrastTmp("返修-简单加工", "返修加工-简单造型", "后处理", ProcessLabel.MODELING));
        processContrastTmpBos.add(this.getFormatContrastTmp("返修-中等加工", "返修加工-中等造型", "后处理", ProcessLabel.MODELING));
        processContrastTmpBos.add(this.getFormatContrastTmp("返修-复杂加工", "返修加工-复杂造型", "后处理", ProcessLabel.MODELING));
        processContrastTmpBos.add(this.getFormatContrastTmp("简单", "造型—简单", "后处理", ProcessLabel.MODELING));
        processContrastTmpBos.add(this.getFormatContrastTmp("中等", "造型—中等", "后处理", ProcessLabel.MODELING));
        processContrastTmpBos.add(this.getFormatContrastTmp("复杂", "造型—复杂", "后处理", ProcessLabel.MODELING));
        processContrastTmpBos.add(this.getFormatContrastTmp("4*4头套", "4*4头套", "缝制中", ProcessLabel.SEWING_HEAD));
        processContrastTmpBos.add(this.getFormatContrastTmp("4*3.5头套", "4*3.5头套", "缝制中", ProcessLabel.SEWING_HEAD));
        processContrastTmpBos.add(this.getFormatContrastTmp("5*5头套", "5*5头套", "缝制中", ProcessLabel.SEWING_HEAD));
        processContrastTmpBos.add(this.getFormatContrastTmp("6*6头套", "6*6头套", "缝制中", ProcessLabel.SEWING_HEAD));
        processContrastTmpBos.add(this.getFormatContrastTmp("13*4头套", "13*4头套", "缝制中", ProcessLabel.SEWING_HEAD));
        processContrastTmpBos.add(this.getFormatContrastTmp("U型头套", "U型头套", "缝制中", ProcessLabel.SEWING_HEAD));
        processContrastTmpBos.add(this.getFormatContrastTmp("机制刘海头套", "机制刘海头套", "缝制中", ProcessLabel.SEWING_HEAD));
        processContrastTmpBos.add(this.getFormatContrastTmp("冰丝发带头套", "冰丝发带头套", "缝制中", ProcessLabel.SEWING_HEAD));
        processContrastTmpBos.add(this.getFormatContrastTmp("13*6头套", "13*6头套", "缝制中", ProcessLabel.SEWING_HEAD));
        processContrastTmpBos.add(this.getFormatContrastTmp("镂空360头套", "镂空360头套", "缝制中", ProcessLabel.SEWING_HEAD));
        processContrastTmpBos.add(this.getFormatContrastTmp("2*6头套", "2*6头套", "缝制中", ProcessLabel.SEWING_HEAD));
        processContrastTmpBos.add(this.getFormatContrastTmp("V-part头套", "V-part头套", "缝制中", ProcessLabel.SEWING_HEAD));
        processContrastTmpBos.add(this.getFormatContrastTmp("半头套", "半头套", "缝制中", ProcessLabel.SEWING_HEAD));
        processContrastTmpBos.add(this.getFormatContrastTmp("发缝头套", "发缝头套", "缝制中", ProcessLabel.SEWING_HEAD));
        processContrastTmpBos.add(this.getFormatContrastTmp("刘海片", "刘海片", "缝制中", ProcessLabel.SEWING_HEAD));
        processContrastTmpBos.add(this.getFormatContrastTmp("半圆马尾", "半圆马尾", "缝制中", ProcessLabel.SEWING_HEAD));
        processContrastTmpBos.add(this.getFormatContrastTmp("抽绳马尾", "抽绳马尾", "缝制中", ProcessLabel.SEWING_HEAD));
        processContrastTmpBos.add(this.getFormatContrastTmp("只缝5个九齿卡", "只缝5个九齿卡", "缝制中", ProcessLabel.CLIPS));
        processContrastTmpBos.add(this.getFormatContrastTmp("缝马尾卡子", "缝马尾卡子", "缝制中", ProcessLabel.CLIPS));
        processContrastTmpBos.add(this.getFormatContrastTmp("缝卡子发", "缝卡子发", "缝制中", ProcessLabel.CLIPS));
        processContrastTmpBos.add(this.getFormatContrastTmp("缝U-part卡子+松紧带", "缝U-part卡子+松紧带", "缝制中", ProcessLabel.CLIPS));
        processContrastTmpBos.add(this.getFormatContrastTmp("1个梳卡+松紧带", "1个梳卡+松紧带", "缝制中", ProcessLabel.CLIPS));
        processContrastTmpBos.add(this.getFormatContrastTmp("缝V-part卡子+松紧带", "缝V-part卡子+松紧带", "缝制中", ProcessLabel.CLIPS));
        processContrastTmpBos.add(this.getFormatContrastTmp("缝半头套卡子", "缝半头套卡子", "缝制中", ProcessLabel.CLIPS));
        processContrastTmpBos.add(this.getFormatContrastTmp("缝V-part卡子(5个九齿卡+3个梳卡)", "缝V-part卡子(5个九齿卡+3个梳卡)", "缝制中", ProcessLabel.CLIPS));
        processContrastTmpBos.add(this.getFormatContrastTmp("缝U-part卡子（4个九齿卡）", "缝U-part卡子（4个九齿卡）", "缝制中", ProcessLabel.CLIPS));
        processContrastTmpBos.add(this.getFormatContrastTmp("缝刘海片卡子（2个九齿卡）", "缝刘海片卡子（2个九齿卡）", "缝制中", ProcessLabel.CLIPS));
        processContrastTmpBos.add(this.getFormatContrastTmp("缝4个梳卡", "缝4个梳卡", "缝制中", ProcessLabel.CLIPS));
        processContrastTmpBos.add(this.getFormatContrastTmp("拆/换松紧带", "拆/换松紧带", "缝制中", ProcessLabel.CLIPS));
        processContrastTmpBos.add(this.getFormatContrastTmp("缝4个梳卡+松紧带", "缝4个梳卡+松紧带", "缝制中", ProcessLabel.CLIPS));
        processContrastTmpBos.add(this.getFormatContrastTmp("发帘拆/换线", "发帘拆/换线", "缝制中", ProcessLabel.CLIPS));
        processContrastTmpBos.add(this.getFormatContrastTmp("卡子发", "卡子发", "缝制中", ProcessLabel.CLIPS));
        processContrastTmpBos.add(this.getFormatContrastTmp("4个梳卡+松紧带", "4个梳卡+松紧带", "缝制中", ProcessLabel.CLIPS));
        processContrastTmpBos.add(this.getFormatContrastTmp("3个梳卡+松紧带", "3个梳卡+松紧带", "缝制中", ProcessLabel.CLIPS));
        processContrastTmpBos.add(this.getFormatContrastTmp("只缝松紧带", "只缝松紧带", "缝制中", ProcessLabel.CLIPS));
        processContrastTmpBos.add(this.getFormatContrastTmp("只缝4个九齿卡", "只缝4个九齿卡", "缝制中", ProcessLabel.CLIPS));
        processContrastTmpBos.add(this.getFormatContrastTmp("拆/缝松紧带", "拆/缝松紧带", "缝制中", ProcessLabel.CLIPS));
        processContrastTmpBos.add(this.getFormatContrastTmp("发帘发块", "离子烫—发帘发块", "缝制中", ProcessLabel.ICON_PERM));
        processContrastTmpBos.add(this.getFormatContrastTmp("头套", "离子烫—头套", "缝制中", ProcessLabel.ICON_PERM));
        processContrastTmpBos.add(this.getFormatContrastTmp("4*4头套", "漂扣4*4头套", "缝制中", ProcessLabel.FLOATING));
        processContrastTmpBos.add(this.getFormatContrastTmp("5*5头套", "漂扣5*5头套", "缝制中", ProcessLabel.FLOATING));
        processContrastTmpBos.add(this.getFormatContrastTmp("13*4头套", "漂扣13*4头套", "缝制中", ProcessLabel.FLOATING));
        processContrastTmpBos.add(this.getFormatContrastTmp("13*6头套", "漂扣13*6头套", "缝制中", ProcessLabel.FLOATING));
        processContrastTmpBos.add(this.getFormatContrastTmp("360头套", "漂扣360头套", "缝制中", ProcessLabel.FLOATING));
        processContrastTmpBos.add(this.getFormatContrastTmp("2*6头套", "漂扣2*6头套", "缝制中", ProcessLabel.FLOATING));
        processContrastTmpBos.add(this.getFormatContrastTmp("全蕾丝", "漂扣全蕾丝", "缝制中", ProcessLabel.FLOATING));
        processContrastTmpBos.add(this.getFormatContrastTmp("发缝头套", "漂扣发缝头套", "缝制中", ProcessLabel.FLOATING));
        processContrastTmpBos.add(this.getFormatContrastTmp("4*4发块", "漂扣4*4发块", "缝制中", ProcessLabel.FLOATING));
        processContrastTmpBos.add(this.getFormatContrastTmp("5*5发块", "漂扣5*5发块", "缝制中", ProcessLabel.FLOATING));
        processContrastTmpBos.add(this.getFormatContrastTmp("13*4发块", "漂扣13*4发块", "缝制中", ProcessLabel.FLOATING));
        processContrastTmpBos.add(this.getFormatContrastTmp("6*6发块", "漂扣6*6发块", "缝制中", ProcessLabel.FLOATING));
        processContrastTmpBos.add(this.getFormatContrastTmp("2*6发块", "漂扣2*6发块", "缝制中", ProcessLabel.FLOATING));
        processContrastTmpBos.add(this.getFormatContrastTmp("360头套", "漂扣360", "缝制中", ProcessLabel.FLOATING));
        processContrastTmpBos.add(this.getFormatContrastTmp("13*6发块", "漂扣13*6发块", "缝制中", ProcessLabel.FLOATING));
        processContrastTmpBos.add(this.getFormatContrastTmp("返修-13*4头套", "返修加工-13*4发块漂扣", "缝制中", ProcessLabel.FLOATING));
        processContrastTmpBos.add(this.getFormatContrastTmp("简单", "清洗—简单", "缝制中", ProcessLabel.CLEAN));
        processContrastTmpBos.add(this.getFormatContrastTmp("复杂", "清洗—复杂", "缝制中", ProcessLabel.CLEAN));
        processContrastTmpBos.add(this.getFormatContrastTmp("简单-头套", "头套染色—简单", "缝制中", ProcessLabel.DYEING));
        processContrastTmpBos.add(this.getFormatContrastTmp("中等-头套", "头套染色—中等", "缝制中", ProcessLabel.DYEING));
        processContrastTmpBos.add(this.getFormatContrastTmp("复杂-头套", "头套染色—复杂", "缝制中", ProcessLabel.DYEING));
        processContrastTmpBos.add(this.getFormatContrastTmp("超级复杂-头套", "头套染色-超级复杂", "缝制中", ProcessLabel.DYEING));
        processContrastTmpBos.add(this.getFormatContrastTmp("简单-发帘发块", "染色发帘发块—简单", "缝制中", ProcessLabel.DYEING));
        processContrastTmpBos.add(this.getFormatContrastTmp("复杂-发帘发块", "染色发帘发块—复杂", "缝制中", ProcessLabel.DYEING));
        processContrastTmpBos.add(this.getFormatContrastTmp("返修-简单染色", "返修加工-简单染色", "缝制中", ProcessLabel.DYEING));

        return processContrastTmpBos;
    }

    public ProcessContrastTmpBo getFormatContrastTmp(String newProcess, String oldProcess, String processFirstName, ProcessLabel processLabel) {
        ProcessContrastTmpBo processContrastTmpBo = new ProcessContrastTmpBo();
        processContrastTmpBo.setNewProcessSecondName(newProcess);
        processContrastTmpBo.setOldProcessSecondName(oldProcess);
        processContrastTmpBo.setProcessFirstName(processFirstName);
        processContrastTmpBo.setProcessLabel(processLabel);
        return processContrastTmpBo;
    }
}
