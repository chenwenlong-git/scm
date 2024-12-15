package com.hete.supply.scm.server.scm.service.base;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.dao.ScmImageDao;
import com.hete.supply.scm.server.scm.entity.bo.ScmImageBo;
import com.hete.supply.scm.server.scm.entity.po.ScmImagePo;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.result.CommonPageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 图片基础类
 *
 * @author ChenWenLong
 * @date 2022/11/15 10:08
 */
@Service
@RequiredArgsConstructor
@Validated
public class ScmImageBaseService {
    private final ScmImageDao scmImageDao;

    /**
     * 根据业务id获取fileCode列表
     *
     * @param imageBizType
     * @param imageBizIdList
     * @return
     */
    public List<String> getFileCodeListByIdAndType(@NotNull(message = "imageBizType不能为空") ImageBizType imageBizType,
                                                   @NotEmpty(message = "imageBizId不能为空") List<Long> imageBizIdList) {

        return scmImageDao.getListByIdAndType(imageBizType, imageBizIdList)
                .stream()
                .map(ScmImagePo::getFileCode)
                .collect(Collectors.toList());
    }

    /**
     * 根据业务id获取fileCodeMap
     * key->id, value -> fileCodeList
     *
     * @param imageBizType
     * @param imageBizIdList
     * @return
     */
    public Map<Long, List<String>> getFileCodeMapByIdAndType(@NotNull(message = "imageBizType不能为空") ImageBizType imageBizType,
                                                             List<Long> imageBizIdList) {
        if (CollectionUtils.isEmpty(imageBizIdList)) {
            return Collections.emptyMap();
        }

        final List<ScmImagePo> scmImagePoList = scmImageDao.getListByIdAndType(imageBizType, imageBizIdList);

        return scmImagePoList.stream()
                .collect(Collectors.groupingBy(ScmImagePo::getImageBizId,
                        Collectors.mapping(ScmImagePo::getFileCode, Collectors.toList())));
    }


    /**
     * 批量新增图片
     *
     * @param fileCodeList
     * @param imageBizType
     * @param imageBizId
     */
    public void insertBatchImage(List<String> fileCodeList,
                                 @NotNull(message = "imageBizType不能为空") ImageBizType imageBizType,
                                 @NotNull(message = "imageBizId不能为空") Long imageBizId) {
        if (CollectionUtils.isEmpty(fileCodeList)) {
            return;
        }

        final List<ScmImagePo> scmImagePoList = fileCodeList.stream()
                .map(fileCode -> {
                    final ScmImagePo scmImagePo = new ScmImagePo();
                    scmImagePo.setFileCode(fileCode);
                    scmImagePo.setImageBizType(imageBizType);
                    scmImagePo.setImageBizId(imageBizId);
                    return scmImagePo;
                }).collect(Collectors.toList());

        scmImageDao.insertBatch(scmImagePoList);
    }

    /**
     * 批量删除图片
     *
     * @param fileCodeList
     * @param imageBizType
     * @param imageBizId
     */
    public void deleteBatchImage(@NotEmpty(message = "fileCodeList不能为空") List<String> fileCodeList,
                                 @NotNull(message = "imageBizType不能为空") ImageBizType imageBizType,
                                 @NotNull(message = "imageBizId不能为空") Long imageBizId) {
        final List<ScmImagePo> scmImagePoList = scmImageDao.getList(fileCodeList, imageBizType, imageBizId);
        Assert.equals(fileCodeList.size(), scmImagePoList.size(), () -> new BizException("图片查找不到，删除失败"));

        scmImageDao.removeBatchByIds(scmImagePoList);
    }


    /**
     * @param newFileCodeList
     * @param oldFileCodeList
     * @param imageBizType
     * @param imageBizId
     */
    public void editImage(List<String> newFileCodeList,
                          List<String> oldFileCodeList,
                          @NotNull(message = "imageBizType不能为空") ImageBizType imageBizType,
                          @NotNull(message = "imageBizId不能为空") Long imageBizId) {
        // 若新的图片列表为空，则删除该业务id及业务类型关联的所有图片
        if (CollectionUtils.isEmpty(newFileCodeList)) {
            this.removeAllImage(imageBizType, imageBizId);
            return;
        }

        final List<ScmImagePo> scmImagePoList = scmImageDao.getList(oldFileCodeList, imageBizType, imageBizId);
        Assert.equals(oldFileCodeList.size(), scmImagePoList.size(), () -> new BizException("图片查找不到，编辑失败"));

        List<String> backupList = new ArrayList<>(oldFileCodeList);

        // oldList 减去 newList 剩下的是删除的fileCodeList
        oldFileCodeList.removeAll(newFileCodeList);
        // newList 减去 oldList的备份 剩下的是新增的fileCodeList
        newFileCodeList.removeAll(backupList);

        scmImageDao.removeByFileCodeList(oldFileCodeList, imageBizType, imageBizId);
        this.insertBatchImage(newFileCodeList, imageBizType, imageBizId);
    }

    /**
     * 批量插入图片
     *
     * @param scmImageBoList
     * @param imageBizType
     */
    public void insertBatchImageBo(@Valid List<ScmImageBo> scmImageBoList,
                                   @NotNull(message = "imageBizType不能为空") ImageBizType imageBizType) {
        if (CollectionUtils.isEmpty(scmImageBoList)) {
            return;
        }

        List<ScmImagePo> poList = new ArrayList<>();
        for (ScmImageBo scmImageBo : scmImageBoList) {
            for (String fileCode : scmImageBo.getFileCodeList()) {
                final ScmImagePo scmImagePo = new ScmImagePo();
                scmImagePo.setImageBizId(scmImageBo.getImageBizId());
                scmImagePo.setImageBizType(imageBizType);
                scmImagePo.setFileCode(fileCode);
                poList.add(scmImagePo);
            }
        }

        scmImageDao.insertBatch(poList);
    }

    public void removeAllImage(@NotNull(message = "imageBizType不能为空") ImageBizType imageBizType,
                               @NotNull(message = "imageBizId不能为空") Long imageBizId) {
        scmImageDao.removeByTypeAndId(imageBizType, imageBizId);
    }

    public void removeAllImageList(@NotNull(message = "imageBizType不能为空") ImageBizType imageBizType,
                                   @NotEmpty(message = "imageBizId不能为空") List<Long> imageBizIdList) {
        scmImageDao.removeByTypeAndIdList(imageBizType, imageBizIdList);
    }

    /**
     * 根据业务id获取第一张fileCode的图片
     * key->id, value -> fileCode
     *
     * @param imageBizType
     * @param imageBizIdList
     * @return
     */
    public Map<Long, String> getOneFileCodeMapByIdAndType(@NotNull(message = "imageBizType不能为空") ImageBizType imageBizType,
                                                          @NotEmpty(message = "imageBizId不能为空") List<Long> imageBizIdList) {

        final List<ScmImagePo> scmImagePoList = scmImageDao.getListByIdAndType(imageBizType, imageBizIdList);

        return scmImagePoList.stream().collect(Collectors.groupingBy(ScmImagePo::getImageBizId, Collectors.collectingAndThen(Collectors.toList(), value -> value.get(0).getFileCode())));
    }

    public CommonPageResult.PageInfo<String> getFileCodeListByIdAndTypePage(Page<ScmImagePo> page, ImageBizType imageBizType,
                                                                            List<Long> imageBizIdList) {
        return scmImageDao.getListByIdAndTypePage(page, imageBizType, imageBizIdList);
    }
}
