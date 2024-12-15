package com.hete.supply.scm.server.scm.supplier.service.base;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.entity.bo.ScmImageBo;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.supplier.converter.SupplierSubjectConverter;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierSubjectDao;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierSubjectCreateDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierSubjectEditDto;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierSubjectPo;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.mybatis.plus.entity.bo.CompareResult;
import com.hete.support.mybatis.plus.util.DataCompareUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/5/15 19:45
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class SupplierSubjectBaseService {
    private final SupplierSubjectDao supplierSubjectDao;
    private final IdGenerateService idGenerateService;
    private final ScmImageBaseService scmImageBaseService;

    /**
     * 编辑主体信息
     *
     * @param dtoList:
     * @param supplierCode:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/15 19:47
     */
    public void editSubject(List<SupplierSubjectEditDto> dtoList,
                            @NotBlank String supplierCode) {

        // 查询供应商对应旧主体信息
        List<SupplierSubjectPo> supplierSubjectPoList = supplierSubjectDao.getListBySupplierCode(supplierCode);
        List<Long> supplierSubjectIdList = supplierSubjectPoList.stream().map(SupplierSubjectPo::getSupplierSubjectId).collect(Collectors.toList());

        // 清除旧图片
        if (CollectionUtils.isNotEmpty(supplierSubjectIdList)) {
            scmImageBaseService.removeAllImageList(ImageBizType.SUPPLIER_SUBJECT_LICENSE, supplierSubjectIdList);
        }

        // 存在数据情况
        if (CollectionUtils.isNotEmpty(dtoList)) {
            List<String> subjectList = dtoList.stream()
                    .map(SupplierSubjectEditDto::getSubject)
                    .distinct()
                    .collect(Collectors.toList());
            Map<String, SupplierSubjectPo> supplierSubjectPoMap = supplierSubjectDao.getMapBySubjectList(subjectList);

            // 判断是否存在重复主体
            Set<String> subjectSet = new HashSet<>();
            for (SupplierSubjectEditDto dto : dtoList) {
                if (!subjectSet.add(dto.getSubject())) {
                    throw new ParamIllegalException("主体名称:{}存在重复，请修改再操作", dto.getSubject());
                }
                SupplierSubjectPo supplierSubjectPo = supplierSubjectPoMap.get(dto.getSubject());
                if (null != supplierSubjectPo && !supplierCode.equals(supplierSubjectPo.getSupplierCode())) {
                    throw new ParamIllegalException("主体名称:{}已经被其他供应商添加了，请修改再操作", dto.getSubject());
                }
                // 验证版本号
                if (null != dto.getSupplierSubjectId()) {
                    supplierSubjectPoList.stream()
                            .filter(po -> po.getSupplierSubjectId().equals(dto.getSupplierSubjectId()))
                            .findFirst()
                            .ifPresent(po -> Assert.isTrue(po.getVersion().equals(dto.getVersion()), () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！")));
                }
            }

            //编辑信息详情
            List<SupplierSubjectPo> supplierSubjectPoDtoList = SupplierSubjectConverter.editDtoToPo(dtoList);
            for (SupplierSubjectPo supplierSubjectPo : supplierSubjectPoDtoList) {
                if (null == supplierSubjectPo.getSupplierSubjectId()) {
                    Long supplierSubjectId = idGenerateService.getSnowflakeId();
                    supplierSubjectPo.setSupplierSubjectId(supplierSubjectId);
                }
                // 图片处理
                SupplierSubjectEditDto supplierSubjectEditDto = dtoList.stream()
                        .filter(dto -> dto.getSubject().equals(supplierSubjectPo.getSubject()))
                        .findFirst()
                        .orElse(null);
                if (null != supplierSubjectEditDto && CollectionUtils.isNotEmpty(supplierSubjectEditDto.getBusinessFileCodeList())) {
                    scmImageBaseService.insertBatchImage(supplierSubjectEditDto.getBusinessFileCodeList(),
                            ImageBizType.SUPPLIER_SUBJECT_LICENSE, supplierSubjectPo.getSupplierSubjectId());
                }
            }

            CompareResult<SupplierSubjectPo> subjectResult = DataCompareUtil.compare(supplierSubjectPoDtoList, supplierSubjectPoList, SupplierSubjectPo::getSupplierSubjectId);
            List<SupplierSubjectPo> deletedItems = subjectResult.getDeletedItems();

            // 验证更新的主体是否存在收款账号中
            List<SupplierSubjectPo> updateItems = subjectResult.getExistingItems();

            supplierSubjectDao.removeBatchByIds(deletedItems);
            supplierSubjectDao.updateBatchByIdVersion(updateItems);
            supplierSubjectDao.insertBatch(subjectResult.getNewItems());
        }

        // 空数据情况
        if (CollectionUtils.isEmpty(dtoList)) {
            supplierSubjectDao.removeBatchByIds(supplierSubjectIdList);
        }


    }

    /**
     * 创建主体信息
     *
     * @param dtoList:
     * @return void
     * @author ChenWenLong
     * @date 2024/5/15 19:49
     */
    public void createSubject(List<SupplierSubjectCreateDto> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }
        List<String> subjectList = dtoList.stream()
                .map(SupplierSubjectCreateDto::getSubject)
                .distinct()
                .collect(Collectors.toList());
        Map<String, SupplierSubjectPo> supplierSubjectPoMap = supplierSubjectDao.getMapBySubjectList(subjectList);

        // 判断是否存在重复主体
        Set<String> subjectSet = new HashSet<>();

        for (SupplierSubjectCreateDto dto : dtoList) {
            if (!subjectSet.add(dto.getSubject())) {
                throw new ParamIllegalException("主体名称:{}存在重复，请修改再操作", dto.getSubject());
            }
            SupplierSubjectPo supplierSubjectPo = supplierSubjectPoMap.get(dto.getSubject());
            if (null != supplierSubjectPo) {
                throw new ParamIllegalException("主体名称:{}已经被其他供应商添加了，请修改再操作", dto.getSubject());
            }
        }

        // 新增的主体信息
        List<SupplierSubjectPo> insertSupplierSubjectPoList = SupplierSubjectConverter.createDtoToPo(dtoList);

        // 营业执照
        List<ScmImageBo> scmImageBoBusinessList = new ArrayList<>();
        for (SupplierSubjectPo supplierSubjectPo : insertSupplierSubjectPoList) {
            final long snowflakeId = idGenerateService.getSnowflakeId();
            supplierSubjectPo.setSupplierSubjectId(snowflakeId);
            dtoList.stream()
                    .filter(dto -> dto.getSubject().equals(supplierSubjectPo.getSubject()))
                    .findFirst()
                    .ifPresent(dto -> {
                        if (CollectionUtils.isNotEmpty(dto.getBusinessFileCodeList())) {
                            ScmImageBo scmImageBo = new ScmImageBo();
                            scmImageBo.setImageBizId(supplierSubjectPo.getSupplierSubjectId());
                            scmImageBo.setFileCodeList(dto.getBusinessFileCodeList());
                            scmImageBoBusinessList.add(scmImageBo);
                        }
                    });
        }

        // 创建图片
        scmImageBaseService.insertBatchImageBo(scmImageBoBusinessList, ImageBizType.SUPPLIER_SUBJECT_LICENSE);

        supplierSubjectDao.insertBatch(insertSupplierSubjectPoList);


    }
}
