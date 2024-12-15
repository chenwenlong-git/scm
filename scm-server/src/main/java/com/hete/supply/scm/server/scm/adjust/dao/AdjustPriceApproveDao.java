package com.hete.supply.scm.server.scm.adjust.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.adjust.entity.dto.AdjustApproveSearchDto;
import com.hete.supply.scm.server.scm.adjust.entity.po.AdjustPriceApprovePo;
import com.hete.supply.scm.server.scm.adjust.entity.vo.AdjustApproveVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 * 调价审批表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-06-13
 */
@Component
@Validated
public class AdjustPriceApproveDao extends BaseDao<AdjustPriceApproveMapper, AdjustPriceApprovePo> {

    public CommonPageResult.PageInfo<AdjustApproveVo> searchAdjustApprove(Page<Void> page, AdjustApproveSearchDto dto) {
        final IPage<AdjustApproveVo> pageResult = baseMapper.searchAdjustApprove(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public AdjustPriceApprovePo getOneByNo(String adjustPriceApproveNo) {
        if (StringUtils.isBlank(adjustPriceApproveNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<AdjustPriceApprovePo>lambdaQuery()
                .eq(AdjustPriceApprovePo::getAdjustPriceApproveNo, adjustPriceApproveNo));
    }
}
