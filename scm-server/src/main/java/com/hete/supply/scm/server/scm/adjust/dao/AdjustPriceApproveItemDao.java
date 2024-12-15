package com.hete.supply.scm.server.scm.adjust.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.adjust.entity.po.AdjustPriceApproveItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 调价审批明细表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-06-13
 */
@Component
@Validated
public class AdjustPriceApproveItemDao extends BaseDao<AdjustPriceApproveItemMapper, AdjustPriceApproveItemPo> {

    public List<AdjustPriceApproveItemPo> getListByNo(String adjustPriceApproveNo) {
        if (StringUtils.isBlank(adjustPriceApproveNo)) {
            return new ArrayList<>();
        }

        return baseMapper.selectList(Wrappers.<AdjustPriceApproveItemPo>lambdaQuery()
                .eq(AdjustPriceApproveItemPo::getAdjustPriceApproveNo, adjustPriceApproveNo));
    }
}
