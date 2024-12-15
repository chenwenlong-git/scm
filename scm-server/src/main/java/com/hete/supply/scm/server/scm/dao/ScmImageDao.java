package com.hete.supply.scm.server.scm.dao;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.entity.po.ScmImagePo;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * scm图片表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-02
 */
@Component
@Validated
public class ScmImageDao extends BaseDao<ScmImageMapper, ScmImagePo> {

    /**
     * 根据业务ID和类型查询列表
     *
     * @author ChenWenLong
     * @date 2022/11/4 14:49
     */
    public List<ScmImagePo> getByImageBizIdAndType(Long imageBizId, ImageBizType imageBizType) {
        return list(Wrappers.<ScmImagePo>lambdaQuery().eq(ScmImagePo::getImageBizId, imageBizId)
                .eq(ScmImagePo::getImageBizType, imageBizType));
    }

    /**
     * 批量ID删除
     *
     * @author ChenWenLong
     * @date 2022/11/15 17:18
     */
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public List<ScmImagePo> getList(List<String> fileCodeList, @NotNull ImageBizType imageBizType, @NotNull Long imageBizId) {
        if (CollectionUtils.isEmpty(fileCodeList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<ScmImagePo>lambdaQuery()
                .in(ScmImagePo::getFileCode, fileCodeList)
                .eq(ScmImagePo::getImageBizType, imageBizType)
                .eq(ScmImagePo::getImageBizId, imageBizId));
    }

    public List<ScmImagePo> getListByIdAndType(@NotNull ImageBizType imageBizType, @NotEmpty List<Long> imageBizIdList) {
        return baseMapper.selectList(Wrappers.<ScmImagePo>lambdaQuery()
                .eq(ScmImagePo::getImageBizType, imageBizType)
                .in(ScmImagePo::getImageBizId, imageBizIdList));
    }

    public void removeByFileCodeList(List<String> oldFileCodeList, @NotNull ImageBizType imageBizType, @NotNull Long imageBizId) {
        if (CollectionUtils.isEmpty(oldFileCodeList)) {
            return;
        }
        baseMapper.delete(Wrappers.<ScmImagePo>lambdaUpdate()
                .in(ScmImagePo::getFileCode, oldFileCodeList)
                .eq(ScmImagePo::getImageBizType, imageBizType)
                .eq(ScmImagePo::getImageBizId, imageBizId));
    }

    public void removeByTypeAndId(@NotNull ImageBizType imageBizType, @NotNull Long imageBizId) {
        final ScmImagePo scmImagePo = new ScmImagePo();

        scmImagePo.setDelTimestamp(DateUtil.current());
        baseMapper.updateSkipCheck(scmImagePo, Wrappers.<ScmImagePo>lambdaUpdate()
                .eq(ScmImagePo::getImageBizType, imageBizType)
                .eq(ScmImagePo::getImageBizId, imageBizId));
    }

    public CommonPageResult.PageInfo<String> getListByIdAndTypePage(Page<ScmImagePo> page, ImageBizType imageBizType, List<Long> imageBizIdList) {
        final Page<ScmImagePo> pageResult = baseMapper.selectPage(page, Wrappers.<ScmImagePo>lambdaQuery()
                .eq(ScmImagePo::getImageBizType, imageBizType)
                .in(ScmImagePo::getImageBizId, imageBizIdList));

        final List<String> fileCodeList = Optional.ofNullable(pageResult.getRecords())
                .orElse(Collections.emptyList())
                .stream()
                .map(ScmImagePo::getFileCode)
                .collect(Collectors.toList());

        return PageInfoUtil.getPageInfo(pageResult, fileCodeList);
    }

    public void removeByTypeAndIdList(@NotNull ImageBizType imageBizType, @NotEmpty List<Long> imageBizIdList) {
        final ScmImagePo scmImagePo = new ScmImagePo();

        scmImagePo.setDelTimestamp(DateUtil.current());
        baseMapper.updateSkipCheck(scmImagePo, Wrappers.<ScmImagePo>lambdaUpdate()
                .eq(ScmImagePo::getImageBizType, imageBizType)
                .in(ScmImagePo::getImageBizId, imageBizIdList));
    }
}
