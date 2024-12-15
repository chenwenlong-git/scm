package com.hete.supply.scm.server.scm.qc.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.vo.QcOrderDefectConfigExportVo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderDefectConfigPo;
import com.hete.support.api.entity.dto.ComPageDto;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * <p>
 * 质检次品配置 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-10-09
 */
@Component
@Validated
@Slf4j
public class QcOrderDefectConfigDao extends BaseDao<QcOrderDefectConfigMapper, QcOrderDefectConfigPo> {

    public List<QcOrderDefectConfigPo> getAll() {
        return baseMapper.selectList(null);
    }

    public QcOrderDefectConfigPo getOneByCode(String defectCode) {
        if (StringUtils.isBlank(defectCode)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<QcOrderDefectConfigPo>lambdaQuery()
                .eq(QcOrderDefectConfigPo::getDefectCode, defectCode));
    }

    public Integer getExportTotals(ComPageDto dto) {
        log.info("统计次品原因:{}", dto);
        return baseMapper.getExportTotals(dto);
    }

    public CommonPageResult.PageInfo<QcOrderDefectConfigExportVo> getExportList(Page<Void> page, ComPageDto dto) {
        log.info("列表次品原因:{}", page);
        IPage<QcOrderDefectConfigExportVo> pageResult = baseMapper.getExportList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }
}
